package com.itgfirm.docengine.token;

import static org.junit.Assert.*;

import java.io.File;

import javax.annotation.PostConstruct;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.itgfirm.docengine.data.ExternalAttributeImpl;
import com.itgfirm.docengine.data.ExternalEntityImpl;
import com.itgfirm.docengine.data.ExternalSchemaImpl;
import com.itgfirm.docengine.util.AbstractTest;
import com.itgfirm.docengine.util.Constants;
import com.itgfirm.docengine.util.TestUtils;
import com.itgfirm.docengine.util.Utils;

/**
 * @author Justin Scott TODO: Description
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Deprecated
public class ExternalDataRepoTest extends AbstractTest {
	private static final Logger LOG = LoggerFactory.getLogger(ExternalDataRepoTest.class);
	private static boolean isSetup = false;

//	@Autowired
	private DefaultExternalDataRepoImpl externalRepo;
	@Value(value = Constants.DATASTORE_SECONDARY)
	private String externalUrl;

	@Test
	public void aa_InsertTest() {
		String projectNumber = TestUtils.getRandomTestString(1);
		String sql = "insert into TR_PROJECT (PROJECT_NBR) values ('" + projectNumber + "')";
		assertEquals(1, externalRepo.update(sql));
	}

	@Test
	public void ab_InsertInvalidSQLTest() {
		String projectNumber = TestUtils.getRandomTestString(2);
		String sql = "insert into TR_DOESNT_EXIST (PROJECT_NBR) values ('" + projectNumber + "')";
		assertEquals(0, externalRepo.update(sql));
		sql = "insert into TR_PROJECT (PROJECT_NBR) values ()";
		assertEquals(0, externalRepo.update(sql));
		sql = "Snicklefritz";
		assertEquals(0, externalRepo.update(sql));
	}

	@Test
	public void ba_UpdateTest() {
		String projectNumber = TestUtils.getRandomTestString(3);
		String sql = "insert into TR_PROJECT (PROJECT_NBR) values ('" + projectNumber + "')";
		assertEquals(1, externalRepo.update(sql));
		sql = "update TR_PROJECT set PROJECT_NBR = 'CHANGED' where PROJECT_NBR = '" + projectNumber + "'";
		assertEquals(1, externalRepo.update(sql));
	}

	@Test
	public void bb_UpdateInvalidParamTest() {
		String projectNumber = TestUtils.getRandomTestString(4);
		String sql = "insert into TR_PROJECT (PROJECT_NBR) values ('" + projectNumber + "')";
		assertEquals(1, externalRepo.update(sql));
		sql = "update DOENT_EXIST set PROJECT_NBR = 'CHANGED' where PROJECT_NBR = '" + projectNumber + "'";
		assertEquals(0, externalRepo.update(sql));
		sql = "Snicklefritz";
		assertEquals(0, externalRepo.update(sql));
	}

	@Test
	public void ca_SelectTest() {
		String projectNumber = TestUtils.getRandomTestString(5);
		String sql = "insert into TR_PROJECT (PROJECT_NBR) values ('" + projectNumber + "')";
		assertEquals(1, externalRepo.update(sql));
		sql = "select PROJECT_NBR from TR_PROJECT where PROJECT_NBR = '" + projectNumber + "'";
		assertEquals(projectNumber, externalRepo.queryForMap(sql).entrySet().iterator().next().getValue().toString());
	}

	@Test
	public void cb_SelectInvalidParamTest() {
		String projectNumber = TestUtils.getRandomTestString(6);
		String sql = "insert into TR_PROJECT (PROJECT_NBR) values ('" + projectNumber + "')";
		assertEquals(1, externalRepo.update(sql));
		sql = "select MY_COLUMN from TR_PROJECT where PROJECT_NBR = '" + projectNumber + "'";
		assertNull(externalRepo.queryForMap(sql));
		sql = "select PROJECT_NBR from DOESNT_EXIST where PROJECT_NBR = '" + projectNumber + "'";
		assertNull(externalRepo.queryForMap(sql));
		sql = "select PROJECT_NBR from TR_PROJECT where PROJECT_NBR = 'NOT_A_REAL_PROJECT'";
		assertNull(externalRepo.queryForMap(sql));
		sql = "Snicklefritz";
		assertNull(externalRepo.queryForMap(sql));
	}

	@Test
	public void da_DeleteTest() {
		String projectNumber = TestUtils.getRandomTestString(7);
		String sql = "insert into TR_PROJECT (PROJECT_NBR) values ('" + projectNumber + "')";
		assertEquals(1, externalRepo.update(sql));
		sql = "delete from TR_PROJECT where PROJECT_NBR = '" + projectNumber + "'";
		assertEquals(1, externalRepo.update(sql));
		sql = "select PROJECT_NBR from TR_PROJECT where PROJECT_NBR = '" + projectNumber + "'";
		assertNull(externalRepo.queryForMap(sql));
	}

	@Test
	public void db_DeleteInvalidParamTest() {
		String projectNumber = TestUtils.getRandomTestString(8);
		String sql = "insert into TR_PROJECT (PROJECT_NBR) values ('" + projectNumber + "')";
		assertEquals(1, externalRepo.update(sql));
		sql = "delete from DOESNT_EXIST where PROJECT_NBR = '" + projectNumber + "'";
		assertEquals(0, externalRepo.update(sql));
		sql = "delete from TR_PROJECT where PROJECT_NBR = 'NOT_A_REAL_PROJECT'";
		assertEquals(0, externalRepo.update(sql));
		sql = "delete from TR_PROJECT where MY_COLUMN = '" + projectNumber + "'";
		assertEquals(0, externalRepo.update(sql));
	}

	@Test
	public void ea_GetExternalSchemaTest() {
		ExternalSchemaImpl schema = externalRepo.getExternalSchema();
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

	// @Test
	public void xx_MinimalCRUDTest() {
		if (!isSetup) {
			String table = "TEST_TABLE";
			String column = "TEST_COLUMN";
			String data = "TEST_DATA";
			String sql = "create table " + table + " (" + column + " VARCHAR(100))";
			externalRepo.execute(sql);
			sql = "insert into " + table + " (" + column + ") values ('" + data + "')";
			assertEquals(1, externalRepo.update(sql));
			sql = "select " + column + " from " + table + " where " + column + " = '" + data + "'";
			assertEquals(data, externalRepo.queryForMap(sql).entrySet().iterator().next().getValue().toString());
			sql = "update " + table + " set " + column + " = '" + data.concat(data) + "' where " + column + " = '"
					+ data + "'";
			assertEquals(1, externalRepo.update(sql));
			sql = "select " + column + " from " + table + " where " + column + " = '" + data.concat(data) + "'";
			assertEquals(data.concat(data),
					externalRepo.queryForMap(sql).entrySet().iterator().next().getValue().toString());
			sql = "delete from " + table + " where " + column + " = '" + data.concat(data) + "'";
			assertEquals(1, externalRepo.update(sql));
		}
	}

	@PostConstruct
	private void setup() {
		if (!Utils.isNotNullOrEmpty(externalRepo.getExternalSchema().getTables())) {
			if (externalUrl.contains("hsqldb") && !isSetup) {
				LOG.info("Setting Up External Schema Using HyperSQL Database.");
				File ddl = TestUtils.getFileFromClasspath("grex.ddl");
				File dml = TestUtils.getFileFromClasspath("grex.dml");
				File testData = TestUtils.getFileFromClasspath("test-data.dml");
				File logic1 = TestUtils.getFileFromClasspath("logic-scenario-1.dml");
				File logic2 = TestUtils.getFileFromClasspath("logic-scenario-2.dml");
				File logic3 = TestUtils.getFileFromClasspath("logic-scenario-3.dml");
				if (ddl.exists()) {
					externalRepo.execute(Utils.breakSqlScriptIntoStatements(ddl));
					if (dml.exists()) {
						externalRepo.execute(Utils.breakSqlScriptIntoStatements(dml));
					}
					if (testData.exists()) {
						externalRepo.execute(Utils.breakSqlScriptIntoStatements(testData));
					}
					if (logic1.exists()) {
						externalRepo.execute(Utils.breakSqlScriptIntoStatements(logic1));
					}
					if (logic2.exists()) {
						externalRepo.execute(Utils.breakSqlScriptIntoStatements(logic2));
					}
					if (logic3.exists()) {
						externalRepo.execute(Utils.breakSqlScriptIntoStatements(logic3));
					}
				}
			}
			isSetup = true;
		}
	}
}