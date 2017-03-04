package com.github.justinericscott.docengine.service.token;

import static com.github.justinericscott.docengine.util.Utils.Constants.*;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrZero;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;
import static java.util.Collections.synchronizedList;
import static java.util.Collections.synchronizedMap;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.justinericscott.docengine.models.TokenDefinition;
import com.github.justinericscott.docengine.models.TokenDefinitions;
import com.github.justinericscott.docengine.pipeline.ExhibitImpl;
import com.github.justinericscott.docengine.pipeline.Project;
import com.github.justinericscott.docengine.repository.token.TokenDictionaryRepository;
import com.github.justinericscott.docengine.service.token.types.TokenData;
import com.github.justinericscott.docengine.service.token.types.TokenValue;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Service
@Transactional(AUTOWIRE_QUALIFIER_ORM_TX)
final class TokenDictionaryServiceImpl implements TokenDictionaryService {
	private static final Logger LOG = LoggerFactory.getLogger(TokenDictionaryServiceImpl.class);
	private static final String SELECT_ = "SELECT ";
	private static final String _FROM_ = " FROM ";
	private static final String _WHERE_ = " WHERE ";
	private static volatile AtomicBoolean _debugMode = new AtomicBoolean();
	private static volatile List<TokenDefinition> _dictionary;
	private static volatile Map<String, Map<String, Map<String, Deque<TokenDefinition>>>> _definitionMap;
	private static volatile Map<String, String> _sqlMap;

	@Autowired
	private TokenDictionaryRepository _definitions;

	@Autowired
	private BusinessDataService _business;

	TokenDictionaryServiceImpl() {
		LOG.debug("Creating new Token Dictionary Service.");
	}
	
	public final void clear() {
		if (_sqlMap != null) {
			synchronized (_sqlMap) {
				_sqlMap = null;
			}
		}
		if (_definitionMap != null) {
			synchronized (_definitionMap) {
				_definitionMap = null;
			}
		}
		if (_dictionary != null) {
			synchronized (_dictionary) {
				_dictionary = null;
			}
		}
	}

	@Override
	public final TokenDefinitions findAll() {
		if (isNotNullOrEmpty(_dictionary)) {
			synchronized (_dictionary) {
				LOG.trace("Found Existing Tokens.");
				return new TokenDefinitions(_dictionary);
			}
		} else {
			refreshDictionary();
			if (isNotNullOrEmpty(_dictionary)) {
				synchronized (_dictionary) {
					LOG.trace("Found Refreshed Tokens.");
					return new TokenDefinitions(_dictionary);
				}
			}
		}
		LOG.warn("Found No Tokens.");
		return null;
	}

	@Override
	public final TokenDefinition findByTokenCd(final String code) {
		if (isNotNullOrEmpty(code)) {
			return _definitions.findByTokenCd(code);
		}
		LOG.warn("The token code must not be null or empty!");
		return null;
	}

	@Override
	public final TokenDefinitions findByTokenCdLike(final String like) {
		if (isNotNullOrEmpty(like)) {
			final Collection<TokenDefinition> definitions = (Collection<TokenDefinition>) _definitions.findByTokenCdLike(like);
			if (isNotNullOrEmpty(definitions)) {
				return new TokenDefinitions(definitions);
			}
		}
		LOG.warn("The search string must not be null or empty!");
		return null;
	}

	@Override
	public final TokenDefinition findOne(final Long id) {
		if (isNotNullOrZero(id)) {
			return _definitions.findOne(id);
		}
		LOG.warn("The token ID must not be null or zero!");
		return null;
	}

	@Override
	public final Map<String, Map<String, Map<String, Deque<TokenDefinition>>>> getDefinitionMap() {
		if (isNotNullOrEmpty(_definitionMap)) {
			synchronized (_definitionMap) {
				LOG.trace("Found Existing 'Where.Entity' To Entity/Attribute/Definitions Map.");
				return _definitionMap;
			}
		} else {
			refreshDefinitions();
			if (isNotNullOrEmpty(_definitionMap)) {
				synchronized (_definitionMap) {
					LOG.trace("Found Refreshed 'Where.Entity' To Entity/Attribute/Definitions Map.");
					return _definitionMap;
				}
			}
		}
		LOG.trace("Found No 'Where.Entity' To Entity/Attribute/Definitions Map.");
		return null;
	}

