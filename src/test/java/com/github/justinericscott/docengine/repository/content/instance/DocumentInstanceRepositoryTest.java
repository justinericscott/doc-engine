package com.github.justinericscott.docengine.repository.content.instance;

import static org.junit.Assert.*;

import static com.github.justinericscott.docengine.util.TestUtils.TestConstants.*;

import java.util.Collection;
import java.util.TreeSet;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.github.justinericscott.docengine.models.Content.Clause;
import com.github.justinericscott.docengine.models.Content.Document;
import com.github.justinericscott.docengine.models.Content.Paragraph;
import com.github.justinericscott.docengine.models.Content.Section;
import com.github.justinericscott.docengine.models.Instance.ClauseInstance;
import com.github.justinericscott.docengine.models.Instance.DocumentInstance;
import com.github.justinericscott.docengine.models.Instance.ParagraphInstance;
import com.github.justinericscott.docengine.models.Instance.SectionInstance;
import com.github.justinericscott.docengine.repository.content.DocumentInstanceRepository;
import com.github.justinericscott.docengine.repository.content.DocumentRepository;
import com.github.justinericscott.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * 
 *         Tests for the Instance Repository.
 * @param <T>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DocumentInstanceRepositoryTest extends AbstractTest {

	@Autowired
	private DocumentRepository _documents;

	@Autowired
	private DocumentInstanceRepository _instances;

	@Test
	public void a_SaveTest() {
		
		// Happy path...
		Document document = makeTestDocument();
		document = _documents.save(document);
		assertNotNull(document);
		assertTrue(document.isValid(true));
		DocumentInstance documentInstance = new DocumentInstance(document, TEST_PROJECT_ID_VALUE);
		documentInstance = _instances.save(documentInstance);
		assertNotNull(documentInstance);
		assertTrue(documentInstance.isValid(true));
		document = documentInstance.getDocument();
		assertNotNull(document);
		assertTrue(document.isValid(true));

		Collection<Document> documents = makeTestDocuments(7);
		documents = (Collection<Document>) _documents.save(documents);
		assertNotNull(documents);
		assertFalse(documents.isEmpty());
		final Collection<DocumentInstance> list = new TreeSet<DocumentInstance>();
		documents.forEach(d -> {
			assertTrue(d.isValid(true));
			list.add(new DocumentInstance(d, TEST_PROJECT_ID_VALUE));
		});
		Collection<DocumentInstance> documentInstances = new TreeSet<DocumentInstance>();
		documentInstances = (Collection<DocumentInstance>) _instances.save(list);
		assertNotNull(documentInstances);
		assertFalse(documentInstances.isEmpty());
		documentInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			Document d = i.getDocument();
			assertNotNull(d);
			assertTrue(d.isValid(true));
		});

		try {
			_instances.save((DocumentInstance) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_instances.save(new DocumentInstance(document, null));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_instances.save(new DocumentInstance((Document) null, TEST_PROJECT_ID_VALUE));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_instances.save(new DocumentInstance(makeTestDocument(), TEST_PROJECT_ID_VALUE));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
	}

	@Test
	public void b_FindTest() {
		
		// Happy path...
		Document document = _documents.save(makeTestDocument());
		assertNotNull(document);
		assertTrue(document.isValid(true));
		final String contentCd = document.getContentCd();
		DocumentInstance documentInstance = new DocumentInstance(document, TEST_PROJECT_ID_VALUE);
		documentInstance = _instances.save(documentInstance);
		assertNotNull(documentInstance);
		assertTrue(documentInstance.isValid(true));
		final Long id = documentInstance.getId();

		documentInstance = _instances.findOne(id);
		assertNotNull(documentInstance);
		assertTrue(documentInstance.isValid(true));
		document = documentInstance.getDocument();
		assertNotNull(document);
		assertTrue(document.isValid(true));
		documentInstance = _instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, contentCd);
		assertNotNull(documentInstance);
		assertTrue(documentInstance.isValid(true));
		document = documentInstance.getDocument();
		assertNotNull(document);
		assertTrue(document.isValid(true));

		Collection<DocumentInstance> documentInstances = (Collection<DocumentInstance>) _instances.findAll(); 
		assertNotNull(documentInstances);
		assertFalse(documentInstances.isEmpty());
		documentInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			Document d = i.getDocument();
			assertNotNull(d);
			assertTrue(d.isValid(true));
		});

		documentInstances = (Collection<DocumentInstance>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "%TEST%");
		assertNotNull(documentInstances);
		assertFalse(documentInstances.isEmpty());
		documentInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			Document d = i.getDocument();
			assertNotNull(d);
			assertTrue(d.isValid(true));
		});
		
		// Break it...		
		assertNull(_instances.findOne(0L));
		assertNull(_instances.findOne(99999999L));
		assertNull(_instances.findByProjectIdAndContentContentCd("", contentCd));
		assertNull(_instances.findByProjectIdAndContentContentCd((String) null, contentCd));
		assertNull(_instances.findByProjectIdAndContentContentCd("Snicklefritz", contentCd));
		assertNull(_instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, null));
		assertNull(_instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, ""));
		assertNull(_instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, "Snicklefritz"));

		documentInstances = (Collection<DocumentInstance>) _instances.findByProjectIdAndContentContentCdLike(null, TEST_CODE_PREFIX_DOCUMENT);
		assertTrue(documentInstances.isEmpty());
		documentInstances = (Collection<DocumentInstance>) _instances.findByProjectIdAndContentContentCdLike("", TEST_CODE_PREFIX_DOCUMENT);
		assertTrue(documentInstances.isEmpty());
		documentInstances = (Collection<DocumentInstance>) _instances.findByProjectIdAndContentContentCdLike("Snicklefritz", TEST_CODE_PREFIX_DOCUMENT);
		assertTrue(documentInstances.isEmpty());
		documentInstances = (Collection<DocumentInstance>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "");
		assertTrue(documentInstances.isEmpty());
		documentInstances = (Collection<DocumentInstance>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "Snicklefritz");
		assertTrue(documentInstances.isEmpty());
		try {
			_instances.findOne((Long) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
	}
	
	@Test
	public void c_DiscriminatorTest() {
		final String contentCd = "DOCUMENT_INSTANCE_DISCRIMINATOR_TEST_" + uuid();
		Document cx = new Document(contentCd, "BLAH BLAH BLAH");
		cx = _documents.save(cx);
		final DocumentInstance x = new DocumentInstance(cx, TEST_PROJECT_ID_VALUE);
		final DocumentInstance y = _instances.save(x);
		assertNull(y.getDiscriminator());
//		final DocumentInstance z = _instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, contentCd);
//		assertEquals(DocumentInstance.class.getSimpleName(), z.getDiscriminator());
	}

	@Test
	public void d_ChildrenTest() {
		Document document = makeTestDocument();
		Section section = makeTestSection();
		document.addSection(section);
		Clause clause = makeTestClause();
		section.addClause(clause);
		Paragraph paragraph = makeTestParagraph();
		clause.addParagraph(paragraph);
		
		document = _documents.save(document);
		assertNotNull(document);
		assertTrue(document.isValid(true));
		assertFalse(document.getSections().isEmpty());
		
		
		section = document.getSections().iterator().next();
		assertNotNull(section);
		assertTrue(section.isValid(true));
		assertFalse(section.getClauses().isEmpty());
		
		clause = section.getClauses().iterator().next();
		assertNotNull(clause);
		assertTrue(clause.isValid(true));
		assertFalse(clause.getParagraphs().isEmpty());
		
		paragraph = clause.getParagraphs().iterator().next();
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));
		assertNotNull(paragraph.getClause());
		assertTrue(paragraph.getClause().isValid(true));
		
		DocumentInstance documentInstance = new DocumentInstance(document, TEST_PROJECT_ID_VALUE);
		documentInstance = _instances.save(documentInstance);
		assertNotNull(documentInstance);
		assertTrue(documentInstance.isValid(true));
		Long id = documentInstance.getId();
		assertFalse(documentInstance.getSections().isEmpty());

		SectionInstance sectionInstance = documentInstance.getSections().iterator().next();		
		assertNotNull(sectionInstance);
		assertTrue(sectionInstance.isValid(true));
		assertNotNull(sectionInstance.getDocument());
		assertFalse(sectionInstance.getClauses().isEmpty());
		
		ClauseInstance clauseInstance = sectionInstance.getClauses().iterator().next();		
		assertNotNull(clauseInstance);
		assertTrue(clauseInstance.isValid(true));
		assertNotNull(clauseInstance.getSection());
		assertFalse(clauseInstance.getParagraphs().isEmpty());
		
		ParagraphInstance paragraphInstance = clauseInstance.getParagraphs().iterator().next();
		assertNotNull(paragraphInstance);
		assertTrue(paragraphInstance.isValid(true));
		assertNotNull(paragraphInstance.getClause());
		assertTrue(paragraphInstance.getClause().isValid(true));	
		
		documentInstance = _instances.findOne(id);
		assertNotNull(documentInstance);
		assertTrue(documentInstance.isValid(true));
		_instances.delete(id);
		_documents.delete(document.getId());
	}
	
	@Test
	public void x_DeleteTest() {
		final String projectId = TEST_PROJECT_ID_VALUE;
		Document document = _documents.save(makeTestDocument());
		assertNotNull(document);
		assertTrue(document.isValid(true));
		DocumentInstance documentInstance = new DocumentInstance(document, projectId);
		documentInstance = _instances.save(documentInstance);
		assertNotNull(documentInstance);
		assertTrue(documentInstance.isValid(true));
		Long id = documentInstance.getId();

		_instances.delete(id);
		assertNull(_instances.findOne(documentInstance.getId()));
		documentInstance = new DocumentInstance(document, projectId);
		documentInstance = _instances.save(documentInstance);
		assertNotNull(documentInstance);
		assertTrue(documentInstance.isValid(true));
		id = documentInstance.getId();
		_instances.delete(documentInstance);
		assertNull(_instances.findOne(documentInstance.getId()));
		
		Collection<Document> documents = makeTestDocuments(7);
		documents = (Collection<Document>) _documents.save(documents);
		assertNotNull(documents);
		assertFalse(documents.isEmpty());
		final Collection<DocumentInstance> list = new TreeSet<DocumentInstance>();
		documents.forEach(d -> {
			assertTrue(d.isValid(true));
			list.add(new DocumentInstance(d, projectId));
		});
		Collection<DocumentInstance> documentInstances = (Collection<DocumentInstance>) _instances.save(list);
		assertNotNull(documentInstances);
		assertFalse(documentInstances.isEmpty());
		documentInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			Document d = i.getDocument();
			assertNotNull(d);
			assertTrue(d.isValid(true));
		});		
		_instances.delete(documentInstances);
		documentInstances.forEach(i -> {
			assertNull(_instances.findOne(i.getId()));;
		});
		
		_instances.deleteAll();
		documentInstances = (Collection<DocumentInstance>) _instances.findAll();
		assertTrue(documentInstances.isEmpty());
	}
}