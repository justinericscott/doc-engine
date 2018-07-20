package com.github.justinericscott.docengine.models;

import static com.github.justinericscott.docengine.util.Utils.*;
import static com.github.justinericscott.docengine.util.Utils.HTML.*;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.AUTO;
import static javax.persistence.TemporalType.DATE;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import com.github.justinericscott.docengine.annotation.ExcelColumn;
import com.github.justinericscott.docengine.annotation.ExcelColumnOrder;
import com.github.justinericscott.docengine.annotation.ExcelSheet;

/**
 * @author Justin Scott
 * 
 *         ContentJpaImpl Data Model
 */
@Entity
@ExcelSheet(Content.XLS_SHEET_CONTENT)
@Table(name = Content.DB_TBL_CONTENT)
public class Content extends AbstractJpaModel implements Comparable<Content> {
	static final String CONTENT_FLAG_FIRST = "FIRST";
	static final String CONTENT_FLAG_FIRST_IN_CLAUSE = "1ST_IN_CLAUSE";
	static final String CONTENT_FLAG_LAST = "LAST";
	static final String CONTENT_FLAG_SUB = "SUB";
	static final String DB_TBL_CONTENT = "CONTENT";
	static final String XLS_SHEET_CONTENT = "Content";
	private static final String DB_SEQ_CONTENT = DB_TBL_CONTENT + "_SQ";
	private static final String DB_COL_BODY = "BODY_TXT";
	private static final String DB_COL_CATEGORY = "CATEGORY_CD";
	private static final String DB_COL_CONTENT_CD = "CONTENT_CD";
	private static final String DB_COL_CONTENT_ID = DB_TBL_CONTENT + "_ID";
	private static final String DB_COL_CONTENT_NBR = "CONTENT_NBR";
	private static final String DB_COL_CSS = "CSS_TXT";
	private static final String DB_COL_HELPER = "HELPER_TXT";
	private static final String DB_COL_VALID_END = "VALID_END_DT";
	private static final String DB_COL_VALID_START = "VALID_START_DT";	
	private static final String XLS_COL_BODY = "Content Body";
	private static final String XLS_COL_CATEGORY = "Category Code";
	private static final String XLS_COL_CONTENT_CD = "Content ID";
	private static final String XLS_COL_CONTENT_NBR = "Content Number";
	private static final String XLS_COL_CSS = "Content CSS";
	private static final String XLS_COL_HELPER = "Helper Text";
	private static final String XLS_COL_VALID_START = "Start Date";
	private static final String XLS_COL_VALID_END = "End Date";
	private static final Logger LOG = LoggerFactory.getLogger(Content.class);

	@Column(name = DB_COL_CONTENT_ID, unique = true)
	@GeneratedValue(strategy = AUTO, generator = DB_SEQ_CONTENT)
	@Id
	@SequenceGenerator(name = DB_SEQ_CONTENT, sequenceName = DB_SEQ_CONTENT)
	protected Long id;
	@Column(name = DB_COL_PARENT, length = 4000)
	private Long parentId;
	@Column(name = DB_COL_DISCRIMINATOR, updatable = false, insertable = false)
	@ExcelColumn(XLS_COL_DISCRIMINATOR)
	@ExcelColumnOrder(1)
	private String discriminator;
	@Column(name = DB_COL_CONTENT_CD, length = 100, nullable = false, unique = true)
	@ExcelColumn(XLS_COL_CONTENT_CD)
	@ExcelColumnOrder(2)
	protected String contentCd;
	@Column(name = DB_COL_CONTENT_NBR, length = 10)
	@ExcelColumn(XLS_COL_CONTENT_NBR)
	@ExcelColumnOrder(3)
	protected String contentNumber;
	@Column(name = DB_COL_NAME, length = 1000)
	@ExcelColumn(XLS_COL_NAME)
	@ExcelColumnOrder(4)
	protected String name;
	@Column(name = DB_COL_DESCRIPTION, length = 4000)
	@ExcelColumn(XLS_COL_DESCRIPTION)
	@ExcelColumnOrder(5)
	protected String description;
	@Column(name = DB_COL_CATEGORY, length = 100)
	@ExcelColumn(XLS_COL_CATEGORY)
	@ExcelColumnOrder(6)
	protected String category;
	@Column(name = DB_COL_ORDER)
	@ExcelColumn(XLS_COL_ORDER)
	@ExcelColumnOrder(7)
	protected Integer orderBy;
	@Column(name = DB_COL_BODY, length = 4000, nullable = false)
	@ExcelColumn(XLS_COL_BODY)
	@ExcelColumnOrder(8)
	protected String body;
	@Column(name = DB_COL_CSS, length = 4000)
	@ExcelColumn(XLS_COL_CSS)
	@ExcelColumnOrder(9)
	protected String css;
	@Column(name = DB_COL_HELPER, length = 4000)
	@ExcelColumn(XLS_COL_HELPER)
	@ExcelColumnOrder(10)
	protected String helper;
	@Column(name = DB_COL_FLAGS, length = 100)
	@ExcelColumn(XLS_COL_FLAGS)
	@ExcelColumnOrder(11)
	protected String flags;
	@Column(name = DB_COL_VALID_START, nullable = false)
	@ExcelColumn(XLS_COL_VALID_START)
	@ExcelColumnOrder(12)
	@Temporal(DATE)
	protected Date validStart = now();
	@Column(name = DB_COL_VALID_END, nullable = false)
	@ExcelColumn(XLS_COL_VALID_END)
	@ExcelColumnOrder(13)
	@Temporal(DATE)
	protected Date validEnd = max();

