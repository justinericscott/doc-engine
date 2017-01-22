package com.itgfirm.docengine.service.ix;

import static org.junit.Assert.*;
import static com.itgfirm.docengine.service.ix.ImportExportServiceTest.ImportExportServiceTestConstants.*;

import java.io.File;
import java.sql.SQLException;
import java.util.Collection;
import java.util.TreeSet;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.itgfirm.docengine.service.content.ContentService;
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

	@Test
	@Ignore
	public void a_ImportTest() throws SQLException {
		createContents(10);
		Contents contents = _contents.findAll();
		assertNotNull(contents);
		Contents objects = _service.importFromFile(Contents.class, TEST_PATH_IMPORT);
		assertNotNull(objects);
		for (final ContentJpaImpl o : objects.getContents()) {
			assertTrue(o.isValid(true));
			LOG.trace("Type of Content is {}.", o.getClass().getSimpleName());
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

	private ContentJpaImpl createContent() {
		ContentJpaImpl content = makeTestContent();
		content = _contents.save(content);
		assertNotNull(content);
		assertTrue(content.isValid(true));
		return content;
	}

	private Contents createContents(int count) {
		Collection<ContentJpaImpl> contents = new TreeSet<ContentJpaImpl>();
		for (int i = 0; i < count; i++) {
			contents.add(createContent());
		}
		return new Contents(contents);
	}

	static class ImportExportServiceTestConstants {
		static final String TEST_PATH_IMPORT = "content-export.xlsx";
		static final String TEST_PATH_EXPORT = "target/content-export.xlsx";
	}
}