package us.norskog.minihal;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	static Set<ParsedLinkSet> annoSet = new HashSet<ParsedLinkSet>();
	static Map<ParsedLinkSet, Evaluator> evaluators = new HashMap<ParsedLinkSet, Evaluator>();;

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
			Map itemChunk = new HashMap();
			for(EmbeddedStore store: storeMap.values()) {
				List embeddedLinks = new ArrayList();
				if (store.getPath() != null) {
					Object items = evaluator.evaluateExpr(store.getPath());
					if (items.getClass().isArray()) {
						Object[] obs = (Object[]) items;
						if (obs.length > 0) {
							System.err.println("First item: " + obs[0].toString());
						}
						for(int i = 0; i < obs.length; i++) {
							List<Map<String, String>> links = evaluator.evaluateEmbeddedItem(store.getName(), response, obs[i]);
							embeddedLinks.add(links);
						}
					} else if (items instanceof Collection) {
						for(Object ob: (Collection) items) {
							List<Map<String, String>> links = evaluator.evaluateEmbeddedItem(store.getName(), response, ob);
							embeddedLinks.add(links);						
						}
					} else {
						System.err.println("Item not a list or array: " + items.toString());
						List<Map<String, String>> links = evaluator.evaluateEmbeddedItem(store.getName(), response, items);
						embeddedLinks.add(links);
					}
				}
				itemChunk.put(store.getName(), embeddedLinks);
			}
			response.put("_embedded", itemChunk);
		}
	}

	private boolean isEmpty(List links) {
		return links.size() > 0;
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
