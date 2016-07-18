package com.itgfirm.docengine.types.jpa;

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
import com.itgfirm.docengine.types.Clause;
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.Paragraph;

/**
 * @author Justin Scott
 * 
 *         ParagraphJpaImpl Data Model
 */
@Entity
@DiscriminatorValue(value = TypeConstants.JPA_DISCRIMINATOR_PARAGRAPH)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class ParagraphJpaImpl extends ContentJpaImpl implements Paragraph {

	@Column(name = "IS_SUB_PARA_BLN")
	private boolean isSubPara = false;
	@Column(name = "IS_FIRST_BLN")
	private boolean isFirst = false;
	@Column(name = "IS_LAST_BLN")
	private boolean isLast = false;
	@Column(name = "IS_PARENT_BLN")
	private boolean isParent = false;
	@Column(name = "IS_OPTIONAL_BLN")
	private boolean isOptional = false;

	/** Parent Type **/
	@JoinColumn(name = "PARENT_ID")
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY,
			targetEntity = ClauseJpaImpl.class)
	@JsonDeserialize(as = ClauseJpaImpl.class)
	private Clause clause;

	public ParagraphJpaImpl() {}

	public ParagraphJpaImpl(String body) {
		super(body);
	}

	public ParagraphJpaImpl(String contentCd, String body) {
		this(body);
		this.setContentCd(contentCd);
	}

	public ParagraphJpaImpl(Content content) {
		super(content);
	}

	public ParagraphJpaImpl(Content content, String contentCd) {
		this(content);
		this.setContentCd(contentCd);
	}

	public ParagraphJpaImpl(Paragraph paragraph) {
		super(paragraph);
		this.isSubPara = paragraph.isSubPara();
		this.isFirst = paragraph.isFirst();
		this.isLast = paragraph.isLast();
		this.isParent = paragraph.isParent();
		this.isOptional = paragraph.isOption();
		this.clause = paragraph.getClause();
	}

	public ParagraphJpaImpl(Paragraph paragraph, String contentCd) {
		this(paragraph);
		this.setContentCd(contentCd);
	}

	public Clause getClause() {
		return clause;
	}

	public void setClause(Clause clause) {
		this.clause = clause;
	}

	public boolean isSubPara() {
		return isSubPara;
	}

	public void setSubPara(boolean isSubPara) {
		this.isSubPara = isSubPara;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

	public boolean isParent() {
		return isParent;
	}

	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	public boolean isOption() {
		return isOptional;
	}

	public void setOption(boolean isOption) {
		this.isOptional = isOption;
	}
}