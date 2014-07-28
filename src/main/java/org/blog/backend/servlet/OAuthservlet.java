package org.blog.backend.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.blog.backend.util.OAuthUtil;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.auth.oauth2.AbstractAppEngineAuthorizationCodeServlet;
import com.google.api.services.oauth2.model.Tokeninfo;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Servlet implementation class OAuthservlet
 */
public class OAuthservlet extends AbstractAppEngineAuthorizationCodeServlet {
	private static final long serialVersionUID = 1L;
	private final UserService userService = UserServiceFactory.getUserService();
	
	public static final Logger LOGGER = Logger.getLogger(OAuthservlet.class.getName());

	public OAuthservlet() {
		super();
	}
	
	public Credential getCredential(String userId) throws ServletException, IOException{
		AuthorizationCodeFlow authflow = initializeFlow();
		return  authflow.loadCredential(userId);
	}
	
	public String getUserIdentification(HttpServletRequest request) throws ServletException, IOException{
		return getUserId(request);
	}
	
	 @Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		super.service(req, resp);
	}

	@Override
	public String getRedirectUri(HttpServletRequest req)
			throws ServletException, IOException {
		return OAuthUtil.getRedirectUri(req);
	}

	@Override
	public AuthorizationCodeFlow initializeFlow() throws ServletException,
	IOException {
		return OAuthUtil.initializeFlow();
	}
	
	public UserService getUserService(){
		return this.userService;
	}
	
	private static void tokenInfo(Credential credential) throws IOException {
	    //validate tokenInfo
		Tokeninfo tokeninfo = OAuthUtil.getOAuth2(credential).tokeninfo().execute();
		LOGGER.info("TOKENNNNN" + tokeninfo.toPrettyString());
	    if (!tokeninfo.getAudience().equals(OAuthUtil.getClientSecrets().getDetails().getClientId())) {
	      LOGGER.info("ERROR: audience does not match our client ID!");
	    }
	  }

}
