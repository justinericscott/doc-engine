/**
 * TODO: License
 */
package com.itgfirm.docengine.types;

import java.util.List;

/**
 * @author Justin Scott
 * 
 *         Document Instance Data Model
 */
public interface AdvancedDocumentInstance extends Instance {

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	List<SectionInstance> getSections();

	/**
	 * TODO: Description
	 * 
	 * @param sections
	 */
	void instantiateSections(List<Section> sections);

	/**
	 * TODO: Description
	 * 
	 * @param section
	 */
	void addSection(SectionInstance section);

	/**
	 * TODO: Description
	 * 
	 * @param sections
	 */
	void addSections(List<SectionInstance> sections);

	/**
	 * TODO: Description
	 * 
	 * @param section
	 */
	void setSections(List<SectionInstance> section);
}