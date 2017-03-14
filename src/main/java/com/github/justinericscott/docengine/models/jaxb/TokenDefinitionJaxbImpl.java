/**TODO: License
 */
package com.github.justinericscott.docengine.models.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Justin Scott
 * TODO: Description
 */
@XmlRootElement( name=TypeConstants.JAXB_ROOT_ELEMENT_NAME_TOKEN_DEF,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlType( name=TypeConstants.JAXB_TYPE_NAME_TOKEN_DEF,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlAccessorType( XmlAccessType.FIELD )
public class TokenDefinitionJaxbImpl extends AbstractModelJaxbImpl implements Comparable<TokenDefinitionJaxbImpl> {
	private static final Logger LOG = LoggerFactory.getLogger(TokenDefinitionJaxbImpl.class);
	
	@XmlElement
	private Long id;
	
	/** TOKEN DETAILS **/
	@XmlElement
	private String tokenCd;
	@XmlElement
	private String name;
	@XmlElement
	private String description;
	
	/** TOKEN CONFIG **/
	@XmlElement
	private String altText;
	@XmlElement
	private String flags;
	@XmlElement
	private Boolean isXable = false;
	@XmlElement
	private Boolean isRoman = false;
	
	/** DATABASE RETRIEVAL **/
	@XmlElement
	private String entity;
	@XmlElement
	private String attribute;
	@XmlElement
	private String where;
	
	/** INPUT INFORMATION **/
	@XmlElement
	private String process;
	@XmlElement
	private String task;
	@XmlElement
	private String label;
	@XmlElement
	private String instructions;
	
	/**
	 * TODO: Description
	 */
	public TokenDefinitionJaxbImpl() { }
	
	public TokenDefinitionJaxbImpl(String code) {
		this.tokenCd = code;
	}
	
	public TokenDefinitionJaxbImpl(String code, String name) {
		this(code);
		this.name = name;
	}
	
	public TokenDefinitionJaxbImpl(TokenDefinitionJaxbImpl token) {
		this.name = token.name;
		this.description = token.description;
		this.altText = token.altText;
		this.isXable = token.isXable;
		this.isRoman = token.isRoman;
		this.entity = token.entity;
		this.attribute = token.attribute;
		this.where = token.where;
		this.process = token.process;
		this.task = token.task;
		this.label = token.label;
		this.instructions = token.instructions;
	}
	
	public TokenDefinitionJaxbImpl(TokenDefinitionJaxbImpl token, String code) {
		this(token);
		this.tokenCd = code;
	}
	
	public Long getId() { return id; }
	//To disable unintended change of Primary Key
	@SuppressWarnings("unused")
	private void setId(Long id) { this.id = id; }

	public String getTokenCd() { return tokenCd; }
	public void setTokenCd(String tokenCd) { this.tokenCd = tokenCd; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	public String getAltText() { return altText; }
	public void setAltText(String altText) { this.altText = altText; }

	public String getFlags() { return flags; }
	public void setFlags(String flags) { this.flags = flags; }

	public Boolean getIsXable() { return isXable; }
	public void setIsXable(Boolean isXable) { this.isXable = isXable; }

	public Boolean getIsRoman() { return isRoman; }
	public void setIsRoman(Boolean isRoman) { this.isRoman = isRoman; }

	public String getEntity() { return entity; }
	public void setEntity(String entity) { this.entity = entity; }

	public String getAttribute() { return attribute; }
	public void setAttribute(String attribute) { this.attribute = attribute; }

	public String getWhere() { return where; }
	public void setWhere(String where) { this.where = where; }

	public String getProcess() { return process; }
	public void setProcess(String process) { this.process = process; }

	public String getTask() { return task; }
	public void setTask(String task) { this.task = task; }

	public String getLabel() { return label; }
	public void setLabel(String label) { this.label = label; }

	public String getInstructions() { return instructions; }
	public void setInstructions(String instructions) { this.instructions = instructions; }
	
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
			LOG.trace("Invalid Where ClauseJaxbImpl In Token Definition!");
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
	public int compareTo(TokenDefinitionJaxbImpl o) {
		return this.tokenCd.compareTo(o.getTokenCd());
	}
}