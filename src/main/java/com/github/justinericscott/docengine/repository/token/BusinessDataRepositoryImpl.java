package com.github.justinericscott.docengine.repository.token;

import static com.github.justinericscott.docengine.util.Utils.Constants.*;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.github.justinericscott.docengine.service.token.types.ExternalAttribute;
import com.github.justinericscott.docengine.service.token.types.ExternalEntity;
import com.github.justinericscott.docengine.service.token.types.ExternalSchema;

/**
 * @author Justin Scott
 * 
 *         Default implementation of the External Repository.
 */
@Repository
@Transactional(transactionManager = AUTOWIRE_QUALIFIER_JDBC_TX)
class BusinessDataRepositoryImpl implements BusinessDataRepository {
	private static final Logger LOG = LoggerFactory.getLogger(BusinessDataRepositoryImpl.class);
	
	@Autowired
	private JdbcTemplate jdbc;
	
	public BusinessDataRepositoryImpl() {
		// Default constructor for Spring
	}

	@Override
	public final void execute(final String sql) {
		if (isNotNullOrEmpty(sql)) {
			LOG.debug("About To Execute SQL: " + sql);
			jdbc.execute(sql);			
		}
	}

	public final void execute(final String[] script) {
		if (isNotNullOrEmpty(script)) {
			LOG.debug("About to execute script.");
			for (final String s : script) {
				execute(s);
			}
		}
	}

	@Override
	public final List<Map<String, Object>> queryForList(final String sql) {
		if (isNotNullOrEmpty(sql)) {
			try {
				return jdbc.queryForList(sql);		
			} catch (final BadSqlGrammarException | EmptyResultDataAccessException e) {
				LOG.error("Problem with SQL query for List!", e);
			}
		}
		return null;
	}

	@Override
	public final Map<String, Object> queryForMap(final String sql) {
		if (isNotNullOrEmpty(sql)) {
			try {
				return jdbc.queryForMap(sql);
			} catch (final BadSqlGrammarException | EmptyResultDataAccessException e) {
				LOG.error("Problem with SQL query for Map!", e);
			}
		}
		return null;
	}

	@Override
	public final int update(final String sql) {
		if (isNotNullOrEmpty(sql)) {
			try {
				return jdbc.update(sql);
			} catch (final BadSqlGrammarException e) {
				LOG.error("Problem with SQL update!", e);
			}
		}
		return 0;
	}

	@Override
	public final Integer[] update(final String[] script) {
		if (isNotNullOrEmpty(script)) {
			final List<Integer> result = new ArrayList<Integer>(script.length);
			LOG.debug("About to execute script.");
			for (final String s : script) {
				result.add(update(s));
			}
			return result.toArray(new Integer[result.size()]);
		} else {
			final Integer[] result = { 0 };
			return result;
		}
	}

	@Override
	public ExternalSchema getExternalSchema() {
		try (final Connection conn = jdbc.getDataSource().getConnection()) {
			final DatabaseMetaData meta = conn.getMetaData();
			final String url = meta.getURL();
			String name = null;
			if (url.contains("mysql")) {
				final String[] parts = url.split("/");
				if (parts.length > 0) {
					name = parts[parts.length - 1];
				}
			} else if (url.contains("hsqldb")) {
				name = "PUBLIC";
			}
			final ExternalSchema schema = new ExternalSchema(name);
			schema.setUrl(url);
			try (final ResultSet tables = meta.getTables(null, name, null, null)) {
				while (tables.next()) {
					final String tableName = tables.getString(3);
					final String tableDescription = tables.getString(5);
					final ExternalEntity table = new ExternalEntity(tableName, tableDescription);
					schema.addTable(table);
					try (final ResultSet columns = meta.getColumns(null, name, tableName, null)) {
						while (columns.next()) {
							final String columnName = columns.getString(4);
							final String columnDescription = columns.getString(12);
							final Integer columnDatatype = columns.getInt(5);
							table.addColumn(new ExternalAttribute(columnName, columnDescription, columnDatatype));
						}						
					}
				}				
			}
			return schema;
		} catch (final SQLException e) {
			LOG.error("Problem getting business schema!", e);
		}
		return null;
	}
}