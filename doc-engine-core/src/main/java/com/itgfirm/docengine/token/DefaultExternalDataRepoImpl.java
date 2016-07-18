package com.itgfirm.docengine.token;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.itgfirm.docengine.data.AbstractSpringJdbcRepo;
import com.itgfirm.docengine.data.ExternalAttributeImpl;
import com.itgfirm.docengine.data.ExternalEntity;
import com.itgfirm.docengine.data.ExternalEntityImpl;
import com.itgfirm.docengine.data.ExternalSchema;
import com.itgfirm.docengine.data.ExternalSchemaImpl;
import com.itgfirm.docengine.util.Utils;

/**
 * @author Justin Scott
 * 
 *         Default implementation of the External Repository.
 */
@Repository
class DefaultExternalDataRepoImpl extends AbstractSpringJdbcRepo implements ExternalDataRepo {
	private static final Logger LOG = LogManager.getLogger(DefaultExternalDataRepoImpl.class);

	public DefaultExternalDataRepoImpl() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.external.ExternalDataRepo#execute( java.lang.String)
	 */
	@Override
	public void execute(String sql) {
		LOG.trace("About To Execute SQL: " + sql);
		super.execute(sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.external.ExternalDataRepo#execute( java.lang.String[])
	 */
	@Override
	public void execute(String[] script) {
		if (Utils.isNotNullOrEmpty(script)) {
			LOG.trace("About to execute script.");
			for (String s : script) {
				this.execute(s);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.external.ExternalDataRepo#queryForList( java.lang.String)
	 */
	@Override
	public List<Map<String, Object>> queryForList(String sql) {
		return super.queryForList(sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.external.ExternalDataRepo#queryForMap( java.lang.String)
	 */
	@Override
	public Map<String, Object> queryForMap(String sql) {
		return super.queryForMap(sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.external.ExternalDataRepo#update( java.lang.String)
	 */
	@Override
	public int update(String sql) {
		return super.update(sql);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.external.ExternalDataRepo#update( java.lang.String[])
	 */
	@Override
	public Integer[] update(String[] script) {
		if (Utils.isNotNullOrEmpty(script)) {
			List<Integer> result = new ArrayList<Integer>(script.length);
			LOG.trace("About to execute script.");
			for (String s : script) {
				result.add(this.update(s));
			}
			return result.toArray(new Integer[result.size()]);
		} else {
			Integer[] result = {
				0
			};
			return result;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.external.ExternalDataRepo#getExternalSchema()
	 */
	@Override
	public ExternalSchema getExternalSchema() {
		String[] parts;
		String schemaName = "NOT SET";
		if (externalUrl.contains("mysql")) {
			parts = externalUrl.split("/");
			if (parts.length > 0) {
				schemaName = parts[parts.length - 1];
			}
		} else if (externalUrl.contains("hsqldb")) {
			schemaName = "PUBLIC";
		}
		ExternalSchema schema = null;
		DatabaseMetaData meta;
		Connection conn = null;
		try {
			conn = getDataSource().getConnection();
			meta = conn.getMetaData();
			schema = new ExternalSchemaImpl(schemaName);
			ResultSet tables = meta.getTables(null, schemaName, null, null);
			while (tables.next()) {
				String tableName = tables.getString(3);
				String tableDescription = tables.getString(5);
				ExternalEntity table = new ExternalEntityImpl(tableName, tableDescription);
				schema.addTable(table);
				ResultSet columns = meta.getColumns(null, schemaName, tableName, null);
				while (columns.next()) {
					String columnName = columns.getString(4);
					String columnDescription = columns.getString(12);
					Integer columnDatatype = columns.getInt(5);
					@SuppressWarnings("unused")
					Integer isNullable = columns.getInt(11);
					table.addColumn(new ExternalAttributeImpl(columnName, columnDescription,
							columnDatatype));
				}
			}
		} catch (SQLException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			Utils.closeQuietly(conn);
		}
		return schema;
	}
}