package com.itgfirm.docengine.repository;

import static com.itgfirm.docengine.DocEngine.Constants.*;
import static com.itgfirm.docengine.repository.DataConfig.DataConfigConstants.*;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
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
@EnableJpaRepositories(basePackages = { REPO_CONTENT, REPO_TOKEN })
@EnableTransactionManagement
public class DataConfig {
	private static final Logger LOG = LoggerFactory.getLogger(DataConfig.class);

	/**
	 * Creates the {@link EntityManagerFactory} for the primary connection.
	 * 
	 * @return {@link LocalContainerEntityManagerFactoryBean}
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LOG.debug("Creating Hibernate JPA Entity Manager.");
		final HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		final LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(jpaDataSource());
		factory.setJpaVendorAdapter(jpaVendorAdapter);
		factory.setPackagesToScan(JPA_PACKAGE);
		factory.getJpaPropertyMap().put(HIBERNATE_TO_DDL_AUTO, HIBERNATE_TO_DDL_AUTO_UPDATE);
		return factory;
	}

	/**
	 * Creates the {@link DataSource} for the secondary connection.
	 * 
	 * @return {@link DataSource}
	 */
	@Bean(name = AUTOWIRE_QUALIFIER_JDBC)
	@ConfigurationProperties(prefix = DATASTORE_SECONDARY)
	public DataSource jdbcDataSource() {
		LOG.debug("Creating Secondary Datasource.");
		return DataSourceBuilder.create(getClass().getClassLoader()).build();
	}

	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(jdbcDataSource());
	}

	/**
	 * Creates the {@link TransactionManager} for the secondary connection.
	 * 
	 * @return {@link PlatformTransactionManager}
	 */
	@Bean(name = AUTOWIRE_QUALIFIER_JDBC_TX)
	public PlatformTransactionManager jdbcTransactionManager() {
		LOG.debug("Creating JDBC Transaction Manager.");
		return new DataSourceTransactionManager(jdbcDataSource());
	}

	/**
	 * Creates the primary {@link DataSource}.
	 * 
	 * @return {@link DataSource}
	 */
	@Bean(name = AUTOWIRE_QUALIFIER_ORM)
	@ConfigurationProperties(prefix = DATASTORE_PRIMARY)
	public DataSource jpaDataSource() {
		LOG.debug("Creating Primary Datasource.");
		return DataSourceBuilder.create(getClass().getClassLoader()).build();
	}

	/**
	 * Creates the {@link TransactionManager} for the primary connection.
	 * 
	 * @return {@link PlatformTransactionManager}
	 */
	@Bean(name = AUTOWIRE_QUALIFIER_ORM_TX)
	public PlatformTransactionManager transactionManager() {
		LOG.debug("Creating JPA Transaction Manager.");
		return new JpaTransactionManager(entityManagerFactory().getObject());
	}

	static class DataConfigConstants {
		static final String DATASTORE_PRIMARY = "datasource.primary";
		static final String DATASTORE_SECONDARY = "datasource.secondary";

		static final String HIBERNATE_TO_DDL_AUTO = "hibernate.hbm2ddl.auto";
		static final String HIBERNATE_TO_DDL_AUTO_UPDATE = "update";
		
		static final String JPA_PACKAGE = "com.itgfirm.docengine.types";

		static final String REPO_CONTENT = "com.itgfirm.docengine.repository.content";
		static final String REPO_TOKEN = "com.itgfirm.docengine.repository.token";
	}
}