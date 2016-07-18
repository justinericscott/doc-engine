/**TODO: License
 */
package com.itgfirm.docengine.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.itgfirm.docengine.DocEngine;
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.TokenDefinition;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.types.jpa.TokenDefinitionJpaImpl;

/**
 * @author Justin Scott
 * TODO: Description
 */
@RunWith( SpringJUnit4ClassRunner.class )
@SpringApplicationConfiguration( classes = DocEngine.class )
@Ignore
public class AbstractTest extends Assert {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(AbstractTest.class);
	
	/**
	 * TODO: Description
	 */
	public AbstractTest() {	}
	
	protected Content makeTestContent(Integer seed) {
		return new ContentJpaImpl(TestUtils.getRandomTestString(seed), 
				"TEST_BODY");
	}
	
	protected TokenDefinition makeTestTokenDefinition(Integer seed) {
		TokenDefinition token = new TokenDefinitionJpaImpl(TestUtils.getRandomTestString(seed), 
				"TEST_TOKEN_NAME");
		token.setEntity("TR_PROJECT");
		token.setAttribute("PROJECT_NBR");
		token.setWhere("PROJECT_NBR = ?");
		return token;
	}

	protected void validate(Content content) {
		assertTrue(content.isValid(true));
	}
	
	protected void validate(TokenDefinition token) {
		assertTrue(token.isValid(true));
	}
}