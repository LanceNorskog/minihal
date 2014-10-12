package us.norskog.minihal;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;


/*
 * Convert arbitrary object into map.
 * Uses Apache BeanUtils. Could hacked to simple reflection to avoid a dependency.
 */

public class Handler {

	public Object convertToMap(Object obj) {	
		Map<String, String> objectAsMap;
		try {
			objectAsMap = BeanUtils.describe(obj);
			objectAsMap.remove("class");
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(e);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException(e);
		}
		return objectAsMap;
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