package com.itgfirm.docengine.service.content;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.TreeSet;

import org.hibernate.LazyInitializationException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import com.itgfirm.docengine.service.content.ContentService;
import com.itgfirm.docengine.types.ClauseJpaImpl;
import com.itgfirm.docengine.types.Clauses;
import com.itgfirm.docengine.types.ContentJpaImpl;
import com.itgfirm.docengine.types.Contents;
import com.itgfirm.docengine.types.DocumentJpaImpl;
import com.itgfirm.docengine.types.ParagraphJpaImpl;
import com.itgfirm.docengine.types.Paragraphs;
import com.itgfirm.docengine.types.SectionJpaImpl;
import com.itgfirm.docengine.types.Sections;
import com.itgfirm.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * 
 *         Tests for the Content Service
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContentServiceTest extends AbstractTest {

	@Autowired
	protected ContentService _contents;

	@Test
	public void aa_SaveTest() {
		// One
		ContentJpaImpl content = makeTestContent();
		content = _contents.save(content);
		assertNotNull(content);
		assertTrue(content.isValid(true));
		assertNull(_contents.save((ContentJpaImpl) null));
		assertNull(_contents.save(new ContentJpaImpl("", "TEST BODY")));
		assertNull(_contents.save(new ContentJpaImpl(TEST_CONTENT_CODE_PREFIX + uuid(), "")));
		assertNull(_contents.save(new ContentJpaImpl(content, content.getContentCd())));

		// Many
		Contents result = _contents.save(new Contents(makeTestContents(9)));
		for (final ContentJpaImpl c : result.getContents()) {
			assertTrue(c.isValid(true));
		}
	}

	@Test
	public void ab_SaveDocumentTest() {
		// Merge 1
		ContentJpaImpl content = createContent();

		// Merge complex one at a time
		DocumentJpaImpl document = (DocumentJpaImpl) _contents
				.save(new DocumentJpaImpl(content, TEST_DOCUMENT_CODE_PREFIX + uuid()));
		assertNotNull(document.getId());

		SectionJpaImpl section = new SectionJpaImpl(content, TEST_SECTION_CODE_PREFIX + uuid());
		document.addSection(section);
		section = (SectionJpaImpl) _contents.save(section);
		assertNotNull(section.getId());

		ClauseJpaImpl clause = new ClauseJpaImpl(content, TEST_CLAUSE_CODE_PREFIX + uuid());
		section.addClause(clause);
		clause = (ClauseJpaImpl) _contents.save(clause);
		assertNotNull(clause.getId());

		ParagraphJpaImpl paragraph = new ParagraphJpaImpl(content, TEST_PARAGRAPH_CODE_PREFIX + uuid());
		clause.addParagraph(paragraph);
		paragraph = (ParagraphJpaImpl) _contents.save(paragraph);
		assertNotNull(paragraph.getId());

		// Need to re-get after assembly to delete.
		document = (DocumentJpaImpl) _contents.findOne(document.getId(), DocumentJpaImpl.class);
		assertNotNull(document);
		assertTrue(document.isValid(true));

		// Merge complex all at once
		document = createDocument();

		// Merge a list
		Collection<ContentJpaImpl> list = new TreeSet<ContentJpaImpl>();
		list.add(createContent());
		list.add(createContent());
		list.add(createContent());
		Collection<ContentJpaImpl> iter = list;
		Contents contents = new Contents(iter.toArray(new ContentJpaImpl[list.size()]));
		contents = _contents.save(contents);
		for (ContentJpaImpl c : contents.getContents()) {
			assertNotNull(c.getId());
		}
		assertNull(_contents.save((ContentJpaImpl) null));
		assertNull(_contents.save((ContentJpaImpl) new ContentJpaImpl("", "TEST BODY")));
		assertNull(_contents.save((ContentJpaImpl) new ContentJpaImpl(TEST_CONTENT_CODE_PREFIX + uuid(), "")));
		assertNull(_contents
				.save((ContentJpaImpl) new ContentJpaImpl(new ContentJpaImpl(""), TEST_CONTENT_CODE_PREFIX + uuid())));
		content = createContent();
		assertNull(_contents.save((ContentJpaImpl) new ContentJpaImpl(content, content.getContentCd())));
	}

	@Test
	public void ba_FindTest() {
		// Find one
		ContentJpaImpl content = createContent();
		content = _contents.findOne(content.getId());
		assertNotNull(content);
		assertTrue(content.isValid(true));
		assertNull(_contents.findOne((Long) null));
		assertNull(_contents.findOne(Long.MIN_VALUE));
		assertNull(_contents.findOne(Long.MAX_VALUE));

		// Find by code
		content = _contents.findByCode(content.getContentCd());
		assertNotNull(content);
		assertTrue(content.isValid(true));
		assertNull(_contents.findByCode((String) null));
		assertNull(_contents.findByCode(""));
		assertNull(_contents.findByCode("%Snicklefritz%"));

		// Find by code like
		createContents(5);
		Contents contents = _contents.findByCodeLike("%TEST%");
		assertNotNull(contents);
		assertNotNull(contents.getContents());
		assertNull(_contents.findByCodeLike((String) null));
		assertNull(_contents.findByCodeLike(""));
		assertNull(_contents.findByCodeLike("%Snicklefritz%"));

		// Find all
		createContents(5);
		contents = _contents.findAll();
		for (final ContentJpaImpl c : contents.getContents()) {
			assertTrue(c.isValid(true));
		}
	}

	@Test
	public void bb_FindDocumentTest() {
		// Get sub-classes, no eagerKids
		DocumentJpaImpl document = createDocument();
		Long id = document.getId();
		String code = document.getContentCd();
		document = _contents.findOne(id, DocumentJpaImpl.class);
		assertNotNull(document);
		assertTrue(document.isValid(true));
		assertTrue(DocumentJpaImpl.class.equals(document.getClass()));
		document = _contents.findByCode(code, DocumentJpaImpl.class);
		assertNotNull(document);
		assertTrue(document.isValid(true));
		assertTrue(DocumentJpaImpl.class.equals(document.getClass()));

		try {
			_contents.findOne(id, DocumentJpaImpl.class).getSections().iterator().next().getId();
			fail();
		} catch (final Exception e) {
			assertTrue(e.getClass().equals(LazyInitializationException.class));
		}
		
		document = _contents.findOne(id, DocumentJpaImpl.class, true);
		assertNotNull(document);
		assertTrue(document.isValid(true));
		assertTrue(DocumentJpaImpl.class.equals(document.getClass()));
		assertFalse(document.getSections().isEmpty());
		
		assertTrue(SectionJpaImpl.class
				.equals(_contents.findOne(document.getSections().iterator().next().getId()).getClass()));
		assertTrue(SectionJpaImpl.class
				.equals(_contents.findByCode(document.getSections().iterator().next().getContentCd()).getClass()));
		try {
			((SectionJpaImpl) _contents.findOne(document.getSections().iterator().next().getId())).getClauses()
					.iterator().next();
			fail();
		} catch (final Exception e) {
			assertTrue(e.getClass().equals(LazyInitializationException.class));
		}
		assertTrue(ClauseJpaImpl.class.equals(_contents
				.findOne(document.getSections().iterator().next().getClauses().iterator().next().getId(), ClauseJpaImpl.class).getClass()));
		assertTrue(ClauseJpaImpl.class.equals(_contents
				.findByCode(document.getSections().iterator().next().getClauses().iterator().next().getContentCd())
				.getClass()));
		try {
			((ClauseJpaImpl) _contents
					.findOne(document.getSections().iterator().next().getClauses().iterator().next().getId(), ClauseJpaImpl.class))
							.getParagraphs().iterator().next();
			fail();
		} catch (final Exception e) {
			assertTrue(e.getClass().equals(LazyInitializationException.class));
		}
		assertTrue(ParagraphJpaImpl.class.equals(_contents.findOne(document.getSections().iterator().next().getClauses()
				.iterator().next().getParagraphs().iterator().next().getId(), ParagraphJpaImpl.class).getClass()));
		assertTrue(ParagraphJpaImpl.class.equals(_contents.findByCode(document.getSections().iterator().next()
				.getClauses().iterator().next().getParagraphs().iterator().next().getContentCd()).getClass()));

		// Get Children by ID
		for (final SectionJpaImpl s : _contents.getChildren(id, Sections.class).getSections()) {
			try {
				s.getClauses().iterator().hasNext();
				fail();
			} catch (final Exception e) {
				assertTrue(e.getClass().equals(LazyInitializationException.class));
			}
			for (final ClauseJpaImpl c : _contents.getChildren(s.getId(), Clauses.class).getClauses()) {
				try {
					c.getParagraphs().iterator().hasNext();
					fail();
				} catch (final Exception e) {
					assertTrue(e.getClass().equals(LazyInitializationException.class));
				}
				for (final ContentJpaImpl p : _contents.getChildren(c.getId(), Paragraphs.class).getParagraphs()) {
					assertTrue(p.isValid(true));
				}
			}
		}

		// Get Children by ID with kids
		for (SectionJpaImpl s : _contents.getChildren(document.getId(), Sections.class, true).getSections()) {
			assertTrue(s.isValid(true));
			for (ClauseJpaImpl c : _contents.getChildren(s.getId(), Clauses.class, true).getClauses()) {
				assertTrue(c.isValid(true));
				for (ParagraphJpaImpl p : _contents.getChildren(c.getId(), Paragraphs.class, true).getParagraphs()) {
					assertTrue(p.isValid(true));
				}
			}
		}

		// Get Children by code
		for (SectionJpaImpl s : _contents.getChildren(document.getContentCd(), Sections.class).getSections()) {
			for (ClauseJpaImpl c : _contents.getChildren(s.getContentCd(), Clauses.class).getClauses()) {
				for (ParagraphJpaImpl p : _contents.getChildren(c.getContentCd(), Paragraphs.class).getParagraphs()) {
					assertNotNull(p.getContentCd());
				}
			}
		}

		// Get Children by code with kids
		for (SectionJpaImpl s : _contents.getChildren(document.getContentCd(), Sections.class, true).getSections()) {
			assertTrue(s.isValid(true));
			assertTrue(s.getClauses().iterator().hasNext());
			assertNotNull(s.getClauses().iterator().next().getId());
			for (ClauseJpaImpl c : _contents.getChildren(s.getContentCd(), Clauses.class, true).getClauses()) {
				assertTrue(c.isValid(true));
				assertTrue(c.getParagraphs().iterator().hasNext());
				assertNotNull(c.getParagraphs().iterator().next().getId());
				for (ParagraphJpaImpl p : _contents.getChildren(c.getContentCd(), Paragraphs.class, true)
						.getParagraphs()) {
					assertTrue(p.isValid(true));
				}
			}
		}
		assertNull(_contents.findByCode((String) null));
		assertNull(_contents.findByCode(""));
		assertNull(_contents.findOne(0L));
		assertNull(_contents.findOne(99999999999999999L));
		assertNull(_contents.findByCode("Snicklefritz"));
	}

	@Test
	public void x_DeleteTest() {
		ContentJpaImpl content = createContent();
		_contents.delete(content);
		assertNull(_contents.findOne(content.getId()));
		content = createContent();
		_contents.delete(content.getContentCd());
		assertNull(_contents.findByCode(content.getContentCd()));
		Contents contents = createContents(3);
		_contents.delete(contents);
		for (final ContentJpaImpl c : contents.getContents()) {
			assertNull(_contents.findOne(c.getId()));
		}
		createContents(5);
		contents = _contents.findByCodeLike("%TEST%");
		assertNotNull(contents);
		assertNotNull(contents.getContents());
		assertNull(_contents.findByCodeLike((String) null));
		assertNull(_contents.findByCodeLike(""));
		assertNull(_contents.findByCodeLike("%Snicklefritz%"));
	}
	
	private Contents createContents(int count) {
		Collection<ContentJpaImpl> contents = new TreeSet<ContentJpaImpl>();
		for (int i = 0; i < count; i++) {
			contents.add(createContent());
		}
		return new Contents(contents);
	}
	
	private ContentJpaImpl createContent() {
		ContentJpaImpl content = makeTestContent();
		content = _contents.save(content);
		assertNotNull(content);
		assertTrue(content.isValid(true));
		return content;
	}
	
	private DocumentJpaImpl createDocument() {
		DocumentJpaImpl document = makeTestDocumentComplete();
		document = _contents.save(document);
		assertNotNull(document);
		assertTrue(document.isValid(true));
		return document;
	}
}