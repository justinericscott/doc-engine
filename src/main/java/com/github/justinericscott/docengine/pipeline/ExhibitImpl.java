package com.github.justinericscott.docengine.pipeline;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class ExhibitImpl {

	@Id
	@Column(name = "DOCUMENT_ID")
	private Long id;
	@Column(name = "EXHIBIT_NBR")
	private String exhibit;
	@Column(name = "DOCUMENT_TYPE_CD")
	private String docType;
	@Column(name = "LEASE_ORDER_BY")
	private Integer orderByLease;
	@Column(name = "RLP_ORDER_BY")
	private Integer orderByRlp;
	@Column(name = "NAME_TXT")
	private String name;
	@Column(name = "PAGE_CNT")
	private Integer pageCount;
	@Column(name = "PROJECT_NBR")
	private String projectNumber;
	@Column(name = "CREATED_BY_PERSON_ID")
	private int createdBy;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DT")
	private Date createdDate;
	@Column(name = "UPDATED_BY_PERSON_ID")
	private int updatedBy;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DT")
	private Date updatedDate;

	public ExhibitImpl() {

	}

	public ExhibitImpl(String name, String exhibit, int pageCount) {
		this.name = name;
		this.exhibit = exhibit;
		this.pageCount = pageCount;
	}

	public ExhibitImpl(Long id, String name, String exhibit, int pageCount) {
		this(name, exhibit, pageCount);
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public Integer getOrderByLease() {
		return orderByLease;
	}

	public void setOrderByLease(Integer orderByLease) {
		this.orderByLease = orderByLease;
	}

	public Integer getOrderByRlp() {
		return orderByRlp;
	}

	public void setOrderByRlp(Integer orderByRlp) {
		this.orderByRlp = orderByRlp;
	}

	public String getExhibit() {
		return exhibit;
	}

	public void setExhibit(String exhibitNumber) {
		this.exhibit = exhibitNumber;
	}

	public String getDisplayName() {
		return String.format("%s, %s", exhibit, name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPageCount() {
		return pageCount;
	}

	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((exhibit == null) ? 0 : exhibit.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExhibitImpl other = (ExhibitImpl) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (exhibit == null) {
			if (other.exhibit != null)
				return false;
		} else if (!exhibit.equals(other.exhibit))
			return false;
		return true;
	}
}