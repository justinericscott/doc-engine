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
import com.itgfirm.docengine.types.ClauseInstance;
import com.itgfirm.docengine.types.Paragraph;
import com.itgfirm.docengine.types.ParagraphInstance;
import com.itgfirm.docengine.types.SectionInstance;

/**
 * @author Justin Scott
 * 
 * ClauseJaxbImpl InstanceJaxbImpl Data Model
 */
@XmlRootElement( name=TypeConstants.JAXB_ROOT_ELEMENT_NAME_CLAUSE_INSTANCE,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlType( name=TypeConstants.JAXB_TYPE_NAME_CLAUSE_INSTANCE,
	namespace=TypeConstants.JAXB_NAMESPACE )
@XmlAccessorType( XmlAccessType.FIELD )
public class ClauseInstanceJaxbImpl extends InstanceJaxbImpl implements ClauseInstance {
	
	/** Parent Type **/
	@XmlElement( type = SectionInstanceJaxbImpl.class )
	private SectionInstance section;
	
	/** Child Type **/
	@XmlElement( type = ParagraphInstanceJaxbImpl.class )
	private List<ParagraphInstance> paragraphs;

	public ClauseInstanceJaxbImpl() {  }
	
	public ClauseInstanceJaxbImpl(Clause clause, String projectId) {
		super(clause);
		this.setProjectId(projectId);
		this.setContent(clause);
		List<Paragraph> paragraphs = clause.getParagraphs();
		if (TypeUtils.isNotNullOrEmpty(paragraphs)) {
			instantiateParagraphs(clause.getParagraphs());
			clause.getParagraphs().clear();			
		}
	}
	
	public ClauseInstanceJaxbImpl(Clause clause, String projectId, String body) {
		this(clause, projectId);
		this.setBody(body);
	}
	
	public ClauseInstanceJaxbImpl(Clause clause, String projectId, String body, 
			boolean isAdHoc) {
		this(clause, projectId, body);
		this.setAdHoc(isAdHoc);
	}

	public SectionInstance getSection() { return section; }
	public void setSection(SectionInstance section) { this.section = section; }

	public List<ParagraphInstance> getParagraphs() { return paragraphs; }
	public void instantiateParagraphs(List<Paragraph> paragraphs) {
		if (TypeUtils.isNotNullOrEmpty(paragraphs)) {
			for (Paragraph p : paragraphs) {
				this.addParagraph(new ParagraphInstanceJaxbImpl(p, this.getProjectId()));
			}
		}
	}
	public void addParagraph(ParagraphInstance paragraph) {
		if (TypeUtils.isNotNullOrEmpty(paragraph)) {
			if (!TypeUtils.isNotNullOrEmpty(paragraphs)) {
				this.paragraphs = new ArrayList<ParagraphInstance>();
			}
			paragraph.setClause(this);
			this.paragraphs.add(paragraph);
		}
	}
	public void addParagraphs(List<ParagraphInstance> paragraphs) {
		if (TypeUtils.isNotNullOrEmpty(paragraphs)) {
			if (!TypeUtils.isNotNullOrEmpty(paragraphs)) {
				this.paragraphs = new ArrayList<ParagraphInstance>();
			}
	 		ListIterator<ParagraphInstance> iter = paragraphs.listIterator();
	 		while (iter.hasNext()) {
	 			ParagraphInstance paragraph = iter.next();
	 			paragraph.setClause(this);
	 			iter.set(paragraph);
	 		}
			this.paragraphs.addAll(paragraphs);
		}		
	}
	public void setParagraphs(List<ParagraphInstance> paragraphs) { this.paragraphs = paragraphs; }
}