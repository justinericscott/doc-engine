package com.github.justinericscott.docengine.controller;

import static com.github.justinericscott.docengine.controller.RestUtils.RestConstants.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

interface RestController<T, W> {

	@RequestMapping(method = DELETE, value = BY_ID)
	ResponseEntity<T> delete(@PathVariable(PARAM_ID) final Long id);
	
	@RequestMapping(method = DELETE, value = BY_CODE)
	ResponseEntity<T> delete(@PathVariable(PARAM_CODE) final String code);
	
	@RequestMapping(method = GET)
	ResponseEntity<W> findAll();
}
