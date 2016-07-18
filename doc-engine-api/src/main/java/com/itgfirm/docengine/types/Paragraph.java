/**
 * TODO: License
 */
package com.itgfirm.docengine.types;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public interface Paragraph extends Content {

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	Clause getClause();

	/**
	 * TODO: Description
	 * 
	 * @param clause
	 */
	void setClause(Clause clause);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	boolean isSubPara();

	/**
	 * TODO: Description
	 * 
	 * @param isSubPara
	 */
	void setSubPara(boolean isSubPara);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	boolean isFirst();

	/**
	 * TODO: Description
	 * 
	 * @param isFirst
	 */
	void setFirst(boolean isFirst);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	boolean isLast();

	/**
	 * TODO: Description
	 * 
	 * @param isLast
	 */
	void setLast(boolean isLast);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	boolean isParent();

	/**
	 * TODO: Description
	 * 
	 * @param isParent
	 */
	void setParent(boolean isParent);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	boolean isOption();

	/**
	 * TODO: Description
	 * 
	 * @param isOption
	 */
	void setOption(boolean isOption);
}