package org.blog.backend.listener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.blog.backend.controller.PropertiesController;

public class PropertiesListener implements ServletContextListener {

	private static final String APP_PROPERTIES = "allProperties";
	private static final Logger log = Logger.getLogger(PropertiesListener.class.getName());

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try{
			ServletContext servletContext = sce.getServletContext();
			String allProperties = servletContext.getInitParameter(APP_PROPERTIES);
			if ((allProperties != null) && (!allProperties.isEmpty())) {
				initPropertiesConfig(servletContext.getInitParameter(APP_PROPERTIES));
			} else {
				log.warning("No properties have been defined.Check your web.xml configuration for a context appProperties parameter");
			}
		}
		catch (Exception e)
		{
			log.log(Level.WARNING, "There was an error initializing the application properties loader", e);
		}	
	}

	private void initPropertiesConfig(String allproperties)
	{
		
		String[] propertiesList = allproperties.trim().split(",");
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		List<Properties> properties = new ArrayList<Properties>();
		
		InputStream configStream = null;
		Properties p = null;
		for (String property : propertiesList)
		{
			configStream = contextClassLoader.getResourceAsStream(property);
			p = new Properties();
			try
			{
				p.load(configStream);
				properties.add(p);
				log.info(property + " added");
			}
			catch (IOException e)
			{
				log.log(Level.SEVERE, "Where is the file man", e);
			}
			catch (NullPointerException e)
			{
				log.log(Level.SEVERE, property + " Null pointer man, there is no property with this name ", e);
			}
		}
		PropertiesController.setProperties(properties);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

}
