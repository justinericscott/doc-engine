package com.itgfirm.docengine.util.rest;

import static com.itgfirm.docengine.controller.RestUtils.RestConstants.*;
import static com.itgfirm.docengine.util.Utils.isNotNullOrEmpty;
import static com.itgfirm.docengine.util.Utils.isNotNullOrZero;
import static com.itgfirm.docengine.util.Logs.*;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.itgfirm.docengine.types.DocumentInstanceJpaImpl;
import com.itgfirm.docengine.types.DocumentInstances;
import com.itgfirm.docengine.types.ClauseInstanceJpaImpl;
import com.itgfirm.docengine.types.ClauseInstances;
import com.itgfirm.docengine.types.InstanceJpaImpl;
import com.itgfirm.docengine.types.Instances;
import com.itgfirm.docengine.types.ParagraphInstanceJpaImpl;
import com.itgfirm.docengine.types.ParagraphInstances;
import com.itgfirm.docengine.types.SectionInstanceJpaImpl;
import com.itgfirm.docengine.types.SectionInstances;

/**
 * @author Justin Scott
 * 
 *         REST Client for the Instance Service, to be used internally only
 */
@Component(RestClient.AUTOWIRE_QUALIFIER_INSTANCE)
class InstanceRestClientImpl extends AbstractRestClient implements RestClient {
	private static final Logger LOG = LoggerFactory.getLogger(InstanceRestClientImpl.class);

	public InstanceRestClientImpl() {
		super(INSTANCE);
		// Default constructor for Spring
	}

	@Override
	public final void delete(final Object object) {
		if (isNotNullOrEmpty(object)) {
			if (object instanceof DocumentInstanceJpaImpl) {
				final DocumentInstanceJpaImpl document = (DocumentInstanceJpaImpl) object;
				delete(DOCUMENT + BY_ID, document.getId());
			} else if (object instanceof SectionInstanceJpaImpl) {
				final SectionInstanceJpaImpl section = (SectionInstanceJpaImpl) object;
				delete(SECTION + BY_ID, section.getId());
			} else if (object instanceof ClauseInstanceJpaImpl) {
				final ClauseInstanceJpaImpl clause = (ClauseInstanceJpaImpl) object;
				delete(CLAUSE + BY_ID, clause.getId());
			} else if (object instanceof ParagraphInstanceJpaImpl) {
				final ParagraphInstanceJpaImpl paragraph = (ParagraphInstanceJpaImpl) object;
				delete(PARAGRAPH + BY_ID, paragraph.getId());
			} else if (object instanceof InstanceJpaImpl) {
				final InstanceJpaImpl content = (InstanceJpaImpl) object;
				delete(BY_ID, content.getId());
			}
		}
	}

	@Override
	public final Instances findAll() {
		return findAll(Instances.class);
	}

	@Override
	public final <T> T findAll(final Class<T> type) {
		return findAll(type, false);
	}
	
	@Override
	public final <T> T findAll(final Class<T> type, final boolean eagerKids) {
		if (isNotNullOrEmpty(type)) {
			final ResponseEntity<T> response = adhocGet(type, eagerKids);
			if (isNotNullOrEmpty(response)) {
				return type.cast(response.getBody());
			}
		} else {
			LOG.warn(LOG_NULL_TYPE);
		}
		return null;		
	}

	@Override
	public final InstanceJpaImpl findByProjectIdAndCode(final String projectId, final String code) {
		return findByProjectIdAndCode(projectId, code, InstanceJpaImpl.class);
	}

	@Override
	public final <T> T findByProjectIdAndCode(final String projectId, final String code, final Class<T> type) {
		return findByProjectIdAndCode(projectId, code, type, false);
	}

