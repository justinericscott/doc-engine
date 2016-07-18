/**
 * TODO: License
 */
package com.itgfirm.docengine.web;

import java.util.List;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public interface RestClient {

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	List<?> get();

	/**
	 * TODO: Description
	 * 
	 * @param id
	 * @return
	 */
	Object get(Long id);

	/**
	 * TODO: Description
	 * 
	 * @param code
	 * @return
	 */
	Object get(String code);

	/**
	 * TODO: Description
	 * 
	 * @param id
	 * @param type
	 * @return
	 */
	Object get(Long id, Class<?> type);

	/**
	 * TODO: Description
	 * 
	 * @param id
	 * @param type
	 * @param eagerKids
	 * @return
	 */
	Object get(Long id, Class<?> type, boolean eagerKids);

	/**
	 * TODO: Description
	 * 
	 * @param code
	 * @param type
	 * @return
	 */
	Object get(String code, Class<?> type);

	/**
	 * TODO: Description
	 * 
	 * @param code
	 * @param type
	 * @param eagerKids
	 * @return
	 */
	Object get(String code, Class<?> type, boolean eagerKids);

	/**
	 * TODO: Description
	 * 
	 * @param projectId
	 * @param code
	 * @return
	 */
	Object get(String projectId, String code);

	/**
	 * TODO: Description
	 * 
	 * @param projectId
	 * @param code
	 * @param type
	 * @return
	 */
	Object get(String projectId, String code, Class<?> type);

	/**
	 * TODO: Description
	 * 
	 * @param projectId
	 * @param code
	 * @param type
	 * @param eagerKids
	 * @return
	 */
	Object get(String projectId, String code, Class<?> type, boolean eagerKids);

	/**
	 * TODO: Description
	 * 
	 * @param like
	 * @param type
	 * @return
	 */
	List<?> getByCodeLike(String like, Class<?> type);

	/**
	 * TODO: Description
	 * 
	 * @param projectId
	 * @param like
	 * @param type
	 * @return
	 */
	List<?> getByProjectAndCodeLike(String projectId, String like, Class<?> type);

	/**
	 * TODO: Description
	 * 
	 * @param id
	 * @param type
	 * @return
	 */
	List<?> getChildren(Long id, Class<?> type);

	/**
	 * TODO: Description
	 * 
	 * @param id
	 * @param type
	 * @param eagerKids
	 * @return
	 */
	List<?> getChildren(Long id, Class<?> type, boolean eagerKids);

	/**
	 * TODO: Description
	 * 
	 * @param code
	 * @param type
	 * @return
	 */
	List<?> getChildren(String code, Class<?> type);

	/**
	 * TODO: Description
	 * 
	 * @param code
	 * @param type
	 * @param eagerKids
	 * @return
	 */
	List<?> getChildren(String code, Class<?> type, boolean eagerKids);

	/**
	 * TODO: Description
	 * 
	 * @param projectId
	 * @param code
	 * @param type
	 * @return
	 */
	List<?> getChildren(String projectId, String code, Class<?> type);

	/**
	 * TODO: Description
	 * 
	 * @param projectId
	 * @param code
	 * @param type
	 * @param eagerKids
	 * @return
	 */
	List<?> getChildren(String projectId, String code, Class<?> type, boolean eagerKids);

	/**
	 * TODO: Description
	 * 
	 * @param list
	 * @return
	 */
	List<?> merge(List<?> list);

	/**
	 * TODO: Description
	 * 
	 * @param object
	 * @param type
	 * @return
	 */
	Object merge(Object object, Class<?> type);

	/**
	 * TODO: Description
	 * 
	 * @param object
	 * @return
	 */
	String writeValueAsString(Object object);

	/**
	 * TODO: Description
	 * 
	 * @param object
	 */
	void delete(Object object);
}