package com.itgfirm.docengine.instance;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itgfirm.docengine.types.AdvancedDocumentInstance;
import com.itgfirm.docengine.types.ClauseInstance;
import com.itgfirm.docengine.types.Instance;
import com.itgfirm.docengine.types.ParagraphInstance;
import com.itgfirm.docengine.types.SectionInstance;
import com.itgfirm.docengine.types.jpa.AdvancedDocumentInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.ClauseInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.InstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.Instances;
import com.itgfirm.docengine.types.jpa.ParagraphInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.SectionInstanceJpaImpl;
import com.itgfirm.docengine.web.RestUrls;

/**
 * @author Justin Scott
 * 
 *         Represents a RESTful endpoint for internal and external services to perform CRUD
 *         operations on Instance data. The controller "class" provides a standardized layer of
 *         abstraction, consumers need not "know" about the Instance Service.
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
class InstanceRestController {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(InstanceRestController.class);
	@Autowired
	private InstanceService service;

	/**
	 * Gets all Instance records.
	 * 
	 * @return All Instance records.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCES)
	ResponseEntity<List<Instance>> get() {
		return new ResponseEntity<List<Instance>>(service.get(), HttpStatus.OK);
	}

	/**
	 * Gets a single Instance based upon the provided ID.
	 * 
	 * @param id
	 * @return Instance record related to the provided ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.BY_ID)
	ResponseEntity<Instance> get(@PathVariable(RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<Instance>(service.get(id), HttpStatus.OK);
	}

	/**
	 * Gets a single Instance based upon the provided code.
	 * 
	 * @param projectId
	 * @param code
	 * @return Instance record related to the provided code.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE
			+ RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE)
	ResponseEntity<Instance> get(@PathVariable(RestUrls.PARAM_PROJECT_ID) String projectId,
			@PathVariable(RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<Instance>(service.get(projectId, code), HttpStatus.OK);
	}

	/**
	 * Gets the immediate child objects, if any, for the provided ID.
	 * 
	 * @param id
	 * @return List of child Instances
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.CHILDREN
			+ RestUrls.BY_ID)
	ResponseEntity<List<? extends Instance>> getChildren(@PathVariable(
			value = RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<List<? extends Instance>>(
				(List<Instance>) service.getChildren(id), HttpStatus.OK);
	}

	/**
	 * Gets all child objects, if any, for the provided ID.
	 * 
	 * @param id
	 * @return List of child Instances
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE
			+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_ID)
	ResponseEntity<List<? extends Instance>> getChildrenWithKids(@PathVariable(
			value = RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<List<? extends Instance>>(
				(List<Instance>) service.getChildren(id, true), HttpStatus.OK);
	}

	/**
	 * Gets the immediate child objects, if any, for the provided code and project ID.
	 * 
	 * @param projectId
	 * @param code
	 * @return List of child Instances
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.CHILDREN
			+ RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE)
	ResponseEntity<List<? extends Instance>> getChildren(@PathVariable(
			value = RestUrls.PARAM_PROJECT_ID) String projectId, @PathVariable(
			value = RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<List<? extends Instance>>(
				(List<Instance>) service.getChildren(projectId, code), HttpStatus.OK);
	}

	/**
	 * Gets all child objects, if any, for the provided code and project ID.
	 * 
	 * @param projectId
	 * @param code
	 * @return List of child Instances
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE
			+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE)
	ResponseEntity<List<? extends Instance>> getChildrenWithKids(@PathVariable(
			value = RestUrls.PARAM_PROJECT_ID) String projectId, @PathVariable(
			value = RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<List<? extends Instance>>(
				(List<Instance>) service.getChildren(projectId, code, true), HttpStatus.OK);
	}

	/**
	 * Gets the Document record for the provided ID.
	 * 
	 * @param id
	 * @return The Document record for the provided ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.DOCUMENT
			+ RestUrls.BY_ID)
	ResponseEntity<AdvancedDocumentInstance> getDocument(@PathVariable(RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<AdvancedDocumentInstance>((AdvancedDocumentInstance) service.get(id),
				HttpStatus.OK);
	}

	/**
	 * Gets the Document record, and children if any. for the provided ID.
	 * 
	 * @param id
	 * @return The Document record, and children if any, for the provided ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE
			+ RestUrls.DOCUMENT_EAGER_KIDS + RestUrls.BY_ID)
	ResponseEntity<AdvancedDocumentInstance> getDocumentWithKids(
			@PathVariable(RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<AdvancedDocumentInstance>((AdvancedDocumentInstance) service.get(id, true),
				HttpStatus.OK);
	}

	/**
	 * Gets the immediate children for the provided Document ID.
	 * 
	 * @param id
	 * @return Immediate children for the provided Document ID.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.DOCUMENT
			+ RestUrls.CHILDREN + RestUrls.BY_ID)
	ResponseEntity<List<SectionInstance>> getDocumentChildren(@PathVariable(
			value = RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<List<SectionInstance>>(
				(List<SectionInstance>) service.getChildren(id), HttpStatus.OK);
	}

	/**
	 * Gets all children for the provided Document ID.
	 * 
	 * @param id
	 * @return All children for the provided Document ID.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.DOCUMENT
			+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_ID)
	ResponseEntity<List<SectionInstance>> getDocumentChildrenWithKids(@PathVariable(
			value = RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<List<SectionInstance>>(
				(List<SectionInstance>) service.getChildren(id, true), HttpStatus.OK);
	}

	/**
	 * Gets the Document record for the provided code and project ID.
	 * 
	 * @param projectId
	 * @param code
	 * @return The Document record for the provided code and project ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.DOCUMENT
			+ RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE)
	ResponseEntity<AdvancedDocumentInstance> getDocument(
			@PathVariable(RestUrls.PARAM_PROJECT_ID) String projectId,
			@PathVariable(RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<AdvancedDocumentInstance>((AdvancedDocumentInstance) service.get(projectId,
				code), HttpStatus.OK);
	}

	/**
	 * Gets the Document record, and children if any, for the provided code and project ID.
	 * 
	 * @param projectId
	 * @param code
	 * @return The Document record, and children if any, for the provided code and project ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE
			+ RestUrls.DOCUMENT_EAGER_KIDS + RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE)
	ResponseEntity<AdvancedDocumentInstance> getDocumentWithKids(
			@PathVariable(RestUrls.PARAM_PROJECT_ID) String projectId,
			@PathVariable(RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<AdvancedDocumentInstance>((AdvancedDocumentInstance) service.get(projectId,
				code, true), HttpStatus.OK);
	}

	/**
	 * Gets the immediate children for the provided Document code and project ID.
	 * 
	 * @param projectId
	 * @param code
	 * @return The immediate children of the provided Document code and project ID.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.DOCUMENT
			+ RestUrls.CHILDREN + RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE)
	ResponseEntity<List<SectionInstance>> getDocumentChildren(@PathVariable(
			value = RestUrls.PARAM_PROJECT_ID) String projectId, @PathVariable(
			value = RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<List<SectionInstance>>(
				(List<SectionInstance>) service.getChildren(projectId, code), HttpStatus.OK);
	}

	/**
	 * Gets all children for the provided Document code and project ID.
	 * 
	 * @param projectId
	 * @param code
	 * @return All children for the provided Document code and project ID.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.DOCUMENT
			+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE)
	ResponseEntity<List<SectionInstance>> getDocumentChildrenWithKids(@PathVariable(
			value = RestUrls.PARAM_PROJECT_ID) String projectId, @PathVariable(
			value = RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<List<SectionInstance>>(
				(List<SectionInstance>) service.getChildren(projectId, code, true),
				HttpStatus.OK);
	}

	/**
	 * Gets the Section record for the provided Section ID.
	 * 
	 * @param id
	 * @return Section record for the provided Section ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.SECTION
			+ RestUrls.BY_ID)
	ResponseEntity<SectionInstance> getSection(@PathVariable(RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<SectionInstance>((SectionInstance) service.get(id),
				HttpStatus.OK);
	}

	/**
	 * Gets the Section, with children if any, record for the provided Section ID.
	 * 
	 * @param id
	 * @return Gets the Section, with children if any, record for the provided Section ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE
			+ RestUrls.SECTION_EAGER_KIDS + RestUrls.BY_ID)
	ResponseEntity<SectionInstance> getSectionWithKids(@PathVariable(RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<SectionInstance>((SectionInstance) service.get(id, true),
				HttpStatus.OK);
	}

	/**
	 * Gets the immediate children records for the provided Section ID.
	 * 
	 * @param id
	 * @return Section record for the provided Section ID.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.SECTION
			+ RestUrls.CHILDREN + RestUrls.BY_ID)
	ResponseEntity<List<ClauseInstance>> getSectionChildren(
			@PathVariable(RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<List<ClauseInstance>>(
				(List<ClauseInstance>) service.getChildren(id), HttpStatus.OK);
	}

	/**
	 * Gets all children records for the provided Section ID.
	 * 
	 * @param id
	 * @return Gets the Section, with children if any, record for the provided Section ID.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.SECTION
			+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_ID)
	ResponseEntity<List<ClauseInstance>> getSectionChildrenWithKids(
			@PathVariable(RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<List<ClauseInstance>>(
				(List<ClauseInstance>) service.getChildren(id, true), HttpStatus.OK);
	}

	/**
	 * Gets the Section record for the provided Section code and project ID.
	 * 
	 * @param projectId
	 * @param code
	 * @return Gets the Section record for the provided Section code and project ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.SECTION
			+ RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE)
	ResponseEntity<SectionInstance> getSection(
			@PathVariable(RestUrls.PARAM_PROJECT_ID) String projectId,
			@PathVariable(RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<SectionInstance>((SectionInstance) service.get(projectId,
				code), HttpStatus.OK);
	}

	/**
	 * Gets the Section, with children if any, record for the provided Section code and project
	 * ID.
	 * 
	 * @param id
	 * @return Gets the Section, with children if any, record for the provided Section code and
	 *         project ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE
			+ RestUrls.SECTION_EAGER_KIDS + RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE)
	ResponseEntity<SectionInstance> getSectionWithKids(
			@PathVariable(RestUrls.PARAM_PROJECT_ID) String projectId,
			@PathVariable(RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<SectionInstance>((SectionInstance) service.get(projectId,
				code, true), HttpStatus.OK);
	}

	/**
	 * Gets the immediate children records for the provided Section code and project ID.
	 * 
	 * @param id
	 * @return Section record for the provided Section code and project ID.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.SECTION
			+ RestUrls.CHILDREN + RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE)
	ResponseEntity<List<ClauseInstance>> getSectionChildren(
			@PathVariable(RestUrls.PARAM_PROJECT_ID) String projectId,
			@PathVariable(RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<List<ClauseInstance>>(
				(List<ClauseInstance>) service.getChildren(projectId, code), HttpStatus.OK);
	}

	/**
	 * Gets all children records for the provided Section code and project ID.
	 * 
	 * @param id
	 * @return Gets the Section, with children if any, record for the provided Section code and
	 *         project ID.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.SECTION
			+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE)
	ResponseEntity<List<ClauseInstance>> getSectionChildrenWithKids(
			@PathVariable(RestUrls.PARAM_PROJECT_ID) String projectId,
			@PathVariable(RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<List<ClauseInstance>>(
				(List<ClauseInstance>) service.getChildren(projectId, code, true),
				HttpStatus.OK);
	}

	/**
	 * Gets the Clause record for the provided Clause ID.
	 * 
	 * @param id
	 * @return Clause record for the provided Clause ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.CLAUSE
			+ RestUrls.BY_ID)
	ResponseEntity<ClauseInstance> getClause(@PathVariable(RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<ClauseInstance>((ClauseInstance) service.get(id),
				HttpStatus.OK);
	}

	/**
	 * Gets the Clause, with children if any, record for the provided Clause ID.
	 * 
	 * @param id
	 * @return Gets the Clause, with children if any, record for the provided Clause ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE
			+ RestUrls.CLAUSE_EAGER_KIDS + RestUrls.BY_ID)
	ResponseEntity<ClauseInstance> getClauseWithKids(@PathVariable(RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<ClauseInstance>((ClauseInstance) service.get(id, true),
				HttpStatus.OK);
	}

	/**
	 * Gets the immediate children records for the provided Clause ID.
	 * 
	 * @param id
	 * @return Clause record for the provided Clause ID.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.CLAUSE
			+ RestUrls.CHILDREN + RestUrls.BY_ID)
	ResponseEntity<List<ParagraphInstance>> getClauseChildren(
			@PathVariable(RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<List<ParagraphInstance>>(
				(List<ParagraphInstance>) service.getChildren(id), HttpStatus.OK);
	}

	/**
	 * Gets all children records for the provided Clause ID.
	 * 
	 * @param id
	 * @return Gets the Clause, with children if any, record for the provided Clause ID.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.CLAUSE
			+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_ID)
	ResponseEntity<List<ParagraphInstance>> getClauseChildrenWithKids(
			@PathVariable(RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<List<ParagraphInstance>>(
				(List<ParagraphInstance>) service.getChildren(id, true), HttpStatus.OK);
	}

	/**
	 * Gets the Clause record for the provided Clause code and project ID.
	 * 
	 * @param projectId
	 * @param code
	 * @return Gets the Clause record for the provided Clause code and project ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.CLAUSE
			+ RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE)
	ResponseEntity<ClauseInstance> getClause(
			@PathVariable(RestUrls.PARAM_PROJECT_ID) String projectId,
			@PathVariable(RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<ClauseInstance>(
				(ClauseInstance) service.get(projectId, code), HttpStatus.OK);
	}

	/**
	 * Gets the Clause, with children if any, record for the provided Clause code and project
	 * ID.
	 * 
	 * @param id
	 * @return Gets the Clause, with children if any, record for the provided Clause code and
	 *         project ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE
			+ RestUrls.CLAUSE_EAGER_KIDS + RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE)
	ResponseEntity<ClauseInstance> getClauseWithKids(
			@PathVariable(RestUrls.PARAM_PROJECT_ID) String projectId,
			@PathVariable(RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<ClauseInstance>((ClauseInstance) service.get(projectId,
				code, true), HttpStatus.OK);
	}

	/**
	 * Gets the immediate children records for the provided Clause code and project ID.
	 * 
	 * @param id
	 * @return Clause record for the provided Clause code and project ID.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.CLAUSE
			+ RestUrls.CHILDREN + RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE)
	ResponseEntity<List<ParagraphInstance>> getClauseChildren(
			@PathVariable(RestUrls.PARAM_PROJECT_ID) String projectId,
			@PathVariable(RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<List<ParagraphInstance>>(
				(List<ParagraphInstance>) service.getChildren(projectId, code), HttpStatus.OK);
	}

	/**
	 * Gets all children records for the provided Clause code and project ID.
	 * 
	 * @param id
	 * @return Gets the Clause, with children if any, record for the provided Clause code and
	 *         project ID.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.CLAUSE
			+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE)
	ResponseEntity<List<ParagraphInstance>> getClauseChildrenWithKids(
			@PathVariable(RestUrls.PARAM_PROJECT_ID) String projectId,
			@PathVariable(RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<List<ParagraphInstance>>(
				(List<ParagraphInstance>) service.getChildren(projectId, code, true),
				HttpStatus.OK);
	}

	/**
	 * Gets the Paragraph record for the provided Paragraph ID.
	 * 
	 * @param id
	 * @return Paragraph record for the provided Paragraph ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.PARAGRAPH
			+ RestUrls.BY_ID)
	ResponseEntity<ParagraphInstance> getParagraph(@PathVariable(RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<ParagraphInstance>((ParagraphInstance) service.get(id),
				HttpStatus.OK);
	}

	/**
	 * Gets the Paragraph record for the provided Paragraph code and project ID.
	 * 
	 * @param projectId
	 * @param code
	 * @return Gets the Clause record for the provided Paragraph code and project ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE + RestUrls.PARAGRAPH
			+ RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE)
	ResponseEntity<ParagraphInstance> getParagraph(
			@PathVariable(RestUrls.PARAM_PROJECT_ID) String projectId,
			@PathVariable(RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<ParagraphInstance>((ParagraphInstance) service.get(
				projectId, code), HttpStatus.OK);
	}

	/**
	 * Gets a list of Instance records based upon the provided project ID and content code
	 * search string.
	 * 
	 * @param projectId
	 * @param like
	 * @return Instance records related to the project ID and content code search string.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.INSTANCE
			+ RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE_LIKE)
	ResponseEntity<List<Instance>> getByProjectAndCodeLike(
			@PathVariable(RestUrls.PARAM_PROJECT_ID) String projectId,
			@PathVariable(RestUrls.PARAM_LIKE) String like) {
		return new ResponseEntity<List<Instance>>(service.getByProjectAndCodeLike(projectId,
				like), HttpStatus.OK);
	}

	/**
	 * Merges (adds or updates) a Instance record.
	 * 
	 * @param instance
	 * @return The newly merged Instance, with created ID if new.
	 */
	@RequestMapping(method = RequestMethod.PUT, value = RestUrls.INSTANCE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<InstanceJpaImpl> merge(@RequestBody InstanceJpaImpl instance) {
		return new ResponseEntity<InstanceJpaImpl>((InstanceJpaImpl) service.merge(instance),
				HttpStatus.OK);
	}

	/**
	 * Merges (adds or updates) a Document Instance record.
	 * 
	 * @param document
	 * @return The newly merged Document Instance, with created ID if new.
	 */
	@RequestMapping(method = RequestMethod.PUT, value = RestUrls.INSTANCE + RestUrls.DOCUMENT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<AdvancedDocumentInstanceJpaImpl> merge(@RequestBody AdvancedDocumentInstanceJpaImpl document) {
		return new ResponseEntity<AdvancedDocumentInstanceJpaImpl>(
				(AdvancedDocumentInstanceJpaImpl) service.merge(document), HttpStatus.OK);
	}

	/**
	 * Merges (adds or updates) a Section Instance record.
	 * 
	 * @param section
	 * @return The newly merged Section Instance, with created ID if new.
	 */
	@RequestMapping(method = RequestMethod.PUT, value = RestUrls.INSTANCE + RestUrls.SECTION,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<SectionInstanceJpaImpl> merge(@RequestBody SectionInstanceJpaImpl section) {
		return new ResponseEntity<SectionInstanceJpaImpl>(
				(SectionInstanceJpaImpl) service.merge(section), HttpStatus.OK);
	}

	/**
	 * Merges (adds or updates) a Clause Instance record.
	 * 
	 * @param clause
	 * @return The newly merged Clause Instance, with created ID if new.
	 */
	@RequestMapping(method = RequestMethod.PUT, value = RestUrls.INSTANCE + RestUrls.CLAUSE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ClauseInstanceJpaImpl> merge(@RequestBody ClauseInstanceJpaImpl clause) {
		return new ResponseEntity<ClauseInstanceJpaImpl>(
				(ClauseInstanceJpaImpl) service.merge(clause), HttpStatus.OK);
	}

	/**
	 * Merges (adds or updates) a Paragraph Instance record.
	 * 
	 * @param paragraph
	 * @return The newly merged Paragraph Instance, with created ID if new.
	 */
	@RequestMapping(method = RequestMethod.PUT,
			value = RestUrls.INSTANCE + RestUrls.PARAGRAPH,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ParagraphInstanceJpaImpl> merge(
			@RequestBody ParagraphInstanceJpaImpl paragraph) {
		return new ResponseEntity<ParagraphInstanceJpaImpl>(
				(ParagraphInstanceJpaImpl) service.merge(paragraph), HttpStatus.OK);
	}

	/**
	 * Merges (adds or updates) a list of Instances.
	 * 
	 * @param instance
	 * @return The newly merged list of items.
	 */
	@RequestMapping(method = RequestMethod.PUT, value = RestUrls.INSTANCES,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Instances> merge(@RequestBody Instances instance) {
		return new ResponseEntity<Instances>(new Instances(service.merge(instance
				.getInstances())), HttpStatus.OK);
	}

	/**
	 * Deletes a Instance record based upon the provided ID.
	 * 
	 * @param id
	 * @return True or False based upon success of the operation.
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = RestUrls.INSTANCE + RestUrls.BY_ID)
	ResponseEntity<Boolean> delete(@PathVariable(value = RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<Boolean>(service.delete(id), HttpStatus.OK);
	}

	/**
	 * Deletes a Instance record based upon the provided content code and project ID.
	 * 
	 * @param projectId
	 * @param code
	 * @return True or False based upon success of the operation.
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = RestUrls.INSTANCE
			+ RestUrls.BY_CODE)
	ResponseEntity<Boolean> delete(
			@PathVariable(value = RestUrls.PARAM_PROJECT_ID) String projectId, @PathVariable(
					value = RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<Boolean>(service.delete(projectId, code), HttpStatus.OK);
	}
}