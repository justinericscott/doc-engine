/**
 * TODO: License
 */
package com.itgfirm.docengine.token;

import java.util.List;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
interface TokenData {

	/**
	 * TODO: Description
	 * 
	 * @param value
	 */
	void addValue(TokenValue value);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getName();

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	TokenValue getValue();

	/**
	 * TODO: Description
	 * 
	 * @param phase
	 * @return
	 */
	TokenValue getValue(String phase);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	List<TokenValue> getValues();

	/**
	 * TODO: Description
	 * 
	 * @param phase
	 * @return
	 */
	List<TokenValue> getValues(String phase);
}