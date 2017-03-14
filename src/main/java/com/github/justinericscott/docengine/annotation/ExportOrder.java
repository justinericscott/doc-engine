/**
 * TODO: License
 */
package com.github.justinericscott.docengine.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExportOrder {

	/**
	 * Order in which the column is displayed in export file.
	 * 
	 * @return Column order
	 */
//	int value();
}
