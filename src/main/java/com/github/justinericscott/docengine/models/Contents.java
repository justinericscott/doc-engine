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
import com.github.justinericscott.docengine.models.Content.Clause;
import com.github.justinericscott.docengine.models.Content.Document;
import com.github.justinericscott.docengine.models.Content.Paragraph;
import com.github.justinericscott.docengine.models.Content.Section;

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
	
	public static class Documents {

		@JsonDeserialize(contentAs = Document.class)
		@JsonSerialize(contentAs = Document.class)
		private Document[] documents;

		public Documents() {
			// Default constructor for Spring
		}

//		public Documents(final Document[] documents) {
//			this.documents = documents;
//		}

		public Documents(final Collection<Document> documents) {
			this.documents = documents.toArray(new Document[documents.size()]);
		}

		public final Document[] getDocuments() {
			return documents;
		}

//		public final void setDocuments(final Document[] documents) {
//			this.documents = documents;
//		}
	}
	
	public static class Sections {
		
		@JsonDeserialize(contentAs = Section.class)
		@JsonSerialize(contentAs = Section.class)
		private Section[] sections;

		public Sections() {
			// Default constructor for Spring
		}

//		public Sections(final Section[] sections) {
//			this.sections = sections;
//		}
		
		public Sections(final Collection<Section> sections) {
			setSections(sections);
		}

//		public final Section[] getSectionsArray() {
//			return sections;
//		}

		@JsonIgnore
		public final Collection<Section> getSections() {
			if (isNotNullOrEmpty(sections)) {
				return Arrays.asList(sections);
			}
			return null;
		}

		@JsonIgnore
		public final void setSections(final Collection<Section> sections) {
			if (isNotNullOrEmpty(sections)) {
				this.sections = sections.toArray(new Section[sections.size()]);
			}
		}
	}
	
	public static class Clauses {

		@JsonDeserialize(contentAs = Clause.class)
		@JsonSerialize(contentAs = Clause.class)
		private Clause[] clauses;

		public Clauses() {
			// TODO Auto-generated constructor stub
		}

		public Clauses(final Collection<Clause> clauses) {
			setClauses(clauses);
		}

		@JsonIgnore
		public final Collection<Clause> getClauses() {
			if (isNotNullOrEmpty(clauses)) {
				return Arrays.asList(clauses);
			}
			return null;
		}

		private void setClauses(final Collection<Clause> clauses) {
			if (isNotNullOrEmpty(clauses)) {
				this.clauses = clauses.toArray(new Clause[clauses.size()]);
			}
		}
	}
	
	public static class Paragraphs {

		@JsonDeserialize(contentAs = Paragraph.class)
		@JsonSerialize(contentAs = Paragraph.class)
		private Paragraph[] paragraphs;
		
		public Paragraphs() {
			// Default constructor for Spring.
		}
		
		public Paragraphs(final Collection<Paragraph> paragraphs) {
			setParagraphs(paragraphs);
		}
		
		@JsonIgnore
		public final Collection<Paragraph> getParagraphs() {
			if (isNotNullOrEmpty(paragraphs)) {
				return Arrays.asList(paragraphs);
			}
			return null;
		}
		
		@JsonIgnore
		public final void setParagraphs(final Collection<Paragraph> paragraphs) {
			if (isNotNullOrEmpty(paragraphs)) {
				this.paragraphs = paragraphs.toArray(new Paragraph[paragraphs.size()]);
			}
		}
	}
}