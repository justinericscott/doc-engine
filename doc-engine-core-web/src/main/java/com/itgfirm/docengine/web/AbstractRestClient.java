/**
 * TODO: License
 */
package com.itgfirm.docengine.web;

import java.util.Map;

import javax.persistence.MappedSuperclass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itgfirm.docengine.util.Constants;
import com.itgfirm.docengine.util.Utils;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@MappedSuperclass
public abstract class AbstractRestClient extends RestTemplate {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractRestClient.class);
	private static final String HTTPS = "https://";
	private static final String HTTP = "http://";
	@Autowired
	private MappingJackson2HttpMessageConverter json;

	@Value(value = Constants.PROPERTY_SYSTEM_URL)
	private String url;
	@Value(value = Constants.PROPERTY_SERVER_PORT)
	private String port;
	@Value(value = Constants.PROPERTY_SERVER_SSL_ENABLED)
	private Boolean isSecure;

	// @Value( value = Constants.PROPERTY_SERVER_CONTEXT )
	// private String contextPath;

	public void delete(String endpoint, Object... params) {
		super.delete(getEndpoint(endpoint), params);
	}

	protected <T> ResponseEntity<T> adhocGet(String endpoint, Class<T> type) {
		try {
			return getForEntity(getEndpoint(endpoint), type);
		} catch (HttpMessageNotReadableException e) {
			LOG.error("Problem Reading Response!", e);
			return null;
		}
	}

	protected <T> ResponseEntity<T> adhocGet(String endpoint, Class<T> type, Object... params) {
		try {
			return getForEntity(getEndpoint(endpoint), type, params);
		} catch (HttpMessageNotReadableException e) {
			LOG.error("PROBLEM!", e);
			return null;
		}
	}

	protected <T> ResponseEntity<T> adhocGet(String endpoint, Class<T> type,
			Map<String, String> params) {
		return getForEntity(getEndpoint(endpoint), type, params);
	}

	protected <T> ResponseEntity<T> put(String endpoint, HttpEntity<?> entity, Class<T> type) {
		return exchange(getEndpoint(endpoint), HttpMethod.PUT, entity, type);
	}

	public String writeValueAsString(Object object) {
		json.setPrettyPrint(true);
		String output = null;
		try {
			output = json.getObjectMapper().writer().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			output = e.getMessage();
		}
		json.setPrettyPrint(false);
		return output;
	}

	private String getEndpoint(String relativeUrl) {
		StringBuilder endpoint = new StringBuilder();
		endpoint.append(getHTTPPrefix() + getURL() + getPort() + getContextPath());
		if (Utils.isNotNullOrEmpty(relativeUrl)) {
			endpoint.append(relativeUrl);
		}
		return endpoint.toString();
	}

	private String getHTTPPrefix() {
		if (isSecure) {
			return HTTPS;
		} else {
			return HTTP;
		}
	}

	private String getURL() {
		if (Utils.isNotNullOrEmpty(url)) {
			return url;
		}
		return "";
	}

	private String getPort() {
		if (Utils.isNotNullOrEmpty(port)) {
			return ":" + port;
		}
		return "";
	}

	private String getContextPath() {
		// if (Utils.isNotNullOrEmpty(contextPath)) {
		// return contextPath;
		// }
		return "";
	}
}