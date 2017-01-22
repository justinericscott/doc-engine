package com.itgfirm.docengine.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

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
import com.itgfirm.docengine.util.rest.RestClient;

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
		final ContentJpaImpl content = createContent();
		assertNotNull(content);
		assertTrue(content.isValid());
		
		DocumentJpaImpl document = createDocument();
		assertNotNull(document);
		assertTrue(document.isValid(true));

		SectionJpaImpl section = new SectionJpaImpl(content, TEST_CONTENT_CODE_PREFIX + uuid());
		document.addSection(section);
		section = _contents.save(section, SectionJpaImpl.class);
		assertNotNull(section);

		ClauseJpaImpl clause = new ClauseJpaImpl(content, TEST_CONTENT_CODE_PREFIX + uuid());
		section.addClause(clause);
		clause = _contents.save(clause, ClauseJpaImpl.class);
		assertNotNull(clause);

		ParagraphJpaImpl paragraph = new ParagraphJpaImpl(content, TEST_CONTENT_CODE_PREFIX + uuid());
		clause.addParagraph(paragraph);
		paragraph = _contents.save(paragraph, ParagraphJpaImpl.class);
		assertNotNull(paragraph);

		// Merge complex all at once
		document = createDocument();

		// Merge a list
		final Collection<ContentJpaImpl> list = new ArrayList<ContentJpaImpl>();
		list.add(makeTestContent());
		list.add(makeTestContent());
		list.add(makeTestContent());
		final Contents contents = new Contents(list.toArray(new ContentJpaImpl[list.size()]));
		final Contents saved = _contents.save(contents, Contents.class);
		assertNotNull(saved);
		for (ContentJpaImpl c : saved.getContents()) {
			assertTrue(c.isValid(true));
		}
