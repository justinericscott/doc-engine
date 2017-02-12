package com.github.justinericscott.docengine.service.content;

import static com.github.justinericscott.docengine.DocEngine.Constants.*;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrZero;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

import org.hibernate.Hibernate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.justinericscott.docengine.models.Clause;
import com.github.justinericscott.docengine.models.Clauses;
import com.github.justinericscott.docengine.models.Content;
import com.github.justinericscott.docengine.models.Contents;
import com.github.justinericscott.docengine.models.Document;
import com.github.justinericscott.docengine.models.Documents;
import com.github.justinericscott.docengine.models.Paragraph;
import com.github.justinericscott.docengine.models.Paragraphs;
import com.github.justinericscott.docengine.models.Section;
import com.github.justinericscott.docengine.models.Sections;
import com.github.justinericscott.docengine.repository.content.ClauseRepository;
import com.github.justinericscott.docengine.repository.content.ContentRepository;
import com.github.justinericscott.docengine.repository.content.DocumentRepository;
import com.github.justinericscott.docengine.repository.content.ParagraphRepository;
import com.github.justinericscott.docengine.repository.content.SectionRepository;

/**
 * @author Justin Scott
 * 
 *         Default implementation of the Content Service.
 */
@Service
@Transactional(AUTOWIRE_QUALIFIER_ORM_TX)
final class ContentServiceImpl implements ContentService {
	private static final Logger LOG = LoggerFactory.getLogger(ContentServiceImpl.class);

	@Autowired
	private ContentRepository _contents;

	@Autowired
	private DocumentRepository _documents;

	@Autowired
	private SectionRepository _sections;

	@Autowired
	private ClauseRepository _clauses;

	@Autowired
	private ParagraphRepository _paragraphs;

	ContentServiceImpl() {
		LOG.debug("Creating new Content Service.");
	}

	@Override
	public final boolean delete(final Long id) {
		if (isNotNullOrZero(id)) {
			_contents.delete(id);
			return (!isNotNullOrEmpty(findOne(id)));
		}
		return false;
	}

	@Override
	public final boolean delete(final Long id, final Class<?> type) {
		if (isNotNullOrEmpty(type)) {
			if (isNotNullOrZero(id)) {
				if (type.equals(Document.class)) {
					_documents.delete(id);
				} else if (type.equals(Section.class)) {
					_sections.delete(id);
				} else if (type.equals(Clause.class)) {
					_clauses.delete(id);
				} else if (type.equals(Paragraph.class)) {
					_paragraphs.delete(id);
				} else if (type.equals(Content.class)) {
					_contents.delete(id);
				} else {
					LOG.error("Could not determine which repository to use for this type: {}", type.getName());
				}
				return !isNotNullOrEmpty(findOne(id));
			}
		}
		return false;
	}

	@Override
	public final boolean delete(final String code) {
		if (isNotNullOrEmpty(code)) {
			delete(findByCode(code));
			return (!isNotNullOrEmpty(findByCode(code)));
		}
		return false;
	}

	@Override
	public final <T> boolean delete(final String code, final Class<T> type) {
		if (isNotNullOrEmpty(type)) {
			if (isNotNullOrEmpty(code)) {
				final T object = findByCode(code, type);
				if (isNotNullOrEmpty(object)) {
					return delete(object, type);
				}
			}
		}
		return false;
	}

	@Override
	public final boolean delete(final Content content) {
		if (isNotNullOrEmpty(content)) {
			_contents.delete(content);
			return (isNotNullOrEmpty(findOne(content.getId())));
		}
		return false;
	}

	@Override
	public final boolean delete(final Contents contents) {
		if (isNotNullOrEmpty(contents)) {
			return delete(contents.getContents());
		}
		return false;
	}

