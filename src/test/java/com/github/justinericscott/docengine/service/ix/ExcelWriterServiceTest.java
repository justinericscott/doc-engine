package com.github.justinericscott.docengine.service.ix;

import static org.junit.Assert.*;

import static com.github.justinericscott.docengine.util.TestUtils.TestConstants.*;
import static com.github.justinericscott.docengine.util.TestUtils.list;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
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

import com.github.justinericscott.docengine.service.ix.ExcelWriterService;
import com.github.justinericscott.docengine.service.ix.ExcelWriterServiceImpl;
import com.github.justinericscott.docengine.service.ix.types.ExampleExcelType;
import com.github.justinericscott.docengine.service.ix.types.ExampleExcelTypeWithCustomLabelsOrdered;
import com.github.justinericscott.docengine.util.AbstractTest;
import com.github.justinericscott.docengine.util.Utils;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExcelWriterServiceTest extends AbstractTest {

	@Autowired
	private ExcelWriterService service;

	private final ExcelWriterServiceImpl excel = new ExcelWriterServiceImpl();

	@Test
	public void a_fillCellTest() {
		final Cell cell = getCell();
		String string = "test";
		excel.fillCell(cell, string);
		assertEquals(string, cell.getStringCellValue());
		boolean bool = true;
		excel.fillCell(cell, bool);
		assertEquals(bool, cell.getBooleanCellValue());
		int num = 0;
		excel.fillCell(cell, num);
		assertEquals(num, cell.getNumericCellValue(), 0);
		Date date = new Date(Calendar.getInstance().getTimeInMillis());
		excel.fillCell(cell, date);
		assertEquals(date, cell.getDateCellValue());
		cell.setCellType(Cell.CELL_TYPE_STRING);
		excel.fillCell(cell, (String) null);
		assertEquals("", cell.getStringCellValue());
		excel.fillCell(null, "test");
	}

	@Test
	public void b_fillHeaderTest() {
		final Row row = getRow();
		excel.fillHeader(row, TEST_CLASS_CUSTOM_LABELS_ORDERED);
		assertEquals(4, row.getPhysicalNumberOfCells());
		assertEquals(TEST_FIELD_LABELS[0], row.getCell(0).getStringCellValue());
		assertEquals(TEST_FIELD_LABELS[1], row.getCell(1).getStringCellValue());
		assertEquals(TEST_FIELD_LABELS[2], row.getCell(2).getStringCellValue());
		assertEquals(TEST_FIELD_LABELS[3], row.getCell(3).getStringCellValue());
		final Iterator<Cell> iter = row.iterator();
		while (iter.hasNext()) {
			iter.next();
			iter.remove();
		}
		excel.fillHeader(row, null);
		assertEquals(0, row.getPhysicalNumberOfCells());
		excel.fillHeader(null, TEST_CLASS_CUSTOM_LABELS_ORDERED);
	}

	@Test
	public void c_fillRowTest() {
		final Row row = getRow();
		final ExampleExcelTypeWithCustomLabelsOrdered example = new ExampleExcelTypeWithCustomLabelsOrdered(1, "name",
				"description", new Boolean(true));
		excel.fillRow(row, TEST_CLASS_CUSTOM_LABELS_ORDERED, example);
		assertEquals(4, row.getPhysicalNumberOfCells());
		assertEquals(1, (int) row.getCell(0).getNumericCellValue());
		assertEquals("name", row.getCell(1).getStringCellValue());
		assertEquals("description", row.getCell(2).getStringCellValue());
		assertEquals(new Boolean(true), row.getCell(3).getBooleanCellValue());
		excel.fillRow(null, TEST_CLASS_CUSTOM_LABELS_ORDERED, example);
		final Iterator<Cell> cells = row.iterator();
		cells.forEachRemaining(cell -> {
			cells.remove();
		});
		excel.fillRow(row, TEST_CLASS_CUSTOM_LABELS_ORDERED, null);
		assertEquals(0, row.getPhysicalNumberOfCells());
		final Iterator<Cell> iter = row.cellIterator();
		iter.forEachRemaining(cell -> {
			iter.remove();
		});
		excel.fillRow(row, null, example);
		assertEquals(0, row.getPhysicalNumberOfCells());
	}

	@Test
	public void d_insertRowTest() {
		final Sheet sheet = getSheet();
		excel.insertRow(sheet, TEST_CLASS_CUSTOM_LABELS_ORDERED, null, 0, true);
		Row row = sheet.getRow(0);
		assertEquals(4, row.getPhysicalNumberOfCells());
		assertEquals(TEST_FIELD_LABELS[0], row.getCell(0).getStringCellValue());
		assertEquals(TEST_FIELD_LABELS[1], row.getCell(1).getStringCellValue());
		assertEquals(TEST_FIELD_LABELS[2], row.getCell(2).getStringCellValue());
		assertEquals(TEST_FIELD_LABELS[3], row.getCell(3).getStringCellValue());
		final ExampleExcelTypeWithCustomLabelsOrdered example = new ExampleExcelTypeWithCustomLabelsOrdered(1, "name",
				"description", new Boolean(true));
		excel.insertRow(sheet, TEST_CLASS_CUSTOM_LABELS_ORDERED, example, 1, false);
		row = sheet.getRow(1);
		assertEquals(4, row.getPhysicalNumberOfCells());
		assertEquals(1, row.getCell(0).getNumericCellValue(), 0);
		assertEquals("name", row.getCell(1).getStringCellValue());
		assertEquals("description", row.getCell(2).getStringCellValue());
		assertEquals(true, row.getCell(3).getBooleanCellValue());
		final Iterator<Row> rows = sheet.rowIterator();
		rows.forEachRemaining(r -> {
			rows.remove();
		});
		excel.insertRow(sheet, null, null, 0, false);
		row = sheet.getRow(0);
		assertNull(row);
		excel.insertRow(sheet, null, example, 1, false);
		row = sheet.getRow(1);
		assertNull(row);
		final Iterator<Row> rs = sheet.rowIterator();
		rs.forEachRemaining(r -> {
			rs.remove();
		});
		excel.insertRow(sheet, TEST_CLASS_CUSTOM_LABELS_ORDERED, null, 0, true);
		row = sheet.getRow(0);
		assertEquals(4, row.getPhysicalNumberOfCells());
		excel.insertRow(sheet, TEST_CLASS_CUSTOM_LABELS_ORDERED, null, 1, false);
		row = sheet.getRow(1);
		assertEquals(0, row.getPhysicalNumberOfCells());
		excel.insertRow(null, TEST_CLASS_CUSTOM_LABELS_ORDERED, null, 0, false);
		excel.insertRow(null, TEST_CLASS_CUSTOM_LABELS_ORDERED, example, 1, false);
	}

	@Test
	public void e_fillSheetTest() {
		final Sheet sheet = getSheet();
		excel.fillSheet(sheet, TEST_CLASS_CUSTOM_LABELS_ORDERED, TEST_DATA, new Boolean(true));
		int idx = 1;
		for (final ExampleExcelTypeWithCustomLabelsOrdered o : TEST_DATA) {
			final Row row = sheet.getRow(idx);
			assertEquals(4, row.getPhysicalNumberOfCells());
			assertEquals(o.getId(), row.getCell(0).getNumericCellValue(), 0);
			assertEquals(o.getName(), row.getCell(1).getStringCellValue());
			assertEquals(o.getDescription(), row.getCell(2).getStringCellValue());
			assertEquals(o.getPositive(), row.getCell(3).getBooleanCellValue());
			idx++;
		}
		final Iterator<Row> rows = sheet.rowIterator();
		rows.forEachRemaining(r -> {
			rows.remove();
		});
		excel.fillSheet(sheet, TEST_CLASS_CUSTOM_LABELS_ORDERED, null, true);
		for (int i = 0; i <= TEST_DATA.size(); i++) {
			assertNull(sheet.getRow(i));
		}
		final Iterator<Row> rs = sheet.rowIterator();
		rs.forEachRemaining(r -> {
			rs.remove();
		});
		excel.fillSheet(sheet, null, TEST_DATA, true);
		for (int i = 0; i <= TEST_DATA.size(); i++) {
			assertNull(sheet.getRow(i));
		}
		excel.fillSheet(null, TEST_CLASS_CUSTOM_LABELS_ORDERED, TEST_DATA, true);
	}

	@Test
	public void f_writeTest() {
		Utils.delete(Utils.get(TEST_FILE_NAME_WRITE));
		File file = Utils.create(TEST_FILE_NAME_WRITE);
		assertEquals(0, Utils.getSize(file));
		file = service.write(TEST_CLASS_CUSTOM_LABELS_ORDERED, file, TEST_DATA, true);
		assertNotNull(file);
		assertTrue(file.exists());
		File copy = Utils.copy(file);
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
		Utils.delete(Utils.get(TEST_FILE_NAME_WRITE));
		file = Utils.create(TEST_FILE_NAME_WRITE);
		assertNull(service.write(null, file, TEST_DATA, true));
		assertNull(service.write(TEST_CLASS_CUSTOM_LABELS_ORDERED, file, null, true));
		assertNull(service.write(TEST_CLASS_CUSTOM_LABELS_ORDERED, null, TEST_DATA, true));
	}

	@Test
	public void g_writeTestSameFileDifferentClassesTest() {
		File file = Utils.create(TEST_FILE_NAME_WRITE);
		file = service.write(TEST_CLASS_CUSTOM_LABELS_ORDERED, file, TEST_DATA, true);
		assertNotNull(file);
		assertTrue(file.exists());
		final Collection<Object> list = new ArrayList<Object>();
		int idx = 0;
		while (idx < TEST_DATA_LIMIT) {
			list.add(new ExampleExcelType(idx, "Name - " + idx, "Description - " + idx, true));
			idx++;
		}
		file = service.write(ExampleExcelType.class, file, list, true);
		assertNotNull(file);
		assertTrue(file.exists());
	}

	@After
	public void x_clearService() {
		cleanup();
	}

	@BeforeClass
	public static void x_clearTemporaryDirectory() {
		Utils.delete(Utils.get("target/Copy of - test-data-write.xlsx"));
		cleanup();
	}

	private static void cleanup() {
		final File temp = Utils.getSystemTempDirectory();
		final Collection<File> contents = (Collection<File>) list(temp, null, false);
		for (final File f : contents) {
			Utils.delete(f);
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
		try (final Workbook wb = new XSSFWorkbook()) {
			return wb;
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}