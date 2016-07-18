package com.itgfirm.docengine.types.jpa;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itgfirm.docengine.types.ClauseInstance;
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.ParagraphInstance;

/**
 * @author Justin Scott
 * 
 * ParagraphJpaImpl InstanceJpaImpl Data Model
 */
@JsonIdentityInfo( property = "@id",
		generator = ObjectIdGenerators.IntSequenceGenerator.class)
@Entity @DiscriminatorValue( TypeConstants.JPA_DISCRIMINATOR_PARAGRAPH )
public class ParagraphInstanceJpaImpl extends InstanceJpaImpl implements ParagraphInstance {
	private static final Logger LOG = LogManager.getLogger(ParagraphInstanceJpaImpl.class);
	
	/** Parent Type **/
	@JoinColumn( name = "PARENT_ID" )
	@ManyToOne( cascade = CascadeType.REFRESH,
			fetch = FetchType.LAZY,
			targetEntity = ClauseInstanceJpaImpl.class )
	@JsonDeserialize( as = ClauseInstanceJpaImpl.class )
	private ClauseInstance clause;

	public ParagraphInstanceJpaImpl() { }

	public ParagraphInstanceJpaImpl(Content content, String projectId) { 
		super(content);
		LOG.debug("Instantiating ParagraphJpaImpl InstanceJpaImpl.");
		this.setProjectId(projectId);
		this.setContent(content);
	}
	
	public ParagraphInstanceJpaImpl(Content content, String projectId, String body) {
		this(content, projectId);
		this.setBody(body);
	}
	
	public ParagraphInstanceJpaImpl(Content content, String projectId, String body, boolean isAdHoc) {
		this(content, projectId, body);
		this.setAdHoc(isAdHoc);
	}
	
	public ClauseInstance getClause() { return clause; }
	public void setClause(ClauseInstance clause) { this.clause = clause; }
}