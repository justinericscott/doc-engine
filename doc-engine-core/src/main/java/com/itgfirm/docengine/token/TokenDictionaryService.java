/**
 * TODO: License
 */
package com.itgfirm.docengine.token;

import java.util.Deque;
import java.util.List;
import java.util.Map;

import com.itgfirm.docengine.pipeline.DefaultProjectImpl;
import com.itgfirm.docengine.types.jpa.TokenDefinitionJpaImpl;

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
	List<TokenDefinitionJpaImpl> get();

	/**
	 * TODO: Description
	 * 
	 * @param id
	 * @return
	 */
	TokenDefinitionJpaImpl get(Long id);

	/**
	 * TODO: Description
	 * 
	 * @param code
	 * @return
	 */
	TokenDefinitionJpaImpl get(String code);

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
	Map<String, String> getSqlMap();

	/**
	 * TODO: Description
	 * 
	 * @param projectId
	 * @return
	 */
	Map<String, DefaultTokenDataImpl> getTokenDataMap(String projectId);

	/**
	 * TODO: Description
	 * 
	 * @param project
	 * @return
	 */
	Map<String, List<DefaultTokenValueImpl>> getTokenValueMap(DefaultProjectImpl project);

	/**
	 * TODO: Description
	 * 
	 * @param project
	 * @return
	 */
	Map<String, ?> getDroolsSafeTokens(DefaultProjectImpl project);

	/**
	 * TODO: Description
	 * 
	 * @param project
	 * @return
	 */
	Map<String, ?> getFreemarkerSafeTokens(DefaultProjectImpl project);

	/**
	 * TODO: Description
	 * 
	 * @param item
	 * @return
	 */
	TokenDefinitionJpaImpl merge(TokenDefinitionJpaImpl token);

	/**
	 * TODO: Description
	 * 
	 * @param tokens
	 * @return
	 */
	List<TokenDefinitionJpaImpl> merge(List<TokenDefinitionJpaImpl> tokens);

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
	boolean delete(TokenDefinitionJpaImpl token);
}