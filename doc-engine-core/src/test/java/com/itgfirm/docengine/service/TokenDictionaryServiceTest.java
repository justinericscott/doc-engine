package com.itgfirm.docengine.service;

import static org.junit.Assert.*;
import static com.itgfirm.docengine.util.TestConstants.*;
import static com.itgfirm.docengine.util.TestUtils.getFileFromClasspath;
import static com.itgfirm.docengine.util.TestUtils.getRandomTestString;
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

import com.itgfirm.docengine.types.ExternalEntityImpl;
import com.itgfirm.docengine.types.ExternalSchemaImpl;
import com.itgfirm.docengine.types.jpa.TokenDefinitionJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TokenDictionaryServiceTest extends AbstractTest {
	private static final Logger LOG = LoggerFactory.getLogger(TokenDictionaryServiceTest.class);
	private static boolean isSetup = false;

	@Autowired
	private TokenDictionaryService dictionary;

	@Autowired
	private BusinessDataService business;

	@Test
	public void a_SaveTest() {
		TokenDefinitionJpaImpl definition = createFakeTokenDefinition(12);
		assertNotNull(definition);
		assertTrue(definition.isValid(true));
		Iterable<TokenDefinitionJpaImpl> definitions = new ArrayList<TokenDefinitionJpaImpl>(
				Arrays.asList(makeTestTokenDefinition(2), makeTestTokenDefinition(3), makeTestTokenDefinition(4),
						makeTestTokenDefinition(5), makeTestTokenDefinition(6)));
		definitions = dictionary.save(definitions);
		definitions.forEach(def -> {
			assertTrue(def.isValid(true));
		});
		assertNull(dictionary.save((TokenDefinitionJpaImpl) null));
		assertNull(dictionary.save(new TokenDefinitionJpaImpl("", "TOKEN_TEST_NAME")));
		assertNull(dictionary.save(new TokenDefinitionJpaImpl(getRandomTestString(1), "")));
		definition = createFakeTokenDefinition(34);
		assertNull(dictionary.save(new TokenDefinitionJpaImpl(definition, definition.getTokenCd())));
	}

	@Test
	public void b_FindTest() {
		// Get All
		Iterable<TokenDefinitionJpaImpl> definitions = new ArrayList<TokenDefinitionJpaImpl>(
				Arrays.asList(createFakeTokenDefinition(91), createFakeTokenDefinition(72), createFakeTokenDefinition(33),
						createFakeTokenDefinition(41), createFakeTokenDefinition(50)));
		definitions = dictionary.getDictionary();
		definitions.forEach(t -> {
			validate(t);
		});

		// Get Token Definition
		TokenDefinitionJpaImpl def = createFakeTokenDefinition(60);
		validate(dictionary.findOne(def.getId()));
		validate(dictionary.findByTokenCd(def.getTokenCd()));
		assertNull(dictionary.findByTokenCd((String) null));
		assertNull(dictionary.findByTokenCd(""));
		assertNull(dictionary.findOne(0L));
		assertNull(dictionary.findByTokenCd("Snicklefritz"));
		definitions = dictionary.findByTokenCdLike("%TEST%");
		assertNotNull(definitions);
		assertTrue(definitions.iterator().hasNext());
		assertNull(dictionary.findByTokenCdLike((String) null));
		assertNull(dictionary.findByTokenCdLike(""));
		assertNull(dictionary.findByTokenCdLike("%Snicklefritz%"));
	}

	@Test
	public void c_GetProjectTokenDataMap() {
		dictionary.getTokenDataMap(TEST_DATA_PROJECT_ID).forEach((key, data) -> {
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
		final Iterable<TokenDefinitionJpaImpl> tokens = dictionary.getDictionary();
		for (TokenDefinitionJpaImpl t : dictionary.getDictionary()) {
			LOG.debug("Adding Token Code: " + t.getTokenCd());
			codes.add(t.getTokenCd());
		}
		LOG.debug("Deconstructing WhereEntity Map.");
		Iterator<Entry<String, Map<String, Map<String, Deque<TokenDefinitionJpaImpl>>>>> whereMap = dictionary
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
		Iterator<Entry<String, String>> whereSqlMap = dictionary.getSqlMap().entrySet().iterator();
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
	public void setup() {
		if (!isSetup) {
			createRealTokenDefinitions();
			if (isNotNullOrEmpty(business)) {
				final ExternalSchemaImpl ext = business.getExternalSchema();
				if (isNotNullOrEmpty(ext)) {
					final Iterable<ExternalEntityImpl> tables = ext.getTables();
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
			isSetup = true;
		}
	}

	private TokenDefinitionJpaImpl createFakeTokenDefinition(final int seed) {
		final TokenDefinitionJpaImpl token = dictionary.save(makeTestTokenDefinition(seed));
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

		Iterable<TokenDefinitionJpaImpl> tokens = new ArrayList<TokenDefinitionJpaImpl>(
				Arrays.asList(projectNumberRlp, projectNumberPropLease, projectNumberAwardLease, leaseNumberRlp,
						leaseNumberPropLease, leaseNumberAwardLease));
		return dictionary.save(tokens);
	}
}