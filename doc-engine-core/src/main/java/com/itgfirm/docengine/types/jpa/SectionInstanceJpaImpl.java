package com.itgfirm.docengine.types.jpa;

import static com.itgfirm.docengine.types.jpa.AbstractJpaModel.ModelConstants.*;

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
 *         SectionJpaImpl InstanceJpaImpl Data Model
 */
@Entity
@DiscriminatorValue(JPA_DSCRMNTR_SECTION)
@JsonIdentityInfo(property = JSON_PROP_ID, generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class SectionInstanceJpaImpl extends InstanceJpaImpl {

	@JoinColumn(name = JPA_COL_PARENT)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, targetEntity = AdvancedDocumentInstanceJpaImpl.class)
	@JsonDeserialize(as = AdvancedDocumentInstanceJpaImpl.class)
	private AdvancedDocumentInstanceJpaImpl document;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = JPA_MAPPED_BY_SECTION, targetEntity = ClauseInstanceJpaImpl.class)
	@JsonDeserialize(contentAs = ClauseInstanceJpaImpl.class)
	private Collection<ClauseInstanceJpaImpl> clauses;

	public SectionInstanceJpaImpl() {
		// Default constructor for Spring
	}

	public SectionInstanceJpaImpl(final SectionJpaImpl section, final String projectId) {
		super(section);
		this.setProjectId(projectId);
		this.setContent(section);
		final Collection<ClauseJpaImpl> clauses = section.getClauses();
		if (isNotNullOrEmpty(clauses)) {
			instantiateClauses(clauses);
			section.getClauses().clear();
		}
	}

	public SectionInstanceJpaImpl(final SectionJpaImpl section, final String projectId, final String body) {
		this(section, projectId);
		this.setBody(body);
	}

	public SectionInstanceJpaImpl(final SectionJpaImpl section, final String projectId, final String body, final boolean isAdHoc) {
		this(section, projectId, body);
		this.setAdHoc(isAdHoc);
	}

	public final AdvancedDocumentInstanceJpaImpl getDocument() {
		return document;
	}

	public final void setDocument(final AdvancedDocumentInstanceJpaImpl document) {
		this.document = document;
	}

	public final Collection<ClauseInstanceJpaImpl> getClauses() {
		return clauses;
	}

	public final void instantiateClauses(final Collection<ClauseJpaImpl> clauses) {
		if (isNotNullOrEmpty(clauses)) {
			clauses.forEach(c -> {
				this.addClause(new ClauseInstanceJpaImpl(c, this.getProjectId()));
			});
		}
	}

	public final void addClause(final ClauseInstanceJpaImpl clause) {
		if (isNotNullOrEmpty(clause)) {
			if (!isNotNullOrEmpty(this.clauses)) {
				this.clauses = new TreeSet<ClauseInstanceJpaImpl>();
			}
			clause.setSection(this);
			this.clauses.add(clause);
		}
	}

	public final void addClauses(final Collection<ClauseInstanceJpaImpl> clauses) {
		if (isNotNullOrEmpty(clauses)) {
			if (!isNotNullOrEmpty(this.clauses)) {
				this.clauses = new TreeSet<ClauseInstanceJpaImpl>();
			}
			clauses.forEach(c -> {
				c.setSection(this);
			});
			this.clauses.addAll(clauses);
		}
	}

	public final void setClauses(final Collection<ClauseInstanceJpaImpl> clauses) {
		this.clauses = clauses;
	}
}