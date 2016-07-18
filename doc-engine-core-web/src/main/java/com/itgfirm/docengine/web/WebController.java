/**
 * TODO: License
 */
package com.itgfirm.docengine.web;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itgfirm.docengine.util.CoreWebConstants;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Controller
public class WebController {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(WebController.class);

	@RequestMapping(value = RestUrls.ROOT)
	public String index() {
		// TODO: Trace log entry
		return CoreWebConstants.VIEW_RESOLVER_INDEX;
	}
}