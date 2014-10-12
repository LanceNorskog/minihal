package us.norskog.minihal;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Unpack Links/Link/Embedded annotation structure
 * into named objects.
 */

public class GetAnnos {
	public final static String REL = "rel";
	public final static String TITLE = "title";
	public final static String HREF = "href";
	
	private final List<LinkStore> links = new ArrayList<LinkStore>();
	private final EmbeddedStore embed;
	
	public GetAnnos(Annotation[] annos) {
		
		for(Annotation anno: annos) {
			
		}
		
		embed = new EmbeddedStore(); // links, 
	}
	
	
	
}

class LinkStore {
	private Map<String,String> parts = new HashMap<String, String>();
	public LinkStore(String rel, String title, String href) {
		parts.put(GetAnnos.REL, rel);
		parts.put(GetAnnos.TITLE, title);
		parts.put(GetAnnos.HREF, href);
	}
	
	public void addPart(String key, String value) {
		parts.put(key, value);
	}
	
	public Map<String,String> getParts() {
		return parts;
	}
}

class EmbeddedStore {
	
}