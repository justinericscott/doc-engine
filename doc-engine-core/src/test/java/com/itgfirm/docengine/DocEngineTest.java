/**TODO: License
 */
package com.itgfirm.docengine;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * @author Justin Scott
 * TODO: Description
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore
public class DocEngineTest extends Assert {

	@Test
	public void aa_MainTest() {
		assertFalse(DocEngine.running());
		DocEngine.main();
		DocEngine.main();
		assertTrue(DocEngine.running());
		assertEquals(1, DocEngine.stop());
		assertEquals(0, DocEngine.stop());
		assertFalse(DocEngine.running());
	}
}