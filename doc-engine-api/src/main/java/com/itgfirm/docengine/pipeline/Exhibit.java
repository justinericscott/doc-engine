/**
 * TODO: License
 */
package com.itgfirm.docengine.pipeline;

import java.util.Date;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public interface Exhibit {

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
	String getDocType();

	/**
	 * TODO: Description
	 * 
	 * @param docType
	 */
	void setDocType(String docType);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	Integer getOrderByLease();

	/**
	 * TODO: Description
	 * 
	 * @param orderByLease
	 */
	void setOrderByLease(Integer orderByLease);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	Integer getOrderByRlp();

	/**
	 * TODO: Description
	 * 
	 * @param orderByRlp
	 */
	void setOrderByRlp(Integer orderByRlp);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getExhibit();

	/**
	 * TODO: Description
	 * 
	 * @param exhibitNumber
	 */
	void setExhibit(String exhibitNumber);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getDisplayName();

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
	Integer getPageCount();

	/**
	 * TODO: Description
	 * 
	 * @param pageCount
	 */
	void setPageCount(Integer pageCount);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getProjectNumber();

	/**
	 * TODO: Description
	 * 
	 * @param projectNumber
	 */
	void setProjectNumber(String projectNumber);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	int getCreatedBy();

	/**
	 * TODO: Description
	 * 
	 * @param createdBy
	 */
	void setCreatedBy(int createdBy);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	Date getCreatedDate();

	/**
	 * TODO: Description
	 * 
	 * @param createdDate
	 */
	void setCreatedDate(Date createdDate);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	int getUpdatedBy();

	/**
	 * TODO: Description
	 * 
	 * @param updatedBy
	 */
	void setUpdatedBy(int updatedBy);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	Date getUpdatedDate();

	/**
	 * TODO: Description
	 * 
	 * @param updatedDate
	 */
	void setUpdatedDate(Date updatedDate);

	@Override
	int hashCode();

	@Override
	boolean equals(Object obj);
}