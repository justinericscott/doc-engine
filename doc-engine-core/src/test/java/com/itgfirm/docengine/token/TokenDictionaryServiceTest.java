package com.itgfirm.docengine.token;

import static org.junit.Assert.*;
import static com.itgfirm.docengine.DocEngine.Constants.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.itgfirm.docengine.types.jpa.TokenDefinitionJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;
import com.itgfirm.docengine.util.TestConstants;
import com.itgfirm.docengine.util.TestUtils;
import com.itgfirm.docengine.util.Utils;

/**
 * @author Justin Scott TODO: Description
 */
@Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SuppressWarnings(value = "deprecation" )
public class TokenDictionaryServiceTest extends AbstractTest {
	private static final Logger LOG = LoggerFactory.getLogger(TokenDictionaryServiceTest.class);
	private static boolean isSetup = false;
	@Autowired
	private TokenDictionaryService tokenDictionaryService;
//	@Autowired
	private DefaultExternalDataRepoImpl externalRepo;
	@Value(value = DATASTORE_SECONDARY)
	private String externalUrl;

	@Test
	public void aa_MergeTest() {
		// Merge 1
		TokenDefinitionJpaImpl token = createFakeTokenDefinition();
		tokenDictionaryService.delete(token);

		List<TokenDefinitionJpaImpl> tokens = new ArrayList<TokenDefinitionJpaImpl>();
		tokens.add(makeTestTokenDefinition(2));
		tokens.add(makeTestTokenDefinition(3));
		tokens.add(makeTestTokenDefinition(4));
		tokens.add(makeTestTokenDefinition(5));
		tokens.add(makeTestTokenDefinition(6));
		tokens = tokenDictionaryService.merge(tokens);
		for (TokenDefinitionJpaImpl t : tokens) {
			assertNotNull(t.getId());
			tokenDictionaryService.delete(t);
		}
		assertNull(tokenDictionaryService.get());
	}

	@Test
	public void ab_MergeInvalidParamTest() {
		// Null Object
		assertNull(tokenDictionaryService.merge((TokenDefinitionJpaImpl) null));
		// Empty Code
		new TokenDefinitionJpaImpl("", "TOKEN_TEST_NAME");
		assertNull(tokenDictionaryService.merge(new TokenDefinitionJpaImpl("", "TOKEN_TEST_NAME")).getId());
		// Empty Name
		assertNull(
				tokenDictionaryService.merge(new TokenDefinitionJpaImpl(TestUtils.getRandomTestString(1), "")).getId());
		// Duplicate Codes
		TokenDefinitionJpaImpl token = createFakeTokenDefinition();
		assertNull(tokenDictionaryService.merge(new TokenDefinitionJpaImpl(token, token.getTokenCd())).getId());
		tokenDictionaryService.delete(token);
		assertNull(tokenDictionaryService.get());
	}

	@Test
	public void ba_GetTest() {
		// Get All
		List<TokenDefinitionJpaImpl> defs = new ArrayList<TokenDefinitionJpaImpl>();
		defs.add(createFakeTokenDefinition(1));
		defs.add(createFakeTokenDefinition(2));
		defs.add(createFakeTokenDefinition(3));
		defs.add(createFakeTokenDefinition(4));
		defs.add(createFakeTokenDefinition(5));
		List<TokenDefinitionJpaImpl> allDefs = tokenDictionaryService.get();
		assertEquals(defs.size(), allDefs.size());
		for (TokenDefinitionJpaImpl t : allDefs) {
//			validate(t);
			tokenDictionaryService.delete(t);
			assertNull(tokenDictionaryService.get(t.getId()));
		}
		assertNull(tokenDictionaryService.get());

		// Get Token Definition
		TokenDefinitionJpaImpl def = createFakeTokenDefinition(6);
//		validate(tokenDictionaryService.get(def.getId()));
//		validate(tokenDictionaryService.get(def.getTokenCd()));
		tokenDictionaryService.delete(def);
		assertNull(tokenDictionaryService.get());
	}

	@Test
	public void bb_GetInvalidParamTest() {
		// Empty Table
		assertNull(tokenDictionaryService.get());
		// Null Param
		assertNull(tokenDictionaryService.get((String) null));
		// Empty Param
		assertNull(tokenDictionaryService.get(""));
		// Zero as ID
		assertNull(tokenDictionaryService.get(0L));
		// Bad Code
		assertNull(tokenDictionaryService.get("Snicklefritz"));
		assertNull(tokenDictionaryService.get());
	}

//	@Test
//	public void ca_GetWithCodeLikeTest() {
//		TokenDefinitionJpaImpl def = createFakeTokenDefinition(7);
//		assertNotNull(tokenDictionaryService.getByCodeLike("%TEST%"));
//		tokenDictionaryService.delete(def);
//		assertNull(tokenDictionaryService.get());
//	}

//	@Test
//	public void cb_GetWithCodeLikeInvalidParamTest() {
//		// No Like String
//		assertNull(tokenDictionaryService.getByCodeLike(null));
//		// Empty Like String
//		assertNull(tokenDictionaryService.getByCodeLike(""));
//		// Bad Like String
//		assertNull(tokenDictionaryService.getByCodeLike("%Snicklefritz%"));
//		assertNull(tokenDictionaryService.get());
//	}

