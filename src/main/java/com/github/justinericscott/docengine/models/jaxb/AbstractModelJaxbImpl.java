/**
 * TODO: License
 */
package com.github.justinericscott.docengine.models.jaxb;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Justin Scott
 * TODO: Description
 */
@XmlRootElement( name=TypeConstants.JAXB_ROOT_ELEMENT_NAME_ABSTRACT,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlType( name=TypeConstants.JAXB_TYPE_NAME_ABSTRACT,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlAccessorType( XmlAccessType.FIELD )
public abstract class AbstractModelJaxbImpl {
		
	@XmlTransient
	private Long createdBy = 1L;
	@XmlTransient
	private Timestamp createdDt = new Timestamp(new Date().getTime());
	@XmlTransient
	private Long updatedBy = 1L;
	@XmlTransient
	private Timestamp updatedDt = new Timestamp(new Date().getTime());
	
	public AbstractModelJaxbImpl() { }
	
	public Long getCreatedBy() { return createdBy; }
	public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

	public Timestamp getCreatedDt() { return createdDt; }
	public void setCreatedDt(Timestamp createdDt) { this.createdDt = createdDt; }

	public Long getUpdatedBy() { return updatedBy;}
	public void setUpdatedBy(Long updatedBy) { this.updatedBy = updatedBy; }

	public Timestamp getUpdatedDt() { return updatedDt; }
	public void setUpdatedDt(Timestamp updatedDt) { this.updatedDt = updatedDt; }
}