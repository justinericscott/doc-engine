/**
 * TODO: License
 */
package com.itgfirm.docengine.document;

import com.itgfirm.docengine.types.jpa.InstanceJpaImpl;

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
	InstanceJpaImpl create(String projectId, String code);
}