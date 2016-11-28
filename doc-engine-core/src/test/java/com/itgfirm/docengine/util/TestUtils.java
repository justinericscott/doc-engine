package com.itgfirm.docengine.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.springframework.core.io.ClassPathResource;

public class TestUtils {
	public static final String TEST_CODE_PREFIX = "TEST_CONTENT";

	private TestUtils() {
		// Do not instantiate
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