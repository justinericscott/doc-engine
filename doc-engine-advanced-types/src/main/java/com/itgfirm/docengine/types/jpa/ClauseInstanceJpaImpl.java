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
import com.itgfirm.docengine.types.Clause;
import com.itgfirm.docengine.types.ClauseInstance;
import com.itgfirm.docengine.types.Paragraph;
import com.itgfirm.docengine.types.ParagraphInstance;
import com.itgfirm.docengine.types.SectionInstance;

/**
 * @author Justin Scott
 * 
 * ClauseJpaImpl InstanceJpaImpl Data Model
 */
@JsonIdentityInfo( property = "@id", 
		generator = ObjectIdGenerators.IntSequenceGenerator.class )
@Entity @DiscriminatorValue( TypeConstants.JPA_DISCRIMINATOR_CLAUSE )
public class ClauseInstanceJpaImpl extends InstanceJpaImpl implements ClauseInstance {
	private static final Logger LOG = LogManager.getLogger(ClauseInstanceJpaImpl.class);
	
	/** Parent Type **/
	@JoinColumn( name = "PARENT_ID" )
	@ManyToOne( cascade = CascadeType.REFRESH,
			fetch = FetchType.LAZY,
			targetEntity = SectionInstanceJpaImpl.class )
	@JsonDeserialize( as = SectionInstanceJpaImpl.class )
	private SectionInstance section;
	
	/** Child Type **/
	@OneToMany( cascade = CascadeType.ALL, 
			mappedBy = "clause",
			targetEntity = ParagraphInstanceJpaImpl.class )
	@JsonDeserialize( contentAs = ParagraphInstanceJpaImpl.class )
	private List<ParagraphInstance> paragraphs;

	public ClauseInstanceJpaImpl() {  }
	
	public ClauseInstanceJpaImpl(Clause clause, String projectId) {
		super(clause);
		LOG.debug("Instantiating Clause Instance.");
		this.setProjectId(projectId);
		this.setContent(clause);
		List<Paragraph> paragraph = clause.getParagraphs();
		if (TypeUtils.isNotNullOrEmpty(paragraph)) {
			LOG.debug("Attemptng To Instantiate Paragraph Instances.");
			instantiateParagraphs(clause.getParagraphs());
			clause.getParagraphs().clear();			
		}
	}
	
	public ClauseInstanceJpaImpl(Clause clause, String projectId, String body) {
		this(clause, projectId);
		this.setBody(body);
	}
	
	public ClauseInstanceJpaImpl(Clause clause, String projectId, String body, 
			boolean isAdHoc) {
		this(clause, projectId, body);
		this.setAdHoc(isAdHoc);
	}

	public SectionInstance getSection() { return section; }
	public void setSection(SectionInstance section) { this.section = section; }

	public List<ParagraphInstance> getParagraphs() { return paragraphs; }
	public void instantiateParagraphs(List<Paragraph> paragraph) {
		if (TypeUtils.isNotNullOrEmpty(paragraph)) {
			for (Paragraph p : paragraph) {
				this.addParagraph(new ParagraphInstanceJpaImpl(p, this.getProjectId()));
			}
		}
	}
	public void addParagraph(ParagraphInstance paragraph) {
		if (TypeUtils.isNotNullOrEmpty(paragraph)) {
			if (!TypeUtils.isNotNullOrEmpty(paragraphs)) {
				this.paragraphs = new ArrayList<ParagraphInstance>();
			}
			paragraph.setClause(this);
			this.paragraphs.add(paragraph);
		}
	}
	public void addParagraphs(List<ParagraphInstance> paragraphs) {
		if (TypeUtils.isNotNullOrEmpty(paragraphs)) {
			if (!TypeUtils.isNotNullOrEmpty(paragraphs)) {
				this.paragraphs = new ArrayList<ParagraphInstance>();
			}
	 		ListIterator<ParagraphInstance> iter = paragraphs.listIterator();
	 		while (iter.hasNext()) {
	 			ParagraphInstance paragraph = iter.next();
	 			paragraph.setClause(this);
	 			iter.set(paragraph);
	 		}
			this.paragraphs.addAll(paragraphs);
		}		
	}
	public void setParagraphs(List<ParagraphInstance> paragraphs) { this.paragraphs = paragraphs; }
}