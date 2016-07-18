package com.itgfirm.docengine.document;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.itgfirm.docengine.util.AdvancedWebConstants;
import com.itgfirm.docengine.web.AbstractRestClient;
import com.itgfirm.docengine.web.RestClient;

/**
 * @author Justin Scott
 * 
 *         REST Client for the Content Service, to be used internally only
 */
@Component(AdvancedWebConstants.NAMED_DOCUMENT_REST_CLIENT)
class DocumentRestClientImpl extends AbstractRestClient implements RestClient {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(DocumentRestClientImpl.class);

	public DocumentRestClientImpl() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get()
	 */
	@Override
	public List<?> get() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.Long)
	 */
	@Override
	public Object get(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String)
	 */
	@Override
	public Object get(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.Long, java.lang.Class)
	 */
	@Override
	public Object get(Long id, Class<?> type) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.Long, java.lang.Class, boolean)
	 */
	@Override
	public Object get(Long id, Class<?> type, boolean eagerKids) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String, java.lang.Class)
	 */
	@Override
	public Object get(String code, Class<?> type) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String, java.lang.Class,
	 * boolean)
	 */
	@Override
	public Object get(String code, Class<?> type, boolean eagerKids) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String, java.lang.String)
	 */
	@Override
	public Object get(String projectId, String code) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String, java.lang.String,
	 * java.lang.Class)
	 */
	@Override
	public Object get(String projectId, String code, Class<?> type) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String, java.lang.String,
	 * java.lang.Class, boolean)
	 */
	@Override
	public Object get(String projectId, String code, Class<?> type, boolean eagerKids) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getByCodeLike(java.lang.String,
	 * java.lang.Class)
	 */
	@Override
	public List<?> getByCodeLike(String like, Class<?> type) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getByProjectAndCodeLike(java.lang.String,
	 * java.lang.String, java.lang.Class)
	 */
	@Override
	public List<?> getByProjectAndCodeLike(String projectId, String like, Class<?> type) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getChildren(java.lang.Long, java.lang.Class)
	 */
	@Override
	public List<?> getChildren(Long id, Class<?> type) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getChildren(java.lang.Long, java.lang.Class,
	 * boolean)
	 */
	@Override
	public List<?> getChildren(Long id, Class<?> type, boolean eagerKids) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getChildren(java.lang.String, java.lang.Class)
	 */
	@Override
	public List<?> getChildren(String code, Class<?> type) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getChildren(java.lang.String, java.lang.Class,
	 * boolean)
	 */
	@Override
	public List<?> getChildren(String code, Class<?> type, boolean eagerKids) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getChildren(java.lang.String,
	 * java.lang.String, java.lang.Class)
	 */
	@Override
	public List<?> getChildren(String projectId, String code, Class<?> type) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getChildren(java.lang.String,
	 * java.lang.String, java.lang.Class, boolean)
	 */
	@Override
	public List<?> getChildren(String projectId, String code, Class<?> type, boolean eagerKids) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#merge(java.util.List)
	 */
	@Override
	public List<?> merge(List<?> list) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#merge(java.lang.Object, java.lang.Class)
	 */
	@Override
	public Object merge(Object object, Class<?> type) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#delete(java.lang.Object)
	 */
	@Override
	public void delete(Object object) {
		// TODO Auto-generated method stub

	}

}