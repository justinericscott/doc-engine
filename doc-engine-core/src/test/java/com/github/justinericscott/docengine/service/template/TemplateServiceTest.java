/**TODO: License
 */
package com.github.justinericscott.docengine.service.template;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.justinericscott.docengine.service.template.TemplateService;
import com.github.justinericscott.docengine.util.AbstractTest;

/**
 * @author Justin Scott TODO: Description
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TemplateServiceTest extends AbstractTest {
	private static final String TEMPLATE_BODY = "Hello, ${name!\" ... What's Your Name?\"}!!!";
	private static final String TEMPLATE_BODY_BROKEN = "Problem Processing Template: test_template_broken\n"
			+ "---- OFFENDING TOKENS: TOKEN NAME: name === VALUE: World\n\n"
			+ "---- EXCEPTION: Syntax error in template \"test_template_broken\" in line 1, column 16:\n"
			+ "Encountered \"...\", but was expecting one of:\n\"..\n\"<DOT_DOT_LESS>\n\"..*\n\"\"}\"";
	private static final String TEMPLATE_NAME = "test_template";
	private static final String TEMPLATE_NAME_BROKEN = "test_template_broken";
	private static final String EXPECTED_RESULT = "Hello, World!!!";
	private static final String EXPECTED_RESULT_NULL_TEMPLATE_NAME = "Template name must not be null/empty!";
	private static final String EXPECTED_RESULT_NULL_TOKEN = "Hello,  ... What's Your Name?!!!";
	private static final String EXPECTED_RESULT_NULL_TOKENS = "Tokens must not be null/empty!";
	private static final String TOKEN_NAME = "name";
	private static final String TOKEN_VALUE = "World";

	@Autowired
	private TemplateService service;

	@Test
	public void a_ProcessTest() {
		Map<String, Object> tokens = new HashMap<String, Object>(1);
		tokens.put(TOKEN_NAME, TOKEN_VALUE);
		service.loadTemplate(TEMPLATE_NAME, TEMPLATE_BODY);
		String actual = service.process(TEMPLATE_NAME, tokens);
		assertEquals(EXPECTED_RESULT, actual);
	}

	@Test
	public void b_ProcessNullToken() {
		Map<String, Object> tokens = new HashMap<String, Object>(1);
		tokens.put(TOKEN_NAME, null);
		service.loadTemplate(TEMPLATE_NAME, TEMPLATE_BODY);
		String actual = service.process(TEMPLATE_NAME, tokens);
		assertEquals(EXPECTED_RESULT_NULL_TOKEN, actual);
	}

	@Test
	public void c_ProcessNullTokens() {
		service.loadTemplate(TEMPLATE_NAME, TEMPLATE_BODY);
		String actual = service.process(TEMPLATE_NAME, null);
		assertEquals(EXPECTED_RESULT_NULL_TOKENS, actual);
	}

	@Test
	public void d_ProcessNullTemplate() {
		Map<String, Object> tokens = new HashMap<String, Object>(1);
		tokens.put(TOKEN_NAME, TOKEN_VALUE);
		String actual = service.process(null, tokens);
		assertEquals(EXPECTED_RESULT_NULL_TEMPLATE_NAME, actual);
	}

	@Test
	public void e_ProcessBrokenTemplate() {
		Map<String, Object> tokens = new HashMap<String, Object>(1);
		tokens.put(TOKEN_NAME, TOKEN_VALUE);
		service.loadTemplate(TEMPLATE_NAME_BROKEN, TEMPLATE_BODY_BROKEN);
		String actual = service.process(TEMPLATE_NAME_BROKEN, tokens);
		assertEquals(TEMPLATE_BODY_BROKEN, actual);
	}
}