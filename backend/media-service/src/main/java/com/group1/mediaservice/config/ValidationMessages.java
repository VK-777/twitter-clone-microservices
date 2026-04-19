package com.group1.mediaservice.config;

import java.io.InputStream;
import java.util.Properties;

import org.springframework.stereotype.Component;

@Component
public class ValidationMessages {
	
	private final Properties properties = new Properties();
	
	public ValidationMessages() {
		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("validation.properties")){
			if(inputStream == null) {
				throw new RuntimeException("validation.properties file not found found in classpath");
			}
			properties.load(inputStream);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load validation.properties file", e);
		}
	}

	
	public String getMessage(String key) {
		return properties.getProperty(key);
	}

}
