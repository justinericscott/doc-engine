/**
 * TODO: License
 */
package com.itgfirm.docengine.service.ix;

import java.io.File;

import com.itgfirm.docengine.types.ContentJpaImpl;

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
	<T extends ContentJpaImpl> Iterable<T> importFromFile(Class<T> clazz, String path);
}