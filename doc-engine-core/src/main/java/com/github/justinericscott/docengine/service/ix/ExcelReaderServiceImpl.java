package com.github.justinericscott.docengine.service.ix;

import static org.apache.poi.ss.usermodel.Cell.*;
import static com.github.justinericscott.docengine.service.ix.ExcelUtils.*;
import static com.github.justinericscott.docengine.util.Utils.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
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
						_LOG.warn("The number sheets reported from the workbook is zero!");
					}
				} else {
					_LOG.warn("Workbook must not be null or empty!");
				}
			} catch (final IOException e) {
				_LOG.error(String.format("Problem reading file %s", file.getAbsolutePath()), e);
			}
		} else {
			_LOG.warn("The class must not be null!");
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
						_LOG.warn(String.format("The header row must not be null while creating objects!\nCLASS: %s",
								(clazz != null ? clazz.getName() : null)));
						return null;
					}
				}
			}
			if (!list.isEmpty()) {
				return list;
			}
		} else {
			_LOG.warn("The sheet to read must not be null!");
		}
		return null;
	}

	final <T> T createObject(final Class<T> type, final Row row, final Iterable<String> fields) {
		if (row != null) {
			final T object = instantiate(type);
			if (isNotNullOrEmpty(object) && type.isInstance(object)) {
				final List<String> names = (List<String>) fields;
				final int size = names.size();
				int idx = 0;
				while (idx < size) {
					final String name = names.get(idx);
					final Cell cell = row.getCell(idx, MissingCellPolicy.RETURN_NULL_AND_BLANK);
					final Object value = getCellValue(cell);
					getWriteMethodAndInvoke(object, name, value);
					idx++;
				}
				return type.cast(object);
			}
		} else {
			_LOG.warn(String.format("The row must not be null!\nCLASS: %s", type.getName()));
		}
		return null;
	}

	final Object getCellValue(final Cell cell) {
		if (cell != null) {
			final int type = cell.getCellType();
			if (type == CELL_TYPE_STRING) {
				final String string = cell.getStringCellValue();
				_LOG.trace("Found cell type {}: String, returning value {}.", type, string);
				return string;
			} else if (type == CELL_TYPE_BOOLEAN) {
				final Boolean bool = new Boolean(cell.getBooleanCellValue());
				_LOG.trace("Found cell type {}: Boolean, returning value {}.", type, bool.toString());
				return bool;
			} else if (type == CELL_TYPE_NUMERIC) {
				_LOG.trace("Found cell type {}: Number, determining Number type.", type);
				final NumberImpl number = new NumberImpl(cell);
				final Object value = number.getValue();
				_LOG.trace("Found Number type {}, returning value {}.", value.getClass().getName(), value.toString());
				return value;
			} else if (type == CELL_TYPE_BLANK) {
				return null;
			} else {
				_LOG.warn("Could not determine cell type: {}", type);
			}
		}
		return null;
	}

	private class NumberImpl extends Number {
		private static final String TYPE_DATE = "date";
		private static final String TYPE_DOUBLE = "double";
		private static final String TYPE_FLOAT = "float";
		private static final String TYPE_INT = "int";
		private static final String TYPE_LONG = "long";
		private static final String TYPE_UNKNOWN = "unknown";
		private static final long serialVersionUID = 1L;
		private final Cell cell;
		private final java.util.Date date;
		private final Double dub;
		private String type;

		NumberImpl(final Cell cell) {
			if (cell != null) {
				this.cell = cell;
				this.dub = doubleValue();
				if (DateUtil.isCellDateFormatted(this.cell)) {
					this.date = this.cell.getDateCellValue();
					this.type = TYPE_DATE;
				} else {
					checkType();
					this.date = null;
				}
			} else {
				throw new IllegalArgumentException("Cell must not be null!");
			}
		}

		@Override
		public final double doubleValue() {
			if (dub == null) {
				return this.cell.getNumericCellValue();	
			}			
			return dub;
		}

		@Override
		public final float floatValue() {
			return (float) doubleValue();
		}

		@Override
		public final int intValue() {
			return (int) longValue();
		}

		@Override
		public final long longValue() {
			return (long) doubleValue();
		}

		final Object getValue() {
			switch (type) {
			case TYPE_INT:
				_LOG.trace("Found cell type {}, returning value {}.", type, intValue());
				return intValue();
			case TYPE_DOUBLE:
				_LOG.trace("Found cell type {}, returning value {}.", type, doubleValue());
				return doubleValue();
			case TYPE_FLOAT:
				_LOG.trace("Found cell type {}, returning value {}.", type, floatValue());
				return floatValue();
			case TYPE_LONG:
				_LOG.trace("Found cell type {}, returning value {}.", type, longValue());
				return longValue();
			case TYPE_DATE:
				_LOG.trace("Found cell type {}, returning value {}.", type, date.toString());
				return date;
			}
			return null;
		}

		private void checkType() {
			String[] check = String.valueOf(dub).split("\\.");
			int length = check.length;
			if (length > 1) {
				if (Long.parseLong(check[1]) != 0) {
					if (dub > Float.MAX_VALUE || dub < Float.MIN_VALUE) {
						this.type = TYPE_DOUBLE;
					} else {
//						this.type = TYPE_FLOAT;
						this.type = TYPE_DOUBLE;
					}
				} else {
					final String val = check[0];
					long l = Long.parseLong(val);
					if (l > Integer.MAX_VALUE || l < Integer.MIN_VALUE) {
						this.type = TYPE_LONG;
					} else {
						this.type = TYPE_INT;
					}
				}
			} else {
				this.type = TYPE_UNKNOWN;
			}
		}
	}
}