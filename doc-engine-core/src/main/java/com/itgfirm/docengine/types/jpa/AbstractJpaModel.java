package com.itgfirm.docengine.types.jpa;

import static com.itgfirm.docengine.types.jpa.AbstractJpaModel.ModelConstants.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@MappedSuperclass
public abstract class AbstractJpaModel {
	
	@Column(name = JPA_COL_CREATED_BY, nullable = false)
	private Long createdBy = -1L;
	@Column(name = JPA_COL_CREATED_DT, nullable = false)
	private Timestamp createdDt = now();
	@Column(name = JPA_DSCRMNTR_COL, nullable = false, updatable = false, insertable = false)
	private String discriminator;
	@Column(name = JPA_COL_UPDATED_BY, nullable = false)
	private Long updatedBy = -1L;
	@Column(name = JPA_COL_UPDATED_DT, nullable = false)
	private Timestamp updatedDt = now();

	static boolean isNotNullOrEmpty(final Object object) {
		return (object != null && !object.toString().trim().isEmpty());
	}

	static boolean isNotNullOrEmpty(final Collection<?> collection) {
		return (collection != null && !collection.isEmpty());
	}

	static boolean isNotNullOrEmpty(final Iterable<?> iterable) {
		return (iterable != null && iterable.iterator().hasNext());
	}

	static boolean isNotNullOrZero(final Number number) {
		return (number != null && (number.longValue() > 0 || number.intValue() > 0 || number.doubleValue() > 0
				|| number.floatValue() > 0));
	}

	static Timestamp now() {
		return Timestamp.from(Calendar.getInstance().toInstant());
	}

	static Timestamp max() {
		return Timestamp.from(Instant.MAX);
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
	
	static final class ModelConstants {
		static final String EXCEL_COL_BODY = "Content Body";
		static final String EXCEL_COL_CATEGORY = "Category Code";
		static final String EXCEL_COL_CONTENT_CD = "Content Code";
		static final String EXCEL_COL_CONTENT_ID = "Content ID";
		static final String EXCEL_COL_CONTENT_NBR = "Content Number";
		static final String EXCEL_COL_CSS = "Content CSS";
		static final String EXCEL_COL_DESCRIPTION = "Description";
		static final String EXCEL_COL_FLAGS = "Flags";
		static final String EXCEL_COL_HELPER = "Helper Text";
		static final String EXCEL_COL_IS_FIRST = "Is First in a List";
		static final String EXCEL_COL_IS_LAST = "Is Last in a List";
		static final String EXCEL_COL_IS_OPTIONAL = "Is an Optional Paragraph";
		static final String EXCEL_COL_IS_PARENT = "Is a Parent to a Sub-Paragraph";
		static final String EXCEL_COL_IS_SUB = "Is a Sub-Paragraph";
		static final String EXCEL_COL_NAME = "Name";
		static final String EXCEL_COL_ORDER = "Order By";
		static final String EXCEL_COL_PARENT = "Parent ID";
		static final String EXCEL_COL_VALID_END = "End Date";
		static final String EXCEL_COL_VALID_START = "Start Date";
		
		static final String EXCEL_SHEET_NAME_CONTENT = "Content";
		static final String EXCEL_SHEET_NAME_INSTANCE = "Instance";
		static final String EXCEL_SHEET_NAME_TOKEN_DICTIONARY = "Token Dictionary";
		
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
		static final String JPA_COL_IS_FIRST = "IS_FIRST_BLN";
		static final String JPA_COL_IS_LAST = "IS_LAST_BLN";
		static final String JPA_COL_IS_MARKED_FOR_ACTION = "IS_MARKED_FOR_ACTION_BLN";
		static final String JPA_COL_IS_OPTIONAL = "IS_OPTIONAL_BLN";
		static final String JPA_COL_IS_PARENT = "IS_PARENT_BLN";
		static final String JPA_COL_IS_ROMAN = "IS_ROMAN_BLN";
		static final String JPA_COL_IS_STRIKE_HEADER = "IS_STRIKE_HEADER_BLN";
		static final String JPA_COL_IS_SUB = "IS_SUB_PARA_BLN";
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

		static final String JPA_DSCRMNTR_COL = "DSCRMNTR";
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
		static final String JPA_MAPPED_BY_CONTENT = JPA_TBL_CONTENT + "_ID";
		static final String JPA_MAPPED_BY_DOCUMENT = "document";
		static final String JPA_MAPPED_BY_SECTION = "section";

		static final String JSON_PROP_ID = "@id";

		static final String STATUS_AUTO_IN = "Automatically Included";
	}
}