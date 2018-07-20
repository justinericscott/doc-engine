package com.github.justinericscott.docengine.repository.content;

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
		documents = (Collection<Document>) _documents.saveAll(documents);
		assertNotNull(documents);
		assertFalse(documents.isEmpty());
		final Collection<DocumentInstance> list = new TreeSet<DocumentInstance>();
		documents.forEach(d -> {
			assertTrue(d.isValid(true));
			list.add(new DocumentInstance(d, TEST_PROJECT_ID_VALUE));
		});
		Collection<DocumentInstance> documentInstances = new TreeSet<DocumentInstance>();
		documentInstances = (Collection<DocumentInstance>) _instances.saveAll(list);
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
		documentInstance = _instances.findById(id).get();
		assertNotNull(documentInstance);
		assertTrue(documentInstance.isValid(true));
		document = documentInstance.getDocument();
		assertNotNull(document);
		assertTrue(document.isValid(true));
		documentInstance = _instances.findOptionalByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, contentCd).get();
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
	}
	
	@Test
	public void bx_FindBreakTest() {
		// Break it...		
		Document document = _documents.save(makeTestDocument());
		assertNotNull(document);
		assertTrue(document.isValid(true));
		final String contentCd = document.getContentCd();
		assertFalse(_instances.findById(0L).isPresent());
		assertFalse(_instances.findById(99999999L).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd("", contentCd).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd((String) null, contentCd).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd("Snicklefritz", contentCd).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, null).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, "").isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, "Snicklefritz").isPresent());
		Collection<DocumentInstance> documentInstances = (Collection<DocumentInstance>) _instances.findByProjectIdAndContentContentCdLike(null, TEST_CODE_PREFIX_DOCUMENT);
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
			_instances.findById((Long) null).get();
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
		documentInstance = _instances.findById(id).get();
		assertNotNull(documentInstance);
		assertTrue(documentInstance.isValid(true));
		_instances.deleteById(id);
		_documents.deleteById(document.getId());
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
		_instances.deleteById(id);
		assertFalse(_instances.findById(documentInstance.getId()).isPresent());
		documentInstance = new DocumentInstance(document, projectId);
		documentInstance = _instances.save(documentInstance);
		assertNotNull(documentInstance);
		assertTrue(documentInstance.isValid(true));
		id = documentInstance.getId();
		_instances.delete(documentInstance);
		assertFalse(_instances.findById(documentInstance.getId()).isPresent());
		Collection<Document> documents = makeTestDocuments(7);
		documents = (Collection<Document>) _documents.saveAll(documents);
		assertNotNull(documents);
		assertFalse(documents.isEmpty());
		final Collection<DocumentInstance> list = new TreeSet<DocumentInstance>();
		documents.forEach(d -> {
			assertTrue(d.isValid(true));
			list.add(new DocumentInstance(d, projectId));
		});
		Collection<DocumentInstance> documentInstances = (Collection<DocumentInstance>) _instances.saveAll(list);
		assertNotNull(documentInstances);
		assertFalse(documentInstances.isEmpty());
		documentInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			Document d = i.getDocument();
			assertNotNull(d);
			assertTrue(d.isValid(true));
		});		
		_instances.deleteAll(documentInstances);
		documentInstances.forEach(i -> {
			assertFalse(_instances.findById(i.getId()).isPresent());;
		});
		_instances.deleteAll();
		documentInstances = (Collection<DocumentInstance>) _instances.findAll();
		assertTrue(documentInstances.isEmpty());
	}
}