package com.itgfirm.docengine.service;

import static org.junit.Assert.*;
import static com.itgfirm.docengine.util.Constants.*;

import java.io.File;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ImportExportServiceTest extends AbstractTest {

	@Autowired
	private ImportExportService service;

	@Autowired
	@Qualifier(AUTOWIRE_QUALIFIER_DEFAULT)
	private ContentService content;

	@Test
	public void a_ImportTest() {
		Iterable<?> objects = service.importFromFile(ContentJpaImpl.class, "content-export.xlsx");
		assertNotNull(objects);
		for (Object o : objects) {
			assertTrue(o instanceof ContentJpaImpl);
			ContentJpaImpl c = (ContentJpaImpl) o;
			assertTrue(c.isValid(true));
		}
	}

	@Test
	public void b_ExportTest() {
		Iterable<? extends ContentJpaImpl> contents = content.findAll();
		assertNotNull(contents);
		File file = service.exportToFile(ContentJpaImpl.class, "target/content-export.xlsx");
		assertNotNull(file);
		assertTrue(file.exists());
	}
}