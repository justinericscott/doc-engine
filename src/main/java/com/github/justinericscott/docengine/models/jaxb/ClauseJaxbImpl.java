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
 * ClauseJaxbImpl Data Model
 */
@XmlRootElement( name=TypeConstants.JAXB_ROOT_ELEMENT_NAME_CLAUSE,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlType( name=TypeConstants.JAXB_TYPE_NAME_CLAUSE,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlAccessorType( XmlAccessType.FIELD )
public class ClauseJaxbImpl extends ContentJaxbImpl {
	
	/** Parent Type **/
	@XmlElement( type = SectionJaxbImpl.class )
	private SectionJaxbImpl section;
	
	/** Child Type **/
	@XmlElement( type = ParagraphJaxbImpl.class )
	private List<ParagraphJaxbImpl> paragraphs;

	public ClauseJaxbImpl() { }
	
	public ClauseJaxbImpl(String body) {
		super(body);
	}

	public ClauseJaxbImpl(String contentCd, String body) {
		this(body);
		this.setContentCd(contentCd);
	}

	public ClauseJaxbImpl(ContentJaxbImpl content) { 
		super(content); 
	}
	
	public ClauseJaxbImpl(ContentJaxbImpl content, String contentCd) {
		this(content);
		this.setContentCd(contentCd);
	}
	
	public ClauseJaxbImpl(ClauseJaxbImpl clause) {
		super(clause);
		this.section = clause.getSection();
	}

	public ClauseJaxbImpl(ClauseJaxbImpl clause, String contentCd) {
		this(clause);
		this.setContentCd(contentCd);
	}

	public SectionJaxbImpl getSection() { return section; }
	public void setSection(SectionJaxbImpl section) { this.section = section; }

	public List<ParagraphJaxbImpl> getParagraphs() { return paragraphs; }
	public void addParagraph(ParagraphJaxbImpl paragraph) {
		if (TypeUtils.isNotNullOrEmpty(paragraph)) {
			if (!TypeUtils.isNotNullOrEmpty(this.paragraphs)) {
				this.paragraphs = new ArrayList<ParagraphJaxbImpl>();
			}
			paragraph.setClause(this);
			this.paragraphs.add(paragraph);			
		}
	}
	public void addParagraphs(List<ParagraphJaxbImpl> paragraphs) {
		if (TypeUtils.isNotNullOrEmpty(paragraphs)) {
			if (!TypeUtils.isNotNullOrEmpty(this.paragraphs)) {
				this.paragraphs = new ArrayList<ParagraphJaxbImpl>();
			}
			ListIterator<ParagraphJaxbImpl> iter = paragraphs.listIterator();
			while (iter.hasNext()) {
				ParagraphJaxbImpl paragraph = iter.next();
				paragraph.setClause(this);
				iter.set(paragraph);
			}			
			this.paragraphs.addAll(paragraphs);			
		}		
	}
	public void setParagraphs(List<ParagraphJaxbImpl> paragraph) { this.paragraphs = paragraph; }
}