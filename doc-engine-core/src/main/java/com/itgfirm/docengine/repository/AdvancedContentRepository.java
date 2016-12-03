package com.itgfirm.docengine.repository;

import static com.itgfirm.docengine.DocEngine.Constants.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itgfirm.docengine.types.jpa.AdvancedDocumentJpaImpl;
import com.itgfirm.docengine.types.jpa.ClauseJpaImpl;
import com.itgfirm.docengine.types.jpa.ParagraphJpaImpl;
import com.itgfirm.docengine.types.jpa.SectionJpaImpl;

@Qualifier(AUTOWIRE_QUALIFIER_ADVANCED)
public interface AdvancedContentRepository extends ContentRepository {
	static final String OBJECT_SECTION = "SectionJpaImpl";
	static final String OBJECT_CLAUSE = "ClauseJpaImpl";
	static final String OBJECT_PARAGRAPH = "ParagraphJpaImpl";

	static final String PARAM_DOCUMENT = "document";
	static final String PARAM_SECTION = "section";
	static final String PARAM_CLAUSE = "clause";

	static final String GET_SECTIONS_BY_PARENT = "select s from " + OBJECT_SECTION + " s where s." + PARAM_DOCUMENT
			+ " = :" + PARAM_DOCUMENT;
	static final String GET_CLAUSES_BY_PARENT = "select c from " + OBJECT_CLAUSE + " c where c." + PARAM_SECTION
			+ " = :" + PARAM_SECTION;
	static final String GET_PARAGRAPHS_BY_PARENT = "select p from " + OBJECT_PARAGRAPH + " p where p." + PARAM_CLAUSE
			+ " = :" + PARAM_CLAUSE;

	@Query(GET_SECTIONS_BY_PARENT)
	Iterable<SectionJpaImpl> findSectionsByParent(@Param(PARAM_DOCUMENT) AdvancedDocumentJpaImpl document);

	@Query(GET_CLAUSES_BY_PARENT)
	Iterable<ClauseJpaImpl> findClausesByParent(@Param(PARAM_SECTION) SectionJpaImpl section);

	@Query(GET_PARAGRAPHS_BY_PARENT)
	Iterable<ParagraphJpaImpl> findParagraphsByParent(@Param(PARAM_CLAUSE) ClauseJpaImpl clause);
}