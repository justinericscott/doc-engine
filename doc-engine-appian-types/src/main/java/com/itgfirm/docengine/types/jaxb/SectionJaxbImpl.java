package com.itgfirm.docengine.types.jaxb;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.itgfirm.docengine.types.AdvancedDocument;
import com.itgfirm.docengine.types.Clause;
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.Section;

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
public class SectionJaxbImpl extends ContentJaxbImpl implements Section {

	/** Parent Type **/
	@XmlElement( type = AdvancedDocumentJaxbImpl.class )
	private AdvancedDocument document;
	
	/** Child Type **/
	@XmlElement( type = ClauseJaxbImpl.class )
	private List<Clause> clauses;

	public SectionJaxbImpl() { }
	
	public SectionJaxbImpl(String body) {
		super(body);
	}

	public SectionJaxbImpl(String contentCd, String body) {
		this(body);
		this.setContentCd(contentCd);
	}

	public SectionJaxbImpl(Content content) { 
		super(content); 
	}
	
	public SectionJaxbImpl(Content content, String contentCd) {
		this(content);
		this.setContentCd(contentCd);
	}
	
	public SectionJaxbImpl(Section section) {
		super(section);
		this.document = section.getDocument();
	}

	public SectionJaxbImpl(Section section, String contentCd) {
		this(section);
		this.setContentCd(contentCd);
	}

	public AdvancedDocument getDocument() { return document; }
	public void setDocument(AdvancedDocument document) { this.document = document; }

	public List<Clause> getClauses() { return clauses; }
	public void addClause(Clause clause) {
		if (TypeUtils.isNotNullOrEmpty(clause)) {
			if (!TypeUtils.isNotNullOrEmpty(this.clauses)) {
				this.clauses = new ArrayList<Clause>();
			}
			clause.setSection(this);
			this.clauses.add(clause);			
		}
	}
	public void addClauses(List<Clause> clauses) {
		if (TypeUtils.isNotNullOrEmpty(clauses)) {
			if (!TypeUtils.isNotNullOrEmpty(this.clauses)) {
				this.clauses = new ArrayList<Clause>();
			}
			ListIterator<Clause> iter = clauses.listIterator();
			while (iter.hasNext()) {
				Clause clause = iter.next();
				clause.setSection(this);
				iter.set(clause);
			}			
			this.clauses.addAll(clauses);			
		}		
	}
	public void setClauses(List<Clause> clauses) { this.clauses = clauses; }
}