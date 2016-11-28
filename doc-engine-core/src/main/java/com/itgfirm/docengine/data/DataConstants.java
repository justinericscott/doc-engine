package com.itgfirm.docengine.data;

/**
 * @author Justin Scott
 * 
 *         DAO related constants
 */
public class DataConstants {
	/** ########## COMMON FIELD NAMES ########## **/
	public static final String PARAM_BODY = "body";
	public static final String PARAM_CONTENT_CD = "contentCd";
//	public static final String PARAM_PARENT = "parent";
	public static final String PARAM_DOCUMENT = "document";
	public static final String PARAM_SECTION = "section";
	public static final String PARAM_CLAUSE = "clause";
	public static final String PARAM_PARAGRAPH = "paragraph";
	public static final String PARAM_PROJECT_ID = "projectId";
	public static final String PARAM_TOKEN_CD = "tokenCd";
	public static final String PARAM_IS_XABLE_BLN = "isXable";

	/** ########## OBJECT NAMES ########## **/
//	public static final String OBJECT_CONTENT = "ContentJpaImpl";
	public static final String OBJECT_INSTANCE = "InstanceJpaImpl";
//	public static final String OBJECT_DOCUMENT = "DocumentJpaImpl";
	public static final String OBJECT_DOCUMENT_INSTANCE = "DocumentInstanceJpaImpl";
//	public static final String OBJECT_SECTION = "SectionJpaImpl";
	public static final String OBJECT_SECTION_INSTANCE = "SectionInstanceJpaImpl";
//	public static final String OBJECT_CLAUSE = "ClauseJpaImpl";
	public static final String OBJECT_CLAUSE_INSTANCE = "ClauseInstanceJpaImpl";
//	public static final String OBJECT_PARAGRAPH = "ParagraphJpaImpl";
	public static final String OBJECT_PARAGRAPH_INSTANCE = "ParagraphInstanceJpaImpl";
	public static final String OBJECT_TOKEN_DEFINITION = "TokenDefinitionJpaImpl";

	/** ########## CONTENT QUERIES ########## **/
	/** BY CODE **/
//	public static final String GET_CONTENT_BY_CONTENT_CD = "from " + OBJECT_CONTENT
//			+ " where " + PARAM_CONTENT_CD + " = :" + PARAM_CONTENT_CD;

	/** BY PARENT **/
//	public static final String GET_SECTIONS_BY_PARENT = "from " + OBJECT_SECTION + " where "
//			+ PARAM_DOCUMENT + " = :" + PARAM_DOCUMENT;
//	public static final String GET_CLAUSES_BY_PARENT = "from " + OBJECT_CLAUSE + " where "
//			+ PARAM_SECTION + " = :" + PARAM_SECTION;
//	public static final String GET_PARAGRAPHS_BY_PARENT = "from " + OBJECT_PARAGRAPH
//			+ " where " + PARAM_CLAUSE + " = :" + PARAM_CLAUSE;

	/** BY CODE / BODY LIKE **/
//	public static final String GET_CONTENTS_BY_CODE_LIKE = "from " + OBJECT_CONTENT
//			+ " where " + PARAM_CONTENT_CD + " like :" + PARAM_CONTENT_CD;
//	public static final String GET_CONTENTS_BY_CONTENT_CD_AND_BODY_LIKE = "from "
//			+ OBJECT_CONTENT + " where " + PARAM_CONTENT_CD + " like :" + PARAM_CONTENT_CD
//			+ " and " + PARAM_BODY + " like :" + PARAM_BODY;

