/**TODO: License
 */
package com.itgfirm.docengine.document;

import static org.junit.Assert.*;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.itgfirm.docengine.service.InstanceService;
import com.itgfirm.docengine.types.jpa.AdvancedDocumentInstanceJpaImpl;
import com.itgfirm.docengine.util.AbstractTest;

/**
 * @author Justin Scott
 * TODO: Description
 */
@FixMethodOrder( MethodSorters.NAME_ASCENDING )
public class DocumentServiceTest extends AbstractTest {
	public static final Logger LOG = LoggerFactory.getLogger(DocumentServiceTest.class);
	@Autowired
	private DocumentService documentService;
	@Autowired
	private InstanceService instanceService;
	
	@Test
	public void aa_CreateTest() {
		AdvancedDocumentInstanceJpaImpl doc = (AdvancedDocumentInstanceJpaImpl) documentService.create("0NJ1234", "R101");
		if (doc != null) {
			assertNotNull(doc.getId());
			instanceService.delete(doc);
			assertNull(instanceService.findOne(doc.getId()));
		}		
	}
}