/**
 * TODO: License
 */
package com.itgfirm.docengine.compiler;

import org.apache.commons.lang.StringUtils;

/**
 * @author Justin
 *
 */
public class CompilerUtils {

	/**
	 * TODO: Description
	 */
	private CompilerUtils() {
		// Do not instantiate
	}

	/**
	 * Creates a self-closing HTML tag.
	 * 
	 * @param htmlTag
	 *            {@link String} - HTML tag to make.
	 * @return {@link String} - HTML tag.
	 */
	public static String htmlSelfCloseTag(String htmlTag) {
		if (htmlTag == null)
			throw new IllegalArgumentException("HTML Tag Must Not Be Null");
		return htmlSelfCloseTag(htmlTag, null);
	}

	/**
	 * Creates a self-closing HTML tag with a CSS class.
	 * 
	 * @param htmlTag
	 *            {@link String} - HTML tag to make.
	 * @param cssClass
	 *            {@link String} - CSS class name to use.
	 * @return {@link String} - HTML tag with CSS.
	 */
	public static String htmlSelfCloseTag(String htmlTag, String cssClass) {
		if (htmlTag == null)
			throw new IllegalArgumentException("HTML Tag Must Not Be Null");
		if (cssClass == null) {
			return String.format("<%s />", htmlTag);
		} else {
			return String.format("<%s class=\"%s\"/>", htmlTag, cssClass);
		}
	}

	/**
	 * Creates the HTML equivalent of "tab" on the keyboard, also known as indent.
	 * 
	 * @param count
	 *            {@code int} - The amount of tabs/indents to create.
	 * @return {@link String} - HTML indents.
	 */
	public static String htmlTab(int count) {
		return StringUtils.repeat("&nbsp;", count);
	}

	/**
	 * Wraps the given content in HTML tags.
	 * 
	 * @param htmlTag
	 *            {@link String} - HTML tag to use.
	 * @param content
	 *            {@link String} - ContentRepo to wrap in tags.
	 * @return {@link String} - HTML content.
	 */
	public static String htmlTag(String htmlTag, String content) {
		if (content == null)
			throw new IllegalArgumentException("ContentRepo Must Not Be Null!");
		return htmlTag(htmlTag, content, null);
	}

	/**
	 * Wraps the given content in HTML tags with CSS.
	 * 
	 * @param htmlTag
	 *            {@link String} - HTML tag to use.
	 * @param content
	 *            {@link String} - ContentRepo to wrap in tags.
	 * @param cssClass
	 *            {@link String} - CSS class to use.
	 * @return {@link String} - HTML content.
	 */
	public static String htmlTag(String htmlTag, String content, String cssClass) {
		if (content == null)
			throw new IllegalArgumentException("ContentRepo Must Not Be Null!");
		if (cssClass == null && htmlTag == null) {
			return content;
		} else if (htmlTag == null) {
			return content;
		} else if (cssClass == null && htmlTag != null) {
			return String.format("<%s>%s</%s>", htmlTag, content, htmlTag);
		} else {
			return String.format("<%s class=\"%s\">%s</%s>", htmlTag, cssClass, content,
					htmlTag);
		}
	}
}
