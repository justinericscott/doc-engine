package com.itgfirm.docengine.types.jpa;

import static javax.persistence.DiscriminatorType.*;
import static javax.persistence.InheritanceType.*;

import static com.itgfirm.docengine.types.jpa.TypeUtils.*;
import static com.itgfirm.docengine.types.jpa.TypeConstants.*;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itgfirm.docengine.annotation.ExcelColumn;
import com.itgfirm.docengine.annotation.ExcelColumnOrder;
import com.itgfirm.docengine.annotation.ExcelSheet;

/**
 * @author Justin Scott
 * 
 *         ContentJpaImpl Data Model
 */
@Entity
@Table(name = JPA_TBL_CONTENT)
@ExcelSheet(IX_SHEET_NAME_CONTENT)
@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorValue(JPA_DSCRMNTR_CONTENT)
@DiscriminatorColumn(name = JPA_DSCRMNTR_COL, discriminatorType = STRING)
public class ContentJpaImpl extends AbstractJpaModel implements Comparable<ContentJpaImpl> {
	private static final Logger LOG = LoggerFactory.getLogger(ContentJpaImpl.class);
	private static final String JPA_COL_BODY = "BODY_TXT";
	private static final String JPA_COL_CATEGORY = "CATEGORY_CD";
	private static final String JPA_COL_CONTENT_CD = "CONTENT_CD";
	private static final String JPA_COL_CONTENT_ID = JPA_TBL_CONTENT + "_ID";
	private static final String JPA_COL_CONTENT_NBR = "CONTENT_NBR";
	private static final String JPA_COL_CSS = "CSS_TXT";
	private static final String JPA_COL_DESCRIPTION = "DESC_TXT";
	private static final String JPA_COL_FLAGS = "FLAGS_CD";
	private static final String JPA_COL_HELPER = "HELPER_TXT";
	private static final String JPA_COL_NAME = "NAME_TXT";
	private static final String JPA_COL_ORDER = "ORDER_BY";
	private static final String JPA_COL_VALID_END = "VALID_END_DT";
	private static final String JPA_COL_VALID_START = "VALID_START_DT";
	private static final String JPA_SEQ_CONTENT = JPA_TBL_CONTENT + "_SQ";
	protected static final String JPA_COL_PARENT = "PARENT_ID";

	/** Fields **/
	@Id
	@ExcelColumn("Content ID")
	@ExcelColumnOrder(1)
	@GeneratedValue(generator = JPA_SEQ_CONTENT, strategy = GenerationType.AUTO)
	@SequenceGenerator(name = JPA_SEQ_CONTENT, sequenceName = JPA_SEQ_CONTENT)
	@Column(name = JPA_COL_CONTENT_ID)
	private Long id;
	@ExcelColumn("Parent ID")
	@ExcelColumnOrder(2)
	@Column(name = JPA_COL_PARENT, insertable = false, updatable = false)
	private Long parentId;
	@ExcelColumn("Content Code")
	@ExcelColumnOrder(3)
	@Column(name = JPA_COL_CONTENT_CD, length = 100, nullable = false, unique = true)
	private String contentCd;
	@ExcelColumn("Name")
	@ExcelColumnOrder(4)
	@Column(name = JPA_COL_NAME, length = 1000)
	private String name;
	@ExcelColumn("Description")
	@ExcelColumnOrder(5)
	@Column(name = JPA_COL_DESCRIPTION, length = 4000)
	private String description;
	@ExcelColumn("Content Number")
	@ExcelColumnOrder(6)
	@Column(name = JPA_COL_CONTENT_NBR, length = 10)
	private String contentNumber;
	@ExcelColumn("Content Body")
	@ExcelColumnOrder(7)
	@Column(name = JPA_COL_BODY, length = 4000, nullable = false)
	private String body;
	@ExcelColumn("Content CSS")
	@ExcelColumnOrder(8)
	@Column(name = JPA_COL_CSS, length = 4000)
	private String css;
	@ExcelColumn("Helper Text")
	@ExcelColumnOrder(9)
	@Column(name = JPA_COL_HELPER, length = 4000)
	private String helper;
	@ExcelColumn("Category Code")
	@ExcelColumnOrder(10)
	@Column(name = JPA_COL_CATEGORY, length = 100)
	private String category;
	@ExcelColumn("Flags")
	@ExcelColumnOrder(11)
	@Column(name = JPA_COL_FLAGS, length = 100)
	private String flags;
	@ExcelColumn("Order By")
	@ExcelColumnOrder(12)
	@Column(name = JPA_COL_ORDER)
	private Integer orderBy;
	@ExcelColumn("Start Date")
	@ExcelColumnOrder(13)
	@Column(name = JPA_COL_VALID_START, nullable = false)
	private Timestamp validStart = now();
	@ExcelColumnOrder(14)
	@ExcelColumn("End Date")
	@Column(name = JPA_COL_VALID_END, nullable = false)
	private Timestamp validEnd = max();

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
	public final void setId(final Long id) {
		this.id = id;
	}

	public final Long getParentId() {
		return parentId;
	}

	// Disables unintended change of Foreign Key
	public final void setParentId(final Long parentId) {
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