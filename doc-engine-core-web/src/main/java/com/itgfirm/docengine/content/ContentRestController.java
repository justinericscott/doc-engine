package com.itgfirm.docengine.content;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.types.jpa.Contents;
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
class ContentRestController {
	@Autowired @Qualifier("default")
	private ContentService service;

	/**
	 * Gets all Content records.
	 * 
	 * @return All Content records.
	 * @throws JsonProcessingException
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENTS)
	ResponseEntity<List<Content>> get() {
		return new ResponseEntity<List<Content>>(service.get(), HttpStatus.OK);
	}

	/**
	 * Gets a single piece of Content based upon the provided ID.
	 * 
	 * @param id
	 * @return Content record related to the provided ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.BY_ID)
	ResponseEntity<Content> get(@PathVariable(RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<Content>(service.get(id), HttpStatus.OK);
	}

	/**
	 * Gets a single piece of Content based upon the provided content code.
	 * 
	 * @param code
	 * @return Content record related to the provided content code.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.BY_CODE)
	ResponseEntity<Content> get(@PathVariable(RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<Content>(service.get(code), HttpStatus.OK);
	}

	/**
	 * Gets a list of Content records based upon the provided content code search string.
	 * 
	 * @param like
	 * @return Content records related to the content code search string.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT
			+ RestUrls.BY_CODE_LIKE)
	ResponseEntity<List<Content>> getByCodeLike(@PathVariable(RestUrls.PARAM_LIKE) String like) {
		return new ResponseEntity<List<Content>>(service.getByCodeLike(like), HttpStatus.OK);
	}

	/**
	 * Merges (adds or updates) a Content record.
	 * 
	 * @param content
	 * @return The newly merged Content, with created ID if new.
	 */
	@RequestMapping(method = RequestMethod.PUT, value = RestUrls.CONTENT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ContentJpaImpl> merge(@RequestBody ContentJpaImpl content) {
		return new ResponseEntity<ContentJpaImpl>((ContentJpaImpl) service.merge(content),
				HttpStatus.OK);
	}

	/**
	 * Merges (adds or updates) a list of Contents.
	 * 
	 * @param contents
	 * @return The newly merged list of items.
	 */
	@RequestMapping(method = RequestMethod.PUT, value = RestUrls.CONTENTS,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Contents> merge(@RequestBody Contents content) {
		return new ResponseEntity<Contents>(
				new Contents(service.merge(content.getContents())), HttpStatus.OK);
	}

	/**
	 * Deletes a Content record based upon the provided ID.
	 * 
	 * @param id
	 * @return True or False based upon success of the operation.
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = RestUrls.CONTENT + RestUrls.BY_ID)
	ResponseEntity<Boolean> delete(@PathVariable(value = RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<Boolean>(service.delete(id), HttpStatus.OK);
	}

	/**
	 * Deletes a Content record based upon the provided content code.
	 * 
	 * @param code
	 * @return True or False based upon success of the operation.
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = RestUrls.CONTENT + RestUrls.BY_CODE)
	ResponseEntity<Boolean> delete(@PathVariable(value = RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<Boolean>(service.delete(code), HttpStatus.OK);
	}

	/**
	 * Deletes a Content record based upon the provided content code search string.
	 * 
	 * @param like
	 * @return True or False based upon success of the operation.
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = RestUrls.CONTENT
			+ RestUrls.BY_CODE_LIKE)
	ResponseEntity<Boolean> deleteByCodeLike(
			@PathVariable(value = RestUrls.PARAM_LIKE) String like) {
		return new ResponseEntity<Boolean>(service.deleteByCodeLike(like), HttpStatus.OK);
	}
}