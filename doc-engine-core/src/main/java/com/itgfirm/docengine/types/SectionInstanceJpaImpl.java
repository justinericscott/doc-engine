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
 *         SectionJpaImpl InstanceJpaImpl Data Model
 */
@Entity
public class SectionInstanceJpaImpl extends InstanceJpaImpl {

	@JsonBackReference("section-instance")
	@ManyToOne(targetEntity = DocumentInstanceJpaImpl.class, fetch = LAZY, cascade = REFRESH)
	private DocumentInstanceJpaImpl document;

	@JsonManagedReference("clause-instance")
	@OrderColumn(name = JPA_COL_ORDER)
	@OneToMany(targetEntity = ClauseInstanceJpaImpl.class, cascade = ALL, mappedBy = JPA_MAPPED_BY_SECTION)
	private Collection<ClauseInstanceJpaImpl> clauses = new TreeSet<ClauseInstanceJpaImpl>();
	
	public SectionInstanceJpaImpl() {
		// Default constructor for Spring
	}

	public SectionInstanceJpaImpl(final String projectId) {
		super(projectId);
	}

	public SectionInstanceJpaImpl(final SectionJpaImpl section, final String projectId) {
		this(projectId);
		super.setContent(section);
		final Collection<ClauseJpaImpl> clauses = section.getClauses();
		if (isNotNullOrEmpty(clauses)) {
			instantiateClauses(clauses);
			clauses.clear();
		}
	}

	public final SectionJpaImpl getSection() {
		return (SectionJpaImpl) content.getClass().cast(content);
	}

	public final void setSection(final SectionJpaImpl section) {
		super.setContent(section);
	}

	public final DocumentInstanceJpaImpl getDocument() {
		return document;
	}

	public final void setDocument(final DocumentInstanceJpaImpl documentInstance) {
		this.document = documentInstance;
	}

	@JsonIgnore
	public final void addClause(final ClauseInstanceJpaImpl clause) {
		if (isNotNullOrEmpty(clause)) {
			clause.setSection(this);
			clauses.add(clause);
		}
	}

	public final Collection<ClauseInstanceJpaImpl> getClauses() {
		return clauses;
	}

	public final void setClauses(final Collection<ClauseInstanceJpaImpl> clauses) {
		if (isNotNullOrEmpty(clauses)) {
			this.clauses = clauses;
		}
	}

	final void instantiateClauses(final Collection<ClauseJpaImpl> clauses) {
		if (isNotNullOrEmpty(clauses)) {
			for (final ClauseJpaImpl clause : clauses) {
				this.addClause(new ClauseInstanceJpaImpl(clause, this.getProjectId()));
			}
		}
	}
}