	@Test
	public void da_GetProjectTokenDataMap() {
		String projectId = TestConstants.TEST_DATA_PROJECT_ID;
		assertNull(tokenDictionaryService.get());
		assertNull(tokenDictionaryService.getTokenDataMap(projectId));
		setupExternalSchema();
		List<TokenDefinitionJpaImpl> tokens = createRealTokenDefinitions();
		assertEquals(tokens.size(), tokenDictionaryService.get().size());
		LOG.debug("Generating Project TokenData Map For: " + projectId);

		Iterator<Entry<String, DefaultTokenDataImpl>> tokendataMap = tokenDictionaryService.getTokenDataMap(projectId).entrySet()
				.iterator();
		while (tokendataMap.hasNext()) {
			Entry<String, DefaultTokenDataImpl> t = tokendataMap.next();
			String key = t.getKey();
			DefaultTokenDataImpl data = t.getValue();
			LOG.debug("Checking TokenData: " + data.getName());
			for (DefaultTokenValueImpl v : data.getValues()) {
				LOG.debug("Project TokenData Map Result Key: " + key + " | Value: " + v.getDefinition().getTokenCd()
						+ ": " + v.getDisplayValue());
			}
		}
		for (TokenDefinitionJpaImpl d : tokens) {
			tokenDictionaryService.delete(d);
			assertNull(tokenDictionaryService.get(d.getId()));
		}
		assertNull(tokenDictionaryService.get());
	}

	@Test
	public void ea_RefreshTokenDictionaryTest() {
		assertNull(tokenDictionaryService.get());
		setupExternalSchema();
		List<TokenDefinitionJpaImpl> tokens = createRealTokenDefinitions();
		assertEquals(tokens.size(), tokenDictionaryService.get().size());
		for (TokenDefinitionJpaImpl t : tokens) {
			assertNotNull(t.getId());
			tokenDictionaryService.delete(t);
		}
		assertNull(tokenDictionaryService.get());
	}

	@Test
	public void eb_RefreshWhereEntityMapTest() {
		assertNull(tokenDictionaryService.get());
		assertNull(tokenDictionaryService.getDefinitionMap());
		setupExternalSchema();
		List<TokenDefinitionJpaImpl> tokens = createRealTokenDefinitions();
		LOG.debug("Created " + tokens.size() + " Token Definitions.");
		List<String> codes = new ArrayList<String>();
		for (TokenDefinitionJpaImpl t : tokens) {
			LOG.debug("Adding Token Code: " + t.getTokenCd());
			codes.add(t.getTokenCd());
		}
		assertEquals(tokens.size(), tokenDictionaryService.get().size());
		LOG.debug("Deconstructing WhereEntity Map.");
		Iterator<Entry<String, Map<String, Map<String, Deque<TokenDefinitionJpaImpl>>>>> whereMap = tokenDictionaryService
				.getDefinitionMap().entrySet().iterator();
		while (whereMap.hasNext()) {
			Entry<String, Map<String, Map<String, Deque<TokenDefinitionJpaImpl>>>> whereMapEntry = whereMap.next();
			String whereMapKey = whereMapEntry.getKey();
			LOG.debug("Found Key At Level 1 (where.entity): " + whereMapKey);
			Iterator<Entry<String, Map<String, Deque<TokenDefinitionJpaImpl>>>> entities = whereMapEntry.getValue().entrySet()
					.iterator();
			while (entities.hasNext()) {
				Entry<String, Map<String, Deque<TokenDefinitionJpaImpl>>> entity = entities.next();
				String entityKey = entity.getKey();
				LOG.debug("Found Key At Level 2 (entity): " + entityKey);
				Iterator<Entry<String, Deque<TokenDefinitionJpaImpl>>> attributes = entity.getValue().entrySet().iterator();
				while (attributes.hasNext()) {
					Entry<String, Deque<TokenDefinitionJpaImpl>> attribute = attributes.next();
					String attributeKey = attribute.getKey();
					LOG.debug("Found Key At Level 3 (attribute): " + attributeKey);
					Iterator<TokenDefinitionJpaImpl> definitions = attribute.getValue().iterator();
					while (definitions.hasNext()) {
						TokenDefinitionJpaImpl definition = definitions.next();
						LOG.debug("Found Definitions: " + definition.toString());
					}

				}
			}
		}
		for (TokenDefinitionJpaImpl t : tokens) {
			assertNotNull(t.getId());
			tokenDictionaryService.delete(t);
		}
		assertNull(tokenDictionaryService.get());
	}

