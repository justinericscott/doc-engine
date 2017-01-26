package com.github.justinericscott.docengine.service.template;

import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Service
class TemplateServiceImpl implements TemplateService {
	private static final Logger LOG = LoggerFactory.getLogger(TemplateServiceImpl.class);

	@Autowired
	private Configuration config;

	TemplateServiceImpl() {
		// Default constructor for Spring
	}

	@Override
	public void loadTemplate(final String name, final String template) {
		if (isNotNullOrEmpty(template) && isNotNullOrEmpty(name)) {
			final TemplateLoader loader = config.getTemplateLoader();
			if (isNotNullOrEmpty(loader)) {
				StringTemplateLoader string = null;
				if (loader instanceof MultiTemplateLoader) {
					final MultiTemplateLoader multi = (MultiTemplateLoader) loader;
					final TemplateLoader first = multi.getTemplateLoader(0);
					if (first instanceof StringTemplateLoader) {
						string = (StringTemplateLoader) first;
					}
				} else if (loader instanceof StringTemplateLoader) {
					string = (StringTemplateLoader) loader;
				}
				if (isNotNullOrEmpty(string)) {
					string.putTemplate(name, template);
				} else {
					LOG.debug("No StringTemplateLoader available!");
				}
			}
		} else {
			LOG.debug("The template name and/or template string must not be null or empty!");
		}
	}

	@Override
	public String process(final String name, final Map<String, Object> tokens) {
		if (isNotNullOrEmpty(name) && isNotNullOrEmpty(tokens)) {
			LOG.debug("Processing Template: {}\n", name);
			return processTemplate(name, tokens);
		} else {
			if (!isNotNullOrEmpty(name)) {
				final String error = "Template name must not be null/empty!";
				LOG.debug(error);
				return error;
			}
			if (!isNotNullOrEmpty(tokens)) {
				final String error = "Tokens must not be null/empty!";
				LOG.debug(error);
				return error;
			}
			return name;
		}
	}

	private String processTemplate(final String name, final Map<String, Object> tokens) {
		Template template = null;
		try {
			template = config.getTemplate(name);
			if (isNotNullOrEmpty(template)) {
				final StringWriter writer = new StringWriter();
				template.process(tokens, writer);
				return writer.toString();
			} else {
				LOG.debug("Template must not be null or empty!");
				return null;
			}
		} catch (final TemplateException | IOException e) {
			final StringBuilder sb = new StringBuilder();
			tokens.forEach((key, token) -> {
				sb.append(String.format("TOKEN NAME: %s === VALUE: %s\n", key, token.toString()));
			});
			if (isNotNullOrEmpty(template)) {
				final String error = String.format(
						"Problem Processing Template:\n ---- OFFENDING TEMPLATE: %s\n ---- OFFENDING TOKENS: %s",
						template.toString(), sb.toString());
				LOG.error(error, e);
				return String.format("%s\n ---- EXCEPTION: %s", error, e.getMessage());
			} else {
				final String error = String.format("Problem Processing Template: %s\n ---- OFFENDING TOKENS: %s", name,
						sb.toString());
				LOG.error(error, e);
				return String.format("%s\n ---- EXCEPTION: %s", error, e.getMessage());
			}
		}
	}
}