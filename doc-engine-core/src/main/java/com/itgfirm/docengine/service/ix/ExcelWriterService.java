package com.itgfirm.docengine.service.ix;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import com.itgfirm.docengine.annotation.ExcelColumn;
import com.itgfirm.docengine.annotation.ExcelColumnOrder;
import com.itgfirm.docengine.annotation.ExcelSheet;

/**
 * <b>Excel Writer</b><br>
 * A class to simplify writing Excel files.<br>
 * <br>
 * How to use:
 * <ol>
 * <li>Annotate your classes with the following:
 * <ul>
 * <li>{@link ExcelColumn}: Used on fields only. Signifies that the data held
 * here should be pulled into the spreadsheet. An optional {@link String} input
 * will customize the column header. (required)</li>
 * <li>{@link ExcelColumnOrder}: Used on fields only. If used, you must provide
 * an {@link Integer} value. This determines where in the spreadsheet, in
 * relation to columns, this field's data will appear. (not required)</li>
 * <li>{@link ExcelSheet}: Used on class only. If used, the name of the class
 * will become the sheet name, if the optional {@link String} input is used,
 * that will be the sheet's name. If not used at all, the sheet name will likely
 * be it's default name (not required)</li>
 * </ul>
 * </li>
 * <li>Use your annotated classes and make objects!</li>
 * <li>Create a {@link File} representing where you want this output to go. The
 * file must actually exist before being sent in.</li>
 * <li>Get your choice of a Singleton instance or a new instance of the
 * {@link ExcelWriterService} by choosing either {@link #getInstance()} or
 * {@link #getNewInstance()}</li>
 * <li>Use the {@link #write(Class, File, Collection, boolean)} method to
 * generate your Excel file which will be returned to you as a {@link File}
 * object:
 * <ul>
 * <li>Class: Your annotated class</li>
 * <li>File: Your {@link File} that exists</li>
 * <li>Collection: Your {@link Collection} of {@link Object}s to pull the data
 * from.</li>
 * <li>boolean: To show the header row or not.</li>
 * </ul>
 * </li>
 * </ol>
 * 
 * @see <a href="https://poi.apache.org/spreadsheet/index.html">Excel POI</a>
 * @see ExcelSheet
 * @see ExcelColumn
 * @see ExcelColumnOrder
 */
public interface ExcelWriterService {

	/**
	 * Writes the provided {@link Collection} of {@link Object}s whose
	 * {@link Class} is annotated with {@link ExcelColumn}, at a minimum, to the
	 * provided {@link File}.
	 * 
	 * @param clazz
	 *            Annotated {@link Class} with {@link ExcelColumn} at a minimum.
	 * @param file
	 *            {@link File} to write to.
	 * @param data
	 *            {@link List} of {@link Object}s to write.
	 * @param header
	 *            Determines if a header is placed in the first row.
	 * @return TODO
	 * @throws RuntimeException
	 *             When {@link IOException} is thrown.
	 */
	File write(Class<?> clazz, File file, Iterable<?> data, boolean header);
}