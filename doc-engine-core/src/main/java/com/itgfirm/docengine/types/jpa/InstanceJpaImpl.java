package com.itgfirm.docengine.types.jpa;

import static com.itgfirm.docengine.types.jpa.TypeUtils.*;
import static com.itgfirm.docengine.types.jpa.TypeConstants.*;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Entity
@Table(name = JPA_TABLE_INSTANCE)
@DiscriminatorColumn(name = JPA_DISCRIMINATOR_COLUMN, discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(JPA_DISCRIMINATOR_INSTANCE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class InstanceJpaImpl extends AbstractJpaModel implements Comparable<InstanceJpaImpl> {
	private static final Logger LOG = LoggerFactory.getLogger(InstanceJpaImpl.class);
	private static final String JPA_COLUMN_CUSTOM_BODY = "CUSTOM_BODY_TXT";
	private static final String JPA_COLUMN_FLAGS = "FLAGS_TXT";
	private static final String JPA_COLUMN_INSTANCE_ID = JPA_TABLE_INSTANCE + "_ID";
	private static final String JPA_COLUMN_IS_AD_HOC = "IS_AD_HOC_BLN";
	private static final String JPA_COLUMN_IS_MARKED_FOR_ACTION = "IS_MARKED_FOR_ACTION_BLN";
	private static final String JPA_COLUMN_IS_STRIKE_HEADER = "IS_STRIKE_HEADER_BLN";
	private static final String JPA_COLUMN_MARKED_COMMENT = "MARKED_FOR_ACTION_COMMENT_TXT";
	private static final String JPA_COLUMN_ORDER = "ORDER_BY";
	private static final String JPA_COLUMN_PROJECT = "PROJECT_NBR";
	private static final String JPA_COLUMN_STATUS = "STATUS_CD";
	private static final String JPA_JOIN_COLUMN_CONTENT = JPA_TABLE_CONTENT + "_ID";
	private static final String JPA_SEQUENCE_INSTANCE = JPA_TABLE_INSTANCE + "_SQ";
	private static final String STATUS_AUTO_IN = "Automatically Included";

	/** Fields **/
	@Id
	@GeneratedValue(generator = JPA_SEQUENCE_INSTANCE, strategy = GenerationType.AUTO)
	@SequenceGenerator(name = JPA_SEQUENCE_INSTANCE, sequenceName = JPA_SEQUENCE_INSTANCE)
	@Column(name = JPA_COLUMN_INSTANCE_ID)
	private Long id;
	@Column(name = JPA_COLUMN_PROJECT, length = 100, nullable = false)
	private String projectId;
	@Column(name = JPA_COLUMN_CUSTOM_BODY, length = 4000)
	private String customBody;
	@Column(name = JPA_COLUMN_FLAGS, length = 100)
	private String flags;
	@Column(name = JPA_COLUMN_STATUS, length = 100)
	private String statusCd;
	@Column(name = JPA_COLUMN_ORDER)
	private Integer orderBy;
	@Column(name = JPA_COLUMN_IS_AD_HOC)
	private boolean isAdHoc = false;
	@Column(name = JPA_COLUMN_IS_STRIKE_HEADER)
	private boolean isStrikeHeader = false;
	@Column(name = JPA_COLUMN_IS_MARKED_FOR_ACTION)
	private boolean isMarkedForAction = false;
	@Column(name = JPA_COLUMN_MARKED_COMMENT)
	private String markedForActionComment;

	/** Source ContentJpaImpl **/
	@JoinColumn(name = JPA_JOIN_COLUMN_CONTENT, nullable = false)
	@ManyToOne(cascade = CascadeType.REFRESH, targetEntity = ContentJpaImpl.class)
	@JsonDeserialize(as = ContentJpaImpl.class)
	private ContentJpaImpl content;

	public InstanceJpaImpl() {
		// Default constructor for Spring
		this.statusCd = STATUS_AUTO_IN;
	}

	public InstanceJpaImpl(final String projectId) {
		this();
		this.projectId = projectId;
	}

	public InstanceJpaImpl(final ContentJpaImpl content) {
		this();
		this.content = content;
		this.orderBy = content.getOrderBy();
	}

	public InstanceJpaImpl(final ContentJpaImpl content, final String projectId) {
		this(content);
		this.projectId = projectId;
	}

	public InstanceJpaImpl(final ContentJpaImpl content, final String projectId, final String body) {
		this(content, projectId);
		this.customBody = body;
	}

	public InstanceJpaImpl(final ContentJpaImpl content, final String projectId, final String body,
			final boolean isAdHoc) {
		this(content, projectId, body);
		this.isAdHoc = isAdHoc;
	}

	public final Long getId() {
		return id;
	}

	// Disables unintended change of the Primary Key
	final void setId(final Long id) {
		this.id = id;
	}

	public final String getProjectId() {
		return projectId;
	}

	public final void setProjectId(final String projectId) {
		this.projectId = projectId;
	}

	// Reduced visibility to ensure getBody & setBody are used.
	@JsonIgnore
	public final String getCustomBody() {
		return customBody;
	}

	private final void setCustomBody(final String customBody) {
		if (isNotNullOrEmpty(customBody) && !content.getBody().equals(customBody)) {
			this.customBody = null;
		} else if (isNotNullOrEmpty(customBody)) {
			this.customBody = customBody;
		} 
	}

	public final String getBody() {
		if (isNotNullOrEmpty(customBody)) {
			return customBody;
		} else {
			return content.getBody();
		}
	}

	public final void setBody(final String body) {
		setCustomBody(body);
	}

	public final String getFlags() {
		return flags;
	}

	public final void setFlags(final String flags) {
		this.flags = flags;
	}

	public final String getStatusCd() {
		return statusCd;
	}

	public final void setStatusCd(final String statusCd) {
		this.statusCd = statusCd;
	}

	public final Integer getOrderBy() {
		return orderBy;
	}

	public final void setOrderBy(final Integer orderBy) {
		this.orderBy = orderBy;
	}

	public final boolean isAdHoc() {
		return isAdHoc;
	}

	public final void setAdHoc(final boolean isAdHoc) {
		this.isAdHoc = isAdHoc;
	}

	public final boolean isStrikeHeader() {
		return isStrikeHeader;
	}

	public final void setStrikeHeader(final boolean isStrikeHeader) {
		this.isStrikeHeader = isStrikeHeader;
	}

	public final boolean isMarkedForAction() {
		return isMarkedForAction;
	}

	public final void setMarkedForAction(final boolean isMarkedForAction) {
		this.isMarkedForAction = isMarkedForAction;
	}

	public final String getMarkedForActionComment() {
		return markedForActionComment;
	}

	public final void setMarkedForActionComment(final String markedForActionComment) {
		this.markedForActionComment = markedForActionComment;
	}

	public final ContentJpaImpl getContent() {
		return content;
	}

	public final void setContent(final ContentJpaImpl content) {
		this.content = content;
	}

	public final boolean isValid() {
		return isValid(false);
	}

	public final boolean isValid(final boolean checkForId) {
		if (checkForId && !isNotNullOrZero(id)) {
			LOG.debug("ID must not be null or zero!");
			return false;
		}
		if (content != null && !content.isValid(true)) {
			LOG.debug("Content must not be null or invalid!");
			return false;
		}
		if (!isNotNullOrEmpty(projectId)) {
			LOG.debug("Project ID/Number must not be null or empty!");
			return false;
		}
		return true;
	}

	@Override
	public final int compareTo(final InstanceJpaImpl o) {
		return this.getContent().getContentCd().compareTo(o.getContent().getContentCd());
	}
}