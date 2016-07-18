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
import com.itgfirm.docengine.data.DataConstants;
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
 * Tests for the Instance Repository.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InstanceRepoTest extends AbstractTest {
	private static final Logger LOG = LogManager.getLogger(InstanceRepoTest.class);
	@Autowired
	private InstanceRepo instanceRepo;
	@Autowired @Qualifier("advanced")
	private AdvancedContentService contentService;
	
	@Test
	public void aa_MergeTest() {
		// Merge 1		
		Instance instance = createInstance(createContent());
		instanceRepo.delete(instance);
		contentService.delete(instance.getContent());
		
		// Merge complex one at a time
		String projectId = TestUtils.getRandomTestString(1);
		AdvancedDocument document = (AdvancedDocument) contentService.merge(new AdvancedDocumentJpaImpl(
				TestUtils.getRandomTestString(2), "TEST_BODY"));
		String code = document.getContentCd();
		assertNotNull(document.getId());
		AdvancedDocumentInstance dInstance = new AdvancedDocumentInstanceJpaImpl(document, projectId);
		dInstance = (AdvancedDocumentInstance) instanceRepo.merge(dInstance);
		
		Section section = new SectionJpaImpl(TestUtils.getRandomTestString(3),
				"TEST_BODY");
		document.addSection(section);
		section = (Section) contentService.merge(section);
		assertNotNull(section.getId());
		SectionInstance sInstance = new SectionInstanceJpaImpl(section, projectId);
		dInstance.addSection(sInstance);
		sInstance = (SectionInstance) instanceRepo.merge(sInstance);
		
		Clause clause = new ClauseJpaImpl(TestUtils.getRandomTestString(4),
				"TEST_BODY");
		section.addClause(clause);
		clause = (Clause) contentService.merge(clause);
		assertNotNull(clause.getId());
		ClauseInstance cInstance = new ClauseInstanceJpaImpl(clause, projectId);
		sInstance.addClause(cInstance);
		cInstance = (ClauseInstance) instanceRepo.merge(cInstance);
		
		Paragraph paragraph = new ParagraphJpaImpl(TestUtils.getRandomTestString(5),
				"TEST_BODY");
		clause.addParagraph(paragraph);
		paragraph = (Paragraph) contentService.merge(paragraph);
		assertNotNull(paragraph.getId());
		ParagraphInstance pInstance = new ParagraphInstanceJpaImpl(paragraph, projectId);
		cInstance.addParagraph(pInstance);
		pInstance = (ParagraphInstance) instanceRepo.merge(pInstance);
		
		dInstance = (AdvancedDocumentInstance) instanceRepo.initialize(instanceRepo.get(projectId, code));
		validate(dInstance);
		instanceRepo.delete(dInstance);
		contentService.delete(dInstance.getContent());
		
		// Merge complex all at once
		dInstance = createDocumentInstance(createDocument());
		instanceRepo.delete(dInstance);
		contentService.delete(dInstance.getContent());
	}
	
	@Test
	public void ab_MergeInvalidParamTest() {
		Content content = createContent();
		// Null Object
		assertNull(instanceRepo.merge(null));
		// Empty Project ID
		assertNull(instanceRepo.merge(
				new InstanceJpaImpl(content)).getId());		
		// No Content
		assertNull(instanceRepo.merge(
				new InstanceJpaImpl(TestUtils.getRandomTestString(1))).getId());
		// Content with no ID
		assertNull(instanceRepo.merge(new InstanceJpaImpl(new ContentJpaImpl(
				TestUtils.getRandomTestString(2), 
				"TEST_BODY"), TestUtils.getRandomTestString(3))).getId());
		// Duplicates
		Instance instance = createInstance(content); 
		assertNull(instanceRepo.merge(new InstanceJpaImpl(content, instance.getProjectId())).getId());
		instanceRepo.delete(instance);
		contentService.delete(instance.getContent());
	}
	
	@Test
	public void ba_GetTest() {
		// Get All
		String projectId = TestUtils.getRandomTestString(1);
		List<Content> contents = new ArrayList<Content>();
		List<Instance> instances = new ArrayList<Instance>();
		contents.add(createContent(1));
		contents.add(createContent(2));
		contents.add(createContent(3));
		contents.add(createContent(4));
		contents.add(createContent(5));
		for (Content c : contents) {
			instances.add(instanceRepo.merge(new InstanceJpaImpl(c, projectId)));
		}
		for (Instance i : instances) {
			validate(i);
			instanceRepo.delete(i);
			contentService.delete(i.getContent());
		}

		// Get Instance
		Instance instance = createInstance(createContent());
		projectId = instance.getProjectId();
		validate(instanceRepo.get(instance.getId()));
		validate(instanceRepo.get(instance.getProjectId(), instance.getContent().getContentCd()));		
		instanceRepo.delete(instance);
		contentService.delete(instance.getContent());
		
		// Get sub-classes, no eagerKids
		AdvancedDocumentInstance document = createDocumentInstance(createDocument());
		projectId = document.getProjectId();
		assertTrue(AdvancedDocumentInstanceJpaImpl.class.equals(instanceRepo.get(
				document.getId()).getClass()));
		assertTrue(AdvancedDocumentInstanceJpaImpl.class.equals(instanceRepo.get(projectId,
				document.getContent().getContentCd()).getClass()));
		try {
			((AdvancedDocumentInstance) instanceRepo.get(document.getId())).getSections().get(0);
			throw new IllegalStateException();
		} catch (LazyInitializationException e) {
			assertTrue(e.getClass().equals(
					LazyInitializationException.class));	
		}
		
		assertTrue(SectionInstanceJpaImpl.class.equals(instanceRepo.get(
				document.getSections().get(0).getId()).getClass()));
		assertTrue(SectionInstanceJpaImpl.class.equals(instanceRepo.get(projectId,
				document.getSections().get(0).getContent().getContentCd()).getClass()));
		try {
			((SectionInstance) instanceRepo.get(document.getSections().get(0).getId())).getClauses().get(0);
			throw new IllegalStateException();
		} catch (LazyInitializationException e) {
			assertTrue(e.getClass().equals(
					LazyInitializationException.class));	
		}		
		
		assertTrue(ClauseInstanceJpaImpl.class.equals(instanceRepo.get(
				document.getSections().get(0).getClauses().get(0).getId()).getClass()));
		assertTrue(ClauseInstanceJpaImpl.class.equals(instanceRepo.get(projectId, 
				document.getSections().get(0).getClauses().get(0).getContent().getContentCd()).getClass()));
		try {
			((ClauseInstance) instanceRepo.get(document.getSections().get(0).getClauses().get(0).getId())).getParagraphs().get(0);
			throw new IllegalStateException();
		} catch (LazyInitializationException e) {
			assertTrue(e.getClass().equals(LazyInitializationException.class));	
		}		
		
		assertTrue(ParagraphInstanceJpaImpl.class.equals(instanceRepo.get(
				document.getSections().get(0).getClauses().get(0).getParagraphs().get(0).getId()).getClass()));
		assertTrue(ParagraphInstanceJpaImpl .class.equals(instanceRepo.get(projectId,
				document.getSections().get(0).getClauses().get(0).getParagraphs().get(0).getContent().getContentCd()).getClass()));		

		// Get sub-classes, then initialize children 
		document = (AdvancedDocumentInstance) instanceRepo.get(document.getId());
		document = (AdvancedDocumentInstance) instanceRepo.initialize(document);
		validate(document);		
		instanceRepo.delete(document);
		contentService.delete(document.getContent());
		
	}
	
	@Test
	public void bb_GetInvalidParamTest() {
		assertNull(instanceRepo.get());
		Instance instance = createInstance(createContent());
		validate(instance);
		String code = instance.getContent().getContentCd();
		
		assertNotNull(instance.getId());
		assertNotNull(instance.getContent().getId());
		
		assertNull(instanceRepo.get(0L));
		assertNull(instanceRepo.get(99999999L));
		assertNull(instanceRepo.get(null));
		
		assertNull(instanceRepo.get("", code));
		assertNull(instanceRepo.get(null, code));
		assertNull(instanceRepo.get("Snicklefritz", code));
		assertNull(instanceRepo.get("TEST_PROJECT_ID", null));
		assertNull(instanceRepo.get("TEST_PROJECT_ID", ""));
		assertNull(instanceRepo.get("TEST_PROJECT_ID", "Snicklefritz"));
		
		instanceRepo.delete(instance);
		contentService.delete(instance.getContent());
	}
	
	@Test
	public void ca_GetByProjectAndCodeLikeTest() {
		Instance instance = createInstance(createContent());
		assertNotNull(instance.getId());
		assertNotNull(instanceRepo.getByProjectAndCodeLike(
				instance.getProjectId(), "TEST"));
		instanceRepo.delete(instance);
		contentService.delete(instance.getContent());
	}
	
	@Test
	public void cb_GetByProjectAndCodeLikeInvalidParamTest() {
		Instance instance = createInstance(createContent());
		String projectId = instance.getProjectId();
		assertNotNull(instance.getId());
		assertNull(instanceRepo.getByProjectAndCodeLike(null, 
						TestUtils.TEST_CODE_PREFIX));
		assertNull(instanceRepo.getByProjectAndCodeLike("", 
				TestUtils.TEST_CODE_PREFIX));
		assertNull(instanceRepo.getByProjectAndCodeLike("Snicklefritz", 
				TestUtils.TEST_CODE_PREFIX));
		assertNull(instanceRepo.getByProjectAndCodeLike(projectId, 
				""));
		assertNull(instanceRepo.getByProjectAndCodeLike(projectId, 
				null));
		assertNull(instanceRepo.getByProjectAndCodeLike(projectId, 
				"Snicklefritz"));
		instanceRepo.delete(instance);
		contentService.delete(instance.getContent());		
	}
	
	@Test
	public void da_GetWithQueryTest() {
		Instance instance = createInstance(createContent());
		String projectId = instance.getProjectId();
		String[] values = { projectId, instance.getContent().getContentCd() };
		String[] paramNames = { DataConstants.PARAM_PROJECT_ID, DataConstants.PARAM_CONTENT_CD };
				
		assertNotNull(instanceRepo.getWithQuery(
				DataConstants.GET_INSTANCE_BY_PROJECT_ID_AND_CONTENT_CD, 
				paramNames, values).get(0));
		instanceRepo.delete(instance);
		contentService.delete(instance.getContent());
	}
	
	@Test
	public void db_GetWithQueryInvalidParamTest() {
		Instance instance = createInstance(createContent());
		String projectId = instance.getProjectId();
		String code = instance.getContent().getContentCd();
		String[] values = { projectId, code };
		String[] badValues1 = { "Snicklefritz", code };
		String[] badValues2 = { projectId, "Snicklefritz" };
		String[] emptyValues = { "", "" };		
		String[] paramNames = { DataConstants.PARAM_PROJECT_ID, DataConstants.PARAM_CONTENT_CD };
		String[] badParamNames1 = { "Snicklefritz", DataConstants.PARAM_CONTENT_CD };
		String[] badParamNames2 = { DataConstants.PARAM_PROJECT_ID, "Snicklefritz" };
		String[] emptyParamNames = { "", "" };
		
		assertNotNull(instanceRepo.getWithQuery(
				DataConstants.GET_INSTANCE_BY_PROJECT_ID_AND_CONTENT_CD, 
				paramNames, values).get(0));
	
		assertNull(instanceRepo.getWithQuery(
				DataConstants.GET_INSTANCE_BY_PROJECT_ID_AND_CONTENT_CD, 
				badParamNames1, values));
		assertNull(instanceRepo.getWithQuery(
				DataConstants.GET_INSTANCE_BY_PROJECT_ID_AND_CONTENT_CD, 
				badParamNames2, values));
		assertNull(instanceRepo.getWithQuery(
				DataConstants.GET_INSTANCE_BY_PROJECT_ID_AND_CONTENT_CD, 
				emptyParamNames, values));
		assertNull(instanceRepo.getWithQuery(
				DataConstants.GET_INSTANCE_BY_PROJECT_ID_AND_CONTENT_CD, 
				(String[]) null, values));
		assertNull(instanceRepo.getWithQuery(
				DataConstants.GET_INSTANCE_BY_PROJECT_ID_AND_CONTENT_CD, 
				paramNames, badValues1));
		assertNull(instanceRepo.getWithQuery(
				DataConstants.GET_INSTANCE_BY_PROJECT_ID_AND_CONTENT_CD, 
				paramNames, badValues2));
		assertNull(instanceRepo.getWithQuery(
				DataConstants.GET_INSTANCE_BY_PROJECT_ID_AND_CONTENT_CD, 
				paramNames, emptyValues));
		assertNull(instanceRepo.getWithQuery(
				DataConstants.GET_INSTANCE_BY_PROJECT_ID_AND_CONTENT_CD, 
				paramNames, null));
		
		instanceRepo.delete(instance);
		contentService.delete(instance.getContent());
	}
	
	@Test
	public void xx_DeleteTest() {
		Instance instance = createInstance(createContent());
		instanceRepo.delete(instance);
		contentService.delete(instance.getContent());		
		assertNull(instanceRepo.get(instance.getId()));		
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
		Instance instance = instanceRepo.merge(new InstanceJpaImpl(content, 
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
		AdvancedDocumentInstance instance = (AdvancedDocumentInstance) instanceRepo.merge(new AdvancedDocumentInstanceJpaImpl(
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