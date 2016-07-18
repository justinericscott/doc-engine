package com.itgfirm.docengine.types.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itgfirm.docengine.types.AdvancedDocument;
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.Document;
import com.itgfirm.docengine.types.Section;

/**
 * @author Justin Scott
 * 
 *         DocumentJpaImpl Data Model
 */
@Entity
@DiscriminatorValue(TypeConstants.JPA_DISCRIMINATOR_ADV_DOCUMENT)
@JsonIdentityInfo(property = "@id", generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class AdvancedDocumentJpaImpl extends ContentJpaImpl implements AdvancedDocument {

	/** Child Type **/
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "document",
			targetEntity = SectionJpaImpl.class)
	@JsonDeserialize(contentAs = SectionJpaImpl.class)
	private List<Section> sections;

	public AdvancedDocumentJpaImpl() {}

	public AdvancedDocumentJpaImpl(String body) {
		super(body);
	}

	public AdvancedDocumentJpaImpl(String contentCd, String body) {
		this(body);
		this.setContentCd(contentCd);
	}

	public AdvancedDocumentJpaImpl(Content content) {
		super(content);
	}

	public AdvancedDocumentJpaImpl(Content content, String contentCd) {
		this(content);
		this.setContentCd(contentCd);
	}

	public AdvancedDocumentJpaImpl(Document document) {
		super(document);
	}

	public AdvancedDocumentJpaImpl(Document document, String contentCd) {
		this(document);
		this.setContentCd(contentCd);
	}

	@Override
	public List<Section> getSections() {
		return sections;
	}

	@Override
	public void addSection(Section section) {
		if (TypeUtils.isNotNullOrEmpty(section)) {
			if (!TypeUtils.isNotNullOrEmpty(this.sections)) {
				this.sections = new ArrayList<Section>();
			}
			section.setDocument(this);
			this.sections.add(section);
		}
	}

	@Override
	public void addSections(List<Section> section) {
		if (TypeUtils.isNotNullOrEmpty(section)) {
			if (!TypeUtils.isNotNullOrEmpty(this.sections)) {
				this.sections = new ArrayList<Section>();
			}
			ListIterator<Section> iter = section.listIterator();
			while (iter.hasNext()) {
				Section sectionJpaImpl = iter.next();
				sectionJpaImpl.setDocument(this);
				iter.set(sectionJpaImpl);
			}
			this.sections.addAll(section);
		}
	}

	@Override
	public void setSections(List<Section> section) {
		this.sections = section;
	}
}