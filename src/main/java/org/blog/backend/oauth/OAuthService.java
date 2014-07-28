package org.blog.backend.oauth;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.blog.backend.util.OAuthUtil;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.appengine.api.users.UserServiceFactory;

@Path("oauth2")
public class OAuthService {

	private AuthorizationCodeFlow flow;
	private final Lock lockAuth = new ReentrantLock();
	private final Lock lockToken = new ReentrantLock();
	private Credential credential;

	@GET @Produces(MediaType.APPLICATION_JSON)
	public Response authorize(@Context HttpServletRequest request) throws ServletException, IOException{

		credential = null;
		lockAuth.lock();
		try{
			String userId = getUserId(request);
			if(flow == null){
				flow = initializeFlow();
			}
			if(userId != null){
				credential = flow.loadCredential(userId);
			}
			if(credential != null && credential.getAccessToken()!=null){
				return Response.ok("Authorized").build();
			}

			AuthorizationCodeRequestUrl authUrl = flow.newAuthorizationUrl();
			authUrl.setRedirectUri("/auth/oauth2/callback");
			return onAuthorization(request, authUrl);

		} finally{

			lockAuth.unlock();
		}
	}

	private String getUserId(HttpServletRequest req) throws ServletException, IOException {
		String response = null;

		if(UserServiceFactory.getUserService().getCurrentUser()!= null){
			response = UserServiceFactory.getUserService().getCurrentUser().getUserId();
		}

		return response;
	}

	public AuthorizationCodeFlow initializeFlow() throws ServletException,
	IOException {
		return OAuthUtil.initializeFlow();
	}

	private Response onAuthorization(HttpServletRequest req, AuthorizationCodeRequestUrl authorizationUrl) throws ServletException, IOException {
		return Response.seeOther(authorizationUrl.toURI()).build();
	}


	@GET @Produces(MediaType.APPLICATION_JSON)
	@Path("callback")
	public Response exchangeCodeForToken(@Context HttpServletRequest request) throws IOException, ServletException{
		Response response; 
		StringBuffer buf = request.getRequestURL();

		if (request.getQueryString() != null) {
			buf.append('?').append(request.getQueryString());
		}

		AuthorizationCodeResponseUrl responseUrl = new AuthorizationCodeResponseUrl(buf.toString());
		String code = responseUrl.getCode();

		if (responseUrl.getError() != null) {
			response = Response.ok(responseUrl.getError()).status(HttpServletResponse.SC_UNAUTHORIZED).build();
		} else if (code == null) {
			response = Response.ok("Missing code").status(HttpServletResponse.SC_BAD_REQUEST).build();
		} else {

			lockToken.lock();
			try {
				if (flow == null) {
					flow = initializeFlow();
				}
				TokenResponse tokenResponse = flow.newTokenRequest(code).setRedirectUri("/auth/oauth").execute();
				String userId = getUserId(request);
				credential = flow.createAndStoreCredential(tokenResponse, userId);
				response = Response.ok("Authorized").build();

			} finally {
				lockToken.unlock();
			}
		}

		return response;
	}

}
