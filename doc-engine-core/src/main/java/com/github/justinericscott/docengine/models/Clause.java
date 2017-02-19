package com.github.justinericscott.docengine.models;

import static com.github.justinericscott.docengine.models.AbstractJpaModel.ModelConstants.*;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;
import static com.github.justinericscott.docengine.util.Utils.HTML.*;

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
 *         ClauseJpaImpl Data Model
 */
@Entity
public class Clause extends Content {
	
	/** Parent Type **/
	@JsonBackReference("clauses")
	@ManyToOne(targetEntity = Section.class, fetch = LAZY, cascade = REFRESH)
	private Section section;

	/** Child Type **/
	@JsonManagedReference("paragraphs")
	@OrderColumn(name = JPA_COL_ORDER)
	@OneToMany(targetEntity = Paragraph.class, cascade = ALL, mappedBy = JPA_MAPPED_BY_CLAUSE)
	private Collection<Paragraph> paragraphs = new TreeSet<Paragraph>();

	public Clause() {
		// Default constructor for Spring/Hibernate
	}

	public Clause(final String code, final String body) {
		super(code, body);
	}

	public Clause(final Content content, final String code) {
		super(content, code);
	}

	public final Section getSection() {
		return section;
	}

	public final void setSection(final Section section) {
		this.section = section;
	}

	@JsonIgnore
	public final void addParagraph(final Paragraph paragraph) {
		if (isNotNullOrEmpty(paragraph)) {
			paragraph.setClause(this);
			paragraphs.add(paragraph);
		}
	}

	public final Collection<Paragraph> getParagraphs() {
		return paragraphs;
	}

	public final void setParagraphs(final Collection<Paragraph> paragraphs) {
		this.paragraphs = paragraphs;
	}

	@JsonIgnore
	@Override
	public final String toHTML() {
		return toHTML(false);
	}
	
	final String toHTML(final boolean header) {
		return toHTML(null, header);
	}

	final String toHTML(final String customBody) {
		return toHTML(customBody, null);
	}
	
	final String toHTML(final String customBody, final boolean header) {
		return toHTML(customBody, null, header);
	}
	
	final String toHTML(final String customBody, final String customNumber) {
		return toHTML(customBody, customNumber, false);
	}

	final String toHTML(final String customBody, final String customNumber, final boolean header) {
		this.body = (isNotNullOrEmpty(customBody) ? customBody : body);
		this.contentNumber = (isNotNullOrEmpty(customNumber) ? customNumber : contentNumber);
		final StringBuilder sb = new StringBuilder();
		sb.append(DIV.open("group"));
		if (isNotNullOrEmpty(contentNumber)) {
			sb.append(H2.wrap(String.format("%s%s%s", contentNumber, tab(), body)));
		} else {
			sb.append(H2.wrap(body));
		}
		if (paragraphs.isEmpty()) {
			sb.append(DIV.close());
		} else {
			sb.append(Paragraph.getParagraphsHTML(this));
		}
		if (header) {
			final String title = TITLE.wrap("Test Title - Clause");
			final String head = HEAD.wrap(title.concat(STYLE.style(css, HTML_CSS_TYPE_TEXT)));
			final String body = BODY.wrap(sb.toString());
			final String html = String.format("%s%s", head, body);
			return doctype().concat(DOCUMENT.wrap(html, null, namespace()));
		} else {
			return sb.toString();
		}
	}
}