package com.github.justinericscott.docengine.repository.content;

import static org.junit.Assert.*;

import static com.github.justinericscott.docengine.util.TestUtils.TestConstants.*;

import java.util.Collection;
import java.util.TreeSet;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import com.github.justinericscott.docengine.models.Content;
import com.github.justinericscott.docengine.models.Instance;
import com.github.justinericscott.docengine.repository.content.ContentRepository;
import com.github.justinericscott.docengine.repository.content.InstanceRepository;
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
		Content content = makeTestContent();
		content = _contents.save(content);
		assertNotNull(content);
		assertTrue(content.isValid(true));
		Instance instance = new Instance(content, TEST_PROJECT_ID_VALUE);
		instance = _instances.save(instance);
		assertNotNull(instance);
		assertTrue(instance.isValid(true));
		content = instance.getContent();
		assertNotNull(content);
		assertTrue(content.isValid(true));
		Collection<Content> contents = makeTestContents(7);
		contents = (Collection<Content>) _contents.saveAll(contents);
		assertNotNull(contents);
		assertFalse(contents.isEmpty());
		final Collection<Instance> list = new TreeSet<Instance>();
		contents.forEach(c -> {
			assertTrue(c.isValid(true));
			list.add(new Instance(c, TEST_PROJECT_ID_VALUE));
		});
		Collection<Instance> instances = new TreeSet<Instance>();
		instances = (Collection<Instance>) _instances.saveAll(list);
		assertNotNull(instances);
		assertFalse(instances.isEmpty());
		instances.forEach(i -> {
			assertTrue(i.isValid(true));
			Content c = i.getContent();
			assertNotNull(c);
			assertTrue(c.isValid(true));
		});
		try {
			_instances.save((Instance) null);
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
		try {
			_instances.save(new Instance(content));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_instances.save(new Instance(TEST_PROJECT_ID_VALUE));
//			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(DataIntegrityViolationException.class, e.getClass());
		}
		try {
			_instances.save(new Instance(makeTestContent(), TEST_PROJECT_ID_VALUE));
			fail("Should throw exception....");
		} catch (final Exception e) {
			assertEquals(InvalidDataAccessApiUsageException.class, e.getClass());
		}
	}

	@Test
	public void b_FindTest() {
		// Happy path...
		Content content = _contents.save(makeTestContent());
		assertNotNull(content);
		assertTrue(content.isValid(true));
		final String contentCd = content.getContentCd();
		Instance instance = new Instance(content, TEST_PROJECT_ID_VALUE);
		instance = _instances.save(instance);
		assertNotNull(instance);
		assertTrue(instance.isValid(true));
		final Long id = instance.getId();
		instance = _instances.findById(id).get();
		assertNotNull(instance);
		assertTrue(instance.isValid(true));
		content = instance.getContent();
		assertNotNull(content);
		assertTrue(content.isValid(true));
		instance = _instances.findOptionalByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, contentCd).get();
		assertNotNull(instance);
		assertTrue(instance.isValid(true));
		content = instance.getContent();
		assertNotNull(content);
		assertTrue(content.isValid(true));
		Collection<Instance> instances = (Collection<Instance>) _instances.findAll(); 
		assertNotNull(instances);
		assertFalse(instances.isEmpty());
		instances.forEach(i -> {
			assertTrue(i.isValid(true));
			Content c = i.getContent();
			assertNotNull(c);
			assertTrue(c.isValid(true));
		});
		instances = (Collection<Instance>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "%TEST%");
		assertNotNull(instances);
		assertFalse(instances.isEmpty());
		instances.forEach(i -> {
			assertTrue(i.isValid(true));
			Content c = i.getContent();
			assertNotNull(c);
			assertTrue(c.isValid(true));
		});		
	}
	
	@Test
	public void bx_FindBreakTest() {
		// Break it...		
		Content content = _contents.save(makeTestContent());
		assertNotNull(content);
		assertTrue(content.isValid(true));
		final String contentCd = content.getContentCd();		
		assertFalse(_instances.findById(0L).isPresent());
		assertFalse(_instances.findById(99999999L).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd("", contentCd).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd((String) null, contentCd).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd("Snicklefritz", contentCd).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, null).isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, "").isPresent());
		assertFalse(_instances.findOptionalByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, "Snicklefritz").isPresent());
		Collection<Instance> instances = (Collection<Instance>) _instances.findByProjectIdAndContentContentCdLike(null, TEST_CODE_PREFIX_CONTENT);
		assertTrue(instances.isEmpty());
		instances = (Collection<Instance>) _instances.findByProjectIdAndContentContentCdLike("", TEST_CODE_PREFIX_CONTENT);
		assertTrue(instances.isEmpty());
		instances = (Collection<Instance>) _instances.findByProjectIdAndContentContentCdLike("Snicklefritz", TEST_CODE_PREFIX_CONTENT);
		assertTrue(instances.isEmpty());
		instances = (Collection<Instance>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "");
		assertTrue(instances.isEmpty());
		instances = (Collection<Instance>) _instances.findByProjectIdAndContentContentCdLike(TEST_PROJECT_ID_VALUE, "Snicklefritz");
		assertTrue(instances.isEmpty());
		try {
			_instances.findById((Long) null).get();
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
		Content cx = new Content(contentCd, "BLAH BLAH BLAH");
		cx = _contents.save(cx);
		final Instance x = new Instance(cx, TEST_PROJECT_ID_VALUE);
		final Instance y = _instances.save(x);
		assertNull(y.getDiscriminator());
		final Instance z = _instances.findOptionalByProjectIdAndContentContentCd(TEST_PROJECT_ID_VALUE, contentCd).get();
		assertEquals(Instance.class.getSimpleName(), z.getDiscriminator());
	}

//	@Test
	public void x_DeleteTest() {
		final String projectId = TEST_PROJECT_ID_VALUE;
		Content content = _contents.save(makeTestContent());
		assertNotNull(content);
		assertTrue(content.isValid(true));
		Instance instance = new Instance(content, projectId);
		instance = _instances.save(instance);
		assertNotNull(instance);
		assertTrue(instance.isValid(true));
		Long id = instance.getId();
		_instances.deleteById(id);
		assertFalse(_instances.findById(instance.getId()).isPresent());
		instance = new Instance(content, projectId);
		instance = _instances.save(instance);
		assertNotNull(instance);
		assertTrue(instance.isValid(true));
		id = instance.getId();
		_instances.delete(instance);
		assertFalse(_instances.findById(instance.getId()).isPresent());
		Collection<Content> contents = makeTestContents(7);
		contents = (Collection<Content>) _contents.saveAll(contents);
		assertNotNull(contents);
		assertFalse(contents.isEmpty());
		final Collection<Instance> list = new TreeSet<Instance>();
		contents.forEach(c -> {
			assertTrue(c.isValid(true));
			list.add(new Instance(c, projectId));
		});
		Collection<Instance> instances = (Collection<Instance>) _instances.saveAll(list);
		assertNotNull(instances);
		assertFalse(instances.isEmpty());
		instances.forEach(i -> {
			assertTrue(i.isValid(true));
			Content c = i.getContent();
			assertNotNull(c);
			assertTrue(c.isValid(true));
		});		
		_instances.deleteAll(instances);
		instances.forEach(i -> {
			assertFalse(_instances.findById(i.getId()).isPresent());;
		});
		
//		_instances.deleteAll();
	}
}