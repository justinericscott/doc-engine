package com.itgfirm.docengine.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <b>Excel Column</b><br>
 * <br>
 * An annotation class to simplify reading and writing Excel files.<br>
 * Place annotation on any field to signify that it should be a spreadsheet
 * column.<br>
 * Give the annotation a value to provide a column header.<br>
 * Example: @ExcelColumn("Project Number")<br>
 * <br>
 * 
 * @author <a href="mailto:justin.scott@itgfirm.com">Justin Scott</a>
 * @see <a href="https://poi.apache.org/spreadsheet/index.html">Excel POI</a>
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface ExcelColumn {

	/**
	 * The {@link String} value to be used as the column header in an Excel
	 * report.
	 * 
	 * @return {@link String} column header name, if provided or an empty
	 *         {@link String}.
	 */
	String value() default "";
}