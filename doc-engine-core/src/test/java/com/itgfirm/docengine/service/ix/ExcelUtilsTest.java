package com.itgfirm.docengine.service.ix;

import static com.itgfirm.docengine.util.Utils.*;
import static com.itgfirm.docengine.service.ix.ExcelUtils.*;
import static com.itgfirm.docengine.util.AbstractTest.TestConstants.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itgfirm.docengine.util.AbstractTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExcelUtilsTest extends AbstractTest {
	private static final Logger LOG = LoggerFactory.getLogger(ExcelUtilsTest.class);

	@Test
	public void a_getWorkbookTest() {
		try (final Workbook wb = getExcelWorkbook(get(TEST_FILE_NAME_READ))) {
			assertNotNull(wb);
			assertEquals(1, wb.getNumberOfSheets());
		} catch (final IOException e) {
			LOG.error(String.format("Problem opening workbook from %s", TEST_FILE_NAME_READ), e);
		}
	}

	@Test
	public void b_getExcelSheetNameFromClassTest() {
		try (final Workbook wb = getExcelWorkbook(get(TEST_FILE_NAME_READ))) {
			final String fromClass = getExcelSheetNameFromClass(TEST_CLASS_CUSTOM_LABELS_ORDERED);
			assertNotNull(fromClass);
			assertEquals(TEST_SHEET_NAME, fromClass);
			final String fromWorkbook = wb.getSheetName(0);
			assertNotNull(fromWorkbook);
			assertEquals(TEST_SHEET_NAME, fromWorkbook);
		} catch (final IOException e) {
			LOG.error(String.format("Problem opening workbook from %s", TEST_FILE_NAME_READ), e);
		}
	}

	@Test
	public void c_getExcelColumnFieldNamesFromRow() {
		try (final Workbook wb = getExcelWorkbook(get(TEST_FILE_NAME_READ))) {
			final Sheet sheet = wb.getSheetAt(0);
			final Iterable<String> fields = getExcelColumnFieldNamesFromRow(TEST_CLASS_CUSTOM_LABELS_ORDERED, sheet.getRow(0));
			assertNotNull(fields);
			final Iterator<String> iter = fields.iterator();
			assertNotNull(iter);
			assertTrue(iter.hasNext());
			int idx = 0;
			while (iter.hasNext()) {
				final String field = iter.next();
				assertEquals(TEST_FIELD_NAMES[idx], field);
				idx++;				
			}
		} catch (final IOException e) {
			LOG.error(String.format("Problem opening workbook from %s", TEST_FILE_NAME_READ), e);
		}
	}
	
	@Test
	public void d_getExcelColumnFieldNamesFromClassTest() {
		Collection<String> fields = (Collection<String>) getExcelColumnFieldNamesFromClass(TEST_CLASS_ANNOTATED);
		assertNotNull(fields);
		assertEquals(4, fields.size());
		int idx = 0;
		for (final String f : fields) {
			assertEquals(TEST_DATA_FIELD_NAMES[idx], f);
			idx++;
		}
		fields.clear();
		assertTrue(fields.isEmpty());
		fields = (Collection<String>) getExcelColumnFieldNamesFromClass(TEST_CLASS_CUSTOM_LABELS_ORDERED);
		assertNotNull(fields);
		assertEquals(4, fields.size());
		idx = 0;
		for (final String f : fields) {
			assertEquals(TEST_DATA_FIELD_NAMES[idx], f);
			idx++;
		}
		fields.clear();
		assertTrue(fields.isEmpty());
		final String[] TEST_FIELD_NAMES_MIXED = { "name", "isTrue", "id", "description" };
		fields.clear();
		assertTrue(fields.isEmpty());
		fields = (Collection<String>) getExcelColumnFieldNamesFromClass(TEST_CLASS_CUSTOM_LABELS_ORDERED_UNORDERED);
		assertNotNull(fields);
		assertEquals(4, fields.size());
		idx = 0;
		for (final String f : fields) {
			assertEquals(TEST_FIELD_NAMES_MIXED[idx], f);
			idx++;
		}
		assertNull(getExcelColumnFieldNamesFromClass(TEST_CLASS_NO_ANNOTATION));
		assertNull(getExcelColumnFieldNamesFromClass(null));
	}

	@Test
	public void e_getExcelColumnFieldsFromClassTest() {
		Collection<Field> fields = (Collection<Field>) getExcelColumnFieldsFromClass(TEST_CLASS_CUSTOM_LABELS_ORDERED);
		assertNotNull(fields);
		assertEquals(4, fields.size());
		int idx = 0;
		for (final Field f : fields) {
			assertEquals(TEST_DATA_FIELD_NAMES[idx], f.getName());
			idx++;
		}
		fields.clear();
		assertTrue(fields.isEmpty());
		fields = (Collection<Field>) getExcelColumnFieldsFromClass(TEST_CLASS_CUSTOM_LABELS_ORDERED_REVERSED);
		assertNotNull(fields);
		assertEquals(4, fields.size());
		idx = 3;
		for (final Field f : fields) {
			assertEquals(TEST_DATA_FIELD_NAMES[idx], f.getName());
			idx--;
		}
		assertNull(getExcelColumnFieldsFromClass(TEST_CLASS_NO_ANNOTATION));
		assertNull(getExcelColumnFieldsFromClass(null));
	}

	@Test
	public void f_getExcelColumnHeaderFromClassTest() {
		Collection<String> labels = (Collection<String>) getExcelColumnHeaderFromClass(TEST_CLASS_ANNOTATED);
		assertNotNull(labels);
		assertEquals(4, labels.size());
		int idx = 0;
		for (final String label : labels) {
			assertEquals(TEST_DATA_FIELD_NAMES[idx], label);
			idx++;
		}
		labels.clear();
		assertTrue(labels.isEmpty());
		labels = (Collection<String>) getExcelColumnHeaderFromClass(TEST_CLASS_CUSTOM_LABELS);
		assertNotNull(labels);
		assertEquals(4, labels.size());
		idx = 0;
		for (final String label : labels) {
			assertEquals(TEST_DATA_FIELD_LABELS[idx], label);
			idx++;
		}
		assertNull(getExcelColumnHeaderFromClass(TEST_CLASS_NO_ANNOTATION));
		assertNull(getExcelColumnHeaderFromClass((Class<?>) null));
	}
	
	@Test
	public void g_getSheetTest() {
		String name = "test";
		Workbook wb = makeWorkbook();
		assertEquals(0, wb.getNumberOfSheets());
		Sheet sheet1 = getExcelSheetFromWorkbook(wb, name);
		assertNotNull(sheet1);
		assertEquals(name, sheet1.getSheetName());
		assertEquals(1, wb.getNumberOfSheets());
		Sheet sheet2 = getExcelSheetFromWorkbook(wb, name);
		assertNotNull(sheet2);
		assertEquals(1, wb.getNumberOfSheets());
		assertEquals(sheet1, sheet2);
		assertNull(getExcelSheetFromWorkbook(null, name));
		assertNull(getExcelSheetFromWorkbook(makeWorkbook(), null));
	}
	
	@Test
	public void h_writeExcelWorkbookToFileTest() {
		Workbook wb = getExcelWorkbook(get(TEST_FILE_NAME_READ));
		File file = get(TEST_FILE_NAME_WRITE);
		if (isNotNullAndExists(file)) {
			delete(file);
		}
		file = create(TEST_FILE_NAME_WRITE);
		writeExcelWorkbookToFile(wb, file);
		assertNotNull(file);
		assertTrue(file.exists());
		assertNotNull(wb);
		assertEquals(1, wb.getNumberOfSheets());
		wb = getExcelWorkbook(file);
		assertNotNull(wb);
		assertEquals(1, wb.getNumberOfSheets());
		file = create("target/test.xlsx");
		writeExcelWorkbookToFile(null, file);
		delete(file);
		writeExcelWorkbookToFile(getExcelWorkbook(get(TEST_FILE_NAME_READ)), null);
	}
	
	private Workbook makeWorkbook() {
		try (final Workbook wb = new XSSFWorkbook()) {
			return wb;
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}