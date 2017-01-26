package com.github.justinericscott.docengine.types;

import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;

import java.util.Arrays;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class DocumentInstances {

	@JsonDeserialize(contentAs = DocumentInstanceJpaImpl.class)
	@JsonSerialize(contentAs = DocumentInstanceJpaImpl.class)
	private DocumentInstanceJpaImpl[] documents;

	public DocumentInstances() {
		// Default constructor for Spring
	}

	public DocumentInstances(final DocumentInstanceJpaImpl[] documents) {
		this.documents = documents;
	}

	public DocumentInstances(final Collection<DocumentInstanceJpaImpl> documents) {
		setDocuments(documents);
	}

	public final DocumentInstanceJpaImpl[] getDocuments() {
		return documents;
	}

	@JsonIgnore
	public final Collection<DocumentInstanceJpaImpl> getDocumentsList() {
		if (isNotNullOrEmpty(documents)) {
			return (Collection<DocumentInstanceJpaImpl>) Arrays.asList(documents);
		}
		return null;
	}

	public final void setDocuments(final DocumentInstanceJpaImpl[] documents) {
		this.documents = documents;
	}

	@JsonIgnore
	public final void setDocuments(final Collection<DocumentInstanceJpaImpl> documents) {
		if (isNotNullOrEmpty(documents)) {
			setDocuments(documents.toArray(new DocumentInstanceJpaImpl[documents.size()]));
		}
	}
}