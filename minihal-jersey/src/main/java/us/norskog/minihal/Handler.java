package us.norskog.minihal;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/*
 * Convert arbitrary object into map.
 * Uses Apache BeanUtils. Could hacked to simple reflection to avoid a dependency.
 */

public class Handler {
	private static final Map<String,Object> mapclass = new HashMap<String, Object>();

	public Map<String, Object> convertToMap(Object obj) {	
		Map<String, Object> objectAsMap;
		long start = System.currentTimeMillis();
		ObjectMapper mapper = new ObjectMapper();
		if (System.currentTimeMillis() - start > 0)
			this.hashCode();
		try {
			byte[] b;
			b = mapper.writeValueAsBytes(obj);
			objectAsMap = mapper.readValue(b, mapclass.getClass());
			return objectAsMap;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


	public Object[] convertToArray(Object[] objArray)  {	
		Object[] asArray = new Object[objArray.length];
		for(int i = 0; i < objArray.length; i++) {
			asArray[i] = convertToMap(objArray[i]);
		}
		return asArray;
	}

	//	public Map getMap(Object obj) {
	//
	//		ObjectMapper om = new ObjectMapper();
	//		Map<String, Object> objectAsMap = om.convertValue(obj, Map.class);
	//		System.err.println("map: " + objectAsMap.toString());
	//		return objectAsMap;
	//	}

	//	public Object convert_object(Object obj) throws IntrospectionException,
	//	IllegalAccessException, IllegalArgumentException,
	//	InvocationTargetException {
	//		if (obj instanceof String || obj instanceof Number)
	//			return obj;
	//		else if (obj.getClass().isArray()) {
	//			Object[] value = new Object
	//		}
	//			
	//	}
	//	
	//	public Object convert_object_to_map_java(Object obj) throws IntrospectionException,
	//	IllegalAccessException, IllegalArgumentException,
	//	InvocationTargetException {
	//		if (obj instanceof String || obj instanceof Number)
	//			return obj;
	//		else if (obj.getClass().isArray()) {
	//			
	//		}
	//			
	//		Map<String, Object> objectAsMap = new HashMap<String, Object>();
	//		BeanInfo info = Introspector.getBeanInfo(obj.getClass());
	//		for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
	//			Method reader = pd.getReadMethod();
	//			if (reader != null && reader.getName().startsWith("get") || reader.getName().startsWith("is"))
	//			{
	//				objectAsMap.put(pd.getName(), (reader.invoke(obj)));
	//			}
	//		}
	//		return objectAsMap;				
	//	}
	//	
	//	public Object convert_object_to_map_java(Object obj) throws IntrospectionException,
	//	IllegalAccessException, IllegalArgumentException,
	//	InvocationTargetException {
	//		if (obj instanceof String || obj instanceof Number)
	//			return obj;
	//		else if (obj.getClass().isArray()) {
	//			
	//		}
	//			
	//		Map<String, Object> objectAsMap = new HashMap<String, Object>();
	//		BeanInfo info = Introspector.getBeanInfo(obj.getClass());
	//		for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
	//			Method reader = pd.getReadMethod();
	//			if (reader != null && reader.getName().startsWith("get") || reader.getName().startsWith("is"))
	//			{
	//				objectAsMap.put(pd.getName(), (reader.invoke(obj)));
	//			}
	//		}
	//		return objectAsMap;				
	//	}

}