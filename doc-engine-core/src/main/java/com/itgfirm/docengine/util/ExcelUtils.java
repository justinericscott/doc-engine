package com.itgfirm.docengine.util;

import static com.itgfirm.docengine.DocEngine.Constants.*;
import static com.itgfirm.docengine.util.Utils.getFieldNameFromAnnotationValue;
import static com.itgfirm.docengine.util.Utils.isNotNullAndExists;
import static com.itgfirm.docengine.util.Utils.isNotNullOrEmpty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itgfirm.docengine.annotation.ExcelColumn;
import com.itgfirm.docengine.annotation.ExcelColumnOrder;
import com.itgfirm.docengine.annotation.ExcelSheet;

public class ExcelUtils {
	private static final Logger LOG = LoggerFactory.getLogger(ExcelUtils.class);
	
	public static Iterable<String> getExcelColumnFieldNamesFromClass(final Class<?> clazz) {
		if (isNotNullOrEmpty(clazz)) {
			final Collection<String> names = new ArrayList<String>();
			final Iterable<Field> fields = getExcelColumnFieldsFromClass(clazz);
			if (isNotNullOrEmpty(fields)) {
				for (final Field field : fields) {
					names.add(field.getName());
				}
				if (!names.isEmpty()) {
					return names;
				}
			}
		} else {
			LOG.debug("The class must not be null!");
		}
		return null;
	}

	public static Iterable<Field> getExcelColumnFieldsFromClass(final Class<?> clazz) {
		if (isNotNullOrEmpty(clazz)) {
			final Collection<Field> fields = new ArrayList<Field>();
			final Collection<Field> unordered = new ArrayList<Field>();
			final SortedMap<Integer, Field> ordered = new TreeMap<Integer, Field>();
			for (final Field field : clazz.getDeclaredFields()) {
				if (isNotNullOrEmpty(getExcelColumnHeaderFromField(field))) {
					final Integer order = getExcelColumnOrderFromField(field);
					if (order != null) {
						ordered.put(order, field);
					} else {
						unordered.add(field);
					}
				}
			}
			// deal with the ordered ones first...
			Iterator<Field> iter = ordered.values().iterator();
			while (iter.hasNext()) {
				fields.add(iter.next());
			}
			// now do the rest of them...
			iter = unordered.iterator();
			while (iter.hasNext()) {
				fields.add(iter.next());
			}
			if (!fields.isEmpty()) {
				return fields;
			}
		} else {
			LOG.debug("The class must not be null!");
		}
		return null;
	}

	public static String getExcelColumnHeaderFromField(final Field field) {
		if (isNotNullOrEmpty(field)) {
			final ExcelColumn anno = field.getAnnotation(ExcelColumn.class);
			if (anno != null) {
				final String label = anno.value();
				if (isNotNullOrEmpty(label)) {
					return label;
				} else {
					return field.getName();
				}
			}
		} else {
			LOG.debug("The field to check the column label of must not be null!");
		}
		return null;
	}
	
	public static Iterable<String> getExcelColumnHeaderFromClass(final Class<?> clazz) {
		if (isNotNullOrEmpty(clazz)) {
			final Collection<String> labels = new ArrayList<String>();
			final Iterable<Field> fields = getExcelColumnFieldsFromClass(clazz);
			if (isNotNullOrEmpty(fields)) {
				for (final Field f : fields) {
					labels.add(getExcelColumnHeaderFromField(f));
				}
				if (!labels.isEmpty()) {
					return labels;
				}
			}
		} else {
			LOG.debug("The class must not be null!");
		}
		return null;
	}

