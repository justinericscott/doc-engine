package com.itgfirm.docengine.template;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itgfirm.docengine.util.Utils;

import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapperBuilder;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

/**
 * 
 * @author Justin Scott
 * 
 *         TODO: Description
 */
//@Service
class TemplateServiceImpl implements TemplateService {
	private static final Logger LOG = LoggerFactory.getLogger(TemplateServiceImpl.class);
	private static final String DEFAULT_TEMPLATE_NAME = "default";

	private TemplateServiceImpl() {
	}

	public String process(String template, Map<String, Object> tokens) {
		if (Utils.isNotNullOrEmpty(template) && Utils.isNotNullOrEmpty(tokens)) {
			LOG.trace("Processing Template:\n" + template);
			String processed = processTemplate(getConfiguration(template), tokens);
			if (Utils.isNotNullOrEmpty(processed)) {
				return processed;
			}
		} else {
			if (Utils.isNotNullOrEmpty(template)) {
				LOG.warn("Template must not be null/empty!");
			}
			if (Utils.isNotNullOrEmpty(tokens)) {
				LOG.warn("Tokens must not be null/empty!");
			}
		}
		return template;
	}

	private boolean checkTemplate(Configuration config) {
		try {
			LOG.debug("Trying to get Template...");
			Template freemarker = config.getTemplate(DEFAULT_TEMPLATE_NAME);
			freemarker.toString(); // For exception handling only...
			LOG.debug("Found Template: " + freemarker);
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	private Configuration getConfiguration(String template) {
		Version version = new Version(2, 3, 22);
		Configuration config = new Configuration(version);
		config.setObjectWrapper(getObjectWrapper(version));
		config.setTemplateLoader(getLoadedTemplateLoader(template));
		if (!checkTemplate(config)) {
			LOG.error("Problem With Template\n ---- OFFENDING TEMPLATE:\n" + template);
		}
		return config;
	}

	private ObjectWrapper getObjectWrapper(Version version) {
		ObjectWrapper wrapper = new DefaultObjectWrapperBuilder(version).build();
		return wrapper;
	}

	private TemplateLoader getLoadedTemplateLoader(String template) {
		StringTemplateLoader loader = new StringTemplateLoader();
		loader.putTemplate(DEFAULT_TEMPLATE_NAME, template);
		return loader;
	}

	private String processTemplate(Configuration config, Map<String, Object> tokens) {
		String template = null;
		try {
			Template freemarker = config.getTemplate(DEFAULT_TEMPLATE_NAME);
			template = freemarker.toString(); // For exception handling only...
			StringWriter writer = new StringWriter();
			freemarker.process(tokens, writer);
			return writer.toString();
		} catch (TemplateException | IOException e) {
			if (tokens.size() > 0) {
				List<String> stringTokens = new ArrayList<String>(tokens.size());
				Iterator<Entry<String, Object>> iter = tokens.entrySet().iterator();
				while (iter.hasNext()) {
					Entry<String, Object> token = iter.next();
					stringTokens.add("TOKEN: " + token.getKey() + " === " + token.getValue().toString());
				}
				StringBuilder sb = new StringBuilder();
				for (String t : stringTokens) {
					sb.append(t + "\n");
				}
				if (template != null) {
					LOG.error("Problem Processing Template:\n ---- OFFENDING TEMPLATE:\n" + template
							+ " ---- OFFENRING TOKENS:\n" + sb.toString(), e);
					return template;
				} else {
					LOG.error(
							"Problem Processing Template! "
									+ "Cannot Obtain Template To Display!\n ---- OFFENDING TOKENS:\n" + sb.toString(),
							e);
				}
			} else {
				if (template != null) {
					LOG.error("Problem Processing Template! "
							+ "Cannot Obtain Tokens To Display!\n ---- OFFENDING TEMPLATE:\n" + template, e);
					return template;
				} else {
					LOG.error("Problem Processing Template! " + "Cannot Obtain Template And Tokens To Display!", e);
				}
			}
		} // StringWriter does not need to be closed.
		return template;
	}
}