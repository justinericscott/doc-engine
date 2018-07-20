package com.github.justinericscott.docengine.controller;

import static org.junit.Assert.*;

import static com.github.justinericscott.docengine.util.TestUtils.TestConstants.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.HttpClientErrorException;

import com.github.justinericscott.docengine.models.Content;
import com.github.justinericscott.docengine.models.Content.Clause;
import com.github.justinericscott.docengine.models.Content.Document;
import com.github.justinericscott.docengine.models.Content.Paragraph;
import com.github.justinericscott.docengine.models.Content.Section;
import com.github.justinericscott.docengine.models.Contents;
import com.github.justinericscott.docengine.models.Instance;
import com.github.justinericscott.docengine.models.Instance.ClauseInstance;
import com.github.justinericscott.docengine.models.Instance.DocumentInstance;
import com.github.justinericscott.docengine.models.Instance.ParagraphInstance;
import com.github.justinericscott.docengine.models.Instance.SectionInstance;
import com.github.justinericscott.docengine.models.Instances;
import com.github.justinericscott.docengine.models.Instances.ClauseInstances;
import com.github.justinericscott.docengine.models.Instances.DocumentInstances;
import com.github.justinericscott.docengine.models.Instances.ParagraphInstances;
import com.github.justinericscott.docengine.models.Instances.SectionInstances;
import com.github.justinericscott.docengine.util.AbstractTest;
import com.github.justinericscott.docengine.util.rest.RestClient;

