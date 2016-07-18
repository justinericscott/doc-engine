/**
 * TODO: License
 */
package com.itgfirm.docengine.document;

import com.itgfirm.docengine.types.Instance;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
interface DocumentService {

	/**
	 * TODO: Description
	 * 
	 * @param projectId
	 * @param code
	 * @return
	 */
	Instance create(String projectId, String code);
}