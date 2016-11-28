package com.itgfirm.docengine.types.jpa;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public class TypeConstants {	
	public static final String JPA_DISCRIMINATOR_COLUMN = "DSCRMNTR_CD";
	public static final String JPA_DISCRIMINATOR_CONTENT = "B";
	public static final String JPA_DISCRIMINATOR_INSTANCE = "I";
	public static final String JPA_DISCRIMINATOR_DOCUMENT = "D";
	public static final String JPA_DISCRIMINATOR_ADV_DOCUMENT = "A";
	public static final String JPA_DISCRIMINATOR_SECTION = "S";
	public static final String JPA_DISCRIMINATOR_CLAUSE = "C";
	public static final String JPA_DISCRIMINATOR_PARAGRAPH = "P";
	public static final String JPA_DISCRIMINATOR_TOKEN = "T";
	public static final String JPA_DISCRIMINATOR_TOKEN_QUESTION = "Q";

	public static final String JPA_ENTITY_GRAPH_SECTIONS = "sections";
	public static final String JPA_ENTITY_GRAPH_CLAUSES = "clauses";
	public static final String JPA_ENTITY_GRAPH_PARAGRAPHS = "paragraphs";
	public static final String JPA_ENTITY_GRAPH_NAME_INIT = "Content.initialize";
	
	public static final String JSON_IDENTITY_PROPERTY = "@id";
	
	public static final String JPA_TABLE_CONTENT = "CONTENT_R";
	public static final String JPA_TABLE_INSTANCE = "CONTENT_INST";
	public static final String JPA_TABLE_TOKEN_DICTIONARY = "TOKEN_DICTIONARY_R";
}