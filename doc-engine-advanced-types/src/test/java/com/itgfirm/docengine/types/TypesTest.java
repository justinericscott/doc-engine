package com.itgfirm.docengine.types;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.types.jpa.InstanceJpaImpl;

/**
 * 
 * @author Justin Scott
 * 
 * TODO: Description
 */
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class TypesTest extends Assert {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(TypesTest.class); 

	@Test
	public void aa_SaveCustomBodyTest() {
		// Create initial objects
		ContentJpaImpl contentJpaImpl = new ContentJpaImpl("CONTENT_TEST_CODE", "CONTENT_TEST_BODY");
		InstanceJpaImpl instanceJpaImpl = new InstanceJpaImpl(contentJpaImpl, "TEST_PROJECT_ID");
		
		// Make sure the methods return the expected defaults
		Assert.assertEquals("CONTENT_TEST_BODY", instanceJpaImpl.getBody());
		Assert.assertNull(instanceJpaImpl.getCustomBody());
		
		// Try to set the template as the custom
		instanceJpaImpl.setBody(contentJpaImpl.getBody());
		Assert.assertNull(instanceJpaImpl.getCustomBody());
		
		// Setting custom
		instanceJpaImpl.setBody("INSTANCE_TEST_BODY");
		Assert.assertEquals("INSTANCE_TEST_BODY", instanceJpaImpl.getCustomBody());
		
		// Clear the custom body with a null, reverting back to the template
		instanceJpaImpl.setBody(null);
		Assert.assertNull(instanceJpaImpl.getCustomBody());
		Assert.assertEquals("CONTENT_TEST_BODY", instanceJpaImpl.getBody());

		// Setting custom
		instanceJpaImpl.setBody("INSTANCE_TEST_BODY");
		Assert.assertEquals("INSTANCE_TEST_BODY", instanceJpaImpl.getCustomBody());
		
		// Clear the custom body with a empty string, reverting back to the template
		instanceJpaImpl.setBody("");
		Assert.assertNull(instanceJpaImpl.getCustomBody());
		Assert.assertEquals("CONTENT_TEST_BODY", instanceJpaImpl.getBody());
	}
}