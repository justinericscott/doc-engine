/**
 * TODO: License
 */
package com.github.justinericscott.docengine.controller;

import static com.github.justinericscott.docengine.controller.RestUtils.RestConstants.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.justinericscott.docengine.models.DocumentInstance;
import com.github.justinericscott.docengine.service.content.CompilerService;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
//@RestController
@Deprecated
@SuppressWarnings("unused")
class CompilerRestController {
	
	private static final Logger LOG = LoggerFactory.getLogger(CompilerRestController.class);
	
	@Autowired
	private CompilerService service;

	@RequestMapping(method = POST, value = INSTANCE + COMPILE)
	public final ResponseEntity<String> compileDocumentInstance(@RequestBody final DocumentInstance instance) {
		return new ResponseEntity<String>(service.compileDocument(instance), OK);
	}
}