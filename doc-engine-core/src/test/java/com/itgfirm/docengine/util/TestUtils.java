package com.itgfirm.docengine.util;

import static com.itgfirm.docengine.util.TestConstants.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.core.io.ClassPathResource;

import com.itgfirm.docengine.types.ExampleExcelTypeWithCustomLabelsOrdered;

public class TestUtils {

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

	public static String getRandomTestString(final Integer seed) {
		return TEST_CODE_PREFIX + (new Date().getTime() / seed);
	}
}