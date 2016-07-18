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
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.Document;
import com.itgfirm.docengine.types.Section;

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
public class AdvancedDocumentJaxbImpl extends ContentJaxbImpl implements AdvancedDocument {
	
	/** Child Type **/
	@XmlElement( type = SectionJaxbImpl.class )
	private List<Section> sections;

	public AdvancedDocumentJaxbImpl() { }
	
	public AdvancedDocumentJaxbImpl(String body) {
		super(body);
	}
	
	public AdvancedDocumentJaxbImpl(String contentCd, String body) {
		this(body);
		this.setContentCd(contentCd);
	}
	
	public AdvancedDocumentJaxbImpl(Content content) { 
		super(content);
	}
	
	public AdvancedDocumentJaxbImpl(Content content, String contentCd) {
		this(content);
		this.setContentCd(contentCd);
	}
	
	public AdvancedDocumentJaxbImpl(Document document) {
		super(document);
	}
	
	public AdvancedDocumentJaxbImpl(Document document, String contentCd) {
		this(document);
		this.setContentCd(contentCd);
	}
	
	public List<Section> getSections() { return sections; }
	public void addSection(Section section) {
		if (TypeUtils.isNotNullOrEmpty(section)) {
			if (!TypeUtils.isNotNullOrEmpty(this.sections)) {
				this.sections = new ArrayList<Section>();
			}
			section.setDocument(this);
			this.sections.add(section);
		}
	}	
	public void addSections(List<Section> sections) {
		if (TypeUtils.isNotNullOrEmpty(sections)) {
	 		if (!TypeUtils.isNotNullOrEmpty(this.sections)) {
				this.sections = new ArrayList<Section>();
			}			
	 		ListIterator<Section> iter = sections.listIterator();
	 		while (iter.hasNext()) {
	 			Section section = iter.next();
	 			section.setDocument(this);
	 			iter.set(section);
	 		}
	 		this.sections.addAll(sections);
		}
	}
	public void setSections(List<Section> sections) { this.sections = sections; }
}