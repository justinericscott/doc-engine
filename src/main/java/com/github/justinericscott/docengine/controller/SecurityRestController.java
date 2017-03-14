/**
 * TODO: License
 */
package com.github.justinericscott.docengine.controller;

import static com.github.justinericscott.docengine.controller.RestUtils.RestConstants.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@RestController
public class SecurityRestController {
	private static final Logger LOG = LoggerFactory.getLogger(SecurityRestController.class);

	/**
	 * TODO: Description
	 */
	public SecurityRestController() {}

	/**
	 * TODO: Description
	 * 
	 * @param user
	 * @return {@link Principal}
	 */
	@RequestMapping(value = USER, method = GET)
	public Principal user(Principal user) {
		// TODO: Trace log entry
		if (user != null && user.getName() != null) {
			LOG.debug("USER NAME !!!!!!!!! " + user.getName());
		} else {
			LOG.debug("NO USER !!!!!!!!!");
		}
		return user;
	}
}