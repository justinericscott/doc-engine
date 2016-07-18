/**
 * TODO: License
 */
package com.itgfirm.docengine.compiler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itgfirm.docengine.types.AdvancedDocumentInstance;
import com.itgfirm.docengine.web.RestUrls;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@RestController
class CompilerRestController {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(CompilerRestController.class);
	@Autowired
	private CompilerService compilerService;

	/**
	 * TODO: Description
	 * 
	 * @param id
	 * @return {@link ResponseEntity}<{@link String}>
	 */
	@RequestMapping(method = RequestMethod.POST, value = RestUrls.INSTANCE + RestUrls.COMPILE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> compileDocumentInstance(
			@RequestBody AdvancedDocumentInstance instance) {
		return new ResponseEntity<String>(compilerService.compileDocument(instance),
				HttpStatus.OK);
	}
}