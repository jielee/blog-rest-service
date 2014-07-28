package org.blog.backend.ws;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.blog.backend.controller.PropertiesController;


@Path("/admin")
public class AdminService {
	
	@Path("/create")
	@POST
	@Produces("application/json;charset=UTF-8")
	public Response createBlogPost(@FormParam("title") String title, @FormParam("content") String content ){
		
		return Response.ok(title + " " + content).build();
	}
	
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response helloWorld(@Context HttpServletRequest req){
		
		return Response.ok("<b>Hello World </b>" + PropertiesController.getValue("app.name")).build();
	}

}
