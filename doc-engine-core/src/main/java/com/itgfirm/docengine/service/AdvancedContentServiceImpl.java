package com.itgfirm.docengine.service;

import static com.itgfirm.docengine.DocEngine.Constants.*;
import static com.itgfirm.docengine.util.Utils.*;

import org.hibernate.Hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itgfirm.docengine.repository.AdvancedContentRepository;
import com.itgfirm.docengine.types.jpa.AdvancedDocumentJpaImpl;
import com.itgfirm.docengine.types.jpa.ClauseJpaImpl;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.types.jpa.SectionJpaImpl;

/**
 * @author Justin Scott
 * 
 *         Default implementation of the Content Service.
 * @param <T>
 */
@Service
@Transactional(value = AUTOWIRE_QUALIFIER_ORM_TX) // Initializes children
@Qualifier(AUTOWIRE_QUALIFIER_ADVANCED)
class AdvancedContentServiceImpl extends ContentServiceImpl implements AdvancedContentService {

	@Autowired
	@Qualifier(AUTOWIRE_QUALIFIER_ADVANCED)
	private AdvancedContentRepository repo;
	
	@Override
	public final void deleteAll() {
		repo.deleteAll();
	}

	@Override
	public final ContentJpaImpl findByContentCd(final String code, final boolean eagerKids) {
		if (eagerKids) {
			return initialize(findByContentCd(code));
		} else {
			return findByContentCd(code);
		}
	}

	@Override
	public final ContentJpaImpl findOne(final Long id, final boolean eagerKids) {
		if (isNotNullOrZero(id)) {
			final ContentJpaImpl content = findOne(id);
			if (eagerKids) {
				return initialize(content);
			} else {
				return content;
			}
		}
		return null;
	}

	@Override
	public final Iterable<? extends ContentJpaImpl> getChildren(final Long id) {
		return getChildren(id, false);
	}

	@Override
	public final Iterable<? extends ContentJpaImpl> getChildren(final Long id, final boolean eagerKids) {
		return getChildren(findOne(id), eagerKids);
	}

	@Override
	public final Iterable<? extends ContentJpaImpl> getChildren(final String code) {
		return getChildren(code, false);
	}

	@Override
	public final Iterable<? extends ContentJpaImpl> getChildren(final String code, final boolean eagerKids) {
		return getChildren(findByContentCd(code), eagerKids);
	}

	@Override
	public final Iterable<? extends ContentJpaImpl> getChildren(final ContentJpaImpl content) {
		return getChildren(content, false);
	}

	@Override
	public final Iterable<? extends ContentJpaImpl> getChildren(final ContentJpaImpl content, boolean eagerKids) {
		if (isNotNullOrEmpty(content)) {
			Iterable<? extends ContentJpaImpl> children = null;
			if (content.getClass().equals(AdvancedDocumentJpaImpl.class)) {
				AdvancedDocumentJpaImpl advanced = (AdvancedDocumentJpaImpl) content;
				children = repo.findSectionsByParent(advanced);
			} else if (content.getClass().equals(SectionJpaImpl.class)) {
				SectionJpaImpl section = (SectionJpaImpl) content;
				children = repo.findClausesByParent(section);
			} else if (content.getClass().equals(ClauseJpaImpl.class)) {
				ClauseJpaImpl clause = (ClauseJpaImpl) content;
				children = repo.findParagraphsByParent(clause);
			}
			if (eagerKids && isNotNullOrEmpty(children)) {
				children.forEach(child -> {
					initialize(child);
				});
			}
			return children;
		}
		return null;
	}

	final ContentJpaImpl initialize(final ContentJpaImpl content) {
		if (isNotNullOrEmpty(content) && isNotNullOrZero(content.getId())) {
			if (content instanceof AdvancedDocumentJpaImpl) {
				final AdvancedDocumentJpaImpl d = (AdvancedDocumentJpaImpl) content;
				Hibernate.initialize(d.getSections());
				for (final SectionJpaImpl s : d.getSections()) {
					Hibernate.initialize(s.getClauses());
					for (final ClauseJpaImpl c : s.getClauses()) {
						Hibernate.initialize(c.getParagraphs());
					}
				}
				return d;
			} else if (content instanceof SectionJpaImpl) {
				final SectionJpaImpl s = (SectionJpaImpl) content;
				Hibernate.initialize(s.getClauses());
				for (final ClauseJpaImpl c : s.getClauses()) {
					Hibernate.initialize(c.getParagraphs());
				}
				return s;
			} else if (content instanceof ClauseJpaImpl) {
				final ClauseJpaImpl c = (ClauseJpaImpl) content;
				Hibernate.initialize(c.getParagraphs());
				return c;
			}
		}
		return null;
	}
}