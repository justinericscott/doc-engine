package com.github.justinericscott.docengine.controller;

import static com.github.justinericscott.docengine.controller.RestUtils.createResponseForNoContent;
import static com.github.justinericscott.docengine.controller.RestUtils.createResponseForSuccess;
import static com.github.justinericscott.docengine.controller.RestUtils.RestConstants.*;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.github.justinericscott.docengine.models.Clause;
//import com.github.justinericscott.docengine.models.Clauses;
import com.github.justinericscott.docengine.models.Content;
import com.github.justinericscott.docengine.models.Content.Clause;
import com.github.justinericscott.docengine.models.Content.Document;
import com.github.justinericscott.docengine.models.Content.Paragraph;
import com.github.justinericscott.docengine.models.Content.Section;
import com.github.justinericscott.docengine.models.Contents;
import com.github.justinericscott.docengine.models.Contents.Clauses;
import com.github.justinericscott.docengine.models.Contents.Documents;
import com.github.justinericscott.docengine.models.Contents.Paragraphs;
import com.github.justinericscott.docengine.models.Contents.Sections;
//import com.github.justinericscott.docengine.models.Document;
//import com.github.justinericscott.docengine.models.Documents;
//import com.github.justinericscott.docengine.models.Paragraph;
//import com.github.justinericscott.docengine.models.Paragraphs;
//import com.github.justinericscott.docengine.models.Section;
//import com.github.justinericscott.docengine.models.Sections;
import com.github.justinericscott.docengine.service.content.ContentService;

/**
 * @author Justin Scott
 * 
 *         Represents a RESTful endpoint for internal and external services to
 *         perform CRUD operations on Content data. The controller "class"
 *         provides a standardized layer of abstraction, consumers need not
 *         "know" about the Content Service.
 */
@RestController
@RequestMapping(CONTENT)
final class ContentRestController {
	private static final Logger LOG = LoggerFactory.getLogger(ContentRestController.class);

	@Autowired
	private ContentService _contents;

	@Autowired
	private RestUtils _utils;

	ContentRestController() {
		LOG.trace("Creating new Content REST Controller.");
	}

	@RestController
	@RequestMapping(CONTENT + DOCUMENT)
	final class DocumentRestController {

		DocumentRestController() {
			LOG.trace("Creating new Document REST Controller.");
		}

		@RequestMapping(method = DELETE, value = BY_ID)
		final ResponseEntity<?> delete(@PathVariable(PARAM_ID) final Long id) {
			final boolean pass = _contents.delete(id, Document.class);
			return new ResponseEntity<>(pass ? OK : BAD_REQUEST);
		}

		@RequestMapping(method = DELETE, value = BY_CODE)
		final ResponseEntity<?> delete(@PathVariable(PARAM_CODE) final String code) {
			final boolean pass = _contents.delete(code, Document.class);
			return new ResponseEntity<>(pass ? OK : BAD_REQUEST);
		}

		@RequestMapping(method = GET, path = DOCUMENTS)
		final ResponseEntity<Documents> findAll() {
			final Documents contents = _contents.findAll(Documents.class);
			if (isNotNullOrEmpty(contents)) {
				return createResponseForSuccess(contents);
			} else {
				return createResponseForNoContent(_utils.getURI(_utils.getDestination(CONTENTS)), contents);
			}
		}

