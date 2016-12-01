package com.itgfirm.docengine.service;

import static org.junit.Assert.*;
import static com.itgfirm.docengine.util.TestConstants.*;
import static com.itgfirm.docengine.util.Utils.*;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.itgfirm.docengine.service.ExcelReaderServiceImpl;
import com.itgfirm.docengine.types.ExampleExcelTypeWithCustomLabelsOrdered;
import com.itgfirm.docengine.util.AbstractTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExcelReaderServiceTest extends AbstractTest {

	@Test
	public void createObjectsTest() {
		ExcelReaderServiceImpl _EXCEL = new ExcelReaderServiceImpl();
		Sheet sheet = x_createRealSheet();
		Collection<?> objects = (Collection<?>) _EXCEL.createObjects(TEST_CLASS_CUSTOM_LABELS_ORDERED, sheet);
		assertNotNull(objects);
		assertEquals(9, objects.size());
		sheet = x_createRealSheet();
		assertNull(_EXCEL.createObjects(null, sheet));
		Row row = x_createRealRow();
		assertNull(_EXCEL.createObject(null, row,
				_EXCEL.getFieldNames(TEST_CLASS_CUSTOM_LABELS_ORDERED, createHeaderRow())));
		assertNull(_EXCEL.createObject(TEST_CLASS_CUSTOM_LABELS_ORDERED, null,
				_EXCEL.getFieldNames(TEST_CLASS_CUSTOM_LABELS_ORDERED, createHeaderRow())));
		assertNull(_EXCEL.createObjects(TEST_CLASS_CUSTOM_LABELS_ORDERED, null));
	}

	@Test
	public void createObjectTest() {
		ExcelReaderServiceImpl _EXCEL = new ExcelReaderServiceImpl();
		Row row = x_createRealRow();
		Object o = _EXCEL.createObject(TEST_CLASS_CUSTOM_LABELS_ORDERED, row,
				_EXCEL.getFieldNames(TEST_CLASS_CUSTOM_LABELS_ORDERED, createHeaderRow()));
		assertNotNull(o);
		assertTrue(TEST_CLASS_CUSTOM_LABELS_ORDERED.isInstance(o));
		ExampleExcelTypeWithCustomLabelsOrdered ex = (ExampleExcelTypeWithCustomLabelsOrdered) o;
		assertEquals(TEST_VALUES[0], ex.getId());
		assertEquals(TEST_VALUES[1], ex.getName());
		assertEquals(TEST_VALUES[2], ex.getDescription());
		assertEquals(TEST_VALUES[3], ex.isPositive());
	}

	@Test
	public void getCellValueTest() {
		ExcelReaderServiceImpl _EXCEL = new ExcelReaderServiceImpl();
		// string ...
		String string = "test";
		Cell cell = getCell();
		cell.setCellValue(string);
		assertEquals(string, _EXCEL.getCellValue(cell));
		// integer ...
		Integer integer = 0;
		cell = getCell();
		cell.setCellValue(integer);
		assertEquals(integer, ((Double) _EXCEL.getCellValue(cell)).intValue(), 0);
		// boolean ...
		boolean bool = true;
		cell = getCell();
		cell.setCellValue(bool);
		assertEquals(bool, _EXCEL.getCellValue(cell));
		// date ...
		Date date = new Date();
		cell = getCell();
		cell.setCellValue(DateUtil.getExcelDate(date));
		assertEquals(date, DateUtil.getJavaDate((double) _EXCEL.getCellValue(cell)));
		assertNull(_EXCEL.getCellValue(getCell()));
		assertNull(_EXCEL.getCellValue(null));
	}

	@Test
	public void getFieldNamesTest() {
		ExcelReaderServiceImpl _EXCEL = new ExcelReaderServiceImpl();
		Row header = createHeaderRow();
		Collection<String> list = (Collection<String>) _EXCEL.getFieldNames(TEST_CLASS_CUSTOM_LABELS_ORDERED, header);
		assertNotNull(list);
		assertEquals(4, list.size());
		int idx = 0;
		for (String s : list) {
			assertEquals(TEST_DATA_FIELD_NAMES[idx], s);
			idx++;
		}
		assertNull(_EXCEL.getFieldNames(null, getRow()));
		assertNull(_EXCEL.getFieldNames(null, (Row) null));
	}

	@Test
	public void readTest() {
		ExcelReaderServiceImpl _EXCEL = new ExcelReaderServiceImpl();
		Collection<?> objects = (Collection<?>) _EXCEL.read(TEST_CLASS_CUSTOM_LABELS_ORDERED, get(TARGET_FILE_NAME_READ));
		assertNotNull(objects);
		assertEquals(100, objects.size());
		assertNull(_EXCEL.read(TEST_CLASS_CUSTOM_LABELS_ORDERED, null));
		assertNull(_EXCEL.read(null, get(TARGET_FILE_NAME_READ)));
	}

	@Test
	public void x_createTestDataTest() {
		assertNotNull(TEST_DATA);
		assertEquals(100L, TEST_DATA.size());
		int idx = 0;
		for (ExampleExcelTypeWithCustomLabelsOrdered e : TEST_DATA) {
			assertEquals(idx, e.getId());
			idx++;
		}
	}

	@After
	public void x_clearService() {
		cleanup();
	}

	@BeforeClass
	public static void x_clearTemporaryDirectory() {
		cleanup();
	}

	private static void cleanup() {
		final File temp = getSystemTempDirectory();
		final Collection<File> contents = (Collection<File>) list(temp, null, false);
		for (final File f : contents) {
			delete(f);
		}
	}
	
	private Row createHeaderRow() {
		Row row = getRow();
		int idx = 0;
		for (String header : TEST_DATA_FIELD_LABELS) {
			row.createCell(idx).setCellValue(header);
			idx++;
		}
		return row;
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

	private Row x_createRealRow() {
		Row row = getRow();
		int idx = 0;
		for (Object o : TEST_VALUES) {
			Cell cell = row.createCell(idx);
			if (o instanceof Integer) {
				cell.setCellValue((int) o);
			} else if (o instanceof String) {
				cell.setCellValue((String) o);
			} else if (o instanceof Boolean) {
				cell.setCellValue((boolean) o);
			} else if (o instanceof Long) {
				cell.setCellValue(Double.valueOf(o.toString())); 
			} else {
				cell.setCellValue(Double.class.cast(o));
			}
			idx++;
		}
		assertEquals(4, row.getPhysicalNumberOfCells());
		return row;
	}

	private Sheet x_createRealSheet() {
		Sheet sheet = getSheet();
		int idxs = 0;
		int idxr = 0;
		Row row = sheet.createRow(idxs);
		for (String header : TEST_DATA_FIELD_LABELS) {
			row.createCell(idxr).setCellValue(header);
			idxr++;
		}
		assertEquals(4, row.getPhysicalNumberOfCells());
		idxs++;
		while (idxs < 10) {
			row = sheet.createRow(idxs);
			idxr = 0;
			for (Object o : TEST_VALUES) {
				Cell cell = row.createCell(idxr);
				if (o instanceof Integer) {
					cell.setCellValue((int) o);
				} else if (o instanceof String) {
					cell.setCellValue((String) o);
				} else if (o instanceof Boolean) {
					cell.setCellValue((boolean) o);
				} else if (o instanceof Long) {
					cell.setCellValue(Double.valueOf(o.toString())); 
				} else {
					cell.setCellValue(Double.class.cast(o));
				}
				idxr++;
			}
			assertEquals(4, row.getPhysicalNumberOfCells());
			idxs++;
		}
		assertEquals(10, sheet.getPhysicalNumberOfRows());
		return sheet;
	}
}