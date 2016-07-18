package com.itgfirm.docengine.logic;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.itgfirm.docengine.pipeline.Project;
import com.itgfirm.docengine.pipeline.DefaultProjectImpl;
import com.itgfirm.docengine.util.AbstractTest;
import com.itgfirm.docengine.util.TestUtils;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LogicServiceTest extends AbstractTest {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(LogicServiceTest.class);
	private static final String EXPECTED = "Project Number Matches. Logic works!";
	private static final String PROJECT_NUMBER = "0NJ4321";

	@Autowired
	private LogicService logicService;

	@Test
	public void aa_SimpleDecisionTableTest() {
		Project project = new DefaultProjectImpl();
		project.setProjectNumber(PROJECT_NUMBER);
		logicService.addResource(TestUtils.getFileFromClasspath("DroolsTesting.xls"));
		List<String> results = logicService.process(project);
		assertNotNull(results);
		assertEquals(1, results.size());
		assertEquals(EXPECTED, results.get(0));
	}
}
