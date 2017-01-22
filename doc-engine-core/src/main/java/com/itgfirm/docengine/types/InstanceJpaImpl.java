package com.itgfirm.docengine.types;

import static com.itgfirm.docengine.util.Utils.isNotNullOrEmpty;
import static com.itgfirm.docengine.util.Utils.isNotNullOrZero;
import static com.itgfirm.docengine.types.AbstractJpaModel.ModelConstants.*;

import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.CascadeType.REFRESH;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = JPA_TBL_INSTANCE)
public class InstanceJpaImpl extends AbstractJpaModel implements Comparable<InstanceJpaImpl> {
	private static final Logger LOG = LoggerFactory.getLogger(InstanceJpaImpl.class);

	@Id
	@GeneratedValue(strategy = AUTO, generator = JPA_SEQ_INSTANCE)
	@SequenceGenerator(name = JPA_SEQ_INSTANCE, sequenceName = JPA_SEQ_INSTANCE)
	@Column(name = JPA_COL_INSTANCE_ID, unique = true)
	protected Long id;
	@Column(name = JPA_COL_PROJECT, length = 100, nullable = false)
	protected String projectId;
	@Column(name = JPA_COL_CUSTOM_BODY, length = 4000)
	protected String customBody;
	@Column(name = JPA_COL_FLAGS, length = 100)
	protected String flags;
	@Column(name = JPA_COL_STATUS, length = 100)
	protected String statusCd;
	@Column(name = JPA_COL_ORDER)
	protected Integer orderBy;
	@Column(name = JPA_COL_IS_AD_HOC)
	protected boolean isAdHoc = false;
	@Column(name = JPA_COL_IS_STRIKE_HEADER)
	protected boolean isStrikeHeader = false;
	@Column(name = JPA_COL_IS_MARKED_FOR_ACTION)
	protected boolean isMarkedForAction = false;
	@Column(name = JPA_COL_MARKED_COMMENT)
	protected String markedForActionComment;

	@OrderColumn(name = JPA_COL_ORDER)
	@ManyToOne(targetEntity = ContentJpaImpl.class, cascade = REFRESH, optional = false)
	protected ContentJpaImpl content;

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

	public final void setId(final Long id) {
		this.id = id;
	}

	public final String getProjectId() {
		return projectId;
	}

	public final void setProjectId(final String projectId) {
		this.projectId = projectId;
	}

	@JsonIgnore
	public final String getBody() {
		if (isNotNullOrEmpty(customBody)) {
			return customBody;
		} else {
			if (isNotNullOrEmpty(content)) {
				return content.getBody();
			}
		}
		return "";
	}

	@JsonIgnore
	public final void setBody(final String customBody) {
		if (isNotNullOrEmpty(customBody)) {
			if (!content.getBody().equals(customBody)) {
				this.customBody = customBody;
			}
		} else {
			this.customBody = null;
		}
	}

	public final String getCustomBody() {
		return customBody;
	}

	public final void setCustomBody(final String customBody) {
		this.customBody = customBody;
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

	public ContentJpaImpl getContent() {
		return content;
	}

	public void setContent(final ContentJpaImpl content) {
		this.content = content;
	}

	@JsonIgnore
	public final boolean isValid() {
		return isValid(false);
	}

	@JsonIgnore
	public final boolean isValid(final boolean checkForId) {
		if (checkForId && !isNotNullOrZero(id)) {
			LOG.debug("ID must not be null or zero!");
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