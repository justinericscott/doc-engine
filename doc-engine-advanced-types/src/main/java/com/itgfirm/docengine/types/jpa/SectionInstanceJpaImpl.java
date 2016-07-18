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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itgfirm.docengine.types.AdvancedDocumentInstance;
import com.itgfirm.docengine.types.Clause;
import com.itgfirm.docengine.types.ClauseInstance;
import com.itgfirm.docengine.types.Section;
import com.itgfirm.docengine.types.SectionInstance;

/**
 * @author Justin Scott
 * 
 * SectionJpaImpl InstanceJpaImpl Data Model
 */
@JsonIdentityInfo( property = "@id", 
		generator = ObjectIdGenerators.IntSequenceGenerator.class )
@Entity @DiscriminatorValue( TypeConstants.JPA_DISCRIMINATOR_SECTION )
public class SectionInstanceJpaImpl extends InstanceJpaImpl implements SectionInstance {
	private static final Logger LOG = LogManager.getLogger(SectionInstanceJpaImpl.class);

	/** Parent Type **/
	@JoinColumn( name = "PARENT_ID" )
	@ManyToOne( cascade = CascadeType.REFRESH,
			fetch = FetchType.LAZY,
			targetEntity = AdvancedDocumentInstanceJpaImpl.class )
	@JsonDeserialize( as = AdvancedDocumentInstanceJpaImpl.class )
	private AdvancedDocumentInstance document;
	
	/** Child Type **/
	@OneToMany( cascade = CascadeType.ALL,
			mappedBy = "section", 
			targetEntity = ClauseInstanceJpaImpl.class )
	@JsonDeserialize( contentAs = ClauseInstanceJpaImpl.class )
	private List<ClauseInstance> clauses;

	public SectionInstanceJpaImpl() { }
	
	public SectionInstanceJpaImpl(Section section, String projectId) {
		super(section);
		LOG.debug("Instantiating SectionJpaImpl InstanceJpaImpl.");
		this.setProjectId(projectId);
		this.setContent(section);
		List<Clause> clauseJpaImpls = section.getClauses();
		if (TypeUtils.isNotNullOrEmpty(clauseJpaImpls)) {
			LOG.debug("Attemptng To Instantiating ClauseJpaImpl Instances.");
			instantiateClauses(section.getClauses());
			section.getClauses().clear();			
		}
	}
	
	public SectionInstanceJpaImpl(Section section, String projectId, String body) {
		this(section, projectId);
		this.setBody(body);
	}
	
	public SectionInstanceJpaImpl(Section section, String projectId, String body, 
			boolean isAdHoc) {
		this(section, projectId, body);
		this.setAdHoc(isAdHoc);
	}

	public AdvancedDocumentInstance getDocument() { return document; }
	public void setDocument(AdvancedDocumentInstance document) { this.document = document; }

	public List<ClauseInstance> getClauses() { return clauses; }
	public void instantiateClauses(List<Clause> clause) {
		if (TypeUtils.isNotNullOrEmpty(clause)) {
			for (Clause c : clause) {
				this.addClause(new ClauseInstanceJpaImpl(c, this.getProjectId()));
			}
		}
	}
	public void addClause(ClauseInstance clause) {
		if (TypeUtils.isNotNullOrEmpty(clause)) {
			if (!TypeUtils.isNotNullOrEmpty(this.clauses)) {
				this.clauses = new ArrayList<ClauseInstance>();
			}
			clause.setSection(this);
			this.clauses.add(clause);
		}
	}
	public void addClauses(List<ClauseInstance> clauses) {
		if (TypeUtils.isNotNullOrEmpty(clauses)) {
			if (!TypeUtils.isNotNullOrEmpty(this.clauses)) {
				this.clauses = new ArrayList<ClauseInstance>();
			}
	 		ListIterator<ClauseInstance> iter = clauses.listIterator();
	 		while (iter.hasNext()) {
	 			ClauseInstance clause = iter.next();
	 			clause.setSection(this);
	 			iter.set(clause);
	 		}
			this.clauses.addAll(clauses);
		}		
	}
	public void setClauses(List<ClauseInstance> clauses) { this.clauses = clauses; }
}