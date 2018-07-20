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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.github.justinericscott.docengine.models.ClauseInstance;
//import com.github.justinericscott.docengine.models.ClauseInstances;
//import com.github.justinericscott.docengine.models.DocumentInstance;
//import com.github.justinericscott.docengine.models.DocumentInstances;
import com.github.justinericscott.docengine.models.Instance;
import com.github.justinericscott.docengine.models.Instance.ClauseInstance;
import com.github.justinericscott.docengine.models.Instance.DocumentInstance;
import com.github.justinericscott.docengine.models.Instance.ParagraphInstance;
import com.github.justinericscott.docengine.models.Instance.SectionInstance;
import com.github.justinericscott.docengine.models.Instances;
import com.github.justinericscott.docengine.models.Instances.ClauseInstances;
import com.github.justinericscott.docengine.models.Instances.DocumentInstances;
import com.github.justinericscott.docengine.models.Instances.ParagraphInstances;
import com.github.justinericscott.docengine.models.Instances.SectionInstances;
//import com.github.justinericscott.docengine.models.ParagraphInstance;
//import com.github.justinericscott.docengine.models.ParagraphInstances;
//import com.github.justinericscott.docengine.models.SectionInstance;
//import com.github.justinericscott.docengine.models.SectionInstances;
import com.github.justinericscott.docengine.service.content.InstanceService;

/**
 * @author Justin Scott
 * 
 *         Represents a RESTful endpoint for internal and external services to
 *         perform CRUD operations on Instance data. The controller "class"
 *         provides a standardized layer of abstraction, consumers need not
 *         "know" about the Instance Service.
 */
@RestController
@RequestMapping(INSTANCE)
final class InstanceRestController<T, L> {
	private static final Logger LOG = LoggerFactory.getLogger(InstanceRestController.class);

	@Autowired
	private InstanceService _instances;

	@Autowired
	private RestUtils _utils;

	InstanceRestController() {
		LOG.trace("Creating new Instance REST Controller.");
	}

	@RestController
	@RequestMapping(INSTANCE + DOCUMENT)
	final class DocumentInstanceRestController {

		DocumentInstanceRestController() {
			LOG.trace("Creating new Document Instance REST Controller.");
		}

		@RequestMapping(method = GET, value = DOCUMENTS)
		final ResponseEntity<DocumentInstances> findAll() {
			final DocumentInstances instances = _instances.findAll(DocumentInstances.class);
			if (isNotNullOrEmpty(instances)) {
				return createResponseForSuccess(instances);
			} else {
				return createResponseForNoContent(_utils.getURI(_utils.getDestination(INSTANCE + DOCUMENT + DOCUMENTS)), instances);
			}
		}

//		@RequestMapping(method = GET, value = DOCUMENTS + IS_EAGER_KIDS)
//		final ResponseEntity<DocumentInstances> findAll(@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
//			final DocumentInstances instances = _instances.findAll(DocumentInstances.class, eagerKids);
//			if (isNotNullOrEmpty(instances)) {
//				return createResponseForSuccess(instances);
//			} else {
//				return createResponseForNoContent(_utils.getURI(_utils.getDestination(INSTANCE + DOCUMENT + DOCUMENTS)), instances);
//			}
//		}

		@RequestMapping(method = GET, value = BY_PROJECT_ID + BY_CODE)
		final ResponseEntity<DocumentInstance> findByProjectIdAndCode(
				@PathVariable(PARAM_PROJECT_ID) final String projectId, @PathVariable(PARAM_CODE) final String code) {
			final DocumentInstance document = _instances.findByProjectIdAndCode(projectId, code,
					DocumentInstance.class);
			if (isNotNullOrEmpty(document)) {
				return createResponseForSuccess(document);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + DOCUMENT, BY_PROJECT_ID + BY_CODE), document);
			}
		}

