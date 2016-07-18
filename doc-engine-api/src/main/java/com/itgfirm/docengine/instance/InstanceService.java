/**
 * TODO: License
 */
package com.itgfirm.docengine.instance;

import java.util.List;

import com.itgfirm.docengine.types.Instance;

/**
 * @author Justin Scott
 * 
 *         Represents the Instance Service as a whole. The Instance Service will be a "base"
 *         service, it will only interact with its REST and Repo counterparts.
 */
public interface InstanceService {

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
	 * Gets an Instance and its children, if any, based upon the provided ID.
	 * 
	 * @param id
	 * @param eagerKids
	 * @return Instance record and its children, if any, related to the provided ID.
	 */
	Instance get(Long id, boolean eagerKids);

	/**
	 * Gets a single Instance based upon the provided project ID and code.
	 * 
	 * @param projectId
	 * @param code
	 * @return Instance record related to the provided code.
	 */
	Instance get(String projectId, String code);

	/**
	 * Gets a complex Instance type based upon the provided project ID, code and Class with its
	 * children included.
	 * 
	 * @param projectId
	 * @param code
	 * @param type
	 * @return The complex Instance type with its children included.
	 */
	Instance get(String projectId, String code, boolean eagerKids);

	/**
	 * Gets the children, if any, for the provided ID.
	 * 
	 * @param id
	 * @return A List of children instances for the provided ID
	 */
	List<? extends Instance> getChildren(Long id);

	/**
	 * Gets the children (and nested children), if any, for the provided ID.
	 * 
	 * @param id
	 * @param eagerKids
	 * @return A List of children (and nested children) instances for the provided ID.
	 */
	List<? extends Instance> getChildren(Long id, boolean eagerKids);

	/**
	 * Gets the children, if any, for the provided project ID and code.
	 * 
	 * @param projectId
	 * @param code
	 * @return A List of children, if any, for the provided project ID and code.
	 */
	List<? extends Instance> getChildren(String projectId, String code);

	/**
	 * Gets the children (and nested children), if any, for the provided project ID and code.
	 * 
	 * @param projectId
	 * @param code
	 * @param eagerKids
	 * @return A List of children (and nested children), if any, for the provided project ID
	 *         and code.
	 */
	List<? extends Instance> getChildren(String projectId, String code, boolean eagerKids);

	/**
	 * Gets the children, if any, for the provided Instance object.
	 * 
	 * @param instance
	 * @return A List of children, if any, for the provided Instance object.
	 */
	List<? extends Instance> getChildren(Instance instance);

	/**
	 * Gets the children (and nested children), if any, for the provided Instance object.
	 * 
	 * @param instance
	 * @param eagerKids
	 * @return A List of children (and nested children), if any, for the provided Instance
	 *         object.
	 */
	List<? extends Instance> getChildren(Instance instance, boolean eagerKids);

	/**
	 * Gets a list of Instance records based upon the provided project ID and code.
	 * 
	 * @param projectId
	 * @param like
	 * @return List of Instance records.
	 */
	List<Instance> getByProjectAndCodeLike(String projectId, String like);

	/**
	 * Merges (add or updated) an Instance record.
	 * 
	 * @param instance
	 * @return The newly merged Instance, with created ID if new.
	 */
	Instance merge(Instance instance);

	/**
	 * Merges (adds or updates) Instance records.
	 * 
	 * @param instances
	 * @return The newly merged Instances, with created IDs if new.
	 */
	List<Instance> merge(List<Instance> instances);

	/**
	 * Deletes the Instance record for the provided ID.
	 * 
	 * @param id
	 * @return True or False based upon success of the operation.
	 */
	boolean delete(Long id);

	/**
	 * Deletes the Instance record for the provided project ID and code.
	 * 
	 * @param projectId
	 * @param code
	 * @return True or False based upon success of the operation.
	 */
	boolean delete(String projectId, String code);

	/**
	 * Deletes the provided Instance object from the database.
	 * 
	 * @param instance
	 * @return True if successfully deleted, False otherwise.
	 */
	boolean delete(Instance instance);
}