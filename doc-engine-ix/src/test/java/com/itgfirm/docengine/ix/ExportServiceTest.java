package com.itgfirm.docengine.ix;

import java.io.File;
import java.util.List;

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
import com.itgfirm.docengine.util.Utils;

/**
 * @author Justin Scott
 * TODO: Description
 */
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class ExportServiceTest extends AbstractTest {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(ExportServiceTest.class);
	@Autowired @Qualifier("default")
	private ContentService contentService;
	@Autowired
	private ExportService exportService;
	
	@Test
	public void aa_ExportTest() {
		List<Content> contents = contentService.get();
		if (Utils.isNotNullOrEmpty(contents)) {
			File content = exportService.export(contents, ContentJpaImpl.class, "target/content-export.xlsx");
			assertNotNull(content);
			assertTrue(content.exists());			
		}
	}
}