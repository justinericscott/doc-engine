package com.github.justinericscott.docengine.config;

import static com.github.justinericscott.docengine.util.Utils.Constants.*;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * <b>Document Engine Data Access Configuration</b> <br>
 * 
 * @author Justin Scott<br>
 *         <br>
 *         <ul>
 *         <li><b>TODO:</b> Enable memory based database configuration for
 *         default.properties.</li>
 *         <li><b>TODO:</b> Create DDL and DML to populate primary data
 *         source.</li>
 *         <li><b>TODO:</b> Make running DDL and DML intelligent.</li>
 *         </ul>
 *         <b>About and How To Configure</b><br>
 *         <ul>
 *         <li>Configures the JPA/Hibernate and JDBC services.</li>
 *         <li>Actual data source specific configurations are done in
 *         *.properties files.</li>
 *         <li>The default configuration properties file is located at
 *         'classpath:default.properties'.</li>
 *         <li>The default configuration can be overridden by creating your own
 *         properties file.
 *         <ul>
 *         <li>Create a folder somewhere in the server that the JVM can
 *         access.</li>
 *         <li>Create a system variable pointing to the path of the new folder
 *         named '<b>DOC_ENGINE_HOME</b>'.</li>
 *         <li>Create a folder under %DOC_ENGINE_HOME% named '<b>config</b>'.
 *         <li>Create a file under %DOC_ENGINE_HOME%/config named
 *         '<b>custom.properties</b>'.</li>
 *         <li>The properties to override are:<br>
 *         <ul>
 *         <br>
 *         <li><b>Primary Data Source</b> - Used for content storage.
 *         <ul>
 *         <li>datasource.primary.driverClassName</li>
 *         <li>datasource.primary.url</li>
 *         <li>datasource.primary.username</li>
 *         <li>datasource.primary.password</li>
 *         <li>datasource.primary.poolSize</li>
 *         </ul>
 *         </li>
 *         </ul>
 *         <ul>
 *         <li><b>Secondary Data Source</b> - Used for token data retrieval.
 *         <ul>
 *         <li>datasource.seconday.driverClassName</li>
 *         <li>datasource.seconday.url</li>
 *         <li>datasource.seconday.username</li>
 *         <li>datasource.seconday.password</li>
 *         <li>datasource.seconday.poolSize</li>
 *         </ul>
 *         </li>
 *         </ul>
 *         </li>
 *         </ul>
 *         </li>
 *         </ul>
 */
@Configuration
@EnableJpaRepositories({ DataConfig.REPO_CONTENT, DataConfig.REPO_TOKEN })
@EnableTransactionManagement
class DataConfig {
	protected static final String REPO_CONTENT = "com.github.justinericscott.docengine.repository.content";
	protected static final String REPO_TOKEN = "com.github.justinericscott.docengine.repository.token";
	private static final String DATASTORE_ENGINE = "app.datasource.first";
	private static final String DATASTORE_BUSINESS = "app.datasource.second";
	private static final String JPA_DATETIME_PACKAGE = "org.springframework.data.jpa.convert.threeten";
	private static final String JPA_PACKAGE = "com.github.justinericscott.docengine.models";
	private static final Logger LOG = LoggerFactory.getLogger(DataConfig.class);

	/**
	 * Creates the {@link EntityManagerFactory} for the primary connection.
	 * 
	 * @return {@link LocalContainerEntityManagerFactoryBean}
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LOG.trace("Creating new Hibernate JPA Entity Manager Factory.");
		final LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(getDataSource());
		factory.setJpaVendorAdapter(getJpaVendorAdaptor());
		factory.setPackagesToScan(JPA_PACKAGE, JPA_DATETIME_PACKAGE);
		return factory;
	}

	/**
	 * Creates the {@link DataSource} for the secondary connection.
	 * 
	 * @return {@link DataSource}
	 */
	@Bean(AUTOWIRE_QUALIFIER_JDBC)
	@ConfigurationProperties(DATASTORE_BUSINESS)
	public DataSource getJdbcDataSource() {
		LOG.trace("Creating new Business Datasource.");
		return DataSourceBuilder.create(getClass().getClassLoader()).build();
	}

	@Bean
	public JdbcTemplate getJdbcTemplate() {
		LOG.trace("Creating new JDBC Template.");
		return new JdbcTemplate(getJdbcDataSource());
	}

	/**
	 * Creates the {@link TransactionManager} for the secondary connection.
	 * 
	 * @return {@link PlatformTransactionManager}
	 */
	@Bean(AUTOWIRE_QUALIFIER_JDBC_TX)
	public PlatformTransactionManager getJdbcTransactionManager() {
		LOG.trace("Creating new JDBC Transaction Manager.");
		return new DataSourceTransactionManager(getJdbcDataSource());
	}

	/**
	 * Creates the primary {@link DataSource}.
	 * 
	 * @return {@link DataSource}
	 */
	@Primary
	@Bean(AUTOWIRE_QUALIFIER_ORM)
	@ConfigurationProperties(DATASTORE_ENGINE)
	public DataSource getDataSource() {
		LOG.trace("Creating new Engine Datasource.");
		return DataSourceBuilder.create(getClass().getClassLoader()).build();
	}

	@Bean
	public JpaVendorAdapter getJpaVendorAdaptor() {
		final HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setGenerateDdl(true);
		jpaVendorAdapter.setShowSql(true);
		return jpaVendorAdapter;
	}
	
	/**
	 * Creates the {@link TransactionManager} for the primary connection.
	 * 
	 * @return {@link PlatformTransactionManager}
	 */
	@Bean(AUTOWIRE_QUALIFIER_ORM_TX)
	public PlatformTransactionManager getTransactionManager() {
		LOG.trace("Creating new JPA Transaction Manager.");
		return new JpaTransactionManager(entityManagerFactory().getObject());
	}
}