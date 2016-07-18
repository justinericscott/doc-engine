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
public interface ClauseInstance extends Instance {

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	SectionInstance getSection();

	/**
	 * TODO: Description
	 * 
	 * @param section
	 */
	void setSection(SectionInstance section);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	List<ParagraphInstance> getParagraphs();

	/**
	 * TODO: Description
	 * 
	 * @param paragraphs
	 */
	void instantiateParagraphs(List<Paragraph> paragraphs);

	/**
	 * TODO: Description
	 * 
	 * @param paragraph
	 */
	void addParagraph(ParagraphInstance paragraph);

	/**
	 * TODO: Description
	 * 
	 * @param paragraphs
	 */
	void addParagraphs(List<ParagraphInstance> paragraphs);

	/**
	 * TODO: Description
	 * 
	 * @param paragraphs
	 */
	void setParagraphs(List<ParagraphInstance> paragraphs);
}