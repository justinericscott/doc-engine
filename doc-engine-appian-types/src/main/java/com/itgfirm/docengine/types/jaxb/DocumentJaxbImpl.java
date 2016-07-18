package com.itgfirm.docengine.types.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.Document;

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
public class DocumentJaxbImpl extends ContentJaxbImpl implements Document {
	
	public DocumentJaxbImpl() { }
	
	public DocumentJaxbImpl(String body) {
		super(body);
	}
	
	public DocumentJaxbImpl(String contentCd, String body) {
		this(body);
		this.setContentCd(contentCd);
	}
	
	public DocumentJaxbImpl(Content content) { 
		super(content);
	}
	
	public DocumentJaxbImpl(Content content, String contentCd) {
		this(content);
		this.setContentCd(contentCd);
	}
	
	public DocumentJaxbImpl(Document document) {
		super(document);
	}
	
	public DocumentJaxbImpl(Document document, String contentCd) {
		this(document);
		this.setContentCd(contentCd);
	}
	
}