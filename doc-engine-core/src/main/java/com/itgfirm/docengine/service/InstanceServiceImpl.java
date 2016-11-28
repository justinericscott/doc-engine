package com.itgfirm.docengine.service;

import static com.itgfirm.docengine.util.Constants.*;
import static com.itgfirm.docengine.util.Utils.*;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itgfirm.docengine.repository.InstanceRepository;
import com.itgfirm.docengine.types.jpa.AdvancedDocumentInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.ClauseInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.InstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.SectionInstanceJpaImpl;

@Service
@Transactional(value = AUTOWIRE_QUALIFIER_ORM_TX)
@Qualifier(AUTOWIRE_QUALIFIER_INSTANCE)
class InstanceServiceImpl implements InstanceService {
	private static final Logger LOG = LoggerFactory.getLogger(InstanceServiceImpl.class);

	@Autowired
	@Qualifier(AUTOWIRE_QUALIFIER_INSTANCE)
	private InstanceRepository repo;

	public InstanceServiceImpl() {
		// Default constructor for Spring
	}

	@Override
	public final void delete(final Long id) {
		if (isNotNullOrZero(id)) {
			repo.delete(id);
		}
	}

	@Override
	public final void delete(final String projectId, final String code) {
		delete(findByProjectIdAndContentCd(projectId, code));
	}

	@Override
	public final void delete(final InstanceJpaImpl instance) {
		if (isNotNullOrEmpty(instance)) {
			repo.delete(instance);
		}
	}

	@Override
	public final Iterable<? extends InstanceJpaImpl> findAll() {
		return repo.findAll();
	}

	@Override
	public final Iterable<? extends InstanceJpaImpl> findByParent(final Long id) {
		return findByParent(id, false);
	}

	@Override
	public final Iterable<? extends InstanceJpaImpl> findByParent(final Long id, final boolean eagerKids) {
		if (isNotNullOrZero(id)) {
			final InstanceJpaImpl instance = findOne(id);
			if (isNotNullOrEmpty(instance)) {
				return findByParent(instance, eagerKids);
			}
		} else {
			LOG.debug("The ID of the parent must not be null, empty or zero!");
		}
		return null;
	}

	@Override
	public final Iterable<? extends InstanceJpaImpl> findByParent(final String projectId, final String code) {
		return findByParent(projectId, code, false);
	}

	@Override
	public final Iterable<? extends InstanceJpaImpl> findByParent(final String projectId, final String code,
			final boolean eagerKids) {
		if (isNotNullOrEmpty(code) && isNotNullOrEmpty(projectId)) {
			return findByParent(findByProjectIdAndContentCd(projectId, code), eagerKids);
		}
		return null;
	}

	@Override
	public final Iterable<? extends InstanceJpaImpl> findByParent(final InstanceJpaImpl instance) {
		return findByParent(instance, false);
	}

	@Override
	public final Iterable<? extends InstanceJpaImpl> findByParent(final InstanceJpaImpl instance,
			final boolean eagerKids) {
		if (isNotNullOrEmpty(instance)) {
			Iterable<? extends InstanceJpaImpl> children = null;
			if (instance instanceof AdvancedDocumentInstanceJpaImpl) {
				final AdvancedDocumentInstanceJpaImpl document = (AdvancedDocumentInstanceJpaImpl) instance;
				Hibernate.initialize(document.getSections());
				children = document.getSections();
			} else if (instance instanceof SectionInstanceJpaImpl) {
				final SectionInstanceJpaImpl section = (SectionInstanceJpaImpl) instance;
				Hibernate.initialize(section.getClauses());
				children = section.getClauses();
			} else if (instance instanceof ClauseInstanceJpaImpl) {
				final ClauseInstanceJpaImpl clause = (ClauseInstanceJpaImpl) instance;
				Hibernate.initialize(clause.getParagraphs());
				children = clause.getParagraphs();
			}
			if (eagerKids && isNotNullOrEmpty(children)) {
				children.forEach(child -> {
					initialize(child);
				});
			}
			return children;
		} else {
			LOG.debug("Parent instance must be null!");
		}
		return null;
	}

	@Override
	public final InstanceJpaImpl findByProjectIdAndContentCd(final String projectId, final String code) {
		return findByProjectIdAndContentCd(projectId, code, false);
	}

	@Override
	public final InstanceJpaImpl findByProjectIdAndContentCd(final String projectId, final String code,
			final boolean eagerKids) {
		if (eagerKids) {
			return initialize(repo.findByProjectIdAndContentContentCd(projectId, code));
		} else {
			return repo.findByProjectIdAndContentContentCd(projectId, code);
		}
	}

	@Override
	public final Iterable<? extends InstanceJpaImpl> findByProjectIdAndContentCdLike(final String projectId,
			final String like) {
		if (isNotNullOrEmpty(projectId) && isNotNullOrEmpty(like)) {
			return repo.findByProjectIdAndContentContentCdLike(projectId, like);
		}
		return null;
	}

	@Override
	public final InstanceJpaImpl findOne(final Long id) {
		if (isNotNullOrZero(id)) {
			return repo.findOne(id);
		}
		return null;
	}

	@Override
	public final InstanceJpaImpl findOne(final Long id, final boolean eagerKids) {
		if (eagerKids) {
			return initialize(findOne(id));
		} else {
			return findOne(id);
		}
	}

	@Override
	public final InstanceJpaImpl save(final InstanceJpaImpl instance) {
		if (isNotNullOrEmpty(instance) && isNotNullOrEmpty(instance.getContent())) {
			return repo.save(instance);
		}
		return null;
	}

	@Override
	public final Iterable<? extends InstanceJpaImpl> save(final Iterable<? extends InstanceJpaImpl> instances) {
		if (isNotNullOrEmpty(instances)) {
			return (Iterable<? extends InstanceJpaImpl>) repo.save(instances);
		}
		return null;
	}

	final InstanceJpaImpl initialize(final InstanceJpaImpl instance) {
		if (isNotNullOrEmpty(instance) && isNotNullOrZero(instance.getId())) {
			if (instance instanceof AdvancedDocumentInstanceJpaImpl) {
				final AdvancedDocumentInstanceJpaImpl d = (AdvancedDocumentInstanceJpaImpl) instance;
				Hibernate.initialize(d.getSections());
				d.getSections().forEach(s -> {
					Hibernate.initialize(s.getClauses());
					s.getClauses().forEach(c -> {
						Hibernate.initialize(c.getParagraphs());
					});
				});
				return d;
			} else if (instance instanceof SectionInstanceJpaImpl) {
				final SectionInstanceJpaImpl s = (SectionInstanceJpaImpl) instance;
				Hibernate.initialize(s.getClauses());
				s.getClauses().forEach(c -> {
					Hibernate.initialize(c.getParagraphs());
				});
				return s;
			} else if (instance instanceof ClauseInstanceJpaImpl) {
				final ClauseInstanceJpaImpl c = (ClauseInstanceJpaImpl) instance;
				Hibernate.initialize(c.getParagraphs());
				return c;
			}
		}
		return null;
	}
}