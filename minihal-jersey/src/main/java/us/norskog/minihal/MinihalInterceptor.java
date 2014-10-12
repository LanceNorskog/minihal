package us.norskog.minihal;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.util.BeanUtil;

import java.beans.BeanInfo;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lance on 10/9/14.
 */

@Provider
@Links
public class MinihalInterceptor implements WriterInterceptor {
	public static final String HAL = "application/hal+json";
	
	Handler handler = new Handler();

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
