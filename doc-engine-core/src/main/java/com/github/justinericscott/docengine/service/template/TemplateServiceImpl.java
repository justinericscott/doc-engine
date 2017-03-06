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
	public String get(final String name) {
		return null;
	}

	@Override
	public void load(final String name, final String ftl) {
		if (isNotNullOrEmpty(ftl) && isNotNullOrEmpty(name)) {
			final TemplateLoader loader = fm.getTemplateLoader();
			if (isNotNullOrEmpty(loader)) {
				if (loader instanceof MultiTemplateLoader) {
					final MultiTemplateLoader multi = (MultiTemplateLoader) loader;
					final TemplateLoader t = multi.getTemplateLoader(0);
					if (t instanceof StringTemplateLoader) {
						final StringTemplateLoader s = (StringTemplateLoader) t;
						final Object existing = s.findTemplateSource(name);
						if (isNotNullOrEmpty(existing)) {
							LOG.warn("Replacing existing string template {} with new body: \n{}", name, ftl);
						}
						s.putTemplate(name, ftl);
					}
				} else if (loader instanceof StringTemplateLoader) {
					final StringTemplateLoader s = (StringTemplateLoader) loader;
					final Object existing = s.findTemplateSource(name);
					if (isNotNullOrEmpty(existing)) {
						LOG.warn("Replacing existing string template {} with new body: \n{}", name, ftl);
					}
					s.putTemplate(name, ftl);
				}
			}
		} else {
			LOG.warn("The template name and/or template string must not be null or empty!");
		}
	}

	@Override
	public String process(final String name, final Map<String, Object> tokens) {
		if (isNotNullOrEmpty(name) && isNotNullOrEmpty(tokens)) {
			LOG.trace("Processing Template: {}\n", name);
			return proc(name, tokens);
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

	@Override
	public String once(final String ftl, final Map<String, Object> tokens) {
		return null;
	}
	
	private String proc(final String name, final Map<String, Object> tokens) {
		Template template = null;
		try {
			template = fm.getTemplate(name);
			if (isNotNullOrEmpty(template)) {
				LOG.trace("Found template: {}.", name);
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
			sb.append(String.format("Problem Processing Template: %s\r\n", name));
			if (isNotNullOrEmpty(template)) {
				sb.append(String.format("---- TEMPLATE: %s\r\n", template.toString()));
			} else {
				sb.append("---- TEMPLATE NOT LOADED!\r\n");
			}
			if (isNotNullOrEmpty(tokens)) {
				sb.append("---- TOKENS:\r\n");
				tokens.forEach((key, token) -> {
					sb.append(String.format("-- %s : %s\r\n", key, token.toString()));
				});				
			}
			sb.append(String.format("---- EXCEPTION:\r\n%s", e.getMessage().replace("\n", "\r\n")));
			final String error = sb.toString();
			LOG.error(error, e);
			return error;
		}
	}
}