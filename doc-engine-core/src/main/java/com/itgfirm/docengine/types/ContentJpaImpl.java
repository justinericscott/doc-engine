package com.itgfirm.docengine.types;

import static com.itgfirm.docengine.types.AbstractJpaModel.ModelConstants.*;
import static com.itgfirm.docengine.util.Utils.isNotNullOrEmpty;
import static com.itgfirm.docengine.util.Utils.isNotNullOrZero;

import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.TemporalType.DATE;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itgfirm.docengine.annotation.ExcelColumn;
import com.itgfirm.docengine.annotation.ExcelColumnOrder;
import com.itgfirm.docengine.annotation.ExcelSheet;

/**
 * @author Justin Scott
 * 
 *         ContentJpaImpl Data Model
 */
@Entity
@ExcelSheet(EXCEL_SHEET_NAME_CONTENT)
@Table(name = JPA_TBL_CONTENT)
public class ContentJpaImpl extends AbstractJpaModel implements Comparable<ContentJpaImpl> {
	private static final Logger LOG = LoggerFactory.getLogger(ContentJpaImpl.class);

	@Id
	@GeneratedValue(strategy = AUTO, generator = JPA_SEQ_CONTENT)
	@SequenceGenerator(name = JPA_SEQ_CONTENT, sequenceName = JPA_SEQ_CONTENT)
	@Column(name = JPA_COL_CONTENT_ID, unique = true)
	@ExcelColumn(EXCEL_COL_CONTENT_ID)
	@ExcelColumnOrder(1)
	protected Long id;
	@Column(name = JPA_COL_PARENT, length = 4000)
	@ExcelColumn(EXCEL_COL_PARENT)
	@ExcelColumnOrder(2)
	private Long parentId;
	@Column(name = JPA_COL_CONTENT_CD, length = 100, nullable = false, unique = true)
	@ExcelColumn(EXCEL_COL_CONTENT_CD)
	@ExcelColumnOrder(3)
	protected String contentCd;
	@Column(name = JPA_COL_NAME, length = 1000)
	@ExcelColumn(EXCEL_COL_NAME)
	@ExcelColumnOrder(4)
	protected String name;
	@Column(name = JPA_COL_DESCRIPTION, length = 4000)
	@ExcelColumn(EXCEL_COL_DESCRIPTION)
	@ExcelColumnOrder(5)
	protected String description;
	@Column(name = JPA_COL_CONTENT_NBR, length = 10)
	@ExcelColumn(EXCEL_COL_CONTENT_NBR)
	@ExcelColumnOrder(6)
	protected String contentNumber;
	@Column(name = JPA_COL_BODY, length = 4000, nullable = false)
	@ExcelColumn(EXCEL_COL_BODY)
	@ExcelColumnOrder(7)
	protected String body;
	@Column(name = JPA_COL_CSS, length = 4000)
	@ExcelColumn(EXCEL_COL_CSS)
	@ExcelColumnOrder(8)
	protected String css;
	@Column(name = JPA_COL_HELPER, length = 4000)
	@ExcelColumn(EXCEL_COL_HELPER)
	@ExcelColumnOrder(9)
	protected String helper;
	@Column(name = JPA_COL_CATEGORY, length = 100)
	@ExcelColumn(EXCEL_COL_CATEGORY)
	@ExcelColumnOrder(10)
	protected String category;
	@Column(name = JPA_COL_FLAGS, length = 100)
	@ExcelColumn(EXCEL_COL_FLAGS)
	@ExcelColumnOrder(11)
	protected String flags;
	@Column(name = JPA_COL_ORDER)
	@ExcelColumn(EXCEL_COL_ORDER)
	@ExcelColumnOrder(12)
	protected Integer orderBy;
	@Temporal(DATE)
	@Column(name = JPA_COL_VALID_START, nullable = false)
	@ExcelColumn(EXCEL_COL_VALID_START)
	@ExcelColumnOrder(13)
	protected Date validStart = now();
	@Temporal(DATE)
	@Column(name = JPA_COL_VALID_END, nullable = false)
	@ExcelColumn(EXCEL_COL_VALID_END)
	@ExcelColumnOrder(14)
	protected Date validEnd = max();
	
	public ContentJpaImpl() {
		// Default constructor for Spring
	}

	public ContentJpaImpl(Integer id) {
		this.id = Long.valueOf(String.valueOf(id));
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
		if (isNotNullOrEmpty(contentCd) && isNotNullOrEmpty(content) && !contentCd.equals(content.getContentCd())) {
			this.contentCd = contentCd;
		}
	}

	public final Long getId() {
		return id;
	}

	public final void setId(final Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
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

	public final Date getValidStart() {
		return validStart;
	}

	public final void setValidStart(final Date validStart) {
		this.validStart = validStart;
	}

	public final Date getValidEnd() {
		return validEnd;
	}

	public final void setValidEnd(final Date validEnd) {
		this.validEnd = validEnd;
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
		if (!isNotNullOrEmpty(contentCd)) {
			LOG.debug("Content code must not be null or empty!");
			return false;
		}
		if (!isNotNullOrEmpty(body)) {
			LOG.debug("Content body must not be null or empty for ID {} and Code {}!", id, contentCd);
			return false;
		}
		return true;
	}

	@JsonIgnore
	@Override
	public final int compareTo(final ContentJpaImpl o) {
		return this.getContentCd().compareTo(o.getContentCd());
	}
}