package com.itgfirm.docengine.service;

import static com.itgfirm.docengine.util.Utils.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Date;

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
import org.springframework.stereotype.Service;

import com.itgfirm.docengine.annotation.ExcelColumn;
import com.itgfirm.docengine.annotation.ExcelColumnOrder;

@Service
class ExcelWriterServiceImpl implements ExcelWriterService {
	private static final Logger LOG = LoggerFactory.getLogger(ExcelWriterServiceImpl.class);

	ExcelWriterServiceImpl() {
		// Default constructor for Spring
	}

	public final File write(final Class<?> clazz, final File file, final Iterable<?> data, final boolean header) {
		if (isNotNullOrEmpty(clazz)) {
			if (isNotNullOrEmpty(data)) {
				if (isNotNullAndExists(file)) {
					final String name = getSheetName(clazz);
					if (getSize(file) > 0) {
						try (final Workbook wb = getWorkbook(file)) {
							fillSheet(getSheet(wb, name), clazz, data, header);
							writeFile(wb, file);
							return file;
						} catch (final IOException e) {
							LOG.error(String.format("Problem writing to existing file: %s", file.getAbsolutePath()), e);
						}
					} else {
						try (final Workbook wb = new XSSFWorkbook()) {
							fillSheet(getSheet(wb, name), clazz, data, header);
							writeFile(wb, file);
							return file;
						} catch (final IOException e) {
							LOG.error(String.format("Problem writing to new file: %s", file.getAbsolutePath()), e);
						}
					}
				} else {
					LOG.debug(String.format("The file to be used must not be null and must exist!\nCLASS: %s",
							clazz.getName()));
				}
			} else {
				LOG.debug(String.format("The data collection must not be null or empty!\nCLASS: %s",
						clazz.getSimpleName()));
			}
		} else {
			LOG.debug("The class must not be null!");
		}
		return null;
	}

	final void fillCell(final Cell cell, final Object value) {
		if (cell != null) {
			if (isNotNullOrEmpty(value)) {
				if (value instanceof String) {
					cell.setCellValue((String) value);
				} else if (value instanceof Boolean) {
					cell.setCellValue((Boolean) value);
				} else if (value instanceof Date) {
					cell.setCellValue((Date) value);
				} else if (value instanceof Long) {
					cell.setCellValue((Long) value);
				} else if (value instanceof Integer) {
					cell.setCellValue((Integer) value);
				} else if (value instanceof Double) {
					cell.setCellValue((Double) value);
				} else {
					cell.setCellValue(value.toString());
				}
			} else {
				LOG.debug(String.format("The value must not be null or empty!\nCOLUMN: %s - ROW: %s",
						cell.getColumnIndex(), cell.getRowIndex()));
			}
		} else {
			LOG.debug("The cell must not be null!");
		}
	}

	final void fillHeader(final Row header, final Class<?> clazz) {
		if (header != null) {
			if (isNotNullOrEmpty(clazz)) {
				int idx = 0;
				for (final String value : getHeader(clazz)) {
					final Cell cell = header.createCell(idx);
					cell.setCellValue(value);
					idx++;
				}
			} else {
				LOG.debug("The class must not be null!");
			}
		} else {
			LOG.debug("The header row must not be null!");
		}
	}

	final void fillRow(final Row row, final Class<?> clazz, final Object obj) {
		if (row != null) {
			if (isNotNullOrEmpty(clazz)) {
				if (isNotNullOrEmpty(obj)) {
					int idx = 0;
					for (String field : getFieldNames(clazz)) {
						fillCell(row.createCell(idx), getReadMethodAndInvoke(obj, field));
						idx++;
					}
				} else {
					LOG.debug(String.format("The object to read from must not be null or empty!\nCLASS: %s - ROW: %s",
							clazz.getName(), row.getRowNum()));
				}
			} else {
				LOG.debug("The class must not be null!");
			}
		} else {
			LOG.debug("The row must not be null!");
		}
	}

