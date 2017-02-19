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
import static org.junit.Assert.*;

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

	private static final boolean ASSERT = true;

	@Autowired
	private TidyFactory factory;

	@Test
	public void a_ParagraphToHTMLTest() {
		final Paragraph paragraph = new Paragraph("TEST.1.01.para01", "STANDALONE PARAGRAPH: NO SUBS");
		paragraph.setCss(TEST_VALUE_CSS_PARAGRAPH);
		paragraph.setOrderBy(1);
		String html = paragraph.toHTML(true);
		html = tidy(html, "target/models/ParagraphToHTMLTestNoSubs.html");
		if (ASSERT) {
			assertEquals(TEST_EXP_PARAGRAPH_NO_SUBS, html);
		}
		final StringBuilder sb = new StringBuilder();
		final Collection<Paragraph> paragraphs = createOrderedListVariableLevel("TEST.1.01");
		paragraphs.forEach(p -> {
			sb.append(p.toHTML());
		});
		String body = BODY.wrap(sb.toString());
		String title = TITLE.wrap("Test Title - Ordered List: Single Level");
		String head = HEAD.wrap(title.concat(STYLE.style(TEST_VALUE_CSS_PARAGRAPH, HTML_CSS_TYPE_TEXT)));
		html = String.format("%s%s", head, body);
		html = doctype().concat(DOCUMENT.wrap(html, null, namespace()));
		html = tidy(html, "target/models/ParagraphToHTMLTestOrderedListOneLevel.html");
		if (ASSERT) {
			assertEquals(TEST_EXP_PARAGRAPH_OL_ONE_LEVEL, html);
		}
		sb.setLength(0);
		paragraphs.clear();
		paragraphs.addAll(createOrderedListVariableLevel("TEST.1.01", 2));
		Iterator<Paragraph> iter = paragraphs.iterator();
		while (iter.hasNext()) {
			sb.append(Paragraph.getParagraphsHTML(null, iter));
		}
		body = BODY.wrap(sb.toString());
		title = TITLE.wrap("Test Title - Ordered List: Double Level");
		head = HEAD.wrap(title.concat(STYLE.style(TEST_VALUE_CSS_PARAGRAPH, HTML_CSS_TYPE_TEXT)));
		html = String.format("%s%s", head, body);
		html = doctype().concat(DOCUMENT.wrap(html, null, namespace()));
		html = tidy(html, "target/models/ParagraphToHTMLTestOrderedListTwoLevel.html");
		if (ASSERT) {
			assertEquals(TEST_EXP_PARAGRAPH_OL_TWO_LEVEL, html);
		}
		sb.setLength(0);
		paragraphs.clear();
		paragraphs.addAll(createOrderedListVariableLevel("TEST.1.01", 5));
		iter = paragraphs.iterator();
		while (iter.hasNext()) {
			sb.append(Paragraph.getParagraphsHTML(null, iter));
		}
		body = BODY.wrap(sb.toString());
		title = TITLE.wrap("Test Title - Ordered List: Variable Levels");
		head = HEAD.wrap(title.concat(STYLE.style(TEST_VALUE_CSS_PARAGRAPH, HTML_CSS_TYPE_TEXT)));
		html = String.format("%s%s", head, body);
		html = doctype().concat(DOCUMENT.wrap(html, null, namespace()));
		html = tidy(html, "target/models/ParagraphToHTMLTestOrderedListVariableLevels.html");
		if (ASSERT) {
			assertEquals(TEST_EXP_PARAGRAPH_OL_VARY_LEVELS, html);
		}
	}

	@Test
	public void b_ClauseToHTMLTest() {
		final Clause clause = new Clause("TEST.1.01", "STANDALONE CLAUSE: NO PARAGRAPHS");
		clause.setContentNumber("1.01");
		clause.setCss(TEST_VALUE_CSS_CLAUSE);
		clause.setOrderBy(1);
		String html = clause.toHTML(true);
		html = tidy(html, "target/models/ClauseToHTMLTestNoParagraphs.html");
		if (ASSERT) {
			assertEquals(TEST_EXP_CLAUSE_NO_PARAGRAPHS, html);
		}
		clause.setBody("CLAUSE: TWO PARAGRAPHS");
		createTwoParagraphs(clause);
		html = clause.toHTML(true);
		html = tidy(html, "target/models/ClauseToHTMLTestTwoParagraphs.html");
		if (ASSERT) {
			assertEquals(TEST_EXP_CLAUSE_TWO_PARAGRAPHS, html);
		}
		clause.getParagraphs().clear();
		clause.setBody("CLAUSE: ORDERED LIST: SINGLE LEVEL");
		createOrderedListVariableLevel(clause);
		html = clause.toHTML(true);
		html = tidy(html, "target/models/ClauseToHTMLTestOrderedListOneLevel.html");
		if (ASSERT) {
			assertEquals(TEST_EXP_CLAUSE_OL_ONE_LEVEL, html);
		}
		clause.getParagraphs().clear();
		clause.setBody("CLAUSE: ORDERED LIST: DOUBLE LEVEL");
		createOrderedListVariableLevel(clause, 2);
		html = clause.toHTML(true);
		html = tidy(html, "target/models/ClauseToHTMLTestOrderedListTwoLevel.html");
		if (ASSERT) {
			assertEquals(TEST_EXP_CLAUSE_OL_TWO_LEVEL, html);
		}
		clause.getParagraphs().clear();
		clause.setBody("CLAUSE: ORDERED LIST: VARIABLE LEVELS");
		createOrderedListVariableLevel(clause, 5);
		html = clause.toHTML(true);
		html = tidy(html, "target/models/ClauseToHTMLTestOrderedListVariableLevels.html");
		if (ASSERT) {
			assertEquals(TEST_EXP_CLAUSE_OL_VARY_LEVELS, html);
		}
	}

	@Test
	public void c_SectionToHTMLTest() {
		final Section section = new Section("TEST.1", "STANDALONE SECTION: NO CLAUSES");
		section.setContentNumber("1.");
		section.setCss(TEST_VALUE_CSS_SECTION);
		section.setOrderBy(1);
		String html = section.toHTML(true);
		html = tidy(html, "target/models/SectionToHTMLTestNoClauses.html");
		if (ASSERT) {
			assertEquals(TEST_EXP_SECTION_NO_CLAUSES, html);
		}
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
		if (ASSERT) {
			assertEquals(TEST_EXP_SECTION_TWO_CLAUSES_NO_PARAGRAPHS, html);
		}
		section.setBody("SECTION: TWO CLAUSES: TWO PARAGRAPHS");
		clause1.setBody("CLAUSE ONE: TWO PARAGRAPHS");
		createTwoParagraphs(clause1);
		clause2.setBody("CLAUSE TWO: TWO PARAGRAPHS");
		createTwoParagraphs(clause2);
		html = section.toHTML(true);
		html = tidy(html, "target/models/SectionToHTMLTestTwoClausesTwoParagraphsEach.html");
		if (ASSERT) {
			assertEquals(TEST_EXP_SECTION_TWO_CLAUSES_TWO_PARAGRAHS_EACH, html);
		}
		clause1.getParagraphs().clear();
		section.setBody("SECTION: TWO CLAUSES: ORDERED LIST: SINGLE LEVEL");
		clause1.setBody("CLAUSE ONE: ORDERED LIST: SINGLE LEVEL");
		createOrderedListVariableLevel(clause1);
		clause2.getParagraphs().clear();
		clause2.setBody("CLAUSE TWO: ORDERED LIST: SINGLE LEVEL");
		createOrderedListVariableLevel(clause2);
		html = section.toHTML(true);
		html = tidy(html, "target/models/SectionToHTMLTestOrderedListSingleLevel.html");
		if (ASSERT) {
			assertEquals(TEST_EXP_SECTION_OL_ONE_LEVEL, html);
		}
		clause1.getParagraphs().clear();
		section.setBody("SECTION: TWO CLAUSES: ORDERED LIST: DOUBLE LEVEL");
		clause1.setBody("CLAUSE ONE: ORDERED LIST: DOUBLE LEVEL");
		createOrderedListVariableLevel(clause1, 2);
		clause2.getParagraphs().clear();
		clause2.setBody("CLAUSE TWO: ORDERED LIST: DOUBLE LEVEL");
		createOrderedListVariableLevel(clause2, 2);
		html = section.toHTML(true);
		html = tidy(html, "target/models/SectionToHTMLTestOrderedListDoubleLevel.html");
		if (ASSERT) {
			assertEquals(TEST_EXP_SECTION_OL_TWO_LEVEL, html);
		}
		clause1.getParagraphs().clear();
		section.setBody("SECTION: TWO CLAUSES: ORDERED LIST: VARIABLE LEVELS");
		clause1.setBody("CLAUSE ONE: ORDERED LIST: VARIABLE LEVELS");
		createOrderedListVariableLevel(clause1, 5);
		clause2.getParagraphs().clear();
		clause2.setBody("CLAUSE TWO: ORDERED LIST: VARIABLE LEVELS");
		createOrderedListVariableLevel(clause2, 5);
		html = section.toHTML(true);
		html = tidy(html, "target/models/SectionToHTMLTestOrderedListVariableLevels.html");
		if (ASSERT) {
			assertEquals(TEST_EXP_SECTION_OL_VARY_LEVELS, html);
		}
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

	private void createOrderedListVariableLevel(final Clause clause) {
		createOrderedListVariableLevel(clause, 1);
	}

	private void createOrderedListVariableLevel(final Clause clause, final int depth) {
		if (isNotNullOrEmpty(clause)) {
			for (final Paragraph p : createOrderedListVariableLevel(clause.getContentCd(), depth)) {
				clause.addParagraph(p);
			}
		}
	}

	private Collection<Paragraph> createOrderedListVariableLevel(final String code) {
		return createOrderedListVariableLevel(code, 1);
	}

	private Collection<Paragraph> createOrderedListVariableLevel(final String code, final int depth) {
		final Collection<Paragraph> list = new LinkedList<Paragraph>();
		if (isNotNullOrEmpty(code)) {
			int orderby = 1;
			int layers = 1;
			if (depth > 0 && depth <= 5) {
				layers = depth;
			}
			if (layers >= 1) {
				final Paragraph list001 = new Paragraph(code + ".list001", "UPPER ALPHA LEVEL: FIRST");
				list001.setFlags(HTML_STYLE_FLAG_FIRST_IN_CLAUSE + HTML_STYLE_FLAG_FIRST + HTML_STYLE_FLAG_SUB);
				if (layers >= 2) {
					list001.setFlags(list001.getFlags().concat(HTML_STYLE_FLAG_PARENT));
				}
				list001.setCss(TEST_VALUE_CSS_PARAGRAPH);
				list001.setOrderBy(orderby++);
				list.add(list001);
				if (layers >= 2) {
					final Paragraph sublist001 = new Paragraph(list001.getContentCd() + ".sublist001",
							"DECIMAL LEVEL: FIRST");
					sublist001.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_FIRST + HTML_STYLE_FLAG_LAST);
					if (layers >= 3) {
						sublist001.setFlags(sublist001.getFlags().concat(HTML_STYLE_FLAG_PARENT));
					}
					sublist001.setCss(TEST_VALUE_CSS_PARAGRAPH);
					sublist001.setOrderBy(orderby++);
					list.add(sublist001);
					if (layers >= 3) {
						final Paragraph subsublist001 = new Paragraph(sublist001.getContentCd() + ".subsublist001",
								"LOWER ALPHA LEVEL: FIRST");
						subsublist001.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_FIRST + HTML_STYLE_FLAG_LAST);
						if (layers >= 4) {
							subsublist001.setFlags(subsublist001.getFlags().concat(HTML_STYLE_FLAG_PARENT));
						}
						subsublist001.setCss(TEST_VALUE_CSS_PARAGRAPH);
						subsublist001.setOrderBy(orderby++);
						list.add(subsublist001);
						if (layers >= 4) {
							final Paragraph subsubsublist001 = new Paragraph(
									subsublist001.getContentCd() + ".subsubsublist001",
									"LOWER ROMAN LEVEL: FIRST ITEM");
							subsubsublist001.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_FIRST);
							if (layers == 5) {
								subsubsublist001.setFlags(subsubsublist001.getFlags().concat(HTML_STYLE_FLAG_PARENT));
							}
							subsubsublist001.setCss(TEST_VALUE_CSS_PARAGRAPH);
							subsubsublist001.setOrderBy(orderby++);
							list.add(subsubsublist001);
							if (layers == 5) {
								final Paragraph subsubsubsublist001 = new Paragraph(
										subsubsublist001.getContentCd() + ".subsubsubsublist001",
										"UNFORMATTED LEVEL: FIRST ITEM");
								subsubsubsublist001.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_FIRST);
								subsubsubsublist001.setCss(TEST_VALUE_CSS_PARAGRAPH);
								subsubsubsublist001.setOrderBy(orderby++);
								list.add(subsubsubsublist001);
								final Paragraph subsubsubsublist002 = new Paragraph(
										subsubsublist001.getContentCd() + ".subsubsubsublist002",
										"UNFORMATTED LEVEL: SECOND ITEM");
								subsubsubsublist002.setFlags(HTML_STYLE_FLAG_SUB);
								subsubsubsublist002.setCss(TEST_VALUE_CSS_PARAGRAPH);
								subsubsubsublist002.setOrderBy(orderby++);
								list.add(subsubsubsublist002);
								final Paragraph subsubsubsublist003 = new Paragraph(
										subsubsublist001.getContentCd() + ".subsubsubsublist003",
										"UNFORMATTED LEVEL: THIRD ITEM");
								subsubsubsublist003.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_LAST);
								subsubsubsublist003.setCss(TEST_VALUE_CSS_PARAGRAPH);
								subsubsubsublist003.setOrderBy(orderby++);
								list.add(subsubsubsublist003);
							} // layer == 5
							final Paragraph subsubsublist002 = new Paragraph(
									subsublist001.getContentCd() + ".subsubsublist002",
									"LOWER ROMAN LEVEL: SECOND ITEM");
							subsubsublist002.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_LAST);
							if (layers == 5) {
								subsubsublist002.setFlags(subsubsublist002.getFlags().concat(HTML_STYLE_FLAG_PARENT));
							}
							subsubsublist002.setCss(TEST_VALUE_CSS_PARAGRAPH);
							subsubsublist002.setOrderBy(orderby++);
							list.add(subsubsublist002);
							if (layers == 5) {
								final Paragraph subsubsubsublist004 = new Paragraph(
										subsubsublist002.getContentCd() + ".subsubsubsublist004",
										"UNFORMATTED LEVEL: FIRST ITEM");
								subsubsubsublist004.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_FIRST);
								subsubsubsublist004.setCss(TEST_VALUE_CSS_PARAGRAPH);
								subsubsubsublist004.setOrderBy(orderby++);
								list.add(subsubsubsublist004);
								final Paragraph subsubsubsublist005 = new Paragraph(
										subsubsublist002.getContentCd() + ".subsubsubsublist005",
										"UNFORMATTED LEVEL: SECOND ITEM");
								subsubsubsublist005.setFlags(HTML_STYLE_FLAG_SUB);
								subsubsubsublist005.setCss(TEST_VALUE_CSS_PARAGRAPH);
								subsubsubsublist005.setOrderBy(orderby++);
								list.add(subsubsubsublist005);
								final Paragraph subsubsubsublist006 = new Paragraph(
										subsubsublist002.getContentCd() + ".subsubsubsublist006",
										"UNFORMATTED LEVEL: THIRD ITEM");
								subsubsubsublist006.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_LAST);
								subsubsubsublist006.setCss(TEST_VALUE_CSS_PARAGRAPH);
								subsubsubsublist006.setOrderBy(orderby++);
								list.add(subsubsubsublist006);
							} // layer == 5
						} // layer >= 4
					} // layer >= 3
				} // layer >= 2
				final Paragraph list002 = new Paragraph(code + ".list002", "UPPER ALPHA LEVEL: SECOND ITEM");
				list002.setFlags(HTML_STYLE_FLAG_SUB);
				if (layers >= 2) {
					list002.setFlags(list002.getFlags().concat(HTML_STYLE_FLAG_PARENT));
				}
				list002.setCss(TEST_VALUE_CSS_PARAGRAPH);
				list002.setOrderBy(orderby++);
				list.add(list002);
				if (layers >= 2) {
					final Paragraph sublist004 = new Paragraph(list002.getContentCd() + ".sublist004",
							"DECIMAL LEVEL: FIRST ITEM");
					sublist004.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_FIRST);
					sublist004.setCss(TEST_VALUE_CSS_PARAGRAPH);
					sublist004.setOrderBy(orderby++);
					list.add(sublist004);
					final Paragraph sublist005 = new Paragraph(list002.getContentCd() + ".sublist005",
							"DECIMAL LEVEL: SECOND ITEM");
					sublist005.setFlags(HTML_STYLE_FLAG_SUB);
					if (layers >= 3) {
						sublist005.setFlags(sublist005.getFlags().concat(HTML_STYLE_FLAG_PARENT));
					}
					sublist005.setCss(TEST_VALUE_CSS_PARAGRAPH);
					sublist005.setOrderBy(orderby++);
					list.add(sublist005);
					if (layers >= 3) {
						final Paragraph subsublist003 = new Paragraph(sublist005.getContentCd() + ".subsublist003",
								"LOWER ALPHA LEVEL: FIRST ITEM");
						subsublist003.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_FIRST);
						subsublist003.setCss(TEST_VALUE_CSS_PARAGRAPH);
						subsublist003.setOrderBy(orderby++);
						list.add(subsublist003);
						final Paragraph subsublist004 = new Paragraph(sublist005.getContentCd() + ".subsublist004",
								"LOWER ALPHA LEVEL: SECOND ITEM");
						subsublist004.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_LAST);
						if (layers >= 4) {
							subsublist004.setFlags(subsublist004.getFlags().concat(HTML_STYLE_FLAG_PARENT));
						}
						subsublist004.setCss(TEST_VALUE_CSS_PARAGRAPH);
						subsublist004.setOrderBy(orderby++);
						list.add(subsublist004);
						if (layers >= 4) {
							final Paragraph subsubsublist004 = new Paragraph(
									subsublist004.getContentCd() + ".subsubsublist004",
									"LOWER ROMAN LEVEL: FIRST ITEM");
							subsubsublist004
									.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_FIRST + HTML_STYLE_FLAG_LAST);
							if (layers == 5) {
								subsubsublist004.setFlags(subsubsublist004.getFlags().concat(HTML_STYLE_FLAG_PARENT));
							}
							subsubsublist004.setCss(TEST_VALUE_CSS_PARAGRAPH);
							subsubsublist004.setOrderBy(orderby++);
							list.add(subsubsublist004);
							if (layers == 5) {
								final Paragraph subsubsubsublist007 = new Paragraph(
										subsubsublist004.getContentCd() + ".subsubsubsublist007",
										"UNFORMATTED LEVEL: FIRST ITEM");
								subsubsubsublist007.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_FIRST);
								subsubsubsublist007.setCss(TEST_VALUE_CSS_PARAGRAPH);
								subsubsubsublist007.setOrderBy(orderby++);
								list.add(subsubsubsublist007);
								final Paragraph subsubsubsublist008 = new Paragraph(
										subsubsublist004.getContentCd() + ".subsubsubsublist008",
										"UNFORMATTED LEVEL: SECOND ITEM");
								subsubsubsublist008.setFlags(HTML_STYLE_FLAG_SUB);
								subsubsubsublist008.setCss(TEST_VALUE_CSS_PARAGRAPH);
								subsubsubsublist008.setOrderBy(orderby++);
								list.add(subsubsubsublist008);
								final Paragraph subsubsubsublist009 = new Paragraph(
										subsubsublist004.getContentCd() + ".subsubsubsublist009",
										"UNFORMATTED LEVEL: THIRD ITEM");
								subsubsubsublist009.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_LAST);
								subsubsubsublist009.setCss(TEST_VALUE_CSS_PARAGRAPH);
								subsubsubsublist009.setOrderBy(orderby++);
								list.add(subsubsubsublist009);
							} // layer == 5
						} // layer >= 4
					} // layer >= 3
					final Paragraph sublist006 = new Paragraph(list002.getContentCd() + ".sublist006",
							"DECIMAL LEVEL: THIRD ITEM");
					sublist006.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_LAST);
					sublist006.setCss(TEST_VALUE_CSS_PARAGRAPH);
					sublist006.setOrderBy(orderby++);
					list.add(sublist006);
				} // layer >= 2
				final Paragraph list003 = new Paragraph(code + ".list003", "UPPER ALPHA LEVEL: THIRD ITEM");
				list003.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_LAST);
				if (layers >= 2) {
					list003.setFlags(list003.getFlags().concat(HTML_STYLE_FLAG_PARENT));
				}
				list003.setCss(TEST_VALUE_CSS_PARAGRAPH);
				list003.setOrderBy(orderby++);
				list.add(list003);
				if (layers >= 2) {
					final Paragraph sublist007 = new Paragraph(list003.getContentCd() + ".sublist007",
							"DECIMAL LEVEL: FIRST ITEM");
					sublist007.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_FIRST);
					sublist007.setCss(TEST_VALUE_CSS_PARAGRAPH);
					sublist007.setOrderBy(orderby++);
					list.add(sublist007);
					final Paragraph sublist008 = new Paragraph(list003.getContentCd() + ".sublist008",
							"DECIMAL LEVEL: SECOND ITEM");
					sublist008.setFlags(HTML_STYLE_FLAG_SUB);
					sublist008.setCss(TEST_VALUE_CSS_PARAGRAPH);
					sublist008.setOrderBy(orderby++);
					list.add(sublist008);
					final Paragraph sublist009 = new Paragraph(list003.getContentCd() + ".sublist009",
							"DECIMAL LEVEL: THIRD ITEM");
					sublist009.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_LAST);
					if (layers >= 3) {
						sublist009.setFlags(sublist009.getFlags().concat(HTML_STYLE_FLAG_PARENT));
					}
					sublist009.setCss(TEST_VALUE_CSS_PARAGRAPH);
					sublist009.setOrderBy(orderby++);
					list.add(sublist009);
					if (layers >= 3) {
						final Paragraph subsublist007 = new Paragraph(sublist009.getContentCd() + ".subsublist007",
								"LOWER ALPHA LEVEL: FIRST ITEM");
						subsublist007.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_FIRST + HTML_STYLE_FLAG_LAST);
						if (layers >= 4) {
							subsublist007.setFlags(subsublist007.getFlags().concat(HTML_STYLE_FLAG_PARENT));
						}
						subsublist007.setCss(TEST_VALUE_CSS_PARAGRAPH);
						subsublist007.setOrderBy(orderby++);
						list.add(subsublist007);
						if (layers >= 4) {
							final Paragraph subsubsublist007 = new Paragraph(
									subsublist007.getContentCd() + ".subsubsublist007",
									"LOWER ROMAN LEVEL: FIRST ITEM");
							subsubsublist007
									.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_FIRST + HTML_STYLE_FLAG_LAST);
							if (layers == 5) {
								subsubsublist007.setFlags(subsubsublist007.getFlags().concat(HTML_STYLE_FLAG_PARENT));
							}
							subsubsublist007.setCss(TEST_VALUE_CSS_PARAGRAPH);
							subsubsublist007.setOrderBy(orderby++);
							list.add(subsubsublist007);
							if (layers == 5) {
								final Paragraph subsubsubsublist010 = new Paragraph(code + ".subsubsubsublist010",
										"UNFORMATTED LEVEL: FIRST ITEM");
								subsubsubsublist010.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_FIRST);
								subsubsubsublist010.setCss(TEST_VALUE_CSS_PARAGRAPH);
								subsubsubsublist010.setOrderBy(orderby++);
								list.add(subsubsubsublist010);
								final Paragraph subsubsubsublist011 = new Paragraph(code + ".subsubsubsublist011",
										"UNFORMATTED LEVEL: SECOND ITEM");
								subsubsubsublist011.setFlags(HTML_STYLE_FLAG_SUB);
								subsubsubsublist011.setCss(TEST_VALUE_CSS_PARAGRAPH);
								subsubsubsublist011.setOrderBy(orderby++);
								list.add(subsubsubsublist011);
								final Paragraph subsubsubsublist012 = new Paragraph(code + ".subsubsubsublist012",
										"UNFORMATTED LEVEL: THIRD ITEM");
								subsubsubsublist012.setFlags(HTML_STYLE_FLAG_SUB + HTML_STYLE_FLAG_LAST);
								subsubsubsublist012.setCss(TEST_VALUE_CSS_PARAGRAPH);
								subsubsubsublist012.setOrderBy(orderby++);
								list.add(subsubsubsublist012);
							} // layer == 5
						} // layer >= 4
					} // layer >= 3
				} // layer >= 2
			} // layer >= 1
		}
		return list;
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
		static final String TEST_EXP_SECTION_OL_VARY_LEVELS = read(
				get("models/SectionExpectedOutput_OrderedList_VariableLevels.html"));
		static final String TEST_EXP_CLAUSE_NO_PARAGRAPHS = read(get("models/ClauseExpectedOutput_NoParagraphs.html"));
		static final String TEST_EXP_CLAUSE_TWO_PARAGRAPHS = read(
				get("models/ClauseExpectedOutput_TwoParagraphs.html"));
		static final String TEST_EXP_CLAUSE_OL_ONE_LEVEL = read(
				get("models/ClauseExpectedOutput_OrderedList_OneLevel.html"));
		static final String TEST_EXP_CLAUSE_OL_TWO_LEVEL = read(
				get("models/ClauseExpectedOutput_OrderedList_TwoLevel.html"));
		static final String TEST_EXP_CLAUSE_OL_VARY_LEVELS = read(
				get("models/ClauseExpectedOutput_OrderedList_VariableLevels.html"));
		static final String TEST_EXP_PARAGRAPH_NO_SUBS = read(get("models/ParagraphExpectedOutput_NoSubs.html"));
		static final String TEST_EXP_PARAGRAPH_OL_ONE_LEVEL = read(
				get("models/ParagraphExpectedOutput_OrderedList_OneLevel.html"));
		static final String TEST_EXP_PARAGRAPH_OL_TWO_LEVEL = read(
				get("models/ParagraphExpectedOutput_OrderedList_TwoLevel.html"));
		static final String TEST_EXP_PARAGRAPH_OL_VARY_LEVELS = read(
				get("models/ParagraphExpectedOutput_OrderedList_VariableLevels.html"));
	}
}