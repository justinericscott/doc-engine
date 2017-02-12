package com.github.justinericscott.docengine.repository.content.template;

import static org.junit.Assert.*;
import static com.github.justinericscott.docengine.util.AbstractTest.TestConstants.*;

import java.util.Collection;
import java.util.TreeSet;

import org.hibernate.LazyInitializationException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.github.justinericscott.docengine.models.Clause;
import com.github.justinericscott.docengine.models.Paragraph;
import com.github.justinericscott.docengine.models.Section;
import com.github.justinericscott.docengine.repository.content.SectionRepository;
import com.github.justinericscott.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * 
 *         Tests for the Content Repository
 * @param <T>
 * @param <T>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SectionRepositoryTest extends AbstractTest {

	@Autowired
	private SectionRepository _sections;

	@Test
	public void a_SaveTest() {
		Section section = makeTestSection();
		section = _sections.save(section);
		assertNotNull(section);
		assertTrue(section.isValid(true));

		Collection<Section> sections = makeTestSections(7);
		sections = (Collection<Section>) _sections.save(sections);
		assertNotNull(sections);
		assertFalse(sections.isEmpty());
		sections.forEach(s -> {
			assertTrue(s.isValid(true));
		});

		try {
			_sections.save((Section) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_sections.save(new Section("", "TEST BODY"));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_sections.save(new Section(TEST_SECTION_CODE_PREFIX + uuid(), ""));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			section = _sections.save(makeTestSection());
			Section copy = new Section(section, section.getContentCd());
			_sections.save(copy);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			Collection<Section> copies = new TreeSet<Section>();
			copies.addAll(sections);
			copies.forEach(s -> {
				s.setId(null);
			});
			copies = (Collection<Section>) _sections.save(copies);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
	}

	@Test
	public void b_FindTest() {
		// Happy path...
		Section section = _sections.save(makeTestSection());
		final Long id = section.getId();
		final String contentCd = section.getContentCd();
		section = _sections.findOne(id);
		assertTrue(section.isValid(true));

		section = _sections.findByContentCd(contentCd);
		assertTrue(section.isValid(true));
		assertEquals(contentCd, section.getContentCd());

		Collection<Section> sections = (Collection<Section>) _sections
				.findByContentCdLike("%TEST%");
		assertNotNull(sections);
		assertFalse(sections.isEmpty());
		sections.forEach(s -> {
			assertTrue(s.isValid(true));
		});

		sections = (Collection<Section>) _sections.findAll();
		assertNotNull(sections);
		assertFalse(sections.isEmpty());
		sections.forEach(s -> {
			assertTrue(s.isValid(true));
		});
		
		// Break it...		
		assertNull(_sections.findOne(Long.MIN_VALUE));
		assertNull(_sections.findOne(Long.MAX_VALUE));
		assertNull(_sections.findByContentCd("Snicklefritz"));
		sections = (Collection<Section>) _sections.findByContentCdLike("%Snicklefritz%");
		assertTrue(sections.isEmpty());
		sections = (Collection<Section>) _sections.findByContentCdLike("");
		assertTrue(sections.isEmpty());
		try {
			_sections.findOne((Long) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_sections.findByContentCdLike((String) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
	}

	@Test
	public void c_DiscriminatorTest() {
		final String contentCd = "SECTION_DISCRIMINATOR_TEST_" + uuid();
		final Section x = new Section(contentCd, "BLAH BLAH BLAH");
		final Section y = _sections.save(x);
		assertNull(y.getDiscriminator());
		final Section z = _sections.findByContentCd(contentCd);
		assertEquals(Section.class.getSimpleName(), z.getDiscriminator());
	}

	@Test
	public void d_ChildrenTest() {
		Section section = makeTestSection();
		Clause clause = makeTestClause();
		section.addClause(clause);
		Paragraph paragraph = makeTestParagraph();
		clause.addParagraph(paragraph);
		section = _sections.save(section);
		assertNotNull(section);
		assertTrue(section.isValid(true));
		Long id = section.getId();
		assertFalse(section.getClauses().isEmpty());
		clause = section.getClauses().iterator().next();
		assertNotNull(clause);
		assertTrue(clause.isValid(true));
		assertNotNull(clause.getSection());
		assertTrue(clause.getSection().isValid(true));
		assertFalse(clause.getParagraphs().isEmpty());
		paragraph = clause.getParagraphs().iterator().next();
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));
		assertNotNull(paragraph.getClause());
		assertTrue(paragraph.getClause().isValid(true));

		section = _sections.findOne(id);
		assertNotNull(section);
		assertTrue(section.isValid(true));
		try {
			section.getClauses().isEmpty();
//			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(LazyInitializationException.class, e.getClass());
		}
		_sections.delete(id);
	}

	@Test
	public void x_DeleteTest() {
		// Happy path...
		Section section = _sections.save(makeTestSection());
		final Long id = section.getId();
		_sections.delete(id);
		assertNull(_sections.findOne(id));

		section = _sections.save(makeTestSection());
		_sections.delete(section);
		assertNull(_sections.findOne(section.getId()));

		Collection<Section> sections = makeTestSections(7);
		sections = (Collection<Section>) _sections.save(sections);
		assertNotNull(sections);
		assertFalse(sections.isEmpty());
		sections.forEach(s -> {
			assertTrue(s.isValid(true));
		});
		_sections.delete(sections);
		sections.forEach(s -> {
			assertNull(_sections.findOne(s.getId()));
		});

//		_sections.deleteAll();
//		sections = (Collection<SectionJpaImpl>) _sections.findAll();
//		assertTrue(sections.isEmpty());
		
		// Break it...
		try {
			_sections.delete(new Section());
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}		
		try {
			_sections.delete((Long) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}		
	}
}