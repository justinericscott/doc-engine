/**
 * TODO: License
 */
package com.itgfirm.docengine.token;

import com.itgfirm.docengine.types.TokenDefinition;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
interface TokenValue {

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	TokenDefinition getDefinition();

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getDisplayValue();

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	Object getValue();
}