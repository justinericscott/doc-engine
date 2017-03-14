package com.github.justinericscott.docengine.models;

import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrZero;
import static javax.persistence.GenerationType.AUTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Entity
@Table(name = TokenDefinition.DB_TBL_TOKEN_DICTIONARY)
public class TokenDefinition extends AbstractJpaModel implements Comparable<TokenDefinition> {
	protected static final String DB_TBL_TOKEN_DICTIONARY = "TOKEN_DICTIONARY";
	private static final String DB_COL_ALT_TEXT = "ALT_TXT";
	private static final String DB_COL_ATTRIBUTE = "ATTR_TXT";
	private static final String DB_COL_DOCUMENT_CD = "DOCUMENT_CD";
	private static final String DB_COL_ENTITY = "ENTITY_TXT";
	private static final String DB_COL_FIELD_INSTR = "FIELD_INSTR_TXT";
	private static final String DB_COL_FIELD_LABEL = "FIELD_LABEL_TXT";
	private static final String DB_COL_IS_ROMAN = "IS_ROMAN_BLN";
	private static final String DB_COL_IS_X_ABLE = "IS_X_ABLE_BLN";
	private static final String DB_COL_PHASE_CD = "PHASE_CD";
	private static final String DB_COL_PROCESS = "PROCESS_TXT";
	private static final String DB_COL_TASK = "TASK_TXT";
	private static final String DB_COL_TOKEN_CD = "TOKEN_CD";
	private static final String DB_COL_TOKEN_ID = DB_TBL_TOKEN_DICTIONARY + "_ID";
	private static final String DB_COL_WHERE = "WHERE_TXT";
	private static final String DB_SEQ_TOKEN = DB_TBL_TOKEN_DICTIONARY + "_SQ";
	private static final Logger LOG = LoggerFactory.getLogger(TokenDefinition.class);

	@Id
	@GeneratedValue(generator = DB_SEQ_TOKEN, strategy = AUTO)
	@SequenceGenerator(name = DB_SEQ_TOKEN, sequenceName = DB_SEQ_TOKEN)
	@Column(name = DB_COL_TOKEN_ID)
	private Long id;
	@Column(name = DB_COL_NAME, length = 100, nullable = false)
	private String name;
	@Column(name = DB_COL_TOKEN_CD, length = 100, nullable = false, unique = true)
	private String tokenCd;
	@Column(name = DB_COL_DOCUMENT_CD, length = 100)
	private String documentCd;
	@Column(name = DB_COL_PHASE_CD, length = 100)
	private String phase;
	@Column(name = DB_COL_DESCRIPTION, length = 1000)
	private String description;
	@Column(name = DB_COL_ALT_TEXT, length = 100)
	private String altText;
	@Column(name = DB_COL_FLAGS, length = 100)
	private String flags;
	@Column(name = DB_COL_IS_X_ABLE)
	private Boolean isXable = false;
	@Column(name = DB_COL_IS_ROMAN)
	private Boolean isRoman = false;
	@Column(name = DB_COL_ENTITY, length = 100)
	private String entity;
	@Column(name = DB_COL_ATTRIBUTE, length = 4000)
	private String attribute;
	@Column(name = DB_COL_WHERE, length = 4000)
	private String where;
	@Column(name = DB_COL_PROCESS, length = 100)
	private String process;
	@Column(name = DB_COL_TASK, length = 100)
	private String task;
	@Column(name = DB_COL_FIELD_LABEL, length = 100)
	private String label;
	@Column(name = DB_COL_FIELD_INSTR, length = 1000)
	private String instructions;

	public TokenDefinition() {
		// Default constructor for Spring
	}

	public TokenDefinition(final String code, final String name) {
		if (isNotNullOrEmpty(code)) {
			this.tokenCd = code;
			if (isNotNullOrEmpty(name)) {
				this.name = name;	
			}
		}
	}

