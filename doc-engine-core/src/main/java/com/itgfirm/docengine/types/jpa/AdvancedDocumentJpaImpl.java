package com.itgfirm.docengine.types.jpa;

import static com.itgfirm.docengine.types.jpa.TypeConstants.*;
import static com.itgfirm.docengine.types.jpa.TypeUtils.*;

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
 *         DocumentJpaImpl Data Model
 */
@Entity
@DiscriminatorValue(JPA_DISCRIMINATOR_ADV_DOCUMENT)
@JsonIdentityInfo(property = JSON_IDENTITY_PROPERTY, generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class AdvancedDocumentJpaImpl extends ContentJpaImpl {
	private static final String JPA_MAPPED_BY_DOCUMENT = "document";

	/** Child Type **/
	@OneToMany(cascade = CascadeType.ALL, mappedBy = JPA_MAPPED_BY_DOCUMENT, targetEntity = SectionJpaImpl.class)
	@JsonDeserialize(contentAs = SectionJpaImpl.class)
	private Collection<SectionJpaImpl> sections;

	public AdvancedDocumentJpaImpl() {
		// Default constructor for Spring/Hibernate
	}

	public AdvancedDocumentJpaImpl(final String code, final String body) {
		super(code, body);
	}

	public AdvancedDocumentJpaImpl(final ContentJpaImpl content, final String code) {
		super(content, code);
	}

	public final Collection<SectionJpaImpl> getSections() {
		return sections;
	}

	public final void addSection(final SectionJpaImpl section) {
		if (isNotNullOrEmpty(section)) {
			if (!isNotNullOrEmpty(this.sections)) {
				this.sections = new TreeSet<SectionJpaImpl>();
			}
			section.setDocument(this);
			this.sections.add(section);
		}
	}

	public final void addSections(final Collection<SectionJpaImpl> sections) {
		if (isNotNullOrEmpty(sections)) {
			if (!isNotNullOrEmpty(this.sections)) {
				this.sections = new TreeSet<SectionJpaImpl>();
			}
			sections.forEach(s -> {
				s.setDocument(this);
			});
			this.sections.addAll(sections);
		}
	}

	public final void setSections(final Collection<SectionJpaImpl> sections) {
		this.sections = sections;
	}
}