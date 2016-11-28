package com.itgfirm.docengine.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.hibernate.QueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.itgfirm.docengine.data.AbstractHibernateOrmRepo;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.util.Utils;

/**
 * @author Justin Scott
 * 
 *         Default implementation of the Content Repository.
 */
// @Repository
// @Qualifier("default")
@Deprecated
class DefaultContentRepoImpl extends AbstractHibernateOrmRepo {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultContentRepoImpl.class);

	public DefaultContentRepoImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.repo.ContentRepo#get()
	 */
	@SuppressWarnings("unchecked")
	public List<ContentJpaImpl> get() {
		LOG.trace("Attempting To Get All Content.");
		List<ContentJpaImpl> contents = (List<ContentJpaImpl>) super.get(ContentJpaImpl.class);
		if (Utils.isNotNullOrEmpty(contents)) {
			int size = contents.size();
			LOG.trace("Found " + size + " Templates In Content Repo.");
			if (size > 0) {
				Collections.sort(contents);
				return contents;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.repo.ContentRepo#get(java.lang.Long)
	 */
	public ContentJpaImpl get(Long id) {
		LOG.trace("Attempting To Get Content For ID: " + id);
		return (ContentJpaImpl) super.get(ContentJpaImpl.class, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.repo.ContentRepo#get(java.lang.String)
	 */
	public ContentJpaImpl get(String code) {
		LOG.trace("Attempting To Get Content For Code: " + code);
////		List<ContentJpaImpl> contents = getWithQuery(DataConstants.GET_CONTENT_BY_CONTENT_CD,
////				DataConstants.PARAM_CONTENT_CD, code);
//		if (Utils.isNotNullOrEmpty(contents)) {
//			return contents.get(0);
//		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.itgfirm.docengine.repo.ContentRepo#getByCodeLike(java.lang.String)
	 */
	public List<ContentJpaImpl> getByCodeLike(String like) {
		if (Utils.isNotNullOrEmpty(like)) {
			LOG.trace("Attempting To Get Content For Code-Like: " + like);
//			return getWithQuery(DataConstants.GET_CONTENTS_BY_CODE_LIKE, DataConstants.PARAM_CONTENT_CD,
//					"%" + like + "%");
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.itgfirm.docengine.content.ContentRepo#getWithQuery(java.lang.String,
	 * java.lang.String, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public List<ContentJpaImpl> getWithQuery(String query, String paramName, Object value) {
		LOG.trace("Attempting To Get Content With Query: " + query);
		try {
			return (List<ContentJpaImpl>) super.getWithQuery(query, paramName, value);
		} catch (NoSuchElementException | IllegalArgumentException e) {
			LOG.error("Problem With Query.", e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.itgfirm.docengine.content.ContentRepo#getWithQuery(java.lang.String,
	 * java.lang.String[], java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	public List<ContentJpaImpl> getWithQuery(String query, String[] paramNames, Object[] values) {
		LOG.trace("Attempting To Get Content With Multiple Param Query: " + query);
		try {
			return (List<ContentJpaImpl>) super.getWithQuery(query, paramNames, values);
		} catch (NoSuchElementException | IllegalArgumentException | QueryException e) {
			LOG.error("Problem With Query.", e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.itgfirm.docengine.content.ContentRepo#merge(com.itgfirm.docengine
	 * .models.Content)
	 */
	public ContentJpaImpl merge(ContentJpaImpl content) {
		if (Utils.isNotNullOrEmpty(content) && content.isValid()) {
			LOG.trace("Attempting To Merge Content: " + content.getContentCd());
			if (Utils.isNotNullOrZero(content.getId())) {
				LOG.trace("Attempting To Update Content: " + content.getContentCd());
				return (ContentJpaImpl) super.merge(content);
			} else if (!Utils.isNotNullOrEmpty(get(content.getContentCd()))) {
				LOG.trace("Attempting To Insert Content: " + content.getContentCd());
				return (ContentJpaImpl) super.merge(content);
			} else {
				LOG.trace("Content Already Exists: " + content.getContentCd());
			}
		} else {
			LOG.error("Content Must Not Be Null!");
		}
		return content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentRepo#deleteAll()
	 */
	@SuppressWarnings("unused")
	private boolean deleteAll() {
		List<ContentJpaImpl> content = get();
		if (Utils.isNotNullOrEmpty(content)) {
			LOG.trace("Attempting To Delete All Content.");
			// super.deleteAll(content);
			content = get();
			if (Utils.isNotNullOrEmpty(content)) {
				String error = "Some Items Were NOT Deleted!!!\n";
				for (ContentJpaImpl c : content) {
					error.concat(" ----- CONTENT ITEM WAS NOT DELETED: " + c.getContentCd() + " !!!\n");
				}
				LOG.trace(error);
				return false;
			} else {
				LOG.trace("All items deleted as expected.");
				return true;
			}
		} else {
			LOG.trace("Nothing to delete.");
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentRepo#delete(
	 * com.itgfirm.docengine.types.jpa.jpa.Content)
	 */
	public boolean delete(ContentJpaImpl content) {
		if (Utils.isNotNullOrEmpty(content)) {
			LOG.trace("Attempting To Delete Content: " + content.getContentCd());
			Long id = content.getId();
			// super.delete(content);
			content = get(id);
			if (Utils.isNotNullOrEmpty(content)) {
				LOG.trace("Item Was NOT Deleted: " + content.getContentCd());
			} else {
				LOG.trace("Item Deleted As Expected.");
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.repo.ContentRepo#deleteByCodeLike()
	 */
	public boolean deleteByCodeLike(String like) {
		List<Boolean> results = new ArrayList<Boolean>();
		List<ContentJpaImpl> content = getByCodeLike(like);
		if (Utils.isNotNullOrEmpty(content)) {
			LOG.trace("Attempting To Delete Content For Code-Like: " + like);
			for (ContentJpaImpl c : content) {
				results.add(delete(c));
			}
			if (results.contains(false)) {
				LOG.trace("Some Items Were NOT Deleted!!!");
				return false;
			} else {
				LOG.trace("All Items Deleted As Expected.");
				return true;
			}
		} else {
			LOG.trace("Nothing To Delete For Provided String: " + like);
			return true;
		}
	}
}