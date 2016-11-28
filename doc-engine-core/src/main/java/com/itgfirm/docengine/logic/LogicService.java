/**
 * TODO: License
 */
package com.itgfirm.docengine.logic;

import java.io.File;
import java.util.List;

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
	 * @param obj
	 * @param table
	 * @return
	 */
	List<String> process(Object obj);

	/**
	 * TODO: Description
	 * 
	 * @param path
	 */
	void addResource(File path);
}