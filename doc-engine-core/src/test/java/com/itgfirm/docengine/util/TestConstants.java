package com.itgfirm.docengine.util;

import static com.itgfirm.docengine.util.TestUtils.*;

import java.util.Collection;

import com.itgfirm.docengine.types.ExampleExcelType;
import com.itgfirm.docengine.types.ExampleExcelTypeWithCustomLabels;
import com.itgfirm.docengine.types.ExampleExcelTypeWithCustomLabelsOrdered;
import com.itgfirm.docengine.types.ExampleExcelTypeWithCustomLabelsOrderedReversed;
import com.itgfirm.docengine.types.ExampleExcelTypeWithCustomLabelsOrderedUnordered;
import com.itgfirm.docengine.types.NoAnnotationType;

public class TestConstants {
	public static final String TARGET_FILE_NAME_READ = "test-data-read.xlsx";
	public static final String TARGET_FILE_NAME_WRITE = "target/test-data-write.xlsx";

	public static final Class<?> TEST_CLASS_ANNOTATED = ExampleExcelType.class;
	public static final Class<?> TEST_CLASS_CUSTOM_LABELS = ExampleExcelTypeWithCustomLabels.class;
	public static final Class<?> TEST_CLASS_CUSTOM_LABELS_ORDERED = ExampleExcelTypeWithCustomLabelsOrdered.class;
	public static final Class<?> TEST_CLASS_CUSTOM_LABELS_ORDERED_REVERSED = ExampleExcelTypeWithCustomLabelsOrderedReversed.class;
	public static final Class<?> TEST_CLASS_CUSTOM_LABELS_ORDERED_UNORDERED = ExampleExcelTypeWithCustomLabelsOrderedUnordered.class;
	public static final Class<?> TEST_CLASS_NO_ANNOTATION = NoAnnotationType.class;

	public static final String TEST_CODE_DOC_TYPE_L201 = "L201";
	public static final String TEST_CODE_DOC_TYPE_R101 = "R101";
	public static final String TEST_CODE_LEASE_MODEL_CD_L201_AWARDED = "leaseModelCd_L201_AWARD";
	public static final String TEST_CODE_LEASE_MODEL_CD_L201_PROPOSED = "leaseModelCd_L201_PROPOSED";
	public static final String TEST_CODE_LEASE_MODEL_CD_R101 = "leaseModelCd_R101";
	public static final String TEST_CODE_LEASE_NBR_L201_AWARDED = "leaseNumber_L201_AWARD";
	public static final String TEST_CODE_LEASE_NBR_L201_PROPOSED = "leaseNumber_L201_PROPOSED";
	public static final String TEST_CODE_LEASE_NBR_R101 = "leaseNumber_R101";
	public static final String TEST_CODE_PHASE_AWARDED = "AWARDED";
	public static final String TEST_CODE_PHASE_PROPOSED = "PROPOSED";
	public static final String TEST_CODE_PREFIX = "TEST_CONTENT";
	public static final String TEST_CODE_PROJECT_NBR_L201_AWARDED = "projectNumber_L201_AWARD";
	public static final String TEST_CODE_PROJECT_NBR_L201_PROPOSED = "projectNumber_L201_PROPOSED";
	public static final String TEST_CODE_PROJECT_NBR_R101 = "projectNumber_R101";

	public static final Collection<ExampleExcelTypeWithCustomLabelsOrdered> TEST_DATA = createTestData();
	public static final String[] TEST_DATA_FIELD_LABELS = { "Identification", "Name", "Description", "Boolean Test" };
	public static final String[] TEST_DATA_FIELD_NAMES = { "id", "name", "description", "positive" };
	public static final Object[] TEST_VALUES = { 111L, "Test Name", "Test Description", true };

	public static final long TEST_DATA_LIMIT = 100L;
	public static final String TEST_DATA_PROJECT_ID = "SIMPH00501";

	public static final String TEST_NAME_ATTRIBUTE_LEASE_MODEL_CD = "LEASE_MODEL_CD";
	public static final String TEST_NAME_ATTRIBUTE_LEASE_NBR = "LEASE_NBR";
	public static final String TEST_NAME_ATTRIBUTE_PROJECT_NBR = "PROJECT_NBR";
	public static final String TEST_NAME_ENTITY = "TR_PROJECT";
	public static final String TEST_NAME_LEASE_MODEL_CD = "leaseModelCd";
	public static final String TEST_NAME_LEASE_NUMBER = "leaseNumber";
	public static final String TEST_NAME_PROJECT_NUMBER = "projectNumber";
	public static final String TEST_NAME_WHERE = "PROJECT_NBR = ?";

	private TestConstants() {
		// Do not instantiate
	}
}