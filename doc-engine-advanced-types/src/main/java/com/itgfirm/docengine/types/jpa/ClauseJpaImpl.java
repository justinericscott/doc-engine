package com.itgfirm.docengine.types.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itgfirm.docengine.types.Clause;
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.Paragraph;
import com.itgfirm.docengine.types.Section;

/**
 * @author Justin Scott
 * 
 *         ClauseJpaImpl Data Model
 */
@Entity
@DiscriminatorValue(value = TypeConstants.JPA_DISCRIMINATOR_CLAUSE)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class ClauseJpaImpl extends ContentJpaImpl implements Clause {

	/** Parent Type **/
	@JoinColumn(name = "PARENT_ID")
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY,
			targetEntity = SectionJpaImpl.class)
	@JsonDeserialize(as = SectionJpaImpl.class)
	private Section section;

	/** Child Type **/
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "clause",
			targetEntity = ParagraphJpaImpl.class)
	@JsonDeserialize(contentAs = ParagraphJpaImpl.class)
	private List<Paragraph> paragraphs;

	public ClauseJpaImpl() {}

	public ClauseJpaImpl(String body) {
		super(body);
	}

	public ClauseJpaImpl(String contentCd, String body) {
		this(body);
		this.setContentCd(contentCd);
	}

	public ClauseJpaImpl(Content content) {
		super(content);
	}

	public ClauseJpaImpl(Content content, String contentCd) {
		this(content);
		this.setContentCd(contentCd);
	}

	public ClauseJpaImpl(Clause clause) {
		super(clause);
		this.section = clause.getSection();
	}

	public ClauseJpaImpl(Clause clause, String contentCd) {
		this(clause);
		this.setContentCd(contentCd);
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public List<Paragraph> getParagraphs() {
		return paragraphs;
	}

	public void addParagraph(Paragraph paragraph) {
		if (TypeUtils.isNotNullOrEmpty(paragraph)) {
			if (!TypeUtils.isNotNullOrEmpty(this.paragraphs)) {
				this.paragraphs = new ArrayList<Paragraph>();
			}
			paragraph.setClause(this);
			this.paragraphs.add(paragraph);
		}
	}

	public void addParagraphs(List<Paragraph> paragraphs) {
		if (TypeUtils.isNotNullOrEmpty(paragraphs)) {
			if (!TypeUtils.isNotNullOrEmpty(this.paragraphs)) {
				this.paragraphs = new ArrayList<Paragraph>();
			}
			ListIterator<Paragraph> iter = paragraphs.listIterator();
			while (iter.hasNext()) {
				Paragraph paragraph = iter.next();
				paragraph.setClause(this);
				iter.set(paragraph);
			}
			this.paragraphs.addAll(paragraphs);
		}
	}

	public void setParagraphs(List<Paragraph> paragraph) {
		this.paragraphs = paragraph;
	}
}