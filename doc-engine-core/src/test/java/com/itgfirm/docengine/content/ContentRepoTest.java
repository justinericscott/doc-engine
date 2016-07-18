package com.itgfirm.docengine.content;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;

import com.itgfirm.docengine.DocEngine;
import com.itgfirm.docengine.data.DataConstants;
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;
import com.itgfirm.docengine.util.TestUtils;

/**
 * @author Justin Scott 
 * 
 * Tests for the Content Repository
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringApplicationConfiguration( classes = DocEngine.class )
public class ContentRepoTest extends AbstractTest {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(ContentRepoTest.class);
	@Autowired 
	ContentRepo contentRepo;

	@Test
	public void aa_MergeTest() {
		// Merge 1
		Content content = createContent();
		contentRepo.delete(content);	
	}

	@Test
	public void ab_MergeInvalidParamTest() {
		// Null Object
		assertNull(contentRepo.merge(null));
		// Empty Code for Content
		try {
			new ContentJpaImpl("", "TEST BODY");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getClass().equals(IllegalArgumentException.class));	
		}
		assertNull(contentRepo.merge(new ContentJpaImpl("", "TEST BODY")).getId());
		// Empty Body for Content
		assertNull(contentRepo.merge(new ContentJpaImpl(
				TestUtils.getRandomTestString(2), "")).getId());
		// Duplicate codes
		Content content = createContent();
		assertNull(contentRepo.merge(new ContentJpaImpl(
				content, content.getContentCd())).getId());
		contentRepo.delete(content);
	}

	@Test
	public void ba_GetTest() {
		// Get All
		List<Content> contents = new ArrayList<Content>();
		contents.add(createContent(1));
		contents.add(createContent(2));
		contents.add(createContent(3));
		contents.add(createContent(4));
		contents.add(createContent(5));
		List<Content> allContent = contentRepo.get();
		for (Content c : allContent) {
			validate(c);
		}
		for (Content c : contents) {
			contentRepo.delete(c);
		}

		// Get Content
		Content content = createContent();
		validate(contentRepo.get(content.getId()));
		validate(contentRepo.get(content.getContentCd()));
		contentRepo.delete(content);		
	}

	@Test
	public void bb_GetInvalidParamTest() {
		// Null Param
		assertNull(contentRepo.get((String) null));
		// Empty Param
		assertNull(contentRepo.get(""));
		// Zero as PARAM_ID
		assertNull(contentRepo.get(0L));
		// Bad PARAM_ID
		assertNull(contentRepo.get(99999999999999999L));
		// Bad Code
		assertNull(contentRepo.get("Snicklefritz"));
	}
	
	@Test
	public void ca_GetByCodeLikeTest() {
		Content content  = createContent();
		assertNotNull(contentRepo.getByCodeLike("TEST"));
		contentRepo.delete(content);
	}

	@Test
	public void cb_GetByCodeLikeInvalidLikeTest() {
		// No Like String
		assertNull(contentRepo.getByCodeLike(null));
		// Empty Like String
		assertNull(contentRepo.getByCodeLike(""));
		// Bad Like String
		assertNull(contentRepo.getByCodeLike("%Snicklefritz%"));
	}

	@Test
	public void da_GetWithQueryTest() {
		Content content  = createContent();
		assertNotNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CODE_LIKE,
				DataConstants.PARAM_CONTENT_CD, "%TEST%"));
		contentRepo.delete(content);		
	}

	@Test
	public void db_GetWithQueryInvalidParamTest() {
		// No Value
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CODE_LIKE,
				DataConstants.PARAM_CONTENT_CD, null));
		// Empty Value
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CODE_LIKE,
				DataConstants.PARAM_CONTENT_CD, ""));
		// Bad Value
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CODE_LIKE,
				DataConstants.PARAM_CONTENT_CD, "Snicklefritz"));
		// No Param Name
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CODE_LIKE, null, "R101"));
		// Empty Param Name
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CODE_LIKE, "", "R101"));
		// Bad Param Name
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CODE_LIKE, "Snicklefritz",
				"R101"));
		// Null Query
		assertNull(contentRepo.getWithQuery(null,
				DataConstants.PARAM_CONTENT_CD, "R101"));
		// Empty Query
		assertNull(contentRepo.getWithQuery("", DataConstants.PARAM_CONTENT_CD,
				"R101%"));
		// Bad Query
		assertNull(contentRepo.getWithQuery("Snicklefritz",
				DataConstants.PARAM_CONTENT_CD, "R101"));
	}

	@Test
	public void ea_GetWithQueryMultiParamTest() {
		List<Content> contents = new ArrayList<Content>();
		contents.add(createContent(1));
		contents.add(createContent(2));
		contents.add(createContent(3));
		contents.add(createContent(4));
		contents.add(createContent(5));
		String[] paramNames = { DataConstants.PARAM_CONTENT_CD,
				DataConstants.PARAM_BODY };
		String[] values = { "%TEST%", "%BODY%" };
		assertNotNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CONTENT_CD_AND_BODY_LIKE,
				paramNames, values));	
		for (Content c : contents) {
			contentRepo.delete(c);
		}
	}

	@Test
	public void eb_GetWithQueryMultiParamInvalidParamTest() {
		String[] paramNames = { DataConstants.PARAM_CONTENT_CD,
				DataConstants.PARAM_BODY };
		String[] values = { "R101%", "%BODY%" };
		String[] noParamNames = { null, null };
		String[] noValues = { null, null };
		String[] emptyParamNames = { "", "" };
		String[] emptyValues = { "", "" };
		String[] badParamNames = { "%Snicklefritz%", "%Snicklefritz%" };
		String[] badValues = { "%Snicklefritz%", "%Snicklefritz%" };
		String[] shortParamNames = { "R101%" };
		String[] shortValues = { "%BODY%" };
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CONTENT_CD_AND_BODY_LIKE,
				paramNames, noValues));
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CONTENT_CD_AND_BODY_LIKE,
				paramNames, emptyValues));
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CONTENT_CD_AND_BODY_LIKE,
				paramNames, badValues));
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CONTENT_CD_AND_BODY_LIKE,
				paramNames, shortValues));
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CONTENT_CD_AND_BODY_LIKE,
				noParamNames, values));
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CONTENT_CD_AND_BODY_LIKE,
				emptyParamNames, values));
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CONTENT_CD_AND_BODY_LIKE,
				badParamNames, values));
		assertNull(contentRepo.getWithQuery(
				DataConstants.GET_CONTENTS_BY_CONTENT_CD_AND_BODY_LIKE,
				shortParamNames, values));
	}

	@Test
	public void xx_DeleteTest() {
		Content content = createContent();
		assertTrue(contentRepo.delete(content));
		assertNull(contentRepo.get(content.getId()));
		createContent(1);
		createContent(2);
		createContent(3);
		createContent(4);
		createContent(5);
		contentRepo.deleteByCodeLike("TEST");
		assertNull(contentRepo.getByCodeLike("TEST"));
	}

	private Content createContent() {
		return createContent(1);
	}
	
	private Content createContent(int seed) {
		Content content = contentRepo.merge(makeTestContent(seed));
		validate(content);
		return content;		
	}
}