//		@RequestMapping(method = GET, value = BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS)
//		final ResponseEntity<DocumentInstance> findByProjectIdAndCode(
//				@PathVariable(PARAM_PROJECT_ID) final String projectId, @PathVariable(PARAM_CODE) final String code,
//				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
//			final DocumentInstance document = _instances.findByProjectIdAndCode(projectId, code,
//					DocumentInstance.class, eagerKids);
//			if (isNotNullOrEmpty(document)) {
//				return createResponseForSuccess(document);
//			} else {
//				return createResponseForNoContent(
//						_utils.getURI(INSTANCE + DOCUMENT, BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS), document);
//			}
//		}

		@RequestMapping(method = GET, value = BY_ID)
		final ResponseEntity<DocumentInstance> findOne(@PathVariable(PARAM_ID) final Long id) {
			final DocumentInstance document = _instances.findOne(id, DocumentInstance.class);
			if (isNotNullOrEmpty(document)) {
				return createResponseForSuccess(document);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + DOCUMENT, BY_ID), document);
			}
		}

//		@RequestMapping(method = GET, value = BY_ID + IS_EAGER_KIDS)
//		final ResponseEntity<DocumentInstance> findOne(@PathVariable(PARAM_ID) final Long id,
//				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
//			final DocumentInstance document = _instances.findOne(id, DocumentInstance.class, eagerKids);
//			if (isNotNullOrEmpty(document)) {
//				return createResponseForSuccess(document);
//			} else {
//				return createResponseForNoContent(_utils.getURI(INSTANCE + DOCUMENT, BY_ID + IS_EAGER_KIDS), document);
//			}
//		}

		@RequestMapping(method = GET, value = CHILDREN + BY_ID)
		final ResponseEntity<SectionInstances> getChildren(@PathVariable(PARAM_ID) final Long id) {
			final SectionInstances sections = _instances.getChildren(id, SectionInstances.class);
			if (isNotNullOrEmpty(sections)) {
				return createResponseForSuccess(sections);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + DOCUMENT, CHILDREN + BY_ID), sections);
			}
		}

//		@RequestMapping(method = GET, value = CHILDREN + BY_ID + IS_EAGER_KIDS)
//		final ResponseEntity<SectionInstances> getChildren(@PathVariable(PARAM_ID) final Long id,
//				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
//			final SectionInstances sections = _instances.getChildren(id, SectionInstances.class, eagerKids);
//			if (isNotNullOrEmpty(sections)) {
//				return createResponseForSuccess(sections);
//			} else {
//				return createResponseForNoContent(_utils.getURI(INSTANCE + DOCUMENT, CHILDREN + BY_ID + IS_EAGER_KIDS),
//						sections);
//			}
//		}

		@RequestMapping(method = GET, value = CHILDREN + BY_PROJECT_ID + BY_CODE)
		final ResponseEntity<SectionInstances> getChildren(@PathVariable(PARAM_PROJECT_ID) final String projectId,
				@PathVariable(PARAM_CODE) final String code) {
			final SectionInstances sections = _instances.getChildren(projectId, code, SectionInstances.class);
			if (isNotNullOrEmpty(sections)) {
				return createResponseForSuccess(sections);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + DOCUMENT, CHILDREN + BY_PROJECT_ID + BY_CODE),
						sections);
			}
		}

