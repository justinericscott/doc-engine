package com.github.justinericscott.docengine.repository.token;

import static org.junit.Assert.*;
import static com.github.justinericscott.docengine.DocEngine.Constants.*;
import static com.github.justinericscott.docengine.util.TestUtils.getFileFromClasspath;
import static com.github.justinericscott.docengine.util.Utils.breakSqlScriptIntoStatements;
import static com.github.justinericscott.docengine.util.Utils.isNotNullAndExists;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.github.justinericscott.docengine.repository.token.BusinessDataRepository;
import com.github.justinericscott.docengine.service.token.types.ExternalAttribute;
import com.github.justinericscott.docengine.service.token.types.ExternalEntity;
import com.github.justinericscott.docengine.service.token.types.ExternalSchema;
import com.github.justinericscott.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BusinessDataRepositoryTest extends AbstractTest {
	private static final Logger LOG = LoggerFactory.getLogger(BusinessDataRepositoryTest.class);
	private static boolean isSetup = false;

	@Autowired
	private BusinessDataRepository repo;

	@Autowired
	@Qualifier(AUTOWIRE_QUALIFIER_JDBC)
	private DataSource jdbc;

	@Test
	public void aa_InsertTest() {
		String projectNumber = TEST_PROJECT_ID_PREFIX + uuid();
		String sql = "insert into TR_PROJECT (PROJECT_NBR) values ('" + projectNumber + "')";
		assertEquals(1, repo.update(sql));
	}

	@Test
	public void ab_InsertInvalidSQLTest() {
		String projectNumber = TEST_PROJECT_ID_PREFIX + uuid();
		String sql = "insert into TR_DOESNT_EXIST (PROJECT_NBR) values ('" + projectNumber + "')";
		assertEquals(0, repo.update(sql));
		sql = "insert into TR_PROJECT (PROJECT_NBR) values ()";
		assertEquals(0, repo.update(sql));
		sql = "Snicklefritz";
		assertEquals(0, repo.update(sql));
	}

	@Test
	public void ba_UpdateTest() {
		String projectNumber = TEST_PROJECT_ID_PREFIX + uuid();
		String sql = "insert into TR_PROJECT (PROJECT_NBR) values ('" + projectNumber + "')";
		assertEquals(1, repo.update(sql));
		sql = "update TR_PROJECT set PROJECT_NBR = 'CHANGED' where PROJECT_NBR = '" + projectNumber + "'";
		assertEquals(1, repo.update(sql));
	}

	@Test
	public void bb_UpdateInvalidParamTest() {
		String projectNumber = TEST_PROJECT_ID_PREFIX + uuid();
		String sql = "insert into TR_PROJECT (PROJECT_NBR) values ('" + projectNumber + "')";
		assertEquals(1, repo.update(sql));
		sql = "update DOENT_EXIST set PROJECT_NBR = 'CHANGED' where PROJECT_NBR = '" + projectNumber + "'";
		assertEquals(0, repo.update(sql));
		sql = "Snicklefritz";
		assertEquals(0, repo.update(sql));
	}

	@Test
	public void ca_SelectTest() {
		String projectNumber = TEST_PROJECT_ID_PREFIX + uuid();
		String sql = "insert into TR_PROJECT (PROJECT_NBR) values ('" + projectNumber + "')";
		assertEquals(1, repo.update(sql));
		sql = "select PROJECT_NBR from TR_PROJECT where PROJECT_NBR = '" + projectNumber + "'";
		assertEquals(projectNumber, repo.queryForMap(sql).entrySet().iterator().next().getValue().toString());
	}

	@Test
	public void cb_SelectInvalidParamTest() {
		String projectNumber = TEST_PROJECT_ID_PREFIX + uuid();
		String sql = "insert into TR_PROJECT (PROJECT_NBR) values ('" + projectNumber + "')";
		assertEquals(1, repo.update(sql));
		sql = "select MY_COLUMN from TR_PROJECT where PROJECT_NBR = '" + projectNumber + "'";
		assertNull(repo.queryForMap(sql));
		sql = "select PROJECT_NBR from DOESNT_EXIST where PROJECT_NBR = '" + projectNumber + "'";
		assertNull(repo.queryForMap(sql));
		sql = "select PROJECT_NBR from TR_PROJECT where PROJECT_NBR = 'NOT_A_REAL_PROJECT'";
		assertNull(repo.queryForMap(sql));
		sql = "Snicklefritz";
		assertNull(repo.queryForMap(sql));
	}

	@Test
	public void da_DeleteTest() {
		String projectNumber = TEST_PROJECT_ID_PREFIX + uuid();
		String sql = "insert into TR_PROJECT (PROJECT_NBR) values ('" + projectNumber + "')";
		assertEquals(1, repo.update(sql));
		sql = "delete from TR_PROJECT where PROJECT_NBR = '" + projectNumber + "'";
		assertEquals(1, repo.update(sql));
		sql = "select PROJECT_NBR from TR_PROJECT where PROJECT_NBR = '" + projectNumber + "'";
		assertNull(repo.queryForMap(sql));
	}

	@Test
	public void db_DeleteInvalidParamTest() {
		String projectNumber = TEST_PROJECT_ID_PREFIX + uuid();
		String sql = "insert into TR_PROJECT (PROJECT_NBR) values ('" + projectNumber + "')";
		assertEquals(1, repo.update(sql));
		sql = "delete from DOESNT_EXIST where PROJECT_NBR = '" + projectNumber + "'";
		assertEquals(0, repo.update(sql));
		sql = "delete from TR_PROJECT where PROJECT_NBR = 'NOT_A_REAL_PROJECT'";
		assertEquals(0, repo.update(sql));
		sql = "delete from TR_PROJECT where MY_COLUMN = '" + projectNumber + "'";
		assertEquals(0, repo.update(sql));
	}

	@Test
	public void ea_GetExternalSchemaTest() {
		ExternalSchema schema = repo.getExternalSchema();
		assertNotNull(schema);
		LOG.debug("SCHEMA: " + schema.getName());
		assertNotNull(schema.getTables());
		for (ExternalEntity e : schema.getTables()) {
			if (e.getName().startsWith("TR_") || e.getName().startsWith("tr_")) {
				assertNotNull(e.getColumns());
				LOG.debug("ENTITY: " + e.getName());
				for (ExternalAttribute a : e.getColumns()) {
					assertNotNull(a.getName());
					LOG.trace("ATTRIBUTE: " + a.getName());
				}
			}
		}
	}

	@Test
	public void xx_MinimalCRUDTest() {
		if (!isSetup) {
			String table = "TEST_TABLE";
			String column = "TEST_COLUMN";
			String data = "TEST_DATA";
			String sql = "create table " + table + " (" + column + " VARCHAR(100))";
			repo.execute(sql);
			sql = "insert into " + table + " (" + column + ") values ('" + data + "')";
			assertEquals(1, repo.update(sql));
			sql = "select " + column + " from " + table + " where " + column + " = '" + data + "'";
			assertEquals(data, repo.queryForMap(sql).entrySet().iterator().next().getValue().toString());
			sql = "update " + table + " set " + column + " = '" + data.concat(data) + "' where " + column + " = '"
					+ data + "'";
			assertEquals(1, repo.update(sql));
			sql = "select " + column + " from " + table + " where " + column + " = '" + data.concat(data) + "'";
			assertEquals(data.concat(data), repo.queryForMap(sql).entrySet().iterator().next().getValue().toString());
			sql = "delete from " + table + " where " + column + " = '" + data.concat(data) + "'";
			assertEquals(1, repo.update(sql));
		}
	}

	@PostConstruct
	private void setup() {
		if (isNotNullOrEmpty(repo)) {
			final ExternalSchema schema = repo.getExternalSchema();
			if (isNotNullOrEmpty(schema)) {
				final Iterable<ExternalEntity> tables = schema.getTables();
				if (!isNotNullOrEmpty(tables)) {
					final String url = schema.getUrl();
					File ddl = null;
					File dml = null;
					File logic1 = null;
					File logic2 = null;
					File logic3 = null;
					File testData = getFileFromClasspath("sql/test-data.dml");
					if (isNotNullOrEmpty(url) && (url.contains("hsqldb") || url.contains("oracle")) && !isSetup) {
						ddl = getFileFromClasspath("sql/grex-oracle.ddl");
						dml = getFileFromClasspath("sql/grex-oracle.dml");
						logic1 = getFileFromClasspath("sql/logic-oracle-scenario-1.dml");
						logic2 = getFileFromClasspath("sql/logic-oracle-scenario-2.dml");
						logic3 = getFileFromClasspath("sql/logic-oracle-scenario-3.dml");
						LOG.info("Setting Up External Schema Using {} Database.", (url.contains("hsqldb")
								? "HyperSQL using Oracle syntax" : (url.contains("oracle") ? "Oracle" : "")));
					} else if (isNotNullOrEmpty(url) && url.contains("mysql") && !isSetup) {
						ddl = getFileFromClasspath("sql/grex-mysql.ddl");
						dml = getFileFromClasspath("sql/grex-mysql.dml");
						logic1 = getFileFromClasspath("sql/logic-mysql-scenario-1.dml");
						logic2 = getFileFromClasspath("sql/logic-mysql-scenario-2.dml");
						logic3 = getFileFromClasspath("sql/logic-mysql-scenario-3.dml");
						LOG.info("Setting Up External Schema Using MySQL Database.");
					}
					if (isNotNullAndExists(ddl)) {
						repo.execute(breakSqlScriptIntoStatements(ddl));
						if (isNotNullAndExists(dml)) {
							repo.execute(breakSqlScriptIntoStatements(dml));
						}
						if (isNotNullAndExists(testData)) {
							repo.execute(breakSqlScriptIntoStatements(testData));
						}
						if (isNotNullAndExists(logic1)) {
							repo.execute(breakSqlScriptIntoStatements(logic1));
						}
						if (isNotNullAndExists(logic2)) {
							repo.execute(breakSqlScriptIntoStatements(logic2));
						}
						if (isNotNullAndExists(logic3)) {
							repo.execute(breakSqlScriptIntoStatements(logic3));
						}
					}
				}
			}
		}
		isSetup = true;
	}
}