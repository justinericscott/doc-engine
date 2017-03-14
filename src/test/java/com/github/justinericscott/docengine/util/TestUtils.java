package com.github.justinericscott.docengine.util;

import static com.github.justinericscott.docengine.util.TestUtils.TestConstants.*;
import static com.github.justinericscott.docengine.util.Utils.collapse;
import static com.github.justinericscott.docengine.util.Utils.create;
import static com.github.justinericscott.docengine.util.Utils.delete;
import static com.github.justinericscott.docengine.util.Utils.get;
import static com.github.justinericscott.docengine.util.Utils.isNotNullAndExists;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;
import static com.github.justinericscott.docengine.util.Utils.read;
import static com.github.justinericscott.docengine.util.Utils.write;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.ClassPathResource;

import org.w3c.tidy.Tidy;

import com.github.justinericscott.docengine.annotation.ExcelColumn;
import com.github.justinericscott.docengine.service.ix.types.ExampleExcelType;
import com.github.justinericscott.docengine.service.ix.types.ExampleExcelTypeWithCustomLabels;
import com.github.justinericscott.docengine.service.ix.types.ExampleExcelTypeWithCustomLabelsOrdered;
import com.github.justinericscott.docengine.service.ix.types.ExampleExcelTypeWithCustomLabelsOrderedReversed;
import com.github.justinericscott.docengine.service.ix.types.ExampleExcelTypeWithCustomLabelsOrderedUnordered;
import com.github.justinericscott.docengine.service.ix.types.NoAnnotationType;
import com.github.justinericscott.docengine.util.Utils.TidyFactory;

@SuppressWarnings("serial")
public class TestUtils {
	private static final Logger LOG = LoggerFactory.getLogger(TestUtils.class);
	private static final TidyFactory TIDY_FACTORY = new TidyFactory();

	private TestUtils() {
		// Do not instantiate
	}

	public static Collection<ExampleExcelTypeWithCustomLabelsOrdered> createTestData() {
		final String name = "TEST Name - ";
		final String description = "TEST Description - ";
		Collection<ExampleExcelTypeWithCustomLabelsOrdered> list = new ArrayList<ExampleExcelTypeWithCustomLabelsOrdered>();
		long id = 0L;
		while (id < TEST_DATA_LIMIT) {
			ExampleExcelTypeWithCustomLabelsOrdered example = new ExampleExcelTypeWithCustomLabelsOrdered(id, name + id,
					description + id, true);
			list.add(example);
			id++;
		}
		return list;
	}

	public static File getFileFromClasspath(final String classpath) {
		try {
			return new ClassPathResource(classpath).getFile();
		} catch (final IOException e) {
			return null;
		}
	}

	/**
	 * Obtains a {@link File} object for the host operating system's temporary
	 * directory.
	 * 
	 * @return {@link File} representing the temporary directory.
	 */
	public static File getSystemTempDirectory() {
		final String path = System.getProperty(FILE_SYS_TEMP_DIR);
		if (isNotNullOrEmpty(path)) {
			final File file = new File(path);
			return (isNotNullAndExists(file) ? file : null);
		} else {
			LOG.warn("Could not determine system temporary directory!");
		}
		return null;
	}

	/**
	 * Provides a {@link List} of all {@link File}s in the given directory or
	 * directory structure.
	 * 
	 * If a file extension is provided, only those files matching the given
	 * extension are returned.
	 * 
	 * @param directory
	 *            Directory to list {@link File}s for.
	 * @param extension
	 *            File extension to filter on.
	 * @param recursive
	 *            Flag to recursively list all files in the directory
	 *            structure..
	 * @return {@link List} of {@link File} objects or null.
	 */
	public static Iterable<File> list(final File directory, final String extension, final boolean recursive) {
		if (isNotNullAndExists(directory) && directory.isDirectory()) {
			final File[] array = directory.listFiles();
			if (array.length > 0) {
				final Collection<File> files = new ArrayList<File>();
				for (final File f : array) {
					if (f.isFile()) {
						if (!isNotNullOrEmpty(extension)) {
							files.add(f);
						} else if (f.getName().endsWith(extension)) {
							files.add(f);
						}
					} else if (f.isDirectory()) {
						if (recursive) {
							final Collection<File> recurse = (Collection<File>) list(f, extension, recursive);
							if (isNotNullOrEmpty(recurse)) {
								files.addAll(recurse);
							}
						}
					}
				}
				if (!files.isEmpty()) {
					return files;
				}
			}
		} else {
			LOG.warn("Directory object must not be null, it must be a directory and exist!");
		}
		return null;
	}

