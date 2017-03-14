package com.github.justinericscott.docengine.service.template;

import java.util.Map;

import freemarker.template.Template;

public interface TemplateService {
	
	String adhoc(String ftl, Map<String, Object> tokens);

	Template get(String name);

	void load(String name, String ftl);
	
	void load(String name, String ftl, boolean overwrite);

	void remove(String name);

	String run(String name, Map<String, Object> tokens);
}