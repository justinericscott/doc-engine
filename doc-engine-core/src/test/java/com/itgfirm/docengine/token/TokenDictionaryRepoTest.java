package com.itgfirm.docengine.token;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.itgfirm.docengine.data.DataConstants;
import com.itgfirm.docengine.types.TokenDefinition;
import com.itgfirm.docengine.types.jpa.TokenDefinitionJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;
import com.itgfirm.docengine.util.TestUtils;

/**
 * @author Justin Scott
 * TODO: Description
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TokenDictionaryRepoTest extends AbstractTest {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(TokenDictionaryRepoTest.class);
	@Autowired
	private TokenDictionaryRepo tokenDictionaryRepo;
	
	@Test
	public void aa_MergeTest() {
		// Merge 1
		TokenDefinition token = createTokenDefinition();
		tokenDictionaryRepo.delete(token);
	}
	
	@Test
	public void ab_MergeInvalidParamTest() {
		// Null Object
		assertNull(tokenDictionaryRepo.merge(null));
		// Empty Code
		new TokenDefinitionJpaImpl("", "TOKEN_TEST_NAME");
		assertNull(tokenDictionaryRepo.merge(new TokenDefinitionJpaImpl("", "TOKEN_TEST_NAME")).getId());
		// Empty Name
		assertNull(tokenDictionaryRepo.merge(new TokenDefinitionJpaImpl(TestUtils.getRandomTestString(1), "")).getId());
		// Duplicate Codes
		TokenDefinition token = createTokenDefinition();
		assertNull(tokenDictionaryRepo.merge(new TokenDefinitionJpaImpl(token, token.getTokenCd())).getId());
		tokenDictionaryRepo.delete(token);
	}
	
	@Test
	public void ba_GetTest(){
		// Get All
		List<TokenDefinition> defs = new ArrayList<TokenDefinition>();
		defs.add(createTokenDefinition(1));
		defs.add(createTokenDefinition(2));
		defs.add(createTokenDefinition(3));
		defs.add(createTokenDefinition(4));
		defs.add(createTokenDefinition(5));
		List<TokenDefinition> allDefs = tokenDictionaryRepo.get();
		for (TokenDefinition t : allDefs) {
			validate(t);
		}
		for (TokenDefinition t : defs) {
			tokenDictionaryRepo.delete(t);
		}
		
		// Get Token Definition
		TokenDefinition def = createTokenDefinition(6);
		validate(tokenDictionaryRepo.get(def.getId()));
		validate(tokenDictionaryRepo.get(def.getTokenCd()));
		tokenDictionaryRepo.delete(def);		
	}
	
	@Test
	public void bb_GetInvalidParamTest() {
		// Empty Table
		assertNull(tokenDictionaryRepo.get());
		// Null Param
		assertNull(tokenDictionaryRepo.get((String) null));
		// Empty Param
		assertNull(tokenDictionaryRepo.get(""));
		// Zero as ID
		assertNull(tokenDictionaryRepo.get(0L));
		// Bad Code
		assertNull(tokenDictionaryRepo.get("Snicklefritz"));
	}
	
	@Test
	public void ca_GetByCodeLikeTest() {
		TokenDefinition def = createTokenDefinition(7);
		assertNotNull(tokenDictionaryRepo.getByCodeLike("%TEST%"));
		tokenDictionaryRepo.delete(def);
	}
	
	@Test
	public void cb_GetByCodeLikeInvalidParamTest() {
		// No Like String
		assertNull(tokenDictionaryRepo.getByCodeLike(null));
		// Empty Like String
		assertNull(tokenDictionaryRepo.getByCodeLike(""));
		// Bad Like String
		assertNull(tokenDictionaryRepo.getByCodeLike("%Snicklefritz%"));
	}
	
	@Test
	public void da_GetWithQueryTest() {
		TokenDefinition def = createTokenDefinition(8);
		assertNotNull(tokenDictionaryRepo.getWithQuery(
				DataConstants.GET_TOKEN_DEF_BY_CODE_LIKE,
				DataConstants.PARAM_TOKEN_CD, "%TEST%"));
		tokenDictionaryRepo.delete(def);
	}
	
	@Test
	public void db_GetWithQueryInvalidParamTest() {
		// No Value
		assertNull(tokenDictionaryRepo.getWithQuery(
				DataConstants.GET_TOKEN_DEF_BY_CODE_LIKE,
				DataConstants.PARAM_TOKEN_CD, null));
		// Empty Value
		assertNull(tokenDictionaryRepo.getWithQuery(
				DataConstants.GET_TOKEN_DEF_BY_CODE_LIKE,
				DataConstants.PARAM_TOKEN_CD, ""));
		// Bad Value
		assertNull(tokenDictionaryRepo.getWithQuery(
				DataConstants.GET_TOKEN_DEF_BY_CODE_LIKE,
				DataConstants.PARAM_TOKEN_CD, "Snicklefritz"));
		// No Param Name
		assertNull(tokenDictionaryRepo.getWithQuery(
				DataConstants.GET_TOKEN_DEF_BY_CODE_LIKE, null, "R101"));
		// Empty Param Name
		assertNull(tokenDictionaryRepo.getWithQuery(
				DataConstants.GET_TOKEN_DEF_BY_CODE_LIKE, "", "R101"));
		// Bad Param Name
		assertNull(tokenDictionaryRepo.getWithQuery(
				DataConstants.GET_TOKEN_DEF_BY_CODE_LIKE, "Snicklefritz",
				"R101"));
		// Null Query
		assertNull(tokenDictionaryRepo.getWithQuery(null,
				DataConstants.PARAM_TOKEN_CD, "R101"));
		// Empty Query
		assertNull(tokenDictionaryRepo.getWithQuery("", DataConstants.PARAM_TOKEN_CD,
				"R101%"));
		// Bad Query
		assertNull(tokenDictionaryRepo.getWithQuery("Snicklefritz",
				DataConstants.PARAM_TOKEN_CD, "R101"));	
	}
	
	@Test
	public void ea_GetWithQueryMultiParamTest() {
		List<TokenDefinition> defs = new ArrayList<TokenDefinition>();
		defs.add(createTokenDefinition(1));
		defs.add(createTokenDefinition(2));
		defs.add(createTokenDefinition(3));
		defs.add(createTokenDefinition(4));
		defs.add(createTokenDefinition(5));
		String[] paramNames = { DataConstants.PARAM_TOKEN_CD, DataConstants.PARAM_IS_XABLE_BLN };
		Object[] values = { "%TEST%", false };
		assertNotNull(tokenDictionaryRepo.getWithQuery(
				DataConstants.GET_TOKEN_DEFS_BY_TOKEN_CD_LIKE_AND_IS_X_ABLE, 
				paramNames, values));
		for (TokenDefinition t : defs) {
			tokenDictionaryRepo.delete(t);
		}
	}
	
	@Test
	public void eb_GetWithQueryMultiParamInvalidParamTest() {
		String[] paramNames = { DataConstants.PARAM_CONTENT_CD,
				DataConstants.PARAM_BODY };
		Object[] values = { "%TEST%", false };
		String[] noParamNames = { null, null };
		String[] noValues = { null, null };
		String[] emptyParamNames = { "", "" };
		String[] emptyValues = { "", "" };
		String[] badParamNames = { "%Snicklefritz%", "%Snicklefritz%" };
		String[] badValues = { "%Snicklefritz%", "%Snicklefritz%" };
		String[] shortParamNames = { "R101%" };
		String[] shortValues = { "%BODY%" };
		assertNull(tokenDictionaryRepo.getWithQuery(
				DataConstants.GET_TOKEN_DEFS_BY_TOKEN_CD_LIKE_AND_IS_X_ABLE,
				paramNames, noValues));
		assertNull(tokenDictionaryRepo.getWithQuery(
				DataConstants.GET_TOKEN_DEFS_BY_TOKEN_CD_LIKE_AND_IS_X_ABLE,
				paramNames, emptyValues));
		assertNull(tokenDictionaryRepo.getWithQuery(
				DataConstants.GET_TOKEN_DEFS_BY_TOKEN_CD_LIKE_AND_IS_X_ABLE,
				paramNames, badValues));
		assertNull(tokenDictionaryRepo.getWithQuery(
				DataConstants.GET_TOKEN_DEFS_BY_TOKEN_CD_LIKE_AND_IS_X_ABLE,
				paramNames, shortValues));
		assertNull(tokenDictionaryRepo.getWithQuery(
				DataConstants.GET_TOKEN_DEFS_BY_TOKEN_CD_LIKE_AND_IS_X_ABLE,
				noParamNames, values));
		assertNull(tokenDictionaryRepo.getWithQuery(
				DataConstants.GET_TOKEN_DEFS_BY_TOKEN_CD_LIKE_AND_IS_X_ABLE,
				emptyParamNames, values));
		assertNull(tokenDictionaryRepo.getWithQuery(
				DataConstants.GET_TOKEN_DEFS_BY_TOKEN_CD_LIKE_AND_IS_X_ABLE,
				badParamNames, values));
		assertNull(tokenDictionaryRepo.getWithQuery(
				DataConstants.GET_TOKEN_DEFS_BY_TOKEN_CD_LIKE_AND_IS_X_ABLE,
				shortParamNames, values));
	}
	
	public void xx_DeleteTest() {
		// TODO: Delete Test For Token Repo
	}
	
	private TokenDefinition createTokenDefinition() {
		return createTokenDefinition(1);
	}
	
	private TokenDefinition createTokenDefinition(int seed) {
		TokenDefinition token = tokenDictionaryRepo.merge(makeTestTokenDefinition(seed));
		validate(token);
		return token;
	}
}