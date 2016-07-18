package com.itgfirm.docengine.content;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.LazyInitializationException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;

import com.itgfirm.docengine.DocEngine;
import com.itgfirm.docengine.data.DataConstants;
import com.itgfirm.docengine.types.AdvancedDocument;
import com.itgfirm.docengine.types.Clause;
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.Paragraph;
import com.itgfirm.docengine.types.Section;
import com.itgfirm.docengine.types.jpa.AdvancedDocumentJpaImpl;
import com.itgfirm.docengine.types.jpa.ClauseJpaImpl;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.types.jpa.ParagraphJpaImpl;
import com.itgfirm.docengine.types.jpa.SectionJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;
import com.itgfirm.docengine.util.TestUtils;

/**
 * @author Justin Scott 
 * 
 * Tests for the Content Repository
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringApplicationConfiguration( classes = DocEngine.class )
public class AdvancedContentRepoTest extends AbstractTest {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(AdvancedContentRepoTest.class);
	@Autowired @Qualifier("advanced")
	AdvancedContentRepo contentRepo;

	@Test
	public void aa_MergeTest() {
		// Merge 1
		Content content = createContent();
		contentRepo.delete(content);
		
		// Merge complex one at a time
		AdvancedDocument document = (AdvancedDocument) contentRepo.merge(new AdvancedDocumentJpaImpl(content,
				TestUtils.getRandomTestString(2)));
		assertNotNull(document.getId());		
		
		Section section = new SectionJpaImpl(content,
				TestUtils.getRandomTestString(3));
		document.addSection(section);
		section = (Section) contentRepo.merge(section);
		assertNotNull(section.getId());		
		
		Clause clause = new ClauseJpaImpl(content, 
				TestUtils.getRandomTestString(4));
		section.addClause(clause);
		clause = (Clause) contentRepo.merge(clause);
		assertNotNull(clause.getId());		
		
		Paragraph paragraph = new ParagraphJpaImpl(content, 
				TestUtils.getRandomTestString(5));
		clause.addParagraph(paragraph);
		paragraph = (Paragraph) contentRepo.merge(paragraph);
		assertNotNull(paragraph.getId());
		document = (AdvancedDocument) contentRepo.initialize(contentRepo.get(document.getId()));
		validate(document);
		contentRepo.delete(document);
		
		// Merge complex all at once
		document = createDocument();
		contentRepo.delete(document);		
	}

	@Test
	public void ab_MergeInvalidParamTest() {
		// Null Object
		assertNull(contentRepo.merge(null));
		// Empty Code for Content
		try {
			new ContentJpaImpl("", "TEST BODY");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getClass().equals(IllegalArgumentException.class));	
		}
		assertNull(contentRepo.merge(new ContentJpaImpl("", "TEST BODY")).getId());
		// Empty Code for Document
		assertNull(contentRepo.merge(new AdvancedDocumentJpaImpl(new ContentJpaImpl("TEST BODY"), 
				"")).getId());
		// Empty Body for Content
		assertNull(contentRepo.merge(new ContentJpaImpl(
				TestUtils.getRandomTestString(2), "")).getId());
		// Empty Body for Document
		assertNull(contentRepo.merge(new AdvancedDocumentJpaImpl(new ContentJpaImpl(""), 
				TestUtils.getRandomTestString(3))).getId());
		// Duplicate codes
		Content content = createContent();
		assertNull(contentRepo.merge(new ContentJpaImpl(
				content, content.getContentCd())).getId());
		contentRepo.delete(content);
	}

	@Test
	public void ba_GetTest() {
		// Get All
		List<Content> contents = new ArrayList<Content>();
		contents.add(createContent(1));
		contents.add(createContent(2));
		contents.add(createContent(3));
		contents.add(createContent(4));
		contents.add(createContent(5));
		AdvancedDocument doc = createDocument();
		List<Content> allContent = contentRepo.get();
		for (Content c : allContent) {
			validate(c);
		}
		validate(doc);
		for (Content c : contents) {
			contentRepo.delete(c);
		}
		contentRepo.delete(doc);

		// Get Content
		Content content = createContent();
		validate(contentRepo.get(content.getId()));
		validate(contentRepo.get(content.getContentCd()));
		contentRepo.delete(content);		
		
		// Get sub-classes, no eagerKids
		AdvancedDocument document = createDocument();
		assertTrue(AdvancedDocumentJpaImpl.class.equals(contentRepo.get(document.getId()).getClass()));
		assertTrue(AdvancedDocumentJpaImpl.class.equals(contentRepo.get(document.getContentCd()).getClass()));
		try {
			((AdvancedDocument) contentRepo.get(document.getId())).getSections().get(0);
			throw new IllegalStateException();
		} catch (LazyInitializationException e) {
			assertTrue(e.getClass().equals(LazyInitializationException.class));	
		}		

		assertTrue(SectionJpaImpl.class.equals(contentRepo.get(
				document.getSections().get(0).getId()).getClass()));
		assertTrue(SectionJpaImpl.class.equals(contentRepo.get(
				document.getSections().get(0).getContentCd()).getClass()));
		try {
			((Section) contentRepo.get(document.getSections().get(0).getId())).getClauses().get(0);
			throw new IllegalStateException();
		} catch (LazyInitializationException e) {
			assertTrue(e.getClass().equals(LazyInitializationException.class));	
		}		
		
		assertTrue(ClauseJpaImpl.class.equals(contentRepo.get(
				document.getSections().get(0).getClauses().get(0).getId()).getClass()));
		assertTrue(ClauseJpaImpl.class.equals(contentRepo.get(
				document.getSections().get(0).getClauses().get(0).getContentCd()).getClass()));
		try {
			((Clause) contentRepo.get(document.getSections().get(0).getClauses().get(0).getId())).getParagraphs().get(0);
			throw new IllegalStateException();
		} catch (LazyInitializationException e) {
			assertTrue(e.getClass().equals(LazyInitializationException.class));	
		}		
		
		assertTrue(ParagraphJpaImpl.class.equals(contentRepo.get(
				document.getSections().get(0).getClauses().get(0).getParagraphs().get(0).getId()).getClass()));
		assertTrue(ParagraphJpaImpl.class.equals(contentRepo.get(
				document.getSections().get(0).getClauses().get(0).getParagraphs().get(0).getContentCd()).getClass()));		

		// Get sub-classes, then initialize children 
		document = (AdvancedDocument) contentRepo.get(document.getId());
		document = (AdvancedDocument) contentRepo.initialize(document);
		validate(document);		
		contentRepo.delete(document);
	}

	@Test
	public void bb_GetInvalidParamTest() {
		// Null Param
		assertNull(contentRepo.get((String) null));
		// Empty Param
		assertNull(contentRepo.get(""));
		// Zero as PARAM_ID
		assertNull(contentRepo.get(0L));
		// Bad PARAM_ID
		assertNull(contentRepo.get(99999999999999999L));
		// Bad Code
		assertNull(contentRepo.get("Snicklefritz"));
	}
	
	@Test
	public void ca_GetByCodeLikeTest() {
		Content content  = createContent();
		assertNotNull(contentRepo.getByCodeLike("TEST"));
		contentRepo.delete(content);
	}

	@Test
	public void cb_GetByCodeLikeInvalidLikeTest() {
		// No Like String
		assertNull(contentRepo.getByCodeLike(null));
		// Empty Like String
		assertNull(contentRepo.getByCodeLike(""));
		// Bad Like String
		assertNull(contentRepo.getByCodeLike("%Snicklefritz%"));
	}

	@Test
	public void da_GetWithQueryTest() {
		Content content  = createContent();
		assertNotNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CODE_LIKE,
				DataConstants.PARAM_CONTENT_CD, "%TEST%"));
		contentRepo.delete(content);		
	}

	@Test
	public void db_GetWithQueryInvalidParamTest() {
		// No Value
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CODE_LIKE,
				DataConstants.PARAM_CONTENT_CD, null));
		// Empty Value
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CODE_LIKE,
				DataConstants.PARAM_CONTENT_CD, ""));
		// Bad Value
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CODE_LIKE,
				DataConstants.PARAM_CONTENT_CD, "Snicklefritz"));
		// No Param Name
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CODE_LIKE, null, "R101"));
		// Empty Param Name
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CODE_LIKE, "", "R101"));
		// Bad Param Name
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CODE_LIKE, "Snicklefritz",
				"R101"));
		// Null Query
		assertNull(contentRepo.getWithQuery(null,
				DataConstants.PARAM_CONTENT_CD, "R101"));
		// Empty Query
		assertNull(contentRepo.getWithQuery("", DataConstants.PARAM_CONTENT_CD,
				"R101%"));
		// Bad Query
		assertNull(contentRepo.getWithQuery("Snicklefritz",
				DataConstants.PARAM_CONTENT_CD, "R101"));
	}

	@Test
	public void ea_GetWithQueryMultiParamTest() {
		List<Content> contents = new ArrayList<Content>();
		contents.add(createContent(1));
		contents.add(createContent(2));
		contents.add(createContent(3));
		contents.add(createContent(4));
		contents.add(createContent(5));
		String[] paramNames = { DataConstants.PARAM_CONTENT_CD,
				DataConstants.PARAM_BODY };
		String[] values = { "%TEST%", "%BODY%" };
		assertNotNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CONTENT_CD_AND_BODY_LIKE,
				paramNames, values));	
		for (Content c : contents) {
			contentRepo.delete(c);
		}
	}

	@Test
	public void eb_GetWithQueryMultiParamInvalidParamTest() {
		String[] paramNames = { DataConstants.PARAM_CONTENT_CD,
				DataConstants.PARAM_BODY };
		String[] values = { "R101%", "%BODY%" };
		String[] noParamNames = { null, null };
		String[] noValues = { null, null };
		String[] emptyParamNames = { "", "" };
		String[] emptyValues = { "", "" };
		String[] badParamNames = { "%Snicklefritz%", "%Snicklefritz%" };
		String[] badValues = { "%Snicklefritz%", "%Snicklefritz%" };
		String[] shortParamNames = { "R101%" };
		String[] shortValues = { "%BODY%" };
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CONTENT_CD_AND_BODY_LIKE,
				paramNames, noValues));
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CONTENT_CD_AND_BODY_LIKE,
				paramNames, emptyValues));
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CONTENT_CD_AND_BODY_LIKE,
				paramNames, badValues));
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CONTENT_CD_AND_BODY_LIKE,
				paramNames, shortValues));
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CONTENT_CD_AND_BODY_LIKE,
				noParamNames, values));
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CONTENT_CD_AND_BODY_LIKE,
				emptyParamNames, values));
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CONTENT_CD_AND_BODY_LIKE,
				badParamNames, values));
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CONTENT_CD_AND_BODY_LIKE,
				shortParamNames, values));
	}

	@Test
	public void xx_DeleteTest() {
		Content content = createContent();
		assertTrue(contentRepo.delete(content));
		assertNull(contentRepo.get(content.getId()));
		createContent(1);
		createContent(2);
		createContent(3);
		createContent(4);
		createContent(5);
		contentRepo.deleteByCodeLike("TEST");
		assertNull(contentRepo.getByCodeLike("TEST"));
	}

	private Content createContent() {
		return createContent(1);
	}
	
	private Content createContent(int seed) {
		Content content = contentRepo.merge(makeTestContent(seed));
		validate(content);
		return content;		
	}
	
	private AdvancedDocument createDocument() {
		AdvancedDocument document =  (AdvancedDocument) contentRepo.merge(makeTestDocument());
		validate(document);
		return document;
	}
	protected AdvancedDocument makeTestDocument() {
		AdvancedDocument document = new AdvancedDocumentJpaImpl("TEST_DOC_" +
				TestUtils.getRandomTestString(4), "TEST_BODY");		
		
		Section section1 = new SectionJpaImpl("TEST_SECTION_" +
				TestUtils.getRandomTestString(1), "TEST_BODY");		
		document.addSection(section1);
		
		
		Clause clause11 = new ClauseJpaImpl("TEST_CLAUSE_" +
				TestUtils.getRandomTestString(11), "TEST_BODY");
		section1.addClause(clause11);		
		
		Paragraph paragraph111 = new ParagraphJpaImpl("TEST_PARA_" +
				TestUtils.getRandomTestString(111), "TEST_BODY");
		clause11.addParagraph(paragraph111);
		
		Paragraph paragraph112 = new ParagraphJpaImpl("TEST_PARA_" +
				TestUtils.getRandomTestString(112), "TEST_BODY");
		clause11.addParagraph(paragraph112);
		
		Paragraph paragraph113 = new ParagraphJpaImpl("TEST_PARA_" +
				TestUtils.getRandomTestString(113), "TEST_BODY");
		clause11.addParagraph(paragraph113);
		
		
		Clause clause12 = new ClauseJpaImpl("TEST_CLAUSE_" +
				TestUtils.getRandomTestString(12), "TEST_BODY");
		section1.addClause(clause12);		
		
		Paragraph paragraph121 = new ParagraphJpaImpl("TEST_PARA_" +
				TestUtils.getRandomTestString(121), "TEST_BODY");
		clause12.addParagraph(paragraph121);
		
		Paragraph paragraph122 = new ParagraphJpaImpl("TEST_PARA_" +
				TestUtils.getRandomTestString(122), "TEST_BODY");
		clause12.addParagraph(paragraph122);
		
		Paragraph paragraph123 = new ParagraphJpaImpl("TEST_PARA_" +
				TestUtils.getRandomTestString(123), "TEST_BODY");
		clause12.addParagraph(paragraph123);
		
		
		Clause clause13 = new ClauseJpaImpl("TEST_CLAUSE_" +
				TestUtils.getRandomTestString(13), "TEST_BODY");
		section1.addClause(clause13);		
		
		Paragraph paragraph131 = new ParagraphJpaImpl("TEST_PARA_" +
				TestUtils.getRandomTestString(131), "TEST_BODY");
		clause13.addParagraph(paragraph131);
		
		Paragraph paragraph132 = new ParagraphJpaImpl("TEST_PARA_" +
				TestUtils.getRandomTestString(132), "TEST_BODY");
		clause13.addParagraph(paragraph132);
		
		Paragraph paragraph133 = new ParagraphJpaImpl("TEST_PARA_" +
				TestUtils.getRandomTestString(133), "TEST_BODY");
		clause13.addParagraph(paragraph133);
		
		
		
		Section section2 = new SectionJpaImpl("TEST_PARA_" +
				TestUtils.getRandomTestString(2), "TEST_BODY");		
		document.addSection(section2);
		
		
		Clause clause21 = new ClauseJpaImpl("TEST_CLAUSE_" +
				TestUtils.getRandomTestString(21), "TEST_BODY");
		section2.addClause(clause21);		
		
		Paragraph paragraph211 = new ParagraphJpaImpl("TEST_PARA_" +
				TestUtils.getRandomTestString(211), "TEST_BODY");
		clause21.addParagraph(paragraph211);
		
		
		
		Section section3 = new SectionJpaImpl("TEST_SECTION_" +
				TestUtils.getRandomTestString(3), "TEST_BODY");		
		document.addSection(section3);
		
		
		Clause clause31 = new ClauseJpaImpl("TEST_CLAUSE_" +
				TestUtils.getRandomTestString(31), "TEST_BODY");
		section3.addClause(clause31);		
		
		Paragraph paragraph311 = new ParagraphJpaImpl("TEST_PARA_" +
				TestUtils.getRandomTestString(311), "TEST_BODY");
		clause31.addParagraph(paragraph311);
		
		return document;
	}
}