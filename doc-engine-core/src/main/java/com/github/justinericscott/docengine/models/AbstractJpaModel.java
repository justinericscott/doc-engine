package com.github.justinericscott.docengine.models;

import java.sql.Timestamp;

import java.time.Instant;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@MappedSuperclass
public abstract class AbstractJpaModel {
	protected static final String CONTENT_FLAG_OPTIONAL = "OPT";
	protected static final String CONTENT_FLAG_PARENT = "PARENT";
	protected static final String CSS_TYPE_TEXT = "text/css";
	protected static final String DB_COL_DESCRIPTION = "DESC_TXT";
	protected static final String DB_COL_DISCRIMINATOR = "DTYPE";
	protected static final String DB_COL_FLAGS = "FLAGS_CD";
	protected static final String DB_COL_NAME = "NAME_TXT";
	protected static final String DB_COL_ORDER = "ORDER_BY";
	protected static final String DB_COL_PARENT = "PARENT_ID";
	protected static final String JPA_MAPPED_BY_CLAUSE = "clause";
	protected static final String JPA_MAPPED_BY_DOCUMENT = "document";
	protected static final String JPA_MAPPED_BY_SECTION = "section";
	protected static final String XLS_COL_DESCRIPTION = "Description";
	protected static final String XLS_COL_DISCRIMINATOR = "Discriminator";
	protected static final String XLS_COL_FLAGS = "Flags";
	protected static final String XLS_COL_NAME = "Name";
	protected static final String XLS_COL_ORDER = "Order By";
	protected static final String XLS_COL_PARENT = "Parent ID";
	private static final String DB_COL_CREATED_BY = "CREATED_BY";
	private static final String DB_COL_CREATED_DT = "CREATED_DT";
	private static final String DB_COL_UPDATED_BY = "UPDATED_BY";
	private static final String DB_COL_UPDATED_DT = "UPDATED_DT";

	@Column(name = DB_COL_CREATED_BY, nullable = false)
	private Long createdBy = -1L;
	@CreationTimestamp
	@Column(name = DB_COL_CREATED_DT, nullable = false)
	private Timestamp createdDt;
	@Column(name = DB_COL_UPDATED_BY, nullable = false)
	private Long updatedBy = -1L;
	@UpdateTimestamp
	@Column(name = DB_COL_UPDATED_DT, nullable = false)
	private Timestamp updatedDt;

	static Date now() {
		return new Date(Instant.now().toEpochMilli());
	}

	static Date max() {		
		return new Calendar.Builder().setDate(2117, 1, 1).build().getTime();
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
}