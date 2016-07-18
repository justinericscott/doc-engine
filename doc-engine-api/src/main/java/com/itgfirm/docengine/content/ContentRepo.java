/**
 * TODO: License
 */
package com.itgfirm.docengine.content;

import java.util.List;

import com.itgfirm.docengine.types.Content;

/**
 * @author Justin Scott
 * 
 *         Represents the Content database table. The repository "class" performs the getting,
 *         setting and deleting of Content templates. Performs the marjority of null checking
 *         in both directions, if a query has no results, a null object will be returned.
 * 
 */
interface ContentRepo {

	/**
	 * Gets all Content records.
	 * 
	 * @return All Content records.
	 */
	List<Content> get();

	/**
	 * Gets a single piece of Content based upon the provided ID.
	 * 
	 * @param id
	 * @return Content record related to the provided ID.
	 */
	Content get(Long id);

	/**
	 * Gets a single piece of Content based upon the provided content code.
	 * 
	 * @param code
	 * @return Content record related to the provided content code.
	 */
	Content get(String code);

	/**
	 * Gets a list of Content records based upon the provided content code search string.
	 * 
	 * @param like
	 * @return Content records related to the content code search string.
	 */
	List<Content> getByCodeLike(String like);

	/**
	 * Gets a list of Content records based upon the provided query and parameter.
	 * 
	 * @param query
	 * @param paramName
	 * @param value
	 * @return Content records returned by the provided query and parameter.
	 */
	List<Content> getWithQuery(String query, String paramName, Object value);

	/**
	 * Gets a list of Content records based upon the provided query and parameters.
	 * 
	 * @param query
	 * @param paramNames
	 * @param values
	 * @return Content records returned by the provided query and parameters.
	 */
	List<Content> getWithQuery(String query, String[] paramNames, Object[] values);

	/**
	 * Merges (adds or updates) a Content record.
	 * 
	 * @param content
	 * @return The newly merged Content, with created ID if new.
	 */
	Content merge(Content content);

	/**
	 * Deletes all Content records. Disabled for now.
	 * 
	 * @return
	 */
	// boolean deleteAll();

	/**
	 * Deletes a Content record based upon the provided Content.
	 * 
	 * @param content
	 * @return True or False based upon success of the operation.
	 */
	boolean delete(Content content);

	/**
	 * Deletes any Content record based upon the provided content code search string.
	 * 
	 * @param like
	 * @return True or False based upon success of the operation.
	 */
	boolean deleteByCodeLike(String like);
}