package com.itgfirm.docengine.instance;

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
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.client.HttpServerErrorException;

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
import com.itgfirm.docengine.util.AdvancedWebConstants;
import com.itgfirm.docengine.util.TestUtils;
import com.itgfirm.docengine.web.RestClient;

/**
 * @author Justin Scott
 * 
 * Instance REST Controller Test
 */
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
@WebIntegrationTest
public class InstanceRestControllerTest extends AbstractTest {
	private static final Logger LOG = LogManager.getLogger(InstanceRestControllerTest.class);
	@Autowired @Qualifier( AdvancedWebConstants.NAMED_INSTANCE_REST_CLIENT ) 
	private RestClient instanceRest;
	@Autowired @Qualifier( AdvancedWebConstants.NAMED_ADVANCED_REST_CLIENT ) 
	private RestClient contentRest;
	
	@SuppressWarnings("unchecked")
	@Test
	public void aa_MergeTest() {
		String projectId = TestUtils.getRandomTestString(1);
		// Merge 1
		Instance singleInstance = createInstance(createContent());
		instanceRest.delete(singleInstance);
		contentRest.delete(singleInstance.getContent());
		
		// Merge complex one at a time
		AdvancedDocument document = (AdvancedDocument) contentRest.merge(new AdvancedDocumentJpaImpl(TestUtils.getRandomTestString(2), "TEST_BODY"), AdvancedDocumentJpaImpl.class);
		assertNotNull(document.getId());
		AdvancedDocumentInstance dInstance = new AdvancedDocumentInstanceJpaImpl(document, projectId);
		dInstance = (AdvancedDocumentInstance) instanceRest.merge(dInstance, AdvancedDocumentInstanceJpaImpl.class);
		
		Section section = new SectionJpaImpl(TestUtils.getRandomTestString(3), "TEST_BODY");
		document.addSection(section);
		section = (Section) contentRest.merge(section, SectionJpaImpl.class);
		assertNotNull(section.getId());
		SectionInstance sInstance = new SectionInstanceJpaImpl(section, projectId);
		dInstance.addSection(sInstance);
		sInstance = (SectionInstance) instanceRest.merge(sInstance, SectionInstanceJpaImpl.class);
		
		Clause clause = new ClauseJpaImpl(TestUtils.getRandomTestString(4), "TEST_BODY");
		section.addClause(clause);
		clause = (Clause) contentRest.merge(clause, ClauseJpaImpl.class);
		assertNotNull(clause.getId());
		ClauseInstance cInstance = new ClauseInstanceJpaImpl(clause, projectId);
		sInstance.addClause(cInstance);
		cInstance = (ClauseInstance) instanceRest.merge(cInstance, ClauseInstanceJpaImpl.class);
		
		Paragraph paragraph = new ParagraphJpaImpl(TestUtils.getRandomTestString(5), "TEST_BODY");
		clause.addParagraph(paragraph);
		paragraph = (Paragraph) contentRest.merge(paragraph, ParagraphJpaImpl.class);
		assertNotNull(paragraph.getId());
		ParagraphInstance pInstance = new ParagraphInstanceJpaImpl(paragraph, projectId);
		cInstance.addParagraph(pInstance);
		pInstance = (ParagraphInstance) instanceRest.merge(pInstance, ParagraphInstanceJpaImpl.class);
		
		dInstance = (AdvancedDocumentInstance) instanceRest.get(dInstance.getId(), AdvancedDocumentInstanceJpaImpl.class, true);
		validate(dInstance);
		instanceRest.delete(dInstance);
		contentRest.delete(dInstance.getContent());
		
		// Merge complex all at once
		AdvancedDocumentInstance singleDocumentInstance = createDocumentInstance(createDocument());
		instanceRest.delete(singleDocumentInstance);		
		contentRest.delete(singleDocumentInstance.getContent());		

		// Merge a list
		List<Instance> instances = new ArrayList<Instance>();
		List<Content> list = new ArrayList<Content>();
		list.add(createContent(1));
		list.add(createContent(2));
		list.add(createContent(3));
		list = (List<Content>) contentRest.merge(list);
		for (Content c : list) {
			instances.add(new InstanceJpaImpl(c, projectId));
		}
		instances = (List<Instance>) instanceRest.merge(instances);
		for (Instance i : instances) {
			validate(i);
			instanceRest.delete(i);
			contentRest.delete(i.getContent());
		}
	}
	
