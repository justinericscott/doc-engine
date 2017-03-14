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
 * DocumentJaxbImpl Data Model
 */
@XmlRootElement( name=TypeConstants.JAXB_ROOT_ELEMENT_NAME_ADVANCED,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlType( name=TypeConstants.JAXB_TYPE_NAME_ADVANCED,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlAccessorType( XmlAccessType.FIELD )
public class AdvancedDocumentJaxbImpl extends ContentJaxbImpl {
	
	/** Child Type **/
	@XmlElement( type = SectionJaxbImpl.class )
	private List<SectionJaxbImpl> sections;

	public AdvancedDocumentJaxbImpl() { }
	
	public AdvancedDocumentJaxbImpl(String body) {
		super(body);
	}
	
	public AdvancedDocumentJaxbImpl(String contentCd, String body) {
		this(body);
		this.setContentCd(contentCd);
	}
	
	public AdvancedDocumentJaxbImpl(ContentJaxbImpl content) { 
		super(content);
	}
	
	public AdvancedDocumentJaxbImpl(ContentJaxbImpl content, String contentCd) {
		this(content);
		this.setContentCd(contentCd);
	}
	
	public AdvancedDocumentJaxbImpl(AdvancedDocumentJaxbImpl document) {
		super(document);
	}
	
	public AdvancedDocumentJaxbImpl(AdvancedDocumentJaxbImpl document, String contentCd) {
		this(document);
		this.setContentCd(contentCd);
	}
	
	public List<SectionJaxbImpl> getSections() { return sections; }
	public void addSection(SectionJaxbImpl section) {
		if (TypeUtils.isNotNullOrEmpty(section)) {
			if (!TypeUtils.isNotNullOrEmpty(this.sections)) {
				this.sections = new ArrayList<SectionJaxbImpl>();
			}
			section.setDocument(this);
			this.sections.add(section);
		}
	}	
	public void addSections(List<SectionJaxbImpl> sections) {
		if (TypeUtils.isNotNullOrEmpty(sections)) {
	 		if (!TypeUtils.isNotNullOrEmpty(this.sections)) {
				this.sections = new ArrayList<SectionJaxbImpl>();
			}			
	 		ListIterator<SectionJaxbImpl> iter = sections.listIterator();
	 		while (iter.hasNext()) {
	 			SectionJaxbImpl section = iter.next();
	 			section.setDocument(this);
	 			iter.set(section);
	 		}
	 		this.sections.addAll(sections);
		}
	}
	public void setSections(List<SectionJaxbImpl> sections) { this.sections = sections; }
}