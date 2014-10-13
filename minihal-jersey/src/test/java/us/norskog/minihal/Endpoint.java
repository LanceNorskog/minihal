package us.norskog.minihal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/*
 * Endpoint test for links
 */

public class Endpoint {

	@Links(linkset = @LinkSet(
			links = {
					@Link(rel = "self", href = "/", title = "Self") }
			)
			)
	public String getLinks() {
		return "[]";
	}

	@Links(linkset = @LinkSet(
			links = {
					@Link(rel = "self", href = "/", title = "Self") }
			),
			embedded = @Embedded(path = "thing.thing2", links = 
			@LinkSet(
					links = {
							@Link(rel = "self", href = "/", title = "Self") }
					)
					)
			)
	public String getAll() {
		return "[]";
	}

}
