/**
 * TODO: License
 */
package com.itgfirm.docengine.instance;

import java.util.List;

import com.itgfirm.docengine.types.Instance;

/**
 * @author Justin Scott
 * 
 *         Represents the Instance database table. The repository "class" performs the getting,
 *         setting and deleting of Instance records. Performs the marjority of null checking in
 *         both directions, if a query has no results, a null object will be returned.
 */
interface InstanceRepo {

	/**
	 * Gets all Instance records.
	 * 
	 * @return All Instance records.
	 */
	List<Instance> get();

	/**
	 * Gets a single Instance based upon the provided ID.
	 * 
	 * @param id
	 * @return Instance record related to the provided ID.
	 */
	Instance get(Long id);

	/**
	 * Gets a single Instance based upon the provided project ID and code.
	 * 
	 * @param projectId
	 * @param code
	 * @return Instance record related to the provided code.
	 */
	Instance get(String projectId, String code);

	/**
	 * Gets a list of Instance records based upon the provided project ID and code.
	 * 
	 * @param projectId
	 * @param like
	 * @return List of Instance records.
	 */
	List<Instance> getByProjectAndCodeLike(String projectId, String like);

	/**
	 * Gets a list of Instance records based upon the provided query and parameters.
	 * 
	 * @param query
	 * @param paramName
	 * @param value
	 * @return List of Instance records.
	 */
	List<Instance> getWithQuery(String query, String paramName, Object value);

	/**
	 * Gets a list of Instance records based upon the provided query and parameters.
	 * 
	 * @param query
	 * @param paramNames
	 * @param values
	 * @return List of Instance records.
	 */
	List<Instance> getWithQuery(String query, String[] paramNames, Object[] values);

	/**
	 * Initializes the children, if any, of the given Instance.
	 * 
	 * @param instance
	 * @return Instance with its children, if any exist.
	 */
	Instance initialize(Instance instance);

	/**
	 * Merges (add or updated) an Instance record.
	 * 
	 * @param instance
	 * @return the newly merged Instance, with the created ID if new.
	 */
	Instance merge(Instance instance);

	/**
	 * Deletes all Instance records.
	 * 
	 * @return True or False based upon success of the operation.
	 */
	boolean deleteAll();

	/**
	 * Deletes the provided Instance object from the database.
	 * 
	 * @param instance
	 * @return True if successfully deleted, False otherwise.
	 */
	boolean delete(Instance instance);
}