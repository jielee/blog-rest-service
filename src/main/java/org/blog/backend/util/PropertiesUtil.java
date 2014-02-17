package org.blog.backend.util;

import org.blog.backend.controller.PropertiesController;

public class PropertiesUtil {
	
	public PropertiesUtil(){}
		
	public static String getString(String key) {
		return PropertiesController.getValue(key);
	}

}
