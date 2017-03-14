package com.github.justinericscott.docengine.service.content;

import static org.junit.Assert.*;
import static com.github.justinericscott.docengine.util.TestUtils.TestConstants.*;

import java.util.Collection;
import java.util.TreeSet;

import org.hibernate.LazyInitializationException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.justinericscott.docengine.models.Content;
import com.github.justinericscott.docengine.models.Content.Clause;
import com.github.justinericscott.docengine.models.Content.Document;
import com.github.justinericscott.docengine.models.Content.Paragraph;
import com.github.justinericscott.docengine.models.Content.Section;
import com.github.justinericscott.docengine.models.Contents;
import com.github.justinericscott.docengine.models.Contents.Clauses;
import com.github.justinericscott.docengine.models.Contents.Documents;
import com.github.justinericscott.docengine.models.Contents.Paragraphs;
import com.github.justinericscott.docengine.models.Contents.Sections;
import com.github.justinericscott.docengine.service.content.ContentService;
import com.github.justinericscott.docengine.util.AbstractTest;

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
		Content content = makeTestContent();
		content = _contents.save(content);
		assertNotNull(content);
		assertTrue(content.isValid(true));
		assertNull(_contents.save((Content) null));
		assertNull(_contents.save(new Content("", "TEST BODY")));
		assertNull(_contents.save(new Content(TEST_CODE_PREFIX_CONTENT + uuid(), "")));
		assertNull(_contents.save(new Content(content, content.getContentCd())));

		// Many
		Contents result = _contents.save(new Contents(makeTestContents(9)));
		for (final Content c : result.getContents()) {
			assertTrue(c.isValid(true));
		}
	}

	@Test
	public void ab_SaveDocumentTest() {
		// Merge 1
		Content content = createContent();

		// Merge complex one at a time
		Document document = (Document) _contents
				.save(new Document(content, TEST_CODE_PREFIX_DOCUMENT + uuid()));
		assertNotNull(document.getId());

		Section section = new Section(content, TEST_CODE_PREFIX_SECTION + uuid());
		document.addSection(section);
		section = (Section) _contents.save(section);
		assertNotNull(section.getId());

		Clause clause = new Clause(content, TEST_CODE_PREFIX_CLAUSE + uuid());
		section.addClause(clause);
		clause = (Clause) _contents.save(clause);
		assertNotNull(clause.getId());

		Paragraph paragraph = new Paragraph(content, TEST_CODE_PREFIX_PARAGRAPH + uuid());
		clause.addParagraph(paragraph);
		paragraph = (Paragraph) _contents.save(paragraph);
		assertNotNull(paragraph.getId());

		// Need to re-get after assembly to delete.
		document = (Document) _contents.findOne(document.getId(), Document.class);
		assertNotNull(document);
		assertTrue(document.isValid(true));

		// Merge complex all at once
		document = createDocument();

		// Merge a list
		Collection<Content> list = new TreeSet<Content>();
		list.add(createContent());
		list.add(createContent());
		list.add(createContent());
		Collection<Content> iter = list;
		Contents contents = new Contents(iter.toArray(new Content[list.size()]));
		contents = _contents.save(contents);
		for (Content c : contents.getContents()) {
			assertNotNull(c.getId());
		}
		assertNull(_contents.save((Content) null));
		assertNull(_contents.save((Content) new Content("", "TEST BODY")));
		assertNull(_contents.save((Content) new Content(TEST_CODE_PREFIX_CONTENT + uuid(), "")));
		assertNull(_contents
				.save((Content) new Content(new Content(""), TEST_CODE_PREFIX_CONTENT + uuid())));
		content = createContent();
		assertNull(_contents.save((Content) new Content(content, content.getContentCd())));
	}

	@Test
	public void ba_FindTest() {
		// Find one
		Content content = createContent();
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
		for (final Content c : contents.getContents()) {
			assertTrue(c.isValid(true));
		}
	}

	@Test
	public void bb_FindDocumentTest() {
		// Get sub-classes, no eagerKids
		Document document = createDocument();
		Long id = document.getId();
		String code = document.getContentCd();
		document = _contents.findOne(id, Document.class);
		assertNotNull(document);
		assertTrue(document.isValid(true));
		assertTrue(Document.class.equals(document.getClass()));
		document = _contents.findByCode(code, Document.class);
		assertNotNull(document);
		assertTrue(document.isValid(true));
		assertTrue(Document.class.equals(document.getClass()));

		try {
			_contents.findOne(id, Document.class).getSections().iterator().next().getId();
			fail();
		} catch (final Exception e) {
			assertTrue(e.getClass().equals(LazyInitializationException.class));
		}
		
		document = _contents.findOne(id, Document.class, true);
		assertNotNull(document);
		assertTrue(document.isValid(true));
		assertTrue(Document.class.equals(document.getClass()));
		assertFalse(document.getSections().isEmpty());
		
		assertTrue(Section.class
				.equals(_contents.findOne(document.getSections().iterator().next().getId()).getClass()));
		assertTrue(Section.class
				.equals(_contents.findByCode(document.getSections().iterator().next().getContentCd()).getClass()));
		try {
			((Section) _contents.findOne(document.getSections().iterator().next().getId())).getClauses()
					.iterator().next();
			fail();
		} catch (final Exception e) {
			assertTrue(e.getClass().equals(LazyInitializationException.class));
		}
		assertTrue(Clause.class.equals(_contents
				.findOne(document.getSections().iterator().next().getClauses().iterator().next().getId(), Clause.class).getClass()));
		assertTrue(Clause.class.equals(_contents
				.findByCode(document.getSections().iterator().next().getClauses().iterator().next().getContentCd())
				.getClass()));
		try {
			((Clause) _contents
					.findOne(document.getSections().iterator().next().getClauses().iterator().next().getId(), Clause.class))
							.getParagraphs().iterator().next();
			fail();
		} catch (final Exception e) {
			assertTrue(e.getClass().equals(LazyInitializationException.class));
		}
		assertTrue(Paragraph.class.equals(_contents.findOne(document.getSections().iterator().next().getClauses()
				.iterator().next().getParagraphs().iterator().next().getId(), Paragraph.class).getClass()));
		assertTrue(Paragraph.class.equals(_contents.findByCode(document.getSections().iterator().next()
				.getClauses().iterator().next().getParagraphs().iterator().next().getContentCd()).getClass()));

		// Get Children by ID
		for (final Section s : _contents.getChildren(id, Sections.class).getSections()) {
			try {
				s.getClauses().iterator().hasNext();
				fail();
			} catch (final Exception e) {
				assertTrue(e.getClass().equals(LazyInitializationException.class));
			}
			for (final Clause c : _contents.getChildren(s.getId(), Clauses.class).getClauses()) {
				try {
					c.getParagraphs().iterator().hasNext();
					fail();
				} catch (final Exception e) {
					assertTrue(e.getClass().equals(LazyInitializationException.class));
				}
				for (final Content p : _contents.getChildren(c.getId(), Paragraphs.class).getParagraphs()) {
					assertTrue(p.isValid(true));
				}
			}
		}

		// Get Children by ID with kids
		for (Section s : _contents.getChildren(document.getId(), Sections.class, true).getSections()) {
			assertTrue(s.isValid(true));
			for (Clause c : _contents.getChildren(s.getId(), Clauses.class, true).getClauses()) {
				assertTrue(c.isValid(true));
				for (Paragraph p : _contents.getChildren(c.getId(), Paragraphs.class, true).getParagraphs()) {
					assertTrue(p.isValid(true));
				}
			}
		}

		// Get Children by code
		for (Section s : _contents.getChildren(document.getContentCd(), Sections.class).getSections()) {
			for (Clause c : _contents.getChildren(s.getContentCd(), Clauses.class).getClauses()) {
				for (Paragraph p : _contents.getChildren(c.getContentCd(), Paragraphs.class).getParagraphs()) {
					assertNotNull(p.getContentCd());
				}
			}
		}

		// Get Children by code with kids
		for (Section s : _contents.getChildren(document.getContentCd(), Sections.class, true).getSections()) {
			assertTrue(s.isValid(true));
			assertTrue(s.getClauses().iterator().hasNext());
			assertNotNull(s.getClauses().iterator().next().getId());
			for (Clause c : _contents.getChildren(s.getContentCd(), Clauses.class, true).getClauses()) {
				assertTrue(c.isValid(true));
				assertTrue(c.getParagraphs().iterator().hasNext());
				assertNotNull(c.getParagraphs().iterator().next().getId());
				for (Paragraph p : _contents.getChildren(c.getContentCd(), Paragraphs.class, true)
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
		
		assertNotNull(_contents.findAll(Documents.class));
	}

	@Test
	public void x_DeleteTest() {
		Content content = createContent();
		_contents.delete(content);
		assertNull(_contents.findOne(content.getId()));
		content = createContent();
		_contents.delete(content.getContentCd());
		assertNull(_contents.findByCode(content.getContentCd()));
		Contents contents = createContents(3);
		_contents.delete(contents);
		for (final Content c : contents.getContents()) {
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
		Collection<Content> contents = new TreeSet<Content>();
		for (int i = 0; i < count; i++) {
			contents.add(createContent());
		}
		return new Contents(contents);
	}
	
	private Content createContent() {
		Content content = makeTestContent();
		content = _contents.save(content);
		assertNotNull(content);
		assertTrue(content.isValid(true));
		return content;
	}
	
	private Document createDocument() {
		Document document = makeTestDocumentComplete();
		document = _contents.save(document);
		assertNotNull(document);
		assertTrue(document.isValid(true));
		return document;
	}
}