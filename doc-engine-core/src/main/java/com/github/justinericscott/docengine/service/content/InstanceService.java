/**
 * TODO: License
 */
package com.github.justinericscott.docengine.service.content;

//import com.github.justinericscott.docengine.models.ClauseInstance;
//import com.github.justinericscott.docengine.models.DocumentInstance;
import com.github.justinericscott.docengine.models.Instance;
import com.github.justinericscott.docengine.models.Instance.ClauseInstance;
import com.github.justinericscott.docengine.models.Instance.DocumentInstance;
import com.github.justinericscott.docengine.models.Instance.ParagraphInstance;
import com.github.justinericscott.docengine.models.Instance.SectionInstance;
import com.github.justinericscott.docengine.models.Instances;
//import com.github.justinericscott.docengine.models.ParagraphInstance;
//import com.github.justinericscott.docengine.models.SectionInstance;

/**
 * @author Justin Scott
 * 
 *         Represents the Instance Service as a whole. The Instance Service will
 *         be a "base" service, it will only interact with its REST and Repo
 *         counterparts.
 */
public interface InstanceService {

	boolean delete(Long id);
	
	/**
	 * Deletes the Instance record for the provided ID.
	 * 
	 * @param id
	 * @return True or False based upon success of the operation.
	 */
	boolean delete(Long id, Class<?> type);

	boolean delete(String projectId, String code);
	
	/**
	 * Deletes the Instance record for the provided project ID and code.
	 * 
	 * @param projectId
	 * @param code
	 * @return True or False based upon success of the operation.
	 */
	<T> boolean delete(String projectId, String code, Class<T> type);

	boolean delete(Instance instance);
	
	/**
	 * Deletes the provided Instance object from the database.
	 * 
	 * @param instance
	 * @return True if successfully deleted, False otherwise.
	 */
	<T> boolean delete(T instance, Class<T> type);
	
	boolean deleteAll();
	
	/**
	 * Gets all Instance records.
	 * 
	 * @return All Instance records.
	 */
	Instances findAll();
	
	<T> T findAll(Class<T> type);
	
	<T> T findAll(Class<T> type, boolean eagerKids);

	Instance findByProjectIdAndCode(String projectId, String code);
	
	/**
	 * Gets a single Instance based upon the provided project ID and code.
	 * 
	 * @param projectId
	 * @param code
	 * @return Instance record related to the provided code.
	 */
	<T> T findByProjectIdAndCode(String projectId, String code, Class<T> type);

	/**
	 * Gets a complex Instance type based upon the provided project ID, code and
	 * Class with its children included.
	 * 
	 * @param projectId
	 * @param code
	 * @param type
	 * @return The complex Instance type with its children included.
	 */
	<T> T findByProjectIdAndCode(String projectId, String code, Class<T> type, boolean eagerKids);

	Instances findByProjectIdAndCodeLike(String projectId, String like);
	
	/**
	 * Gets a list of Instance records based upon the provided project ID and
	 * code.
	 * 
	 * @param projectId
	 * @param like
	 * @return List of Instance records.
	 */
	<T> T findByProjectIdAndCodeLike(String projectId, String like, Class<T> type);

	Instance findOne(Long id);
	
	/**
	 * Obtains an {@link Instance} for the provided ID.
	 * 
	 * @param id
	 *            Identifier of the {@link Instance} to return.
	 * @return Requested {@link Instance}.
	 */
	<T> T findOne(Long id, Class<T> type);

	/**
	 * Gets an Instance and its children, if any, based upon the provided ID.
	 * 
	 * @param id
	 * @param eagerKids
	 * @return Instance record and its children, if any, related to the provided
	 *         ID.
	 */
	<T> T findOne(Long id, Class<T> type, boolean eagerKids);

	/**
	 * Gets the children, if any, for the provided ID.
	 * 
	 * @param id
	 * @return A List of children instances for the provided ID
	 */
	<T> T getChildren(Long id, Class<T> type);

	/**
	 * Gets the children (and nested children), if any, for the provided ID.
	 * 
	 * @param id
	 * @param eagerKids
	 * @return A List of children (and nested children) instances for the
	 *         provided ID.
	 */
	<T> T getChildren(Long id, Class<T> type, boolean eagerKids);

	/**
	 * Gets the children, if any, for the provided project ID and code.
	 * 
	 * @param projectId
	 * @param code
	 * @return A List of children, if any, for the provided project ID and code.
	 */
	<T> T getChildren(String projectId, String code, Class<T> type);

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
	<T> T getChildren(String projectId, String code, Class<T> type, boolean eagerKids);

	/**
	 * Gets the children, if any, for the provided Instance object.
	 * 
	 * @param instance
	 * @return A List of children, if any, for the provided Instance object.
	 */
	<T, P> T getChildren(P instance, Class<T> type);

	/**
	 * Gets the children (and nested children), if any, for the provided
	 * Instance object.
	 * 
	 * @param instance
	 * @param eagerKids
	 * @return A List of children (and nested children), if any, for the
	 *         provided Instance object.
	 */
	<T, P> T getChildren(P instance, Class<T> type, boolean eagerKids);

	/**
	 * Merges (add or updated) an Instance record.
	 * 
	 * @param instance
	 * @return The newly merged Instance, with created ID if new.
	 */
	Instance save(Instance instance);
	
	/**
	 * TODO: Document
	 * 
	 * @param document
	 * @return
	 */
	DocumentInstance save(DocumentInstance document);
	
	/**
	 * TODO: Document
	 * 
	 * @param section
	 * @return
	 */
	SectionInstance save(SectionInstance section);
	
	/**
	 * TODO: Document
	 * 
	 * @param clause
	 * @return
	 */
	ClauseInstance save(ClauseInstance clause);
	
	/**
	 * TODO: Document
	 * 
	 * @param paragraph
	 * @return
	 */
	ParagraphInstance save(ParagraphInstance paragraph);

	/**
	 * Merges (adds or updates) Instance records.
	 * 
	 * @param instances
	 * @return The newly merged Instances, with created IDs if new.
	 */
	Instances save(Instances instances);
}