	public static String tidy(final String xhtml, final String path) {
		if (isNotNullOrEmpty(xhtml)) {
			final Tidy tidy = TIDY_FACTORY.getTidy();
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

	public static class TestConstants {

		private TestConstants() {
			// Do not instantiate
		}

		public static final String FILE_SYS_TEMP_DIR = "java.io.tmpdir";

		public static final String TEST_ANNOTATION_VALUE_IDENTIFICATION = "Identification";
		public static final String TEST_ANNOTATION_VALUE_NAME = "Name";
		public static final String TEST_ANNOTATION_VALUE_DESCRIPTION = "Description";
		public static final String TEST_ANNOTATION_VALUE_POSITIVE = "Positive";
		public static final String TEST_REGEX_VALUE_EXPECTED = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta name=\"generator\" content=\"HTML Tidy for Java vers. 2009-12-01, see jtidy.sourceforge.net\" /><title>Test Title Section</title><style type=\"text/css\">/*<![CDATA[*/body, h1 { font-family: Calibri; font-size: 8pt; line-height: 120%; } h1 { font-size: 14px; font-weight: bold; line-height: 100%; } hr { height: 2px; display: block; color: black; background-color: black; } .blue-text { font-weight: bold; color: blue; } .break { page-break-before: always; } /*]]>*/</style></head><body><hr /><h1>1.&nbsp;&nbsp;&nbsp;STANDALONE SECTION: NO CLAUSES</h1><hr /></body></html>";
		public static final Class<ExcelColumn> TEST_ANNOTATION_CLASS = ExcelColumn.class;

		public static final String TEST_BODY_CONTENT = "TEST BODY";

		public static final Class<?> TEST_CLASS_ANNOTATED = ExampleExcelType.class;
		public static final Class<?> TEST_CLASS_CUSTOM_LABELS = ExampleExcelTypeWithCustomLabels.class;
		public static final Class<?> TEST_CLASS_CUSTOM_LABELS_ORDERED = ExampleExcelTypeWithCustomLabelsOrdered.class;
		public static final Class<?> TEST_CLASS_CUSTOM_LABELS_ORDERED_REVERSED = ExampleExcelTypeWithCustomLabelsOrderedReversed.class;
		public static final Class<?> TEST_CLASS_CUSTOM_LABELS_ORDERED_UNORDERED = ExampleExcelTypeWithCustomLabelsOrderedUnordered.class;
		public static final Class<?> TEST_CLASS_NO_ANNOTATION = NoAnnotationType.class;

		public static final String TEST_CODE_DOC_TYPE_L201 = "L201";
		public static final String TEST_CODE_DOC_TYPE_R101 = "R101";

		public static final String TEST_CODE_PREFIX_CLAUSE = "TEST_CLAUSE_CODE_";
		public static final String TEST_CODE_PREFIX_CONTENT = "TEST_CONTENT_CODE_";
		public static final String TEST_CODE_PREFIX_DOCUMENT = "TEST_DOCUMENT_CODE_";
		public static final String TEST_CODE_PREFIX_PARAGRAPH = "TEST_PARAGRAPH_CODE_";
		public static final String TEST_CODE_PREFIX_SECTION = "TEST_SECTION_CODE_";
		public static final String TEST_CODE_PREFIX_TOKEN = "TEST_TOKEN_CODE_";

		public static final String TEST_CSS_VALUE_CLAUSE = read(get("css/clause.css"));
		public static final String TEST_CSS_VALUE_PARAGRAPH = read(get("css/paragraph.css"));
		public static final String TEST_CSS_VALUE_SECTION = read(get("css/section.css"));

		public static final Collection<ExampleExcelTypeWithCustomLabelsOrdered> TEST_DATA = createTestData();
		public static final long TEST_DATA_LIMIT = 100L;
		public static final String TEST_DATA_PROJECT_ID = "SIMPH00501";

		public static final String TEST_EXP_SECTION_NO_CLAUSES = read(
				get("models/SectionExpectedOutput_NoClauses.html"));
		public static final String TEST_EXP_SECTION_TWO_CLAUSES_NO_PARAGRAPHS = read(
				get("models/SectionExpectedOutput_TwoClausesNoParagraphs.html"));
		public static final String TEST_EXP_SECTION_TWO_CLAUSES_TWO_PARAGRAHS_EACH = read(
				get("models/SectionExpectedOutput_TwoClausesTwoParagraphsEach.html"));
		public static final String TEST_EXP_SECTION_OL_ONE_LEVEL = read(
				get("models/SectionExpectedOutput_OrderedList_OneLevel.html"));
		public static final String TEST_EXP_SECTION_OL_TWO_LEVEL = read(
				get("models/SectionExpectedOutput_OrderedList_TwoLevel.html"));
		public static final String TEST_EXP_SECTION_OL_VARY_LEVELS = read(
				get("models/SectionExpectedOutput_OrderedList_VariableLevels.html"));
		public static final String TEST_EXP_CLAUSE_NO_PARAGRAPHS = read(
				get("models/ClauseExpectedOutput_NoParagraphs.html"));
		public static final String TEST_EXP_CLAUSE_TWO_PARAGRAPHS = read(
				get("models/ClauseExpectedOutput_TwoParagraphs.html"));
		public static final String TEST_EXP_CLAUSE_OL_ONE_LEVEL = read(
				get("models/ClauseExpectedOutput_OrderedList_OneLevel.html"));
		public static final String TEST_EXP_CLAUSE_OL_TWO_LEVEL = read(
				get("models/ClauseExpectedOutput_OrderedList_TwoLevel.html"));
		public static final String TEST_EXP_CLAUSE_OL_VARY_LEVELS = read(
				get("models/ClauseExpectedOutput_OrderedList_VariableLevels.html"));
		public static final String TEST_EXP_PARAGRAPH_NO_SUBS = read(get("models/ParagraphExpectedOutput_NoSubs.html"));
		public static final String TEST_EXP_PARAGRAPH_OL_ONE_LEVEL = read(
				get("models/ParagraphExpectedOutput_OrderedList_OneLevel.html"));
		public static final String TEST_EXP_PARAGRAPH_OL_TWO_LEVEL = read(
				get("models/ParagraphExpectedOutput_OrderedList_TwoLevel.html"));
		public static final String TEST_EXP_PARAGRAPH_OL_VARY_LEVELS = read(
				get("models/ParagraphExpectedOutput_OrderedList_VariableLevels.html"));

		public static final String TEST_FIELD_NAME_ID = "id";
		public static final String TEST_FIELD_NAME_NAME = "name";
		public static final String TEST_FIELD_NAME_DESCRIPTION = "description";
		public static final String TEST_FIELD_NAME_POSITIVE = "positive";
		public static final String[] TEST_FIELD_NAMES = { TEST_FIELD_NAME_ID, TEST_FIELD_NAME_NAME,
				TEST_FIELD_NAME_DESCRIPTION, TEST_FIELD_NAME_POSITIVE };
		public static final String[] TEST_FIELD_LABELS = { "Identification", "Name", "Description", "Positive" };
		public static final Object[] TEST_FIELD_VALUES = { 111L, "Test Name", "Test Description", new Boolean(true) };

		public static final String TEST_FILE_BODY = "template/template-body.ftl";
		public static final String TEST_FILE_CONTENT = "ix/Document Engine Content.xlsx";
		public static final String TEST_FILE_EXP_BODY = "template/expected-body.html";
		public static final String TEST_FILE_EXP_BROKEN_BODY = "template/expected-broken-body.html";
		public static final String TEST_FILE_EXP_NULL_TEMPLATE_NAME = "template/expected-null-template-name.html";
		public static final String TEST_FILE_EXP_NULL_TOKEN = "template/expected-null-token.html";
		public static final String TEST_FILE_EXP_NULL_TOKENS = "template/expected-null-tokens.html";
		public static final String TEST_FILE_EXPORT = "target/content-export.xlsx";
		public static final String TEST_FILE_IMPORT = "ix/content-import.xlsx";
		public static final String TEST_FILE_NAME_READ = "ix/test-data-read.xlsx";
		public static final String TEST_FILE_NAME_WRITE = "target/test-data-write.xlsx";

		public static final String TEST_FTL_BODY = read(get(TEST_FILE_BODY));
		public static final String TEST_FTL_BODY_NAME = "test_template";
		public static final String TEST_FTL_BROKEN_BODY = "Hello, ${name!\" ... What's Your Name?}!!!";
		public static final String TEST_FTL_BROKEN_NAME = "test_template_broken";
		public static final String TEST_FTL_EXP_BODY = read(get(TEST_FILE_EXP_BODY));
		public static final String TEST_FTL_EXP_BROKEN_BODY = read(get(TEST_FILE_EXP_BROKEN_BODY));
		public static final String TEST_FTL_EXP_NULL_TEMPLATE_NAME = read(get(TEST_FILE_EXP_NULL_TEMPLATE_NAME));
		public static final String TEST_FTL_EXP_NULL_TOKEN = read(get(TEST_FILE_EXP_NULL_TOKEN));
		public static final String TEST_FTL_EXP_NULL_TOKENS = read(get(TEST_FILE_EXP_NULL_TOKENS));

		public static final String TEST_PROJECT_ID_PREFIX = "TEST_PROJECT_ID_";
		public static final String TEST_PROJECT_ID_VALUE = "SIMPH00501";

		public static final String TEST_SHEET_NAME = "Excel Example Type";
		
		public static final String TEST_TOKEN_NAME = "world";
		public static final Object TEST_TOKEN_VALUE = "World";	
		public static final Map<String, Object> TOKENS = new HashMap<String, Object>() {
			{
				put(TEST_TOKEN_NAME, TEST_TOKEN_VALUE);
			}
		};
	}
}