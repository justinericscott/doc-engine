package com.github.justinericscott.docengine.models;

import static com.github.justinericscott.docengine.models.AbstractJpaModel.ModelConstants.*;
import static com.github.justinericscott.docengine.models.ToHTMLTest.ToHTMLTestConstants.*;
import static com.github.justinericscott.docengine.util.Utils.collapse;
import static com.github.justinericscott.docengine.util.Utils.create;
import static com.github.justinericscott.docengine.util.Utils.delete;
import static com.github.justinericscott.docengine.util.Utils.get;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;
import static com.github.justinericscott.docengine.util.Utils.read;
import static com.github.justinericscott.docengine.util.Utils.write;
import static com.github.justinericscott.docengine.util.Utils.HTML.*;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.tidy.Tidy;

import com.github.justinericscott.docengine.config.TemplateConfig.TidyFactory;
import com.github.justinericscott.docengine.util.AbstractTest;

/**
 * 
 * @author Justin Scott
 * 
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ToHTMLTest extends AbstractTest {

	@Autowired
	private TidyFactory factory;

	@Test
	public void a_ParagraphToHTMLTest() {
		final Paragraph para001 = new Paragraph("TEST.1.01.para01", "STANDALONE PARAGRAPH: NO SUBS");
		para001.setCss(TEST_VALUE_CSS_PARAGRAPH);
		para001.setOrderBy(1);
		String html = para001.toHTML(true);
		html = tidy(html, "target/models/ParagraphToHTMLTestNoSubs.html");
		assertEquals(TEST_EXP_PARAGRAPH_NO_SUBS, html);
		final StringBuilder sb = new StringBuilder();
		final Collection<Paragraph> paragraphs = createOrderedListOneLevel("TEST.1.01");
		paragraphs.forEach(p -> {
			sb.append(p.toHTML());
		});
		String body = BODY.wrap(sb.toString());
		String title = TITLE.wrap("Test Title - Ordered List: Single Level");
		String head = HEAD.wrap(title.concat(STYLE.style(TEST_VALUE_CSS_PARAGRAPH, HTML_CSS_TYPE_TEXT)));
		html = String.format("%s%s", head, body);
		html = doctype().concat(DOCUMENT.wrap(html, null, namespace()));
		html = tidy(html, "target/models/ParagraphToHTMLTestOrderedListOneLevel.html");
		assertEquals(TEST_EXP_PARAGRAPH_OL_ONE_LEVEL, html);
		sb.setLength(0);
		paragraphs.clear();
		paragraphs.addAll(createOrderedListTwoLevel("TEST.1.01"));
		final Iterator<Paragraph> iter = paragraphs.iterator();
		while (iter.hasNext()) {
			sb.append(getParagraphsHTML(iter));
		}
		body = BODY.wrap(sb.toString());
		title = TITLE.wrap("Test Title - Ordered List: Double Level");
		head = HEAD.wrap(title.concat(STYLE.style(TEST_VALUE_CSS_PARAGRAPH, HTML_CSS_TYPE_TEXT)));
		html = String.format("%s%s", head, body);
		html = doctype().concat(DOCUMENT.wrap(html, null, namespace()));
		html = tidy(html, "target/models/ParagraphToHTMLTestOrderedListTwoLevel.html");
		assertEquals(TEST_EXP_PARAGRAPH_OL_TWO_LEVEL, html);
	}

	@Test
	public void b_ClauseToHTMLTest() {
		final Clause clause = new Clause("TEST.1.01", "STANDALONE CLAUSE: NO PARAGRAPHS");
		clause.setContentNumber("1.01");
		clause.setCss(TEST_VALUE_CSS_CLAUSE);
		clause.setOrderBy(1);
		String html = clause.toHTML(true);
		html = tidy(html, "target/models/ClauseToHTMLTestNoParagraphs.html");
		assertEquals(TEST_EXP_CLAUSE_NO_PARAGRAPHS, html);
		clause.setBody("CLAUSE: TWO PARAGRAPHS");
		createTwoParagraphs(clause);
		html = clause.toHTML(true);
		html = tidy(html, "target/models/ClauseToHTMLTestTwoParagraphs.html");
		assertEquals(TEST_EXP_CLAUSE_TWO_PARAGRAPHS, html);
		clause.getParagraphs().clear();
		clause.setBody("CLAUSE: ORDERED LIST: SINGLE LEVEL");
		createOrderedListOneLevel(clause);
		html = clause.toHTML(true);
		html = tidy(html, "target/models/ClauseToHTMLTestOrderedListOneLevel.html");
		assertEquals(TEST_EXP_CLAUSE_OL_ONE_LEVEL, html);
		clause.getParagraphs().clear();
		clause.setBody("CLAUSE: ORDERED LIST: DOUBLE LEVEL");
		createOrderedListTwoLevel(clause);
		html = clause.toHTML(true);
		html = tidy(html, "target/models/ClauseToHTMLTestOrderedListTwoLevel.html");
		assertEquals(TEST_EXP_CLAUSE_OL_TWO_LEVEL, html);
	}

	@Test
	public void c_SectionToHTMLTest() {
		final Section section = new Section("TEST.1", "STANDALONE SECTION: NO CLAUSES");
		section.setContentNumber("1.");
		section.setCss(TEST_VALUE_CSS_SECTION);
		section.setOrderBy(1);
		String html = section.toHTML(true);
		html = tidy(html, "target/models/SectionToHTMLTestNoClauses.html");
		assertEquals(TEST_EXP_SECTION_NO_CLAUSES, html);
		section.setBody("SECTION: TWO CLAUSES");
		final Clause clause1 = new Clause("TEST.1.01", "STANDALONE CLAUSE ONE: NO PARAGRAPHS");
		clause1.setContentNumber("1.01");
		clause1.setCss(TEST_VALUE_CSS_CLAUSE);
		clause1.setOrderBy(1);
		section.addClause(clause1);
		final Clause clause2 = new Clause("TEST.1.02", "STANDALONE CLAUSE TWO: NO PARAGRAPHS");
		clause2.setContentNumber("1.02");
		clause2.setCss(TEST_VALUE_CSS_CLAUSE);
		clause2.setOrderBy(2);
		section.addClause(clause2);
		html = section.toHTML(true);
		html = tidy(html, "target/models/SectionToHTMLTestTwoClausesNoParagraphs.html");
		assertEquals(TEST_EXP_SECTION_TWO_CLAUSES_NO_PARAGRAPHS, html);
		section.setBody("SECTION: TWO CLAUSES: TWO PARAGRAPHS");
		clause1.setBody("CLAUSE ONE: TWO PARAGRAPHS");
		createTwoParagraphs(clause1);
		clause2.setBody("CLAUSE TWO: TWO PARAGRAPHS");
		createTwoParagraphs(clause2);
		html = section.toHTML(true);
		html = tidy(html, "target/models/SectionToHTMLTestTwoClausesTwoParagraphsEach.html");
		assertEquals(TEST_EXP_SECTION_TWO_CLAUSES_TWO_PARAGRAHS_EACH, html);
		clause1.getParagraphs().clear();
		section.setBody("SECTION: TWO CLAUSES: ORDERED LIST: SINGLE LEVEL");
		clause1.setBody("CLAUSE ONE: ORDERED LIST: SINGLE LEVEL");
		createOrderedListOneLevel(clause1);
		clause2.getParagraphs().clear();
		clause2.setBody("CLAUSE TWO: ORDERED LIST: SINGLE LEVEL");
		createOrderedListOneLevel(clause2);
		html = section.toHTML(true);
		html = tidy(html, "target/models/SectionToHTMLTestOrderedListSingleLevel.html");
		assertEquals(TEST_EXP_SECTION_OL_ONE_LEVEL, html);
		clause1.getParagraphs().clear();
		section.setBody("SECTION: TWO CLAUSES: ORDERED LIST: DOUBLE LEVEL");
		clause1.setBody("CLAUSE ONE: ORDERED LIST: DOUBLE LEVEL");
		createOrderedListTwoLevel(clause1);
		clause2.getParagraphs().clear();
		clause2.setBody("CLAUSE TWO: ORDERED LIST: DOUBLE LEVEL");
		createOrderedListTwoLevel(clause2);
		html = section.toHTML(true);
		html = tidy(html, "target/models/SectionToHTMLTestOrderedListDoubleLevel.html");
		assertEquals(TEST_EXP_SECTION_OL_TWO_LEVEL, html);
	}

	public String tidy(final String xhtml, final String path) {
		if (isNotNullOrEmpty(xhtml)) {
			final Tidy tidy = factory.getTidy();
			try (final InputStream input = new ByteArrayInputStream(xhtml.getBytes())) {
				if (path != null && path.endsWith(".html")) {
					final File html = create(path);
					final String errpath = path.substring(0, path.length() - 5).concat("_TIDY.txt");
					final String prepath = path.substring(0, path.length() - 5).concat("_PRE-TIDY.html");
					final File errors = create(errpath);
					final File pretidy = create(prepath);
					write(pretidy, xhtml);
					try (final PrintWriter output = new PrintWriter(html)) {
						try (final PrintWriter errout = new PrintWriter(errors)) {
							tidy.setErrout(errout);
							tidy.parse(input, output);
							output.flush();
							errout.flush();
						}
					}
					final String h = collapse(read(html));
					final String e = read(errors);
					if (isNotNullOrEmpty(e)) {
						return h + "\n" + e;
					} else {
						delete(pretidy);
						delete(errors);
						return h;
					}
				} else {
					try (final StringWriter html = new StringWriter()) {
						try (final PrintWriter output = new PrintWriter(html)) {
							tidy.setQuiet(false);
							tidy.parse(input, output);
							output.flush();
							return collapse(html.toString());
						}
					}
				}
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private void createTwoParagraphs(final Clause clause) {
		if (isNotNullOrEmpty(clause)) {
			final String code = clause.getContentCd();
			final Paragraph paragraph1 = new Paragraph(code + ".para01", "STANDALONE PARAGRAPH ONE: NO SUBS");
			paragraph1.setFlags(HTML_STYLE_FLAG_FIRST_IN_CLAUSE);
			paragraph1.setCss(TEST_VALUE_CSS_PARAGRAPH);
			paragraph1.setOrderBy(1);
			clause.addParagraph(paragraph1);
			final Paragraph paragraph2 = new Paragraph(code + ".para02", "STANDALONE PARAGRAPH TWO: NO SUBS");
			paragraph2.setCss(TEST_VALUE_CSS_PARAGRAPH);
			paragraph2.setOrderBy(2);
			clause.addParagraph(paragraph2);
		}
	}

	private void createOrderedListOneLevel(final Clause clause) {
		if (isNotNullOrEmpty(clause)) {
			for (final Paragraph p : createOrderedListOneLevel(clause.getContentCd())) {
				clause.addParagraph(p);
			}
		}
	}

	private Collection<Paragraph> createOrderedListOneLevel(final String code) {
		if (isNotNullOrEmpty(code)) {
			final Collection<Paragraph> list = new LinkedList<Paragraph>();
			final Paragraph list001 = new Paragraph(code + ".list001", "ORDERED LIST: SINGLE LEVEL - FIRST ITEM");
			list001.setFlags(HTML_STYLE_FLAG_FIRST_IN_CLAUSE + HTML_STYLE_FLAG_FIRST + HTML_STYLE_FLAG_SUB);
			list001.setCss(TEST_VALUE_CSS_PARAGRAPH);
			list001.setOrderBy(1);
			list.add(list001);
			final Paragraph list002 = new Paragraph(code + ".list002", "ORDERED LIST: SINGLE LEVEL - SECOND ITEM");
			list002.setFlags(HTML_STYLE_FLAG_SUB);
			list002.setCss(TEST_VALUE_CSS_PARAGRAPH);
			list002.setOrderBy(2);
			list.add(list002);
			final Paragraph list003 = new Paragraph(code + ".list003", "ORDERED LIST: SINGLE LEVEL - THIRD ITEM");
			list003.setFlags(HTML_STYLE_FLAG_LAST + HTML_STYLE_FLAG_SUB);
			list003.setCss(TEST_VALUE_CSS_PARAGRAPH);
			list003.setOrderBy(3);
			list.add(list003);
			return list;
		}
		return null;
	}
	
	private void createOrderedListTwoLevel(final Clause clause) {
		if (isNotNullOrEmpty(clause)) {
			for (final Paragraph p : createOrderedListTwoLevel(clause.getContentCd())) {
				clause.addParagraph(p);
			}
		}
	}

	private Collection<Paragraph> createOrderedListTwoLevel(final String code) {
		if (isNotNullOrEmpty(code)) {
			final Collection<Paragraph> list = new LinkedList<Paragraph>();
			final Paragraph list001 = new Paragraph(code + ".list001", "ORDERED LIST: TWO LEVEL - FIRST LEVEL: FIRST ITEM");
			list001.setFlags(HTML_STYLE_FLAG_FIRST_IN_CLAUSE + HTML_STYLE_FLAG_FIRST + HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_PARENT);
			list001.setCss(TEST_VALUE_CSS_PARAGRAPH);
			list001.setOrderBy(1);
			list.add(list001);
			final Paragraph list002 = new Paragraph(code + ".list002", "ORDERED LIST: TWO LEVEL - SECOND LEVEL: FIRST ITEM");
			list002.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_FIRST);
			list002.setCss(TEST_VALUE_CSS_PARAGRAPH);
			list002.setOrderBy(2);
			list.add(list002);
			final Paragraph list003 = new Paragraph(code + ".list003", "ORDERED LIST: TWO LEVEL - SECOND LEVEL: SECOND ITEM");
			list003.setFlags(HTML_STYLE_FLAG_SUB);
			list003.setCss(TEST_VALUE_CSS_PARAGRAPH);
			list003.setOrderBy(3);
			list.add(list003);
			final Paragraph list004 = new Paragraph(code + ".list004", "ORDERED LIST: TWO LEVEL - SECOND LEVEL: THIRD ITEM");
			list004.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_LAST);
			list004.setCss(TEST_VALUE_CSS_PARAGRAPH);
			list004.setOrderBy(4);
			list.add(list004);
			
			final Paragraph list005 = new Paragraph(code + ".list005", "ORDERED LIST: TWO LEVEL - FIRST LEVEL: SECOND ITEM");
			list005.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_PARENT);
			list005.setCss(TEST_VALUE_CSS_PARAGRAPH);
			list005.setOrderBy(5);
			list.add(list005);
			final Paragraph list006 = new Paragraph(code + ".list006", "ORDERED LIST: TWO LEVEL - SECOND LEVEL: FIRST ITEM");
			list006.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_FIRST);
			list006.setCss(TEST_VALUE_CSS_PARAGRAPH);
			list006.setOrderBy(6);
			list.add(list006);
			final Paragraph list007= new Paragraph(code + ".list007", "ORDERED LIST: TWO LEVEL - SECOND LEVEL: SECOND ITEM");
			list007.setFlags(HTML_STYLE_FLAG_SUB);
			list007.setCss(TEST_VALUE_CSS_PARAGRAPH);
			list007.setOrderBy(7);
			list.add(list007);
			final Paragraph list008 = new Paragraph(code + ".list008", "ORDERED LIST: TWO LEVEL - SECOND LEVEL: THIRD ITEM");
			list008.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_LAST);
			list008.setCss(TEST_VALUE_CSS_PARAGRAPH);
			list008.setOrderBy(8);
			list.add(list008);
			
			final Paragraph list009 = new Paragraph(code + ".list009", "ORDERED LIST: TWO LEVEL - FIRST LEVEL: THIRD ITEM");
			list009.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_LAST + HTML_STYLE_FLAG_PARENT);
			list009.setCss(TEST_VALUE_CSS_PARAGRAPH);
			list009.setOrderBy(9);
			list.add(list009);
			final Paragraph list010 = new Paragraph(code + ".list010", "ORDERED LIST: TWO LEVEL - SECOND LEVEL: FIRST ITEM");
			list010.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_FIRST);
			list010.setCss(TEST_VALUE_CSS_PARAGRAPH);
			list010.setOrderBy(10);
			list.add(list010);
			final Paragraph list011 = new Paragraph(code + ".list011", "ORDERED LIST: TWO LEVEL - SECOND LEVEL: SECOND ITEM");
			list011.setFlags(HTML_STYLE_FLAG_SUB);
			list011.setCss(TEST_VALUE_CSS_PARAGRAPH);
			list011.setOrderBy(11);
			list.add(list011);
			final Paragraph list012 = new Paragraph(code + ".list012", "ORDERED LIST: TWO LEVEL - SECOND LEVEL: THIRD ITEM");
			list012.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_LAST);
			list012.setCss(TEST_VALUE_CSS_PARAGRAPH);
			list012.setOrderBy(12);
			list.add(list012);
			return list;
		}	
		return null;
	}

	private String getParagraphsHTML(final Iterator<Paragraph> iter) {
		StringBuilder sb = new StringBuilder();
		Paragraph paragraph = null;
		while (iter.hasNext()) {
			paragraph = iter.next();
			final String options = paragraph.getFlags();
			boolean isOption = false; 
			boolean isParent = false;
			boolean isLast = false;
			if (isNotNullOrEmpty(options)) {
				isOption = options.contains(HTML_STYLE_FLAG_OPTIONAL);
				isParent = options.contains(HTML_STYLE_FLAG_PARENT);
				isLast = options.contains(HTML_STYLE_FLAG_LAST);
			}
			if (isParent && (!isOption || (isOption && true))) {
				sb.append(paragraph.toHTML(getParagraphsHTML(iter)));
			} else if (isLast && (!isOption || (isOption && true))) {
				sb.append(paragraph.toHTML());
				return sb.toString();
			} else {
				sb.append(paragraph.toHTML());
			}
		}
		return sb.toString();
	}
	
	static class ToHTMLTestConstants {
		static final String TEST_VALUE_CSS_SECTION = read(get("css/section.css"));
		static final String TEST_VALUE_CSS_CLAUSE = read(get("css/clause.css"));
		static final String TEST_VALUE_CSS_PARAGRAPH = read(get("css/paragraph.css"));
		static final String TEST_EXP_SECTION_NO_CLAUSES = read(get("models/SectionExpectedOutput_NoClauses.html"));
		static final String TEST_EXP_SECTION_TWO_CLAUSES_NO_PARAGRAPHS = read(
				get("models/SectionExpectedOutput_TwoClausesNoParagraphs.html"));
		static final String TEST_EXP_SECTION_TWO_CLAUSES_TWO_PARAGRAHS_EACH = read(
				get("models/SectionExpectedOutput_TwoClausesTwoParagraphsEach.html"));
		static final String TEST_EXP_SECTION_OL_ONE_LEVEL = read(
				get("models/SectionExpectedOutput_OrderedList_OneLevel.html"));
		static final String TEST_EXP_SECTION_OL_TWO_LEVEL = read(
				get("models/SectionExpectedOutput_OrderedList_TwoLevel.html"));
		static final String TEST_EXP_CLAUSE_NO_PARAGRAPHS = read(get("models/ClauseExpectedOutput_NoParagraphs.html"));
		static final String TEST_EXP_CLAUSE_TWO_PARAGRAPHS = read(
				get("models/ClauseExpectedOutput_TwoParagraphs.html"));
		static final String TEST_EXP_CLAUSE_OL_ONE_LEVEL = read(
				get("models/ClauseExpectedOutput_OrderedList_OneLevel.html"));
		static final String TEST_EXP_CLAUSE_OL_TWO_LEVEL = read(
				get("models/ClauseExpectedOutput_OrderedList_TwoLevel.html"));
		static final String TEST_EXP_PARAGRAPH_NO_SUBS = read(get("models/ParagraphExpectedOutput_NoSubs.html"));
		static final String TEST_EXP_PARAGRAPH_OL_ONE_LEVEL = read(
				get("models/ParagraphExpectedOutput_OrderedList_OneLevel.html"));
		static final String TEST_EXP_PARAGRAPH_OL_TWO_LEVEL = read(
				get("models/ParagraphExpectedOutput_OrderedList_TwoLevel.html"));
	}
}