//		@RequestMapping(method = GET, value = CHILDREN + BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS)
//		final ResponseEntity<SectionInstances> getChildren(@PathVariable(PARAM_PROJECT_ID) final String projectId,
//				@PathVariable(PARAM_CODE) final String code, @PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
//			final SectionInstances sections = _instances.getChildren(projectId, code, SectionInstances.class,
//					eagerKids);
//			if (isNotNullOrEmpty(sections)) {
//				return createResponseForSuccess(sections);
//			} else {
//				return createResponseForNoContent(
//						_utils.getURI(INSTANCE + DOCUMENT, CHILDREN + BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS),
//						sections);
//			}
//		}

		@RequestMapping(method = PUT)
		final ResponseEntity<DocumentInstance> save(@RequestBody final DocumentInstance document) {
			final DocumentInstance saved = _instances.save(document);
			if (isNotNullOrEmpty(saved)) {
				return createResponseForSuccess(saved);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + DOCUMENT), saved);
			}
		}
	}

	@RestController
	@RequestMapping(INSTANCE + SECTION)
	final class SectionInstanceRestController {

		SectionInstanceRestController() {
			LOG.trace("Creating new Section Instance REST Controller.");
		}

		@RequestMapping(method = GET, value = BY_PROJECT_ID + BY_CODE)
		final ResponseEntity<SectionInstance> findByProjectIdAndCode(
				@PathVariable(PARAM_PROJECT_ID) final String projectId, @PathVariable(PARAM_CODE) final String code) {
			final SectionInstance section = _instances.findByProjectIdAndCode(projectId, code,
					SectionInstance.class);
			if (isNotNullOrEmpty(section)) {
				return createResponseForSuccess(section);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + SECTION, BY_PROJECT_ID + BY_CODE), section);
			}
		}

//		@RequestMapping(method = GET, value = BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS)
//		final ResponseEntity<SectionInstance> findByProjectIdAndCode(
//				@PathVariable(PARAM_PROJECT_ID) final String projectId, @PathVariable(PARAM_CODE) final String code,
//				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
//			final SectionInstance section = _instances.findByProjectIdAndCode(projectId, code,
//					SectionInstance.class, eagerKids);
//			if (isNotNullOrEmpty(section)) {
//				return createResponseForSuccess(section);
//			} else {
//				return createResponseForNoContent(
//						_utils.getURI(INSTANCE + SECTION, BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS), section);
//			}
//		}

		@RequestMapping(method = GET, value = BY_ID)
		final ResponseEntity<SectionInstance> findOne(@PathVariable(PARAM_ID) final Long id) {
			final SectionInstance section = _instances.findOne(id, SectionInstance.class);
			if (isNotNullOrEmpty(section)) {
				return createResponseForSuccess(section);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + SECTION, BY_ID), section);
			}
		}

//		@RequestMapping(method = GET, value = BY_ID + IS_EAGER_KIDS)
//		final ResponseEntity<SectionInstance> findOne(@PathVariable(PARAM_ID) final Long id,
//				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
//			final SectionInstance section = _instances.findOne(id, SectionInstance.class, eagerKids);
//			if (isNotNullOrEmpty(section)) {
//				return createResponseForSuccess(section);
//			} else {
//				return createResponseForNoContent(_utils.getURI(INSTANCE + SECTION, BY_ID + IS_EAGER_KIDS), section);
//			}
//		}

		@RequestMapping(method = GET, value = CHILDREN + BY_ID)
		final ResponseEntity<ClauseInstances> getChildren(@PathVariable(PARAM_ID) final Long id) {
			final ClauseInstances clauses = _instances.getChildren(id, ClauseInstances.class);
			if (isNotNullOrEmpty(clauses)) {
				return createResponseForSuccess(clauses);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + SECTION, CHILDREN + BY_ID), clauses);
			}
		}

//		@RequestMapping(method = GET, value = CHILDREN + BY_ID + IS_EAGER_KIDS)
//		final ResponseEntity<ClauseInstances> getChildren(@PathVariable(PARAM_ID) final Long id,
//				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
//			final ClauseInstances clauses = _instances.getChildren(id, ClauseInstances.class, eagerKids);
//			if (isNotNullOrEmpty(clauses)) {
//				return createResponseForSuccess(clauses);
//			} else {
//				return createResponseForNoContent(_utils.getURI(INSTANCE + SECTION, CHILDREN + BY_ID + IS_EAGER_KIDS),
//						clauses);
//			}
//		}

		@RequestMapping(method = GET, value = CHILDREN + BY_PROJECT_ID + BY_CODE)
		final ResponseEntity<ClauseInstances> getChildren(@PathVariable(PARAM_PROJECT_ID) final String projectId,
				@PathVariable(PARAM_CODE) final String code) {
			final ClauseInstances clauses = _instances.getChildren(projectId, code, ClauseInstances.class);
			if (isNotNullOrEmpty(clauses)) {
				return createResponseForSuccess(clauses);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + SECTION, CHILDREN + BY_PROJECT_ID + BY_CODE),
						clauses);
			}
		}

