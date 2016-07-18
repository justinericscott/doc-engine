package com.itgfirm.docengine.types.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.itgfirm.docengine.types.Clause;
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.Paragraph;

/**
 * @author Justin Scott
 * 
 * ParagraphJaxbImpl Data Model
 */
@XmlRootElement( name=TypeConstants.JAXB_ROOT_ELEMENT_NAME_PARAGRAPH,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlType( name=TypeConstants.JAXB_TYPE_NAME_PARAGRAPH,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlAccessorType( XmlAccessType.FIELD )
public class ParagraphJaxbImpl extends ContentJaxbImpl implements Paragraph {
	
	private boolean isSubPara = false;
	private boolean isFirst = false;
	private boolean isLast = false;
	private boolean isParent = false;
	private boolean isOptional = false;

	/** Parent Type **/
	@XmlElement( type = ClauseJaxbImpl.class )
	private Clause clause;

	public ParagraphJaxbImpl() { }
	
	public ParagraphJaxbImpl(String body) {
		super(body);
	}

	public ParagraphJaxbImpl(String contentCd, String body) {
		this(body);
		this.setContentCd(contentCd);
	}

	public ParagraphJaxbImpl(Content content) { 
		super(content); 
	}
	
	public ParagraphJaxbImpl(Content content, String contentCd) {
		this(content);
		this.setContentCd(contentCd);
	}
	
	public ParagraphJaxbImpl(Paragraph paragraph) {
		super(paragraph);
		this.isSubPara = paragraph.isSubPara();
		this.isFirst = paragraph.isFirst();
		this.isLast = paragraph.isLast();
		this.isParent = paragraph.isParent();
		this.isOptional = paragraph.isOption();
		this.clause = paragraph.getClause();
	}

	public ParagraphJaxbImpl(Paragraph paragraph, String contentCd) {
		this(paragraph);
		this.setContentCd(contentCd);
	}

	public Clause getClause() { return clause; }
	public void setClause(Clause clause) { this.clause = clause; }

	public boolean isSubPara() { return isSubPara; }
	public void setSubPara(boolean isSubPara) { this.isSubPara = isSubPara; }

	public boolean isFirst() { return isFirst; }
	public void setFirst(boolean isFirst) { this.isFirst = isFirst; }

	public boolean isLast() { return isLast; }
	public void setLast(boolean isLast) { this.isLast = isLast; }

	public boolean isParent() { return isParent; }
	public void setParent(boolean isParent) { this.isParent = isParent; }

	public boolean isOption() { return isOptional; }
	public void setOption(boolean isOption) { this.isOptional = isOption; }
}