package com.itgfirm.docengine.token;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itgfirm.docengine.pipeline.Exhibit;
import com.itgfirm.docengine.pipeline.Project;
import com.itgfirm.docengine.types.TokenDefinition;
import com.itgfirm.docengine.util.Utils;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Service
class DefaultTokenDictionaryServiceImpl implements TokenDictionaryService {
	private static final Logger LOG = LogManager
			.getLogger(DefaultTokenDictionaryServiceImpl.class);
	private static final String SELECT_ = "SELECT ";
	private static final String _FROM_ = "_FROM_";
	private static final String _WHERE_ = "_WHERE_";
	private static volatile AtomicBoolean _debugMode = new AtomicBoolean();
	private static volatile List<TokenDefinition> _dictionary;
	private static volatile Map<String, Map<String, Map<String, Deque<TokenDefinition>>>> _definitionMap;
	private static volatile Map<String, String> _sqlMap;
	@Autowired
	private TokenDictionaryRepo repo;
	@Autowired
	private ExternalDataRepo ext;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.token.TokenDictionaryService#get()
	 */
	@Override
	public synchronized List<TokenDefinition> get() {
		if (Utils.isNotNullOrEmpty(_dictionary)) {
			LOG.trace("Found Existing Tokens.");
			return _dictionary;
		} else {
			refreshTokenDictionary();
			if (Utils.isNotNullOrEmpty(_dictionary)) {
				LOG.trace("Found Refreshed Tokens.");
				return _dictionary;
			}
		}
		LOG.info("Found No Tokens.");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.token.TokenDictionaryService#get(java.lang.Long)
	 */
	@Override
	public TokenDefinition get(Long id) {
		return repo.get(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.token.TokenDictionaryService#get( java.lang.String)
	 */
	@Override
	public TokenDefinition get(String code) {
		return repo.get(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.token.TokenDictionaryService#getByCodeLike( java.lang.String)
	 */
	@Override
	public List<TokenDefinition> getByCodeLike(String like) {
		return repo.getByCodeLike(like);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.token.TokenDictionaryService#getDefinitionMap()
	 */
	@Override
	public synchronized Map<String, Map<String, Map<String, Deque<TokenDefinition>>>> getDefinitionMap() {
		if (Utils.isNotNullOrEmpty(_definitionMap)) {
			LOG.trace("Found Existing 'Where.Entity' To Entity/Attribute/Definitions Map.");
			return _definitionMap;
		} else {
			refreshDefinitionMap();
			if (Utils.isNotNullOrEmpty(_definitionMap)) {
				LOG.trace("Found Refreshed 'Where.Entity' To Entity/Attribute/Definitions Map.");
				return _definitionMap;
			}
		}
		LOG.info("Found No 'Where.Entity' To Entity/Attribute/Definitions Map.");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.token.TokenDictionaryService#getSqlMap()
	 */
	@Override
	public synchronized Map<String, String> getSqlMap() {
		if (Utils.isNotNullOrEmpty(_sqlMap)) {
			LOG.trace("Found Existing 'Where.Entity' To SQL Map.");
			return _sqlMap;
		} else {
			refreshSqlMap();
			if (Utils.isNotNullOrEmpty(_sqlMap)) {
				LOG.trace("Found Refreshed 'Where.Entity' To SQL Map.");
				return _sqlMap;
			}
		}
		LOG.info("Found No 'Where.Entity' To SQL Map.");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.token.TokenDictionaryService#
	 * getTokenDataMap(java.lang.String)
	 */
	@Override
	public synchronized Map<String, TokenData> getTokenDataMap(String projectId) {
		if (Utils.isNotNullOrEmpty(_dictionary)) {
			Map<String, TokenData> projectTokens =
					new HashMap<String, TokenData>(get().size());
			Iterator<Entry<String, String>> sqlMap = _sqlMap.entrySet().iterator();
			while (sqlMap.hasNext()) {
				Entry<String, String> entrySqlMap = sqlMap.next();
				String sqlKey = entrySqlMap.getKey();
				String sql = entrySqlMap.getValue();
				String param = "'" + projectId + "'";
				sql = sql.replace("?", param);
				Map<String, Object> response = ext.queryForMap(sql);
				if (Utils.isNotNullOrEmpty(response)) {
					Iterator<Entry<String, Object>> results = response.entrySet().iterator();
					while (results.hasNext()) {
						Entry<String, Object> result = results.next();
						Iterator<Entry<String, Map<String, Deque<TokenDefinition>>>> entities =
								_definitionMap.get(sqlKey).entrySet().iterator();
						while (entities.hasNext()) {
							Iterator<Entry<String, Deque<TokenDefinition>>> attributes =
									entities.next().getValue().entrySet().iterator();
							while (attributes.hasNext()) {
								Entry<String, Deque<TokenDefinition>> attribute =
										attributes.next();
								String attributeKey = attribute.getKey();
								if (attributeKey.contains(" AS ")) {
									attributeKey =
											attributeKey.substring(
													attributeKey.indexOf(" AS ") + 4,
													attributeKey.length());
								}
								Iterator<TokenDefinition> definitions =
										attribute.getValue().iterator();
								while (definitions.hasNext()) {
									TokenDefinition definition = definitions.next();
									if (definition.getAttribute() != null
											&& definition.getAttribute().equals(attributeKey)) {
										TokenData data =
												projectTokens.get(definition.getName());
										if (!Utils.isNotNullOrEmpty(data)) {
											data = new DefaultTokenDataImpl(definition);
											projectTokens.put(definition.getName(), data);
										}
										if (result.getKey().equals(attributeKey)) {
											data.addValue(new DefaultTokenValueImpl(
													definition, result.getValue()));
										}
									}
								}
							}
						}
					}
				}
			}
			mergeProjectTokenDataMapWithEmptyTokenDataObjects(projectTokens);
			return projectTokens;
		}
		LOG.info("Cannot Create Project TokenData Map! Token Dictionary Is Null/Empty!");
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.token.TokenDictionaryService#getTokenValueMap(
	 * com.itgfirm.docengine.pipeline.Project)
	 */
	@Override
	public Map<String, List<TokenValue>> getTokenValueMap(Project project) {
		Map<String, List<TokenValue>> tokensValuesMap =
				new HashMap<String, List<TokenValue>>(get().size());
		for (TokenData data : getTokenDataMap(project.getProjectNumber()).values()) {
			List<TokenValue> phaseValues =
					new ArrayList<TokenValue>(data.getValues(project.getPhase()));
			if (!phaseValues.isEmpty()) {
				tokensValuesMap.put(data.getName(), phaseValues);
			}
		}
		mergeProjectTokenValueMapWithEmptyTokenValueObjects(tokensValuesMap,
				project.getPhase());
		return tokensValuesMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.token.TokenDictionaryService#getDroolsSafeTokens(
	 * com.itgfirm.docengine.pipeline.Project)
	 */
	@Override
	public Map<String, ?> getDroolsSafeTokens(Project project) {
		Map<String, List<TokenValue>> projectTokens = getTokenValueMap(project);
		Map<String, Object> droolsTokens = new HashMap<String, Object>(projectTokens.size());
		for (String key : projectTokens.keySet()) {
			List<TokenValue> list = projectTokens.get(key);
			Object data = null;
			if (list != null && !list.isEmpty()) {
				data = list.get(0).getValue();
				if (data != null) {
					if (StringUtils.left(key, 2).equals("is")) {
						Number val;
						boolean bool = false;
						if (data.getClass().getName().equals(String.class.getName())) {
							try {
								val = (Number) Integer.valueOf(data.toString());
							} catch (NumberFormatException e) {
								LOG.error("Found A Non_Integer In An IS Field!!! " + key
										+ ": " + data.toString());
								val = 0;
							}
						} else {
							val = (Number) data;
						}
						if (val.intValue() == 1) {
							bool = true;
						}
						droolsTokens.put(key, bool);
					} else {
						droolsTokens.put(key, data);
					}
				} else {
					droolsTokens.put(key, DefaultTokenValueImpl.NOT_DEFINED_TXT);
				}
			}
		}
		project.setTokens(droolsTokens);
		return droolsTokens;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.token.TokenDictionaryService
	 * #getFreemarkerSafeTokens(java.lang.String)
	 */
	@Override
	public Map<String, ?> getFreemarkerSafeTokens(Project project) {
		Map<String, List<TokenValue>> valueMap = getTokenValueMap(project);
		if (Utils.isNotNullOrEmpty(project) && Utils.isNotNullOrEmpty(valueMap)) {
			Map<String, Object> freemarkerTokens = new HashMap<String, Object>();
			Iterator<Entry<String, List<TokenValue>>> projectTokens =
					valueMap.entrySet().iterator();
			while (projectTokens.hasNext()) {
				Entry<String, List<TokenValue>> entry = projectTokens.next();
				String name = entry.getKey();
				List<TokenValue> values = entry.getValue();
				TokenValue value = null;
				String error = null;
				if (values.isEmpty()) {
					error =
							"[" + DefaultTokenValueImpl.NOT_DEFINED_TXT
									+ " - XXX --- ODD BALL1 EMPTY: " + name + "]";
				} else if (values.size() > 1) {
					error =
							"[" + DefaultTokenValueImpl.NOT_DEFINED_TXT
									+ " - XXX --- ODD BALL2 MULTIPLES: " + name + "]";
				} else {
					value = values.get(0);
				}
				if (value != null) {
					if (value.getDisplayValue() != null && !value.getDisplayValue().isEmpty()) {
						synchronized (_debugMode) {
							if (_debugMode.get()) {
								freemarkerTokens.put(name,
										"[" + name + "=" + value.getDisplayValue() + "]");
							} else {
								freemarkerTokens.put(name, value.getDisplayValue());
							}
						}
					} else {
						if (!freemarkerTokens.containsKey(name)) {
							if (error != null) {
								freemarkerTokens.put(name, error);
							} else {
								error =
										"[" + DefaultTokenValueImpl.NOT_DEFINED_TXT
												+ " - XXX --- ODD BALL3 EMPTY STILL: " + name
												+ "]";
								freemarkerTokens.put(name, error);
							}
						}
					}
				}
			}
			if (!project.getExhibits().isEmpty()) {
				freemarkerTokens.put("exhibits",
						Collections.unmodifiableList(project.getExhibits()));
				for (Exhibit exhibit : project.getExhibits()) {
					freemarkerTokens.put("exhibit" + exhibit.getExhibit(),
							exhibit.getDisplayName());
				}
			}
			String leaseCode = project.getLeaseNumber();
			freemarkerTokens.put("leaseCode", leaseCode);
			freemarkerTokens.put("extendedLeaseCode",
					String.format("%s %s", project.getDocumentCd(), leaseCode));
			String leaseNumber = (String) freemarkerTokens.get("newLeaseNumber");
			if (leaseNumber != null) {
				project.setLeaseNumber(freemarkerTokens.get("newLeaseNumber").toString());
			}
			project.setTokens(freemarkerTokens);
			return freemarkerTokens;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.token.TokenDictionaryService#merge(
	 * com.itgfirm.docengine.types.jpa.TokenDictionaryItem)
	 */
	@Override
	public TokenDefinition merge(TokenDefinition item) {
		TokenDefinition result = repo.merge(item);
		refresh();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.token.TokenDictionaryService#merge(java.util.List)
	 */
	@Override
	public List<TokenDefinition> merge(List<TokenDefinition> tokens) {
		if (Utils.isNotNullOrEmpty(tokens)) {
			LOG.trace("Attempting To Merge List Of Token Definitions: " + tokens.size()
					+ " Items.");
			ListIterator<TokenDefinition> iter = tokens.listIterator();
			while (iter.hasNext()) {
				iter.set(merge(iter.next()));
			}
			return tokens;
		}
		LOG.trace("List Of Token Definitions Must Not Be Null!");
		return tokens;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.token.TokenDictionaryService#delete( java.lang.Long)
	 */
	@Override
	public boolean delete(Long id) {
		return delete(get(id));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.token.TokenDictionaryService#delete( java.lang.String)
	 */
	@Override
	public boolean delete(String code) {
		return delete(get(code));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.token.TokenDictionaryService#delete(
	 * com.itgfirm.docengine.types.jpa.TokenDefinition)
	 */
	@Override
	public boolean delete(TokenDefinition token) {
		boolean result = repo.delete(token);
		refresh();
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.token.TokenDictionaryService#refresh()
	 */
	@PostConstruct
	@Override
	public void refresh() {
		LOG.trace("Refreshing Token Service.");
		refreshTokenDictionary();
		refreshDefinitionMap();
		refreshSqlMap();
	}

	/**
	 * TODO: Description
	 *
	 */
	private synchronized void refreshTokenDictionary() {
		LOG.trace("Attempting To Refresh Token Dictionary.");
		List<TokenDefinition> newTokens = repo.get();
		if (Utils.isNotNullOrEmpty(newTokens)) {
			LOG.trace("Found " + newTokens.size() + " Tokens In The Database.");
			_dictionary =
					Collections.synchronizedList(Collections.unmodifiableList(newTokens));
		} else {
			LOG.warn("Cannot Create/Update Token Dictionary! Database Has No Tokens!");
			if (Utils.isNotNullOrEmpty(_dictionary)) {
				_dictionary = null;
			}
		}
	}

	/**
	 * TODO: Description
	 *
	 */
	private synchronized void refreshDefinitionMap() {
		if (Utils.isNotNullOrEmpty(_dictionary)) {
			LOG.trace("Attempting To Refresh The 'Where.Entity' "
					+ "To Entity/Attribute/Definitions Map.");
			Map<String, Map<String, Map<String, Deque<TokenDefinition>>>> newDefinitionMap =
					new HashMap<String, Map<String, Map<String, Deque<TokenDefinition>>>>();
			Iterator<TokenDefinition> it = get().iterator();
			while (it.hasNext()) {
				TokenDefinition tokenDefinition = it.next();
				if (tokenDefinition.getEntity() != null
						&& tokenDefinition.getAttribute() != null
						&& tokenDefinition.getWhere() != null) {
					String definitionKey =
							String.format("%s.%s", tokenDefinition.getWhere(),
									tokenDefinition.getEntity());
					if (newDefinitionMap.get(definitionKey) == null) {
						newDefinitionMap.put(definitionKey,
								new HashMap<String, Map<String, Deque<TokenDefinition>>>());
					}
					if (newDefinitionMap.get(definitionKey).get(tokenDefinition.getEntity()) == null) {
						newDefinitionMap.get(definitionKey).put(tokenDefinition.getEntity(),
								new HashMap<String, Deque<TokenDefinition>>());
					}
					if (newDefinitionMap.get(definitionKey).get(tokenDefinition.getEntity())
							.get(tokenDefinition.getAttribute()) == null) {
						newDefinitionMap
								.get(definitionKey)
								.get(tokenDefinition.getEntity())
								.put(tokenDefinition.getAttribute(),
										new ArrayDeque<TokenDefinition>());
					}
					newDefinitionMap.get(definitionKey).get(tokenDefinition.getEntity())
							.get(tokenDefinition.getAttribute()).push(tokenDefinition);
				}
			}
			if (Utils.isNotNullOrEmpty(newDefinitionMap)) {
				_definitionMap =
						Collections.synchronizedMap(Collections
								.unmodifiableMap(newDefinitionMap));
			} else {
				_definitionMap = null;
			}
		} else {
			_definitionMap = null;
			LOG.warn("Cannot Create/Update 'Where.Entity' "
					+ "To Entity/Attribute/Definitions Map! "
					+ "Token Dictionary Is Null/Empty!");
		}
	}

	/**
	 * TODO: Description
	 *
	 */
	private synchronized void refreshSqlMap() {
		if (Utils.isNotNullOrEmpty(_definitionMap)) {
			LOG.trace("Attempting To Refresh The 'Where.Entity' To SQL Map.");
			Map<String, String> newSqlMap = new HashMap<String, String>();
			Iterator<Entry<String, Map<String, Map<String, Deque<TokenDefinition>>>>> definitions =
					_definitionMap.entrySet().iterator();
			while (definitions.hasNext()) {
				StringBuilder sb = new StringBuilder();
				Entry<String, Map<String, Map<String, Deque<TokenDefinition>>>> definition =
						definitions.next();
				String definitionKey = definition.getKey();
				String entityKey = null;
				String where = null;
				Iterator<Entry<String, Map<String, Deque<TokenDefinition>>>> entities =
						definition.getValue().entrySet().iterator();
				while (entities.hasNext()) {
					Entry<String, Map<String, Deque<TokenDefinition>>> entity =
							entities.next();
					entityKey = entity.getKey();
					String attributeKey = null;
					Iterator<Entry<String, Deque<TokenDefinition>>> attributes =
							entity.getValue().entrySet().iterator();
					while (attributes.hasNext()) {
						Entry<String, Deque<TokenDefinition>> attribute = attributes.next();
						attributeKey = attribute.getKey();
						where = attribute.getValue().getFirst().getWhere();
						sb.append(attributeKey).append(",");
					} // For Attributes
				} // For Entities
				sb.setLength(sb.length() - 1); // Delete the last comma
				sb.insert(0, SELECT_) // Insert SELECT in front of the collected attributes
						.append(_FROM_).append(entityKey).append(_WHERE_).append(where);
				newSqlMap.put(definitionKey, sb.toString());
			} // For Definitions
			if (Utils.isNotNullOrEmpty(newSqlMap)) {
				_sqlMap = Collections.synchronizedMap(Collections.unmodifiableMap(newSqlMap));
			} else {
				_sqlMap = null;
			}
		} else {
			_sqlMap = null;
			LOG.warn("Cannot Create/Update 'Where.Entity' To SQL Map! "
					+ "'Where.Entity' To Entity/Attribute/Definitions Map " + "Is Null/Empty!");
		}
	}

	/**
	 * TODO: Description
	 * 
	 * @param projectTokens
	 */
	private void mergeProjectTokenDataMapWithEmptyTokenDataObjects(
			Map<String, TokenData> projectTokens) {
		if (Utils.isNotNullOrEmpty(projectTokens)) {
			for (TokenDefinition definition : get()) {
				if (projectTokens.containsKey(definition.getName())) {
					TokenData data = projectTokens.get(definition.getName());
					if (data.getValues().isEmpty()) {
						data.addValue(new DefaultTokenValueImpl(definition, null));
					}
				} else {
					TokenData data = new DefaultTokenDataImpl(definition);
					if (data.getValues().isEmpty()) {
						data.addValue(new DefaultTokenValueImpl(definition, null));
					}
					projectTokens.put(definition.getName(), data);
				}
			}
		}
	}

	/**
	 * TODO: Description
	 * 
	 * @param projectTokens
	 * @param phase
	 */
	private void mergeProjectTokenValueMapWithEmptyTokenValueObjects(
			Map<String, List<TokenValue>> projectTokens, String phase) {
		if (Utils.isNotNullOrEmpty(projectTokens)) {
			for (TokenDefinition definition : get()) {
				if (phase.equals(definition.getPhase())) {
					List<TokenValue> tokenValues = projectTokens.get(definition.getName());
					if (tokenValues == null) {
						tokenValues = new ArrayList<TokenValue>(1);
						projectTokens.put(definition.getName(), tokenValues);
					}
					if (tokenValues.isEmpty()) {
						tokenValues.add(new DefaultTokenValueImpl(definition, null));
					}
				}
			}
		}
	}
}