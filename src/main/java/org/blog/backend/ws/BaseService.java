package org.blog.backend.ws;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.blog.backend.controller.PropertiesController;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@Path("hello")
public class BaseService {

	private static UserService userService = UserServiceFactory.getUserService();

	public BaseService(){}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response helloWorld(@Context HttpServletRequest req){
		String response;
		String email = "";

		String login = "";

		String logout = "";
		String admin = "";
		String thisURL = req.getRequestURI();

		if (req.getUserPrincipal() != null) {
			response = "<p>Hello, " +
					req.getUserPrincipal().getName() +
					"!  You can <a href=\"" +
					userService.createLogoutURL(thisURL) +
					"\">sign out</a>.</p>";
			email = "  " +userService.getCurrentUser();

			login = "  " +userService.createLoginURL(thisURL);

			logout = "  "+userService.createLogoutURL(thisURL);
			
			if(userService.isUserAdmin()){
				admin = "I AM ADMINNNNN";
			}

		} else {
			response="<p>Please <a href=\"" +
					userService.createLoginURL(thisURL) +
					"\">sign in</a>.</p>";
		}
		String logged ="";
		if(userService.isUserLoggedIn()){
			logged ="I AMMMM LOGGEDDDDDDDDDD ";
		} 
		
		return Response.ok("<b>Hello World </b>" + PropertiesController.getValue("app.name") +" " +logged+ admin + email +login+logout + " " + response ).build();
	}

	public UserService getUserService() {
		return userService;
	}
}
