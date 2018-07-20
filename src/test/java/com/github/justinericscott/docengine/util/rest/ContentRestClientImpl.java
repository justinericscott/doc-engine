package com.github.justinericscott.docengine.util.rest;

import static com.github.justinericscott.docengine.controller.RestUtils.RestConstants.*;
import static com.github.justinericscott.docengine.util.Logs.*;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrZero;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

//import com.github.justinericscott.docengine.models.Clause;
//import com.github.justinericscott.docengine.models.Clauses;
import com.github.justinericscott.docengine.models.Content;
import com.github.justinericscott.docengine.models.Content.Clause;
import com.github.justinericscott.docengine.models.Content.Document;
import com.github.justinericscott.docengine.models.Content.Paragraph;
import com.github.justinericscott.docengine.models.Content.Section;
import com.github.justinericscott.docengine.models.Contents;
import com.github.justinericscott.docengine.models.Contents.Clauses;
//import com.github.justinericscott.docengine.models.Document;
//import com.github.justinericscott.docengine.models.Documents;
//import com.github.justinericscott.docengine.models.Paragraph;
//import com.github.justinericscott.docengine.models.Paragraphs;
//import com.github.justinericscott.docengine.models.Section;
//import com.github.justinericscott.docengine.models.Sections;
import com.github.justinericscott.docengine.models.Contents.Documents;
import com.github.justinericscott.docengine.models.Contents.Paragraphs;
import com.github.justinericscott.docengine.models.Contents.Sections;

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
			if (object instanceof Document) {
				final Document document = (Document) object;
				delete(DOCUMENT + BY_ID, document.getId());
			} else if (object instanceof Section) {
				final Section section = (Section) object;
				delete(SECTION + BY_ID, section.getId());
			} else if (object instanceof Clause) {
				final Clause clause = (Clause) object;
				delete(CLAUSE + BY_ID, clause.getId());
			} else if (object instanceof Paragraph) {
				final Paragraph paragraph = (Paragraph) object;
				delete(PARAGRAPH + BY_ID, paragraph.getId());
			} else if (object instanceof Content) {
				final Content content = (Content) object;
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
//		return findAll(type, false);
	}

