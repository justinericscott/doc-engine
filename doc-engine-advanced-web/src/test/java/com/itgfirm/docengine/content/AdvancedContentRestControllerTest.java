package com.itgfirm.docengine.content;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.web.client.HttpServerErrorException;

import com.itgfirm.docengine.types.AdvancedDocument;
import com.itgfirm.docengine.types.Clause;
import com.itgfirm.docengine.types.Content;
//import com.itgfirm.docengine.types.Document;
import com.itgfirm.docengine.types.Paragraph;
import com.itgfirm.docengine.types.Section;
import com.itgfirm.docengine.types.jpa.AdvancedDocumentJpaImpl;
import com.itgfirm.docengine.types.jpa.ClauseJpaImpl;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
//import com.itgfirm.docengine.types.jpa.DocumentJpaImpl;
import com.itgfirm.docengine.types.jpa.ParagraphJpaImpl;
import com.itgfirm.docengine.types.jpa.SectionJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;
import com.itgfirm.docengine.util.AdvancedWebConstants;
import com.itgfirm.docengine.util.TestUtils;
import com.itgfirm.docengine.web.RestClient;

/**
 * @author Justin Scott
 * 
 * Tests for the Content REST Controller
 */
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
@WebIntegrationTest
public class AdvancedContentRestControllerTest extends AbstractTest {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(AdvancedContentRestControllerTest.class);
	@Autowired @Qualifier( value = AdvancedWebConstants.NAMED_ADVANCED_REST_CLIENT ) 
	RestClient rest;
	
	@SuppressWarnings("unchecked")
	@Test
	public void aa_MergeTest() {
		// Merge 1
		Content content = createContent();
		rest.delete(content);

		// Merge complex one at a time
		AdvancedDocument document = (AdvancedDocument) rest.merge(new AdvancedDocumentJpaImpl(content,
				TestUtils.getRandomTestString(2)), AdvancedDocumentJpaImpl.class);  
		assertNotNull(document.getId());		
		
		Section section = new SectionJpaImpl(content,
				TestUtils.getRandomTestString(3));
		document.addSection(section);
		section = (Section) rest.merge(section, SectionJpaImpl.class);
		assertNotNull(section.getId());		
		
		Clause clause = new ClauseJpaImpl(content, 
				TestUtils.getRandomTestString(4));
		section.addClause(clause);
		clause = (Clause) rest.merge(clause, ClauseJpaImpl.class);
		assertNotNull(clause.getId());		
		
		Paragraph paragraph = new ParagraphJpaImpl(content, 
				TestUtils.getRandomTestString(5));
		clause.addParagraph(paragraph);
		paragraph = (Paragraph) rest.merge(paragraph, ParagraphJpaImpl.class);
		assertNotNull(paragraph.getId());	
		rest.delete(document);		
		
		// Merge complex all at once
		document = createDocument();
		rest.delete(document);
		
		// Merge a list
		List<Content> list = new ArrayList<Content>();
		list.add(makeTestContent(1));
		list.add(makeTestContent(2));
		list.add(makeTestContent(3));
		list = (List<Content>) rest.merge(list);
		for (Content c : list) {
			assertNotNull(c.getId());
			rest.delete(c);
		}
	}
	
