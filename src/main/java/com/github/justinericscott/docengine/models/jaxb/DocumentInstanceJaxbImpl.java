package com.github.justinericscott.docengine.models.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Justin Scott
 * 
 * DocumentJaxbImpl InstanceJaxbImpl Data Model
 */
@XmlRootElement( name=TypeConstants.JAXB_ROOT_ELEMENT_NAME_DOCUMENT_INSTANCE,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlType( name=TypeConstants.JAXB_TYPE_NAME_DOCUMENT_INSTANCE,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlAccessorType( XmlAccessType.FIELD )
public class DocumentInstanceJaxbImpl extends InstanceJaxbImpl {

	public DocumentInstanceJaxbImpl() { }
	
	public DocumentInstanceJaxbImpl(DocumentJaxbImpl documentJaxbImpl, String projectId) {
		super(documentJaxbImpl);
		this.setProjectId(projectId);
		this.setContent(documentJaxbImpl);
	}
	
	public DocumentInstanceJaxbImpl(DocumentJaxbImpl documentJaxbImpl, String projectId, String body) {
		this(documentJaxbImpl, projectId);
		this.setBody(body);
	}
	
	public DocumentInstanceJaxbImpl(DocumentJaxbImpl documentJaxbImpl, String projectId, String body, 
			boolean isAdHoc) {
		this(documentJaxbImpl, projectId, body);
		this.setAdHoc(isAdHoc);
	}
	
}