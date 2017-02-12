package com.github.justinericscott.docengine.models;

import static com.github.justinericscott.docengine.models.AbstractJpaModel.ModelConstants.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@MappedSuperclass
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class AbstractJpaModel {

	@Column(name = JPA_COL_CREATED_BY, nullable = false)
	private Long createdBy = -1L;
	@CreationTimestamp
	@Column(name = JPA_COL_CREATED_DT, nullable = false)
	private Timestamp createdDt = new Timestamp(now().getTime());
	@Column(name = JPA_COL_UPDATED_BY, nullable = false)
	private Long updatedBy = -1L;
	@UpdateTimestamp
	@Column(name = JPA_COL_UPDATED_DT, nullable = false)
	private Timestamp updatedDt = new Timestamp(now().getTime());

	static Date now() {
		return new Date(Instant.now().toEpochMilli());
	}

	static Date max() {		
		return new Calendar.Builder().setDate(2117, 1, 1).build().getTime();
	}
	
	<T> List<T> toList() {
		return null;
	}
	
	<T> List<T> toList(Class<? extends T> type) {
		
		return null;
	}
	
	public AbstractJpaModel() {
		// Default constructor for Spring
	}

	public final Long getCreatedBy() {
		return createdBy;
	}

	public final void setCreatedBy(final Long createdBy) {
		this.createdBy = createdBy;
	}

	public final Timestamp getCreatedDt() {
		return createdDt;
	}

	public final void setCreatedDt(final Timestamp createdDt) {
		this.createdDt = createdDt;
	}

	public final Long getUpdatedBy() {
		return updatedBy;
	}

	public final void setUpdatedBy(final Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public final Timestamp getUpdatedDt() {
		return updatedDt;
	}

	public final void setUpdatedDt(final Timestamp updatedDt) {
		this.updatedDt = updatedDt;
	}
	
	abstract String toHTML();

 	public static final class ModelConstants {
		static final String EXCEL_COL_CONTENT_ID = "Content ID";
		static final String EXCEL_COL_PARENT = "Parent ID";
		static final String EXCEL_COL_CONTENT_CD = "Content Code";
		static final String EXCEL_COL_NAME = "Name";
		static final String EXCEL_COL_DISCRIMINATOR = "Discriminator";
		static final String EXCEL_COL_DESCRIPTION = "Description";
		static final String EXCEL_COL_CONTENT_NBR = "Content Number";
		static final String EXCEL_COL_BODY = "Content Body";
		static final String EXCEL_COL_CSS = "Content CSS";
		static final String EXCEL_COL_HELPER = "Helper Text";
		static final String EXCEL_COL_CATEGORY = "Category Code";
		static final String EXCEL_COL_FLAGS = "Flags";
		static final String EXCEL_COL_ORDER = "Order By";
		static final String EXCEL_COL_VALID_START = "Start Date";
		static final String EXCEL_COL_VALID_END = "End Date";
//		static final String EXCEL_COL_IS_FIRST = "Is First in a List";
//		static final String EXCEL_COL_IS_LAST = "Is Last in a List";
//		static final String EXCEL_COL_IS_OPTIONAL = "Is an Optional Paragraph";
//		static final String EXCEL_COL_IS_PARENT = "Is a Parent to a Sub-Paragraph";
//		static final String EXCEL_COL_IS_SUB = "Is a Sub-Paragraph";
		public static final String[] EXCEL_COLS_CONTENT = { EXCEL_COL_CONTENT_ID, EXCEL_COL_PARENT,
				EXCEL_COL_CONTENT_CD, EXCEL_COL_NAME, EXCEL_COL_DESCRIPTION, EXCEL_COL_CONTENT_NBR, EXCEL_COL_BODY,
				EXCEL_COL_CSS, EXCEL_COL_HELPER, EXCEL_COL_CATEGORY, EXCEL_COL_FLAGS, EXCEL_COL_ORDER,
				EXCEL_COL_VALID_START, EXCEL_COL_VALID_END };

		public static final String EXCEL_SHEET_NAME_CONTENT = "Content";
		public static final String EXCEL_SHEET_NAME_INSTANCE = "Instance";
		public static final String EXCEL_SHEET_NAME_TOKEN_DICTIONARY = "Token Dictionary";

		
		
		static final String HTML_CSS_TYPE_TEXT = "text/css";
		static final String HTML_STYLE_FLAG_FIRST = "FIRST";
		static final String HTML_STYLE_FLAG_FIRST_IN_CLAUSE = "1ST_IN_CLAUSE";
		static final String HTML_STYLE_FLAG_LAST = "LAST";
		static final String HTML_STYLE_FLAG_OPTIONAL = "OPT";
		static final String HTML_STYLE_FLAG_PARENT = "PARENT";
		static final String HTML_STYLE_FLAG_SUB = "SUB";
		
		
		static final String JPA_TBL_CONTENT = "CONTENT";
		static final String JPA_TBL_INSTANCE = "INSTANCE";
		static final String JPA_TBL_TOKEN_DICTIONARY = "TOKEN_DICTIONARY";

		static final String JPA_SEQ_CONTENT = JPA_TBL_CONTENT + "_SQ";
		static final String JPA_SEQ_INSTANCE = JPA_TBL_INSTANCE + "_SQ";
		static final String JPA_SEQ_TOKEN = JPA_TBL_TOKEN_DICTIONARY + "_SQ";

		static final String JPA_COL_ALT_TEXT = "ALT_TXT";
		static final String JPA_COL_ATTRIBUTE = "ATTR_TXT";
		static final String JPA_COL_BODY = "BODY_TXT";
		static final String JPA_COL_CATEGORY = "CATEGORY_CD";
		static final String JPA_COL_CONTENT_CD = "CONTENT_CD";
		static final String JPA_COL_CONTENT_ID = JPA_TBL_CONTENT + "_ID";
		static final String JPA_COL_CONTENT_NBR = "CONTENT_NBR";
		static final String JPA_COL_CREATED_BY = "CREATED_BY";
		static final String JPA_COL_CREATED_DT = "CREATED_DT";
		static final String JPA_COL_CUSTOM_BODY = "CUSTOM_BODY_TXT";
		static final String JPA_COL_CUSTOM_CONTENT_NBR = "CUSTOM_CONTENT_NBR";
		static final String JPA_COL_CSS = "CSS_TXT";
		static final String JPA_COL_DESCRIPTION = "DESC_TXT";
		static final String JPA_COL_DOCUMENT_CD = "DOCUMENT_CD";
		static final String JPA_COL_ENTITY = "ENTITY_TXT";
		static final String JPA_COL_FIELD_INSTR = "FIELD_INSTR_TXT";
		static final String JPA_COL_FIELD_LABEL = "FIELD_LABEL_TXT";
		static final String JPA_COL_FLAGS = "FLAGS_CD";
		static final String JPA_COL_HELPER = "HELPER_TXT";
		static final String JPA_COL_INSTANCE_ID = JPA_TBL_INSTANCE + "_ID";
		static final String JPA_COL_IS_AD_HOC = "IS_AD_HOC_BLN";
//		static final String JPA_COL_IS_FIRST = "IS_FIRST_BLN";
//		static final String JPA_COL_IS_LAST = "IS_LAST_BLN";
		static final String JPA_COL_IS_MARKED_FOR_ACTION = "IS_MARKED_FOR_ACTION_BLN";
//		static final String JPA_COL_IS_OPTIONAL = "IS_OPTIONAL_BLN";
//		static final String JPA_COL_IS_PARENT = "IS_PARENT_BLN";
		static final String JPA_COL_IS_ROMAN = "IS_ROMAN_BLN";
		static final String JPA_COL_IS_STRIKE_HEADER = "IS_STRIKE_HEADER_BLN";
//		static final String JPA_COL_IS_SUB = "IS_SUB_PARA_BLN";
		static final String JPA_COL_IS_X_ABLE = "IS_X_ABLE_BLN";
		static final String JPA_COL_MARKED_COMMENT = "MARKED_FOR_ACTION_COMMENT_TXT";
		static final String JPA_COL_NAME = "NAME_TXT";
		static final String JPA_COL_ORDER = "ORDER_BY";
		static final String JPA_COL_PARENT = "PARENT_ID";
		static final String JPA_COL_PHASE_CD = "PHASE_CD";
		static final String JPA_COL_PROCESS = "PROCESS_TXT";
		static final String JPA_COL_PROJECT = "PROJECT_NBR";
		static final String JPA_COL_STATUS = "STATUS_CD";
		static final String JPA_COL_TASK = "TASK_TXT";
		static final String JPA_COL_TOKEN_CD = "TOKEN_CD";
		static final String JPA_COL_TOKEN_ID = JPA_TBL_TOKEN_DICTIONARY + "_ID";
		static final String JPA_COL_UPDATED_BY = "UPDATED_BY";
		static final String JPA_COL_UPDATED_DT = "UPDATED_DT";
		static final String JPA_COL_VALID_END = "VALID_END_DT";
		static final String JPA_COL_VALID_START = "VALID_START_DT";
		static final String JPA_COL_WHERE = "WHERE_TXT";

		static final String JPA_DSCRMNTR_COL = "DTYPE";
		static final String JPA_DSCRMNTR_ADV_DOCUMENT = "A";
		static final String JPA_DSCRMNTR_CLAUSE = "C";
		static final String JPA_DSCRMNTR_CONTENT = "B";
		static final String JPA_DSCRMNTR_DOCUMENT = "D";
		static final String JPA_DSCRMNTR_INSTANCE = "I";
		static final String JPA_DSCRMNTR_PARAGRAPH = "P";
		static final String JPA_DSCRMNTR_QUESTION = "Q";
		static final String JPA_DSCRMNTR_SECTION = "S";
		static final String JPA_DSCRMNTR_TOKEN = "T";

		static final String JPA_MAPPED_BY_CLAUSE = "clause";
		static final String JPA_MAPPED_BY_CLAUSE_INST = "clauseInstance";
		static final String JPA_MAPPED_BY_CONTENT = "content";
		static final String JPA_MAPPED_BY_DOCUMENT = "document";
		static final String JPA_MAPPED_BY_DOCUMENT_INST = "documentInstance";
		static final String JPA_MAPPED_BY_SECTION = "section";
		static final String JPA_MAPPED_BY_SECTION_INST = "sectionInstance";

//		static final String JSON_PROP_ID = "@id";

		static final String STATUS_AUTO_IN = "Automatically Included";
		static final String STATUS_AUTO_OUT = "Automatically Excluded";
		static final String STATUS_MAN_IN = "Manually Included";
		static final String STATUS_MAN_OUT = "Manually Excluded";
		static final String STATUS_PENDING = "Pending Review";
		static final String[] STATUSES_IN = { STATUS_AUTO_IN, STATUS_MAN_IN };
		static final String[] STATUSES_OUT = { STATUS_AUTO_OUT, STATUS_MAN_OUT, STATUS_PENDING };
	}
}