	/** ########## INSTANCE QUERIES ########## **/
	/** BY PROJECT ID AND CODE **/
	public static final String GET_INSTANCE_BY_PROJECT_ID_AND_CONTENT_CD = "from "
			+ OBJECT_INSTANCE + " as i " + " where i." + PARAM_PROJECT_ID + " =:"
			+ PARAM_PROJECT_ID + " and i.content." + PARAM_CONTENT_CD + " =:"
			+ PARAM_CONTENT_CD;
	public static final String GET_DOCUMENT_INSTANCE_BY_PROJECT_ID_AND_CONTENT_CD = "from "
			+ OBJECT_DOCUMENT_INSTANCE + " as i " + " where i.content." + PARAM_CONTENT_CD
			+ " = :" + PARAM_CONTENT_CD + " and i." + PARAM_PROJECT_ID + " = :"
			+ PARAM_PROJECT_ID;
	public static final String GET_SECTION_INSTANCE_BY_PROJECT_ID_AND_CONTENT_CD = "from "
			+ OBJECT_SECTION_INSTANCE + " as i " + " where i.content." + PARAM_CONTENT_CD
			+ " = :" + PARAM_CONTENT_CD + " and i." + PARAM_PROJECT_ID + " = :"
			+ PARAM_PROJECT_ID;
	public static final String GET_CLAUSE_INSTANCE_BY_PROJECT_ID_AND_CONTENT_CD = "from "
			+ OBJECT_CLAUSE_INSTANCE + " as i " + " where i.content." + PARAM_CONTENT_CD
			+ " = :" + PARAM_CONTENT_CD + " and i." + PARAM_PROJECT_ID + " = :"
			+ PARAM_PROJECT_ID;
	public static final String GET_PARAGRAPH_INSTANCE_BY_PROJECT_ID_AND_CONTENT_CD = "from "
			+ OBJECT_PARAGRAPH_INSTANCE + " as i " + " where i.content." + PARAM_CONTENT_CD
			+ " = :" + PARAM_CONTENT_CD + " and i." + PARAM_PROJECT_ID + " = :"
			+ PARAM_PROJECT_ID;

	/** BY PARENT **/
	public static final String GET_SECTION_INSTANCES_BY_PARENT = "from "
			+ OBJECT_SECTION_INSTANCE + " where " + PARAM_DOCUMENT + " = :" + PARAM_DOCUMENT;
	public static final String GET_CLAUSE_INSTANCES_BY_PARENT = "from "
			+ OBJECT_CLAUSE_INSTANCE + " where " + PARAM_SECTION + " = :" + PARAM_SECTION;
	public static final String GET_PARAGRAPH_INSTANCES_BY_PARENT = "from "
			+ OBJECT_PARAGRAPH_INSTANCE + " where " + PARAM_CLAUSE + " = :" + PARAM_CLAUSE;

	/** BY PROJECT ID AND CODE LIKE **/
	public static final String GET_INSTANCES_BY_PROJECT_AND_CODE_LIKE = "from "
			+ OBJECT_INSTANCE + " as i " + " where i.content." + PARAM_CONTENT_CD + " like :"
			+ PARAM_CONTENT_CD + " and i." + PARAM_PROJECT_ID + " = :" + PARAM_PROJECT_ID;

	/** ########## TOKEN DEFINITION QUERIES ########## **/
	/** BY CODE **/
	public static final String GET_TOKEN_DEF_BY_TOKEN_CD = "from " + OBJECT_TOKEN_DEFINITION
			+ " where " + PARAM_TOKEN_CD + " = :" + PARAM_TOKEN_CD;

	/** BY CODE LIKE **/
	public static final String GET_TOKEN_DEF_BY_CODE_LIKE = "from " + OBJECT_TOKEN_DEFINITION
			+ " where " + PARAM_TOKEN_CD + " like :" + PARAM_TOKEN_CD;

	/** BY CODE LIKE AND IS X-ABLE **/
	public static final String GET_TOKEN_DEFS_BY_TOKEN_CD_LIKE_AND_IS_X_ABLE = "from "
			+ OBJECT_TOKEN_DEFINITION + " where " + PARAM_TOKEN_CD + " like :"
			+ PARAM_TOKEN_CD + " and " + PARAM_IS_XABLE_BLN + " = :" + PARAM_IS_XABLE_BLN;

	private DataConstants() {}
}