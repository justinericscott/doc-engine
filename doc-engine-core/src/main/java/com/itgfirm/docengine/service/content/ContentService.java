/**
 * TODO: License
 */
package com.itgfirm.docengine.service.content;

import com.itgfirm.docengine.types.ContentJpaImpl;

/**
 * @author Justin Scott
 * 
 *         Represents the Content Service as a whole. The Content Service will
 *         be a "base" service, it will only interact with its REST and Repo
 *         counterparts.
 * 
 */
public interface ContentService {

	/**
	 * Deletes a Content record based upon the provided ID.
	 * 
	 * @param id
	 *            The ID of the Content record to delete.
	 */
	void delete(Long id);

	/**
	 * Deletes a Content record based upon the provided content code.
	 * 
	 * @param code
	 *            The content code of the Content record to delete.
	 * 
	 * @return True or False based upon success of the operation.
	 */
	void delete(String code);

	/**
	 * Deletes a Content record based upon the provided Content.
	 * 
	 * @param content
	 *            Content to delete.
	 */
	void delete(ContentJpaImpl content);

	/**
	 * Deletes many Content records for the provided {@link Iterable}.
	 * 
	 * @param contents
	 *            {@link Iterable} group of Contents to delete.
	 */
	void delete(Iterable<? extends ContentJpaImpl> contents);

	/**
	 * Deletes all Content records.
	 */
	void deleteAll();

	/**
	 * Deletes any Content record based upon the provided content code search
	 * string.
	 * 
	 * @param like
	 * @return True or False based upon success of the operation.
	 */
	void deleteByContentCdLike(String like);
	
	/**
	 * Gets all Content records.
	 * 
	 * @return All Content records.
	 */
	Iterable<? extends ContentJpaImpl> findAll();

	/**
	 * Gets a single piece of Content based upon the provided content code.
	 * 
	 * @param code
	 *            The content code to look for.
	 * 
	 * @return Content record related to the provided content code.
	 */
	ContentJpaImpl findByContentCd(String code);

	/**
	 * Gets a list of Content records based upon the provided content code
	 * search string.
	 * 
	 * @param like
	 * @return Content records related to the content code search string.
	 */
	Iterable<? extends ContentJpaImpl> findByContentCdLike(String like);

	/**
	 * Gets a single piece of Content based upon the provided ID.
	 * 
	 * @param id
	 * @return Content record related to the provided ID.
	 */
	ContentJpaImpl findOne(Long id);

	/**
	 * Merges (adds or updates) a Content record.
	 * 
	 * @param content
	 * @return The newly merged Content, with created ID if new.
	 */
	ContentJpaImpl save(ContentJpaImpl content);

	/**
	 * Merges (adds or updates) a list of Contents.
	 * 
	 * @param contents
	 * @return The newly merged list of items.
	 */
	Iterable<? extends ContentJpaImpl> save(Iterable<? extends ContentJpaImpl> contents);
}