//		@RequestMapping(method = GET, value = CHILDREN + BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS)
//		final ResponseEntity<ClauseInstances> getChildren(@PathVariable(PARAM_PROJECT_ID) final String projectId,
//				@PathVariable(PARAM_CODE) final String code, @PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
//			final ClauseInstances clauses = _instances.getChildren(projectId, code, ClauseInstances.class, eagerKids);
//			if (isNotNullOrEmpty(clauses)) {
//				return createResponseForSuccess(clauses);
//			} else {
//				return createResponseForNoContent(
//						_utils.getURI(INSTANCE + SECTION, CHILDREN + BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS), clauses);
//			}
//		}

		@RequestMapping(method = PUT)
		final ResponseEntity<SectionInstance> save(@RequestBody final SectionInstance section) {
			final SectionInstance saved = _instances.save(section);
			if (isNotNullOrEmpty(saved)) {
				return createResponseForSuccess(saved);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + SECTION), saved);
			}
		}
	}

	@RestController
	@RequestMapping(INSTANCE + CLAUSE)
	final class ClauseInstanceRestController {

		ClauseInstanceRestController() {
			LOG.trace("Creating new Clause Instance REST Controller.");
		}

		@RequestMapping(method = GET, value = BY_PROJECT_ID + BY_CODE)
		final ResponseEntity<ClauseInstance> findByProjectIdAndCode(
				@PathVariable(PARAM_PROJECT_ID) final String projectId, @PathVariable(PARAM_CODE) final String code) {
			final ClauseInstance clause = _instances.findByProjectIdAndCode(projectId, code,
					ClauseInstance.class);
			if (isNotNullOrEmpty(clause)) {
				return createResponseForSuccess(clause);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + CLAUSE, BY_PROJECT_ID + BY_CODE), clause);
			}
		}

//		@RequestMapping(method = GET, value = BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS)
//		final ResponseEntity<ClauseInstance> findByProjectIdAndCode(
//				@PathVariable(PARAM_PROJECT_ID) final String projectId, @PathVariable(PARAM_CODE) final String code,
//				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
//			final ClauseInstance clause = _instances.findByProjectIdAndCode(projectId, code,
//					ClauseInstance.class, eagerKids);
//			if (isNotNullOrEmpty(clause)) {
//				return createResponseForSuccess(clause);
//			} else {
//				return createResponseForNoContent(
//						_utils.getURI(INSTANCE + CLAUSE, BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS), clause);
//			}
//		}

		@RequestMapping(method = GET, value = BY_ID)
		final ResponseEntity<ClauseInstance> findOne(@PathVariable(PARAM_ID) final Long id) {
			final ClauseInstance clause = _instances.findOne(id, ClauseInstance.class);
			if (isNotNullOrEmpty(clause)) {
				return createResponseForSuccess(clause);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + CLAUSE, BY_ID), clause);
			}
		}

//		@RequestMapping(method = GET, value = BY_ID + IS_EAGER_KIDS)
//		final ResponseEntity<ClauseInstance> findOne(@PathVariable(PARAM_ID) final Long id,
//				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
//			final ClauseInstance clause = _instances.findOne(id, ClauseInstance.class, eagerKids);
//			if (isNotNullOrEmpty(clause)) {
//				return createResponseForSuccess(clause);
//			} else {
//				return createResponseForNoContent(_utils.getURI(INSTANCE + CLAUSE, BY_ID + IS_EAGER_KIDS), clause);
//			}
//		}

		@RequestMapping(method = GET, value = CHILDREN + BY_ID)
		final ResponseEntity<ParagraphInstances> getChildren(@PathVariable(PARAM_ID) final Long id) {
			final ParagraphInstances paragraphs = _instances.getChildren(id, ParagraphInstances.class);
			if (isNotNullOrEmpty(paragraphs)) {
				return createResponseForSuccess(paragraphs);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + CLAUSE, CHILDREN + BY_ID), paragraphs);
			}
		}

