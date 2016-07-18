package com.itgfirm.docengine.document;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itgfirm.docengine.types.Instance;
import com.itgfirm.docengine.web.RestUrls;

/**
 * @author Justin Scott
 * 
 *         Represents a RESTful endpoint for internal and external services to perform CRUD
 *         operations on Content data. The controller "class" provides a standardized layer of
 *         abstraction, consumers need not "know" about the Content Service.
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
class DocumentRestController {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(DocumentRestController.class);
	@Autowired
	private DocumentService service;

	public DocumentRestController() {}

	@RequestMapping(method = RequestMethod.POST, value = RestUrls.INSTANCE + RestUrls.CREATE
			+ RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE)
	ResponseEntity<Instance> create(@PathVariable(RestUrls.PARAM_PROJECT_ID) String projectId,
			@PathVariable(RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<Instance>(service.create(projectId, code), HttpStatus.OK);
	}
}