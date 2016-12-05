package com.itgfirm.docengine.service;

import static org.apache.poi.ss.usermodel.Cell.*;
import static com.itgfirm.docengine.util.ExcelUtils.*;
import static com.itgfirm.docengine.util.Utils.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

@Service
class ExcelReaderServiceImpl implements ExcelReaderService {
	private static final Logger _LOG = LoggerFactory.getLogger(ExcelReaderServiceImpl.class);

	ExcelReaderServiceImpl() {
		// Default constructor for Spring
	}

	@Override
	public final <T> Iterable<T> read(final Class<T> clazz, final File file) {
		if (isNotNullOrEmpty(clazz)) {
			try (final Workbook wb = getExcelWorkbook(file)) {
				if (isNotNullOrEmpty(wb)) {
					final int sheets = wb.getNumberOfSheets();
					if (sheets > 0) {
						int idx = 0;
						while (idx < sheets) {
							final Sheet sheet = wb.getSheetAt(idx);
							if (sheet.getSheetName().equals(getExcelSheetNameFromClass(clazz))) {
								final Iterable<T> objects = createObjects(clazz, sheet);
								if (isNotNullOrEmpty(objects)) {
									return objects;
								}
							}
							idx++;
						}
					} else {
						_LOG.debug("The number sheets reported from the workbook is zero!");
					}
				} else {
					_LOG.debug("Workbook must not be null or empty!");
				}
			} catch (final IOException e) {
				_LOG.error(String.format("Problem reading file %s", file.getAbsolutePath()), e);
			}
		} else {
			_LOG.debug("The class must not be null!");
		}
		return null;
	}

	final <T> Iterable<T> createObjects(final Class<T> clazz, final Sheet sheet) {
		if (sheet != null) {
			final Collection<T> list = new ArrayList<T>(sheet.getPhysicalNumberOfRows());
			final Iterator<Row> rows = sheet.rowIterator();
			Collection<String> fields = null;
			while (rows.hasNext()) {
				if (isNotNullOrEmpty(fields)) {
					list.add(createObject(clazz, rows.next(), fields));
				} else {
					fields = (Collection<String>) getExcelColumnFieldNamesFromRow(clazz, rows.next());
					if (!isNotNullOrEmpty(fields)) {
						_LOG.debug(String.format("The header row must not be null while creating objects!\nCLASS: %s",
								(clazz != null ? clazz.getName() : null)));
						return null;
					}
				}
			}
			if (!list.isEmpty()) {
				return list;
			}
		} else {
			_LOG.debug("The sheet to read must not be null!");
		}
		return null;
	}

	final <T> T createObject(final Class<T> clazz, final Row row, final Iterable<String> fields) {
		if (row != null) {
			final T object = instantiate(clazz);
			if (isNotNullOrEmpty(object) && clazz.isInstance(object)) {
				final List<String> names = (List<String>) fields;
				final int size = names.size();
				int idx = 0;
				while (idx < size) {
					final String name = names.get(idx);
					final Cell cell = row.getCell(idx, MissingCellPolicy.RETURN_NULL_AND_BLANK);
					final Object value = getCellValue(cell);
					if (isNotNullOrEmpty(value)) {
						final String cellValueClass = value.getClass().getName();
						_LOG.debug(
								"Searching for write method by the name {} whose cell value class is {}. Value of the cell {}.",
								name, cellValueClass, value);						
					} else {
						_LOG.debug(
								"Searching for write method by the name {} whose cell value class is {}. Value of the cell {}.",
								name, "NONE", "NULL");												
					}
					getWriteMethodAndInvoke(object, name, value);
					idx++;
				}
				return clazz.cast(object);
			}
		} else {
			_LOG.debug(String.format("The row must not be null!\nCLASS: %s", clazz.getName()));
		}
		return null;
	}

	final Object getCellValue(final Cell cell) {
		if (cell != null) {
			final int type = cell.getCellType();
			if (type == CELL_TYPE_STRING) {
				final String string = cell.getStringCellValue();
				_LOG.debug("Found cell type {}: String, returning value {}.", type, string);
				return string;
			} else if (type == CELL_TYPE_BOOLEAN) {
				final Boolean bool = new Boolean(cell.getBooleanCellValue());
				_LOG.debug("Found cell type {}: Boolean, returning value {}.", type, bool.toString());
				return bool;
			} else if (type == CELL_TYPE_NUMERIC) {
				_LOG.debug("Found cell type {}: Number, determining Number type.", type);
				final NumberImpl number = new NumberImpl(cell);
				final Object value = number.getValue();
				_LOG.debug("Found Number type {}, returning value {}.", value.getClass().getName(), value.toString());
				return value;
			} else if (type == CELL_TYPE_BLANK) {
				return "";
			} else {
				_LOG.debug("Could not determine cell type: {}", type);
			}
		}
		return null;
	}

	private class NumberImpl extends Number {
		private static final String TYPE_LONG = "long";
		private static final String TYPE_INT = "int";
		private static final String TYPE_FLOAT = "float";
		private static final String TYPE_DOUBLE = "double";
		private static final long serialVersionUID = 1L;
		private final Double dub;
		private final Float f;
		private final Integer i;
		private final Long l;
		private final String[] check;
		private final int length;
		private String type;

		NumberImpl(final Cell cell) {
			if (cell != null) {
				this.dub = cell.getNumericCellValue();
				if (this.dub < 0.0 || this.dub > 0.0) {
					final String string = String.valueOf(this.dub);
					this.check = string.split("\\.");
					this.length = this.check.length;
					this.f = floatValue();
					doubleValue();
					this.l = longValue();
					this.i = intValue();
				} else {
					this.type = TYPE_INT;
					this.check = null;
					this.length = 0;
					this.f = 0.0F;
					this.l = 0L;
					this.i = 0;
				}
			} else {
				throw new IllegalArgumentException("Cell must not be null!");
			}
		}

		@Override
		public final double doubleValue() {
			type = TYPE_DOUBLE;
			return dub;
		}

		@Override
		public final float floatValue() {
			if (dub < Float.MAX_VALUE && dub > Float.MIN_VALUE) {
				type = TYPE_FLOAT;
				return dub.floatValue();
			}
			return 0;
		}

		@Override
		public final int intValue() {
			final long front = Long.parseLong(check[0]);
			if (front < Integer.MAX_VALUE && front > Integer.MIN_VALUE) {
				if (length == 2) {
					final long end = Long.parseLong(check[1]);
					if (end == 0) {
						type = TYPE_INT;
						return dub.intValue();
					}
				} else if (length == 1) {
					type = TYPE_INT;
					return dub.intValue();
				}
			}
			return 0;
		}

		@Override
		public final long longValue() {
			if (length == 2) {
				if (Long.parseLong(check[1]) == 0) {
					type = TYPE_LONG;
					return dub.longValue();
				}
			} else if (length == 1) {
				type = TYPE_LONG;
				return dub.longValue();
			}
			return 0;
		}

		final Object getValue() {
			switch (type) {
			case TYPE_INT:
				_LOG.debug("Found cell type {}, returning value {}.", type, i.toString());
				return i;
			case TYPE_DOUBLE:
				_LOG.debug("Found cell type {}, returning value {}.", type, dub.toString());
				return dub;
			case TYPE_FLOAT:
				_LOG.debug("Found cell type {}, returning value {}.", type, f.toString());
				return f;
			case TYPE_LONG:
				_LOG.debug("Found cell type {}, returning value {}.", type, l.toString());
				return l;
			}
			return 0;
		}
	}
}