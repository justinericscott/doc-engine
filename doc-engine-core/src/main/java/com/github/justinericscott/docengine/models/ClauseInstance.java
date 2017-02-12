package com.github.justinericscott.docengine.models;

import static com.github.justinericscott.docengine.models.AbstractJpaModel.ModelConstants.*;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;
import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
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
 *         ClauseJpaImpl InstanceJpaImpl Data Model
 */
@Entity
public class ClauseInstance extends Instance {

	/** Parent Type **/
	@JsonBackReference("clause-instance")
	@ManyToOne(targetEntity = SectionInstance.class, fetch = LAZY, cascade = REFRESH)
	private SectionInstance section;

	/** Child Type **/
	@JsonManagedReference("paragraph-instance")
	@OrderColumn(name = JPA_COL_ORDER)
	@OneToMany(targetEntity = ParagraphInstance.class, cascade = ALL, mappedBy = JPA_MAPPED_BY_CLAUSE)
	private Collection<ParagraphInstance> paragraphs = new TreeSet<ParagraphInstance>();

	public ClauseInstance() {
		// Default constructor for Spring
	}

	public ClauseInstance(final String projectId) {
		super(projectId);
	}

	public ClauseInstance(final Clause clause, final String projectId) {
		this(projectId);
		super.setContent(clause);
		final Collection<Paragraph> paragraphs = clause.getParagraphs();
		if (isNotNullOrEmpty(paragraphs)) {
			instantiateParagraphs(paragraphs);
			paragraphs.clear();
		}
	}

	public Clause getClause() {
		return (Clause) content.getClass().cast(content);
	}

	public final void setClause(final Clause clause) {
		super.setContent(clause);
	}

	public final SectionInstance getSection() {
		return section;
	}

	public final void setSection(final SectionInstance sectionInstance) {
		this.section = sectionInstance;
	}

	@JsonIgnore
	public final void addParagraph(final ParagraphInstance paragraph) {
		if (isNotNullOrEmpty(paragraph)) {
			paragraph.setClause(this);
			this.paragraphs.add(paragraph);
		}
	}

	public final Collection<ParagraphInstance> getParagraphs() {
		return paragraphs;
	}

	public final void setParagraphs(final Collection<ParagraphInstance> paragraphs) {
		if (isNotNullOrEmpty(paragraphs)) {
			this.paragraphs = paragraphs;
		}
	}

	final void instantiateParagraphs(final Collection<Paragraph> paragraphs) {
		if (isNotNullOrEmpty(paragraphs)) {
			for (final Paragraph paragraph : paragraphs) {
				this.addParagraph(new ParagraphInstance(paragraph, this.getProjectId()));
			}
		}
	}

	@JsonIgnore
	@Override
	public final String toHTML() {
		final StringBuilder sb = new StringBuilder();
		if (Arrays.asList(STATUSES_IN).contains(statusCd)) {
			sb.append(getClause().toHTML(customBody, customNumber));
			final Iterator<ParagraphInstance> it = getParagraphs().iterator();
			while (it.hasNext()) {
				sb.append(getParagraphsHTML(it));
			}	
		}
		return sb.toString();
	}

	private String getParagraphsHTML(final Iterator<ParagraphInstance> iter) {
		while (iter.hasNext()) {
			final ParagraphInstance current = iter.next();
			boolean isIncluded = Arrays.asList(STATUSES_IN).contains(current.getStatusCd());
			if (isIncluded) {
				if (current.getParagraph().getFlags().contains(HTML_STYLE_FLAG_PARENT)) {
					return current.toHTML(getParagraphsHTML(iter), isIncluded);					
				} else {
					return current.toHTML();
				}
			}			
		}
		return "";
	}
}