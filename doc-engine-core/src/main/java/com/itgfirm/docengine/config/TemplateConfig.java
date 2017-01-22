package com.itgfirm.docengine.config;

import static com.itgfirm.docengine.util.Utils.isNotNullAndExists;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;

import com.itgfirm.docengine.util.Utils;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
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
	public Configuration config() {
		LOG.debug("Creating new Configuration.");
		final Configuration config = new Configuration(version());
		config.setObjectWrapper(wrapper());
		config.setTemplateLoader(loader());
		return config;
	}

	private Version version() {
		return Configuration.VERSION_2_3_25;
	}

	private ObjectWrapper wrapper() {
		LOG.debug("Creating new DefaultObjectWrapper.");
		return new DefaultObjectWrapperBuilder(version()).build();
	}

	private TemplateLoader loader() {
		try {
			LOG.debug("Attempting to create new MultiTemplateLoader.");
			final File baseDir = Utils.get("");
			if (isNotNullAndExists(baseDir) && baseDir.isDirectory()) {
				final TemplateLoader[] loaders = { new StringTemplateLoader(), new FileTemplateLoader(baseDir) };
				final TemplateLoader loader = new MultiTemplateLoader(loaders);
				return loader;
			}
		} catch (final IOException e) {
			LOG.error("Problem loading FileTemplateLoader!", e);
		}
		LOG.debug("Attempt to create new MultiTemplateLoader failed, attempting to create new StringTemplateLoader.");
		return new StringTemplateLoader();
	}
}