/**TODO: License
 */
package com.itgfirm.docengine.instance;

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

import com.itgfirm.docengine.content.AdvancedContentService;
import com.itgfirm.docengine.types.AdvancedDocument;
import com.itgfirm.docengine.types.AdvancedDocumentInstance;
import com.itgfirm.docengine.types.Clause;
import com.itgfirm.docengine.types.ClauseInstance;
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.Instance;
import com.itgfirm.docengine.types.Paragraph;
import com.itgfirm.docengine.types.ParagraphInstance;
import com.itgfirm.docengine.types.Section;
import com.itgfirm.docengine.types.SectionInstance;
import com.itgfirm.docengine.types.jpa.AdvancedDocumentInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.AdvancedDocumentJpaImpl;
import com.itgfirm.docengine.types.jpa.ClauseInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.ClauseJpaImpl;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.types.jpa.InstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.ParagraphInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.ParagraphJpaImpl;
import com.itgfirm.docengine.types.jpa.SectionInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.SectionJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;
import com.itgfirm.docengine.util.TestUtils;

/**
 * @author Justin Scott
 * 
 * Tests for the Instance Service
 */
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class InstanceServiceTest extends AbstractTest {
	private static final Logger LOG = LogManager.getLogger(InstanceServiceTest.class);
	@Autowired
	private InstanceService instanceService;
	@Autowired @Qualifier("advanced")
	private AdvancedContentService contentService;

	@Test
	public void aa_MergeTest() {
		String projectId = TestUtils.getRandomTestString(1);
		// Merge 1		
		Instance instance = createInstance(createContent());
		instanceService.delete(instance);
		contentService.delete(instance.getContent());
		
		// Merge complex one at a time
		AdvancedDocument document = (AdvancedDocument) contentService.merge(new AdvancedDocumentJpaImpl(
				TestUtils.getRandomTestString(2), "TEST_BODY"));
		assertNotNull(document.getId());
		AdvancedDocumentInstance dInstance = new AdvancedDocumentInstanceJpaImpl(document, projectId);
		dInstance = (AdvancedDocumentInstance) instanceService.merge(dInstance);
		
		Section section = new SectionJpaImpl(TestUtils.getRandomTestString(3),
				"TEST_BODY");
		document.addSection(section);
		section = (Section) contentService.merge(section);
		assertNotNull(section.getId());
		SectionInstance sInstance = new SectionInstanceJpaImpl(section, projectId);
		dInstance.addSection(sInstance);
		sInstance = (SectionInstance) instanceService.merge(sInstance);
		
		Clause clause = new ClauseJpaImpl(TestUtils.getRandomTestString(4),
				"TEST_BODY");
		section.addClause(clause);
		clause = (Clause) contentService.merge(clause);
		assertNotNull(clause.getId());
		ClauseInstance cInstance = new ClauseInstanceJpaImpl(clause, projectId);
		sInstance.addClause(cInstance);
		cInstance = (ClauseInstance) instanceService.merge(cInstance);
		
		Paragraph paragraph = new ParagraphJpaImpl(TestUtils.getRandomTestString(5),
				"TEST_BODY");
		clause.addParagraph(paragraph);
		paragraph = (Paragraph) contentService.merge(paragraph);
		assertNotNull(paragraph.getId());
		ParagraphInstance pInstance = new ParagraphInstanceJpaImpl(paragraph, projectId);
		cInstance.addParagraph(pInstance);
		pInstance = (ParagraphInstance) instanceService.merge(pInstance);
		
		dInstance = (AdvancedDocumentInstance) instanceService.get(dInstance.getId(), true);
		validate(dInstance);
		instanceService.delete(dInstance);
		contentService.delete(dInstance.getContent());
		
		// Merge complex all at once
		dInstance = createDocumentInstance(createDocument());
		instanceService.delete(dInstance);
		contentService.delete(dInstance.getContent());
		
		// Merge a list
		List<Instance> instances = new ArrayList<Instance>();
		List<Content> list = new ArrayList<Content>();
		list.add(createContent(1));
		list.add(createContent(2));
		list.add(createContent(3));
		list = (List<Content>) contentService.merge(list);
		for (Content c : list) {
			instances.add(new InstanceJpaImpl(c, projectId));
		}
		instances = instanceService.merge(instances);
		for (Instance i : instances) {
			validate(i);
			instanceService.delete(i);
			contentService.delete(i.getContent());
		}
	}
	
	@Test
	public void ab_MergeInvalidParamTest() {
		String projectId = TestUtils.getRandomTestString(2);
		
		Content content = createContent();
		Instance noContentInstance = new InstanceJpaImpl();
		noContentInstance.setProjectId(projectId);
		noContentInstance = instanceService.merge(noContentInstance);
		assertNull(instanceService.merge(noContentInstance).getId());
		
		Instance noProjectIdInstance = new InstanceJpaImpl(content);
		noProjectIdInstance = instanceService.merge(noProjectIdInstance);
		assertNull(instanceService.merge(noProjectIdInstance).getId());
		
		Instance noContentIdInstance = new InstanceJpaImpl(new ContentJpaImpl(
				TestUtils.getRandomTestString(1), 
				"TEST_BODY"), projectId);
		noContentIdInstance = instanceService.merge(noContentIdInstance);		
		assertNull(instanceService.merge(noContentIdInstance).getId());

		contentService.delete(content);
	}
	
	@Test
	public void ba_GetTest() {
		String projectId = TestUtils.getRandomTestString(1);
		// Get All
		List<Instance> instances = new ArrayList<Instance>();
		List<Content> list = new ArrayList<Content>();
		list.add(createContent(1));
		list.add(createContent(2));
		list.add(createContent(3));
		list = (List<Content>) contentService.merge(list);
		for (Content c : list) {
			instances.add(new InstanceJpaImpl(c, projectId));
		}
		instances = instanceService.merge(instances);
		List<Instance> allInstances = instanceService.get();
		for (Instance i : allInstances) {
			validate(i);
		}
		for (Instance i : instances) {
			instanceService.delete(i);
			contentService.delete(i.getContent());
		}
		
		// Get Instance
		Instance instance = createInstance(createContent());
		projectId = instance.getProjectId();
		validate(instanceService.get(instance.getId()));
		validate(instanceService.get(instance.getProjectId(), instance.getContent().getContentCd()));		
		instanceService.delete(instance);
		contentService.delete(instance.getContent());
		
		// Get sub-classes, no eagerKids
		AdvancedDocumentInstance document = createDocumentInstance(createDocument());
		projectId = document.getProjectId();
		assertTrue(AdvancedDocumentInstanceJpaImpl.class.equals(instanceService.get(
				document.getId()).getClass()));
		assertTrue(AdvancedDocumentInstanceJpaImpl.class.equals(instanceService.get(projectId,
				document.getContent().getContentCd()).getClass()));
		try {
			((AdvancedDocumentInstance) instanceService.get(document.getId())).getSections().get(0);
			throw new IllegalStateException();
		} catch (LazyInitializationException e) {
			assertTrue(e.getClass().equals(
					LazyInitializationException.class));	
		}
		
		assertTrue(SectionInstanceJpaImpl.class.equals(instanceService.get(
				document.getSections().get(0).getId()).getClass()));
		assertTrue(SectionInstanceJpaImpl.class.equals(instanceService.get(projectId,
				document.getSections().get(0).getContent().getContentCd()).getClass()));
		try {
			((SectionInstance) instanceService.get(document.getSections().get(0).getId())).getClauses().get(0);
			throw new IllegalStateException();
		} catch (LazyInitializationException e) {
			assertTrue(e.getClass().equals(
					LazyInitializationException.class));	
		}		
		
		assertTrue(ClauseInstanceJpaImpl.class.equals(instanceService.get(
				document.getSections().get(0).getClauses().get(0).getId()).getClass()));
		assertTrue(ClauseInstanceJpaImpl.class.equals(instanceService.get(projectId, 
				document.getSections().get(0).getClauses().get(0).getContent().getContentCd()).getClass()));
		try {
			((ClauseInstance) instanceService.get(document.getSections().get(0).getClauses().get(0).getId())).getParagraphs().get(0);
			throw new IllegalStateException();
		} catch (LazyInitializationException e) {
			assertTrue(e.getClass().equals(LazyInitializationException.class));	
		}		
		
		assertTrue(ParagraphInstanceJpaImpl.class.equals(instanceService.get(
				document.getSections().get(0).getClauses().get(0).getParagraphs().get(0).getId()).getClass()));
		assertTrue(ParagraphInstanceJpaImpl.class.equals(instanceService.get(projectId,
				document.getSections().get(0).getClauses().get(0).getParagraphs().get(0).getContent().getContentCd()).getClass()));
		
		// Get Children by ID
		assertNotNull(document.getId());
		for (Instance s : instanceService.getChildren(document.getId())) {
			if (s instanceof SectionInstanceJpaImpl) {
				try {
					((SectionInstance) s).getClauses().get(0);
					throw new IllegalStateException();
				} catch (LazyInitializationException e) {
					assertTrue(e.getClass().equals(
							LazyInitializationException.class));	
				}				
				for (Instance c : instanceService.getChildren(s.getId())) {
					if (c instanceof ClauseInstanceJpaImpl) {
						try {
							((ClauseInstance) c).getParagraphs().get(0);
							throw new IllegalStateException();
						} catch (LazyInitializationException e) {
							assertTrue(e.getClass().equals(
									LazyInitializationException.class));	
						}				
						for (Instance p : instanceService.getChildren(c.getId())) {
							if (p instanceof ParagraphInstanceJpaImpl) {
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
		for (Instance s : instanceService.getChildren(document.getId(), true)) {
			if (s instanceof SectionInstance) {
				SectionInstance section = (SectionInstance) s;
				validate(section);
				for (Instance c : instanceService.getChildren(s.getId(), true)) {
					if (c instanceof ClauseInstance) {
						ClauseInstance clause = (ClauseInstance) c;
						validate(clause);
						for (Instance p : instanceService.getChildren(c.getId(), true)) {
							if (p instanceof ParagraphInstance) {
								validate((ParagraphInstance) p); 
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
		for (Instance s : instanceService.getChildren(projectId, document.getContent().getContentCd())) {
			if (s instanceof SectionInstance) {
				for (Instance c : instanceService.getChildren(projectId, s.getContent().getContentCd())) {
					if (c instanceof ClauseInstance) {
						for (Instance p : instanceService.getChildren(projectId, c.getContent().getContentCd())) {
							if (p instanceof ParagraphInstance) {
								assertNotNull(p.getId());
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
		for (Instance s : instanceService.getChildren(projectId, document.getContent().getContentCd(), true)) {
			if (s instanceof SectionInstance) {
				SectionInstance section = (SectionInstance) s;
				validate(section);
				for (Instance c : instanceService.getChildren(projectId, s.getContent().getContentCd(), true)) {
					if (c instanceof ClauseInstance) {
						ClauseInstance clause = (ClauseInstance) c;
						validate(clause);
						assertNotNull(clause.getParagraphs().get(0).getId());
						for (Instance p : instanceService.getChildren(projectId, c.getContent().getContentCd(), true)) {
							if (p instanceof ParagraphInstance) {
								validate((ParagraphInstance) p);
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
		instanceService.delete(document);		
		contentService.delete(document.getContent());
	}
	
	@Test
	public void bb_GetInvalidParamTest() {
		Instance instance = createInstance(createContent());
		instance = instanceService.merge(instance);
		String code = instance.getContent().getContentCd();
		
		assertNotNull(instance.getId());
		assertNotNull(instance.getContent().getId());
		
		assertNull(instanceService.get(0L));
		assertNull(instanceService.get(99999999L));
		assertNull(instanceService.get(null));
		
		assertNull(instanceService.get("", code));
		assertNull(instanceService.get(null, code));
		assertNull(instanceService.get("Snicklefritz", code));
		assertNull(instanceService.get("TEST_PROJECT_ID", null));
		assertNull(instanceService.get("TEST_PROJECT_ID", ""));
		assertNull(instanceService.get("TEST_PROJECT_ID", "Snicklefritz"));
		
		instanceService.delete(instance);
		contentService.delete(instance.getContent());
	}

	@Test
	public void ca_GetByProjectAndCodeLikeTest() {
		Instance instance = createInstance(createContent());
		assertNotNull(instance.getId());
		assertNotNull(instanceService.getByProjectAndCodeLike(
				instance.getProjectId(), "TEST"));
		instanceService.delete(instance);
		contentService.delete(instance.getContent());
	}

	@Test
	public void cb_GetByProjectAndCodeLikeInvalidParamTest() {
		Instance instance = createInstance(createContent());
		String projectId = instance.getProjectId();
		assertNotNull(instance.getId());
		assertNull(instanceService.getByProjectAndCodeLike(null, 
						TestUtils.TEST_CODE_PREFIX));
		assertNull(instanceService.getByProjectAndCodeLike("", 
				TestUtils.TEST_CODE_PREFIX));
		assertNull(instanceService.getByProjectAndCodeLike("Snicklefritz", 
				TestUtils.TEST_CODE_PREFIX));
		assertNull(instanceService.getByProjectAndCodeLike(projectId, 
				""));
		assertNull(instanceService.getByProjectAndCodeLike(projectId, 
				null));
		assertNull(instanceService.getByProjectAndCodeLike(projectId, 
				"Snicklefritz"));
		instanceService.delete(instance);
		contentService.delete(instance.getContent());		
	}
	
	@Test
	public void xx_DeleteTest() {
		Instance instance = createInstance(createContent());
		String projectId = instance.getProjectId();
		String code = instance.getContent().getContentCd();
		instanceService.delete(instance.getId());
		contentService.delete(instance.getContent());
		assertNull(instanceService.get(instance.getId()));
		instance = createInstance(createContent());
		code = instance.getContent().getContentCd();
		projectId = instance.getProjectId();
		instanceService.delete(projectId, code);
		assertNull(instanceService.get(instance.getId()));
//		instanceService.deleteAll();
		contentService.delete(instance.getContent());
	}

	private Content createContent() {
		return createContent(1);
	}
	
	private Content createContent(int seed) {
		Content content = (Content) contentService.merge(makeTestContent(seed));
		validate(content);
		return content;		
	}
	
	private Instance createInstance(Content content) {
		Instance instance = instanceService.merge(new InstanceJpaImpl(content, 
				TestUtils.getRandomTestString(2)));
		validate(instance);
		return instance;
	}
	
	private AdvancedDocument createDocument() {
		AdvancedDocument document =  (AdvancedDocument) contentService.merge(makeTestDocument());
		validate(document);
		return document;
	}

	private AdvancedDocumentInstance createDocumentInstance(AdvancedDocument document) {
		AdvancedDocumentInstance instance = (AdvancedDocumentInstance) instanceService.merge(new AdvancedDocumentInstanceJpaImpl(
				document, TestUtils.getRandomTestString(1)));
		validate(instance);
		return instance;
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

	protected void validate(AdvancedDocument document) { 
		assertTrue(document.isValid(true));
		assertNotNull(document.getSections());
		for (Section s : document.getSections()) {
			validate(s);
		}
	}

	protected void validate(Section section) { 
		assertTrue(section.isValid(true));
		assertNotNull(section.getClauses());
		for (Clause c : section.getClauses()) {
			validate(c);
		}
	}

	protected void validate(Clause clause) {
		assertTrue(clause.isValid(true));
		assertNotNull(clause.getParagraphs());
		for (Paragraph p : clause.getParagraphs()) {
			validate(p);					
		}
	}
	
	protected void validate(Paragraph paragraph) {
		assertTrue(paragraph.isValid(true));
	}
	
	protected void validate(Instance instance) {
		assertTrue(instance.isValid(true));	
		assertNotNull(instance.getContent().getId());
	}
	
	protected void validate(AdvancedDocumentInstance document) {
		assertTrue(document.isValid(true));
		LOG.trace("Validating Document Instance.");
		assertNotNull(document.getSections());
		for (SectionInstance s : document.getSections()) {
			LOG.trace("Validating Section Instance.");
			s.isValid(true);
			assertNotNull(s.getClauses());
			for (ClauseInstance c : s.getClauses()) {
				LOG.trace("Validating Clause Instance.");
				c.isValid(true);
				assertNotNull(c.getParagraphs());
				for (ParagraphInstance p : c.getParagraphs()) {
					LOG.trace("Validating Paragraph Instance.");
					p.isValid(true);
				}
			}
		}
	}
}