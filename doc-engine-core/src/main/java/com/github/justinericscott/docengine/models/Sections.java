package com.github.justinericscott.docengine.models;

import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;

import java.util.Arrays;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Sections {
	
	@JsonDeserialize(contentAs = Section.class)
	@JsonSerialize(contentAs = Section.class)
	private Section[] sections;

	public Sections() {
		// Default constructor for Spring
	}

	public Sections(final Section[] sections) {
		this.sections = sections;
	}
	
	public Sections(final Collection<Section> sections) {
		setSections(sections);
	}

	public final Section[] getSections() {
		return sections;
	}

	@JsonIgnore
	public final Collection<Section> getSectionsList() {
		if (isNotNullOrEmpty(sections)) {
			return Arrays.asList(sections);
		}
		return null;
	}

	public final void setSections(final Section[] sections) {
		this.sections = sections;
	}

	@JsonIgnore
	public final void setSections(final Collection<Section> sections) {
		if (isNotNullOrEmpty(sections)) {
			this.sections = sections.toArray(new Section[sections.size()]);
		}
	}
}