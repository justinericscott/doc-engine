package com.github.justinericscott.docengine.repository.content.instance;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.TreeSet;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.github.justinericscott.docengine.repository.content.DocumentInstanceRepository;
import com.github.justinericscott.docengine.repository.content.DocumentRepository;
import com.github.justinericscott.docengine.types.ClauseInstanceJpaImpl;
import com.github.justinericscott.docengine.types.ClauseJpaImpl;
import com.github.justinericscott.docengine.types.DocumentInstanceJpaImpl;
import com.github.justinericscott.docengine.types.DocumentJpaImpl;
import com.github.justinericscott.docengine.types.ParagraphInstanceJpaImpl;
import com.github.justinericscott.docengine.types.ParagraphJpaImpl;
import com.github.justinericscott.docengine.types.SectionInstanceJpaImpl;
import com.github.justinericscott.docengine.types.SectionJpaImpl;
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
		DocumentJpaImpl document = makeTestDocument();
		document = _documents.save(document);
		assertNotNull(document);
		assertTrue(document.isValid(true));
		DocumentInstanceJpaImpl documentInstance = new DocumentInstanceJpaImpl(document, TEST_PROJECT_ID_VALUE);
		documentInstance = _instances.save(documentInstance);
		assertNotNull(documentInstance);
		assertTrue(documentInstance.isValid(true));
		document = documentInstance.getDocument();
		assertNotNull(document);
		assertTrue(document.isValid(true));

		Collection<DocumentJpaImpl> documents = makeTestDocuments(7);
		documents = (Collection<DocumentJpaImpl>) _documents.save(documents);
		assertNotNull(documents);
		assertFalse(documents.isEmpty());
		final Collection<DocumentInstanceJpaImpl> list = new TreeSet<DocumentInstanceJpaImpl>();
		documents.forEach(d -> {
			assertTrue(d.isValid(true));
			list.add(new DocumentInstanceJpaImpl(d, TEST_PROJECT_ID_VALUE));
		});
		Collection<DocumentInstanceJpaImpl> documentInstances = new TreeSet<DocumentInstanceJpaImpl>();
		documentInstances = (Collection<DocumentInstanceJpaImpl>) _instances.save(list);
		assertNotNull(documentInstances);
		assertFalse(documentInstances.isEmpty());
		documentInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			DocumentJpaImpl d = i.getDocument();
			assertNotNull(d);
			assertTrue(d.isValid(true));
		});

		try {
			_instances.save((DocumentInstanceJpaImpl) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_instances.save(new DocumentInstanceJpaImpl(document, null));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_instances.save(new DocumentInstanceJpaImpl((DocumentJpaImpl) null, TEST_PROJECT_ID_VALUE));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_instances.save(new DocumentInstanceJpaImpl(makeTestDocument(), TEST_PROJECT_ID_VALUE));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
	}

	@Test
	public void b_FindTest() {
		
		// Happy path...
		DocumentJpaImpl document = _documents.save(makeTestDocument());
		assertNotNull(document);
		assertTrue(document.isValid(true));
		final String contentCd = document.getContentCd();
		DocumentInstanceJpaImpl documentInstance = new DocumentInstanceJpaImpl(document, TEST_PROJECT_ID_VALUE);
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

		Collection<DocumentInstanceJpaImpl> documentInstances = (Collection<DocumentInstanceJpaImpl>) _instances.findAll(); 
		assertNotNull(documentInstances);
		assertFalse(documentInstances.isEmpty());
		documentInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			DocumentJpaImpl d = i.getDocument();
			assertNotNull(d);
			assertTrue(d.isValid(true));
		});

		documentInstances = (Collection<DocumentInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "%TEST%");
		assertNotNull(documentInstances);
		assertFalse(documentInstances.isEmpty());
		documentInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			DocumentJpaImpl d = i.getDocument();
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

		documentInstances = (Collection<DocumentInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike(null, TEST_DOCUMENT_CODE_PREFIX);
		assertTrue(documentInstances.isEmpty());
		documentInstances = (Collection<DocumentInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike("", TEST_DOCUMENT_CODE_PREFIX);
		assertTrue(documentInstances.isEmpty());
		documentInstances = (Collection<DocumentInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike("Snicklefritz", TEST_DOCUMENT_CODE_PREFIX);
		assertTrue(documentInstances.isEmpty());
		documentInstances = (Collection<DocumentInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "");
		assertTrue(documentInstances.isEmpty());
		documentInstances = (Collection<DocumentInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "Snicklefritz");
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
		DocumentJpaImpl cx = new DocumentJpaImpl(contentCd, "BLAH BLAH BLAH");
		cx = _documents.save(cx);
		final DocumentInstanceJpaImpl x = new DocumentInstanceJpaImpl(cx, TEST_PROJECT_ID_VALUE);
		final DocumentInstanceJpaImpl y = _instances.save(x);
		assertNull(y.getDiscriminator());
		final DocumentInstanceJpaImpl z = _instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, contentCd);
		assertEquals(DocumentInstanceJpaImpl.class.getSimpleName(), z.getDiscriminator());
	}

	@Test
	public void d_ChildrenTest() {
		DocumentJpaImpl document = makeTestDocument();
		SectionJpaImpl section = makeTestSection();
		document.addSection(section);
		ClauseJpaImpl clause = makeTestClause();
		section.addClause(clause);
		ParagraphJpaImpl paragraph = makeTestParagraph();
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
		
		DocumentInstanceJpaImpl documentInstance = new DocumentInstanceJpaImpl(document, TEST_PROJECT_ID_VALUE);
		documentInstance = _instances.save(documentInstance);
		assertNotNull(documentInstance);
		assertTrue(documentInstance.isValid(true));
		Long id = documentInstance.getId();
		assertFalse(documentInstance.getSections().isEmpty());

		SectionInstanceJpaImpl sectionInstance = documentInstance.getSections().iterator().next();		
		assertNotNull(sectionInstance);
		assertTrue(sectionInstance.isValid(true));
		assertNotNull(sectionInstance.getDocument());
		assertFalse(sectionInstance.getClauses().isEmpty());
		
		ClauseInstanceJpaImpl clauseInstance = sectionInstance.getClauses().iterator().next();		
		assertNotNull(clauseInstance);
		assertTrue(clauseInstance.isValid(true));
		assertNotNull(clauseInstance.getSection());
		assertFalse(clauseInstance.getParagraphs().isEmpty());
		
		ParagraphInstanceJpaImpl paragraphInstance = clauseInstance.getParagraphs().iterator().next();
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
		DocumentJpaImpl document = _documents.save(makeTestDocument());
		assertNotNull(document);
		assertTrue(document.isValid(true));
		DocumentInstanceJpaImpl documentInstance = new DocumentInstanceJpaImpl(document, projectId);
		documentInstance = _instances.save(documentInstance);
		assertNotNull(documentInstance);
		assertTrue(documentInstance.isValid(true));
		Long id = documentInstance.getId();

		_instances.delete(id);
		assertNull(_instances.findOne(documentInstance.getId()));
		documentInstance = new DocumentInstanceJpaImpl(document, projectId);
		documentInstance = _instances.save(documentInstance);
		assertNotNull(documentInstance);
		assertTrue(documentInstance.isValid(true));
		id = documentInstance.getId();
		_instances.delete(documentInstance);
		assertNull(_instances.findOne(documentInstance.getId()));
		
		Collection<DocumentJpaImpl> documents = makeTestDocuments(7);
		documents = (Collection<DocumentJpaImpl>) _documents.save(documents);
		assertNotNull(documents);
		assertFalse(documents.isEmpty());
		final Collection<DocumentInstanceJpaImpl> list = new TreeSet<DocumentInstanceJpaImpl>();
		documents.forEach(d -> {
			assertTrue(d.isValid(true));
			list.add(new DocumentInstanceJpaImpl(d, projectId));
		});
		Collection<DocumentInstanceJpaImpl> documentInstances = (Collection<DocumentInstanceJpaImpl>) _instances.save(list);
		assertNotNull(documentInstances);
		assertFalse(documentInstances.isEmpty());
		documentInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			DocumentJpaImpl d = i.getDocument();
			assertNotNull(d);
			assertTrue(d.isValid(true));
		});		
		_instances.delete(documentInstances);
		documentInstances.forEach(i -> {
			assertNull(_instances.findOne(i.getId()));;
		});
		
		_instances.deleteAll();
		documentInstances = (Collection<DocumentInstanceJpaImpl>) _instances.findAll();
		assertTrue(documentInstances.isEmpty());
	}
}