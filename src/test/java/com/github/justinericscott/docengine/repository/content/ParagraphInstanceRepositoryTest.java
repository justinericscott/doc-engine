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
import com.github.justinericscott.docengine.models.Instance.ParagraphInstance;
import com.github.justinericscott.docengine.repository.content.ParagraphInstanceRepository;
import com.github.justinericscott.docengine.repository.content.ParagraphRepository;
import com.github.justinericscott.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * 
 *         Tests for the Instance Repository.
 * @param <T>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ParagraphInstanceRepositoryTest extends AbstractTest {

	@Autowired
	private ParagraphRepository _paragraphs;

	@Autowired
	private ParagraphInstanceRepository _instances;

	@Test
	public void a_SaveTest() {
		
		// Happy path...
		Paragraph paragraph = makeTestParagraph();
		paragraph = _paragraphs.save(paragraph);
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));
		ParagraphInstance paragraphInstance = new ParagraphInstance(paragraph, TEST_PROJECT_ID_VALUE);
		paragraphInstance = _instances.save(paragraphInstance);
		assertNotNull(paragraphInstance);
		assertTrue(paragraphInstance.isValid(true));
		paragraph = paragraphInstance.getParagraph();
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));
		Collection<Paragraph> paragraphs = makeTestParagraphs(7);
		paragraphs = (Collection<Paragraph>) _paragraphs.saveAll(paragraphs);
		assertNotNull(paragraphs);
		assertFalse(paragraphs.isEmpty());
		final Collection<ParagraphInstance> list = new TreeSet<ParagraphInstance>();
		paragraphs.forEach(p -> {
			assertTrue(p.isValid(true));
			list.add(new ParagraphInstance(p, TEST_PROJECT_ID_VALUE));
		});
		Collection<ParagraphInstance> paragraphInstances = new TreeSet<ParagraphInstance>();
		paragraphInstances = (Collection<ParagraphInstance>) _instances.saveAll(list);
		assertNotNull(paragraphInstances);
		assertFalse(paragraphInstances.isEmpty());
		paragraphInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			Paragraph p = i.getParagraph();
			assertNotNull(p);
			assertTrue(p.isValid(true));
		});
		try {
			_instances.save((ParagraphInstance) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_instances.save(new ParagraphInstance(paragraph, null));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_instances.save(new ParagraphInstance(TEST_PROJECT_ID_VALUE));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_instances.save(new ParagraphInstance(makeTestParagraph(), TEST_PROJECT_ID_VALUE));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
	}

	@Test
	public void b_FindTest() {
		
		// Happy path...
		Paragraph paragraph = _paragraphs.save(makeTestParagraph());
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));
		final String contentCd = paragraph.getContentCd();
		ParagraphInstance paragraphInstance = new ParagraphInstance(paragraph, TEST_PROJECT_ID_VALUE);
		paragraphInstance = _instances.save(paragraphInstance);
		assertNotNull(paragraphInstance);
		assertTrue(paragraphInstance.isValid(true));
		final Long id = paragraphInstance.getId();
		paragraphInstance = _instances.findById(id).get();
		assertNotNull(paragraphInstance);
		assertTrue(paragraphInstance.isValid(true));
		paragraph = paragraphInstance.getParagraph();
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));
		paragraphInstance = _instances.findOptionalByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, contentCd).get();
		assertNotNull(paragraphInstance);
		assertTrue(paragraphInstance.isValid(true));
		paragraph = paragraphInstance.getParagraph();
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));
		Collection<ParagraphInstance> paragraphInstances = (Collection<ParagraphInstance>) _instances.findAll(); 
		assertNotNull(paragraphInstances);
		assertFalse(paragraphInstances.isEmpty());
		paragraphInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			Paragraph p = i.getParagraph();
			assertNotNull(p);
			assertTrue(p.isValid(true));
		});
		paragraphInstances = (Collection<ParagraphInstance>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "%TEST%");
		assertNotNull(paragraphInstances);
		assertFalse(paragraphInstances.isEmpty());
		paragraphInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			Paragraph p = i.getParagraph();
			assertNotNull(p);
			assertTrue(p.isValid(true));
		});		
	}
	
	@Test
	public void bx_FindBreakTest() {
		// Break it...		
		Paragraph paragraph = _paragraphs.save(makeTestParagraph());
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));
		final String contentCd = paragraph.getContentCd();
		assertFalse(_instances.findById(0L).isPresent());
		assertFalse(_instances.findById(99999999L).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd("", contentCd).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd((String) null, contentCd).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd("Snicklefritz", contentCd).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, null).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, "").isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, "Snicklefritz").isPresent());
		Collection<ParagraphInstance> paragraphInstances = (Collection<ParagraphInstance>) _instances.findByProjectIdAndContentContentCdLike(null, TEST_CODE_PREFIX_PARAGRAPH);
		assertTrue(paragraphInstances.isEmpty());
		paragraphInstances = (Collection<ParagraphInstance>) _instances.findByProjectIdAndContentContentCdLike("", TEST_CODE_PREFIX_PARAGRAPH);
		assertTrue(paragraphInstances.isEmpty());
		paragraphInstances = (Collection<ParagraphInstance>) _instances.findByProjectIdAndContentContentCdLike("Snicklefritz", TEST_CODE_PREFIX_PARAGRAPH);
		assertTrue(paragraphInstances.isEmpty());
		paragraphInstances = (Collection<ParagraphInstance>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "");
		assertTrue(paragraphInstances.isEmpty());
		paragraphInstances = (Collection<ParagraphInstance>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "Snicklefritz");
		assertTrue(paragraphInstances.isEmpty());
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
		final String contentCd = "PARAGRAPH_INSTANCE_DISCRIMINATOR_TEST_" + uuid();
		Paragraph cx = new Paragraph(contentCd, "BLAH BLAH BLAH");
		cx = _paragraphs.save(cx);
		final ParagraphInstance x = new ParagraphInstance(cx, TEST_PROJECT_ID_VALUE);
		final ParagraphInstance y = _instances.save(x);
		assertNull(y.getDiscriminator());
