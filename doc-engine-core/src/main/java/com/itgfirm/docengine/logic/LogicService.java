/**
 * TODO: License
 */
package com.itgfirm.docengine.logic;

import java.io.File;

/**
 * 
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public interface LogicService {

	/**
	 * TODO: Description
	 * 
	 * @param path
	 */
	void addResource(File path);

	/**
	 * TODO: Description
	 * 
	 * @param obj
	 * @param table
	 * @return
	 */
	Iterable<String> process(Object obj);
}