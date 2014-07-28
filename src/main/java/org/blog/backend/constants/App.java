package org.blog.backend.constants;

import org.blog.backend.controller.PropertiesController;


public class App {

	
	public enum Main {
		NAME ("appId", PropertiesController.getValue("app.name")), 
		OAUTH ("appDescription", PropertiesController.getValue("app.oauth")), 
		OAUTH_CALLBACK ("activityId", PropertiesController.getValue("app.oauthcallback"));
		private String fieldName;
		private String fieldValue;
		
		private Main(String name, String fieldValue){
			this.fieldName = name;
			this.fieldValue = fieldValue;
		}
		
		public String field(){
			return this.fieldName;
		}
		
		public String value(){
			return this.fieldValue;
		}
		
	}	
	
}
