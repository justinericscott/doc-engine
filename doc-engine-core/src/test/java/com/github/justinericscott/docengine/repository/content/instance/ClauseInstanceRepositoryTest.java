package com.github.justinericscott.docengine.repository.content.instance;

import static org.junit.Assert.*;
import static com.github.justinericscott.docengine.util.AbstractTest.TestConstants.*;

import java.util.Collection;
import java.util.TreeSet;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

//import com.github.justinericscott.docengine.models.Clause;
//import com.github.justinericscott.docengine.models.ClauseInstance;
import com.github.justinericscott.docengine.models.Content.Clause;
import com.github.justinericscott.docengine.models.Content.Paragraph;
import com.github.justinericscott.docengine.models.Instance.ClauseInstance;
import com.github.justinericscott.docengine.models.Instance.ParagraphInstance;
//import com.github.justinericscott.docengine.models.Paragraph;
//import com.github.justinericscott.docengine.models.ParagraphInstance;
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
		clauses = (Collection<Clause>) _clauses.save(clauses);
		assertNotNull(clauses);
		assertFalse(clauses.isEmpty());
		final Collection<ClauseInstance> list = new TreeSet<ClauseInstance>();
		clauses.forEach(c -> {
			assertTrue(c.isValid(true));
			list.add(new ClauseInstance(c, TEST_PROJECT_ID_VALUE));
		});
		Collection<ClauseInstance> clauseInstances = new TreeSet<ClauseInstance>();
		clauseInstances = (Collection<ClauseInstance>) _instances.save(list);
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
		
		// Break it...		
		assertNull(_instances.findOne(0L));
		assertNull(_instances.findOne(99999999L));
		assertNull(_instances.findByProjectIdAndContentContentCd("", contentCd));
		assertNull(_instances.findByProjectIdAndContentContentCd((String) null, contentCd));
		assertNull(_instances.findByProjectIdAndContentContentCd("Snicklefritz", contentCd));
		assertNull(_instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, null));
		assertNull(_instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, ""));
		assertNull(_instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, "Snicklefritz"));

		clauseInstances = (Collection<ClauseInstance>) _instances.findByProjectIdAndContentContentCdLike(null, TEST_CLAUSE_CODE_PREFIX);
		assertTrue(clauseInstances.isEmpty());
		clauseInstances = (Collection<ClauseInstance>) _instances.findByProjectIdAndContentContentCdLike("", TEST_CLAUSE_CODE_PREFIX);
		assertTrue(clauseInstances.isEmpty());
		clauseInstances = (Collection<ClauseInstance>) _instances.findByProjectIdAndContentContentCdLike("Snicklefritz", TEST_CLAUSE_CODE_PREFIX);
		assertTrue(clauseInstances.isEmpty());
		clauseInstances = (Collection<ClauseInstance>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "");
		assertTrue(clauseInstances.isEmpty());
		clauseInstances = (Collection<ClauseInstance>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "Snicklefritz");
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
		
		clauseInstance = _instances.findOne(id);
		assertNotNull(clauseInstance);
		assertTrue(clauseInstance.isValid(true));
		_instances.delete(id);
		_clauses.delete(clause.getId());
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

		_instances.delete(id);
		assertNull(_instances.findOne(clauseInstance.getId()));
		clauseInstance = new ClauseInstance(clause, projectId);
		clauseInstance = _instances.save(clauseInstance);
		assertNotNull(clauseInstance);
		assertTrue(clauseInstance.isValid(true));
		id = clauseInstance.getId();
		_instances.delete(clauseInstance);
		assertNull(_instances.findOne(clauseInstance.getId()));
		
		Collection<Clause> clauses = makeTestClauses(7);
		clauses = (Collection<Clause>) _clauses.save(clauses);
		assertNotNull(clauses);
		assertFalse(clauses.isEmpty());
		final Collection<ClauseInstance> list = new TreeSet<ClauseInstance>();
		clauses.forEach(c -> {
			assertTrue(c.isValid(true));
			list.add(new ClauseInstance(c, projectId));
		});
		Collection<ClauseInstance> clauseInstances = (Collection<ClauseInstance>) _instances.save(list);
		assertNotNull(clauseInstances);
		assertFalse(clauseInstances.isEmpty());
		clauseInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			Clause c = i.getClause();
			assertNotNull(c);
			assertTrue(c.isValid(true));
		});		
		_instances.delete(clauseInstances);
		clauseInstances.forEach(i -> {
			assertNull(_instances.findOne(i.getId()));;
		});
		
		_instances.deleteAll();
		clauseInstances = (Collection<ClauseInstance>) _instances.findAll();
		assertTrue(clauseInstances.isEmpty());
	}
}