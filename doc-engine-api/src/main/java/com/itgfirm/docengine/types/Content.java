/**
 * TODO: License
 */
package com.itgfirm.docengine.types;

import java.sql.Timestamp;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public interface Content extends Comparable<Content> {

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	Long getId();

	/**
	 * TODO: Description
	 * 
	 * @param id
	 */
	void setId(Long id);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getContentCd();

	/**
	 * TODO: Description
	 * 
	 * @param contentCd
	 */
	void setContentCd(String contentCd);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getName();

	/**
	 * TODO: Description
	 * 
	 * @param name
	 */
	void setName(String name);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getDescription();

	/**
	 * TODO: Description
	 * 
	 * @param description
	 */
	void setDescription(String description);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getContentNumber();

	/**
	 * TODO: Description
	 * 
	 * @param contentNumber
	 */
	void setContentNumber(String contentNumber);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getBody();

	/**
	 * TODO: Description
	 * 
	 * @param body
	 */
	void setBody(String body);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getCss();

	/**
	 * TODO: Description
	 * 
	 * @param css
	 */
	void setCss(String css);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getHelper();

	/**
	 * TODO: Description
	 * 
	 * @param helper
	 */
	void setHelper(String helper);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getCategory();

	/**
	 * TODO: Description
	 * 
	 * @param category
	 */
	void setCategory(String category);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getFlags();

	/**
	 * TODO: Description
	 * 
	 * @param flags
	 */
	void setFlags(String flags);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	Integer getOrderBy();

	/**
	 * TODO: Description
	 * 
	 * @param orderBy
	 */
	void setOrderBy(Integer orderBy);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	Timestamp getValidStart();

	/**
	 * TODO: Description
	 * 
	 * @param validStart
	 */
	void setValidStart(Timestamp validStart);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	Timestamp getValidEnd();

	/**
	 * TODO: Description
	 * 
	 * @param validEnd
	 */
	void setValidEnd(Timestamp validEnd);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	boolean isValid();

	/**
	 * TODO: Description
	 * 
	 * @param checkForId
	 * @return
	 */
	boolean isValid(boolean checkForId);
}