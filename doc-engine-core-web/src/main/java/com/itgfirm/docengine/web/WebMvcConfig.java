package com.itgfirm.docengine.web;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.itgfirm.docengine.util.CoreWebConstants;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Configuration
public class WebMvcConfig extends WebMvcAutoConfigurationAdapter {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(WebMvcConfig.class);

	@Bean
	public InternalResourceViewResolver getInternalResourceViewResolver() {
		// TODO: Trace log entry
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix(CoreWebConstants.VIEW_RESOLVER_PREFIX);
		resolver.setSuffix(CoreWebConstants.VIEW_RESOLVER_SUFFIX);
		return resolver;
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		// TODO: Trace log entry
		converters.add(jacksonMessageConverter());
		super.configureMessageConverters(converters);
	}

	private MappingJackson2HttpMessageConverter jacksonMessageConverter() {
		// TODO: Trace log entry
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.registerModule(new Hibernate4Module());
		MappingJackson2HttpMessageConverter conv = new MappingJackson2HttpMessageConverter();
		conv.setObjectMapper(mapper);
		// conv.setPrefixJson(true);
		// conv.setJsonPrefix(")]}',\n");
		return conv;
	}
}