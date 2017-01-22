package com.itgfirm.docengine.types;

import static com.itgfirm.docengine.util.Utils.isNotNullOrEmpty;

import java.util.Arrays;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class SectionInstances {

	@JsonDeserialize(contentAs = SectionInstanceJpaImpl.class)
	@JsonSerialize(contentAs = SectionInstanceJpaImpl.class)
	private SectionInstanceJpaImpl[] sections;

	public SectionInstances() {
		// Default constructor for Spring
	}

	public SectionInstances(final SectionInstanceJpaImpl[] sections) {
		this.sections = sections;
	}
	
	public SectionInstances(final Collection<SectionInstanceJpaImpl> sections) {
		setSections(sections);
	}

	public final SectionInstanceJpaImpl[] getSections() {
		return sections;
	}

	@JsonIgnore
	public final Collection<SectionInstanceJpaImpl> getSectionsList() {
		if (isNotNullOrEmpty(sections)) {
			return Arrays.asList(sections);
		}
		return null;
	}

	public final void setSections(final SectionInstanceJpaImpl[] sections) {
		this.sections = sections;
	}

	@JsonIgnore
	public final void setSections(final Collection<SectionInstanceJpaImpl> sections) {
		if (isNotNullOrEmpty(sections)) {
			this.sections = sections.toArray(new SectionInstanceJpaImpl[sections.size()]);
		}
	}
}