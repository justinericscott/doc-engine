package com.itgfirm.docengine.types.jpa;

import static com.itgfirm.docengine.types.jpa.TypeUtils.*;
import static com.itgfirm.docengine.types.jpa.TypeConstants.*;

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
 *         ClauseJpaImpl InstanceJpaImpl Data Model
 */
@Entity
@DiscriminatorValue(JPA_DISCRIMINATOR_CLAUSE)
@JsonIdentityInfo(property = JSON_IDENTITY_PROPERTY, generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class ClauseInstanceJpaImpl extends InstanceJpaImpl {
	private static final String JPA_COLUMN_PARENT = "PARENT_ID";
	private static final String JPA_MAPPED_BY_CLAUSE = "clause";

	/** Parent Type **/
	@JoinColumn(name = JPA_COLUMN_PARENT)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, targetEntity = SectionInstanceJpaImpl.class)
	@JsonDeserialize(as = SectionInstanceJpaImpl.class)
	private SectionInstanceJpaImpl section;

	/** Child Type **/
	@OneToMany(cascade = CascadeType.ALL, mappedBy = JPA_MAPPED_BY_CLAUSE, targetEntity = ParagraphInstanceJpaImpl.class)
	@JsonDeserialize(contentAs = ParagraphInstanceJpaImpl.class)
	private Collection<ParagraphInstanceJpaImpl> paragraphs;

	public ClauseInstanceJpaImpl() {
		// Default constructor for Spring
	}

	public ClauseInstanceJpaImpl(final ClauseJpaImpl clause, final String projectId) {
		super(clause);
		this.setProjectId(projectId);
		this.setContent(clause);
		final Collection<ParagraphJpaImpl> paragraphs = clause.getParagraphs();
		if (isNotNullOrEmpty(paragraphs)) {
			instantiateParagraphs(paragraphs);
			clause.getParagraphs().clear();
		}
	}

	public ClauseInstanceJpaImpl(final ClauseJpaImpl clause, final String projectId, final String body) {
		this(clause, projectId);
		this.setBody(body);
	}

	public ClauseInstanceJpaImpl(final ClauseJpaImpl clause, final String projectId, final String body,
			final boolean isAdHoc) {
		this(clause, projectId, body);
		this.setAdHoc(isAdHoc);
	}

	public final SectionInstanceJpaImpl getSection() {
		return section;
	}

	public final void setSection(final SectionInstanceJpaImpl section) {
		this.section = section;
	}

	public final Collection<ParagraphInstanceJpaImpl> getParagraphs() {
		return paragraphs;
	}

	public final void instantiateParagraphs(final Collection<ParagraphJpaImpl> paragraphs) {
		if (isNotNullOrEmpty(paragraphs)) {
			paragraphs.forEach(p -> {
				this.addParagraph(new ParagraphInstanceJpaImpl(p, this.getProjectId()));
			});
		}
	}

	public final void addParagraph(final ParagraphInstanceJpaImpl paragraph) {
		if (isNotNullOrEmpty(paragraph)) {
			if (!isNotNullOrEmpty(paragraphs)) {
				this.paragraphs = new TreeSet<ParagraphInstanceJpaImpl>();
			}
			paragraph.setClause(this);
			this.paragraphs.add(paragraph);
		}
	}

	public final void addParagraphs(final Collection<ParagraphInstanceJpaImpl> paragraphs) {
		if (isNotNullOrEmpty(paragraphs)) {
			if (!isNotNullOrEmpty(paragraphs)) {
				this.paragraphs = new TreeSet<ParagraphInstanceJpaImpl>();
			}
			paragraphs.forEach(p -> {
				p.setClause(this);
			});
			this.paragraphs.addAll(paragraphs);
		}
	}

	public final void setParagraphs(final Collection<ParagraphInstanceJpaImpl> paragraphs) {
		this.paragraphs = paragraphs;
	}
}