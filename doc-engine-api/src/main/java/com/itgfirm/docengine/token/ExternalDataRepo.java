/**
 * TODO: License
 */
package com.itgfirm.docengine.token;

import java.util.List;
import java.util.Map;

import com.itgfirm.docengine.data.ExternalSchema;

/**
 * @author Justin Scott
 * 
 *         Represents the External database. The repository "class" performs the getting of
 *         business data. Performs the majority of null checking in both directions, if a query
 *         has no results, a null object will be returned.
 * 
 */
interface ExternalDataRepo {

	/**
	 * Executes the provided SQL, there is nothing returned. Typically used for DDL operations.
	 * 
	 * @param sql
	 */
	void execute(String sql);

	/**
	 * Breaks a multi-statement SQL script into individual statements and executes them.
	 * Nothing is returned.
	 * 
	 * @param script
	 */
	void execute(String[] script);

	/**
	 * Obtains the database metadata and creates objects representing the external schema.
	 * 
	 * @return A populated ExternalSchema object.
	 */
	ExternalSchema getExternalSchema();

	/**
	 * Performs a query that is expecting many results.
	 * 
	 * @param sql
	 * @return A list of results.
	 */
	List<Map<String, Object>> queryForList(String sql);

	/**
	 * Performs a query that is expecting one result.
	 * 
	 * @param sql
	 * @return A map object, made up of column names and their values.
	 */
	Map<String, Object> queryForMap(String sql);

	/**
	 * Performs an update based on the provided SQL.
	 * 
	 * @param sql
	 * @return The count of effected rows.
	 */
	int update(String sql);

	/**
	 * Breaks a multi-statement SQL script into individual statements and performs updates with
	 * them.
	 * 
	 * @param script
	 * @return
	 */
	Integer[] update(String[] script);
}