//		saved.getContents().forEach(c -> {
//			assertTrue(c.isValid(true));
//		});
		assertNull(_contents.save(null, ContentJpaImpl.class));
		assertNull(_contents.save(new ContentJpaImpl("TEST"), ContentJpaImpl.class));
		assertNull(_contents.save(new ContentJpaImpl(TEST_CONTENT_CODE_PREFIX + uuid(), ""), ContentJpaImpl.class));
		assertNull(_contents.save(new ContentJpaImpl(content, content.getContentCd()), ContentJpaImpl.class));
		assertNull(_contents.save(new DocumentJpaImpl(new ContentJpaImpl("TEST", "CODE")), DocumentJpaImpl.class));
		assertNull(_contents.save(new DocumentJpaImpl(new ContentJpaImpl(""), TEST_CONTENT_CODE_PREFIX + uuid()),
				DocumentJpaImpl.class));

	}

	@Test
	public void b_GetTest() {
		// Get All
		final Collection<ContentJpaImpl> list = new ArrayList<ContentJpaImpl>();
		list.add(createContent());
		list.add(createContent());
		list.add(createContent());
		list.add(createContent());
		list.add(createContent());
		Contents contents = _contents.findAll(Contents.class);
		assertNotNull(contents);
		for (ContentJpaImpl c : contents.getContents()) {
			assertTrue(c.isValid(true));
		}
//		contents.getContents().forEach(c -> {
//			assertTrue(c.isValid(true));
//		});

		// Get Content
		final ContentJpaImpl content = createContent();
		Long id = content.getId();
		String code = content.getContentCd();
		assertTrue(((ContentJpaImpl) _contents.findOne(id)).isValid(true));
		assertTrue(((ContentJpaImpl) _contents.findByCode(code)).isValid(true));
		assertNull(_contents.findOne((Long) null));
		assertNull(_contents.findOne(0L));
		assertNull(_contents.findByCode(""));
		assertNull(_contents.findOne(999999999L));
		assertNull(_contents.findByCode((String) null));
		assertNull(_contents.findByCode("Snicklefritz"));
		
		DocumentJpaImpl document = createDocument();
		code = document.getContentCd();
		id = document.getId();
		assertNotNull(_contents.findOne(id, DocumentJpaImpl.class));
		assertNotNull(_contents.findByCode(code, DocumentJpaImpl.class));

		assertNotNull(_contents.findByCode(document.getSections().iterator().next().getContentCd(), SectionJpaImpl.class, true));
		assertNotNull(_contents.findOne(document.getSections().iterator().next().getId(), SectionJpaImpl.class, true));

		assertNotNull(_contents.findOne(document.getSections().iterator().next().getClauses().iterator().next().getId(),
				ClauseJpaImpl.class, true));
		assertNotNull(
				_contents.findByCode(document.getSections().iterator().next().getClauses().iterator().next().getContentCd(),
						ClauseJpaImpl.class, true));

		assertNotNull(_contents.findOne(document.getSections().iterator().next().getClauses().iterator().next()
				.getParagraphs().iterator().next().getId(), ParagraphJpaImpl.class));
		assertNotNull(_contents.findByCodeLike(document.getSections().iterator().next().getClauses().iterator().next()
				.getParagraphs().iterator().next().getContentCd(), Paragraphs.class));

		// Get Children by ID
		for (SectionJpaImpl s : _contents.getChildren(id, Sections.class, true).getSectionsList()) {
			assertTrue(s.isValid(true));
			System.err.println("SECTION ID: " + s.getId());
			for (ClauseJpaImpl c : _contents.getChildren(s.getId(), Clauses.class, true).getClausesList()) {
				assertTrue(c.isValid(true));
				System.err.println("CLAUSE ID: " + c.getId());
				for (ParagraphJpaImpl p : _contents.getChildren(c.getId(), Paragraphs.class, true).getParagraphsList()) {
					System.err.println("PARA ID: " + p.getId());
					assertTrue(p.isValid(true));
				}
			}
		}

		// Get Children by ID with kids
		Sections sections = _contents.getChildren(document.getId(), Sections.class, true);
		for (SectionJpaImpl s : sections.getSectionsList()) {
			assertTrue(s.isValid(true));
			assertNotNull(s.getClauses());
			for (ClauseJpaImpl c : _contents.getChildren(s.getId(), Clauses.class, true).getClausesList()) {
				assertTrue(c.isValid(true));
				assertNotNull(c.getParagraphs());
				for (ParagraphJpaImpl p : _contents.getChildren(c.getId(), Paragraphs.class).getParagraphsList()) {
					assertTrue(p.isValid(true));
				}
			}
		}

		// Get Children by code
		for (SectionJpaImpl s : _contents.getChildren(document.getContentCd(), Sections.class, true).getSectionsList()) {
			assertTrue(s.isValid(true));
			for (ClauseJpaImpl c : _contents.getChildren(s.getContentCd(), Clauses.class, true).getClausesList()) {
				assertTrue(c.isValid(true));
				for (ParagraphJpaImpl p : _contents.getChildren(c.getContentCd(), Paragraphs.class).getParagraphsList()) {
					assertTrue(p.isValid(true));
				}
			}
		}

		// Get Children by code with kids
		for (SectionJpaImpl s : _contents.getChildren(document.getContentCd(), Sections.class, true).getSectionsList()) {
			assertTrue(s.isValid(true));
			assertNotNull(s.getClauses());
			for (ClauseJpaImpl c : _contents.getChildren(s.getContentCd(), Clauses.class, true).getClausesList()) {
				assertTrue(c.isValid(true));
				assertNotNull(c.getParagraphs());
				for (ParagraphJpaImpl p : _contents.getChildren(c.getContentCd(), Paragraphs.class).getParagraphsList()) {
					if (p instanceof ParagraphJpaImpl) {
						assertTrue(p.isValid(true));
					}
				}
			}
		}
		assertNull(_contents.getChildren(999999999L, DocumentJpaImpl.class));
		
		document = new DocumentJpaImpl(content, TEST_CONTENT_CODE_PREFIX + uuid());
		document = _contents.save(document, DocumentJpaImpl.class);
		assertTrue(document.isValid(true));

		SectionJpaImpl section = new SectionJpaImpl(content, TEST_CONTENT_CODE_PREFIX + uuid());
		section.setDocument(document);
		document.addSection(section);
		document = _contents.save(document, DocumentJpaImpl.class);
		section = document.getSections().iterator().next();
		assertTrue(section.isValid(true));

		ClauseJpaImpl clause = new ClauseJpaImpl(content, TEST_CONTENT_CODE_PREFIX + uuid());
		clause.setSection(section);
		section.addClause(clause);
		section = _contents.save(section, SectionJpaImpl.class);
		clause = section.getClauses().iterator().next();
		assertTrue(clause.isValid(true));

		ParagraphJpaImpl paragraph = new ParagraphJpaImpl(content, TEST_CONTENT_CODE_PREFIX + uuid());
		paragraph.setClause(clause);
		clause.addParagraph(paragraph);
		clause = _contents.save(clause, ClauseJpaImpl.class);
		paragraph = clause.getParagraphs().iterator().next();
		assertTrue(paragraph.isValid(true));

		document = _contents.findByCode(code, DocumentJpaImpl.class, true);
		assertTrue(document.isValid(true));
		assertNotNull(document.getSections());
		for (SectionJpaImpl s : document.getSections()) {
			assertTrue(s.isValid(true));
			assertNotNull(s.getClauses());
			for (ClauseJpaImpl c : s.getClauses()) {
				assertTrue(c.isValid(true));
				assertNotNull(c.getParagraphs());
				for (ParagraphJpaImpl p : c.getParagraphs()) {
					assertTrue(p.isValid(true));
				}
			}
		}
		section = _contents.findByCode(document.getSections().iterator().next().getContentCd(), SectionJpaImpl.class, true);
		assertTrue(section.isValid(true));
		assertNotNull(section.getClauses());
		for (ClauseJpaImpl c : section.getClauses()) {
			assertTrue(c.isValid(true));
			assertNotNull(c.getParagraphs());
			for (ParagraphJpaImpl p : c.getParagraphs()) {
				assertTrue(p.isValid(true));
			}
		}
		clause = _contents.findByCode(section.getClauses().iterator().next().getContentCd(), ClauseJpaImpl.class, true);
		assertTrue(clause.isValid(true));
		assertNotNull(clause.getParagraphs());
		for (ParagraphJpaImpl p : clause.getParagraphs()) {
			assertTrue(p.isValid(true));
		}
		paragraph = _contents.findByCode(clause.getParagraphs().iterator().next().getContentCd(), ParagraphJpaImpl.class);
		assertNotNull(paragraph);
		assertTrue(paragraph.isValid(true));
		document = _contents.findByCode(code, DocumentJpaImpl.class, true);
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

	}

	@Test
	public void c_GetByCodeLikeTest() {
		final Collection<ContentJpaImpl> test = makeTestContents(5);
		assertNotNull(test);
		final Contents saved = _contents.save(new Contents(test), Contents.class);
		assertNotNull(saved);
		assertNotNull(saved.getContents());
		final Contents contents = _contents.findByCodeLike("TEST", Contents.class);
		assertNotNull(contents);
		assertNotNull(contents.getContents());
		for (ContentJpaImpl c : contents.getContents()) {
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
		ContentJpaImpl content = createContent();
		Long id = content.getId();
		_contents.delete(content);
		content = null;
		content = (ContentJpaImpl) _contents.findOne(id);
		assertNull(content);
		_contents.delete(content);
		_contents.delete(null);
	}
	
	private ContentJpaImpl createContent() {
		ContentJpaImpl content = makeTestContent();
		content = _contents.save(content, ContentJpaImpl.class);
		assertNotNull(content);
		assertTrue(content.isValid(true));
		return content;
	}

	private DocumentJpaImpl createDocument() {
		DocumentJpaImpl document = makeTestDocumentComplete();
		document = _contents.save(document, DocumentJpaImpl.class);
		assertNotNull(document);
		assertTrue(document.isValid(true));
		return document;
	}
}