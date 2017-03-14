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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.justinericscott.docengine.models.TokenDefinition;
import com.github.justinericscott.docengine.models.TokenDefinitions;
import com.github.justinericscott.docengine.service.token.TokenDictionaryService;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@RestController
@RequestMapping(TOKEN)
final class TokenRestController {
	private static final Logger LOG = LoggerFactory.getLogger(TokenRestController.class);

	@Autowired
	private TokenDictionaryService _dictionary;

	TokenRestController() {
		LOG.trace("Creating new Token REST Controller.");
	}
	
	@RequestMapping(method = GET, value = TOKENS)
	final ResponseEntity<TokenDefinitions> findAll() {
		return new ResponseEntity<TokenDefinitions>(_dictionary.findAll(), OK);
	}

	@RequestMapping(method = GET, value = BY_ID)
	final ResponseEntity<TokenDefinition> findOne(@PathVariable(PARAM_ID) final Long id) {
		return new ResponseEntity<TokenDefinition>(_dictionary.findOne(id), OK);
	}

	@RequestMapping(method = PUT)
	final ResponseEntity<TokenDefinition> save(@RequestBody final TokenDefinition item) {
		return new ResponseEntity<TokenDefinition>(_dictionary.save(item), OK);
	}
}