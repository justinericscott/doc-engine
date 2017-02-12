package com.github.justinericscott.docengine.models;

import java.util.Collection;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Documents {

	@JsonDeserialize(contentAs = Document.class)
	@JsonSerialize(contentAs = Document.class)
	private Document[] documents;

	public Documents() {
		// Default constructor for Spring
	}

	public Documents(final Document[] documents) {
		this.documents = documents;
	}

	public Documents(final Collection<Document> documents) {
		this.documents = documents.toArray(new Document[documents.size()]);
	}

	public final Document[] getDocuments() {
		return documents;
	}

	public final void setDocuments(final Document[] documents) {
		this.documents = documents;
	}
}