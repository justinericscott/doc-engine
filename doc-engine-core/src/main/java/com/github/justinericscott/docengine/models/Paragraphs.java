package com.github.justinericscott.docengine.models;

import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;

import java.util.Arrays;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Paragraphs {

	@JsonDeserialize(contentAs = Paragraph.class)
	@JsonSerialize(contentAs = Paragraph.class)
	private Paragraph[] paragraphs;
	
	public Paragraphs() {
		// Default constructor for Spring.
	}
	
	public Paragraphs(final Paragraph[] paragraphs) {
		this.paragraphs = paragraphs;
	}
	
	public Paragraphs(final Collection<Paragraph> paragraphs) {
		setParagraphs(paragraphs);
	}
	
	public final Paragraph[] getParagraphs() {
		return paragraphs;
	}
	
	@JsonIgnore
	public final Collection<Paragraph> getParagraphsList() {
		if (isNotNullOrEmpty(paragraphs)) {
			return Arrays.asList(paragraphs);
		}
		return null;
	}
	
	public final void setParagraphs(final Paragraph[] paragraphs) {
		this.paragraphs = paragraphs;
	}

	@JsonIgnore
	public final void setParagraphs(final Collection<Paragraph> paragraphs) {
		if (isNotNullOrEmpty(paragraphs)) {
			this.paragraphs = paragraphs.toArray(new Paragraph[paragraphs.size()]);
		}
	}
}
