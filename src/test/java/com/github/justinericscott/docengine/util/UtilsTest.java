package com.github.justinericscott.docengine.util;

import static org.junit.Assert.*;

import static com.github.justinericscott.docengine.util.TestUtils.TestConstants.*;
import static com.github.justinericscott.docengine.util.Utils.*;

import org.junit.Test;

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
		final String newlines = "\n" + "A\n" + "B\n" + "C\n" + "D\n" + "\n";
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
}