/**
 * TODO: License
 */
package com.itgfirm.docengine.types;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public interface Instance extends Comparable<Instance> {

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	Long getId();

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getProjectId();

	/**
	 * TODO: Description
	 * 
	 * @param projectId
	 */
	void setProjectId(String projectId);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getCustomBody();

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
	String getStatusCd();

	/**
	 * TODO: Description
	 * 
	 * @param statusCd
	 */
	void setStatusCd(String statusCd);

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
	boolean isAdHoc();

	/**
	 * TODO: Description
	 * 
	 * @param isAdHoc
	 */
	void setAdHoc(boolean isAdHoc);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	boolean isStrikeHeader();

	/**
	 * TODO: Description
	 * 
	 * @param isStrikeHeader
	 */
	void setStrikeHeader(boolean isStrikeHeader);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	boolean isMarkedForAction();

	/**
	 * TODO: Description
	 * 
	 * @param isMarkedForAction
	 */
	void setMarkedForAction(boolean isMarkedForAction);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getMarkedForActionComment();

	/**
	 * TODO: Description
	 * 
	 * @param markedForActionComment
	 */
	void setMarkedForActionComment(String markedForActionComment);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	Content getContent();

	/**
	 * TODO: Description
	 * 
	 * @param content
	 */
	void setContent(Content content);

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