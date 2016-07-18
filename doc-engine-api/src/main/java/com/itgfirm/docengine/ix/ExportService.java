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
public interface ExportService {

	/**
	 * TODO: Description
	 * 
	 * @param data
	 * @param annotatedClass
	 * @param path
	 * @return
	 */
	File export(List<?> data, Class<?> annotatedClass, String path);
}