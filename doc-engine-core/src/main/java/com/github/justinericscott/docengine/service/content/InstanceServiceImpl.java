package com.github.justinericscott.docengine.service.content;

import static com.github.justinericscott.docengine.util.Utils.Constants.*;
import static com.github.justinericscott.docengine.util.Utils.*;

import java.util.Collection;
import java.util.List;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.justinericscott.docengine.models.Instance;
import com.github.justinericscott.docengine.models.Instance.ClauseInstance;
import com.github.justinericscott.docengine.models.Instance.DocumentInstance;
import com.github.justinericscott.docengine.models.Instance.ParagraphInstance;
import com.github.justinericscott.docengine.models.Instance.SectionInstance;
import com.github.justinericscott.docengine.models.Instances;
import com.github.justinericscott.docengine.models.Instances.ClauseInstances;
import com.github.justinericscott.docengine.models.Instances.DocumentInstances;
import com.github.justinericscott.docengine.models.Instances.ParagraphInstances;
import com.github.justinericscott.docengine.models.Instances.SectionInstances;
import com.github.justinericscott.docengine.repository.content.ClauseInstanceRepository;
import com.github.justinericscott.docengine.repository.content.DocumentInstanceRepository;
import com.github.justinericscott.docengine.repository.content.InstanceRepository;
import com.github.justinericscott.docengine.repository.content.ParagraphInstanceRepository;
import com.github.justinericscott.docengine.repository.content.SectionInstanceRepository;

@Service
@Transactional(AUTOWIRE_QUALIFIER_ORM_TX)
final class InstanceServiceImpl implements InstanceService {
	private static final Logger LOG = LoggerFactory.getLogger(InstanceServiceImpl.class);

	@Autowired
	private InstanceRepository _instances;

	@Autowired
	private DocumentInstanceRepository _documents;

	@Autowired
	private SectionInstanceRepository _sections;

	@Autowired
	private ClauseInstanceRepository _clauses;

	@Autowired
	private ParagraphInstanceRepository _paragraphs;

	InstanceServiceImpl() {
		LOG.debug("Creating new Instance Service.");
	}

	@Override
	public final boolean delete(final Long id) {
		if (isNotNullOrZero(id)) {
			_instances.delete(id);
			return !isNotNullOrEmpty(findOne(id));
		}
		return false;
	}

	@Override
	public final boolean delete(final Long id, final Class<?> type) {
		if (isNotNullOrZero(id)) {
			_instances.delete(id);
			if (isNotNullOrEmpty(type)) {
				return !isNotNullOrEmpty(findOne(id, type));
			} else {
				return !isNotNullOrEmpty(findOne(id));
			}
		}
		return false;
	}

	@Override
	public final boolean delete(final String projectId, final String code) {
		return delete(findByProjectIdAndCode(projectId, code));
	}

	@Override
	public final <T> boolean delete(final String projectId, final String code, Class<T> type) {
		return delete(type.cast(findByProjectIdAndCode(projectId, code, type)), type);
	}

	@Override
	public final boolean delete(final Instance instance) {
		if (isNotNullOrEmpty(instance)) {
			final Long id = Instance.class.cast(instance).getId();
			_instances.delete(id);
			return !isNotNullOrEmpty(findOne(id));
		}
		return false;
	}

	@Override
	public final <T> boolean delete(final T instance, Class<T> type) {
		if (isNotNullOrEmpty(instance)) {
			final Long id = Instance.class.cast(instance).getId();
			_instances.delete(id);
			return !isNotNullOrEmpty(findOne(id, type));
		}
		return false;
	}

	@Override
	public final boolean deleteAll() {
		_documents.deleteAll();
		_sections.deleteAll();
		_clauses.deleteAll();
		_paragraphs.deleteAll();
		_instances.deleteAll();
		return (!_documents.findAll().iterator().hasNext() && !_sections.findAll().iterator().hasNext()
				&& !_clauses.findAll().iterator().hasNext() && !_paragraphs.findAll().iterator().hasNext()
				&& !_instances.findAll().iterator().hasNext());
	}

