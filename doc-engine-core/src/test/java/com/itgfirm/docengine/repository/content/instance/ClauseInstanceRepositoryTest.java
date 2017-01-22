package com.itgfirm.docengine.repository.content.instance;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.TreeSet;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.itgfirm.docengine.repository.content.ClauseInstanceRepository;
import com.itgfirm.docengine.repository.content.ClauseRepository;
import com.itgfirm.docengine.types.ClauseInstanceJpaImpl;
import com.itgfirm.docengine.types.ClauseJpaImpl;
import com.itgfirm.docengine.types.ParagraphInstanceJpaImpl;
import com.itgfirm.docengine.types.ParagraphJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * 
 *         Tests for the Instance Repository.
 * @param <T>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClauseInstanceRepositoryTest extends AbstractTest {

	@Autowired
	private ClauseRepository _clauses;

	@Autowired
	private ClauseInstanceRepository _instances;

	@Test
	public void a_SaveTest() {
		
		// Happy path...
		ClauseJpaImpl clause = makeTestClause();
		clause = _clauses.save(clause);
		assertNotNull(clause);
		assertTrue(clause.isValid(true));
		ClauseInstanceJpaImpl clauseInstance = new ClauseInstanceJpaImpl(clause, TEST_PROJECT_ID_VALUE);
		clauseInstance = _instances.save(clauseInstance);
		assertNotNull(clauseInstance);
		assertTrue(clauseInstance.isValid(true));
		clause = clauseInstance.getClause();
		assertNotNull(clause);
		assertTrue(clause.isValid(true));

		Collection<ClauseJpaImpl> clauses = makeTestClauses(7);
		clauses = (Collection<ClauseJpaImpl>) _clauses.save(clauses);
		assertNotNull(clauses);
		assertFalse(clauses.isEmpty());
		final Collection<ClauseInstanceJpaImpl> list = new TreeSet<ClauseInstanceJpaImpl>();
		clauses.forEach(c -> {
			assertTrue(c.isValid(true));
			list.add(new ClauseInstanceJpaImpl(c, TEST_PROJECT_ID_VALUE));
		});
		Collection<ClauseInstanceJpaImpl> clauseInstances = new TreeSet<ClauseInstanceJpaImpl>();
		clauseInstances = (Collection<ClauseInstanceJpaImpl>) _instances.save(list);
		assertNotNull(clauseInstances);
		assertFalse(clauseInstances.isEmpty());
		clauseInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			ClauseJpaImpl c = i.getClause();
			assertNotNull(c);
			assertTrue(c.isValid(true));
		});

		try {
			_instances.save((ClauseInstanceJpaImpl) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_instances.save(new ClauseInstanceJpaImpl(clause, null));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_instances.save(new ClauseInstanceJpaImpl(TEST_PROJECT_ID_VALUE));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_instances.save(new ClauseInstanceJpaImpl(makeTestClause(), TEST_PROJECT_ID_VALUE));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
	}

	@Test
	public void b_FindTest() {
		
		// Happy path...
		ClauseJpaImpl clause = _clauses.save(makeTestClause());
		assertNotNull(clause);
		assertTrue(clause.isValid(true));
		final String contentCd = clause.getContentCd();
		ClauseInstanceJpaImpl clauseInstance = new ClauseInstanceJpaImpl(clause, TEST_PROJECT_ID_VALUE);
		clauseInstance = _instances.save(clauseInstance);
		assertNotNull(clauseInstance);
		assertTrue(clauseInstance.isValid(true));
		final Long id = clauseInstance.getId();

		clauseInstance = _instances.findOne(id);
		assertNotNull(clauseInstance);
		assertTrue(clauseInstance.isValid(true));
		clause = clauseInstance.getClause();
		assertNotNull(clause);
		assertTrue(clause.isValid(true));
		clauseInstance = _instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, contentCd);
		assertNotNull(clauseInstance);
		assertTrue(clauseInstance.isValid(true));
		clause = clauseInstance.getClause();
		assertNotNull(clause);
		assertTrue(clause.isValid(true));

		Collection<ClauseInstanceJpaImpl> clauseInstances = (Collection<ClauseInstanceJpaImpl>) _instances.findAll(); 
		assertNotNull(clauseInstances);
		assertFalse(clauseInstances.isEmpty());
		clauseInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			ClauseJpaImpl c = i.getClause();
			assertNotNull(c);
			assertTrue(c.isValid(true));
		});

		clauseInstances = (Collection<ClauseInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "%TEST%");
		assertNotNull(clauseInstances);
		assertFalse(clauseInstances.isEmpty());
		clauseInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			ClauseJpaImpl c = i.getClause();
			assertNotNull(c);
			assertTrue(c.isValid(true));
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

		clauseInstances = (Collection<ClauseInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike(null, TEST_CLAUSE_CODE_PREFIX);
		assertTrue(clauseInstances.isEmpty());
		clauseInstances = (Collection<ClauseInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike("", TEST_CLAUSE_CODE_PREFIX);
		assertTrue(clauseInstances.isEmpty());
		clauseInstances = (Collection<ClauseInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike("Snicklefritz", TEST_CLAUSE_CODE_PREFIX);
		assertTrue(clauseInstances.isEmpty());
		clauseInstances = (Collection<ClauseInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "");
		assertTrue(clauseInstances.isEmpty());
		clauseInstances = (Collection<ClauseInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "Snicklefritz");
		assertTrue(clauseInstances.isEmpty());
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
		final String contentCd = "CLAUSE_INSTANCE_DISCRIMINATOR_TEST_" + uuid();
		ClauseJpaImpl cx = new ClauseJpaImpl(contentCd, "BLAH BLAH BLAH");
		cx = _clauses.save(cx);
		final ClauseInstanceJpaImpl x = new ClauseInstanceJpaImpl(cx, TEST_PROJECT_ID_VALUE);
		final ClauseInstanceJpaImpl y = _instances.save(x);
		assertNull(y.getDiscriminator());
		final ClauseInstanceJpaImpl z = _instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, contentCd);
		assertEquals(ClauseInstanceJpaImpl.class.getSimpleName(), z.getDiscriminator());
	}

	@Test
	public void d_ChildrenTest() {
		ClauseJpaImpl clause = makeTestClause();
		ParagraphJpaImpl paragraph = makeTestParagraph();
		clause.addParagraph(paragraph);
		
		clause = _clauses.save(clause);
		assertNotNull(clause);
		assertTrue(clause.isValid(true));
		assertFalse(clause.getParagraphs().isEmpty());
		
		paragraph = clause.getParagraphs().iterator().next();
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));
		assertNotNull(paragraph.getClause());
		assertTrue(paragraph.getClause().isValid(true));
		
		ClauseInstanceJpaImpl clauseInstance = new ClauseInstanceJpaImpl(clause, TEST_PROJECT_ID_VALUE);
		clauseInstance = _instances.save(clauseInstance);
		assertNotNull(clauseInstance);
		assertTrue(clauseInstance.isValid(true));
		Long id = clauseInstance.getId();
		assertFalse(clauseInstance.getParagraphs().isEmpty());
		
		ParagraphInstanceJpaImpl paragraphInstance = clauseInstance.getParagraphs().iterator().next();
		assertNotNull(paragraphInstance);
		assertTrue(paragraphInstance.isValid(true));
		assertNotNull(paragraphInstance.getClause());
		assertTrue(paragraphInstance.getClause().isValid(true));	
		
		clauseInstance = _instances.findOne(id);
		assertNotNull(clauseInstance);
		assertTrue(clauseInstance.isValid(true));
		_instances.delete(id);
		_clauses.delete(clause.getId());
	}
	
	@Test
	public void x_DeleteTest() {
		final String projectId = TEST_PROJECT_ID_VALUE;
		ClauseJpaImpl clause = _clauses.save(makeTestClause());
		assertNotNull(clause);
		assertTrue(clause.isValid(true));
		ClauseInstanceJpaImpl clauseInstance = new ClauseInstanceJpaImpl(clause, projectId);
		clauseInstance = _instances.save(clauseInstance);
		assertNotNull(clauseInstance);
		assertTrue(clauseInstance.isValid(true));
		Long id = clauseInstance.getId();

		_instances.delete(id);
		assertNull(_instances.findOne(clauseInstance.getId()));
		clauseInstance = new ClauseInstanceJpaImpl(clause, projectId);
		clauseInstance = _instances.save(clauseInstance);
		assertNotNull(clauseInstance);
		assertTrue(clauseInstance.isValid(true));
		id = clauseInstance.getId();
		_instances.delete(clauseInstance);
		assertNull(_instances.findOne(clauseInstance.getId()));
		
		Collection<ClauseJpaImpl> clauses = makeTestClauses(7);
		clauses = (Collection<ClauseJpaImpl>) _clauses.save(clauses);
		assertNotNull(clauses);
		assertFalse(clauses.isEmpty());
		final Collection<ClauseInstanceJpaImpl> list = new TreeSet<ClauseInstanceJpaImpl>();
		clauses.forEach(c -> {
			assertTrue(c.isValid(true));
			list.add(new ClauseInstanceJpaImpl(c, projectId));
		});
		Collection<ClauseInstanceJpaImpl> clauseInstances = (Collection<ClauseInstanceJpaImpl>) _instances.save(list);
		assertNotNull(clauseInstances);
		assertFalse(clauseInstances.isEmpty());
		clauseInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			ClauseJpaImpl c = i.getClause();
			assertNotNull(c);
			assertTrue(c.isValid(true));
		});		
		_instances.delete(clauseInstances);
		clauseInstances.forEach(i -> {
			assertNull(_instances.findOne(i.getId()));;
		});
		
		_instances.deleteAll();
		clauseInstances = (Collection<ClauseInstanceJpaImpl>) _instances.findAll();
		assertTrue(clauseInstances.isEmpty());
	}
}