	@Test
	public void ab_MergeTestInvalidParamTest() {
		// Null Object
		try {
			rest.merge(null, Content.class);
			throw new IllegalStateException();
		} catch (HttpServerErrorException e) {
			assertEquals(e.getClass(), HttpServerErrorException.class);
		}
		// Empty Code Content
		assertNull(((Content) rest.merge(
				new ContentJpaImpl("TEST"), Content.class)).getId());
		// Empty Code Document
		assertNull(((Content) rest.merge(new AdvancedDocumentJpaImpl(new ContentJpaImpl("TEST")), 
				AdvancedDocument.class)).getId());
		// Empty Body Content 
		assertNull(((Content) rest.merge(new ContentJpaImpl(
				TestUtils.getRandomTestString(1), ""), Content.class)).getId());
		// Empty Body Document
		assertNull(((Content) rest.merge(new AdvancedDocumentJpaImpl(new ContentJpaImpl(""), 
						TestUtils.getRandomTestString(2)),	
						AdvancedDocument.class)).getId());
		// Duplicates 
		Content content = createContent();
		String code = content.getContentCd();
		assertNull(((Content) rest.merge(new ContentJpaImpl(content, code), 
				Content.class)).getId());
		rest.delete(content);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void ba_GetTest() {
		// Get All
		List<Content> contents = new ArrayList<Content>();
		contents.add(createContent(1));
		contents.add(createContent(2));
		contents.add(createContent(3));
		contents.add(createContent(4));
		contents.add(createContent(5));
		List<Content> allContent = (List<Content>) rest.get();
		assertNotNull(allContent);
		for (Content c : allContent) {
			validate(c);
		}
		for (Content c : contents) {
			rest.delete(c);
		}
		
		// Get Content
		Content content = createContent();
		validate((Content) rest.get(content.getId()));
		validate((Content) rest.get(content.getContentCd()));
		rest.delete(content);		
		
		// Get sub-classes
		AdvancedDocument document = createDocument();
		String code = document.getContentCd();
		
		assertTrue(AdvancedDocument.class.isAssignableFrom(rest.get(document.getId(), AdvancedDocument.class).getClass()));
		
		assertTrue(AdvancedDocument.class.isAssignableFrom(rest.get(code, AdvancedDocument.class).getClass())); 
		assertNull(((AdvancedDocument) rest.get(document.getId(), AdvancedDocument.class)).getSections());
		
		assertTrue(Section.class.isAssignableFrom(rest.get(
				document.getSections().get(0).getId(), Section.class).getClass()));
		assertTrue(Section.class.isAssignableFrom(rest.get(
				document.getSections().get(0).getContentCd(), Section.class).getClass()));
		assertNull(((Section) rest.get(
				document.getSections().get(0).getId(), Section.class)).getClauses());
		
		assertTrue(Clause.class.isAssignableFrom(rest.get(
				document.getSections().get(0).getClauses().get(0).getId(), Clause.class).getClass()));
		assertTrue(Clause.class.isAssignableFrom(rest.get(
				document.getSections().get(0).getClauses().get(0).getContentCd(), Clause.class).getClass()));
		assertNull(((Clause) rest.get(
				document.getSections().get(0).getClauses().get(0).getId(), Clause.class)).getParagraphs());
		
		assertTrue(Paragraph.class.isAssignableFrom(rest.get(
				document.getSections().get(0).getClauses().get(0).getParagraphs().get(0).getId(), Paragraph.class).getClass()));
		assertTrue(Paragraph.class.isAssignableFrom(rest.get(
				document.getSections().get(0).getClauses().get(0).getParagraphs().get(0).getContentCd(), Paragraph.class).getClass()));

		// Get Children by ID
		for (Object s : rest.getChildren(document.getId(), AdvancedDocument.class)) {
			if (s instanceof Section) {
				assertNull(((Section) s).getClauses());
				for (Object c : rest.getChildren(((Content) s).getId(), Section.class)) {
					if (c instanceof Clause) {
						assertNull(((Clause) c).getParagraphs());
						for (Object p : rest.getChildren(((Content) c).getId(), Clause.class)) {
							if (p instanceof Paragraph) {
								assertNotNull(((Content) p).getId());
							} else {
								throw new IllegalStateException();
							}
						}
 					} else {
 						throw new IllegalStateException();
 					}
				}
			} else {
				throw new IllegalStateException();
			}
		}
		
		// Get Children by ID with kids
		for (Object s : rest.getChildren(document.getId(), AdvancedDocument.class, true)) {
			if (s instanceof Section) {
				Section section = (Section) s;
				validate(section);
				for (Object c : rest.getChildren(((Content) s).getId(), Section.class, true)) {
					if (c instanceof Clause) {
						Clause clause = (Clause) c;
						validate(clause);
						for (Object p : rest.getChildren(((Content) c).getId(), Clause.class, true)) {
							if (p instanceof Paragraph) {
								validate((Paragraph) p);
							} else {
								throw new IllegalStateException();
							}
						}
 					} else {
 						throw new IllegalStateException();
 					}
				}
			} else {
				throw new IllegalStateException();
			}
		}
		
		// Get Children by code
		for (Object s : rest.getChildren(document.getContentCd(), AdvancedDocument.class)) {
			if (s instanceof Section) {
				for (Object c : rest.getChildren(((Content) s).getContentCd(), Section.class)) {
					if (c instanceof Clause) {
						for (Object p : rest.getChildren(((Content) c).getContentCd(), Clause.class)) {
							if (p instanceof Paragraph) {
								assertNotNull(((Content) p).getContentCd());
							} else {
								throw new IllegalStateException();
							}
						}
 					} else {
 						throw new IllegalStateException();
 					}
				}
			} else {
				throw new IllegalStateException();
			}
		}

		// Get Children by code with kids
		for (Object s : rest.getChildren(document.getContentCd(), AdvancedDocument.class, true)) {
			if (s instanceof Section) {
				Section section = (Section) s;
				validate(section);
				for (Object c : rest.getChildren(((Content) s).getContentCd(), Section.class, true)) {
					if (c instanceof Clause) {
						Clause clause = (Clause) c;
						validate(clause);
						for (Object p : rest.getChildren(((Content) c).getContentCd(), Clause.class, true)) {
							if (p instanceof Paragraph) {
								validate((Paragraph) p);								
							} else {
								throw new IllegalStateException();
							}
						}
 					} else {
 						throw new IllegalStateException();
 					}
				}
			} else {
				throw new IllegalStateException();
			}
		}
		rest.delete(document);
	}
	
	@Test
	public void bb_GetInvalidParamTest() {
		// Get By ID
		try {
			rest.get((Long) null);
			throw new IllegalStateException();
		} catch (HttpServerErrorException e) {
			assertEquals(e.getClass(), HttpServerErrorException.class);
		}
		assertNull(rest.get(0L));
		try {
			rest.get("");
			throw new IllegalStateException();
		} catch (HttpServerErrorException e) {
			assertEquals(e.getClass(), HttpServerErrorException.class);
		}
		assertNull(rest.get(999999999L));
		// Get By Code
		try {
			rest.get((String) null);
			throw new IllegalStateException();
		} catch (HttpServerErrorException e) {
			assertEquals(e.getClass(), HttpServerErrorException.class);
		}
		assertNull(rest.get("Snicklefritz"));

		// Invalid Parent ID
		assertNull(rest.getChildren(999999999L, AdvancedDocument.class));		
	}
	
	@Test
	public void bc_GetLazyObjectTest() {
		Content content = new ContentJpaImpl("", "TEST BODY");
		
		AdvancedDocument document = new AdvancedDocumentJpaImpl(content,
				TestUtils.getRandomTestString(2)); 
		document = (AdvancedDocument) rest.merge(document, AdvancedDocumentJpaImpl.class);
		assertNotNull(document.getId());
		String docCode = document.getContentCd();
		
		Section section = new SectionJpaImpl(content,
				TestUtils.getRandomTestString(3));
		section.setDocument(document);
		section = (Section) rest.merge(section, SectionJpaImpl.class);
		String secCode = section.getContentCd();
		
		Clause clause = new ClauseJpaImpl(content, 
				TestUtils.getRandomTestString(4));
		clause.setSection(section);
		clause = (Clause) rest.merge(clause, ClauseJpaImpl.class);
		String claCode = clause.getContentCd();
		
		Paragraph paragraph = new ParagraphJpaImpl(content, 
				TestUtils.getRandomTestString(5));
		paragraph.setClause(clause);
		paragraph = (Paragraph) rest.merge(paragraph, ParagraphJpaImpl.class);
		String paraCode = paragraph.getContentCd();
		
		document = (AdvancedDocument) rest.get(docCode, AdvancedDocument.class, true);		
		for (Section s : document.getSections()) {
			assertNotNull(s.getId());
			for (Clause c : s.getClauses()) {
				assertNotNull(c.getId());
				for (Paragraph p : c.getParagraphs()) {
					assertNotNull(p.getId());
				}
			}
		}
		section = (Section) rest.get(secCode, Section.class, true);		
		for (Clause c : section.getClauses()) {
			assertNotNull(c.getId());
			for (Paragraph p : c.getParagraphs()) {
				assertNotNull(p.getId());
			}
		}
		clause = (Clause) rest.get(claCode, Clause.class, true);		
		for (Paragraph p : clause.getParagraphs()) {
			assertNotNull(p.getId());
		}
		paragraph = (Paragraph) rest.get(paraCode, Paragraph.class);		
		assertNotNull(paragraph.getId());
		document = (AdvancedDocument) rest.get(docCode, AdvancedDocument.class, true);
		rest.delete(document);
	}
	
	@Test
	public void ca_GetByCodeLikeTest() {
		assertNotNull(rest.merge(TestUtils.getListOfRandomContents(3)));
		List<?> array = rest.getByCodeLike("TEST", Content[].class);
		assertNotNull(array);
		for (Object o : array) {
			Content c = (Content) o;
			assertNotNull(c);
			rest.delete(c);
		}				
	}
	
	@Test
	public void cb_GetByCodeLikeInvalidParamTest() {
		assertNull(rest.getByCodeLike("Snicklefritz", Content[].class));
		assertNull(rest.getByCodeLike("", Content[].class));
		assertNull(rest.getByCodeLike((String) null, Content[].class));
	}

	@Test
	public void xx_DeleteTest() {
		Content content = createContent();
		Long id = content.getId();
		rest.delete(content);
		assertNull(rest.get(id));
		rest.delete(content);
	}

	private Content createContent() {
		return createContent(1);
	}
	
	private Content createContent(int seed) {
		Content content = (Content) rest.merge(makeTestContent(seed), ContentJpaImpl.class);
		validate(content);
		return content;
	}
	
	private AdvancedDocument createDocument() {
		AdvancedDocument document =  (AdvancedDocument) rest.merge(makeTestDocument(), AdvancedDocumentJpaImpl.class);
		document = (AdvancedDocument) rest.get(document.getId(), AdvancedDocumentJpaImpl.class, true);
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