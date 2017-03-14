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
 * DocumentJaxbImpl InstanceJaxbImpl Data Model
 */
@XmlRootElement( name=TypeConstants.JAXB_ROOT_ELEMENT_NAME_ADVANCED_INSTANCE,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlType( name=TypeConstants.JAXB_TYPE_NAME_ADVANCED_INSTANCE,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlAccessorType( XmlAccessType.FIELD )
public class AdvancedDocumentInstanceJaxbImpl extends InstanceJaxbImpl {

	/** Child Type **/
	@XmlElement( type = SectionInstanceJaxbImpl.class )
	private List<SectionInstanceJaxbImpl> sections;

	public AdvancedDocumentInstanceJaxbImpl() { }
	
	public AdvancedDocumentInstanceJaxbImpl(AdvancedDocumentJaxbImpl documentJaxbImpl, String projectId) {
		super(documentJaxbImpl);
		this.setProjectId(projectId);
		this.setContent(documentJaxbImpl);
		List<SectionJaxbImpl> sectionJaxbImpls = documentJaxbImpl.getSections();
		if (TypeUtils.isNotNullOrEmpty(sectionJaxbImpls)) {
			instantiateSections(documentJaxbImpl.getSections());
			documentJaxbImpl.getSections().clear();
		}		
	}
	
	public AdvancedDocumentInstanceJaxbImpl(AdvancedDocumentJaxbImpl documentJaxbImpl, String projectId, String body) {
		this(documentJaxbImpl, projectId);
		this.setBody(body);
	}
	
	public AdvancedDocumentInstanceJaxbImpl(AdvancedDocumentJaxbImpl documentJaxbImpl, String projectId, String body, 
			boolean isAdHoc) {
		this(documentJaxbImpl, projectId, body);
		this.setAdHoc(isAdHoc);
	}
	
	public List<SectionInstanceJaxbImpl> getSections() { return sections; }
	public void instantiateSections(List<SectionJaxbImpl> sectionJaxbImpls) {
		if (TypeUtils.isNotNullOrEmpty(sectionJaxbImpls)) {
			for (SectionJaxbImpl s : sectionJaxbImpls) {
				this.addSection(new SectionInstanceJaxbImpl(s, this.getProjectId()));
			}
		}
	}
	public void addSection(SectionInstanceJaxbImpl section) {
		if (TypeUtils.isNotNullOrEmpty(section)) {
			if (!TypeUtils.isNotNullOrEmpty(this.sections)) {
				this.sections = new ArrayList<SectionInstanceJaxbImpl>();
			}
			section.setDocument(this);
			this.sections.add(section);
		}
	}
	public void addSections(List<SectionInstanceJaxbImpl> sections) {
		if (TypeUtils.isNotNullOrEmpty(sections)) {
			if (!TypeUtils.isNotNullOrEmpty(this.sections)) {
				this.sections = new ArrayList<SectionInstanceJaxbImpl>();
			}
	 		ListIterator<SectionInstanceJaxbImpl> iter = sections.listIterator();
	 		while (iter.hasNext()) {
	 			SectionInstanceJaxbImpl section = iter.next();
	 			section.setDocument(this);
	 			iter.set(section);
	 		}
			this.sections.addAll(sections);
		}		
	}
	public void setSections(List<SectionInstanceJaxbImpl> section) { this.sections = section; }
}