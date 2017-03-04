/**
 * TODO: License
 */
package com.github.justinericscott.docengine.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.net.URISyntaxException;
import java.net.URL;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import java.sql.Date;
import java.sql.Timestamp;

import java.time.Instant;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.tidy.Tidy;

import com.github.justinericscott.docengine.models.Content;
import com.github.justinericscott.docengine.models.Instance;
import com.github.justinericscott.docengine.models.TokenDefinition;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public class Utils {
	private static final Logger LOG = LoggerFactory.getLogger(Utils.class);
	private static final String PREFIX_COPY_OF = "Copy of - ";
	private static final String REGEX_SPLIT_PATH = "\\.(?=[^\\.]+$)";
	private static final String SYS_TEMP_DIR = "java.io.tmpdir";

	private Utils() {
		// Do not instantiate
	}

	public static String[] breakSqlScriptIntoStatements(final File file) {
		if (isNotNullAndExists(file)) {
			final String sql = readFile(file);
			String[] script = null;
			if (isNotNullOrEmpty(sql)) {
				script = sql.split(";");
			}
			return script;
		}
		return null;
	}

	public static String collapse(String string) {
		if (isNotNullOrEmpty(string)) {
			String result = string.replaceAll("[(\n\r\t)]", "");
			result = result.replaceAll(">+[ ]+<", "><");
			result = result.replaceAll("[( )][^A-Za-z0-9<>\\-\\*\\.,;:\\?=/{}\"\\\\\\(\\)]", " ");
			result = result.replaceAll("[^A-Za-z0-9<>\\-\\*\\.,;:\\?=/{}\"\\\\\\(\\)][( )]", " ");
			result = result.replaceAll("  ", " ");
			result = result.replaceAll("}", " } ");
			result = result.replaceAll("}  <", "}<");
			return result.replaceAll("\"\"", "\" \"");
		}
		LOG.warn("The string to collapse must not be null or empty!");
		return "";
	}

	/**
	 * Makes a copy of the given file or directory in the same parent directory
	 * of the original. <br>
	 * <br>
	 * The file or directory will be prepended with "Copy of - ".<br>
	 * <br>
	 * If a file or directory already exists, an integer will be appended to the
	 * name and incremented until it can be safely copied.
	 * 
	 * @param file
	 *            {@link File} object of the item to copy.
	 * @return {@link File} object representing the copied item or null.
	 */
	public static File copy(final File file) {
		final File copy = getNewFileForCopy(file);
		if (copy != null) {
			try {
				if (file.isDirectory()) {
					FileUtils.copyDirectory(file, copy);
					if (isNotNullAndExists(get(copy.getAbsolutePath()))) {
						return copy;
					} else {
						LOG.error(String.format("Copy Failed: Source: %s | Copy: %s!", file.getAbsolutePath(),
								copy.getAbsolutePath()));
					}
				} else {
					Files.copy(file.toPath(), copy.toPath(), StandardCopyOption.REPLACE_EXISTING);
					if (isNotNullAndExists(copy)) {
						return copy;
					} else {
						LOG.error(String.format("Copy Failed: Source: %s | Copy: %s!", file.getAbsolutePath(),
								copy.getAbsolutePath()));
					}
				}
			} catch (final IOException e) {
				LOG.error(String.format("Problem copying file: Source: %s!", file.getAbsolutePath()), e);
			}
		} else {
			LOG.error("Problem creating copied File object!");
		}
		return null;
	}

	/**
	 * Creates an empty {@link File} at the given path.<br>
	 * <br>
	 * If a file or directory exists with the same path and name, it will not be
	 * overwritten.
	 * 
	 * @param path
	 *            Path of {@link File} to create.
	 * @return {@link File} object representing the new file.
	 * @see #create(File)
	 * @see #create(File, String)
	 */
	public static File create(final String path) {
		return create(path, true);
	}

	public static File create(final String path, final boolean overwrite) {
		if (isNotNullOrEmpty(path)) {
			File file = get(path);
			if (!isNotNullAndExists(file)) {
				file = new File(path);
			}
			return create(file, overwrite);
		} else {
			LOG.warn("Path must not be null or empty!");
		}
		return null;
	}

	public static File createDirectory(final File parent, final String name) {
		if (isNotNullAndExists(parent)) {
			if (isNotNullOrEmpty(name)) {
				String path = null;
				if (parent.isFile()) {
					path = parent.getParentFile().getAbsolutePath().concat(File.separator.concat(name));
				} else if (parent.isDirectory()) {
					path = parent.getAbsolutePath().concat(File.separator.concat(name));
				}
				return createDirectory(path);
			} else {
				LOG.warn("Directory name must not be null or empty!\nPARENT: {}", parent.getAbsolutePath());
			}
		} else {
			LOG.warn("Parent file must not be null and must exist!");
		}
		return null;
	}

	/**
	 * Recursively deletes a file or directory and all of its sub-directories
	 * and files.
	 * 
	 * @param file
	 *            {@link File} object representing the file or directory to
	 *            delete.
	 * @return True or False based upon success of the delete. Returns true if
	 *         successful and if there is nothing to delete. Otherwise, false is
	 *         returned.
	 */
	public static boolean delete(final File file) {
		if (isNotNullAndExists(file)) {
			if (file.isDirectory()) {
				try {
					FileUtils.deleteDirectory(file);
					return !file.exists();
				} catch (final IOException e) {
					LOG.error(String.format("Problem deleting directory: %s", file.getAbsolutePath()), e);
				}
			} else {
				file.delete();
				return !file.exists();
			}
		}
		return true;
	}

	/**
	 * Obtains the {@link Class} for the name given and catches any exceptions.
	 * 
	 * @param name
	 *            Name of the {@link Class} to obtain.
	 * @return The desired {@link Class} if it exists.
	 */
	public static Class<?> find(final String name) {
		if (isNotNullOrEmpty(name)) {
			try {
				return Class.forName(name);
			} catch (final ClassNotFoundException e) {
				LOG.error(String.format("Problem finding class: %s", name), e);
			}
		} else {
			LOG.warn("Class name cannot be null or empty!");
		}
		return null;
	}

	/**
	 * Obtains a {@link File} object for the given path, if it exists,
	 * otherwise, null is returned.
	 * 
	 * @param path
	 *            The file path of the object to get.
	 * @return {@link File} object, if the file exists, otherwise, null.
	 */
	public static File get(final String path) {
		File file = null;
		if (path != null) {
			LOG.trace("Attempting to get file using new File({})...", path);
			file = new File(path);
			try {
				URL url = null;
				if (!isNotNullAndExists(file)) {
					LOG.trace("Attempting to get file using Utils.class.getResource({})...", path);
					url = Utils.class.getResource(path);
					if (url != null) {
						LOG.trace("Attempting to get file using new File(url.toURI()) from class...");
						file = new File(url.toURI());
					}
				}
				if (!isNotNullAndExists(file)) {
					LOG.trace(
							"Attempting to get file using Thread.currentThread().getContextClassLoader().getResource({})...",
							path);
					url = Thread.currentThread().getContextClassLoader().getResource(path);
					if (url != null) {
						LOG.trace("Attempting to get file using new File(url.toURI()) from thread...");
						file = new File(url.toURI());
					}
				}
			} catch (final URISyntaxException e) {
				LOG.error(String.format("Problem getting URI: %s", path), e);
			}
			if (!isNotNullAndExists(file)) {
				try {
					LOG.trace("Attempting to get file using Paths.get({}).toFile()...", path);
					file = Paths.get(path).toFile();
				} catch (final UnsupportedOperationException | InvalidPathException e) {
					LOG.error(String.format("Problem using Paths: ", path), e);
				}
			}
		} else {
			LOG.warn("File path must not be null!");
		}
		return ((isNotNullAndExists(file)) ? file : null);
	}

	/**
	 * Retrieves the name of a {@link Field} with an {@link Annotation} matching
	 * the provided annotation {@link Class} and its value. The annotation will
	 * need a property named "value" in order to get the value.
	 * 
	 * @param clazz
	 *            {@link Class} to search.
	 * @param annotation
	 *            Annotation {@link Class} to check value of.
	 * @param value
	 *            Value of the value property in the {@link Annotation}.
	 * @return Name of the {@link Field}.
	 * 
	 */
	public static String getFieldNameFromAnnotationValue(final Class<?> clazz, Class<? extends Annotation> annotation,
			final String value) {
		if (isNotNullOrEmpty(clazz)) {
			if (isNotNullOrEmpty(annotation)) {
				if (isNotNullOrEmpty(value)) {
					for (final Field field : clazz.getDeclaredFields()) {
						final Annotation anno = field.getAnnotation(annotation);
						if (isNotNullOrEmpty(anno)) {
							final String string = anno.toString();
							if (string.contains("value=".concat(value))) {
								final String name = field.getName();
								LOG.trace("Found annnotation {} with the value {} in the class {} for the field {}.",
										anno.getClass().getName(), value, clazz.getName(), name);
								return field.getName();
							}
						}
					}
				} else {
					LOG.warn("A value is required to check the annotation {} in the class {} for the associated field!",
							annotation.getName(), clazz.getName());
				}
			} else {
				LOG.warn("Excel annotation class in the provided class {} must not be null!", clazz.getName());
			}
		} else {
			LOG.warn("Class cannot be null!");
		}
		return null;
	}

	/**
	 * Finds and invokes a parameterless {@link Method} from the provided
	 * {@link Object} using the provided {@link Field} name to locate the
	 * {@link Method}.
	 * 
	 * @param object
	 *            {@link Object} to invoke the {@link Method} on.
	 * @param name
	 *            Name of the {@link Field} to obtain the read {@link Method}
	 *            for.
	 * @return Result of invoking the {@link Method}.
	 * 
	 * @see #getWriteMethodAndInvoke(Object, String, Object)
	 * @see #invoke(Method, Object, Object...)
	 */
	public static Object getReadMethodAndInvoke(final Object object, final String name) {
		if (isNotNullOrEmpty(object)) {
			return invoke(getReadMethod(object.getClass(), name), object);
		} else {
			LOG.warn("Object to read from cannot be null!");
		}
		return null;
	}

	/**
	 * Obtains the size of the given {@link File} in bytes
	 * 
	 * @param file
	 *            {@link File} to get size.
	 * @return {@link File} size in bytes.
	 */
	public static long getSize(final File file) {
		if (isNotNullAndExists(file)) {
			try {
				final BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class,
						LinkOption.NOFOLLOW_LINKS);
				if (attr != null) {
					return attr.size();
				}
			} catch (final IOException e) {
				LOG.error(String.format("Problem getting file size: %s", file.getAbsolutePath()), e);
			}
		} else {
			LOG.warn("The file you want the size of must not be null and must exists!");
		}
		return 0L;
	}

	public static File getSystemTempDirectory() {
		final String path = System.getProperty(SYS_TEMP_DIR);
		if (isNotNullOrEmpty(path)) {
			final File file = new File(path);
			if (isNotNullAndExists(file) && file.isDirectory()) {
				return file;
			}
		} else {
			LOG.warn("Could not determine system temporary directory!");
		}
		return null;
	}

	/**
	 * Finds and invokes a single parameter {@link Method} from the provided
	 * {@link Object} using the provided {@link Field} name and {@link Class} of
	 * the provided parameter {@link Object} to locate the {@link Method}.
	 * 
	 * @param object
	 *            {@link Object} to invoke the {@link Method} on.
	 * @param name
	 *            Name of the {@link Field} to obtain the write {@link Method}
	 *            for.
	 * @param param
	 *            {@link Object} to write to with the {@link Method}.
	 * 
	 * @see #getReadMethodAndInvoke(Object, String)
	 * @see #invoke(Method, Object, Object...)
	 */
	public static void getWriteMethodAndInvoke(final Object object, final String name, final Object param) {
		if (isNotNullOrEmpty(object)) {
			final Method method = getWriteMethod(object.getClass(), name);
			if (isNotNullOrEmpty(method)) {
				final Class<?> type = getParameterType(method);
				if (isNotNullOrEmpty(type) && isNotNullOrEmpty(param)) {
					if (type.equals(param.getClass())) {
						invoke(method, object, param);
					} else {
						LOG.trace(
								"Param type {} from method {} for the field {} and param type {} from value {} do not match for the object type {}!",
								type.getName(), method.getName(), name, param.getClass().getName(), object.toString(),
								object.getClass().getName());
						invoke(method, object, cast(type, param));
					}
				}
			} else {
				LOG.warn(String.format("Could not find write method using %s as the field name for class: %s", name,
						object.getClass().getName()));
			}
		} else {
			LOG.warn("The object to write to cannot be null!");
		}
	}

	/**
	 * Creates an instance of the provided {@link Class}. The {@link Class} must
	 * not be an interface and must have a default (no-parameter) constructor.
	 * 
	 * @param type
	 *            {@link Class} to instantiate.
	 * @param <T>
	 *            Expected type to be returned.
	 * @return Instance of the provided {@link Class}, if successful, otherwise,
	 *         null.
	 * 
	 * @see #instantiate(String)
	 */
	public static <T> T instantiate(final Class<T> type) {
		if (isNotNullOrEmpty(type)) {
			if (!type.isInterface()) {
				if (hasDefaultConstructor(type)) {
					try {
						return type.newInstance();
					} catch (final InstantiationException | IllegalAccessException e) {
						LOG.error(String.format("Problem instantiating the class: %s", type.getName()), e);
					}
				} else {
					LOG.warn(String.format("Class must have a default (no-parameter) constructor!\nCLASS: %s",
							type.getName()));
				}
			} else {
				LOG.warn("Class cannot be an interface.");
			}
		} else {
			LOG.warn("Class cannot be null!");
		}
		return null;
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

	public static boolean isNotNullOrEmpty(final Map<?, ?> map) {
		return (map != null && !map.isEmpty());
	}

	public static boolean isNotNullOrEmpty(final Object object) {
		return (object != null && !object.toString().trim().isEmpty());
	}

	public static boolean isNotNullOrEmpty(final Object[] object) {
		return (object != null && object.length > 0);
	}

	public static boolean isNotNullOrEmpty(final Content content) {
		return (content != null && content.isValid());
	}

	public static boolean isNotNullOrEmpty(final Instance instance) {
		return (instance != null && instance.isValid());
	}

	public static boolean isNotNullOrEmpty(final TokenDefinition token) {
		return (token != null && token.isValid());
	}

	public static boolean isNotNullOrZero(final Number val) {
		return (val != null && val.longValue() != 0);
	}

	public static String read(final File file) {
		if (isNotNullAndExists(file)) {
			final long limit = 1073741824L; // 1GB
			final long size = getSize(file);
			if (limit > size) {
				try {
					final byte[] bytes = Files.readAllBytes(file.toPath());
					return new String(bytes, Charset.forName("UTF-8"));
				} catch (final IOException e) {
					LOG.error(String.format("Problem reading file: %s", file.getAbsolutePath()), e);
				}
			}
		} else {
			LOG.warn("The file you are trying to read must not by null and must exist!");
		}
		return null;
	}

	public static File write(final File file, final String string) {
		if (isNotNullAndExists(file)) {
			if (string != null) {
				try (final FileWriter writer = new FileWriter(file)) {
					writer.write(string);
					writer.flush();
					return file;
				} catch (final IOException e) {
					LOG.error(String.format("Problem writing file: %s", file.getAbsolutePath()), e);
				}
			} else {
				LOG.warn("The text you want to write to the file must not be null!\nFILE: {}", file.getAbsolutePath());
			}
		} else {
			LOG.warn("The file you want to write to cannot be null and must exist!");
		}
		return null;
	}

	private static Object cast(final Class<?> type, final Object object) {
		if (isNotNullOrEmpty(type)) {
			if (isNotNullOrEmpty(object)) {
				try {
					if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
						if (object instanceof Double) {
							final Double dub = Double.valueOf((double) object);
							final Integer i = dub != null ? Integer.valueOf(dub.intValue()) : null;
							return i;
						} else if (!(object instanceof Date)) {
							return Integer.valueOf(String.valueOf(object));
						}
					} else if (type.equals(Long.class) || type.equals(Long.TYPE)) {
						if (object instanceof Double) {
							final Double dub = Double.valueOf((double) object);
							final Long l = dub != null ? Long.valueOf(dub.intValue()) : null;
							return l;
						} else if (object instanceof Date) {
							return Long.valueOf(((Date) object).getTime());
						} else if (object instanceof java.util.Date) {
							return Long.valueOf(((java.util.Date) object).getTime());
						} else {
							return Long.valueOf(String.valueOf(object));
						}
					} else if (type.equals(Date.class)) {
						if (object instanceof Long) {
							return new Date(Long.valueOf(String.valueOf(object)));
						}
					} else if (type.equals(java.util.Date.class)) {
						if (object instanceof Long) {
							return new java.util.Date(Long.valueOf(String.valueOf(object)));
						} else if (object instanceof String) {
							Instant instant = Instant.parse(object.toString());
							return new java.util.Date(instant.toEpochMilli());
							// return java.util.Date.from(instant);
						}
					} else if (type.equals(Double.class)) {
						if (!(object instanceof Date)) {
							return Double.valueOf(String.valueOf(object));
						}
					} else if (type.equals(Timestamp.class)) {
						if (!(object instanceof Date)) {
							return Timestamp.valueOf(object.toString());
						}
					} else if (type.equals(String.class)) {
						return String.valueOf(object);
					}
				} catch (final Exception e) {
					LOG.error(String.format("Problem casting %s to %s!", object.getClass().getName(), type.getName()),
							e);
				}
				return object;
			} else {
				LOG.warn("The object you are trying to cast must not be null or empty!");
			}
		} else {
			LOG.warn("The class you are trying to cast to must not be null or empty!");
		}
		return null;
	}

	private static File create(final File file, final boolean overwrite) {
		if (file != null) {
			final boolean exists = file.exists();
			if (exists && overwrite) {
				if (!delete(file)) {
					LOG.warn(String.format("Delete failed! File cannot be overwritten: Source: %s",
							file.getAbsolutePath()));
					return file;
				}
			} else if (exists && !overwrite) {
				LOG.trace(String.format("File is marked to not be overwritten: Source: %s", file.getAbsolutePath()));
				return file;
			} else if (!exists) {
				if (file.getParentFile() != null && !file.getParentFile().exists()) {
					file.getParentFile().mkdirs();
				}
			}
			try {
				return (file.createNewFile() ? file : null);
			} catch (final IOException e) {
				LOG.error(String.format("Problem creating file %s", file.getAbsolutePath()), e);
			}
		} else {
			LOG.warn("File object must not be null!");
		}
		return null;
	}

	private static File createDirectory(final String path) {
		if (isNotNullOrEmpty(path)) {
			File file = get(path);
			if (!isNotNullAndExists(file)) {
				file = new File(path);
				if (file.mkdirs()) {
					if (isNotNullAndExists(file) && file.isDirectory()) {
						return file;
					}
				}
			} else if (isNotNullAndExists(file) && file.isDirectory()) {
				return file;
			}
		} else {
			LOG.warn("Path must not be null or empty.");
		}
		return null;
	}

	private static Class<?> getParameterType(final Method method) {
		if (isNotNullOrEmpty(method)) {
			final int count = method.getParameterCount();
			final String name = method.getName();
			final Class<?> type = method.getParameters()[0].getType();
			if (count == 1) {
				LOG.trace("Found one parameter in the method {}, its class is {}.", name, type.getName());
				return type;
			} else if (count > 1) {
				LOG.trace("Too many parameters found for method: {}!", name);
			} else {
				LOG.trace("No parameters found for method: {}!", name);
			}
		} else {
			LOG.warn("Method cannot be null!");
		}
		return null;
	}

	private static File getNewFileForCopy(final File file) {
		if (isNotNullAndExists(file)) {
			String name = file.getName();
			final String parentDirName = file.getParentFile().getAbsolutePath();
			int version = 1;
			if (!name.startsWith(PREFIX_COPY_OF)) {
				name = PREFIX_COPY_OF.concat(file.getName());
			}
			File copy = new File(parentDirName.concat(File.separator.concat(name)));
			while (isNotNullAndExists(copy)) {
				final String ver = String.format(" (%s)", String.valueOf(version));
				final String[] splits = name.split(REGEX_SPLIT_PATH);
				String newName = splits[0];
				String ext = "";
				if (!file.isDirectory() && (splits.length > 1)) {
					ext = String.format(".%s", splits[1]);
				}
				copy = new File(parentDirName.concat(File.separator.concat(newName.concat(ver.concat(ext)))));
				version++;
			}
			return copy;
		} else {
			LOG.warn("File to make a copy of must not be null and must exist!");
		}
		return null;
	}

	private static Method getReadMethod(final Class<?> clazz, final String name) {
		if (isNotNullOrEmpty(clazz)) {
			if (isNotNullOrEmpty(name)) {
				try {
					final BeanInfo info = Introspector.getBeanInfo(clazz);
					if (isNotNullOrEmpty(info)) {
						for (final PropertyDescriptor p : info.getPropertyDescriptors()) {
							if (p.getName().equals(name)) {
								return p.getReadMethod();
							}
						}
					}
				} catch (final IntrospectionException e) {
					LOG.error(String.format("Cannot get read method named field %s from class: %s!", name,
							clazz.getName()), e);
				}
			} else {
				LOG.warn(String.format("Field name cannot be null or empty!\nCLASS: %s", clazz.getName()));
			}
		} else {
			LOG.warn("Class and/or field name cannot be null!");
		}
		return null;
	}

	private static <T> Method getWriteMethod(final Class<T> type, final String name) {
		if (isNotNullOrEmpty(type)) {
			if (isNotNullOrEmpty(name)) {
				try {
					final BeanInfo info = Introspector.getBeanInfo(type, Object.class);
					final PropertyDescriptor[] properties = info.getPropertyDescriptors();
					for (final PropertyDescriptor p : properties) {
						if (p.getName().equals(name)) {
							return p.getWriteMethod();
						}
					}
				} catch (final IntrospectionException e) {
					LOG.error(String.format("Cannot get write method named %s from class: %s!", name, type.getName()),
							e);
				}
			} else {
				LOG.warn(String.format("Field name cannot be null or empty!\nCLASS: %s", type.getName()));
			}
		} else {
			LOG.warn("Class cannot be null!");
		}
		return null;
	}

	private static boolean hasDefaultConstructor(final Class<?> clazz) {
		if (isNotNullOrEmpty(clazz)) {
			for (final Constructor<?> c : clazz.getConstructors()) {
				if (c.getParameterCount() == 0) {
					return true;
				}
			}
		} else {
			LOG.warn("Class cannot be null!");
		}
		return false;
	}

	private static Object invoke(final Method method, final Object object, final Object... params) {
		if (isNotNullOrEmpty(method)) {
			method.setAccessible(true);
			final boolean isStatic = Modifier.isStatic(method.getModifiers());
			try {
				if (isStatic) {
					return method.invoke(null, params);
				} else if (!isStatic && isNotNullOrEmpty(object)) {
					return method.invoke(object, params);
				} else {
					LOG.warn(String.format("Object was null and method was not static!\nMETHOD: %s", method.getName()));
				}
			} catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				LOG.error(String.format("Problem invoking method: %s!", method.getName()), e);
			}
		} else {
			LOG.warn("Method cannot be null!");
		}
		return null;
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

	public static final class Constants {
		public static final String AUTOWIRE_QUALIFIER_DOCUMENT = "document";
		public static final String AUTOWIRE_QUALIFIER_SECTION = "section";
		public static final String AUTOWIRE_QUALIFIER_CLAUSE = "clause";
		public static final String AUTOWIRE_QUALIFIER_PARAGRAPH = "paragraph";
		public static final String AUTOWIRE_QUALIFIER_JDBC_TX = "jdbcTransactionManager";
		public static final String AUTOWIRE_QUALIFIER_ORM_TX = "transactionManager";
		public static final String AUTOWIRE_QUALIFIER_JDBC = "jdbc";
		public static final String AUTOWIRE_QUALIFIER_ORM = "orm";
		public static final String AUTOWIRE_QUALIFIER_ENDPOINT = "endpoint";

		public static final String ENGINE_CONTROL_STOP = "stop";

		public static final String PROPERTY_DEFAULT = "classpath:default.properties";
		public static final String PROPERTY_CUSTOM = "file:${DOC_ENGINE_HOME}/config/custom.properties";

		public static final String SYSTEM_VARIABLE_FOR_HOME = "DOC_ENGINE_HOME";
	}
	
	public enum HTML {
		BODY("body"), BR("br"), DIV("div"), DOCUMENT("html"), H1("h1"), H2("h2"), H3("h3"), H4("h4"), HEAD("head"), HR(
				"hr"), LI("li"), OL("ol"), P("p"), SPAN("span"), STYLE("style"), TABLE("table"), TITLE("title");

		private static final Logger LOG = LoggerFactory.getLogger(HTML.class);
		private static final String ATTR_CSS_CLASS = " class=\"%s\"";
		private static final String ATTR_CSS_TYPE = "type=\"%s\"";
		private static final String BRACKET_CLOSE = ">";
		private static final String BRACKET_OPEN = "<";
//		private static final String HEADER_HTML5 = "<!DOCTYPE html>";
		private static final String HEADER_XHTML_STRICT = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">";
		private static final String NAMESPACE = "xmlns=\"http://www.w3.org/1999/xhtml\"";
//		private static final String NON_BREAKING_SPACE = "&nbsp;";
		private static final String XML_NON_BREAKING_SPACE = "&#160;";
		private static final String SLASH = "/";

		private final String tag;

		private HTML(final String tag) {
			this.tag = tag;
		}

		public static String doctype() {
			return HEADER_XHTML_STRICT;
		}

		public static String namespace() {
			return NAMESPACE;
		}

		public static String space() {
			return XML_NON_BREAKING_SPACE;
		}

		public static String tab() {
			return String.format("%s%s%s", space(), space(), space());
		}

		public final String close() {
			return String.format("%s%s%s%s", BRACKET_OPEN, SLASH, tag, BRACKET_CLOSE);
		}

		public final String open() {
			return open(null);
		}

		public final String open(final String cssClass) {
			return open(cssClass, null);
		}

		public final String open(final String cssClass, final String inline) {
			String css = null;
			if (isNotNullOrEmpty(cssClass)) {
				css = String.format(ATTR_CSS_CLASS, cssClass);
			}
			if (isNotNullOrEmpty(inline)) {
				if (isNotNullOrEmpty(css)) {
					return String.format("%s%s %s %s %s", BRACKET_OPEN, tag, css, inline, BRACKET_CLOSE);
				} else {
					return String.format("%s%s %s %s", BRACKET_OPEN, tag, inline, BRACKET_CLOSE);
				}
			} else {
				if (isNotNullOrEmpty(css)) {
					return String.format("%s%s %s %s", BRACKET_OPEN, tag, css, BRACKET_CLOSE);
				} else {
					return String.format("%s%s%s", BRACKET_OPEN, tag, BRACKET_CLOSE);
				}
			}
		}

		public final String self() {
			return self(null);
		}

		public final String self(final String cssClass) {
			return self(cssClass, null);
		}

		public final String self(final String cssClass, final String inline) {
			String html = null;
			String css = null;
			if (isNotNullOrEmpty(cssClass)) {
				css = String.format(ATTR_CSS_CLASS, inline);
			}
			if (isNotNullOrEmpty(inline)) {
				if (isNotNullOrEmpty(css)) {
					html = String.format("%s%s %s %s %s%s", BRACKET_OPEN, tag, css, inline, SLASH, BRACKET_CLOSE);
				} else {
					html = String.format("%s%s %s %s%s", BRACKET_OPEN, tag, inline, SLASH, BRACKET_CLOSE);
				}
			} else {
				if (isNotNullOrEmpty(css)) {
					html = String.format("%s%s %s %s%s", BRACKET_OPEN, tag, css, SLASH, BRACKET_CLOSE);
				} else {
					html = String.format("%s%s %s%s", BRACKET_OPEN, tag, SLASH, BRACKET_CLOSE);
				}
			}
			if (isNotNullOrEmpty(html)) {
				return html;
			}
			return "";			
		}
		
		public final String style(final String css, final String type) {
			if (this.equals(STYLE)) {
				if (isNotNullOrEmpty(css)) {
					return STYLE.wrap(css, null, String.format(ATTR_CSS_TYPE, type));
				} else {
					return STYLE.wrap(null, null, String.format(ATTR_CSS_TYPE, type));
				}				
			}
			return "";
		}
		
		public final String tag() {
			return tag;
		}

		public final String wrap(final String html) {
			return wrap(html, null);
		}

		public final String wrap(final String html, final String cssClass) {
			return wrap(html, cssClass, null);
		}

		public final String wrap(final String html, final String cssClass, final String inline) {
			LOG.trace("Wrapping HTML with the tag {} using CSS class {} and inline attributes {}. HTML: {}", this.tag, cssClass, inline, html);
			return String.format("%s%s%s", open(cssClass, inline), html, close());
		}
	}
	
	public static class RomanNumber {
		private final static TreeMap<Integer, String> INT_TO_ROMAN_MAP = new TreeMap<Integer, String>();
		static {
			INT_TO_ROMAN_MAP.put(1000, "M");
			INT_TO_ROMAN_MAP.put(900, "CM");
			INT_TO_ROMAN_MAP.put(500, "D");
			INT_TO_ROMAN_MAP.put(400, "CD");
			INT_TO_ROMAN_MAP.put(100, "C");
			INT_TO_ROMAN_MAP.put(90, "XC");
			INT_TO_ROMAN_MAP.put(50, "L");
			INT_TO_ROMAN_MAP.put(40, "XL");
			INT_TO_ROMAN_MAP.put(10, "X");
			INT_TO_ROMAN_MAP.put(9, "IX");
			INT_TO_ROMAN_MAP.put(5, "V");
			INT_TO_ROMAN_MAP.put(4, "IV");
			INT_TO_ROMAN_MAP.put(1, "I");
		}

		public static String toRoman(final int number) {
			final int l = INT_TO_ROMAN_MAP.floorKey(number);
			if (number == l) {
				return INT_TO_ROMAN_MAP.get(number);
			}
			return INT_TO_ROMAN_MAP.get(l) + toRoman(number - l);
		}
	}

	public static class TidyFactory {
		public final Tidy getTidy() {
			final Tidy tidy = new Tidy();
			tidy.setXmlOut(true);
			tidy.setXmlTags(true);
			tidy.setXHTML(true);
			tidy.setShowWarnings(true);
			tidy.setQuiet(true);
			tidy.setIndentContent(true);
			tidy.setIndentCdata(true);
			tidy.setSmartIndent(true);
			tidy.setMakeClean(true);
			return tidy;				
		}
	}
}