package com.itgfirm.docengine.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.QueryException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.itgfirm.docengine.data.AbstractHibernateOrmRepo;
import com.itgfirm.docengine.data.DataConstants;
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.util.Utils;

/**
 * @author Justin Scott
 * 
 *         Default implementation of the Content Repository.
 */
@Repository @Qualifier("default")
class DefaultContentRepoImpl extends AbstractHibernateOrmRepo implements ContentRepo {
	private static final Logger LOG = LogManager.getLogger(DefaultContentRepoImpl.class);

	public DefaultContentRepoImpl() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.repo.ContentRepo#get()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Content> get() {
		LOG.trace("Attempting To Get All Content.");
		List<Content> contents = (List<Content>) super.get(ContentJpaImpl.class);
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
	@Override
	public Content get(Long id) {
		LOG.trace("Attempting To Get Content For ID: " + id);
		return (Content) super.get(ContentJpaImpl.class, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.repo.ContentRepo#get(java.lang.String)
	 */
	@Override
	public Content get(String code) {
		LOG.trace("Attempting To Get Content For Code: " + code);
		List<Content> contents =
				getWithQuery(DataConstants.GET_CONTENT_BY_CONTENT_CD,
						DataConstants.PARAM_CONTENT_CD, code);
		if (Utils.isNotNullOrEmpty(contents)) {
			return contents.get(0);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.repo.ContentRepo#getByCodeLike(java.lang.String)
	 */
	@Override
	public List<Content> getByCodeLike(String like) {
		if (Utils.isNotNullOrEmpty(like)) {
			LOG.trace("Attempting To Get Content For Code-Like: " + like);
			return getWithQuery(DataConstants.GET_CONTENTS_BY_CODE_LIKE,
					DataConstants.PARAM_CONTENT_CD, "%" + like + "%");
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentRepo#getWithQuery(java.lang.String,
	 * java.lang.String, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Content> getWithQuery(String query, String paramName, Object value) {
		LOG.trace("Attempting To Get Content With Query: " + query);
		try {
			return (List<Content>) super.getWithQuery(query, paramName, value);
		} catch (NoSuchElementException | IllegalArgumentException e) {
			LOG.error("Problem With Query.", e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentRepo#getWithQuery(java.lang.String,
	 * java.lang.String[], java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Content> getWithQuery(String query, String[] paramNames, Object[] values) {
		LOG.trace("Attempting To Get Content With Multiple Param Query: " + query);
		try {
			return (List<Content>) super.getWithQuery(query, paramNames, values);
		} catch (NoSuchElementException | IllegalArgumentException | QueryException e) {
			LOG.error("Problem With Query.", e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentRepo#merge(com.itgfirm.docengine
	 * .models.Content)
	 */
	@Override
	public Content merge(Content content) {
		if (Utils.isNotNullOrEmpty(content) && content.isValid()) {
			LOG.trace("Attempting To Merge Content: " + content.getContentCd());
			if (Utils.isNotNullOrZero(content.getId())) {
				LOG.trace("Attempting To Update Content: " + content.getContentCd());
				return (Content) super.merge(content);
			} else if (!Utils.isNotNullOrEmpty(get(content.getContentCd()))) {
				LOG.trace("Attempting To Insert Content: " + content.getContentCd());
				return (Content) super.merge(content);
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
	// @Override
	@SuppressWarnings("unused")
	private boolean deleteAll() {
		List<Content> content = get();
		if (Utils.isNotNullOrEmpty(content)) {
			LOG.trace("Attempting To Delete All Content.");
			super.deleteAll(content);
			content = get();
			if (Utils.isNotNullOrEmpty(content)) {
				String error = "Some Items Were NOT Deleted!!!\n";
				for (Content c : content) {
					error.concat(" ----- CONTENT ITEM WAS NOT DELETED: " + c.getContentCd()
							+ " !!!\n");
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
	@Override
	public boolean delete(Content content) {
		if (Utils.isNotNullOrEmpty(content)) {
			LOG.trace("Attempting To Delete Content: " + content.getContentCd());
			Long id = content.getId();
			super.delete(content);
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
	@Override
	public boolean deleteByCodeLike(String like) {
		List<Boolean> results = new ArrayList<Boolean>();
		List<Content> content = getByCodeLike(like);
		if (Utils.isNotNullOrEmpty(content)) {
			LOG.trace("Attempting To Delete Content For Code-Like: " + like);
			for (Content c : content) {
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