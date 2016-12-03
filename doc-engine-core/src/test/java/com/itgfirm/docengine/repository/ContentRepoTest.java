package com.itgfirm.docengine.repository;

import static org.junit.Assert.*;

import static com.itgfirm.docengine.DocEngine.Constants.*;

import java.util.Collection;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.itgfirm.docengine.repository.ContentRepository;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;
import com.itgfirm.docengine.util.TestUtils;

/**
 * @author Justin Scott
 * 
 *         Tests for the Content Repository
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContentRepoTest extends AbstractTest {
	
	@Autowired
	@Qualifier(AUTOWIRE_QUALIFIER_DEFAULT)
	protected ContentRepository repo;

	@Test
	public void a_SaveTest() {
		// One
		ContentJpaImpl content = repo.save(makeTestContent(1));
		assertNotNull(content);
		assertTrue(content.isValid(true));
		try {
			repo.save((ContentJpaImpl) null);
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			repo.save(new ContentJpaImpl("", "TEST BODY"));
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			repo.save(new ContentJpaImpl(TestUtils.getRandomTestString(2), ""));
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			content = makeTestContent(2);
			repo.save(new ContentJpaImpl(content, content.getContentCd()));
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		// Many
		Collection<ContentJpaImpl> contents = (Collection<ContentJpaImpl>) repo.save(makeTestContents(3));
		assertNotNull(contents);
		for (final ContentJpaImpl c : contents) {
			assertTrue(c.isValid(true));
		}
		contents = (Collection<ContentJpaImpl>) makeTestContents(5);
		try {
			contents.add(new ContentJpaImpl("", "TEST BODY"));
		} catch (final Exception e) {
			assertEquals(NullPointerException.class, e.getClass());
		}
		
		try {
			repo.save(contents);
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
	}

	@Test
	public void b_FindTest() {
		// Find one
		ContentJpaImpl content = createContent(34);
		final Long id = content.getId();
		content = repo.findOne(id);
		assertTrue(content.isValid(true));
		
		// Find by code
		final String code = content.getContentCd();
		content = repo.findByContentCd(code);
		assertTrue(content.isValid(true));
		assertEquals(code, content.getContentCd());
		try {
			repo.findOne((Long) null);
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		assertNull(repo.findOne(Long.MIN_VALUE));
		assertNull(repo.findOne(Long.MAX_VALUE));
		assertNull(repo.findByContentCd("Snicklefritz"));

		// Find by code like
		createContents(4);
		Iterable<? extends ContentJpaImpl> contents = repo.findByContentCdLike("%TEST%");
		assertNotNull(contents);
		assertTrue(contents.iterator().hasNext());
		try {
			repo.findByContentCdLike((String) null);
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			repo.findByContentCdLike("");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		assertFalse(repo.findByContentCdLike("%Snicklefritz%").iterator().hasNext());
		
		// Find all
		contents = (Collection<ContentJpaImpl>) repo.findAll();
		assertNotNull(contents);
		assertTrue(contents.iterator().hasNext());
}

	@Test
	public void x_DeleteTest() {
		// Delete one
		ContentJpaImpl content = createContent(56);
		repo.delete(content);
		assertNull(repo.findOne(content.getId()));
		
		// Delete all
		final Iterable<ContentJpaImpl> contents = createContents(4);
		repo.delete(contents);
		for (final ContentJpaImpl c : contents) {
			assertNull(repo.findOne(c.getId()));
		}
	}
	
	protected ContentJpaImpl createContent(final int seed) {
		return repo.save(makeTestContent(seed));
	}

	protected Iterable<ContentJpaImpl> createContents(final int count) {
		return repo.save(makeTestContents(count));
	}
}