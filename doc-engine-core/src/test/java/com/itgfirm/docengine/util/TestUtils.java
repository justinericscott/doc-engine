package com.itgfirm.docengine.util;

import static com.itgfirm.docengine.util.Utils.isNotNullAndExists;
import static com.itgfirm.docengine.util.Utils.isNotNullOrEmpty;
import static com.itgfirm.docengine.util.AbstractTest.TestConstants.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.itgfirm.docengine.service.ix.types.ExampleExcelTypeWithCustomLabelsOrdered;

public class TestUtils {
	private static final Logger LOG = LoggerFactory.getLogger(TestUtils.class);

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
}