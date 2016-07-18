package com.itgfirm.docengine.types.jaxb;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.itgfirm.docengine.types.AdvancedDocumentInstance;
import com.itgfirm.docengine.types.Clause;
import com.itgfirm.docengine.types.ClauseInstance;
import com.itgfirm.docengine.types.Section;
import com.itgfirm.docengine.types.SectionInstance;

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
public class SectionInstanceJaxbImpl extends InstanceJaxbImpl implements SectionInstance {

	/** Parent Type **/
	@XmlElement( type = AdvancedDocumentInstanceJaxbImpl.class )
	private AdvancedDocumentInstance document;
	
	/** Child Type **/
	@XmlElement( type = ClauseInstanceJaxbImpl.class )
	private List<ClauseInstance> clauses;

	public SectionInstanceJaxbImpl() { }
	
	public SectionInstanceJaxbImpl(Section section, String projectId) {
		super(section);
		this.setProjectId(projectId);
		this.setContent(section);
		List<Clause> clauses = section.getClauses();
		if (TypeUtils.isNotNullOrEmpty(clauses)) {
			instantiateClauses(section.getClauses());
			section.getClauses().clear();			
		}
	}
	
	public SectionInstanceJaxbImpl(Section section, String projectId, String body) {
		this(section, projectId);
		this.setBody(body);
	}
	
	public SectionInstanceJaxbImpl(Section section, String projectId, String body, 
			boolean isAdHoc) {
		this(section, projectId, body);
		this.setAdHoc(isAdHoc);
	}

	public AdvancedDocumentInstance getDocument() { return document; }
	public void setDocument(AdvancedDocumentInstance document) { this.document = document; }

	public List<ClauseInstance> getClauses() { return clauses; }
	public void instantiateClauses(List<Clause> clauses) {
		if (TypeUtils.isNotNullOrEmpty(clauses)) {
			for (Clause c : clauses) {
				this.addClause(new ClauseInstanceJaxbImpl(c, this.getProjectId()));
			}
		}
	}
	public void addClause(ClauseInstance clause) {
		if (TypeUtils.isNotNullOrEmpty(clause)) {
			if (!TypeUtils.isNotNullOrEmpty(this.clauses)) {
				this.clauses = new ArrayList<ClauseInstance>();
			}
			clause.setSection(this);
			this.clauses.add(clause);
		}
	}
	public void addClauses(List<ClauseInstance> clauses) {
		if (TypeUtils.isNotNullOrEmpty(clauses)) {
			if (!TypeUtils.isNotNullOrEmpty(this.clauses)) {
				this.clauses = new ArrayList<ClauseInstance>();
			}
	 		ListIterator<ClauseInstance> iter = clauses.listIterator();
	 		while (iter.hasNext()) {
	 			ClauseInstance clause = iter.next();
	 			clause.setSection(this);
	 			iter.set(clause);
	 		}
			this.clauses.addAll(clauses);
		}		
	}
	public void setClauses(List<ClauseInstance> clauses) { this.clauses = clauses; }
}