package com.itgfirm.docengine.types;

import static com.itgfirm.docengine.util.Utils.isNotNullOrEmpty;

import java.util.Arrays;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Paragraphs {

	@JsonDeserialize(contentAs = ParagraphJpaImpl.class)
	@JsonSerialize(contentAs = ParagraphJpaImpl.class)
	private ParagraphJpaImpl[] paragraphs;
	
	public Paragraphs() {
		// Default constructor for Spring.
	}
	
	public Paragraphs(final ParagraphJpaImpl[] paragraphs) {
		this.paragraphs = paragraphs;
	}
	
	public Paragraphs(final Collection<ParagraphJpaImpl> paragraphs) {
		setParagraphs(paragraphs);
	}
	
	public final ParagraphJpaImpl[] getParagraphs() {
		return paragraphs;
	}
	
	@JsonIgnore
	public final Collection<ParagraphJpaImpl> getParagraphsList() {
		if (isNotNullOrEmpty(paragraphs)) {
			return Arrays.asList(paragraphs);
		}
		return null;
	}
	
	public final void setParagraphs(final ParagraphJpaImpl[] paragraphs) {
		this.paragraphs = paragraphs;
	}

	@JsonIgnore
	public final void setParagraphs(final Collection<ParagraphJpaImpl> paragraphs) {
		if (isNotNullOrEmpty(paragraphs)) {
			this.paragraphs = paragraphs.toArray(new ParagraphJpaImpl[paragraphs.size()]);
		}
	}
}
