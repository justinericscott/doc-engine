package com.github.justinericscott.docengine.service.content;

import static com.github.justinericscott.docengine.util.Utils.Constants.*;
import static com.github.justinericscott.docengine.util.Utils.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.justinericscott.docengine.models.Content;
import com.github.justinericscott.docengine.models.Content.Clause;
import com.github.justinericscott.docengine.models.Content.Document;
import com.github.justinericscott.docengine.models.Content.Paragraph;
import com.github.justinericscott.docengine.models.Content.Section;
import com.github.justinericscott.docengine.models.Contents;
import com.github.justinericscott.docengine.models.Contents.Clauses;
import com.github.justinericscott.docengine.models.Contents.Documents;
import com.github.justinericscott.docengine.models.Contents.Paragraphs;
import com.github.justinericscott.docengine.models.Contents.Sections;
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
class InstanceServiceImpl implements InstanceService {
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

	@Autowired
	private ContentService _content;
	
	InstanceServiceImpl() {
		LOG.trace("Creating new Instance Service.");
	}

	@Override
	public boolean delete(final Long id) {
		if (isNotNullOrZero(id)) {
			_instances.deleteById(id);
			return !isNotNullOrEmpty(findOne(id));
		}
		return false;
	}

	@Override
	public boolean delete(final Long id, final Class<?> type) {
		if (isNotNullOrZero(id)) {
			_instances.deleteById(id);
			if (isNotNullOrEmpty(type)) {
				return !isNotNullOrEmpty(findOne(id, type));
			} else {
				return !isNotNullOrEmpty(findOne(id));
			}
		}
		return false;
	}

	@Override
	public boolean delete(final String projectId, final String code) {
		return delete(findByProjectIdAndCode(projectId, code));
	}

	@Override
	public <T> boolean delete(final String projectId, final String code, Class<T> type) {
		return delete(type.cast(findByProjectIdAndCode(projectId, code, type)), type);
	}

	@Override
	public boolean delete(final Instance instance) {
		if (isNotNullOrEmpty(instance)) {
			final Long id = Instance.class.cast(instance).getId();
			_instances.deleteById(id);
			return !isNotNullOrEmpty(findOne(id));
		}
		return false;
	}

	@Override
	public <T> boolean delete(final T instance, Class<T> type) {
		if (isNotNullOrEmpty(instance)) {
			final Long id = Instance.class.cast(instance).getId();
			_instances.deleteById(id);
			return !isNotNullOrEmpty(findOne(id, type));
		}
		return false;
	}

