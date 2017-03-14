package com.github.justinericscott.docengine.controller;

import static com.github.justinericscott.docengine.util.Utils.Constants.*;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;
import static org.springframework.http.HttpStatus.*;

import java.io.UnsupportedEncodingException;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

@Component
public class RestUtils {
	private static final Logger LOG = LoggerFactory.getLogger(RestUtils.class);

	@Autowired
	@Qualifier(AUTOWIRE_QUALIFIER_ENDPOINT)
	private String endpoint;

	public RestUtils() {

	}

	public static <T> ResponseEntity<T> createResponseForBadRequest(final URI uri, final T body) {
		return ResponseEntity.badRequest().location(uri).body(body);
	}

	public static <T> ResponseEntity<T> createResponseForInternalServerError(final URI uri, final T body) {
		return ResponseEntity.status(INTERNAL_SERVER_ERROR).location(uri).body(body);
	}

	public static <T> ResponseEntity<T> createResponseForNoContent(final URI uri, final T body) {
		return ResponseEntity.status(NO_CONTENT).body(body);
	}

	public static ResponseEntity<Void> createResponseForNoContent(final URI uri) {
		return ResponseEntity.status(NO_CONTENT).location(uri).build();
	}

	public static <T> ResponseEntity<T> createResponseForSuccess(final T object) {
		return ResponseEntity.ok(object);
	}

	public final String getDestination(final String base) {
		return getDestination(base, null);
	}

	public final String getDestination(final String base, final String relative) {
		if (isNotNullOrEmpty(endpoint)) {
			if (isNotNullOrEmpty(base)) {
				if (isNotNullOrEmpty(relative)) {
					return endpoint.concat(base).concat(relative);
				} else {
					return endpoint.concat(base);
				}
			} else {
				LOG.warn("The class base mapping does not have a value!");
			}
		} else {
			LOG.warn("The endpoint does not have a value!");
		}
		return null;
	}

	public final URI getURI(final String dest) {
		if (isNotNullOrEmpty(dest)) {
			try {
				return URI.create(UriUtils.encode(dest, "UTF-8"));
			} catch (final UnsupportedEncodingException e) {
				LOG.error("Problem encoding the intended destination to URI.", e);
			}
		}
		return null;
	}

	public final URI getURI(final String base, final String relative) {
		return getURI(getDestination(base, relative));
	}

	public static class RestConstants {

//		public static final String VIEW_RESOLVER_PREFIX = "/static/html/";
//		public static final String VIEW_RESOLVER_SUFFIX = ".html";

		public static final String ROOT = "/";

		/** CLASSES **/
		public static final String CONTENT = "/content";
		public static final String CONTENTS = "/contents";
		public static final String DOCUMENT = "/document";
		public static final String DOCUMENTS = "/documents";
		public static final String SECTION = "/section";
		public static final String CLAUSE = "/clause";
		public static final String PARAGRAPH = "/paragraph";
		public static final String INSTANCE = "/instance";
		public static final String INSTANCES = "/instances";

		public static final String TOKEN = "/token";
		public static final String TOKENS = "/tokens";

		/** METHODS **/
		public static final String CREATE = "/create";
		public static final String COMPILE = "/compile";
		public static final String FIND_ALL = "/all";
		public static final String CHILDREN = "/children";

		/** FIELDS **/
		public static final String PARAM_ID = "id";
		public static final String PARAM_CODE = "code";
		public static final String PARAM_EAGER_KIDS = "eager";
		public static final String PARAM_LIKE = "like";
		public static final String PARAM_PROJECT_ID = "projectId";
		public static final String PARAM_TYPE = "type";

		/** DISCRIMINATORS **/
		public static final String BY_ID = "/" + PARAM_ID + "/{" + PARAM_ID + "}";
		public static final String BY_CODE = "/" + PARAM_CODE + "/{" + PARAM_CODE + "}";
		public static final String BY_CODE_LIKE = "/" + PARAM_LIKE + "/{" + PARAM_LIKE + "}";
		public static final String BY_PROJECT_ID = "/" + PARAM_PROJECT_ID + "/{" + PARAM_PROJECT_ID + "}";
		public static final String BY_TYPE = "/" + PARAM_TYPE + "/{" + PARAM_TYPE + "}";
		public static final String IS_EAGER_KIDS = "/" + PARAM_EAGER_KIDS + "/{" + PARAM_EAGER_KIDS + "}";

		/** OTHER **/
		public static final String USER = "/user";
	}
}