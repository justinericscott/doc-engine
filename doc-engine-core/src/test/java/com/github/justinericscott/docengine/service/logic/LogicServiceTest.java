package com.github.justinericscott.docengine.service.logic;

import static com.github.justinericscott.docengine.util.TestUtils.getFileFromClasspath;
import static org.junit.Assert.*;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message.Level;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.Results;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.builder.model.KieSessionModel.KieSessionType;
import org.kie.api.io.Resource;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.StatelessKieSession;
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
	public void a_SimpleDecisionTableTest() {
		final Project project = new Project();
		project.setProjectNumber(PROJECT_NUMBER);
		final File file = getFileFromClasspath(LOGIC_TABLE);
		assertNotNull(file);
		assertTrue(file.exists());
		service.addResource(file);
		final Iterable<String> results = service.process(project);
		assertNotNull(results);
		assertTrue(results.iterator().hasNext());
		assertEquals(EXPECTED, results.iterator().next());
	}

	@Ignore
	@Test
	public void b_exploringTest() {
		final KieServices services = KieServices.Factory.get();
		final KieModuleModel module = services.newKieModuleModel();
		module.getConfigurationProperties().forEach((key, value) -> {
			LOG.debug("Key: {} | Value: {}", key, value);
		});
		final KieBaseModel base = module.newKieBaseModel("TESTING_BASE");
		final KieSessionModel session = base.newKieSessionModel("TESTING_SESSION");
		session.setType(KieSessionType.STATELESS);
		session.setDefault(true);
		final KieFileSystem fs = services.newKieFileSystem();
		KieBuilder builder = services.newKieBuilder(fs).buildAll();
		Results results = builder.getResults();
		if (results.hasMessages(Level.INFO, Level.WARNING, Level.ERROR)) {
			results.getMessages(Level.INFO, Level.WARNING, Level.ERROR).forEach(m -> {
				LOG.warn("Problem with KieBuilder!\nMessage Level: {}\nPath: {}\nLine: {}\nColumn: {}", m.getLevel(),
						m.getPath(), m.getLine(), m.getColumn());
			});
		}
		final ReleaseId id = services.getRepository().getDefaultReleaseId();
		KieContainer container = services.newKieContainer(id);
		ArrayList<String> response = new ArrayList<String>();
		final Project project = new Project();
		project.setProjectNumber(PROJECT_NUMBER);
		StatelessKieSession stateless = container.newStatelessKieSession();
		stateless.setGlobal("response", response);
		stateless.execute(Arrays.asList(new Object[] { project, response }));
		response.forEach(r -> {
			LOG.debug("Response: {}", r);
		});
		final File file = getFileFromClasspath(LOGIC_TABLE);
		assertNotNull(file);
		assertTrue(file.exists());
		final Resource resource = ResourceFactory.newFileResource(file);
		fs.write(resource);
		builder = services.newKieBuilder(fs).buildAll();
		results = builder.getResults();
		assertFalse(results.hasMessages(Level.INFO, Level.WARNING, Level.ERROR));
		if (results.hasMessages(Level.INFO, Level.WARNING, Level.ERROR)) {
			results.getMessages(Level.INFO, Level.WARNING, Level.ERROR).forEach(m -> {
				LOG.warn("Problem with KieBuilder!\nMessage Level: {}\nPath: {}\nLine: {}\nColumn: {}", m.getLevel(),
						m.getPath(), m.getLine(), m.getColumn());
			});
		}
		container = services.newKieContainer(id);
		stateless = container.newStatelessKieSession();
		stateless.setGlobal("response", response);
		stateless.execute(Arrays.asList(new Object[] { project, response }));
		assertNotNull(response);
		assertTrue(!response.isEmpty());
		assertTrue(response.iterator().hasNext());
		assertEquals(EXPECTED, response.iterator().next());
		response.forEach(r -> {
			LOG.debug("Response: {}", r);
		});
	}
}