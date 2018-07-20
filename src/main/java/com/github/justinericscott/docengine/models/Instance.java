package com.github.justinericscott.docengine.models;

import static com.github.justinericscott.docengine.util.Utils.*;
import static com.github.justinericscott.docengine.util.Utils.HTML.P;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.AUTO;

import java.util.Arrays;
import java.util.Collection;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import com.github.justinericscott.docengine.models.Content.Clause;
import com.github.justinericscott.docengine.models.Content.Document;
import com.github.justinericscott.docengine.models.Content.Paragraph;
import com.github.justinericscott.docengine.models.Content.Section;

@Entity
@Table(name = Instance.DB_TBL_INSTANCE)
public class Instance extends AbstractJpaModel implements Comparable<Instance> {
	protected static final String STATUS_AUTO_IN = "Automatically Included";
	protected static final String STATUS_AUTO_OUT = "Automatically Excluded";
	protected static final String STATUS_MAN_IN = "Manually Included";
	protected static final String STATUS_MAN_OUT = "Manually Excluded";
	protected static final String STATUS_PENDING = "Pending Review";
	protected static final String[] STATUSES_IN = { STATUS_AUTO_IN, STATUS_MAN_IN };
	protected static final String[] STATUSES_OUT = { STATUS_AUTO_OUT, STATUS_MAN_OUT, STATUS_PENDING };
	protected static final String DB_TBL_INSTANCE = "INSTANCE";
	private static final String DB_SEQ_INSTANCE = DB_TBL_INSTANCE + "_SQ";
	private static final String DB_COL_CUSTOM_BODY = "CUSTOM_BODY_TXT";
	private static final String DB_COL_CUSTOM_CONTENT_NBR = "CUSTOM_CONTENT_NBR";
	private static final String DB_COL_INSTANCE_ID = DB_TBL_INSTANCE + "_ID";
	private static final String DB_COL_IS_AD_HOC = "IS_AD_HOC_BLN";
	private static final String DB_COL_IS_MARKED_FOR_ACTION = "IS_MARKED_FOR_ACTION_BLN";
	private static final String DB_COL_IS_STRIKE_HEADER = "IS_STRIKE_HEADER_BLN";
	private static final String DB_COL_MARKED_COMMENT = "MARKED_FOR_ACTION_COMMENT_TXT";
	private static final String DB_COL_PROJECT = "PROJECT_NBR";
	private static final String DB_COL_STATUS = "STATUS_CD";
	private static final Logger LOG = LoggerFactory.getLogger(Instance.class);

	@Column(name = DB_COL_INSTANCE_ID, unique = true)
	@GeneratedValue(strategy = AUTO, generator = DB_SEQ_INSTANCE)
	@Id
	@SequenceGenerator(name = DB_SEQ_INSTANCE, sequenceName = DB_SEQ_INSTANCE)
	protected Long id;
	@Column(name = DB_COL_PROJECT, length = 100, nullable = false)
	protected String projectId;
	@Column(name = DB_COL_CUSTOM_BODY, length = 4000)
	protected String customBody;
	@Column(name = DB_COL_CUSTOM_CONTENT_NBR, length = 10)
	protected String customNumber;
	@Column(name = DB_COL_DISCRIMINATOR, updatable = false, insertable = false)
	private String discriminator;
	@Column(name = DB_COL_FLAGS, length = 100)
	protected String flags;
	@Column(name = DB_COL_STATUS, length = 100)
	protected String statusCd;
	@Column(name = DB_COL_ORDER)
	protected Integer orderBy;
	@Column(name = DB_COL_IS_AD_HOC)
	protected boolean isAdHoc = false;
	@Column(name = DB_COL_IS_STRIKE_HEADER)
	protected boolean isStrikeHeader = false;
	@Column(name = DB_COL_IS_MARKED_FOR_ACTION)
	protected boolean isMarkedForAction = false;
	@Column(name = DB_COL_MARKED_COMMENT)
	protected String markedForActionComment;

	@OrderColumn(name = DB_COL_ORDER)
	@ManyToOne(targetEntity = Content.class, cascade = REFRESH, optional = false)
	protected Content content;

	public Instance() {
		// Default constructor for Spring
		this.statusCd = STATUS_AUTO_IN;
	}

	public Instance(final String projectId) {
		this();
		this.projectId = projectId;
	}

	public Instance(final Content content) {
		this();
		this.content = content;
		this.orderBy = content.getOrderBy();
	}

	public Instance(final Content content, final String projectId) {
		this(content);
		this.projectId = projectId;
	}

	public Instance(final Content content, final String projectId, final String body) {
		this(content, projectId);
		this.customBody = body;
	}

