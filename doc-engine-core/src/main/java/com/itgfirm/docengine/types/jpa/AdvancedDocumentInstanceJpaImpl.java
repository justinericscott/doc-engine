package com.itgfirm.docengine.types.jpa;

import static com.itgfirm.docengine.types.jpa.AbstractJpaModel.ModelConstants.*;

import java.util.Collection;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author Justin Scott
 * 
 *         DocumentJpaImpl InstanceJpaImpl Data Model
 */
@Entity
@DiscriminatorValue(JPA_DSCRMNTR_ADV_DOCUMENT)
@JsonIdentityInfo(property = JSON_PROP_ID, generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class AdvancedDocumentInstanceJpaImpl extends InstanceJpaImpl {
	private static final String JPA_MAPPED_BY_DOCUMENT = "document";

	@OneToMany(cascade = CascadeType.ALL, mappedBy = JPA_MAPPED_BY_DOCUMENT, targetEntity = SectionInstanceJpaImpl.class)
	@JsonDeserialize(contentAs = SectionInstanceJpaImpl.class)
	private Collection<SectionInstanceJpaImpl> sections;

	public AdvancedDocumentInstanceJpaImpl() {
		// Default constructor for Spring
	}

	public AdvancedDocumentInstanceJpaImpl(final AdvancedDocumentJpaImpl document, final String projectId) {
		super(document);
		this.setProjectId(projectId);
		if (isNotNullOrEmpty(document)) {
			this.setContent(document);
			final Iterable<SectionJpaImpl> sections = document.getSections();
			if (isNotNullOrEmpty(sections)) {
				instantiateSections(sections);
				document.getSections().clear();
			}
		}
	}

	public AdvancedDocumentInstanceJpaImpl(final AdvancedDocumentJpaImpl document, final String projectId,
			final String body) {
		this(document, projectId);
		this.setBody(body);
	}

	public AdvancedDocumentInstanceJpaImpl(final AdvancedDocumentJpaImpl document, final String projectId,
			final String body, final boolean isAdHoc) {
		this(document, projectId, body);
		this.setAdHoc(isAdHoc);
	}

	public final Iterable<SectionInstanceJpaImpl> getSections() {
		return sections;
	}

	public final void instantiateSections(final Iterable<SectionJpaImpl> sections) {
		if (isNotNullOrEmpty(sections)) {
			sections.forEach(s -> {
				this.addSection(new SectionInstanceJpaImpl(s, this.getProjectId()));
			});
		}
	}

	public final void addSection(final SectionInstanceJpaImpl section) {
		if (isNotNullOrEmpty(section)) {
			if (!isNotNullOrEmpty(this.sections)) {
				this.sections = new TreeSet<SectionInstanceJpaImpl>();
			}
			section.setDocument(this);
			this.sections.add(section);
		}
	}

	public final void addSections(final Iterable<SectionInstanceJpaImpl> sections) {
		if (isNotNullOrEmpty(sections)) {
			if (!isNotNullOrEmpty(this.sections)) {
				this.sections = new TreeSet<SectionInstanceJpaImpl>();
			}
			sections.forEach(section -> {
				section.setDocument(this);
			});
			this.sections.addAll((Collection<SectionInstanceJpaImpl>) sections);
		}
	}

	public final void setSections(final Iterable<SectionInstanceJpaImpl> section) {
		this.sections = (Collection<SectionInstanceJpaImpl>) section;
	}
}