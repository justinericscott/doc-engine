package com.github.justinericscott.docengine.models;

import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;

import java.util.Arrays;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

//@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class DocumentInstances {

	@JsonDeserialize(contentAs = DocumentInstance.class)
	@JsonSerialize(contentAs = DocumentInstance.class)
	private DocumentInstance[] documents;

	public DocumentInstances() {
		// Default constructor for Spring
	}

	public DocumentInstances(final DocumentInstance[] documents) {
		this.documents = documents;
	}

	public DocumentInstances(final Collection<DocumentInstance> documents) {
		setDocuments(documents);
	}

	public final DocumentInstance[] getDocuments() {
		return documents;
	}

	@JsonIgnore
	public final Collection<DocumentInstance> getDocumentsList() {
		if (isNotNullOrEmpty(documents)) {
			return (Collection<DocumentInstance>) Arrays.asList(documents);
		}
		return null;
	}

	public final void setDocuments(final DocumentInstance[] documents) {
		this.documents = documents;
	}

	@JsonIgnore
	public final void setDocuments(final Collection<DocumentInstance> documents) {
		if (isNotNullOrEmpty(documents)) {
			setDocuments(documents.toArray(new DocumentInstance[documents.size()]));
		}
	}
}