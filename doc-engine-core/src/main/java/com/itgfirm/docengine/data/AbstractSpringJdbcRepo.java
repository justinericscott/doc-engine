package com.itgfirm.docengine.data;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import com.itgfirm.docengine.util.Utils;

/**
 * @author Justin Scott
 * 
 *         Base Class for all JDBC needs.
 */
//@Transactional
@Deprecated
public abstract class AbstractSpringJdbcRepo extends JdbcDaoSupport {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractSpringJdbcRepo.class);

//	@Autowired
//	@Qualifier(value = RepositoryConfig.QUALIFIER_EXTERNAL_DATASOURCE)
//	private DataSource ds;

//	@Value(Constants.PROPERTY_EXTERNAL_URL)
//	protected String externalUrl;

	public AbstractSpringJdbcRepo() {}

	/**
	 * Executes the provided SQL, there is nothing returned. Typically used for DDL operations.
	 * 
	 * @param sql
	 */
	protected void execute(String sql) {
		if (Utils.isNotNullOrEmpty(sql)) {
			LOG.trace("About to execute SQL: " + sql);
			getJdbcTemplate().execute(sql);
		}
	}

	/**
	 * Performs a query that is expecting many results.
	 * 
	 * @param sql
	 * @return A list of results.
	 */
	protected List<Map<String, Object>> queryForList(String sql) {
		if (Utils.isNotNullOrEmpty(sql)) {
			LOG.trace("About to query for list using SQL: " + sql);
			try {
				return getJdbcTemplate().queryForList(sql);
			} catch (BadSqlGrammarException | EmptyResultDataAccessException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return null;
	}

	/**
	 * Performs a query that is expecting one result.
	 * 
	 * @param sql
	 * @return A map object, made up of column names and their values.
	 */
	protected Map<String, Object> queryForMap(String sql) {
		if (Utils.isNotNullOrEmpty(sql)) {
			LOG.trace("About to query for map using SQL: " + sql);
			try {
				return getJdbcTemplate().queryForMap(sql);
			} catch (BadSqlGrammarException | EmptyResultDataAccessException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return null;
	}

	/**
	 * Performs an update based on the provided SQL.
	 * 
	 * @param sql
	 * @return The count of effected rows.
	 */
	protected int update(String sql) {
		if (Utils.isNotNullOrEmpty(sql)) {
			LOG.trace("About to update using SQL: " + sql);
			try {
				return getJdbcTemplate().update(sql);
			} catch (BadSqlGrammarException e) {
				LOG.error("Problem Executing Update!", e);
			}
		}
		return 0;
	}

	/**
	 * Initializes {@link JdbcTemplate}
	 */
	@PostConstruct
	private void init() {
		LOG.trace("Setting DataSource.");
//		setDataSource(ds);
	}
}