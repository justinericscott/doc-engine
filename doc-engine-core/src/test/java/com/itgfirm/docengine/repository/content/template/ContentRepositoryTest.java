package com.itgfirm.docengine.repository.content.template;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.TreeSet;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.itgfirm.docengine.repository.content.ContentRepository;
import com.itgfirm.docengine.types.ContentJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * 
 *         Tests for the Content Repository
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContentRepositoryTest extends AbstractTest {

	@Autowired
	private ContentRepository _contents;

	@Test
	public void a_SaveTest() {
		
		// Happy path...		
		ContentJpaImpl content = makeTestContent();
		content = _contents.save(content);
		assertNotNull(content);
		assertTrue(content.isValid(true));
		
		Collection<ContentJpaImpl> contents = makeTestContents(7);
		contents = (Collection<ContentJpaImpl>) _contents.save(contents);
		assertNotNull(contents);
		assertFalse(contents.isEmpty());
		contents.forEach(c -> {
			assertTrue(c.isValid(true));
		});
		
		// Break it...
		try {
			_contents.save((ContentJpaImpl) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_contents.save(new ContentJpaImpl("", "TEST BODY"));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_contents.save(new ContentJpaImpl(TEST_CONTENT_CODE_PREFIX + uuid(), ""));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			content = _contents.save(makeTestContent());
			ContentJpaImpl copy = new ContentJpaImpl(content, content.getContentCd());
			_contents.save(copy);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}		
		try {
			Collection<ContentJpaImpl> copies = new TreeSet<ContentJpaImpl>();
			copies.addAll(contents);
			copies.forEach(c -> {
				c.setId(null);
			});
			copies = (Collection<ContentJpaImpl>) _contents.save(copies);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
	}

	@Test
	public void b_FindTest() {
		
		// Happy path...
		ContentJpaImpl content = _contents.save(makeTestContent());
		final Long id = content.getId();
		final String contentCd = content.getContentCd();
		content = _contents.findOne(id);
		assertTrue(content.isValid(true));

		content = _contents.findByContentCd(contentCd);
		assertTrue(content.isValid(true));
		assertEquals(contentCd, content.getContentCd());

		Collection<ContentJpaImpl> contents = (Collection<ContentJpaImpl>) _contents.findByContentCdLike("%TEST%");
		assertNotNull(contents);
		assertFalse(contents.isEmpty());
		contents.forEach(c -> {
			assertTrue(c.isValid(true));
		});

		contents = (Collection<ContentJpaImpl>) _contents.findAll();
		assertNotNull(contents);
		assertFalse(contents.isEmpty());
		contents.forEach(c -> {
			assertTrue(c.isValid(true));
		});

		// Break it...		
		assertNull(_contents.findOne(Long.MIN_VALUE));
		assertNull(_contents.findOne(Long.MAX_VALUE));
		assertNull(_contents.findByContentCd("Snicklefritz"));
		contents = (Collection<ContentJpaImpl>) _contents.findByContentCdLike("%Snicklefritz%");
		assertTrue(contents.isEmpty());
		contents = (Collection<ContentJpaImpl>) _contents.findByContentCdLike("");
		assertTrue(contents.isEmpty());
		try {
			_contents.findOne((Long) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_contents.findByContentCdLike((String) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
	}

	@Test
	public void c_DiscriminatorTest() {
		final String contentCd = "CONTENT_DISCRIMINATOR_TEST_" + uuid();
		final ContentJpaImpl x = new ContentJpaImpl(contentCd, "BLAH BLAH BLAH");
		final ContentJpaImpl y = _contents.save(x);
		assertNull(y.getDiscriminator());
		final ContentJpaImpl z = _contents.findByContentCd(contentCd);
		assertEquals(ContentJpaImpl.class.getSimpleName(), z.getDiscriminator());
	}
	
	@Test
	public void x_DeleteTest() {
		
		// Happy path...
		ContentJpaImpl content = _contents.save(makeTestContent());
		final Long id = content.getId();
		_contents.delete(id);
		assertNull(_contents.findOne(id));

		content = _contents.save(makeTestContent());
		_contents.delete(content);
		assertNull(_contents.findOne(content.getId()));

		Collection<ContentJpaImpl> contents = makeTestContents(7);
		contents = (Collection<ContentJpaImpl>) _contents.save(contents);
		assertNotNull(contents);
		assertFalse(contents.isEmpty());
		contents.forEach(c -> {
			assertTrue(c.isValid(true));
		});
		_contents.delete(contents);
		contents.forEach(c -> {
			assertNull(_contents.findOne(c.getId()));
		});

//		_contents.deleteAll();
//		contents = (Collection<ContentJpaImpl>) _contents.findAll();
//		assertTrue(contents.isEmpty());
		
		// Break it...
		try {
			_contents.delete(new ContentJpaImpl());
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}		
		try {
			_contents.delete((Long) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}		
	}
}