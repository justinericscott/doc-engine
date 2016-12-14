package com.itgfirm.docengine.util;

import static org.junit.Assert.*;

import static com.itgfirm.docengine.util.TestConstants.*;
import static com.itgfirm.docengine.util.Utils.*;

import org.junit.Test;

public class UtilsTest extends AbstractTest {

	@Test
	public void a_getFieldNameFromAnnotationValueTest() {
		String name = getFieldNameFromAnnotationValue(TEST_CLASS_CUSTOM_LABELS_ORDERED, TEST_ANNOTATION_CLASS,
				TEST_ANNOTATION_VALUE_NAME);
		assertNotNull(name);
		assertEquals(TEST_FIELD_NAME_NAME, name);
	}
}