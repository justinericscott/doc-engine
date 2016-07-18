/**
 * TODO: License
 */
package com.itgfirm.docengine.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.itgfirm.docengine.types.Clause;
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.Instance;
import com.itgfirm.docengine.types.Paragraph;
import com.itgfirm.docengine.types.Section;
import com.itgfirm.docengine.types.TokenDefinition;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public class Utils {
	private static final Logger LOG = LogManager.getLogger(Utils.class);

	public static String[] breakSqlScriptIntoStatements(File file) {
		String sql = Utils.readFile(file);
		String[] script = null;
		if (Utils.isNotNullOrEmpty(sql)) {
			LOG.trace("Splitting Script.");
			script = sql.split(";");
		}
		return script;
	}

	public static void closeQuietly(InputStream in) {
		try {
			in.close();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public static void closeQuietly(OutputStream out) {
		try {
			out.close();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public static void closeQuietly(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public static boolean isAllSections(Collection<?> content) {
		if (isNotNullOrEmpty(content)) {
			for (Object o : content) {
				if (!(o instanceof Section))
					return false;
			}
			return true;
		}
		return false;
	}

	public static boolean isAllClauses(Collection<?> content) {
		if (isNotNullOrEmpty(content)) {
			for (Object o : content) {
				if (!(o instanceof Clause))
					return false;
			}
			return true;
		}
		LOG.debug("Null Or Empty Clause Collection.");
		return false;
	}

	public static boolean isAllParagraphs(Collection<?> content) {
		if (isNotNullOrEmpty(content)) {
			for (Object o : content) {
				if (!(o instanceof Paragraph))
					return false;
			}
			return true;
		}
		return false;
	}

	public static boolean isExist(Content content) {
		if (content != null && isNotNullOrZero(content.getId())) {
			LOG.debug("Returning TRUE: " + content.getContentCd()
					+ " | PARAM_ID: "
					+ content.getId());
			return true;
		}
		return false;
	}

	public static boolean isNotNullAndExists(File file) {
		return (file != null && file.exists());
	}
	
	public static boolean isNotNullOrEmpty(Collection<?> collection) {
		return (collection != null && !collection.isEmpty());
	}

	public static boolean isNotNullOrEmpty(Object object) {
		if (object instanceof String) {
			String string = (String) object;
			return (string != null && !string.trim().toString().isEmpty());
		}
		return (object != null && !object.toString().isEmpty());
	}

	public static boolean isNotNullOrEmpty(Content content) {
		if (content != null) {
			return content.isValid();
		}
		return false;
	}

	public static boolean isNotNullOrEmpty(Instance instance) {
		if (instance != null) {
			return instance.isValid();
		}
		return false;
	}

	public static boolean isNotNullOrEmpty(TokenDefinition token) {
		if (token != null) {
			return token.isValid();
		}
		return false;
	}

	public static boolean isNotNullOrZero(Number val) {
		if (val != null) {
			Long id = val.longValue();
			if (id > 0)
				return true;
		}
		return false;
	}

	private static String readFile(File file) {
		byte[] bytes = null;
		try {
			bytes = Files.readAllBytes(Paths.get(file.getPath()));
		} catch (IOException e) {
			LOG.debug(e.getMessage(), e);
		}
		return new String(bytes);
	}

	private Utils() {
	}
}