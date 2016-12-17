package com.itgfirm.docengine.service.content;

import static org.junit.Assert.*;
import static com.itgfirm.docengine.DocEngine.Constants.*;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.itgfirm.docengine.service.content.ContentService;
import com.itgfirm.docengine.types.ContentJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;
import com.itgfirm.docengine.util.TestUtils;

/**
 * @author Justin Scott
 * 
 *         Tests for the Content Service
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ContentServiceTest extends AbstractTest {

	@Autowired
	@Qualifier(AUTOWIRE_QUALIFIER_DEFAULT)
	protected ContentService service;

	@Test
	public void a_SaveTest() {
		// One
		ContentJpaImpl content = makeTestContent(1);
		content = service.save(content);
		assertNotNull(content);
		assertTrue(content.isValid(true));
		assertNull(service.save((ContentJpaImpl) null));
		assertNull(service.save(new ContentJpaImpl("", "TEST BODY")));
		assertNull(service.save(new ContentJpaImpl(TestUtils.getRandomTestString(1), "")));
		assertNull(service.save(new ContentJpaImpl(content, content.getContentCd())));

		// Many
		Iterable<? extends ContentJpaImpl> result = service.save(makeTestContents(3));
		for (final ContentJpaImpl c : result) {
			assertTrue(c.isValid(true));
		}
	}

	@Test
	public void b_FindTest() {
		// Find one
		ContentJpaImpl content = createContent();
		content = service.findOne(content.getId());
		assertNotNull(content);
		assertTrue(content.isValid(true));
		assertNull(service.findOne((Long) null));
		assertNull(service.findOne(Long.MIN_VALUE));
		assertNull(service.findOne(Long.MAX_VALUE));

		// Find by code
		content = service.findByContentCd(content.getContentCd());
		assertNotNull(content);
		assertTrue(content.isValid(true));
		assertNull(service.findByContentCd((String) null));
		assertNull(service.findByContentCd(""));
		assertNull(service.findByContentCd("%Snicklefritz%"));

		// Find by code like
		createContents(4);
		Iterable<? extends ContentJpaImpl> contents = service.findByContentCdLike("%TEST%");
		assertNotNull(contents);
		assertTrue(contents.iterator().hasNext());
		assertNull(service.findByContentCdLike((String) null));
		assertNull(service.findByContentCdLike(""));
		assertFalse(service.findByContentCdLike("%Snicklefritz%").iterator().hasNext());

		// Find all
		createContents(5);
		contents = service.findAll();
		for (final ContentJpaImpl c : contents) {
			assertTrue(c.isValid(true));
		}
	}

	@Test
	public void x_DeleteTest() {
		ContentJpaImpl content = createContent();
		service.delete(content);
		assertNull(service.findOne(content.getId()));
		content = createContent();
		service.delete(content.getContentCd());
		assertNull(service.findByContentCd(content.getContentCd()));
		Iterable<? extends ContentJpaImpl> contents = createContents(3);
		service.delete(contents);
		for (final ContentJpaImpl c : contents) {
			assertNull(service.findOne(c.getId()));
		}
		createContents(4);
		contents = service.findByContentCdLike("%TEST%");
		assertNotNull(contents);
		assertTrue(contents.iterator().hasNext());
		assertNull(service.findByContentCdLike((String) null));
		assertNull(service.findByContentCdLike(""));
		assertFalse(service.findByContentCdLike("%Snicklefritz%").iterator().hasNext());
	}

	private ContentJpaImpl createContent() {
		return createContent(1);
	}

	private ContentJpaImpl createContent(int seed) {
		return service.save(makeTestContent(seed));
	}

	private Iterable<? extends ContentJpaImpl> createContents(final int count) {
		return service.save(makeTestContents(count));
	}
}