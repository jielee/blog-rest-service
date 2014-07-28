package org.blog.backend.ws;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.blog.backend.util.OAuthUtil;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeServlet;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@Path("login")
public class OAuthService extends AbstractAppEngineAuthorizationCodeServlet {

	private final UserService userService = UserServiceFactory.getUserService();
	private static final long serialVersionUID = -9120914618574873041L;
	public static final Logger LOGGER = Logger.getLogger(OAuthService.class.getName());

	
	@GET()
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@Context HttpServletRequest request, @Context HttpServletResponse response, @PathParam("state") String state) throws IOException, URISyntaxException, ServletException{
		User user  = UserServiceFactory.getUserService().getCurrentUser();
		String res = "";
		if(user!= null){
		Credential credential = OAuthUtil.getCredential(request);
		res = "com credenciais";
		if(credential == null){
			res = "sem crdenciais";
			}
		}
		res = "uiser is nulll";
		return Response.ok(res).build();
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		super.service(req, resp);
	}


	@Override
	protected AuthorizationCodeFlow initializeFlow() throws ServletException,IOException {
		return OAuthUtil.initializeFlow();
	}

	public UserService getUserService(){
		return this.userService;
	}

	@Override
	protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
		return OAuthUtil.getRedirectUri(req);
	}
	
	public String getUserIdentification(HttpServletRequest request) throws ServletException, IOException{
		return getUserId(request);
	}

}
