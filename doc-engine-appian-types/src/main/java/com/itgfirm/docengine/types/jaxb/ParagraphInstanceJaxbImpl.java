package com.itgfirm.docengine.types.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.itgfirm.docengine.types.ClauseInstance;
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.ParagraphInstance;

/**
 * @author Justin Scott
 * 
 * ParagraphJaxbImpl InstanceJaxbImpl Data Model
 */
@XmlRootElement( name=TypeConstants.JAXB_ROOT_ELEMENT_NAME_PARAGRAPH_INSTANCE,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlType( name=TypeConstants.JAXB_TYPE_NAME_PARAGRAPH_INSTANCE,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlAccessorType( XmlAccessType.FIELD )
public class ParagraphInstanceJaxbImpl extends InstanceJaxbImpl implements ParagraphInstance {
	
	/** Parent Type **/
	@XmlElement( type = ClauseInstanceJaxbImpl.class )
	private ClauseInstance clause;

	public ParagraphInstanceJaxbImpl() { }

	public ParagraphInstanceJaxbImpl(Content content, String projectId) { 
		super(content);
		this.setProjectId(projectId);
		this.setContent(content);
	}
	
	public ParagraphInstanceJaxbImpl(Content content, String projectId, String body) {
		this(content, projectId);
		this.setBody(body);
	}
	
	public ParagraphInstanceJaxbImpl(Content content, String projectId, String body, boolean isAdHoc) {
		this(content, projectId, body);
		this.setAdHoc(isAdHoc);
	}
	
	public ClauseInstance getClause() { return clause; }
	public void setClause(ClauseInstance clause) { this.clause = clause; }
}