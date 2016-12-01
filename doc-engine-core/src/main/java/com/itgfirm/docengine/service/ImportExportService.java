/**
 * TODO: License
 */
package com.itgfirm.docengine.service;

import java.io.File;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public interface ImportExportService {

	/**
	 * TODO: Description
	 * 
	 * @param clazz
	 * @param path
	 * @return
	 */
	File exportToFile(Class<?> clazz, String path);
	
	/**
	 * TODO: Description
	 * 
	 * @param clazz
	 * @param path
	 * 
	 * @return
	 */
	Iterable<?> importFromFile(Class<?> clazz, String path);
}