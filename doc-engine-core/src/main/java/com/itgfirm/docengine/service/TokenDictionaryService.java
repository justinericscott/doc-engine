/**
 * TODO: License
 */
package com.itgfirm.docengine.service;

import java.util.Deque;
import java.util.Map;

import com.itgfirm.docengine.pipeline.Project;
import com.itgfirm.docengine.types.TokenData;
import com.itgfirm.docengine.types.TokenValue;
import com.itgfirm.docengine.types.jpa.TokenDefinitionJpaImpl;

/**
 * @author Justin
 * 
 *         TODO: Description
 */
interface TokenDictionaryService {
	
	void clear();

	/**
	 * TODO: Description
	 * 
	 * @param id
	 * @return
	 */
	TokenDefinitionJpaImpl findOne(Long id);

	/**
	 * TODO: Description
	 * 
	 * @param code
	 * @return
	 */
	TokenDefinitionJpaImpl findByTokenCd(String code);
	
	/**
	 * TODO: Description
	 * 
	 * @param like
	 * @return
	 */
	Iterable<TokenDefinitionJpaImpl> findByTokenCdLike(String like);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	Map<String, Map<String, Map<String, Deque<TokenDefinitionJpaImpl>>>> getDefinitionMap();
	
	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	Iterable<TokenDefinitionJpaImpl> getDictionary();

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	Map<String, String> getSqlMap();

	/**
	 * TODO: Description
	 * 
	 * @param projectId
	 * @return
	 */
	Map<String, TokenData> getTokenDataMap(String projectId);

	/**
	 * TODO: Description
	 * 
	 * @param project
	 * @return
	 */
	Map<String, Iterable<TokenValue>> getTokenValueMap(Project project);

	/**
	 * TODO: Description
	 * 
	 * @param project
	 * @return
	 */
	Map<String, ?> getDroolsSafeTokens(Project project);

	/**
	 * TODO: Description
	 * 
	 * @param project
	 * @return
	 */
	Map<String, ?> getFreemarkerSafeTokens(Project project);

	/**
	 * TODO: Description
	 * 
	 * @param item
	 * @return
	 */
	TokenDefinitionJpaImpl save(TokenDefinitionJpaImpl token);

	/**
	 * TODO: Description
	 * 
	 * @param tokens
	 * @return
	 */
	Iterable<TokenDefinitionJpaImpl> save(Iterable<TokenDefinitionJpaImpl> tokens);

	/**
	 * TODO: Description
	 *
	 */
	void refresh();

	/**
	 * TODO: Description
	 * 
	 * @param id
	 */
	void delete(Long id);

	/**
	 * TODO: Description
	 * 
	 * @param code
	 */
	void delete(String code);

	/**
	 * TODO: Description
	 * 
	 * @param token
	 */
	void delete(TokenDefinitionJpaImpl token);
	
	
}