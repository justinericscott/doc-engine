package com.itgfirm.docengine.types.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.Instance;

/**
 * @author Justin Scott
 * 
 * InstanceJaxbImpl Data Model
 */
@XmlRootElement( name=TypeConstants.JAXB_ROOT_ELEMENT_NAME_INSTANCE,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlType( name=TypeConstants.JAXB_TYPE_NAME_INSTANCE,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlAccessorType( XmlAccessType.FIELD )
public class InstanceJaxbImpl extends AbstractModelJaxbImpl implements Instance {

	/** Fields **/
	private Long id;
	private String projectId;
	String customBody;
	private String flags;	
	private String statusCd;
	private Integer orderBy;
	private boolean isAdHoc = false;
	private boolean isStrikeHeader = false;
	private boolean isMarkedForAction = false;
	private String markedForActionComment;
	
	/** Source Content **/
	@XmlElement( type = ContentJaxbImpl.class )
	private Content content;
	
	public InstanceJaxbImpl() { 
		this.statusCd = TypeConstants.STATUS_AUTO_IN;
	}
	
	public InstanceJaxbImpl(String projectId) {
		this();
		this.projectId = projectId;
	}
	
	public InstanceJaxbImpl(Content content) {
		this();
		this.content = content;
		this.orderBy = content.getOrderBy();
	}

	public InstanceJaxbImpl(Content content, String projectId) {
		this(content);
		this.projectId = projectId;
	}

	public InstanceJaxbImpl(Content content, String projectId, String body) {
		this(content, projectId);
		this.customBody = body;
	}
	
	public InstanceJaxbImpl(Content content, String projectId, String body, 
			boolean isAdHoc) {
		this(content, projectId, body);
		this.isAdHoc = isAdHoc;
	}

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getProjectId() { return projectId; }
	public void setProjectId(String projectId) { this.projectId = projectId; }

	// Reduced visibility to ensure getBody & setBody are used.
	public String getCustomBody() { return customBody; }
	private void setCustomBody(String customBody) { 
		if (!content.getBody().equals(customBody)) {
			if (!TypeUtils.isNotNullOrEmpty(customBody)) {
				this.customBody = null;
			} else {
				this.customBody = customBody;
			}
		}
	}
	
	public String getBody() {
		if (TypeUtils.isNotNullOrEmpty(customBody)) {
			return customBody;
		} else if (TypeUtils.isNotNullOrEmpty(content)) {
			return content.getBody();		
		}
		return null;
	}
	public void setBody(String body) { setCustomBody(body); }

	public String getFlags() { return flags; }
	public void setFlags(String flags) { this.flags = flags; }

	public String getStatusCd() { return statusCd; }
	public void setStatusCd(String statusCd) { 	this.statusCd = statusCd; }

	public Integer getOrderBy() { return orderBy; }
	public void setOrderBy(Integer orderBy) { this.orderBy = orderBy; }

	public boolean isAdHoc() { return isAdHoc; }
	public void setAdHoc(boolean isAdHoc) { this.isAdHoc = isAdHoc; }

	public boolean isStrikeHeader() { return isStrikeHeader; }
	public void setStrikeHeader(boolean isStrikeHeader) {
		this.isStrikeHeader = isStrikeHeader;
	}

	public boolean isMarkedForAction() { return isMarkedForAction; }
	public void setMarkedForAction(boolean isMarkedForAction) {
		this.isMarkedForAction = isMarkedForAction;
	}

	public String getMarkedForActionComment() { return markedForActionComment; }
	public void setMarkedForActionComment(String markedForActionComment) {
		this.markedForActionComment = markedForActionComment;
	}

	public Content getContent() { return content; }
	public void setContent(Content contentJaxbImpl) { this.content = contentJaxbImpl; }

	public boolean isValid() { 	return isValid(false); }	
	public boolean isValid(boolean checkForId) {
		if (checkForId) {
			if (!TypeUtils.isNotNullOrZero(id)) {
				return false;
			}
		}
		if (!content.isValid(true)) {
			return false;
		}
		if (!TypeUtils.isNotNullOrEmpty(projectId)) {
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Instance o) { return 0; }
}