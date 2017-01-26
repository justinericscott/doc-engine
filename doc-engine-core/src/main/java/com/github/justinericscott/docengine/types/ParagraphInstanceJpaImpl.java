package com.github.justinericscott.docengine.types;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * @author Justin Scott
 * 
 *         ParagraphJpaImpl InstanceJpaImpl Data Model
 */
@Entity
public class ParagraphInstanceJpaImpl extends InstanceJpaImpl {

	/** Parent Type **/
	@JsonBackReference("paragraph-instance")
	@ManyToOne(targetEntity = ClauseInstanceJpaImpl.class, fetch = LAZY, cascade = REFRESH)
	private ClauseInstanceJpaImpl clause;
	
	public ParagraphInstanceJpaImpl() {
		// Default constructor for Spring
	}
	
	public ParagraphInstanceJpaImpl(final String projectId) {
		super(projectId); 
	}

	public ParagraphInstanceJpaImpl(final ParagraphJpaImpl paragraph, final String projectId) {
		this(projectId);
		super.setContent(paragraph);
	}

	public ParagraphInstanceJpaImpl(final ParagraphJpaImpl paragraph, final String projectId, final String body) {
		this(paragraph, projectId);
		this.customBody = body;
	}

	public ParagraphInstanceJpaImpl(final ParagraphJpaImpl paragraph, final String projectId, final String body, final boolean isAdHoc) {
		this(paragraph, projectId, body);
		this.isAdHoc = isAdHoc;
	}

	public final ParagraphJpaImpl getParagraph() {
		return (ParagraphJpaImpl) content.getClass().cast(content);
	}

	public final void setParagraph(final ParagraphJpaImpl paragraph) {
		super.setContent(paragraph);
	}

	public final ClauseInstanceJpaImpl getClause() {
		return clause;
	}

	public final void setClause(final ClauseInstanceJpaImpl clauseInstance) {
		this.clause = clauseInstance;
	}
}