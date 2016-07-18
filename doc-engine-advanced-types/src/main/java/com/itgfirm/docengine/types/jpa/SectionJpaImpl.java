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
import com.itgfirm.docengine.types.AdvancedDocument;
import com.itgfirm.docengine.types.Clause;
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.Section;

/**
 * @author Justin Scott
 * 
 *         SectionJpaImpl Data Model
 */
@Entity
@DiscriminatorValue(value = TypeConstants.JPA_DISCRIMINATOR_SECTION)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
public class SectionJpaImpl extends ContentJpaImpl implements Section {

	/** Parent Type **/
	@JoinColumn(name = "PARENT_ID")
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY,
			targetEntity = AdvancedDocumentJpaImpl.class)
	@JsonDeserialize(as = AdvancedDocumentJpaImpl.class)
	private AdvancedDocument document;

	/** Child Type **/
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "section",
			targetEntity = ClauseJpaImpl.class)
	@JsonDeserialize(contentAs = ClauseJpaImpl.class)
	private List<Clause> clauses;

	public SectionJpaImpl() {}

	public SectionJpaImpl(String body) {
		super(body);
	}

	public SectionJpaImpl(String contentCd, String body) {
		this(body);
		this.setContentCd(contentCd);
	}

	public SectionJpaImpl(Content content) {
		super(content);
	}

	public SectionJpaImpl(Content content, String contentCd) {
		this(content);
		this.setContentCd(contentCd);
	}

	public SectionJpaImpl(Section section) {
		super(section);
		this.document = section.getDocument();
	}

	public SectionJpaImpl(Section section, String contentCd) {
		this(section);
		this.setContentCd(contentCd);
	}

	public AdvancedDocument getDocument() {
		return document;
	}

	public void setDocument(AdvancedDocument document) {
		this.document = document;
	}

	public List<Clause> getClauses() {
		return clauses;
	}

	public void addClause(Clause clause) {
		if (TypeUtils.isNotNullOrEmpty(clause)) {
			if (!TypeUtils.isNotNullOrEmpty(this.clauses)) {
				this.clauses = new ArrayList<Clause>();
			}
			clause.setSection(this);
			this.clauses.add(clause);
		}
	}

	public void addClauses(List<Clause> clauses) {
		if (TypeUtils.isNotNullOrEmpty(clauses)) {
			if (!TypeUtils.isNotNullOrEmpty(this.clauses)) {
				this.clauses = new ArrayList<Clause>();
			}
			ListIterator<Clause> iter = clauses.listIterator();
			while (iter.hasNext()) {
				Clause clause = iter.next();
				clause.setSection(this);
				iter.set(clause);
			}
			this.clauses.addAll(clauses);
		}
	}

	public void setClauses(List<Clause> clauses) {
		this.clauses = clauses;
	}
}