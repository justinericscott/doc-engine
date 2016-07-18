package com.itgfirm.docengine.content;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.web.client.HttpServerErrorException;

import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;
import com.itgfirm.docengine.util.CoreWebConstants;
import com.itgfirm.docengine.util.TestUtils;
import com.itgfirm.docengine.web.RestClient;

/**
 * @author Justin Scott
 * 
 * Tests for the Content REST Controller
 */
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
@WebIntegrationTest
public class ContentRestControllerTest extends AbstractTest {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(ContentRestControllerTest.class);
	@Autowired @Qualifier( value = CoreWebConstants.QUALIFIER_REST_CLIENT_CONTENT ) 
	RestClient rest;
	
	@SuppressWarnings("unchecked")
	@Test
	public void aa_MergeTest() {
		// Merge 1
		Content content = createContent();
		rest.delete(content);

		// Merge a list
		List<Content> list = new ArrayList<Content>();
		list.add(makeTestContent(1));
		list.add(makeTestContent(2));
		list.add(makeTestContent(3));
		list = (List<Content>) rest.merge(list);
		for (Content c : list) {
			assertNotNull(c.getId());
			rest.delete(c);
		}
	}
	
	@Test
	public void ab_MergeTestInvalidParamTest() {
		// Null Object
		try {
			rest.merge(null, Content.class);
			throw new IllegalStateException();
		} catch (HttpServerErrorException e) {
			assertEquals(e.getClass(), HttpServerErrorException.class);
		}
		// Empty Code Content
		assertNull(((Content) rest.merge(
				new ContentJpaImpl("TEST"), Content.class)).getId());
		// Empty Body Content 
		assertNull(((Content) rest.merge(new ContentJpaImpl(
				TestUtils.getRandomTestString(1), ""), Content.class)).getId());
		// Duplicates 
		Content content = createContent();
		String code = content.getContentCd();
		assertNull(((Content) rest.merge(new ContentJpaImpl(content, code), 
				Content.class)).getId());
		rest.delete(content);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void ba_GetTest() {
		// Get All
		List<Content> contents = new ArrayList<Content>();
		contents.add(createContent(1));
		contents.add(createContent(2));
		contents.add(createContent(3));
		contents.add(createContent(4));
		contents.add(createContent(5));
		List<Content> allContent = (List<Content>) rest.get();
		assertNotNull(allContent);
		for (Content c : allContent) {
			validate(c);
		}
		for (Content c : contents) {
			rest.delete(c);
		}
		
		// Get Content
		Content content = createContent();
		validate((Content) rest.get(content.getId()));
		validate((Content) rest.get(content.getContentCd()));
		rest.delete(content);		
		
	}
	
	@Test
	public void bb_GetInvalidParamTest() {
		// Get By ID
		try {
			rest.get((Long) null);
			throw new IllegalStateException();
		} catch (HttpServerErrorException e) {
			assertEquals(e.getClass(), HttpServerErrorException.class);
		}
		assertNull(rest.get(0L));
		try {
			rest.get("");
			throw new IllegalStateException();
		} catch (HttpServerErrorException e) {
			assertEquals(e.getClass(), HttpServerErrorException.class);
		}
		assertNull(rest.get(999999999L));
		// Get By Code
		try {
			rest.get((String) null);
			throw new IllegalStateException();
		} catch (HttpServerErrorException e) {
			assertEquals(e.getClass(), HttpServerErrorException.class);
		}
		assertNull(rest.get("Snicklefritz"));

	}
	
	@Test
	public void ca_GetByCodeLikeTest() {
		assertNotNull(rest.merge(TestUtils.getListOfRandomContents(3)));
		List<?> array = rest.getByCodeLike("TEST", Content[].class);
		assertNotNull(array);
		for (Object o : array) {
			Content c = (Content) o;
			assertNotNull(c);
			rest.delete(c);
		}				
	}
	
	@Test
	public void cb_GetByCodeLikeInvalidParamTest() {
		assertNull(rest.getByCodeLike("Snicklefritz", Content[].class));
		assertNull(rest.getByCodeLike("", Content[].class));
		assertNull(rest.getByCodeLike((String) null, Content[].class));
	}

	@Test
	public void xx_DeleteTest() {
		Content content = createContent();
		Long id = content.getId();
		rest.delete(content);
		assertNull(rest.get(id));
		rest.delete(content);
	}

	private Content createContent() {
		return createContent(1);
	}
	
	private Content createContent(int seed) {
		Content content = (Content) rest.merge(makeTestContent(seed), ContentJpaImpl.class);
		validate(content);
		return content;
	}
	
}