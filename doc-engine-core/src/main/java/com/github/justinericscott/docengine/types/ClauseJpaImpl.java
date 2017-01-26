package com.github.justinericscott.docengine.types;

import static com.github.justinericscott.docengine.types.AbstractJpaModel.ModelConstants.*;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;
import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

import java.util.Collection;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * @author Justin Scott
 * 
 *         ClauseJpaImpl Data Model
 */
@Entity
public class ClauseJpaImpl extends ContentJpaImpl {

	/** Parent Type **/
	@JsonBackReference("clauses")
	@ManyToOne(targetEntity = SectionJpaImpl.class, fetch = LAZY, cascade = REFRESH)
	private SectionJpaImpl section;

	/** Child Type **/
	@JsonManagedReference("paragraphs")
	@OrderColumn(name = JPA_COL_ORDER)
	@OneToMany(targetEntity = ParagraphJpaImpl.class, cascade = ALL, mappedBy = JPA_MAPPED_BY_CLAUSE)
	private Collection<ParagraphJpaImpl> paragraphs = new TreeSet<ParagraphJpaImpl>();

	public ClauseJpaImpl() {
		// Default constructor for Spring/Hibernate
	}

	public ClauseJpaImpl(final String code, final String body) {
		super(code, body);
	}

	public ClauseJpaImpl(final ContentJpaImpl content, final String code) {
		super(content, code);
	}

	public final SectionJpaImpl getSection() {
		return section;
	}

	public final void setSection(final SectionJpaImpl section) {
		this.section = section;
	}

	@JsonIgnore
	public final void addParagraph(final ParagraphJpaImpl paragraph) {
		if (isNotNullOrEmpty(paragraph)) {
			paragraph.setClause(this);
			paragraphs.add(paragraph);
		}
	}

	public final Collection<ParagraphJpaImpl> getParagraphs() {
		return paragraphs;
	}

	public final void setParagraphs(final Collection<ParagraphJpaImpl> paragraphs) {
		this.paragraphs = paragraphs;
	}
}