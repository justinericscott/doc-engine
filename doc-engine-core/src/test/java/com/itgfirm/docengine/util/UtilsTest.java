package com.itgfirm.docengine.util;

import static org.junit.Assert.*;

import static com.itgfirm.docengine.util.AbstractTest.TestConstants.*;
import static com.itgfirm.docengine.util.Utils.*;
import static com.itgfirm.docengine.util.UtilsTest.UtilsTestConstants.*;

import org.junit.Test;

import com.itgfirm.docengine.annotation.ExcelColumn;

public class UtilsTest extends AbstractTest {

	@Test
	public void a_getFieldNameFromAnnotationValueTest() {
		String name = getFieldNameFromAnnotationValue(TEST_CLASS_CUSTOM_LABELS_ORDERED, TEST_ANNOTATION_CLASS,
				TEST_ANNOTATION_VALUE_NAME);
		assertNotNull(name);
		assertEquals(TEST_FIELD_NAME_NAME, name);
	}
	
	static class UtilsTestConstants {
		static final String TEST_ANNOTATION_VALUE_IDENTIFICATION = "Identification";
		static final String TEST_ANNOTATION_VALUE_NAME = "Name";
		static final String TEST_ANNOTATION_VALUE_DESCRIPTION = "Description";
		static final String TEST_ANNOTATION_VALUE_POSITIVE = "Positive";
		
		static final Class<ExcelColumn> TEST_ANNOTATION_CLASS = ExcelColumn.class;

	}
}