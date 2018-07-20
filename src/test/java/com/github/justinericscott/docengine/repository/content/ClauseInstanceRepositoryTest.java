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
import com.github.justinericscott.docengine.models.Content.Paragraph;
import com.github.justinericscott.docengine.models.Instance.ClauseInstance;
import com.github.justinericscott.docengine.models.Instance.ParagraphInstance;
import com.github.justinericscott.docengine.repository.content.ClauseInstanceRepository;
import com.github.justinericscott.docengine.repository.content.ClauseRepository;
import com.github.justinericscott.docengine.util.AbstractTest;

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
		Clause clause = makeTestClause();
		clause = _clauses.save(clause);
		assertNotNull(clause);
		assertTrue(clause.isValid(true));
		ClauseInstance clauseInstance = new ClauseInstance(clause, TEST_PROJECT_ID_VALUE);
		clauseInstance = _instances.save(clauseInstance);
		assertNotNull(clauseInstance);
		assertTrue(clauseInstance.isValid(true));
		clause = clauseInstance.getClause();
		assertNotNull(clause);
		assertTrue(clause.isValid(true));
		Collection<Clause> clauses = makeTestClauses(7);
		clauses = (Collection<Clause>) _clauses.saveAll(clauses);
		assertNotNull(clauses);
		assertFalse(clauses.isEmpty());
		final Collection<ClauseInstance> list = new TreeSet<ClauseInstance>();
		clauses.forEach(c -> {
			assertTrue(c.isValid(true));
			list.add(new ClauseInstance(c, TEST_PROJECT_ID_VALUE));
		});
		Collection<ClauseInstance> clauseInstances = new TreeSet<ClauseInstance>();
		clauseInstances = (Collection<ClauseInstance>) _instances.saveAll(list);
		assertNotNull(clauseInstances);
		assertFalse(clauseInstances.isEmpty());
		clauseInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			Clause c = i.getClause();
			assertNotNull(c);
			assertTrue(c.isValid(true));
		});
		try {
			_instances.save((ClauseInstance) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_instances.save(new ClauseInstance(clause, null));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_instances.save(new ClauseInstance(TEST_PROJECT_ID_VALUE));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_instances.save(new ClauseInstance(makeTestClause(), TEST_PROJECT_ID_VALUE));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
	}

	@Test
	public void b_FindTest() {
		
		// Happy path...
		Clause clause = _clauses.save(makeTestClause());
		assertNotNull(clause);
		assertTrue(clause.isValid(true));
		final String contentCd = clause.getContentCd();
		ClauseInstance clauseInstance = new ClauseInstance(clause, TEST_PROJECT_ID_VALUE);
		clauseInstance = _instances.save(clauseInstance);
		assertNotNull(clauseInstance);
		assertTrue(clauseInstance.isValid(true));
		final Long id = clauseInstance.getId();
		clauseInstance = _instances.findById(id).get();
		assertNotNull(clauseInstance);
		assertTrue(clauseInstance.isValid(true));
		clause = clauseInstance.getClause();
		assertNotNull(clause);
		assertTrue(clause.isValid(true));
		clauseInstance = _instances.findOptionalByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, contentCd).get();
		assertNotNull(clauseInstance);
		assertTrue(clauseInstance.isValid(true));
		clause = clauseInstance.getClause();
		assertNotNull(clause);
		assertTrue(clause.isValid(true));
		Collection<ClauseInstance> clauseInstances = (Collection<ClauseInstance>) _instances.findAll(); 
		assertNotNull(clauseInstances);
		assertFalse(clauseInstances.isEmpty());
		clauseInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			Clause c = i.getClause();
			assertNotNull(c);
			assertTrue(c.isValid(true));
		});
		clauseInstances = (Collection<ClauseInstance>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "%TEST%");
		assertNotNull(clauseInstances);
		assertFalse(clauseInstances.isEmpty());
		clauseInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			Clause c = i.getClause();
			assertNotNull(c);
			assertTrue(c.isValid(true));
		});		
	}
	
	@Test
	public void bx_FindBreakTest() {
		// Break it...		
		Clause clause = _clauses.save(makeTestClause());
		assertNotNull(clause);
		assertTrue(clause.isValid(true));
		final String contentCd = clause.getContentCd();
		assertFalse(_instances.findById(0L).isPresent());
		assertFalse(_instances.findById(99999999L).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd("", contentCd).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd((String) null, contentCd).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd("Snicklefritz", contentCd).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, null).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, "").isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, "Snicklefritz").isPresent());
		Collection<ClauseInstance> clauseInstances = (Collection<ClauseInstance>) _instances.findByProjectIdAndContentContentCdLike(null, TEST_CODE_PREFIX_CLAUSE);
		assertTrue(clauseInstances.isEmpty());
		clauseInstances = (Collection<ClauseInstance>) _instances.findByProjectIdAndContentContentCdLike("", TEST_CODE_PREFIX_CLAUSE);
		assertTrue(clauseInstances.isEmpty());
		clauseInstances = (Collection<ClauseInstance>) _instances.findByProjectIdAndContentContentCdLike("Snicklefritz", TEST_CODE_PREFIX_CLAUSE);
		assertTrue(clauseInstances.isEmpty());
		clauseInstances = (Collection<ClauseInstance>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "");
		assertTrue(clauseInstances.isEmpty());
		clauseInstances = (Collection<ClauseInstance>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "Snicklefritz");
		assertTrue(clauseInstances.isEmpty());
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
		final String contentCd = "CLAUSE_INSTANCE_DISCRIMINATOR_TEST_" + uuid();
		Clause cx = new Clause(contentCd, "BLAH BLAH BLAH");
		cx = _clauses.save(cx);
		final ClauseInstance x = new ClauseInstance(cx, TEST_PROJECT_ID_VALUE);
		final ClauseInstance y = _instances.save(x);
		assertNull(y.getDiscriminator());
