package com.itgfirm.docengine.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <b>Excel Column Order</b><br>
 * <br>
 * An annotation class to simplify reading and writing Excel files.<br>
 * Place annotation on any field to signify the order a column should be on the
 * spreadsheet from left to right.<br>
 * Give the annotation a value to provide a sheet name.<br>
 * Example: @ExcelColumnOrder(1), @ExcelColumnOrder(3), @ExcelColumnOrder(5)<br>
 * Would make the fields in columns A, B, C in the spreadsheet. The number does
 * not bind to specific column, just order.<br>
 * 
 * @see <a href="https://poi.apache.org/spreadsheet/index.html">Excel POI</a>
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface ExcelColumnOrder {

	/**
	 * Order in which the column is displayed in a spreadsheet. Required.
	 * 
	 * @return Column order, must be provided.
	 */
	int value();
}
