/**
 * TODO: License
 */
package com.itgfirm.docengine.service.token;

import java.util.Deque;
import java.util.Map;

import com.itgfirm.docengine.pipeline.Project;
import com.itgfirm.docengine.service.token.types.TokenData;
import com.itgfirm.docengine.service.token.types.TokenValue;
import com.itgfirm.docengine.types.TokenDefinitionJpaImpl;
import com.itgfirm.docengine.types.TokenDefinitions;

/**
 * @author Justin
 * 
 *         TODO: Description
 */
public interface TokenDictionaryService {
	
	void clear();
	
	TokenDefinitions findAll();

	TokenDefinitionJpaImpl findByTokenCd(String code);
	
	TokenDefinitions findByTokenCdLike(String like);

	TokenDefinitionJpaImpl findOne(Long id);

	Map<String, Map<String, Map<String, Deque<TokenDefinitionJpaImpl>>>> getDefinitionMap();

	Map<String, ?> getDroolsSafeTokens(Project project);

	Map<String, ?> getFreemarkerSafeTokens(Project project);

	Map<String, String> getSqlMap();

	Map<String, TokenData> getTokenDataMap(String projectId);

	Map<String, Iterable<TokenValue>> getTokenValueMap(Project project);

	TokenDefinitionJpaImpl save(TokenDefinitionJpaImpl definition);

	TokenDefinitions save(TokenDefinitions definitions);

	void refresh();

	boolean delete(Long id);

	boolean delete(String code);

	boolean delete(TokenDefinitionJpaImpl definition);	
}