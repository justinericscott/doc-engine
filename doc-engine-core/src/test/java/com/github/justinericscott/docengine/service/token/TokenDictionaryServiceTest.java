package com.github.justinericscott.docengine.service.token;

import static org.junit.Assert.*;
import static com.github.justinericscott.docengine.service.token.TokenDictionaryServiceTest.TokenDictionaryServiceTestConstants.*;
import static com.github.justinericscott.docengine.util.AbstractTest.TestConstants.*;
import static com.github.justinericscott.docengine.util.TestUtils.getFileFromClasspath;
import static com.github.justinericscott.docengine.util.Utils.breakSqlScriptIntoStatements;
import static com.github.justinericscott.docengine.util.Utils.isNotNullAndExists;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.github.justinericscott.docengine.models.TokenDefinition;
import com.github.justinericscott.docengine.models.TokenDefinitions;
import com.github.justinericscott.docengine.service.token.BusinessDataService;
import com.github.justinericscott.docengine.service.token.TokenDictionaryService;
import com.github.justinericscott.docengine.service.token.types.ExternalEntity;
import com.github.justinericscott.docengine.service.token.types.ExternalSchema;
import com.github.justinericscott.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TokenDictionaryServiceTest extends AbstractTest {
	private static boolean isSetup = false;

	@Autowired
	private TokenDictionaryService _dictionary;

	@Autowired
	private BusinessDataService _business;

	@Test
	public void a_SaveTest() {
		TokenDefinition definition = createFakeTokenDefinition();
		assertNotNull(definition);
		assertTrue(definition.isValid(true));
		TokenDefinitions definitions = new TokenDefinitions(new ArrayList<TokenDefinition>(
				Arrays.asList(makeTestToken(), makeTestToken(), makeTestToken(), makeTestToken(), makeTestToken())));
		definitions = _dictionary.save(definitions);
		Arrays.asList(definitions.getTokens()).forEach(def -> {
			assertTrue(def.isValid(true));
		});
		assertNull(_dictionary.save((TokenDefinition) null));
		assertNull(_dictionary.save(new TokenDefinition("", "TOKEN_TEST_NAME")));
		assertNull(_dictionary.save(new TokenDefinition(TEST_TOKEN_CODE_PREFIX + uuid(), "")));
		definition = createFakeTokenDefinition();
		assertNull(_dictionary.save(new TokenDefinition(definition, definition.getTokenCd())));
	}

	@Test
	public void b_FindTest() {
		// Get All
		Iterable<TokenDefinition> definitions = new ArrayList<TokenDefinition>(
				Arrays.asList(createFakeTokenDefinition(), createFakeTokenDefinition(),
						createFakeTokenDefinition(), createFakeTokenDefinition(),
						createFakeTokenDefinition()));
		definitions = Arrays.asList(_dictionary.findAll().getTokens());
		definitions.forEach(t -> {
			assertNotNull(t);
			assertTrue(t.isValid(true));

		});

		// Get Token Definition
		TokenDefinition def = createFakeTokenDefinition();
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
		definitions = Arrays.asList(_dictionary.findByTokenCdLike("%TEST%").getTokens());
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
		final Iterable<TokenDefinition> tokens = Arrays.asList(_dictionary.findAll().getTokens());
		for (TokenDefinition t : _dictionary.findAll().getTokens()) {
			codes.add(t.getTokenCd());
		}
		Iterator<Entry<String, Map<String, Map<String, Deque<TokenDefinition>>>>> whereMap = _dictionary
				.getDefinitionMap().entrySet().iterator();
		while (whereMap.hasNext()) {
			Entry<String, Map<String, Map<String, Deque<TokenDefinition>>>> whereMapEntry = whereMap.next();
			Iterator<Entry<String, Map<String, Deque<TokenDefinition>>>> entities = whereMapEntry.getValue()
					.entrySet().iterator();
			while (entities.hasNext()) {
				Entry<String, Map<String, Deque<TokenDefinition>>> entity = entities.next();
				Iterator<Entry<String, Deque<TokenDefinition>>> attributes = entity.getValue().entrySet()
						.iterator();
				while (attributes.hasNext()) {
					Entry<String, Deque<TokenDefinition>> attribute = attributes.next();
					Iterator<TokenDefinition> definitions = attribute.getValue().iterator();
					while (definitions.hasNext()) {
						TokenDefinition definition = definitions.next();
						assertTrue(definition.isValid(true));
					}
				}
			}
		}
		for (TokenDefinition t : tokens) {
			assertNotNull(t.getId());
		}
	}

//	@Test
	public void f_RefreshWhereEntitySqlMapTest() {
		
	}

	public void xx_DeleteTest() {
	}

	@PostConstruct
	private void setup() {
		if (isNotNullOrEmpty(_business)) {
			final ExternalSchema schema = _business.getExternalSchema();
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
					} else if (isNotNullOrEmpty(url) && url.contains("mysql") && !isSetup) {
						ddl = getFileFromClasspath("sql/grex-mysql.ddl");
						dml = getFileFromClasspath("sql/grex-mysql.dml");
						logic1 = getFileFromClasspath("sql/logic-mysql-scenario-1.dml");
						logic2 = getFileFromClasspath("sql/logic-mysql-scenario-2.dml");
						logic3 = getFileFromClasspath("sql/logic-mysql-scenario-3.dml");
					}
					if (isNotNullAndExists(ddl)) {
						_business.execute(breakSqlScriptIntoStatements(ddl));
						if (isNotNullAndExists(dml)) {
							_business.execute(breakSqlScriptIntoStatements(dml));
						}
						if (isNotNullAndExists(testData)) {
							_business.execute(breakSqlScriptIntoStatements(testData));
						}
						if (isNotNullAndExists(logic1)) {
							_business.execute(breakSqlScriptIntoStatements(logic1));
						}
						if (isNotNullAndExists(logic2)) {
							_business.execute(breakSqlScriptIntoStatements(logic2));
						}
						if (isNotNullAndExists(logic3)) {
							_business.execute(breakSqlScriptIntoStatements(logic3));
						}
					}
					if (!isSetup) {
						createRealTokenDefinitions();
					}
				}
			}
		}
		isSetup = true;
	}

	private TokenDefinition createFakeTokenDefinition() {
		final TokenDefinition token = _dictionary.save(makeTestToken());
		assertNotNull(token);
		assertTrue(token.isValid(true));
		return token;
	}

	private void createRealTokenDefinitions() {
		// Project Number
		final TokenDefinition projectNumberRlp = new TokenDefinition(TEST_CODE_PROJECT_NBR_R101,
				TEST_NAME_PROJECT_NUMBER);
		projectNumberRlp.setEntity(TEST_NAME_ENTITY);
		projectNumberRlp.setAttribute(TEST_NAME_ATTRIBUTE_PROJECT_NBR);
		projectNumberRlp.setWhere(TEST_NAME_WHERE);
		projectNumberRlp.setDocumentCd(TEST_CODE_DOC_TYPE_R101);

		final TokenDefinition projectNumberPropLease = new TokenDefinition(projectNumberRlp,
				TEST_CODE_PROJECT_NBR_L201_PROPOSED);
		projectNumberPropLease.setDocumentCd(TEST_CODE_DOC_TYPE_L201);
		projectNumberPropLease.setPhase(TEST_CODE_PHASE_PROPOSED);

		final TokenDefinition projectNumberAwardLease = new TokenDefinition(projectNumberPropLease,
				TEST_CODE_PROJECT_NBR_L201_AWARDED);
		projectNumberAwardLease.setPhase(TEST_CODE_PHASE_AWARDED);

		// Lease Number
		final TokenDefinition leaseNumberRlp = new TokenDefinition(TEST_CODE_LEASE_NBR_R101,
				TEST_NAME_LEASE_NUMBER);
		leaseNumberRlp.setEntity(TEST_NAME_ENTITY);
		leaseNumberRlp.setAttribute(TEST_NAME_ATTRIBUTE_LEASE_NBR);
		leaseNumberRlp.setWhere(TEST_NAME_WHERE);
		leaseNumberRlp.setDocumentCd(TEST_CODE_DOC_TYPE_R101);

		final TokenDefinition leaseNumberPropLease = new TokenDefinition(leaseNumberRlp,
				TEST_CODE_LEASE_NBR_L201_PROPOSED);
		leaseNumberPropLease.setDocumentCd(TEST_CODE_DOC_TYPE_L201);
		leaseNumberPropLease.setPhase(TEST_CODE_PHASE_PROPOSED);

		final TokenDefinition leaseNumberAwardLease = new TokenDefinition(leaseNumberPropLease,
				TEST_CODE_LEASE_NBR_L201_AWARDED);
		leaseNumberAwardLease.setPhase(TEST_CODE_PHASE_AWARDED);

		Collection<TokenDefinition> tokens = new ArrayList<TokenDefinition>(
				Arrays.asList(projectNumberRlp, projectNumberPropLease, projectNumberAwardLease, leaseNumberRlp,
						leaseNumberPropLease, leaseNumberAwardLease));
		TokenDefinitions defs = new TokenDefinitions(tokens);
		try {
			_dictionary.save(defs);
		} catch (final DataIntegrityViolationException e) {
			// Eat it ...
		}
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