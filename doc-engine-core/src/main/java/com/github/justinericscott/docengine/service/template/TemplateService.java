package com.github.justinericscott.docengine.service.template;

import java.util.Map;

public interface TemplateService {

	String get(String name);
	
	void load(String name, String ftl);
	
	String process(String name, Map<String, Object> tokens);
	
	String once(String ftl, Map<String, Object> tokens);
}
