/**TODO: License
 */
package com.itgfirm.docengine.service.content;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;

import static com.itgfirm.docengine.util.AbstractTest.TestConstants.*;
import static com.itgfirm.docengine.util.TestUtils.getFileFromClasspath;
import static com.itgfirm.docengine.util.Utils.breakSqlScriptIntoStatements;
import static com.itgfirm.docengine.util.Utils.isNotNullAndExists;
import static com.itgfirm.docengine.util.Utils.isNotNullOrEmpty;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.itgfirm.docengine.service.content.InitializerService;
import com.itgfirm.docengine.service.content.InstanceService;
import com.itgfirm.docengine.service.ix.ImportExportService;
import com.itgfirm.docengine.service.token.BusinessDataService;
import com.itgfirm.docengine.service.token.types.ExternalEntity;
import com.itgfirm.docengine.service.token.types.ExternalSchema;
import com.itgfirm.docengine.types.DocumentInstanceJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Ignore
@SuppressWarnings({ "unused" })
@Deprecated
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InitializerServiceTest extends AbstractTest {
	public static final Logger LOG = LoggerFactory.getLogger(InitializerServiceTest.class);

//	@Autowired
//	private InitializerService documentService;

	@Autowired
	private InstanceService instanceService;

	@Autowired
	private BusinessDataService business;

	@Autowired
	private ImportExportService ix;

//	@Test
	public void aa_CreateTest() {
//		DocumentInstanceJpaImpl doc = (DocumentInstanceJpaImpl) documentService.create(TEST_DATA_PROJECT_ID,
//				TEST_CODE_DOC_TYPE_R101);
//		assertNotNull(doc);
//		assertNotNull(doc.getId());
		// instanceService.delete(doc);
		// assertNull(instanceService.findOne(doc.getId()));
	}

	@Before
	public void setup() {
		if (isNotNullOrEmpty(business)) {
			final ExternalSchema ext = business.getExternalSchema();
			if (isNotNullOrEmpty(ext)) {
				final Iterable<ExternalEntity> tables = ext.getTables();
				if (!isNotNullOrEmpty(tables)) {
					final File ddl = getFileFromClasspath("grex.ddl");
					if (isNotNullAndExists(ddl)) {
						business.execute(breakSqlScriptIntoStatements(ddl));
						final File dml = getFileFromClasspath("grex.dml");
						if (isNotNullAndExists(dml)) {
							business.execute(breakSqlScriptIntoStatements(dml));
							final File testData = getFileFromClasspath("test-data.dml");
							if (isNotNullAndExists(testData)) {
								business.execute(breakSqlScriptIntoStatements(testData));
							}
							final File logic1 = getFileFromClasspath("logic-scenario-1.dml");
							if (isNotNullAndExists(logic1)) {
								business.execute(breakSqlScriptIntoStatements(logic1));
							}
							final File logic2 = getFileFromClasspath("logic-scenario-2.dml");
							if (isNotNullAndExists(logic2)) {
								business.execute(breakSqlScriptIntoStatements(logic2));
							}
							final File logic3 = getFileFromClasspath("logic-scenario-3.dml");
							if (isNotNullAndExists(logic3)) {
								business.execute(breakSqlScriptIntoStatements(logic3));
							}
						}
					}
				}
			}
		}
	}
}