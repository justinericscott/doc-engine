/**TODO: License
 */
package com.github.justinericscott.docengine.util;

import static com.github.justinericscott.docengine.util.Utils.Constants.*;
import static com.github.justinericscott.docengine.util.TestUtils.TestConstants.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.TreeSet;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.justinericscott.docengine.models.Content;
import com.github.justinericscott.docengine.models.Content.Clause;
import com.github.justinericscott.docengine.models.Content.Document;
import com.github.justinericscott.docengine.models.Content.Paragraph;
import com.github.justinericscott.docengine.models.Content.Section;
import com.github.justinericscott.docengine.models.TokenDefinition;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@SpringBootTest(properties = { "server.port=8087" }, webEnvironment = DEFINED_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AbstractTest {
	@Qualifier(AUTOWIRE_QUALIFIER_ORM)
	@Autowired
	private DataSource _ds;

	@Test
	public void a_test() {

	}

	protected final Content makeTestContent() {
		final Content content = new Content(nextContentCode(), TEST_BODY_CONTENT);
		assertTrue(content.isValid());
		return content;
	}

	protected final Collection<Content> makeTestContents(final int count) {
		final Collection<Content> contents = new TreeSet<Content>();
		for (int i = 0; i < count; i++) {
			contents.add(makeTestContent());
		}
		return contents;
	}

	protected final Document makeTestDocument() {
		final Document document = new Document(nextDocumentCode(), TEST_BODY_CONTENT);
		assertTrue(document.isValid());
		return document;
	}

	protected final Collection<Document> makeTestDocuments(final int count) {
		final Collection<Document> documents = new TreeSet<Document>();
		for (int i = 0; i < count; i++) {
			documents.add(makeTestDocument());
		}
		return documents;
	}

	protected final Section makeTestSection() {
		final Section section = new Section(nextSectionCode(), TEST_BODY_CONTENT);
		assertTrue(section.isValid());
		return section;
	}

	protected final Collection<Section> makeTestSections(final int count) {
		final Collection<Section> sections = new TreeSet<Section>();
		for (int i = 0; i < count; i++) {
			sections.add(makeTestSection());
		}
		return sections;
	}

	protected final Clause makeTestClause() {
		final Clause clause = new Clause(nextClauseCode(), TEST_BODY_CONTENT);
		assertTrue(clause.isValid());
		return clause;
	}

	protected final Collection<Clause> makeTestClauses(final int count) {
		final Collection<Clause> clauses = new TreeSet<Clause>();
		for (int i = 0; i < count; i++) {
			clauses.add(makeTestClause());
		}
		return clauses;
	}

	protected final Paragraph makeTestParagraph() {
		final Paragraph paragraph = new Paragraph(nextParagraphCode(), TEST_BODY_CONTENT);
		assertTrue(paragraph.isValid());
		return paragraph;
	}

	protected final Collection<Paragraph> makeTestParagraphs(final int count) {
		final Collection<Paragraph> paragraphs = new TreeSet<Paragraph>();
		for (int i = 0; i < count; i++) {
			paragraphs.add(makeTestParagraph());
		}
		return paragraphs;
	}

	protected final Document makeTestDocumentComplete() {
		final Document document = makeTestDocument();

		final Section section1 = makeTestSection();
		document.addSection(section1);
		section1.setDocument(document);

		final Clause clause11 = makeTestClause();
		section1.addClause(clause11);
		clause11.setSection(section1);

		final Paragraph paragraph111 = makeTestParagraph();
		clause11.addParagraph(paragraph111);
		paragraph111.setClause(clause11);

		final Paragraph paragraph112 = makeTestParagraph();
		clause11.addParagraph(paragraph112);
		paragraph112.setClause(clause11);

		final Paragraph paragraph113 = makeTestParagraph();
		clause11.addParagraph(paragraph113);
		paragraph113.setClause(clause11);

		final Clause clause12 = makeTestClause();
		section1.addClause(clause12);
		clause12.setSection(section1);

		final Paragraph paragraph121 = makeTestParagraph();
		clause12.addParagraph(paragraph121);
		paragraph121.setClause(clause12);

		final Paragraph paragraph122 = makeTestParagraph();
		clause12.addParagraph(paragraph122);
		paragraph122.setClause(clause12);

		final Paragraph paragraph123 = makeTestParagraph();
		clause12.addParagraph(paragraph123);
		paragraph123.setClause(clause12);

		final Clause clause13 = makeTestClause();
		section1.addClause(clause13);
		clause13.setSection(section1);

		final Paragraph paragraph131 = makeTestParagraph();
		clause13.addParagraph(paragraph131);
		paragraph131.setClause(clause13);

		final Paragraph paragraph132 = makeTestParagraph();
		clause13.addParagraph(paragraph132);
		paragraph132.setClause(clause13);

		final Paragraph paragraph133 = makeTestParagraph();
		clause13.addParagraph(paragraph133);
		paragraph133.setClause(clause13);

		final Section section2 = makeTestSection();
		document.addSection(section2);
		section2.setDocument(document);

		final Clause clause21 = makeTestClause();
		section2.addClause(clause21);
		clause21.setSection(section2);

		final Paragraph paragraph211 = makeTestParagraph();
		clause21.addParagraph(paragraph211);
		paragraph211.setClause(clause21);

		final Section section3 = makeTestSection();
		document.addSection(section3);
		section3.setDocument(document);

		final Clause clause31 = makeTestClause();
		section3.addClause(clause31);
		clause31.setSection(section3);

		final Paragraph paragraph311 = makeTestParagraph();
		clause31.addParagraph(paragraph311);
		paragraph311.setClause(clause31);

		return document;
	}

	protected final TokenDefinition makeTestToken() {
		final TokenDefinition token = new TokenDefinition(nextTokenCode(), "TEST_TOKEN_NAME");
		token.setEntity("TR_PROJECT");
		token.setAttribute("PROJECT_NBR");
		token.setWhere("PROJECT_NBR = ?");
		return token;
	}

	protected final Collection<TokenDefinition> makeTestTokens(final int count) {
		final Collection<TokenDefinition> tokens = new TreeSet<TokenDefinition>();
		for (int i = 0; i < count; i++) {
			tokens.add(makeTestToken());
		}
		return tokens;
	}

	private final String nextContentCode() {
		return TEST_CODE_PREFIX_CONTENT + uuid();
	}

	private final String nextDocumentCode() {
		return TEST_CODE_PREFIX_DOCUMENT + uuid();
	}

	private final String nextSectionCode() {
		return TEST_CODE_PREFIX_SECTION + uuid();
	}

	private final String nextClauseCode() {
		return TEST_CODE_PREFIX_CLAUSE + uuid();
	}

	private final String nextParagraphCode() {
		return TEST_CODE_PREFIX_PARAGRAPH + uuid();
	}

	private final String nextTokenCode() {
		return TEST_CODE_PREFIX_TOKEN + uuid();
	}

	protected final static String uuid() {
		return UUID.randomUUID().toString();
	}

//	public static class TestConstants {
//
//		private TestConstants() {
//			// Do not instantiate
//		}
//
//		public static final String FILE_SYS_TEMP_DIR = "java.io.tmpdir";
//
//		public static final String TEST_BODY_CONTENT = "TEST BODY";
//
//		public static final Class<?> TEST_CLASS_ANNOTATED = ExampleExcelType.class;
//		public static final Class<?> TEST_CLASS_CUSTOM_LABELS = ExampleExcelTypeWithCustomLabels.class;
//		public static final Class<?> TEST_CLASS_CUSTOM_LABELS_ORDERED = ExampleExcelTypeWithCustomLabelsOrdered.class;
//		public static final Class<?> TEST_CLASS_CUSTOM_LABELS_ORDERED_REVERSED = ExampleExcelTypeWithCustomLabelsOrderedReversed.class;
//		public static final Class<?> TEST_CLASS_CUSTOM_LABELS_ORDERED_UNORDERED = ExampleExcelTypeWithCustomLabelsOrderedUnordered.class;
//		public static final Class<?> TEST_CLASS_NO_ANNOTATION = NoAnnotationType.class;
//
//		public static final String TEST_CODE_DOC_TYPE_L201 = "L201";
//		public static final String TEST_CODE_DOC_TYPE_R101 = "R101";
//
//		public static final String TEST_CODE_PREFIX_CLAUSE = "TEST_CLAUSE_CODE_";
//		public static final String TEST_CODE_PREFIX_CONTENT = "TEST_CONTENT_CODE_";
//		public static final String TEST_CODE_PREFIX_DOCUMENT = "TEST_DOCUMENT_CODE_";
//		public static final String TEST_CODE_PREFIX_PARAGRAPH = "TEST_PARAGRAPH_CODE_";
//		public static final String TEST_CODE_PREFIX_SECTION = "TEST_SECTION_CODE_";
//		public static final String TEST_CODE_PREFIX_TOKEN = "TEST_TOKEN_CODE_";
//
//		public static final String TEST_CSS_VALUE_CLAUSE = read(get("css/clause.css"));
//		public static final String TEST_CSS_VALUE_PARAGRAPH = read(get("css/paragraph.css"));
//		public static final String TEST_CSS_VALUE_SECTION = read(get("css/section.css"));
//
//		public static final Collection<ExampleExcelTypeWithCustomLabelsOrdered> TEST_DATA = createTestData();
//		public static final long TEST_DATA_LIMIT = 100L;
//		public static final String TEST_DATA_PROJECT_ID = "SIMPH00501";
//		
//		public static final String TEST_EXP_SECTION_NO_CLAUSES = read(
//				get("models/SectionExpectedOutput_NoClauses.html"));
//		public static final String TEST_EXP_SECTION_TWO_CLAUSES_NO_PARAGRAPHS = read(
//				get("models/SectionExpectedOutput_TwoClausesNoParagraphs.html"));
//		public static final String TEST_EXP_SECTION_TWO_CLAUSES_TWO_PARAGRAHS_EACH = read(
//				get("models/SectionExpectedOutput_TwoClausesTwoParagraphsEach.html"));
//		public static final String TEST_EXP_SECTION_OL_ONE_LEVEL = read(
//				get("models/SectionExpectedOutput_OrderedList_OneLevel.html"));
//		public static final String TEST_EXP_SECTION_OL_TWO_LEVEL = read(
//				get("models/SectionExpectedOutput_OrderedList_TwoLevel.html"));
//		public static final String TEST_EXP_SECTION_OL_VARY_LEVELS = read(
//				get("models/SectionExpectedOutput_OrderedList_VariableLevels.html"));
//		public static final String TEST_EXP_CLAUSE_NO_PARAGRAPHS = read(
//				get("models/ClauseExpectedOutput_NoParagraphs.html"));
//		public static final String TEST_EXP_CLAUSE_TWO_PARAGRAPHS = read(
//				get("models/ClauseExpectedOutput_TwoParagraphs.html"));
//		public static final String TEST_EXP_CLAUSE_OL_ONE_LEVEL = read(
//				get("models/ClauseExpectedOutput_OrderedList_OneLevel.html"));
//		public static final String TEST_EXP_CLAUSE_OL_TWO_LEVEL = read(
//				get("models/ClauseExpectedOutput_OrderedList_TwoLevel.html"));
//		public static final String TEST_EXP_CLAUSE_OL_VARY_LEVELS = read(
//				get("models/ClauseExpectedOutput_OrderedList_VariableLevels.html"));
//		public static final String TEST_EXP_PARAGRAPH_NO_SUBS = read(get("models/ParagraphExpectedOutput_NoSubs.html"));
//		public static final String TEST_EXP_PARAGRAPH_OL_ONE_LEVEL = read(
//				get("models/ParagraphExpectedOutput_OrderedList_OneLevel.html"));
//		public static final String TEST_EXP_PARAGRAPH_OL_TWO_LEVEL = read(
//				get("models/ParagraphExpectedOutput_OrderedList_TwoLevel.html"));
//		public static final String TEST_EXP_PARAGRAPH_OL_VARY_LEVELS = read(
//				get("models/ParagraphExpectedOutput_OrderedList_VariableLevels.html"));
//
//		public static final String TEST_FIELD_NAME_ID = "id";
//		public static final String TEST_FIELD_NAME_NAME = "name";
//		public static final String TEST_FIELD_NAME_DESCRIPTION = "description";
//		public static final String TEST_FIELD_NAME_POSITIVE = "positive";
//		public static final String[] TEST_FIELD_NAMES = { TEST_FIELD_NAME_ID, TEST_FIELD_NAME_NAME,
//				TEST_FIELD_NAME_DESCRIPTION, TEST_FIELD_NAME_POSITIVE };
//		public static final String[] TEST_FIELD_LABELS = { "Identification", "Name", "Description", "Positive" };
//		public static final Object[] TEST_FIELD_VALUES = { 111L, "Test Name", "Test Description", new Boolean(true) };
//		
//		public static final String TEST_FILE_EXPORT = "target/content-export.xlsx";
//		public static final String TEST_FILE_IMPORT = "ix/content-import.xlsx";
//		public static final String TEST_FILE_NAME_READ = "ix/test-data-read.xlsx";
//		public static final String TEST_FILE_NAME_WRITE = "target/test-data-write.xlsx";
//
//		public static final String TEST_PROJECT_ID_PREFIX = "TEST_PROJECT_ID_";
//		public static final String TEST_PROJECT_ID_VALUE = "SIMPH00501";
//
//		public static final String TEST_SHEET_NAME = "Excel Example Type";
//	}
}