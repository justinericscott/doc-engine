package com.github.justinericscott.docengine.config;

import static com.github.justinericscott.docengine.util.Utils.createDirectory;
import static com.github.justinericscott.docengine.util.Utils.get;
import static com.github.justinericscott.docengine.util.Utils.getSystemTempDirectory;
import static com.github.justinericscott.docengine.util.Utils.isNotNullAndExists;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;

import com.github.justinericscott.docengine.util.Utils.TidyFactory;

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
	private static final DocumentBuilderFactory DOC_BUILDER_FACTORY = getDocumentBuilderFactory();

	@Bean
	public DocumentBuilder getDocumentBuilder() {
		try {
			return DOC_BUILDER_FACTORY.newDocumentBuilder();
		} catch (final ParserConfigurationException e) {
			LOG.error("Problem obtaining a XML DocumentBuilder!");
		}
		return null;
	}

	@Bean
	public Configuration getFreemarker() {
		LOG.debug("Creating new Configuration.");
		final Configuration config = new Configuration(version());
		config.setObjectWrapper(wrapper());
		config.setTemplateLoader(loader());
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
		LOG.debug("Creating new DefaultObjectWrapper.");
		return new DefaultObjectWrapperBuilder(version()).build();
	}

	private TemplateLoader loader() {
		try {
			LOG.debug("Creating new MultiTemplateLoader to allow Strings and Files.");
			final File temp = getSystemTempDirectory();
			final File templates = createDirectory(temp, "Freemarker_Templates");
			final File internal = get("templates" + File.separator + "..");
			if (isNotNullAndExists(templates) && isNotNullAndExists(internal) && internal.isDirectory()) {
				final TemplateLoader[] loaders = { new StringTemplateLoader(), new FileTemplateLoader(templates), new FileTemplateLoader(internal) };
				final TemplateLoader loader = new MultiTemplateLoader(loaders);
				return loader;
			} else {
				if (!isNotNullAndExists(templates)) {
					LOG.warn("The templates directory in the system temporary directory does not exist!");
				} else if (!templates.isDirectory()) {
					LOG.warn("The templates directory in the system temporary directory is not a directory!");
				}
				if (!isNotNullAndExists(internal)) {
					LOG.warn("The internal templates directory does not exist!");
				} else if (!internal.isDirectory()) {
					LOG.warn("The internal templates directory is not a directory!");
				}				
			}
		} catch (final IOException e) {
			LOG.error("Problem loading MultiTemplateLoader!", e);
		}
		LOG.warn("Attempt to create new MultiTemplateLoader failed, attempting to create new StringTemplateLoader.");
		return new StringTemplateLoader();
	}
	
	private static DocumentBuilderFactory getDocumentBuilderFactory() {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);
		return factory;	
	}	
}