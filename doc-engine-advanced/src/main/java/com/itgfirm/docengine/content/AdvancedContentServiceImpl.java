package com.itgfirm.docengine.content;

import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.itgfirm.docengine.data.DataConstants;
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.jpa.AdvancedDocumentJpaImpl;
import com.itgfirm.docengine.types.jpa.ClauseJpaImpl;
import com.itgfirm.docengine.types.jpa.SectionJpaImpl;
import com.itgfirm.docengine.util.Utils;

/**
 * @author Justin Scott
 * 
 *         Default implementation of the Content Service.
 */
@Service @Qualifier("advanced")
class AdvancedContentServiceImpl extends DefaultContentServiceImpl implements AdvancedContentService {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(AdvancedContentServiceImpl.class);
	@Autowired @Qualifier("advanced")
	private AdvancedContentRepo repo;

	public AdvancedContentServiceImpl() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentService#get(java.lang.Long, boolean)
	 */
	@Override
	public Content get(Long id, boolean eagerKids) {
		if (eagerKids) {
			return repo.initialize(repo.get(id));
		} else {
			return repo.get(id);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentService#get(java.lang.String, boolean)
	 */
	@Override
	public Content get(String code, boolean eagerKids) {
		if (eagerKids) {
			return repo.initialize(repo.get(code));
		} else {
			return repo.get(code);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentService#getChildren( java.lang.Long,
	 * java.lang.Class)
	 */
	@Override
	public List<? extends Content> getChildren(Long id) {
		return getChildren(id, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentService#getChildren(java.lang.Long, boolean)
	 */
	@Override
	public List<? extends Content> getChildren(Long id, boolean eagerKids) {
		if (Utils.isNotNullOrZero(id)) {
			Content content = get(id);
			if (Utils.isNotNullOrEmpty(content)) {
				return getChildren(content, eagerKids);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentService#getChildren(java.lang.String)
	 */
	@Override
	public List<? extends Content> getChildren(String code) {
		return getChildren(code, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentService#getChildren(java.lang.String, boolean)
	 */
	@Override
	public List<? extends Content> getChildren(String code, boolean eagerKids) {
		if (Utils.isNotNullOrEmpty(code)) {
			Content content = get(code);
			return getChildren(content, eagerKids);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentService#getChildren(
	 * com.itgfirm.docengine.types.jpa.jpa.Content)
	 */
	@Override
	public List<? extends Content> getChildren(Content content) {
		return getChildren(content, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.itgfirm.docengine.content.ContentService#getChildren(com.itgfirm.docengine.types
	 * .jpa.jpa.Content, boolean)
	 */
	@Override
	public List<? extends Content> getChildren(Content content, boolean eagerKids) {
		List<? extends Content> children = null;
		if (content.getClass().equals(AdvancedDocumentJpaImpl.class)) {
			children =
					repo.getWithQuery(DataConstants.GET_SECTIONS_BY_PARENT,
							DataConstants.PARAM_DOCUMENT, content);
		} else if (content.getClass().equals(SectionJpaImpl.class)) {
			children =
					repo.getWithQuery(DataConstants.GET_CLAUSES_BY_PARENT,
							DataConstants.PARAM_SECTION, content);
		} else if (content.getClass().equals(ClauseJpaImpl.class)) {
			children =
					repo.getWithQuery(DataConstants.GET_PARAGRAPHS_BY_PARENT,
							DataConstants.PARAM_CLAUSE, content);
		}
		if (eagerKids && Utils.isNotNullOrEmpty(children)) {
			@SuppressWarnings("unchecked")
			ListIterator<Content> iter = (ListIterator<Content>) children.listIterator();
			while (iter.hasNext()) {
				iter.set(repo.initialize(iter.next()));
			}
		}
		return children;
	}
}