/**
 * TODO: License
 */
package com.github.justinericscott.docengine.service.ix;

import java.io.File;

import com.github.justinericscott.docengine.models.Content;

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
	Iterable<? extends Content> importFromFile(final Class<? extends Content>[] types, final String path);
	
	<T> Iterable<T> importFromFile(final Class<T> type, final String path);
}