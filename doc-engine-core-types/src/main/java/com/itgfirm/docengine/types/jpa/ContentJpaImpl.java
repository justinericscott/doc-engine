package com.itgfirm.docengine.types.jpa;

import java.sql.Timestamp;
import java.util.Date;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.itgfirm.docengine.annotation.ExportOrder;
import com.itgfirm.docengine.types.Content;

/**
 * @author Justin Scott
 * 
 *         ContentJpaImpl Data Model
 */
@Entity
@Table(name = TypeConstants.JPA_TABLE_CONTENT)
@DiscriminatorColumn(name = TypeConstants.JPA_DISCRIMINATOR_COLUMN,
		discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(TypeConstants.JPA_DISCRIMINATOR_CONTENT)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ContentJpaImpl extends AbstractModelJpaImpl implements Content {
	private static final Logger LOG = LogManager.getLogger(ContentJpaImpl.class);

	/** Fields **/
	@Id
	@ExportOrder(1)
	@GeneratedValue(generator = "content_sq", strategy = GenerationType.AUTO)
	@SequenceGenerator(name = "content_sq", sequenceName = "CB_CONTENT_R_SQ",
			allocationSize = 1)
	@Column(name = TypeConstants.JPA_TABLE_CONTENT + "_ID")
	private Long id;
	@ExportOrder(2)
	@Column(name = "PARENT_ID", insertable = false, updatable = false)
	private Long parentId;
	@ExportOrder(3)
	@Column(name = "CONTENT_CD", length = 100, nullable = false, unique = true)
	private String contentCd;
	@ExportOrder(4)
	@Column(name = "NAME_TXT", length = 1000)
	private String name;
	@ExportOrder(5)
	@Column(name = "DESCRIPTION_TXT", length = 4000)
	private String description;
	@ExportOrder(6)
	@Column(name = "CONTENT_NBR", length = 10)
	private String contentNumber;
	@ExportOrder(7)
	@Column(name = "BODY_TXT", length = 4000, nullable = false)
	private String body;
	@ExportOrder(8)
	@Column(name = "CSS_TXT", length = 4000)
	private String css;
	@ExportOrder(9)
	@Column(name = "HELPER_TXT", length = 4000)
	private String helper;
	@ExportOrder(10)
	@Column(name = "CATEGORY_CD", length = 100)
	private String category;
	@ExportOrder(11)
	@Column(name = "FLAGS_CD", length = 100)
	private String flags;
	@ExportOrder(12)
	@Column(name = "ORDER_BY")
	private Integer orderBy;
	/* Set to 01/01/2015 */
	@ExportOrder(13)
	@Column(name = "VALID_START_DT", nullable = false)
	private Timestamp validStart = new Timestamp(new Date(1420088400000L).getTime());
	/* Set to 01/01/2115 */
	@ExportOrder(14)
	@Column(name = "VALID_END_DT", nullable = false)
	private Timestamp validEnd = new Timestamp(new Date(4575762000000L).getTime());

	public ContentJpaImpl() {}

	public ContentJpaImpl(String body) {
		if (!body.isEmpty())
			this.body = body;
	}

	public ContentJpaImpl(String contentCd, String body) {
		if (!contentCd.isEmpty())
			this.contentCd = contentCd;
		if (!body.isEmpty())
			this.body = body;
	}

	public ContentJpaImpl(Content content) {
		name = content.getName();
		description = content.getDescription();
		body = content.getBody();
		css = content.getCss();
		helper = content.getHelper();
		category = content.getCategory();
		flags = content.getFlags();
		orderBy = content.getOrderBy();
	}

	public ContentJpaImpl(Content content, String contentCd) {
		this(content);
		this.contentCd = contentCd;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	// To disable unintended change of Foreign Key
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getContentCd() {
		return contentCd;
	}

	public void setContentCd(String contentCd) {
		this.contentCd = contentCd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContentNumber() {
		return contentNumber;
	}

	public void setContentNumber(String contentNumber) {
		this.contentNumber = contentNumber;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public String getHelper() {
		return helper;
	}

	public void setHelper(String helper) {
		this.helper = helper;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getFlags() {
		return flags;
	}

	public void setFlags(String flags) {
		this.flags = flags;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	public Timestamp getValidStart() {
		return validStart;
	}

	public void setValidStart(Timestamp validStart) {
		this.validStart = validStart;
	}

	public Timestamp getValidEnd() {
		return validEnd;
	}

	public void setValidEnd(Timestamp validEnd) {
		this.validEnd = validEnd;
	}

	public boolean isValid() {
		return isValid(false);
	}

	public boolean isValid(boolean checkForId) {
		if (checkForId) {
			if (!TypeUtils.isNotNullOrZero(id)) {
				LOG.trace("Invalid ID!");
				return false;
			}
		}
		if (!TypeUtils.isNotNullOrEmpty(contentCd)) {
			LOG.trace("Invalid ContentJpaImpl Code!");
			return false;
		}
		if (!TypeUtils.isNotNullOrEmpty(body)) {
			LOG.trace("Invalid Body!");
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Content o) {
		return this.contentCd.compareTo(o.getContentCd());
	}
}