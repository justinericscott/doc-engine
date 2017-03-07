package com.github.justinericscott.docengine.service.template;

import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
class TemplateServiceImpl implements TemplateService {
	private static final Logger LOG = LoggerFactory.getLogger(TemplateServiceImpl.class);
	public static final String TEMP_NAME = "TEMP_NAME";

	@Autowired
	private Configuration fm;
	private StringTemplateLoader stl;

	TemplateServiceImpl() {
		// Default constructor for Spring
	}

	@Override
	public String adhoc(final String ftl, final Map<String, Object> tokens) {
		if (isNotNullOrEmpty(ftl)) {
			final String name = TEMP_NAME;
			load(name, ftl);
			final String html = run(name, tokens);
			remove(name);
			if (isNotNullOrEmpty(html)) {
				return html;
			}
		} else {
			LOG.warn("Template string must not be null!");
		}
		return null;
	}

	@Override
	public Template get(final String name) {
		if (isNotNullOrEmpty(name)) {
			final Object source = stl.findTemplateSource(name);
			if (isNotNullOrEmpty(source)) {
				final Reader reader = stl.getReader(source, null);
				if (isNotNullOrEmpty(reader)) {
					try {
						final Template template = new Template(name, reader, fm);
						if (isNotNullOrEmpty(template)) {
							return template;
						}
					} catch (final IOException e) {
						final Template template = buildErrorTemplate(name, null, e);
						LOG.error(buildError(template, null, e));
						return template;
					}
				}
			}
		} else {
			LOG.warn("The template name must not be null or empty!");
		}
		return null;
	}

	@Override
	public void load(final String name, final String ftl) {
		load(name, ftl, false);
	}

	@Override
	public void load(final String name, final String ftl, final boolean overwrite) {
		if (isNotNullOrEmpty(ftl) && isNotNullOrEmpty(name)) {
			if (isNotNullOrEmpty(get(name))) {
				if (overwrite) {
					LOG.trace("Replacing existing string template {} with new body: \n{}", name, ftl);
					stl.putTemplate(name, ftl);
				} else {
					LOG.trace("Existing string template {} with body will not be overwritten: \n{}", name, ftl);
				}
			} else {
				LOG.trace("Adding string template {} with body: \n{}", name, ftl);
				stl.putTemplate(name, ftl);
			}
		} else {
			LOG.warn("The template name and/or template string must not be null or empty!");
		}
	}

	@Override
	public void remove(final String name) {
		if (isNotNullOrEmpty(name) && isNotNullOrEmpty(get(name))) {
			try {
				LOG.trace("Attempting to remove template: {}", name);
				stl.removeTemplate(name);
				fm.removeTemplateFromCache(name);
			} catch (final IOException e) {
				LOG.error(String.format("Problem removing template: %s!", name), e);
			}
		}
	}

	@Override
	public String run(final String name, final Map<String, Object> tokens) {
		if (isNotNullOrEmpty(name)) {
			LOG.trace("Processing Template: {}\n", name);
			return process(name, tokens);
		} else {
			if (!isNotNullOrEmpty(name)) {
				final String error = "Template name must not be null/empty!";
				LOG.warn(error);
				return error;
			}
			return name;
		}
	}

	@PostConstruct
	public void setup() {
		stl = (StringTemplateLoader) fm.getTemplateLoader();
	}

	private String process(final String name, final Map<String, Object> tokens) {
		Template template = null;
		try {
			template = get(name);
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
			final String error = buildError(template, tokens, e);
			LOG.error(error, e);
			return error;
		}
	}

	private String buildError(final Template template, final Map<String, Object> tokens, final Exception e) {
		final StringBuilder sb = new StringBuilder();
		if (isNotNullOrEmpty(template)) {
			sb.append(String.format("Problem Processing Template: %s\r\n", template.getName()));
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
		if (isNotNullOrEmpty(e)) {
			sb.append(String.format("---- EXCEPTION:\r\n%s", e.getMessage().replace("\n", "\r\n")));
		}
		return sb.toString();
	}

	private Template buildErrorTemplate(final String name, final Map<String, Object> tokens, final Exception e) {
		if (isNotNullOrEmpty(name)) {
			final Reader reader = new StringReader(buildError(null, tokens, e));
			if (isNotNullOrEmpty(reader)) {
				try {
					final Template template = new Template(name, reader, fm);
					if (isNotNullOrEmpty(template)) {
						return template;
					}
				} catch (final IOException ex) {
					LOG.error(String.format("Problem creating error Template: %s", name), ex);
				}
			}
		}
		return null;
	}
}