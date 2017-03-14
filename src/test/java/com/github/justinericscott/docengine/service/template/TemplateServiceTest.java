package com.github.justinericscott.docengine.service.template;

import static com.github.justinericscott.docengine.util.TestUtils.TestConstants.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.justinericscott.docengine.service.template.TemplateService;
import com.github.justinericscott.docengine.util.AbstractTest;

import freemarker.template.Template;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TemplateServiceTest extends AbstractTest {

	@Autowired
	private TemplateService service;

	@Test
	public void aa_LoadTest() {
		service.load(TEST_FTL_BODY_NAME, TEST_FTL_BODY);
	}

	@Test
	public void ab_LoadOverwriteTest() {
		service.load(TEST_FTL_BODY_NAME, TEST_FTL_BODY);
		service.load(TEST_FTL_BODY_NAME, TEST_FTL_BODY, false);
	}

	@Test
	public void ac_LoadNullNameTest() {
		service.load(null, TEST_FTL_BODY);
	}

	@Test
	public void ad_LoadNullTemplateTest() {
		service.load(TEST_FTL_BODY_NAME, null);
	}

	@Test
	public void ae_LoadBrokenTemplateTest() {
		service.load(TEST_FTL_BROKEN_NAME, TEST_FTL_BROKEN_BODY);
	}

	@Test
	public void ba_GetTest() {
		service.load(TEST_FTL_BODY_NAME, TEST_FTL_BODY);
		final Template template = service.get(TEST_FTL_BODY_NAME);
		assertNotNull(template);
		assertEquals(TEST_FTL_BODY, template.toString());
	}

	@Test
	public void bb_GetNullNameTest() {
		service.load(TEST_FTL_BODY_NAME, TEST_FTL_BODY);
		assertNull(service.get(null));
	}

	@Test
	public void ca_RunTest() {
		service.load(TEST_FTL_BODY_NAME, TEST_FTL_BODY);
		assertEquals(TEST_FTL_EXP_BODY, service.run(TEST_FTL_BODY_NAME, TOKENS));
	}

	@Test
	public void cb_RunNullTokenName() {
		service.load(TEST_FTL_BODY_NAME, TEST_FTL_BODY);
		final Map<String, Object> tokens = new HashMap<String, Object>();
		tokens.put(null, TEST_TOKEN_VALUE);
		assertEquals(TEST_FTL_EXP_NULL_TOKEN, service.run(TEST_FTL_BODY_NAME, tokens));
	}

	@Test
	public void cc_RunNullTokenValue() {
		service.load(TEST_FTL_BODY_NAME, TEST_FTL_BODY);
		final Map<String, Object> tokens = new HashMap<String, Object>();
		tokens.put(TEST_TOKEN_NAME, null);
		assertEquals(TEST_FTL_EXP_NULL_TOKEN, service.run(TEST_FTL_BODY_NAME, tokens));
	}

	@Test
	public void cd_RunNullTokens() {
		service.load(TEST_FTL_BODY_NAME, TEST_FTL_BODY);
		assertEquals(TEST_FTL_EXP_NULL_TOKENS, service.run(TEST_FTL_BODY_NAME, null));
	}

	@Test
	public void ce_RunNullTemplateName() {
		assertEquals(TEST_FTL_EXP_NULL_TEMPLATE_NAME, service.run(null, TOKENS));
	}

	@Test
	public void cf_RunBrokenTemplate() {
		service.load(TEST_FTL_BROKEN_NAME, TEST_FTL_BROKEN_BODY);
		assertEquals(TEST_FTL_EXP_BROKEN_BODY, service.run(TEST_FTL_BROKEN_NAME, TOKENS));
	}

	@Test
	public void da_RemoveTest() {
		service.load(TEST_FTL_BODY_NAME, TEST_FTL_BODY);
		service.remove(TEST_FTL_BODY_NAME);
		assertNull(service.get(TEST_FTL_BODY_NAME));
	}

	@Test
	public void db_RemoveNonExistentTemplateTest() {
		service.remove(TEST_FTL_BODY_NAME);
		assertNull(service.get(TEST_FTL_BODY_NAME));		
	}

	@Test
	public void dc_RemoveNullNameTest() {
		service.load(TEST_FTL_BODY_NAME, TEST_FTL_BODY);
		service.remove(null);
		assertNotNull(service.get(TEST_FTL_BODY_NAME));
	}
	
	@Test
	public void ea_AdhocTest() {
		assertEquals(TEST_FTL_EXP_BODY, service.adhoc(TEST_FTL_BODY, TOKENS));
		assertNull(service.get(TemplateServiceImpl.TEMP_NAME));
	}
	
	@Test
	public void eb_AdHocNullBodyTest() {
		assertNull(service.adhoc(null, TOKENS));
	}

	@Test
	public void ec_AdHocNullTokensTest() {
		assertEquals(TEST_FTL_EXP_NULL_TOKENS, service.adhoc(TEST_FTL_BODY, null));
		assertNull(service.get(TemplateServiceImpl.TEMP_NAME));
	}
}