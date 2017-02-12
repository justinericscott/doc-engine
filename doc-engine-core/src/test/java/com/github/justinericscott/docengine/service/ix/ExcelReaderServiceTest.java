package com.github.justinericscott.docengine.service.ix;

import static org.junit.Assert.*;
import static com.github.justinericscott.docengine.service.ix.ExcelUtils.*;
import static com.github.justinericscott.docengine.util.AbstractTest.TestConstants.*;
import static com.github.justinericscott.docengine.util.TestUtils.getSystemTempDirectory;
import static com.github.justinericscott.docengine.util.TestUtils.list;
import static com.github.justinericscott.docengine.util.Utils.delete;
import static com.github.justinericscott.docengine.util.Utils.get;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.justinericscott.docengine.service.ix.ExcelReaderService;
import com.github.justinericscott.docengine.service.ix.ExcelReaderServiceImpl;
import com.github.justinericscott.docengine.service.ix.types.ExampleExcelTypeWithCustomLabelsOrdered;
import com.github.justinericscott.docengine.util.AbstractTest;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExcelReaderServiceTest extends AbstractTest {
	private static final Logger LOG = LoggerFactory.getLogger(ExcelReaderServiceTest.class);
	
	@Autowired
	private ExcelReaderService service;

	@Test
	public void a_getCellValueTest() {
		final ExcelReaderServiceImpl _EXCEL = new ExcelReaderServiceImpl();
		final String expectedString = "test";
		Object object = null;
		Cell cell = getCell();
		cell.setCellValue(expectedString);
		object = _EXCEL.getCellValue(cell);
		assertEquals(String.class, object.getClass());
		assertEquals(expectedString, object.toString());
		final Integer expectedInt = 0;
		cell = getCell();
		cell.setCellValue(expectedInt);
		object = _EXCEL.getCellValue(cell);
		assertEquals(Integer.class, object.getClass());
		final Integer integer = (Integer) object;
		assertEquals(expectedInt.intValue(), integer.intValue());
		final Boolean expectedBoolean = true;
		cell = getCell();
		cell.setCellValue(expectedBoolean);
		object = _EXCEL.getCellValue(cell);
		assertEquals(Boolean.class, object.getClass());
		final Boolean bool = (Boolean) object;
		assertEquals(expectedBoolean, bool.booleanValue());
		final Date expectedDate = new Date();
		cell = getCell();
		cell.setCellValue(DateUtil.getExcelDate(expectedDate));
		object = _EXCEL.getCellValue(cell);
		final Double dub = (Double) object;
		final Date date = DateUtil.getJavaDate(dub);
		assertEquals(expectedDate, date);
		assertNull(_EXCEL.getCellValue(getCell()));
		assertNull(_EXCEL.getCellValue(null));
	}

	@Test
	public void b_getFieldNamesTest() {
		final Row header = createHeaderRow();
		final Collection<String> list = (Collection<String>) getExcelColumnFieldNamesFromRow(TEST_CLASS_CUSTOM_LABELS_ORDERED,
				header);
		assertNotNull(list);
		assertEquals(4, list.size());
		int idx = 0;
		for (final String s : list) {
			assertEquals(TEST_DATA_FIELD_NAMES[idx], s);
			idx++;
		}
		assertNull(getExcelColumnFieldNamesFromRow(null, getRow()));
		assertNull(getExcelColumnFieldNamesFromRow(null, (Row) null));
	}

	@Test
	public void c_createObjectTest() {
		final ExcelReaderServiceImpl _EXCEL = new ExcelReaderServiceImpl();
		final Row row = x_createRealRow();
		final Object o = _EXCEL.createObject(TEST_CLASS_CUSTOM_LABELS_ORDERED, row,
				getExcelColumnFieldNamesFromRow(TEST_CLASS_CUSTOM_LABELS_ORDERED, createHeaderRow()));
		assertNotNull(o);
		assertTrue(TEST_CLASS_CUSTOM_LABELS_ORDERED.isInstance(o));
		final ExampleExcelTypeWithCustomLabelsOrdered ex = (ExampleExcelTypeWithCustomLabelsOrdered) o;
		assertEquals(TEST_VALUES[0], ex.getId());
		assertEquals(TEST_VALUES[1], ex.getName());
		assertEquals(TEST_VALUES[2], ex.getDescription());
		assertEquals(TEST_VALUES[3], ex.getPositive());
	}

	@Test
	public void d_createObjectsTest() {
		final ExcelReaderServiceImpl _EXCEL = new ExcelReaderServiceImpl();
		final Sheet sheet = x_createRealSheet();
		final Collection<?> objects = (Collection<?>) _EXCEL.createObjects(TEST_CLASS_CUSTOM_LABELS_ORDERED, sheet);
		assertNotNull(objects);
		assertEquals(9, objects.size());
		assertNull(_EXCEL.createObjects(null, sheet));
		final Row row = x_createRealRow();
		assertNull(_EXCEL.createObject(null, row,
				getExcelColumnFieldNamesFromRow(TEST_CLASS_CUSTOM_LABELS_ORDERED, createHeaderRow())));
		assertNull(_EXCEL.createObject(TEST_CLASS_CUSTOM_LABELS_ORDERED, null,
				getExcelColumnFieldNamesFromRow(TEST_CLASS_CUSTOM_LABELS_ORDERED, createHeaderRow())));
		assertNull(_EXCEL.createObjects(TEST_CLASS_CUSTOM_LABELS_ORDERED, null));
	}

	@Test
	public void e_readTest() {
		final Collection<?> objects = (Collection<?>) service.read(TEST_CLASS_CUSTOM_LABELS_ORDERED, get(TEST_FILE_NAME_READ));
		assertNotNull(objects);
		assertEquals(100, objects.size());
		assertNull(service.read(TEST_CLASS_CUSTOM_LABELS_ORDERED, null));
		assertNull(service.read((Class<?>) null, get(TEST_FILE_NAME_READ)));
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
		final Row row = getRow();
		int idx = 0;
		for (final String header : TEST_DATA_FIELD_LABELS) {
			row.createCell(idx).setCellValue(header);
			idx++;
		}
		return row;
	}

	private Cell getCell() {
		final Cell cell = getRow().createCell(0);
		cell.setCellType(Cell.CELL_TYPE_STRING);
		cell.setCellValue((String) null); 
		return cell;
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

	private Row x_createRealRow() {
		final Row row = getRow();
		int idx = 0;
		for (final Object o : TEST_VALUES) {
			final Cell cell = row.createCell(idx);
			if (o instanceof Integer) {
				cell.setCellValue((int) o);
			} else if (o instanceof String) {
				cell.setCellValue((String) o);
			} else if (o instanceof Boolean) {
				cell.setCellValue((Boolean) o);
			} else if (o instanceof Long) {
				cell.setCellValue(Double.valueOf(o.toString()));
			} else {
				cell.setCellValue((boolean) Boolean.getBoolean(o.toString()));
			}
			idx++;
		}
		assertEquals(4, row.getPhysicalNumberOfCells());
		return row;
	}

	private Sheet x_createRealSheet() {
		final Sheet sheet = getSheet();
		int rowIdx = 0;
		int colIdx = 0;
		Row row = sheet.createRow(rowIdx);
		for (final String header : TEST_DATA_FIELD_LABELS) {
			row.createCell(colIdx).setCellValue(header);
			colIdx++;
		}
		assertEquals(4, row.getPhysicalNumberOfCells());
		rowIdx++;
		while (rowIdx < 10) {
			row = sheet.createRow(rowIdx);
			colIdx = 0;
			for (final Object o : TEST_VALUES) {
				final Cell cell = row.createCell(colIdx);
				if (o instanceof Integer) {
					final Integer i = (Integer) o;
					cell.setCellValue(i.intValue());
				} else if (o instanceof Long) {
					final Long l = (Long) o;
					cell.setCellValue(l.longValue());
				} else if (o instanceof Float) {
					final Float f = (Float) o;
					cell.setCellValue(f.floatValue());
				} else if (o instanceof Double) {
					final Double d = (Double) o;
					cell.setCellValue(d.doubleValue());
				} else if (o instanceof String) {
					final String s = (String) o;
					cell.setCellValue(s);
				} else if (o instanceof Boolean) {
					final Boolean b = (Boolean) o;
					cell.setCellValue(b.booleanValue());
				} else {
					LOG.debug("Could not determine value type {}.", o.getClass().getName());
					cell.setCellValue(o.toString());
				}
				colIdx++;
			}
			assertEquals(4, row.getPhysicalNumberOfCells());
			rowIdx++;
		}
		assertEquals(10, sheet.getPhysicalNumberOfRows());
		return sheet;
	}
}