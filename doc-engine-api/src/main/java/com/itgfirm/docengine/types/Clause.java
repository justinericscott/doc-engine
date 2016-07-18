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
public interface Clause extends Content {

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	Section getSection();

	/**
	 * TODO: Description
	 * 
	 * @param section
	 */
	void setSection(Section section);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	List<Paragraph> getParagraphs();

	/**
	 * TODO: Description
	 * 
	 * @param paragraph
	 */
	void addParagraph(Paragraph paragraph);

	/**
	 * TODO: Description
	 * 
	 * @param paragraphs
	 */
	void addParagraphs(List<Paragraph> paragraphs);

	/**
	 * TODO: Description
	 * 
	 * @param paragraphs
	 */
	void setParagraphs(List<Paragraph> paragraphs);
}