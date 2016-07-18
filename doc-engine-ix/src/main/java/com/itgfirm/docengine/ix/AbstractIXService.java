/**TODO: License
 */
package com.itgfirm.docengine.ix;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.Table;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;

import com.itgfirm.docengine.annotation.ExportOrder;

/**
 * @author Justin Scott
 * TODO: Description
 */
public class AbstractIXService {
	private static final Logger LOG = LogManager.getLogger(AbstractIXService.class);
	
	protected void closeQuietly(Workbook wb) {
		try {
			wb.close();
		} catch (IOException e) {
			LOG.error("Problem Closing Workbook!", e);
		}
	}
	
	protected Map<String, String> getElements(Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		if (fields.length > 0) {
			SortedMap<Integer, Map<String, String>> ordered =
					new TreeMap<Integer, Map<String, String>>();
			for (Field field : fields) {
				Annotation[] array = field.getAnnotations();
				if (array != null && array.length > 1) {
					ExportOrder order = null;
					Column column = null;
					for (Annotation a : array) {
						if (a instanceof ExportOrder) {
							order = (ExportOrder) a;
						}
						if (a instanceof Column) {
							column = (Column) a;
						}
					}
					if (order != null && column != null) {
						Map<String, String> entry = new HashMap<String, String>(1);
						entry.put(field.getName(), column.name());
						ordered.put(order.value(), entry);
					}
				}
			}
			Map<String, String> map = new LinkedHashMap<String, String>(fields.length);
			Iterator<Map<String, String>> iter = ordered.values().iterator();
			while (iter.hasNext()) {
				Map<String, String> entry = iter.next();
				map.put(entry.keySet().iterator().next(), entry.values().iterator().next());
			}
			return map;
		}
		return null;
	}

	protected String getTableName(Class<?> clazz) {
		Table table = (Table) clazz.getAnnotation(Table.class);
		return table.name();
	}
}