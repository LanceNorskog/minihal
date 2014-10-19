package us.norskog.minihal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Store and evaluate link sets
 * 
 * @author lance
 *
 */

public class Evaluator {
	Executor executor = new Executor();

	private final ParsedLinkSet parsedLinkSet;
	private List<Map<String, List<Expression>>> links;
	private List<Map<String, List<Expression>>> embeddedLinks = null;

	Evaluator(ParsedLinkSet parsedLinkSet) {
		this.parsedLinkSet = parsedLinkSet;
		this.links = parse(parsedLinkSet.getLinks());
		if (parsedLinkSet.getEmbedded() != null)
			this.embeddedLinks = parse(parsedLinkSet.getEmbedded().getLinks());
	}

	public List<Map<String, List<Expression>>> parse(List<LinkStore> linkStore) {
		List<Map<String,List<Expression>>> parsed = new ArrayList<Map<String,List<Expression>>>();
		for(LinkStore store: linkStore) {
			Map<String, List<Expression>> link = new HashMap<String, List<Expression>>();
			for(String part: store.getParts().keySet()) {
				Parser p = new Parser(store.getParts().get(part));
				List<Expression> expressions = new ArrayList<Expression>();
				for(Expression e: p.getExpressions()) {
					expressions.add(e);
				}
				link.put(part, expressions);
			}
			parsed.add(link);
		}
		return parsed;
	}
	
	public List<Map<String, String>> evaluateLinks(Object response) {
		if (links == null)
			return null;
		return getLinks(response, null, links);
	}
	
	public List<Map<String, String>> evaluateEmbeddedLinks(Map response, Map item) {
		if (links == null || embeddedLinks == null)
			return null;
		return getLinks(response, item, embeddedLinks);
	}
	
	public List<Map<String, String>> getLinks(Object response, Object item, List<Map<String, List<Expression>>> linksType) {
		executor.setVar("response", response);
		if (item != null)
			executor.setVar("item", item);
		List<Map<String, String>> linkSet = new ArrayList();
		for(Map<String, List<Expression>> link: linksType) {
			Map<String, String> linkSetPart = new HashMap<String, String>();
			for(String linkParts: link.keySet()) {
				StringBuilder sb = new StringBuilder();
				for(Expression expression: link.get(linkParts)) {
					String value = expression.eval(executor).toString();	
					sb.append(value);
				}
				linkSetPart.put(linkParts, sb.toString());
			}
			linkSet.add(linkSetPart);
		}
		
		return linkSet;

	}

	public List<Map<String, List<Expression>>> getLinks() {
		return links;
	}

	public List<Map<String, List<Expression>>> getEmbeddedLinks() {
		return embeddedLinks;
	}

}