	@Override
	public final Instances findAll() {
		final List<Instance> temp = (List<Instance>) _instances.findAll();
		if (isNotNullOrEmpty(temp)) {
			return new Instances(temp);
		}
		return null;
	}

	@Override
	public final <T> T findAll(final Class<T> type) {
		return findAll(type, false);
	}
	
	@Override
	public final <T> T findAll(final Class<T> type, final boolean eagerKids) {
		if (isNotNullOrEmpty(type)) {
			T one = null;
			if (type.equals(DocumentInstances.class)) {
				final Collection<DocumentInstance> documents = (Collection<DocumentInstance>) _documents.findAll();
				if (isNotNullOrEmpty(documents)) {
					LOG.trace("Found {} template object...", type.getSimpleName());
					one = type.cast(new DocumentInstances(documents));
				}
			} else if (type.equals(SectionInstances.class)) {
				final Collection<SectionInstance> sections = (Collection<SectionInstance>) _sections.findAll();
				if (isNotNullOrEmpty(sections)) {
					LOG.trace("Found {} template object...", type.getSimpleName());
					one = type.cast(new SectionInstances(sections));
				}
			} else if (type.equals(ClauseInstances.class)) {
				final Collection<ClauseInstance> clauses = (Collection<ClauseInstance>) _clauses.findAll();
				if (isNotNullOrEmpty(clauses)) {
					LOG.trace("Found {} template object...", type.getSimpleName());
					one = type.cast(new ClauseInstances(clauses));
				}
			} else if (type.equals(ParagraphInstances.class)) {
				final Collection<ParagraphInstance> paragraphs = (Collection<ParagraphInstance>) _paragraphs.findAll();
				if (isNotNullOrEmpty(paragraphs)) {
					LOG.trace("Found {} template object...", type.getSimpleName());
					one = type.cast(new ParagraphInstances(paragraphs));
				}
			} else if (type.equals(Instances.class)) {
				final Collection<Instance> contents = (Collection<Instance>) _instances.findAll();
				if (isNotNullOrEmpty(contents)) {
					LOG.trace("Found {} template object...", type.getSimpleName());
					one = type.cast(new Instances(contents));
				}
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
	public final Instance findByProjectIdAndCode(String projectId, String code) {
		if (isNotNullOrEmpty(projectId) && isNotNullOrEmpty(code)) {
			return _instances.findByProjectIdAndContentContentCd(projectId, code);
		}
		return null;
	}

	@Override
	public final <T> T findByProjectIdAndCode(final String projectId, final String code, final Class<T> type) {
		return findByProjectIdAndCode(projectId, code, type, false);
	}

	@Override
	public final <T> T findByProjectIdAndCode(final String projectId, final String code, final Class<T> type,
			final boolean eagerKids) {
		if (isNotNullOrEmpty(code) && isNotNullOrEmpty(projectId) && isNotNullOrEmpty(type)) {
			if (type.equals(DocumentInstance.class)) {
				final DocumentInstance document = _documents.findByProjectIdAndContentContentCd(projectId, code);
				if (eagerKids) {
					initialize(document, eagerKids);
					return type.cast(document);
				}
				return type.cast(document);
			} else if (type.equals(SectionInstance.class)) {
				final SectionInstance section = _sections.findByProjectIdAndContentContentCd(projectId, code);
				if (eagerKids) {
					initialize(section, eagerKids);
					return type.cast(section);
				}
				return type.cast(section);
			} else if (type.equals(ClauseInstance.class)) {
				final ClauseInstance clause = _clauses.findByProjectIdAndContentContentCd(projectId, code);
				if (eagerKids) {
					initialize(clause, eagerKids);
					return type.cast(clause);
				}
				return type.cast(clause);
			} else if (type.equals(ParagraphInstance.class)) {
				final ParagraphInstance paragraph = _paragraphs.findByProjectIdAndContentContentCd(projectId,
						code);
				if (eagerKids) {
					initialize(paragraph, eagerKids);
					return type.cast(paragraph);
				}
				return type.cast(paragraph);
			} else if (type.equals(Instance.class)) {
				final Instance instance = _instances.findByProjectIdAndContentContentCd(projectId, code);
				if (eagerKids) {
					initialize(instance, eagerKids);
					return type.cast(instance);
				}
				return type.cast(instance);
			}
		}
		return null;
	}

	@Override
	public final Instances findByProjectIdAndCodeLike(final String projectId, final String like) {
		if (isNotNullOrEmpty(projectId) && isNotNullOrEmpty(like)) {
			final List<Instance> temp = (List<Instance>) _instances
					.findByProjectIdAndContentContentCdLike(projectId, like);
			if (isNotNullOrEmpty(temp)) {
				return new Instances(temp);
			}
		}
		return null;
	}

	@Override
	public final <T> T findByProjectIdAndCodeLike(final String projectId, final String like, final Class<T> type) {
		if (isNotNullOrEmpty(projectId) && isNotNullOrEmpty(like)) {
			final List<Instance> temp = (List<Instance>) _instances
					.findByProjectIdAndContentContentCdLike(projectId, like);
			if (isNotNullOrEmpty(temp)) {
				return type.cast(new Instances(temp));
			}
		}
		return null;
	}

	@Override
	public final Instance findOne(final Long id) {
		return findOne(id, Instance.class);
	}

	@Override
	public final <T> T findOne(final Long id, Class<T> type) {
		return findOne(id, type, false);
	}

	@Override
	public final <T> T findOne(final Long id, Class<T> type, final boolean eagerKids) {
		if (isNotNullOrZero(id)) {
			T one = null;
			if (type.equals(DocumentInstance.class)) {
				LOG.trace("Looking for {} instance object with id {}", type.getSimpleName(), id);
				final DocumentInstance document = _documents.findOne(id);
				if (isNotNullOrEmpty(document)) {
					LOG.trace("Found {} instance object...", type.getSimpleName());
					one = type.cast(document);
				}
			} else if (type.equals(SectionInstance.class)) {
				LOG.trace("Looking for {} instance object with id {}", type.getSimpleName(), id);
				final SectionInstance section = _sections.findOne(id);
				if (isNotNullOrEmpty(section)) {
					LOG.trace("Found {} instance object...", type.getSimpleName());
					one = type.cast(section);
				}
			} else if (type.equals(ClauseInstance.class)) {
				LOG.trace("Looking for {} instance object with id {}", type.getSimpleName(), id);
				final ClauseInstance clause = _clauses.findOne(id);
				if (isNotNullOrEmpty(clause)) {
					LOG.trace("Found {} instance object...", type.getSimpleName());
					one = type.cast(clause);
				}
			} else if (type.equals(ParagraphInstance.class)) {
				LOG.trace("Looking for {} instance object with id {}", type.getSimpleName(), id);
				final ParagraphInstance paragraph = _paragraphs.findOne(id);
				if (isNotNullOrEmpty(paragraph)) {
					LOG.trace("Found {} instance object...", type.getSimpleName());
					one = type.cast(paragraph);
				}
			} else if (type.equals(Instance.class)) {
				LOG.trace("Looking for {} instance object with id {}", type.getSimpleName(), id);
				final Instance instance = _instances.findOne(id);
				if (isNotNullOrEmpty(instance)) {
					LOG.trace("Found {} instance object...", type.getSimpleName());
					one = type.cast(instance);
				}
			} else {
				LOG.warn("Could not determine type to look for! Class: {}", type.getName());
			}
			if (one != null) {
				if (eagerKids) {
					LOG.trace("Eager fetch requested for {} instance object...", type.getSimpleName());
					initialize(one, eagerKids);
				}
				return one;
			} else {
				LOG.warn("Nothing to return!");
			}
		} else {
			LOG.warn("Instance ID must not be null or zero!");
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
			T instance = null;
			if (isNotNullOrEmpty(type)) {
				if (type.equals(SectionInstances.class)) {
					final DocumentInstance document = findOne(id, DocumentInstance.class);
					Hibernate.initialize(document.getSections());
					Collection<SectionInstance> sections = document.getSections();
					if (eagerKids) {
						sections.forEach(s -> {
							initialize(s, eagerKids);
						});
					}
					return type.cast(new SectionInstances(sections));
				} else if (type.equals(ClauseInstances.class)) {
					final SectionInstance section = findOne(id, SectionInstance.class);
					Hibernate.initialize(section.getClauses());
					Collection<ClauseInstance> clauses = section.getClauses();
					if (eagerKids) {
						clauses.forEach(c -> {
							initialize(c, eagerKids);
						});
					}
					return type.cast(new ClauseInstances(clauses));
				} else if (type.equals(ParagraphInstances.class)) {
					final ClauseInstance clause = findOne(id, ClauseInstance.class);
					Hibernate.initialize(clause.getParagraphs());
					Collection<ParagraphInstance> paragraphs = clause.getParagraphs();
					if (eagerKids) {
						paragraphs.forEach(p -> {
							initialize(p, eagerKids);
						});
					}
					return type.cast(new ParagraphInstances(paragraphs));
				} else if (type.equals(Instances.class)) {
					if (eagerKids) {
						final Instance one = findOne(id);
						initialize(one, eagerKids);
						return type.cast(one);
					} else {
						return type.cast(instance);
					}
				}
			}
		} else {
			LOG.debug("The ID of the parent must not be null, empty or zero!");
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
		if (isNotNullOrEmpty(code) && isNotNullOrEmpty(projectId) && isNotNullOrEmpty(type)) {
			if (type.equals(SectionInstances.class)) {
				final DocumentInstance document = findByProjectIdAndCode(projectId, code,
						DocumentInstance.class);
				return getChildren(document, type, eagerKids);
			} else if (type.equals(ClauseInstances.class)) {
				final SectionInstance section = findByProjectIdAndCode(projectId, code,
						SectionInstance.class);
				return getChildren(section, type, eagerKids);
			} else if (type.equals(ParagraphInstances.class)) {
				final ClauseInstance clause = findByProjectIdAndCode(projectId, code,
						ClauseInstance.class);
				return getChildren(clause, type, eagerKids);
			} else if (type.equals(Instances.class)) {
				final Instance instance = findByProjectIdAndCode(projectId, code);
				return getChildren(instance, type, eagerKids);
			}
		}
		return null;
	}

	@Override
	public final <T, P> T getChildren(final P instance, final Class<T> type) {
		return getChildren(instance, type, false);
	}

	@Override
	public final <T, P> T getChildren(final P instance, final Class<T> type, final boolean eagerKids) {
		if (isNotNullOrEmpty(instance)) {
			if (type.equals(SectionInstances.class)) {
				final DocumentInstance document = (DocumentInstance) instance;
				Hibernate.initialize(document.getSections());
				Collection<SectionInstance> sections = document.getSections();
				if (eagerKids) {
					sections.forEach(s -> {
						initialize(s, eagerKids);
					});
				}
				return type.cast(new SectionInstances(sections));
			} else if (type.equals(ClauseInstances.class)) {
				final SectionInstance section = (SectionInstance) instance;
				Hibernate.initialize(section.getClauses());
				Collection<ClauseInstance> clauses = section.getClauses();
				if (eagerKids) {
					clauses.forEach(c -> {
						initialize(c, eagerKids);
					});
				}
				return type.cast(new ClauseInstances(clauses));
			} else if (type.equals(ParagraphInstances.class)
					&& instance.getClass().equals(ClauseInstance.class)) {
				final ClauseInstance clause = (ClauseInstance) instance;
				Hibernate.initialize(clause.getParagraphs());
				Collection<ParagraphInstance> paragraphs = clause.getParagraphs();
				if (eagerKids) {
					paragraphs.forEach(p -> {
						initialize(p, eagerKids);
					});
				}
				return type.cast(new ParagraphInstances(paragraphs));
			} else if (type.equals(Instances.class)) {
				if (eagerKids) {
					initialize(type.cast(instance), eagerKids);
				} else {
					return type.cast(instance);
				}
			}
		} else {
			LOG.warn("Parent instance must be null!");
		}
		return null;
	}

	@Override
	public final Instance save(final Instance instance) {
		if (isNotNullOrEmpty(instance) && isNotNullOrEmpty(instance.getContent())) {
			return _instances.save(instance);
		}
		return null;
	}

	@Override
	public final Instances save(final Instances instances) {
		if (isNotNullOrEmpty(instances)) {
			Collection<Instance> collection = instances.getInstances();
			collection = (Collection<Instance>) _instances.save(collection);
			if (isNotNullOrEmpty(collection)) {
				return new Instances(collection);
			}
		}
		return null;
	}

	@Override
	public final DocumentInstance save(final DocumentInstance document) {
		if (isNotNullOrEmpty(document)) {
			return _instances.save(document);
		}
		return null;
	}

	@Override
	public final SectionInstance save(final SectionInstance section) {
		if (isNotNullOrEmpty(section)) {
			return _instances.save(section);
		}
		return null;
	}

	@Override
	public final ClauseInstance save(final ClauseInstance clause) {
		if (isNotNullOrEmpty(clause)) {
			return _instances.save(clause);
		}
		return null;
	}

	@Override
	public final ParagraphInstance save(final ParagraphInstance paragraph) {
		if (isNotNullOrEmpty(paragraph)) {
			return _instances.save(paragraph);
		}
		return null;
	}

	final void initialize(Object content, boolean recursive) {
		if (isNotNullOrEmpty(content)) {
			final Class<?> type = content.getClass();
			if (type.equals(DocumentInstance.class)) {
				LOG.trace("Initializing {} instance object...", type.getSimpleName());
				final DocumentInstance d = (DocumentInstance) content;
				Hibernate.initialize(d);
				if (recursive) {
					LOG.trace("Resurively initializing {} instance object...", type.getSimpleName());
					for (final SectionInstance s : d.getSections()) {
						initialize(s, recursive);
					}
				}
			} else if (type.equals(SectionInstance.class)) {
				LOG.trace("Initializing {} instance object...", type.getSimpleName());
				final SectionInstance s = (SectionInstance) content;
				Hibernate.initialize(s);
				if (recursive) {
					LOG.trace("Resurively initializing {} instance object...", type.getSimpleName());
					for (final ClauseInstance c : s.getClauses()) {
						initialize(c, recursive);
					}
				}
			} else if (type.equals(ClauseInstance.class)) {
				LOG.trace("Initializing {} instance object...", type.getSimpleName());
				final ClauseInstance c = (ClauseInstance) content;
				Hibernate.initialize(c);
				if (recursive) {
					LOG.trace("Resurively initializing {} instance object...", type.getSimpleName());
					for (final ParagraphInstance p : c.getParagraphs()) {
						initialize(p, recursive);
					}
				}
			} else if (content.getClass().equals(ParagraphInstance.class)) {
				LOG.trace("Initializing {} instance object...", type.getSimpleName());
				final ParagraphInstance p = (ParagraphInstance) content;
				Hibernate.initialize(p);
			} else {
				LOG.error("Attempting to initialize, cannot determine type! Class: {}", type.getName());
			}
		}
	}
}