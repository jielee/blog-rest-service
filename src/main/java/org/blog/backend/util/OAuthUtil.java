package org.blog.backend.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.blog.backend.constants.App;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.datastore.AppEngineDataStoreFactory;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;
import com.google.api.services.plus.PlusScopes;
import com.google.appengine.api.users.UserServiceFactory;

public class OAuthUtil {

	private static Logger log = Logger.getLogger(OAuthUtil.class.getName());
	
	//blog-rest-service Application Secrets
	private static GoogleClientSecrets clientSecrets;
	
	//To get service credentials Just for apps in domain
	static final String SERVICE_EMAIL = "952973791007-790bvoc8ctei04133im0dbnah5op8r7g@developer.gserviceaccount.com";
	
	public static final HttpTransport HTTP_TRANSPORT = new UrlFetchTransport();
	public static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final AppEngineDataStoreFactory DATA_STORE_FACTORY = AppEngineDataStoreFactory.getDefaultInstance();
	

	/**
	 * Loads the client secrets from json in resource path
	 * @return
	 * @throws IOException
	 */
	public static GoogleClientSecrets getClientSecrets() throws IOException{
		if(clientSecrets == null){
			InputStreamReader inputReader = new InputStreamReader(OAuthUtil.class.getResourceAsStream("/client_secret.json"));
			clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, inputReader);
		}
		return clientSecrets;
	}

	/**
	 * Initializes the flow with default scopes
	 * @return
	 * @throws IOException
	 */
	public static GoogleAuthorizationCodeFlow initializeFlow() throws IOException{
		Set<String> permissions = getDriveScopes();
		return new GoogleAuthorizationCodeFlow.Builder(
				HTTP_TRANSPORT, JSON_FACTORY, getClientSecrets(), permissions)
				.setDataStoreFactory(DATA_STORE_FACTORY)
				.setAccessType("offline").build();
	}

	/**
	 * Return drive scopes
	 * @return
	 */
	private static Set<String> getDriveScopes() {
		Set<String> permissions = new HashSet<String>();
		permissions.add(DriveScopes.DRIVE_FILE);
		permissions.add(PlusScopes.PLUS_ME);
		permissions.add(PlusScopes.USERINFO_PROFILE);
		return permissions;
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public static String getRedirectUri(HttpServletRequest req){
		GenericUrl requestUrl = new GenericUrl(req.getRequestURL().toString());
	    requestUrl.setRawPath(App.Main.OAUTH_CALLBACK.value());
	    return requestUrl.build();
	}
	
	/**
	 * 
	 * @param credential
	 * @return
	 */
	public static Oauth2 getOAuth2(Credential credential){
		Oauth2 oauth2 = new Oauth2.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(
		          App.Main.NAME.value()).build();
		return oauth2;
	}
	
	/**
	 * Service Credential
	 * @param user
	 * @return
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	public static Credential getServiceCredential(String user) throws GeneralSecurityException, IOException{
		GoogleCredential credential = new GoogleCredential.Builder().setTransport(HTTP_TRANSPORT)
	            .setJsonFactory(JSON_FACTORY)
	            .setServiceAccountId(SERVICE_EMAIL)
	            .setServiceAccountScopes(getDriveScopes())
	            .setServiceAccountPrivateKeyFromP12File(new File("/key.p12"))
	            .setServiceAccountUser(user)
	            .build();
		return credential;
	}
	
	/**
	 * AppIdentity Credentials
	 * @return
	 */
	public static AppIdentityCredential getAppIdentityCredential(){
		return new AppIdentityCredential(getDriveScopes());
	}
	
	/**
	 * Get user info from token
	 * @param credential
	 * @throws IOException
	 */
	public static void tokenInfo(Credential credential) throws IOException {
	    //validate tokenInfo
		Tokeninfo tokeninfo = OAuthUtil.getOAuth2(credential).tokeninfo().execute();
		log.info("TOKENNNNN" + tokeninfo.toPrettyString());
	    if (!tokeninfo.getAudience().equals(OAuthUtil.getClientSecrets().getDetails().getClientId())) {
	      log.info("ERROR: audience does not match our client ID!");
	    }
	  }
	
	public static Credential getCredential(String userId) throws ServletException, IOException{
		AuthorizationCodeFlow authflow = initializeFlow();
		return  authflow.loadCredential(userId);
	}
	
	public static Credential getCredential(HttpServletRequest req) throws ServletException, IOException{
		AuthorizationCodeFlow authflow = initializeFlow();
		return  authflow.loadCredential(getUserId(req));
	}
	
	public static String getUserId(HttpServletRequest req) throws ServletException, IOException {
	    log.info(UserServiceFactory.getUserService().getCurrentUser().getUserId());
		return UserServiceFactory.getUserService().getCurrentUser().getUserId();
	  }
	
}
