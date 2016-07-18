/**
 * TODO: License
 */
package com.itgfirm.docengine.types;

import java.util.List;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public interface AdvancedDocument extends Content {

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	List<Section> getSections();

	/**
	 * TODO: Description
	 * 
	 * @param section
	 */
	void addSection(Section section);

	/**
	 * TODO: Description
	 * 
	 * @param sections
	 */
	void addSections(List<Section> sections);

	/**
	 * TODO: Description
	 * 
	 * @param section
	 */
	void setSections(List<Section> section);
}