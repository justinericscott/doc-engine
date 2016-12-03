package com.itgfirm.docengine.types.jpa;

import static javax.persistence.DiscriminatorType.STRING;
import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.InheritanceType.SINGLE_TABLE;

import static com.itgfirm.docengine.types.jpa.AbstractJpaModel.ModelConstants.*;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@ExcelSheet(EXCEL_SHEET_NAME_CONTENT)
@Inheritance(strategy = SINGLE_TABLE)
@DiscriminatorValue(JPA_DSCRMNTR_CONTENT)
@DiscriminatorColumn(name = JPA_DSCRMNTR_COL, discriminatorType = STRING)
public class ContentJpaImpl extends AbstractJpaModel implements Comparable<ContentJpaImpl> {
	private static final Logger LOG = LoggerFactory.getLogger(ContentJpaImpl.class);

	@Id
	@ExcelColumn(EXCEL_COL_CONTENT_ID)
	@ExcelColumnOrder(1)
	@GeneratedValue(generator = JPA_SEQ_CONTENT, strategy = AUTO)
	@SequenceGenerator(name = JPA_SEQ_CONTENT, sequenceName = JPA_SEQ_CONTENT)
	@Column(name = JPA_COL_CONTENT_ID)
	private Long id;
	@ExcelColumn(EXCEL_COL_PARENT)
	@ExcelColumnOrder(2)
	@Column(name = JPA_COL_PARENT, insertable = false, updatable = false)
	private Long parentId;
	@ExcelColumn(EXCEL_COL_CONTENT_CD)
	@ExcelColumnOrder(3)
	@Column(name = JPA_COL_CONTENT_CD, length = 100, nullable = false, unique = true)
	private String contentCd;
	@ExcelColumn(EXCEL_COL_NAME)
	@ExcelColumnOrder(4)
	@Column(name = JPA_COL_NAME, length = 1000)
	private String name;
	@ExcelColumn(EXCEL_COL_DESCRIPTION)
	@ExcelColumnOrder(5)
	@Column(name = JPA_COL_DESCRIPTION, length = 4000)
	private String description;
	@ExcelColumn(EXCEL_COL_CONTENT_NBR)
	@ExcelColumnOrder(6)
	@Column(name = JPA_COL_CONTENT_NBR, length = 10)
	private String contentNumber;
	@ExcelColumn(EXCEL_COL_BODY)
	@ExcelColumnOrder(7)
	@Column(name = JPA_COL_BODY, length = 4000, nullable = false)
	private String body;
	@ExcelColumn(EXCEL_COL_CSS)
	@ExcelColumnOrder(8)
	@Column(name = JPA_COL_CSS, length = 4000)
	private String css;
	@ExcelColumn(EXCEL_COL_HELPER)
	@ExcelColumnOrder(9)
	@Column(name = JPA_COL_HELPER, length = 4000)
	private String helper;
	@ExcelColumn(EXCEL_COL_CATEGORY)
	@ExcelColumnOrder(10)
	@Column(name = JPA_COL_CATEGORY, length = 100)
	private String category;
	@ExcelColumn(EXCEL_COL_FLAGS)
	@ExcelColumnOrder(11)
	@Column(name = JPA_COL_FLAGS, length = 100)
	private String flags;
	@ExcelColumn(EXCEL_COL_ORDER)
	@ExcelColumnOrder(12)
	@Column(name = JPA_COL_ORDER)
	private Integer orderBy;
	@ExcelColumn(EXCEL_COL_VALID_START)
	@ExcelColumnOrder(13)
	@Column(name = JPA_COL_VALID_START, nullable = false)
	private Timestamp validStart = now();
	@ExcelColumn(EXCEL_COL_VALID_END)
	@ExcelColumnOrder(14)
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

	public final void setId(final Long id) {
		this.id = id;
	}

	public final Long getParentId() {
		return parentId;
	}

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