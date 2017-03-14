package com.github.justinericscott.docengine.repository.content.instance;

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
		paragraphs = (Collection<Paragraph>) _paragraphs.save(paragraphs);
		assertNotNull(paragraphs);
		assertFalse(paragraphs.isEmpty());
		final Collection<ParagraphInstance> list = new TreeSet<ParagraphInstance>();
		paragraphs.forEach(p -> {
			assertTrue(p.isValid(true));
			list.add(new ParagraphInstance(p, TEST_PROJECT_ID_VALUE));
		});
		Collection<ParagraphInstance> paragraphInstances = new TreeSet<ParagraphInstance>();
		paragraphInstances = (Collection<ParagraphInstance>) _instances.save(list);
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
		
		// Break it...		
		assertNull(_instances.findOne(0L));
		assertNull(_instances.findOne(99999999L));
		assertNull(_instances.findByProjectIdAndContentContentCd("", contentCd));
		assertNull(_instances.findByProjectIdAndContentContentCd((String) null, contentCd));
		assertNull(_instances.findByProjectIdAndContentContentCd("Snicklefritz", contentCd));
		assertNull(_instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, null));
		assertNull(_instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, ""));
		assertNull(_instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, "Snicklefritz"));

		paragraphInstances = (Collection<ParagraphInstance>) _instances.findByProjectIdAndContentContentCdLike(null, TEST_CODE_PREFIX_PARAGRAPH);
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

		_instances.delete(id);
		assertNull(_instances.findOne(paragraphInstance.getId()));
		paragraphInstance = new ParagraphInstance(paragraph, projectId);
		paragraphInstance = _instances.save(paragraphInstance);
		assertNotNull(paragraphInstance);
		assertTrue(paragraphInstance.isValid(true));
		id = paragraphInstance.getId();
		_instances.delete(paragraphInstance);
		assertNull(_instances.findOne(paragraphInstance.getId()));
		
		Collection<Paragraph> paragraphs = makeTestParagraphs(7);
		paragraphs = (Collection<Paragraph>) _paragraphs.save(paragraphs);
		assertNotNull(paragraphs);
		assertFalse(paragraphs.isEmpty());
		final Collection<ParagraphInstance> list = new TreeSet<ParagraphInstance>();
		paragraphs.forEach(p -> {
			assertTrue(p.isValid(true));
			list.add(new ParagraphInstance(p, projectId));
		});
		Collection<ParagraphInstance> paragraphInstances = (Collection<ParagraphInstance>) _instances.save(list);
		assertNotNull(paragraphInstances);
		assertFalse(paragraphInstances.isEmpty());
		paragraphInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			Paragraph p = i.getParagraph();
			assertNotNull(p);
			assertTrue(p.isValid(true));
		});		
		_instances.delete(paragraphInstances);
		paragraphInstances.forEach(i -> {
			assertNull(_instances.findOne(i.getId()));;
		});
		
		_instances.deleteAll();
		paragraphInstances = (Collection<ParagraphInstance>) _instances.findAll();
		assertTrue(paragraphInstances.isEmpty());
	}
}