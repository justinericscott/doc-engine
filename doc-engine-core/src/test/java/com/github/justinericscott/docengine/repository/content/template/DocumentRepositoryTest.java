package com.github.justinericscott.docengine.repository.content.template;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.TreeSet;

import org.hibernate.LazyInitializationException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.github.justinericscott.docengine.repository.content.DocumentRepository;
import com.github.justinericscott.docengine.types.ClauseJpaImpl;
import com.github.justinericscott.docengine.types.DocumentJpaImpl;
import com.github.justinericscott.docengine.types.ParagraphJpaImpl;
import com.github.justinericscott.docengine.types.SectionJpaImpl;
import com.github.justinericscott.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * 
 *         Tests for the Content Repository
 * @param <T>
 * @param <T>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DocumentRepositoryTest extends AbstractTest {
	
	@Autowired
	private DocumentRepository _documents;

	@Test
	public void a_SaveTest() {
		DocumentJpaImpl document = makeTestDocument();
		document = _documents.save(document);
		assertNotNull(document);
		assertTrue(document.isValid(true));

		Collection<DocumentJpaImpl> documents = makeTestDocuments(7);
		documents = (Collection<DocumentJpaImpl>) _documents.save(documents);
		assertNotNull(documents);
		assertFalse(documents.isEmpty());
		documents.forEach(d -> {
			assertTrue(d.isValid(true));
		});

		try {
			_documents.save((DocumentJpaImpl) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_documents.save(new DocumentJpaImpl("", "TEST BODY"));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_documents.save(new DocumentJpaImpl(TEST_SECTION_CODE_PREFIX + uuid(), ""));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			document = _documents.save(makeTestDocument());
			DocumentJpaImpl copy = new DocumentJpaImpl(document, document.getContentCd());
			_documents.save(copy);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			Collection<DocumentJpaImpl> copies = new TreeSet<DocumentJpaImpl>();
			copies.addAll(documents);
			copies.forEach(d -> {
				d.setId(null);
			});
			copies = (Collection<DocumentJpaImpl>) _documents.save(copies);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
	}

	@Test
	public void b_FindTest() {
		// Happy path...
		DocumentJpaImpl document = _documents.save(makeTestDocument());
		final Long id = document.getId();
		final String contentCd = document.getContentCd();
		document = _documents.findOne(id);
		assertTrue(document.isValid(true));

		document = _documents.findByContentCd(contentCd);
		assertTrue(document.isValid(true));
		assertEquals(contentCd, document.getContentCd());

		Collection<DocumentJpaImpl> documents = (Collection<DocumentJpaImpl>) _documents
				.findByContentCdLike("%TEST%");
		assertNotNull(documents);
		assertFalse(documents.isEmpty());
		documents.forEach(d -> {
			assertTrue(d.isValid(true));
		});

		documents = (Collection<DocumentJpaImpl>) _documents.findAll();
		assertNotNull(documents);
		assertFalse(documents.isEmpty());
		documents.forEach(d -> {
			assertTrue(d.isValid(true));
		});
		
		// Break it...		
		assertNull(_documents.findOne(Long.MIN_VALUE));
		assertNull(_documents.findOne(Long.MAX_VALUE));
		assertNull(_documents.findByContentCd("Snicklefritz"));
		documents = (Collection<DocumentJpaImpl>) _documents.findByContentCdLike("%Snicklefritz%");
		assertTrue(documents.isEmpty());
		documents = (Collection<DocumentJpaImpl>) _documents.findByContentCdLike("");
		assertTrue(documents.isEmpty());
		try {
			_documents.findOne((Long) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_documents.findByContentCdLike((String) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
	}

	@Test
	public void c_DiscriminatorTest() {
		final String contentCd = "DOCUMENT_DISCRIMINATOR_TEST_" + uuid();
		final DocumentJpaImpl x = new DocumentJpaImpl(contentCd, "BLAH BLAH BLAH");
		final DocumentJpaImpl y = _documents.save(x);
		assertNull(y.getDiscriminator());
		final DocumentJpaImpl z = _documents.findByContentCd(contentCd);
		assertEquals(DocumentJpaImpl.class.getSimpleName(), z.getDiscriminator());
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
		Long id = document.getId();
		assertFalse(document.getSections().isEmpty());
		section = document.getSections().iterator().next();		
		assertNotNull(section);
		assertTrue(section.isValid(true));
		assertFalse(section.getClauses().isEmpty());
		clause = section.getClauses().iterator().next();
		assertNotNull(clause);
		assertTrue(clause.isValid(true));
		assertNotNull(clause.getSection());
		assertTrue(clause.getSection().isValid(true));
		assertFalse(clause.getParagraphs().isEmpty());
		paragraph = clause.getParagraphs().iterator().next();
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));
		assertNotNull(paragraph.getClause());
		assertTrue(paragraph.getClause().isValid(true));

		document = _documents.findOne(id);
		assertNotNull(document);
		assertTrue(document.isValid(true));
		try {
			document.getSections().isEmpty();
//			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(LazyInitializationException.class, e.getClass());
		}
		_documents.delete(id);
	}

	@Test
	public void x_DeleteTest() {
		// Happy path...
		DocumentJpaImpl document = _documents.save(makeTestDocument());
		final Long id = document.getId();
		_documents.delete(id);
		assertNull(_documents.findOne(id));

		document = _documents.save(makeTestDocument());
		_documents.delete(document);
		assertNull(_documents.findOne(document.getId()));

		Collection<DocumentJpaImpl> documents = makeTestDocuments(7);
		documents = (Collection<DocumentJpaImpl>) _documents.save(documents);
		assertNotNull(documents);
		assertFalse(documents.isEmpty());
		documents.forEach(d -> {
			assertTrue(d.isValid(true));
		});
		_documents.delete(documents);
		documents.forEach(d -> {
			assertNull(_documents.findOne(d.getId()));
		});

//		_documents.deleteAll();
//		documents = (Collection<DocumentJpaImpl>) _documents.findAll();
//		assertTrue(documents.isEmpty());
		
		// Break it...
		try {
			_documents.delete(new DocumentJpaImpl());
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}		
		try {
			_documents.delete((Long) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}		
	}
}