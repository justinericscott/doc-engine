/**
 * TODO: License
 */
package com.itgfirm.docengine.util;

import static com.itgfirm.docengine.DocEngine.Constants.*;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
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
						LOG.debug(String.format("Copy Failed: Source: %s | Copy: %s!", file.getAbsolutePath(),
								copy.getAbsolutePath()));
					}
				} else {
					Files.copy(file.toPath(), copy.toPath(), StandardCopyOption.REPLACE_EXISTING);
					if (isNotNullAndExists(copy)) {
						return copy;
					} else {
						LOG.debug(String.format("Copy Failed: Source: %s | Copy: %s!", file.getAbsolutePath(),
								copy.getAbsolutePath()));
					}
				}
			} catch (final IOException e) {
				LOG.error(String.format("Problem copying file: Source: %s!", file.getAbsolutePath()), e);
			}
		} else {
			LOG.debug("Problem creating copied File object!");
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
			LOG.debug("Path must not be null or empty!");
		}
		return null;
	}

	/**
	 * Creates an empty {@link File} on disk.<br>
	 * <br>
	 * If a file or directory exists with the same path and name, it will not be
	 * overwritten.
	 * 
	 * @param file
	 *            {@link File} to create.
	 * @return {@link File} object representing the new file.
	 * @see #create(File, String)
	 * @see #create(String)
	 */
	public static File create(final File file) {
		return create(file, true);
	}

	/**
	 * Creates an empty {@link File} at the given path.<br>
	 * <br>
	 * If a file or directory exists with the same path and name, it will not be
	 * overwritten.
	 * 
	 * @param parent
	 *            {@link File} representing the target directory to create the
	 *            new file in.
	 * @param name
	 *            Name of the {@link File} to create.
	 * @return {@link File} object representing the new file.
	 * @throws RuntimeException
	 *             When an {@link IOException} is thrown.
	 * @see #create(File)
	 * @see #create(String)
	 */
	public static File create(final File parent, final String name) {
		return create(parent, name, true);
	}

	private static File create(final File parent, final String name, final boolean overwrite) {
		if (isNotNullAndExists(parent)) {
			if (isNotNullOrEmpty(name)) {
				if (parent.isDirectory()) {
					return create(parent.getAbsolutePath().concat(File.separator.concat(name)), overwrite);
				} else if (parent.isFile()) {
					return create(parent.getParentFile().getAbsolutePath().concat(File.separator.concat(name)),
							overwrite);
				}
			} else {
				LOG.debug(String.format("Name cannot be null or empty!\nPARENT FILE: %s", parent.getAbsolutePath()));
			}
		} else {
			LOG.debug("Parent File object cannot be null and must exist!");
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
			LOG.debug("Class name cannot be null or empty!");
		}
		return null;
	}

	/**
	 * Returns the desired {@link Method} for the provided {@link Class}, name
	 * and the parameter types.
	 * 
	 * @param clazz
	 * @param name
	 * @param params
	 * @return
	 */
	public static Method find(final Class<?> clazz, final String name, final Class<?>... params) {
		if (isNotNullOrEmpty(clazz)) {
			if (isNotNullOrEmpty(name)) {
				for (final Method m : clazz.getDeclaredMethods()) {
					if (name.equals(m.getName()) && equals(params, m.getParameterTypes())) {
						return m;
					}
				}
			} else {
				LOG.debug("Method name cannot be null or empty!");
			}
		} else {
			LOG.debug("Class cannot be null!");
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
		if (isNotNullOrEmpty(path)) {
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
			LOG.debug("File path must not be null or empty!");
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
								LOG.debug("Found annnotation {} with the value {} in the class {} for the field {}.",
										anno.getClass().getName(), value, clazz.getName(), name);
								return field.getName();
							}
						}
					}
				} else {
					LOG.debug(
							"A value is required to check the annotation {} in the class {} for the associated field!",
							annotation.getName(), clazz.getName());
				}
			} else {
				LOG.debug("Excel annotation class in the provided class {} must not be null!", clazz.getName());
			}
		} else {
			LOG.debug("Class cannot be null!");
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
			LOG.debug("The file you want the size of must not be null and must exists!");
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
			LOG.debug("Object to read from cannot be null!");
		}
		return null;
	}

	/**
	 * Obtains a {@link File} object for the host operating system's temporary
	 * directory.
	 * 
	 * @return {@link File} representing the temporary directory.
	 */
	public static File getSystemTempDirectory() {
		final String path = System.getProperty(FILE_SYS_TEMP_DIR);
		if (isNotNullOrEmpty(path)) {
			final File file = new File(path);
			return (isNotNullAndExists(file) ? file : null);
		} else {
			LOG.debug("Could not determine system temporary directory!");
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
						LOG.debug(
								"Param type {} from method {} for the field {} and param type {} from value {} do not match for the object type {}!",
								type.getName(), method.getName(), name, param.getClass().getName(), object.toString(),
								object.getClass().getName());
						invoke(method, object, cast(type, param));
					}
				}
			} else {
				LOG.debug(String.format("Could not find write method using %s as the field name for class: %s", name,
						object.getClass().getName()));
			}
		} else {
			LOG.debug("The object to write to cannot be null!");
		}
	}

	public static boolean isAllSections(final Iterable<? extends ContentJpaImpl> contents) {
		if (isNotNullOrEmpty(contents)) {
			for (final Object content : contents) {
				if (!(content instanceof SectionJpaImpl))
					return false;
			}
			return true;
		}
		return false;
	}

	public static boolean isAllClauses(final Iterable<? extends ContentJpaImpl> contents) {
		if (isNotNullOrEmpty(contents)) {
			for (final Object content : contents) {
				if (!(content instanceof ClauseJpaImpl))
					return false;
			}
			return true;
		}
		return false;
	}

	public static boolean isAllParagraphs(final Iterable<? extends ParagraphJpaImpl> contents) {
		if (isNotNullOrEmpty(contents)) {
			for (final Object content : contents) {
				if (!(content instanceof ParagraphJpaImpl))
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

	/**
	 * Finds and creates an instance of the provided {@link Class} matching the
	 * provided name.
	 * 
	 * @param name
	 *            Name of the {@link Class} to find and instantiate.
	 * @return {@link Object} of the {@link Class} matching the provided name or
	 *         null.
	 * 
	 * @see #instantiate(Class)
	 */
	public static Object instantiate(final String name) {
		return instantiate(find(name));
	}

	/**
	 * Creates an instance of the provided {@link Class}. The {@link Class} must
	 * not be an interface and must have a default (no-parameter) constructor.
	 * 
	 * @param clazz
	 *            {@link Class} to instantiate.
	 * @param <T>
	 *            Expected type to be returned.
	 * @return Instance of the provided {@link Class}, if successful, otherwise,
	 *         null.
	 * 
	 * @see #instantiate(String)
	 */
	public static <T> T instantiate(final Class<T> clazz) {
		if (isNotNullOrEmpty(clazz)) {
			if (!clazz.isInterface()) {
				if (hasDefaultConstructor(clazz)) {
					try {
						return clazz.newInstance();
					} catch (final InstantiationException | IllegalAccessException e) {
						LOG.error(String.format("Problem instantiating the class: %s", clazz.getName()), e);
					}
				} else {
					LOG.debug(String.format("Class must have a default (no-parameter) constructor!\nCLASS: %s",
							clazz.getName()));
				}
			} else {
				LOG.debug("Class cannot be an interface.");
			}
		} else {
			LOG.debug("Class cannot be null!");
		}
		return null;
	}

	/**
	 * Finds and invokes a {@link Method} in the provided {@link Class} matching
	 * the provided method name. If an {@link Object} is provided and the
	 * {@link Method} is not {@code static}, the {@link Method} will be invoked
	 * on the {@link Object}. If the {@link Method} is {@code static}, it will
	 * be invoked on the {@link Class} and the {@link Object} will be ignored.
	 * 
	 * @param clazz
	 *            {@link Class} to search.
	 * @param object
	 *            {@link Object} to invoke {@link Method} on.
	 * @param name
	 *            Name of the {@link Method} to search for and invoke.
	 * @param params
	 *            {@link Class}es that match the provided {@link Method}
	 *            parameters, in order.
	 * @return Return value of the invoked {@link Method}. If the return type is
	 *         void or if unsuccessful null is returned.
	 * 
	 * @see #invoke(Class, String)
	 * @see #invoke(Method, Object, Object...)
	 */
	public static Object invoke(final Class<?> clazz, final Object object, final String name, final Object... params) {
		return invoke(find(clazz, name, getTypes(params)), object, params);
	}

	/**
	 * Finds and invokes a static {@link Method} in the provided {@link Class}
	 * matching the provided method name without any parameters.
	 * 
	 * @param clazz
	 *            {@link Class} to search.
	 * @param name
	 *            Name of the {@link Method} to search for and invoke. The
	 *            {@link Method} must be {@code static} and must not have any
	 *            parameters.
	 * @return Return value of the invoked {@link Method}. If the return type is
	 *         void, null is returned.
	 * 
	 * @see #invoke(Class, Object, String, Object...)
	 */
	public static Object invoke(final Class<?> clazz, final String name) {
		return invoke(clazz, (Object) null, name);
	}

	/**
	 * Invoke the provided {@link Method} with the given {@link Object} and
	 * optional parameter values as {@link Object}s. If no {@link Object} is
	 * given to invoke with, simply provide <code>null</code> and it will be
	 * invoked statically. If the {@link Method} is not static, an
	 * {@link Object} must be provided,
	 * 
	 * @param method
	 *            {@link Method} to invoke.
	 * @param object
	 *            {@link Object} to invoke the {@link Method} on, if needed, it
	 *            can be null if the {@link Method} is static.
	 * @param params
	 *            The values of the parameters of the {@link Method}. Optional
	 * @return {@link Object} result of the invoked {@link Method} if
	 *         successful, otherwise, null.
	 * 
	 * @see #invoke(Class, String)
	 * @see #invoke(Class, Object, String, Object...)
	 * @see #getReadMethodAndInvoke(Object, String)
	 * @see #getWriteMethodAndInvoke(Object, String, Object)
	 */
	public static Object invoke(final Method method, final Object object, final Object... params) {
		if (isNotNullOrEmpty(method)) {
			method.setAccessible(true);
			final boolean isStatic = Modifier.isStatic(method.getModifiers());
			try {
				if (isStatic) {
					return method.invoke(null, params);
				} else if (!isStatic && isNotNullOrEmpty(object)) {
					return method.invoke(object, params);
				} else {
					LOG.debug(
							String.format("Object was null and method was not static!\nMETHOD: %s", method.getName()));
				}
			} catch (final IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				LOG.error(String.format("Problem invoking method: %s!", method.getName()), e);
			}
		} else {
			LOG.debug("Method cannot be null!");
		}
		return null;
	}

	/**
	 * Provides a {@link List} of all {@link File}s in the given directory or
	 * directory structure.
	 * 
	 * If a file extension is provided, only those files matching the given
	 * extension are returned.
	 * 
	 * @param directory
	 *            Directory to list {@link File}s for.
	 * @param extension
	 *            File extension to filter on.
	 * @param recursive
	 *            Flag to recursively list all files in the directory
	 *            structure..
	 * @return {@link List} of {@link File} objects or null.
	 */
	public static Iterable<File> list(final File directory, final String extension, final boolean recursive) {
		if (isNotNullAndExists(directory) && directory.isDirectory()) {
			final File[] array = directory.listFiles();
			if (array.length > 0) {
				final Collection<File> files = new ArrayList<File>();
				for (final File f : array) {
					if (f.isFile()) {
						if (!isNotNullOrEmpty(extension)) {
							files.add(f);
						} else if (f.getName().endsWith(extension)) {
							files.add(f);
						}
					} else if (f.isDirectory()) {
						if (recursive) {
							final Collection<File> recurse = (Collection<File>) list(f, extension, recursive);
							if (isNotNullOrEmpty(recurse)) {
								files.addAll(recurse);
							}
						}
					}
				}
				if (!files.isEmpty()) {
					return files;
				}
			}
		} else {
			LOG.debug("Directory object must not be null, it must be a directory and exist!");
		}
		return null;
	}

	private static Object cast(final Class<?> target, final Object object) {
		if (isNotNullOrEmpty(target)) {
			if (isNotNullOrEmpty(object)) {
				try {
					if (target.equals(Integer.class) || target.equals(Integer.TYPE)) {
						if (object instanceof Double) {
							final Double dub = Double.valueOf((double) object);
							final Integer i = dub != null ? Integer.valueOf(dub.intValue()) : null;
							return i;
						} else if (!(object instanceof Date)) {
							return Integer.valueOf(String.valueOf(object));
						}
					} else if (target.equals(Long.class) || target.equals(Long.TYPE)) {
						if (object instanceof Double) {
							final Double dub = Double.valueOf((double) object);
							final Long l = dub != null ? Long.valueOf(dub.intValue()) : null;
							return l;
						} else if (object instanceof Date) {
							return Long.valueOf(((Date) object).getTime());
						} else {
							return Long.valueOf(String.valueOf(object));
						}
					} else if (target.equals(Date.class)) {
						if (object instanceof Long) {
							return new Date(Long.valueOf(String.valueOf(object)));
						}
					} else if (target.equals(Double.class)) {
						if (!(object instanceof Date)) {
							return Double.valueOf(String.valueOf(object));
						}
					} else if (target.equals(Timestamp.class)) {
						if (!(object instanceof Date)) {
							return Timestamp.valueOf(object.toString());
						}
					} else if (target.equals(String.class)) {
						return String.valueOf(object);
					} else {
						return object;
					}
				} catch (final ClassCastException | NumberFormatException e) {
					LOG.error(String.format("Problem casting %s to %s!", object.getClass().getName(), target.getName()),
							e);
				}
			} else {
				LOG.debug("The object you are trying to cast must not be null or empty!");
			}
		} else {
			LOG.debug("The class you are trying to cast to must not be null or empty!");
		}
		return null;
	}

	private static File create(final File file, final boolean overwrite) {
		if (file != null) {
			final boolean exists = file.exists();
			if (exists && overwrite) {
				if (!delete(file)) {
					LOG.debug(String.format("Delete failed! File cannot be overwritten: Source: %s",
							file.getAbsolutePath()));
					return file;
				}
			} else if (exists && !overwrite) {
				LOG.debug(String.format("File is marked to not be overwritten: Source: %s", file.getAbsolutePath()));
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
			LOG.debug("File object must not be null!");
		}
		return null;
	}

	private static boolean equals(final Class<?>[] search, final Class<?>[] method) {
		return (!isNotNullOrEmpty(search) && !isNotNullOrEmpty(method)) || Arrays.equals(search, method);
	}

	private static Class<?> getParameterType(final Method method) {
		if (isNotNullOrEmpty(method)) {
			final int count = method.getParameterCount();
			final String name = method.getName();
			final Class<?> type = method.getParameters()[0].getType();
			if (count == 1) {
				LOG.debug("Found one parameter in the method {}, its class is {}.", name, type.getName());
				return type;
			} else if (count > 1) {
				LOG.debug("Too many parameters found for method: {}!", name);
			} else {
				LOG.debug("No parameters found for method: {}!", name);
			}
		} else {
			LOG.debug("Method cannot be null!");
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
			LOG.debug("File to make a copy of must not be null and must exist!");
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
				LOG.debug(String.format("Field name cannot be null or empty!\nCLASS: %s", clazz.getName()));
			}
		} else {
			LOG.debug("Class and/or field name cannot be null!");
		}
		return null;
	}

	private static Class<?>[] getTypes(final Object... objects) {
		if (isNotNullOrEmpty(objects)) {
			Class<?>[] types = null;
			int i = 0;
			types = new Class<?>[objects.length];
			for (final Object o : objects) {
				types[i] = o.getClass();
				i++;
			}
			if (types.length > 0) {
				return types;
			}
		} else {
			LOG.debug("Object(s) cannot be null!");
		}
		return null;
	}

	private static Method getWriteMethod(final Class<?> clazz, final String name) {
		if (isNotNullOrEmpty(clazz)) {
			if (isNotNullOrEmpty(name)) {
				try {
					final BeanInfo info = Introspector.getBeanInfo(clazz, Object.class);
					final PropertyDescriptor[] properties = info.getPropertyDescriptors();
					for (final PropertyDescriptor p : properties) {
						if (p.getName().equals(name)) {
							return p.getWriteMethod();
						}
					}
				} catch (final IntrospectionException e) {
					LOG.error(String.format("Cannot get write method named %s from class: %s!", name, clazz.getName()),
							e);
				}
			} else {
				LOG.debug(String.format("Field name cannot be null or empty!\nCLASS: %s", clazz.getName()));
			}
		} else {
			LOG.debug("Class cannot be null!");
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
			LOG.debug("Class cannot be null!");
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

	private Utils() {
		// Do not instantiate
	}
}