package com.itgfirm.docengine.repository;

import static org.junit.Assert.*;

import static com.itgfirm.docengine.DocEngine.Constants.*;
import static com.itgfirm.docengine.util.TestUtils.getFileFromClasspath;
import static com.itgfirm.docengine.util.TestUtils.getRandomTestString;
import static com.itgfirm.docengine.util.Utils.breakSqlScriptIntoStatements;
import static com.itgfirm.docengine.util.Utils.isNotNullOrEmpty;

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

import com.itgfirm.docengine.types.ExternalAttributeImpl;
import com.itgfirm.docengine.types.ExternalEntityImpl;
import com.itgfirm.docengine.types.ExternalSchemaImpl;
import com.itgfirm.docengine.util.AbstractTest;

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
		String projectNumber = getRandomTestString(1);
		String sql = "insert into TR_PROJECT (PROJECT_NBR) values ('" + projectNumber + "')";
		assertEquals(1, repo.update(sql));
	}

	@Test
	public void ab_InsertInvalidSQLTest() {
		String projectNumber = getRandomTestString(2);
		String sql = "insert into TR_DOESNT_EXIST (PROJECT_NBR) values ('" + projectNumber + "')";
		assertEquals(0, repo.update(sql));
		sql = "insert into TR_PROJECT (PROJECT_NBR) values ()";
		assertEquals(0, repo.update(sql));
		sql = "Snicklefritz";
		assertEquals(0, repo.update(sql));
	}

	@Test
	public void ba_UpdateTest() {
		String projectNumber = getRandomTestString(3);
		String sql = "insert into TR_PROJECT (PROJECT_NBR) values ('" + projectNumber + "')";
		assertEquals(1, repo.update(sql));
		sql = "update TR_PROJECT set PROJECT_NBR = 'CHANGED' where PROJECT_NBR = '" + projectNumber + "'";
		assertEquals(1, repo.update(sql));
	}

	@Test
	public void bb_UpdateInvalidParamTest() {
		String projectNumber = getRandomTestString(4);
		String sql = "insert into TR_PROJECT (PROJECT_NBR) values ('" + projectNumber + "')";
		assertEquals(1, repo.update(sql));
		sql = "update DOENT_EXIST set PROJECT_NBR = 'CHANGED' where PROJECT_NBR = '" + projectNumber + "'";
		assertEquals(0, repo.update(sql));
		sql = "Snicklefritz";
		assertEquals(0, repo.update(sql));
	}

	@Test
	public void ca_SelectTest() {
		String projectNumber = getRandomTestString(5);
		String sql = "insert into TR_PROJECT (PROJECT_NBR) values ('" + projectNumber + "')";
		assertEquals(1, repo.update(sql));
		sql = "select PROJECT_NBR from TR_PROJECT where PROJECT_NBR = '" + projectNumber + "'";
		assertEquals(projectNumber, repo.queryForMap(sql).entrySet().iterator().next().getValue().toString());
	}

	@Test
	public void cb_SelectInvalidParamTest() {
		String projectNumber = getRandomTestString(6);
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
		String projectNumber = getRandomTestString(7);
		String sql = "insert into TR_PROJECT (PROJECT_NBR) values ('" + projectNumber + "')";
		assertEquals(1, repo.update(sql));
		sql = "delete from TR_PROJECT where PROJECT_NBR = '" + projectNumber + "'";
		assertEquals(1, repo.update(sql));
		sql = "select PROJECT_NBR from TR_PROJECT where PROJECT_NBR = '" + projectNumber + "'";
		assertNull(repo.queryForMap(sql));
	}

	@Test
	public void db_DeleteInvalidParamTest() {
		String projectNumber = getRandomTestString(8);
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
		ExternalSchemaImpl schema = repo.getExternalSchema();
		assertNotNull(schema);
		LOG.debug("SCHEMA: " + schema.getName());
		assertNotNull(schema.getTables());
		for (ExternalEntityImpl e : schema.getTables()) {
			if (e.getName().startsWith("TR_") || e.getName().startsWith("tr_")) {
				assertNotNull(e.getColumns());
				LOG.debug("ENTITY: " + e.getName());
				for (ExternalAttributeImpl a : e.getColumns()) {
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
			final ExternalSchemaImpl schema = repo.getExternalSchema();
			if (isNotNullOrEmpty(schema)) {
				final Iterable<ExternalEntityImpl> tables = schema.getTables();
				if (!isNotNullOrEmpty(tables)) {
					final String url = schema.getUrl();
					if (isNotNullOrEmpty(url) && url.contains("hsqldb") && !isSetup) {
						LOG.info("Setting Up External Schema Using HyperSQL Database.");
						File ddl = getFileFromClasspath("grex.ddl");
						File dml = getFileFromClasspath("grex.dml");
						File testData = getFileFromClasspath("test-data.dml");
						File logic1 = getFileFromClasspath("logic-scenario-1.dml");
						File logic2 = getFileFromClasspath("logic-scenario-2.dml");
						File logic3 = getFileFromClasspath("logic-scenario-3.dml");
						if (ddl.exists()) {
							repo.execute(breakSqlScriptIntoStatements(ddl));
							if (dml.exists()) {
								repo.execute(breakSqlScriptIntoStatements(dml));
							}
							if (testData.exists()) {
								repo.execute(breakSqlScriptIntoStatements(testData));
							}
							if (logic1.exists()) {
								repo.execute(breakSqlScriptIntoStatements(logic1));
							}
							if (logic2.exists()) {
								repo.execute(breakSqlScriptIntoStatements(logic2));
							}
							if (logic3.exists()) {
								repo.execute(breakSqlScriptIntoStatements(logic3));
							}
						}
					}
					isSetup = true;					
				}
			}
		}
	}
}