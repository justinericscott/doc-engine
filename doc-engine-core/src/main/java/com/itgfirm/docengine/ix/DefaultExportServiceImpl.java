package com.itgfirm.docengine.ix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.WordUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Service
public class DefaultExportServiceImpl extends AbstractIXService implements ExportService {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultExportServiceImpl.class);

	@Override
	public File export(List<?> objects, Class<?> clazz, String path) {
		Workbook wb = new XSSFWorkbook();
		fillSheet(wb.createSheet(getTableName(clazz)), objects, clazz);
		return createFile(wb, path);
	}

	@Override
	public File export(Iterable<?> objects, Class<?> clazz, String path) {
		List<Object> list = new ArrayList<Object>();
		objects.forEach(list::add);
		return export(list, clazz, path);
	}

	private File createFile(final Workbook wb, final String path) {
		final File file = new File(path);
		try (final FileOutputStream out = new FileOutputStream(file)) {
			wb.write(out);
		} catch (final IOException e) {
			LOG.error("Problem Creating File: " + path, e);
		}
		if (file.exists()) {
			return file;
		}
		return null;
	}

	private Map<String, Integer> determineHeader(Row header, Map<String, String> elements) {
		Map<String, Integer> cellMap = new HashMap<String, Integer>(elements.size());
		int idx = 0;
		for (String field : elements.keySet()) {
			Cell cell = header.createCell(idx);
			cell.setCellValue(elements.get(field));
			cellMap.put(field, idx);
			idx++;
		}
		return cellMap;
	}

	private void fillCell(Cell cell, Object result) {
		if (result instanceof String) {
			cell.setCellValue(String.valueOf(result));
		} else if (result instanceof Boolean) {
			cell.setCellValue(Boolean.valueOf(String.valueOf(result)));
		} else if (result instanceof Integer) {
			cell.setCellValue(Integer.valueOf(String.valueOf(result)));
		} else if (result instanceof Long) {
			cell.setCellValue(Integer.valueOf(String.valueOf(result)));
		} else if (result instanceof Date) {
			cell.setCellValue(Date.valueOf(String.valueOf(result)));
		} else {
			String val = String.valueOf(result);
			if (val != null && !val.isEmpty()) {
				cell.setCellValue(val);
			}
		}
	}

	private void fillRow(Row row, Object obj, Map<String, String> elements,
			Map<String, Integer> cellMap, Class<?> clazz) {
		for (String field : elements.keySet()) {
			Method method =
					ReflectionUtils.findMethod(clazz, "get" + WordUtils.capitalize(field));
			if (method != null) {
				Object result = ReflectionUtils.invokeMethod(method, obj);
				if (result != null) {
					fillCell(row.createCell(cellMap.get(field)), result);
				}
			}
		}
	}

	private void fillSheet(Sheet sheet, List<?> objects, Class<?> clazz) {
		Map<String, String> elements = getElements(clazz);
		Map<String, Integer> cellMap = determineHeader(sheet.createRow(0), elements);
		int r = 1;
		for (Object obj : objects) {
			insertRow(sheet, obj, elements, cellMap, clazz, r);
			r++;
		}
	}

	private void insertRow(Sheet sheet, Object obj, Map<String, String> elements,
			Map<String, Integer> cellMap, Class<?> clazz, int index) {
		Row row = sheet.createRow(index);
		fillRow(row, obj, elements, cellMap, clazz);
	}
}
