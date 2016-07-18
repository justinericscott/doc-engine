/**
 * TODO: License
 */
package com.itgfirm.docengine.security;

import java.security.Principal;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itgfirm.docengine.web.RestUrls;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@RestController
public class SecurityRestController {
	private static final Logger LOG = LogManager.getLogger(SecurityRestController.class);

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
	@RequestMapping(value = RestUrls.USER, method = RequestMethod.GET)
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