//	@Override
//	public final <T> T findAll(final Class<T> type, boolean eagerKids) {
//		if (isNotNullOrEmpty(type)) {
//			final ResponseEntity<T> response = adhocGet(type);
//			if (isNotNullOrEmpty(response)) {
//				return type.cast(response.getBody());
//			}
//		} else {
//			LOG.warn(LOG_NULL_TYPE);
//		}
//		return null;
//	}

	@Override
	public final Content findByCode(final String code) {
		return findByCode(code, Content.class);
	}

	@Override
	public final <T> T findByCode(final String code, final Class<T> type) {
		if (isNotNullOrEmpty(type)) {
			if (isNotNullOrEmpty(code)) {
				String dest = "";
				if (type.equals(Document.class)) {
					dest = DOCUMENT;
				} else if (type.equals(Section.class)) {
					dest = SECTION;
				} else if (type.equals(Clause.class)) {
					dest = CLAUSE;
				} else if (type.equals(Paragraph.class)) {
					dest = PARAGRAPH;
				}
				dest = dest + BY_CODE;
				if (isNotNullOrEmpty(dest)) {
					final ResponseEntity<?> response = adhocGet(dest, type, code);
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
//		return findByCode(code, type, false);
	}

//	@Override
//	public final <T> T findByCode(final String code, final Class<T> type, final boolean eagerKids) {
//		if (isNotNullOrEmpty(type)) {
//			if (isNotNullOrEmpty(code)) {
//				String dest = "";
//				if (type.equals(Document.class)) {
//					dest = DOCUMENT;
//				} else if (type.equals(Section.class)) {
//					dest = SECTION;
//				} else if (type.equals(Clause.class)) {
//					dest = CLAUSE;
//				} else if (type.equals(Paragraph.class)) {
//					dest = PARAGRAPH;
//				}
//				dest = dest + BY_CODE;
//				if (eagerKids) {
//					dest = dest + IS_EAGER_KIDS;
//				}
//				if (isNotNullOrEmpty(dest)) {
//					final ResponseEntity<?> response = adhocGet(dest, type, code, eagerKids);
//					if (isNotNullOrEmpty(response)) {
//						return type.cast(response.getBody());
//					}
//				} else {
//					LOG.warn(LOG_NULL_DESTINATION);
//				}
//			} else {
//				LOG.warn(LOG_NULL_CODE);
//			}
//		} else {
//			LOG.warn(LOG_NULL_TYPE);
//		}
//		return null;
//	}

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
	public final Content findOne(final Long id) {
		return findOne(id, Content.class);
	}

	public final <T> T findOne(final Long id, final Class<T> type) {
		if (isNotNullOrEmpty(type)) {
			if (isNotNullOrZero(id)) {
				String dest = "";
				if (type.equals(Document.class)) {
					dest = DOCUMENT;
				} else if (type.equals(Section.class)) {
					dest = SECTION;
				} else if (type.equals(Clause.class)) {
					dest = CLAUSE;
				} else if (type.equals(Paragraph.class)) {
					dest = PARAGRAPH;
				}
				dest = dest + BY_ID;
				final ResponseEntity<T> response = adhocGet(dest, type, id);
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

//	@Override
//	public final <T> T findOne(final Long id, final Class<T> type, final boolean eagerKids) {
//		if (isNotNullOrEmpty(type)) {
//			if (isNotNullOrZero(id)) {
//				String dest = "";
//				if (type.equals(Document.class)) {
//					dest = DOCUMENT;
//				} else if (type.equals(Section.class)) {
//					dest = SECTION;
//				} else if (type.equals(Clause.class)) {
//					dest = CLAUSE;
//				} else if (type.equals(Paragraph.class)) {
//					dest = PARAGRAPH;
//				}
//				dest = dest + BY_ID;
//				if (eagerKids && !type.equals(Paragraph.class) && !type.equals(Content.class)) {
//					dest = dest + IS_EAGER_KIDS;
//				}
//				final ResponseEntity<T> response = adhocGet(dest, type, id, eagerKids);
//				if (isNotNullOrEmpty(response)) {
//					return response.getBody();
//				}
//			} else {
//				LOG.warn(LOG_NULL_CONTENT_ID);
//			}
//		} else {
//			LOG.warn(LOG_NULL_TYPE);
//		}
//		return null;
//	}

	@Override
	public final <T> T getChildren(final Long id, final Class<T> type) {
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
				final ResponseEntity<T> response = adhocGet(dest, type, id);
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
//		return getChildren(id, type, false);
	}

//	@Override
//	public final <T> T getChildren(final Long id, final Class<T> type, final boolean eagerKids) {
//		if (isNotNullOrEmpty(type)) {
//			if (isNotNullOrZero(id)) {
//				String dest = "";
//				if (type.equals(Sections.class)) {
//					dest = DOCUMENT;
//				} else if (type.equals(Clauses.class)) {
//					dest = SECTION;
//				} else if (type.equals(Paragraphs.class)) {
//					dest = CLAUSE;
//				} else {
//					return null;
//				}
//				dest = dest + CHILDREN + BY_ID;
//				if (eagerKids) {
//					dest = dest + IS_EAGER_KIDS;
//				}
//				final ResponseEntity<T> response = adhocGet(dest, type, id, eagerKids);
//				if (isNotNullOrEmpty(response)) {
//					return response.getBody();
//				}
//			} else {
//				LOG.warn(LOG_NULL_CONTENT_ID);
//			}
//		} else {
//			LOG.warn(LOG_NULL_TYPE);
//		}
//		return null;
//	}

	@Override
	public final <T> T getChildren(final String code, final Class<T> type) {
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
				final ResponseEntity<?> response = adhocGet(dest, type, code);
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
//		return getChildren(code, type, false);
	}

//	@Override
//	public final <T> T getChildren(final String code, final Class<T> type, final boolean eagerKids) {
//		if (isNotNullOrEmpty(type)) {
//			if (isNotNullOrEmpty(code)) {
//				String dest = "";
//				if (type.equals(Sections.class)) {
//					dest = DOCUMENT;
//				} else if (type.equals(Clauses.class)) {
//					dest = SECTION;
//				} else if (type.equals(Paragraphs.class)) {
//					dest = CLAUSE;
//				} else {
//					return null;
//				}
//				dest = dest + CHILDREN + BY_CODE;
//				if (eagerKids) {
//					dest = dest + IS_EAGER_KIDS;
//				}
//				final ResponseEntity<?> response = adhocGet(dest, type, code, eagerKids);
//				if (isNotNullOrEmpty(response)) {
//					return type.cast(response.getBody());
//				}
//			} else {
//				LOG.warn(LOG_NULL_CODE);
//			}
//		} else {
//			LOG.warn(LOG_NULL_TYPE);
//		}
//		return null;
//	}

	@Override
	public final <T> T save(final T object, final Class<T> type) {
		if (isNotNullOrEmpty(object)) {
			if (isNotNullOrEmpty(type)) {
				if (type.isInstance(object)) {
					final HttpEntity<T> entity = new HttpEntity<T>(object);
					ResponseEntity<?> response = null;
					try {
						if (type.equals(Document.class)) {
							response = put(DOCUMENT, entity, type);
						} else if (type.equals(Section.class)) {
							response = put(SECTION, entity, type);
						} else if (type.equals(Clause.class)) {
							response = put(CLAUSE, entity, type);
						} else if (type.equals(Paragraph.class)) {
							response = put(PARAGRAPH, entity, type);
						} else if (type.equals(Contents.class)) {
							response = put(CONTENTS, entity, type);
						} else if (type.equals(Content.class)) {
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