	@Override
	public final Map<String, String> getSqlMap() {
		if (isNotNullOrEmpty(_sqlMap)) {
			synchronized (_sqlMap) {
				LOG.trace("Found Existing 'Where.Entity' To SQL Map.");
				return _sqlMap;
			}
		} else {
			refreshSqlMap();
			if (isNotNullOrEmpty(_sqlMap)) {
				synchronized (_sqlMap) {
					LOG.trace("Found Refreshed 'Where.Entity' To SQL Map.");
					return _sqlMap;
				}
			}
		}
		LOG.trace("Found No 'Where.Entity' To SQL Map.");
		return null;
	}

	@Override
	public final Map<String, ?> getDroolsSafeTokens(final Project project) {
		final Map<String, Iterable<TokenValue>> projectTokens = getTokenValueMap(project);
		final Map<String, Object> droolsTokens = new HashMap<String, Object>(projectTokens.size());
		final Collection<String> keys = projectTokens.keySet();
		keys.forEach(key -> {
			if (isNotNullOrEmpty(key)) {
				Object data = projectTokens.get(key);
				if (isNotNullOrEmpty(data)) {
					if (StringUtils.left(key, 2).equals("is")) {
						Number val;
						boolean bool = false;
						if (data.getClass().equals(String.class)) {
							try {
								val = (Number) Integer.valueOf(data.toString());
							} catch (final NumberFormatException e) {
								LOG.error("Found A Non_Integer In An IS Field!!! " + key + ": " + data.toString());
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
					droolsTokens.put(key, TokenValue.NOT_DEFINED_TXT);
				}
			}
		});
		project.setTokens(droolsTokens);
		return droolsTokens;
	}

	@Override
	public final Map<String, ?> getFreemarkerSafeTokens(final Project project) {
		final Map<String, Iterable<TokenValue>> map = getTokenValueMap(project);
		if (isNotNullOrEmpty(project) && isNotNullOrEmpty(map)) {
			final Map<String, Object> freemarker = new HashMap<String, Object>();
			final Iterator<Entry<String, Iterable<TokenValue>>> tokens = map.entrySet().iterator();
			while (tokens.hasNext()) {
				final Entry<String, Iterable<TokenValue>> entry = tokens.next();
				final String key = entry.getKey();
				final Iterable<TokenValue> values = entry.getValue();
				if (isNotNullOrEmpty(values) && ((Collection<TokenValue>) values).size() == 1) {
					final TokenValue value = values.iterator().next();
					if (isNotNullOrEmpty(value) && isNotNullOrEmpty(value.getDisplayValue())) {
						synchronized (_debugMode) {
							if (_debugMode.get()) {
								freemarker.put(key, "[" + key + "=" + value.getDisplayValue() + "]");
							} else {
								freemarker.put(key, value.getDisplayValue());
							}
						}
					}
				}
				if (!freemarker.containsKey(key)) {
					if (!values.iterator().hasNext()) {
						freemarker.put(key,
								"[" + TokenValue.NOT_DEFINED_TXT + " - XXX --- ODD BALL1 EMPTY: " + key + "]");
					} else if (((Collection<TokenValue>) values).size() > 1) {
						freemarker.put(key,
								"[" + TokenValue.NOT_DEFINED_TXT + " - XXX --- ODD BALL2 MULTIPLES: " + key + "]");
					} else {
						freemarker.put(key,
								"[" + TokenValue.NOT_DEFINED_TXT + " - XXX --- ODD BALL3 EMPTY STILL: " + key + "]");
					}
				}
			}
			if (!project.getExhibits().isEmpty()) {
				freemarker.put("exhibits", unmodifiableList(project.getExhibits()));
				for (final ExhibitImpl exhibit : project.getExhibits()) {
					freemarker.put("exhibit" + exhibit.getExhibit(), exhibit.getDisplayName());
				}
			}
			final String leaseCode = project.getLeaseNumber();
			freemarker.put("leaseCode", leaseCode);
			freemarker.put("extendedLeaseCode", String.format("%s %s", project.getDocumentCd(), leaseCode));
			final String leaseNumber = (String) freemarker.get("newLeaseNumber");
			if (isNotNullOrEmpty(leaseNumber)) {
				project.setLeaseNumber(leaseNumber);
			}
			project.setTokens(freemarker);
			return freemarker;
		}
		return null;
	}

	@Override
	public final Map<String, TokenData> getTokenDataMap(final String projectId) {
		final Map<String, TokenData> tokens = new HashMap<String, TokenData>();
		getSqlMap().forEach((definitionKey, sql) -> {
			sql = sql.replace("?", "'" + projectId + "'");
			LOG.trace("Attempting to query for map using:\n{}", sql);
			final Map<String, Object> response = _business.queryForMap(sql);
			if (isNotNullOrEmpty(response)) {
				response.forEach((resultKey, result) -> {
					getDefinitionMap().get(definitionKey).forEach((entityKey, entity) -> {
						final Iterator<String> attributeKeys = entity.keySet().iterator();
						while (attributeKeys.hasNext()) {
							String attributeKey = attributeKeys.next();
							if (attributeKey.contains(" AS ")) {
								attributeKey = attributeKey.substring(attributeKey.indexOf(" AS ") + 4,
										attributeKey.length());
							}
							final Iterator<TokenDefinition> definitions = entity.get(attributeKey).iterator();
							while (definitions.hasNext()) {
								final TokenDefinition definition = definitions.next();
								final String attr = definition.getAttribute();
								final String name = definition.getName();
								if (isNotNullOrEmpty(attr) && attr.equals(attributeKey)) {
									TokenData data = tokens.get(name);
									if (!isNotNullOrEmpty(data)) {
										data = new TokenData(definition);
										tokens.put(name, data);
									}
									if (resultKey.equals(attributeKey)) {
										data.addValue(new TokenValue(definition, result));
									}
								}
							}
						}
					});
				});
			}
		});
		mergeDataMapWithEmptyData(tokens);
		return tokens;
	}

	@Override
	public final Map<String, Iterable<TokenValue>> getTokenValueMap(final Project project) {
		final Map<String, Iterable<TokenValue>> map = new HashMap<String, Iterable<TokenValue>>();
		getTokenDataMap(project.getProjectNumber()).forEach((name, data) -> {
			final Collection<TokenValue> values = (Collection<TokenValue>) data.getValues(project.getPhase());
			if (isNotNullOrEmpty(values)) {
				map.put(data.getName(), values);
			}
		});
		mergeValueMapWithEmptyValues(map, project.getPhase());
		return map;
	}

	@Override
	public final TokenDefinition save(final TokenDefinition definition) {
		if (isNotNullOrEmpty(definition)) {
			final TokenDefinition result = _definitions.save(definition);
			refresh();
			return result;
		}
		LOG.warn("Token Definition Must Not Be Null!");
		return null;
	}

	@Override
	public final TokenDefinitions save(final TokenDefinitions definitions) {
		if (isNotNullOrEmpty(definitions)) {
			LOG.trace("Attempting To Save Collection Of Token Definitions.");
			final Collection<TokenDefinition> result = (Collection<TokenDefinition>) _definitions.save(Arrays.asList(definitions.getTokens()));
			if (isNotNullOrEmpty(result)) {
				refresh();
				return new TokenDefinitions(result);
			}
		}
		LOG.warn("Collection Of Token Definitions Must Not Be Null!");
		return null;
	}

	@Override
	public final boolean delete(final Long id) {
		if (isNotNullOrZero(id)) {
			delete(findOne(id));
		}
		return false;
	}

	@Override
	public final boolean delete(final String code) {
		if (isNotNullOrEmpty(code)) {
			return delete(findByTokenCd(code));
		}
		return false;
	}

	@Override
	public final boolean delete(final TokenDefinition definition) {
		if (isNotNullOrEmpty(definition)) {
			final Long id = definition.getId();
			_definitions.delete(definition);
			final TokenDefinition def = findOne(id);
			if (def == null) {
				refresh();
				return true;
			}
		}
		return false;
	}

	@PostConstruct
	@Override
	public final void refresh() {
		LOG.info("Refreshing Token Dictionary Service...");
		refreshDictionary();
		refreshDefinitions();
		refreshSqlMap();
	}

	private void mergeDataMapWithEmptyData(final Map<String, TokenData> tokens) {
		if (isNotNullOrEmpty(tokens)) {
			for (final TokenDefinition definition : findAll().getTokens()) {
				if (tokens.containsKey(definition.getName())) {
					final TokenData data = tokens.get(definition.getName());
					if (!isNotNullOrEmpty(data.getValues())) {
						data.addValue(new TokenValue(definition, null));
					}
				} else {
					final TokenData data = new TokenData(definition);
					if (!isNotNullOrEmpty(data.getValues())) {
						data.addValue(new TokenValue(definition, null));
					}
					tokens.put(definition.getName(), data);
				}
			}
		}
	}

	private void mergeValueMapWithEmptyValues(final Map<String, Iterable<TokenValue>> tokens, final String phase) {
		if (isNotNullOrEmpty(tokens)) {
			for (final TokenDefinition definition : findAll().getTokens()) {
				if (phase.equals(definition.getPhase())) {
					final String tokenCd = definition.getTokenCd();
					final Iterable<TokenValue> values = tokens.get(tokenCd);
					if (!isNotNullOrEmpty(values)) {
						tokens.put(tokenCd, Arrays.asList(new TokenValue(definition, null)));
					}
				}
			}
		}
	}
	
	private void refreshDefinitions() {
		if (_dictionary != null) {
			final Map<String, Map<String, Map<String, Deque<TokenDefinition>>>> definitions = new HashMap<String, Map<String, Map<String, Deque<TokenDefinition>>>>();
			for (final TokenDefinition def : findAll().getTokens()) {
				final String entityKey = def.getEntity();
				final String attributeKey = def.getAttribute();
				final String where = def.getWhere();
				if (isNotNullOrEmpty(entityKey) && isNotNullOrEmpty(attributeKey) && isNotNullOrEmpty(where)) {
					final String definitionKey = String.format("%s.%s", where, entityKey);
					if (definitions.containsKey(definitionKey)) {
						final Map<String, Map<String, Deque<TokenDefinition>>> definition = definitions
								.get(definitionKey);
						if (definition.containsKey(entityKey)) {
							final Map<String, Deque<TokenDefinition>> entity = definition.get(entityKey);
							if (entity.containsKey(attributeKey)) {
								final Deque<TokenDefinition> attribute = entity.get(attributeKey);
								if (attribute.contains(def)) {
									attribute.remove(def);
									attribute.push(def);
								} else {
									attribute.push(def);
								}
							} else {
								final Deque<TokenDefinition> attribute = new ArrayDeque<TokenDefinition>();
								attribute.push(def);
								entity.put(attributeKey, attribute);
							}
						} else {
							final Deque<TokenDefinition> attribute = new ArrayDeque<TokenDefinition>();
							attribute.push(def);
							final Map<String, Deque<TokenDefinition>> entity = new HashMap<String, Deque<TokenDefinition>>();
							entity.put(attributeKey, attribute);
							definition.put(entityKey, entity);
						}
					} else {
						final Deque<TokenDefinition> attribute = new ArrayDeque<TokenDefinition>();
						attribute.push(def);
						final Map<String, Deque<TokenDefinition>> entity = new HashMap<String, Deque<TokenDefinition>>();
						entity.put(attributeKey, attribute);
						final Map<String, Map<String, Deque<TokenDefinition>>> definition = new HashMap<String, Map<String, Deque<TokenDefinition>>>();
						definition.put(entityKey, entity);
						definitions.put(definitionKey, definition);
					}
				}
			}
			if (isNotNullOrEmpty(definitions)) {
				if (_definitionMap != null) {
					synchronized (_definitionMap) {
						_definitionMap = synchronizedMap(unmodifiableMap(definitions));
					}
				} else {
					_definitionMap = synchronizedMap(unmodifiableMap(definitions));
				}
			} else {
				if (_definitionMap != null) {
					synchronized (_definitionMap) {
						_definitionMap = null;
					}
				}
			}
		}
	}
	
	private void refreshDictionary() {
		final Iterable<TokenDefinition> definitions = _definitions.findAll();
		if (isNotNullOrEmpty(definitions)) {
			final List<TokenDefinition> list = (List<TokenDefinition>) definitions;
			if (_dictionary != null) {
				synchronized (_dictionary) {
					_dictionary = synchronizedList(unmodifiableList(list));
				}
			} else {
				_dictionary = synchronizedList(unmodifiableList(list));
			}
		} else {
			if (_dictionary != null) {
				synchronized (_dictionary) {
					_dictionary = null;
				}
			}
		}
	}

	private void refreshSqlMap() {
		if (_definitionMap != null) {
			final Map<String, String> sqlMap = new HashMap<String, String>();
			getDefinitionMap().forEach((definitionKey, definition) -> {
				final StringBuilder sb = new StringBuilder();
				definition.forEach((entityKey, entity) -> {
					final Iterator<String> keySet = entity.keySet().iterator();
					String where = null;
					while (keySet.hasNext()) {
						final String attributeKey = keySet.next();
						where = entity.get(attributeKey).getFirst().getWhere();
						sb.append(attributeKey).append(", ");
					}
					sb.setLength(sb.length() - 2);
					sb.insert(0, SELECT_).append(_FROM_).append(entityKey).append(_WHERE_).append(where);
					sqlMap.put(definitionKey, sb.toString());
				});
			});
			if (isNotNullOrEmpty(sqlMap)) {
				if (_sqlMap != null) {
					synchronized (_sqlMap) {
						_sqlMap = synchronizedMap(unmodifiableMap(sqlMap));
					}
				} else {
					_sqlMap = synchronizedMap(unmodifiableMap(sqlMap));
				}
			} else {
				if (_sqlMap != null) {
					synchronized (_sqlMap) {
						_sqlMap = null;
					}
				}
			}
		}
	}
}