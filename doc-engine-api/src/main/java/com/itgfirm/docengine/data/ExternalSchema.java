/**
 * TODO: License
 */
package com.itgfirm.docengine.data;

import java.util.List;

/**
 * @author Justin Scott
 * 
 *         External Schema Data Model (non-persistent)
 */
public interface ExternalSchema {

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
	List<ExternalEntity> getTables();

	/**
	 * TODO: Description
	 * 
	 * @param table
	 */
	void addTable(ExternalEntity table);

	/**
	 * TODO: Description
	 * 
	 * @param tables
	 */
	void setTables(List<ExternalEntity> tables);
}