	public Instance(final Content content, final String projectId, final String body,
			final boolean isAdHoc) {
		this(content, projectId, body);
		this.isAdHoc = isAdHoc;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(final String projectId) {
		this.projectId = projectId;
	}

	public String getBody() {
		if (isNotNullOrEmpty(customBody)) {
			return customBody;
		} else {
			if (isNotNullOrEmpty(content)) {
				return content.getBody();
			}
		}
		return "";
	}

	public void setBody(final String customBody) {
		if (isNotNullOrEmpty(customBody)) {
			if (!content.getBody().equals(customBody)) {
				this.customBody = customBody;
			}
		} else {
			this.customBody = null;
		}
	}

	public String getCustomBody() {
		return customBody;
	}

	public void setCustomBody(final String customBody) {
		this.customBody = customBody;
	}

	public String getCustomNumber() {
		return customNumber;
	}

	public void setCustomNumber(final String customNumber) {
		this.customNumber = customNumber;
	}

	public String getDiscriminator() {
		return discriminator;
	}

	void setDiscriminator(String discriminator) {
		this.discriminator = discriminator;
	}

	public String getFlags() {
		return flags;
	}

	public void setFlags(final String flags) {
		this.flags = flags;
	}

	public String getStatusCd() {
		return statusCd;
	}

	public void setStatusCd(final String statusCd) {
		this.statusCd = statusCd;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(final Integer orderBy) {
		this.orderBy = orderBy;
	}

	public boolean isAdHoc() {
		return isAdHoc;
	}

	public void setAdHoc(final boolean isAdHoc) {
		this.isAdHoc = isAdHoc;
	}

	public boolean isStrikeHeader() {
		return isStrikeHeader;
	}

	public void setStrikeHeader(final boolean isStrikeHeader) {
		this.isStrikeHeader = isStrikeHeader;
	}

	public boolean isMarkedForAction() {
		return isMarkedForAction;
	}

	public void setMarkedForAction(final boolean isMarkedForAction) {
		this.isMarkedForAction = isMarkedForAction;
	}

	public String getMarkedForActionComment() {
		return markedForActionComment;
	}

	public void setMarkedForActionComment(final String markedForActionComment) {
		this.markedForActionComment = markedForActionComment;
	}

	public Content getContent() {
		return content;
	}

	public void setContent(final Content content) {
		this.content = content;
	}

	public boolean isValid() {
		return isValid(false);
	}

	public boolean isValid(final boolean checkForId) {
		if (checkForId && !isNotNullOrZero(id)) {
			LOG.warn("ID must not be null or zero!");
			return false;
		}
		if (!isNotNullOrEmpty(projectId)) {
			LOG.warn("Project ID/Number must not be null or empty!");
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(final Instance o) {
		return this.getContent().getContentCd().compareTo(o.getContent().getContentCd());
	}
	
//	@Override
	public String toHTML() {		
		return getBody();
	}

	@Entity
	public static class DocumentInstance extends Instance {

		@JsonManagedReference("section-instance")
		@OrderColumn(name = DB_COL_ORDER)
		@OneToMany(targetEntity = SectionInstance.class, cascade = ALL, mappedBy = JPA_MAPPED_BY_DOCUMENT)
		private Collection<SectionInstance> sections = new TreeSet<SectionInstance>();
		
		public DocumentInstance() {
			// Default constructor for Spring
		}

		public DocumentInstance(final Document document, final String projectId) {
			this.projectId = projectId;
			if (isNotNullOrEmpty(document)) {
				super.setContent(document);
				final Collection<Section> sections = document.getSections();
				if (isNotNullOrEmpty(sections)) {
					instantiateSections(sections);
					sections.clear();
				}
			}
		}

		public Document getDocument() {
			return (Document) content.getClass().cast(content);
		}

		public void setDocument(final Document document) {
			super.setContent(document);
		}

		public void addSection(final SectionInstance section) {
			if (isNotNullOrEmpty(section)) {
				section.setDocument(this);
				sections.add(section);
			}
		}

		public Collection<SectionInstance> getSections() {
			return sections;
		}

		public void setSections(final Collection<SectionInstance> sections) {
			if (isNotNullOrEmpty(sections)) {
				this.sections = sections;
			}
		}
		
		@Override
		public String toHTML() {
			final StringBuilder sb = new StringBuilder();
			for (final SectionInstance section : getSections()) {
				sb.append(section.toHTML());
			}
			return sb.toString();
		}

		void instantiateSections(final Collection<Section> sections) {
			if (isNotNullOrEmpty(sections)) {
				for (final Section section : sections) {
					this.addSection(new SectionInstance(section, this.getProjectId()));
				}
			}
		}	
	}

	@Entity
	public static class SectionInstance extends Instance {

		@JsonBackReference("section-instance")
		@ManyToOne(targetEntity = DocumentInstance.class, fetch = LAZY, cascade = REFRESH)
		private DocumentInstance document;

		@JsonManagedReference("clause-instance")
		@OrderColumn(name = DB_COL_ORDER)
		@OneToMany(targetEntity = ClauseInstance.class, cascade = ALL, mappedBy = JPA_MAPPED_BY_SECTION)
		private Collection<ClauseInstance> clauses = new TreeSet<ClauseInstance>();

		public SectionInstance() {
			// Default constructor for Spring
		}

		public SectionInstance(final String projectId) {
			super(projectId);
		}

		public SectionInstance(final Section section, final String projectId) {
			this(projectId);
			super.setContent(section);
			final Collection<Clause> clauses = section.getClauses();
			if (isNotNullOrEmpty(clauses)) {
				instantiateClauses(clauses);
				clauses.clear();
			}
		}

		public Section getSection() {
			return (Section) content.getClass().cast(content);
		}

		public void setSection(final Section section) {
			super.setContent(section);
		}

		public DocumentInstance getDocument() {
			return document;
		}

		public void setDocument(final DocumentInstance documentInstance) {
			this.document = documentInstance;
		}

		public void addClause(final ClauseInstance clause) {
			if (isNotNullOrEmpty(clause)) {
				clause.setSection(this);
				clauses.add(clause);
			}
		}

		public Collection<ClauseInstance> getClauses() {
			return clauses;
		}

		public void setClauses(final Collection<ClauseInstance> clauses) {
			if (isNotNullOrEmpty(clauses)) {
				this.clauses = clauses;
			}
		}

		void instantiateClauses(final Collection<Clause> clauses) {
			if (isNotNullOrEmpty(clauses)) {
				for (final Clause clause : clauses) {
					this.addClause(new ClauseInstance(clause, this.getProjectId()));
				}
			}
		}

		@Override
		public String toHTML() {
			final StringBuilder sb = new StringBuilder();
			if (Arrays.asList(STATUSES_IN).contains(statusCd)) {
				sb.append(getSection().toHTML(customBody, customNumber));
				for (final ClauseInstance clause : getClauses()) {
					sb.append(clause.toHTML());
				}			
			}
			return sb.toString();
		}
	}

	@Entity
	public static class ClauseInstance extends Instance {

		/** Parent Type **/
		@JsonBackReference("clause-instance")
		@ManyToOne(targetEntity = SectionInstance.class, fetch = LAZY, cascade = REFRESH)
		private SectionInstance section;

		/** Child Type **/
		@JsonManagedReference("paragraph-instance")
		@OrderColumn(name = DB_COL_ORDER)
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

		public void setClause(final Clause clause) {
			super.setContent(clause);
		}

		public SectionInstance getSection() {
			return section;
		}

		public void setSection(final SectionInstance sectionInstance) {
			this.section = sectionInstance;
		}

		public void addParagraph(final ParagraphInstance paragraph) {
			if (isNotNullOrEmpty(paragraph)) {
				paragraph.setClause(this);
				this.paragraphs.add(paragraph);
			}
		}

		public Collection<ParagraphInstance> getParagraphs() {
			return paragraphs;
		}

		public void setParagraphs(final Collection<ParagraphInstance> paragraphs) {
			if (isNotNullOrEmpty(paragraphs)) {
				this.paragraphs = paragraphs;
			}
		}

		void instantiateParagraphs(final Collection<Paragraph> paragraphs) {
			if (isNotNullOrEmpty(paragraphs)) {
				for (final Paragraph paragraph : paragraphs) {
					this.addParagraph(new ParagraphInstance(paragraph, this.getProjectId()));
				}
			}
		}

		@Override
		public String toHTML() {
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
					if (current.getParagraph().getFlags().contains(CONTENT_FLAG_PARENT)) {
						return current.toHTML(getParagraphsHTML(iter), isIncluded);					
					} else {
						return current.toHTML();
					}
				}			
			}
			return "";
		}
	}

	@Entity
	public static class ParagraphInstance extends Instance {

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

		public Paragraph getParagraph() {
			return (Paragraph) content.getClass().cast(content);
		}

		public void setParagraph(final Paragraph paragraph) {
			super.setContent(paragraph);
		}

		public ClauseInstance getClause() {
			return clause;
		}

		public void setClause(final ClauseInstance clauseInstance) {
			this.clause = clauseInstance;
		}

		@Override
		public String toHTML() {
			return toHTML(null);
		}

		String toHTML(final String recursion) {
			return toHTML(recursion, false);
		}

		String toHTML(final String recursion, final boolean isParentIncluded) {
			boolean isIncluded = Arrays.asList(STATUSES_IN).contains(statusCd);
			if (isIncluded) {
				return getParagraph().toHTML(recursion, customBody, false);
			} else if (!isIncluded && !getParagraph().getFlags().contains(CONTENT_FLAG_OPTIONAL) && isParentIncluded) {
				return P.wrap("INTENTIONALLY DELETED");
			}
			return "";
		}
	}
}