	@Override
	public final <T> T findByProjectIdAndCode(final String projectId, final String code, final Class<T> type,
			final boolean eagerKids) {
		if (isNotNullOrEmpty(type)) {
			if (isNotNullOrEmpty(code)) {
				if (isNotNullOrEmpty(projectId)) {
					String dest = "";
					if (type.equals(DocumentInstanceJpaImpl.class)) {
						dest = DOCUMENT;
					} else if (type.equals(SectionInstanceJpaImpl.class)) {
						dest = SECTION;
					} else if (type.equals(ClauseInstanceJpaImpl.class)) {
						dest = CLAUSE;
					} else if (type.equals(ParagraphInstanceJpaImpl.class)) {
						dest = PARAGRAPH;
					}
					dest = dest + BY_PROJECT_ID + BY_CODE;
					if (eagerKids) {
						dest = dest + IS_EAGER_KIDS;
					}
					final Map<String, String> params = new HashMap<String, String>();
					params.put(PARAM_PROJECT_ID, projectId);
					params.put(PARAM_CODE, code);
					params.put(PARAM_EAGER_KIDS, String.valueOf(eagerKids));
					final ResponseEntity<?> response = adhocGet(dest, type, params);
					if (isNotNullOrEmpty(response)) {
						return type.cast(response.getBody());
					}
				}
			}
		}
		return null;
	}

