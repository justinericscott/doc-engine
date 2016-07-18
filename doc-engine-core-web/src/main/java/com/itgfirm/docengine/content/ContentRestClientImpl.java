package com.itgfirm.docengine.content;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.types.jpa.Contents;
import com.itgfirm.docengine.util.CoreWebConstants;
import com.itgfirm.docengine.util.Utils;
import com.itgfirm.docengine.web.AbstractRestClient;
import com.itgfirm.docengine.web.RestClient;
import com.itgfirm.docengine.web.RestUrls;

/**
 * @author Justin Scott REST Client for the Content Service, to be used internally only
 */
@Component(CoreWebConstants.QUALIFIER_REST_CLIENT_CONTENT)
class ContentRestClientImpl extends AbstractRestClient implements RestClient {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(ContentRestClientImpl.class);

	public ContentRestClientImpl() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get()
	 */
	@Override
	public List<?> get() {
		ResponseEntity<ContentJpaImpl[]> response =
				adhocGet(RestUrls.CONTENTS, ContentJpaImpl[].class);
		if (Utils.isNotNullOrEmpty(response)) {
			Content[] contents = null;
			contents = response.getBody();
			return Arrays.asList(contents);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.Long)
	 */
	@Override
	public Object get(Long id) {
		return adhocGet(RestUrls.CONTENT + RestUrls.BY_ID, ContentJpaImpl.class, id).getBody();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String)
	 */
	@Override
	public Object get(String code) {
		return adhocGet(RestUrls.CONTENT + RestUrls.BY_CODE, ContentJpaImpl.class, code)
				.getBody();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String, java.lang.String)
	 */
	@Override
	public Object get(String projectId, String code) {
		return get(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String, java.lang.String,
	 * java.lang.Class)
	 */
	@Override
	public Object get(String projectId, String code, Class<?> type) {
		return get(code, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String, java.lang.String,
	 * java.lang.Class, boolean)
	 */
	@Override
	public Object get(String projectId, String code, Class<?> type, boolean eagerKids) {
		return get(code, type, eagerKids);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getByCodeLike(java.lang.String,
	 * java.lang.Class)
	 */
	@Override
	public List<?> getByCodeLike(String like, Class<?> type) {
		if (type.equals(Content[].class)) {
			Content[] contents;
			contents =
					adhocGet(RestUrls.CONTENT + RestUrls.BY_CODE_LIKE, ContentJpaImpl[].class,
							like).getBody();
			if (Utils.isNotNullOrEmpty(contents)) {
				return Arrays.asList(contents);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getByProjectAndCodeLike( java.lang.String,
	 * java.lang.String, java.lang.Class)
	 */
	@Override
	public List<?> getByProjectAndCodeLike(String projectId, String like, Class<?> type) {
		return getByCodeLike(like, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#merge(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<?> merge(List<?> list) {
		List<Content> content = (List<Content>) list;
		return put(RestUrls.CONTENTS, new HttpEntity<Contents>(new Contents(content)),
				Contents.class).getBody().getContents();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#merge(java.lang.Object, java.lang.Class)
	 */
	@Override
	public Object merge(Object content, Class<?> type) {
		return put(RestUrls.CONTENT,
				new HttpEntity<ContentJpaImpl>((ContentJpaImpl) content),
				ContentJpaImpl.class).getBody();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#delete(java.lang.Object)
	 */
	@Override
	public void delete(Object object) {
		if (object instanceof Content) {
			Content content = (Content) object;
			super.delete(RestUrls.CONTENT + RestUrls.BY_ID, content.getId());
		}
	}

	/* (non-Javadoc)
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.Long, java.lang.Class)
	 */
	@Override
	public Object get(Long id, Class<?> type) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.Long, java.lang.Class, boolean)
	 */
	@Override
	public Object get(Long id, Class<?> type, boolean eagerKids) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String, java.lang.Class)
	 */
	@Override
	public Object get(String code, Class<?> type) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String, java.lang.Class, boolean)
	 */
	@Override
	public Object get(String code, Class<?> type, boolean eagerKids) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.itgfirm.docengine.web.RestClient#getChildren(java.lang.Long, java.lang.Class)
	 */
	@Override
	public List<?> getChildren(Long id, Class<?> type) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.itgfirm.docengine.web.RestClient#getChildren(java.lang.Long, java.lang.Class, boolean)
	 */
	@Override
	public List<?> getChildren(Long id, Class<?> type, boolean eagerKids) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.itgfirm.docengine.web.RestClient#getChildren(java.lang.String, java.lang.Class)
	 */
	@Override
	public List<?> getChildren(String code, Class<?> type) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.itgfirm.docengine.web.RestClient#getChildren(java.lang.String, java.lang.Class, boolean)
	 */
	@Override
	public List<?> getChildren(String code, Class<?> type, boolean eagerKids) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.itgfirm.docengine.web.RestClient#getChildren(java.lang.String, java.lang.String, java.lang.Class)
	 */
	@Override
	public List<?> getChildren(String projectId, String code, Class<?> type) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.itgfirm.docengine.web.RestClient#getChildren(java.lang.String, java.lang.String, java.lang.Class, boolean)
	 */
	@Override
	public List<?> getChildren(String projectId, String code, Class<?> type, boolean eagerKids) {
		// TODO Auto-generated method stub
		return null;
	}
}