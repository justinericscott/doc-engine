package com.itgfirm.docengine.service;

import static org.junit.Assert.*;
import static com.itgfirm.docengine.util.TestConstants.*;
import static com.itgfirm.docengine.util.Utils.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.itgfirm.docengine.service.ExcelWriterService;
import com.itgfirm.docengine.service.ExcelWriterServiceImpl;
import com.itgfirm.docengine.types.ExampleExcelType;
import com.itgfirm.docengine.types.ExampleExcelTypeWithCustomLabelsOrdered;
import com.itgfirm.docengine.util.AbstractTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExcelWriterServiceTest extends AbstractTest {

	@Autowired
	private ExcelWriterService service;

	@Test
	public void fillCellTest() {
		ExcelWriterServiceImpl _EXCEL = new ExcelWriterServiceImpl();
		Cell cell = getCell();
		String string = "test";
		_EXCEL.fillCell(cell, string);
		assertEquals(string, cell.getStringCellValue());
		boolean bool = true;
		_EXCEL.fillCell(cell, bool);
		assertEquals(bool, cell.getBooleanCellValue());
		int num = 0;
		_EXCEL.fillCell(cell, num);
		assertEquals(num, cell.getNumericCellValue(), 0);
		Date date = new Date(Calendar.getInstance().getTimeInMillis());
		_EXCEL.fillCell(cell, date);
		assertEquals(date, cell.getDateCellValue());
		cell = getCell();
		_EXCEL.fillCell(cell, null);
		assertEquals("", cell.getStringCellValue());
		_EXCEL.fillCell(null, "test");
	}

	@Test
	public void fillHeaderTest() {
		ExcelWriterServiceImpl _EXCEL = new ExcelWriterServiceImpl();
		Row row = getRow();
		_EXCEL.fillHeader(row, TEST_CLASS_CUSTOM_LABELS_ORDERED);
		assertEquals(4, row.getPhysicalNumberOfCells());
		assertEquals(TEST_DATA_FIELD_LABELS[0], row.getCell(0).getStringCellValue());
		assertEquals(TEST_DATA_FIELD_LABELS[1], row.getCell(1).getStringCellValue());
		assertEquals(TEST_DATA_FIELD_LABELS[2], row.getCell(2).getStringCellValue());
		assertEquals(TEST_DATA_FIELD_LABELS[3], row.getCell(3).getStringCellValue());
		row = getRow();
		_EXCEL.fillHeader(row, null);
		assertEquals(0, row.getPhysicalNumberOfCells());
		_EXCEL.fillHeader(null, TEST_CLASS_CUSTOM_LABELS_ORDERED);
	}

	@Test
	public void fillRowTest() {
		ExcelWriterServiceImpl _EXCEL = new ExcelWriterServiceImpl();
		Row row = getRow();
		ExampleExcelTypeWithCustomLabelsOrdered example = new ExampleExcelTypeWithCustomLabelsOrdered(1, "name",
				"description", true);
		_EXCEL.fillRow(row, TEST_CLASS_CUSTOM_LABELS_ORDERED, example);
		assertEquals(4, row.getPhysicalNumberOfCells());
		assertEquals(1, row.getCell(0).getNumericCellValue(), 0);
		assertEquals("name", row.getCell(1).getStringCellValue());
		assertEquals("description", row.getCell(2).getStringCellValue());
		assertEquals(true, row.getCell(3).getBooleanCellValue());
		example = new ExampleExcelTypeWithCustomLabelsOrdered(1, "name", "description", true);
		_EXCEL.fillRow(null, TEST_CLASS_CUSTOM_LABELS_ORDERED, example);
		row = getRow();
		_EXCEL.fillRow(row, TEST_CLASS_CUSTOM_LABELS_ORDERED, null);
		assertEquals(0, row.getPhysicalNumberOfCells());
		row = getRow();
		example = new ExampleExcelTypeWithCustomLabelsOrdered(1, "name", "description", true);
		_EXCEL.fillRow(row, null, example);
		assertEquals(0, row.getPhysicalNumberOfCells());
	}

	@Test
	public void fillSheetTest() {
		ExcelWriterServiceImpl _EXCEL = new ExcelWriterServiceImpl();
		Sheet sheet = getSheet();
		_EXCEL.fillSheet(sheet, TEST_CLASS_CUSTOM_LABELS_ORDERED, TEST_DATA, true);
		int idx = 1;
		Row row = null;
		for (ExampleExcelTypeWithCustomLabelsOrdered o : TEST_DATA) {
			row = sheet.getRow(idx);
			assertEquals(4, row.getPhysicalNumberOfCells());
			assertEquals(o.getId(), row.getCell(0).getNumericCellValue(), 0);
			assertEquals(o.getName(), row.getCell(1).getStringCellValue());
			assertEquals(o.getDescription(), row.getCell(2).getStringCellValue());
			assertEquals(o.isPositive(), row.getCell(3).getBooleanCellValue());
			idx++;
		}
		sheet = getSheet();
		_EXCEL.fillSheet(sheet, TEST_CLASS_CUSTOM_LABELS_ORDERED, null, true);
		for (int i = 0; i <= TEST_DATA.size(); i++) {
			assertNull(sheet.getRow(i));
		}
		sheet = getSheet();
		_EXCEL.fillSheet(sheet, null, TEST_DATA, true);
		for (int i = 0; i <= TEST_DATA.size(); i++) {
			assertNull(sheet.getRow(i));
		}
		_EXCEL.fillSheet(null, TEST_CLASS_CUSTOM_LABELS_ORDERED, TEST_DATA, true);
	}

	@Test
	public void getFieldNamesTest() {
		ExcelWriterServiceImpl _EXCEL = new ExcelWriterServiceImpl();
		Collection<String> fields = (Collection<String>) _EXCEL.getFieldNames(TEST_CLASS_ANNOTATED);
		assertNotNull(fields);
		assertEquals(4, fields.size());
		int idx = 0;
		for (String f : fields) {
			assertEquals(TEST_DATA_FIELD_NAMES[idx], f);
			idx++;
		}
		fields.clear();
		assertTrue(fields.isEmpty());
		fields = (Collection<String>) _EXCEL.getFieldNames(TEST_CLASS_CUSTOM_LABELS_ORDERED);
		assertNotNull(fields);
		assertEquals(4, fields.size());
		idx = 0;
		for (String f : fields) {
			assertEquals(TEST_DATA_FIELD_NAMES[idx], f);
			idx++;
		}
		fields.clear();
		assertTrue(fields.isEmpty());
		final String[] TEST_FIELD_NAMES_MIXED = { "name", "isTrue", "id", "description" };
		fields.clear();
		assertTrue(fields.isEmpty());
		fields = (Collection<String>) _EXCEL.getFieldNames(TEST_CLASS_CUSTOM_LABELS_ORDERED_UNORDERED);
		assertNotNull(fields);
		assertEquals(4, fields.size());
		idx = 0;
		for (String f : fields) {
			assertEquals(TEST_FIELD_NAMES_MIXED[idx], f);
			idx++;
		}
		assertNull(_EXCEL.getFieldNames(TEST_CLASS_NO_ANNOTATION));
		assertNull(_EXCEL.getFieldNames(null));
	}

	@Test
	public void getFieldsTest() {
		ExcelWriterServiceImpl _EXCEL = new ExcelWriterServiceImpl();
		Collection<Field> fields = (Collection<Field>) _EXCEL.getFields(TEST_CLASS_CUSTOM_LABELS_ORDERED);
		assertNotNull(fields);
		assertEquals(4, fields.size());
		int idx = 0;
		for (Field f : fields) {
			assertEquals(TEST_DATA_FIELD_NAMES[idx], f.getName());
			idx++;
		}
		fields.clear();
		assertTrue(fields.isEmpty());
		fields = (Collection<Field>) _EXCEL.getFields(TEST_CLASS_CUSTOM_LABELS_ORDERED_REVERSED);
		assertNotNull(fields);
		assertEquals(4, fields.size());
		idx = 3;
		for (Field f : fields) {
			assertEquals(TEST_DATA_FIELD_NAMES[idx], f.getName());
			idx--;
		}
		assertNull(_EXCEL.getFields(TEST_CLASS_NO_ANNOTATION));
		assertNull(_EXCEL.getFields(null));
	}

	@Test
	public void getHeaderFromClassTest() {
		ExcelWriterServiceImpl _EXCEL = new ExcelWriterServiceImpl();
		Collection<String> labels = (Collection<String>) _EXCEL.getHeader(TEST_CLASS_ANNOTATED);
		assertNotNull(labels);
		assertEquals(4, labels.size());
		int idx = 0;
		for (String label : labels) {
			assertEquals(TEST_DATA_FIELD_NAMES[idx], label);
			idx++;
		}
		labels.clear();
		assertTrue(labels.isEmpty());
		labels = (Collection<String>) _EXCEL.getHeader(TEST_CLASS_CUSTOM_LABELS);
		assertNotNull(labels);
		assertEquals(4, labels.size());
		idx = 0;
		for (String label : labels) {
			assertEquals(TEST_DATA_FIELD_LABELS[idx], label);
			idx++;
		}
		assertNull(_EXCEL.getHeader(TEST_CLASS_NO_ANNOTATION));
		assertNull(_EXCEL.getHeader((Class<?>) null));
	}

	@Test
	public void getSheetTest() {
		String name = "test";
		ExcelWriterServiceImpl _EXCEL = new ExcelWriterServiceImpl();
		Workbook wb = makeWorkbook();
		assertEquals(0, wb.getNumberOfSheets());
		Sheet sheet1 = _EXCEL.getSheet(wb, name);
		assertNotNull(sheet1);
		assertEquals(name, sheet1.getSheetName());
		assertEquals(1, wb.getNumberOfSheets());
		Sheet sheet2 = _EXCEL.getSheet(wb, name);
		assertNotNull(sheet2);
		assertEquals(1, wb.getNumberOfSheets());
		assertEquals(sheet1, sheet2);
		assertNull(_EXCEL.getSheet(null, name));
		assertNull(_EXCEL.getSheet(makeWorkbook(), null));
	}

	@Test
	public void insertRowTest() {
		ExcelWriterServiceImpl _EXCEL = new ExcelWriterServiceImpl();
		Sheet sheet = getSheet();
		_EXCEL.insertRow(sheet, TEST_CLASS_CUSTOM_LABELS_ORDERED, null, 0, true);
		Row row = sheet.getRow(0);
		assertEquals(4, row.getPhysicalNumberOfCells());
		assertEquals(TEST_DATA_FIELD_LABELS[0], row.getCell(0).getStringCellValue());
		assertEquals(TEST_DATA_FIELD_LABELS[1], row.getCell(1).getStringCellValue());
		assertEquals(TEST_DATA_FIELD_LABELS[2], row.getCell(2).getStringCellValue());
		assertEquals(TEST_DATA_FIELD_LABELS[3], row.getCell(3).getStringCellValue());
		ExampleExcelTypeWithCustomLabelsOrdered example = new ExampleExcelTypeWithCustomLabelsOrdered(1, "name",
				"description", true);
		_EXCEL.insertRow(sheet, TEST_CLASS_CUSTOM_LABELS_ORDERED, example, 1, false);
		row = sheet.getRow(1);
		assertEquals(4, row.getPhysicalNumberOfCells());
		assertEquals(1, row.getCell(0).getNumericCellValue(), 0);
		assertEquals("name", row.getCell(1).getStringCellValue());
		assertEquals("description", row.getCell(2).getStringCellValue());
		assertEquals(true, row.getCell(3).getBooleanCellValue());
		sheet = getSheet();
		_EXCEL.insertRow(sheet, null, null, 0, false);
		row = sheet.getRow(0);
		assertNull(row);
		example = new ExampleExcelTypeWithCustomLabelsOrdered(1, "name", "description", true);
		_EXCEL.insertRow(sheet, null, example, 1, false);
		row = sheet.getRow(1);
		assertNull(row);
		sheet = getSheet();
		_EXCEL.insertRow(sheet, TEST_CLASS_CUSTOM_LABELS_ORDERED, null, 0, true);
		row = sheet.getRow(0);
		assertEquals(4, row.getPhysicalNumberOfCells());
		_EXCEL.insertRow(sheet, TEST_CLASS_CUSTOM_LABELS_ORDERED, null, 1, false);
		row = sheet.getRow(1);
		assertEquals(0, row.getPhysicalNumberOfCells());
		_EXCEL.insertRow(null, TEST_CLASS_CUSTOM_LABELS_ORDERED, null, 0, false);
		example = new ExampleExcelTypeWithCustomLabelsOrdered(1, "name", "description", true);
		_EXCEL.insertRow(null, TEST_CLASS_CUSTOM_LABELS_ORDERED, example, 1, false);
	}

	@Test
	public void writeFileTest() {
		Workbook wb = getWorkbook(get(TARGET_FILE_NAME_READ));
		File file = get(TARGET_FILE_NAME_WRITE);
		if (isNotNullAndExists(file)) {
			delete(file);
		}
		file = create(TARGET_FILE_NAME_WRITE);
		ExcelWriterServiceImpl _EXCEL = new ExcelWriterServiceImpl();
		_EXCEL.writeFile(wb, file);
		assertNotNull(file);
		assertTrue(file.exists());
		assertNotNull(wb);
		assertEquals(1, wb.getNumberOfSheets());
		wb = getWorkbook(file);
		assertNotNull(wb);
		assertEquals(1, wb.getNumberOfSheets());
		file = create("target/test.xlsx");
		_EXCEL = new ExcelWriterServiceImpl();
		_EXCEL.writeFile(null, file);
		delete(file);
		_EXCEL.writeFile(getWorkbook(get(TARGET_FILE_NAME_READ)), null);
	}

	@Test
	public void writeTest() {
		delete(get(TARGET_FILE_NAME_WRITE));
		File file = create(TARGET_FILE_NAME_WRITE);
		assertEquals(0, getSize(file));

		file = service.write(TEST_CLASS_CUSTOM_LABELS_ORDERED, file, TEST_DATA, true);
		assertNotNull(file);
		assertTrue(file.exists());
		assertEquals(5908, getSize(file), 2);

		File copy = copy(file);
		List<ExampleExcelTypeWithCustomLabelsOrdered> list = new ArrayList<ExampleExcelTypeWithCustomLabelsOrdered>(
				TEST_DATA.size());
		for (ExampleExcelTypeWithCustomLabelsOrdered e : TEST_DATA) {
			ExampleExcelTypeWithCustomLabelsOrdered ex = new ExampleExcelTypeWithCustomLabelsOrdered(e);
			ex.setName(e.getName() + " - EDITED");
			list.add(ex);
		}
		copy = service.write(TEST_CLASS_CUSTOM_LABELS_ORDERED, copy, list, true);
		assertNotNull(copy);
		assertTrue(copy.exists());
		assertEquals(6318, getSize(copy), 2);
		delete(get(TARGET_FILE_NAME_WRITE));
		file = create(TARGET_FILE_NAME_WRITE);
		assertEquals(0, getSize(file));
		assertNull(service.write(null, file, TEST_DATA, true));
		assertEquals(0, getSize(file));
		assertNull(service.write(TEST_CLASS_CUSTOM_LABELS_ORDERED, file, null, true));
		assertNull(service.write(TEST_CLASS_CUSTOM_LABELS_ORDERED, null, TEST_DATA, true));
	}

	@Test
	public void writeTestSameFileDifferentClassesTest() {
		File file = create(TARGET_FILE_NAME_WRITE);
		assertEquals(0, getSize(file));

		file = service.write(TEST_CLASS_CUSTOM_LABELS_ORDERED, file, TEST_DATA, true);
		assertNotNull(file);
		assertTrue(file.exists());
		assertEquals(5908, getSize(file), 2);

		Collection<Object> list = new ArrayList<Object>();
		int idx = 0;
		while (idx < TEST_DATA_LIMIT) {
			list.add(new ExampleExcelType(idx, "Name - " + idx, "Description - " + idx, true));
			idx++;
		}
		file = service.write(ExampleExcelType.class, file, list, true);
		assertNotNull(file);
		assertTrue(file.exists());
		assertEquals(8861, getSize(file), 2);
	}

	/* x_createTestData test - DONE */

	@After
	public void x_clearService() {
		cleanup();
	}

	@BeforeClass
	public static void x_clearTemporaryDirectory() {
		delete(get("target/Copy of - test-data-write.xlsx"));
		cleanup();
	}

	private static void cleanup() {
		final File temp = getSystemTempDirectory();
		final Collection<File> contents = (Collection<File>) list(temp, null, false);
		for (final File f : contents) {
			delete(f);
		}
	}

	private Cell getCell() {
		return getRow().createCell(0);
	}

	private Row getRow() {
		return getSheet().createRow(0);
	}

	private Sheet getSheet() {
		return makeWorkbook().createSheet("test");
	}

	private Workbook makeWorkbook() {
		try (Workbook wb = new XSSFWorkbook()) {
			return wb;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}