package com.itgfirm.docengine.types;

import static com.itgfirm.docengine.util.Utils.isNotNullOrEmpty;

import java.util.Arrays;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class ParagraphInstances {

	@JsonDeserialize(contentAs = ParagraphInstanceJpaImpl.class)
	@JsonSerialize(contentAs = ParagraphInstanceJpaImpl.class)
	private ParagraphInstanceJpaImpl[] paragraphs;

	public ParagraphInstances() {
		// Default constructor for Spring
	}

	public ParagraphInstances(final ParagraphInstanceJpaImpl[] paragraphs) {
		this.paragraphs = paragraphs;
	}

	public ParagraphInstances(final Collection<ParagraphInstanceJpaImpl> paragraphs) {
		setParagraphs(paragraphs);
	}

	public final ParagraphInstanceJpaImpl[] getParagraphs() {
		return paragraphs;
	}

	@JsonIgnore
	public final Collection<ParagraphInstanceJpaImpl> getParagraphsList() {
		if (isNotNullOrEmpty(paragraphs)) {
			return Arrays.asList(paragraphs);
		}
		return null;
	}

	public final void setParagraphs(final ParagraphInstanceJpaImpl[] paragraphs) {
		this.paragraphs = paragraphs;
	}

	@JsonIgnore
	public final void setParagraphs(final Collection<ParagraphInstanceJpaImpl> paragraphs) {
		if (isNotNullOrEmpty(paragraphs)) {
			this.paragraphs = paragraphs.toArray(new ParagraphInstanceJpaImpl[paragraphs.size()]);
		}
	}
}