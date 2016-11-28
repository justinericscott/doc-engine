/**
 * TODO: License
 */
package com.itgfirm.docengine.service;

import static com.itgfirm.docengine.util.Constants.*;

import org.springframework.beans.factory.annotation.Qualifier;

import com.itgfirm.docengine.types.jpa.ContentJpaImpl;

/**
 * @author Justin Scott
 * 
 *         Represents the Content Service as a whole. The Content Service will be a "base"
 *         service, it will only interact with its REST and Repo counterparts.
 * 
 */
@Qualifier(AUTOWIRE_QUALIFIER_ADVANCED)
public interface AdvancedContentService extends ContentService {
	
	/**
	 * Gets a single piece of Content based upon the provided content code.
	 * 
	 * @param code
	 * @param eagerKids
	 * @return Content record related to the provided content code.
	 */
	ContentJpaImpl findByContentCd(String code, boolean eagerKids);
	
	/**
	 * Gets a single piece of Content based upon the provided ID.
	 * 
	 * @param id
	 * @param eagerKids
	 * @return Content record related to the provided ID.
	 */
	ContentJpaImpl findOne(Long id, boolean eagerKids);

	/**
	 * Gets the children, if any, for the provided ID.
	 * 
	 * @param id
	 * @return A list of children content for the provided ID.
	 */
	Iterable<? extends ContentJpaImpl> getChildren(Long id);

	/**
	 * Gets the children, if any, for the provided ID.
	 * 
	 * @param id
	 * @param eagerKids
	 * @return A list of children content for the provided ID
	 */
	Iterable<? extends ContentJpaImpl> getChildren(Long id, boolean eagerKids);

	/**
	 * Gets the children, if any, for the provided code.
	 * 
	 * @param code
	 * @return A list of children content for the provided code.
	 */
	Iterable<? extends ContentJpaImpl> getChildren(String code);

	/**
	 * Gets the children, if any, for the provided code.
	 * 
	 * @param code
	 * @param eagerKids
	 * @return A list of children content for the provided code.
	 */
	Iterable<? extends ContentJpaImpl> getChildren(String code, boolean eagerKids);

	/**
	 * Gets the children, if any, for the provided Content.
	 * 
	 * @param content
	 * @return A list of children content for the provided Content.
	 */
	Iterable<? extends ContentJpaImpl> getChildren(ContentJpaImpl content);

	/**
	 * Gets the children, if any, for the provided Content.
	 * 
	 * @param content
	 * @param eagerKids
	 * @return A list of children content for the provided Content.
	 */
	Iterable<? extends ContentJpaImpl> getChildren(ContentJpaImpl content, boolean eagerKids);
}