	@Test
	public void ec_RefreshWhereEntitySqlMapTest() {
		LOG.debug("Examining The WhereEntitySql Map.");
		assertNull(tokenDictionaryService.get());
		assertNull(tokenDictionaryService.getDefinitionMap());
		setupExternalSchema();
		List<TokenDefinitionJpaImpl> tokens = createRealTokenDefinitions();
		assertEquals(tokens.size(), tokenDictionaryService.get().size());
		Iterator<Entry<String, String>> whereSqlMap = tokenDictionaryService.getSqlMap().entrySet().iterator();
		while (whereSqlMap.hasNext()) {
			Entry<String, String> entry = whereSqlMap.next();
			String key = entry.getKey();
			String value = entry.getValue();
			LOG.debug("Key: " + key + " | Value: " + value);
		}
		for (TokenDefinitionJpaImpl t : tokens) {
			tokenDictionaryService.delete(t);
			assertNull(tokenDictionaryService.get(t.getId()));
		}
		assertNull(tokenDictionaryService.get());
	}

	public void xx_DeleteTest() {
	}

	private TokenDefinitionJpaImpl createFakeTokenDefinition() {
		return createFakeTokenDefinition(1);
	}

	private TokenDefinitionJpaImpl createFakeTokenDefinition(int seed) {
		TokenDefinitionJpaImpl token = tokenDictionaryService.merge(makeTestTokenDefinition(seed));
//		validate(token);
		return token;
	}

	private List<TokenDefinitionJpaImpl> createRealTokenDefinitions() {
		// Project Number
		TokenDefinitionJpaImpl projectNumberRlp = new TokenDefinitionJpaImpl(TestConstants.TEST_CODE_PROJECT_NBR_R101,
				TestConstants.TEST_NAME_PROJECT_NUMBER);
		projectNumberRlp.setEntity(TestConstants.TEST_NAME_ENTITY);
		projectNumberRlp.setAttribute(TestConstants.TEST_NAME_ATTRIBUTE_PROJECT_NBR);
		projectNumberRlp.setWhere(TestConstants.TEST_NAME_WHERE);
		projectNumberRlp.setDocumentCd(TestConstants.TEST_CODE_DOC_TYPE_R101);

		TokenDefinitionJpaImpl projectNumberPropLease = new TokenDefinitionJpaImpl(projectNumberRlp,
				TestConstants.TEST_CODE_PROJECT_NBR_L201_PROPOSED);
		projectNumberPropLease.setDocumentCd(TestConstants.TEST_CODE_DOC_TYPE_L201);
		projectNumberPropLease.setPhase(TestConstants.TEST_CODE_PHASE_PROPOSED);

		TokenDefinitionJpaImpl projectNumberAwardLease = new TokenDefinitionJpaImpl(projectNumberPropLease,
				TestConstants.TEST_CODE_PROJECT_NBR_L201_AWARDED);
		projectNumberAwardLease.setPhase(TestConstants.TEST_CODE_PHASE_AWARDED);

		// Lease Number
		TokenDefinitionJpaImpl leaseNumberRlp = new TokenDefinitionJpaImpl(TestConstants.TEST_CODE_LEASE_NBR_R101,
				TestConstants.TEST_NAME_LEASE_NUMBER);
		leaseNumberRlp.setEntity(TestConstants.TEST_NAME_ENTITY);
		leaseNumberRlp.setAttribute(TestConstants.TEST_NAME_ATTRIBUTE_LEASE_NBR);
		leaseNumberRlp.setWhere(TestConstants.TEST_NAME_WHERE);
		leaseNumberRlp.setDocumentCd(TestConstants.TEST_CODE_DOC_TYPE_R101);

		TokenDefinitionJpaImpl leaseNumberPropLease = new TokenDefinitionJpaImpl(leaseNumberRlp,
				TestConstants.TEST_CODE_LEASE_NBR_L201_PROPOSED);
		leaseNumberPropLease.setDocumentCd(TestConstants.TEST_CODE_DOC_TYPE_L201);
		leaseNumberPropLease.setPhase(TestConstants.TEST_CODE_PHASE_PROPOSED);

		TokenDefinitionJpaImpl leaseNumberAwardLease = new TokenDefinitionJpaImpl(leaseNumberPropLease,
				TestConstants.TEST_CODE_LEASE_NBR_L201_AWARDED);
		leaseNumberAwardLease.setPhase(TestConstants.TEST_CODE_PHASE_AWARDED);

		List<TokenDefinitionJpaImpl> tokens = new ArrayList<TokenDefinitionJpaImpl>();
		tokens.add(projectNumberRlp);
		tokens.add(projectNumberPropLease);
		tokens.add(projectNumberAwardLease);
		tokens.add(leaseNumberRlp);
		tokens.add(leaseNumberPropLease);
		tokens.add(leaseNumberAwardLease);
//		tokens = tokenDictionaryService.merge(tokens);
//		for (TokenDefinitionJpaImpl t : tokens) {
//			validate(t);
//		}
		return tokens;
	}

	private void setupExternalSchema() {
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