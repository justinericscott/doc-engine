package com.itgfirm.docengine.service.token;

import static org.junit.Assert.*;
import static com.itgfirm.docengine.service.token.TokenDictionaryServiceTest.TokenDictionaryServiceTestConstants.*;
import static com.itgfirm.docengine.util.AbstractTest.TestConstants.*;
import static com.itgfirm.docengine.util.TestUtils.getFileFromClasspath;
import static com.itgfirm.docengine.util.Utils.breakSqlScriptIntoStatements;
import static com.itgfirm.docengine.util.Utils.isNotNullAndExists;
import static com.itgfirm.docengine.util.Utils.isNotNullOrEmpty;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.itgfirm.docengine.service.token.BusinessDataService;
import com.itgfirm.docengine.service.token.TokenDictionaryService;
import com.itgfirm.docengine.service.token.types.ExternalEntity;
import com.itgfirm.docengine.service.token.types.ExternalSchema;
import com.itgfirm.docengine.types.TokenDefinitionJpaImpl;
import com.itgfirm.docengine.types.TokenDefinitions;
import com.itgfirm.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
//@Ignore
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TokenDictionaryServiceTest extends AbstractTest {
	private static final Logger LOG = LoggerFactory.getLogger(TokenDictionaryServiceTest.class);
	private static boolean isSetup = false;

	@Autowired
	private TokenDictionaryService _dictionary;

	@Autowired
	private BusinessDataService _business;

	@Test
	public void a_SaveTest() {
		TokenDefinitionJpaImpl definition = createFakeTokenDefinition();
		assertNotNull(definition);
		assertTrue(definition.isValid(true));
		TokenDefinitions definitions = new TokenDefinitions(new ArrayList<TokenDefinitionJpaImpl>(
				Arrays.asList(makeTestToken(), makeTestToken(), makeTestToken(), makeTestToken(), makeTestToken())));
		definitions = _dictionary.save(definitions);
		definitions.getDefinitionsList().forEach(def -> {
			assertTrue(def.isValid(true));
		});
		assertNull(_dictionary.save((TokenDefinitionJpaImpl) null));
		assertNull(_dictionary.save(new TokenDefinitionJpaImpl("", "TOKEN_TEST_NAME")));
		assertNull(_dictionary.save(new TokenDefinitionJpaImpl(TEST_TOKEN_CODE_PREFIX + uuid(), "")));
		definition = createFakeTokenDefinition();
		assertNull(_dictionary.save(new TokenDefinitionJpaImpl(definition, definition.getTokenCd())));
	}

	@Test
	public void b_FindTest() {
		// Get All
		Iterable<TokenDefinitionJpaImpl> definitions = new ArrayList<TokenDefinitionJpaImpl>(
				Arrays.asList(createFakeTokenDefinition(), createFakeTokenDefinition(),
						createFakeTokenDefinition(), createFakeTokenDefinition(),
						createFakeTokenDefinition()));
		definitions = _dictionary.findAll().getDefinitionsList();
		definitions.forEach(t -> {
			assertNotNull(t);
			assertTrue(t.isValid(true));

		});

		// Get Token Definition
		TokenDefinitionJpaImpl def = createFakeTokenDefinition();
		def = _dictionary.findOne(def.getId());
		assertNotNull(def);
		assertTrue(def.isValid(true));
		def = _dictionary.findByTokenCd(def.getTokenCd());
		assertNotNull(def);
		assertTrue(def.isValid(true));
		assertNull(_dictionary.findByTokenCd((String) null));
		assertNull(_dictionary.findByTokenCd(""));
		assertNull(_dictionary.findOne(0L));
		assertNull(_dictionary.findByTokenCd("Snicklefritz"));
		definitions = _dictionary.findByTokenCdLike("%TEST%").getDefinitionsList();
		assertNotNull(definitions);
		assertTrue(definitions.iterator().hasNext());
		assertNull(_dictionary.findByTokenCdLike((String) null));
		assertNull(_dictionary.findByTokenCdLike(""));
		assertNull(_dictionary.findByTokenCdLike("%Snicklefritz%"));
	}

	@Test
	public void c_GetProjectTokenDataMap() {
		_dictionary.getTokenDataMap(TEST_PROJECT_ID_VALUE).forEach((key, data) -> {
			data.getValues().forEach(v -> {
				assertNotNull(v.getValue());
			});
		});
	}

	// @Test
	public void d_RefreshTokenDictionaryTest() {

	}

	@Test
	public void e_RefreshWhereEntityMapTest() {
		Collection<String> codes = new ArrayList<String>();
		final Iterable<TokenDefinitionJpaImpl> tokens = _dictionary.findAll().getDefinitionsList();
		for (TokenDefinitionJpaImpl t : _dictionary.findAll().getDefinitionsList()) {
			LOG.debug("Adding Token Code: " + t.getTokenCd());
			codes.add(t.getTokenCd());
		}
		LOG.debug("Deconstructing WhereEntity Map.");
		Iterator<Entry<String, Map<String, Map<String, Deque<TokenDefinitionJpaImpl>>>>> whereMap = _dictionary
				.getDefinitionMap().entrySet().iterator();
		while (whereMap.hasNext()) {
			Entry<String, Map<String, Map<String, Deque<TokenDefinitionJpaImpl>>>> whereMapEntry = whereMap.next();
			String whereMapKey = whereMapEntry.getKey();
			LOG.debug("Found Key At Level 1 (where.entity): " + whereMapKey);
			Iterator<Entry<String, Map<String, Deque<TokenDefinitionJpaImpl>>>> entities = whereMapEntry.getValue()
					.entrySet().iterator();
			while (entities.hasNext()) {
				Entry<String, Map<String, Deque<TokenDefinitionJpaImpl>>> entity = entities.next();
				String entityKey = entity.getKey();
				LOG.debug("Found Key At Level 2 (entity): " + entityKey);
				Iterator<Entry<String, Deque<TokenDefinitionJpaImpl>>> attributes = entity.getValue().entrySet()
						.iterator();
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
		}
	}

	@Test
	public void f_RefreshWhereEntitySqlMapTest() {
		Iterator<Entry<String, String>> whereSqlMap = _dictionary.getSqlMap().entrySet().iterator();
		while (whereSqlMap.hasNext()) {
			Entry<String, String> entry = whereSqlMap.next();
			String key = entry.getKey();
			String value = entry.getValue();
			LOG.debug("Key: " + key + " | Value: " + value);
		}
	}

	public void xx_DeleteTest() {
	}

	@Before
	public void setupBefore() {
		if (!isSetup) {
			createRealTokenDefinitions();
			if (isNotNullOrEmpty(_business)) {
				final ExternalSchema ext = _business.getExternalSchema();
				if (isNotNullOrEmpty(ext)) {
					final Iterable<ExternalEntity> tables = ext.getTables();
					if (!isNotNullOrEmpty(tables)) {
						final File ddl = getFileFromClasspath("grex.ddl");
						if (isNotNullAndExists(ddl)) {
							_business.execute(breakSqlScriptIntoStatements(ddl));
							final File dml = getFileFromClasspath("grex.dml");
							if (isNotNullAndExists(dml)) {
								_business.execute(breakSqlScriptIntoStatements(dml));
								final File testData = getFileFromClasspath("test-data.dml");
								if (isNotNullAndExists(testData)) {
									_business.execute(breakSqlScriptIntoStatements(testData));
								}
								final File logic1 = getFileFromClasspath("logic-scenario-1.dml");
								if (isNotNullAndExists(logic1)) {
									_business.execute(breakSqlScriptIntoStatements(logic1));
								}
								final File logic2 = getFileFromClasspath("logic-scenario-2.dml");
								if (isNotNullAndExists(logic2)) {
									_business.execute(breakSqlScriptIntoStatements(logic2));
								}
								final File logic3 = getFileFromClasspath("logic-scenario-3.dml");
								if (isNotNullAndExists(logic3)) {
									_business.execute(breakSqlScriptIntoStatements(logic3));
								}
							}
						}
					}
				}
			}
			isSetup = true;
		}
	}

	private TokenDefinitionJpaImpl createFakeTokenDefinition() {
		final TokenDefinitionJpaImpl token = _dictionary.save(makeTestToken());
		assertNotNull(token);
		assertTrue(token.isValid(true));
		return token;
	}

	private Iterable<TokenDefinitionJpaImpl> createRealTokenDefinitions() {
		// Project Number
		final TokenDefinitionJpaImpl projectNumberRlp = new TokenDefinitionJpaImpl(TEST_CODE_PROJECT_NBR_R101,
				TEST_NAME_PROJECT_NUMBER);
		projectNumberRlp.setEntity(TEST_NAME_ENTITY);
		projectNumberRlp.setAttribute(TEST_NAME_ATTRIBUTE_PROJECT_NBR);
		projectNumberRlp.setWhere(TEST_NAME_WHERE);
		projectNumberRlp.setDocumentCd(TEST_CODE_DOC_TYPE_R101);

		final TokenDefinitionJpaImpl projectNumberPropLease = new TokenDefinitionJpaImpl(projectNumberRlp,
				TEST_CODE_PROJECT_NBR_L201_PROPOSED);
		projectNumberPropLease.setDocumentCd(TEST_CODE_DOC_TYPE_L201);
		projectNumberPropLease.setPhase(TEST_CODE_PHASE_PROPOSED);

		final TokenDefinitionJpaImpl projectNumberAwardLease = new TokenDefinitionJpaImpl(projectNumberPropLease,
				TEST_CODE_PROJECT_NBR_L201_AWARDED);
		projectNumberAwardLease.setPhase(TEST_CODE_PHASE_AWARDED);

		// Lease Number
		final TokenDefinitionJpaImpl leaseNumberRlp = new TokenDefinitionJpaImpl(TEST_CODE_LEASE_NBR_R101,
				TEST_NAME_LEASE_NUMBER);
		leaseNumberRlp.setEntity(TEST_NAME_ENTITY);
		leaseNumberRlp.setAttribute(TEST_NAME_ATTRIBUTE_LEASE_NBR);
		leaseNumberRlp.setWhere(TEST_NAME_WHERE);
		leaseNumberRlp.setDocumentCd(TEST_CODE_DOC_TYPE_R101);

		final TokenDefinitionJpaImpl leaseNumberPropLease = new TokenDefinitionJpaImpl(leaseNumberRlp,
				TEST_CODE_LEASE_NBR_L201_PROPOSED);
		leaseNumberPropLease.setDocumentCd(TEST_CODE_DOC_TYPE_L201);
		leaseNumberPropLease.setPhase(TEST_CODE_PHASE_PROPOSED);

		final TokenDefinitionJpaImpl leaseNumberAwardLease = new TokenDefinitionJpaImpl(leaseNumberPropLease,
				TEST_CODE_LEASE_NBR_L201_AWARDED);
		leaseNumberAwardLease.setPhase(TEST_CODE_PHASE_AWARDED);

		Collection<TokenDefinitionJpaImpl> tokens = new ArrayList<TokenDefinitionJpaImpl>(
				Arrays.asList(projectNumberRlp, projectNumberPropLease, projectNumberAwardLease, leaseNumberRlp,
						leaseNumberPropLease, leaseNumberAwardLease));
		return _dictionary.save(new TokenDefinitions(tokens)).getDefinitionsList();
	}

	static class TokenDictionaryServiceTestConstants {
		static final String TEST_CODE_LEASE_MODEL_CD_L201_AWARDED = "leaseModelCd_L201_AWARD";
		static final String TEST_CODE_LEASE_MODEL_CD_L201_PROPOSED = "leaseModelCd_L201_PROPOSED";
		static final String TEST_CODE_LEASE_MODEL_CD_R101 = "leaseModelCd_R101";

		static final String TEST_CODE_LEASE_NBR_L201_AWARDED = "leaseNumber_L201_AWARD";
		static final String TEST_CODE_LEASE_NBR_L201_PROPOSED = "leaseNumber_L201_PROPOSED";
		static final String TEST_CODE_LEASE_NBR_R101 = "leaseNumber_R101";

		static final String TEST_CODE_PHASE_AWARDED = "AWARDED";
		static final String TEST_CODE_PHASE_PROPOSED = "PROPOSED";

		static final String TEST_CODE_PROJECT_NBR_L201_AWARDED = "projectNumber_L201_AWARD";
		static final String TEST_CODE_PROJECT_NBR_L201_PROPOSED = "projectNumber_L201_PROPOSED";
		static final String TEST_CODE_PROJECT_NBR_R101 = "projectNumber_R101";

		static final String TEST_NAME_ATTRIBUTE_LEASE_MODEL_CD = "LEASE_MODEL_CD";
		static final String TEST_NAME_ATTRIBUTE_LEASE_NBR = "LEASE_NBR";
		static final String TEST_NAME_ATTRIBUTE_PROJECT_NBR = "PROJECT_NBR";
		static final String TEST_NAME_ENTITY = "TR_PROJECT";
		static final String TEST_NAME_LEASE_MODEL_CD = "leaseModelCd";
		static final String TEST_NAME_LEASE_NUMBER = "leaseNumber";
		static final String TEST_NAME_PROJECT_NUMBER = "projectNumber";
		static final String TEST_NAME_WHERE = "PROJECT_NBR = ?";

	}
}