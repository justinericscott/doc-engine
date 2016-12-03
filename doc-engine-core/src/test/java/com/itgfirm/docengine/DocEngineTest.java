package com.itgfirm.docengine;

import static org.junit.Assert.*;
import static com.itgfirm.docengine.DocEngine.Constants.*;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DocEngineTest {

	@Test
	public void aa_MainTest() {
		assertFalse(DocEngine.running());
		DocEngine.main();
		DocEngine.main();
		assertTrue(DocEngine.running());
		DocEngine.main(new String[] { ENGINE_CONTROL_STOP });
		assertFalse(DocEngine.running());
	}
}