/**
 * TODO: License
 */
package com.itgfirm.docengine.types.jpa;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public class Contents {

	@JsonDeserialize(contentAs = ContentJpaImpl.class)
	private List<ContentJpaImpl> contents;

	public Contents() {
	}

	public Contents(List<ContentJpaImpl> content) {
		this.contents = content;
	}

	public List<ContentJpaImpl> getContents() {
		return contents;
	}

	public void setContents(List<ContentJpaImpl> contents) {
		this.contents = contents;
	}
}