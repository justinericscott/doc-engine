package com.itgfirm.docengine.types.jpa;

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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.itgfirm.docengine.types.TokenDefinition;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Entity
@Table(name = TypeConstants.JPA_TABLE_TOKEN_DICTIONARY)
@DiscriminatorColumn(name = TypeConstants.JPA_DISCRIMINATOR_COLUMN,
		discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue(TypeConstants.JPA_DISCRIMINATOR_TOKEN)
public class TokenDefinitionJpaImpl extends AbstractModelJpaImpl implements TokenDefinition {
	private static final Logger LOG = LogManager.getLogger(TokenDefinitionJpaImpl.class);

	@Id
	@GeneratedValue(generator = "token_sq", strategy = GenerationType.AUTO)
	@SequenceGenerator(name = "token_sq", sequenceName = "CB_TOKEN_DICTIONARY_R_SQ",
			allocationSize = 1)
	@Column(name = TypeConstants.JPA_TABLE_TOKEN_DICTIONARY + "_ID")
	private Long id;

	/** TOKEN DETAILS **/
	@Column(name = "NAME_TXT", length = 100)
	private String name;
	@Column(name = "TOKEN_CD", length = 100, nullable = false, unique = true)
	private String tokenCd;
	@Column(name = "DOCUMENT_CD", length = 100)
	private String documentCd;
	@Column(name = "PHASE_CD", length = 100)
	private String phase;
	@Column(name = "DESCRIPTION_TXT", length = 1000)
	private String description;

	/** TOKEN CONFIG **/
	@Column(name = "ALT_TXT", length = 100)
	private String altText;
	@Column(name = "FLAGS_CD", length = 100)
	private String flags;
	@Column(name = "IS_X_ABLE_BLN")
	private Boolean isXable = false;
	@Column(name = "IS_ROMAN_BLN")
	private Boolean isRoman = false;

	/** DATABASE INFO **/
	@Column(name = "ENTITY_TXT", length = 100)
	private String entity;
	@Column(name = "ATTRIBUTE_TXT", length = 4000)
	private String attribute;
	@Column(name = "WHERE_TXT", length = 4000)
	private String where;

	/** INPUT INFORMATION **/
	@Column(name = "PROCESS_TXT", length = 100)
	private String process;
	@Column(name = "TASK_TXT", length = 100)
	private String task;
	@Column(name = "FIELD_LABEL_TXT", length = 100)
	private String label;
	@Column(name = "FIELD_INSTR_TXT", length = 1000)
	private String instructions;

	public TokenDefinitionJpaImpl() {}

	public TokenDefinitionJpaImpl(String code, String name) {
		this.tokenCd = code;
		this.name = name;
	}

	public TokenDefinitionJpaImpl(TokenDefinition token) {
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

	public TokenDefinitionJpaImpl(TokenDefinition token, String code) {
		this(token);
		this.tokenCd = code;
	}

	public Long getId() {
		return id;
	}

	// To disable unintended change of Primary Key
	@SuppressWarnings("unused")
	private void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTokenCd() {
		return tokenCd;
	}

	public void setTokenCd(String tokenCd) {
		this.tokenCd = tokenCd;
	}

	public String getDocumentCd() {
		return documentCd;
	}

	public void setDocumentCd(String documentCd) {
		this.documentCd = documentCd;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAltText() {
		return altText;
	}

	public void setAltText(String altText) {
		this.altText = altText;
	}

	public String getFlags() {
		return flags;
	}

	public void setFlags(String flags) {
		this.flags = flags;
	}

	public Boolean isXable() {
		return isXable;
	}

	public void setIsXable(Boolean isXable) {
		this.isXable = isXable;
	}

	public Boolean isRoman() {
		return isRoman;
	}

	public void setIsRoman(Boolean isRoman) {
		this.isRoman = isRoman;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public boolean isValid() {
		return isValid(false);
	}

	public boolean isValid(boolean checkForId) {
		if (checkForId) {
			if (!TypeUtils.isNotNullOrZero(id)) {
				LOG.trace("Invalid ID!");
				return false;
			}
		}
		if (!TypeUtils.isNotNullOrEmpty(tokenCd)) {
			LOG.trace("Invalid Token Definition Code!");
			return false;
		}
		if (!TypeUtils.isNotNullOrEmpty(name)) {
			LOG.trace("Invalid Token Definition Name!");
			return false;
		}
		if (!TypeUtils.isNotNullOrEmpty(entity)) {
			LOG.trace("Invalid Entity In Token Definition!");
			return false;
		}
		if (!TypeUtils.isNotNullOrEmpty(attribute)) {
			LOG.trace("Invalid Attribute In Token Definition!");
			return false;
		}
		if (!TypeUtils.isNotNullOrEmpty(where)) {
			LOG.trace("Invalid Where ClauseJpaImpl In Token Definition!");
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
	public int compareTo(TokenDefinition o) {
		return this.tokenCd.compareTo(o.getTokenCd());
	}

	@Override
	public String toString() {
		String result = "Code - %s: Name - %s: select %s from %s where %s";
		return String.format(result, tokenCd, name, attribute, entity, where);
	}
}