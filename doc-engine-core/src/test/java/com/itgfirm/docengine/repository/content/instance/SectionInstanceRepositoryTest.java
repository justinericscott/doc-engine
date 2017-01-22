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

import com.itgfirm.docengine.repository.content.SectionInstanceRepository;
import com.itgfirm.docengine.repository.content.SectionRepository;
import com.itgfirm.docengine.types.ClauseInstanceJpaImpl;
import com.itgfirm.docengine.types.ClauseJpaImpl;
import com.itgfirm.docengine.types.ParagraphInstanceJpaImpl;
import com.itgfirm.docengine.types.ParagraphJpaImpl;
import com.itgfirm.docengine.types.SectionInstanceJpaImpl;
import com.itgfirm.docengine.types.SectionJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;

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
		SectionJpaImpl section = makeTestSection();
		section = _sections.save(section);
		assertNotNull(section);
		assertTrue(section.isValid(true));
		SectionInstanceJpaImpl sectionInstance = new SectionInstanceJpaImpl(section, TEST_PROJECT_ID_VALUE);
		sectionInstance = _instances.save(sectionInstance);
		assertNotNull(sectionInstance);
		assertTrue(sectionInstance.isValid(true));
		section = sectionInstance.getSection();
		assertNotNull(section);
		assertTrue(section.isValid(true));

		Collection<SectionJpaImpl> sections = makeTestSections(7);
		sections = (Collection<SectionJpaImpl>) _sections.save(sections);
		assertNotNull(sections);
		assertFalse(sections.isEmpty());
		final Collection<SectionInstanceJpaImpl> list = new TreeSet<SectionInstanceJpaImpl>();
		sections.forEach(s -> {
			assertTrue(s.isValid(true));
			list.add(new SectionInstanceJpaImpl(s, TEST_PROJECT_ID_VALUE));
		});
		Collection<SectionInstanceJpaImpl> sectionInstances = new TreeSet<SectionInstanceJpaImpl>();
		sectionInstances = (Collection<SectionInstanceJpaImpl>) _instances.save(list);
		assertNotNull(sectionInstances);
		assertFalse(sectionInstances.isEmpty());
		sectionInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			SectionJpaImpl s = i.getSection();
			assertNotNull(s);
			assertTrue(s.isValid(true));
		});

		try {
			_instances.save((SectionInstanceJpaImpl) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_instances.save(new SectionInstanceJpaImpl(section, null));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_instances.save(new SectionInstanceJpaImpl(TEST_PROJECT_ID_VALUE));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_instances.save(new SectionInstanceJpaImpl(makeTestSection(), TEST_PROJECT_ID_VALUE));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
	}

	@Test
	public void b_FindTest() {
		
		// Happy path...
		SectionJpaImpl section = _sections.save(makeTestSection());
		assertNotNull(section);
		assertTrue(section.isValid(true));
		final String contentCd = section.getContentCd();
		SectionInstanceJpaImpl sectionInstance = new SectionInstanceJpaImpl(section, TEST_PROJECT_ID_VALUE);
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

		Collection<SectionInstanceJpaImpl> sectionInstances = (Collection<SectionInstanceJpaImpl>) _instances.findAll(); 
		assertNotNull(sectionInstances);
		assertFalse(sectionInstances.isEmpty());
		sectionInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			SectionJpaImpl s = i.getSection();
			assertNotNull(s);
			assertTrue(s.isValid(true));
		});

		sectionInstances = (Collection<SectionInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "%TEST%");
		assertNotNull(sectionInstances);
		assertFalse(sectionInstances.isEmpty());
		sectionInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			SectionJpaImpl s = i.getSection();
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

		sectionInstances = (Collection<SectionInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike(null, TEST_SECTION_CODE_PREFIX);
		assertTrue(sectionInstances.isEmpty());
		sectionInstances = (Collection<SectionInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike("", TEST_SECTION_CODE_PREFIX);
		assertTrue(sectionInstances.isEmpty());
		sectionInstances = (Collection<SectionInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike("Snicklefritz", TEST_SECTION_CODE_PREFIX);
		assertTrue(sectionInstances.isEmpty());
		sectionInstances = (Collection<SectionInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "");
		assertTrue(sectionInstances.isEmpty());
		sectionInstances = (Collection<SectionInstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "Snicklefritz");
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
		SectionJpaImpl cx = new SectionJpaImpl(contentCd, "BLAH BLAH BLAH");
		cx = _sections.save(cx);
		final SectionInstanceJpaImpl x = new SectionInstanceJpaImpl(cx, TEST_PROJECT_ID_VALUE);
		final SectionInstanceJpaImpl y = _instances.save(x);
		assertNull(y.getDiscriminator());
		final SectionInstanceJpaImpl z = _instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, contentCd);
		assertEquals(SectionInstanceJpaImpl.class.getSimpleName(), z.getDiscriminator());
	}

	@Test
	public void d_ChildrenTest() {
		SectionJpaImpl section = makeTestSection();
		ClauseJpaImpl clause = makeTestClause();
		section.addClause(clause);
		ParagraphJpaImpl paragraph = makeTestParagraph();
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
		
		SectionInstanceJpaImpl sectionInstance = new SectionInstanceJpaImpl(section, TEST_PROJECT_ID_VALUE);		
		sectionInstance = _instances.save(sectionInstance);
		assertNotNull(sectionInstance);
		assertTrue(sectionInstance.isValid(true));
		Long id = sectionInstance.getId();
		assertFalse(sectionInstance.getClauses().isEmpty());
		
		ClauseInstanceJpaImpl clauseInstance = sectionInstance.getClauses().iterator().next();		
		assertNotNull(clauseInstance);
		assertTrue(clauseInstance.isValid(true));
		assertFalse(clauseInstance.getParagraphs().isEmpty());
		
		ParagraphInstanceJpaImpl paragraphInstance = clauseInstance.getParagraphs().iterator().next();
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
		SectionJpaImpl section = _sections.save(makeTestSection());
		assertNotNull(section);
		assertTrue(section.isValid(true));
		SectionInstanceJpaImpl sectionInstance = new SectionInstanceJpaImpl(section, projectId);
		sectionInstance = _instances.save(sectionInstance);
		assertNotNull(sectionInstance);
		assertTrue(sectionInstance.isValid(true));
		Long id = sectionInstance.getId();

		_instances.delete(id);
		assertNull(_instances.findOne(sectionInstance.getId()));
		sectionInstance = new SectionInstanceJpaImpl(section, projectId);
		sectionInstance = _instances.save(sectionInstance);
		assertNotNull(sectionInstance);
		assertTrue(sectionInstance.isValid(true));
		id = sectionInstance.getId();
		_instances.delete(sectionInstance);
		assertNull(_instances.findOne(sectionInstance.getId()));
		
		Collection<SectionJpaImpl> sections = makeTestSections(7);
		sections = (Collection<SectionJpaImpl>) _sections.save(sections);
		assertNotNull(sections);
		assertFalse(sections.isEmpty());
		final Collection<SectionInstanceJpaImpl> list = new TreeSet<SectionInstanceJpaImpl>();
		sections.forEach(s -> {
			assertTrue(s.isValid(true));
			list.add(new SectionInstanceJpaImpl(s, projectId));
		});
		Collection<SectionInstanceJpaImpl> sectionInstances = (Collection<SectionInstanceJpaImpl>) _instances.save(list);
		assertNotNull(sectionInstances);
		assertFalse(sectionInstances.isEmpty());
		sectionInstances.forEach(i -> {
			assertTrue(i.isValid(true));
			SectionJpaImpl s = i.getSection();
			assertNotNull(s);
			assertTrue(s.isValid(true));
		});		
		_instances.delete(sectionInstances);
		sectionInstances.forEach(i -> {
			assertNull(_instances.findOne(i.getId()));;
		});
		
		_instances.deleteAll();
		sectionInstances = (Collection<SectionInstanceJpaImpl>) _instances.findAll();
		assertTrue(sectionInstances.isEmpty());
	}
}