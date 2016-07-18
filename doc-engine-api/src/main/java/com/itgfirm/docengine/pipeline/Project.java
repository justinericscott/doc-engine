/**
 * TODO: License
 */
package com.itgfirm.docengine.pipeline;

import java.util.List;
import java.util.Map;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public interface Project {

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
	String getLeaseNumber();

	/**
	 * TODO: Description
	 * 
	 * @param leaseNumber
	 */
	void setLeaseNumber(String leaseNumber);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getDocumentCd();

	/**
	 * TODO: Description
	 * 
	 * @param documentCd
	 */
	void setDocumentCd(String documentCd);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getPackageCd();

	/**
	 * TODO: Description
	 * 
	 * @param packageCd
	 */
	void setPackageCd(String packageCd);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getPhase();

	/**
	 * TODO: Description
	 * 
	 * @param phase
	 */
	void setPhase(String phase);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	Map<String, ?> getTokens();

	/**
	 * TODO: Description
	 * 
	 * @param tokens
	 */
	void setTokens(Map<String, ?> tokens);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	List<Exhibit> getExhibits();

	/**
	 * TODO: Description
	 * 
	 * @param exhibits
	 */
	void setExhibits(List<Exhibit> exhibits);
}