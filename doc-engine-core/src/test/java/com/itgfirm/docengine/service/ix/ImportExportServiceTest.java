package com.itgfirm.docengine.service.ix;

import static org.junit.Assert.*;
import static com.itgfirm.docengine.service.ix.ImportExportServiceTest.ImportExportServiceTestConstants.*;

import java.io.File;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import com.itgfirm.docengine.service.content.ContentService;
import com.itgfirm.docengine.service.content.InstanceService;
import com.itgfirm.docengine.service.ix.ImportExportService;
import com.itgfirm.docengine.types.ContentJpaImpl;
import com.itgfirm.docengine.types.Contents;
import com.itgfirm.docengine.util.AbstractTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ImportExportServiceTest extends AbstractTest {
	private static final Logger LOG = LoggerFactory.getLogger(ImportExportServiceTest.class);

	@Autowired
	private ImportExportService _service;

	@Autowired
	private ContentService _contents;
	
	@Autowired
	private InstanceService _instances;

	@Test
	public void a_ImportTest() {
		if (_instances.deleteAll()) {
			if (_contents.deleteAll()) {
				Contents objects = _service.importFromFile(Contents.class, TEST_PATH_IMPORT);
				assertNotNull(objects);
				for (final ContentJpaImpl o : objects.getContents()) {
					assertTrue(o.isValid(true));
					LOG.trace("Type of Content is {}.", o.getClass().getSimpleName());
				}			
			} else {
				throw new IllegalStateException("Contents have not been cleared!");
			}
		} else {
			throw new IllegalStateException("Instances have not been cleared!");
		}
	}

	@Test
	public void b_ExportTest() {
		Contents contents = _contents.findAll();
		assertNotNull(contents);
		File file = _service.exportToFile(ContentJpaImpl.class, TEST_PATH_EXPORT);
		assertNotNull(file);
		assertTrue(file.exists());		
	}
	
	@Test
	public void c_LiveContentTest() {
		
	}

	static class ImportExportServiceTestConstants {
		static final String TEST_PATH_IMPORT = "content-export.xlsx";
		static final String TEST_PATH_EXPORT = "target/content-export.xlsx";
	}
}