/**TODO: License
 */
package com.itgfirm.docengine.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;

/**
 * @author Justin Scott
 * TODO: Description
 */
public class TestUtils {
	private static final Logger LOG = LogManager.getLogger(TestUtils.class);
	public static final String TEST_CODE_PREFIX = "TEST_CONTENT";

	
	public static File getFileFromClasspath(String classpath) {
		try {
			return new ClassPathResource(classpath).getFile();
		} catch (IOException e) {
			LOG.debug(e.getMessage(), e);
			return null;
		}
	}
	
	public static String getRandomTestString(Integer seed) {
		if (seed == null || seed == 0) seed = 1;
		Long time = new Date().getTime();
		Long suffix = time / seed;
		String id = TEST_CODE_PREFIX + suffix;
//		LOG.debug("Generated PARAM_ID: " + id);
		return id;
	}
	
	public static List<Content> getListOfRandomContents(int count) {
		List<Content> content = new ArrayList<Content>();
		
		while (count > 0) {
			int seed = count * 3;
			count--;
			content.add(new ContentJpaImpl(
					TestUtils.getRandomTestString(seed) + seed, "TEST_BODY"));
		}
		return content;
	}
	
	private TestUtils() { }
}
