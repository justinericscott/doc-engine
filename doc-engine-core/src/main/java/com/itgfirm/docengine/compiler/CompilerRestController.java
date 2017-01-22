/**
 * TODO: License
 */
package com.itgfirm.docengine.compiler;

import static com.itgfirm.docengine.controller.RestUtils.RestConstants.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itgfirm.docengine.types.DocumentInstanceJpaImpl;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@RestController
class CompilerRestController {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(CompilerRestController.class);
	
	@Autowired
	private CompilerService service;

	/**
	 * TODO: Description
	 * 
	 * @param id
	 * @return {@link ResponseEntity}<{@link String}>
	 */
	@RequestMapping(method = POST, value = INSTANCE + COMPILE)
	public final ResponseEntity<String> compileDocumentInstance(@RequestBody final DocumentInstanceJpaImpl instance) {
		return new ResponseEntity<String>(service.compileDocument(instance), OK);
	}
}