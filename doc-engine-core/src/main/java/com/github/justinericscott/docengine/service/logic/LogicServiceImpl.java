package com.github.justinericscott.docengine.service.logic;

import static com.github.justinericscott.docengine.util.Utils.isNotNullAndExists;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;
import static org.kie.api.builder.Message.Level.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Results;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.internal.io.ResourceFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Service
class LogicServiceImpl implements LogicService {
	private static final Logger LOG = LoggerFactory.getLogger(LogicServiceImpl.class);
	private static final String DEFAULT_GLOBAL = "response";

	@Autowired
	private KieContainer container;

	@Autowired
	private KieFileSystem fs;

	@Autowired
	private KieServices services;

	public final void addResource(final File file) {
		if (isNotNullAndExists(file)) {
			fs.write(ResourceFactory.newFileResource(file));
			final Results results = services.newKieBuilder(fs).buildAll().getResults();
			if (results.hasMessages(INFO, WARNING, ERROR)) {
				results.getMessages(INFO, WARNING, ERROR).forEach(m -> {
					LOG.warn("Problem with KieBuilder!\nMessage Level: {}\nPath: {}\nLine: {}\nColumn: {}",
							m.getLevel(), m.getPath(), m.getLine(), m.getColumn());
				});
			}
			container = services.newKieContainer(container.getReleaseId());
		} else {
			LOG.warn("File must not be null and must exist!");
		}
	}

	public final Iterable<String> process(final Object tokens) {
		if (isNotNullOrEmpty(tokens)) {
			final ArrayList<String> response = new ArrayList<String>();
			final Object[] assets = { tokens, response };
			final StatelessKieSession session = container.newStatelessKieSession();
			session.setGlobal(DEFAULT_GLOBAL, response);
			session.execute(Arrays.asList(assets));
			if (isNotNullOrEmpty(response)) {
				return response;
			} else {
				LOG.debug("No response from logic service after execution.");
			}
		} else {
			LOG.warn("Tokens must not be null or empty!");
		}
		return null;
	}
}