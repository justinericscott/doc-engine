/**
 * TODO: License
 */
package com.github.justinericscott.docengine.controller;

import static com.github.justinericscott.docengine.controller.RestUtils.RestConstants.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Controller
public class WebController {
	private static final String VIEW_RESOLVER_INDEX = "index";

	@RequestMapping(ROOT)
	public String index() {
		// TODO: Trace log entry
		return VIEW_RESOLVER_INDEX;
	}
}