	public Content() {
		// Default constructor for Spring
	}

	public Content(Integer id) {
		this.id = Long.valueOf(String.valueOf(id));
	}

	public Content(final String body) {
		if (isNotNullOrEmpty(body)) {
			this.body = body;
		}
	}

	public Content(final String contentCd, final String body) {
		this(body);
		if (isNotNullOrEmpty(contentCd)) {
			this.contentCd = contentCd;
		}
	}

	public Content(final Content content) {
		if (isNotNullOrEmpty(content)) {
			name = content.getName();
			description = content.getDescription();
			body = content.getBody();
			css = content.getCss();
			helper = content.getHelper();
			category = content.getCategory();
			flags = content.getFlags();
			orderBy = content.getOrderBy();
		}
	}

	public Content(final Content content, final String contentCd) {
		this(content);
		if (isNotNullOrEmpty(contentCd) && isNotNullOrEmpty(content) && !contentCd.equals(content.getContentCd())) {
			this.contentCd = contentCd;
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getDiscriminator() {
		return discriminator;
	}

	public void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	public String getContentCd() {
		return contentCd;
	}

	public void setContentCd(final String contentCd) {
		this.contentCd = contentCd;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getContentNumber() {
		return contentNumber;
	}

	public void setContentNumber(final String contentNumber) {
		this.contentNumber = contentNumber;
	}

	public String getBody() {
		return body;
	}

	public void setBody(final String body) {
		this.body = body;
	}

	public String getCss() {
		return css;
	}

	public void setCss(final String css) {
		this.css = css;
	}

	public String getHelper() {
		return helper;
	}

	public void setHelper(final String helper) {
		this.helper = helper;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(final String category) {
		this.category = category;
	}

	public String getFlags() {
		return flags;
	}

	public void setFlags(final String flags) {
		this.flags = flags;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(final Integer orderBy) {
		this.orderBy = orderBy;
	}

	public Date getValidStart() {
		return validStart;
	}

	public void setValidStart(final Date validStart) {
		this.validStart = validStart;
	}

	public Date getValidEnd() {
		return validEnd;
	}

	public void setValidEnd(final Date validEnd) {
		this.validEnd = validEnd;
	}

	public boolean isValid() {
		return isValid(false);
	}

	public boolean isValid(final boolean checkForId) {
		if (checkForId && !isNotNullOrZero(id)) {
			LOG.warn("ID must not be null or zero!");
			return false;
		}
		if (!isNotNullOrEmpty(contentCd)) {
			LOG.warn("Content code must not be null or empty!");
			return false;
		}
		if (!isNotNullOrEmpty(body)) {
			LOG.warn("Content body must not be null or empty for ID {} and Code {}!", id, contentCd);
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(final Content o) {
		if (isNotNullOrEmpty(o)) {
			if (isNotNullOrEmpty(o.getContentCd())) {
				if (isNotNullOrEmpty(this.getContentCd())) {
					return this.getContentCd().compareTo(o.getContentCd());	
				} else {
					LOG.debug("This object does not have a content code.");
				}
			} else {
				LOG.debug("The comparison object does not have a content code.");
			}
		} else {
			LOG.debug("The comparison object is null.");
		}
		return 0;
	}
	
//	@Override
	public String toHTML() {
		return body;
	}

	@Entity
	public static class Document extends Content {

		/** Child Type **/
		@JsonManagedReference("sections")
		@OneToMany(cascade = ALL, mappedBy = JPA_MAPPED_BY_DOCUMENT, targetEntity = Section.class)
		@OrderColumn(name = DB_COL_ORDER)
		private Collection<Section> sections = new TreeSet<Section>();

		public Document() {
			// Default constructor for Spring/Hibernate
		}

		public Document(final String code, final String body) {
			super(code, body);
		}

		public Document(final Content content) {
			super(content);
		}

		public Document(final Content content, final String code) {
			super(content, code);
		}

		public void addSection(final Section section) {
			if (isNotNullOrEmpty(section)) {
				section.setDocument(this);
				this.sections.add(section);
			}
		}

		public Collection<Section> getSections() {
			return (Collection<Section>) sections;
		}

		public void setSections(final Collection<Section> sections) {
			this.sections = sections;
		}

		@Override
		public String toHTML() {
			final StringBuilder sb = new StringBuilder();
			for (final Section section : getSections()) {
				sb.append(section.toHTML());
			}
			return sb.toString();
		}	
	}

	@Entity
	public static class Section extends Content {

		/** Parent Type **/
		@JsonBackReference("sections")
		@ManyToOne(cascade = REFRESH, fetch = LAZY, targetEntity = Document.class)
		private Document document;

		/** Child Type **/
		@JsonManagedReference("clauses")
		@OneToMany(cascade = ALL, mappedBy = JPA_MAPPED_BY_SECTION, targetEntity = Clause.class)
		@OrderColumn(name = DB_COL_ORDER)
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

		public Document getDocument() {
			return document;
		}

		public void setDocument(final Document document) {
			this.document = document;
		}

		public void addClause(final Clause clause) {
			if (isNotNullOrEmpty(clause)) {
				clause.setSection(this);
				clauses.add(clause);
			}
		}

		public Collection<Clause> getClauses() {
			return clauses;
		}

		public void setClauses(final Collection<Clause> clauses) {
			this.clauses = clauses;
		}

		@Override
		public String toHTML() {
			return toHTML(false);
		}
		
		public String toHTML(final boolean header) {
			return toHTML(null, header);
		}

		String toHTML(final String customBody) {
			return toHTML(customBody, null);
		}
		
		String toHTML(final String customBody, final boolean header) {
			return toHTML(customBody, null, header);
		}
		
		String toHTML(final String customBody, final String customNumber) {
			return toHTML(customBody, customNumber, false);
		}

		String toHTML(final String customBody, final String customNumber, final boolean header) {
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
				sb.append(clause.toHTML());
				final String add = clause.getCss();
				if (isNotNullOrEmpty(css) && !css.contains(add)) {
					css = css.concat("\n\n").concat(add);
				} else if (!isNotNullOrEmpty(css)) {
					css = add;
				}
			});
			if (header) {
				final String title = TITLE.wrap("Test Title - Section");
				final String head = HEAD.wrap(title.concat(STYLE.style(css, CSS_TYPE_TEXT)));
				final String body = BODY.wrap(sb.toString());
				final String html = String.format("%s%s", head, body);
				return doctype().concat(DOCUMENT.wrap(html, null, namespace()));
			} else {
				return sb.toString();
			}
		}
	}

	@Entity
	public static class Clause extends Content {
		
		/** Parent Type **/
		@JsonBackReference("clauses")
		@ManyToOne(cascade = REFRESH, fetch = LAZY, targetEntity = Section.class)
		private Section section;

		/** Child Type **/
		@JsonManagedReference("paragraphs")
		@OneToMany(cascade = ALL, mappedBy = JPA_MAPPED_BY_CLAUSE, targetEntity = Paragraph.class)
		@OrderColumn(name = DB_COL_ORDER)
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

		public Section getSection() {
			return section;
		}

		public void setSection(final Section section) {
			this.section = section;
		}

		public void addParagraph(final Paragraph paragraph) {
			if (isNotNullOrEmpty(paragraph)) {
				paragraph.setClause(this);
				paragraphs.add(paragraph);
			}
		}

		public Collection<Paragraph> getParagraphs() {
			return paragraphs;
		}

		public void setParagraphs(final Collection<Paragraph> paragraphs) {
			this.paragraphs = paragraphs;
		}

		@Override
		public String toHTML() {
			return toHTML(false);
		}
		
		String toHTML(final boolean header) {
			return toHTML(null, header);
		}

		String toHTML(final String customBody) {
			return toHTML(customBody, null);
		}
		
		String toHTML(final String customBody, final boolean header) {
			return toHTML(customBody, null, header);
		}
		
		String toHTML(final String customBody, final String customNumber) {
			return toHTML(customBody, customNumber, false);
		}

		String toHTML(final String customBody, final String customNumber, final boolean header) {
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
				final String head = HEAD.wrap(title.concat(STYLE.style(css, CSS_TYPE_TEXT)));
				final String body = BODY.wrap(sb.toString());
				final String html = String.format("%s%s", head, body);
				return doctype().concat(DOCUMENT.wrap(html, null, namespace()));
			} else {
				return sb.toString();
			}
		}
	}

	@Entity
	public static class Paragraph extends Content {
		@Transient
		private boolean isFirst = false;
		@Transient
		private boolean isFirstInClause = false;
		@Transient
		private boolean isLast = false;
		@Transient
		private boolean isOptional = false;
		@Transient
		private boolean isParent = false;
		@Transient
		private boolean isSubPara = false;
		@Transient
		private boolean isTable = false;

		@JsonBackReference("paragraphs")
		@ManyToOne(cascade = REFRESH, fetch = LAZY, targetEntity = Clause.class)
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

		public Clause getClause() {
			return clause;
		}

		public void setClause(final Clause clause) {
			this.clause = clause;
		}

		public String toHTML() {
			return toHTML(null);
		}

		static String getParagraphsHTML(final Clause clause) {
			if (isNotNullOrEmpty(clause) && !clause.getParagraphs().isEmpty()) {
				final StringBuilder sb = new StringBuilder();
				final Iterator<Paragraph> paragraphs = clause.getParagraphs().iterator();
				while (paragraphs.hasNext()) {
					sb.append(getParagraphsHTML(clause, paragraphs));
				}
				return sb.toString();
			}
			return null;
		}
		
		static String getParagraphsHTML(final Clause clause, final Iterator<Paragraph> iter) {
			final StringBuilder sb = new StringBuilder();
			while (iter.hasNext()) {
				boolean isOption = false;
				boolean isParent = false;
				boolean isLast = false;
				final Paragraph paragraph = iter.next();
				if (isNotNullOrEmpty(clause)) {
					final String add = paragraph.getCss();
					final String clauseCss = clause.getCss();
					if (isNotNullOrEmpty(clauseCss) && isNotNullOrEmpty(add) && !clauseCss.contains(add)) {
						clause.setCss(clauseCss.concat("\n\n").concat(add));
					} else if (!isNotNullOrEmpty(clauseCss) && isNotNullOrEmpty(add)) {
						clause.setCss(add);
					}				
				}
				final String options = paragraph.getFlags();
				if (isNotNullOrEmpty(options)) {
					isOption = options.contains(CONTENT_FLAG_OPTIONAL);
					isParent = options.contains(CONTENT_FLAG_PARENT);
					isLast = options.contains(CONTENT_FLAG_LAST);
				}
				if ((!isLast && isParent) && (!isOption || (isOption && true))) {
					sb.append(paragraph.toHTML(getParagraphsHTML(clause, iter)));
				} else if ((isLast && isParent) && (!isOption || (isOption && true))) {
					sb.append(paragraph.toHTML(getParagraphsHTML(clause, iter)));
					return sb.toString();
				} else if (isLast && (!isOption || (isOption && true))) {
					sb.append(paragraph.toHTML());
					return sb.toString();
				} else {
					sb.append(paragraph.toHTML());
				}
			}
			return sb.toString();
		}
		
		String toHTML(final boolean header) {
			return toHTML(null, null, header);
		}

		String toHTML(final String recursion) {
			return toHTML(recursion, null, false);
		}

		String toHTML(final String recursion, final boolean header) {
			return toHTML(recursion, null, header);
		}

		String toHTML(final String recursion, final String custom, boolean header) {
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
				final String head = HEAD.wrap(title.concat(STYLE.style(css.trim(), CSS_TYPE_TEXT)));
				final String body = BODY.wrap(sb.toString());
				return doctype().concat(DOCUMENT.wrap(String.format("%s%s", head, body), null, namespace()));
			} else {
				return sb.toString();
			}
		}

		private void setFlags() {
			if (isNotNullOrEmpty(flags)) {
				isFirst = flags.contains(CONTENT_FLAG_FIRST);
				isFirstInClause = flags.contains(CONTENT_FLAG_FIRST_IN_CLAUSE);
				isLast = flags.contains(CONTENT_FLAG_LAST);
				isOptional = flags.contains(CONTENT_FLAG_OPTIONAL);
				isParent = flags.contains(CONTENT_FLAG_PARENT);
				isSubPara = flags.contains(CONTENT_FLAG_SUB);
				isTable = contentCd.toLowerCase().contains(TABLE.tag().toLowerCase());
			}
		}
	}
}