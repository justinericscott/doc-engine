package com.itgfirm.docengine.content;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
//import com.itgfirm.docengine.types.jpa.DocumentJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;
import com.itgfirm.docengine.util.TestUtils;

/**
 * @author Justin Scott
 * 
 * Tests for the Content Service
 */
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class ContentServiceTest extends AbstractTest {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(ContentServiceTest.class);
	@Autowired
	private ContentService contentService;
	
	@Test
	public void aa_MergeTest() {
		// Merge 1
		Content content = createContent();
		contentService.delete(content);		
		
		// Merge a list
		List<Content> list = new ArrayList<Content>();
		list.add(createContent(1));
		list.add(createContent(2));
		list.add(createContent(3));
		list = contentService.merge(list);
		for (Content c : list) {
			assertNotNull(c.getId());
			contentService.delete(c);
		}
	}

	@Test
	public void ab_MergeInvalidParamTest() {
		// Null Object
		assertNull(contentService.merge((Content) null));
		// Empty Code Content
		try {
			new ContentJpaImpl("", "TEST BODY");
		} catch (IllegalArgumentException e) {
			assertTrue(e.getClass().equals(IllegalArgumentException.class));	
		}		
		// Empty Body Content 
		try {
			new ContentJpaImpl(TestUtils.getRandomTestString(1), "");			
		} catch (IllegalArgumentException e) {
			assertTrue(e.getClass().equals(IllegalArgumentException.class));	
		}		
		// Duplicates 
		Content content = createContent();
		assertNull(contentService.merge(new ContentJpaImpl(
				content, content.getContentCd())).getId());
		contentService.delete(content);
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
		List<Content> allContent = (List<Content>) contentService.get();
		for (Content c : allContent) {
			validate(c);
		}
		for (Content c : contents) {
			contentService.delete(c);
		}
		
		// Get Content
		Content content = createContent();
		validate((Content) contentService.get(content.getId()));
		validate((Content) contentService.get(content.getContentCd()));
		contentService.delete(content);	
	}
	
	@Test
	public void bb_GetInvalidParamTest() {
		assertNull(contentService.get((String) null));
		assertNull(contentService.get(""));
		assertNull(contentService.get(0L));
		assertNull(contentService.get(99999999999999999L));
		assertNull(contentService.get("Snicklefritz"));
	}
	
	@Test
	public void ca_GetByCodeLikeTest() {
		Content content = createContent();
		content = contentService.getByCodeLike("TEST").get(0);
		assertNotNull(content.getId());
		contentService.delete(content);
	}
	
	@Test
	public void cb_GetByCodeLikeInvalidParamTest() {
		assertNull(contentService.getByCodeLike(null));
		assertNull(contentService.getByCodeLike(""));
		assertNull(contentService.getByCodeLike("%Snicklefritz%"));
	}

	@Test
	public void xx_DeleteTest() {
		Content content = createContent();
		assertTrue(contentService.delete(content));
		content = createContent();
		assertTrue(contentService.delete(content.getId()));
		assertNull(contentService.get(content.getId()));
		content = createContent();
		assertTrue(contentService.delete(content.getContentCd()));
		assertNull(contentService.get(content.getContentCd()));
		createContent(1);
		createContent(2);
		createContent(3);
		createContent(4);
		createContent(5);
		assertTrue(contentService.deleteByCodeLike("TEST"));
		assertNull(contentService.getByCodeLike("TEST"));
	}

	private Content createContent() {
		return createContent(1);
	}
	
	private Content createContent(int seed) {
		Content content = contentService.merge(makeTestContent(seed));
		validate(content);
		return content;		
	}
}