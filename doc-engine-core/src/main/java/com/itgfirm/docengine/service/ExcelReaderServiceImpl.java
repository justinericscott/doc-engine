package com.itgfirm.docengine.service;

import static com.itgfirm.docengine.util.Utils.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.itgfirm.docengine.annotation.ExcelColumn;

@Service
class ExcelReaderServiceImpl implements ExcelReaderService {
	private static final Logger LOG = LoggerFactory.getLogger(ExcelReaderServiceImpl.class);

	ExcelReaderServiceImpl() {
		// Default constructor for Spring
	}

	@Override
	public final Iterable<?> read(final Class<?> clazz, final File file) {
		if (isNotNullOrEmpty(clazz)) {
			try (final Workbook wb = getWorkbook(file)) {
				if (isNotNullOrEmpty(wb)) {
					final int sheets = wb.getNumberOfSheets();
					if (sheets > 0) {
						int idx = 0;
						while (idx < sheets) {
							final String bookSheetName = wb.getSheetName(idx);
							final String classSheetName = getSheetName(clazz);
							if (bookSheetName.equals(classSheetName) || bookSheetName.equals(clazz.getSimpleName())) {
								final Iterable<?> objects = createObjects(clazz, wb.getSheetAt(idx));
								if (isNotNullOrEmpty(objects)) {
									return objects;
								}
							}
							idx++;
						}
					} else {
						LOG.debug("The number sheets reported from the workbook is zero!");
					}
				} else {
					LOG.debug("Workbook must not be null or empty!");
				}
			} catch (final IOException e) {
				LOG.error(String.format("Problem reading file %s", file.getAbsolutePath()), e);
			}
		} else {
			LOG.debug("The class must not be null!");
		}
		return null;
	}

	final Iterable<?> createObjects(final Class<?> clazz, final Sheet sheet) {
		if (sheet != null) {
			final Collection<Object> list = new ArrayList<Object>(sheet.getPhysicalNumberOfRows());
			final Iterator<Row> rows = sheet.rowIterator();
			Collection<String> header = null;
			while (rows.hasNext()) {
				if (header == null) {
					header = (Collection<String>) getFieldNames(clazz, rows.next());
					if (header == null) {
						LOG.debug(String.format("The header row must not be null while creating objects!\nCLASS: %s", (clazz != null ? clazz.getName() : null)));
						return null;
					}
				} else if (header.size() > 0) {
					list.add(createObject(clazz, rows.next(), header));
				}
			}
			if (!list.isEmpty()) {
				return list;
			}
		} else {
			LOG.debug("The sheet to read must not be null!");
		}
		return null;
	}

	final Object createObject(final Class<?> clazz, final Row row, final Iterable<String> fields) {
		if (row != null) {
			final Object object = instantiate(clazz);
			if (isNotNullOrEmpty(object) && clazz.isInstance(object)) {
				final Iterator<Cell> cells = row.cellIterator();
				final Iterator<String> names = fields.iterator();
				while (cells.hasNext()) {
					getWriteMethodAndInvoke(object, names.next(), getCellValue(cells.next()));
				}
				return clazz.cast(object);
			}
		} else {
			LOG.debug(String.format("The row must not be null!\nCLASS: %s", clazz.getName()));
		}
		return null;
	}

	final Object getCellValue(final Cell cell) {
		if (cell != null) {
			final int type = cell.getCellType();
			if (type == Cell.CELL_TYPE_STRING) {
				return cell.getStringCellValue();
			} else if (type == Cell.CELL_TYPE_BOOLEAN) {
				return cell.getBooleanCellValue();
			} else if (type == Cell.CELL_TYPE_NUMERIC) {
				Object o = cell.getNumericCellValue();
				return o;
			} else if (type == Cell.CELL_TYPE_BLANK) {
				return null;
			} else {
				return String.format("Could not determine type: %s", type);
			}
		} else {
			LOG.debug("The cell to read must not be null!");
		}
		return null;
	}

	final Iterable<String> getFieldNames(final Class<?> clazz, final Row header) {
		if (isNotNullOrEmpty(clazz)) {
			if (header != null) {
				final Collection<String> list = new ArrayList<String>();
				final Iterator<Cell> cells = header.cellIterator();
				while (cells.hasNext()) {
					final String name = getAnnotatedFieldNameWithValue(clazz, ExcelColumn.class,
							getCellValue(cells.next()).toString());
					if (isNotNullOrEmpty(name)) {
						list.add(name);
					}
				}
				if (!list.isEmpty()) {
					return list;
				}
			} else {
				LOG.debug(String.format("The header row must not be null while getting field names!\nCLASS: %s", clazz.getName()));
			}
		} else {
			LOG.debug("The class must not be null!");
		}
		return null;
	}
}