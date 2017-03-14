package com.github.justinericscott.docengine.models.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Justin Scott
 * 
 * DocumentJaxbImpl Data Model
 */
@XmlRootElement( name=TypeConstants.JAXB_ROOT_ELEMENT_NAME_DOCUMENT,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlType( name=TypeConstants.JAXB_TYPE_NAME_DOCUMENT,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlAccessorType( XmlAccessType.FIELD )
public class DocumentJaxbImpl extends ContentJaxbImpl {
	
	public DocumentJaxbImpl() { }
	
	public DocumentJaxbImpl(String body) {
		super(body);
	}
	
	public DocumentJaxbImpl(String contentCd, String body) {
		this(body);
		this.setContentCd(contentCd);
	}
	
	public DocumentJaxbImpl(ContentJaxbImpl content) { 
		super(content);
	}
	
	public DocumentJaxbImpl(ContentJaxbImpl content, String contentCd) {
		this(content);
		this.setContentCd(contentCd);
	}
	
	public DocumentJaxbImpl(DocumentJaxbImpl document) {
		super(document);
	}
	
	public DocumentJaxbImpl(DocumentJaxbImpl document, String contentCd) {
		this(document);
		this.setContentCd(contentCd);
	}
	
}