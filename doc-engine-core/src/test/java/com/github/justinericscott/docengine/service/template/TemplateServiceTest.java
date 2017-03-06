package com.github.justinericscott.docengine.service.template;

import static com.github.justinericscott.docengine.util.TestUtils.TestConstants.*;
import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.justinericscott.docengine.service.template.TemplateService;
import com.github.justinericscott.docengine.util.AbstractTest;

@SuppressWarnings("serial")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TemplateServiceTest extends AbstractTest {

	@Autowired
	private TemplateService service;
	
	@Test
	public void a_LoadTest() {
		service.load(TEST_FTL_BODY_NAME, TEST_FTL_BODY);		
	}

	@Test
	public void a_ProcessTest() {
		service.load(TEST_FTL_BODY_NAME, TEST_FTL_BODY);
		assertEquals(TEST_FTL_EXP_BODY, service.process(TEST_FTL_BODY_NAME, TOKENS));
	}

	@Test
	public void b_ProcessNullToken() {
		service.load(TEST_FTL_BODY_NAME, TEST_FTL_BODY);
		assertEquals(TEST_FTL_EXP_NULL_TOKEN, service.process(TEST_FTL_BODY_NAME, new HashMap<String, Object>() {
			{
				put(TEST_TOKEN_NAME, null);
			}
		}));
	}

	@Test
	public void c_ProcessNullTokens() {
		service.load(TEST_FTL_BODY_NAME, TEST_FTL_BODY);
		assertEquals(TEST_FTL_EXP_NULL_TOKENS, service.process(TEST_FTL_BODY_NAME, null));
	}

	@Test
	public void d_ProcessNullTemplate() {
		assertEquals(TEST_FTL_EXP_NULL_TEMPLATE_NAME, service.process(null, TOKENS));
	}

	@Test
	public void e_ProcessBrokenTemplate() {
		service.load(TEST_FTL_BROKEN_NAME, TEST_FTL_BROKEN_BODY);
//		assertTrue(RESULT_BROKEN_BODY.equals(service.run(FTL_BROKEN_NAME, TOKENS)));
		assertEquals(TEST_FTL_EXP_BROKEN_BODY, service.process(TEST_FTL_BROKEN_NAME, TOKENS));
	}
}