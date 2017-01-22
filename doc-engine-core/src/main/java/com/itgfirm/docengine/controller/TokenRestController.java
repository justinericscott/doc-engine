/**
 * TODO: License
 */
package com.itgfirm.docengine.controller;

import static com.itgfirm.docengine.controller.RestUtils.RestConstants.*;
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

import com.itgfirm.docengine.service.token.TokenDictionaryService;
import com.itgfirm.docengine.types.TokenDefinitionJpaImpl;
import com.itgfirm.docengine.types.TokenDefinitions;

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
		LOG.info("Creating new Token REST Controller.");
	}
	
	@RequestMapping(method = GET)
	final ResponseEntity<TokenDefinitions> findAll() {
		return new ResponseEntity<TokenDefinitions>(_dictionary.findAll(), OK);
	}

	@RequestMapping(method = GET, value = BY_ID)
	final ResponseEntity<TokenDefinitionJpaImpl> findOne(@PathVariable(PARAM_ID) final Long id) {
		return new ResponseEntity<TokenDefinitionJpaImpl>(_dictionary.findOne(id), OK);
	}

	@RequestMapping(method = PUT)
	final ResponseEntity<TokenDefinitionJpaImpl> save(@RequestBody final TokenDefinitionJpaImpl item) {
		return new ResponseEntity<TokenDefinitionJpaImpl>(_dictionary.save(item), OK);
	}
}