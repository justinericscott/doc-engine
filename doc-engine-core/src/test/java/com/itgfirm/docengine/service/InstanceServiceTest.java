/**TODO: License
 */
package com.itgfirm.docengine.service;

import static com.itgfirm.docengine.util.Constants.*;
import static com.itgfirm.docengine.util.TestUtils.*;
import static org.junit.Assert.*;

import java.util.Collection;
import java.util.TreeSet;

import org.hibernate.LazyInitializationException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.itgfirm.docengine.service.AdvancedContentService;
import com.itgfirm.docengine.service.InstanceService;
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
 *         Tests for the Instance Service
 * @param <T>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InstanceServiceTest extends AbstractTest {
	private static final Logger LOG = LoggerFactory.getLogger(InstanceServiceTest.class);

	@Autowired
	@Qualifier(AUTOWIRE_QUALIFIER_INSTANCE)
	private InstanceService service;
	@Autowired
	@Qualifier(AUTOWIRE_QUALIFIER_ADVANCED)
	private AdvancedContentService advanced;

	@Test
	public void a_SaveTest() {
		String projectId = getRandomTestString(1);
		ContentJpaImpl content = createContent(12);
		// Save one
		InstanceJpaImpl instance = createInstance(content);
		assertNotNull(instance);
		assertNotNull(instance.getId());

		// Save advanced one at a time
		AdvancedDocumentJpaImpl doc = (AdvancedDocumentJpaImpl) advanced
				.save(new AdvancedDocumentJpaImpl(getRandomTestString(2), "TEST_BODY"));
		assertNotNull(doc);
		assertNotNull(doc.getId());
		AdvancedDocumentInstanceJpaImpl document = new AdvancedDocumentInstanceJpaImpl(doc, projectId);
		document = (AdvancedDocumentInstanceJpaImpl) service.save(document);
		assertNotNull(document);
		assertNotNull(document.getId());

		SectionJpaImpl sec = new SectionJpaImpl(getRandomTestString(3), "TEST_BODY");
		doc.addSection(sec);
		sec = (SectionJpaImpl) advanced.save(sec);
		assertNotNull(sec);
		assertNotNull(sec.getId());
		SectionInstanceJpaImpl section = new SectionInstanceJpaImpl(sec, projectId);
		document.addSection(section);
		section = (SectionInstanceJpaImpl) service.save(section);
		assertNotNull(section);
		assertNotNull(section.getId());

		ClauseJpaImpl cla = new ClauseJpaImpl(getRandomTestString(4), "TEST_BODY");
		sec.addClause(cla);
		cla = (ClauseJpaImpl) advanced.save(cla);
		assertNotNull(cla);
		assertNotNull(cla.getId());
		ClauseInstanceJpaImpl clause = new ClauseInstanceJpaImpl(cla, projectId);
		section.addClause(clause);
		clause = (ClauseInstanceJpaImpl) service.save(clause);
		assertNotNull(clause);
		assertNotNull(clause.getId());

		ParagraphJpaImpl para = new ParagraphJpaImpl(getRandomTestString(5), "TEST_BODY");
		cla.addParagraph(para);
		para = (ParagraphJpaImpl) advanced.save(para);
		assertNotNull(para);
		assertNotNull(para.getId());
		ParagraphInstanceJpaImpl paragraph = new ParagraphInstanceJpaImpl(para, projectId);
		clause.addParagraph(paragraph);
		paragraph = (ParagraphInstanceJpaImpl) service.save(paragraph);
		assertNotNull(paragraph);
		assertNotNull(paragraph.getId());

		document = (AdvancedDocumentInstanceJpaImpl) service.findOne(document.getId(), true);
		assertNotNull(document);
		assertNotNull(document.getId());
		validate(document);

		// Save advanced all at once
		document = createDocumentInstance(createDocument(79));
		assertNotNull(document);
		assertNotNull(document.getId());
		validate(document);

		// Save a collection
		Collection<InstanceJpaImpl> instances = new TreeSet<InstanceJpaImpl>();
		Iterable<? extends ContentJpaImpl> contents = createContents(3);
		contents = advanced.save(contents);
		contents.forEach(c -> {
			instances.add(new InstanceJpaImpl(c, projectId));
		});
		Iterable<? extends InstanceJpaImpl> iter = service.save(instances);
		assertNotNull(iter);
		assertTrue(iter.iterator().hasNext());
		iter.forEach(inst -> {
			validate(inst);
		});
		content = createContent(11);
		instance = new InstanceJpaImpl();
		instance.setProjectId(projectId);
		assertNull(service.save(instance));
		instance = new InstanceJpaImpl(content);
		assertNull(service.save(instance));
		content = new ContentJpaImpl("TEST_BODY");
		instance = new InstanceJpaImpl(content, projectId);
		assertNull(service.save(instance));
	}

	@Test
	public void b_FindTest() {
		final String projectId = getRandomTestString(1);

		// Find all
		Collection<InstanceJpaImpl> instances = new TreeSet<InstanceJpaImpl>();
		Iterable<? extends ContentJpaImpl> contents = createContents(3);
		contents = advanced.save(contents);
		contents.forEach(c -> {
			instances.add(new InstanceJpaImpl(c, projectId));
		});
		Iterable<? extends InstanceJpaImpl> iter = service.save(instances);
		assertNotNull(iter);
		assertTrue(iter.iterator().hasNext());
		iter = service.findAll();
		assertNotNull(iter);
		assertTrue(iter.iterator().hasNext());
		iter.forEach(i -> {
			validate(i);
		});

		// Find One
		InstanceJpaImpl instance = createInstance(createContent(22));
		instance = service.findOne(instance.getId());
		assertNotNull(instance);
		assertNotNull(instance.getId());

		// Find by Project ID and Content CD
		instance = service.findByProjectIdAndContentCd(instance.getProjectId(), instance.getContent().getContentCd());
		assertNotNull(instance);
		assertNotNull(instance.getId());

		// Find sub-classes, no eagerKids
		AdvancedDocumentInstanceJpaImpl document = createDocumentInstance(createDocument(35));
		assertEquals(AdvancedDocumentInstanceJpaImpl.class, service.findOne(document.getId()).getClass());
		assertEquals(AdvancedDocumentInstanceJpaImpl.class, service
				.findByProjectIdAndContentCd(document.getProjectId(), document.getContent().getContentCd()).getClass());
		try {
			((AdvancedDocumentInstanceJpaImpl) service.findOne(document.getId())).getSections().iterator().next();
			fail();
		} catch (final Exception e) {
			assertEquals(LazyInitializationException.class, e.getClass());
		}

		assertEquals(SectionInstanceJpaImpl.class,
				service.findOne(document.getSections().iterator().next().getId()).getClass());
		assertEquals(SectionInstanceJpaImpl.class, service.findByProjectIdAndContentCd(document.getProjectId(),
				document.getSections().iterator().next().getContent().getContentCd()).getClass());
		try {
			((SectionInstanceJpaImpl) service.findOne(document.getSections().iterator().next().getId())).getClauses()
					.iterator().next();
			fail();
		} catch (final Exception e) {
			assertEquals(LazyInitializationException.class, e.getClass());
		}

		assertEquals(ClauseInstanceJpaImpl.class, service
				.findOne(document.getSections().iterator().next().getClauses().iterator().next().getId()).getClass());
		assertEquals(ClauseInstanceJpaImpl.class, service.findByProjectIdAndContentCd(document.getProjectId(),
				document.getSections().iterator().next().getClauses().iterator().next().getContent().getContentCd())
				.getClass());
		try {
			((ClauseInstanceJpaImpl) service
					.findOne(document.getSections().iterator().next().getClauses().iterator().next().getId()))
							.getParagraphs().iterator().next();
			fail();
		} catch (final Exception e) {
			assertEquals(LazyInitializationException.class, e.getClass());
		}

		assertEquals(ParagraphInstanceJpaImpl.class, service.findOne(document.getSections().iterator().next()
				.getClauses().iterator().next().getParagraphs().iterator().next().getId()).getClass());
		assertEquals(ParagraphInstanceJpaImpl.class,
				service.findByProjectIdAndContentCd(document.getProjectId(), document.getSections().iterator().next()
						.getClauses().iterator().next().getParagraphs().iterator().next().getContent().getContentCd())
						.getClass());

		// Get Children by ID
		Long docId = document.getId();
		Iterable<? extends InstanceJpaImpl> sections = service.findByParent(docId);
		assertNotNull(sections);
		sections.forEach(s -> {
			if (s instanceof SectionInstanceJpaImpl) {
				try {
					((SectionInstanceJpaImpl) s).getClauses().iterator().next();
					fail();
				} catch (final Exception e) {
					assertEquals(LazyInitializationException.class, e.getClass());
				}
				Long sectionId = s.getId();
				Iterable<? extends InstanceJpaImpl> clauses = service.findByParent(sectionId);
				clauses.forEach(c -> {
					if (c instanceof ClauseInstanceJpaImpl) {
						try {
							((ClauseInstanceJpaImpl) c).getParagraphs().iterator().next();
							fail();
						} catch (final Exception e) {
							assertEquals(LazyInitializationException.class, e.getClass());
						}
						Long clauseId = c.getId();
						Iterable<? extends InstanceJpaImpl> paras = service.findByParent(clauseId);
						paras.forEach(p -> {
							if (p instanceof ParagraphInstanceJpaImpl) {
								validate(p);
							}
						});
					}
				});
			}
		});

		// Get Children by ID with kids
		for (InstanceJpaImpl s : service.findByParent(document.getId(), true)) {
			if (s instanceof SectionInstanceJpaImpl) {
				SectionInstanceJpaImpl section = (SectionInstanceJpaImpl) s;
				validate(section);
				for (InstanceJpaImpl c : service.findByParent(s.getId(), true)) {
					if (c instanceof ClauseInstanceJpaImpl) {
						ClauseInstanceJpaImpl clause = (ClauseInstanceJpaImpl) c;
						validate(clause);
						for (InstanceJpaImpl p : service.findByParent(c.getId(), true)) {
							if (p instanceof ParagraphInstanceJpaImpl) {
								validate((ParagraphInstanceJpaImpl) p);
							}
						}
					}
				}
			}
		}

		// Get Children by code
		for (InstanceJpaImpl s : service.findByParent(document.getProjectId(), document.getContent().getContentCd())) {
			if (s instanceof SectionInstanceJpaImpl) {
				for (InstanceJpaImpl c : service.findByParent(s.getProjectId(), s.getContent().getContentCd())) {
					if (c instanceof ClauseInstanceJpaImpl) {
						for (InstanceJpaImpl p : service.findByParent(c.getProjectId(),
								c.getContent().getContentCd())) {
							if (p instanceof ParagraphInstanceJpaImpl) {
								assertNotNull(p.getId());
							}
						}
					}
				}
			}
		}

		// Get Children by code with kids
		for (InstanceJpaImpl s : service.findByParent(document.getProjectId(), document.getContent().getContentCd(),
				true)) {
			if (s instanceof SectionInstanceJpaImpl) {
				SectionInstanceJpaImpl section = (SectionInstanceJpaImpl) s;
				validate(section);
				for (InstanceJpaImpl c : service.findByParent(s.getProjectId(), s.getContent().getContentCd(), true)) {
					if (c instanceof ClauseInstanceJpaImpl) {
						ClauseInstanceJpaImpl clause = (ClauseInstanceJpaImpl) c;
						validate(clause);
						for (InstanceJpaImpl p : service.findByParent(c.getProjectId(), c.getContent().getContentCd(),
								true)) {
							if (p instanceof ParagraphInstanceJpaImpl) {
								validate((ParagraphInstanceJpaImpl) p);
							}
						}
					}
				}
			}
		}

		// Project ID and Content Code Like
		instance = createInstance(createContent(77));
		assertNotNull(instance.getId());
		assertNotNull(service.findByProjectIdAndContentCdLike(instance.getProjectId(), "TEST"));

		
		// Invalids
		instance = createInstance(createContent(55));
		instance = service.save(instance);
		String code = instance.getContent().getContentCd();

		assertNotNull(instance.getId());
		assertNotNull(instance.getContent().getId());

		assertNull(service.findOne(0L));
		assertNull(service.findOne(99999999L));
		assertNull(service.findOne((Long) null));

		assertNull(service.findByProjectIdAndContentCd("", code));
		assertNull(service.findByProjectIdAndContentCd(null, code));
		assertNull(service.findByProjectIdAndContentCd("Snicklefritz", code));
		assertNull(service.findByProjectIdAndContentCd("TEST_PROJECT_ID", null));
		assertNull(service.findByProjectIdAndContentCd("TEST_PROJECT_ID", ""));
		assertNull(service.findByProjectIdAndContentCd("TEST_PROJECT_ID", "Snicklefritz"));

		instance = createInstance(createContent(88));
		assertNotNull(instance.getId());
		assertNull(service.findByProjectIdAndContentCdLike(null, TEST_CODE_PREFIX));
		assertNull(service.findByProjectIdAndContentCdLike("", TEST_CODE_PREFIX));
		assertFalse(service.findByProjectIdAndContentCdLike("Snicklefritz", TEST_CODE_PREFIX).iterator().hasNext());
		assertNull(service.findByProjectIdAndContentCdLike(instance.getProjectId(), ""));
		assertNull(service.findByProjectIdAndContentCdLike(instance.getProjectId(), null));
		assertFalse(service.findByProjectIdAndContentCdLike(instance.getProjectId(), "Snicklefritz").iterator().hasNext());
	
	}

	@Test
	public void x_DeleteTest() {
		InstanceJpaImpl instance = createInstance(createContent(99));
		String projectId = instance.getProjectId();
		String code = instance.getContent().getContentCd();
		service.delete(instance.getId());
		advanced.delete(instance.getContent());
		assertNull(service.findOne(instance.getId()));
		instance = createInstance(createContent(44));
		code = instance.getContent().getContentCd();
		projectId = instance.getProjectId();
		service.delete(projectId, code);
		assertNull(service.findOne(instance.getId()));
		// instanceService.deleteAll();
		advanced.delete(instance.getContent());
	}

	// private ContentJpaImpl createContent() {
	// return createContent(1);
	// }

	private ContentJpaImpl createContent(int seed) {
		ContentJpaImpl content = (ContentJpaImpl) advanced.save(makeTestContent(seed));
		assertTrue(content.isValid(true));
		return content;
	}

	private Iterable<ContentJpaImpl> createContents(int count) {
		Collection<ContentJpaImpl> contents = new TreeSet<ContentJpaImpl>();
		for (int i = 0; i < count; i++) {
			contents.add(createContent((count + i) * 3));
		}
		return contents;
	}

	private InstanceJpaImpl createInstance(ContentJpaImpl content) {
		InstanceJpaImpl instance = service.save(new InstanceJpaImpl(content, getRandomTestString(2)));
		validate(instance);
		return instance;
	}

	private AdvancedDocumentJpaImpl createDocument(int seed) {
		AdvancedDocumentJpaImpl document = (AdvancedDocumentJpaImpl) advanced.save(makeTestDocument(seed));
		validate(document);
		return document;
	}

	private AdvancedDocumentInstanceJpaImpl createDocumentInstance(AdvancedDocumentJpaImpl document) {
		AdvancedDocumentInstanceJpaImpl instance = (AdvancedDocumentInstanceJpaImpl) service
				.save(new AdvancedDocumentInstanceJpaImpl(document, getRandomTestString(1)));
		validate(instance);
		return instance;
	}

	protected void validate(AdvancedDocumentJpaImpl document) {
		assertTrue(document.isValid(true));
		assertNotNull(document.getSections());
		for (SectionJpaImpl s : document.getSections()) {
			validate(s);
		}
	}

	protected void validate(SectionJpaImpl section) {
		assertTrue(section.isValid(true));
		assertNotNull(section.getClauses());
		for (ClauseJpaImpl c : section.getClauses()) {
			validate(c);
		}
	}

	protected void validate(ClauseJpaImpl clause) {
		assertTrue(clause.isValid(true));
		assertNotNull(clause.getParagraphs());
		for (ParagraphJpaImpl p : clause.getParagraphs()) {
			validate(p);
		}
	}

	protected void validate(ParagraphJpaImpl paragraph) {
		assertTrue(paragraph.isValid(true));
	}

	protected void validate(InstanceJpaImpl instance) {
		assertTrue(instance.isValid(true));
		assertNotNull(instance.getContent().getId());
	}

	protected void validate(AdvancedDocumentInstanceJpaImpl document) {
		assertNotNull(document);
		assertTrue(document.isValid(true));
		LOG.trace("Validating Document Instance.");
		assertNotNull(document.getSections());
		for (SectionInstanceJpaImpl s : document.getSections()) {
			LOG.trace("Validating Section Instance.");
			s.isValid(true);
			assertNotNull(s.getClauses());
			for (ClauseInstanceJpaImpl c : s.getClauses()) {
				LOG.trace("Validating Clause Instance.");
				c.isValid(true);
				assertNotNull(c.getParagraphs());
				for (ParagraphInstanceJpaImpl p : c.getParagraphs()) {
					LOG.trace("Validating Paragraph Instance.");
					p.isValid(true);
				}
			}
		}
	}
}