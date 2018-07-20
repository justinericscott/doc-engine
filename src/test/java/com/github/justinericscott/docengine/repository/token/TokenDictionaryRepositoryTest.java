package com.github.justinericscott.docengine.repository.token;

import static org.junit.Assert.*;

import static com.github.justinericscott.docengine.util.TestUtils.TestConstants.*;

import java.util.Collection;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.github.justinericscott.docengine.models.TokenDefinition;
import com.github.justinericscott.docengine.repository.token.TokenDictionaryRepository;
import com.github.justinericscott.docengine.util.AbstractTest;

/**
 * @author Justin Scott TODO: Description
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TokenDictionaryRepositoryTest extends AbstractTest {

	@Autowired
	private TokenDictionaryRepository _tokens;

	@Test
	public void a_SaveTest() {
		// Merge 1
		TokenDefinition token = makeTestToken();
		token = _tokens.save(token);
		assertNotNull(token);
		assertTrue(token.isValid(true));

		Collection<TokenDefinition> tokens = makeTestTokens(5);
		tokens = (Collection<TokenDefinition>) _tokens.saveAll(tokens);
		assertNotNull(tokens);
		assertFalse(tokens.isEmpty());

		try {
			_tokens.save((TokenDefinition) null);
			fail();
		} catch (Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_tokens.save(new TokenDefinition("", "TOKEN_TEST_NAME"));
			fail();
		} catch (Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_tokens.save(new TokenDefinition(TEST_CODE_PREFIX_TOKEN + uuid(), ""));
			fail();
		} catch (Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_tokens.save(new TokenDefinition(token, token.getTokenCd()));
			fail();
		} catch (Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
	}

	@Test
	public void b_FindTest() {
		// Get All
		TokenDefinition token = makeTestToken();
		token = _tokens.save(token);
		assertNotNull(token);
		assertTrue(token.isValid(true));
		final Long id = token.getId();
		final String tokenCd = token.getTokenCd();
		
		token = _tokens.findById(id).get();
		assertNotNull(token);
		assertTrue(token.isValid(true));
		
		token = _tokens.findOptionalByTokenCd(tokenCd).get();
		assertNotNull(token);
		assertTrue(token.isValid(true));
		
		Collection<TokenDefinition> tokens = (Collection<TokenDefinition>) _tokens.findAll();
		assertNotNull(tokens);
		assertFalse(tokens.isEmpty());
		tokens.forEach(t -> {
			assertTrue(t.isValid(true));
		});

		tokens = (Collection<TokenDefinition>) _tokens.findByTokenCdLike("%TEST%");
		assertNotNull(tokens);
		assertFalse(tokens.isEmpty());
		tokens.forEach(t -> {
			assertTrue(t.isValid(true));
		});

		assertFalse(_tokens.findOptionalByTokenCd((String) null).isPresent());
		assertFalse(_tokens.findOptionalByTokenCd("").isPresent());
		assertFalse(_tokens.findById(0L).isPresent());
		assertFalse(_tokens.findOptionalByTokenCd("Snicklefritz").isPresent());
		try {
			_tokens.findByTokenCdLike(null);
			fail();
		} catch (Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		tokens = (Collection<TokenDefinition>) _tokens.findByTokenCdLike("");
		assertTrue(tokens.isEmpty());
		tokens = (Collection<TokenDefinition>) _tokens.findByTokenCdLike("%Snicklefritz%");
		assertTrue(tokens.isEmpty());
	}

	// @Test
	public void da_GetWithQueryTest() {
		// TokenDefinitionJpaImpl def = createTokenDefinition(8);
		// assertNotNull(tokenDictionaryRepo.getWithQuery(DataConstants.GET_TOKEN_DEF_BY_CODE_LIKE,
		// DataConstants.PARAM_TOKEN_CD, "%TEST%"));
	}

	// @Test
	public void db_GetWithQueryInvalidParamTest() {
		// No Value
		// assertNull(tokenDictionaryRepo.getWithQuery(DataConstants.GET_TOKEN_DEF_BY_CODE_LIKE,
		// DataConstants.PARAM_TOKEN_CD, null));
		// Empty Value
		// assertNull(tokenDictionaryRepo.getWithQuery(DataConstants.GET_TOKEN_DEF_BY_CODE_LIKE,
		// DataConstants.PARAM_TOKEN_CD, ""));
		// Bad Value
		// assertNull(tokenDictionaryRepo.getWithQuery(DataConstants.GET_TOKEN_DEF_BY_CODE_LIKE,
		// DataConstants.PARAM_TOKEN_CD, "Snicklefritz"));
		// No Param Name
		// assertNull(tokenDictionaryRepo.getWithQuery(DataConstants.GET_TOKEN_DEF_BY_CODE_LIKE,
		// null, "R101"));
		// Empty Param Name
		// assertNull(tokenDictionaryRepo.getWithQuery(DataConstants.GET_TOKEN_DEF_BY_CODE_LIKE,
		// "", "R101"));
		// Bad Param Name
		// assertNull(tokenDictionaryRepo.getWithQuery(DataConstants.GET_TOKEN_DEF_BY_CODE_LIKE,
		// "Snicklefritz", "R101"));
		// Null Query
		// assertNull(tokenDictionaryRepo.getWithQuery(null,
		// DataConstants.PARAM_TOKEN_CD, "R101"));
		// Empty Query
		// assertNull(tokenDictionaryRepo.getWithQuery("",
		// DataConstants.PARAM_TOKEN_CD, "R101%"));
		// Bad Query
		// assertNull(tokenDictionaryRepo.getWithQuery("Snicklefritz",
		// DataConstants.PARAM_TOKEN_CD, "R101"));
	}

	// @Test
	public void ea_GetWithQueryMultiParamTest() {
		// List<TokenDefinitionJpaImpl> defs = new
		// ArrayList<TokenDefinitionJpaImpl>();
		// defs.add(createTokenDefinition(1));
		// defs.add(createTokenDefinition(2));
		// defs.add(createTokenDefinition(3));
		// defs.add(createTokenDefinition(4));
		// defs.add(createTokenDefinition(5));
		// String[] paramNames = { DataConstants.PARAM_TOKEN_CD,
		// DataConstants.PARAM_IS_XABLE_BLN };
		// Object[] values = { "%TEST%", false };
		// assertNotNull(tokenDictionaryRepo.getWithQuery(DataConstants.GET_TOKEN_DEFS_BY_TOKEN_CD_LIKE_AND_IS_X_ABLE,
		// paramNames, values));
		// for (TokenDefinitionJpaImpl t : defs) {
		// repo.delete(t);
		// }
	}

	// @Test
	public void eb_GetWithQueryMultiParamInvalidParamTest() {
		// String[] paramNames = { DataConstants.PARAM_CONTENT_CD,
		// DataConstants.PARAM_BODY };
		// Object[] values = { "%TEST%", false };
		// String[] noParamNames = { null, null };
		// String[] noValues = { null, null };
		// String[] emptyParamNames = { "", "" };
		// String[] emptyValues = { "", "" };
		// String[] badParamNames = { "%Snicklefritz%", "%Snicklefritz%" };
		// String[] badValues = { "%Snicklefritz%", "%Snicklefritz%" };
		// String[] shortParamNames = { "R101%" };
		// String[] shortValues = { "%BODY%" };
		// assertNull(tokenDictionaryRepo.getWithQuery(DataConstants.GET_TOKEN_DEFS_BY_TOKEN_CD_LIKE_AND_IS_X_ABLE,
		// paramNames, noValues));
		// assertNull(tokenDictionaryRepo.getWithQuery(DataConstants.GET_TOKEN_DEFS_BY_TOKEN_CD_LIKE_AND_IS_X_ABLE,
		// paramNames, emptyValues));
		// assertNull(tokenDictionaryRepo.getWithQuery(DataConstants.GET_TOKEN_DEFS_BY_TOKEN_CD_LIKE_AND_IS_X_ABLE,
		// paramNames, badValues));
		// assertNull(tokenDictionaryRepo.getWithQuery(DataConstants.GET_TOKEN_DEFS_BY_TOKEN_CD_LIKE_AND_IS_X_ABLE,
		// paramNames, shortValues));
		// assertNull(tokenDictionaryRepo.getWithQuery(DataConstants.GET_TOKEN_DEFS_BY_TOKEN_CD_LIKE_AND_IS_X_ABLE,
		// noParamNames, values));
		// assertNull(tokenDictionaryRepo.getWithQuery(DataConstants.GET_TOKEN_DEFS_BY_TOKEN_CD_LIKE_AND_IS_X_ABLE,
		// emptyParamNames, values));
		// assertNull(tokenDictionaryRepo.getWithQuery(DataConstants.GET_TOKEN_DEFS_BY_TOKEN_CD_LIKE_AND_IS_X_ABLE,
		// badParamNames, values));
		// assertNull(tokenDictionaryRepo.getWithQuery(DataConstants.GET_TOKEN_DEFS_BY_TOKEN_CD_LIKE_AND_IS_X_ABLE,
		// shortParamNames, values));
	}

	public void xx_DeleteTest() {
		// TODO: Delete Test For Token Repo
	}
}