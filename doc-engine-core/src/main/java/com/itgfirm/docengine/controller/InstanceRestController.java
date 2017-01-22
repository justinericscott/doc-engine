package com.itgfirm.docengine.controller;

import static com.itgfirm.docengine.controller.RestUtils.createResponseForNoContent;
import static com.itgfirm.docengine.controller.RestUtils.createResponseForSuccess;
import static com.itgfirm.docengine.controller.RestUtils.RestConstants.*;
import static com.itgfirm.docengine.util.Utils.isNotNullOrEmpty;
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

import com.itgfirm.docengine.service.content.InstanceService;
import com.itgfirm.docengine.types.DocumentInstanceJpaImpl;
import com.itgfirm.docengine.types.ClauseInstanceJpaImpl;
import com.itgfirm.docengine.types.ClauseInstances;
import com.itgfirm.docengine.types.InstanceJpaImpl;
import com.itgfirm.docengine.types.Instances;
import com.itgfirm.docengine.types.ParagraphInstanceJpaImpl;
import com.itgfirm.docengine.types.ParagraphInstances;
import com.itgfirm.docengine.types.SectionInstanceJpaImpl;
import com.itgfirm.docengine.types.SectionInstances;

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
		LOG.info("Creating new Instance REST Controller.");
	}

	@RestController
	@RequestMapping(INSTANCE + DOCUMENT)
	final class DocumentInstanceRestController {

		DocumentInstanceRestController() {
			LOG.info("Creating new Document Instance REST Controller.");
		}

		@RequestMapping(method = GET, value = BY_PROJECT_ID + BY_CODE)
		final ResponseEntity<DocumentInstanceJpaImpl> findByProjectIdAndCode(
				@PathVariable(PARAM_PROJECT_ID) final String projectId, @PathVariable(PARAM_CODE) final String code) {
			final DocumentInstanceJpaImpl document = _instances.findByProjectIdAndCode(projectId, code,
					DocumentInstanceJpaImpl.class);
			if (isNotNullOrEmpty(document)) {
				return createResponseForSuccess(document);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + DOCUMENT, BY_PROJECT_ID + BY_CODE), document);
			}
		}

		@RequestMapping(method = GET, value = BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS)
		final ResponseEntity<DocumentInstanceJpaImpl> findByProjectIdAndCode(
				@PathVariable(PARAM_PROJECT_ID) final String projectId, @PathVariable(PARAM_CODE) final String code,
				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final DocumentInstanceJpaImpl document = _instances.findByProjectIdAndCode(projectId, code,
					DocumentInstanceJpaImpl.class, eagerKids);
			if (isNotNullOrEmpty(document)) {
				return createResponseForSuccess(document);
			} else {
				return createResponseForNoContent(
						_utils.getURI(INSTANCE + DOCUMENT, BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS), document);
			}
		}

		@RequestMapping(method = GET, value = BY_ID)
		final ResponseEntity<DocumentInstanceJpaImpl> findOne(@PathVariable(PARAM_ID) final Long id) {
			final DocumentInstanceJpaImpl document = _instances.findOne(id, DocumentInstanceJpaImpl.class);
			if (isNotNullOrEmpty(document)) {
				return createResponseForSuccess(document);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + DOCUMENT, BY_ID), document);
			}
		}

		@RequestMapping(method = GET, value = BY_ID + IS_EAGER_KIDS)
		final ResponseEntity<DocumentInstanceJpaImpl> findOne(@PathVariable(PARAM_ID) final Long id,
				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			LOG.debug("Received request for {} with children.", DocumentInstanceJpaImpl.class.getSimpleName());
			final DocumentInstanceJpaImpl document = _instances.findOne(id, DocumentInstanceJpaImpl.class, eagerKids);
			if (isNotNullOrEmpty(document)) {
				return createResponseForSuccess(document);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + DOCUMENT, BY_ID + IS_EAGER_KIDS), document);
			}
		}

		@RequestMapping(method = GET, value = CHILDREN + BY_ID)
		final ResponseEntity<SectionInstances> getChildren(@PathVariable(PARAM_ID) final Long id) {
			final SectionInstances sections = _instances.getChildren(id, SectionInstances.class);
			if (isNotNullOrEmpty(sections)) {
				return createResponseForSuccess(sections);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + DOCUMENT, CHILDREN + BY_ID), sections);
			}
		}

		@RequestMapping(method = GET, value = CHILDREN + BY_ID + IS_EAGER_KIDS)
		final ResponseEntity<SectionInstances> getChildren(@PathVariable(PARAM_ID) final Long id,
				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final SectionInstances sections = _instances.getChildren(id, SectionInstances.class, eagerKids);
			if (isNotNullOrEmpty(sections)) {
				return createResponseForSuccess(sections);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + DOCUMENT, CHILDREN + BY_ID + IS_EAGER_KIDS),
						sections);
			}
		}

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

		@RequestMapping(method = GET, value = CHILDREN + BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS)
		final ResponseEntity<SectionInstances> getChildren(@PathVariable(PARAM_PROJECT_ID) final String projectId,
				@PathVariable(PARAM_CODE) final String code, @PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final SectionInstances sections = _instances.getChildren(projectId, code, SectionInstances.class,
					eagerKids);
			if (isNotNullOrEmpty(sections)) {
				return createResponseForSuccess(sections);
			} else {
				return createResponseForNoContent(
						_utils.getURI(INSTANCE + DOCUMENT, CHILDREN + BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS),
						sections);
			}
		}

		@RequestMapping(method = PUT)
		final ResponseEntity<DocumentInstanceJpaImpl> save(@RequestBody final DocumentInstanceJpaImpl document) {
			final DocumentInstanceJpaImpl saved = _instances.save(document);
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
			LOG.info("Creating new Section Instance REST Controller.");
		}

		@RequestMapping(method = GET, value = BY_PROJECT_ID + BY_CODE)
		final ResponseEntity<SectionInstanceJpaImpl> findByProjectIdAndCode(
				@PathVariable(PARAM_PROJECT_ID) final String projectId, @PathVariable(PARAM_CODE) final String code) {
			final SectionInstanceJpaImpl section = _instances.findByProjectIdAndCode(projectId, code,
					SectionInstanceJpaImpl.class);
			if (isNotNullOrEmpty(section)) {
				return createResponseForSuccess(section);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + SECTION, BY_PROJECT_ID + BY_CODE), section);
			}
		}

		@RequestMapping(method = GET, value = BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS)
		final ResponseEntity<SectionInstanceJpaImpl> findByProjectIdAndCode(
				@PathVariable(PARAM_PROJECT_ID) final String projectId, @PathVariable(PARAM_CODE) final String code,
				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final SectionInstanceJpaImpl section = _instances.findByProjectIdAndCode(projectId, code,
					SectionInstanceJpaImpl.class, eagerKids);
			if (isNotNullOrEmpty(section)) {
				return createResponseForSuccess(section);
			} else {
				return createResponseForNoContent(
						_utils.getURI(INSTANCE + SECTION, BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS), section);
			}
		}

		@RequestMapping(method = GET, value = BY_ID)
		final ResponseEntity<SectionInstanceJpaImpl> findOne(@PathVariable(PARAM_ID) final Long id) {
			final SectionInstanceJpaImpl section = _instances.findOne(id, SectionInstanceJpaImpl.class);
			if (isNotNullOrEmpty(section)) {
				return createResponseForSuccess(section);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + SECTION, BY_ID), section);
			}
		}

		@RequestMapping(method = GET, value = BY_ID + IS_EAGER_KIDS)
		final ResponseEntity<SectionInstanceJpaImpl> findOne(@PathVariable(PARAM_ID) final Long id,
				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final SectionInstanceJpaImpl section = _instances.findOne(id, SectionInstanceJpaImpl.class, eagerKids);
			if (isNotNullOrEmpty(section)) {
				return createResponseForSuccess(section);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + SECTION, BY_ID + IS_EAGER_KIDS), section);
			}
		}

		@RequestMapping(method = GET, value = CHILDREN + BY_ID)
		final ResponseEntity<ClauseInstances> getChildren(@PathVariable(PARAM_ID) final Long id) {
			final ClauseInstances clauses = _instances.getChildren(id, ClauseInstances.class);
			if (isNotNullOrEmpty(clauses)) {
				return createResponseForSuccess(clauses);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + SECTION, CHILDREN + BY_ID), clauses);
			}
		}

		@RequestMapping(method = GET, value = CHILDREN + BY_ID + IS_EAGER_KIDS)
		final ResponseEntity<ClauseInstances> getChildren(@PathVariable(PARAM_ID) final Long id,
				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final ClauseInstances clauses = _instances.getChildren(id, ClauseInstances.class, eagerKids);
			if (isNotNullOrEmpty(clauses)) {
				return createResponseForSuccess(clauses);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + SECTION, CHILDREN + BY_ID + IS_EAGER_KIDS),
						clauses);
			}
		}

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

		@RequestMapping(method = GET, value = CHILDREN + BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS)
		final ResponseEntity<ClauseInstances> getChildren(@PathVariable(PARAM_PROJECT_ID) final String projectId,
				@PathVariable(PARAM_CODE) final String code, @PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final ClauseInstances clauses = _instances.getChildren(projectId, code, ClauseInstances.class, eagerKids);
			if (isNotNullOrEmpty(clauses)) {
				return createResponseForSuccess(clauses);
			} else {
				return createResponseForNoContent(
						_utils.getURI(INSTANCE + SECTION, CHILDREN + BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS), clauses);
			}
		}

		@RequestMapping(method = PUT)
		final ResponseEntity<SectionInstanceJpaImpl> save(@RequestBody final SectionInstanceJpaImpl section) {
			final SectionInstanceJpaImpl saved = _instances.save(section);
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
			LOG.info("Creating new Clause Instance REST Controller.");
		}

		@RequestMapping(method = GET, value = BY_PROJECT_ID + BY_CODE)
		final ResponseEntity<ClauseInstanceJpaImpl> findByProjectIdAndCode(
				@PathVariable(PARAM_PROJECT_ID) final String projectId, @PathVariable(PARAM_CODE) final String code) {
			final ClauseInstanceJpaImpl clause = _instances.findByProjectIdAndCode(projectId, code,
					ClauseInstanceJpaImpl.class);
			if (isNotNullOrEmpty(clause)) {
				return createResponseForSuccess(clause);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + CLAUSE, BY_PROJECT_ID + BY_CODE), clause);
			}
		}

		@RequestMapping(method = GET, value = BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS)
		final ResponseEntity<ClauseInstanceJpaImpl> findByProjectIdAndCode(
				@PathVariable(PARAM_PROJECT_ID) final String projectId, @PathVariable(PARAM_CODE) final String code,
				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final ClauseInstanceJpaImpl clause = _instances.findByProjectIdAndCode(projectId, code,
					ClauseInstanceJpaImpl.class, eagerKids);
			if (isNotNullOrEmpty(clause)) {
				return createResponseForSuccess(clause);
			} else {
				return createResponseForNoContent(
						_utils.getURI(INSTANCE + CLAUSE, BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS), clause);
			}
		}

		@RequestMapping(method = GET, value = BY_ID)
		final ResponseEntity<ClauseInstanceJpaImpl> findOne(@PathVariable(PARAM_ID) final Long id) {
			final ClauseInstanceJpaImpl clause = _instances.findOne(id, ClauseInstanceJpaImpl.class);
			if (isNotNullOrEmpty(clause)) {
				return createResponseForSuccess(clause);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + CLAUSE, BY_ID), clause);
			}
		}

		@RequestMapping(method = GET, value = BY_ID + IS_EAGER_KIDS)
		final ResponseEntity<ClauseInstanceJpaImpl> findOne(@PathVariable(PARAM_ID) final Long id,
				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final ClauseInstanceJpaImpl clause = _instances.findOne(id, ClauseInstanceJpaImpl.class, eagerKids);
			if (isNotNullOrEmpty(clause)) {
				return createResponseForSuccess(clause);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + CLAUSE, BY_ID + IS_EAGER_KIDS), clause);
			}
		}

		@RequestMapping(method = GET, value = CHILDREN + BY_ID)
		final ResponseEntity<ParagraphInstances> getChildren(@PathVariable(PARAM_ID) final Long id) {
			final ParagraphInstances paragraphs = _instances.getChildren(id, ParagraphInstances.class);
			if (isNotNullOrEmpty(paragraphs)) {
				return createResponseForSuccess(paragraphs);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + CLAUSE, CHILDREN + BY_ID), paragraphs);
			}
		}

		@RequestMapping(method = GET, value = CHILDREN + BY_ID + IS_EAGER_KIDS)
		final ResponseEntity<ParagraphInstances> getChildren(@PathVariable(PARAM_ID) final Long id,
				@PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final ParagraphInstances paragraphs = _instances.getChildren(id, ParagraphInstances.class, eagerKids);
			if (isNotNullOrEmpty(paragraphs)) {
				return createResponseForSuccess(paragraphs);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + CLAUSE, CHILDREN + BY_ID + IS_EAGER_KIDS),
						paragraphs);
			}
		}

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

		@RequestMapping(method = GET, value = CHILDREN + BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS)
		final ResponseEntity<ParagraphInstances> getChildren(@PathVariable(PARAM_PROJECT_ID) final String projectId,
				@PathVariable(PARAM_CODE) final String code, @PathVariable(PARAM_EAGER_KIDS) final Boolean eagerKids) {
			final ParagraphInstances paragraphs = _instances.getChildren(projectId, code, ParagraphInstances.class,
					eagerKids);
			if (isNotNullOrEmpty(paragraphs)) {
				return createResponseForSuccess(paragraphs);
			} else {
				return createResponseForNoContent(
						_utils.getURI(INSTANCE + CLAUSE, CHILDREN + BY_PROJECT_ID + BY_CODE + IS_EAGER_KIDS),
						paragraphs);
			}
		}

		@RequestMapping(method = PUT)
		final ResponseEntity<ClauseInstanceJpaImpl> save(@RequestBody final ClauseInstanceJpaImpl clause) {
			final ClauseInstanceJpaImpl saved = _instances.save(clause);
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
			LOG.info("Creating new Paragraph Instance REST Controller.");
		}

		@RequestMapping(method = GET, value = BY_PROJECT_ID + BY_CODE)
		final ResponseEntity<ParagraphInstanceJpaImpl> findByProjectIdAndCode(
				@PathVariable(PARAM_PROJECT_ID) final String projectId, @PathVariable(PARAM_CODE) final String code) {
			final ParagraphInstanceJpaImpl paragraph = _instances.findByProjectIdAndCode(projectId, code,
					ParagraphInstanceJpaImpl.class);
			if (isNotNullOrEmpty(paragraph)) {
				return createResponseForSuccess(paragraph);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + PARAGRAPH, BY_PROJECT_ID + BY_CODE),
						paragraph);
			}
		}

		@RequestMapping(method = GET, value = BY_ID)
		final ResponseEntity<ParagraphInstanceJpaImpl> findOne(@PathVariable(PARAM_ID) final Long id) {
			final ParagraphInstanceJpaImpl paragraph = _instances.findOne(id, ParagraphInstanceJpaImpl.class);
			if (isNotNullOrEmpty(paragraph)) {
				return createResponseForSuccess(paragraph);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + PARAGRAPH, BY_ID), paragraph);
			}
		}

		@RequestMapping(method = PUT)
		final ResponseEntity<ParagraphInstanceJpaImpl> save(@RequestBody final ParagraphInstanceJpaImpl paragraph) {
			final ParagraphInstanceJpaImpl saved = _instances.save(paragraph);
			if (isNotNullOrEmpty(saved)) {
				return createResponseForSuccess(saved);
			} else {
				return createResponseForNoContent(_utils.getURI(INSTANCE + PARAGRAPH), saved);
			}
		}
	}

	@RequestMapping(method = DELETE, value = BY_ID)
	final ResponseEntity<?> delete(@PathVariable(PARAM_ID) final Long id) {
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
		return new ResponseEntity<Instances>(_instances.findAll(), OK);
	}

	@RequestMapping(method = GET, value = BY_PROJECT_ID + BY_CODE)
	final ResponseEntity<InstanceJpaImpl> findByProjectIdAndCode(@PathVariable(PARAM_PROJECT_ID) final String projectId,
			@PathVariable(PARAM_CODE) final String code) {
		final InstanceJpaImpl instance = _instances.findByProjectIdAndCode(projectId, code);
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
	final ResponseEntity<InstanceJpaImpl> findOne(@PathVariable(PARAM_ID) final Long id) {
		final InstanceJpaImpl instance = _instances.findOne(id);
		if (isNotNullOrEmpty(instance)) {
			return createResponseForSuccess(instance);
		} else {
			return createResponseForNoContent(_utils.getURI(INSTANCE, BY_ID), instance);
		}
	}

	@RequestMapping(method = PUT)
	final ResponseEntity<InstanceJpaImpl> save(@RequestBody final InstanceJpaImpl instance) {
		final InstanceJpaImpl saved = _instances.save(instance);
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