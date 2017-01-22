package com.itgfirm.docengine.util.rest;

import static com.itgfirm.docengine.controller.RestUtils.RestConstants.*;
import static com.itgfirm.docengine.util.Utils.isNotNullOrEmpty;
import static com.itgfirm.docengine.util.Utils.isNotNullOrZero;
import static com.itgfirm.docengine.util.Logs.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.itgfirm.docengine.types.ClauseJpaImpl;
import com.itgfirm.docengine.types.Clauses;
import com.itgfirm.docengine.types.ContentJpaImpl;
import com.itgfirm.docengine.types.Contents;
import com.itgfirm.docengine.types.DocumentJpaImpl;
import com.itgfirm.docengine.types.Documents;
import com.itgfirm.docengine.types.ParagraphJpaImpl;
import com.itgfirm.docengine.types.Paragraphs;
import com.itgfirm.docengine.types.SectionJpaImpl;
import com.itgfirm.docengine.types.Sections;

/**
 * @author Justin Scott REST Client for the Content Service, to be used
 *         internally only
 */
@Component(RestClient.AUTOWIRE_QUALIFIER_CONTENT)
class ContentRestClientImpl extends AbstractRestClient implements RestClient {
	private static final Logger LOG = LoggerFactory.getLogger(ContentRestClientImpl.class);

	public ContentRestClientImpl() {
		super(CONTENT);
		// Default constructor for Spring
	}

	@Override
	public final void delete(final Object object) {
		if (isNotNullOrEmpty(object)) {
			if (object instanceof DocumentJpaImpl) {
				final DocumentJpaImpl document = (DocumentJpaImpl) object;
				delete(DOCUMENT + BY_ID, document.getId());
			} else if (object instanceof SectionJpaImpl) {
				final SectionJpaImpl section = (SectionJpaImpl) object;
				delete(SECTION + BY_ID, section.getId());
			} else if (object instanceof ClauseJpaImpl) {
				final ClauseJpaImpl clause = (ClauseJpaImpl) object;
				delete(CLAUSE + BY_ID, clause.getId());
			} else if (object instanceof ParagraphJpaImpl) {
				final ParagraphJpaImpl paragraph = (ParagraphJpaImpl) object;
				delete(PARAGRAPH + BY_ID, paragraph.getId());
			} else if (object instanceof ContentJpaImpl) {
				final ContentJpaImpl content = (ContentJpaImpl) object;
				delete(BY_ID, content.getId());
			}
		}
	}

	@Override
	public final Contents findAll() {
		return findAll(Contents.class);
	}

	@Override
	public final <T> T findAll(final Class<T> type) {
		if (isNotNullOrEmpty(type)) {
			final ResponseEntity<T> response = adhocGet(type);
			if (isNotNullOrEmpty(response)) {
				return type.cast(response.getBody());
			}
		} else {
			LOG.warn(LOG_NULL_TYPE);
		}
		return null;
	}

