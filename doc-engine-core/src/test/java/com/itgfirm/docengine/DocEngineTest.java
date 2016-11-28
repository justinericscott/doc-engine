package com.itgfirm.docengine;

import static org.junit.Assert.*;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.itgfirm.docengine.util.Constants;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DocEngineTest {

	@Test
	public void aa_MainTest() {
		assertFalse(DocEngine.running());
		DocEngine.main();
		DocEngine.main();
		assertTrue(DocEngine.running());
		DocEngine.main(new String[] { Constants.ENGINE_CONTROL_STOP });
		assertFalse(DocEngine.running());
	}
}