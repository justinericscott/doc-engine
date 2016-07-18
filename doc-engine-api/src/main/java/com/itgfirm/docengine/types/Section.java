/**
 * TODO: License
 */
package com.itgfirm.docengine.types;

import java.util.List;

/**
 * @author Justin Scott
 * 
 *         Section Data Model
 */
public interface Section extends Content {

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	AdvancedDocument getDocument();

	/**
	 * TODO: Description
	 * 
	 * @param document
	 */
	void setDocument(AdvancedDocument document);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	List<Clause> getClauses();

	/**
	 * TODO: Description
	 * 
	 * @param clause
	 */
	void addClause(Clause clause);

	/**
	 * TODO: Description
	 * 
	 * @param clauses
	 */
	void addClauses(List<Clause> clauses);

	/**
	 * TODO: Description
	 * 
	 * @param clauses
	 */
	void setClauses(List<Clause> clauses);
}