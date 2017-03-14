package com.github.justinericscott.docengine.service.logic;

import static com.github.justinericscott.docengine.util.TestUtils.getFileFromClasspath;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;
import static org.junit.Assert.*;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message.Level;
import org.kie.api.builder.Results;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel.KieSessionType;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.internal.io.ResourceFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.justinericscott.docengine.pipeline.Project;
import com.github.justinericscott.docengine.service.logic.LogicService;
import com.github.justinericscott.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LogicServiceTest extends AbstractTest {
	private static final Logger LOG = LoggerFactory.getLogger(LogicServiceTest.class);
	private static final String EXPECTED = "Project Number Matches. Logic works!";
	private static final String LOGIC_TABLE = "logic/DroolsTesting.xls";
	private static final String PROJECT_NUMBER = "0NJ4321";

	@Autowired
	private LogicService service;

	@Test
	public void a_LoadTest() {
		final File file = getFileFromClasspath(LOGIC_TABLE);
		assertNotNull(file);
		assertTrue(file.exists());
		service.load(file);
		service.load(null);
	}
	
	@Test
	public void b_StatelessDecisionTableTest() {
		final File file = getFileFromClasspath(LOGIC_TABLE);
		assertNotNull(file);
		assertTrue(file.exists());
		service.load(file);
		final Project project = new Project();
		project.setProjectNumber(PROJECT_NUMBER);
		final Iterable<String> results = service.stateless(project);
		assertNotNull(results);
		assertTrue(results.iterator().hasNext());
		assertEquals(EXPECTED, results.iterator().next());
		project.setProjectNumber("NOPE");
		assertNull(service.stateless(project));
		service.load(null);
		assertNull(service.stateless(project));
		service.load(file);
		assertNull(service.stateless(null));
	}

	@Test
	public void x_NonSpringConfigTest() {
		final KieServices services = KieServices.Factory.get();

		final KieModuleModel module = services.newKieModuleModel();

		final String baseModelName = "DocEngineKieBaseModel";
		final String sessionName = "DocEngineKieStatelessSessionModel";
		module.newKieBaseModel(baseModelName).setDefault(true).setEqualsBehavior(EqualityBehaviorOption.EQUALITY)
				.setEventProcessingMode(EventProcessingOption.STREAM).newKieSessionModel(sessionName).setDefault(true)
				.setType(KieSessionType.STATELESS).setClockType(ClockTypeOption.get("realtime"));

		final File file = getFileFromClasspath(LOGIC_TABLE);
		assertNotNull(file);
		assertTrue(file.exists());
		final Resource resource = ResourceFactory.newFileResource(file);
		LOG.debug("Source Path: {}", resource.getSourcePath());
		LOG.debug("Target Path: {}", resource.getTargetPath());
		LOG.debug("Resource Type: {}", resource.getResourceType().getName());

		final KieFileSystem fs = services.newKieFileSystem().writeKModuleXML(module.toXML()).write(resource);

		final KieBuilder builder = services.newKieBuilder(fs).buildAll();
		final Results results = builder.getResults();
		if (results.hasMessages(Level.INFO, Level.WARNING, Level.ERROR)) {
			results.getMessages(Level.INFO, Level.WARNING, Level.ERROR).forEach(m -> {
				LOG.warn("Problem with KieBuilder!\nMessage Level: {}\nPath: {}\nLine: {}\nColumn: {}", m.getLevel(),
						m.getPath(), m.getLine(), m.getColumn());
			});
		} else {
			LOG.debug("No results.");
		}

		final KieContainer container = services.newKieContainer(services.getRepository().getDefaultReleaseId());

		final Project project = new Project();
		project.setProjectNumber(PROJECT_NUMBER);
		
		final ArrayList<String> response = new ArrayList<String>();
		final StatelessKieSession stateless = container.newStatelessKieSession();
		stateless.setGlobal("response", response);
		stateless.execute(Arrays.asList(new Object[] { project, response }));
		assertTrue(!response.isEmpty());
		if (isNotNullOrEmpty(response)) {
			response.forEach(r -> {
				LOG.debug("Response: {}", r);
			});
		} else {
			LOG.debug("No response.");
		}
		assertEquals(EXPECTED, response.iterator().next());
	}
}