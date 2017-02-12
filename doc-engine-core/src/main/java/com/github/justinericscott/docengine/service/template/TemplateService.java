/**
 * TODO: License
 */
package com.github.justinericscott.docengine.service.template;

import java.util.Map;

/**
 * 
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public interface TemplateService {

	/**
	 * 
	 * @param name
	 * @param template
	 */
	void load(String name, String template);
	
	/**
	 * TODO: Description
	 * 
	 * @param template
	 * @param tokens
	 * @return
	 */
	String run(String name, Map<String, Object> tokens);
}
