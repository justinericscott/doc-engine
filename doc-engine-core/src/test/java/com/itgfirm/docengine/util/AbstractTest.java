/**TODO: License
 */
package com.itgfirm.docengine.util;

import static com.itgfirm.docengine.util.TestUtils.*;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.TreeSet;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.itgfirm.docengine.types.jpa.AdvancedDocumentJpaImpl;
import com.itgfirm.docengine.types.jpa.ClauseJpaImpl;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.types.jpa.ParagraphJpaImpl;
import com.itgfirm.docengine.types.jpa.SectionJpaImpl;
import com.itgfirm.docengine.types.jpa.TokenDefinitionJpaImpl;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Ignore
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class AbstractTest {

	protected final ContentJpaImpl makeTestContent(final Integer seed) {
		final ContentJpaImpl content = new ContentJpaImpl(TestUtils.getRandomTestString(seed), "TEST_BODY");
		assertTrue(content.isValid());
		return content;
	}

	protected final Iterable<ContentJpaImpl> makeTestContents(final Integer count) {
		final Collection<ContentJpaImpl> contents = new TreeSet<ContentJpaImpl>();
		for (int i = 0; i < count; i++) {
			contents.add(makeTestContent((i + count) * 2));
		}
		return contents;
	}

	protected final AdvancedDocumentJpaImpl makeTestDocument(final int seed) {
		final AdvancedDocumentJpaImpl document = new AdvancedDocumentJpaImpl("TEST_DOC_" + getRandomTestString(seed * 4),
				"TEST_BODY");

		final SectionJpaImpl section1 = new SectionJpaImpl("TEST_SECTION_" + getRandomTestString(seed * 1), "TEST_BODY");
		document.addSection(section1);

		final ClauseJpaImpl clause11 = new ClauseJpaImpl("TEST_CLAUSE_" + getRandomTestString(seed * 11), "TEST_BODY");
		section1.addClause(clause11);

		final ParagraphJpaImpl paragraph111 = new ParagraphJpaImpl("TEST_PARA_" + getRandomTestString(seed * 111),
				"TEST_BODY");
		clause11.addParagraph(paragraph111);

		final ParagraphJpaImpl paragraph112 = new ParagraphJpaImpl("TEST_PARA_" + getRandomTestString(seed * 112),
				"TEST_BODY");
		clause11.addParagraph(paragraph112);

		final ParagraphJpaImpl paragraph113 = new ParagraphJpaImpl("TEST_PARA_" + getRandomTestString(seed * 113),
				"TEST_BODY");
		clause11.addParagraph(paragraph113);

		final ClauseJpaImpl clause12 = new ClauseJpaImpl("TEST_CLAUSE_" + getRandomTestString(seed * 12), "TEST_BODY");
		section1.addClause(clause12);

		final ParagraphJpaImpl paragraph121 = new ParagraphJpaImpl("TEST_PARA_" + getRandomTestString(seed * 121),
				"TEST_BODY");
		clause12.addParagraph(paragraph121);

		final ParagraphJpaImpl paragraph122 = new ParagraphJpaImpl("TEST_PARA_" + getRandomTestString(seed * 122),
				"TEST_BODY");
		clause12.addParagraph(paragraph122);

		final ParagraphJpaImpl paragraph123 = new ParagraphJpaImpl("TEST_PARA_" + getRandomTestString(seed * 123),
				"TEST_BODY");
		clause12.addParagraph(paragraph123);

		final ClauseJpaImpl clause13 = new ClauseJpaImpl("TEST_CLAUSE_" + getRandomTestString(seed * 13), "TEST_BODY");
		section1.addClause(clause13);

		final ParagraphJpaImpl paragraph131 = new ParagraphJpaImpl("TEST_PARA_" + getRandomTestString(seed * 131),
				"TEST_BODY");
		clause13.addParagraph(paragraph131);

		final ParagraphJpaImpl paragraph132 = new ParagraphJpaImpl("TEST_PARA_" + getRandomTestString(seed * 132),
				"TEST_BODY");
		clause13.addParagraph(paragraph132);

		final ParagraphJpaImpl paragraph133 = new ParagraphJpaImpl("TEST_PARA_" + getRandomTestString(seed * 133),
				"TEST_BODY");
		clause13.addParagraph(paragraph133);

		final SectionJpaImpl section2 = new SectionJpaImpl("TEST_PARA_" + getRandomTestString(seed * 2), "TEST_BODY");
		document.addSection(section2);

		final ClauseJpaImpl clause21 = new ClauseJpaImpl("TEST_CLAUSE_" + getRandomTestString(seed * 21), "TEST_BODY");
		section2.addClause(clause21);

		final ParagraphJpaImpl paragraph211 = new ParagraphJpaImpl("TEST_PARA_" + getRandomTestString(seed * 211),
				"TEST_BODY");
		clause21.addParagraph(paragraph211);

		final SectionJpaImpl section3 = new SectionJpaImpl("TEST_SECTION_" + getRandomTestString(seed * 3), "TEST_BODY");
		document.addSection(section3);

		final ClauseJpaImpl clause31 = new ClauseJpaImpl("TEST_CLAUSE_" + getRandomTestString(seed * 31), "TEST_BODY");
		section3.addClause(clause31);

		final ParagraphJpaImpl paragraph311 = new ParagraphJpaImpl("TEST_PARA_" + getRandomTestString(seed * 311),
				"TEST_BODY");
		clause31.addParagraph(paragraph311);

		return document;
	}

	protected final TokenDefinitionJpaImpl makeTestTokenDefinition(final Integer seed) {
		final TokenDefinitionJpaImpl token = new TokenDefinitionJpaImpl(TestUtils.getRandomTestString(seed),
				"TEST_TOKEN_NAME");
		token.setEntity("TR_PROJECT");
		token.setAttribute("PROJECT_NBR");
		token.setWhere("PROJECT_NBR = ?");
		return token;
	}

	protected final void validate(final TokenDefinitionJpaImpl token) {
		assertTrue(token.isValid(true));
	}
}