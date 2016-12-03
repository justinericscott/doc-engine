package com.itgfirm.docengine.types.jpa;

import static com.itgfirm.docengine.types.jpa.AbstractJpaModel.ModelConstants.*;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author Justin Scott
 * 
 *         ParagraphJpaImpl InstanceJpaImpl Data Model
 */
@Entity
@DiscriminatorValue(JPA_DSCRMNTR_PARAGRAPH)
@JsonIdentityInfo(property = JSON_PROP_ID, generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class ParagraphInstanceJpaImpl extends InstanceJpaImpl {
	private static final String JPA_COLUMN_PARENT = "PARENT_ID";

	/** Parent Type **/
	@JoinColumn(name = JPA_COLUMN_PARENT)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY, targetEntity = ClauseInstanceJpaImpl.class)
	@JsonDeserialize(as = ClauseInstanceJpaImpl.class)
	private ClauseInstanceJpaImpl clause;

	public ParagraphInstanceJpaImpl() {
		// Default constructor for Spring
	}

	public ParagraphInstanceJpaImpl(final ContentJpaImpl content, final String projectId) {
		super(content);
		this.setProjectId(projectId);
		this.setContent(content);
	}

	public ParagraphInstanceJpaImpl(final ContentJpaImpl content, final String projectId, final String body) {
		this(content, projectId);
		this.setBody(body);
	}

	public ParagraphInstanceJpaImpl(final ContentJpaImpl content, final String projectId, final String body, final boolean isAdHoc) {
		this(content, projectId, body);
		this.setAdHoc(isAdHoc);
	}

	public final ClauseInstanceJpaImpl getClause() {
		return clause;
	}

	public final void setClause(final ClauseInstanceJpaImpl clause) {
		this.clause = clause;
	}
}