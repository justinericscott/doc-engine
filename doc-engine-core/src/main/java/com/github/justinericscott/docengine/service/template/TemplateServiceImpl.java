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
	private Configuration fm;

	TemplateServiceImpl() {
		// Default constructor for Spring
	}

	@Override
	public void load(final String name, final String template) {
		if (isNotNullOrEmpty(template) && isNotNullOrEmpty(name)) {
			final TemplateLoader loader = fm.getTemplateLoader();
			if (isNotNullOrEmpty(loader)) {
				if (loader instanceof MultiTemplateLoader) {
					final MultiTemplateLoader multi = (MultiTemplateLoader) loader;
					final TemplateLoader t = multi.getTemplateLoader(0);
					if (t instanceof StringTemplateLoader) {
						final StringTemplateLoader s = (StringTemplateLoader) t;
						s.putTemplate(name, template);
					}
				} else if (loader instanceof StringTemplateLoader) {
					final StringTemplateLoader s = (StringTemplateLoader) loader;
					s.putTemplate(name, template);
				}
			}
		} else {
			LOG.warn("The template name and/or template string must not be null or empty!");
		}
	}

	@Override
	public String run(final String name, final Map<String, Object> tokens) {
		if (isNotNullOrEmpty(name) && isNotNullOrEmpty(tokens)) {
			LOG.trace("Processing Template: {}\n", name);
			return process(name, tokens);
		} else {
			if (!isNotNullOrEmpty(name)) {
				final String error = "Template name must not be null/empty!";
				LOG.warn(error);
				return error;
			}
			if (!isNotNullOrEmpty(tokens)) {
				final String error = "Tokens must not be null/empty!";
				LOG.warn(error);
				return error;
			}
			return name;
		}
	}

	private String process(final String name, final Map<String, Object> tokens) {
		Template template = null;
		try {
			template = fm.getTemplate(name);
			if (isNotNullOrEmpty(template)) {
				final StringWriter writer = new StringWriter();
				template.process(tokens, writer);
				return writer.toString();
			} else {
				final String error = String.format("Template not found: %s!", name); 
				LOG.warn(error);
				return error;
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