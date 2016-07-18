/**
 * TODO: License
 */
package com.itgfirm.docengine.token;

import java.util.List;

import com.itgfirm.docengine.types.TokenDefinition;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
interface TokenDictionaryRepo {

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
	 * @param query
	 * @param paramName
	 * @param value
	 * @return
	 */
	List<TokenDefinition> getWithQuery(String query, String paramName, Object value);

	/**
	 * TODO: Description
	 * 
	 * @param query
	 * @param paramNames
	 * @param values
	 * @return
	 */
	List<TokenDefinition> getWithQuery(String query, String[] paramNames, Object[] values);

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
	 * @param token
	 * @return
	 */
	boolean delete(TokenDefinition token);

	/**
	 * TODO: Description
	 * 
	 * @param like
	 * @return
	 */
	boolean deleteByCodeLike(String like);
}