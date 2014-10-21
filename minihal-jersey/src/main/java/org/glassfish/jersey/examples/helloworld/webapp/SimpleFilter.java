package org.glassfish.jersey.examples.helloworld.webapp;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import us.norskog.minihal.Link;
import us.norskog.minihal.LinkSet;
import us.norskog.minihal.Links;

//@Provider
//@Links(linkset = @LinkSet(links = { @Link(href = "", rel = "") }))
public class SimpleFilter implements ContainerRequestFilter, ContainerResponseFilter {
	static ThreadLocal<URI> baseURIs = new ThreadLocal<URI>(); 
	
	public void filter(ContainerRequestContext requestContext)
			throws IOException {
		URI baseUri = requestContext.getUriInfo().getBaseUri();
		System.out.println("Request base URI: " + baseUri);
		System.out.println("\tThread: " + Thread.currentThread().toString());
		baseURIs.set(baseUri);
	}

	public void filter(ContainerRequestContext requestContext,
			ContainerResponseContext responseContext) throws IOException 
	{
		System.out.println("Response Base URI: " + baseURIs.get());
		System.out.println("\tThread: " + Thread.currentThread().toString());

	}

}
