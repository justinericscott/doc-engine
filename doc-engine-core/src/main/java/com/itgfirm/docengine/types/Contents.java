/**
 * TODO: License
 */
package com.itgfirm.docengine.types;

import static com.itgfirm.docengine.util.Utils.isNotNullOrEmpty;

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

	@JsonDeserialize(contentAs = ContentJpaImpl.class)
	@JsonSerialize(contentAs = ContentJpaImpl.class)
	private ContentJpaImpl[] contents = null;

	public Contents() {
		// Default constructor for Spring
	}

	public Contents(final ContentJpaImpl[] contents) {
		this.contents = contents;
	}

	public Contents(final Collection<ContentJpaImpl> contents) {
		setContents(contents);
	}

	public final ContentJpaImpl[] getContents() {
		return contents;
	}

	@JsonIgnore
	public final Collection<ContentJpaImpl> getContentsList() {
		if (isNotNullOrEmpty(contents)) {
			return Arrays.asList(contents);
		}
		return null;
	}

	public final void setContents(final ContentJpaImpl[] contents) {
		this.contents = contents;
	}

	@JsonIgnore
	public final void setContents(final Collection<ContentJpaImpl> contents) {
		if (isNotNullOrEmpty(contents)) {
			this.contents = contents.toArray(new ContentJpaImpl[contents.size()]);
		}
	}
}