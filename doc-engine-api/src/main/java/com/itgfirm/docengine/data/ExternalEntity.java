/**
 * TODO: License
 */
package com.itgfirm.docengine.data;

import java.util.List;

/**
 * @author Justin Scott
 * 
 *         External Entity Data Model (non-persistent)
 */
public interface ExternalEntity {

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
	List<ExternalAttribute> getColumns();

	/**
	 * TODO: Description
	 * 
	 * @param column
	 */
	void addColumn(ExternalAttribute column);

	/**
	 * TODO: Description
	 * 
	 * @param columns
	 */
	void setColumns(List<ExternalAttribute> columns);
}