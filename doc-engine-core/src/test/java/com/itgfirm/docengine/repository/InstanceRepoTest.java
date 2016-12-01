package com.itgfirm.docengine.repository;

import static org.junit.Assert.*;
import static com.itgfirm.docengine.util.Constants.*;
import static com.itgfirm.docengine.util.TestConstants.*;
import static com.itgfirm.docengine.util.TestUtils.*;

import java.util.Collection;
import java.util.TreeSet;

import org.hibernate.LazyInitializationException;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.itgfirm.docengine.repository.InstanceRepository;
import com.itgfirm.docengine.service.AdvancedContentService;
import com.itgfirm.docengine.types.jpa.AdvancedDocumentInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.AdvancedDocumentJpaImpl;
import com.itgfirm.docengine.types.jpa.ClauseInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.ClauseJpaImpl;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.types.jpa.InstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.ParagraphInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.ParagraphJpaImpl;
import com.itgfirm.docengine.types.jpa.SectionInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.SectionJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * 
 *         Tests for the Instance Repository.
 * @param <T>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InstanceRepoTest extends AbstractTest {
	@Autowired
	private InstanceRepository repo;
	@Autowired
	@Qualifier(AUTOWIRE_QUALIFIER_ADVANCED)
	private AdvancedContentService service;

	@Test
	public void a_SaveTest() {
		// Save 1
		InstanceJpaImpl instance = createInstance(createContent());

		// Merge complex one at a time
		String projectId = getRandomTestString(1);
		String code = getRandomTestString(2);
		AdvancedDocumentJpaImpl doc = (AdvancedDocumentJpaImpl) service.save(new AdvancedDocumentJpaImpl(code, "TEST_BODY"));
		assertNotNull(doc);
		assertNotNull(doc.getId());
		AdvancedDocumentInstanceJpaImpl document = new AdvancedDocumentInstanceJpaImpl(doc, projectId);
		document = repo.save(document);
		assertNotNull(document);
		assertNotNull(document.getId());

		SectionJpaImpl sec = new SectionJpaImpl(getRandomTestString(3), "TEST_BODY");
		doc.addSection(sec);
		sec = (SectionJpaImpl) service.save(sec);
		assertNotNull(sec);
		assertNotNull(sec.getId());
		SectionInstanceJpaImpl section = new SectionInstanceJpaImpl(sec, projectId);
		document.addSection(section);
		section = (SectionInstanceJpaImpl) repo.save(section);
		assertNotNull(section);
		assertNotNull(section.getId());

		ClauseJpaImpl cla = new ClauseJpaImpl(getRandomTestString(4), "TEST_BODY");
		sec.addClause(cla);
		cla = (ClauseJpaImpl) service.save(cla);
		assertNotNull(cla);
		assertNotNull(cla.getId());
		ClauseInstanceJpaImpl clause = new ClauseInstanceJpaImpl(cla, projectId);
		section.addClause(clause);
		clause = (ClauseInstanceJpaImpl) repo.save(clause);
		assertNotNull(clause);
		assertNotNull(clause.getId());

		ParagraphJpaImpl para = new ParagraphJpaImpl(getRandomTestString(5), "TEST_BODY");
		cla.addParagraph(para);
		para = (ParagraphJpaImpl) service.save(para);
		assertNotNull(para);
		assertNotNull(para.getId());
		ParagraphInstanceJpaImpl paragraph = new ParagraphInstanceJpaImpl(para, projectId);
		clause.addParagraph(paragraph);
		paragraph = (ParagraphInstanceJpaImpl) repo.save(paragraph);
		assertNotNull(paragraph);
		assertNotNull(paragraph.getId());

		document = (AdvancedDocumentInstanceJpaImpl) repo.findByProjectIdAndContentContentCd(projectId, code);
		assertNotNull(document);
		assertNotNull(document.getId());

		// Merge complex all at once
		document = createDocumentInstance(createDocument(99));
		assertNotNull(document);
		assertNotNull(document.getId());

		ContentJpaImpl content = createContent();
		try {
			repo.save((InstanceJpaImpl) null);
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			repo.save(new InstanceJpaImpl(content));
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			repo.save(new InstanceJpaImpl(getRandomTestString(31)));
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			repo.save(new InstanceJpaImpl(new ContentJpaImpl(getRandomTestString(2), "TEST_BODY"),
					getRandomTestString(3)));
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		instance = createInstance(content);
		instance = repo.save(new InstanceJpaImpl(content, instance.getProjectId()));
		assertNotNull(instance);
		assertNotNull(instance.getId());
	}

	@Test
	public void b_FindTest() {
		// Get All
		String projectId = getRandomTestString(17);
		Collection<ContentJpaImpl> contents = new TreeSet<ContentJpaImpl>();
		Collection<InstanceJpaImpl> instances = new TreeSet<InstanceJpaImpl>();
		contents.add(createContent(1));
		contents.add(createContent(2));
		contents.add(createContent(3));
		contents.add(createContent(4));
		contents.add(createContent(5));
		for (ContentJpaImpl c : contents) {
			instances.add(repo.save(new InstanceJpaImpl(c, projectId)));
		}
		for (InstanceJpaImpl i : instances) {
			assertTrue(i.isValid(true));
		}

		// Get Instance
		InstanceJpaImpl instance = createInstance(createContent());
		projectId = instance.getProjectId();
		
		assertTrue(repo.findOne(instance.getId()).isValid(true));
		assertTrue(((InstanceJpaImpl) repo.findByProjectIdAndContentContentCd(instance.getProjectId(), instance.getContent().getContentCd())).isValid(true));

		// Get sub-classes, no eagerKids
		AdvancedDocumentInstanceJpaImpl document = createDocumentInstance(createDocument(66));
		projectId = document.getProjectId();
		assertTrue(AdvancedDocumentInstanceJpaImpl.class.equals(repo.findOne(document.getId()).getClass()));
		assertTrue(AdvancedDocumentInstanceJpaImpl.class.equals(
				repo.findByProjectIdAndContentContentCd(projectId, document.getContent().getContentCd()).getClass()));
		try {
			((AdvancedDocumentInstanceJpaImpl) repo.findOne(document.getId())).getSections().iterator().next();
			throw new IllegalStateException();
		} catch (LazyInitializationException e) {
			assertTrue(e.getClass().equals(LazyInitializationException.class));
		}

		assertTrue(SectionInstanceJpaImpl.class
				.equals(repo.findOne(document.getSections().iterator().next().getId()).getClass()));
		assertTrue(SectionInstanceJpaImpl.class.equals(repo.findByProjectIdAndContentContentCd(projectId,
				document.getSections().iterator().next().getContent().getContentCd()).getClass()));
		try {
			((SectionInstanceJpaImpl) repo.findOne(document.getSections().iterator().next().getId())).getClauses()
					.iterator().next();
			throw new IllegalStateException();
		} catch (LazyInitializationException e) {
			assertTrue(e.getClass().equals(LazyInitializationException.class));
		}

		assertTrue(ClauseInstanceJpaImpl.class.equals(repo
				.findOne(document.getSections().iterator().next().getClauses().iterator().next().getId()).getClass()));
		assertTrue(ClauseInstanceJpaImpl.class.equals(repo.findByProjectIdAndContentContentCd(projectId,
				document.getSections().iterator().next().getClauses().iterator().next().getContent().getContentCd())
				.getClass()));
		try {
			((ClauseInstanceJpaImpl) repo
					.findOne(document.getSections().iterator().next().getClauses().iterator().next().getId()))
							.getParagraphs().iterator().next();
		} catch (LazyInitializationException e) {
			assertEquals(LazyInitializationException.class, e.getClass());
		}

		assertTrue(ParagraphInstanceJpaImpl.class.equals(repo.findOne(document.getSections().iterator().next()
				.getClauses().iterator().next().getParagraphs().iterator().next().getId()).getClass()));
		assertTrue(
				ParagraphInstanceJpaImpl.class
						.equals(repo
								.findByProjectIdAndContentContentCd(projectId,
										document.getSections().iterator().next().getClauses().iterator().next()
												.getParagraphs().iterator().next().getContent().getContentCd())
								.getClass()));

		document = (AdvancedDocumentInstanceJpaImpl) repo.findOne(document.getId());
		assertNotNull(document);
		assertNotNull(document.getId());

		assertNotNull(repo.findAll());
		instance = createInstance(createContent());
		String code = instance.getContent().getContentCd();

		assertNotNull(instance.getId());
		assertNotNull(instance.getContent().getId());

		assertNull(repo.findOne(0L));
		assertNull(repo.findOne(99999999L));
		try {
			repo.findOne((Long) null);
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		assertNull(repo.findByProjectIdAndContentContentCd("", code));
		assertNull(repo.findByProjectIdAndContentContentCd((String) null, code));
		assertNull(repo.findByProjectIdAndContentContentCd("Snicklefritz", code));
		assertNull(repo.findByProjectIdAndContentContentCd("TEST_PROJECT_ID", null));
		assertNull(repo.findByProjectIdAndContentContentCd("TEST_PROJECT_ID", ""));
		assertNull(repo.findByProjectIdAndContentContentCd("TEST_PROJECT_ID", "Snicklefritz"));
	}

	@Test
	public void c_GetByProjectAndCodeLikeTest() {
		InstanceJpaImpl instance = createInstance(createContent());
		String projectId = instance.getProjectId();
		Iterable<InstanceJpaImpl> instances = repo.findByProjectIdAndContentContentCdLike(instance.getProjectId(),
				"%TEST%");
		assertNotNull(instances);
		assertTrue(repo.findByProjectIdAndContentContentCdLike(instance.getProjectId(), "%TEST%").iterator().hasNext());
		assertFalse(repo.findByProjectIdAndContentContentCdLike(null, TEST_CODE_PREFIX).iterator().hasNext());
		assertFalse(repo.findByProjectIdAndContentContentCdLike("", TEST_CODE_PREFIX).iterator().hasNext());
		assertFalse(repo.findByProjectIdAndContentContentCdLike("Snicklefritz", TEST_CODE_PREFIX).iterator().hasNext());
		assertFalse(repo.findByProjectIdAndContentContentCdLike(projectId, "").iterator().hasNext());
		try {
			repo.findByProjectIdAndContentContentCdLike(projectId, null);
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		assertFalse(repo.findByProjectIdAndContentContentCdLike(projectId, "Snicklefritz").iterator().hasNext());
	}

	@Test
	public void x_DeleteTest() {
		InstanceJpaImpl instance = createInstance(createContent());
		repo.delete(instance);
		service.delete(instance.getContent());
		assertNull(repo.findOne(instance.getId()));
	}

	private ContentJpaImpl createContent() {
		return createContent(1);
	}

	private ContentJpaImpl createContent(int seed) {
		return (ContentJpaImpl) service.save(makeTestContent(seed));
	}

	private InstanceJpaImpl createInstance(ContentJpaImpl content) {
		return repo.save(new InstanceJpaImpl(content, getRandomTestString(2)));
	}

	private AdvancedDocumentJpaImpl createDocument(int seed) {
		return (AdvancedDocumentJpaImpl) service.save(makeTestDocument(seed));
	}

	private AdvancedDocumentInstanceJpaImpl createDocumentInstance(AdvancedDocumentJpaImpl document) {
		return (AdvancedDocumentInstanceJpaImpl) repo
				.save(new AdvancedDocumentInstanceJpaImpl(document, getRandomTestString(11)));
	}

	// protected void validate(AdvancedDocumentJpaImpl document) {
	// assertTrue(document.isValid(true));
	// assertNotNull(document.getSections());
	// for (SectionJpaImpl s : document.getSections()) {
	// validate(s);
	// }
	// }

	// protected void validate(SectionJpaImpl section) {
	// assertTrue(section.isValid(true));
	// assertNotNull(section.getClauses());
	// for (ClauseJpaImpl c : section.getClauses()) {
	// validate(c);
	// }
	// }

	// protected void validate(ClauseJpaImpl clause) {
	// assertTrue(clause.isValid(true));
	// assertNotNull(clause.getParagraphs());
	// for (ParagraphJpaImpl p : clause.getParagraphs()) {
	// validate(p);
	// }
	// }

	// protected void validate(ParagraphJpaImpl paragraph) {
	// assertTrue(paragraph.isValid(true));
	// }

	// protected void validate(InstanceJpaImpl instance) {
	// assertTrue(instance.isValid(true));
	// assertNotNull(instance.getContent().getId());
	// }

	// protected void validate(AdvancedDocumentInstanceJpaImpl document) {
	// assertTrue(document.isValid(true));
	// LOG.trace("Validating Document Instance.");
	// assertNotNull(document.getSections());
	// for (SectionInstanceJpaImpl s : document.getSections()) {
	// LOG.trace("Validating Section Instance.");
	// s.isValid(true);
	// assertNotNull(s.getClauses());
	// for (ClauseInstanceJpaImpl c : s.getClauses()) {
	// LOG.trace("Validating Clause Instance.");
	// c.isValid(true);
	// assertNotNull(c.getParagraphs());
	// for (ParagraphInstanceJpaImpl p : c.getParagraphs()) {
	// LOG.trace("Validating Paragraph Instance.");
	// p.isValid(true);
	// }
	// }
	// }
	// }
}