//		@RequestMapping(method = GET, value = CHILDREN + BY_ID + IS_EAGER_KIDS)
//		final ResponseEntity<ParagraphInstances> getChildren(@PathVariable(PARAM_ID) final Long id,
//				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
//			final ParagraphInstances paragraphs = _instances.getChildren(id, ParagraphInstances.class, eagerKids);
//			if (isNotNullOrEmpty(paragraphs)) {
//				return createResponseForSuccess(paragraphs);
//			} else {
//				return createResponseForNoContent(_utils.getURI(INSTANCE + CLAUSE, CHILDREN + BY_ID + IS_EAGER_KIDS),
//						paragraphs);
//			}
//		}

		@RequestMapping(method = GET, value = CHILDREN + BY_PROJECT_ID + BY_CODE)
		final ResponseEntity<ParagraphInstances> getChildren(@PathVariable(PARAM_PROJECT_ID) final String projectId,
				@PathVariable(PARAM_CODE) final String code) {
			final ParagraphInstances paragraphs = _instances.getChildren(projectId, code, ParagraphInstances.class);
			if (isNotNullOrEmpty(paragraphs)) {
				return createResponseForSuccess(paragraphs);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + CLAUSE, CHILDREN + BY_PROJECT_ID + BY_CODE),
						paragraphs);
			}
		}

