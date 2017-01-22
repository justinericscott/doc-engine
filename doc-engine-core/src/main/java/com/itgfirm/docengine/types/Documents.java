package com.itgfirm.docengine.types;

import static com.itgfirm.docengine.util.Utils.isNotNullOrEmpty;

import java.util.Arrays;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Documents {

	private DocumentJpaImpl[] documents;

	public Documents() {
		// Default constructor for Spring
	}

	public Documents(final DocumentJpaImpl[] documents) {
		this.documents = documents;
	}

	public Documents(final Collection<DocumentJpaImpl> documents) {
		setDocuments(documents);
	}

	public final DocumentJpaImpl[] getDocuments() {
		return documents;
	}

	@JsonIgnore
	public final Collection<DocumentJpaImpl> getDocumentsList() {
		if (isNotNullOrEmpty(documents)) {
			return Arrays.asList(documents);
		}
		return null;
	}

	public final void setDocuments(final DocumentJpaImpl[] documents) {
		this.documents = documents;
	}

	@JsonIgnore
	public final void setDocuments(final Collection<DocumentJpaImpl> documents) {
		if (isNotNullOrEmpty(documents)) {
			this.documents = documents.toArray(new DocumentJpaImpl[documents.size()]);
		}
	}
}