package com.itgfirm.docengine.repository.content;

import static org.junit.Assert.*;

import static com.itgfirm.docengine.DocEngine.Constants.*;
import static com.itgfirm.docengine.util.TestUtils.*;

import org.hibernate.LazyInitializationException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.itgfirm.docengine.repository.content.AdvancedContentRepository;
import com.itgfirm.docengine.types.AdvancedDocumentJpaImpl;
import com.itgfirm.docengine.types.ClauseJpaImpl;
import com.itgfirm.docengine.types.ContentJpaImpl;
import com.itgfirm.docengine.types.ParagraphJpaImpl;
import com.itgfirm.docengine.types.SectionJpaImpl;

/**
 * @author Justin Scott
 * 
 *         Tests for the Content Repository
 * @param <T>
 * @param <T>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdvancedContentRepositoryTest extends ContentRepositoryTest {

	@Autowired
	@Qualifier(AUTOWIRE_QUALIFIER_ADVANCED)
	private AdvancedContentRepository repo;

	@Test
	public void a_SaveTest() {
		// save 1
		ContentJpaImpl content = createContent(22);

		AdvancedDocumentJpaImpl document = new AdvancedDocumentJpaImpl(content, getRandomTestString(2));
		document = (AdvancedDocumentJpaImpl) repo.save(document);
		assertNotNull(document);
		assertTrue(document.isValid(true));

		SectionJpaImpl section = new SectionJpaImpl(content, getRandomTestString(3));
		document.addSection(section);
		section = (SectionJpaImpl) repo.save(section);
		assertNotNull(section);
		assertTrue(section.isValid(true));

		ClauseJpaImpl clause = new ClauseJpaImpl(content, getRandomTestString(4));
		section.addClause(clause);
		clause = (ClauseJpaImpl) repo.save(clause);
		assertNotNull(clause);
		assertTrue(clause.isValid(true));

		ParagraphJpaImpl paragraph = new ParagraphJpaImpl(content, getRandomTestString(5));
		clause.addParagraph(paragraph);
		paragraph = (ParagraphJpaImpl) repo.save(paragraph);
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));

		// save complex all at once
		document = createDocument(32);
		assertNotNull(document);
		assertTrue(document.isValid(true));
	}

	@Test
	public void b_FindTest() {
		// Get All
		AdvancedDocumentJpaImpl doc = createDocument(41);
		doc = (AdvancedDocumentJpaImpl) repo.findOne(doc.getId());
		assertNotNull(doc);
		assertTrue(doc.isValid(true));

		// Get sub-classes, no eagerKids
		doc = createDocument(91);
		assertTrue(AdvancedDocumentJpaImpl.class.equals(repo.findOne(doc.getId()).getClass()));
		assertTrue(AdvancedDocumentJpaImpl.class.equals(repo.findByContentCd(doc.getContentCd()).getClass()));
		try {
			((AdvancedDocumentJpaImpl) repo.findOne(doc.getId())).getSections().iterator().next();
			fail();
		} catch (LazyInitializationException e) {
			assertTrue(e.getClass().equals(LazyInitializationException.class));
		}
		assertTrue(SectionJpaImpl.class
				.equals(repo.findOne(doc.getSections().iterator().next().getId()).getClass()));
		assertTrue(SectionJpaImpl.class
				.equals(repo.findByContentCd(doc.getSections().iterator().next().getContentCd()).getClass()));
		try {
			((SectionJpaImpl) repo.findOne(doc.getSections().iterator().next().getId())).getClauses().iterator()
					.next();
			fail();
		} catch (LazyInitializationException e) {
			assertTrue(e.getClass().equals(LazyInitializationException.class));
		}
		assertTrue(ClauseJpaImpl.class.equals(repo
				.findOne(doc.getSections().iterator().next().getClauses().iterator().next().getId()).getClass()));
		assertTrue(ClauseJpaImpl.class.equals(repo
				.findByContentCd(doc.getSections().iterator().next().getClauses().iterator().next().getContentCd())
				.getClass()));
		try {
			((ClauseJpaImpl) repo
					.findOne(doc.getSections().iterator().next().getClauses().iterator().next().getId()))
							.getParagraphs().iterator().next();
			fail();
		} catch (LazyInitializationException e) {
			assertTrue(e.getClass().equals(LazyInitializationException.class));
		}
		assertTrue(ParagraphJpaImpl.class.equals(repo.findOne(doc.getSections().iterator().next().getClauses()
				.iterator().next().getParagraphs().iterator().next().getId()).getClass()));
		assertTrue(ParagraphJpaImpl.class.equals(repo.findByContentCd(doc.getSections().iterator().next()
				.getClauses().iterator().next().getParagraphs().iterator().next().getContentCd()).getClass()));
	}

	@Test
	public void x_DeleteTest() {
		AdvancedDocumentJpaImpl adv = (AdvancedDocumentJpaImpl) createDocument(71);
		repo.delete(adv);
		assertNull(repo.findOne(adv.getId()));
	}

	private AdvancedDocumentJpaImpl createDocument(final int seed) {
		return (AdvancedDocumentJpaImpl) repo.save(makeTestDocument(seed));
	}
}