	@Override
	public boolean deleteAll() {
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
	public Instances findAll() {
		final List<Instance> temp = (List<Instance>) _instances.findAll();
		if (isNotNullOrEmpty(temp)) {
			return new Instances(temp);
		}
		return null;
	}

	@Override
	public <T> T findAll(final Class<T> type) {
		if (isNotNullOrEmpty(type)) {
			T one = null;
			if (type.equals(DocumentInstances.class)) {
				final Collection<DocumentInstance> documents = (Collection<DocumentInstance>) _documents.findAll();
				if (isNotNullOrEmpty(documents)) {
					LOG.debug("Found {} instance object...", type.getSimpleName());
					one = type.cast(new DocumentInstances(documents));
				}
			} else if (type.equals(SectionInstances.class)) {
				final Collection<SectionInstance> sections = (Collection<SectionInstance>) _sections.findAll();
				if (isNotNullOrEmpty(sections)) {
					LOG.debug("Found {} instance object...", type.getSimpleName());
					one = type.cast(new SectionInstances(sections));
				}
			} else if (type.equals(ClauseInstances.class)) {
				final Collection<ClauseInstance> clauses = (Collection<ClauseInstance>) _clauses.findAll();
				if (isNotNullOrEmpty(clauses)) {
					LOG.debug("Found {} instance object...", type.getSimpleName());
					one = type.cast(new ClauseInstances(clauses));
				}
			} else if (type.equals(ParagraphInstances.class)) {
				final Collection<ParagraphInstance> paragraphs = (Collection<ParagraphInstance>) _paragraphs.findAll();
				if (isNotNullOrEmpty(paragraphs)) {
					LOG.debug("Found {} instance object...", type.getSimpleName());
					one = type.cast(new ParagraphInstances(paragraphs));
				}
			} else if (type.equals(Instances.class)) {
				final Collection<Instance> contents = (Collection<Instance>) _instances.findAll();
				if (isNotNullOrEmpty(contents)) {
					LOG.debug("Found {} instance object...", type.getSimpleName());
					one = type.cast(new Instances(contents));
				}
			}
			if (one != null) {
				initialize(one);
				return one;
			}
		}
		return null;
	}

	@Override
	public Instance findByProjectIdAndCode(String projectId, String code) {
		return findByProjectIdAndCode(projectId, code, Instance.class);
	}

	@Override
	public <T> T findByProjectIdAndCode(final String projectId, final String code, final Class<T> type) {
		if (isNotNullOrEmpty(code) && isNotNullOrEmpty(projectId) && isNotNullOrEmpty(type)) {
			Optional<?> obj = null;
			if (type.equals(DocumentInstance.class)) {
				obj = _documents.findOptionalByProjectIdAndContentContentCd(projectId, code);
			} else if (type.equals(SectionInstance.class)) {
				obj = _sections.findOptionalByProjectIdAndContentContentCd(projectId, code);
			} else if (type.equals(ClauseInstance.class)) {
				obj = _clauses.findOptionalByProjectIdAndContentContentCd(projectId, code);
			} else if (type.equals(ParagraphInstance.class)) {
				obj = _paragraphs.findOptionalByProjectIdAndContentContentCd(projectId, code);
			} else if (type.equals(Instance.class)) {
				obj = _instances.findOptionalByProjectIdAndContentContentCd(projectId, code);
			} else {
				LOG.warn("Could not determine type to look for! Class: {}", type.getName());
			}
			if (isNotNullOrEmpty(obj)) {
				if (obj.isPresent()) {
					LOG.debug("Found {} instance object...", type.getSimpleName());
					Object one = obj.get();
					initialize(one);
					return type.cast(one);
				} else {
					LOG.debug("Nothing is present in the Optional object.");
				}
			} else {
				LOG.debug("No Optional object returned from repository.");
			}

		} else {
			LOG.warn("Content code must not be null or zero!");
		}
		return null;
	}

	@Override
	public Instances findByProjectIdAndCodeLike(final String projectId, final String like) {
		if (isNotNullOrEmpty(projectId) && isNotNullOrEmpty(like)) {
			final List<Instance> temp = (List<Instance>) _instances.findByProjectIdAndContentContentCdLike(projectId,
					like);
			if (isNotNullOrEmpty(temp)) {
				return new Instances(temp);
			}
		}
		return null;
	}

	@Override
	public <T> T findByProjectIdAndCodeLike(final String projectId, final String like, final Class<T> type) {
		if (isNotNullOrEmpty(projectId) && isNotNullOrEmpty(like)) {
			final List<Instance> temp = (List<Instance>) _instances.findByProjectIdAndContentContentCdLike(projectId,
					like);
			if (isNotNullOrEmpty(temp)) {
				return type.cast(new Instances(temp));
			}
		}
		return null;
	}

	@Override
	public Instance findOne(final Long id) {
		return findOne(id, Instance.class);
	}

	@Override
	public <T> T findOne(final Long id, Class<T> type) {
		if (isNotNullOrZero(id)) {
			Optional<?> obj = null;
			LOG.debug("Looking for {} instance object with id {}", type.getSimpleName(), id);
			if (type.equals(DocumentInstance.class)) {
				obj = _documents.findById(id);
			} else if (type.equals(SectionInstance.class)) {
				obj = _sections.findById(id);
			} else if (type.equals(ClauseInstance.class)) {
				obj = _clauses.findById(id);
			} else if (type.equals(ParagraphInstance.class)) {
				obj = _paragraphs.findById(id);
			} else if (type.equals(Instance.class)) {
				obj = _instances.findById(id);
			} else {
				LOG.warn("Could not determine type to look for! Class: {}", type.getName());
			}
			if (isNotNullOrEmpty(obj)) {
				if (obj.isPresent()) {
					LOG.debug("Found {} instance object...", type.getSimpleName());
					Object one = obj.get();
					initialize(one);
					return type.cast(one);
				} else {
					LOG.debug("Nothing is present in the Optional object.");
				}
			} else {
				LOG.debug("No Optional object returned from repository.");
			}
		} else {
			LOG.warn("Instance ID must not be null or zero!");
		}
		return null;
	}

	@Override
	public <T> T getChildren(final Long id, final Class<T> type) {
		if (isNotNullOrZero(id)) {
			if (isNotNullOrEmpty(type)) {
				if (type.equals(SectionInstances.class)) {
					final DocumentInstance document = findOne(id, DocumentInstance.class);
					Hibernate.initialize(document.getSections());
					Collection<SectionInstance> sections = document.getSections();
					sections.forEach(s -> {
						initialize(s);
					});
					return type.cast(new SectionInstances(sections));
				} else if (type.equals(ClauseInstances.class)) {
					final SectionInstance section = findOne(id, SectionInstance.class);
					Hibernate.initialize(section.getClauses());
					Collection<ClauseInstance> clauses = section.getClauses();
					clauses.forEach(c -> {
						initialize(c);
					});
					return type.cast(new ClauseInstances(clauses));
				} else if (type.equals(ParagraphInstances.class)) {
					final ClauseInstance clause = findOne(id, ClauseInstance.class);
					Hibernate.initialize(clause.getParagraphs());
					Collection<ParagraphInstance> paragraphs = clause.getParagraphs();
					paragraphs.forEach(p -> {
						initialize(p);
					});
					return type.cast(new ParagraphInstances(paragraphs));
				} else if (type.equals(Instances.class)) {
					final Instance one = findOne(id);
					initialize(one);
					return type.cast(one);
				}
			}
		} else {
			LOG.warn("The ID of the parent must not be null, empty or zero!");
		}
		return null;
	}

	@Override
	public <T> T getChildren(final String projectId, final String code, final Class<T> type) {
		if (isNotNullOrEmpty(code) && isNotNullOrEmpty(projectId) && isNotNullOrEmpty(type)) {
			if (type.equals(SectionInstances.class)) {
				final DocumentInstance document = findByProjectIdAndCode(projectId, code, DocumentInstance.class);
				return getChildren(document, type);
			} else if (type.equals(ClauseInstances.class)) {
				final SectionInstance section = findByProjectIdAndCode(projectId, code, SectionInstance.class);
				return getChildren(section, type);
			} else if (type.equals(ParagraphInstances.class)) {
				final ClauseInstance clause = findByProjectIdAndCode(projectId, code, ClauseInstance.class);
				return getChildren(clause, type);
			} else if (type.equals(Instances.class)) {
				final Instance instance = findByProjectIdAndCode(projectId, code);
				return getChildren(instance, type);
			}
		}
		return null;
	}

	@Override
	public <T, P> T getChildren(final P instance, final Class<T> type) {
		if (isNotNullOrEmpty(instance)) {
			if (type.equals(SectionInstances.class)) {
				final DocumentInstance document = (DocumentInstance) instance;
				Hibernate.initialize(document.getSections());
				Collection<SectionInstance> sections = document.getSections();
				sections.forEach(s -> {
					initialize(s);
				});
				return type.cast(new SectionInstances(sections));
			} else if (type.equals(ClauseInstances.class)) {
				final SectionInstance section = (SectionInstance) instance;
				Hibernate.initialize(section.getClauses());
				Collection<ClauseInstance> clauses = section.getClauses();
				clauses.forEach(c -> {
					initialize(c);
				});
				return type.cast(new ClauseInstances(clauses));
			} else if (type.equals(ParagraphInstances.class) && instance.getClass().equals(ClauseInstance.class)) {
				final ClauseInstance clause = (ClauseInstance) instance;
				Hibernate.initialize(clause.getParagraphs());
				Collection<ParagraphInstance> paragraphs = clause.getParagraphs();
				paragraphs.forEach(p -> {
					initialize(p);
				});
				return type.cast(new ParagraphInstances(paragraphs));
			} else if (type.equals(Instances.class)) {
				initialize(type.cast(instance));
				return type.cast(instance);
			}
		} else {
			LOG.warn("Parent instance must be null!");
		}
		return null;
		// return getChildren(instance, type, false);
	}

	@Override
	public Instance save(final Instance instance) {
		if (isNotNullOrEmpty(instance) && isNotNullOrEmpty(instance.getContent())
				&& instance.getContent().isValid(true)) {

			return _instances.save(instance);
		}
		return null;
	}

	@Override
	public Instances save(final Instances instances) {
		if (isNotNullOrEmpty(instances)) {
			Collection<Instance> collection = instances.getInstances();
			collection = (Collection<Instance>) _instances.saveAll(collection);
			if (isNotNullOrEmpty(collection)) {
				return new Instances(collection);
			}
		}
		return null;
	}

	@Override
	public DocumentInstance save(final DocumentInstance document) {
		if (isNotNullOrEmpty(document)) {
			return _instances.save(document);
		}
		return null;
	}

	@Override
	public SectionInstance save(final SectionInstance section) {
		if (isNotNullOrEmpty(section)) {
			return _instances.save(section);
		}
		return null;
	}

	@Override
	public ClauseInstance save(final ClauseInstance clause) {
		if (isNotNullOrEmpty(clause)) {
			return _instances.save(clause);
		}
		return null;
	}

	@Override
	public ParagraphInstance save(final ParagraphInstance paragraph) {
		if (isNotNullOrEmpty(paragraph)) {
			return _instances.save(paragraph);
		}
		return null;
	}

	void initialize(Object instance) {
		if (isNotNullOrEmpty(instance)) {
			final Class<?> type = instance.getClass();
			if (type.equals(Content.class) || type.equals(Contents.class) || type.equals(Document.class) || type.equals(Documents.class) || type.equals(Section.class) || type.equals(Sections.class) || type.equals(Clause.class) || type.equals(Clauses.class) || type.equals(Paragraph.class) || type.equals(Paragraphs.class)) {
				_content.initialize(instance);
			}
			if (type.equals(DocumentInstance.class)) {
				LOG.debug("Initializing {} instance object...", type.getSimpleName());
				final DocumentInstance d = (DocumentInstance) instance;
				Hibernate.initialize(d);
				initialize(d.getContent());
				for (final SectionInstance s : d.getSections()) {
					initialize(s);
				}
			} else if (type.equals(DocumentInstances.class)) {
				LOG.debug("Initializing {} instance object...", type.getSimpleName());
				final DocumentInstances d = (DocumentInstances) instance;
				for (final DocumentInstance i : d.getDocumentsList()) {
					initialize(i);
				}
			} else if (type.equals(SectionInstance.class)) {
				LOG.debug("Initializing {} instance object...", type.getSimpleName());
				final SectionInstance s = (SectionInstance) instance;
				Hibernate.initialize(s);
				initialize(s.getContent());
				for (final ClauseInstance c : s.getClauses()) {
					initialize(c);
				}
			} else if (type.equals(SectionInstances.class)) {
				LOG.debug("Initializing {} instance object...", type.getSimpleName());
				final SectionInstances s = (SectionInstances) instance;
				for (final SectionInstance i : s.getSectionsList()) {
					initialize(i);
				}
			} else if (type.equals(ClauseInstance.class)) {
				LOG.debug("Initializing {} instance object...", type.getSimpleName());
				final ClauseInstance c = (ClauseInstance) instance;
				Hibernate.initialize(c);
				initialize(c.getContent());
				for (final ParagraphInstance p : c.getParagraphs()) {
					initialize(p);
				}
			} else if (type.equals(ClauseInstances.class)) {
				LOG.debug("Initializing {} instance object...", type.getSimpleName());
				final ClauseInstances c = (ClauseInstances) instance;
				for (final ClauseInstance i : c.getClausesList()) {
					initialize(i);
				}
			} else if (type.equals(ParagraphInstance.class)) {
				LOG.debug("Initializing {} instance object...", type.getSimpleName());
				final ParagraphInstance p = (ParagraphInstance) instance;
				Hibernate.initialize(p);
				initialize(p.getContent());
			} else if (type.equals(ParagraphInstances.class)) {
				LOG.debug("Initializing {} instance object...", type.getSimpleName());
				final ParagraphInstances p = (ParagraphInstances) instance;
				for (final ParagraphInstance i : p.getParagraphsList()) {
					initialize(i);
				}
			} else if (type.equals(Instance.class)) {
				LOG.debug("Initializing {} instance object...", type.getSimpleName());
				final Instance i = (Instance) instance;
				Hibernate.initialize(i);
				initialize(i.getContent());
			} else if (type.equals(Instances.class)) {
				LOG.debug("Initializing {} instance object...", type.getSimpleName());
				final Instances i = (Instances) instance;
				for (final Instance idx : i.getInstances()) {
					initialize(idx);
				}
			} else {
				LOG.error("Attempting to initialize, cannot determine type! Class: {}", type.getName());
			}
		}
	}
}