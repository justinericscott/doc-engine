package com.itgfirm.docengine.types;

import static com.itgfirm.docengine.types.AbstractJpaModel.ModelConstants.*;

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
 *         SectionJpaImpl Data Model
 */
@Entity
@DiscriminatorValue(value = JPA_DSCRMNTR_SECTION)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = JSON_PROP_ID)
public class SectionJpaImpl extends ContentJpaImpl {

	/** Parent Type **/
	@JoinColumn(name = JPA_COL_PARENT)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, targetEntity = AdvancedDocumentJpaImpl.class)
	@JsonDeserialize(as = AdvancedDocumentJpaImpl.class)
	private AdvancedDocumentJpaImpl document;

	/** Child Type **/
	@OneToMany(cascade = CascadeType.ALL, mappedBy = JPA_MAPPED_BY_SECTION, targetEntity = ClauseJpaImpl.class)
	@JsonDeserialize(contentAs = ClauseJpaImpl.class)
	private Collection<ClauseJpaImpl> clauses;

	public SectionJpaImpl() {
		// Default constructor for Spring/Hibernate
	}

	public SectionJpaImpl(final String code, final String body) {
		super(code, body);
	}

	public SectionJpaImpl(final ContentJpaImpl content, final String code) {
		super(content, code);
	}

	public SectionJpaImpl(final SectionJpaImpl section) {
		super(section);
		this.document = section.getDocument();
	}

	public final AdvancedDocumentJpaImpl getDocument() {
		return document;
	}

	public final void setDocument(final AdvancedDocumentJpaImpl document) {
		this.document = document;
	}

	public final Collection<ClauseJpaImpl> getClauses() {
		return clauses;
	}

	public final void addClause(final ClauseJpaImpl clause) {
		if (isNotNullOrEmpty(clause)) {
			if (!isNotNullOrEmpty(this.clauses)) {
				this.clauses = new TreeSet<ClauseJpaImpl>();
			}
			clause.setSection(this);
			this.clauses.add(clause);
		}
	}

	public final void addClauses(final Collection<ClauseJpaImpl> clauses) {
		if (isNotNullOrEmpty(clauses)) {
			if (!isNotNullOrEmpty(this.clauses)) {
				this.clauses = new TreeSet<ClauseJpaImpl>();
			}
			clauses.forEach(clause -> {
				clause.setSection(this);
			});
		}
		this.clauses.addAll(clauses);
	}

	public final void setClauses(final Collection<ClauseJpaImpl> clauses) {
		this.clauses = clauses;
	}
}