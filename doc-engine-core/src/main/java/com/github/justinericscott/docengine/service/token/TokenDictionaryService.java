/**
 * TODO: License
 */
package com.github.justinericscott.docengine.service.token;

import java.util.Deque;
import java.util.Map;

import com.github.justinericscott.docengine.models.TokenDefinition;
import com.github.justinericscott.docengine.models.TokenDefinitions;
import com.github.justinericscott.docengine.pipeline.Project;
import com.github.justinericscott.docengine.service.token.types.TokenData;
import com.github.justinericscott.docengine.service.token.types.TokenValue;

/**
 * @author Justin
 * 
 *         TODO: Description
 */
public interface TokenDictionaryService {
	
	void clear();
	
	TokenDefinitions findAll();

	TokenDefinition findByTokenCd(String code);
	
	TokenDefinitions findByTokenCdLike(String like);

	TokenDefinition findOne(Long id);

	Map<String, Map<String, Map<String, Deque<TokenDefinition>>>> getDefinitionMap();

	Map<String, ?> getDroolsSafeTokens(Project project);

	Map<String, ?> getFreemarkerSafeTokens(Project project);

	Map<String, String> getSqlMap();

	Map<String, TokenData> getTokenDataMap(String projectId);

	Map<String, Iterable<TokenValue>> getTokenValueMap(Project project);

	TokenDefinition save(TokenDefinition definition);

	TokenDefinitions save(TokenDefinitions definitions);

	void refresh();

	boolean delete(Long id);

	boolean delete(String code);

	boolean delete(TokenDefinition definition);	
}