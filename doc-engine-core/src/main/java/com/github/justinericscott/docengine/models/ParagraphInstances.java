package com.github.justinericscott.docengine.models;

import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;

import java.util.Arrays;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class ParagraphInstances {

	@JsonDeserialize(contentAs = ParagraphInstance.class)
	@JsonSerialize(contentAs = ParagraphInstance.class)
	private ParagraphInstance[] paragraphs;

	public ParagraphInstances() {
		// Default constructor for Spring
	}

	public ParagraphInstances(final ParagraphInstance[] paragraphs) {
		this.paragraphs = paragraphs;
	}

	public ParagraphInstances(final Collection<ParagraphInstance> paragraphs) {
		setParagraphs(paragraphs);
	}

	public final ParagraphInstance[] getParagraphs() {
		return paragraphs;
	}

	@JsonIgnore
	public final Collection<ParagraphInstance> getParagraphsList() {
		if (isNotNullOrEmpty(paragraphs)) {
			return Arrays.asList(paragraphs);
		}
		return null;
	}

	public final void setParagraphs(final ParagraphInstance[] paragraphs) {
		this.paragraphs = paragraphs;
	}

	@JsonIgnore
	public final void setParagraphs(final Collection<ParagraphInstance> paragraphs) {
		if (isNotNullOrEmpty(paragraphs)) {
			this.paragraphs = paragraphs.toArray(new ParagraphInstance[paragraphs.size()]);
		}
	}
}