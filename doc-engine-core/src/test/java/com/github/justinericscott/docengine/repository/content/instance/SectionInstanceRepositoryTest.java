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

import com.github.justinericscott.docengine.models.Clause;
import com.github.justinericscott.docengine.models.ClauseInstance;
import com.github.justinericscott.docengine.models.Paragraph;
import com.github.justinericscott.docengine.models.ParagraphInstance;
import com.github.justinericscott.docengine.models.Section;
import com.github.justinericscott.docengine.models.SectionInstance;
import com.github.justinericscott.docengine.repository.content.SectionInstanceRepository;
import com.github.justinericscott.docengine.repository.content.SectionRepository;
import com.github.justinericscott.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * 
 *         Tests for the Instance Repository.
 * @param <T>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SectionInstanceRepositoryTest extends AbstractTest {

	@Autowired
	private SectionRepository _sections;

	@Autowired
	private SectionInstanceRepository _instances;

	@Test
	public void a_SaveTest() {
		
		// Happy path...
		Section section = makeTestSection();
		section = _sections.save(section);
		assertNotNull(section);
		assertTrue(section.isValid(true));
		SectionInstance sectionInstance = new SectionInstance(section, TEST_PROJECT_ID_VALUE);
		sectionInstance = _instances.save(sectionInstance);
		assertNotNull(sectionInstance);
		assertTrue(sectionInstance.isValid(true));
		section = sectionInstance.getSection();
		assertNotNull(section);
		assertTrue(section.isValid(true));

		Collection<Section> sections = makeTestSections(7);
		sections = (Collection<Section>) _sections.save(sections);
		assertNotNull(sections);
		assertFalse(sections.isEmpty());
		final Collection<SectionInstance> list = new TreeSet<SectionInstance>();
		sections.forEach(s -> {
			assertTrue(s.isValid(true));
			list.add(new SectionInstance(s, TEST_PROJECT_ID_VALUE));
		});
		Collection<SectionInstance> sectionInstances = new TreeSet<SectionInstance>();
		sectionInstances = (Collection<SectionInstance>) _instances.save(list);
		assertNotNull(sectionInstances);
		assertFalse(sectionInstances.isEmpty());
		sectionInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			Section s = i.getSection();
			assertNotNull(s);
			assertTrue(s.isValid(true));
		});

		try {
			_instances.save((SectionInstance) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_instances.save(new SectionInstance(section, null));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_instances.save(new SectionInstance(TEST_PROJECT_ID_VALUE));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_instances.save(new SectionInstance(makeTestSection(), TEST_PROJECT_ID_VALUE));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
	}

	@Test
	public void b_FindTest() {
		
		// Happy path...
		Section section = _sections.save(makeTestSection());
		assertNotNull(section);
		assertTrue(section.isValid(true));
		final String contentCd = section.getContentCd();
		SectionInstance sectionInstance = new SectionInstance(section, TEST_PROJECT_ID_VALUE);
		sectionInstance = _instances.save(sectionInstance);
		assertNotNull(sectionInstance);
		assertTrue(sectionInstance.isValid(true));
		final Long id = sectionInstance.getId();

		sectionInstance = _instances.findOne(id);
		assertNotNull(sectionInstance);
		assertTrue(sectionInstance.isValid(true));
		section = sectionInstance.getSection();
		assertNotNull(section);
		assertTrue(section.isValid(true));
		sectionInstance = _instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, contentCd);
		assertNotNull(sectionInstance);
		assertTrue(sectionInstance.isValid(true));
		section = sectionInstance.getSection();
		assertNotNull(section);
		assertTrue(section.isValid(true));

		Collection<SectionInstance> sectionInstances = (Collection<SectionInstance>) _instances.findAll(); 
		assertNotNull(sectionInstances);
		assertFalse(sectionInstances.isEmpty());
		sectionInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			Section s = i.getSection();
			assertNotNull(s);
			assertTrue(s.isValid(true));
		});

		sectionInstances = (Collection<SectionInstance>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "%TEST%");
		assertNotNull(sectionInstances);
		assertFalse(sectionInstances.isEmpty());
		sectionInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			Section s = i.getSection();
			assertNotNull(s);
			assertTrue(s.isValid(true));
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

		sectionInstances = (Collection<SectionInstance>) _instances.findByProjectIdAndContentContentCdLike(null, TEST_SECTION_CODE_PREFIX);
		assertTrue(sectionInstances.isEmpty());
		sectionInstances = (Collection<SectionInstance>) _instances.findByProjectIdAndContentContentCdLike("", TEST_SECTION_CODE_PREFIX);
		assertTrue(sectionInstances.isEmpty());
		sectionInstances = (Collection<SectionInstance>) _instances.findByProjectIdAndContentContentCdLike("Snicklefritz", TEST_SECTION_CODE_PREFIX);
		assertTrue(sectionInstances.isEmpty());
		sectionInstances = (Collection<SectionInstance>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "");
		assertTrue(sectionInstances.isEmpty());
		sectionInstances = (Collection<SectionInstance>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "Snicklefritz");
		assertTrue(sectionInstances.isEmpty());
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
		final String contentCd = "SECTION_INSTANCE_DISCRIMINATOR_TEST_" + uuid();
		Section cx = new Section(contentCd, "BLAH BLAH BLAH");
		cx = _sections.save(cx);
		final SectionInstance x = new SectionInstance(cx, TEST_PROJECT_ID_VALUE);
		final SectionInstance y = _instances.save(x);
		assertNull(y.getDiscriminator());
		final SectionInstance z = _instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, contentCd);
		assertEquals(SectionInstance.class.getSimpleName(), z.getDiscriminator());
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
		assertFalse(section.getClauses().isEmpty());
		
		clause = section.getClauses().iterator().next();
		assertNotNull(clause);
		assertTrue(clause.isValid(true));
		assertFalse(clause.getParagraphs().isEmpty());
		
		paragraph = clause.getParagraphs().iterator().next();
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));
		assertNotNull(paragraph.getClause());
		assertTrue(paragraph.getClause().isValid(true));
		
		SectionInstance sectionInstance = new SectionInstance(section, TEST_PROJECT_ID_VALUE);		
		sectionInstance = _instances.save(sectionInstance);
		assertNotNull(sectionInstance);
		assertTrue(sectionInstance.isValid(true));
		Long id = sectionInstance.getId();
		assertFalse(sectionInstance.getClauses().isEmpty());
		
		ClauseInstance clauseInstance = sectionInstance.getClauses().iterator().next();		
		assertNotNull(clauseInstance);
		assertTrue(clauseInstance.isValid(true));
		assertFalse(clauseInstance.getParagraphs().isEmpty());
		
		ParagraphInstance paragraphInstance = clauseInstance.getParagraphs().iterator().next();
		assertNotNull(paragraphInstance);
		assertTrue(paragraphInstance.isValid(true));
		assertNotNull(paragraphInstance.getClause());
		assertTrue(paragraphInstance.getClause().isValid(true));	
		
		sectionInstance = _instances.findOne(id);
		assertNotNull(sectionInstance);
		assertTrue(sectionInstance.isValid(true));
		
		_instances.delete(id);
		_sections.delete(section.getId());
	}
	
	@Test
	public void x_DeleteTest() {
		final String projectId = TEST_PROJECT_ID_VALUE;
		Section section = _sections.save(makeTestSection());
		assertNotNull(section);
		assertTrue(section.isValid(true));
		SectionInstance sectionInstance = new SectionInstance(section, projectId);
		sectionInstance = _instances.save(sectionInstance);
		assertNotNull(sectionInstance);
		assertTrue(sectionInstance.isValid(true));
		Long id = sectionInstance.getId();

		_instances.delete(id);
		assertNull(_instances.findOne(sectionInstance.getId()));
		sectionInstance = new SectionInstance(section, projectId);
		sectionInstance = _instances.save(sectionInstance);
		assertNotNull(sectionInstance);
		assertTrue(sectionInstance.isValid(true));
		id = sectionInstance.getId();
		_instances.delete(sectionInstance);
		assertNull(_instances.findOne(sectionInstance.getId()));
		
		Collection<Section> sections = makeTestSections(7);
		sections = (Collection<Section>) _sections.save(sections);
		assertNotNull(sections);
		assertFalse(sections.isEmpty());
		final Collection<SectionInstance> list = new TreeSet<SectionInstance>();
		sections.forEach(s -> {
			assertTrue(s.isValid(true));
			list.add(new SectionInstance(s, projectId));
		});
		Collection<SectionInstance> sectionInstances = (Collection<SectionInstance>) _instances.save(list);
		assertNotNull(sectionInstances);
		assertFalse(sectionInstances.isEmpty());
		sectionInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			Section s = i.getSection();
			assertNotNull(s);
			assertTrue(s.isValid(true));
		});		
		_instances.delete(sectionInstances);
		sectionInstances.forEach(i -> {
			assertNull(_instances.findOne(i.getId()));;
		});
		
		_instances.deleteAll();
		sectionInstances = (Collection<SectionInstance>) _instances.findAll();
		assertTrue(sectionInstances.isEmpty());
	}
}