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
public interface AdvancedContentService extends ContentService {

	/**
	 * Gets a single piece of Content based upon the provided ID.
	 * 
	 * @param id
	 * @param eagerKids
	 * @return Content record related to the provided ID.
	 */
	Content get(Long id, boolean eagerKids);
	
	/**
	 * Gets a single piece of Content based upon the provided content code.
	 * 
	 * @param code
	 * @param eagerKids
	 * @return Content record related to the provided content code.
	 */
	Content get(String code, boolean eagerKids);

	/**
	 * Gets the children, if any, for the provided ID.
	 * 
	 * @param id
	 * @return A list of children content for the provided ID.
	 */
	List<? extends Content> getChildren(Long id);

	/**
	 * Gets the children, if any, for the provided ID.
	 * 
	 * @param id
	 * @param eagerKids
	 * @return A list of children content for the provided ID
	 */
	List<? extends Content> getChildren(Long id, boolean eagerKids);

	/**
	 * Gets the children, if any, for the provided code.
	 * 
	 * @param code
	 * @return A list of children content for the provided code.
	 */
	List<? extends Content> getChildren(String code);

	/**
	 * Gets the children, if any, for the provided code.
	 * 
	 * @param code
	 * @param eagerKids
	 * @return A list of children content for the provided code.
	 */
	List<? extends Content> getChildren(String code, boolean eagerKids);

	/**
	 * Gets the children, if any, for the provided Content.
	 * 
	 * @param content
	 * @return A list of children content for the provided Content.
	 */
	List<? extends Content> getChildren(Content content);

	/**
	 * Gets the children, if any, for the provided Content.
	 * 
	 * @param content
	 * @param eagerKids
	 * @return A list of children content for the provided Content.
	 */
	List<? extends Content> getChildren(Content content, boolean eagerKids);
}