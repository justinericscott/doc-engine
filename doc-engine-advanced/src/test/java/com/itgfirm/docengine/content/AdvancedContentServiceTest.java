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

import com.itgfirm.docengine.types.AdvancedDocument;
import com.itgfirm.docengine.types.Clause;
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.Paragraph;
import com.itgfirm.docengine.types.Section;
import com.itgfirm.docengine.types.jpa.AdvancedDocumentJpaImpl;
import com.itgfirm.docengine.types.jpa.ClauseJpaImpl;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
//import com.itgfirm.docengine.types.jpa.DocumentJpaImpl;
import com.itgfirm.docengine.types.jpa.ParagraphJpaImpl;
import com.itgfirm.docengine.types.jpa.SectionJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;
import com.itgfirm.docengine.util.TestUtils;

/**
 * @author Justin Scott
 * 
 * Tests for the Content Service
 */
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class AdvancedContentServiceTest extends AbstractTest {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(AdvancedContentServiceTest.class);
	@Autowired @Qualifier("advanced")
	private AdvancedContentService contentService;
	
	@Test
	public void aa_MergeTest() {
		// Merge 1
		Content content = createContent();
		contentService.delete(content);		
		
		// Merge complex one at a time
		AdvancedDocument document = (AdvancedDocument) contentService.merge(new AdvancedDocumentJpaImpl(content,
				TestUtils.getRandomTestString(2))); 
		assertNotNull(document.getId());		
		
		Section section = new SectionJpaImpl(content,
				TestUtils.getRandomTestString(3));
		document.addSection(section);
		section = (Section) contentService.merge(section);
		assertNotNull(section.getId());		
		
		Clause clause = new ClauseJpaImpl(content, 
				TestUtils.getRandomTestString(4));
		section.addClause(clause);
		clause = (Clause) contentService.merge(clause);
		assertNotNull(clause.getId());		
		
		Paragraph paragraph = new ParagraphJpaImpl(content, 
				TestUtils.getRandomTestString(5));
		clause.addParagraph(paragraph);
		paragraph = (Paragraph) contentService.merge(paragraph);
		assertNotNull(paragraph.getId());

		//Need to re-get after assembly to delete.
		document = (AdvancedDocument) contentService.get(document.getId(), true);
		validate(document);
		contentService.delete(document); 
		
		// Merge complex all at once
		document = createDocument();
		contentService.delete(document);
		
		// Merge a list
		List<Content> list = new ArrayList<Content>();
		list.add(createContent(1));
		list.add(createContent(2));
		list.add(createContent(3));
		list = contentService.merge(list);
		for (Content c : list) {
			assertNotNull(c.getId());
			contentService.delete(c);
		}
	}

	@Test
	public void ab_MergeInvalidParamTest() {
		// Null Object
		assertNull(contentService.merge((Content) null));
		// Empty Code Content
		try {
			new ContentJpaImpl("", "TEST BODY");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getClass().equals(IllegalArgumentException.class));	
		}		
		// Empty Body Content 
		try {
			new ContentJpaImpl(TestUtils.getRandomTestString(1), "");			
		} catch (IllegalArgumentException e) {
			assertTrue(e.getClass().equals(IllegalArgumentException.class));	
		}		
		// Empty Body Document
		assertNull(contentService.merge(new AdvancedDocumentJpaImpl(new ContentJpaImpl(""), 
				TestUtils.getRandomTestString(2))).getId());
		// Duplicates 
		Content content = createContent();
		assertNull(contentService.merge(new ContentJpaImpl(
				content, content.getContentCd())).getId());
		contentService.delete(content);
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
		contents.add(createDocument());
		List<Content> allContent = (List<Content>) contentService.get();
		for (Content c : allContent) {
			validate(c);
		}
		for (Content c : contents) {
			contentService.delete(c);
		}
		
		// Get Content
		Content content = createContent();
		validate((Content) contentService.get(content.getId()));
		validate((Content) contentService.get(content.getContentCd()));
		contentService.delete(content);		
		
		// Get sub-classes, no eagerKids
		AdvancedDocument document = createDocument();
		
		assertTrue(AdvancedDocumentJpaImpl.class.equals(contentService.get(document.getId()).getClass()));
		assertTrue(AdvancedDocumentJpaImpl.class.equals(contentService.get(document.getContentCd()).getClass()));
		try {
			((AdvancedDocument) contentService.get(document.getId())).getSections().get(0);
			throw new IllegalStateException();
		} catch (LazyInitializationException e) {
			assertTrue(e.getClass().equals(
					LazyInitializationException.class));	
		}
		
		assertTrue(SectionJpaImpl.class.equals(contentService.get(
				document.getSections().get(0).getId()).getClass()));
		assertTrue(SectionJpaImpl.class.equals(contentService.get(
				document.getSections().get(0).getContentCd()).getClass()));
		try {
			((Section) contentService.get(document.getSections().get(0).getId())).getClauses().get(0);
			throw new IllegalStateException();
		} catch (LazyInitializationException e) {
			assertTrue(e.getClass().equals(
					LazyInitializationException.class));	
		}
		
		assertTrue(ClauseJpaImpl.class.equals(contentService.get(
				document.getSections().get(0).getClauses().get(0).getId()).getClass()));
		assertTrue(ClauseJpaImpl.class.equals(contentService.get(
				document.getSections().get(0).getClauses().get(0).getContentCd()).getClass()));
		try {
			((Clause) contentService.get(document.getSections().get(0).getClauses().get(0).getId())).getParagraphs().get(0);
			throw new IllegalStateException();
		} catch (LazyInitializationException e) {
			assertTrue(e.getClass().equals(
					LazyInitializationException.class));	
		}
		
		assertTrue(ParagraphJpaImpl.class.equals(contentService.get(
				document.getSections().get(0).getClauses().get(0).getParagraphs().get(0).getId()).getClass()));
		assertTrue(ParagraphJpaImpl.class.equals(contentService.get(
				document.getSections().get(0).getClauses().get(0).getParagraphs().get(0).getContentCd()).getClass()));

		// Get Children by ID
		for (Content s : contentService.getChildren(document.getId())) {
			if (s instanceof Section) {
				try {
					((Section) s).getClauses().get(0);
					throw new IllegalStateException();
				} catch (LazyInitializationException e) {
					assertTrue(e.getClass().equals(
							LazyInitializationException.class));	
				}				
				for (Content c : contentService.getChildren(s.getId())) {
					if (c instanceof Clause) {
						try {
							((Clause) c).getParagraphs().get(0);
							throw new IllegalStateException();
						} catch (LazyInitializationException e) {
							assertTrue(e.getClass().equals(
									LazyInitializationException.class));	
						}				
						for (Content p : contentService.getChildren(c.getId())) {
							if (p instanceof Paragraph) {
								validate(p);
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
		for (Content s : contentService.getChildren(document.getId(), true)) {
			if (s instanceof Section) {
				Section section = (Section) s;
				validate(section);
				for (Content c : contentService.getChildren(s.getId(), true)) {
					if (c instanceof Clause) {
						Clause clause = (Clause) c;
						validate(clause);
						for (Content p : contentService.getChildren(c.getId(), true)) {
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
		for (Content s : contentService.getChildren(document.getContentCd())) {
			if (s instanceof Section) {
				for (Content c : contentService.getChildren(s.getContentCd())) {
					if (c instanceof Clause) {
						for (Content p : contentService.getChildren(c.getContentCd())) {
							if (p instanceof Paragraph) {
								assertNotNull(p.getContentCd());
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
		for (Content s : contentService.getChildren(document.getContentCd(), true)) {
			if (s instanceof Section) {
				Section section = (Section) s;
				validate(section);
				for (Content c : contentService.getChildren(s.getContentCd(), true)) {
					if (c instanceof Clause) {
						Clause clause = (Clause) c;
						validate(clause);
						assertNotNull(clause.getParagraphs().get(0).getId());
						for (Content p : contentService.getChildren(c.getContentCd(), true)) {
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
		contentService.delete(document);
	}
	
	@Test
	public void bb_GetInvalidParamTest() {
		assertNull(contentService.get((String) null));
		assertNull(contentService.get(""));
		assertNull(contentService.get(0L));
		assertNull(contentService.get(99999999999999999L));
		assertNull(contentService.get("Snicklefritz"));
	}
	
	@Test
	public void ca_GetByCodeLikeTest() {
		Content content = createContent();
		content = contentService.getByCodeLike("TEST").get(0);
		assertNotNull(content.getId());
		contentService.delete(content);
	}
	
	@Test
	public void cb_GetByCodeLikeInvalidParamTest() {
		assertNull(contentService.getByCodeLike(null));
		assertNull(contentService.getByCodeLike(""));
		assertNull(contentService.getByCodeLike("%Snicklefritz%"));
	}

	@Test
	public void xx_DeleteTest() {
		Content content = createContent();
		assertTrue(contentService.delete(content));
		content = createContent();
		assertTrue(contentService.delete(content.getId()));
		assertNull(contentService.get(content.getId()));
		content = createContent();
		assertTrue(contentService.delete(content.getContentCd()));
		assertNull(contentService.get(content.getContentCd()));
		createContent(1);
		createContent(2);
		createContent(3);
		createContent(4);
		createContent(5);
		assertTrue(contentService.deleteByCodeLike("TEST"));
		assertNull(contentService.getByCodeLike("TEST"));
	}

	private Content createContent() {
		return createContent(1);
	}
	
	private Content createContent(int seed) {
		Content content = contentService.merge(makeTestContent(seed));
		validate(content);
		return content;		
	}
	
	private AdvancedDocument createDocument() {
		AdvancedDocument document = (AdvancedDocument) contentService.merge(makeTestDocument());
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