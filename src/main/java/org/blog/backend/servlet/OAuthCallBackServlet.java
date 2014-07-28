package org.blog.backend.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.blog.backend.util.OAuthUtil;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeCallbackServlet;
import com.google.appengine.api.users.UserServiceFactory;

public class OAuthCallBackServlet extends AbstractAppEngineAuthorizationCodeCallbackServlet {
	private static final long serialVersionUID = 1L;
	public static final Logger LOGGER = Logger.getLogger(OAuthCallBackServlet.class.getName());  
    public OAuthCallBackServlet() {
        super();
    }
    
    @Override
    protected void onSuccess(HttpServletRequest req, HttpServletResponse resp,
    		Credential credential) throws ServletException, IOException {
    	resp.sendRedirect("/s/admin/");
    }
    
    @Override
    protected void onError(
        HttpServletRequest req, HttpServletResponse resp, AuthorizationCodeResponseUrl errorResponse)
        throws ServletException, IOException {
      String nickname = UserServiceFactory.getUserService().getCurrentUser().getNickname();
      resp.setStatus(200);
      resp.addHeader("Content-Type", "text/html");
      resp.getWriter().print("<h3>" + nickname + ", did not authorize </h3>");
      return;
    }

	@Override
	protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
		return OAuthUtil.getRedirectUri(req);
	}

	@Override
	protected AuthorizationCodeFlow initializeFlow() throws ServletException, IOException {
		return OAuthUtil.initializeFlow();
	}

}
