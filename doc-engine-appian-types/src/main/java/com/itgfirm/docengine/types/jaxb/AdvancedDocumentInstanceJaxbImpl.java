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
import com.itgfirm.docengine.types.AdvancedDocumentInstance;
import com.itgfirm.docengine.types.Section;
import com.itgfirm.docengine.types.SectionInstance;

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
public class AdvancedDocumentInstanceJaxbImpl extends InstanceJaxbImpl implements AdvancedDocumentInstance {

	/** Child Type **/
	@XmlElement( type = SectionInstanceJaxbImpl.class )
	private List<SectionInstance> sections;

	public AdvancedDocumentInstanceJaxbImpl() { }
	
	public AdvancedDocumentInstanceJaxbImpl(AdvancedDocument documentJaxbImpl, String projectId) {
		super(documentJaxbImpl);
		this.setProjectId(projectId);
		this.setContent(documentJaxbImpl);
		List<Section> sectionJaxbImpls = documentJaxbImpl.getSections();
		if (TypeUtils.isNotNullOrEmpty(sectionJaxbImpls)) {
			instantiateSections(documentJaxbImpl.getSections());
			documentJaxbImpl.getSections().clear();
		}		
	}
	
	public AdvancedDocumentInstanceJaxbImpl(AdvancedDocument documentJaxbImpl, String projectId, String body) {
		this(documentJaxbImpl, projectId);
		this.setBody(body);
	}
	
	public AdvancedDocumentInstanceJaxbImpl(AdvancedDocument documentJaxbImpl, String projectId, String body, 
			boolean isAdHoc) {
		this(documentJaxbImpl, projectId, body);
		this.setAdHoc(isAdHoc);
	}
	
	public List<SectionInstance> getSections() { return sections; }
	public void instantiateSections(List<Section> sectionJaxbImpls) {
		if (TypeUtils.isNotNullOrEmpty(sectionJaxbImpls)) {
			for (Section s : sectionJaxbImpls) {
				this.addSection(new SectionInstanceJaxbImpl(s, this.getProjectId()));
			}
		}
	}
	public void addSection(SectionInstance section) {
		if (TypeUtils.isNotNullOrEmpty(section)) {
			if (!TypeUtils.isNotNullOrEmpty(this.sections)) {
				this.sections = new ArrayList<SectionInstance>();
			}
			section.setDocument(this);
			this.sections.add(section);
		}
	}
	public void addSections(List<SectionInstance> sections) {
		if (TypeUtils.isNotNullOrEmpty(sections)) {
			if (!TypeUtils.isNotNullOrEmpty(this.sections)) {
				this.sections = new ArrayList<SectionInstance>();
			}
	 		ListIterator<SectionInstance> iter = sections.listIterator();
	 		while (iter.hasNext()) {
	 			SectionInstance section = iter.next();
	 			section.setDocument(this);
	 			iter.set(section);
	 		}
			this.sections.addAll(sections);
		}		
	}
	public void setSections(List<SectionInstance> section) { this.sections = section; }
}