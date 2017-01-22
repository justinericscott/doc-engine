package com.itgfirm.docengine.types;

import static com.itgfirm.docengine.util.Utils.isNotNullOrEmpty;

import java.util.Arrays;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Sections {
	
	@JsonDeserialize(contentAs = SectionJpaImpl.class)
	@JsonSerialize(contentAs = SectionJpaImpl.class)
	private SectionJpaImpl[] sections;

	public Sections() {
		// Default constructor for Spring
	}

	public Sections(final SectionJpaImpl[] sections) {
		this.sections = sections;
	}
	
	public Sections(final Collection<SectionJpaImpl> sections) {
		setSections(sections);
	}

	public final SectionJpaImpl[] getSections() {
		return sections;
	}

	@JsonIgnore
	public final Collection<SectionJpaImpl> getSectionsList() {
		if (isNotNullOrEmpty(sections)) {
			return Arrays.asList(sections);
		}
		return null;
	}

	public final void setSections(final SectionJpaImpl[] sections) {
		this.sections = sections;
	}

	@JsonIgnore
	public final void setSections(final Collection<SectionJpaImpl> sections) {
		if (isNotNullOrEmpty(sections)) {
			this.sections = sections.toArray(new SectionJpaImpl[sections.size()]);
		}
	}
}