	@Override
	public final boolean delete(final Content[] contents) {
		if (isNotNullOrEmpty(contents)) {
			List<Content> list = Arrays.asList(contents);
			_contents.delete(list);
			for (final Content content : contents) {
				if (isNotNullOrEmpty(findOne(content.getId()))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public final <T> boolean delete(final T object, final Class<T> type) {
		Long id = null;
		if (type.equals(Document.class)) {
			final Document document = Document.class.cast(object);
			id = document.getId();
			_documents.delete(document);
		} else if (type.equals(Documents.class)) {
			final Documents documents = Documents.class.cast(object);
			_documents.delete(Arrays.asList(documents.getDocuments()));
		} else if (type.equals(Section.class)) {
			final Section section = Section.class.cast(object);
			id = section.getId();
			_sections.delete(section);
		} else if (type.equals(Sections.class)) {
			final Sections sections = Sections.class.cast(object);
			_sections.delete(sections.getSectionsList());
		} else if (type.equals(Clause.class)) {
			final Clause clause = Clause.class.cast(object);
			id = clause.getId();
			_clauses.delete(clause);
		} else if (type.equals(Clauses.class)) {
			final Clauses clauses = Clauses.class.cast(object);
			_clauses.delete(clauses.getClausesList());
		} else if (type.equals(Paragraph.class)) {
			final Paragraph paragraph = Paragraph.class.cast(object);
			id = paragraph.getId();
			_paragraphs.delete(paragraph);
		} else if (type.equals(Paragraphs.class)) {
			final Paragraphs paragraphs = Paragraphs.class.cast(object);
			_paragraphs.delete(paragraphs.getParagraphsList());
		} else if (type.equals(Content.class)) {
			return delete(Content.class.cast(object));
		} else {
			LOG.error("Could not determine which repository to use for this type: {}", type.getName());
		}
		if (isNotNullOrZero(id)) {
			return !isNotNullOrEmpty(findOne(id));
		}
		return false;
	}

	@Override
	public boolean deleteAll() {
		_documents.deleteAll();
		_sections.deleteAll();
		_clauses.deleteAll();
		_paragraphs.deleteAll();
		_contents.deleteAll();
		return (findAll(Document.class) == null && findAll(Section.class) == null
				&& findAll(Clause.class) == null && findAll(Paragraph.class) == null
				&& findAll() == null);
	}

	@Override
	public final <T> boolean deleteByCodeLike(final String like, final Class<T> type) {
		if (isNotNullOrEmpty(type)) {
			if (isNotNullOrEmpty(like)) {
				return delete(findByCodeLike(like, type), type);
			}
		}
		return false;
	}

	@Override
	public Contents findAll() {
		return findAll(Contents.class);
	}

	@Override
	public final <T> T findAll(final Class<T> type) {
		return findAll(type, false);
	}

	@Override
	public final <T> T findAll(final Class<T> type, final boolean eagerKids) {
		if (isNotNullOrEmpty(type)) {
			T one = null;
			if (type.equals(Documents.class)) {
				final Collection<Document> documents = (Collection<Document>) _documents.findAll();
				if (isNotNullOrEmpty(documents)) {
					LOG.trace("Found {} template object...", type.getSimpleName());
					one = type.cast(new Documents(documents));
				}
			} else if (type.equals(Sections.class)) {
				final Collection<Section> sections = (Collection<Section>) _sections.findAll();
				if (isNotNullOrEmpty(sections)) {
					LOG.trace("Found {} template object...", type.getSimpleName());
					one = type.cast(new Sections(sections));
				}
			} else if (type.equals(Clauses.class)) {
				final Collection<Clause> clauses = (Collection<Clause>) _clauses.findAll();
				if (isNotNullOrEmpty(clauses)) {
					LOG.trace("Found {} template object...", type.getSimpleName());
					one = type.cast(new Clauses(clauses));
				}
			} else if (type.equals(Paragraphs.class)) {
				final Collection<Paragraph> paragraphs = (Collection<Paragraph>) _paragraphs.findAll();
				if (isNotNullOrEmpty(paragraphs)) {
					LOG.trace("Found {} template object...", type.getSimpleName());
					one = type.cast(new Paragraphs(paragraphs));
				}
			} else if (type.equals(Contents.class)) {
				final Collection<Content> contents = (Collection<Content>) _contents.findAll();
				if (isNotNullOrEmpty(contents)) {
					LOG.trace("Found {} template object...", type.getSimpleName());
					one = type.cast(new Contents(contents));
				}
			}
			if (one != null) {
				if (eagerKids) {
					LOG.trace("Eager fetch requested for {} template object...", type.getSimpleName());
					initialize(one, eagerKids);
				}
				return one;
			}
		} else {
			LOG.warn("Type must not be null!");
		}
		return null;
	}

	@Override
	public final Content findByCode(final String code) {
		if (isNotNullOrEmpty(code)) {
			return _contents.findByContentCd(code);
		}
		return null;
	}

	@Override
	public final <T> T findByCode(final String code, Class<T> type) {
		return findByCode(code, type, false);
	}

	@Override
	public final <T> T findByCode(final String code, Class<T> type, final boolean eagerKids) {
		if (type.equals(Document.class)) {
			final Document content = _documents.findByContentCd(code);
			if (isNotNullOrEmpty(content)) {
				if (eagerKids) {
					initialize(content, eagerKids);
				}
				return type.cast(content);
			}
		} else if (type.equals(Section.class)) {
			final Section content = _sections.findByContentCd(code);
			if (isNotNullOrEmpty(content)) {
				if (eagerKids) {
					initialize(content, eagerKids);
				}
				return type.cast(content);
			}
		} else if (type.equals(Clause.class)) {
			final Clause content = _clauses.findByContentCd(code);
			if (isNotNullOrEmpty(content)) {
				if (eagerKids) {
					initialize(content, eagerKids);
				}
				return type.cast(content);
			}
		} else if (type.equals(Paragraph.class)) {
			final Paragraph content = _paragraphs.findByContentCd(code);
			if (isNotNullOrEmpty(content)) {
				return type.cast(content);
			}
		} else {
			final Content content = findByCode(code);
			if (isNotNullOrEmpty(content)) {
				return type.cast(content);
			}
		}
		return null;
	}

	@Override
	public final Contents findByCodeLike(final String like) {
		if (isNotNullOrEmpty(like)) {
			final String term = ((like.startsWith("%") || like.endsWith("%")) ? like : "%".concat(like).concat("%"));
			final Collection<Content> list = (Collection<Content>) _contents.findByContentCdLike(term);
			if (isNotNullOrEmpty(list)) {
				return new Contents(list.toArray(new Content[list.size()]));
			}
		}
		return null;
	}

	@Override
	public final <T> T findByCodeLike(final String like, Class<T> type) {
		return findByCodeLike(like, type, false);
	}

	@Override
	public final <T> T findByCodeLike(final String like, Class<T> type, final boolean eagerKids) {
		if (isNotNullOrEmpty(like)) {
			final String term = ((like.startsWith("%") || like.endsWith("%")) ? like : "%".concat(like).concat("%"));
			T result = null;
			if (type.equals(Documents.class)) {
				final List<Document> list = (List<Document>) _documents.findByContentCdLike(term);
				final Documents content = new Documents(list);
				if (isNotNullOrEmpty(content)) {
					result = type.cast(content);
					if (eagerKids) {
						initialize(result, eagerKids);
					}
					return result;
				}
			} else if (type.equals(Sections.class)) {
				final List<Section> list = (List<Section>) _sections.findByContentCdLike(term);
				final Sections content = new Sections(list);
				if (isNotNullOrEmpty(content)) {
					result = type.cast(content);
					if (eagerKids) {
						initialize(result, eagerKids);
					}
					return result;
				}
			} else if (type.equals(Clauses.class)) {
				final List<Clause> list = (List<Clause>) _clauses.findByContentCdLike(term);
				final Clauses content = new Clauses(list);
				if (isNotNullOrEmpty(content)) {
					result = type.cast(content);
					if (eagerKids) {
						initialize(result, eagerKids);
					}
					return result;
				}
			} else if (type.equals(Paragraphs.class)) {
				final List<Paragraph> list = (List<Paragraph>) _paragraphs.findByContentCdLike(term);
				final Paragraphs content = new Paragraphs(list);
				if (isNotNullOrEmpty(content)) {
					return type.cast(content);
				}
			} else if (type.equals(Contents.class)) {
				Collection<Content> list = (Collection<Content>) _contents.findByContentCdLike(term);
				final Contents contents = new Contents(list.toArray(new Content[list.size()]));
				if (isNotNullOrEmpty(contents)) {
					return type.cast(contents);
				}
			}
		}
		return null;
	}

	@Override
	public final Content findOne(final Long id) {
		if (isNotNullOrZero(id)) {
			return _contents.findOne(id);
		}
		return null;
	}

	@Override
	public final <T> T findOne(final Long id, final Class<T> type) {
		return findOne(id, type, false);
	}

	@Override
	public final <T> T findOne(final Long id, final Class<T> type, final boolean eagerKids) {
		if (isNotNullOrZero(id)) {
			T one = null;
			if (type.equals(Document.class)) {
				final Document document = _documents.findOne(id);
				if (isNotNullOrEmpty(document)) {
					LOG.trace("Found {} template object...", type.getSimpleName());
					one = type.cast(document);
				}
			} else if (type.equals(Section.class)) {
				final Section section = _sections.findOne(id);
				if (isNotNullOrEmpty(section)) {
					LOG.trace("Found {} template object...", type.getSimpleName());
					one = type.cast(section);
				}
			} else if (type.equals(Clause.class)) {
				final Clause clause = _clauses.findOne(id);
				if (isNotNullOrEmpty(clause)) {
					LOG.trace("Found {} template object...", type.getSimpleName());
					one = type.cast(clause);
				}
			} else if (type.equals(Paragraph.class)) {
				final Paragraph paragraph = _paragraphs.findOne(id);
				if (isNotNullOrEmpty(paragraph)) {
					LOG.trace("Found {} template object...", type.getSimpleName());
					one = type.cast(paragraph);
				}
			} else if (type.equals(Content.class)) {
				final Content content = findOne(id);
				if (isNotNullOrEmpty(content)) {
					LOG.trace("Found {} template object...", type.getSimpleName());
					one = type.cast(content);
				}
			} else {
				LOG.warn("Could not determine type to look for! Class: {}", type.getName());
			}
			if (one != null) {
				if (eagerKids) {
					LOG.trace("Eager fetch requested for {} template object...", type.getSimpleName());
					initialize(one, eagerKids);
				}
				return one;
			}
		}
		return null;
	}

	@Override
	public final <T> T getChildren(final Long id, final Class<T> type) {
		return getChildren(id, type, false);
	}

	@Override
	public final <T> T getChildren(final Long id, final Class<T> type, final boolean eagerKids) {
		if (isNotNullOrZero(id)) {
			if (isNotNullOrEmpty(type)) {
				if (type.equals(Sections.class)) {
					return getChildren(findOne(id, Document.class), type, eagerKids);
				} else if (type.equals(Clauses.class)) {
					return getChildren(findOne(id, Section.class), type, eagerKids);
				} else if (type.equals(Paragraphs.class)) {
					return getChildren(findOne(id, Clause.class), type, eagerKids);
				} else if (type.equals(Contents.class)) {
					return getChildren(findOne(id), type, eagerKids);
				}
			}
		}
		return null;
	}

	@Override
	public final <T> T getChildren(final String code, final Class<T> type) {
		return getChildren(code, type, false);
	}

	@Override
	public final <T> T getChildren(final String code, final Class<T> type, final boolean eagerKids) {
		return getChildren(findByCode(code), type, eagerKids);
	}

	@Override
	public final <T, P> T getChildren(final P content, final Class<T> type) {
		return getChildren(content, type, false);
	}

	@Override
	public final <T, P> T getChildren(final P content, final Class<T> type, boolean eagerKids) {
		if (isNotNullOrEmpty(content)) {
			Object children = null;
			if (content.getClass().equals(Document.class)) {
				final Document parent = (Document) content;
				initialize(parent, eagerKids);
				children = new Sections(parent.getSections());
			} else if (content.getClass().equals(Section.class)) {
				final Section parent = (Section) content;
				initialize(parent, eagerKids);
				children = new Clauses(parent.getClauses());
			} else if (content.getClass().equals(Clause.class)) {
				final Clause parent = (Clause) content;
				initialize(parent, eagerKids);
				children = new Paragraphs(parent.getParagraphs());
			}
			if (children != null) {
				return type.cast(children);
			}
		}
		return null;
	}

	@Override
	public Content save(final Content content) {
		if (isNotNullOrEmpty(content)) {
			return _contents.save(content);
		}
		return null;
	}

	@Override
	public final <T> Iterable<T> save(final Iterable<T> objects, final Class<T> type) {
		if (isNotNullOrEmpty(objects)) {
			final Collection<T> saved = new TreeSet<T>();
			objects.forEach(o -> {
				if (type.equals(Document.class)) {
					final Document d = _documents.save((Document) o);
					saved.add(type.cast(d));
				} else if (type.equals(Section.class)) {
					final Section s = _sections.save((Section) o);
					saved.add(type.cast(s));
				} else if (type.equals(Clause.class)) {
					final Clause c = _clauses.save((Clause) o);
					saved.add(type.cast(c));
				} else if (type.equals(Paragraph.class)) {
					final Paragraph p = _paragraphs.save((Paragraph) o);
					saved.add(type.cast(p));
				} else if (type.equals(Content.class)) {
					final Content c = _contents.save((Content) o);
					saved.add(type.cast(c));					
				}
			});
			return saved;
		}
		return null;
	}

	@Override
	public final Contents save(final Contents contents) {
		if (isNotNullOrEmpty(contents)) {
			Collection<Content> collection = Arrays.asList(contents.getContents());
			collection = (Collection<Content>) _contents.save(collection);
			if (isNotNullOrEmpty(collection)) {
				return new Contents(collection.toArray(new Content[collection.size()]));
			}
		}
		return null;
	}

	@Override
	public final Document save(final Document document) {
		if (isNotNullOrEmpty(document)) {
			return _documents.save(document);
		}
		return null;
	}

	@Override
	public final Section save(final Section document) {
		if (isNotNullOrEmpty(document)) {
			return _sections.save(document);
		}
		return null;
	}

	@Override
	public final Clause save(final Clause document) {
		if (isNotNullOrEmpty(document)) {
			return _clauses.save(document);
		}
		return null;
	}

	@Override
	public final Paragraph save(final Paragraph document) {
		if (isNotNullOrEmpty(document)) {
			return _paragraphs.save(document);
		}
		return null;
	}

	final void initialize(Object content, boolean recursive) {
		if (isNotNullOrEmpty(content)) {
			final Class<?> type = content.getClass();
			if (type.equals(Document.class)) {
				LOG.trace("Initializing {} template object...", type.getSimpleName());
				final Document d = (Document) content;
				Hibernate.initialize(d);
				if (recursive) {
					LOG.trace("Resurively initializing {} template object...", type.getSimpleName());
					for (final Section s : d.getSections()) {
						initialize(s, recursive);
					}
				}
			} else if (type.equals(Section.class)) {
				LOG.trace("Initializing Section template object...");
				final Section s = (Section) content;
				Hibernate.initialize(s);
				if (recursive) {
					LOG.trace("Resurively initializing {} template object...", type.getSimpleName());
					for (final Clause c : s.getClauses()) {
						initialize(c, recursive);
					}
				}
			} else if (type.equals(Clause.class)) {
				LOG.trace("Initializing Clause template object...");
				final Clause c = (Clause) content;
				Hibernate.initialize(c);
				if (recursive) {
					LOG.trace("Resurively initializing {} template object...", type.getSimpleName());
					for (final Paragraph p : c.getParagraphs()) {
						initialize(p, recursive);
					}
				}
			} else if (type.equals(Paragraph.class)) {
				LOG.trace("Initializing Paragraph template object...");
				final Paragraph p = (Paragraph) content;
				Hibernate.initialize(p);
			} else {
				LOG.error("Attempting to initialize, cannot determine type! Class: {}", type.getName());
			}
		}
	}
}