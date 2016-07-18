/**
 * TODO: License
 */
package com.itgfirm.docengine.ix;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.persistence.Column;

import org.apache.commons.lang.WordUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.itgfirm.docengine.util.Utils;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Service
public class DefaultImportServiceImpl extends AbstractIXService implements ImportService {
	private static final Logger LOG = LogManager.getLogger(DefaultImportServiceImpl.class);

	@Override
	public List<Object> importFromFile(File file, Class<?> clazz) {
		List<Object> objects = null;
		Workbook wb = readFile(file);
		if (wb.getNumberOfSheets() > 0) {
			Sheet sheet = wb.getSheet(getTableName(clazz));
			if (sheet != null) {
				objects = new ArrayList<Object>();
				Map<Integer, Method> map = getMethodMap(sheet, clazz);
				Iterator<Row> rows = sheet.rowIterator();
				rows.next(); // Get rid of the header row...
				while (rows.hasNext()) {
					Row row = rows.next();
					objects.add(createObject(map, row, clazz));
				}
			}
		}
		closeQuietly(wb);
		return objects;
	}

	private Object createObject(Map<Integer, Method> map, Row row, Class<?> clazz) {
		Object object = BeanUtils.instantiate(clazz);
		int idx = 0;
		while (idx <= map.size()) {
			Method method = map.get(idx);
			if (Utils.isNotNullOrEmpty(method)) {
				Class<?>[] params = method.getParameterTypes();
				if (params != null && params.length > 0) {
					Class<?> param = params[0];
					if (param.equals(Long.class)) {
						Cell cell = row.getCell(idx);
						if (Utils.isNotNullOrEmpty(cell)) {
							double dub = cell.getNumericCellValue();
							long val = (long) dub;
							ReflectionUtils.invokeMethod(method, object, val);
						}
//						Long val = Math.round(cell.getNumericCellValue());
//						ReflectionUtils.invokeMethod(method, object, val);
					} else if (param.equals(String.class)) {
						Cell cell = row.getCell(idx);
						if (Utils.isNotNullOrEmpty(cell)) {
							String val = cell.getStringCellValue();
							ReflectionUtils.invokeMethod(method, object, val);
						}					
					} else if (param.equals(Integer.class)) {
						Cell cell = row.getCell(idx);
						if (Utils.isNotNullOrEmpty(cell)) {
							int val = (int) cell.getNumericCellValue();
							ReflectionUtils.invokeMethod(method, object, val);						
						}
					} else if (param.equals(Timestamp.class)) {
						Cell cell = row.getCell(idx);
						if (Utils.isNotNullOrEmpty(cell)) {
							// FIXME: java.lang.IllegalStateException: Cannot get a numeric value from a text cell (Date/Timestamp cell)
//							Timestamp val = new Timestamp(cell.getDateCellValue().getTime());
//							ReflectionUtils.invokeMethod(method, object, val);						
						}
					}
				}				
			}
			idx++;
		}
		return object;
	}

	private Map<Integer, String> getColumnMap(Row row) {
		Map<Integer, String> columns = new TreeMap<Integer, String>();
		int cells = row.getLastCellNum();
		for (int i = 0; i < cells; i++) {
			String value = row.getCell(i).getStringCellValue();
			if (value != null && !value.isEmpty()) {
				columns.put(i, value);
			}
		}
		return columns;
	}

	private Map<String, String> getFieldMap(Class<?> clazz) {
		Map<String, String> fields = new HashMap<String, String>();
		LOG.trace("Inspecting Class: " + clazz.getName());
		for (Field f : clazz.getDeclaredFields()) {
			LOG.trace("Inspecting Field: " + f.getName());
			for (Annotation a : f.getAnnotations()) {
				LOG.trace("Inspecting Annotation: " + a.annotationType().getSimpleName());
				if (a.annotationType().equals(Column.class)) {
					Column col = (Column) a;
					String columnName = col.name();
					LOG.trace("Found Column: " + columnName);
					fields.put(columnName, f.getName());
				}
			}
		}
		return fields;
	}

	private Map<Integer, Method> getMethodMap(Sheet sheet, Class<?> clazz) {
		Map<Integer, Method> methodMap = null;
		Map<String, String> fieldMap = getFieldMap(clazz);
		Map<Integer, String> columnMap = getColumnMap(sheet.getRow(0));
		if (columnMap.size() == fieldMap.size()) {
			methodMap = new TreeMap<Integer, Method>();
			Iterator<Entry<Integer, String>> columnIter = columnMap.entrySet().iterator();
			while (columnIter.hasNext()) {
				Entry<Integer, String> column = columnIter.next();
				Integer index = column.getKey();
				String columnName = column.getValue();
				String fieldName = fieldMap.get(columnName);
				Method method = getSetMethodFromName(fieldName, clazz);
				String methodName = method.getName();
				LOG.trace(index + ": " + columnName + ": " + fieldName + ": " + methodName);
				methodMap.put(index, method);
			}
		} else {
			LOG.warn("Columns And Fields Do Not Match! Columns: " + columnMap.size()
					+ " | Fields: " + fieldMap.size());
		}
		return methodMap;
	}

	private Method getSetMethodFromName(String field, Class<?> clazz) {
		String fieldName = WordUtils.capitalize(field);
		String methodName = "set" + fieldName;
		Method method = ReflectionUtils.findMethod(clazz, methodName, String.class);
		if (method == null) {
			method = ReflectionUtils.findMethod(clazz, methodName, Integer.class);
			if (method == null) {
				method = ReflectionUtils.findMethod(clazz, methodName, Long.class);
				if (method == null) {
					method = ReflectionUtils.findMethod(clazz, methodName, Timestamp.class);
				}
			}
		}
		return method;
	}

	private Workbook readFile(File file) {
		if (Utils.isNotNullAndExists(file)) {
			InputStream in = null;
			try {
				if (file.getName().endsWith(".xlsx")) {
					in = new FileInputStream(file);
					return new XSSFWorkbook(in);
				}
			} catch (IOException e) {
				LOG.error("Problem Reading File! " + file.getName(), e);
			} finally {
				Utils.closeQuietly(in);
			}
		}
		return null;
	}
}