//		@RequestMapping(method = GET, value = CHILDREN + BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS)
//		final ResponseEntity<ParagraphInstances> getChildren(@PathVariable(PARAM_PROJECT_ID) final String projectId,
//				@PathVariable(PARAM_CODE) final String code, @PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
//			final ParagraphInstances paragraphs = _instances.getChildren(projectId, code, ParagraphInstances.class,
//					eagerKids);
//			if (isNotNullOrEmpty(paragraphs)) {
//				return createResponseForSuccess(paragraphs);
//			} else {
//				return createResponseForNoContent(
//						_utils.getURI(INSTANCE + CLAUSE, CHILDREN + BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS),
//						paragraphs);
//			}
//		}

		@RequestMapping(method = PUT)
		final ResponseEntity<ClauseInstance> save(@RequestBody final ClauseInstance clause) {
			final ClauseInstance saved = _instances.save(clause);
			if (isNotNullOrEmpty(saved)) {
				return createResponseForSuccess(saved);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + CLAUSE, CHILDREN + BY_PROJECT_ID + BY_CODE),
						saved);
			}
		}
	}

	@RestController
	@RequestMapping(INSTANCE + PARAGRAPH)
	final class ParagraphInstanceRestController {

		ParagraphInstanceRestController() {
			LOG.trace("Creating new Paragraph Instance REST Controller.");
		}

		@RequestMapping(method = GET, value = BY_PROJECT_ID + BY_CODE)
		final ResponseEntity<ParagraphInstance> findByProjectIdAndCode(
				@PathVariable(PARAM_PROJECT_ID) final String projectId, @PathVariable(PARAM_CODE) final String code) {
			final ParagraphInstance paragraph = _instances.findByProjectIdAndCode(projectId, code,
					ParagraphInstance.class);
			if (isNotNullOrEmpty(paragraph)) {
				return createResponseForSuccess(paragraph);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + PARAGRAPH, BY_PROJECT_ID + BY_CODE),
						paragraph);
			}
		}

		@RequestMapping(method = GET, value = BY_ID)
		final ResponseEntity<ParagraphInstance> findOne(@PathVariable(PARAM_ID) final Long id) {
			final ParagraphInstance paragraph = _instances.findOne(id, ParagraphInstance.class);
			if (isNotNullOrEmpty(paragraph)) {
				return createResponseForSuccess(paragraph);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + PARAGRAPH, BY_ID), paragraph);
			}
		}

		@RequestMapping(method = PUT)
		final ResponseEntity<ParagraphInstance> save(@RequestBody final ParagraphInstance paragraph) {
			final ParagraphInstance saved = _instances.save(paragraph);
			if (isNotNullOrEmpty(saved)) {
				return createResponseForSuccess(saved);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + PARAGRAPH), saved);
			}
		}
	}

	@RequestMapping(method = DELETE, value = BY_ID)
	final ResponseEntity<Instance> delete(@PathVariable(PARAM_ID) final Long id) {
		final boolean pass = _instances.delete(id);
		return ResponseEntity.status(pass ? OK : BAD_REQUEST).build();
	}

	@RequestMapping(method = DELETE, value = BY_PROJECT_ID + BY_CODE)
	final ResponseEntity<HttpStatus> delete(@PathVariable(PARAM_PROJECT_ID) String projectId,
			@PathVariable(PARAM_CODE) String code) {
		final boolean pass = _instances.delete(projectId, code);
		return new ResponseEntity<HttpStatus>(pass ? OK : BAD_REQUEST);
	}

	@RequestMapping(method = GET, value = INSTANCES)
	final ResponseEntity<Instances> findAll() {
		return new ResponseEntity<Instances>(_instances.findAll(Instances.class), OK);
	}

	@RequestMapping(method = GET, value = BY_PROJECT_ID + BY_CODE)
	final ResponseEntity<Instance> findByProjectIdAndCode(@PathVariable(PARAM_PROJECT_ID) final String projectId,
			@PathVariable(PARAM_CODE) final String code) {
		final Instance instance = _instances.findByProjectIdAndCode(projectId, code);
		if (isNotNullOrEmpty(instance)) {
			return createResponseForSuccess(instance);
		} else {
			return createResponseForNoContent(_utils.getURI(INSTANCE, BY_PROJECT_ID + BY_CODE), instance);
		}
	}

	@RequestMapping(method = GET, value = BY_PROJECT_ID + BY_CODE_LIKE)
	final ResponseEntity<Instances> findByProjectIdAndCodeLike(@PathVariable(PARAM_PROJECT_ID) final String projectId,
			@PathVariable(PARAM_LIKE) final String like) {
		final Instances instances = _instances.findByProjectIdAndCodeLike(projectId, like);
		if (isNotNullOrEmpty(instances)) {
			return createResponseForSuccess(instances);
		} else {
			return createResponseForNoContent(_utils.getURI(INSTANCE, BY_PROJECT_ID + BY_CODE_LIKE), instances);
		}
	}

	@RequestMapping(method = GET, value = BY_ID)
	final ResponseEntity<Instance> findOne(@PathVariable(PARAM_ID) final Long id) {
		final Instance instance = _instances.findOne(id);
		if (isNotNullOrEmpty(instance)) {
			return createResponseForSuccess(instance);
		} else {
			return createResponseForNoContent(_utils.getURI(INSTANCE, BY_ID), instance);
		}
	}

	@RequestMapping(method = PUT)
	final ResponseEntity<Instance> save(@RequestBody final Instance instance) {
		final Instance saved = _instances.save(instance);
		if (isNotNullOrEmpty(saved)) {
			return createResponseForSuccess(saved);
		} else {
			return createResponseForNoContent(_utils.getURI(_utils.getDestination(INSTANCE)), saved);
		}
	}

	@RequestMapping(method = PUT, value = INSTANCES)
	final ResponseEntity<Instances> save(@RequestBody final Instances instances) {
		final Instances saved = _instances.save(instances);
		if (isNotNullOrEmpty(saved)) {
			return createResponseForSuccess(saved);
		} else {
			return createResponseForNoContent(_utils.getURI(INSTANCE, INSTANCES), saved);
		}
	}
}