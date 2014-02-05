package org.blog.backend.ws;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("hello")
public class BaseService {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response helloWorld(){
		return Response.ok("<b>Hello World</b>").build();
	}
}
