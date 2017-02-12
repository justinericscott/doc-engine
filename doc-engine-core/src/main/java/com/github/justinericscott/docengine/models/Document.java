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
 *         DocumentJpaImpl Data Model
 */
@Entity
public class Document extends Content {

	/** Child Type **/
	@JsonManagedReference("sections")
	@OrderColumn(name = JPA_COL_ORDER)
	@OneToMany(targetEntity = Section.class, cascade = ALL, mappedBy = JPA_MAPPED_BY_DOCUMENT)
	private Collection<Section> sections = new TreeSet<Section>();

	public Document() {
		// Default constructor for Spring/Hibernate
	}

	public Document(final String code, final String body) {
		super(code, body);
	}

	public Document(final Content content) {
		super(content);
	}

	public Document(final Content content, final String code) {
		super(content, code);
	}

	@JsonIgnore
	public final void addSection(final Section section) {
		if (isNotNullOrEmpty(section)) {
			section.setDocument(this);
			this.sections.add(section);
		}
	}

	public final Collection<Section> getSections() {
		return (Collection<Section>) sections;
	}

	public final void setSections(final Collection<Section> sections) {
		this.sections = sections;
	}

	@JsonIgnore
	@Override
	public String toHTML() {
		final StringBuilder sb = new StringBuilder();
		for (final Section section : getSections()) {
			sb.append(section.toHTML());
		}
		return sb.toString();
	}	
}