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
 *         SectionJpaImpl Data Model
 */
@Entity
public class SectionJpaImpl extends ContentJpaImpl {

	/** Parent Type **/
	@JsonBackReference("sections")
	@ManyToOne(targetEntity = DocumentJpaImpl.class, fetch = LAZY, cascade = REFRESH)
	private DocumentJpaImpl document;

	/** Child Type **/
	@JsonManagedReference("clauses")
	@OrderColumn(name = JPA_COL_ORDER)
	@OneToMany(targetEntity = ClauseJpaImpl.class, cascade = ALL, mappedBy = JPA_MAPPED_BY_SECTION)
	private Collection<ClauseJpaImpl> clauses = new TreeSet<ClauseJpaImpl>();

	public SectionJpaImpl() {
		// Default constructor for Spring/Hibernate
	}

	public SectionJpaImpl(final String code, final String body) {
		super(code, body);
	}

	public SectionJpaImpl(final ContentJpaImpl content, final String code) {
		super(content, code);
	}

	public final DocumentJpaImpl getDocument() {
		return document;
	}

	public final void setDocument(final DocumentJpaImpl document) {
		this.document = document;
	}

	@JsonIgnore
	public final void addClause(final ClauseJpaImpl clause) {
		if (isNotNullOrEmpty(clause)) {
			clause.setSection(this);
			clauses.add(clause);
		}
	}

	public final Collection<ClauseJpaImpl> getClauses() {
		return clauses;
	}

	public final void setClauses(final Collection<ClauseJpaImpl> clauses) {
		this.clauses = clauses;
	}
}