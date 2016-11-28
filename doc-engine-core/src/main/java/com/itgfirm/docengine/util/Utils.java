/**
 * TODO: License
 */
package com.itgfirm.docengine.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itgfirm.docengine.types.jpa.ClauseJpaImpl;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.types.jpa.InstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.ParagraphJpaImpl;
import com.itgfirm.docengine.types.jpa.SectionJpaImpl;
import com.itgfirm.docengine.types.jpa.TokenDefinitionJpaImpl;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public class Utils {
	private static final Logger LOG = LoggerFactory.getLogger(Utils.class);

	public static String[] breakSqlScriptIntoStatements(final File file) {
		if (isNotNullAndExists(file)) {
			final String sql = readFile(file);
			String[] script = null;
			if (isNotNullOrEmpty(sql)) {
				LOG.trace("Splitting Script.");
				script = sql.split(";");
			}
			return script;			
		}
		return null;
	}

	public static boolean isAllSections(final Iterable<?> content) {
		if (isNotNullOrEmpty(content)) {
			for (final Object o : content) {
				if (!(o instanceof SectionJpaImpl))
					return false;
			}
			return true;
		}
		return false;
	}

	public static boolean isAllClauses(final Iterable<?> content) {
		if (isNotNullOrEmpty(content)) {
			for (final Object o : content) {
				if (!(o instanceof ClauseJpaImpl))
					return false;
			}
			return true;
		}
		return false;
	}

	public static boolean isAllParagraphs(final Iterable<?> content) {
		if (isNotNullOrEmpty(content)) {
			for (final Object o : content) {
				if (!(o instanceof ParagraphJpaImpl))
					return false;
			}
			return true;
		}
		return false;
	}

	public static boolean isNotNullAndExists(final File file) {
		return (file != null && file.exists());
	}

	public static boolean isNotNullOrEmpty(final Collection<?> collection) {
		return (collection != null && !collection.isEmpty());
	}

	public static boolean isNotNullOrEmpty(final Iterable<?> iterable) {
		return (iterable != null && iterable.iterator().hasNext());
	}

	public static boolean isNotNullOrEmpty(final Object object) {
		return (object != null && !object.toString().trim().isEmpty());
	}

	public static boolean isNotNullOrEmpty(final ContentJpaImpl content) {
		return (content != null && content.isValid());
	}

	public static boolean isNotNullOrEmpty(final InstanceJpaImpl instance) {
		return (instance != null && instance.isValid());
	}

	public static boolean isNotNullOrEmpty(final TokenDefinitionJpaImpl token) {
		return (token != null && token.isValid());
	}

	public static boolean isNotNullOrZero(final Number val) {
		return (val != null && val.longValue() != 0);
	}

	private static String readFile(final File file) {
		if (isNotNullAndExists(file)) {
			byte[] bytes = null;
			try {
				bytes = Files.readAllBytes(Paths.get(file.getPath()));
			} catch (final IOException e) {
				LOG.debug(e.getMessage(), e);
			}
			return new String(bytes);			
		}
		return null;
	}

	private Utils() {
		// Do not instantiate
	}
}