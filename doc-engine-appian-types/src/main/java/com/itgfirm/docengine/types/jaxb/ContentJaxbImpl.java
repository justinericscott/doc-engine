package com.itgfirm.docengine.types.jaxb;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.itgfirm.docengine.types.Content;

/**
 * @author Justin Scott
 * 
 * ContentJaxbImpl Data Model
 */
@XmlRootElement( name=TypeConstants.JAXB_ROOT_ELEMENT_NAME_CONTENT,
		namespace=TypeConstants.JAXB_NAMESPACE )
@XmlType( name=TypeConstants.JAXB_TYPE_NAME_CONTENT,
		namespace=TypeConstants.JAXB_NAMESPACE )
@XmlAccessorType( XmlAccessType.FIELD )
public class ContentJaxbImpl extends AbstractModelJaxbImpl implements Content {
	
	/** Fields **/
	@XmlElement
	private Long id;
	@XmlElement
	private String contentCd;
	@XmlElement
	private String name;
	@XmlElement
	private String description;
	@XmlElement
	private String contentNumber;
	@XmlElement
	private String body; 
	@XmlElement
	private String css;
	@XmlElement
	private String helper; 
	@XmlElement
	private String category;
	@XmlElement
	private String flags;	
	@XmlElement
	private Integer orderBy;
	@XmlTransient
	private Timestamp validStart = new Timestamp(1420088400000L); // Set to 01/01/2015
	@XmlTransient
	private Timestamp validEnd = new Timestamp(4575762000000L); // Set to 01/01/2115
	
	public ContentJaxbImpl() { }
	
	public ContentJaxbImpl(String body) {
		if (!body.isEmpty()) this.body = body;
	}
	
	public ContentJaxbImpl(String contentCd, String body) {
		if (!contentCd.isEmpty()) this.contentCd = contentCd;
		if (!body.isEmpty()) this.body = body;
	}
	
	public ContentJaxbImpl(Content content) {
		name = content.getName();
		description = content.getDescription();
		body = content.getBody();
		css = content.getCss();
		helper = content.getHelper();
		category = content.getCategory();
		flags = content.getFlags();
		orderBy = content.getOrderBy();
	}

	public ContentJaxbImpl(Content content, String contentCd) {
		this(content);
		this.contentCd = contentCd;
	}
	
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	
	public String getContentCd() { return contentCd; }
	public void setContentCd(String contentCd) { this.contentCd = contentCd; }
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	public String getContentNumber() { return contentNumber; }
	public void setContentNumber(String contentNumber) { this.contentNumber = contentNumber; }

	public String getBody() { return body; }
	public void setBody(String body) { this.body = body; }

	public String getCss() { return css; }
	public void setCss(String css) { this.css = css; }

	public String getHelper() { return helper; }
	public void setHelper(String helper) { this.helper = helper; }

	public String getCategory() { return category; }
	public void setCategory(String category) { this.category = category; }

	public String getFlags() { return flags; }
	public void setFlags(String flags) { this.flags = flags; }

	public Integer getOrderBy() { return orderBy; }
	public void setOrderBy(Integer orderBy) { this.orderBy = orderBy; }

	public Timestamp getValidStart() { return validStart; }
	public void setValidStart(Timestamp validStart) { this.validStart = validStart; }

	public Timestamp getValidEnd() { return validEnd; }
	public void setValidEnd(Timestamp validEnd) { this.validEnd = validEnd; }
	
	public boolean isValid() {
		return isValid(false);
	}
	
	public boolean isValid(boolean checkForId) {
		if (checkForId) {
			if (!TypeUtils.isNotNullOrZero(id)) {
				return false;
			}
		}
		if (!TypeUtils.isNotNullOrEmpty(contentCd)) {
			return false;
		}
		if (!TypeUtils.isNotNullOrEmpty(body)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Content o) {
		return contentCd.compareTo(o.getContentCd());
	}
}