package com.itgfirm.docengine.service;

import static org.junit.Assert.*;
import static com.itgfirm.docengine.DocEngine.Constants.*;
import static com.itgfirm.docengine.util.TestConstants.*;

import java.io.File;
import java.util.Collection;

import org.junit.FixMethodOrder;

import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ImportExportServiceTest extends AbstractTest {
	private static final Logger LOG = LoggerFactory.getLogger(ImportExportServiceTest.class);

	@Autowired
	private ImportExportService service;

	@Autowired
	@Qualifier(AUTOWIRE_QUALIFIER_DEFAULT)
	private ContentService content;
	
	private static int importSize = 0;
	private static int exportSize = 0;

	@Test
	public void a_ImportTest() {
		Collection<? extends ContentJpaImpl> objects = (Collection<? extends ContentJpaImpl>) service.importFromFile(ContentJpaImpl.class, TEST_PATH_IMPORT);
		assertNotNull(objects);
		assertFalse(objects.isEmpty());
		for (final ContentJpaImpl o : objects) {
			assertTrue(o.isValid(true));
		}
		importSize = objects.size();
		LOG.debug("Size of contents from import is {}.", importSize);

	}

	@Test
	public void b_ExportTest() {
		Collection<? extends ContentJpaImpl> contents = (Collection<? extends ContentJpaImpl>) content.findAll();
		assertNotNull(contents);
		assertFalse(contents.isEmpty());
		exportSize = contents.size();
		LOG.debug("Size of contents from export {}.", exportSize);
		File file = service.exportToFile(ContentJpaImpl.class, TEST_PATH_EXPORT);
		assertNotNull(file);
		assertTrue(file.exists());
		assertEquals(importSize, exportSize);
	}
}