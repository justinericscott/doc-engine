package com.github.justinericscott.docengine.types;

import static org.junit.Assert.*;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.justinericscott.docengine.types.ContentJpaImpl;
import com.github.justinericscott.docengine.types.InstanceJpaImpl;
import com.github.justinericscott.docengine.util.AbstractTest;

/**
 * 
 * @author Justin Scott
 * 
 * TODO: Description
 */
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class TypesTest extends AbstractTest {

	@Test
	public void aa_SaveCustomBodyTest() {
		// Create initial objects
		ContentJpaImpl contentJpaImpl = new ContentJpaImpl("CONTENT_TEST_CODE_" + uuid(), "CONTENT_TEST_BODY");
		InstanceJpaImpl instanceJpaImpl = new InstanceJpaImpl(contentJpaImpl, "TEST_PROJECT_ID_" + uuid());
		
		// Make sure the methods return the expected defaults
		assertEquals("CONTENT_TEST_BODY", instanceJpaImpl.getBody());
		
		// Try to set the template as the custom
		final String body = contentJpaImpl.getBody();
		instanceJpaImpl.setBody(body);
		assertNull(instanceJpaImpl.getCustomBody());
		
		// Setting custom
		instanceJpaImpl.setBody("INSTANCE_TEST_BODY");
		assertEquals("INSTANCE_TEST_BODY", instanceJpaImpl.getBody());
		
		// Clear the custom body with a null, reverting back to the template
		instanceJpaImpl.setBody(null);
		assertNull(instanceJpaImpl.getCustomBody());
		assertEquals("CONTENT_TEST_BODY", instanceJpaImpl.getBody());

		// Setting custom
		instanceJpaImpl.setBody("INSTANCE_TEST_BODY");
		assertEquals("INSTANCE_TEST_BODY", instanceJpaImpl.getBody());
		
		// Clear the custom body with a empty string, reverting back to the template
		instanceJpaImpl.setBody("");
		assertEquals("CONTENT_TEST_BODY", instanceJpaImpl.getBody());
	}
}