	@Override
	public final <T> T findAll(final Class<T> type, boolean eagerKids) {
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
	public final ContentJpaImpl findByCode(final String code) {
		return findByCode(code, ContentJpaImpl.class);
	}

	@Override
	public final <T> T findByCode(final String code, final Class<T> type) {
		return findByCode(code, type, false);
	}

	@Override
	public final <T> T findByCode(final String code, final Class<T> type, final boolean eagerKids) {
		if (isNotNullOrEmpty(type)) {
			if (isNotNullOrEmpty(code)) {
				String dest = "";
				if (type.equals(DocumentJpaImpl.class)) {
					dest = DOCUMENT;
				} else if (type.equals(SectionJpaImpl.class)) {
					dest = SECTION;
				} else if (type.equals(ClauseJpaImpl.class)) {
					dest = CLAUSE;
				} else if (type.equals(ParagraphJpaImpl.class)) {
					dest = PARAGRAPH;
				}
				dest = dest + BY_CODE;
				if (eagerKids) {
					dest = dest + IS_EAGER_KIDS;
				}
				if (isNotNullOrEmpty(dest)) {
					final ResponseEntity<?> response = adhocGet(dest, type, code, eagerKids);
					if (isNotNullOrEmpty(response)) {
						return type.cast(response.getBody());
					}
				} else {
					LOG.warn(LOG_NULL_DESTINATION);
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
	public final Contents findByCodeLike(final String like) {
		return findByCodeLike(like, Contents.class);
	}

	@Override
	public final <T> T findByCodeLike(final String like, final Class<T> type) {
		if (isNotNullOrEmpty(type)) {
			if (isNotNullOrEmpty(like)) {
				String dest = "";
				if (type.equals(Documents.class)) {
					dest = DOCUMENT;
				} else if (type.equals(Sections.class)) {
					dest = SECTION;
				} else if (type.equals(Clauses.class)) {
					dest = CLAUSE;
				} else if (type.equals(Paragraphs.class)) {
					dest = PARAGRAPH;
//				} else if (type.equals(Contents.class)) {
//					dest = CONTENTS;
				}
				dest = dest + BY_CODE_LIKE;
				final ResponseEntity<T> response = adhocGet(dest, type, like);
				if (isNotNullOrEmpty(response)) {
					return type.cast(response.getBody());
				}
			} else {
				LOG.warn(LOG_NULL_LIKE);
			}
		} else {
			LOG.warn(LOG_NULL_TYPE);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final ContentJpaImpl findOne(final Long id) {
		return findOne(id, ContentJpaImpl.class);
	}

	public final <T> T findOne(final Long id, final Class<T> type) {
		return findOne(id, type, false);
	}

	@Override
	public final <T> T findOne(final Long id, final Class<T> type, final boolean eagerKids) {
		if (isNotNullOrEmpty(type)) {
			if (isNotNullOrZero(id)) {
				String dest = "";
				if (type.equals(DocumentJpaImpl.class)) {
					dest = DOCUMENT;
				} else if (type.equals(SectionJpaImpl.class)) {
					dest = SECTION;
				} else if (type.equals(ClauseJpaImpl.class)) {
					dest = CLAUSE;
				} else if (type.equals(ParagraphJpaImpl.class)) {
					dest = PARAGRAPH;
				}
				dest = dest + BY_ID;
				if (eagerKids && !type.equals(ParagraphJpaImpl.class) && !type.equals(ContentJpaImpl.class)) {
					dest = dest + IS_EAGER_KIDS;
				}
				final ResponseEntity<T> response = adhocGet(dest, type, id, eagerKids);
				if (isNotNullOrEmpty(response)) {
					return response.getBody();
				}
			} else {
				LOG.warn(LOG_NULL_CONTENT_ID);
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
				if (type.equals(Sections.class)) {
					dest = DOCUMENT;
				} else if (type.equals(Clauses.class)) {
					dest = SECTION;
				} else if (type.equals(Paragraphs.class)) {
					dest = CLAUSE;
				} else {
					return null;
				}
				dest = dest + CHILDREN + BY_ID;
				if (eagerKids) {
					dest = dest + IS_EAGER_KIDS;
				}
				final ResponseEntity<T> response = adhocGet(dest, type, id, eagerKids);
				if (isNotNullOrEmpty(response)) {
					return response.getBody();
				}
			} else {
				LOG.warn(LOG_NULL_CONTENT_ID);
			}
		} else {
			LOG.warn(LOG_NULL_TYPE);
		}
		return null;
	}

	@Override
	public final <T> T getChildren(final String code, final Class<T> type) {
		return getChildren(code, type, false);
	}

	@Override
	public final <T> T getChildren(final String code, final Class<T> type, final boolean eagerKids) {
		if (isNotNullOrEmpty(type)) {
			if (isNotNullOrEmpty(code)) {
				String dest = "";
				if (type.equals(Sections.class)) {
					dest = DOCUMENT;
				} else if (type.equals(Clauses.class)) {
					dest = SECTION;
				} else if (type.equals(Paragraphs.class)) {
					dest = CLAUSE;
				} else {
					return null;
				}
				dest = dest + CHILDREN + BY_CODE;
				if (eagerKids) {
					dest = dest + IS_EAGER_KIDS;
				}
				final ResponseEntity<?> response = adhocGet(dest, type, code, eagerKids);
				if (isNotNullOrEmpty(response)) {
					return type.cast(response.getBody());
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
				if (type.isInstance(object)) {
					final HttpEntity<T> entity = new HttpEntity<T>(object);
					ResponseEntity<?> response = null;
					try {
						if (type.equals(DocumentJpaImpl.class)) {
							response = put(DOCUMENT, entity, type);
						} else if (type.equals(SectionJpaImpl.class)) {
							response = put(SECTION, entity, type);
						} else if (type.equals(ClauseJpaImpl.class)) {
							response = put(CLAUSE, entity, type);
						} else if (type.equals(ParagraphJpaImpl.class)) {
							response = put(PARAGRAPH, entity, type);
						} else if (type.equals(Contents.class)) {
							response = put(CONTENTS, entity, type);
						} else if (type.equals(ContentJpaImpl.class)) {
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