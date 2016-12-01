package com.itgfirm.docengine.types.jpa;

import static com.itgfirm.docengine.types.jpa.TypeConstants.*;
import static com.itgfirm.docengine.types.jpa.TypeUtils.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author Justin Scott
 * 
 *         ParagraphJpaImpl Data Model
 */
@Entity
@DiscriminatorValue(value = JPA_DSCRMNTR_PARAGRAPH)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = JSON_PROP_ID)
public class ParagraphJpaImpl extends ContentJpaImpl {
	private static final String JPA_COLUMN_IS_SUB = "IS_SUB_PARA_BLN";
	private static final String JPA_COLUMN_IS_FIRST = "IS_FIRST_BLN";
	private static final String JPA_COLUMN_IS_LAST = "IS_LAST_BLN";
	private static final String JPA_COLUMN_IS_PARENT = "IS_PARENT_BLN";
	private static final String JPA_COLUMN_IS_OPTIONAL = "IS_OPTIONAL_BLN";

	@Column(name = JPA_COLUMN_IS_SUB)
	private boolean isSubPara = false;
	@Column(name = JPA_COLUMN_IS_FIRST)
	private boolean isFirst = false;
	@Column(name = JPA_COLUMN_IS_LAST)
	private boolean isLast = false;
	@Column(name = JPA_COLUMN_IS_PARENT)
	private boolean isParent = false;
	@Column(name = JPA_COLUMN_IS_OPTIONAL)
	private boolean isOptional = false;

	/** Parent Type **/
	@JoinColumn(name = JPA_COL_PARENT)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, targetEntity = ClauseJpaImpl.class)
	@JsonDeserialize(as = ClauseJpaImpl.class)
	private ClauseJpaImpl clause;
	
	public ParagraphJpaImpl() {
		// Default constructor for Spring/Hibernate
	}
	
	public ParagraphJpaImpl(final String code, final String body) {
		super(code, body);
	}

	public ParagraphJpaImpl(final ContentJpaImpl content, final String code) {
		super(content, code);
	}

	public ParagraphJpaImpl(final ParagraphJpaImpl paragraph) {
		super(paragraph);
		if (isNotNullOrEmpty(paragraph)) {
			this.isSubPara = paragraph.isSubPara();
			this.isFirst = paragraph.isFirst();
			this.isLast = paragraph.isLast();
			this.isParent = paragraph.isParent();
			this.isOptional = paragraph.isOption();
			this.clause = paragraph.getClause();
		}
	}

	public final ClauseJpaImpl getClause() {
		return clause;
	}

	public final void setClause(final ClauseJpaImpl clause) {
		this.clause = clause;
	}

	public final boolean isSubPara() {
		return isSubPara;
	}

	public final void setSubPara(final boolean isSubPara) {
		this.isSubPara = isSubPara;
	}

	public final boolean isFirst() {
		return isFirst;
	}

	public final void setFirst(final boolean isFirst) {
		this.isFirst = isFirst;
	}

	public final boolean isLast() {
		return isLast;
	}

	public final void setLast(final boolean isLast) {
		this.isLast = isLast;
	}

	public final boolean isParent() {
		return isParent;
	}

	public final void setParent(final boolean isParent) {
		this.isParent = isParent;
	}

	public final boolean isOption() {
		return isOptional;
	}

	public final void setOption(final boolean isOption) {
		this.isOptional = isOption;
	}
}