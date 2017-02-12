package com.github.justinericscott.docengine.models;

import static com.github.justinericscott.docengine.models.AbstractJpaModel.ModelConstants.*;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;
import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

import java.util.Arrays;
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
public class SectionInstance extends Instance {

	@JsonBackReference("section-instance")
	@ManyToOne(targetEntity = DocumentInstance.class, fetch = LAZY, cascade = REFRESH)
	private DocumentInstance document;

	@JsonManagedReference("clause-instance")
	@OrderColumn(name = JPA_COL_ORDER)
	@OneToMany(targetEntity = ClauseInstance.class, cascade = ALL, mappedBy = JPA_MAPPED_BY_SECTION)
	private Collection<ClauseInstance> clauses = new TreeSet<ClauseInstance>();

	public SectionInstance() {
		// Default constructor for Spring
	}

	public SectionInstance(final String projectId) {
		super(projectId);
	}

	public SectionInstance(final Section section, final String projectId) {
		this(projectId);
		super.setContent(section);
		final Collection<Clause> clauses = section.getClauses();
		if (isNotNullOrEmpty(clauses)) {
			instantiateClauses(clauses);
			clauses.clear();
		}
	}

	public final Section getSection() {
		return (Section) content.getClass().cast(content);
	}

	public final void setSection(final Section section) {
		super.setContent(section);
	}

	public final DocumentInstance getDocument() {
		return document;
	}

	public final void setDocument(final DocumentInstance documentInstance) {
		this.document = documentInstance;
	}

	@JsonIgnore
	public final void addClause(final ClauseInstance clause) {
		if (isNotNullOrEmpty(clause)) {
			clause.setSection(this);
			clauses.add(clause);
		}
	}

	public final Collection<ClauseInstance> getClauses() {
		return clauses;
	}

	public final void setClauses(final Collection<ClauseInstance> clauses) {
		if (isNotNullOrEmpty(clauses)) {
			this.clauses = clauses;
		}
	}

	final void instantiateClauses(final Collection<Clause> clauses) {
		if (isNotNullOrEmpty(clauses)) {
			for (final Clause clause : clauses) {
				this.addClause(new ClauseInstance(clause, this.getProjectId()));
			}
		}
	}

	@JsonIgnore
	@Override
	public final String toHTML() {
		final StringBuilder sb = new StringBuilder();
		if (Arrays.asList(STATUSES_IN).contains(statusCd)) {
			sb.append(getSection().toHTML(customBody, customNumber));
			for (final ClauseInstance clause : getClauses()) {
				sb.append(clause.toHTML());
			}			
		}
		return sb.toString();
	}
}