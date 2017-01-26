/**TODO: License
 */
package com.itgfirm.docengine.util;

import static com.itgfirm.docengine.DocEngine.Constants.*;
import static com.itgfirm.docengine.util.TestUtils.createTestData;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.TreeSet;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.itgfirm.docengine.service.ix.types.ExampleExcelType;
import com.itgfirm.docengine.service.ix.types.ExampleExcelTypeWithCustomLabels;
import com.itgfirm.docengine.service.ix.types.ExampleExcelTypeWithCustomLabelsOrdered;
import com.itgfirm.docengine.service.ix.types.ExampleExcelTypeWithCustomLabelsOrderedReversed;
import com.itgfirm.docengine.service.ix.types.ExampleExcelTypeWithCustomLabelsOrderedUnordered;
import com.itgfirm.docengine.service.ix.types.NoAnnotationType;
import com.itgfirm.docengine.types.ClauseJpaImpl;
import com.itgfirm.docengine.types.ContentJpaImpl;
import com.itgfirm.docengine.types.DocumentJpaImpl;
import com.itgfirm.docengine.types.ParagraphJpaImpl;
import com.itgfirm.docengine.types.SectionJpaImpl;
import com.itgfirm.docengine.types.TokenDefinitionJpaImpl;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Ignore
@SpringBootTest(properties = { "server.port=8087" }, webEnvironment = DEFINED_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class AbstractTest {
	@Qualifier(AUTOWIRE_QUALIFIER_ORM)
	@Autowired
	private DataSource _ds;

	protected static final String TEST_CONTENT_CODE_PREFIX = "TEST_CONTENT_CODE_";
	protected static final String TEST_PARAGRAPH_CODE_PREFIX = "TEST_PARAGRAPH_CODE_";
	protected static final String TEST_CLAUSE_CODE_PREFIX = "TEST_CLAUSE_CODE_";
	protected static final String TEST_SECTION_CODE_PREFIX = "TEST_SECTION_CODE_";
	protected static final String TEST_DOCUMENT_CODE_PREFIX = "TEST_DOCUMENT_CODE_";
	protected static final String TEST_CONTENT_BODY = "TEST BODY";
	protected static final String TEST_PROJECT_ID_PREFIX = "TEST_PROJECT_ID_";
	protected static final String TEST_PROJECT_ID_VALUE = "SIMPH00501";
	protected static final String TEST_TOKEN_CODE_PREFIX = "TEST_TOKEN_CODE_";

//	protected int index = 0;

	protected final ContentJpaImpl makeTestContent() {
		final ContentJpaImpl content = new ContentJpaImpl(nextContentCode(), TEST_CONTENT_BODY);
		assertTrue(content.isValid());
		return content;
	}

	protected final Collection<ContentJpaImpl> makeTestContents(final int count) {
		final Collection<ContentJpaImpl> contents = new TreeSet<ContentJpaImpl>();
		for (int i = 0; i < count; i++) {
			contents.add(makeTestContent());
		}
		return contents;
	}

	protected final DocumentJpaImpl makeTestDocument() {
		final DocumentJpaImpl document = new DocumentJpaImpl(nextDocumentCode(), TEST_CONTENT_BODY);
		assertTrue(document.isValid());
		return document;
	}

	protected final Collection<DocumentJpaImpl> makeTestDocuments(final int count) {
		final Collection<DocumentJpaImpl> documents = new TreeSet<DocumentJpaImpl>();
		for (int i = 0; i < count; i++) {
			documents.add(makeTestDocument());
		}
		return documents;
	}

	protected final SectionJpaImpl makeTestSection() {
		final SectionJpaImpl section = new SectionJpaImpl(nextSectionCode(), TEST_CONTENT_BODY);
		assertTrue(section.isValid());
		return section;
	}

	protected final Collection<SectionJpaImpl> makeTestSections(final int count) {
		final Collection<SectionJpaImpl> sections = new TreeSet<SectionJpaImpl>();
		for (int i = 0; i < count; i++) {
			sections.add(makeTestSection());
		}
		return sections;
	}

	protected final ClauseJpaImpl makeTestClause() {
		final ClauseJpaImpl clause = new ClauseJpaImpl(nextClauseCode(), TEST_CONTENT_BODY);
		assertTrue(clause.isValid());
		return clause;
	}

	protected final Collection<ClauseJpaImpl> makeTestClauses(final int count) {
		final Collection<ClauseJpaImpl> clauses = new TreeSet<ClauseJpaImpl>();
		for (int i = 0; i < count; i++) {
			clauses.add(makeTestClause());
		}
		return clauses;
	}

	protected final ParagraphJpaImpl makeTestParagraph() {
		final ParagraphJpaImpl paragraph = new ParagraphJpaImpl(nextParagraphCode(), TEST_CONTENT_BODY);
		assertTrue(paragraph.isValid());
		return paragraph;
	}

	protected final Collection<ParagraphJpaImpl> makeTestParagraphs(final int count) {
		final Collection<ParagraphJpaImpl> paragraphs = new TreeSet<ParagraphJpaImpl>();
		for (int i = 0; i < count; i++) {
			paragraphs.add(makeTestParagraph());
		}
		return paragraphs;
	}

	protected final DocumentJpaImpl makeTestDocumentComplete() {
		final DocumentJpaImpl document = makeTestDocument();

		final SectionJpaImpl section1 = makeTestSection();
		document.addSection(section1);
		section1.setDocument(document);

		final ClauseJpaImpl clause11 = makeTestClause();
		section1.addClause(clause11);
		clause11.setSection(section1);

		final ParagraphJpaImpl paragraph111 = makeTestParagraph();
		clause11.addParagraph(paragraph111);
		paragraph111.setClause(clause11);

		final ParagraphJpaImpl paragraph112 = makeTestParagraph();
		clause11.addParagraph(paragraph112);
		paragraph112.setClause(clause11);

		final ParagraphJpaImpl paragraph113 = makeTestParagraph();
		clause11.addParagraph(paragraph113);
		paragraph113.setClause(clause11);

		final ClauseJpaImpl clause12 = makeTestClause();
		section1.addClause(clause12);
		clause12.setSection(section1);

		final ParagraphJpaImpl paragraph121 = makeTestParagraph();
		clause12.addParagraph(paragraph121);
		paragraph121.setClause(clause12);

		final ParagraphJpaImpl paragraph122 = makeTestParagraph();
		clause12.addParagraph(paragraph122);
		paragraph122.setClause(clause12);

		final ParagraphJpaImpl paragraph123 = makeTestParagraph();
		clause12.addParagraph(paragraph123);
		paragraph123.setClause(clause12);

		final ClauseJpaImpl clause13 = makeTestClause();
		section1.addClause(clause13);
		clause13.setSection(section1);

		final ParagraphJpaImpl paragraph131 = makeTestParagraph();
		clause13.addParagraph(paragraph131);
		paragraph131.setClause(clause13);

		final ParagraphJpaImpl paragraph132 = makeTestParagraph();
		clause13.addParagraph(paragraph132);
		paragraph132.setClause(clause13);

		final ParagraphJpaImpl paragraph133 = makeTestParagraph();
		clause13.addParagraph(paragraph133);
		paragraph133.setClause(clause13);

		final SectionJpaImpl section2 = makeTestSection();
		document.addSection(section2);
		section2.setDocument(document);

		final ClauseJpaImpl clause21 = makeTestClause();
		section2.addClause(clause21);
		clause21.setSection(section2);

		final ParagraphJpaImpl paragraph211 = makeTestParagraph();
		clause21.addParagraph(paragraph211);
		paragraph211.setClause(clause21);

		final SectionJpaImpl section3 = makeTestSection();
		document.addSection(section3);
		section3.setDocument(document);

		final ClauseJpaImpl clause31 = makeTestClause();
		section3.addClause(clause31);
		clause31.setSection(section3);

		final ParagraphJpaImpl paragraph311 = makeTestParagraph();
		clause31.addParagraph(paragraph311);
		paragraph311.setClause(clause31);

		return document;
	}

	protected final TokenDefinitionJpaImpl makeTestToken() {
		final TokenDefinitionJpaImpl token = new TokenDefinitionJpaImpl(nextTokenCode(), "TEST_TOKEN_NAME");
		token.setEntity("TR_PROJECT");
		token.setAttribute("PROJECT_NBR");
		token.setWhere("PROJECT_NBR = ?");
		return token;
	}

	protected final Collection<TokenDefinitionJpaImpl> makeTestTokens(final int count) {
		final Collection<TokenDefinitionJpaImpl> tokens = new TreeSet<TokenDefinitionJpaImpl>();
		for (int i = 0; i < count; i++) {
			tokens.add(makeTestToken());
		}
		return tokens;
	}

	private final String nextContentCode() {
		return TEST_CONTENT_CODE_PREFIX + uuid();
	}

	private final String nextDocumentCode() {
		return TEST_DOCUMENT_CODE_PREFIX + uuid();
	}

	private final String nextSectionCode() {
		return TEST_SECTION_CODE_PREFIX + uuid();
	}

	private final String nextClauseCode() {
		return TEST_CLAUSE_CODE_PREFIX + uuid();
	}

	private final String nextParagraphCode() {
		return TEST_PARAGRAPH_CODE_PREFIX + uuid();
	}

	private final String nextTokenCode() {
		return TEST_TOKEN_CODE_PREFIX + uuid();
	}
	
	protected final static String uuid() {
		return UUID.randomUUID().toString();
	}

	public static class TestConstants {

		private TestConstants() {
			// Do not instantiate
		}

		public static final String FILE_SYS_TEMP_DIR = "java.io.tmpdir";

		public static final Class<?> TEST_CLASS_ANNOTATED = ExampleExcelType.class;
		public static final Class<?> TEST_CLASS_CUSTOM_LABELS = ExampleExcelTypeWithCustomLabels.class;
		public static final Class<?> TEST_CLASS_CUSTOM_LABELS_ORDERED = ExampleExcelTypeWithCustomLabelsOrdered.class;
		public static final Class<?> TEST_CLASS_CUSTOM_LABELS_ORDERED_REVERSED = ExampleExcelTypeWithCustomLabelsOrderedReversed.class;
		public static final Class<?> TEST_CLASS_CUSTOM_LABELS_ORDERED_UNORDERED = ExampleExcelTypeWithCustomLabelsOrderedUnordered.class;
		public static final Class<?> TEST_CLASS_NO_ANNOTATION = NoAnnotationType.class;

		public static final String TEST_CODE_DOC_TYPE_L201 = "L201";
		public static final String TEST_CODE_DOC_TYPE_R101 = "R101";

		// public static final String TEST_CODE_PREFIX = "TEST_CONTENT";

		public static final String TEST_FIELD_NAME_ID = "id";
		public static final String TEST_FIELD_NAME_NAME = "name";
		public static final String TEST_FIELD_NAME_DESCRIPTION = "description";
		public static final String TEST_FIELD_NAME_POSITIVE = "positive";
		public static final String[] TEST_FIELD_NAMES = { TEST_FIELD_NAME_ID, TEST_FIELD_NAME_NAME,
				TEST_FIELD_NAME_DESCRIPTION, TEST_FIELD_NAME_POSITIVE };

		public static final String TEST_FILE_NAME_READ = "test-data-read.xlsx";
		public static final String TEST_FILE_NAME_WRITE = "target/test-data-write.xlsx";

		public static final Collection<ExampleExcelTypeWithCustomLabelsOrdered> TEST_DATA = createTestData();
		public static final String[] TEST_DATA_FIELD_LABELS = { "Identification", "Name", "Description", "Positive" };
		public static final String[] TEST_DATA_FIELD_NAMES = { "id", "name", "description", "positive" };
		public static final Object[] TEST_VALUES = { 111L, "Test Name", "Test Description", new Boolean(true) };

		public static final long TEST_DATA_LIMIT = 100L;
		public static final String TEST_DATA_PROJECT_ID = "SIMPH00501";

		public static final String TEST_SHEET_NAME = "Excel Example Type";
	}
}