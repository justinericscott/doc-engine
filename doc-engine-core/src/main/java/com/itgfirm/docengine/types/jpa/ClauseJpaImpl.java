package com.itgfirm.docengine.types.jpa;

import static com.itgfirm.docengine.types.jpa.TypeConstants.*;
import static com.itgfirm.docengine.types.jpa.TypeUtils.*;

import java.util.Collection;
import java.util.TreeSet;

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

/**
 * @author Justin Scott
 * 
 *         ClauseJpaImpl Data Model
 */
@Entity
@DiscriminatorValue(value = JPA_DISCRIMINATOR_CLAUSE)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = JSON_IDENTITY_PROPERTY)
public class ClauseJpaImpl extends ContentJpaImpl {
	private static final String JPA_MAPPED_BY_CLAUSE = "clause";

	/** Parent Type **/
	@JoinColumn(name = JPA_COLUMN_PARENT)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, targetEntity = SectionJpaImpl.class)
	@JsonDeserialize(as = SectionJpaImpl.class)
	private SectionJpaImpl section;

	/** Child Type **/
	@OneToMany(cascade = CascadeType.ALL, mappedBy = JPA_MAPPED_BY_CLAUSE, targetEntity = ParagraphJpaImpl.class)
	@JsonDeserialize(contentAs = ParagraphJpaImpl.class)
	private Collection<ParagraphJpaImpl> paragraphs;
	
	public ClauseJpaImpl() {
		// Default constructor for Spring/Hibernate
	}
	
	public ClauseJpaImpl(final String code, final String body) {
		super(code, body);
	}

	public ClauseJpaImpl(final ContentJpaImpl content, final String code) {
		super(content, code);
	}

	public ClauseJpaImpl(final ClauseJpaImpl clause) {
		super(clause);
		this.section = clause.getSection();
	}

	public final SectionJpaImpl getSection() {
		return section;
	}

	public final void setSection(final SectionJpaImpl section) {
		this.section = section;
	}

	public final Collection<ParagraphJpaImpl> getParagraphs() {
		return paragraphs;
	}

	public final void addParagraph(final ParagraphJpaImpl paragraph) {
		if (isNotNullOrEmpty(paragraph)) {
			if (!isNotNullOrEmpty(this.paragraphs)) {
				this.paragraphs = new TreeSet<ParagraphJpaImpl>();
			}
			paragraph.setClause(this);
			this.paragraphs.add(paragraph);
		}
	}

	public final void addParagraphs(final Collection<ParagraphJpaImpl> paragraphs) {
		if (isNotNullOrEmpty(paragraphs)) {
			if (!isNotNullOrEmpty(this.paragraphs)) {
				this.paragraphs = new TreeSet<ParagraphJpaImpl>();
			}
			paragraphs.forEach(paragraph -> {
				paragraph.setClause(this);
			});
			this.paragraphs.addAll(paragraphs);
		}
	}

	public final void setParagraphs(final Collection<ParagraphJpaImpl> paragraph) {
		this.paragraphs = paragraph;
	}
}