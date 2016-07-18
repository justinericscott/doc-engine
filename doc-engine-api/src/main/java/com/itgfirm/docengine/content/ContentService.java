/**
 * TODO: License
 */
package com.itgfirm.docengine.content;

import java.util.List;

import com.itgfirm.docengine.types.Content;

/**
 * @author Justin Scott
 * 
 *         Represents the Content Service as a whole. The Content Service will be a "base"
 *         service, it will only interact with its REST and Repo counterparts.
 * 
 */
public interface ContentService {

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
	 * Merges (adds or updates) a Content record.
	 * 
	 * @param content
	 * @return The newly merged Content, with created ID if new.
	 */
	Content merge(Content content);

	/**
	 * Merges (adds or updates) a list of Contents.
	 * 
	 * @param contents
	 * @return The newly merged list of items.
	 */
	List<Content> merge(List<Content> contents);

	/**
	 * Deletes a Content record based upon the provided ID.
	 * 
	 * @param id
	 * @return True or False based upon success of the operation.
	 */
	boolean delete(Long id);

	/**
	 * Deletes a Content record based upon the provided content code.
	 * 
	 * @param code
	 * @return True or False based upon success of the operation.
	 */
	boolean delete(String code);

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