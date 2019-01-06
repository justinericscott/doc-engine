package com.github.justinericscott.docengine.config;

import static com.github.justinericscott.docengine.util.Utils.Constants.*;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrZero;

import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Configuration
class WebConfig implements WebMvcConfigurer {
	private static final Logger LOG = LoggerFactory.getLogger(WebConfig.class);
	private static final String HTTPS = "https://";
	private static final String HTTP = "http://";
	private String endpoint;
	private Integer port;

	@Autowired
	private Environment _environment;

	@Autowired
	private ServerProperties _server;
	
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder()
                .indentOutput(true)
                .dateFormat(new SimpleDateFormat("yyyy-MM-dd"))
                .modulesToInstall(new ParameterNamesModule());
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }

	@Bean(AUTOWIRE_QUALIFIER_ENDPOINT)
	public String getEndpoint() {
		if (!isNotNullOrEmpty(this.endpoint)) {
			final StringBuilder endpoint = new StringBuilder();
			endpoint.append(getHTTPPrefix()).append(getURL()).append(getPort()).append(getContextPath());
			this.endpoint = endpoint.toString();
			LOG.trace("Found the endpoint {}.", this.endpoint);
		}
		return this.endpoint;
	}

	private String getContextPath() {
		final String path = _server.getServlet().getContextPath();
		LOG.trace("Found the context path {}.", path);
		return path;
	}

	private String getHTTPPrefix() {
		if (_server.getSsl().isEnabled()) {
			return HTTPS;
		}
		return HTTP;
	}

	private String getPort() {
		if (isNotNullOrZero(port)) {
			LOG.trace("Found the existing port number {}.", port);
		} else {
			port = _server.getPort();
			LOG.trace("Found the port number using _server.getPort() {}.", port);
			if (!isNotNullOrZero(port)) {
				String val = _environment.getProperty("system.port");
				if (isNotNullOrEmpty(val)) {
					port = Integer.valueOf(val);
					LOG.trace("Found the port number using property system.port {}.", port);					
				}
				if (!isNotNullOrZero(port)) {
					val = _environment.getProperty("server.port");
					if (isNotNullOrEmpty(val)) {
						port = Integer.valueOf(val);
						LOG.trace("Found the port number using property server.port {}.", port);						
					}
				}
			}
		}
		return ":".concat(String.valueOf(port));
	}

	private String getURL() {
		if (isNotNullOrEmpty(endpoint)) {
			LOG.trace("Found existing endpoint config {}.", endpoint);
		} else {
			String name = _environment.getProperty("system.address");
			if (isNotNullOrEmpty(name)) {
				LOG.trace("Found system address {}.", name);
				return name;
			}
		}
		return null;
	}
}