package com.itgfirm.docengine.types;

import static com.itgfirm.docengine.types.AbstractJpaModel.ModelConstants.*;
import static com.itgfirm.docengine.util.Utils.isNotNullOrEmpty;

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
 *         ClauseJpaImpl InstanceJpaImpl Data Model
 */
@Entity
public class ClauseInstanceJpaImpl extends InstanceJpaImpl {

	/** Parent Type **/
	@JsonBackReference("clause-instance")
	@ManyToOne(targetEntity = SectionInstanceJpaImpl.class, fetch = LAZY, cascade = REFRESH)
	private SectionInstanceJpaImpl section;

	/** Child Type **/
	@JsonManagedReference("paragraph-instance")
	@OrderColumn(name = JPA_COL_ORDER)
	@OneToMany(targetEntity = ParagraphInstanceJpaImpl.class, cascade = ALL, mappedBy = JPA_MAPPED_BY_CLAUSE)
	private Collection<ParagraphInstanceJpaImpl> paragraphs = new TreeSet<ParagraphInstanceJpaImpl>();
	
	public ClauseInstanceJpaImpl() {
		// Default constructor for Spring
	}

	public ClauseInstanceJpaImpl(final String projectId) {
		super(projectId);
	}

	public ClauseInstanceJpaImpl(final ClauseJpaImpl clause, final String projectId) {
		this(projectId);
		super.setContent(clause);
		final Collection<ParagraphJpaImpl> paragraphs = clause.getParagraphs();
		if (isNotNullOrEmpty(paragraphs)) {
			instantiateParagraphs(paragraphs);
			paragraphs.clear();
		}
	}

	public ClauseJpaImpl getClause() {
		return (ClauseJpaImpl) content.getClass().cast(content);
	}

	public final void setClause(final ClauseJpaImpl clause) {
		super.setContent(clause);
	}

	public final SectionInstanceJpaImpl getSection() {
		return section;
	}

	public final void setSection(final SectionInstanceJpaImpl sectionInstance) {
		this.section = sectionInstance;
	}

	@JsonIgnore
	public final void addParagraph(final ParagraphInstanceJpaImpl paragraph) {
		if (isNotNullOrEmpty(paragraph)) {
			paragraph.setClause(this);
			this.paragraphs.add(paragraph);
		}
	}

	public final Collection<ParagraphInstanceJpaImpl> getParagraphs() {
		return paragraphs;
	}

	public final void setParagraphs(final Collection<ParagraphInstanceJpaImpl> paragraphs) {
		if (isNotNullOrEmpty(paragraphs)) {
			this.paragraphs = paragraphs;
		}
	}

	final void instantiateParagraphs(final Collection<ParagraphJpaImpl> paragraphs) {
		if (isNotNullOrEmpty(paragraphs)) {
			for (final ParagraphJpaImpl paragraph : paragraphs) {
				this.addParagraph(new ParagraphInstanceJpaImpl(paragraph, this.getProjectId()));
			}
		}
	}
}