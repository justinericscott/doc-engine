package com.itgfirm.docengine.types.jpa;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@MappedSuperclass
public abstract class AbstractModelJpaImpl {

	@Column(name = "CREATED_BY", nullable = false)
	private Long createdBy = 1L;
	@Column(name = "CREATED_DT", nullable = false)
	private Timestamp createdDt = new Timestamp(new Date().getTime());
	@Column(name = "UPDATED_BY", nullable = false)
	private Long updatedBy = 1L;
	@Column(name = "UPDATED_DT", nullable = false)
	private Timestamp updatedDt = new Timestamp(new Date().getTime());

	public AbstractModelJpaImpl() {}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDt() {
		return createdDt;
	}

	public void setCreatedDt(Timestamp createdDt) {
		this.createdDt = createdDt;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Timestamp getUpdatedDt() {
		return updatedDt;
	}

	public void setUpdatedDt(Timestamp updatedDt) {
		this.updatedDt = updatedDt;
	}
}