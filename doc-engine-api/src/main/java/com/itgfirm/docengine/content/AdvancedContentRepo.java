/**
 * TODO: License
 */
package com.itgfirm.docengine.content;

import com.itgfirm.docengine.types.Content;

/**
 * @author Justin Scott
 * 
 *         Represents the Content database table. The repository "class" performs the getting,
 *         setting and deleting of Content templates. Performs the marjority of null checking
 *         in both directions, if a query has no results, a null object will be returned.
 * 
 */
interface AdvancedContentRepo extends ContentRepo {

	/**
	 * Initializes the children, if any, of the given Content.
	 * 
	 * @param content
	 * @return Content with its children, if any exist.
	 */
	Content initialize(Content content);
}