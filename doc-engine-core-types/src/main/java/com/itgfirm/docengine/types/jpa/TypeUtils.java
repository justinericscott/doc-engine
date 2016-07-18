package com.itgfirm.docengine.types.jpa;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * 
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public class TypeUtils {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(TypeUtils.class);

	/**
	 * TODO: Description
	 */
	private TypeUtils() {}

	/**
	 * TODO: Description
	 * 
	 * @param object
	 * @return
	 */
	public static boolean isNotNullOrEmpty(Object object) {
		if (object != null && object instanceof String) {
			String string = (String) object;
			return (!string.trim().toString().isEmpty());
		}
		return (object != null && !object.toString().isEmpty());
	}

	/**
	 * TODO: Description
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isNotNullOrZero(Number val) {
		if (val != null) {
			Long id = val.longValue();
			if (id > 0)
				return true;
		}
		return false;
	}
}