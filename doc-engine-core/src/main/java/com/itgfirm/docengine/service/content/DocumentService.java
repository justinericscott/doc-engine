/**
 * TODO: License
 */
package com.itgfirm.docengine.service.content;

import com.itgfirm.docengine.types.InstanceJpaImpl;

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