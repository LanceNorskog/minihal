package us.norskog.minihal;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Jersey Interceptor for Minihal links unpacker
 * For all requests for hal+json, add hyperlinks defined
 * by @Links annotation on endpoint.
 * Hyperlinks are defined with text titles, urls etc. which
 * include substitution strings which are filled in by Java EL
 * language.
 */

@Provider
@Links(linkset = @LinkSet(links = { @Link(href = "", rel = "") }))
public class MinihalInterceptor implements WriterInterceptor {
	public static final String HAL = "application/hal+json";

	static Mapify mapifier = new Mapify();
	static Map<ParsedLinkSet, Evaluator> evaluators = new LinkedHashMap<ParsedLinkSet, Evaluator>();;

	public MinihalInterceptor() {
		System.err.println("MinihalInterceptor created");
	}

	//   @Override
	public void aroundWriteTo(WriterInterceptorContext context)
			throws IOException, WebApplicationException {
		System.err.println("MinihalInterceptor called");
		Object entity = context.getEntity();
		if (! context.getMediaType().toString().equals(HAL) || entity == null) {
			context.proceed();
			return;
		}
		ParsedLinkSet parsedLinkSet = ParsedLinkSet.getParsedLinkSet(context.getAnnotations());
		if (parsedLinkSet == null) {
			context.proceed();
			return;
		}

		Map<String, Object> response = mapifier.convertToMap(entity);
		evaluate(parsedLinkSet, response);
		System.err.println("map: " + response.toString());
		context.setEntity(response);
		context.proceed();
	}

	private void evaluate(ParsedLinkSet parsedLinkSet,
			Map<String, Object> response) {
		Evaluator evaluator = init(parsedLinkSet);
		List<Map<String, String>> expanded = evaluator.evaluateLinks(response);
		if (expanded != null) {
			response.put("_links", expanded);
			Map<String, EmbeddedStore> storeMap = parsedLinkSet.getEmbeddedMap();
			Map<String, List<List<Map<String, String>>>> itemChunk = new LinkedHashMap<String, List<List<Map<String, String>>>>();
			for(String name: storeMap.keySet()) {
				EmbeddedStore store = storeMap.get(name);
				List<List<Map<String, String>>> embeddedLinks = new ArrayList<List<Map<String, String>>>();
				itemChunk.put(store.getName(), embeddedLinks);
				if (store.getPath() != null) {
					Object items = evaluator.evaluateExpr(store.getPath());
					if (items == null)
						continue;
					if (items.getClass().isArray()) {
						Object[] obs = (Object[]) items;
						if (obs.length > 0) {
							System.err.println("First item: " + obs[0].toString());
						}
						for(int i = 0; i < obs.length; i++) {
							KV kv = new KV(Integer.toString(i), obs[i]);
							List<Map<String, String>> links = evaluator.evaluateEmbeddedItem(store.getName(), response, kv);
							embeddedLinks.add(links);
						}
					} else if (items instanceof Map) {
						for(Object key: ((Map<String,Object>) items).keySet()) {
							KV kv = new KV(key.toString(), ((Map<?, ?>) items).get(key.toString()));
							List<Map<String, String>> links = evaluator.evaluateEmbeddedItem(store.getName(), response, kv);
							embeddedLinks.add(links);						
						}
					} else if (items instanceof Collection) {
						int i = 0;
						for(Object ob: (Collection<?>) items) {
							KV kv = new KV(Integer.toString(i), ob);
							List<Map<String, String>> links = evaluator.evaluateEmbeddedItem(store.getName(), response, kv);
							embeddedLinks.add(links);		
							i++;
						}
					} else {
						System.err.println("Item not a list or array: " + items.toString());
						KV kv = new KV("0", items);
						List<Map<String, String>> links = evaluator.evaluateEmbeddedItem(store.getName(), response, kv);
						embeddedLinks.add(links);
					}
				}
			}
			response.put("_embedded", itemChunk);
		}
	}

	private Evaluator init(ParsedLinkSet parsedLinkSet) {
		if (! evaluators.containsKey(parsedLinkSet)) {
			// idempotent race condition v.s. synchronized in every call 
			synchronized(MinihalInterceptor.class) {
				Evaluator evaluator = new Evaluator(parsedLinkSet);
				evaluators.put(parsedLinkSet, evaluator);
			}
		}
		return evaluators.get(parsedLinkSet);
	}

	void dumpContext(WriterInterceptorContext context) {
		System.out.println("getType: " + context.getType().getClass());
		System.out.println("getEntity: " + context.getEntity().getClass());
		System.out.println("getGenericType: " + context.getGenericType().getTypeName());
		System.out.println("getMediaType: " + context.getMediaType().toString());
		Annotation[] annos = context.getAnnotations();
		for(Annotation anno: annos) {
			System.out.println("\tanno: " + anno.toString());
		}
	}

}
