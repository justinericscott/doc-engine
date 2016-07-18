/**TODO: License
 */
package com.itgfirm.docengine.template;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.itgfirm.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * TODO: Description
 */
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class TemplateServiceTest extends AbstractTest {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(TemplateServiceTest.class);
	private static final String template = "Hello, ${name!\" ... What's Your Name?\"}!!!";
	private static final String brokenTemplate = "Hello, ${name! ... What's Your Name?\"}!!!";
	private static final String expected = "Hello, World!!!";
	private static final String expectedNullToken = "Hello,  ... What's Your Name?!!!";
	private static final String tokenName = "name";
	private static final String tokenValue = "World";

	@Autowired
	private TemplateService templateService;
	
	@Test
	public void aa_ProcessTest() {		
		Map<String, Object> tokens = new HashMap<String, Object>(1);
		tokens.put(tokenName, tokenValue);
		String actual = templateService.process(template, tokens);
		assertEquals(expected, actual);
	}

	@Test
	public void ab_ProcessNullToken() {
		Map<String, Object> tokens = new HashMap<String, Object>(1);
		tokens.put(tokenName, null);
		String actual = templateService.process(template, tokens);
		assertEquals(expectedNullToken, actual);
	}
	
	@Test
	public void ac_ProcessNullTokens() {
		String actual = templateService.process(template, null);
		assertEquals(template, actual);
	}
	
	@Test
	public void ad_ProcessNullTemplate() {
		Map<String, Object> tokens = new HashMap<String, Object>(1);
		tokens.put(tokenName, tokenValue);
		String actual = templateService.process(null, tokens);
		assertNull(actual);
	}
	
	@Test
	public void ae_ProcessBrokenTemplate() {
		Map<String, Object> tokens = new HashMap<String, Object>(1);
		tokens.put(tokenName, tokenValue);
		String actual = templateService.process(brokenTemplate, tokens);
		assertEquals(brokenTemplate, actual);
	}
}