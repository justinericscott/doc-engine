/**
 * TODO: License
 */
package com.itgfirm.docengine.util;

/**
 * @author Justin Scott<br>
 * 
 *         TODO: Description
 */
public final class Constants {

	public static final String AUTOWIRE_QUALIFIER_DEFAULT = "default";
	public static final String AUTOWIRE_QUALIFIER_ADVANCED = "advanced";
	public static final String AUTOWIRE_QUALIFIER_SECTION = "section";
	public static final String AUTOWIRE_QUALIFIER_CLAUSE = "clause";
	public static final String AUTOWIRE_QUALIFIER_PARAGRAPH = "paragraph";
	public static final String AUTOWIRE_QUALIFIER_INSTANCE = "instance";
	public static final String AUTOWIRE_QUALIFIER_JDBC_TX = "jdbcTransactionManager";
	public static final String AUTOWIRE_QUALIFIER_ORM_TX = "transactionManager";
	
	public static final String CONTENT_TYPE_ADVANCED = "COMPLEX";
	
	public static final String DATASTORE_PRIMARY = "datasource.primary";
	public static final String DATASTORE_SECONDARY = "datasource.secondary";

	public static final String ENGINE_CONTROL_STOP = "stop";
	
	public static final String FILE_EXTENSION_EXCEL = ".xlsx";
	public static final String FILE_SYS_TEMP_DIR = "java.io.tmpdir";
	
	public static final String HIBERNATE_TO_DDL_AUTO = "hibernate.hbm2ddl.auto";
	public static final String HIBERNATE_TO_DDL_AUTO_UPDATE = "update";
	
	public static final String JPA_PACKAGE = "com.itgfirm.docengine.types.jpa";

	public static final String PREFIX_COPY_OF = "Copy of - ";

	public static final String PROPERTY_DEFAULT = "classpath:default.properties";
	public static final String PROPERTY_CUSTOM = "file:${DOC_ENGINE_HOME}/config/custom.properties";
	
	public static final String REGEX_SPLIT_PATH = "\\.(?=[^\\.]+$)";
	
	public static final String REPO_PACKAGE = "com.itgfirm.docengine.repository";
	
	public static final String SYSTEM_VARIABLE_FOR_HOME = "DOC_ENGINE_HOME";

	private Constants() {
		// Do not instantiate
	}
}