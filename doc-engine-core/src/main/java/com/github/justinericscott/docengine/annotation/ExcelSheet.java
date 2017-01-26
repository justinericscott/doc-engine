package com.github.justinericscott.docengine.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <b>Excel Sheet</b><br>
 * <br>
 * An annotation class to simplify reading and writing Excel files.<br>
 * Place annotation on class to signify that it should be a spreadsheet.<br>
 * Give the annotation a value to provide a sheet name.<br>
 * Example: @ExcelSheet("List of Projects")<br>
 * <br>
 * 
 * @see <a href="https://poi.apache.org/spreadsheet/index.html">Excel POI</a>
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface ExcelSheet {

	/**
	 * The {@link String} value to be used as the sheet name in an Excel report.
	 * 
	 * @return {@link String} sheet name or an empty {@link String}.
	 */
	String value() default "";
}
