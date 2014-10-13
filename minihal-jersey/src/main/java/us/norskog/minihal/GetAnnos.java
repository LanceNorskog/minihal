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

	private Annotation anno = null;
	private List<LinkStore> links = new ArrayList<LinkStore>();
	private EmbeddedStore embedded = null;

	public GetAnnos(Annotation[] annos) {

		for(Annotation anno: annos) {
			if (anno.annotationType().equals(Links.class)) {
				this.anno = anno;
				Links linksAnno = (Links) anno;
				LinkSet linkset = linksAnno.linkset();
				links = new ArrayList<LinkStore>();
				storeLinks(linkset, links);
				if (linksAnno.embedded().length == 1) {
					Embedded embedded = (Embedded) linksAnno.embedded()[0];
					storeEmbedded(embedded);
				} else if (linksAnno.embedded().length > 1)
						throw new IllegalArgumentException();
				this.hashCode();
				break;
			}
		}
	}

	private void storeEmbedded(Embedded embedded) {
		List<LinkStore> embeddedLinks = new ArrayList<LinkStore>();
		storeLinks(embedded.links(), embeddedLinks);
		this.embedded = new EmbeddedStore(embedded.path(), embeddedLinks);
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

	public EmbeddedStore getEmbedded() {
		return embedded;
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
	String path;

	List<LinkStore> links;

	public EmbeddedStore(String path, List<LinkStore> links) {
		this.path = path;
		this.links = links;
	}

	public String getPath() {
		return path;
	}

	public List<LinkStore> getLinks() {
		return links;
	}
}