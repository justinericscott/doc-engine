package com.github.justinericscott.docengine.models;

import static com.github.justinericscott.docengine.models.AbstractJpaModel.ModelConstants.*;
import static com.github.justinericscott.docengine.util.Utils.HTML.*;
import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Justin Scott
 * 
 *         ParagraphJpaImpl InstanceJpaImpl Data Model
 */
@Entity
public class ParagraphInstance extends Instance {

	/** Parent Type **/
	@JsonBackReference("paragraph-instance")
	@ManyToOne(targetEntity = ClauseInstance.class, fetch = LAZY, cascade = REFRESH)
	private ClauseInstance clause;

	public ParagraphInstance() {
		// Default constructor for Spring
	}

	public ParagraphInstance(final String projectId) {
		super(projectId);
	}

	public ParagraphInstance(final Paragraph paragraph, final String projectId) {
		this(projectId);
		super.setContent(paragraph);
	}

	public ParagraphInstance(final Paragraph paragraph, final String projectId, final String body) {
		this(paragraph, projectId);
		this.customBody = body;
	}

	public ParagraphInstance(final Paragraph paragraph, final String projectId, final String body,
			final boolean isAdHoc) {
		this(paragraph, projectId, body);
		this.isAdHoc = isAdHoc;
	}

	public final Paragraph getParagraph() {
		return (Paragraph) content.getClass().cast(content);
	}

	public final void setParagraph(final Paragraph paragraph) {
		super.setContent(paragraph);
	}

	public final ClauseInstance getClause() {
		return clause;
	}

	public final void setClause(final ClauseInstance clauseInstance) {
		this.clause = clauseInstance;
	}

	@JsonIgnore
	@Override
	public final String toHTML() {
		return toHTML(null);
	}

	final String toHTML(final String recursion) {
		return toHTML(recursion, false);
	}

	final String toHTML(final String recursion, final boolean isParentIncluded) {
		boolean isIncluded = Arrays.asList(STATUSES_IN).contains(statusCd);
		if (isIncluded) {
			return getParagraph().toHTML(recursion, customBody, false);
		} else if (!isIncluded && !getParagraph().getFlags().contains(HTML_STYLE_FLAG_OPTIONAL) && isParentIncluded) {
			return P.wrap("INTENTIONALLY DELETED");
		}
		return "";
	}
}