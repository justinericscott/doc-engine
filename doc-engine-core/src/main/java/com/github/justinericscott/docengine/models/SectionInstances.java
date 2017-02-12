package com.github.justinericscott.docengine.models;

import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;

import java.util.Arrays;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class SectionInstances {

	@JsonDeserialize(contentAs = SectionInstance.class)
	@JsonSerialize(contentAs = SectionInstance.class)
	private SectionInstance[] sections;

	public SectionInstances() {
		// Default constructor for Spring
	}

	public SectionInstances(final SectionInstance[] sections) {
		this.sections = sections;
	}
	
	public SectionInstances(final Collection<SectionInstance> sections) {
		setSections(sections);
	}

	public final SectionInstance[] getSections() {
		return sections;
	}

	@JsonIgnore
	public final Collection<SectionInstance> getSectionsList() {
		if (isNotNullOrEmpty(sections)) {
			return Arrays.asList(sections);
		}
		return null;
	}

	public final void setSections(final SectionInstance[] sections) {
		this.sections = sections;
	}

	@JsonIgnore
	public final void setSections(final Collection<SectionInstance> sections) {
		if (isNotNullOrEmpty(sections)) {
			this.sections = sections.toArray(new SectionInstance[sections.size()]);
		}
	}
}