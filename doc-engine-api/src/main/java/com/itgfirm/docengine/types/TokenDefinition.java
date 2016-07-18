/**
 * TODO: License
 */
package com.itgfirm.docengine.types;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public interface TokenDefinition extends Comparable<TokenDefinition> {

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
	String getTokenCd();

	/**
	 * TODO: Description
	 * 
	 * @param tokenCd
	 */
	void setTokenCd(String tokenCd);

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
	String getAltText();

	/**
	 * TODO: Description
	 * 
	 * @param altText
	 */
	void setAltText(String altText);

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
	Boolean isXable();

	/**
	 * TODO: Description
	 * 
	 * @param isXable
	 */
	void setIsXable(Boolean isXable);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	Boolean isRoman();

	/**
	 * TODO: Description
	 * 
	 * @param isRoman
	 */
	void setIsRoman(Boolean isRoman);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getEntity();

	/**
	 * TODO: Description
	 * 
	 * @param entity
	 */
	void setEntity(String entity);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getAttribute();

	/**
	 * TODO: Description
	 * 
	 * @param attribute
	 */
	void setAttribute(String attribute);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getWhere();

	/**
	 * TODO: Description
	 * 
	 * @param where
	 */
	void setWhere(String where);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getProcess();

	/**
	 * TODO: Description
	 * 
	 * @param process
	 */
	void setProcess(String process);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getTask();

	/**
	 * TODO: Description
	 * 
	 * @param task
	 */
	void setTask(String task);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getLabel();

	/**
	 * TODO: Description
	 * 
	 * @param label
	 */
	void setLabel(String label);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	String getInstructions();

	/**
	 * TODO: Description
	 * 
	 * @param instructions
	 */
	void setInstructions(String instructions);

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