/**
 * TODO: License
 */
package com.itgfirm.docengine.types.jpa;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.itgfirm.docengine.types.Content;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public class Contents {

	@JsonDeserialize(contentAs = ContentJpaImpl.class)
	private List<Content> contents;

	public Contents() {}

	public Contents(List<Content> content) {
		this.contents = content;
	}

	public List<Content> getContents() {
		return contents;
	}

	public void setContents(List<Content> contents) {
		this.contents = contents;
	}
}