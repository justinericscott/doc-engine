package com.itgfirm.docengine.service.content;

import static com.itgfirm.docengine.DocEngine.Constants.*;
import static com.itgfirm.docengine.util.Utils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.itgfirm.docengine.repository.content.ContentRepository;
import com.itgfirm.docengine.types.ContentJpaImpl;

/**
 * @author Justin Scott
 * 
 *         Default implementation of the Content Service.
 */
@Service
@Qualifier(AUTOWIRE_QUALIFIER_DEFAULT)
class ContentServiceImpl implements ContentService {
	private static final Logger LOG = LoggerFactory.getLogger(ContentServiceImpl.class);

	@Autowired
	@Qualifier(AUTOWIRE_QUALIFIER_DEFAULT)
	private ContentRepository repo;

	@Override
	public final ContentJpaImpl save(final ContentJpaImpl content) {
		if (isNotNullOrEmpty(content)) {
			final String code = ((ContentJpaImpl) content).getContentCd();
			final ContentJpaImpl check = (ContentJpaImpl) repo.findByContentCd(code);
			if (!isNotNullOrEmpty(check)) {
				return repo.save(content);
			} else {
				LOG.warn("Content with this code {} already exists in the database!", code);
			}
		}
		return null;
	}

	@Override
	public final Iterable<? extends ContentJpaImpl> save(final Iterable<? extends ContentJpaImpl> contents) {
		if (isNotNullOrEmpty(contents)) {
			return repo.save(contents);
		}
		return null;
	}

	@Override
	public final ContentJpaImpl findOne(final Long id) {
		if (isNotNullOrZero(id)) {
			return repo.findOne(id);
		}
		return null;
	}

	@Override
	public final ContentJpaImpl findByContentCd(final String code) {
		if (isNotNullOrEmpty(code)) {
			return repo.findByContentCd(code);
		}
		return null;
	}

	@Override
	public final Iterable<? extends ContentJpaImpl> findByContentCdLike(final String like) {
		if (isNotNullOrEmpty(like)) {
			return repo.findByContentCdLike(like);
		}
		return null;
	}

	@Override
	public final Iterable<? extends ContentJpaImpl> findAll() {
		return repo.findAll();
	}

	@Override
	public final void delete(final Long id) {
		if (isNotNullOrZero(id)) {
			repo.delete(id);
		}
	}

	@Override
	public final void delete(final String code) {
		if (isNotNullOrEmpty(code)) {
			repo.delete(findByContentCd(code));
		}
	}

	@Override
	public final void delete(final ContentJpaImpl content) {
		if (isNotNullOrEmpty(content)) {
			repo.delete(content);
		}
	}

	@Override
	public final void delete(final Iterable<? extends ContentJpaImpl> contents) {
		if (isNotNullOrEmpty(contents)) {
			repo.delete(contents);
		}
	}

	@Override
	public void deleteAll() {
		repo.deleteAll();
	}

	@Override
	public final void deleteByContentCdLike(final String like) {
		if (isNotNullOrEmpty(like)) {
			repo.delete(repo.findByContentCdLike(like));
		}
	}
}