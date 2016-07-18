package com.itgfirm.docengine.types.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itgfirm.docengine.types.AdvancedDocument;
import com.itgfirm.docengine.types.AdvancedDocumentInstance;
import com.itgfirm.docengine.types.Section;
import com.itgfirm.docengine.types.SectionInstance;

/**
 * @author Justin Scott
 * 
 * DocumentJpaImpl InstanceJpaImpl Data Model
 */
@JsonIdentityInfo( property = "@id",
		generator = ObjectIdGenerators.IntSequenceGenerator.class )
@Entity @DiscriminatorValue( TypeConstants.JPA_DISCRIMINATOR_ADV_DOCUMENT )
public class AdvancedDocumentInstanceJpaImpl extends InstanceJpaImpl implements AdvancedDocumentInstance {
	private static final Logger LOG = LogManager.getLogger(AdvancedDocumentInstanceJpaImpl.class);

	@OneToMany( cascade = CascadeType.ALL,
			mappedBy = "document",
			targetEntity = SectionInstanceJpaImpl.class )
	@JsonDeserialize( contentAs = SectionInstanceJpaImpl.class )
	private List<SectionInstance> sections;

	public AdvancedDocumentInstanceJpaImpl() { }
	
	public AdvancedDocumentInstanceJpaImpl(AdvancedDocument document, String projectId) {
		super(document);
		LOG.debug("Instantiating DocumentJpaImpl InstanceJpaImpl.");
		this.setProjectId(projectId);
		this.setContent(document);
		List<Section> section = document.getSections();
		if (TypeUtils.isNotNullOrEmpty(section)) {
			LOG.debug("Attemptng To Instantiating SectionJpaImpl Instances.");
			instantiateSections(document.getSections());
			document.getSections().clear();
		}	
	}
	
	public AdvancedDocumentInstanceJpaImpl(AdvancedDocument document, String projectId, String body) {
		this(document, projectId);
		this.setBody(body);
	}
	
	public AdvancedDocumentInstanceJpaImpl(AdvancedDocument document, String projectId, String body, 
			boolean isAdHoc) {
		this(document, projectId, body);
		this.setAdHoc(isAdHoc);
	}
	
	public List<SectionInstance> getSections() { return sections; }
	public void instantiateSections(List<Section> section) {
		if (TypeUtils.isNotNullOrEmpty(section)) {
			for (Section s : section) {
				this.addSection(new SectionInstanceJpaImpl(s, this.getProjectId()));
			}
		}
	}
	public void addSection(SectionInstance section) {
		if (TypeUtils.isNotNullOrEmpty(section)) {
			if (!TypeUtils.isNotNullOrEmpty(this.sections)) {
				this.sections = new ArrayList<SectionInstance>();
			}
			section.setDocument(this);
			this.sections.add(section);
		}
	}
	public void addSections(List<SectionInstance> sections) {
		if (TypeUtils.isNotNullOrEmpty(sections)) {
			if (!TypeUtils.isNotNullOrEmpty(this.sections)) {
				this.sections = new ArrayList<SectionInstance>();
			}
	 		ListIterator<SectionInstance> iter = sections.listIterator();
	 		while (iter.hasNext()) {
	 			SectionInstance section = iter.next();
	 			section.setDocument(this);
	 			iter.set(section);
	 		}
			this.sections.addAll(sections);
		}		
	}
	public void setSections(List<SectionInstance> section) { this.sections = section; }
}