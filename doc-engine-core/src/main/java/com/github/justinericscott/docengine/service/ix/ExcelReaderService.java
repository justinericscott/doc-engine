package com.github.justinericscott.docengine.service.ix;

import java.io.File;

import com.github.justinericscott.docengine.annotation.ExcelColumn;
import com.github.justinericscott.docengine.annotation.ExcelColumnOrder;
import com.github.justinericscott.docengine.annotation.ExcelSheet;
import com.github.justinericscott.docengine.models.Content;

/**
 * <b>Excel Reader</b><br>
 * A class to simplify reading Excel files.<br>
 * <br>
 * <b>How to use:</b>
 * <ol>
 * <li>Annotate your classes with the following:
 * <ul>
 * <li>{@link ExcelColumn}: Used on fields only. Signifies that the data will be
 * held here while being pulled from the spreadsheet. An optional {@link String}
 * input will indicate the column header (required)</li>
 * <li>{@link ExcelColumnOrder}: Used on fields only. If used, you must provide
 * an {@link Integer} value. This determines where in the spreadsheet, in
 * relation to columns, this field's data will appear. (required)</li>
 * <li>{@link ExcelSheet}: Used on class only. If used, the name of the class
 * will be the sheet name to read, if the optional {@link String} input is used,
 * that will be the sheet's name to read. If not used at all, the sheet name
 * will likely be its default name (not required)</li>
 * </ul>
 * </li>
 * <li>Create a {@link File} representing where the Excel file you want to read
 * from. The file must actually exist before being sent in.</li>
 * <li>Get your choice of a Singleton instance or a new instance of the
 * {@link ExcelReaderService} by choosing either {@link #getInstance()} or
 * {@link #getNewInstance()}</li>
 * <li>Use the {@link #read(Class, File)} method to generate your objects which
 * will be returned to you as a Collection of objects based off of your
 * annotated classes.
 * <ul>
 * <li>Class: Your annotated class</li>
 * <li>File: The Excel {@link File} to read</li>
 * </ul>
 * </li>
 * </ol>
 * 
 * @see <a href="https://poi.apache.org/spreadsheet/index.html">Excel POI</a>
 * @see ExcelSheet
 * @see ExcelColumn
 * @see ExcelColumnOrder
 */
public interface ExcelReaderService {

	/**
	 * Reads the provided {@link File} into {@link Object}s of the provided
	 * {@link Class}. The {@link Class} must have its {@link Field}s annotated
	 * with {@link ExcelColumn} and the header {@link Row} of the Excel
	 * {@link Sheet} must match the annotation values or the {@link Field}
	 * names.
	 * 
	 * @param clazz
	 *            {@link Class} to mold data into.
	 * @param file
	 *            {@link File} to read.
	 * 
	 * @param <T>
	 *            Expected return array type.
	 * @return Array of {@link Object}s molded from the provided {@link Class}.
	 */
	Iterable<?> read(Class<?> type, File file);
	
	Iterable<? extends Content> read(Class<? extends Content>[] types, File file);
}
