package us.norskog.minihal;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.Test;

public class GetAnnosTest {

	@Test
	public void test() throws NoSuchMethodException, SecurityException {
		Endpoint e = new Endpoint();
		Method getLinks = e.getClass().getMethod("getLinks", new Class[0]);
		Method getAll = e.getClass().getMethod("getAll", new Class[0]);
		Annotation[] linksAnnos = getLinks.getAnnotations();
		Annotation[] allAnnos = getAll.getAnnotations();
		GetAnnos getAnnosLinks = new GetAnnos(linksAnnos);
		GetAnnos getAnnosAll = new GetAnnos(allAnnos);
	}

}
