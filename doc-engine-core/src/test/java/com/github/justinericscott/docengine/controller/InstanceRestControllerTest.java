package com.github.justinericscott.docengine.controller;

import static org.junit.Assert.*;

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

import com.github.justinericscott.docengine.types.ClauseInstanceJpaImpl;
import com.github.justinericscott.docengine.types.ClauseInstances;
import com.github.justinericscott.docengine.types.ClauseJpaImpl;
import com.github.justinericscott.docengine.types.ContentJpaImpl;
import com.github.justinericscott.docengine.types.Contents;
import com.github.justinericscott.docengine.types.DocumentInstanceJpaImpl;
import com.github.justinericscott.docengine.types.DocumentJpaImpl;
import com.github.justinericscott.docengine.types.InstanceJpaImpl;
import com.github.justinericscott.docengine.types.Instances;
import com.github.justinericscott.docengine.types.ParagraphInstanceJpaImpl;
import com.github.justinericscott.docengine.types.ParagraphInstances;
import com.github.justinericscott.docengine.types.ParagraphJpaImpl;
import com.github.justinericscott.docengine.types.SectionInstanceJpaImpl;
import com.github.justinericscott.docengine.types.SectionInstances;
import com.github.justinericscott.docengine.types.SectionJpaImpl;
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
		ContentJpaImpl content = new ContentJpaImpl(TEST_CONTENT_CODE_PREFIX + uuid(), "TEST_BODY");
		content = _contents.save(content, ContentJpaImpl.class);
		assertNotNull(content);
		assertTrue(content.isValid(true));

		InstanceJpaImpl instance = new InstanceJpaImpl(content, projectId);
		instance = _instances.save(instance, InstanceJpaImpl.class);
		assertNotNull(instance);
		assertTrue(instance.isValid(true));

		// Merge complex one at a time
		DocumentJpaImpl doc = new DocumentJpaImpl(TEST_DOCUMENT_CODE_PREFIX + uuid(), "TEST_BODY");
		doc = _contents.save(doc, DocumentJpaImpl.class);
		assertNotNull(doc);
		assertTrue(doc.isValid(true));
		DocumentInstanceJpaImpl docInst = new DocumentInstanceJpaImpl(doc, projectId);
		docInst = _instances.save(docInst, DocumentInstanceJpaImpl.class);
		assertNotNull(docInst);
		Long id = docInst.getId();
		assertTrue(docInst.isValid(true));

		SectionJpaImpl sec = new SectionJpaImpl(TEST_SECTION_CODE_PREFIX + uuid(), "TEST_BODY");
		doc.addSection(sec);
		sec = _contents.save(sec, SectionJpaImpl.class);
		assertNotNull(sec);
		assertTrue(sec.isValid(true));
		SectionInstanceJpaImpl secInst = new SectionInstanceJpaImpl(sec, projectId);
		docInst.addSection(secInst);
		secInst = _instances.save(secInst, SectionInstanceJpaImpl.class);
		assertNotNull(secInst);
		assertTrue(secInst.isValid(true));

		ClauseJpaImpl cla = new ClauseJpaImpl(TEST_CLAUSE_CODE_PREFIX + uuid(), "TEST_BODY");
		sec.addClause(cla);
		cla = _contents.save(cla, ClauseJpaImpl.class);
		assertNotNull(cla);
		assertTrue(cla.isValid(true));
		ClauseInstanceJpaImpl claInst = new ClauseInstanceJpaImpl(cla, projectId);
		secInst.addClause(claInst);
		claInst = _instances.save(claInst, ClauseInstanceJpaImpl.class);
		assertNotNull(claInst);
		assertTrue(claInst.isValid(true));

		ParagraphJpaImpl para = new ParagraphJpaImpl(TEST_PARAGRAPH_CODE_PREFIX + uuid(), "TEST_BODY");
		cla.addParagraph(para);
		para = _contents.save(para, ParagraphJpaImpl.class);
		assertNotNull(para);
		assertTrue(para.isValid(true));
		ParagraphInstanceJpaImpl paraInst = new ParagraphInstanceJpaImpl(para, projectId);
		claInst.addParagraph(paraInst);
		paraInst = _instances.save(paraInst, ParagraphInstanceJpaImpl.class);
		assertNotNull(paraInst);
		assertTrue(paraInst.isValid(true));

		docInst = _instances.findOne(id, DocumentInstanceJpaImpl.class);
		assertNotNull(docInst);
		assertTrue(docInst.isValid(true));

		docInst = null;
		doc = null;
		
		// Merge complex all at once
		docInst = createDocumentInstance();
		assertNotNull(docInst);
		assertTrue(docInst.isValid(true));

		// Merge a list
		List<ContentJpaImpl> list = new ArrayList<ContentJpaImpl>();
		List<InstanceJpaImpl> instances = new ArrayList<InstanceJpaImpl>();
		list.add(createContent());
		list.add(createContent());
		list.add(createContent());
		Contents contents = new Contents(list.toArray(new ContentJpaImpl[list.size()]));
		contents = _contents.save(contents, Contents.class);
		assertNotNull(contents);
		for (ContentJpaImpl c : contents.getContents()) {
			assertTrue(c.isValid(true));
			instances.add(new InstanceJpaImpl(c, projectId));
		}
		Instances saved = _instances.save(new Instances(instances), Instances.class);
		assertNotNull(saved);
		for (InstanceJpaImpl i : saved.getInstances()) {
			assertTrue(i.isValid(true));
		}
		content = createContent();
		// Null Object
		assertNull(_instances.save(null, InstanceJpaImpl.class));
		// Empty Project ID
		assertNull(_instances.save(new InstanceJpaImpl(content), InstanceJpaImpl.class));
		// No Content
		assertNull(_instances.save(new InstanceJpaImpl(TEST_PROJECT_ID_PREFIX + uuid()), InstanceJpaImpl.class));

		// Content with no ID
		content = new ContentJpaImpl(TEST_CONTENT_CODE_PREFIX + uuid(), "TEST_BODY");
		InstanceJpaImpl in = new InstanceJpaImpl(content, TEST_PROJECT_ID_PREFIX + uuid());
		assertNull(_instances.save(in, InstanceJpaImpl.class));
		// Duplicates
		instance = createInstance();
		assertNull(_instances.save(new InstanceJpaImpl(content, instance.getProjectId()), InstanceJpaImpl.class));
	}

	@Test
	public void b_FindTest() {
		// Get All
		Collection<InstanceJpaImpl> instances = new ArrayList<InstanceJpaImpl>();
		instances.add(createInstance());
		instances.add(createInstance());
		instances.add(createInstance());
		Instances inst = new Instances(instances);
		inst = _instances.save(inst, Instances.class);
		assertNotNull(inst);
		Instances all = (Instances) _instances.findAll();
		assertNotNull(all);
		for (InstanceJpaImpl i : all.getInstances()) {
			assertTrue(i.isValid(true));
		}

		// Get Instance
		InstanceJpaImpl instance = createInstance();
		instance = _instances.findOne(instance.getId(), InstanceJpaImpl.class);

		instance = _instances.findByProjectIdAndCode(instance.getProjectId(), instance.getContent().getContentCd(),
				InstanceJpaImpl.class);

		// Get sub-classes, no eagerKids
		DocumentInstanceJpaImpl document = createDocumentInstance();
		String projectId = document.getProjectId();
		Long id = document.getId();
		// String code = document.getContent().getContentCd();

		assertTrue(
				DocumentInstanceJpaImpl.class.equals(_instances.findOne(id, DocumentInstanceJpaImpl.class, true).getClass()));
		assertTrue(DocumentInstanceJpaImpl.class.equals(_instances
				.findByProjectIdAndCode(projectId, document.getDocument().getContentCd(), DocumentInstanceJpaImpl.class, true)
				.getClass()));
		assertTrue(_instances.findOne(id, DocumentInstanceJpaImpl.class).getSections().isEmpty());

		assertTrue(SectionInstanceJpaImpl.class.equals(_instances
				.findOne(document.getSections().iterator().next().getId(), SectionInstanceJpaImpl.class, true).getClass()));
		assertTrue(SectionInstanceJpaImpl.class.equals(_instances.findByProjectIdAndCode(projectId,
				document.getSections().iterator().next().getSection().getContentCd(), SectionInstanceJpaImpl.class, true)
				.getClass()));
//		assertNull(((SectionInstanceJpaImpl) _instances.findOne(document.getSectionsList().iterator().next().getId(),
//				SectionInstanceJpaImpl.class, true)).getClausesList());

		assertTrue(ClauseInstanceJpaImpl.class.equals(
				_instances.findOne(document.getSections().iterator().next().getClauses().iterator().next().getId(),
						ClauseInstanceJpaImpl.class, true).getClass()));

		assertTrue(
				ClauseInstanceJpaImpl.class.equals(_instances
						.findByProjectIdAndCode(projectId, document.getSections().iterator().next().getClauses()
								.iterator().next().getClause().getContentCd(), ClauseInstanceJpaImpl.class, true)
						.getClass()));

//		assertNull(((ClauseInstanceJpaImpl) _instances.findOne(
//				document.getSectionsList().iterator().next().getClausesList().iterator().next().getId(),
//				ClauseInstanceJpaImpl.class, true)).getParagraphsList());

		assertTrue(ParagraphInstanceJpaImpl.class
				.equals(_instances.findOne(document.getSections().iterator().next().getClauses().iterator().next()
						.getParagraphs().iterator().next().getId(), ParagraphInstanceJpaImpl.class).getClass()));
		assertTrue(ParagraphInstanceJpaImpl.class.equals(_instances
				.findByProjectIdAndCode(projectId,
						document.getSections().iterator().next().getClauses().iterator().next().getParagraphs()
								.iterator().next().getParagraph().getContentCd(),
						ParagraphInstanceJpaImpl.class)
				.getClass()));

		// Get Children by ID
		assertNotNull(document.getId());
		SectionInstances sections = _instances.getChildren(document.getId(), SectionInstances.class);
		for (SectionInstanceJpaImpl s : sections.getSectionsList()) {
			assertTrue(s.getClauses().isEmpty());
			ClauseInstances clauses = _instances.getChildren(s.getId(), ClauseInstances.class);
			for (ClauseInstanceJpaImpl c : clauses.getClausesList()) {
				assertTrue(c.getParagraphs().isEmpty());
				ParagraphInstances paragraphs = _instances.getChildren(c.getId(), ParagraphInstances.class);
				for (ParagraphInstanceJpaImpl p : paragraphs.getParagraphsList()) {
					assertTrue(p.isValid(true));
				}
			}
		}

		// Get Children by ID with kids
		for (SectionInstanceJpaImpl s : _instances.getChildren(document.getId(), SectionInstances.class, true)
				.getSectionsList()) {
			if (SectionInstanceJpaImpl.class.isInstance(s)) {
				for (Object c : _instances.getChildren(s.getId(), ClauseInstances.class, true).getClausesList()) {
					if (c instanceof ClauseInstanceJpaImpl) {
						ClauseInstanceJpaImpl clause = ClauseInstanceJpaImpl.class.cast(c);
						for (Object p : _instances.getChildren(clause.getId(), ParagraphInstances.class, true)
								.getParagraphsList()) {
							if (p instanceof ParagraphInstanceJpaImpl) {

							}
						}
					}
				}
			}
		}

		// Get Children by code
		DocumentJpaImpl doc = document.getDocument();
		assertNotNull(doc);
		String code = doc.getContentCd();
		assertNotNull(code);
		System.err.println(code);
		SectionInstances secs = _instances.getChildren(projectId, code, SectionInstances.class);
		assertNotNull(secs);
		Iterable<SectionInstanceJpaImpl> iter = secs.getSectionsList();

		for (SectionInstanceJpaImpl s : iter) {
			if (s instanceof SectionInstanceJpaImpl) {
				SectionInstanceJpaImpl section = (SectionInstanceJpaImpl) s;
				for (Object c : _instances
						.getChildren(projectId, section.getSection().getContentCd(), ClauseInstances.class)
						.getClausesList()) {
					if (c instanceof ClauseInstanceJpaImpl) {
						ClauseInstanceJpaImpl clause = (ClauseInstanceJpaImpl) c;
						for (Object p : _instances
								.getChildren(projectId, clause.getClause().getContentCd(), ParagraphInstances.class)
								.getParagraphsList()) {
							if (p instanceof ParagraphInstanceJpaImpl) {
								assertNotNull(((InstanceJpaImpl) p).getId());
							}
						}
					}
				}
			}
		}

		// Get Children by code with kids
		for (InstanceJpaImpl s : _instances.getChildren(projectId, code, SectionInstances.class, true).getSectionsList()) {
			if (s instanceof SectionInstanceJpaImpl) {
				SectionInstanceJpaImpl section = (SectionInstanceJpaImpl) s;
				code = section.getSection().getContentCd();
				for (InstanceJpaImpl c : _instances.getChildren(projectId, code, ClauseInstances.class, true)
						.getClausesList()) {
					if (c instanceof ClauseInstanceJpaImpl) {
						ClauseInstanceJpaImpl clause = (ClauseInstanceJpaImpl) c;
						assertNotNull(clause.getParagraphs().iterator().next().getId());
						_instances.getChildren(projectId, clause.getClause().getContentCd(),
								ClauseInstanceJpaImpl.class, true);
						for (Object p : _instances.getChildren(projectId, clause.getClause().getContentCd(),
								ParagraphInstances.class, true).getParagraphsList()) {
							if (p instanceof ParagraphInstanceJpaImpl) {

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
		assertNotNull(_instances.findByProjectIdAndCodeLike(instance.getProjectId(), "%TEST%", InstanceJpaImpl.class));

		instance = createInstance();
		code = instance.getContent().getContentCd();

		assertNull(_instances.findOne(0L, InstanceJpaImpl.class));
		assertNull(_instances.findOne(99999999L, InstanceJpaImpl.class));
		assertNull(_instances.findByProjectIdAndCode("TEST_PROJECT_ID", "Snicklefritz", InstanceJpaImpl.class));
		assertNull(_instances.findByProjectIdAndCode("Snicklefritz", code, InstanceJpaImpl.class));
		try {
			_instances.findByProjectIdAndCode("TEST_PROJECT_ID", null, InstanceJpaImpl.class);
		} catch (Exception e) {
			assertEquals(e.getClass(), HttpClientErrorException.class);
		}
		try {
			_instances.findByProjectIdAndCode("TEST_PROJECT_ID", "", InstanceJpaImpl.class);
		} catch (Exception e) {
			assertEquals(e.getClass(), HttpClientErrorException.class);
		}
		try {
			_instances.findOne((Long) null, InstanceJpaImpl.class);
		} catch (Exception e) {
			assertEquals(e.getClass(), HttpClientErrorException.class);
		}
		try {
			_instances.findByProjectIdAndCode("", code, InstanceJpaImpl.class);
		} catch (Exception e) {
			assertEquals(e.getClass(), HttpClientErrorException.class);
		}
		try {
			_instances.findByProjectIdAndCode(null, code, InstanceJpaImpl.class);
		} catch (Exception e) {
			assertEquals(e.getClass(), HttpClientErrorException.class);
		}
		instance = createInstance();
		projectId = instance.getProjectId();
		assertNotNull(instance.getId());
		assertNull(_instances.findByProjectIdAndCodeLike(null, "TEST", null));
		assertNull(_instances.findByProjectIdAndCodeLike("", "TEST", null));
		assertNull(_instances.findByProjectIdAndCodeLike("Snicklefritz", "TEST", null));
		assertNull(_instances.findByProjectIdAndCodeLike(projectId, "", null));
		assertNull(_instances.findByProjectIdAndCodeLike(projectId, null, null));
		assertNull(_instances.findByProjectIdAndCodeLike(projectId, "Snicklefritz", null));

	}

	@Test
	public void xx_DeleteTest() {
		InstanceJpaImpl instance = createInstance();
		_instances.delete(instance);
		_contents.delete(instance.getContent());
		assertNull(_instances.findOne(instance.getId(), InstanceJpaImpl.class));
		instance = createInstance();
		_instances.delete(instance);
		assertNull(_instances.findOne(instance.getId()));
		_contents.delete(instance.getContent());
	}
	
	private ContentJpaImpl createContent() {
		ContentJpaImpl content = makeTestContent();
		content = _contents.save(content, ContentJpaImpl.class);
		assertNotNull(content);
		assertTrue(content.isValid(true));
		return content;
	}

	private DocumentJpaImpl createDocument() {
		DocumentJpaImpl document = makeTestDocumentComplete();
		document = _contents.save(document, DocumentJpaImpl.class);
		assertNotNull(document);
		assertTrue(document.isValid(true));
		return document;
	}

	private DocumentInstanceJpaImpl createDocumentInstance() {
		DocumentJpaImpl document = createDocument();
		DocumentInstanceJpaImpl documentInstance = new DocumentInstanceJpaImpl(document, TEST_PROJECT_ID_VALUE);
		documentInstance = _instances.save(documentInstance, DocumentInstanceJpaImpl.class);
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

	private InstanceJpaImpl createInstance() {
		InstanceJpaImpl instance = new InstanceJpaImpl(createContent(), TEST_PROJECT_ID_VALUE);
		instance = _instances.save(instance, InstanceJpaImpl.class);
		assertNotNull(instance);
		assertTrue(instance.isValid(true));
		assertNotNull(instance.getContent());
		assertTrue(instance.getContent().isValid(true));
		return instance;
	}
}