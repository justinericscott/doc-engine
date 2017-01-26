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

import com.github.justinericscott.docengine.repository.content.ClauseRepository;
import com.github.justinericscott.docengine.types.ClauseJpaImpl;
import com.github.justinericscott.docengine.types.ParagraphJpaImpl;
import com.github.justinericscott.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * 
 *         Tests for the Content Repository
 * @param <T>
 * @param <T>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClauseRepositoryTest extends AbstractTest {

	@Autowired
	private ClauseRepository _clauses;

	@Test
	public void a_SaveTest() {
		ClauseJpaImpl clause = makeTestClause();
		clause = _clauses.save(clause);
		assertNotNull(clause);
		assertTrue(clause.isValid(true));

		Collection<ClauseJpaImpl> clauses = makeTestClauses(7);
		clauses = (Collection<ClauseJpaImpl>) _clauses.save(clauses);
		assertNotNull(clauses);
		assertFalse(clauses.isEmpty());
		clauses.forEach(c -> {
			assertTrue(c.isValid(true));
		});

		try {
			_clauses.save((ClauseJpaImpl) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_clauses.save(new ClauseJpaImpl("", "TEST BODY"));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_clauses.save(new ClauseJpaImpl(TEST_CLAUSE_CODE_PREFIX + uuid(), ""));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			clause = _clauses.save(makeTestClause());
			ClauseJpaImpl copy = new ClauseJpaImpl(clause, clause.getContentCd());
			_clauses.save(copy);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			Collection<ClauseJpaImpl> copies = new TreeSet<ClauseJpaImpl>();
			copies.addAll(clauses);
			copies.forEach(c -> {
				c.setId(null);
			});
			copies = (Collection<ClauseJpaImpl>) _clauses.save(copies);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
	}

	@Test
	public void b_FindTest() {
		// Happy path...
		ClauseJpaImpl clause = _clauses.save(makeTestClause());
		final Long id = clause.getId();
		final String contentCd = clause.getContentCd();
		clause = _clauses.findOne(id);
		assertTrue(clause.isValid(true));

		clause = _clauses.findByContentCd(contentCd);
		assertTrue(clause.isValid(true));
		assertEquals(contentCd, clause.getContentCd());

		Collection<ClauseJpaImpl> clauses = (Collection<ClauseJpaImpl>) _clauses.findByContentCdLike("%TEST%");
		assertNotNull(clauses);
		assertFalse(clauses.isEmpty());
		clauses.forEach(c -> {
			assertTrue(c.isValid(true));
		});

		clauses = (Collection<ClauseJpaImpl>) _clauses.findAll();
		assertNotNull(clauses);
		assertFalse(clauses.isEmpty());
		clauses.forEach(c -> {
			assertTrue(c.isValid(true));
		});

		// Break it...
		assertNull(_clauses.findOne(Long.MIN_VALUE));
		assertNull(_clauses.findOne(Long.MAX_VALUE));
		assertNull(_clauses.findByContentCd("Snicklefritz"));
		clauses = (Collection<ClauseJpaImpl>) _clauses.findByContentCdLike("%Snicklefritz%");
		assertTrue(clauses.isEmpty());
		clauses = (Collection<ClauseJpaImpl>) _clauses.findByContentCdLike("");
		assertTrue(clauses.isEmpty());
		try {
			_clauses.findOne((Long) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_clauses.findByContentCdLike((String) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
	}

	@Test
	public void c_DiscriminatorTest() {
		final String contentCd = "CLAUSE_DISCRIMINATOR_TEST_" + uuid();
		final ClauseJpaImpl x = new ClauseJpaImpl(contentCd, "BLAH BLAH BLAH");
		final ClauseJpaImpl y = _clauses.save(x);
		assertNull(y.getDiscriminator());
		final ClauseJpaImpl z = _clauses.findByContentCd(contentCd);
		assertEquals(ClauseJpaImpl.class.getSimpleName(), z.getDiscriminator());
	}

	@Test
	public void d_ChildrenTest() {
		ClauseJpaImpl clause = makeTestClause();
		ParagraphJpaImpl paragraph = makeTestParagraph();
		clause.addParagraph(paragraph);
		clause = _clauses.save(clause);
		assertNotNull(clause);
		assertTrue(clause.isValid(true));
		Long id = clause.getId();
		assertFalse(clause.getParagraphs().isEmpty());
		paragraph = clause.getParagraphs().iterator().next();
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));
		assertNotNull(paragraph.getClause());
		assertTrue(paragraph.getClause().isValid(true));

		clause = _clauses.findOne(id);
		assertNotNull(clause);
		assertTrue(clause.isValid(true));
		try {
			clause.getParagraphs().isEmpty();
//			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(LazyInitializationException.class, e.getClass());
		}
		_clauses.delete(id);
	}

	@Test
	public void x_DeleteTest() {
		// Happy path...
		ClauseJpaImpl clause = _clauses.save(makeTestClause());
		final Long id = clause.getId();
		_clauses.delete(id);
		assertNull(_clauses.findOne(id));

		clause = _clauses.save(makeTestClause());
		_clauses.delete(clause);
		assertNull(_clauses.findOne(clause.getId()));

		Collection<ClauseJpaImpl> clauses = makeTestClauses(7);
		clauses = (Collection<ClauseJpaImpl>) _clauses.save(clauses);
		assertNotNull(clauses);
		assertFalse(clauses.isEmpty());
		clauses.forEach(c -> {
			assertTrue(c.isValid(true));
		});
		_clauses.delete(clauses);
		clauses.forEach(c -> {
			assertNull(_clauses.findOne(c.getId()));
		});

//		_clauses.deleteAll();
//		clauses = (Collection<ClauseJpaImpl>) _clauses.findAll();
//		assertTrue(clauses.isEmpty());

		// Break it...
		try {
			_clauses.delete(new ClauseJpaImpl());
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_clauses.delete((Long) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
	}
}