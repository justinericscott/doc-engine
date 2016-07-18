package com.itgfirm.docengine.web;

/**
 * @author Justin Scott
 * 
 *         Content Service REST URLs
 */
public class RestUrls {
	public static final String ROOT = "/";

	/** SERVICES **/
	public static final String CONTENT = "/content";
	public static final String CONTENTS = "/contents";
	public static final String INSTANCE = "/instance";
	public static final String INSTANCES = "/instances";
	public static final String TOKEN = "/token";
	public static final String TOKENS = "/tokens";
	public static final String CREATE = "/create";
	public static final String COMPILE = "/compile";

	/** CLASSES **/
	public static final String CHILDREN = "/children";
	public static final String CHILDREN_EAGER_KIDS = "/children/eager-kids";
	public static final String DOCUMENT = "/document";
	public static final String DOCUMENT_EAGER_KIDS = "/document/eager-kids";
	public static final String SECTION = "/section";
	public static final String SECTION_EAGER_KIDS = "/section/eager-kids";
	public static final String CLAUSE = "/clause";
	public static final String CLAUSE_EAGER_KIDS = "/clause/eager-kids";
	public static final String PARAGRAPH = "/paragraph";
	public static final String PARAGRAPH_EAGER_KIDS = "/paragraph/eager-kids";

	/** FIELDS **/
	public static final String PARAM_ID = "id";
	public static final String PARAM_CODE = "code";
	public static final String PARAM_LIKE = "like";
	public static final String PARAM_PROJECT_ID = "projectId";

	/** DISCRIMINATORS **/
	public static final String BY_ID = "/" + PARAM_ID + "/{" + PARAM_ID + "}";
	public static final String BY_CODE = "/" + PARAM_CODE + "/{" + PARAM_CODE + "}";
	public static final String BY_CODE_LIKE = "/" + PARAM_CODE + "/" + PARAM_LIKE + "/{"
			+ PARAM_LIKE + "}";
	public static final String BY_PROJECT_ID = "/" + PARAM_PROJECT_ID + "/{"
			+ PARAM_PROJECT_ID + "}";

	/** OTHER **/
	public static final String USER = "/user";

	public RestUrls() {}
}