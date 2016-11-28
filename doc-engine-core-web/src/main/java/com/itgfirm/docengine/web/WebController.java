/**
 * TODO: License
 */
package com.itgfirm.docengine.web;

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

	@RequestMapping(value = RestUrls.ROOT)
	public String index() {
		// TODO: Trace log entry
		return CoreWebConstants.VIEW_RESOLVER_INDEX;
	}
}