package com.github.justinericscott.docengine;

import static com.github.justinericscott.docengine.DocEngine.Constants.*;
import static org.junit.Assert.*;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.github.justinericscott.docengine.DocEngine;

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