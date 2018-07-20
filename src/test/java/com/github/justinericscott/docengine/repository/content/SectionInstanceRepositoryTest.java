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

import com.github.justinericscott.docengine.models.Content.Clause;
import com.github.justinericscott.docengine.models.Content.Paragraph;
import com.github.justinericscott.docengine.models.Content.Section;
import com.github.justinericscott.docengine.models.Instance.ClauseInstance;
import com.github.justinericscott.docengine.models.Instance.ParagraphInstance;
import com.github.justinericscott.docengine.models.Instance.SectionInstance;
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
		sections = (Collection<Section>) _sections.saveAll(sections);
		assertNotNull(sections);
		assertFalse(sections.isEmpty());
		final Collection<SectionInstance> list = new TreeSet<SectionInstance>();
		sections.forEach(s -> {
			assertTrue(s.isValid(true));
			list.add(new SectionInstance(s, TEST_PROJECT_ID_VALUE));
		});
		Collection<SectionInstance> sectionInstances = new TreeSet<SectionInstance>();
		sectionInstances = (Collection<SectionInstance>) _instances.saveAll(list);
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
		sectionInstance = _instances.findById(id).get();
		assertNotNull(sectionInstance);
		assertTrue(sectionInstance.isValid(true));
		section = sectionInstance.getSection();
		assertNotNull(section);
		assertTrue(section.isValid(true));
		sectionInstance = _instances.findOptionalByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, contentCd).get();
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
	}
	
	@Test
	public void bx_FindBreakTest() {
		// Break it...		
		Section section = _sections.save(makeTestSection());
		assertNotNull(section);
		assertTrue(section.isValid(true));
		final String contentCd = section.getContentCd();
		assertFalse(_instances.findById(0L).isPresent());
		assertFalse(_instances.findById(99999999L).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd("", contentCd).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd((String) null, contentCd).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd("Snicklefritz", contentCd).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, null).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, "").isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, "Snicklefritz").isPresent());
		Collection<SectionInstance> sectionInstances = (Collection<SectionInstance>) _instances.findByProjectIdAndContentContentCdLike(null, TEST_CODE_PREFIX_SECTION);
		assertTrue(sectionInstances.isEmpty());
		sectionInstances = (Collection<SectionInstance>) _instances.findByProjectIdAndContentContentCdLike("", TEST_CODE_PREFIX_SECTION);
		assertTrue(sectionInstances.isEmpty());
		sectionInstances = (Collection<SectionInstance>) _instances.findByProjectIdAndContentContentCdLike("Snicklefritz", TEST_CODE_PREFIX_SECTION);
		assertTrue(sectionInstances.isEmpty());
		sectionInstances = (Collection<SectionInstance>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "");
		assertTrue(sectionInstances.isEmpty());
		sectionInstances = (Collection<SectionInstance>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "Snicklefritz");
		assertTrue(sectionInstances.isEmpty());
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
		final String contentCd = "SECTION_INSTANCE_DISCRIMINATOR_TEST_" + uuid();
		Section cx = new Section(contentCd, "BLAH BLAH BLAH");
		cx = _sections.save(cx);
		final SectionInstance x = new SectionInstance(cx, TEST_PROJECT_ID_VALUE);
		final SectionInstance y = _instances.save(x);
		assertNull(y.getDiscriminator());
//		final SectionInstance z = _instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, contentCd);
//		assertEquals(SectionInstance.class.getSimpleName(), z.getDiscriminator());
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
		sectionInstance = _instances.findById(id).get();
		assertNotNull(sectionInstance);
		assertTrue(sectionInstance.isValid(true));
		_instances.deleteById(id);
		_sections.deleteById(section.getId());
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

		_instances.deleteById(id);
		assertFalse(_instances.findById(sectionInstance.getId()).isPresent());
		sectionInstance = new SectionInstance(section, projectId);
		sectionInstance = _instances.save(sectionInstance);
		assertNotNull(sectionInstance);
		assertTrue(sectionInstance.isValid(true));
		id = sectionInstance.getId();
		_instances.delete(sectionInstance);
		assertFalse(_instances.findById(sectionInstance.getId()).isPresent());
		
		Collection<Section> sections = makeTestSections(7);
		sections = (Collection<Section>) _sections.saveAll(sections);
		assertNotNull(sections);
		assertFalse(sections.isEmpty());
		final Collection<SectionInstance> list = new TreeSet<SectionInstance>();
		sections.forEach(s -> {
			assertTrue(s.isValid(true));
			list.add(new SectionInstance(s, projectId));
		});
		Collection<SectionInstance> sectionInstances = (Collection<SectionInstance>) _instances.saveAll(list);
		assertNotNull(sectionInstances);
		assertFalse(sectionInstances.isEmpty());
		sectionInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			Section s = i.getSection();
			assertNotNull(s);
			assertTrue(s.isValid(true));
		});		
		_instances.deleteAll(sectionInstances);
		sectionInstances.forEach(i -> {
			assertFalse(_instances.findById(i.getId()).isPresent());;
		});
		
		_instances.deleteAll();
		sectionInstances = (Collection<SectionInstance>) _instances.findAll();
		assertTrue(sectionInstances.isEmpty());
	}
}