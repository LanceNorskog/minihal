/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.jersey.examples.helloworld.webapp;

import us.norskog.minihal.Embedded;
import us.norskog.minihal.Link;
import us.norskog.minihal.LinkSet;
import us.norskog.minihal.Links;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author Pavel Bucek (pavel.bucek at oracle.com)
 */
@Path("helloworld")
public class HelloWorldResource {

	@GET
	@Path("string")
	@Produces("text/plain")
	public String getString() {
		return "[]";
	}

	@GET
	@Path("value")
	@Links(linkset = @LinkSet(links = {
			@Link(rel = "self", href = "/helloworld/value", title = "Self"),
			@Link(rel = "first", href = "/helloworld/value?id=${response.first}", title = "First") }), 
			embedded = {
		@Embedded(name = "Constance", path = "hello", links = @LinkSet(links = { @Link(rel = "only", href = "/helloworld/value?id=${item.value}", title = "id ${item.key}") })),
		@Embedded(name = "Nullz", path = "${x}", links = @LinkSet(links = { @Link(rel = "only", href = "/helloworld/value?id=${item.value}", title = "id ${item.key}") })),
		@Embedded(name = "Objectificicated", path = "${response.first}", links = @LinkSet(links = { @Link(rel = "only", href = "/helloworld/value?id=${item.value}", title = "id ${item.key}") })),
		@Embedded(name = "Arraysious", path = "${response.array}", links = @LinkSet(links = { @Link(rel = "only", href = "/helloworld/value?id=${item.value}", title = "id ${item.key}") })),
		@Embedded(name = "Listicle", path = "${response.list}", links = @LinkSet(links = { @Link(rel = "only", href = "/helloworld/value?id=${item.value}", title = "id ${item.key}") })),
		@Embedded(name = "Mappacious", path = "${response.map}", links = @LinkSet(links = { @Link(rel = "only", href = "/helloworld/value?id=${item.value}", title = "id ${item.key}") })) })

	@Produces({"application/hal+json",MediaType.APPLICATION_JSON})
	public Value getHello() {
		return new Value();
	}

	@GET
	@Path("array")
	@Produces(MediaType.APPLICATION_JSON)
	public String getArray() {
		return "[]";
	}

}

