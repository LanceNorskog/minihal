package us.norskog.minihal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MakeLinks {
	ELBase base = new ELBase();

	private final GetAnnos getAnnos;
	private List<Map<String, List<Expression>>> links;
	private List<Map<String, List<Expression>>> embeddedLinks;

	MakeLinks(GetAnnos getAnnos) {
		this.getAnnos = getAnnos;
		base.setTypes(Map.class);
		base.setItemType(Map.class);
		this.links = parse(getAnnos.getLinks());
		this.embeddedLinks = parse(getAnnos.getEmbedded().getLinks());
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
	
	public List<Map<String, Expression>> evaluateLinks(Map response) {
		return getLinks(response, null, links);
	}
	
	public List<Map<String, Expression>> evaluateEmbeddedLinks(Map response, Map item) {
		return getLinks(response, item, links);
	}
	
	public List<Map<String, Expression>> getLinks(Map response, Map item, List<Map<String, List<Expression>>> linksType) {
		base.setVars(response, item);
		List<Map<String, Expression>> linkSet = new ArrayList();
		for(Map<String, List<Expression>> link: linksType) {
			Map<String,String> linkev = new HashMap<String,String>();
			for(String linkParts: link.keySet()) {
				StringBuilder sb = new StringBuilder();
				for(Expression expression: link.get(linkParts)) {
					String value = expression.eval(response, item).toString();	
					sb.append(value);
				}
			}
		}
		
		return linkSet;

	}

}
