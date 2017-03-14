/**
 * TODO: License
 */
package com.github.justinericscott.docengine.models.jaxb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Justin
 *
 */
public class TypeUtils {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(TypeUtils.class);
	
	/**
	 * TODO: Description
	 */
	private TypeUtils() { }
	
	/**
	 * TODO: Description
	 * @param object
	 * @return
	 */
	public static boolean isNotNullOrEmpty(Object object) {
		if (object instanceof String) {
			String string = (String) object;
			return (string != null && !string.trim().toString().isEmpty());
		}
		return (object != null && !object.toString().isEmpty());
	}
	
	/**
	 * TODO: Description
	 * @param val
	 * @return
	 */
	public static boolean isNotNullOrZero(Number val) {
		if (val != null) {
			Long id = val.longValue();
			if (id > 0) return true;
		}
		return false;
	}
}