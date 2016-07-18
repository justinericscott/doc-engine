package com.itgfirm.docengine.content;

import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.util.Utils;

/**
 * @author Justin Scott
 * 
 *         Default implementation of the Content Service.
 */
@Service @Qualifier("default")
class DefaultContentServiceImpl implements ContentService {
	private static final Logger LOG = LogManager.getLogger(DefaultContentServiceImpl.class);
	@Autowired @Qualifier("default")
	private ContentRepo repo;

	public DefaultContentServiceImpl() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentService#get()
	 */
	@Override
	public List<Content> get() {
		return repo.get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentService#get(java.lang.Long)
	 */
	@Override
	public Content get(Long id) {
		return repo.get(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentService#getByCode(java.lang .String)
	 */
	@Override
	public Content get(String code) {
		return repo.get(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentService#getByCodeLike(java. lang.String)
	 */
	@Override
	public List<Content> getByCodeLike(String like) {
		return repo.getByCodeLike(like);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentService#merge(java.lang.Object)
	 */
	@Override
	public Content merge(Content content) {
		return repo.merge(content);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentService#mergeAll(java.util.List)
	 */
	@Override
	public List<Content> merge(List<Content> contents) {
		if (Utils.isNotNullOrEmpty(contents)) {
			LOG.trace("Attempting To Merge List Of Contents: " + contents.size() + " Items.");
			ListIterator<Content> iter = contents.listIterator();
			while (iter.hasNext()) {
				iter.set(merge(iter.next()));
			}
			return contents;
		}
		LOG.trace("List Of Contents Must Not Be Null!");
		return contents;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentService#deleteById(java.lang.Long)
	 */
	@Override
	public boolean delete(Long id) {
		return delete(get(id));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentService#deleteByCode(java.lang.String )
	 */
	@Override
	public boolean delete(String code) {
		return delete(get(code));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentService#delete(
	 * com.itgfirm.docengine.types.jpa.jpa.Content)
	 */
	@Override
	public boolean delete(Content content) {
		return repo.delete(content);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentService#deleteContentCdLike(java .lang.String)
	 */
	@Override
	public boolean deleteByCodeLike(String like) {
		return repo.deleteByCodeLike(like);
	}
}