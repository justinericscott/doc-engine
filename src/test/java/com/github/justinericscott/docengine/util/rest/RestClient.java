package com.github.justinericscott.docengine.util.rest;

import static com.github.justinericscott.docengine.util.Logs.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Rest Client Interface
 * 
 * TODO: Document
 * 
 * @author scottj
 *
 */
public interface RestClient {
	String AUTOWIRE_QUALIFIER_CONTENT = "content";
	String AUTOWIRE_QUALIFIER_INSTANCE = "instance";
	Logger LOG = LoggerFactory.getLogger(RestClient.class);

	/**
	 * TODO: Document
	 * 
	 * @param object
	 */
	void delete(Object object);

	/**
	 * TODO: Document
	 * 
	 * @return
	 */
	Object findAll();

	/**
	 * TODO: Document
	 * 
	 * @param type
	 * @return
	 */
	<T> T findAll(Class<T> type);
	
	<T> T findAll(Class<T> type, boolean eagerKids);

	default Object findByCode(String code) {
		LOG.error(LOG_NO_OP_BY_CODE);
		return null;
	}

	default <T> T findByCode(String code, Class<T> type) {
		LOG.error(LOG_NO_OP_BY_CODE);
		return null;
	}

	default <T> T findByCode(String code, Class<T> type, boolean eagerKids) {
		LOG.error(LOG_NO_OP_BY_CODE);
		return null;
	}

	default Object findByCodeLike(String like) {
		LOG.error(LOG_NO_OP_BY_CODE_LIKE);
		return null;
	}

	default <T> T findByCodeLike(String like, Class<T> type) {
		LOG.error(LOG_NO_OP_BY_CODE_LIKE);
		return null;
	}

	default Object findByProjectIdAndCode(String projectId, String code) {
		LOG.error(LOG_NO_OP_BY_PROJECT_ID_CODE);
		return null;
	}

	default <T> T findByProjectIdAndCode(String projectId, String code, Class<T> type) {
		LOG.error(LOG_NO_OP_BY_PROJECT_ID_CODE_TYPE);
		return null;
	}

	default <T> T findByProjectIdAndCode(String projectId, String code, Class<T> type, boolean eagerKids) {
		LOG.error(LOG_NO_OP_BY_PROJECT_ID_CODE_TYPE_KIDS);
		return null;
	}

	default <T> T findByProjectIdAndCodeLike(String projectId, String like, Class<T> type) {
		LOG.error(LOG_NO_OP_BY_PROJECT_ID_LIKE_TYPE);
		return null;
	}

	<T> T findOne(Long id);

	<T> T findOne(Long id, Class<T> type);

	<T> T findOne(Long id, Class<T> type, boolean eagerKids);

	default <T> T getChildren(Long id, Class<T> type) {
		LOG.error(LOG_NO_OP_CHILDREN_BY_ID_TYPE);
		return null;
	}

	default <T> T getChildren(Long id, Class<T> type, boolean eagerKids) {
		LOG.error(LOG_NO_OP_CHILDREN_BY_ID_TYPE_KIDS);
		return null;
	}

	default <T> T getChildren(String code, Class<T> type) {
		LOG.error(LOG_NO_OP_CHILDREN_BY_CODE_TYPE);
		return null;
	}

	default <T> T getChildren(String code, Class<T> type, boolean eagerKids) {
		LOG.error(LOG_NO_OP_CHILDREN_BY_CODE_TYPE_KIDS);
		return null;
	}

	default <T> T getChildren(String projectId, String code, Class<T> type) {
		LOG.error(LOG_NO_OP_CHILDREN_BY_PROJECT_ID_CODE_TYPE);
		return null;
	}

	default <T> T getChildren(String projectId, String code, Class<T> type, boolean eagerKids) {
		LOG.error(LOG_NO_OP_CHILDREN_BY_PROJECT_ID_CODE_TYPE_KIDS);
		return null;
	}

	<T> T save(T content, Class<T> type);
	
//	<T> T save(T content);
}