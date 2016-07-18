/**
 * TODO: License
 */
package com.itgfirm.docengine.util;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public final class Constants {

	/* SYSTEM */
	/** For Application Disk Storage Location **/
	public static final String SYS_VAR_DOC_ENGINE_HOME = "DOC_ENGINE_HOME";

	/* INTERNAL */
	/** For Spring PropertyResources **/
	public static final String PROPERTY_DEFAULT = "classpath:default.properties";
	public static final String PROPERTY_CUSTOM = "file:${DOC_ENGINE_HOME}/config/custom.properties";

	/** Property Placeholders **/
	public static final String PROPERTY_PRIMARY_DRIVER_CLASS = "${primary.driverClassName}";
	public static final String PROPERTY_PRIMARY_PASSWORD = "${primary.password}";
	public static final String PROPERTY_PRIMARY_URL = "${primary.url}";
	public static final String PROPERTY_PRIMARY_USERNAME = "${primary.username}";

	public static final String PROPERTY_EXTERNAL_DRIVER_CLASS = "${external.driverClassName}";
	public static final String PROPERTY_EXTERNAL_PASSWORD = "${external.password}";
	public static final String PROPERTY_EXTERNAL_URL = "${external.url}";
	public static final String PROPERTY_EXTERNAL_USERNAME = "${external.username}";

	public static final String PROPERTY_HIBERNATE_HBM2DDL = "${hibernate.hbm2ddl.auto}";
	public static final String PROPERTY_HIBERNATE_SHOW_SQL = "${hibernate.show_sql}";
	public static final String PROPERTY_HIBERNATE_DEFAULT_SCHEMA = "${hibernate.default_schema}";

	public static final String PROPERTY_SYSTEM_URL = "${system.url}";
	public static final String PROPERTY_SERVER_CONTEXT = "${server.contextPath}";
	public static final String PROPERTY_SERVER_PORT = "${server.port}";
	public static final String PROPERTY_SERVER_SSL_ENABLED = "${server.ssl.enabled}";

	private Constants() {
	}
}