package com.itgfirm.docengine.repository.token;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.itgfirm.docengine.repository.token.TokenDictionaryRepository;
import com.itgfirm.docengine.types.TokenDefinitionJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;
import com.itgfirm.docengine.util.TestUtils;

/**
 * @author Justin Scott TODO: Description
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TokenDictionaryRepositoryTest extends AbstractTest {

	@Autowired
	private TokenDictionaryRepository repo;

	@Test
	public void a_SaveTest() {
		// Merge 1
		TokenDefinitionJpaImpl definition = createTokenDefinition(11);
		assertNotNull(definition);
		assertTrue(definition.isValid(true));
		try {
			repo.save((TokenDefinitionJpaImpl) null);
			fail();
		} catch (Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}		
		try {
			repo.save(new TokenDefinitionJpaImpl("", "TOKEN_TEST_NAME"));
			fail();
		} catch (Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			repo.save(new TokenDefinitionJpaImpl(TestUtils.getRandomTestString(1), ""));
			fail();
		} catch (Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		definition = new TokenDefinitionJpaImpl(definition, definition.getTokenCd());
		assertNotNull(definition);
		assertFalse(definition.isValid());
		try {
			repo.save(definition);
			fail();
		} catch (Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}		
	}

	@Test
	public void b_FindTest() {
		// Get All
		TokenDefinitionJpaImpl definition = createTokenDefinition(6);
		definition = repo.findOne(definition.getId());
		assertNotNull(definition);
		assertTrue(definition.isValid(true));
		definition = repo.findByTokenCd(definition.getTokenCd());
		assertNotNull(definition);
		assertTrue(definition.isValid(true));
		Collection<TokenDefinitionJpaImpl> list = new ArrayList<TokenDefinitionJpaImpl>();
		list.add(createTokenDefinition(12));
		list.add(createTokenDefinition(21));
		list.add(createTokenDefinition(32));
		list.add(createTokenDefinition(43));
		list.add(createTokenDefinition(54));
		Iterable<TokenDefinitionJpaImpl> definitions = repo.findAll();
		definitions.forEach(def -> {
			assertTrue(def.isValid(true));
		});
		// Get Token Definition
		assertNull(repo.findByTokenCd((String) null));
		assertNull(repo.findByTokenCd(""));
		assertNull(repo.findOne(0L));
		assertNull(repo.findByTokenCd("Snicklefritz"));
		definitions = repo.findByTokenCdLike("%TEST%");
		assertNotNull(definitions);
		assertTrue(definitions.iterator().hasNext());
		definitions.forEach(def -> {
			assertTrue(def.isValid(true));
		});
		try {
			repo.findByTokenCdLike(null);
			fail();
		} catch (Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}		
		assertFalse(repo.findByTokenCdLike("").iterator().hasNext());
		assertFalse(repo.findByTokenCdLike("%Snicklefritz%").iterator().hasNext());
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

	private TokenDefinitionJpaImpl createTokenDefinition(final int seed) {
		final TokenDefinitionJpaImpl token = repo.save(makeTestTokenDefinition(seed));
		assertNotNull(token);
		assertTrue(token.isValid(true));
		return token;
	}
}