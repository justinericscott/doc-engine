/**
 * TODO: License
 */
package com.itgfirm.docengine.template;

import java.util.Map;

/**
 * 
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public interface TemplateService {

	/**
	 * TODO: Description
	 * 
	 * @param template
	 * @param tokens
	 * @return
	 */
	String process(String template, Map<String, Object> tokens);
}
