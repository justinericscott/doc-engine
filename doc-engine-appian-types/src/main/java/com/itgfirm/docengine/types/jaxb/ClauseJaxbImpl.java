package com.itgfirm.docengine.types.jaxb;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.itgfirm.docengine.types.Clause;
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.Paragraph;
import com.itgfirm.docengine.types.Section;

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
public class ClauseJaxbImpl extends ContentJaxbImpl implements Clause {
	
	/** Parent Type **/
	@XmlElement( type = SectionJaxbImpl.class )
	private Section section;
	
	/** Child Type **/
	@XmlElement( type = ParagraphJaxbImpl.class )
	private List<Paragraph> paragraphs;

	public ClauseJaxbImpl() { }
	
	public ClauseJaxbImpl(String body) {
		super(body);
	}

	public ClauseJaxbImpl(String contentCd, String body) {
		this(body);
		this.setContentCd(contentCd);
	}

	public ClauseJaxbImpl(Content content) { 
		super(content); 
	}
	
	public ClauseJaxbImpl(Content content, String contentCd) {
		this(content);
		this.setContentCd(contentCd);
	}
	
	public ClauseJaxbImpl(Clause clause) {
		super(clause);
		this.section = clause.getSection();
	}

	public ClauseJaxbImpl(Clause clause, String contentCd) {
		this(clause);
		this.setContentCd(contentCd);
	}

	public Section getSection() { return section; }
	public void setSection(Section section) { this.section = section; }

	public List<Paragraph> getParagraphs() { return paragraphs; }
	public void addParagraph(Paragraph paragraph) {
		if (TypeUtils.isNotNullOrEmpty(paragraph)) {
			if (!TypeUtils.isNotNullOrEmpty(this.paragraphs)) {
				this.paragraphs = new ArrayList<Paragraph>();
			}
			paragraph.setClause(this);
			this.paragraphs.add(paragraph);			
		}
	}
	public void addParagraphs(List<Paragraph> paragraphs) {
		if (TypeUtils.isNotNullOrEmpty(paragraphs)) {
			if (!TypeUtils.isNotNullOrEmpty(this.paragraphs)) {
				this.paragraphs = new ArrayList<Paragraph>();
			}
			ListIterator<Paragraph> iter = paragraphs.listIterator();
			while (iter.hasNext()) {
				Paragraph paragraph = iter.next();
				paragraph.setClause(this);
				iter.set(paragraph);
			}			
			this.paragraphs.addAll(paragraphs);			
		}		
	}
	public void setParagraphs(List<Paragraph> paragraph) { this.paragraphs = paragraph; }
}