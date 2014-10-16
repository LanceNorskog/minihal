package us.norskog.minihal;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import java.io.IOException;
import java.lang.annotation.Annotation;

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
	
	static Handler handler = new Handler();
	static GetAnnos getAnnos;
	static MakeLinks makeLinks;

	public MinihalInterceptor() {
		System.err.println("MinihalInterceptor created");
	}

	//   @Override
	public void aroundWriteTo(WriterInterceptorContext context)
			throws IOException, WebApplicationException {
		System.err.println("MinihalInterceptor called");
		if (! context.getMediaType().toString().equals(HAL)) {
			context.proceed();
			return;
		}
		dumpContext(context);
		Object ob = context.getEntity();
		if (ob == null)
			System.err.println("\tEntity object is null!");
		else
			System.err.println("\tEntity type: " + ob.getClass().getCanonicalName().toString());
		Handler h = new Handler();
		Object objectAsMap = h.convertToMap(context.getEntity());
		if (getAnnos == null) {
			getAnnos = new GetAnnos(context.getAnnotations());
		}
		
		System.err.println("map: " + objectAsMap.toString());
	    context.setEntity(objectAsMap);
		context.proceed();
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
