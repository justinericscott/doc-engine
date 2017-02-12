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
 *         SectionJpaImpl Data Model
 */
@Entity
public class Section extends Content {

	/** Parent Type **/
	@JsonBackReference("sections")
	@ManyToOne(targetEntity = Document.class, fetch = LAZY, cascade = REFRESH)
	private Document document;

	/** Child Type **/
	@JsonManagedReference("clauses")
	@OrderColumn(name = JPA_COL_ORDER)
	@OneToMany(targetEntity = Clause.class, cascade = ALL, mappedBy = JPA_MAPPED_BY_SECTION)
	private Collection<Clause> clauses = new TreeSet<Clause>();

	public Section() {
		// Default constructor for Spring/Hibernate
	}

	public Section(final String code, final String body) {
		super(code, body);
	}

	public Section(final Content content, final String code) {
		super(content, code);
	}

	public final Document getDocument() {
		return document;
	}

	public final void setDocument(final Document document) {
		this.document = document;
	}

	@JsonIgnore
	public final void addClause(final Clause clause) {
		if (isNotNullOrEmpty(clause)) {
			clause.setSection(this);
			clauses.add(clause);
		}
	}

	public final Collection<Clause> getClauses() {
		return clauses;
	}

	public final void setClauses(final Collection<Clause> clauses) {
		this.clauses = clauses;
	}

	@JsonIgnore
	@Override
	public final String toHTML() {
		return toHTML(false);
	}
	
	public final String toHTML(final boolean header) {
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
		if (orderBy > 1) {
			sb.append(BR.self("break"));
		}
		sb.append(HR.self());
		if (isNotNullOrEmpty(contentNumber)) {
			sb.append(H1.wrap(String.format("%s%s%s", contentNumber, tab(), body)));
		} else {
			sb.append(H1.wrap(body));
		}
		sb.append(HR.self());
		clauses.forEach(clause -> {
			final String add = clause.getCss();
			if (isNotNullOrEmpty(css) && !css.contains(add)) {
				css = css.concat("\n\n").concat(add);
			} else if (!isNotNullOrEmpty(css)) {
				css = add;
			}
			sb.append(clause.toHTML());
		});
		if (header) {
			final String title = TITLE.wrap("Test Title - Section");
			final String head = HEAD.wrap(title.concat(STYLE.style(css, HTML_CSS_TYPE_TEXT)));
			final String body = BODY.wrap(sb.toString());
			final String html = String.format("%s%s", head, body);
			return doctype().concat(DOCUMENT.wrap(html, null, namespace()));
		} else {
			return sb.toString();
		}
	}
}