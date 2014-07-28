package org.blog.backend.ws;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;

import org.blog.backend.util.OAuthUtil;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeCallbackServlet;
import com.google.appengine.api.users.UserServiceFactory;

@Path("oauth2callback")
public class OAuthCallbackService extends
AbstractAppEngineAuthorizationCodeCallbackServlet {
	private static final long serialVersionUID = -8274869572832578319L;

	@Override
	protected AuthorizationCodeFlow initializeFlow() throws ServletException,
	IOException {
		return OAuthUtil.initializeFlow();
	}
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.service(req, resp);
	}

	@Override
	protected void onError(HttpServletRequest req, HttpServletResponse resp,
			AuthorizationCodeResponseUrl errorResponse)
					throws ServletException, IOException {
		String nickname = UserServiceFactory.getUserService().getCurrentUser().getNickname();
		resp.setStatus(200);
		resp.addHeader("Content-Type", "text/html");
		resp.getWriter().print("<h3>" + nickname + ", did not authorize </h3>");
		return;
	}

	@Override
	protected void onSuccess(HttpServletRequest req, HttpServletResponse resp,
			Credential credential) throws ServletException, IOException {
		resp.sendRedirect("/s/hello");
	}

	@Override
	protected String getRedirectUri(HttpServletRequest req)
			throws ServletException, IOException {
		return OAuthUtil.getRedirectUri(req);
	}

}
