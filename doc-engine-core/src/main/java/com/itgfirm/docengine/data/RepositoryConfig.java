package com.itgfirm.docengine.data;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Environment;
import org.hsqldb.jdbcDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.itgfirm.docengine.types.jpa.TypeConstants;
import com.itgfirm.docengine.util.Constants;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author Justin Scott
 * 
 *         Configuration Class responsible for all data access needs.
 */
@Configuration
@EnableTransactionManagement
public class RepositoryConfig {
	private static final Logger LOG = LogManager.getLogger(RepositoryConfig.class);
	public static final String QUALIFIER_PRIMARY_DATASOURCE = "primary";
	public static final String QUALIFIER_EXTERNAL_DATASOURCE = "external";

	/** Internal / Primary Config (Content, Instances, Tokens) **/
	@Value(Constants.PROPERTY_PRIMARY_DRIVER_CLASS)
	private String primaryDriverClassName;
	@Value(Constants.PROPERTY_PRIMARY_PASSWORD)
	private String primaryPassword;
	@Value(Constants.PROPERTY_PRIMARY_URL)
	private String primaryUrl;
	@Value(Constants.PROPERTY_PRIMARY_USERNAME)
	private String primaryUsername;

	/** External Config (Business Data) **/
	@Value(Constants.PROPERTY_EXTERNAL_DRIVER_CLASS)
	private String externalDriverClassName;
	@Value(Constants.PROPERTY_EXTERNAL_PASSWORD)
	private String externalPassword;
	@Value(Constants.PROPERTY_EXTERNAL_URL)
	private String externalUrl;
	@Value(Constants.PROPERTY_EXTERNAL_USERNAME)
	private String externalUsername;

	/** Hibernate Config (Used Internally Only **/
	@Value(Constants.PROPERTY_HIBERNATE_HBM2DDL)
	private String hbm2ddlAuto;
	@Value(Constants.PROPERTY_HIBERNATE_SHOW_SQL)
	private String showSql;
	@Value(Constants.PROPERTY_HIBERNATE_DEFAULT_SCHEMA)
	private String defaultSchema;

	/**
	 * Creates a {@link DataSource} object to connect to internal/content data.
	 * 
	 * @return Internal {@link DataSource}.
	 */
	@Bean
	@Primary
	@Qualifier(value = QUALIFIER_PRIMARY_DATASOURCE)
	public DataSource getDataSource() {
		LOG.trace("Attempting to create primary datasource for " + primaryUrl);
		ComboPooledDataSource pool = new ComboPooledDataSource();
		try {
			pool.setDriverClass(primaryDriverClassName);
			pool.setJdbcUrl(primaryUrl);
			pool.setUser(primaryUsername);
			pool.setPassword(primaryPassword);
			pool.setAcquireIncrement(2);
			pool.setMinPoolSize(3);
			pool.setMaxPoolSize(9);
		} catch (PropertyVetoException e) {
			LOG.error("Problem Creating Primary Connection Pool!", e);
		}
		return pool;
	}

	/**
	 * Creates a {@link DataSource} object to connect to external/business data.
	 * 
	 * @return External {@link DataSource}.
	 */
	@Bean
	@Qualifier(value = QUALIFIER_EXTERNAL_DATASOURCE)
	public DataSource getExternalDataSource() {
		LOG.trace("Attempting to create an extenal datasource for: " + externalUrl);
		ComboPooledDataSource pool = new ComboPooledDataSource();
		try {
			pool.setDriverClass(externalDriverClassName);
			pool.setJdbcUrl(externalUrl);
			pool.setUser(externalUsername);
			pool.setPassword(externalPassword);
			pool.setAcquireIncrement(2);
			pool.setMinPoolSize(3);
			pool.setMaxPoolSize(9);
		} catch (PropertyVetoException e) {
			LOG.error("Problem Creating External Connection Pool!", e);
		}
		return pool;
	}

	/**
	 * Creates a Hibernate {@link SessionFactory}
	 * 
	 * @return A new {@link SessionFactory}
	 */
	@Bean
	public LocalSessionFactoryBean getSessionFactory(DataSource ds) {
		LOG.trace("Attempting to create a SessionFactory.");
		LocalSessionFactoryBean lsfb = new LocalSessionFactoryBean();
		lsfb.setDataSource(ds);
		lsfb.setHibernateProperties(getHibernateProperties());
		lsfb.setPackagesToScan(new String[] {
			TypeConstants.JPA_PACKAGE
		});
		return lsfb;
	}

	/**
	 * Creates a {@link HibernateTransactionManager}
	 * 
	 * @param sessionFactory
	 * @return {@link HibernateTransactionManager}
	 */
	@Autowired
	@Bean
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		LOG.trace("Creating Hibernate Transaction Manager.");
		return new HibernateTransactionManager(sessionFactory);
	}

	/**
	 * Creates a {@link DataSourceTransactionManager}
	 * 
	 * @return {@link DataSourceTransactionManager}
	 */
	@Bean
	public DataSourceTransactionManager transactionManager() {
		return new DataSourceTransactionManager(getExternalDataSource());
	}

	/**
	 * Creates a {@link Properties} object to be used by Hibernate.
	 * 
	 * @return Hibernate {@link Properties} object.
	 */
	private Properties getHibernateProperties() {
		LOG.trace("Setting Hibernate Properties.");
		Properties props = new Properties();
		props.put(Environment.SHOW_SQL, showSql);
		if (primaryDriverClassName.equals(jdbcDriver.class.getClass().getName())) {
			props.put(Environment.HBM2DDL_AUTO, "update");
		} else {
			props.put(Environment.HBM2DDL_AUTO, hbm2ddlAuto);
		}
		if (defaultSchema != null && !defaultSchema.isEmpty()) {
			props.put(Environment.DEFAULT_SCHEMA, defaultSchema);
		}
		return props;
	}
}