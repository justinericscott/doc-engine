package com.itgfirm.docengine.types.jpa;

import static com.itgfirm.docengine.types.jpa.TypeUtils.*;
import static com.itgfirm.docengine.types.jpa.TypeConstants.*;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = JPA_TBL_TOKEN_DICTIONARY)
@DiscriminatorColumn(name = JPA_DSCRMNTR_COL, discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(JPA_DSCRMNTR_TOKEN)
public class TokenDefinitionJpaImpl extends AbstractJpaModel implements Comparable<TokenDefinitionJpaImpl> {
	private static final Logger LOG = LoggerFactory.getLogger(TokenDefinitionJpaImpl.class);
	private static final String JPA_COLUMN_ALT_TEXT = "ALT_TXT";
	private static final String JPA_COLUMN_ATTRIBUTE = "ATTRIBUTE_TXT";
	private static final String JPA_COLUMN_DESCRIPTION = "DESCRIPTION_TXT";
	private static final String JPA_COLUMN_DOCUMENT_CD = "DOCUMENT_CD";
	private static final String JPA_COLUMN_ENTITY = "ENTITY_TXT";
	private static final String JPA_COLUMN_FIELD_LABEL = "FIELD_LABEL_TXT";
	private static final String JPA_COLUMN_FIELD_INSTR = "FIELD_INSTR_TXT";
	private static final String JPA_COLUMN_FLAGS = "FLAGS_CD";
	private static final String JPA_COLUMN_IS_ROMAN = "IS_ROMAN_BLN";
	private static final String JPA_COLUMN_IS_X_ABLE = "IS_X_ABLE_BLN";
	private static final String JPA_COLUMN_NAME = "NAME_TXT";
	private static final String JPA_COLUMN_PHASE_CD = "PHASE_CD";
	private static final String JPA_COLUMN_TOKEN_CD = "TOKEN_CD";
	private static final String JPA_COLUMN_PROCESS = "PROCESS_TXT";
	private static final String JPA_COLUMN_TASK = "TASK_TXT";
	private static final String JPA_COLUMN_TOKEN_ID = JPA_TBL_TOKEN_DICTIONARY + "_ID";
	private static final String JPA_COLUMN_WHERE = "WHERE_TXT";
	private static final String JPA_SEQUENCE_TOKEN = JPA_TBL_TOKEN_DICTIONARY + "_SQ";

	@Id
	@GeneratedValue(generator = JPA_SEQUENCE_TOKEN, strategy = GenerationType.AUTO)
	@SequenceGenerator(name = JPA_SEQUENCE_TOKEN, sequenceName = JPA_SEQUENCE_TOKEN)
	@Column(name = JPA_COLUMN_TOKEN_ID)
	private Long id;

	/** TOKEN DETAILS **/
	@Column(name = JPA_COLUMN_NAME, length = 100)
	private String name;
	@Column(name = JPA_COLUMN_TOKEN_CD, length = 100, nullable = false, unique = true)
	private String tokenCd;
	@Column(name = JPA_COLUMN_DOCUMENT_CD, length = 100)
	private String documentCd;
	@Column(name = JPA_COLUMN_PHASE_CD, length = 100)
	private String phase;
	@Column(name = JPA_COLUMN_DESCRIPTION, length = 1000)
	private String description;

	/** TOKEN CONFIG **/
	@Column(name = JPA_COLUMN_ALT_TEXT, length = 100)
	private String altText;
	@Column(name = JPA_COLUMN_FLAGS, length = 100)
	private String flags;
	@Column(name = JPA_COLUMN_IS_X_ABLE)
	private Boolean isXable = false;
	@Column(name = JPA_COLUMN_IS_ROMAN)
	private Boolean isRoman = false;

	/** DATABASE INFO **/
	@Column(name = JPA_COLUMN_ENTITY, length = 100)
	private String entity;
	@Column(name = JPA_COLUMN_ATTRIBUTE, length = 4000)
	private String attribute;
	@Column(name = JPA_COLUMN_WHERE, length = 4000)
	private String where;

	/** INPUT INFORMATION **/
	@Column(name = JPA_COLUMN_PROCESS, length = 100)
	private String process;
	@Column(name = JPA_COLUMN_TASK, length = 100)
	private String task;
	@Column(name = JPA_COLUMN_FIELD_LABEL, length = 100)
	private String label;
	@Column(name = JPA_COLUMN_FIELD_INSTR, length = 1000)
	private String instructions;

	public TokenDefinitionJpaImpl() {
		// Default constructor for Spring
	}

	public TokenDefinitionJpaImpl(final String code, final String name) {
		this.tokenCd = code;
		this.name = name;
	}

	public TokenDefinitionJpaImpl(final TokenDefinitionJpaImpl token) {
		if (isNotNullOrEmpty(token)) {
			this.name = token.getName();
			this.tokenCd = token.getTokenCd();
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

	public TokenDefinitionJpaImpl(final TokenDefinitionJpaImpl token, final String code) {
		this(token);
		this.tokenCd = code;
	}

	public final Long getId() {
		return id;
	}

	// To disable unintended change of Primary Key
	final void setId(final Long id) {
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
			LOG.debug("ID must not be null or zero!");
			return false;
		}
		if (!isNotNullOrEmpty(tokenCd)) {
			LOG.debug("Token code must not be null or empty!");
			return false;
		}
		if (!isNotNullOrEmpty(name)) {
			LOG.debug("Token name must not be null or empty!");
			return false;
		}
		if (!isNotNullOrEmpty(entity)) {
			LOG.debug("Target entity must not be null or empty!");
			return false;
		}
		if (!isNotNullOrEmpty(attribute)) {
			LOG.debug("Target attribute must not be null or empty!");
			return false;
		}
		if (!isNotNullOrEmpty(where)) {
			LOG.debug("Target where clause must not be null or empty!");
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public final int compareTo(final TokenDefinitionJpaImpl o) {
		return this.getTokenCd().compareTo(o.getTokenCd());
	}

	@Override
	public final String toString() {
		final String result = "Code - %s: Name - %s: select %s from %s where %s";
		return String.format(result, tokenCd, name, attribute, entity, where);
	}
}