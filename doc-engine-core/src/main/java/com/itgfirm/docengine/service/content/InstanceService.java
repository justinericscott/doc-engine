/**
 * TODO: License
 */
package com.itgfirm.docengine.service.content;

import static com.itgfirm.docengine.DocEngine.Constants.*;

import org.springframework.beans.factory.annotation.Qualifier;

import com.itgfirm.docengine.types.InstanceJpaImpl;

/**
 * @author Justin Scott
 * 
 *         Represents the Instance Service as a whole. The Instance Service will
 *         be a "base" service, it will only interact with its REST and Repo
 *         counterparts.
 */
@Qualifier(AUTOWIRE_QUALIFIER_INSTANCE)
public interface InstanceService {

	/**
	 * Deletes the Instance record for the provided ID.
	 * 
	 * @param id
	 * @return True or False based upon success of the operation.
	 */
	void delete(Long id);

	/**
	 * Deletes the Instance record for the provided project ID and code.
	 * 
	 * @param projectId
	 * @param code
	 * @return True or False based upon success of the operation.
	 */
	void delete(String projectId, String code);

	/**
	 * Deletes the provided Instance object from the database.
	 * 
	 * @param instance
	 * @return True if successfully deleted, False otherwise.
	 */
	void delete(InstanceJpaImpl instance);
	
	/**
	 * Gets all Instance records.
	 * 
	 * @return All Instance records.
	 */
	Iterable<? extends InstanceJpaImpl> findAll();

	/**
	 * Gets the children, if any, for the provided ID.
	 * 
	 * @param id
	 * @return A List of children instances for the provided ID
	 */
	Iterable<? extends InstanceJpaImpl> findByParent(Long id);

	/**
	 * Gets the children (and nested children), if any, for the provided ID.
	 * 
	 * @param id
	 * @param eagerKids
	 * @return A List of children (and nested children) instances for the
	 *         provided ID.
	 */
	Iterable<? extends InstanceJpaImpl> findByParent(Long id, boolean eagerKids);

	/**
	 * Gets the children, if any, for the provided project ID and code.
	 * 
	 * @param projectId
	 * @param code
	 * @return A List of children, if any, for the provided project ID and code.
	 */
	Iterable<? extends InstanceJpaImpl> findByParent(String projectId, String code);

	/**
	 * Gets the children (and nested children), if any, for the provided project
	 * ID and code.
	 * 
	 * @param projectId
	 * @param code
	 * @param eagerKids
	 * @return A List of children (and nested children), if any, for the
	 *         provided project ID and code.
	 */
	Iterable<? extends InstanceJpaImpl> findByParent(String projectId, String code, boolean eagerKids);

	/**
	 * Gets the children, if any, for the provided Instance object.
	 * 
	 * @param instance
	 * @return A List of children, if any, for the provided Instance object.
	 */
	Iterable<? extends InstanceJpaImpl> findByParent(InstanceJpaImpl instance);

	/**
	 * Gets the children (and nested children), if any, for the provided
	 * Instance object.
	 * 
	 * @param instance
	 * @param eagerKids
	 * @return A List of children (and nested children), if any, for the
	 *         provided Instance object.
	 */
	Iterable<? extends InstanceJpaImpl> findByParent(InstanceJpaImpl instance, boolean eagerKids);

	/**
	 * Gets a single Instance based upon the provided project ID and code.
	 * 
	 * @param projectId
	 * @param code
	 * @return Instance record related to the provided code.
	 */
	InstanceJpaImpl findByProjectIdAndContentCd(String projectId, String code);

	/**
	 * Gets a complex Instance type based upon the provided project ID, code and
	 * Class with its children included.
	 * 
	 * @param projectId
	 * @param code
	 * @param type
	 * @return The complex Instance type with its children included.
	 */
	InstanceJpaImpl findByProjectIdAndContentCd(String projectId, String code, boolean eagerKids);

	/**
	 * Gets a list of Instance records based upon the provided project ID and
	 * code.
	 * 
	 * @param projectId
	 * @param like
	 * @return List of Instance records.
	 */
	Iterable<? extends InstanceJpaImpl> findByProjectIdAndContentCdLike(String projectId, String like);

	/**
	 * Obtains an {@link InstanceJpaImpl} for the provided ID.
	 * 
	 * @param id
	 *            Identifier of the {@link InstanceJpaImpl} to return.
	 * @return Requested {@link InstanceJpaImpl}.
	 */
	InstanceJpaImpl findOne(Long id);

	/**
	 * Gets an Instance and its children, if any, based upon the provided ID.
	 * 
	 * @param id
	 * @param eagerKids
	 * @return Instance record and its children, if any, related to the provided
	 *         ID.
	 */
	InstanceJpaImpl findOne(Long id, boolean eagerKids);

	/**
	 * Merges (add or updated) an Instance record.
	 * 
	 * @param instance
	 * @return The newly merged Instance, with created ID if new.
	 */
	InstanceJpaImpl save(InstanceJpaImpl instance);

	/**
	 * Merges (adds or updates) Instance records.
	 * 
	 * @param instances
	 * @return The newly merged Instances, with created IDs if new.
	 */
	Iterable<? extends InstanceJpaImpl> save(Iterable<? extends InstanceJpaImpl> instances);
}