	public static Iterable<String> getExcelColumnFieldNamesFromRow(final Class<?> clazz, final Row header) {
		if (isNotNullOrEmpty(clazz)) {
			if (header != null) {
				final Collection<String> fields = new ArrayList<String>();
				final Iterator<Cell> cells = header.cellIterator();
				while (cells.hasNext()) {
					final String column = cells.next().getStringCellValue();
					final String field = getFieldNameFromAnnotationValue(clazz, ExcelColumn.class, column);
					if (isNotNullOrEmpty(field)) {
						LOG.debug("Adding field name {} from the annotation {} value {}.", field,
								ExcelColumn.class.getName(), column);
						fields.add(field);
					}
				}
				if (!fields.isEmpty()) {
					return fields;
				} else {
					LOG.debug(
							"Collection is empty from attempting to obtain field names from row. Nothing matched the values in the class {} using the ExcelColumn annotation. Returning null.");
				}
			} else {
				LOG.debug(String.format("The header row must not be null while getting field names!\nCLASS: %s",
						clazz.getName()));
			}
		} else {
			LOG.debug("The class must not be null!");
		}
		return null;
	}
	
	public static Integer getExcelColumnOrderFromField(final Field field) {
		if (isNotNullOrEmpty(field)) {
			final ExcelColumnOrder anno = field.getAnnotation(ExcelColumnOrder.class);
			if (anno != null) {
				final Integer idx = anno.value();
				if (idx != null) {
					return idx;
				}
			}
		} else {
			LOG.debug("The field to check the order of must not be null!");
		}
		return null;
	}

	public static Sheet getExcelSheetFromWorkbook(final Workbook wb, final String name) {
		if (wb != null) {
			if (isNotNullOrEmpty(name)) {
				final Sheet sheet = wb.getSheet(name);
				if (sheet != null) {
					return sheet;
				} else {
					return wb.createSheet(name);
				}
			} else {
				LOG.debug("The sheet name must not be null or empty!");
			}
		} else {
			LOG.debug("The workbook must not be null!");
		}
		return null;
	}

	/**
	 * Used with {@link ExcelSheet} to determine the sheet name. If a value is
	 * provided with the annotation {@link ExcelSheet}, that will be used as the
	 * name, otherwise the classname will be used.<br>
	 * <br>
	 * If a {@link Class} is provided that does not have the annotation
	 * {@link ExcelSheet}, a null will be returned.
	 * 
	 * @param clazz
	 *            {@link Class} to inspect.
	 * @return The resulting sheet name.
	 */
	public static String getExcelSheetNameFromClass(final Class<?> clazz) {
		if (isNotNullOrEmpty(clazz)) {
			final ExcelSheet sheet = (ExcelSheet) clazz.getAnnotation(ExcelSheet.class);
			if (isNotNullOrEmpty(sheet)) {
				final String name = sheet.value();
				if (!isNotNullOrEmpty(name)) {
					return clazz.getSimpleName();
				}
				return name;
			} else {
				return clazz.getSimpleName();
			}
		} else {
			LOG.debug("The class must not be null!");
		}
		return null;
	}

	/**
	 * Creates a {@link Workbook} in the provided {@link File} if the
	 * {@link File} is in the supported format (XLSX).
	 * 
	 * @param file
	 *            {@link File} to read.
	 * @return An Excel {@link Workbook}
	 */
	public static Workbook getExcelWorkbook(final File file) {
		if (isNotNullAndExists(file)) {
			if (file.getName().endsWith(FILE_EXTENSION_EXCEL)) {
				try (final InputStream in = new FileInputStream(file)) {
					return new XSSFWorkbook(in);
				} catch (final IOException e) {
					LOG.error(String.format("Problem reading file: %s", file.getAbsolutePath()), e);
				}
			} else {
				LOG.debug(String.format("The provided file does not have a .xlsx extension!\nFILE: %s",
						file.getAbsolutePath()));
			}
		} else {
			LOG.debug("The file must not be null and must exist!");
		}
		return null;
	}
	

	public static void writeExcelWorkbookToFile(final Workbook wb, final File file) {
		if (wb != null) {
			if (isNotNullAndExists(file)) {
				try (final FileOutputStream out = new FileOutputStream(file)) {
					wb.write(out);
				} catch (final IOException e) {
					LOG.error(String.format("Problem writing file: %s", file.getAbsolutePath()), e);
				}
			} else {
				LOG.debug("The file to write to must not be null and must exist!");
			}
		} else {
			LOG.debug("The workbook must not be null or empty!");
		}
	}

	private ExcelUtils() {
		// Do not instantiate
	}	
}