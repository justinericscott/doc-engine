/**
 * TODO: License
 */
package com.itgfirm.docengine.token;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itgfirm.docengine.types.TokenDefinition;
import com.itgfirm.docengine.web.RestUrls;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@RestController
class TokenRestController {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(TokenRestController.class);

	/** TODO: Description **/
	@Autowired
	private TokenDictionaryService tokenDictionaryService;

	/**
	 * TODO: Description
	 * 
	 * @return {@link ResponseEntity}<{@link List}<{@link TokenDictionaryItem}>>
	 */
	@RequestMapping(value = RestUrls.TOKENS, method = RequestMethod.GET)
	public ResponseEntity<List<TokenDefinition>> getAll() {
		// TODO: Trace log entry
		return new ResponseEntity<List<TokenDefinition>>(this.tokenDictionaryService.get(),
				HttpStatus.OK);
	}

	/**
	 * TODO: Description
	 * 
	 * @param id
	 * @return {@link ResponseEntity}<{@link TokenDictionaryItem}>
	 */
	@RequestMapping(value = RestUrls.TOKEN + RestUrls.BY_ID, method = RequestMethod.GET)
	public ResponseEntity<TokenDefinition> getById(@PathVariable("id") Long id) {
		// TODO: Trace log entry
		return new ResponseEntity<TokenDefinition>(this.tokenDictionaryService.get(id),
				HttpStatus.OK);
	}

	/**
	 * TODO: Description
	 * 
	 * @param item
	 * @return {@link ResponseEntity}<{@link TokenDictionaryItem}>
	 */
	@RequestMapping(value = RestUrls.TOKEN, method = RequestMethod.POST)
	public ResponseEntity<TokenDefinition> merge(@RequestBody TokenDefinition item) {
		// TODO: Trace log entry
		return new ResponseEntity<TokenDefinition>(this.tokenDictionaryService.merge(item),
				HttpStatus.OK);
	}
}