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

import com.itgfirm.docengine.repository.content.ParagraphRepository;
import com.itgfirm.docengine.types.ParagraphJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * 
 *         Tests for the Content Repository
 * @param <T>
 * @param <T>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParagraphRepositoryTest extends AbstractTest {

	@Autowired
	private ParagraphRepository _paragraphs;

	@Test
	public void a_SaveTest() {
		// Happy path...
		ParagraphJpaImpl paragraph = makeTestParagraph();
		paragraph = _paragraphs.save(paragraph);
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));

		Collection<ParagraphJpaImpl> paragraphs = makeTestParagraphs(7);
		paragraphs = (Collection<ParagraphJpaImpl>) _paragraphs.save(paragraphs);
		assertNotNull(paragraphs);
		assertFalse(paragraphs.isEmpty());
		paragraphs.forEach(p -> {
			assertTrue(p.isValid(true));
		});

		try {
			_paragraphs.save((ParagraphJpaImpl) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_paragraphs.save(new ParagraphJpaImpl("", "TEST BODY"));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_paragraphs.save(new ParagraphJpaImpl(TEST_PARAGRAPH_CODE_PREFIX + uuid(), ""));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			paragraph = _paragraphs.save(makeTestParagraph());
			ParagraphJpaImpl copy = new ParagraphJpaImpl(paragraph, paragraph.getContentCd());
			_paragraphs.save(copy);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			Collection<ParagraphJpaImpl> copies = new TreeSet<ParagraphJpaImpl>();
			copies.addAll(paragraphs);
			copies.forEach(p -> {
				p.setId(null);
			});
			copies = (Collection<ParagraphJpaImpl>) _paragraphs.save(copies);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
	}

	@Test
	public void b_FindTest() {
		// Happy path...
		ParagraphJpaImpl paragraph = _paragraphs.save(makeTestParagraph());
		final Long id = paragraph.getId();
		final String contentCd = paragraph.getContentCd();
		paragraph = _paragraphs.findOne(id);
		assertTrue(paragraph.isValid(true));

		paragraph = _paragraphs.findByContentCd(contentCd);
		assertTrue(paragraph.isValid(true));
		assertEquals(contentCd, paragraph.getContentCd());

		Collection<ParagraphJpaImpl> paragraphs = (Collection<ParagraphJpaImpl>) _paragraphs
				.findByContentCdLike("%TEST%");
		assertNotNull(paragraphs);
		assertFalse(paragraphs.isEmpty());
		paragraphs.forEach(p -> {
			assertTrue(p.isValid(true));
		});

		paragraphs = (Collection<ParagraphJpaImpl>) _paragraphs.findAll();
		assertNotNull(paragraphs);
		assertFalse(paragraphs.isEmpty());
		paragraphs.forEach(p -> {
			assertTrue(p.isValid(true));
		});
		
		// Break it...		
		assertNull(_paragraphs.findOne(Long.MIN_VALUE));
		assertNull(_paragraphs.findOne(Long.MAX_VALUE));
		assertNull(_paragraphs.findByContentCd("Snicklefritz"));
		paragraphs = (Collection<ParagraphJpaImpl>) _paragraphs.findByContentCdLike("%Snicklefritz%");
		assertTrue(paragraphs.isEmpty());
		paragraphs = (Collection<ParagraphJpaImpl>) _paragraphs.findByContentCdLike("");
		assertTrue(paragraphs.isEmpty());
		try {
			_paragraphs.findOne((Long) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_paragraphs.findByContentCdLike((String) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
	}
	
	@Test
	public void c_DiscriminatorTest() {
		final String contentCd = "PARAGRAPH_DISCRIMINATOR_TEST_" + uuid();
		final ParagraphJpaImpl x = new ParagraphJpaImpl(contentCd, "BLAH BLAH BLAH");
		final ParagraphJpaImpl y = _paragraphs.save(x);
		assertNull(y.getDiscriminator());
		final ParagraphJpaImpl z = _paragraphs.findByContentCd(contentCd);
		assertEquals(ParagraphJpaImpl.class.getSimpleName(), z.getDiscriminator());
	}

	@Test
	public void x_DeleteTest() {
		// Happy path...
		ParagraphJpaImpl paragraph = _paragraphs.save(makeTestParagraph());
		final Long id = paragraph.getId();
		_paragraphs.delete(id);
		assertNull(_paragraphs.findOne(id));

		paragraph = _paragraphs.save(makeTestParagraph());
		_paragraphs.delete(paragraph);
		assertNull(_paragraphs.findOne(paragraph.getId()));

		Collection<ParagraphJpaImpl> paragraphs = makeTestParagraphs(7);
		paragraphs = (Collection<ParagraphJpaImpl>) _paragraphs.save(paragraphs);
		assertNotNull(paragraphs);
		assertFalse(paragraphs.isEmpty());
		paragraphs.forEach(p -> {
			assertTrue(p.isValid(true));
		});
		_paragraphs.delete(paragraphs);
		paragraphs.forEach(p -> {
			assertNull(_paragraphs.findOne(p.getId()));
		});

//		_paragraphs.deleteAll();
//		paragraphs = (Collection<ParagraphJpaImpl>) _paragraphs.findAll();
//		assertTrue(paragraphs.isEmpty());
		
		// Break it...
		try {
			_paragraphs.delete(new ParagraphJpaImpl());
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}		
		try {
			_paragraphs.delete((Long) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}		
	}
}