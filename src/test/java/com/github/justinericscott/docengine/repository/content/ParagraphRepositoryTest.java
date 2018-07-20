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

import com.github.justinericscott.docengine.models.Content.Paragraph;
import com.github.justinericscott.docengine.repository.content.ParagraphRepository;
import com.github.justinericscott.docengine.util.AbstractTest;

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
		Paragraph paragraph = makeTestParagraph();
		paragraph = _paragraphs.save(paragraph);
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));
		Collection<Paragraph> paragraphs = makeTestParagraphs(7);
		paragraphs = (Collection<Paragraph>) _paragraphs.saveAll(paragraphs);
		assertNotNull(paragraphs);
		assertFalse(paragraphs.isEmpty());
		paragraphs.forEach(p -> {
			assertTrue(p.isValid(true));
		});
		try {
			_paragraphs.save((Paragraph) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_paragraphs.save(new Paragraph("", "TEST BODY"));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_paragraphs.save(new Paragraph(TEST_CODE_PREFIX_PARAGRAPH + uuid(), ""));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			paragraph = _paragraphs.save(makeTestParagraph());
			Paragraph copy = new Paragraph(paragraph, paragraph.getContentCd());
			_paragraphs.save(copy);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			Collection<Paragraph> copies = new TreeSet<Paragraph>();
			copies.addAll(paragraphs);
			copies.forEach(p -> {
				p.setId(null);
			});
			copies = (Collection<Paragraph>) _paragraphs.saveAll(copies);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
	}

	@Test
	public void b_FindTest() {
		// Happy path...
		Paragraph paragraph = _paragraphs.save(makeTestParagraph());
		final Long id = paragraph.getId();
		final String contentCd = paragraph.getContentCd();
		paragraph = _paragraphs.findById(id).get();
		assertTrue(paragraph.isValid(true));
		paragraph = _paragraphs.findOptionalByContentCd(contentCd).get();
		assertTrue(paragraph.isValid(true));
		assertEquals(contentCd, paragraph.getContentCd());
		Collection<Paragraph> paragraphs = (Collection<Paragraph>) _paragraphs
				.findByContentCdLike("%TEST%");
		assertNotNull(paragraphs);
		assertFalse(paragraphs.isEmpty());
		paragraphs.forEach(p -> {
			assertTrue(p.isValid(true));
		});
		paragraphs = (Collection<Paragraph>) _paragraphs.findAll();
		assertNotNull(paragraphs);
		assertFalse(paragraphs.isEmpty());
		paragraphs.forEach(p -> {
			assertTrue(p.isValid(true));
		});		
	}
	
	@Test
	public void bx_FindBreakTest() {
		// Break it...		
		assertFalse(_paragraphs.findById(Long.MIN_VALUE).isPresent());
		assertFalse(_paragraphs.findById(Long.MAX_VALUE).isPresent());
		assertFalse(_paragraphs.findOptionalByContentCd("Snicklefritz").isPresent());
		Collection<Paragraph> paragraphs = (Collection<Paragraph>) _paragraphs.findByContentCdLike("%Snicklefritz%");
		assertTrue(paragraphs.isEmpty());
		paragraphs = (Collection<Paragraph>) _paragraphs.findByContentCdLike("");
		assertTrue(paragraphs.isEmpty());
		try {
			_paragraphs.findById((Long) null).get();
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
		final Paragraph x = new Paragraph(contentCd, "BLAH BLAH BLAH");
		final Paragraph y = _paragraphs.save(x);
		assertNull(y.getDiscriminator());
//		final Paragraph z = _paragraphs.findByContentCd(contentCd);
//		assertEquals(Paragraph.class.getSimpleName(), z.getDiscriminator());
	}

	@Test
	public void x_DeleteTest() {
		// Happy path...
		Paragraph paragraph = _paragraphs.save(makeTestParagraph());
		final Long id = paragraph.getId();
		_paragraphs.deleteById(id);
		assertFalse(_paragraphs.findById(id).isPresent());
		paragraph = _paragraphs.save(makeTestParagraph());
		_paragraphs.delete(paragraph);
		assertFalse(_paragraphs.findById(paragraph.getId()).isPresent());
		Collection<Paragraph> paragraphs = makeTestParagraphs(7);
		paragraphs = (Collection<Paragraph>) _paragraphs.saveAll(paragraphs);
		assertNotNull(paragraphs);
		assertFalse(paragraphs.isEmpty());
		paragraphs.forEach(p -> {
			assertTrue(p.isValid(true));
		});
		_paragraphs.deleteAll(paragraphs);
		paragraphs.forEach(p -> {
			assertFalse(_paragraphs.findById(p.getId()).isPresent());
		});

//		_paragraphs.deleteAll();
//		paragraphs = (Collection<ParagraphJpaImpl>) _paragraphs.findAll();
//		assertTrue(paragraphs.isEmpty());	
	}
	
	@Test
	public void xx_DeleteBreakTest() {
		// Break it...
		try {
			_paragraphs.delete(new Paragraph());
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}		
		try {
			_paragraphs.deleteById((Long) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}	
	}
}