/**TODO: License
 */
package com.github.justinericscott.docengine.models;

import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;

import java.util.Arrays;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.justinericscott.docengine.models.Instance.ClauseInstance;
import com.github.justinericscott.docengine.models.Instance.DocumentInstance;
import com.github.justinericscott.docengine.models.Instance.ParagraphInstance;
import com.github.justinericscott.docengine.models.Instance.SectionInstance;

/**
 * @author Justin Scott TODO: Description
 */
public class Instances {

	@JsonDeserialize(contentAs = Instance.class)
	@JsonSerialize(contentAs = Instance.class)
	private Instance[] instances = null;

	public Instances() {
		// Default constructor for Spring
	}

	public Instances(final Collection<Instance> instances) {
		setInstances(instances);
	}

	@JsonIgnore
	public Collection<Instance> getInstances() {
		if (isNotNullOrEmpty(instances)) {
			return Arrays.asList(instances);
		}
		return null;
	}

	@JsonIgnore
	public void setInstances(final Collection<Instance> instances) {
		if (isNotNullOrEmpty(instances)) {
			this.instances = instances.toArray(new Instance[instances.size()]);
		}
	}
	
	public static class DocumentInstances {

		@JsonDeserialize(contentAs = DocumentInstance.class)
		@JsonSerialize(contentAs = DocumentInstance.class)
		private DocumentInstance[] documents;

		public DocumentInstances() {
			// Default constructor for Spring
		}

		public DocumentInstances(final Collection<DocumentInstance> documents) {
			setDocuments(documents);
		}

		@JsonIgnore
		public Collection<DocumentInstance> getDocumentsList() {
			if (isNotNullOrEmpty(documents)) {
				return (Collection<DocumentInstance>) Arrays.asList(documents);
			}
			return null;
		}

		@JsonIgnore
		public void setDocuments(final Collection<DocumentInstance> documents) {
			if (isNotNullOrEmpty(documents)) {
				this.documents = documents.toArray(new DocumentInstance[documents.size()]);
			}
		}
	}
	
	public static class SectionInstances {

		@JsonDeserialize(contentAs = SectionInstance.class)
		@JsonSerialize(contentAs = SectionInstance.class)
		private SectionInstance[] sections;

		public SectionInstances() {
			// Default constructor for Spring
		}

		public SectionInstances(final Collection<SectionInstance> sections) {
			setSections(sections);
		}

		@JsonIgnore
		public Collection<SectionInstance> getSectionsList() {
			if (isNotNullOrEmpty(sections)) {
				return Arrays.asList(sections);
			}
			return null;
		}

		@JsonIgnore
		public void setSections(final Collection<SectionInstance> sections) {
			if (isNotNullOrEmpty(sections)) {
				this.sections = sections.toArray(new SectionInstance[sections.size()]);
			}
		}
	}
	
	public static class ClauseInstances {

		@JsonDeserialize(contentAs = ClauseInstance.class)
		@JsonSerialize(contentAs = ClauseInstance.class)
		private ClauseInstance[] clauses;

		public ClauseInstances() {
			// Default constructor for Spring
		}
		
		public ClauseInstances(final Collection<ClauseInstance> clauses) {
			setClauses(clauses);
		}

		@JsonIgnore
		public Collection<ClauseInstance> getClausesList() {
			if (isNotNullOrEmpty(clauses)) {
				return Arrays.asList(clauses);	
			}
			return null;
		}

		@JsonIgnore
		public void setClauses(final Collection<ClauseInstance> clauses) {
			if (isNotNullOrEmpty(clauses)) {
				this.clauses = clauses.toArray(new ClauseInstance[clauses.size()]);	
			}		
		}
	}
	
	public static class ParagraphInstances {

		@JsonDeserialize(contentAs = ParagraphInstance.class)
		@JsonSerialize(contentAs = ParagraphInstance.class)
		private ParagraphInstance[] paragraphs;

		public ParagraphInstances() {
			// Default constructor for Spring
		}

		public ParagraphInstances(final Collection<ParagraphInstance> paragraphs) {
			setParagraphs(paragraphs);
		}

		@JsonIgnore
		public Collection<ParagraphInstance> getParagraphsList() {
			if (isNotNullOrEmpty(paragraphs)) {
				return Arrays.asList(paragraphs);
			}
			return null;
		}

		@JsonIgnore
		public void setParagraphs(final Collection<ParagraphInstance> paragraphs) {
			if (isNotNullOrEmpty(paragraphs)) {
				this.paragraphs = paragraphs.toArray(new ParagraphInstance[paragraphs.size()]);
			}
		}
	}
}