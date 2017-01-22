package com.itgfirm.docengine.types;

import static com.itgfirm.docengine.types.AbstractJpaModel.ModelConstants.*;
import static com.itgfirm.docengine.util.Utils.isNotNullOrEmpty;

import static javax.persistence.CascadeType.ALL;

import java.util.Collection;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

/**
 * @author Justin Scott
 * 
 *         DocumentJpaImpl InstanceJpaImpl Data Model
 */
@Entity
public class DocumentInstanceJpaImpl extends InstanceJpaImpl {

	@JsonManagedReference("section-instance")
	@OrderColumn(name = JPA_COL_ORDER)
	@OneToMany(targetEntity = SectionInstanceJpaImpl.class, cascade = ALL, mappedBy = JPA_MAPPED_BY_DOCUMENT)
	private Collection<SectionInstanceJpaImpl> sections = new TreeSet<SectionInstanceJpaImpl>();
	
	public DocumentInstanceJpaImpl() {
		// Default constructor for Spring
	}

	public DocumentInstanceJpaImpl(final DocumentJpaImpl document, final String projectId) {
		this.projectId = projectId;
		if (isNotNullOrEmpty(document)) {
			super.setContent(document);
			final Collection<SectionJpaImpl> sections = document.getSections();
			if (isNotNullOrEmpty(sections)) {
				instantiateSections(sections);
				sections.clear();
			}
		}
	}

	public final DocumentJpaImpl getDocument() {
		return (DocumentJpaImpl) content.getClass().cast(content);
	}

	public final void setDocument(final DocumentJpaImpl document) {
		super.setContent(document);
	}

	@JsonIgnore
	public final void addSection(final SectionInstanceJpaImpl section) {
		if (isNotNullOrEmpty(section)) {
			section.setDocument(this);
			sections.add(section);
		}
	}

	public final Collection<SectionInstanceJpaImpl> getSections() {
		return sections;
	}

	public final void setSections(final Collection<SectionInstanceJpaImpl> sections) {
		if (isNotNullOrEmpty(sections)) {
			this.sections = sections;
		}
	}

	final void instantiateSections(final Collection<SectionJpaImpl> sections) {
		if (isNotNullOrEmpty(sections)) {
			for (final SectionJpaImpl section : sections) {
				this.addSection(new SectionInstanceJpaImpl(section, this.getProjectId()));
			}
		}
	}
}