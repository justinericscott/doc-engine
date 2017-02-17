package com.github.justinericscott.docengine.models;

import static com.github.justinericscott.docengine.models.AbstractJpaModel.ModelConstants.*;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;
import static com.github.justinericscott.docengine.util.Utils.HTML.*;
import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

/**
 * @author Justin Scott
 * 
 *         ParagraphJpaImpl Data Model
 */
@Entity
public class Paragraph extends Content {

	private boolean isFirst = false;
	private boolean isFirstInClause = false;
	private boolean isLast = false;
	private boolean isOptional = false;
	private boolean isParent = false;
	private boolean isSubPara = false;
	private boolean isTable = false;

	@JsonBackReference("paragraphs")
	@ManyToOne(targetEntity = Clause.class, fetch = LAZY, cascade = REFRESH)
	private Clause clause;

	public Paragraph() {
		// Default constructor for Spring/Hibernate
	}

	public Paragraph(final String code, final String body) {
		super(code, body);
	}

	public Paragraph(final Content content) {
		super(content);
	}

	public Paragraph(final Content content, final String code) {
		super(content, code);
	}

	public Paragraph(final Paragraph paragraph, final String code) {
		super(paragraph, code);
		this.clause = paragraph.getClause();
	}

	public Paragraph(final Paragraph paragraph) {
		super(paragraph);
		this.clause = paragraph.getClause();
	}

	public final Clause getClause() {
		return clause;
	}

	public final void setClause(final Clause clause) {
		this.clause = clause;
	}

	public final String toHTML() {
		return toHTML(null);
	}

	final String toHTML(final boolean header) {
		return toHTML(null, null, header);
	}

	final String toHTML(final String recursion) {
		return toHTML(recursion, null, false);
	}

	final String toHTML(final String recursion, final boolean header) {
		return toHTML(recursion, null, header);
	}

	final String toHTML(final String recursion, final String custom, boolean header) {
		setFlags();
		this.body = (isNotNullOrEmpty(custom) ? custom : body);
		final StringBuilder sb = new StringBuilder();
		if (isNotNullOrEmpty(helper)) {
			sb.append(helper);
		}
		if (!isOptional || (isOptional && true)) {
			if (isSubPara && !isTable) {
				if (isFirst) {
					sb.append(OL.open());
				}
				
				if (isNotNullOrEmpty(clause) && isFirstInClause) {
					if (isParent && isNotNullOrEmpty(recursion)) {
						sb.append(LI.wrap(body.concat(recursion)));
					} else {
						sb.append(LI.wrap(body));
					}
					sb.append(OL.close());
					sb.append(DIV.close());
					if (!isLast) {
						sb.append(OL.open(null, "start=2"));
					}
				} else {
					if (isParent && isNotNullOrEmpty(recursion)) {
						sb.append(LI.wrap(body.concat(recursion)));
					} else {
						sb.append(LI.wrap(body));
					}
				}
				
				if (isLast) {
					sb.append(OL.close());
				}
			
			} else if (!isSubPara && !isTable) {
				sb.append(P.wrap(body));
				if (isNotNullOrEmpty(clause) && isFirstInClause) {
					sb.append(DIV.close());
				}
				if (isNotNullOrEmpty(recursion)) {
					sb.append(recursion);
				}
			} else {
				sb.append(body);
				if (isFirstInClause && isNotNullOrEmpty(clause)) {
					sb.append(DIV.close());
				}
				if (isNotNullOrEmpty(recursion)) {
					sb.append(recursion);
				}
			}
		}
		if (header) {
			final String title = TITLE.wrap("Test Title - Paragraph");
			final String head = HEAD.wrap(title.concat(STYLE.style(css.trim(), HTML_CSS_TYPE_TEXT)));
			final String body = BODY.wrap(sb.toString());
			return doctype().concat(DOCUMENT.wrap(String.format("%s%s", head, body), null, namespace()));
		} else {
			return sb.toString();
		}
	}

	private void setFlags() {
		if (isNotNullOrEmpty(flags)) {
			isFirst = flags.contains(HTML_STYLE_FLAG_FIRST);
			isFirstInClause = flags.contains(HTML_STYLE_FLAG_FIRST_IN_CLAUSE);
			isLast = flags.contains(HTML_STYLE_FLAG_LAST);
			isOptional = flags.contains(HTML_STYLE_FLAG_OPTIONAL);
			isParent = flags.contains(HTML_STYLE_FLAG_PARENT);
			isSubPara = flags.contains(HTML_STYLE_FLAG_SUB);
			isTable = contentCd.toLowerCase().contains(TABLE.tag().toLowerCase());
		}
	}
}