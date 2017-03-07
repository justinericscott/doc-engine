package com.github.justinericscott.docengine.config;

import static org.kie.api.builder.Message.Level.*;
import static org.kie.api.builder.model.KieSessionModel.KieSessionType.*;
import static org.kie.api.conf.EqualityBehaviorOption.*;

import org.drools.core.ClockType;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.Results;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
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

	@Bean
	public static KModuleBeanFactoryPostProcessor processor() {
		return new KModuleBeanFactoryPostProcessor();
	}

	@Bean
	public KieContainer container() {
		LOG.trace("Creating new KieContainer.");
		builderForStateless();
		return services().newKieContainer(release());
	}

	@Bean
	public KieFileSystem fileSystem() {
		LOG.trace("Creating new KieFileSystem.");
		final KieFileSystem fs = services().newKieFileSystem();
		return fs;
	}

	@Bean
	public KieModuleModel module() {
		LOG.trace("Creating new KieModuleModel.");
		final KieModuleModel module = services().newKieModuleModel();
		return module;
	}

	@Bean
	public KieServices services() {
		LOG.trace("Getting KieServices...");
		return KieServices.Factory.get();
	}

	private KieBaseModel base() {
		LOG.trace("Creating new KieBaseModel: {}.", KIE_BASE_NAME);
		final KieBaseModel base = module().newKieBaseModel(KIE_BASE_NAME);
		base.setDefault(true);
		base.setEqualsBehavior(EQUALITY);
		return base;
	}

	private KieBuilder builderForStateless() {
		LOG.trace("Creating new KieBuilder for stateless use.");
		stateless();
		final KieBuilder builder = services().newKieBuilder(fileSystem());
		final Results results = builder.getResults();
		if (results.hasMessages(INFO, WARNING, ERROR)) {
			results.getMessages(INFO, WARNING, ERROR).forEach(m -> {
				LOG.warn("Problem with KieBuilder!\nMessage Level: {}\nPath: {}\nLine: {}\nColumn: {}", m.getLevel(),
						m.getPath(), m.getLine(), m.getColumn());
			});
		}
		return builder;
	}

	private ReleaseId release() {
		LOG.trace("Creating new ReleaseId.");
		return services().getRepository().getDefaultReleaseId();
	}

	private void stateless() {
		LOG.trace("Creating new KieSessionModel for stateless sessions.");
		final KieSessionModel session = base().newKieSessionModel(KIE_STATELESS_SESSION_MODEL_NAME);
		session.setType(STATELESS);
		session.setDefault(true);
		session.setClockType(ClockTypeOption.get(ClockType.REALTIME_CLOCK.name()));
	}
}