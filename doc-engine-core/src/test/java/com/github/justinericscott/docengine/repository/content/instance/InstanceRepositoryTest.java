package com.github.justinericscott.docengine.repository.content.instance;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.TreeSet;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.github.justinericscott.docengine.repository.content.ContentRepository;
import com.github.justinericscott.docengine.repository.content.InstanceRepository;
import com.github.justinericscott.docengine.types.ContentJpaImpl;
import com.github.justinericscott.docengine.types.InstanceJpaImpl;
import com.github.justinericscott.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * 
 *         Tests for the Instance Repository.
 * @param <T>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InstanceRepositoryTest extends AbstractTest {

	@Autowired
	private ContentRepository _contents;

	@Autowired
	private InstanceRepository _instances;

	@Test
	public void a_SaveTest() {
		
		// Happy path...
		ContentJpaImpl content = makeTestContent();
		content = _contents.save(content);
		assertNotNull(content);
		assertTrue(content.isValid(true));
		InstanceJpaImpl instance = new InstanceJpaImpl(content, TEST_PROJECT_ID_VALUE);
		instance = _instances.save(instance);
		assertNotNull(instance);
		assertTrue(instance.isValid(true));
		content = instance.getContent();
		assertNotNull(content);
		assertTrue(content.isValid(true));

		Collection<ContentJpaImpl> contents = makeTestContents(7);
		contents = (Collection<ContentJpaImpl>) _contents.save(contents);
		assertNotNull(contents);
		assertFalse(contents.isEmpty());
		final Collection<InstanceJpaImpl> list = new TreeSet<InstanceJpaImpl>();
		contents.forEach(c -> {
			assertTrue(c.isValid(true));
			list.add(new InstanceJpaImpl(c, TEST_PROJECT_ID_VALUE));
		});
		Collection<InstanceJpaImpl> instances = new TreeSet<InstanceJpaImpl>();
		instances = (Collection<InstanceJpaImpl>) _instances.save(list);
		assertNotNull(instances);
		assertFalse(instances.isEmpty());
		instances.forEach(i -> {
			assertTrue(i.isValid(true));
			ContentJpaImpl c = i.getContent();
			assertNotNull(c);
			assertTrue(c.isValid(true));
		});

		try {
			_instances.save((InstanceJpaImpl) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_instances.save(new InstanceJpaImpl(content));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_instances.save(new InstanceJpaImpl(TEST_PROJECT_ID_VALUE));
//			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_instances.save(new InstanceJpaImpl(makeTestContent(), TEST_PROJECT_ID_VALUE));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
	}

	@Test
	public void b_FindTest() {
		
		// Happy path...
		ContentJpaImpl content = _contents.save(makeTestContent());
		assertNotNull(content);
		assertTrue(content.isValid(true));
		final String contentCd = content.getContentCd();
		InstanceJpaImpl instance = new InstanceJpaImpl(content, TEST_PROJECT_ID_VALUE);
		instance = _instances.save(instance);
		assertNotNull(instance);
		assertTrue(instance.isValid(true));
		final Long id = instance.getId();

		instance = _instances.findOne(id);
		assertNotNull(instance);
		assertTrue(instance.isValid(true));
		content = instance.getContent();
		assertNotNull(content);
		assertTrue(content.isValid(true));
		instance = _instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, contentCd);
		assertNotNull(instance);
		assertTrue(instance.isValid(true));
		content = instance.getContent();
		assertNotNull(content);
		assertTrue(content.isValid(true));

		Collection<InstanceJpaImpl> instances = (Collection<InstanceJpaImpl>) _instances.findAll(); 
		assertNotNull(instances);
		assertFalse(instances.isEmpty());
		instances.forEach(i -> {
			assertTrue(i.isValid(true));
			ContentJpaImpl c = i.getContent();
			assertNotNull(c);
			assertTrue(c.isValid(true));
		});

		instances = (Collection<InstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "%TEST%");
		assertNotNull(instances);
		assertFalse(instances.isEmpty());
		instances.forEach(i -> {
			assertTrue(i.isValid(true));
			ContentJpaImpl c = i.getContent();
			assertNotNull(c);
			assertTrue(c.isValid(true));
		});
		
		// Break it...		
		assertNull(_instances.findOne(0L));
		assertNull(_instances.findOne(99999999L));
		assertNull(_instances.findByProjectIdAndContentContentCd("", contentCd));
		assertNull(_instances.findByProjectIdAndContentContentCd((String) null, contentCd));
		assertNull(_instances.findByProjectIdAndContentContentCd("Snicklefritz", contentCd));
		assertNull(_instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, null));
		assertNull(_instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, ""));
		assertNull(_instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, "Snicklefritz"));

		instances = (Collection<InstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike(null, TEST_CONTENT_CODE_PREFIX);
		assertTrue(instances.isEmpty());
		instances = (Collection<InstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike("", TEST_CONTENT_CODE_PREFIX);
		assertTrue(instances.isEmpty());
		instances = (Collection<InstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike("Snicklefritz", TEST_CONTENT_CODE_PREFIX);
		assertTrue(instances.isEmpty());
		instances = (Collection<InstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "");
		assertTrue(instances.isEmpty());
		instances = (Collection<InstanceJpaImpl>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "Snicklefritz");
		assertTrue(instances.isEmpty());
		try {
			_instances.findOne((Long) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
	}
	
	@Test
	public void c_DiscriminatorTest() {
		final String contentCd = "INSTANCE_DISCRIMINATOR_TEST_" + uuid();
		ContentJpaImpl cx = new ContentJpaImpl(contentCd, "BLAH BLAH BLAH");
		cx = _contents.save(cx);
		final InstanceJpaImpl x = new InstanceJpaImpl(cx, TEST_PROJECT_ID_VALUE);
		final InstanceJpaImpl y = _instances.save(x);
		assertNull(y.getDiscriminator());
		final InstanceJpaImpl z = _instances.findByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, contentCd);
		assertEquals(InstanceJpaImpl.class.getSimpleName(), z.getDiscriminator());
	}

	@Test
	public void x_DeleteTest() {
		final String projectId = TEST_PROJECT_ID_VALUE;
		ContentJpaImpl content = _contents.save(makeTestContent());
		assertNotNull(content);
		assertTrue(content.isValid(true));
		InstanceJpaImpl instance = new InstanceJpaImpl(content, projectId);
		instance = _instances.save(instance);
		assertNotNull(instance);
		assertTrue(instance.isValid(true));
		Long id = instance.getId();

		_instances.delete(id);
		assertNull(_instances.findOne(instance.getId()));
		instance = new InstanceJpaImpl(content, projectId);
		instance = _instances.save(instance);
		assertNotNull(instance);
		assertTrue(instance.isValid(true));
		id = instance.getId();
		_instances.delete(instance);
		assertNull(_instances.findOne(instance.getId()));
		
		Collection<ContentJpaImpl> contents = makeTestContents(7);
		contents = (Collection<ContentJpaImpl>) _contents.save(contents);
		assertNotNull(contents);
		assertFalse(contents.isEmpty());
		final Collection<InstanceJpaImpl> list = new TreeSet<InstanceJpaImpl>();
		contents.forEach(c -> {
			assertTrue(c.isValid(true));
			list.add(new InstanceJpaImpl(c, projectId));
		});
		Collection<InstanceJpaImpl> instances = (Collection<InstanceJpaImpl>) _instances.save(list);
		assertNotNull(instances);
		assertFalse(instances.isEmpty());
		instances.forEach(i -> {
			assertTrue(i.isValid(true));
			ContentJpaImpl c = i.getContent();
			assertNotNull(c);
			assertTrue(c.isValid(true));
		});		
		_instances.delete(instances);
		instances.forEach(i -> {
			assertNull(_instances.findOne(i.getId()));;
		});
		
		_instances.deleteAll();
	}
}