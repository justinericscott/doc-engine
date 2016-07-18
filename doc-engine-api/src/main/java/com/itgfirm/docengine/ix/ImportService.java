/**
 * TODO: License
 */
package com.itgfirm.docengine.ix;

import java.io.File;
import java.util.List;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public interface ImportService {

	/**
	 * TODO: Description
	 * 
	 * @param file
	 * @return
	 */
	List<Object> importFromFile(File file, Class<?> clazz);
}