//		final ParagraphInstance z = _instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, contentCd);
//		assertEquals(ParagraphInstance.class.getSimpleName(), z.getDiscriminator());
	}

	@Test
	public void x_DeleteTest() {
		final String projectId = TEST_PROJECT_ID_VALUE;
		Paragraph paragraph = _paragraphs.save(makeTestParagraph());
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));
		ParagraphInstance paragraphInstance = new ParagraphInstance(paragraph, projectId);
		paragraphInstance = _instances.save(paragraphInstance);
		assertNotNull(paragraphInstance);
		assertTrue(paragraphInstance.isValid(true));
		Long id = paragraphInstance.getId();
		_instances.deleteById(id);
		assertFalse(_instances.findById(paragraphInstance.getId()).isPresent());
		paragraphInstance = new ParagraphInstance(paragraph, projectId);
		paragraphInstance = _instances.save(paragraphInstance);
		assertNotNull(paragraphInstance);
		assertTrue(paragraphInstance.isValid(true));
		id = paragraphInstance.getId();
		_instances.delete(paragraphInstance);
		assertFalse(_instances.findById(paragraphInstance.getId()).isPresent());
		Collection<Paragraph> paragraphs = makeTestParagraphs(7);
		paragraphs = (Collection<Paragraph>) _paragraphs.saveAll(paragraphs);
		assertNotNull(paragraphs);
		assertFalse(paragraphs.isEmpty());
		final Collection<ParagraphInstance> list = new TreeSet<ParagraphInstance>();
		paragraphs.forEach(p -> {
			assertTrue(p.isValid(true));
			list.add(new ParagraphInstance(p, projectId));
		});
		Collection<ParagraphInstance> paragraphInstances = (Collection<ParagraphInstance>) _instances.saveAll(list);
		assertNotNull(paragraphInstances);
		assertFalse(paragraphInstances.isEmpty());
		paragraphInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			Paragraph p = i.getParagraph();
			assertNotNull(p);
			assertTrue(p.isValid(true));
		});		
		_instances.deleteAll(paragraphInstances);
		paragraphInstances.forEach(i -> {
			assertFalse(_instances.findById(i.getId()).isPresent());;
		});
		_instances.deleteAll();
		paragraphInstances = (Collection<ParagraphInstance>) _instances.findAll();
		assertTrue(paragraphInstances.isEmpty());
	}
}