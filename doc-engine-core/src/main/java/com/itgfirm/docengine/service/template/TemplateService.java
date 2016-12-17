/**
 * TODO: License
 */
package com.itgfirm.docengine.service.template;

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
	void loadTemplate(String name, String template);
	
	/**
	 * TODO: Description
	 * 
	 * @param template
	 * @param tokens
	 * @return
	 */
	String process(String template, Map<String, Object> tokens);
}
