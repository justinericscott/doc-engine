/**
 * TODO: License
 */
package com.itgfirm.docengine.types;

import java.util.List;

/**
 * @author Justin Scott
 * 
 *         Section Instance Data Model
 */
public interface SectionInstance extends Instance {

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	AdvancedDocumentInstance getDocument();

	/**
	 * TODO: Description
	 * 
	 * @param document
	 */
	void setDocument(AdvancedDocumentInstance document);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	List<ClauseInstance> getClauses();

	/**
	 * TODO: Description
	 * 
	 * @param clauses
	 */
	void instantiateClauses(List<Clause> clauses);

	/**
	 * TODO: Description
	 * 
	 * @param clause
	 */
	void addClause(ClauseInstance clause);

	/**
	 * TODO: Description
	 * 
	 * @param clauses
	 */
	void addClauses(List<ClauseInstance> clauses);

	/**
	 * TODO: Description
	 * 
	 * @param clauses
	 */
	void setClauses(List<ClauseInstance> clauses);
}