package com.github.justinericscott.docengine.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;

import com.github.justinericscott.docengine.util.Utils.TidyFactory;

import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.ObjectWrapper;
import freemarker.template.Version;

@org.springframework.context.annotation.Configuration
public class TemplateConfig {
	private static final Logger LOG = LoggerFactory.getLogger(TemplateConfig.class);	

	@Bean
	public Configuration getFreemarker() {
		LOG.trace("Creating new Configuration.");
		final Configuration config = new Configuration(version());
		config.setObjectWrapper(wrapper());
		config.setTemplateLoader(loader());
		config.setLogTemplateExceptions(true);
		return config;
	}
	
	@Bean
	public TidyFactory getTidyFactory() {
		return new TidyFactory();
	}
	
	private Version version() {
		return Configuration.VERSION_2_3_25;
	}

	private ObjectWrapper wrapper() {
		LOG.trace("Creating new DefaultObjectWrapper.");
		return new DefaultObjectWrapperBuilder(version()).build();
	}

	private TemplateLoader loader() {
		LOG.trace("Creating new StringTemplateLoader.");
		return new StringTemplateLoader();
	}	
}