/**
 * @author Justin Scott
 * 
 *         Instance REST Controller Test
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InstanceRestControllerTest extends AbstractTest {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(InstanceRestControllerTest.class);

	@Autowired
	@Qualifier(RestClient.AUTOWIRE_QUALIFIER_INSTANCE)
	private RestClient _instances;

	@Autowired
	@Qualifier(RestClient.AUTOWIRE_QUALIFIER_CONTENT)
	private RestClient _contents;

	@Test
	public void a_SaveTest() {
		final String projectId = TEST_PROJECT_ID_PREFIX + uuid();
		// Merge 1
		Content content = new Content(TEST_CODE_PREFIX_CONTENT + uuid(), "TEST_BODY");
		content = _contents.save(content, Content.class);
		assertNotNull(content);
		assertTrue(content.isValid(true));

		Instance instance = new Instance(content, projectId);
		instance = _instances.save(instance, Instance.class);
		assertNotNull(instance);
		assertTrue(instance.isValid(true));

		// Merge complex one at a time
		Document doc = new Document(TEST_CODE_PREFIX_DOCUMENT + uuid(), "TEST_BODY");
		doc = _contents.save(doc, Document.class);
		assertNotNull(doc);
		assertTrue(doc.isValid(true));
		DocumentInstance docInst = new DocumentInstance(doc, projectId);
		docInst = _instances.save(docInst, DocumentInstance.class);
		assertNotNull(docInst);
//		Long id = docInst.getId();
		assertTrue(docInst.isValid(true));

		Section sec = new Section(TEST_CODE_PREFIX_SECTION + uuid(), "TEST_BODY");
		doc.addSection(sec);
		sec = _contents.save(sec, Section.class);
		assertNotNull(sec);
		assertTrue(sec.isValid(true));
		SectionInstance secInst = new SectionInstance(sec, projectId);
		docInst.addSection(secInst);
		secInst = _instances.save(secInst, SectionInstance.class);
		assertNotNull(secInst);
		assertTrue(secInst.isValid(true));

		Clause cla = new Clause(TEST_CODE_PREFIX_CLAUSE + uuid(), "TEST_BODY");
		sec.addClause(cla);
		cla = _contents.save(cla, Clause.class);
		assertNotNull(cla);
		assertTrue(cla.isValid(true));
		ClauseInstance claInst = new ClauseInstance(cla, projectId);
		secInst.addClause(claInst);
		claInst = _instances.save(claInst, ClauseInstance.class);
		assertNotNull(claInst);
		assertTrue(claInst.isValid(true));

		Paragraph para = new Paragraph(TEST_CODE_PREFIX_PARAGRAPH + uuid(), "TEST_BODY");
		cla.addParagraph(para);
		para = _contents.save(para, Paragraph.class);
		assertNotNull(para);
		assertTrue(para.isValid(true));
		ParagraphInstance paraInst = new ParagraphInstance(para, projectId);
		claInst.addParagraph(paraInst);
		paraInst = _instances.save(paraInst, ParagraphInstance.class);
		assertNotNull(paraInst);
		assertTrue(paraInst.isValid(true));

//		docInst = _instances.findOne(id, DocumentInstance.class, true);
//		assertNotNull(docInst);
//		assertTrue(docInst.isValid(true));

		docInst = null;
		doc = null;
		
		// Merge complex all at once
		docInst = createDocumentInstance();
		assertNotNull(docInst);
		assertTrue(docInst.isValid(true));

		// Merge a list
		List<Content> list = new ArrayList<Content>();
		List<Instance> instances = new ArrayList<Instance>();
		list.add(createContent());
		list.add(createContent());
		list.add(createContent());
		Contents contents = new Contents(list.toArray(new Content[list.size()]));
		contents = _contents.save(contents, Contents.class);
		assertNotNull(contents);
		for (Content c : contents.getContents()) {
			assertTrue(c.isValid(true));
			instances.add(new Instance(c, projectId));
		}
		Instances saved = _instances.save(new Instances(instances), Instances.class);
		assertNotNull(saved);
		for (Instance i : saved.getInstances()) {
			assertTrue(i.isValid(true));
		}
		content = createContent();
		// Null Object
		assertNull(_instances.save(null, Instance.class));
		// Empty Project ID
		assertNull(_instances.save(new Instance(content), Instance.class));
		// No Content
		assertNull(_instances.save(new Instance(TEST_PROJECT_ID_PREFIX + uuid()), Instance.class));

		// Content with no ID
		content = new Content(TEST_CODE_PREFIX_CONTENT + uuid(), "TEST_BODY");
		Instance in = new Instance(content, TEST_PROJECT_ID_PREFIX + uuid());
		assertNull(_instances.save(in, Instance.class));
		// Duplicates
//		Instance inst = createInstance();
//		assertNull(_instances.save(new Instance(inst.getContent(), inst.getProjectId()), Instance.class));
	}

	@Test
	public void b_FindTest() {
		// Get Instance
		Instance instance = createInstance();
		instance = _instances.findOne(instance.getId(), Instance.class);
		assertNotNull(instance);
		assertTrue(instance.isValid(true));
		assertNotNull(instance.getContent());
		assertTrue(instance.getContent().isValid(true));
		instance = _instances.findByProjectIdAndCode(instance.getProjectId(), instance.getContent().getContentCd(),
				Instance.class);
		assertNotNull(instance);
		assertTrue(instance.isValid(true));
		assertNotNull(instance.getContent());
		assertTrue(instance.getContent().isValid(true));		
		// Get All
		Collection<Instance> instances = new ArrayList<Instance>();
		instances.add(createInstance());
		instances.add(createInstance());
		instances.add(createInstance());
//		Instances inst = new Instances(instances);
//		inst = _instances.save(inst, Instances.class);
//		assertNotNull(inst);
		createDocumentInstance();
		createDocumentInstance();
		DocumentInstances all = (DocumentInstances) _instances.findAll(DocumentInstances.class);
		assertNotNull(all);
		for (DocumentInstance i : all.getDocumentsList()) {
			assertTrue(i.isValid(true));
			assertNotNull(i.getContent());
			assertTrue(i.getContent().isValid(true));
		}
		// Get sub-classes, no eagerKids
		DocumentInstance document = createDocumentInstance();
		String projectId = document.getProjectId();
		Long id = document.getId();
//		String code = document.getContent().getContentCd();
		assertTrue(
				DocumentInstance.class.equals(_instances.findOne(id, DocumentInstance.class).getClass()));
		assertTrue(DocumentInstance.class.equals(_instances
				.findByProjectIdAndCode(projectId, document.getDocument().getContentCd(), DocumentInstance.class)
				.getClass()));
//		assertTrue(_instances.findOne(id, DocumentInstance.class).getSections().isEmpty());
		assertTrue(SectionInstance.class.equals(_instances
				.findOne(document.getSections().iterator().next().getId(), SectionInstance.class).getClass()));
		assertTrue(SectionInstance.class.equals(_instances.findByProjectIdAndCode(projectId,
				document.getSections().iterator().next().getSection().getContentCd(), SectionInstance.class)
				.getClass()));
//		assertNull(((SectionInstanceJpaImpl) _instances.findOne(document.getSectionsList().iterator().next().getId(),
//				SectionInstanceJpaImpl.class, true)).getClausesList());
		assertTrue(ClauseInstance.class.equals(
				_instances.findOne(document.getSections().iterator().next().getClauses().iterator().next().getId(),
						ClauseInstance.class).getClass()));
		assertTrue(
				ClauseInstance.class.equals(_instances
						.findByProjectIdAndCode(projectId, document.getSections().iterator().next().getClauses()
								.iterator().next().getClause().getContentCd(), ClauseInstance.class)
						.getClass()));
//		assertNull(((ClauseInstanceJpaImpl) _instances.findOne(
//				document.getSectionsList().iterator().next().getClausesList().iterator().next().getId(),
//				ClauseInstanceJpaImpl.class, true)).getParagraphsList());
		assertTrue(ParagraphInstance.class
				.equals(_instances.findOne(document.getSections().iterator().next().getClauses().iterator().next()
						.getParagraphs().iterator().next().getId(), ParagraphInstance.class).getClass()));
		assertTrue(ParagraphInstance.class.equals(_instances
				.findByProjectIdAndCode(projectId,
						document.getSections().iterator().next().getClauses().iterator().next().getParagraphs()
								.iterator().next().getParagraph().getContentCd(),
						ParagraphInstance.class)
				.getClass()));
		// Get Children by ID
		assertNotNull(document.getId());
		SectionInstances sections = _instances.getChildren(document.getId(), SectionInstances.class);
		for (SectionInstance s : sections.getSectionsList()) {
			assertFalse(s.getClauses().isEmpty());
			ClauseInstances clauses = _instances.getChildren(s.getId(), ClauseInstances.class);
			for (ClauseInstance c : clauses.getClausesList()) {
				assertFalse(c.getParagraphs().isEmpty());
				ParagraphInstances paragraphs = _instances.getChildren(c.getId(), ParagraphInstances.class);
				for (ParagraphInstance p : paragraphs.getParagraphsList()) {
					assertTrue(p.isValid(true));
				}
			}
		}
		// Get Children by ID with kids
		for (SectionInstance s : _instances.getChildren(document.getId(), SectionInstances.class)
				.getSectionsList()) {
			if (SectionInstance.class.isInstance(s)) {
				for (Object c : _instances.getChildren(s.getId(), ClauseInstances.class).getClausesList()) {
					if (c instanceof ClauseInstance) {
						ClauseInstance clause = ClauseInstance.class.cast(c);
						for (Object p : _instances.getChildren(clause.getId(), ParagraphInstances.class)
								.getParagraphsList()) {
							if (p instanceof ParagraphInstance) {

							}
						}
					}
				}
			}
		}
		// Get Children by code
		Document doc = document.getDocument();
		assertNotNull(doc);
		String code = doc.getContentCd();
		assertNotNull(code);
		SectionInstances secs = _instances.getChildren(projectId, code, SectionInstances.class);
		assertNotNull(secs);
		Iterable<SectionInstance> iter = secs.getSectionsList();
		for (SectionInstance s : iter) {
			if (s instanceof SectionInstance) {
				SectionInstance section = (SectionInstance) s;
				for (Object c : _instances
						.getChildren(projectId, section.getSection().getContentCd(), ClauseInstances.class)
						.getClausesList()) {
					if (c instanceof ClauseInstance) {
						ClauseInstance clause = (ClauseInstance) c;
						for (Object p : _instances
								.getChildren(projectId, clause.getClause().getContentCd(), ParagraphInstances.class)
								.getParagraphsList()) {
							if (p instanceof ParagraphInstance) {
								assertNotNull(((Instance) p).getId());
							}
						}
					}
				}
			}
		}
		// Get Children by code with kids
		for (Instance s : _instances.getChildren(projectId, code, SectionInstances.class).getSectionsList()) {
			if (s instanceof SectionInstance) {
				SectionInstance section = (SectionInstance) s;
				code = section.getSection().getContentCd();
				for (Instance c : _instances.getChildren(projectId, code, ClauseInstances.class)
						.getClausesList()) {
					if (c instanceof ClauseInstance) {
						ClauseInstance clause = (ClauseInstance) c;
						assertNotNull(clause.getParagraphs().iterator().next().getId());
						_instances.getChildren(projectId, clause.getClause().getContentCd(),
								ClauseInstance.class);
						for (Object p : _instances.getChildren(projectId, clause.getClause().getContentCd(),
								ParagraphInstances.class).getParagraphsList()) {
							if (p instanceof ParagraphInstance) {

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
		instance = createInstance();
//		assertNotNull(_instances.findByProjectIdAndCodeLike(instance.getProjectId(), "%R101%", Instances.class));
		instance = createInstance();
		code = instance.getContent().getContentCd();
		assertNull(_instances.findOne(0L, Instance.class));
		assertNull(_instances.findOne(99999999L, Instance.class));
		assertNull(_instances.findByProjectIdAndCode("TEST_PROJECT_ID", "Snicklefritz", Instance.class));
		assertNull(_instances.findByProjectIdAndCode("Snicklefritz", code, Instance.class));
		try {
			_instances.findByProjectIdAndCode("TEST_PROJECT_ID", null, Instance.class);
		} catch (Exception e) {
			assertEquals(e.getClass(), HttpClientErrorException.class);
		}
		try {
			_instances.findByProjectIdAndCode("TEST_PROJECT_ID", "", Instance.class);
		} catch (Exception e) {
			assertEquals(e.getClass(), HttpClientErrorException.class);
		}
		try {
			_instances.findOne((Long) null, Instance.class);
		} catch (Exception e) {
			assertEquals(e.getClass(), HttpClientErrorException.class);
		}
		try {
			_instances.findByProjectIdAndCode("", code, Instance.class);
		} catch (Exception e) {
			assertEquals(e.getClass(), HttpClientErrorException.class);
		}
		try {
			_instances.findByProjectIdAndCode(null, code, Instance.class);
		} catch (Exception e) {
			assertEquals(e.getClass(), HttpClientErrorException.class);
		}
		instance = createInstance();
		projectId = instance.getProjectId();
		assertNotNull(instance.getId());
//		assertNull(_instances.findByProjectIdAndCodeLike(null, "TEST", null));
//		assertNull(_instances.findByProjectIdAndCodeLike("", "TEST", null));
//		assertNull(_instances.findByProjectIdAndCodeLike("Snicklefritz", "TEST", null));
//		assertNull(_instances.findByProjectIdAndCodeLike(projectId, "", null));
//		assertNull(_instances.findByProjectIdAndCodeLike(projectId, null, null));
//		assertNull(_instances.findByProjectIdAndCodeLike(projectId, "Snicklefritz", null));
	}

	@Test
	public void xx_DeleteTest() {
		Instance instance = createInstance();
		_instances.delete(instance);
		_contents.delete(instance.getContent());
		assertNull(_instances.findOne(instance.getId(), Instance.class));
		instance = createInstance();
		_instances.delete(instance);
		assertNull(_instances.findOne(instance.getId()));
		_contents.delete(instance.getContent());
	}
	
	private Content createContent() {
		Content content = makeTestContent();
		content = _contents.save(content, Content.class);
		assertNotNull(content);
		assertTrue(content.isValid(true));
		return content;
	}

	private Document createDocument() {
		Document document = makeTestDocumentComplete();
		document = _contents.save(document, Document.class);
		assertNotNull(document);
		assertTrue(document.isValid(true));
		return document;
	}

	private DocumentInstance createDocumentInstance() {
		Document document = createDocument();
		DocumentInstance documentInstance = new DocumentInstance(document, TEST_PROJECT_ID_VALUE);
		documentInstance = _instances.save(documentInstance, DocumentInstance.class);
		assertNotNull(documentInstance);
		assertTrue(documentInstance.isValid(true));
		assertNotNull(documentInstance.getDocument());
		assertTrue(documentInstance.getDocument().isValid(true));
		assertFalse(documentInstance.getSections().isEmpty());
		assertTrue(documentInstance.getSections().iterator().next().isValid(true));
		assertTrue(documentInstance.getSections().iterator().next().getSection().isValid(true));
		assertFalse(documentInstance.getSections().iterator().next().getClauses().isEmpty());
		assertTrue(
				documentInstance.getSections().iterator().next().getClauses().iterator().next().isValid(true));
		assertTrue(documentInstance.getSections().iterator().next().getClauses().iterator().next().getClause()
				.isValid(true));
		assertFalse(documentInstance.getSections().iterator().next().getClauses().iterator().next()
				.getParagraphs().isEmpty());
		assertTrue(documentInstance.getSections().iterator().next().getClauses().iterator().next()
				.getParagraphs().iterator().next().isValid(true));
		assertTrue(documentInstance.getSections().iterator().next().getClauses().iterator().next()
				.getParagraphs().iterator().next().getParagraph().isValid(true));
		return documentInstance;
	}

	private Instance createInstance() {
		Instance instance = new Instance(createContent(), TEST_PROJECT_ID_VALUE);
		instance = _instances.save(instance, Instance.class);
		assertNotNull(instance);
		assertTrue(instance.isValid(true));
		assertNotNull(instance.getContent());
		assertTrue(instance.getContent().isValid(true));
		return instance;
	}
}