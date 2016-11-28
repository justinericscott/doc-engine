package com.itgfirm.docengine.types.jpa;

import java.sql.Timestamp;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@MappedSuperclass
public abstract class AbstractJpaModel {
	private static final String JPA_COLUMN_CREATED_BY = "CREATED_BY";
	private static final String JPA_COLUMN_CREATED_DT = "CREATED_DT";
	private static final String JPA_COLUMN_UPDATED_BY = "UPDATED_BY";
	private static final String JPA_COLUMN_UPDATED_DT = "UPDATED_DT";	
	
	@Column(name = JPA_COLUMN_CREATED_BY, nullable = false)
	private Long createdBy = -1L;
	@Column(name = JPA_COLUMN_CREATED_DT, nullable = false)
	private Timestamp createdDt = Timestamp.from(Calendar.getInstance().toInstant());
	@Column(name = JPA_COLUMN_UPDATED_BY, nullable = false)
	private Long updatedBy = -1L;
	@Column(name = JPA_COLUMN_UPDATED_DT, nullable = false)
	private Timestamp updatedDt = Timestamp.from(Calendar.getInstance().toInstant());

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
}