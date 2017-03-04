package com.github.justinericscott.docengine.controller;

import static org.junit.Assert.*;

import static com.github.justinericscott.docengine.util.TestUtils.TestConstants.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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
import com.github.justinericscott.docengine.util.AbstractTest;
import com.github.justinericscott.docengine.util.rest.RestClient;

/**
 * @author Justin Scott
 * 
 *         Tests for the Content REST Controller
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContentRestControllerTest extends AbstractTest {
	
	@Autowired
	@Qualifier(RestClient.AUTOWIRE_QUALIFIER_CONTENT)
	RestClient _contents;

	@Test
	public void a_SaveTest() {
		// Merge 1
		final Content content = createContent();
		assertNotNull(content);
		assertTrue(content.isValid());
		
		Document document = createDocument();
		assertNotNull(document);
		assertTrue(document.isValid(true));

		Section section = new Section(content, TEST_CODE_PREFIX_CONTENT + uuid());
		document.addSection(section);
		section = _contents.save(section, Section.class);
		assertNotNull(section);

		Clause clause = new Clause(content, TEST_CODE_PREFIX_CONTENT + uuid());
		section.addClause(clause);
		clause = _contents.save(clause, Clause.class);
		assertNotNull(clause);

		Paragraph paragraph = new Paragraph(content, TEST_CODE_PREFIX_CONTENT + uuid());
		clause.addParagraph(paragraph);
		paragraph = _contents.save(paragraph, Paragraph.class);
		assertNotNull(paragraph);

		// Merge complex all at once
		document = createDocument();

		// Merge a list
		final Collection<Content> list = new ArrayList<Content>();
		list.add(makeTestContent());
		list.add(makeTestContent());
		list.add(makeTestContent());
		final Contents contents = new Contents(list.toArray(new Content[list.size()]));
		final Contents saved = _contents.save(contents, Contents.class);
		assertNotNull(saved);
		for (Content c : saved.getContents()) {
			assertTrue(c.isValid(true));
		}
//		saved.getContents().forEach(c -> {
//			assertTrue(c.isValid(true));
//		});
		assertNull(_contents.save(null, Content.class));
		assertNull(_contents.save(new Content("TEST"), Content.class));
		assertNull(_contents.save(new Content(TEST_CODE_PREFIX_CONTENT + uuid(), ""), Content.class));
		assertNull(_contents.save(new Content(content, content.getContentCd()), Content.class));
		assertNull(_contents.save(new Document(new Content("TEST", "CODE")), Document.class));
		assertNull(_contents.save(new Document(new Content(""), TEST_CODE_PREFIX_CONTENT + uuid()),
				Document.class));

	}

	@Test
	public void b_GetTest() {
		// Get All
		final Collection<Content> list = new ArrayList<Content>();
		list.add(createContent());
		list.add(createContent());
		list.add(createContent());
		list.add(createContent());
		list.add(createContent());
		Contents contents = _contents.findAll(Contents.class);
		assertNotNull(contents);
		for (Content c : contents.getContents()) {
			assertTrue(c.isValid(true));
		}
//		contents.getContents().forEach(c -> {
//			assertTrue(c.isValid(true));
//		});

		// Get Content
		final Content content = createContent();
		Long id = content.getId();
		String code = content.getContentCd();
		assertTrue(((Content) _contents.findOne(id)).isValid(true));
		assertTrue(((Content) _contents.findByCode(code)).isValid(true));
		assertNull(_contents.findOne((Long) null));
		assertNull(_contents.findOne(0L));
		assertNull(_contents.findByCode(""));
		assertNull(_contents.findOne(999999999L));
		assertNull(_contents.findByCode((String) null));
		assertNull(_contents.findByCode("Snicklefritz"));
		
		Document document = createDocument();
		code = document.getContentCd();
		id = document.getId();
		assertNotNull(_contents.findOne(id, Document.class));
		assertNotNull(_contents.findByCode(code, Document.class));

		assertNotNull(_contents.findByCode(document.getSections().iterator().next().getContentCd(), Section.class, true));
		assertNotNull(_contents.findOne(document.getSections().iterator().next().getId(), Section.class, true));

		assertNotNull(_contents.findOne(document.getSections().iterator().next().getClauses().iterator().next().getId(),
				Clause.class, true));
		assertNotNull(
				_contents.findByCode(document.getSections().iterator().next().getClauses().iterator().next().getContentCd(),
						Clause.class, true));

		assertNotNull(_contents.findOne(document.getSections().iterator().next().getClauses().iterator().next()
				.getParagraphs().iterator().next().getId(), Paragraph.class));
		assertNotNull(_contents.findByCodeLike(document.getSections().iterator().next().getClauses().iterator().next()
				.getParagraphs().iterator().next().getContentCd(), Paragraphs.class));

		// Get Children by ID
		for (Section s : _contents.getChildren(id, Sections.class, true).getSections()) {
			assertTrue(s.isValid(true));
			for (Clause c : _contents.getChildren(s.getId(), Clauses.class, true).getClauses()) {
				assertTrue(c.isValid(true));
				for (Paragraph p : _contents.getChildren(c.getId(), Paragraphs.class, true).getParagraphs()) {
					assertTrue(p.isValid(true));
				}
			}
		}

		// Get Children by ID with kids
		Sections sections = _contents.getChildren(document.getId(), Sections.class, true);
		for (Section s : sections.getSections()) {
			assertTrue(s.isValid(true));
			assertNotNull(s.getClauses());
			for (Clause c : _contents.getChildren(s.getId(), Clauses.class, true).getClauses()) {
				assertTrue(c.isValid(true));
				assertNotNull(c.getParagraphs());
				for (Paragraph p : _contents.getChildren(c.getId(), Paragraphs.class).getParagraphs()) {
					assertTrue(p.isValid(true));
				}
			}
		}

		// Get Children by code
		for (Section s : _contents.getChildren(document.getContentCd(), Sections.class, true).getSections()) {
			assertTrue(s.isValid(true));
			for (Clause c : _contents.getChildren(s.getContentCd(), Clauses.class, true).getClauses()) {
				assertTrue(c.isValid(true));
				for (Paragraph p : _contents.getChildren(c.getContentCd(), Paragraphs.class).getParagraphs()) {
					assertTrue(p.isValid(true));
				}
			}
		}

		// Get Children by code with kids
		for (Section s : _contents.getChildren(document.getContentCd(), Sections.class, true).getSections()) {
			assertTrue(s.isValid(true));
			assertNotNull(s.getClauses());
			for (Clause c : _contents.getChildren(s.getContentCd(), Clauses.class, true).getClauses()) {
				assertTrue(c.isValid(true));
				assertNotNull(c.getParagraphs());
				for (Paragraph p : _contents.getChildren(c.getContentCd(), Paragraphs.class).getParagraphs()) {
					if (p instanceof Paragraph) {
						assertTrue(p.isValid(true));
					}
				}
			}
		}
		assertNull(_contents.getChildren(999999999L, Document.class));
		
		document = new Document(content, TEST_CODE_PREFIX_CONTENT + uuid());
		document = _contents.save(document, Document.class);
		assertTrue(document.isValid(true));

		Section section = new Section(content, TEST_CODE_PREFIX_CONTENT + uuid());
		section.setDocument(document);
		document.addSection(section);
		document = _contents.save(document, Document.class);
		section = document.getSections().iterator().next();
		assertTrue(section.isValid(true));

		Clause clause = new Clause(content, TEST_CODE_PREFIX_CONTENT + uuid());
		clause.setSection(section);
		section.addClause(clause);
		section = _contents.save(section, Section.class);
		clause = section.getClauses().iterator().next();
		assertTrue(clause.isValid(true));

		Paragraph paragraph = new Paragraph(content, TEST_CODE_PREFIX_CONTENT + uuid());
		paragraph.setClause(clause);
		clause.addParagraph(paragraph);
		clause = _contents.save(clause, Clause.class);
		paragraph = clause.getParagraphs().iterator().next();
		assertTrue(paragraph.isValid(true));

		document = _contents.findByCode(code, Document.class, true);
		assertTrue(document.isValid(true));
		assertNotNull(document.getSections());
		for (Section s : document.getSections()) {
			assertTrue(s.isValid(true));
			assertNotNull(s.getClauses());
			for (Clause c : s.getClauses()) {
				assertTrue(c.isValid(true));
				assertNotNull(c.getParagraphs());
				for (Paragraph p : c.getParagraphs()) {
					assertTrue(p.isValid(true));
				}
			}
		}
		section = _contents.findByCode(document.getSections().iterator().next().getContentCd(), Section.class, true);
		assertTrue(section.isValid(true));
		assertNotNull(section.getClauses());
		for (Clause c : section.getClauses()) {
			assertTrue(c.isValid(true));
			assertNotNull(c.getParagraphs());
			for (Paragraph p : c.getParagraphs()) {
				assertTrue(p.isValid(true));
			}
		}
		clause = _contents.findByCode(section.getClauses().iterator().next().getContentCd(), Clause.class, true);
		assertTrue(clause.isValid(true));
		assertNotNull(clause.getParagraphs());
		for (Paragraph p : clause.getParagraphs()) {
			assertTrue(p.isValid(true));
		}
		paragraph = _contents.findByCode(clause.getParagraphs().iterator().next().getContentCd(), Paragraph.class);
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));
		document = _contents.findByCode(code, Document.class, true);
		assertTrue(document.isValid(true));
		assertNotNull(document.getSections());
		document.getSections().forEach(s -> {
			assertTrue(s.isValid(true));
			assertNotNull(s.getClauses());
			s.getClauses().forEach(c -> {
				assertTrue(c.isValid(true));
				assertNotNull(c.getParagraphs());
				c.getParagraphs().forEach(p -> {
					assertTrue(p.isValid(true));
				});
			});
		}); 
		Documents doc = _contents.findAll(Documents.class);
		assertNotNull(doc);
		assertNotNull(doc.getDocuments());
	}

	@Test
	public void c_GetByCodeLikeTest() {
		final Collection<Content> test = makeTestContents(5);
		assertNotNull(test);
		final Contents saved = _contents.save(new Contents(test), Contents.class);
		assertNotNull(saved);
		assertNotNull(saved.getContents());
		final Contents contents = _contents.findByCodeLike("TEST", Contents.class);
		assertNotNull(contents);
		assertNotNull(contents.getContents());
		for (Content c : contents.getContents()) {
			assertTrue(c.isValid(true));
		}
//		contents.getContents().forEach(c -> {
//			assertTrue(c.getId() > 0L);
//		});
		assertNull(_contents.findByCodeLike("Snicklefritz"));
		assertNull(_contents.findByCodeLike(""));
		assertNull(_contents.findByCodeLike((String) null));
	}

	@Test
	public void xx_DeleteTest() {
		Content content = createContent();
		Long id = content.getId();
		_contents.delete(content);
		content = null;
		content = (Content) _contents.findOne(id);
		assertNull(content);
		_contents.delete(content);
		_contents.delete(null);
	}
	
	private Content createContent() {
		Content content = makeTestContent();
		content = _contents.save(content, Content.class);
		assertNotNull(content);
		assertTrue(content.isValid(true));
		return content;
	}

	private Document createDocument() {
		Document document = makeTestDocumentComplete();
		document = _contents.save(document, Document.class);
		assertNotNull(document);
		assertTrue(document.isValid(true));
		return document;
	}
}