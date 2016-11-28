package com.itgfirm.docengine.types.jpa;

import static com.itgfirm.docengine.types.jpa.TypeUtils.*;
import static com.itgfirm.docengine.types.jpa.TypeConstants.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.itgfirm.docengine.annotation.ExportOrder;

/**
 * @author Justin Scott
 * 
 *         ContentJpaImpl Data Model
 */
@Entity
@Table(name = JPA_TABLE_CONTENT)
@DiscriminatorColumn(name = JPA_DISCRIMINATOR_COLUMN, discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(JPA_DISCRIMINATOR_CONTENT)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ContentJpaImpl extends AbstractJpaModel implements Comparable<ContentJpaImpl> {
	private static final Logger LOG = LoggerFactory.getLogger(ContentJpaImpl.class);
	private static final String JPA_COLUMN_BODY = "BODY_TXT";
	private static final String JPA_COLUMN_CATEGORY = "CATEGORY_CD";
	private static final String JPA_COLUMN_CONTENT_CD = "CONTENT_CD";
	private static final String JPA_COLUMN_CONTENT_ID = JPA_TABLE_CONTENT + "_ID";
	private static final String JPA_COLUMN_CONTENT_NBR = "CONTENT_NBR";
	private static final String JPA_COLUMN_CSS = "CSS_TXT";
	private static final String JPA_COLUMN_DESCRIPTION = "DESCRIPTION_TXT";
	private static final String JPA_COLUMN_FLAGS = "FLAGS_CD";
	private static final String JPA_COLUMN_HELPER = "HELPER_TXT";
	private static final String JPA_COLUMN_NAME = "NAME_TXT";
	private static final String JPA_COLUMN_ORDER = "ORDER_BY";
	private static final String JPA_COLUMN_VALID_END = "VALID_END_DT";
	private static final String JPA_COLUMN_VALID_START = "VALID_START_DT";
	private static final String JPA_SEQUENCE_CONTENT = JPA_TABLE_CONTENT + "_SQ";
	protected static final String JPA_COLUMN_PARENT = "PARENT_ID";

	/** Fields **/
	@Id
	@ExportOrder(1)
	@GeneratedValue(generator = JPA_SEQUENCE_CONTENT, strategy = GenerationType.AUTO)
	@SequenceGenerator(name = JPA_SEQUENCE_CONTENT, sequenceName = JPA_SEQUENCE_CONTENT)
	@Column(name = JPA_COLUMN_CONTENT_ID)
	private Long id;
	@ExportOrder(2)
	@Column(name = JPA_COLUMN_PARENT, insertable = false, updatable = false)
	private Long parentId;
	@ExportOrder(3)
	@Column(name = JPA_COLUMN_CONTENT_CD, length = 100, nullable = false, unique = true)
	private String contentCd;
	@ExportOrder(4)
	@Column(name = JPA_COLUMN_NAME, length = 1000)
	private String name;
	@ExportOrder(5)
	@Column(name = JPA_COLUMN_DESCRIPTION, length = 4000)
	private String description;
	@ExportOrder(6)
	@Column(name = JPA_COLUMN_CONTENT_NBR, length = 10)
	private String contentNumber;
	@ExportOrder(7)
	@Column(name = JPA_COLUMN_BODY, length = 4000, nullable = false)
	private String body;
	@ExportOrder(8)
	@Column(name = JPA_COLUMN_CSS, length = 4000)
	private String css;
	@ExportOrder(9)
	@Column(name = JPA_COLUMN_HELPER, length = 4000)
	private String helper;
	@ExportOrder(10)
	@Column(name = JPA_COLUMN_CATEGORY, length = 100)
	private String category;
	@ExportOrder(11)
	@Column(name = JPA_COLUMN_FLAGS, length = 100)
	private String flags;
	@ExportOrder(12)
	@Column(name = JPA_COLUMN_ORDER)
	private Integer orderBy;
	@ExportOrder(13)
	@Column(name = JPA_COLUMN_VALID_START, nullable = false)
	private Timestamp validStart = Timestamp.from(Calendar.getInstance().toInstant());
	@ExportOrder(14)
	@Column(name = JPA_COLUMN_VALID_END, nullable = false)
	private Timestamp validEnd = Timestamp.from(Instant.MAX);
	
	public ContentJpaImpl() {
		// Default constructor for Spring
	}

	public ContentJpaImpl(final String body) {
		if (isNotNullOrEmpty(body)) {
			this.body = body;
		}
	}

	public ContentJpaImpl(final String contentCd, final String body) {
		this(body);
		if (isNotNullOrEmpty(contentCd)) {
			this.contentCd = contentCd;
		}
	}

	public ContentJpaImpl(final ContentJpaImpl content) {
		if (isNotNullOrEmpty(content)) {
			name = content.getName();
			description = content.getDescription();
			body = content.getBody();
			css = content.getCss();
			helper = content.getHelper();
			category = content.getCategory();
			flags = content.getFlags();
			orderBy = content.getOrderBy();
		}
	}

	public ContentJpaImpl(final ContentJpaImpl content, final String contentCd) {
		this(content);
		if (isNotNullOrEmpty(contentCd)) {
			this.contentCd = contentCd;
		}
	}

	public final Long getId() {
		return id;
	}

	// Disables unintended change of Primary Key
	final void setId(final Long id) {
		this.id = id;
	}

	public final Long getParentId() {
		return parentId;
	}

	// Disables unintended change of Foreign Key
	final void setParentId(final Long parentId) {
		this.parentId = parentId;
	}

	public final String getContentCd() {
		return contentCd;
	}

	public final void setContentCd(final String contentCd) {
		this.contentCd = contentCd;
	}

	public final String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public final String getDescription() {
		return description;
	}

	public final void setDescription(final String description) {
		this.description = description;
	}

	public final String getContentNumber() {
		return contentNumber;
	}

	public final void setContentNumber(final String contentNumber) {
		this.contentNumber = contentNumber;
	}

	public final String getBody() {
		return body;
	}

	public final void setBody(final String body) {
		this.body = body;
	}

	public final String getCss() {
		return css;
	}

	public final void setCss(final String css) {
		this.css = css;
	}

	public final String getHelper() {
		return helper;
	}

	public final void setHelper(final String helper) {
		this.helper = helper;
	}

	public final String getCategory() {
		return category;
	}

	public final void setCategory(final String category) {
		this.category = category;
	}

	public final String getFlags() {
		return flags;
	}

	public final void setFlags(final String flags) {
		this.flags = flags;
	}

	public final Integer getOrderBy() {
		return orderBy;
	}

	public final void setOrderBy(final Integer orderBy) {
		this.orderBy = orderBy;
	}

	public final Timestamp getValidStart() {
		return validStart;
	}

	public final void setValidStart(final Timestamp validStart) {
		this.validStart = validStart;
	}

	public final Timestamp getValidEnd() {
		return validEnd;
	}

	public final void setValidEnd(final Timestamp validEnd) {
		this.validEnd = validEnd;
	}

	public final boolean isValid() {
		return isValid(false);
	}

	public final boolean isValid(final boolean checkForId) {
		if (checkForId && !isNotNullOrZero(id)) {
			LOG.debug("ID must not be null or zero!");
			return false;
		}
		if (!isNotNullOrEmpty(contentCd)) {
			LOG.debug("Content code must not be null or empty!");
			return false;
		}
		if (!isNotNullOrEmpty(body)) {
			LOG.debug("Content body must not be null or empty!");
			return false;
		}
		return true;
	}

	@Override
	public final int compareTo(final ContentJpaImpl o) {
		return this.getContentCd().compareTo(o.getContentCd());
	}
}