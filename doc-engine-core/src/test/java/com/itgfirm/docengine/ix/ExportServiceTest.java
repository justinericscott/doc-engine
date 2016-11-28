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
import com.itgfirm.docengine.util.Utils;

/**
 * @author Justin Scott
 * TODO: Description
 */
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class ExportServiceTest extends AbstractTest {
	@Autowired @Qualifier("default")
	private ContentService contentService;
	@Autowired
	private ExportService exportService;
	
	@Test
	public void aa_ExportTest() {
		Iterable<? extends ContentJpaImpl> contents = contentService.findAll();
		if (Utils.isNotNullOrEmpty(contents)) {
			File content = exportService.export(contents, ContentJpaImpl.class, "target/content-export.xlsx");
			assertNotNull(content);
			assertTrue(content.exists());			
		}
	}
}