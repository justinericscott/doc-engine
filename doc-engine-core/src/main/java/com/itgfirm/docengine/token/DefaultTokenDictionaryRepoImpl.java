package com.itgfirm.docengine.token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.QueryException;
import org.springframework.stereotype.Repository;

import com.itgfirm.docengine.data.AbstractHibernateOrmRepo;
import com.itgfirm.docengine.data.DataConstants;
import com.itgfirm.docengine.types.TokenDefinition;
import com.itgfirm.docengine.types.jpa.TokenDefinitionJpaImpl;
import com.itgfirm.docengine.util.Utils;

/**
 * @author Justin Scott TODO: Description
 */
@Repository
class DefaultTokenDictionaryRepoImpl extends AbstractHibernateOrmRepo implements
		TokenDictionaryRepo {
	private static final Logger LOG = LogManager
			.getLogger(DefaultTokenDictionaryRepoImpl.class);

	public DefaultTokenDictionaryRepoImpl() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.repo.TokenRepo#getAll()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TokenDefinition> get() {
		LOG.trace("Attempting To Get All Token Definitions.");
		List<TokenDefinition> items =
				(List<TokenDefinition>) super.get(TokenDefinitionJpaImpl.class);
		if (Utils.isNotNullOrEmpty(items)) {
			int size = items.size();
			LOG.trace("Found " + size + " Tokens Definitions Token Dictionary.");
			if (size > 0) {
				Collections.sort(items);
				return items;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.repo.TokenRepo#getById(java.lang.Long)
	 */
	@Override
	public TokenDefinition get(Long id) {
		LOG.trace("Attempting To Get Token Definition For ID: " + id);
		return (TokenDefinition) super.get(TokenDefinitionJpaImpl.class, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.token.TokenDictionaryRepo#get( java.lang.String)
	 */
	@Override
	public TokenDefinition get(String code) {
		LOG.trace("Attempting To Get Token Definition For Code: " + code);
		List<TokenDefinition> defs =
				getWithQuery(DataConstants.GET_TOKEN_DEF_BY_TOKEN_CD,
						DataConstants.PARAM_TOKEN_CD, code);
		if (Utils.isNotNullOrEmpty(defs)) {
			return defs.get(0);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.token.TokenDictionaryRepo#getByCodeLike( java.lang.String)
	 */
	@Override
	public List<TokenDefinition> getByCodeLike(String like) {
		LOG.trace("Attempting To Get Token Definition For Code-Like: " + like);
		return getWithQuery(DataConstants.GET_TOKEN_DEF_BY_CODE_LIKE,
				DataConstants.PARAM_TOKEN_CD, like);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.token.TokenDictionaryRepo#getWithQuery( java.lang.String,
	 * java.lang.String, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TokenDefinition> getWithQuery(String query, String paramName, Object value) {
		LOG.trace("Attempting To Get Token Definition With Query: " + query);
		try {
			return (List<TokenDefinition>) super.getWithQuery(query, paramName, value);
		} catch (NoSuchElementException | IllegalArgumentException e) {
			LOG.error("Problem With Query.", e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.token.TokenDictionaryRepo#getWithQuery( java.lang.String,
	 * java.lang.String[], java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TokenDefinition> getWithQuery(String query, String[] paramNames,
			Object[] values) {
		LOG.trace("Attempting To Get Token Definition With Multiple Param " + "Query: "
				+ query);
		try {
			return (List<TokenDefinition>) super.getWithQuery(query, paramNames, values);
		} catch (NoSuchElementException | IllegalArgumentException | QueryException e) {
			LOG.error("Problem With Query.", e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.repo.TokenRepo#merge(
	 * com.itgfirm.docengine.types.jpa.jpa.TokenDictionaryItem)
	 */
	@Override
	public TokenDefinition merge(TokenDefinition token) {
		if (Utils.isNotNullOrEmpty(token)) {
			LOG.trace("Attempting To Merge Token Definition: " + token.getTokenCd());
			if (Utils.isNotNullOrZero(token.getId())) {
				LOG.trace("Attempting To Update Token Definition: " + token.getTokenCd());
				return (TokenDefinition) super.merge((TokenDefinitionJpaImpl) token);
			} else if (!Utils.isNotNullOrEmpty(get(token.getTokenCd()))) {
				LOG.trace("Attempting To Insert Token Definition: " + token.getTokenCd());
				return (TokenDefinition) super.merge((TokenDefinitionJpaImpl) token);
			} else {
				LOG.trace("Token Definition Already Exists: " + token.getTokenCd());
			}
		} else {
			LOG.error("Token Definition Must Not Be Null!");
		}
		return token;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.token.TokenDictionaryRepo#delete(
	 * com.itgfirm.docengine.types.jpa.jpa.TokenDefinition)
	 */
	@Override
	public boolean delete(TokenDefinition token) {
		if (Utils.isNotNullOrEmpty(token)) {
			LOG.trace("Attempting To Delete Token Definition: " + token.getTokenCd());
			Long id = token.getId();
			super.delete((TokenDefinitionJpaImpl) token);
			token = get(id);
			if (Utils.isNotNullOrEmpty(token)) {
				LOG.trace("Item Was NOT Deleted: " + token.getTokenCd());
			} else {
				LOG.trace("Item Deleted As Expected.");
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.token.TokenDictionaryRepo#deleteByCodeLike( java.lang.String)
	 */
	@Override
	public boolean deleteByCodeLike(String like) {
		List<Boolean> results = new ArrayList<Boolean>();
		List<TokenDefinition> tokens = getByCodeLike(like);
		if (Utils.isNotNullOrEmpty(tokens)) {
			LOG.trace("Attempting To Delete Token Definition For Code-Like: " + like);
			for (TokenDefinition t : tokens) {
				results.add(delete(t));
			}
			if (results.contains(false)) {
				LOG.trace("Some Items Were NOT Deleted!!!");
				return false;
			} else {
				LOG.trace("All Items Deleted As Expected.");
				return true;
			}
		} else {
			LOG.trace("Nothing To Delete For Provided String: " + like);
			return true;
		}
	}
}