/**
 * TODO: License
 */
package com.itgfirm.docengine.ix;

import java.io.File;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.itgfirm.docengine.content.ContentService;
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;
import com.itgfirm.docengine.util.TestUtils;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class ImportServiceTest extends AbstractTest {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(ImportServiceTest.class);
	@Autowired @Qualifier("default")
	private ContentService contentService;
	@Autowired
	private ImportService importService;

	@Test
	public void aa_ImportTest() {
		File file = TestUtils.getFileFromClasspath("content-export.xlsx");
		for (Object o : importService.importFromFile(file, ContentJpaImpl.class)) {
			assertTrue(o instanceof Content);
			Content c = (Content) o;
			assertNotNull(c.getId());
		}
	}
}