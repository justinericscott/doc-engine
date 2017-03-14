package com.github.justinericscott.docengine.config;

import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;

import static org.kie.api.builder.Message.Level.*;
import static org.kie.api.builder.model.KieSessionModel.KieSessionType.*;
import static org.kie.api.conf.EqualityBehaviorOption.*;
import static org.kie.api.conf.EventProcessingOption.*;

import org.drools.core.ClockType;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.Results;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.spring.KModuleBeanFactoryPostProcessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogicConfig {
	private static final Logger LOG = LoggerFactory.getLogger(LogicConfig.class);
	private static final String KIE_BASE_NAME = "DocEngineKieBaseModel";
	private static final String KIE_STATELESS_SESSION_MODEL_NAME = "DocEngineKieStatelessSessionModel";
	private KieModuleModel module = null;
	private KieFileSystem fs = null;

	@Bean
	public static KModuleBeanFactoryPostProcessor processor() {
		return new KModuleBeanFactoryPostProcessor();
	}

	@Bean
	public KieContainer container() {
		LOG.trace("Creating new KieContainer.");
		module();
		build();
		return services().newKieContainer(release());
	}

	@Bean
	public KieFileSystem fileSystem() {
		if (!isNotNullOrEmpty(fs)) {
			LOG.trace("Creating new KieFileSystem.");
			fs = services().newKieFileSystem();
		}
		return fs;
	}

	@Bean
	public KieServices services() {
		LOG.trace("Getting KieServices...");
		return KieServices.Factory.get();
	}

	private void build() {
		LOG.trace("Creating new KieBuilder for stateless use.");
		final KieBuilder builder = services().newKieBuilder(fileSystem());
		final Results results = builder.getResults();
		if (results.hasMessages(INFO, WARNING, ERROR)) {
			results.getMessages(INFO, WARNING, ERROR).forEach(m -> {
				LOG.warn("Problem with KieBuilder!\nMessage Level: {}\nPath: {}\nLine: {}\nColumn: {}", m.getLevel(),
						m.getPath(), m.getLine(), m.getColumn());
			});
		}
	}

	private KieModuleModel module() {
		if (!isNotNullOrEmpty(module)) {
			LOG.trace("Creating new KieModuleModel.");
			module = services().newKieModuleModel();
			LOG.trace("Creating new KieBaseModel ({}) and KieSessionModel ({}) for stateless sessions.", KIE_BASE_NAME,
					KIE_STATELESS_SESSION_MODEL_NAME);
			module.newKieBaseModel(KIE_BASE_NAME).setEventProcessingMode(STREAM).setDefault(true)
					.setEqualsBehavior(EQUALITY).newKieSessionModel(KIE_STATELESS_SESSION_MODEL_NAME).setType(STATELESS)
					.setDefault(true).setClockType(ClockTypeOption.get(ClockType.REALTIME_CLOCK.name()));
		}
		return module;
	}

	private ReleaseId release() {
		LOG.trace("Creating new ReleaseId.");
		return services().getRepository().getDefaultReleaseId();
	}
}