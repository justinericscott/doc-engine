/**
 * TODO: License
 */
package com.itgfirm.docengine.data;

/**
 * @author Justin Scott
 * 
 *         External Attribute Data Model (non-persistent)
 */
public interface ExternalAttribute {

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getName();

	/**
	 * TODO: Description
	 * 
	 * @param name
	 */
	void setName(String name);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getDescription();

	/**
	 * TODO: Description
	 * 
	 * @param description
	 */
	void setDescription(String description);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	Integer getDatatype();

	/**
	 * TODO: Description
	 * 
	 * @param datatype
	 */
	void setDatatype(Integer datatype);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	boolean isNullable();

	/**
	 * TODO: Description
	 * 
	 * @param isNullable
	 */
	void setNullable(boolean isNullable);
}