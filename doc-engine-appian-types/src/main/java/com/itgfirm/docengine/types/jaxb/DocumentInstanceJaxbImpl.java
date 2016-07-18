package com.itgfirm.docengine.types.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.itgfirm.docengine.types.Document;
import com.itgfirm.docengine.types.DocumentInstance;

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
public class DocumentInstanceJaxbImpl extends InstanceJaxbImpl implements DocumentInstance {

	public DocumentInstanceJaxbImpl() { }
	
	public DocumentInstanceJaxbImpl(Document documentJaxbImpl, String projectId) {
		super(documentJaxbImpl);
		this.setProjectId(projectId);
		this.setContent(documentJaxbImpl);
	}
	
	public DocumentInstanceJaxbImpl(Document documentJaxbImpl, String projectId, String body) {
		this(documentJaxbImpl, projectId);
		this.setBody(body);
	}
	
	public DocumentInstanceJaxbImpl(Document documentJaxbImpl, String projectId, String body, 
			boolean isAdHoc) {
		this(documentJaxbImpl, projectId, body);
		this.setAdHoc(isAdHoc);
	}
	
}