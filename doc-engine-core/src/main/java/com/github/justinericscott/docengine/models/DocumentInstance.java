package com.github.justinericscott.docengine.models;

import static com.github.justinericscott.docengine.models.AbstractJpaModel.ModelConstants.*;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;
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
public class DocumentInstance extends Instance {

	@JsonManagedReference("section-instance")
	@OrderColumn(name = JPA_COL_ORDER)
	@OneToMany(targetEntity = SectionInstance.class, cascade = ALL, mappedBy = JPA_MAPPED_BY_DOCUMENT)
	private Collection<SectionInstance> sections = new TreeSet<SectionInstance>();
	
	public DocumentInstance() {
		// Default constructor for Spring
	}

	public DocumentInstance(final Document document, final String projectId) {
		this.projectId = projectId;
		if (isNotNullOrEmpty(document)) {
			super.setContent(document);
			final Collection<Section> sections = document.getSections();
			if (isNotNullOrEmpty(sections)) {
				instantiateSections(sections);
				sections.clear();
			}
		}
	}

	public final Document getDocument() {
		return (Document) content.getClass().cast(content);
	}

	public final void setDocument(final Document document) {
		super.setContent(document);
	}

	@JsonIgnore
	public final void addSection(final SectionInstance section) {
		if (isNotNullOrEmpty(section)) {
			section.setDocument(this);
			sections.add(section);
		}
	}

	public final Collection<SectionInstance> getSections() {
		return sections;
	}

	public final void setSections(final Collection<SectionInstance> sections) {
		if (isNotNullOrEmpty(sections)) {
			this.sections = sections;
		}
	}
	
	@JsonIgnore
	@Override
	public String toHTML() {
		final StringBuilder sb = new StringBuilder();
		for (final SectionInstance section : getSections()) {
			sb.append(section.toHTML());
		}
		return sb.toString();
	}

	final void instantiateSections(final Collection<Section> sections) {
		if (isNotNullOrEmpty(sections)) {
			for (final Section section : sections) {
				this.addSection(new SectionInstance(section, this.getProjectId()));
			}
		}
	}	
}