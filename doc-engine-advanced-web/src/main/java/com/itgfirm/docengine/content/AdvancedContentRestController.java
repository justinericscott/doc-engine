package com.itgfirm.docengine.content;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itgfirm.docengine.types.AdvancedDocument;
import com.itgfirm.docengine.types.Clause;
import com.itgfirm.docengine.types.Content;
//import com.itgfirm.docengine.types.Document;
import com.itgfirm.docengine.types.Paragraph;
import com.itgfirm.docengine.types.Section;
import com.itgfirm.docengine.types.jpa.AdvancedDocumentJpaImpl;
import com.itgfirm.docengine.types.jpa.ClauseJpaImpl;
import com.itgfirm.docengine.types.jpa.ParagraphJpaImpl;
import com.itgfirm.docengine.types.jpa.SectionJpaImpl;
import com.itgfirm.docengine.web.RestUrls;

/**
 * @author Justin Scott
 * 
 *         Represents a RESTful endpoint for internal and external services to perform CRUD
 *         operations on Content data. The controller "class" provides a standardized layer of
 *         abstraction, consumers need not "know" about the Content Service.
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
final class AdvancedContentRestController {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(AdvancedContentRestController.class);
	@Autowired @Qualifier("advanced")
	private AdvancedContentService service;

	/**
	 * Gets the immediate child objects, if any, for the provided ID.
	 * 
	 * @param id
	 * @return List of child Contents
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.CHILDREN
			+ RestUrls.BY_ID)
	ResponseEntity<List<? extends Content>> getChildren(@PathVariable(
			value = RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<List<? extends Content>>(service.getChildren(id),
				HttpStatus.OK);
	}

	/**
	 * Gets all child objects, if any, for the provided ID.
	 * 
	 * @param id
	 * @return List of child Contents
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT
			+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_ID)
	ResponseEntity<List<? extends Content>> getChildrenWithKids(@PathVariable(
			value = RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<List<? extends Content>>(service.getChildren(id, true),
				HttpStatus.OK);
	}

	/**
	 * Gets the immediate child objects, if any, for the provided code.
	 * 
	 * @param code
	 * @return List of child Contents
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.CHILDREN
			+ RestUrls.BY_CODE)
	ResponseEntity<List<? extends Content>> getChildren(@PathVariable(
			value = RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<List<? extends Content>>(service.getChildren(code),
				HttpStatus.OK);
	}

	/**
	 * Gets all child objects, if any, for the provided code.
	 * 
	 * @param code
	 * @return List of child Contents
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT
			+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_CODE)
	ResponseEntity<List<? extends Content>> getChildrenWithKids(@PathVariable(
			value = RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<List<? extends Content>>(service.getChildren(code, true),
				HttpStatus.OK);
	}

	/**
	 * Gets the Document record for the provided ID.
	 * 
	 * @param id
	 * @return The Document record for the provided ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.DOCUMENT
			+ RestUrls.BY_ID)
	ResponseEntity<AdvancedDocument> getDocument(@PathVariable(RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<AdvancedDocument>((AdvancedDocument) service.get(id), HttpStatus.OK);
	}

	/**
	 * Gets the Document record, and children if any. for the provided ID.
	 * 
	 * @param id
	 * @return The Document record, and children if any, for the provided ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT
			+ RestUrls.DOCUMENT_EAGER_KIDS + RestUrls.BY_ID)
	ResponseEntity<AdvancedDocument> getDocumentWithKids(@PathVariable(RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<AdvancedDocument>((AdvancedDocument) service.get(id, true), HttpStatus.OK);
	}

	/**
	 * Gets the immediate children for the provided Document ID.
	 * 
	 * @param id
	 * @return The immediate children of the provided Document ID.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.DOCUMENT
			+ RestUrls.CHILDREN + RestUrls.BY_ID)
	ResponseEntity<List<Section>> getDocumentChildren(
			@PathVariable(value = RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<List<Section>>((List<Section>) service.getChildren(id),
				HttpStatus.OK);
	}

	/**
	 * Gets all children for the provided Document ID.
	 * 
	 * @param id
	 * @return All children for the provided Document ID.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.DOCUMENT
			+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_ID)
	ResponseEntity<List<Section>> getDocumentChildrenWithKids(@PathVariable(
			value = RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<List<Section>>(
				(List<Section>) service.getChildren(id, true), HttpStatus.OK);
	}

	/**
	 * Gets the Document record for the provided code.
	 * 
	 * @param code
	 * @return The Document record for the provided code.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.DOCUMENT
			+ RestUrls.BY_CODE)
	ResponseEntity<AdvancedDocument> getDocument(@PathVariable(RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<AdvancedDocument>((AdvancedDocument) service.get(code), HttpStatus.OK);
	}

	/**
	 * Gets the Document record, and children if any, for the provided code.
	 * 
	 * @param code
	 * @return The Document record for the provided code.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT
			+ RestUrls.DOCUMENT_EAGER_KIDS + RestUrls.BY_CODE)
	ResponseEntity<AdvancedDocument> getDocumentWithKids(@PathVariable(RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<AdvancedDocument>((AdvancedDocument) service.get(code, true), HttpStatus.OK);
	}

	/**
	 * Gets the immediate children for the provided Document code.
	 * 
	 * @param code
	 * @return Immediate children for the provided Document code.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.DOCUMENT
			+ RestUrls.CHILDREN + RestUrls.BY_CODE)
	ResponseEntity<List<Section>> getDocumentChildren(@PathVariable(
			value = RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<List<Section>>((List<Section>) service.getChildren(code),
				HttpStatus.OK);
	}

	/**
	 * Gets all children for the provided Document code.
	 * 
	 * @param code
	 * @return All children for the provided Document code.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.DOCUMENT
			+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_CODE)
	ResponseEntity<List<Section>> getDocumentChildrenWithKids(@PathVariable(
			value = RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<List<Section>>((List<Section>) service.getChildren(code,
				true), HttpStatus.OK);
	}

	/**
	 * Gets the Section record for the provided Section ID.
	 * 
	 * @param id
	 * @return Section record for the provided Section ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.SECTION
			+ RestUrls.BY_ID)
	ResponseEntity<Section> getSection(@PathVariable(value = RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<Section>((Section) service.get(id), HttpStatus.OK);
	}

	/**
	 * Gets the Section, with children if any, record for the provided Section ID.
	 * 
	 * @param id
	 * @return Gets the Section, with children if any, record for the provided Section ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT
			+ RestUrls.SECTION_EAGER_KIDS + RestUrls.BY_ID)
	ResponseEntity<Section> getSectionWithKids(@PathVariable(value = RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<Section>((Section) service.get(id, true), HttpStatus.OK);
	}

	/**
	 * Gets the immediate children records for the provided Section ID.
	 * 
	 * @param id
	 * @return Section record for the provided Section ID.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.SECTION
			+ RestUrls.CHILDREN + RestUrls.BY_ID)
	ResponseEntity<List<Clause>> getSectionChildren(
			@PathVariable(value = RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<List<Clause>>((List<Clause>) service.getChildren(id),
				HttpStatus.OK);
	}

	/**
	 * Gets all children records for the provided Section ID.
	 * 
	 * @param id
	 * @return Gets the Section, with children if any, record for the provided Section ID.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.SECTION
			+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_ID)
	ResponseEntity<List<Clause>> getSectionChildrenWithKids(@PathVariable(
			value = RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<List<Clause>>((List<Clause>) service.getChildren(id, true),
				HttpStatus.OK);
	}

	/**
	 * Gets the Section record for the provided Section code.
	 * 
	 * @param code
	 * @return Gets the Section record for the provided Section code.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.SECTION
			+ RestUrls.BY_CODE)
	ResponseEntity<Section> getSection(@PathVariable(value = RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<Section>((Section) service.get(code), HttpStatus.OK);
	}

	/**
	 * Gets the Section, with children if any, record for the provided Section code.
	 * 
	 * @param code
	 * @return The Section and all children, if any.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT
			+ RestUrls.SECTION_EAGER_KIDS + RestUrls.BY_CODE)
	ResponseEntity<Section> getSectionWithKids(
			@PathVariable(value = RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<Section>((Section) service.get(code, true), HttpStatus.OK);
	}

	/**
	 * Gets the immediate children records for the provided Section code.
	 * 
	 * @param code
	 * @return Section record for the provided Section code.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.SECTION
			+ RestUrls.CHILDREN + RestUrls.BY_CODE)
	ResponseEntity<List<Clause>> getSectionChildren(
			@PathVariable(value = RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<List<Clause>>((List<Clause>) service.getChildren(code),
				HttpStatus.OK);
	}

	/**
	 * Gets all children records for the provided Section code.
	 * 
	 * @param code
	 * @return Gets the Section, with children if any, record for the provided Section code.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.SECTION
			+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_CODE)
	ResponseEntity<List<Clause>> getSectionChildrenWithKids(@PathVariable(
			value = RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<List<Clause>>(
				(List<Clause>) service.getChildren(code, true), HttpStatus.OK);
	}

	/**
	 * Gets the Clause record for the provided Clause ID.
	 * 
	 * @param id
	 * @return Clause record for the provided Clause ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.CLAUSE
			+ RestUrls.BY_ID)
	ResponseEntity<Clause> getClause(@PathVariable(value = RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<Clause>((Clause) service.get(id), HttpStatus.OK);
	}

	/**
	 * Gets the Clause, with children if any, record for the provided Clause ID.
	 * 
	 * @param id
	 * @return Gets the Clause, with children if any, record for the provided Clause ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT
			+ RestUrls.CLAUSE_EAGER_KIDS + RestUrls.BY_ID)
	ResponseEntity<Clause> getClauseWithKids(@PathVariable(value = RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<Clause>((Clause) service.get(id, true), HttpStatus.OK);
	}

	/**
	 * Gets the immediate children records for the provided Clause ID.
	 * 
	 * @param id
	 * @return Clause record for the provided Clause ID.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.CLAUSE
			+ RestUrls.CHILDREN + RestUrls.BY_ID)
	ResponseEntity<List<Paragraph>> getClauseChildren(
			@PathVariable(value = RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<List<Paragraph>>((List<Paragraph>) service.getChildren(id),
				HttpStatus.OK);
	}

	/**
	 * Gets all children records for the provided Clause ID.
	 * 
	 * @param id
	 * @return Gets the Clause, with children if any, record for the provided Clause ID.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.CLAUSE
			+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_ID)
	ResponseEntity<List<Paragraph>> getClauseChildrenWithKids(@PathVariable(
			value = RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<List<Paragraph>>((List<Paragraph>) service.getChildren(id,
				true), HttpStatus.OK);
	}

	/**
	 * Gets the Clause record for the provided Clause code.
	 * 
	 * @param code
	 * @return Gets the Clause record for the provided Clause code and project ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.CLAUSE
			+ RestUrls.BY_CODE)
	ResponseEntity<Clause> getClause(@PathVariable(value = RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<Clause>((Clause) service.get(code), HttpStatus.OK);
	}

	/**
	 * Gets the Clause, with children if any, record for the provided Clause code.
	 * 
	 * @param code
	 * @return Gets the Clause, with children if any, record for the provided Clause code.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT
			+ RestUrls.CLAUSE_EAGER_KIDS + RestUrls.BY_CODE)
	ResponseEntity<Clause> getClauseWithKids(
			@PathVariable(value = RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<Clause>((Clause) service.get(code, true), HttpStatus.OK);
	}

	/**
	 * Gets the immediate children records for the provided Clause code.
	 * 
	 * @param code
	 * @return Clause record for the provided Clause code and project ID.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.CLAUSE
			+ RestUrls.CHILDREN + RestUrls.BY_CODE)
	ResponseEntity<List<Paragraph>> getClauseChildren(@PathVariable(
			value = RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<List<Paragraph>>(
				(List<Paragraph>) service.getChildren(code), HttpStatus.OK);
	}

	/**
	 * Gets all children records for the provided Clause code and project ID.
	 * 
	 * @param code
	 * @return Gets the Clause, with children if any, record for the provided Clause code and
	 *         project ID.
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.CLAUSE
			+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_CODE)
	ResponseEntity<List<Paragraph>> getClauseChildrenWithKids(@PathVariable(
			value = RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<List<Paragraph>>((List<Paragraph>) service.getChildren(code,
				true), HttpStatus.OK);
	}

	/**
	 * Gets the Paragraph record for the provided Paragraph ID.
	 * 
	 * @param id
	 * @return Paragraph record for the provided Paragraph ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.PARAGRAPH
			+ RestUrls.BY_ID)
	ResponseEntity<Paragraph> getParagraph(@PathVariable(value = RestUrls.PARAM_ID) Long id) {
		return new ResponseEntity<Paragraph>((Paragraph) service.get(id), HttpStatus.OK);
	}

	/**
	 * Gets the Paragraph record for the provided Paragraph code.
	 * 
	 * @param code
	 * @return Gets the Clause record for the provided Paragraph code and project ID.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RestUrls.CONTENT + RestUrls.PARAGRAPH
			+ RestUrls.BY_CODE)
	ResponseEntity<Paragraph> getParagraph(
			@PathVariable(value = RestUrls.PARAM_CODE) String code) {
		return new ResponseEntity<Paragraph>((Paragraph) service.get(code), HttpStatus.OK);
	}

	/**
	 * Merges (adds or updates) a Document record.
	 * 
	 * @param document
	 * @return The newly merged Document, with created ID if new.
	 */
	@RequestMapping(method = RequestMethod.PUT, value = RestUrls.CONTENT + RestUrls.DOCUMENT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<AdvancedDocumentJpaImpl> merge(@RequestBody AdvancedDocumentJpaImpl document) {
		return new ResponseEntity<AdvancedDocumentJpaImpl>((AdvancedDocumentJpaImpl) service.merge(document),
				HttpStatus.OK);
	}

	/**
	 * Merges (adds or updates) a Section record.
	 * 
	 * @param section
	 * @return The newly merged Section, with created ID if new.
	 */
	@RequestMapping(method = RequestMethod.PUT, value = RestUrls.CONTENT + RestUrls.SECTION,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<SectionJpaImpl> merge(@RequestBody SectionJpaImpl section) {
		return new ResponseEntity<SectionJpaImpl>((SectionJpaImpl) service.merge(section),
				HttpStatus.OK);
	}

	/**
	 * Merges (adds or updates) a Clause record.
	 * 
	 * @param clause
	 * @return The newly merged Clause, with created ID if new.
	 */
	@RequestMapping(method = RequestMethod.PUT, value = RestUrls.CONTENT + RestUrls.CLAUSE,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ClauseJpaImpl> merge(@RequestBody ClauseJpaImpl clause) {
		return new ResponseEntity<ClauseJpaImpl>((ClauseJpaImpl) service.merge(clause),
				HttpStatus.OK);
	}

	/**
	 * Merges (adds or updates) a Paragraph record.
	 * 
	 * @param paragraph
	 * @return The newly merged Paragraph, with created ID if new.
	 */
	@RequestMapping(method = RequestMethod.PUT, value = RestUrls.CONTENT + RestUrls.PARAGRAPH,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<ParagraphJpaImpl> merge(@RequestBody ParagraphJpaImpl paragraph) {
		return new ResponseEntity<ParagraphJpaImpl>(
				(ParagraphJpaImpl) service.merge(paragraph), HttpStatus.OK);
	}
}