	@Override
	public final <T> T findByProjectIdAndCodeLike(final String projectId, final String like, final Class<T> type) {
		if (isNotNullOrEmpty(type)) {
			if (isNotNullOrEmpty(projectId)) {
				if (isNotNullOrEmpty(like)) {
					String dest = "";
					if (type.equals(DocumentInstances.class)) {
						dest = DOCUMENT;
					} else if (type.equals(SectionInstances.class)) {
						dest = SECTION;
					} else if (type.equals(ClauseInstances.class)) {
						dest = CLAUSE;
					} else if (type.equals(ParagraphInstances.class)) {
						dest = PARAGRAPH;
					}
					dest = dest + BY_PROJECT_ID + BY_CODE_LIKE;
					final Map<String, String> params = new HashMap<String, String>();
					params.put(PARAM_PROJECT_ID, projectId);
					params.put(PARAM_LIKE, like);
					final ResponseEntity<?> response = adhocGet(dest, type, params);
					if (isNotNullOrEmpty(response)) {
						return type.cast(response.getBody());
					}
				} else {
					LOG.warn(LOG_NULL_LIKE);
				}
			} else {
				LOG.warn(LOG_NULL_PROJECT_ID);
			}
		} else {
			LOG.warn(LOG_NULL_TYPE);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final InstanceJpaImpl findOne(final Long id) {
		return findOne(id, InstanceJpaImpl.class);
	}

	@Override
	public final <T> T findOne(final Long id, final Class<T> type) {
		return findOne(id, type, false);
	}

	@Override
	public final <T> T findOne(final Long id, final Class<T> type, final boolean eagerKids) {
		if (isNotNullOrEmpty(type)) {
			if (isNotNullOrZero(id)) {
				String dest = "";
				if (type.equals(DocumentInstanceJpaImpl.class)) {
					dest = DOCUMENT;
				} else if (type.equals(SectionInstanceJpaImpl.class)) {
					dest = SECTION;
				} else if (type.equals(ClauseInstanceJpaImpl.class)) {
					dest = CLAUSE;
				} else if (type.equals(ParagraphInstanceJpaImpl.class)) {
					dest = PARAGRAPH;
				}
				dest = dest + BY_ID;
				if (eagerKids) {
					LOG.debug("Setting fetch children for {}...", type.getSimpleName());
					dest = dest + IS_EAGER_KIDS;
				}
				final ResponseEntity<T> response = adhocGet(dest, type, id, eagerKids);
				if (isNotNullOrEmpty(response)) {
					return type.cast(response.getBody());
				} else {
					LOG.warn("Could not find newly saved object - Type: {}", type.getName());
				}
			} else {
				LOG.warn(LOG_NULL_INSTANCE_ID);
			}
		} else {
			LOG.warn(LOG_NULL_TYPE);
		}
		return null;
	}

	@Override
	public final <T> T getChildren(final Long id, final Class<T> type) {
		return getChildren(id, type, false);
	}

	@Override
	public final <T> T getChildren(final Long id, final Class<T> type, final boolean eagerKids) {
		if (isNotNullOrEmpty(type)) {
			if (isNotNullOrZero(id)) {
				String dest = "";
				if (type.equals(SectionInstances.class)) {
					dest = DOCUMENT;
				} else if (type.equals(ClauseInstances.class)) {
					dest = SECTION;
				} else if (type.equals(ParagraphInstances.class)) {
					dest = CLAUSE;
				} else {
					return null;
				}
				dest = dest + CHILDREN + BY_ID;
				if (eagerKids) {
					dest = dest + IS_EAGER_KIDS;
				}
				final ResponseEntity<?> response = adhocGet(dest, type, id, eagerKids);
				if (isNotNullOrEmpty(response)) {
					return type.cast(response.getBody());
				}
			} else {
				LOG.warn(LOG_NULL_INSTANCE_ID);
			}
		} else {
			LOG.warn(LOG_NULL_TYPE);
		}
		return null;
	}

	@Override
	public final <T> T getChildren(final String projectId, final String code, final Class<T> type) {
		return getChildren(projectId, code, type, false);
	}

	@Override
	public final <T> T getChildren(final String projectId, final String code, final Class<T> type,
			final boolean eagerKids) {
		if (isNotNullOrEmpty(type)) {
			if (isNotNullOrEmpty(code)) {
				if (isNotNullOrEmpty(projectId)) {
					String dest = "";
					if (type.equals(SectionInstances.class)) {
						dest = DOCUMENT;
					} else if (type.equals(ClauseInstances.class)) {
						dest = SECTION;
					} else if (type.equals(ParagraphInstances.class)) {
						dest = CLAUSE;
					} else {
						return null;
					}
					dest = dest + CHILDREN + BY_PROJECT_ID + BY_CODE;
					final Map<String, String> params = new HashMap<String, String>();
					params.put(PARAM_PROJECT_ID, projectId);
					params.put(PARAM_CODE, code);
					if (eagerKids) {
						dest = dest + IS_EAGER_KIDS;
						params.put(PARAM_EAGER_KIDS, String.valueOf(eagerKids));					
					}
					final ResponseEntity<?> response = adhocGet(dest, type, params);
					if (isNotNullOrEmpty(response)) {
						return type.cast(response.getBody());
					}
				} else {
					LOG.warn(LOG_NULL_PROJECT_ID);
				}
			} else {
				LOG.warn(LOG_NULL_CODE);
			}
		} else {
			LOG.warn(LOG_NULL_TYPE);
		}
		return null;
	}

	@Override
	public final <T> T save(final T object, final Class<T> type) {
		if (isNotNullOrEmpty(object)) {
			if (isNotNullOrEmpty(type)) {
				final HttpEntity<T> entity = new HttpEntity<T>(object);
				ResponseEntity<?> response = null;
				try {
					if (type.equals(DocumentInstanceJpaImpl.class)) {
						response = put(DOCUMENT, entity, type);
					} else if (type.equals(SectionInstanceJpaImpl.class)) {
						response = put(SECTION, entity, type);
					} else if (type.equals(ClauseInstanceJpaImpl.class)) {
						response = put(CLAUSE, entity, type);
					} else if (type.equals(ParagraphInstanceJpaImpl.class)) {
						response = put(PARAGRAPH, entity, type);
					} else if (type.equals(Instances.class)) {
						response = put(INSTANCES, entity, type);
					} else if (type.equals(InstanceJpaImpl.class)) {
						response = put(entity, type);
					}
				} catch (final Exception e) {
					LOG.error("Problem trying to save object!", e);
				}
				if (isNotNullOrEmpty(response)) {
					return type.cast(response.getBody());
				} else {
					LOG.warn("Could not find newly saved object - Type: {}", type.getName());
				}
			} else {
				LOG.warn(LOG_NULL_TYPE);
			}
		} else {
			LOG.warn(LOG_NULL_OBJECT_SAVE);
		}
		return null;
	}

//	@Override
//	public final <T> T save(final T object) {
//		if (isNotNullOrEmpty(object)) {
//			return save(object, object.getClass());
//		}
//		return null;
//	}
}