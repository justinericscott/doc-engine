/**
 * TODO: License
 */
package com.github.justinericscott.docengine.util.rest;

import static com.github.justinericscott.docengine.controller.RestUtils.createResponseForBadRequest;
import static com.github.justinericscott.docengine.controller.RestUtils.createResponseForInternalServerError;
import static com.github.justinericscott.docengine.controller.RestUtils.RestConstants.*;
import static com.github.justinericscott.docengine.util.Logs.*;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;

import static org.springframework.http.HttpMethod.*;

import java.util.Map;

import javax.persistence.MappedSuperclass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.github.justinericscott.docengine.controller.RestUtils;
import com.github.justinericscott.docengine.models.Content.Clause;
import com.github.justinericscott.docengine.models.Content.Document;
import com.github.justinericscott.docengine.models.Content.Paragraph;
import com.github.justinericscott.docengine.models.Content.Section;
import com.github.justinericscott.docengine.models.Contents;
import com.github.justinericscott.docengine.models.Contents.Clauses;
import com.github.justinericscott.docengine.models.Contents.Documents;
import com.github.justinericscott.docengine.models.Contents.Paragraphs;
import com.github.justinericscott.docengine.models.Contents.Sections;
import com.github.justinericscott.docengine.models.Instances;
import com.github.justinericscott.docengine.models.Instances.DocumentInstances;


/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@MappedSuperclass
public abstract class AbstractRestClient extends RestTemplate {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractRestClient.class);

	protected final String base;

	@Autowired
	protected RestUtils utils;

	public AbstractRestClient() {
		LOG.warn("New REST client created without adding a subject base path!");
		this.base = ROOT;
	}

	public AbstractRestClient(final String base) {
		LOG.debug("Adding {} as a subjective base path.", base);
		this.base = base;
	}

	@Override
	public void delete(String rel, Object... params) {
		if (rel != null) {
			try {
				super.delete(utils.getDestination(base, rel), params);
			} catch (final HttpServerErrorException e) {
				LOG.error(LOG_ERROR_DELETE, e);
			}
		} else {
			LOG.warn(LOG_NULL_REL_PATH);
		}
	}

	protected final <T> ResponseEntity<T> adhocGet(final Class<T> type) {
		return adhocGet(type, false);
	}

	protected final <T> ResponseEntity<T> adhocGet(final String rel, final Class<T> type) {
		return adhocGet(rel, type, (Object[]) null);
	}

	protected final <T> ResponseEntity<T> adhocGet(final Class<T> type, boolean eagerKids) {
		if (type.equals(Document.class) || type.equals(Documents.class)
				|| type.equals(DocumentInstances.class)) {
			if (eagerKids) {
				return adhocGet(DOCUMENT + DOCUMENTS + IS_EAGER_KIDS, type, eagerKids);
			}
			return adhocGet(DOCUMENT + DOCUMENTS, type);
		} else if (type.equals(Section.class) || type.equals(Sections.class)) {
			if (eagerKids) {
				return adhocGet(CONTENTS + BY_TYPE + IS_EAGER_KIDS, type, eagerKids);
			}
			return adhocGet(CONTENTS + BY_TYPE, type);
		} else if (type.equals(Clause.class) || type.equals(Clauses.class)) {
			if (eagerKids) {
				return adhocGet(CONTENTS + BY_TYPE + IS_EAGER_KIDS, type, eagerKids);
			}
			return adhocGet(CONTENTS + BY_TYPE, type);
		} else if (type.equals(Paragraph.class) || type.equals(Paragraphs.class)) {
			return adhocGet(CONTENTS + BY_TYPE, type);
		} else if (type.equals(Contents.class)) {
			return adhocGet(CONTENTS, type);
		} else if (type.equals(Instances.class)) {
			return adhocGet(INSTANCES, type);
		}
		return null;
	}

	protected final <T> ResponseEntity<T> adhocGet(final String rel, final Class<T> type, final Object... params) {
		final String dest = utils.getDestination(base, rel);
		if (isNotNullOrEmpty(type)) {
			try {
				if (isNotNullOrEmpty(params)) {
					LOG.trace("Sending request with params to {}...", dest);
					return super.getForEntity(dest, type, params);
				} else {
					LOG.trace("Sending request without params to {}...", dest);
					return super.getForEntity(dest, type);
				}
			} catch (final HttpMessageNotReadableException e) {
				final String log = String.format(LOG_ERROR_READ_JSON, dest, e.getMessage());
				LOG.error(log, e);
				try {
					final T body = type.newInstance();
					return createResponseForInternalServerError(utils.getURI(dest), body);
				} catch (InstantiationException | IllegalAccessException e1) {
					LOG.error("Problem creating new instance of T!");
				}
			}
		} else {
			LOG.warn(LOG_NULL_TYPE);
			try {
				final T body = type.newInstance();
				return createResponseForBadRequest(utils.getURI(dest), body);
			} catch (InstantiationException | IllegalAccessException e1) {
				LOG.error("Problem creating new instance of T!");
			}
		}
		return null;
	}

	protected final <T> ResponseEntity<?> adhocGet(final String rel, final Class<T> type, final Map<String, ?> params) {
		final String dest = utils.getDestination(base, rel);
		if (isNotNullOrEmpty(type)) {
			try {
				if (isNotNullOrEmpty(params)) {
					return super.getForEntity(dest, type, params);
				} else {
					return super.getForEntity(dest, type);
				}
			} catch (final HttpMessageNotReadableException e) {
				final String log = String.format(LOG_ERROR_READ_JSON, dest, e.getMessage());
				LOG.error(log, e);
				return createResponseForInternalServerError(utils.getURI(dest), type);
			}
		} else {
			LOG.warn(LOG_NULL_TYPE);
			return createResponseForBadRequest(utils.getURI(dest), LOG_NULL_TYPE);
		}
	}

	protected final <T> ResponseEntity<?> put(final HttpEntity<T> entity, final Class<T> type) {
		return put("", entity, type);
	}

	protected final <T> ResponseEntity<?> put(final String rel, final HttpEntity<T> entity, final Class<T> type) {
		final String dest = utils.getDestination(base, rel);
		if (isNotNullOrEmpty(type)) {
			if (isNotNullOrEmpty(entity)) {
				return super.exchange(dest, PUT, entity, type);
			} else {
				LOG.warn(LOG_NULL_HTTP_ENTITY);
				return createResponseForBadRequest(utils.getURI(dest), LOG_NULL_HTTP_ENTITY);
			}
		} else {
			LOG.warn(LOG_NULL_TYPE);
			return createResponseForBadRequest(utils.getURI(dest), LOG_NULL_TYPE);
		}
	}
}