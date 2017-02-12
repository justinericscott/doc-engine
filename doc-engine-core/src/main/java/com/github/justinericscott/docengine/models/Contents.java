/**
 * TODO: License
 */
package com.github.justinericscott.docengine.models;

import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;

import java.util.Arrays;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public class Contents {

	@JsonDeserialize(contentAs = Content.class)
	@JsonSerialize(contentAs = Content.class)
	private Content[] contents = null;

	public Contents() {
		// Default constructor for Spring
	}

	public Contents(final Content[] contents) {
		this.contents = contents;
	}

	public Contents(final Collection<Content> contents) {
		setContents(contents);
	}

	public final Content[] getContents() {
		return contents;
	}

	@JsonIgnore
	public final Collection<Content> getContentsList() {
		if (isNotNullOrEmpty(contents)) {
			return Arrays.asList(contents);
		}
		return null;
	}

	public final void setContents(final Content[] contents) {
		this.contents = contents;
	}

	@JsonIgnore
	public final void setContents(final Collection<Content> contents) {
		if (isNotNullOrEmpty(contents)) {
			this.contents = contents.toArray(new Content[contents.size()]);
		}
	}
}