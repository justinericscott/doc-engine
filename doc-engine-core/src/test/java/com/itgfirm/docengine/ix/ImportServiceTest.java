/**
 * TODO: License
 */
package com.itgfirm.docengine.ix;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.itgfirm.docengine.service.ContentService;
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
	@Autowired @Qualifier("default")
	private ContentService contentService;
	@Autowired
	private ImportService importService;

	@Test
	public void aa_ImportTest() {
		File file = TestUtils.getFileFromClasspath("content-export.xlsx");
		for (Object o : importService.importFromFile(file, ContentJpaImpl.class)) {
			assertTrue(o instanceof ContentJpaImpl);
			ContentJpaImpl c = (ContentJpaImpl) o;
			assertNotNull(c.getId());
		}
	}
}