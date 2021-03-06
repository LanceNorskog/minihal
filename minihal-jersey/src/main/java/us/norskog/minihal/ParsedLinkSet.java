package us.norskog.minihal;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 * Unpack Links/Link/Embedded annotation structure
 * into named objects.
 */

public class ParsedLinkSet {
	public final static String REL = "rel";
	public final static String TITLE = "title";
	public final static String HREF = "href";
	private final static Map<Annotation[], ParsedLinkSet> parsedLinks = new ConcurrentHashMap<Annotation[], ParsedLinkSet>();

	private Annotation[] annos;
	private List<LinkStore> links;
	private Map<String,EmbeddedStore> embeddedMap = null;

	public ParsedLinkSet(Annotation[] annos) {
		this.annos = annos;
		for(Annotation anno: annos) {
			if (anno.annotationType().equals(Links.class)) {
				Links linksAnno = (Links) anno;
				LinkSet linkset = linksAnno.linkset();
				links = new ArrayList<LinkStore>();
				storeLinks(linkset, links);
				Embedded[] embeddedAnno = linksAnno.embedded();
				if (embeddedAnno.length > 0) {
					embeddedMap = new LinkedHashMap<String, EmbeddedStore>();
					for(int i = 0; i < embeddedAnno.length; i++) {
						Embedded embedded = embeddedAnno[i];
						storeEmbedded(embedded);
					}
				} 
				this.hashCode();
				break;
			}
		}
	}

	private void storeEmbedded(Embedded embedded) {
		List<LinkStore> embeddedLinks = new ArrayList<LinkStore>();
		storeLinks(embedded.links(), embeddedLinks);
		embeddedMap.put(embedded.name(), new EmbeddedStore(embedded.name(), embedded.path(), embeddedLinks));
	}

	private void storeLinks(LinkSet linkset, List<LinkStore> links) {
		for(Link link: linkset.links()) {
			LinkStore store = new LinkStore(link.rel(), link.title(), link.href());
			if (link.more().length > 0) {
				String[] more = link.more();
				if (more.length % 2 == 1)
					throw new IllegalArgumentException();
				for(int i = 0; i < more.length; i+=2) {
					String key = more[i];
					String value = more[i + 1];
					store.addPart(key, value);
				}
			}
			links.add(store);
		}
	}

	public List<LinkStore> getLinks() {
		return links;
	}

	public Map<String,EmbeddedStore> getEmbeddedMap() {
		return embeddedMap;
	}

	@Override
	public boolean equals(Object obj) {
		if (! (obj instanceof ParsedLinkSet))
			return false;
		return Arrays.equals(annos, ((ParsedLinkSet) obj).annos);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(annos);
	}

	public static ParsedLinkSet getParsedLinkSet(Annotation[] annos) {
		if (parsedLinks.containsKey(annos)) {
			return parsedLinks.get(annos);
		}
		if (hasLinks(annos)) {
			ParsedLinkSet ga = new ParsedLinkSet(annos);
			parsedLinks.put(annos, ga);
		} else {
			parsedLinks.put(annos, null);
		}
		return parsedLinks.get(annos);
	}

	private static boolean hasLinks(Annotation[] annos) {
		for(Annotation anno: annos) {
			if (anno.annotationType().equals(Links.class)) {
				return true;
			}
		}
		return false;
	}

}

class LinkStore {
	private Map<String,String> parts = new LinkedHashMap<String, String>();

	public LinkStore(String rel, String title, String href) {
		parts.put(ParsedLinkSet.REL, rel);
		parts.put(ParsedLinkSet.TITLE, title);
		parts.put(ParsedLinkSet.HREF, href);
	}

	public void addPart(String key, String value) {
		parts.put(key, value);
	}

	public Map<String,String> getParts() {
		return parts;
	}
}

class EmbeddedStore {
	private final String name;
	private final String path;
	private final List<LinkStore> links;

	public EmbeddedStore(String name, String path, List<LinkStore> links) {
		this.name = name;
		this.path = path;
		this.links = links;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public List<LinkStore> getLinks() {
		return links;
	}

}