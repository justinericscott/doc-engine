/**TODO: License
 */
package com.itgfirm.docengine.document;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.itgfirm.docengine.instance.InstanceService;
import com.itgfirm.docengine.types.AdvancedDocumentInstance;
import com.itgfirm.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * TODO: Description
 */
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class DocumentServiceTest extends AbstractTest {
	public static final Logger LOG = LogManager.getLogger(DocumentServiceTest.class);
	@Autowired
	private DocumentService documentService;
	@Autowired
	private InstanceService instanceService;
	
	@Test
	public void aa_CreateTest() {
		AdvancedDocumentInstance doc = (AdvancedDocumentInstance) documentService.create("0NJ1234", "R101");
		if (doc != null) {
			assertNotNull(doc.getId());
			instanceService.delete(doc);
			assertNull(instanceService.get(doc.getId()));
		}		
	}
}