	@Test
	public void ab_MergeInvalidParamTest() {
		Content content = createContent();
		// Null Object
		try {
			instanceRest.merge(null, InstanceJpaImpl.class);
			throw new IllegalStateException();
		} catch ( HttpServerErrorException e) {
			assertEquals(e.getClass(), 
					HttpServerErrorException.class);
		}
		// Empty Project ID
		assertNull(((Instance) instanceRest.merge(
				new InstanceJpaImpl(content), InstanceJpaImpl.class)).getId());
		// No Content
		try {
			instanceRest.merge(new InstanceJpaImpl(TestUtils.getRandomTestString(1)), 
					InstanceJpaImpl.class);	
			throw new IllegalStateException();
		} catch (HttpMessageNotWritableException e) {
			assertTrue(e.getClass().equals(
					HttpMessageNotWritableException.class));
		}
		// Content with no ID		
		assertNull(((Instance) instanceRest.merge(
				new InstanceJpaImpl(new ContentJpaImpl(TestUtils.getRandomTestString(2), 
				"TEST_BODY"), TestUtils.getRandomTestString(3)), 
				InstanceJpaImpl.class)).getId());
		// Duplicates
		Instance instance = createInstance(content);
		assertNull(((Instance) instanceRest.merge(
				new InstanceJpaImpl(content, instance.getProjectId()), 
				InstanceJpaImpl.class)).getId());
		instanceRest.delete(instance);
		contentRest.delete(instance.getContent());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void ba_GetTest() {
		// Get All
		List<Instance> instances = new ArrayList<Instance>();
		instances.add(createInstance(createContent(1)));
		instances.add(createInstance(createContent(2)));
		instances.add(createInstance(createContent(3)));
		instances = (List<Instance>) instanceRest.merge(instances);
		List<Instance> allInstances = (List<Instance>) instanceRest.get();
		for (Instance i : allInstances) {
			validate(i);
		}
		for (Instance i : instances) {
			instanceRest.delete(i);
			contentRest.delete(i.getContent());
		}
		
		// Get Instance
		Instance instance = createInstance(createContent());
		validate((Instance) instanceRest.get(instance.getId(), InstanceJpaImpl.class));
		validate((Instance) instanceRest.get(instance.getProjectId(), instance.getContent().getContentCd(), 
				InstanceJpaImpl.class));		
		instanceRest.delete(instance);
		contentRest.delete(instance.getContent());
		
		// Get sub-classes, no eagerKids
		AdvancedDocumentInstance document = createDocumentInstance(createDocument());
		String projectId = document.getProjectId();
//		String code = document.getContent().getContentCd();

		assertTrue(AdvancedDocumentInstanceJpaImpl.class.equals(instanceRest.get(
				document.getId(), AdvancedDocumentInstanceJpaImpl.class).getClass()));
		assertTrue(AdvancedDocumentInstanceJpaImpl.class.equals(instanceRest.get(projectId,
				document.getContent().getContentCd(), AdvancedDocumentInstanceJpaImpl.class).getClass()));
		assertNull(((AdvancedDocumentInstanceJpaImpl) instanceRest.get(document.getId(), 
				AdvancedDocumentInstanceJpaImpl.class)).getSections());
		
		assertTrue(SectionInstanceJpaImpl.class.equals(instanceRest.get(
				document.getSections().get(0).getId(), 
				SectionInstanceJpaImpl.class).getClass()));
		assertTrue(SectionInstanceJpaImpl.class.equals(instanceRest.get(projectId,
				document.getSections().get(0).getContent().getContentCd(), 
				SectionInstanceJpaImpl.class).getClass()));
		assertNull(((SectionInstance) instanceRest.get(document.getSections().get(0).getId(), 
					SectionInstanceJpaImpl.class)).getClauses());
		
		assertTrue(ClauseInstanceJpaImpl.class.equals(instanceRest.get(
				document.getSections().get(0).getClauses().get(0).getId(), 
				ClauseInstanceJpaImpl.class).getClass()));
		
		assertTrue(ClauseInstanceJpaImpl.class.equals(instanceRest.get(projectId, 
				document.getSections().get(0).getClauses().get(0).getContent().getContentCd(), 
				ClauseInstanceJpaImpl.class).getClass()));
		
		assertNull(((ClauseInstance) instanceRest.get(document.getSections().get(0).getClauses().get(0).getId(), 
					ClauseInstanceJpaImpl.class)).getParagraphs());
		
		assertTrue(ParagraphInstanceJpaImpl.class.equals(instanceRest.get(
				document.getSections().get(0).getClauses().get(0).getParagraphs().get(0).getId(), 
				ParagraphInstanceJpaImpl.class).getClass()));
		assertTrue(ParagraphInstanceJpaImpl.class.equals(instanceRest.get(projectId,
				document.getSections().get(0).getClauses().get(0).getParagraphs().get(0).getContent().getContentCd(), 
				ParagraphInstance.class).getClass()));
		
		// Get Children by ID
		assertNotNull(document.getId());
		List<SectionInstance> sections = (List<SectionInstance>) instanceRest.getChildren(document.getId(), AdvancedDocumentInstanceJpaImpl.class);
		for (Object s : sections) {
			if (s instanceof SectionInstanceJpaImpl) {
				SectionInstance section = (SectionInstance) s;
				assertNull(section.getClauses());
				for (Object c : instanceRest.getChildren(section.getId(), SectionInstanceJpaImpl.class)) {
					if (c instanceof ClauseInstanceJpaImpl) {
						ClauseInstance clause = (ClauseInstance) c;
						assertNull(clause.getParagraphs());
						for (Object p : instanceRest.getChildren(clause.getId(), 
								ClauseInstanceJpaImpl.class)) {
							if (p instanceof ParagraphInstanceJpaImpl) {
								ParagraphInstance paragraph = (ParagraphInstance) p;
								validate(paragraph);
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
		for (Object s : instanceRest.getChildren(document.getId(), AdvancedDocumentInstanceJpaImpl.class, true)) {
			if (s instanceof SectionInstance) {
				SectionInstance section = (SectionInstance) s;
				validate(section);
				for (Object c : instanceRest.getChildren(section.getId(), 
						SectionInstance.class, true)) {
					if (c instanceof ClauseInstance) {
						ClauseInstance clause = (ClauseInstance) c;
						validate(clause);
						for (Object p : instanceRest.getChildren(clause.getId(), 
								ClauseInstance.class, true)) {
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
		for (Object s : instanceRest.getChildren(projectId, document.getContent().getContentCd(), 
				AdvancedDocumentInstanceJpaImpl.class)) {
			if (s instanceof SectionInstanceJpaImpl) {
				SectionInstance section = (SectionInstance) s;
				for (Object c : instanceRest.getChildren(projectId, section.getContent().getContentCd(), 
						SectionInstanceJpaImpl.class)) {
					if (c instanceof ClauseInstanceJpaImpl) {
						ClauseInstance clause = (ClauseInstance) c;
						for (Object p : instanceRest.getChildren(projectId, clause.getContent().getContentCd(), 
								ClauseInstanceJpaImpl.class)) {
							if (p instanceof ParagraphInstanceJpaImpl) {
								assertNotNull(((Instance) p).getId());
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
		for (Object s : instanceRest.getChildren(projectId, document.getContent().getContentCd(), 
				AdvancedDocumentInstanceJpaImpl.class, true)) {
			if (s instanceof SectionInstanceJpaImpl) {
				SectionInstance section = (SectionInstance) s;
				validate(section);
				for (Object c : instanceRest.getChildren(projectId, section.getContent().getContentCd(), 
						SectionInstanceJpaImpl.class, true)) {
					if (c instanceof ClauseInstanceJpaImpl) {
						ClauseInstance clause = (ClauseInstance) c;
						validate(clause);
						assertNotNull(clause.getParagraphs().get(0).getId());
						for (Object p : instanceRest.getChildren(projectId, clause.getContent().getContentCd(), 
								ClauseInstanceJpaImpl.class, true)) {
							if (p instanceof ParagraphInstanceJpaImpl) {
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
		instanceRest.delete(document);		
		contentRest.delete(document.getContent());
	}

	@Test
	public void bb_GetInvalidParamTest() {
		Instance instance = createInstance(createContent());
		String code = instance.getContent().getContentCd();
		
		assertNull(instanceRest.get(0L, InstanceJpaImpl.class));
		assertNull(instanceRest.get(99999999L, InstanceJpaImpl.class));
		assertNull(instanceRest.get("TEST_PROJECT_ID", "Snicklefritz", InstanceJpaImpl.class));
		assertNull(instanceRest.get("Snicklefritz", code, InstanceJpaImpl.class));
		try {
			instanceRest.get("TEST_PROJECT_ID", null, InstanceJpaImpl.class);
			throw new IllegalStateException();
		} catch (HttpServerErrorException e) {
			assertEquals(e.getClass(), HttpServerErrorException.class);
		}
		try {
			instanceRest.get("TEST_PROJECT_ID", "", InstanceJpaImpl.class);
			throw new IllegalStateException();
		} catch (HttpServerErrorException e) {
			assertEquals(e.getClass(), HttpServerErrorException.class);
		}
		try {
			instanceRest.get((Long) null, InstanceJpaImpl.class);
			throw new IllegalStateException();
		} catch (HttpServerErrorException e) {
			assertEquals(e.getClass(), HttpServerErrorException.class);
		}
		try {
			instanceRest.get("", code, InstanceJpaImpl.class);
			throw new IllegalStateException();
		} catch (HttpServerErrorException e) {
			assertEquals(e.getClass(), HttpServerErrorException.class);
		}
		try {
			instanceRest.get(null, code, InstanceJpaImpl.class);
			throw new IllegalStateException();
		} catch (HttpServerErrorException e) {
			assertEquals(e.getClass(), HttpServerErrorException.class);
		}
		instanceRest.delete(instance);
		contentRest.delete(instance.getContent());
	}

//	@Test FIXME
	public void ca_GetByProjectAndCodeLikeTest() {
		Instance instance = createInstance(createContent());
		assertNotNull(instanceRest.getByProjectAndCodeLike(
				instance.getProjectId(), "TEST", Instance.class));
		instanceRest.delete(instance);
		contentRest.delete(instance.getContent());
	}

	@Test
	public void cb_GetByProjectAndCodeLikeInvalidParamTest() {
		Instance instance = createInstance(createContent());
		String projectId = instance.getProjectId();
		assertNotNull(instance.getId());
		assertNull(instanceRest.getByProjectAndCodeLike(null, "TEST", 
				InstanceJpaImpl.class));
		assertNull(instanceRest.getByProjectAndCodeLike("", 
				"TEST", InstanceJpaImpl.class));
		assertNull(instanceRest.getByProjectAndCodeLike("Snicklefritz", 
				"TEST", InstanceJpaImpl.class));
		assertNull(instanceRest.getByProjectAndCodeLike(projectId, 
				"", InstanceJpaImpl.class));
		assertNull(instanceRest.getByProjectAndCodeLike(projectId, 
				null, InstanceJpaImpl.class));
		assertNull(instanceRest.getByProjectAndCodeLike(projectId, 
				"Snicklefritz", InstanceJpaImpl.class));
		instanceRest.delete(instance);
		contentRest.delete(instance.getContent());		
	}
	
	@Test
	public void xx_DeleteTest() {
		Instance instance = createInstance(createContent());
		instanceRest.delete(instance);
		contentRest.delete(instance.getContent());
		assertNull(instanceRest.get(instance.getId(), InstanceJpaImpl.class));
		instance = createInstance(createContent());
		instanceRest.delete(instance);
		assertNull(instanceRest.get(instance.getId()));
//		service.deleteAll();
		contentRest.delete(instance.getContent());
	}

	private Content createContent() {
		return createContent(1);
	}
	
	private Content createContent(int seed) {
		Content content = (Content) contentRest.merge(makeTestContent(seed), ContentJpaImpl.class);
		validate(content);
		return content;		
	}
	
	private Instance createInstance(Content content) {
		Instance instance = (Instance) instanceRest.merge(new InstanceJpaImpl(content, 
				TestUtils.getRandomTestString(2)), InstanceJpaImpl.class);
		validate(instance);
		return instance;
	}
	
	private AdvancedDocument createDocument() {
		AdvancedDocument document =  (AdvancedDocument) contentRest.merge(makeTestDocument(), AdvancedDocumentJpaImpl.class);
		validate(document);
		return document;
	}

	private AdvancedDocumentInstance createDocumentInstance(AdvancedDocument document) {
		AdvancedDocumentInstance instance = (AdvancedDocumentInstance) instanceRest.merge(new AdvancedDocumentInstanceJpaImpl(
				document, TestUtils.getRandomTestString(1)), AdvancedDocumentInstanceJpaImpl.class);
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