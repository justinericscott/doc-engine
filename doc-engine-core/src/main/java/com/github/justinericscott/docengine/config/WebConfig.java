package com.github.justinericscott.docengine.config;

import static com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module.Feature.*;
import static com.github.justinericscott.docengine.util.Utils.Constants.*;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

import java.util.Enumeration;
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
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Configuration
class WebConfig extends WebMvcConfigurerAdapter {
	private static final Logger LOG = LoggerFactory.getLogger(WebConfig.class);
	private static final String HTTPS = "https://";
	private static final String HTTP = "http://";
	private String endpoint;
	private String port;

	@Autowired
	private Environment _environment;

	@Autowired
	private ServerProperties _server;

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		LOG.debug("Adding Mapping Jackson 2 HTTP Message Converter...");
		converters.add(new MappingJackson2HttpMessageConverter(objectMapper()));
	}

	@Bean(AUTOWIRE_QUALIFIER_ENDPOINT)
	public String getEndpoint() {
		if (!isNotNullOrEmpty(this.endpoint)) {
			final StringBuilder endpoint = new StringBuilder();
			endpoint.append(getHTTPPrefix()).append(getURL()).append(getPort()).append(getContextPath());
			this.endpoint = endpoint.toString();
			LOG.debug("Found the endpoint {}.", this.endpoint);
		}
		return this.endpoint;
	}

	@Bean
	public ObjectMapper objectMapper() {
		final Hibernate5Module module = new Hibernate5Module();
		module.enable(SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS);
		return Jackson2ObjectMapperBuilder.json().failOnEmptyBeans(false).indentOutput(true).modulesToInstall(module)
				.build();
	}

	private String getContextPath() {
		final String path = _server.getContextPath();
		LOG.debug("Found the context path {}.", path);
		return path;
	}

	private String getHTTPPrefix() {
		if (_server.getSsl().isEnabled()) {
			return HTTPS;
		}
		return HTTP;
	}

	private String getPort() {
		if (isNotNullOrEmpty(port)) {
			LOG.debug("Found the port number {}.", port);
		} else {
			port = String.valueOf(_server.getPort());
			LOG.debug("Found the port number {}.", port);
			if (Integer.valueOf(port) <= 0) {
				port = _environment.getProperty("system.port");
				LOG.debug("Found the port number {}.", port);
				if (!isNotNullOrEmpty(port)) {
					port = _environment.getProperty("server.port");
					LOG.debug("Found the port number {}.", port);
				}
			}
		}
		return ":".concat(String.valueOf(port));
	}

	private String getURL() {
		try {
			final Enumeration<NetworkInterface> network = NetworkInterface.getNetworkInterfaces();
			String name = null;
			if (network.hasMoreElements()) {
				final Enumeration<InetAddress> addresses = network.nextElement().getInetAddresses();
				if (addresses.hasMoreElements()) {
					final InetAddress address = addresses.nextElement();
					final String n = address.getHostName();
					if (isNotNullOrEmpty(n)) {
						name = n;
						LOG.debug("Found host name: {}.", name);
					}
				}
			}
			if (isNotNullOrEmpty(name)) {
				return name;
			} else {
				throw new IllegalStateException("Could not determine host name!");
			}
		} catch (final SocketException e) {
			LOG.error("Problem obtaining host name!", e);
		}
		return null;
	}
}