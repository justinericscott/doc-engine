package com.itgfirm.docengine.types.jpa;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.Instance;

/**
 * @author Justin Scott
 * 
 * InstanceJpaImpl Data Model
 */
@Entity @Table( name = TypeConstants.JPA_TABLE_CONTENT_INSTANCE )
@DiscriminatorColumn( name = TypeConstants.JPA_DISCRIMINATOR_COLUMN,
	discriminatorType = DiscriminatorType.STRING )
@DiscriminatorValue( TypeConstants.JPA_DISCRIMINATOR_INSTANCE )
@Inheritance( strategy = InheritanceType.SINGLE_TABLE )
public class InstanceJpaImpl extends AbstractModelJpaImpl implements Instance {
	private static final Logger LOG = LogManager.getLogger(InstanceJpaImpl.class);
	private static final String STATUS_AUTO_IN = "Automatically Included";

	/** Fields **/
	@Id	
	@GeneratedValue( generator="instance_sq", strategy = GenerationType.AUTO )
	@SequenceGenerator( name="instance_sq", sequenceName = "CB_CONTENT_INST_SQ", allocationSize = 1 )
	@Column( name = TypeConstants.JPA_TABLE_CONTENT_INSTANCE + "_ID" )
	private Long id;
	@Column( name = "PROJECT_NBR", length = 100, nullable = false )
	private String projectId;
	@Column( name = "CUSTOM_BODY_TXT", length = 4000 )
	private String customBody;
	@Column( name = "FLAGS_TXT", length = 100 )
	private String flags;	
	@Column( name = "STATUS_CD", length = 100 )
	private String statusCd;
	@Column( name = "ORDER_BY" )
	private Integer orderBy;
	@Column( name = "IS_AD_HOC_BLN" )
	private boolean isAdHoc = false;
	@Column( name = "IS_STRIKE_HEADER_BLN" )
	private boolean isStrikeHeader = false;
	@Column( name = "IS_MARKED_FOR_ACTION_BLN")
	private boolean isMarkedForAction = false;
	@Column( name = "MARKED_FOR_ACTION_COMMENT_TXT" )
	private String markedForActionComment;
	
	/** Source ContentJpaImpl **/
	@JoinColumn( name = "CB_CONTENT_R_ID" )
	@ManyToOne( cascade = CascadeType.REFRESH,
			targetEntity = ContentJpaImpl.class )
	@JsonDeserialize( as = ContentJpaImpl.class )
	private Content content;
	
	public InstanceJpaImpl() { 
		this.statusCd = STATUS_AUTO_IN;
	}
	
	public InstanceJpaImpl(String projectId) {
		this();
		this.projectId = projectId;
	}
	
	public InstanceJpaImpl(Content content) {
		this();
		this.content = content;
		this.orderBy = content.getOrderBy();
	}

	public InstanceJpaImpl(Content content, String projectId) {
		this(content);
		this.projectId = projectId;
	}

	public InstanceJpaImpl(Content content, String projectId, String body) {
		this(content, projectId);
		this.customBody = body;
	}
	
	public InstanceJpaImpl(Content content, String projectId, String body, 
			boolean isAdHoc) {
		this(content, projectId, body);
		this.isAdHoc = isAdHoc;
	}

	public Long getId() { return id; }
	//To disable unintended change of the Primary Key
	@SuppressWarnings("unused")
	private void setId(Long id) { this.id = id; }

	public String getProjectId() { return projectId; }
	public void setProjectId(String projectId) { this.projectId = projectId; }

	// Reduced visibility to ensure getBody & setBody are used.
	@JsonIgnore
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
		} else {
			return content.getBody();
		}
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
	public void setContent(Content content) { this.content = content; }

	public boolean isValid() { 	return isValid(false); }	
	public boolean isValid(boolean checkForId) {
		if (checkForId) {
			if (!TypeUtils.isNotNullOrZero(id)) {
				LOG.trace("Invalid ID!");
				return false;
			}
		}
		if (content != null) {
			if (!content.isValid(true)) {
				LOG.trace("Invalid ContentJpaImpl!");
				return false;
			}		
		} else {
			return false;
		}
		if (!TypeUtils.isNotNullOrEmpty(projectId)) {
			LOG.trace("Invalid DefaultProjectImpl ID!");
			return false;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Instance o) { 
		return this.content.getContentCd().compareTo(
				o.getContent().getContentCd()); 
	}
}