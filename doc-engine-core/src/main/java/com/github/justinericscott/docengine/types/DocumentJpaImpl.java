package com.github.justinericscott.docengine.types;

import static com.github.justinericscott.docengine.types.AbstractJpaModel.ModelConstants.*;
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
public class DocumentJpaImpl extends ContentJpaImpl {

	/** Child Type **/
	@JsonManagedReference("sections")
	@OrderColumn(name = JPA_COL_ORDER)
	@OneToMany(targetEntity = SectionJpaImpl.class, cascade = ALL, mappedBy = JPA_MAPPED_BY_DOCUMENT)
	private Collection<SectionJpaImpl> sections = new TreeSet<SectionJpaImpl>();

	public DocumentJpaImpl() {
		// Default constructor for Spring/Hibernate
	}

	public DocumentJpaImpl(final String code, final String body) {
		super(code, body);
	}

	public DocumentJpaImpl(final ContentJpaImpl content) {
		super(content);
	}

	public DocumentJpaImpl(final ContentJpaImpl content, final String code) {
		super(content, code);
	}

	@JsonIgnore
	public final void addSection(final SectionJpaImpl section) {
		if (isNotNullOrEmpty(section)) {
			section.setDocument(this);
			this.sections.add(section);
		}
	}

	public final Collection<SectionJpaImpl> getSections() {
		return (Collection<SectionJpaImpl>) sections;
	}

	public final void setSections(final Collection<SectionJpaImpl> sections) {
		this.sections = sections;
	}
}