	final void fillSheet(final Sheet sheet, final Class<?> clazz, final Iterable<?> data, final boolean header) {
		if (sheet != null) {
			if (isNotNullOrEmpty(clazz)) {
				if (isNotNullOrEmpty(data)) {
					int idx = 0;
					if (header) {
						sheet.createFreezePane(0, 1);
						insertRow(sheet, clazz, null, idx, header);
						idx++;
					}
					for (final Object d : data) {
						insertRow(sheet, clazz, d, idx, header);
						idx++;
					}
				} else {
					LOG.debug(String.format("The collection of data must not be null or empty!\nCLASS: %s",
							clazz.getName()));
				}
			} else {
				LOG.debug("The class must not be null!");
			}
		} else {
			LOG.debug("The sheet must not be null!");
		}
	}

	final Iterable<String> getFieldNames(final Class<?> clazz) {
		if (isNotNullOrEmpty(clazz)) {
			final Collection<String> names = new ArrayList<String>();
			final Iterable<Field> fields = getFields(clazz);
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

	final Iterable<Field> getFields(final Class<?> clazz) {
		if (isNotNullOrEmpty(clazz)) {
			final Collection<Field> fields = new ArrayList<Field>();
			final Collection<Field> unordered = new ArrayList<Field>();
			final SortedMap<Integer, Field> ordered = new TreeMap<Integer, Field>();
			for (final Field f : clazz.getDeclaredFields()) {
				if (isNotNullOrEmpty(getLabel(f))) {
					final Integer order = getOrder(f);
					if (order != null) {
						ordered.put(order, f);
					} else {
						unordered.add(f);
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

	final Iterable<String> getHeader(final Class<?> clazz) {
		if (isNotNullOrEmpty(clazz)) {
			final Collection<String> labels = new ArrayList<String>();
			final Iterable<Field> fields = getFields(clazz);
			if (isNotNullOrEmpty(fields)) {
				for (final Field f : fields) {
					labels.add(getLabel(f));
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

	final Sheet getSheet(final Workbook wb, final String name) {
		if (wb != null) {
			if (isNotNullOrEmpty(name)) {
				Sheet sheet = wb.getSheet(name);
				if (sheet == null) {
					sheet = wb.createSheet(name);
				}
				return sheet;
			} else {
				LOG.debug("The sheet name must not be null or empty!");
			}
		} else {
			LOG.debug("The workbook must not be null!");
		}
		return null;
	}

	final void insertRow(final Sheet sheet, final Class<?> clazz, final Object data, final int idx,
			final boolean header) {
		if (isNotNullOrEmpty(clazz)) {
			if (sheet != null) {
				if (header && idx == 0) {
					fillHeader(sheet.createRow(idx), clazz);
				} else {
					fillRow(sheet.createRow(idx), clazz, data);
				}
			} else {
				LOG.debug(String.format("The sheet must not be null!\nCLASS: %s", clazz.getName()));
			}
		} else {
			LOG.debug("The class must not be null or empty!");
		}
	}

	final void writeFile(final Workbook wb, final File file) {
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

	private String getLabel(final Field field) {
		if (isNotNullOrEmpty(field)) {
			final ExcelColumn column = field.getAnnotation(ExcelColumn.class);
			if (column != null) {
				final String label = column.value();
				if (isNotNullOrEmpty(label)) {
					return label;
				} else {
					return field.getName();
				}
			}
		} else {
			LOG.debug("The field to check for the column label for must not be null!");
		}
		return null;
	}

	private Integer getOrder(final Field field) {
		if (isNotNullOrEmpty(field)) {
			final ExcelColumnOrder order = field.getAnnotation(ExcelColumnOrder.class);
			if (order != null) {
				final Integer idx = order.value();
				if (isNotNullOrEmpty(idx)) {
					return idx;
				}
			}
		} else {
			LOG.debug("The field to check the order for must not be null!");
		}
		return null;
	}
}