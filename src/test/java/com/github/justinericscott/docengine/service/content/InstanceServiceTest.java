/**TODO: License
 */
package com.github.justinericscott.docengine.service.content;

import static org.junit.Assert.*;

import static com.github.justinericscott.docengine.util.TestUtils.TestConstants.*;

import java.util.Collection;
import java.util.TreeSet;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

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
import com.github.justinericscott.docengine.models.Instances.ParagraphInstances;
import com.github.justinericscott.docengine.models.Instances.SectionInstances;
import com.github.justinericscott.docengine.service.content.ContentService;
import com.github.justinericscott.docengine.service.content.InstanceService;
import com.github.justinericscott.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * 
 *         Tests for the Instance Service
 * @param <T>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InstanceServiceTest extends AbstractTest {
	private static final Logger LOG = LoggerFactory.getLogger(InstanceServiceTest.class);

	@Autowired
	private InstanceService _instances;

	@Autowired
	private ContentService _contents;

	@Test
	public void a_SaveTest() {
		String projectId = TEST_PROJECT_ID_PREFIX + uuid();
		Content content = createContent();
		// Save one
		Instance instance = createInstance();
		assertNotNull(instance);
		assertNotNull(instance.getId());

		// Save advanced one at a time
		Document doc = (Document) _contents
				.save(new Document(TEST_CODE_PREFIX_CONTENT + uuid(), "TEST_BODY"));
		assertNotNull(doc);
		assertNotNull(doc.getId());
		DocumentInstance document = new DocumentInstance(doc, projectId);
		document = _instances.save(document);
		assertNotNull(document);
		assertNotNull(document.getId());

		Section sec = new Section(TEST_CODE_PREFIX_CONTENT + uuid(), "TEST_BODY");
		doc.addSection(sec);
		sec = (Section) _contents.save(sec);
		assertNotNull(sec);
		assertNotNull(sec.getId());
		SectionInstance section = new SectionInstance(sec, projectId);
		document.addSection(section);
		section = _instances.save(section);
		assertNotNull(section);
		assertNotNull(section.getId());

		Clause cla = new Clause(TEST_CODE_PREFIX_CONTENT + uuid(), "TEST_BODY");
		sec.addClause(cla);
		cla = (Clause) _contents.save(cla);
		assertNotNull(cla);
		assertNotNull(cla.getId());
		ClauseInstance clause = new ClauseInstance(cla, projectId);
		section.addClause(clause);
		clause = _instances.save(clause);
		assertNotNull(clause);
		assertNotNull(clause.getId());

		Paragraph para = new Paragraph(TEST_CODE_PREFIX_CONTENT + uuid(), "TEST_BODY");
		cla.addParagraph(para);
		para = (Paragraph) _contents.save(para);
		assertNotNull(para);
		assertNotNull(para.getId());
		ParagraphInstance paragraph = new ParagraphInstance(para, projectId);
		clause.addParagraph(paragraph);
		paragraph = _instances.save(paragraph);
		assertNotNull(paragraph);
		assertNotNull(paragraph.getId());

//		document = _instances.findOne(document.getId(), DocumentInstance.class, true);
//		assertNotNull(document);
//		assertNotNull(document.getId());
//		validate(document);

		// Save advanced all at once
		document = createDocumentInstance();
		assertNotNull(document);
		assertNotNull(document.getId());
		validate(document);

		// Save a collection
		Collection<Instance> instances = new TreeSet<Instance>();
		Contents contents = createContents(5);
		contents = _contents.save(contents);
		for (Content c : contents.getContents()) {
			instances.add(new Instance(c, projectId));
		}
		// contents.getContents().forEach(c -> {
		// instances.add(new InstanceJpaImpl(c, projectId));
		// });
		Collection<Instance> iter = _instances.save(new Instances(instances)).getInstances();
		assertNotNull(iter);
		assertTrue(iter.iterator().hasNext());
		iter.forEach(inst -> {
			validate(inst);
		});
		content = createContent();
		instance = new Instance();
		instance.setProjectId(projectId);
		assertNull(_instances.save(instance));
		instance = new Instance(content);
		assertNull(_instances.save(instance));
		content = new Content("TEST_BODY");
		instance = new Instance(content, projectId);
		assertNull(_instances.save(instance));
	}

	@Test
	public void b_FindTest() {
		String projectId = TEST_PROJECT_ID_PREFIX + uuid();
		final String pId = projectId;

		// Find all
		Collection<Instance> instances = new TreeSet<Instance>();
		Contents contents = createContents(5);
		contents = _contents.save(contents);
		for (Content c : contents.getContents()) {
			instances.add(new Instance(c, pId));
		}
		Collection<Instance> iter = _instances.save(new Instances(instances)).getInstances();
		assertNotNull(iter);
		assertTrue(iter.iterator().hasNext());
		iter = _instances.findAll().getInstances();
		assertNotNull(iter);
		assertTrue(iter.iterator().hasNext());
		iter.forEach(i -> {
			validate(i);
		});

		// Find One
		Instance instance = createInstance();
		instance = _instances.findOne(instance.getId(), Instance.class);
		assertNotNull(instance);
		assertNotNull(instance.getId());

		// Find by Project ID and Content CD
		instance = _instances.findByProjectIdAndCode(instance.getProjectId(), instance.getContent().getContentCd(),
				Instance.class);
		assertNotNull(instance);
		assertNotNull(instance.getId());

		DocumentInstance document = createDocumentInstance();
		projectId = document.getProjectId();
		Long id = document.getId();
		String code = document.getDocument().getContentCd();
		Class<?> type = DocumentInstance.class;

		// Find sub-classes, no eagerKids
		assertEquals(type, _instances.findOne(id, DocumentInstance.class).getClass());
		assertEquals(type,
				_instances.findByProjectIdAndCode(projectId, code, DocumentInstance.class).getClass());

		SectionInstance section = document.getSections().iterator().next();
		id = section.getId();
		code = section.getSection().getContentCd();
		type = SectionInstance.class;

		assertEquals(type, _instances.findOne(id, SectionInstance.class).getClass());
		assertEquals(type, _instances.findByProjectIdAndCode(projectId, code, SectionInstance.class).getClass());

		ClauseInstance clause = section.getClauses().iterator().next();
		id = clause.getId();
		code = clause.getClause().getContentCd();
		type = ClauseInstance.class;

		assertEquals(type, _instances.findOne(id, ClauseInstance.class).getClass());
		assertEquals(type, _instances.findByProjectIdAndCode(projectId, code, ClauseInstance.class).getClass());

		ParagraphInstance paragraph = clause.getParagraphs().iterator().next();
		id = paragraph.getId();
		code = paragraph.getParagraph().getContentCd();
		type = ParagraphInstance.class;

		assertEquals(type, _instances.findOne(id, ParagraphInstance.class).getClass());
		assertEquals(type,
				_instances.findByProjectIdAndCode(projectId, code, ParagraphInstance.class).getClass());

		id = document.getId();
		code = document.getDocument().getContentCd();

		// Get Children by ID
		Collection<SectionInstance> sections = _instances.getChildren(id, SectionInstances.class)
				.getSectionsList();
		assertNotNull(sections);
		assertFalse(sections.isEmpty());
		sections.forEach(s -> {
			Long sectionId = s.getId();
			Collection<ClauseInstance> clauses = _instances.getChildren(sectionId, ClauseInstances.class)
					.getClausesList();
			assertNotNull(clauses);
			assertFalse(clauses.isEmpty());
			clauses.forEach(c -> {
				Long clauseId = c.getId();
				Collection<ParagraphInstance> paragraphs = _instances.getChildren(clauseId, ParagraphInstances.class)
						.getParagraphsList();
				assertNotNull(paragraphs);
				assertFalse(paragraphs.isEmpty());
				paragraphs.forEach(p -> {
					validate(p);
				});
			});
		});

		// Get Children by ID with kids
		for (SectionInstance s : _instances.getChildren(document.getId(), SectionInstances.class)
				.getSectionsList()) {
			validate(s);
			for (ClauseInstance c : _instances.getChildren(s.getId(), ClauseInstances.class)
					.getClausesList()) {
				validate(c);
				for (ParagraphInstance p : _instances.getChildren(c.getId(), ParagraphInstances.class)
						.getParagraphsList()) {
					validate((ParagraphInstance) p);
				}
			}
		}

		// Get Children by code

		code = document.getDocument().getContentCd();

		SectionInstances sectionInstances = _instances.getChildren(projectId, code, SectionInstances.class);
		assertNotNull(sectionInstances);
		sections = sectionInstances.getSectionsList();
		assertNotNull(sections);
		for (SectionInstance s : sections) {
			Section sec = (Section) s.getSection();
			assertNotNull(sec);
			code = sec.getContentCd();
			assertNotNull(code);
			ClauseInstances clauseInstances = _instances.getChildren(projectId, code, ClauseInstances.class);
			assertNotNull(clauseInstances);
			Iterable<ClauseInstance> clauses = clauseInstances.getClausesList();
			assertNotNull(clauses);
			for (ClauseInstance c : clauses) {
				Clause cla = (Clause) c.getClause();
				assertNotNull(cla);
				code = cla.getContentCd();
				ParagraphInstances paraInstances = _instances.getChildren(c.getProjectId(), code,
						ParagraphInstances.class);
				assertNotNull(paraInstances);
				Iterable<ParagraphInstance> paras = paraInstances.getParagraphsList();
				assertNotNull(paras);
				for (ParagraphInstance p : paras) {
					assertNotNull(p.getProjectId());
					assertNotNull(p.getParagraph());
					assertNotNull(p.getParagraph().getContentCd());
					assertNotNull(p.getId());
				}
			}
		}

		code = document.getDocument().getContentCd();

		// Get Children by code with kids
		for (SectionInstance s : _instances.getChildren(projectId, code, SectionInstances.class)
				.getSectionsList()) {
			code = s.getSection().getContentCd();
			for (ClauseInstance c : _instances.getChildren(projectId, code, ClauseInstances.class)
					.getClausesList()) {
				code = c.getClause().getContentCd();
				for (ParagraphInstance p : _instances
						.getChildren(projectId, code, ParagraphInstances.class).getParagraphsList()) {
					assertNotNull(p.getId());
				}
			}
		}

		// Project ID and Content Code Like
		instance = createInstance();

		assertNotNull(instance.getId());
		Instances result = _instances.findByProjectIdAndCodeLike(instance.getProjectId(), "%TEST%");
		assertNotNull(result);

		// Invalids
		instance = createInstance();
		instance = _instances.save(instance);

		code = instance.getContent().getContentCd();

		assertNotNull(instance.getId());
		assertNotNull(instance.getContent().getId());

		assertNull(_instances.findOne(0L));
		assertNull(_instances.findOne(99999999L));
		assertNull(_instances.findOne((Long) null));

		assertNull(_instances.findByProjectIdAndCode("", code));
		assertNull(_instances.findByProjectIdAndCode(null, code));
		assertNull(_instances.findByProjectIdAndCode("Snicklefritz", code));
		assertNull(_instances.findByProjectIdAndCode("TEST_PROJECT_ID", null));
		assertNull(_instances.findByProjectIdAndCode("TEST_PROJECT_ID", ""));
		assertNull(_instances.findByProjectIdAndCode("TEST_PROJECT_ID", "Snicklefritz"));

		instance = createInstance();
		assertNotNull(instance.getId());
		assertNull(_instances.findByProjectIdAndCodeLike(null, TEST_CODE_PREFIX_CONTENT));
		assertNull(_instances.findByProjectIdAndCodeLike("", TEST_CODE_PREFIX_CONTENT));
		assertNull(_instances.findByProjectIdAndCodeLike("Snicklefritz", TEST_CODE_PREFIX_CONTENT));
		assertNull(_instances.findByProjectIdAndCodeLike(instance.getProjectId(), ""));
		assertNull(_instances.findByProjectIdAndCodeLike(instance.getProjectId(), null));
		assertNull(_instances.findByProjectIdAndCodeLike(instance.getProjectId(), "Snicklefritz"));

	}

	@Test
	public void x_DeleteTest() {
		Instance instance = createInstance();
		String projectId = instance.getProjectId();
		String code = instance.getContent().getContentCd();
		_instances.delete(instance.getId());
		_contents.delete(instance.getContent());
		assertNull(_instances.findOne(instance.getId()));
		instance = createInstance();
		code = instance.getContent().getContentCd();
		projectId = instance.getProjectId();
		_instances.delete(projectId, code);
		assertNull(_instances.findOne(instance.getId()));
		// instanceService.deleteAll();
		_contents.delete(instance.getContent());
	}

	protected void validate(Document document) {
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

	protected void validate(DocumentInstance document) {
		assertNotNull(document);
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

	private Instance createInstance() {
		Instance instance = new Instance(createContent(), TEST_PROJECT_ID_VALUE);
		instance = _instances.save(instance);
		assertNotNull(instance);
		assertTrue(instance.isValid(true));
		assertNotNull(instance.getContent());
		assertTrue(instance.getContent().isValid(true));
		return instance;
	}

	private DocumentInstance createDocumentInstance() {
		Document document = createDocument();
		DocumentInstance documentInstance = new DocumentInstance(document, TEST_PROJECT_ID_VALUE);
		documentInstance = _instances.save(documentInstance);
		assertNotNull(documentInstance);
		assertTrue(documentInstance.isValid(true));
		assertNotNull(documentInstance.getDocument());
		assertTrue(documentInstance.getDocument().isValid(true));
		assertFalse(documentInstance.getSections().isEmpty());
		assertTrue(documentInstance.getSections().iterator().next().isValid(true));
		assertTrue(documentInstance.getSections().iterator().next().getSection().isValid(true));
		assertFalse(documentInstance.getSections().iterator().next().getClauses().isEmpty());
		assertTrue(documentInstance.getSections().iterator().next().getClauses().iterator().next().isValid(true));
		assertTrue(documentInstance.getSections().iterator().next().getClauses().iterator().next().getClause()
				.isValid(true));
		assertFalse(documentInstance.getSections().iterator().next().getClauses().iterator().next().getParagraphs()
				.isEmpty());
		assertTrue(documentInstance.getSections().iterator().next().getClauses().iterator().next().getParagraphs()
				.iterator().next().isValid(true));
		assertTrue(documentInstance.getSections().iterator().next().getClauses().iterator().next().getParagraphs()
				.iterator().next().getParagraph().isValid(true));
		return documentInstance;
	}

	private Content createContent() {
		Content content = makeTestContent();
		content = _contents.save(content);
		assertNotNull(content);
		assertTrue(content.isValid(true));
		return content;
	}

	private Contents createContents(int count) {
		Collection<Content> contents = new TreeSet<Content>();
		for (int i = 0; i < count; i++) {
			contents.add(createContent());
		}
		return new Contents(contents);
	}

	private Document createDocument() {
		Document document = makeTestDocumentComplete();
		document = _contents.save(document);
		assertNotNull(document);
		assertTrue(document.isValid(true));
		return document;
	}
}