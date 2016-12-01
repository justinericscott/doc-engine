package com.itgfirm.docengine.types.jpa;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.Collection;

/**
 * 
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public class TypeUtils {

	/**
	 * TODO: Description
	 */
	private TypeUtils() {
		// Do not instantiate
	}

	/**
	 * TODO: Description
	 * 
	 * @param object
	 * @return
	 */
	public static boolean isNotNullOrEmpty(final Object object) {
		return (object != null && !object.toString().trim().isEmpty());
	}

	/**
	 * TODO: Description
	 * 
	 * @param collection
	 * @return
	 */
	public static boolean isNotNullOrEmpty(final Collection<?> collection) {
		return (collection != null && !collection.isEmpty());
	}

	/**
	 * TODO: Description
	 * 
	 * @param iterable
	 * @return
	 */
	public static boolean isNotNullOrEmpty(final Iterable<?> iterable) {
		return (iterable != null && iterable.iterator().hasNext());
	}

	/**
	 * TODO: Description
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isNotNullOrZero(final Number number) {
		return (number != null && (number.longValue() > 0 || number.intValue() > 0 || number.doubleValue() > 0
				|| number.floatValue() > 0));
	}

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	public static Timestamp now() {
		return Timestamp.from(Calendar.getInstance().toInstant());
	}

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	public static Timestamp max() {
		return Timestamp.from(Instant.MAX);
	}
}