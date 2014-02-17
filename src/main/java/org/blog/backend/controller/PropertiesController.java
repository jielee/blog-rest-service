package org.blog.backend.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PropertiesController {

	private static List<Properties> properties;
	private static final Logger log = Logger.getLogger(PropertiesController.class.getName());
	
	public static String getValue(String key){

		Map<String, String> properties = loadproperties();
		if ((properties != null) && (properties.containsKey(key))) {
			return (String)properties.get(key);
		}
		
		return null;
	}
	
	private static Map<String, String> loadproperties() {
		Map<String, String> data = new HashMap<String, String>();
		
		for (Properties prop: properties){
			Iterator<Object> itKeys = prop.keySet().iterator();
			String key = null;
			String value = null;
			
			while (itKeys.hasNext()){
				key = (String)itKeys.next();
				value = (String)data.get(key);

				if (value == null) {
					try{
						value = prop.getProperty(key);
						data.put(key, value);
					}
					catch (Exception e){
						log.log(Level.SEVERE, "Property " + key + " not found", e);
						value = null;
					}
				}
			}
		}
		return data;
		
	}
	
	public static void setProperties(List<Properties> properties) {
		PropertiesController.properties = properties;
	}

}