//		final ClauseInstance z = _instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, contentCd);
//		assertEquals(ClauseInstance.class.getSimpleName(), z.getDiscriminator());
	}

	@Test
	public void d_ChildrenTest() {
		Clause clause = makeTestClause();
		Paragraph paragraph = makeTestParagraph();
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
		ClauseInstance clauseInstance = new ClauseInstance(clause, TEST_PROJECT_ID_VALUE);
		clauseInstance = _instances.save(clauseInstance);
		assertNotNull(clauseInstance);
		assertTrue(clauseInstance.isValid(true));
		Long id = clauseInstance.getId();
		assertFalse(clauseInstance.getParagraphs().isEmpty());
		ParagraphInstance paragraphInstance = clauseInstance.getParagraphs().iterator().next();
		assertNotNull(paragraphInstance);
		assertTrue(paragraphInstance.isValid(true));
		assertNotNull(paragraphInstance.getClause());
		assertTrue(paragraphInstance.getClause().isValid(true));	
		clauseInstance = _instances.findById(id).get();
		assertNotNull(clauseInstance);
		assertTrue(clauseInstance.isValid(true));
		_instances.deleteById(id);
		_clauses.deleteById(clause.getId());
	}
	
	@Test
	public void x_DeleteTest() {
		final String projectId = TEST_PROJECT_ID_VALUE;
		Clause clause = _clauses.save(makeTestClause());
		assertNotNull(clause);
		assertTrue(clause.isValid(true));
		ClauseInstance clauseInstance = new ClauseInstance(clause, projectId);
		clauseInstance = _instances.save(clauseInstance);
		assertNotNull(clauseInstance);
		assertTrue(clauseInstance.isValid(true));
		Long id = clauseInstance.getId();
		_instances.deleteById(id);
		assertFalse(_instances.findById(clauseInstance.getId()).isPresent());
		clauseInstance = new ClauseInstance(clause, projectId);
		clauseInstance = _instances.save(clauseInstance);
		assertNotNull(clauseInstance);
		assertTrue(clauseInstance.isValid(true));
		id = clauseInstance.getId();
		_instances.delete(clauseInstance);
		assertFalse(_instances.findById(clauseInstance.getId()).isPresent());
		Collection<Clause> clauses = makeTestClauses(7);
		clauses = (Collection<Clause>) _clauses.saveAll(clauses);
		assertNotNull(clauses);
		assertFalse(clauses.isEmpty());
		final Collection<ClauseInstance> list = new TreeSet<ClauseInstance>();
		clauses.forEach(c -> {
			assertTrue(c.isValid(true));
			list.add(new ClauseInstance(c, projectId));
		});
		Collection<ClauseInstance> clauseInstances = (Collection<ClauseInstance>) _instances.saveAll(list);
		assertNotNull(clauseInstances);
		assertFalse(clauseInstances.isEmpty());
		clauseInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			Clause c = i.getClause();
			assertNotNull(c);
			assertTrue(c.isValid(true));
		});		
		_instances.deleteAll(clauseInstances);
		clauseInstances.forEach(i -> {
			assertFalse(_instances.findById(i.getId()).isPresent());;
		});
		_instances.deleteAll();
		clauseInstances = (Collection<ClauseInstance>) _instances.findAll();
		assertTrue(clauseInstances.isEmpty());
	}
}