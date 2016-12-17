package com.itgfirm.docengine.service.ix;

import static com.itgfirm.docengine.service.ix.ExcelUtils.*;
import static com.itgfirm.docengine.util.Utils.*;

import java.io.File;
import java.io.IOException;
import java.sql.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
					final String name = getExcelSheetNameFromClass(clazz);
					if (getSize(file) > 0) {
						try (final Workbook wb = getExcelWorkbook(file)) {
							fillSheet(getExcelSheetFromWorkbook(wb, name), clazz, data, header);
							writeExcelWorkbookToFile(wb, file);
							return file;
						} catch (final IOException e) {
							LOG.error(String.format("Problem writing to existing file: %s", file.getAbsolutePath()), e);
						}
					} else {
						try (final Workbook wb = new XSSFWorkbook()) {
							fillSheet(getExcelSheetFromWorkbook(wb, name), clazz, data, header);
							writeExcelWorkbookToFile(wb, file);
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
				cell.setCellType(Cell.CELL_TYPE_BLANK);
			}
		} else {
			LOG.debug("The cell must not be null!");
		}
	}

	final void fillHeader(final Row header, final Class<?> clazz) {
		if (header != null) {
			if (isNotNullOrEmpty(clazz)) {
				int idx = 0;
				for (final String value : getExcelColumnHeaderFromClass(clazz)) {
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
					for (String field : getExcelColumnFieldNamesFromClass(clazz)) {
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
}