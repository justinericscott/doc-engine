/**
 * TODO: License
 */
package com.itgfirm.docengine.token;

import java.util.Deque;
import java.util.List;
import java.util.Map;

import com.itgfirm.docengine.pipeline.Project;
import com.itgfirm.docengine.types.TokenDefinition;

/**
 * @author Justin
 * 
 *         TODO: Description
 */
interface TokenDictionaryService {

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	List<TokenDefinition> get();

	/**
	 * TODO: Description
	 * 
	 * @param id
	 * @return
	 */
	TokenDefinition get(Long id);

	/**
	 * TODO: Description
	 * 
	 * @param code
	 * @return
	 */
	TokenDefinition get(String code);

	/**
	 * TODO: Description
	 * 
	 * @param like
	 * @return
	 */
	List<TokenDefinition> getByCodeLike(String like);

	/**
	 * TODO: Description
	 * 
	 * @return
	 */
	Map<String, Map<String, Map<String, Deque<TokenDefinition>>>> getDefinitionMap();

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
	Map<String, List<TokenValue>> getTokenValueMap(Project project);

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
	TokenDefinition merge(TokenDefinition token);

	/**
	 * TODO: Description
	 * 
	 * @param tokens
	 * @return
	 */
	List<TokenDefinition> merge(List<TokenDefinition> tokens);

	/**
	 * TODO: Description
	 *
	 */
	void refresh();

	/**
	 * TODO: Description
	 * 
	 * @param id
	 * @return
	 */
	boolean delete(Long id);

	/**
	 * TODO: Description
	 * 
	 * @param code
	 * @return
	 */
	boolean delete(String code);

	/**
	 * TODO: Description
	 * 
	 * @param token
	 * @return
	 */
	boolean delete(TokenDefinition token);
}