		@RequestMapping(method = GET, value = BY_CODE)
		final ResponseEntity<Document> findByCode(@PathVariable(PARAM_CODE) final String code) {
			final Document document = _contents.findByCode(code, Document.class);
			if (isNotNullOrEmpty(document)) {
				return createResponseForSuccess(document);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + DOCUMENT, BY_CODE), document);
			}
		}

		@RequestMapping(method = GET, value = BY_CODE + IS_EAGER_KIDS)
		final ResponseEntity<Document> findByCode(@PathVariable(PARAM_CODE) final String code,
				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final Document document = _contents.findByCode(code, Document.class, eagerKids);
			if (isNotNullOrEmpty(document)) {
				return createResponseForSuccess(document);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + DOCUMENT, BY_CODE + IS_EAGER_KIDS), document);
			}
		}

		@RequestMapping(method = GET, value = BY_CODE_LIKE)
		final ResponseEntity<Documents> findByCodeLike(@PathVariable(PARAM_LIKE) final String like) {
			final Documents document = _contents.findByCodeLike(like, Documents.class);
			if (isNotNullOrEmpty(document)) {
				return createResponseForSuccess(document);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + DOCUMENT, BY_CODE_LIKE), document);
			}
		}

		@RequestMapping(method = GET, value = BY_ID)
		final ResponseEntity<Document> findOne(@PathVariable(PARAM_ID) final Long id) {
			final Document document = _contents.findOne(id, Document.class);
			if (isNotNullOrEmpty(document)) {
				return createResponseForSuccess(document);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + DOCUMENT, BY_ID), document);
			}
		}

		@RequestMapping(method = GET, value = BY_ID + IS_EAGER_KIDS)
		final ResponseEntity<Document> findOne(@PathVariable(PARAM_ID) final Long id,
				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final Document document = _contents.findOne(id, Document.class, eagerKids);
			if (isNotNullOrEmpty(document)) {
				return createResponseForSuccess(document);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + DOCUMENT, BY_ID + IS_EAGER_KIDS), document);
			}
		}

		@RequestMapping(method = GET, value = CHILDREN + BY_ID)
		final ResponseEntity<Sections> getChildren(@PathVariable(PARAM_ID) final Long id) {
			final Sections sections = _contents.getChildren(id, Sections.class);
			if (isNotNullOrEmpty(sections)) {
				return createResponseForSuccess(sections);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + DOCUMENT, CHILDREN + BY_ID), sections);
			}
		}

		@RequestMapping(method = GET, value = CHILDREN + BY_ID + IS_EAGER_KIDS)
		final ResponseEntity<Sections> getChildren(@PathVariable(PARAM_ID) final Long id,
				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final Sections sections = _contents.getChildren(id, Sections.class, eagerKids);
			if (isNotNullOrEmpty(sections)) {
				return createResponseForSuccess(sections);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + DOCUMENT, CHILDREN + BY_ID + IS_EAGER_KIDS),
						sections);
			}
		}

		@RequestMapping(method = GET, value = CHILDREN + BY_CODE)
		final ResponseEntity<Sections> getChildren(@PathVariable(PARAM_CODE) final String code) {
			final Sections sections = _contents.getChildren(code, Sections.class);
			if (isNotNullOrEmpty(sections)) {
				return createResponseForSuccess(sections);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + DOCUMENT, CHILDREN + BY_CODE), sections);
			}
		}

		@RequestMapping(method = GET, value = CHILDREN + BY_CODE + IS_EAGER_KIDS)
		final ResponseEntity<Sections> getChildren(@PathVariable(PARAM_CODE) final String code,
				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final Sections sections = _contents.getChildren(code, Sections.class, eagerKids);
			if (isNotNullOrEmpty(sections)) {
				return createResponseForSuccess(sections);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + DOCUMENT, CHILDREN + BY_CODE + IS_EAGER_KIDS),
						sections);
			}
		}

		@RequestMapping(method = PUT)
		final ResponseEntity<Document> save(@RequestBody final Document document) {
			final Document saved = _contents.save(document);
			if (isNotNullOrEmpty(saved)) {
				return createResponseForSuccess(saved);
			} else {
				return createResponseForNoContent(_utils.getURI(_utils.getDestination(CONTENT + DOCUMENT)), document);
			}
		}
	}

	@RestController
	@RequestMapping(CONTENT + SECTION)
	final class SectionRestController {

		SectionRestController() {
			LOG.trace("Creating new Section REST Controller.");
		}

		@RequestMapping(method = GET, value = BY_CODE)
		final ResponseEntity<Section> findByCode(@PathVariable(PARAM_CODE) final String code) {
			final Section section = _contents.findByCode(code, Section.class);
			if (isNotNullOrEmpty(section)) {
				return createResponseForSuccess(section);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + SECTION, BY_CODE), section);
			}
		}

		@RequestMapping(method = GET, value = BY_CODE + IS_EAGER_KIDS)
		final ResponseEntity<Section> findByCode(@PathVariable(PARAM_CODE) final String code,
				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final Section section = _contents.findByCode(code, Section.class, eagerKids);
			if (isNotNullOrEmpty(section)) {
				return createResponseForSuccess(section);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + SECTION, BY_CODE + IS_EAGER_KIDS), section);
			}
		}

		@RequestMapping(method = GET, value = BY_CODE_LIKE)
		final ResponseEntity<Sections> getByCodeLike(@PathVariable(PARAM_LIKE) final String like) {
			final Sections section = _contents.findByCodeLike(like, Sections.class);
			if (isNotNullOrEmpty(section)) {
				return createResponseForSuccess(section);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + SECTION, BY_CODE_LIKE), section);
			}
		}

		@RequestMapping(method = GET, value = BY_ID)
		final ResponseEntity<Section> findOne(@PathVariable(PARAM_ID) final Long id) {
			final Section section = _contents.findOne(id, Section.class);
			if (isNotNullOrEmpty(section)) {
				return createResponseForSuccess(section);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + SECTION, BY_ID), section);
			}
		}

		@RequestMapping(method = GET, value = BY_ID + IS_EAGER_KIDS)
		final ResponseEntity<Section> findOne(@PathVariable(PARAM_ID) final Long id,
				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final Section section = _contents.findOne(id, Section.class, eagerKids);
			if (isNotNullOrEmpty(section)) {
				return createResponseForSuccess(section);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + SECTION, BY_ID + IS_EAGER_KIDS), section);
			}
		}

		@RequestMapping(method = GET, value = CHILDREN + BY_ID)
		final ResponseEntity<Clauses> getChildren(@PathVariable(PARAM_ID) final Long id) {
			final Clauses clauses = _contents.getChildren(id, Clauses.class);
			if (isNotNullOrEmpty(clauses)) {
				return createResponseForSuccess(clauses);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + SECTION, CHILDREN + BY_ID), clauses);
			}
		}

		@RequestMapping(method = GET, value = CHILDREN + BY_ID + IS_EAGER_KIDS)
		final ResponseEntity<Clauses> getChildren(@PathVariable(PARAM_ID) final Long id,
				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final Clauses clauses = _contents.getChildren(id, Clauses.class, eagerKids);
			if (isNotNullOrEmpty(clauses)) {
				return createResponseForSuccess(clauses);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + SECTION, CHILDREN + BY_ID + IS_EAGER_KIDS),
						clauses);
			}
		}

		@RequestMapping(method = GET, value = CHILDREN + BY_CODE)
		final ResponseEntity<Clauses> getChildren(@PathVariable(PARAM_CODE) final String code) {
			final Clauses clauses = _contents.getChildren(code, Clauses.class);
			if (isNotNullOrEmpty(clauses)) {
				return createResponseForSuccess(clauses);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + SECTION, CHILDREN + BY_CODE), clauses);
			}
		}

		@RequestMapping(method = GET, value = CHILDREN + BY_CODE + IS_EAGER_KIDS)
		final ResponseEntity<Clauses> getChildren(@PathVariable(PARAM_CODE) final String code,
				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final Clauses clauses = _contents.getChildren(code, Clauses.class, eagerKids);
			if (isNotNullOrEmpty(clauses)) {
				return createResponseForSuccess(clauses);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + SECTION, CHILDREN + BY_CODE + IS_EAGER_KIDS),
						clauses);
			}
		}

		@RequestMapping(method = PUT)
		final ResponseEntity<Section> save(@RequestBody final Section section) {
			final Section saved = _contents.save(section);
			if (isNotNullOrEmpty(saved)) {
				return createResponseForSuccess(saved);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + SECTION), saved);
			}
		}
	}

	@RestController
	@RequestMapping(CONTENT + CLAUSE)
	final class ClauseRestController {

		ClauseRestController() {
			LOG.trace("Creating new Clause REST Controller.");
		}

		@RequestMapping(method = GET, value = BY_CODE)
		final ResponseEntity<Clause> findByCode(@PathVariable(PARAM_CODE) final String code) {
			final Clause clause = _contents.findByCode(code, Clause.class);
			if (isNotNullOrEmpty(clause)) {
				return createResponseForSuccess(clause);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + CLAUSE, BY_CODE), clause);
			}
		}

		@RequestMapping(method = GET, value = BY_CODE + IS_EAGER_KIDS)
		final ResponseEntity<Clause> findByCode(@PathVariable(PARAM_CODE) final String code,
				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final Clause clause = _contents.findByCode(code, Clause.class, eagerKids);
			if (isNotNullOrEmpty(clause)) {
				return createResponseForSuccess(clause);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + CLAUSE, BY_CODE + IS_EAGER_KIDS), clause);
			}
		}

		@RequestMapping(method = GET, value = BY_CODE_LIKE)
		final ResponseEntity<Clauses> findByCodeLike(@PathVariable(PARAM_LIKE) final String like) {
			final Clauses clause = _contents.findByCodeLike(like, Clauses.class);
			if (isNotNullOrEmpty(clause)) {
				return createResponseForSuccess(clause);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + CLAUSE, BY_CODE_LIKE), clause);
			}
		}

		@RequestMapping(method = GET, value = BY_ID)
		final ResponseEntity<Clause> findOne(@PathVariable(PARAM_ID) final Long id) {
			final Clause clause = _contents.findOne(id, Clause.class);
			if (isNotNullOrEmpty(clause)) {
				return createResponseForSuccess(clause);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + CLAUSE, BY_ID), clause);
			}
		}

		@RequestMapping(method = GET, value = BY_ID + IS_EAGER_KIDS)
		final ResponseEntity<Clause> findOne(@PathVariable(PARAM_ID) final Long id,
				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final Clause clause = _contents.findOne(id, Clause.class, eagerKids);
			if (isNotNullOrEmpty(clause)) {
				return createResponseForSuccess(clause);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + CLAUSE, BY_ID + IS_EAGER_KIDS), clause);
			}
		}

		@RequestMapping(method = GET, value = CHILDREN + BY_ID)
		final ResponseEntity<Paragraphs> getChildren(@PathVariable(PARAM_ID) final Long id) {
			final Paragraphs paragraphs = _contents.getChildren(id, Paragraphs.class);
			if (isNotNullOrEmpty(paragraphs)) {
				return createResponseForSuccess(paragraphs);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + CLAUSE, CHILDREN + BY_ID), paragraphs);
			}
		}

		@RequestMapping(method = GET, value = CHILDREN + BY_ID + IS_EAGER_KIDS)
		final ResponseEntity<Paragraphs> getChildren(@PathVariable(PARAM_ID) final Long id,
				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final Paragraphs paragraphs = _contents.getChildren(id, Paragraphs.class, eagerKids);
			if (isNotNullOrEmpty(paragraphs)) {
				return createResponseForSuccess(paragraphs);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + CLAUSE, CHILDREN + BY_ID + IS_EAGER_KIDS),
						paragraphs);
			}
		}

		@RequestMapping(method = GET, value = CHILDREN + BY_CODE)
		final ResponseEntity<Paragraphs> getChildren(@PathVariable(PARAM_CODE) final String code) {
			final Paragraphs paragraphs = _contents.getChildren(code, Paragraphs.class);
			if (isNotNullOrEmpty(paragraphs)) {
				return createResponseForSuccess(paragraphs);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + CLAUSE, CHILDREN + BY_CODE), paragraphs);
			}
		}

		@RequestMapping(method = GET, value = CHILDREN + BY_CODE + IS_EAGER_KIDS)
		final ResponseEntity<Paragraphs> getChildren(@PathVariable(PARAM_CODE) final String code,
				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final Paragraphs paragraphs = _contents.getChildren(code, Paragraphs.class, eagerKids);
			if (isNotNullOrEmpty(paragraphs)) {
				return createResponseForSuccess(paragraphs);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + CLAUSE, CHILDREN + BY_CODE + IS_EAGER_KIDS),
						paragraphs);
			}
		}

		@RequestMapping(method = PUT)
		final ResponseEntity<Clause> save(@RequestBody final Clause clause) {
			final Clause saved = _contents.save(clause);
			if (isNotNullOrEmpty(saved)) {
				return createResponseForSuccess(saved);
			} else {
				return createResponseForNoContent(_utils.getURI(_utils.getDestination(CONTENT + CLAUSE)), saved);
			}
		}
	}

	@RestController
	@RequestMapping(CONTENT + PARAGRAPH)
	final class ParagraphRestController {

		ParagraphRestController() {
			LOG.trace("Creating new Paragraph REST Controller.");
		}

		@RequestMapping(method = GET, value = BY_CODE)
		final ResponseEntity<Paragraph> findByCode(@PathVariable(PARAM_CODE) final String code) {
			final Paragraph paragraph = _contents.findByCode(code, Paragraph.class);
			if (isNotNullOrEmpty(paragraph)) {
				return createResponseForSuccess(paragraph);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + PARAGRAPH, BY_CODE), paragraph);
			}
		}

		@RequestMapping(method = GET, value = BY_CODE_LIKE)
		final ResponseEntity<Paragraphs> findByCodeLike(@PathVariable(PARAM_LIKE) final String like) {
			final Paragraphs paragraph = _contents.findByCodeLike(like, Paragraphs.class);
			if (isNotNullOrEmpty(paragraph)) {
				return createResponseForSuccess(paragraph);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + PARAGRAPH, BY_CODE_LIKE), paragraph);
			}
		}

		@RequestMapping(method = GET, value = BY_ID)
		final ResponseEntity<Paragraph> findOne(@PathVariable(PARAM_ID) final Long id) {
			final Paragraph paragraph = _contents.findOne(id, Paragraph.class);
			if (isNotNullOrEmpty(paragraph)) {
				return createResponseForSuccess(paragraph);
			} else {
				return createResponseForNoContent(_utils.getURI(CONTENT + PARAGRAPH, BY_ID), paragraph);
			}
		}

		@RequestMapping(method = PUT)
		final ResponseEntity<Paragraph> save(@RequestBody final Paragraph paragraph) {
			final Paragraph saved = _contents.save(paragraph);
			if (isNotNullOrEmpty(saved)) {
				return createResponseForSuccess(saved);
			} else {
				return createResponseForNoContent(_utils.getURI(_utils.getDestination(CONTENT + PARAGRAPH)), paragraph);
			}
		}
	}

	@RequestMapping(method = DELETE, value = BY_ID)
	final ResponseEntity<?> delete(@PathVariable(PARAM_ID) final Long id) {
		final boolean pass = _contents.delete(id, Content.class);
		return new ResponseEntity<>(pass ? OK : BAD_REQUEST);
	}

	@RequestMapping(method = DELETE, value = BY_CODE)
	final ResponseEntity<?> delete(@PathVariable(PARAM_CODE) final String code) {
		final boolean pass = _contents.delete(code, Content.class);
		return new ResponseEntity<>(pass ? OK : BAD_REQUEST);
	}

	@RequestMapping(method = GET, path = CONTENTS)
	final ResponseEntity<?> findAll() {
		final Contents contents = _contents.findAll();
		if (isNotNullOrEmpty(contents)) {
			return createResponseForSuccess(contents);
		} else {
			return createResponseForNoContent(_utils.getURI(_utils.getDestination(CONTENTS)), contents);
		}
	}

	@RequestMapping(method = GET, value = BY_CODE)
	final ResponseEntity<?> findByCode(@PathVariable(PARAM_CODE) final String code) {
		final Content content = _contents.findByCode(code);
		if (isNotNullOrEmpty(content)) {
			return createResponseForSuccess(content);
		} else {
			return createResponseForNoContent(_utils.getURI(CONTENT, BY_CODE), content);
		}
	}

	@RequestMapping(method = GET, value = BY_CODE_LIKE)
	final ResponseEntity<?> findByCodeLike(@PathVariable(PARAM_LIKE) final String like) {
		final Contents incoming = _contents.findByCodeLike(like, Contents.class);
		if (isNotNullOrEmpty(incoming) && isNotNullOrEmpty(incoming.getContents())) {
			return createResponseForSuccess(incoming);
		} else {
			return createResponseForNoContent(_utils.getURI(CONTENT, BY_CODE_LIKE), incoming);
		}
	}

	@RequestMapping(method = GET, value = BY_ID)
	final ResponseEntity<Content> findOne(@PathVariable(PARAM_ID) final Long id) {
		final Content content = _contents.findOne(id);
		if (isNotNullOrEmpty(content)) {
			return createResponseForSuccess(content);
		} else {
			return createResponseForNoContent(_utils.getURI(CONTENT, BY_ID), content);
		}
	}

	@RequestMapping(method = PUT)
	final ResponseEntity<Content> save(@RequestBody final Content content) {
		final Content saved = _contents.save(content);
		if (isNotNullOrEmpty(saved)) {
			return createResponseForSuccess(saved);
		} else {
			LOG.warn("Saved contents came back null.");
			return createResponseForNoContent(_utils.getURI(_utils.getDestination(CONTENT)), saved);
		}
	}

	@RequestMapping(method = PUT, path = CONTENTS)
	final ResponseEntity<Contents> save(@RequestBody final Contents contents) {
		final Contents saved = _contents.save(contents);
		if (isNotNullOrEmpty(saved)) {
			return createResponseForSuccess(saved);
		} else {
			LOG.warn("Saved contents came back null.");
			return createResponseForNoContent(_utils.getURI(CONTENT, CONTENTS), saved);
		}
	}
}