/**
 * 
 */
package com.itgfirm.docengine.types;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public interface ParagraphInstance extends Instance {

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	ClauseInstance getClause();

	/**
	 * TODO: Description
	 * 
	 * @param clause
	 */
	void setClause(ClauseInstance clause);
}