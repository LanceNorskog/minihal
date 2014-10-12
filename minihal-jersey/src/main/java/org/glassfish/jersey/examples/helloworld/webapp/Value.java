package org.glassfish.jersey.examples.helloworld.webapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class Value {
	String first = "one";
	String second = "two";
	List<String> list = new ArrayList<String>();
	Map<String, Integer> map = new HashMap<String, Integer>();

	public Value() {
		list.add("ten");
		list.add("eleven");
		map.put("100", 100);
		map.put("101", 101);
	}
	
	public List<String> getList() { return list; }
	public Map<String, Integer> getMap() {return map;}
}
