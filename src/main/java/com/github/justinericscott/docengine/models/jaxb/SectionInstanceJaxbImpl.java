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
 * SectionJaxbImpl InstanceJaxbImpl Data Model
 */
@XmlRootElement( name=TypeConstants.JAXB_ROOT_ELEMENT_NAME_SECTION_INSTANCE,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlType( name=TypeConstants.JAXB_TYPE_NAME_SECTION_INSTANCE,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlAccessorType( XmlAccessType.FIELD )
public class SectionInstanceJaxbImpl extends InstanceJaxbImpl {

	/** Parent Type **/
	@XmlElement( type = AdvancedDocumentInstanceJaxbImpl.class )
	private AdvancedDocumentInstanceJaxbImpl document;
	
	/** Child Type **/
	@XmlElement( type = ClauseInstanceJaxbImpl.class )
	private List<ClauseInstanceJaxbImpl> clauses;

	public SectionInstanceJaxbImpl() { }
	
	public SectionInstanceJaxbImpl(SectionJaxbImpl section, String projectId) {
		super(section);
		this.setProjectId(projectId);
		this.setContent(section);
		List<ClauseJaxbImpl> clauses = section.getClauses();
		if (TypeUtils.isNotNullOrEmpty(clauses)) {
			instantiateClauses(section.getClauses());
			section.getClauses().clear();			
		}
	}
	
	public SectionInstanceJaxbImpl(SectionJaxbImpl section, String projectId, String body) {
		this(section, projectId);
		this.setBody(body);
	}
	
	public SectionInstanceJaxbImpl(SectionJaxbImpl section, String projectId, String body, 
			boolean isAdHoc) {
		this(section, projectId, body);
		this.setAdHoc(isAdHoc);
	}

	public AdvancedDocumentInstanceJaxbImpl getDocument() { return document; }
	public void setDocument(AdvancedDocumentInstanceJaxbImpl document) { this.document = document; }

	public List<ClauseInstanceJaxbImpl> getClauses() { return clauses; }
	public void instantiateClauses(List<ClauseJaxbImpl> clauses) {
		if (TypeUtils.isNotNullOrEmpty(clauses)) {
			for (ClauseJaxbImpl c : clauses) {
				this.addClause(new ClauseInstanceJaxbImpl(c, this.getProjectId()));
			}
		}
	}
	public void addClause(ClauseInstanceJaxbImpl clause) {
		if (TypeUtils.isNotNullOrEmpty(clause)) {
			if (!TypeUtils.isNotNullOrEmpty(this.clauses)) {
				this.clauses = new ArrayList<ClauseInstanceJaxbImpl>();
			}
			clause.setSection(this);
			this.clauses.add(clause);
		}
	}
	public void addClauses(List<ClauseInstanceJaxbImpl> clauses) {
		if (TypeUtils.isNotNullOrEmpty(clauses)) {
			if (!TypeUtils.isNotNullOrEmpty(this.clauses)) {
				this.clauses = new ArrayList<ClauseInstanceJaxbImpl>();
			}
	 		ListIterator<ClauseInstanceJaxbImpl> iter = clauses.listIterator();
	 		while (iter.hasNext()) {
	 			ClauseInstanceJaxbImpl clause = iter.next();
	 			clause.setSection(this);
	 			iter.set(clause);
	 		}
			this.clauses.addAll(clauses);
		}		
	}
	public void setClauses(List<ClauseInstanceJaxbImpl> clauses) { this.clauses = clauses; }
}