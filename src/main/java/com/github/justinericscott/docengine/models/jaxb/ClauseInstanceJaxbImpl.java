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
 * ClauseJaxbImpl InstanceJaxbImpl Data Model
 */
@XmlRootElement( name=TypeConstants.JAXB_ROOT_ELEMENT_NAME_CLAUSE_INSTANCE,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlType( name=TypeConstants.JAXB_TYPE_NAME_CLAUSE_INSTANCE,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlAccessorType( XmlAccessType.FIELD )
public class ClauseInstanceJaxbImpl extends InstanceJaxbImpl {
	
	/** Parent Type **/
	@XmlElement( type = SectionInstanceJaxbImpl.class )
	private SectionInstanceJaxbImpl section;
	
	/** Child Type **/
	@XmlElement( type = ParagraphInstanceJaxbImpl.class )
	private List<ParagraphInstanceJaxbImpl> paragraphs;

	public ClauseInstanceJaxbImpl() {  }
	
	public ClauseInstanceJaxbImpl(ClauseJaxbImpl clause, String projectId) {
		super(clause);
		this.setProjectId(projectId);
		this.setContent(clause);
		List<ParagraphJaxbImpl> paragraphs = clause.getParagraphs();
		if (TypeUtils.isNotNullOrEmpty(paragraphs)) {
			instantiateParagraphs(clause.getParagraphs());
			clause.getParagraphs().clear();			
		}
	}
	
	public ClauseInstanceJaxbImpl(ClauseJaxbImpl clause, String projectId, String body) {
		this(clause, projectId);
		this.setBody(body);
	}
	
	public ClauseInstanceJaxbImpl(ClauseJaxbImpl clause, String projectId, String body, 
			boolean isAdHoc) {
		this(clause, projectId, body);
		this.setAdHoc(isAdHoc);
	}

	public SectionInstanceJaxbImpl getSection() { return section; }
	public void setSection(SectionInstanceJaxbImpl section) { this.section = section; }

	public List<ParagraphInstanceJaxbImpl> getParagraphs() { return paragraphs; }
	public void instantiateParagraphs(List<ParagraphJaxbImpl> paragraphs) {
		if (TypeUtils.isNotNullOrEmpty(paragraphs)) {
			for (ParagraphJaxbImpl p : paragraphs) {
				this.addParagraph(new ParagraphInstanceJaxbImpl(p, this.getProjectId()));
			}
		}
	}
	public void addParagraph(ParagraphInstanceJaxbImpl paragraph) {
		if (TypeUtils.isNotNullOrEmpty(paragraph)) {
			if (!TypeUtils.isNotNullOrEmpty(paragraphs)) {
				this.paragraphs = new ArrayList<ParagraphInstanceJaxbImpl>();
			}
			paragraph.setClause(this);
			this.paragraphs.add(paragraph);
		}
	}
	public void addParagraphs(List<ParagraphInstanceJaxbImpl> paragraphs) {
		if (TypeUtils.isNotNullOrEmpty(paragraphs)) {
			if (!TypeUtils.isNotNullOrEmpty(paragraphs)) {
				this.paragraphs = new ArrayList<ParagraphInstanceJaxbImpl>();
			}
	 		ListIterator<ParagraphInstanceJaxbImpl> iter = paragraphs.listIterator();
	 		while (iter.hasNext()) {
	 			ParagraphInstanceJaxbImpl paragraph = iter.next();
	 			paragraph.setClause(this);
	 			iter.set(paragraph);
	 		}
			this.paragraphs.addAll(paragraphs);
		}		
	}
	public void setParagraphs(List<ParagraphInstanceJaxbImpl> paragraphs) { this.paragraphs = paragraphs; }
}