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

import com.itgfirm.docengine.types.ParagraphJpaImpl;
import com.itgfirm.docengine.repository.content.ParagraphInstanceRepository;
import com.itgfirm.docengine.repository.content.ParagraphRepository;
import com.itgfirm.docengine.types.ParagraphInstanceJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;

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
		ParagraphJpaImpl paragraph = makeTestParagraph();
		paragraph = _paragraphs.save(paragraph);
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));
		ParagraphInstanceJpaImpl paragraphInstance = new ParagraphInstanceJpaImpl(paragraph, TEST_PROJECT_ID_VALUE);
		paragraphInstance = _instances.save(paragraphInstance);
		assertNotNull(paragraphInstance);
		assertTrue(paragraphInstance.isValid(true));
		paragraph = paragraphInstance.getParagraph();
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));

		Collection<ParagraphJpaImpl> paragraphs = makeTestParagraphs(7);
		paragraphs = (Collection<ParagraphJpaImpl>) _paragraphs.save(paragraphs);
		assertNotNull(paragraphs);
		assertFalse(paragraphs.isEmpty());
		final Collection<ParagraphInstanceJpaImpl> list = new TreeSet<ParagraphInstanceJpaImpl>();
		paragraphs.forEach(p -> {
			assertTrue(p.isValid(true));
			list.add(new ParagraphInstanceJpaImpl(p, TEST_PROJECT_ID_VALUE));
		});
		Collection<ParagraphInstanceJpaImpl> paragraphInstances = new TreeSet<ParagraphInstanceJpaImpl>();
		paragraphInstances = (Collection<ParagraphInstanceJpaImpl>) _instances.save(list);
		assertNotNull(paragraphInstances);
		assertFalse(paragraphInstances.isEmpty());
		paragraphInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			ParagraphJpaImpl p = i.getParagraph();
			assertNotNull(p);
			assertTrue(p.isValid(true));
		});

		try {
			_instances.save((ParagraphInstanceJpaImpl) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_instances.save(new ParagraphInstanceJpaImpl(paragraph, null));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_instances.save(new ParagraphInstanceJpaImpl(TEST_PROJECT_ID_VALUE));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_instances.save(new ParagraphInstanceJpaImpl(makeTestParagraph(), TEST_PROJECT_ID_VALUE));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
	}

	@Test
	public void b_FindTest() {
		
		// Happy path...
		ParagraphJpaImpl paragraph = _paragraphs.save(makeTestParagraph());
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));
		final String contentCd = paragraph.getContentCd();
		ParagraphInstanceJpaImpl paragraphInstance = new ParagraphInstanceJpaImpl(paragraph, TEST_PROJECT_ID_VALUE);
		paragraphInstance = _instances.save(paragraphInstance);
		assertNotNull(paragraphInstance);
		assertTrue(paragraphInstance.isValid(true));
		final Long id = paragraphInstance.getId();

		paragraphInstance = _instances.findOne(id);
		assertNotNull(paragraphInstance);
		assertTrue(paragraphInstance.isValid(true));
		paragraph = paragraphInstance.getParagraph();
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));
		paragraphInstance = _instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, contentCd);
		assertNotNull(paragraphInstance);
		assertTrue(paragraphInstance.isValid(true));
		paragraph = paragraphInstance.getParagraph();
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));

		Collection<ParagraphInstanceJpaImpl> paragraphInstances = (Collection<ParagraphInstanceJpaImpl>) _instances.findAll(); 
		assertNotNull(paragraphInstances);
		assertFalse(paragraphInstances.isEmpty());
		paragraphInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			ParagraphJpaImpl p = i.getParagraph();
			assertNotNull(p);
			assertTrue(p.isValid(true));
		});

		paragraphInstances = (Collection<ParagraphInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "%TEST%");
		assertNotNull(paragraphInstances);
		assertFalse(paragraphInstances.isEmpty());
		paragraphInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			ParagraphJpaImpl p = i.getParagraph();
			assertNotNull(p);
			assertTrue(p.isValid(true));
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

		paragraphInstances = (Collection<ParagraphInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike(null, TEST_PARAGRAPH_CODE_PREFIX);
		assertTrue(paragraphInstances.isEmpty());
		paragraphInstances = (Collection<ParagraphInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike("", TEST_PARAGRAPH_CODE_PREFIX);
		assertTrue(paragraphInstances.isEmpty());
		paragraphInstances = (Collection<ParagraphInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike("Snicklefritz", TEST_PARAGRAPH_CODE_PREFIX);
		assertTrue(paragraphInstances.isEmpty());
		paragraphInstances = (Collection<ParagraphInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "");
		assertTrue(paragraphInstances.isEmpty());
		paragraphInstances = (Collection<ParagraphInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "Snicklefritz");
		assertTrue(paragraphInstances.isEmpty());
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
		final String contentCd = "PARAGRAPH_INSTANCE_DISCRIMINATOR_TEST_" + uuid();
		ParagraphJpaImpl cx = new ParagraphJpaImpl(contentCd, "BLAH BLAH BLAH");
		cx = _paragraphs.save(cx);
		final ParagraphInstanceJpaImpl x = new ParagraphInstanceJpaImpl(cx, TEST_PROJECT_ID_VALUE);
		final ParagraphInstanceJpaImpl y = _instances.save(x);
		assertNull(y.getDiscriminator());
		final ParagraphInstanceJpaImpl z = _instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, contentCd);
		assertEquals(ParagraphInstanceJpaImpl.class.getSimpleName(), z.getDiscriminator());
	}

	@Test
	public void x_DeleteTest() {
		final String projectId = TEST_PROJECT_ID_VALUE;
		ParagraphJpaImpl paragraph = _paragraphs.save(makeTestParagraph());
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));
		ParagraphInstanceJpaImpl paragraphInstance = new ParagraphInstanceJpaImpl(paragraph, projectId);
		paragraphInstance = _instances.save(paragraphInstance);
		assertNotNull(paragraphInstance);
		assertTrue(paragraphInstance.isValid(true));
		Long id = paragraphInstance.getId();

		_instances.delete(id);
		assertNull(_instances.findOne(paragraphInstance.getId()));
		paragraphInstance = new ParagraphInstanceJpaImpl(paragraph, projectId);
		paragraphInstance = _instances.save(paragraphInstance);
		assertNotNull(paragraphInstance);
		assertTrue(paragraphInstance.isValid(true));
		id = paragraphInstance.getId();
		_instances.delete(paragraphInstance);
		assertNull(_instances.findOne(paragraphInstance.getId()));
		
		Collection<ParagraphJpaImpl> paragraphs = makeTestParagraphs(7);
		paragraphs = (Collection<ParagraphJpaImpl>) _paragraphs.save(paragraphs);
		assertNotNull(paragraphs);
		assertFalse(paragraphs.isEmpty());
		final Collection<ParagraphInstanceJpaImpl> list = new TreeSet<ParagraphInstanceJpaImpl>();
		paragraphs.forEach(p -> {
			assertTrue(p.isValid(true));
			list.add(new ParagraphInstanceJpaImpl(p, projectId));
		});
		Collection<ParagraphInstanceJpaImpl> paragraphInstances = (Collection<ParagraphInstanceJpaImpl>) _instances.save(list);
		assertNotNull(paragraphInstances);
		assertFalse(paragraphInstances.isEmpty());
		paragraphInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			ParagraphJpaImpl p = i.getParagraph();
			assertNotNull(p);
			assertTrue(p.isValid(true));
		});		
		_instances.delete(paragraphInstances);
		paragraphInstances.forEach(i -> {
			assertNull(_instances.findOne(i.getId()));;
		});
		
		_instances.deleteAll();
		paragraphInstances = (Collection<ParagraphInstanceJpaImpl>) _instances.findAll();
		assertTrue(paragraphInstances.isEmpty());
	}
}