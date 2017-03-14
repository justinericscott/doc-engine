package com.github.justinericscott.docengine.models.jaxb;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Justin Scott
 * 
 * SectionJaxbImpl Data Model
 */
@XmlRootElement( name=TypeConstants.JAXB_ROOT_ELEMENT_NAME_SECTION,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlType( name=TypeConstants.JAXB_TYPE_NAME_SECTION,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlAccessorType( XmlAccessType.FIELD )
public class SectionJaxbImpl extends ContentJaxbImpl {

	/** Parent Type **/
	@XmlElement( type = AdvancedDocumentJaxbImpl.class )
	private AdvancedDocumentJaxbImpl document;
	
	/** Child Type **/
	@XmlElement( type = ClauseJaxbImpl.class )
	private List<ClauseJaxbImpl> clauses;

	public SectionJaxbImpl() { }
	
	public SectionJaxbImpl(String body) {
		super(body);
	}

	public SectionJaxbImpl(String contentCd, String body) {
		this(body);
		this.setContentCd(contentCd);
	}

	public SectionJaxbImpl(ContentJaxbImpl content) { 
		super(content); 
	}
	
	public SectionJaxbImpl(ContentJaxbImpl content, String contentCd) {
		this(content);
		this.setContentCd(contentCd);
	}
	
	public SectionJaxbImpl(SectionJaxbImpl section) {
		super(section);
		this.document = section.getDocument();
	}

	public SectionJaxbImpl(SectionJaxbImpl section, String contentCd) {
		this(section);
		this.setContentCd(contentCd);
	}

	public AdvancedDocumentJaxbImpl getDocument() { return document; }
	public void setDocument(AdvancedDocumentJaxbImpl document) { this.document = document; }

	public List<ClauseJaxbImpl> getClauses() { return clauses; }
	public void addClause(ClauseJaxbImpl clause) {
		if (TypeUtils.isNotNullOrEmpty(clause)) {
			if (!TypeUtils.isNotNullOrEmpty(this.clauses)) {
				this.clauses = new ArrayList<ClauseJaxbImpl>();
			}
			clause.setSection(this);
			this.clauses.add(clause);			
		}
	}
	public void addClauses(List<ClauseJaxbImpl> clauses) {
		if (TypeUtils.isNotNullOrEmpty(clauses)) {
			if (!TypeUtils.isNotNullOrEmpty(this.clauses)) {
				this.clauses = new ArrayList<ClauseJaxbImpl>();
			}
			ListIterator<ClauseJaxbImpl> iter = clauses.listIterator();
			while (iter.hasNext()) {
				ClauseJaxbImpl clause = iter.next();
				clause.setSection(this);
				iter.set(clause);
			}			
			this.clauses.addAll(clauses);			
		}		
	}
	public void setClauses(List<ClauseJaxbImpl> clauses) { this.clauses = clauses; }
}