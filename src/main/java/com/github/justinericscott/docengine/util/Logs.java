package com.github.justinericscott.docengine.util;

public class Logs {

	public static final String LOG_BASE = "The object is a member of the default class, {}. Returning as-is.";
	public static final String LOG_ERROR_CAST = "Problem attempting to cast values!";
	public static final String LOG_ERROR_DELETE = "Problem trying to delete item!";
	public static final String LOG_ERROR_READ_JSON = "Problem reading JSON message for endpoint %s! Message:\n%s";
	public static final String LOG_TYPE_HANDLING = "You may want to use the AdvanceContentRestClientImpl, it has significantly better type handling.";
	public static final String LOG_NONE = "The object is not a sub-class or member of the default class, {}. Actual class is {}. Retuning null";
	public static final String LOG_NO_OP_BY_CODE = "The method, findByCode, you are trying to use has not been implemented by the contrete class you are using, try the ContentRestClientImpl or AdvancedContentRestClientImpl classes.";
	public static final String LOG_NO_OP_BY_CODE_LIKE = "The method, findByCodeLike, you are trying to use has not been implemented by the contrete class you are using, try the ContentRestClientImpl or AdvancedContentRestClientImpl class.";
	public static final String LOG_NO_OP_BY_PROJECT_ID_CODE = "The method, findByProjectIdAndCode, you are trying to use has not been implemented by the contrete class you are using, try the AdvancedContentRestClientImpl class.";
	public static final String LOG_NO_OP_BY_PROJECT_ID_LIKE_TYPE = "The method, findByProjectIdAndCodeLikeWithType, you are trying to use has not been implemented by the contrete class you are using, try the AdvancedContentRestClientImpl class.";
	public static final String LOG_NO_OP_BY_PROJECT_ID_CODE_TYPE = "The method, findByProjectIdAndCodeWithType, you are trying to use has not been implemented by the contrete class you are using, try the AdvancedContentRestClientImpl class.";
	public static final String LOG_NO_OP_BY_PROJECT_ID_CODE_TYPE_KIDS = "The method, findByProjectIdAndCodeWithTypeAndEagerKids, you are trying to use has not been implemented by the contrete class you are using, try the AdvancedContentRestClientImpl class.";
	public static final String LOG_NO_OP_ONE_TYPE_KIDS = "The method, findOneWithTypeAndEagerKids, you are trying to use has not been implemented by the contrete class you are using, try the AdvancedContentRestClientImpl class.";
	public static final String LOG_NO_OP_CHILDREN_BY_ID_TYPE = "The method, getChildren, you are trying to use has not been implemented by the contrete class you are using, try the AdvancedContentRestClientImpl class.";
	public static final String LOG_NO_OP_CHILDREN_BY_ID_TYPE_KIDS = "The method, getChildren, you are trying to use has not been implemented by the contrete class you are using, try the AdvancedContentRestClientImpl class.";
	public static final String LOG_NO_OP_CHILDREN_BY_CODE_TYPE = "The method, getChildren, you are trying to use has not been implemented by the contrete class you are using, try the AdvancedContentRestClientImpl class.";
	public static final String LOG_NO_OP_CHILDREN_BY_CODE_TYPE_KIDS = "The method, getChildren, you are trying to use has not been implemented by the contrete class you are using, try the AdvancedContentRestClientImpl class.";
	public static final String LOG_NO_OP_CHILDREN_BY_PROJECT_ID_CODE_TYPE = "The method, getChildren, you are trying to use has not been implemented by the contrete class you are using, try the AdvancedContentRestClientImpl class.";
	public static final String LOG_NO_OP_CHILDREN_BY_PROJECT_ID_CODE_TYPE_KIDS = "The method, getChildren, you are trying to use has not been implemented by the contrete class you are using, try the AdvancedContentRestClientImpl class.";
	public static final String LOG_NO_SEARCH_RESULT = "Search for {} using {} as the term did not provide any results.";
	public static final String LOG_NULL_CLASS = "Class must not be null!";
	public static final String LOG_NULL_CODE = "Content code must not be null or empty!";
	public static final String LOG_NULL_DESTINATION = "Destination must not be null or empty!";
	public static final String LOG_NULL_CONTENT_ID = "Content ID must not be null or zero!";
	public static final String LOG_NULL_HTTP_ENTITY = "HTTP entity must not be null!";
	public static final String LOG_NULL_INSTANCE_ID = "Instance ID must not be null or zero!";
	public static final String LOG_NULL_LIKE = "The search string must not be null or empty!";
	public static final String LOG_NULL_OBJECT_DELETE = "The object you are attempting to delete must not be null!";
	public static final String LOG_NULL_OBJECT_SAVE = "The object you are attempting to save must not be null!";
	public static final String LOG_NULL_PROJECT_ID = "The project ID must not be null!";
	public static final String LOG_NULL_REL_PATH = "Relative path may be null, but, it is not common. You may want to investigate this if you are encountering a problem!";
	public static final String LOG_NULL_TYPE = "The type must not be null!";
	public static final String LOG_SUB = "The object is a sub-class, {}, of the base/default class, {}. Returning as-is. This will need to be cast into the desired type.";

}
