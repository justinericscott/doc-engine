package com.itgfirm.docengine.service.content;

import static com.itgfirm.docengine.DocEngine.Constants.*;
import static com.itgfirm.docengine.util.Utils.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itgfirm.docengine.repository.content.ClauseInstanceRepository;
import com.itgfirm.docengine.repository.content.DocumentInstanceRepository;
import com.itgfirm.docengine.repository.content.InstanceRepository;
import com.itgfirm.docengine.repository.content.ParagraphInstanceRepository;
import com.itgfirm.docengine.repository.content.SectionInstanceRepository;
import com.itgfirm.docengine.types.DocumentInstanceJpaImpl;
import com.itgfirm.docengine.types.ClauseInstanceJpaImpl;
import com.itgfirm.docengine.types.ClauseInstances;
import com.itgfirm.docengine.types.InstanceJpaImpl;
import com.itgfirm.docengine.types.Instances;
import com.itgfirm.docengine.types.ParagraphInstanceJpaImpl;
import com.itgfirm.docengine.types.ParagraphInstances;
import com.itgfirm.docengine.types.SectionInstanceJpaImpl;
import com.itgfirm.docengine.types.SectionInstances;

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
		LOG.info("Creating new Instance Service.");
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
	public final boolean delete(final InstanceJpaImpl instance) {
		if (isNotNullOrEmpty(instance)) {
			final Long id = InstanceJpaImpl.class.cast(instance).getId();
			_instances.delete(id);
			return !isNotNullOrEmpty(findOne(id));
		}
		return false;
	}

	@Override
	public final <T> boolean delete(final T instance, Class<T> type) {
		if (isNotNullOrEmpty(instance)) {
			final Long id = InstanceJpaImpl.class.cast(instance).getId();
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
		final List<InstanceJpaImpl> temp = (List<InstanceJpaImpl>) _instances.findAll();
		if (isNotNullOrEmpty(temp)) {
			return new Instances(temp);
		}
		return null;
	}

	@Override
	public final InstanceJpaImpl findByProjectIdAndCode(String projectId, String code) {
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
			if (type.equals(DocumentInstanceJpaImpl.class)) {
				final DocumentInstanceJpaImpl document = _documents.findByProjectIdAndContentContentCd(projectId, code);
				if (eagerKids) {
					initialize(document, eagerKids);
					return type.cast(document);
				}
				return type.cast(document);
			} else if (type.equals(SectionInstanceJpaImpl.class)) {
				final SectionInstanceJpaImpl section = _sections.findByProjectIdAndContentContentCd(projectId, code);
				if (eagerKids) {
					initialize(section, eagerKids);
					return type.cast(section);
				}
				return type.cast(section);
			} else if (type.equals(ClauseInstanceJpaImpl.class)) {
				final ClauseInstanceJpaImpl clause = _clauses.findByProjectIdAndContentContentCd(projectId, code);
				if (eagerKids) {
					initialize(clause, eagerKids);
					return type.cast(clause);
				}
				return type.cast(clause);
			} else if (type.equals(ParagraphInstanceJpaImpl.class)) {
				final ParagraphInstanceJpaImpl paragraph = _paragraphs.findByProjectIdAndContentContentCd(projectId,
						code);
				if (eagerKids) {
					initialize(paragraph, eagerKids);
					return type.cast(paragraph);
				}
				return type.cast(paragraph);
			} else if (type.equals(InstanceJpaImpl.class)) {
				final InstanceJpaImpl instance = _instances.findByProjectIdAndContentContentCd(projectId, code);
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
			final List<InstanceJpaImpl> temp = (List<InstanceJpaImpl>) _instances
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
			final List<InstanceJpaImpl> temp = (List<InstanceJpaImpl>) _instances
					.findByProjectIdAndContentContentCdLike(projectId, like);
			if (isNotNullOrEmpty(temp)) {
				return type.cast(new Instances(temp));
			}
		}
		return null;
	}

	@Override
	public final InstanceJpaImpl findOne(final Long id) {
		return findOne(id, InstanceJpaImpl.class);
	}

	@Override
	public final <T> T findOne(final Long id, Class<T> type) {
		return findOne(id, type, false);
	}

	@Override
	public final <T> T findOne(final Long id, Class<T> type, final boolean eagerKids) {
		if (isNotNullOrZero(id)) {
			T one = null;
			if (type.equals(DocumentInstanceJpaImpl.class)) {
				LOG.debug("Looking for {} instance object with id {}", type.getSimpleName(), id);
				final DocumentInstanceJpaImpl document = _documents.findOne(id);
				if (isNotNullOrEmpty(document)) {
					LOG.debug("Found {} instance object...", type.getSimpleName());
					one = type.cast(document);
				}
			} else if (type.equals(SectionInstanceJpaImpl.class)) {
				LOG.debug("Looking for {} instance object with id {}", type.getSimpleName(), id);
				final SectionInstanceJpaImpl section = _sections.findOne(id);
				if (isNotNullOrEmpty(section)) {
					LOG.debug("Found {} instance object...", type.getSimpleName());
					one = type.cast(section);
				}
			} else if (type.equals(ClauseInstanceJpaImpl.class)) {
				LOG.debug("Looking for {} instance object with id {}", type.getSimpleName(), id);
				final ClauseInstanceJpaImpl clause = _clauses.findOne(id);
				if (isNotNullOrEmpty(clause)) {
					LOG.debug("Found {} instance object...", type.getSimpleName());
					one = type.cast(clause);
				}
			} else if (type.equals(ParagraphInstanceJpaImpl.class)) {
				LOG.debug("Looking for {} instance object with id {}", type.getSimpleName(), id);
				final ParagraphInstanceJpaImpl paragraph = _paragraphs.findOne(id);
				if (isNotNullOrEmpty(paragraph)) {
					LOG.debug("Found {} instance object...", type.getSimpleName());
					one = type.cast(paragraph);
				}
			} else if (type.equals(InstanceJpaImpl.class)) {
				LOG.debug("Looking for {} instance object with id {}", type.getSimpleName(), id);
				final InstanceJpaImpl instance = _instances.findOne(id);
				if (isNotNullOrEmpty(instance)) {
					LOG.debug("Found {} instance object...", type.getSimpleName());
					one = type.cast(instance);
				}
			} else {
				LOG.warn("Could not determine type to look for! Class: {}", type.getName());
			}
			if (one != null) {
				if (eagerKids) {
					LOG.debug("Eager fetch requested for {} instance object...", type.getSimpleName());
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
					final DocumentInstanceJpaImpl document = findOne(id, DocumentInstanceJpaImpl.class);
					Hibernate.initialize(document.getSections());
					Collection<SectionInstanceJpaImpl> sections = document.getSections();
					if (eagerKids) {
						sections.forEach(s -> {
							initialize(s, eagerKids);
						});
					}
					return type.cast(new SectionInstances(sections));
				} else if (type.equals(ClauseInstances.class)) {
					final SectionInstanceJpaImpl section = findOne(id, SectionInstanceJpaImpl.class);
					Hibernate.initialize(section.getClauses());
					Collection<ClauseInstanceJpaImpl> clauses = section.getClauses();
					if (eagerKids) {
						clauses.forEach(c -> {
							initialize(c, eagerKids);
						});
					}
					return type.cast(new ClauseInstances(clauses));
				} else if (type.equals(ParagraphInstances.class)) {
					final ClauseInstanceJpaImpl clause = findOne(id, ClauseInstanceJpaImpl.class);
					Hibernate.initialize(clause.getParagraphs());
					Collection<ParagraphInstanceJpaImpl> paragraphs = clause.getParagraphs();
					if (eagerKids) {
						paragraphs.forEach(p -> {
							initialize(p, eagerKids);
						});
					}
					return type.cast(new ParagraphInstances(paragraphs));
				} else if (type.equals(Instances.class)) {
					if (eagerKids) {
						final InstanceJpaImpl one = findOne(id);
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
				final DocumentInstanceJpaImpl document = findByProjectIdAndCode(projectId, code,
						DocumentInstanceJpaImpl.class);
				return getChildren(document, type, eagerKids);
			} else if (type.equals(ClauseInstances.class)) {
				final SectionInstanceJpaImpl section = findByProjectIdAndCode(projectId, code,
						SectionInstanceJpaImpl.class);
				return getChildren(section, type, eagerKids);
			} else if (type.equals(ParagraphInstances.class)) {
				final ClauseInstanceJpaImpl clause = findByProjectIdAndCode(projectId, code,
						ClauseInstanceJpaImpl.class);
				return getChildren(clause, type, eagerKids);
			} else if (type.equals(Instances.class)) {
				final InstanceJpaImpl instance = findByProjectIdAndCode(projectId, code);
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
				final DocumentInstanceJpaImpl document = (DocumentInstanceJpaImpl) instance;
				Hibernate.initialize(document.getSections());
				Collection<SectionInstanceJpaImpl> sections = document.getSections();
				if (eagerKids) {
					sections.forEach(s -> {
						initialize(s, eagerKids);
					});
				}
				return type.cast(new SectionInstances(sections));
			} else if (type.equals(ClauseInstances.class)) {
				final SectionInstanceJpaImpl section = (SectionInstanceJpaImpl) instance;
				Hibernate.initialize(section.getClauses());
				Collection<ClauseInstanceJpaImpl> clauses = section.getClauses();
				if (eagerKids) {
					clauses.forEach(c -> {
						initialize(c, eagerKids);
					});
				}
				return type.cast(new ClauseInstances(clauses));
			} else if (type.equals(ParagraphInstances.class)
					&& instance.getClass().equals(ClauseInstanceJpaImpl.class)) {
				final ClauseInstanceJpaImpl clause = (ClauseInstanceJpaImpl) instance;
				Hibernate.initialize(clause.getParagraphs());
				Collection<ParagraphInstanceJpaImpl> paragraphs = clause.getParagraphs();
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
			LOG.debug("Parent instance must be null!");
		}
		return null;
	}

	@Override
	public final InstanceJpaImpl save(final InstanceJpaImpl instance) {
		if (isNotNullOrEmpty(instance) && isNotNullOrEmpty(instance.getContent())) {
			return _instances.save(instance);
		}
		return null;
	}

	@Override
	public final Instances save(final Instances instances) {
		if (isNotNullOrEmpty(instances)) {
			Collection<? extends InstanceJpaImpl> collection = Arrays.asList(instances.getInstances());
			collection = (Collection<? extends InstanceJpaImpl>) _instances.save(collection);
			if (isNotNullOrEmpty(collection)) {
				return new Instances(collection.toArray(new InstanceJpaImpl[collection.size()]));
			}
		}
		return null;
	}

	@Override
	public final DocumentInstanceJpaImpl save(final DocumentInstanceJpaImpl document) {
		if (isNotNullOrEmpty(document)) {
			return _instances.save(document);
		}
		return null;
	}

	@Override
	public final SectionInstanceJpaImpl save(final SectionInstanceJpaImpl section) {
		if (isNotNullOrEmpty(section)) {
			return _instances.save(section);
		}
		return null;
	}

	@Override
	public final ClauseInstanceJpaImpl save(final ClauseInstanceJpaImpl clause) {
		if (isNotNullOrEmpty(clause)) {
			return _instances.save(clause);
		}
		return null;
	}

	@Override
	public final ParagraphInstanceJpaImpl save(final ParagraphInstanceJpaImpl paragraph) {
		if (isNotNullOrEmpty(paragraph)) {
			return _instances.save(paragraph);
		}
		return null;
	}

	final void initialize(Object content, boolean recursive) {
		if (isNotNullOrEmpty(content)) {
			final Class<?> type = content.getClass();
			if (type.equals(DocumentInstanceJpaImpl.class)) {
				LOG.debug("Initializing {} instance object...", type.getSimpleName());
				final DocumentInstanceJpaImpl d = (DocumentInstanceJpaImpl) content;
				Hibernate.initialize(d);
				if (recursive) {
					LOG.debug("Resurively initializing {} instance object...", type.getSimpleName());
					for (final SectionInstanceJpaImpl s : d.getSections()) {
						initialize(s, recursive);
					}
				}
			} else if (type.equals(SectionInstanceJpaImpl.class)) {
				LOG.debug("Initializing {} instance object...", type.getSimpleName());
				final SectionInstanceJpaImpl s = (SectionInstanceJpaImpl) content;
				Hibernate.initialize(s);
				if (recursive) {
					LOG.debug("Resurively initializing {} instance object...", type.getSimpleName());
					for (final ClauseInstanceJpaImpl c : s.getClauses()) {
						initialize(c, recursive);
					}
				}
			} else if (type.equals(ClauseInstanceJpaImpl.class)) {
				LOG.debug("Initializing {} instance object...", type.getSimpleName());
				final ClauseInstanceJpaImpl c = (ClauseInstanceJpaImpl) content;
				Hibernate.initialize(c);
				if (recursive) {
					LOG.debug("Resurively initializing {} instance object...", type.getSimpleName());
					for (final ParagraphInstanceJpaImpl p : c.getParagraphs()) {
						initialize(p, recursive);
					}
				}
			} else if (content.getClass().equals(ParagraphInstanceJpaImpl.class)) {
				LOG.debug("Initializing {} instance object...", type.getSimpleName());
				final ParagraphInstanceJpaImpl p = (ParagraphInstanceJpaImpl) content;
				Hibernate.initialize(p);
			} else {
				LOG.error("Attempting to initialize, cannot determine type! Class: {}", type.getName());
			}
		}
	}

	// final <T> T initialize(final T instance, Class<T> type) {
	// if (isNotNullOrEmpty(instance)) {
	// if (type.equals(DocumentInstanceJpaImpl.class)) {
	// final DocumentInstanceJpaImpl d = (DocumentInstanceJpaImpl) instance;
	// Hibernate.initialize(d.getSectionsList());
	// Hibernate.initialize(d.getContent());
	// d.getSectionsList().forEach(s -> {
	// Hibernate.initialize(s.getClausesList());
	// Hibernate.initialize(s.getContent());
	// s.getClausesList().forEach(c -> {
	// Hibernate.initialize(c.getParagraphsList());
	// Hibernate.initialize(c.getContent());
	// c.getParagraphsList().forEach(p -> {
	// Hibernate.initialize(p.getContent());
	// });
	// });
	// });
	// return type.cast(d);
	// } else if (type.equals(SectionInstanceJpaImpl.class)) {
	// final SectionInstanceJpaImpl s = (SectionInstanceJpaImpl) instance;
	// Hibernate.initialize(s.getClausesList());
	// Hibernate.initialize(s.getContent());
	// s.getClausesList().forEach(c -> {
	// Hibernate.initialize(c.getParagraphsList());
	// Hibernate.initialize(c.getContent());
	// c.getParagraphsList().forEach(p -> {
	// Hibernate.initialize(p.getContent());
	// });
	// });
	// return type.cast(s);
	// } else if (type.equals(ClauseInstanceJpaImpl.class)) {
	// final ClauseInstanceJpaImpl c = (ClauseInstanceJpaImpl) instance;
	// Hibernate.initialize(c.getParagraphsList());
	// Hibernate.initialize(c.getContent());
	// c.getParagraphsList().forEach(p -> {
	// Hibernate.initialize(p.getContent());
	// });
	// return type.cast(c);
	// } else if (type.equals(InstanceJpaImpl.class)) {
	// final InstanceJpaImpl inst = (InstanceJpaImpl) instance;
	// Hibernate.initialize(inst.getContent());
	// }
	// }
	// return null;
	// }
}