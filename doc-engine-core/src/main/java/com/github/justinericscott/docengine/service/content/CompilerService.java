/**
 * TODO: License
 */
package com.github.justinericscott.docengine.service.content;

import com.github.justinericscott.docengine.types.DocumentInstanceJpaImpl;

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
	String compileDocument(DocumentInstanceJpaImpl document);

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