/**
 * TODO: License
 */
package com.itgfirm.docengine.compiler;

import com.itgfirm.docengine.types.AdvancedDocumentInstanceJpaImpl;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public interface CompilerService {

	/**
	 * TODO: Description
	 * 
	 * @param doc
	 * @return {@link String}
	 */
	String compileDocument(AdvancedDocumentInstanceJpaImpl document);

	/**
	 * TODO: Description
	 * 
	 * @param section
	 * @return {@link String}
	 */
	// public String compileSection(SectionInstance section);

	/**
	 * TODO: Description
	 * 
	 * @param clause
	 * @return {@link String}
	 */
	// public String compileClause(ClauseInstance clause);

	/**
	 * TODO: Description
	 * 
	 * @param paragraph
	 * @return {@link String}
	 */
	// public String compileParagraph(ParagraphInstance paragraph);
}