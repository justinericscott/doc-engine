/**
 * TODO: License
 */
package com.github.justinericscott.docengine.service.ix;

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
	<T> T importFromFile(Class<T> clazz, String path);
}