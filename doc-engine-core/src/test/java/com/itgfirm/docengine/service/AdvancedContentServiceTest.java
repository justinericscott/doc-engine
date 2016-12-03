package com.itgfirm.docengine.service;

import static org.junit.Assert.*;
import static com.itgfirm.docengine.DocEngine.Constants.*;
import static com.itgfirm.docengine.util.TestUtils.*;

import java.util.Collection;
import java.util.TreeSet;

import org.hibernate.LazyInitializationException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.itgfirm.docengine.service.AdvancedContentService;
import com.itgfirm.docengine.types.jpa.AdvancedDocumentJpaImpl;
import com.itgfirm.docengine.types.jpa.ClauseJpaImpl;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.types.jpa.ParagraphJpaImpl;
import com.itgfirm.docengine.types.jpa.SectionJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * 
 *         Tests for the Content Service
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdvancedContentServiceTest extends AbstractTest {

	@Autowired
	@Qualifier(AUTOWIRE_QUALIFIER_ADVANCED)
	private AdvancedContentService service;

	@Test
	public void a_SaveTest() {
		// Merge 1
		ContentJpaImpl content = createContent();

		// Merge complex one at a time
		AdvancedDocumentJpaImpl document = (AdvancedDocumentJpaImpl) service.save(new AdvancedDocumentJpaImpl(content, getRandomTestString(2)));
		assertNotNull(document.getId());

		SectionJpaImpl section = new SectionJpaImpl(content, getRandomTestString(3));
		document.addSection(section);
		section = (SectionJpaImpl) service.save(section);
		assertNotNull(section.getId());

		ClauseJpaImpl clause = new ClauseJpaImpl(content, getRandomTestString(4));
		section.addClause(clause);
		clause = (ClauseJpaImpl) service.save(clause);
		assertNotNull(clause.getId());

		ParagraphJpaImpl paragraph = new ParagraphJpaImpl(content, getRandomTestString(5));
		clause.addParagraph(paragraph);
		paragraph = (ParagraphJpaImpl) service.save(paragraph);
		assertNotNull(paragraph.getId());

		// Need to re-get after assembly to delete.
		document = (AdvancedDocumentJpaImpl) service.findOne(document.getId(), true);
		assertTrue(document.isValid(true));

		// Merge complex all at once
		document = createDocument(33);

		// Merge a list
		Collection<ContentJpaImpl> list = new TreeSet<ContentJpaImpl>();
		list.add(createContent(1));
		list.add(createContent(2));
		list.add(createContent(3));
		Iterable<? extends ContentJpaImpl> iter = list;
		iter = service.save(iter);
		for (ContentJpaImpl c : iter) {
			assertNotNull(c.getId());
		}
		assertNull(service.save((ContentJpaImpl) null));
		assertNull(service.save((ContentJpaImpl) new ContentJpaImpl("", "TEST BODY")));
		assertNull(service.save((ContentJpaImpl) new ContentJpaImpl(getRandomTestString(1), "")));
		assertNull(service.save((ContentJpaImpl) new ContentJpaImpl(new ContentJpaImpl(""), getRandomTestString(2))));
		content = createContent();
		assertNull(service.save((ContentJpaImpl) new ContentJpaImpl(content, content.getContentCd())));
	}

	@Test
	public void b_FindTest() {
		// Get sub-classes, no eagerKids
		final AdvancedDocumentJpaImpl document = createDocument(19);
		assertTrue(AdvancedDocumentJpaImpl.class.equals(service.findOne(document.getId()).getClass()));
		assertTrue(AdvancedDocumentJpaImpl.class.equals(service.findByContentCd(document.getContentCd()).getClass()));
		try {
			((AdvancedDocumentJpaImpl) service.findOne(document.getId())).getSections().iterator().next().getId();
			fail();
		} catch (final LazyInitializationException e) {
			assertTrue(e.getClass().equals(LazyInitializationException.class));
		}
		assertTrue(SectionJpaImpl.class
				.equals(service.findOne(document.getSections().iterator().next().getId()).getClass()));
		assertTrue(SectionJpaImpl.class
				.equals(service.findByContentCd(document.getSections().iterator().next().getContentCd()).getClass()));
		try {
			((SectionJpaImpl) service.findOne(document.getSections().iterator().next().getId())).getClauses().iterator()
					.next();
			fail();
		} catch (final LazyInitializationException e) {
			assertTrue(e.getClass().equals(LazyInitializationException.class));
		}
		assertTrue(ClauseJpaImpl.class.equals(service
				.findOne(document.getSections().iterator().next().getClauses().iterator().next().getId()).getClass()));
		assertTrue(ClauseJpaImpl.class.equals(service
				.findByContentCd(document.getSections().iterator().next().getClauses().iterator().next().getContentCd())
				.getClass()));
		try {
			((ClauseJpaImpl) service
					.findOne(document.getSections().iterator().next().getClauses().iterator().next().getId()))
							.getParagraphs().iterator().next();
			fail();
		} catch (final LazyInitializationException e) {
			assertTrue(e.getClass().equals(LazyInitializationException.class));
		}
		assertTrue(ParagraphJpaImpl.class.equals(service.findOne(document.getSections().iterator().next().getClauses()
				.iterator().next().getParagraphs().iterator().next().getId()).getClass()));
		assertTrue(ParagraphJpaImpl.class.equals(service.findByContentCd(document.getSections().iterator().next()
				.getClauses().iterator().next().getParagraphs().iterator().next().getContentCd()).getClass()));

		// Get Children by ID
		for (final ContentJpaImpl s : service.getChildren(document.getId())) {
			if (s instanceof SectionJpaImpl) {
				try {
					((SectionJpaImpl) s).getClauses().iterator().next();
					fail();
				} catch (final LazyInitializationException e) {
					assertTrue(e.getClass().equals(LazyInitializationException.class));
				}
				for (final ContentJpaImpl c : service.getChildren(s.getId())) {
					if (c instanceof ClauseJpaImpl) {
						try {
							((ClauseJpaImpl) c).getParagraphs().iterator().next();
							fail();
						} catch (final LazyInitializationException e) {
							assertTrue(e.getClass().equals(LazyInitializationException.class));
						}
						for (final ContentJpaImpl p : service.getChildren(c.getId())) {
							if (p instanceof ParagraphJpaImpl) {
								assertTrue(p.isValid(true));
							} else {
								throw new IllegalStateException();
							}
						}
					} else {
						throw new IllegalStateException();
					}
				}
			} else {
				throw new IllegalStateException();
			}
		}

		// Get Children by ID with kids
		for (ContentJpaImpl s : service.getChildren(document.getId(), true)) {
			if (s instanceof SectionJpaImpl) {
				SectionJpaImpl section = (SectionJpaImpl) s;
				assertTrue(section.isValid(true));
				for (ContentJpaImpl c : service.getChildren(s.getId(), true)) {
					if (c instanceof ClauseJpaImpl) {
						ClauseJpaImpl clause = (ClauseJpaImpl) c;
						assertTrue(clause.isValid(true));
						for (ContentJpaImpl p : service.getChildren(c.getId(), true)) {
							if (p instanceof ParagraphJpaImpl) {
								assertTrue(p.isValid(true));
							} else {
								throw new IllegalStateException();
							}
						}
					} else {
						throw new IllegalStateException();
					}
				}
			} else {
				throw new IllegalStateException();
			}
		}

		// Get Children by code
		for (ContentJpaImpl s : service.getChildren(document.getContentCd())) {
			if (s instanceof SectionJpaImpl) {
				for (ContentJpaImpl c : service.getChildren(s.getContentCd())) {
					if (c instanceof ClauseJpaImpl) {
						for (ContentJpaImpl p : service.getChildren(c.getContentCd())) {
							if (p instanceof ParagraphJpaImpl) {
								assertNotNull(p.getContentCd());
							} else {
								throw new IllegalStateException();
							}
						}
					} else {
						throw new IllegalStateException();
					}
				}
			} else {
				throw new IllegalStateException();
			}
		}

		// Get Children by code with kids
		for (ContentJpaImpl s : service.getChildren(document.getContentCd(), true)) {
			if (s instanceof SectionJpaImpl) {
				SectionJpaImpl section = (SectionJpaImpl) s;
				assertTrue(section.isValid(true));
				for (ContentJpaImpl c : service.getChildren(s.getContentCd(), true)) {
					if (c instanceof ClauseJpaImpl) {
						ClauseJpaImpl clause = (ClauseJpaImpl) c;
						assertTrue(clause.isValid(true));
						assertNotNull(clause.getParagraphs().iterator().next().getId());
						for (ContentJpaImpl p : service.getChildren(c.getContentCd(), true)) {
							if (p instanceof ParagraphJpaImpl) {
								assertTrue(p.isValid(true));
							} else {
								throw new IllegalStateException();
							}
						}
					} else {
						throw new IllegalStateException();
					}
				}
			} else {
				throw new IllegalStateException();
			}
		}
		assertNull(service.findByContentCd((String) null));
		assertNull(service.findByContentCd(""));
		assertNull(service.findOne(0L));
		assertNull(service.findOne(99999999999999999L));
		assertNull(service.findByContentCd("Snicklefritz"));
	}

	@Test
	public void x_DeleteTest() {

	}

	private AdvancedDocumentJpaImpl createDocument(int seed) {
		AdvancedDocumentJpaImpl document = (AdvancedDocumentJpaImpl) service.save(makeTestDocument(seed));
		assertTrue(document.isValid(true));
		return document;
	}

	private ContentJpaImpl createContent() {
		return createContent(1);
	}

	private ContentJpaImpl createContent(int seed) {
		return service.save(makeTestContent(seed));
	}
}