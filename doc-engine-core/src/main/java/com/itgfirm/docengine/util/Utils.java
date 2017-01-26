/**
 * TODO: License
 */
package com.itgfirm.docengine.util;

import static com.itgfirm.docengine.util.Utils.UtilsConstants.*;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
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

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itgfirm.docengine.types.ContentJpaImpl;
import com.itgfirm.docengine.types.InstanceJpaImpl;
import com.itgfirm.docengine.types.TokenDefinitionJpaImpl;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public class Utils {
	private static final Logger LOG = LoggerFactory.getLogger(Utils.class);
	
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
			file = new File(path);
			try {
				URL url = null;
				if (!isNotNullAndExists(file)) {
					url = Utils.class.getResource(path);
					if (url != null) {
						file = new File(url.toURI());
					}
				}
				if (!isNotNullAndExists(file)) {
					url = Thread.currentThread().getContextClassLoader().getResource(path);
					if (url != null) {
						file = new File(url.toURI());
					}
				}
			} catch (final URISyntaxException e) {
				LOG.error(String.format("Problem getting URI: %s", path), e);
			}
			if (!isNotNullAndExists(file)) {
				try {
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
					LOG.warn(
							"A value is required to check the annotation {} in the class {} for the associated field!",
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
					LOG.warn(
							String.format("Object was null and method was not static!\nMETHOD: %s", method.getName()));
				}
			} catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				LOG.error(String.format("Problem invoking method: %s!", method.getName()), e);
			}
		} else {
			LOG.warn("Method cannot be null!");
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
//							return java.util.Date.from(instant);
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

	static class UtilsConstants {
		static final String PREFIX_COPY_OF = "Copy of - ";
		static final String REGEX_SPLIT_PATH = "\\.(?=[^\\.]+$)";
	}
}