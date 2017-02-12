package com.github.justinericscott.docengine.util;

import static org.junit.Assert.*;
import static com.github.justinericscott.docengine.util.AbstractTest.TestConstants.*;
import static com.github.justinericscott.docengine.util.Utils.*;
import static com.github.justinericscott.docengine.util.UtilsTest.UtilsTestConstants.*;

import org.junit.Test;

import com.github.justinericscott.docengine.annotation.ExcelColumn;

public class UtilsTest extends AbstractTest {

	@Test
	public void a_getFieldNameFromAnnotationValueTest() {
		String name = getFieldNameFromAnnotationValue(TEST_CLASS_CUSTOM_LABELS_ORDERED, TEST_ANNOTATION_CLASS,
				TEST_ANNOTATION_VALUE_NAME);
		assertNotNull(name);
		assertEquals(TEST_FIELD_NAME_NAME, name);
	}
	
	@Test
	public void b_StringBuilderTest() {
		final StringBuilder sb = new StringBuilder();
		assertNotNull(sb.toString());
		assertEquals(0, sb.toString().length());
	}
	
	@Test
	public void c_collapseTest() {
		final String tabs = "	A	B	C	D	";
		assertEquals("ABCD", tabs.replaceAll("\t", ""));
		final String newlines = "\n"
				+ "A\n"
				+ "B\n"
				+ "C\n"
				+ "D\n"
				+ "\n";
		assertEquals("ABCD", newlines.replaceAll("\n", ""));
		final String spaces = "    A    B    C    D    ";
		assertEquals("ABCD", spaces.replaceAll("[ ]", ""));
		final String html = "<html>    <body>This is what the world is about</body>    </html>";
		assertEquals("<html><body>This is what the world is about</body></html>", html.replaceAll(">+[ ]+<", "><"));
		final String complex = read(get("html/regex.html"));
		String result = complex.replaceAll("[(\n\r\t)]", "");
		result = result.replaceAll(">+[ ]+<", "><");
		result = result.replaceAll("[( )][^A-Za-z0-9<>\\*\\.,;:\\?=/{}\"\\\\\\(\\)]", " ");
		result = result.replaceAll("[^A-Za-z0-9<>\\*\\.,;:\\?=/{}\"\\\\\\(\\)][( )]", " ");
		result = result.replaceAll("  ", " ");
		result = result.replaceAll("}", " } ");
		result = result.replaceAll("\"\"", "\" \"");
		assertEquals(TEST_REGEX_VALUE_EXPECTED, result);
	}
	
	static class UtilsTestConstants {
		static final String TEST_ANNOTATION_VALUE_IDENTIFICATION = "Identification";
		static final String TEST_ANNOTATION_VALUE_NAME = "Name";
		static final String TEST_ANNOTATION_VALUE_DESCRIPTION = "Description";
		static final String TEST_ANNOTATION_VALUE_POSITIVE = "Positive";
		static final String TEST_REGEX_VALUE_EXPECTED = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta name=\"generator\" content=\"HTML Tidy for Java vers. 2009-12-01, see jtidy.sourceforge.net\" /><title>Test Title Section</title><style type=\"text/css\">/*<![CDATA[*/body, h1 { font-family: Calibri; font-size: 8pt; line-height: 120%; } h1 { font-size: 14px; font-weight: bold; line-height: 100%; } hr { height: 2px; display: block; color: black; background-color: black; } .blue-text { font-weight: bold; color: blue; } .break { page-break-before: always; } /*]]>*/</style></head><body><hr /><h1>1.&nbsp;&nbsp;&nbsp;STANDALONE SECTION: NO CLAUSES</h1><hr /></body></html>";
		
	
		static final Class<ExcelColumn> TEST_ANNOTATION_CLASS = ExcelColumn.class;

	}
}