	public TokenDefinition(final TokenDefinition token) {
		if (isNotNullOrEmpty(token)) {
//			this.id = token.getId();
			this.name = token.getName();
//			this.tokenCd = token.getTokenCd();
			this.documentCd = token.getDocumentCd();
			this.phase = token.getPhase();
			this.description = token.getDescription();
			this.altText = token.getAltText();
			this.flags = token.getFlags();
			this.isXable = token.isXable();
			this.isRoman = token.isRoman();
			this.entity = token.getEntity();
			this.attribute = token.getAttribute();
			this.where = token.getWhere();
			this.process = token.getProcess();
			this.task = token.getTask();
			this.label = token.getLabel();
			this.instructions = token.getInstructions();
		}
	}

	public TokenDefinition(final TokenDefinition token, final String code) {
		this(token);
		if (!token.getTokenCd().equals(code)) {
			this.tokenCd = code;	
		}		
	}

	public final Long getId() {
		return id;
	}

	public final void setId(final Long id) {
		this.id = id;
	}

	public final String getName() {
		return name;
	}

	public final void setName(final String name) {
		this.name = name;
	}

	public final String getTokenCd() {
		return tokenCd;
	}

	public final void setTokenCd(final String tokenCd) {
		this.tokenCd = tokenCd;
	}

	public final String getDocumentCd() {
		return documentCd;
	}

	public final void setDocumentCd(final String documentCd) {
		this.documentCd = documentCd;
	}

	public final String getPhase() {
		return phase;
	}

	public final void setPhase(final String phase) {
		this.phase = phase;
	}

	public final String getDescription() {
		return description;
	}

	public final void setDescription(final String description) {
		this.description = description;
	}

	public final String getAltText() {
		return altText;
	}

	public final void setAltText(final String altText) {
		this.altText = altText;
	}

	public final String getFlags() {
		return flags;
	}

	public final void setFlags(final String flags) {
		this.flags = flags;
	}

	public final Boolean isXable() {
		return isXable;
	}

	public final void setIsXable(final Boolean isXable) {
		this.isXable = isXable;
	}

	public final Boolean isRoman() {
		return isRoman;
	}

	public final void setIsRoman(final Boolean isRoman) {
		this.isRoman = isRoman;
	}

	public final String getEntity() {
		return entity;
	}

	public final void setEntity(final String entity) {
		this.entity = entity;
	}

	public final String getAttribute() {
		return attribute;
	}

	public final void setAttribute(final String attribute) {
		this.attribute = attribute;
	}

	public final String getWhere() {
		return where;
	}

	public final void setWhere(final String where) {
		this.where = where;
	}

	public final String getProcess() {
		return process;
	}

	public final void setProcess(final String process) {
		this.process = process;
	}

	public final String getTask() {
		return task;
	}

	public final void setTask(final String task) {
		this.task = task;
	}

	public final String getLabel() {
		return label;
	}

	public final void setLabel(final String label) {
		this.label = label;
	}

	public final String getInstructions() {
		return instructions;
	}

	public final void setInstructions(final String instructions) {
		this.instructions = instructions;
	}

	public final boolean isValid() {
		return isValid(false);
	}

	public final boolean isValid(final boolean checkForId) {
		if (checkForId && !isNotNullOrZero(id)) {
			LOG.warn("ID must not be null or zero!");
			return false;
		}
		if (!isNotNullOrEmpty(tokenCd)) {
			LOG.warn("Token code must not be null or empty!");
			return false;
		}
		if (!isNotNullOrEmpty(name)) {
			LOG.warn("Token name must not be null or empty!");
			return false;
		}
		if (!isNotNullOrEmpty(entity)) {
			LOG.warn("Target entity must not be null or empty!");
			return false;
		}
		if (!isNotNullOrEmpty(attribute)) {
			LOG.warn("Target attribute must not be null or empty!");
			return false;
		}
		if (!isNotNullOrEmpty(where)) {
			LOG.warn("Target where clause must not be null or empty!");
			return false;
		}
		return true;
	}

	@Override
	public final int compareTo(final TokenDefinition o) {
		return this.getTokenCd().compareTo(o.getTokenCd());
	}
	
	@Override
	public String toHTML() {		
		return toString();
	}

	@Override
	public final String toString() {
		final String result = "Code - %s: Name - %s: select %s from %s where %s";
		return String.format(result, tokenCd, name, attribute, entity, where);
	}
}