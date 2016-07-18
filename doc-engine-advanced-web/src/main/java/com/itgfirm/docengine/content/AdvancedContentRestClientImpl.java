package com.itgfirm.docengine.content;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.itgfirm.docengine.types.AdvancedDocument;
import com.itgfirm.docengine.types.Clause;
import com.itgfirm.docengine.types.Content;
//import com.itgfirm.docengine.types.Document;
import com.itgfirm.docengine.types.Paragraph;
import com.itgfirm.docengine.types.Section;
import com.itgfirm.docengine.types.jpa.AdvancedDocumentJpaImpl;
import com.itgfirm.docengine.types.jpa.ClauseJpaImpl;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.types.jpa.Contents;
//import com.itgfirm.docengine.types.jpa.DocumentJpaImpl;
import com.itgfirm.docengine.types.jpa.ParagraphJpaImpl;
import com.itgfirm.docengine.types.jpa.SectionJpaImpl;
import com.itgfirm.docengine.util.AdvancedWebConstants;
import com.itgfirm.docengine.util.Utils;
import com.itgfirm.docengine.web.AbstractRestClient;
import com.itgfirm.docengine.web.RestClient;
import com.itgfirm.docengine.web.RestUrls;

/**
 * @author Justin Scott REST Client for the Content Service, to be used internally only
 */
@Component(AdvancedWebConstants.NAMED_ADVANCED_REST_CLIENT)
class AdvancedContentRestClientImpl extends AbstractRestClient implements RestClient {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(AdvancedContentRestClientImpl.class);

	public AdvancedContentRestClientImpl() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get()
	 */
	@Override
	public List<?> get() {
		ResponseEntity<ContentJpaImpl[]> response =
				adhocGet(RestUrls.CONTENTS, ContentJpaImpl[].class);
		if (Utils.isNotNullOrEmpty(response)) {
			Content[] contents = null;
			contents = response.getBody();
			return Arrays.asList(contents);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.Long)
	 */
	@Override
	public Object get(Long id) {
		return get(id, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String)
	 */
	@Override
	public Object get(String code) {
		return adhocGet(RestUrls.CONTENT + RestUrls.BY_CODE, ContentJpaImpl.class, code)
				.getBody();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.Long, java.lang.Class)
	 */
	@Override
	public Object get(Long id, Class<?> type) {
		return (Content) get(id, type, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.Long, java.lang.Class, boolean)
	 */
	@Override
	public Object get(Long id, Class<?> type, boolean eagerKids) {
		Content response = null;
		if (type != null) {
			if (type.isAssignableFrom(AdvancedDocumentJpaImpl.class)) {
				if (eagerKids) {
					response =
							(AdvancedDocument) adhocGet(
									RestUrls.CONTENT + RestUrls.DOCUMENT_EAGER_KIDS
											+ RestUrls.BY_ID, AdvancedDocumentJpaImpl.class, id)
									.getBody();
				} else {
					response =
							(AdvancedDocument) adhocGet(
									RestUrls.CONTENT + RestUrls.DOCUMENT + RestUrls.BY_ID,
									AdvancedDocumentJpaImpl.class, id).getBody();
				}
			} else if (type.equals(Section.class)) {
				if (eagerKids) {
					response =
							adhocGet(
									RestUrls.CONTENT + RestUrls.SECTION_EAGER_KIDS
											+ RestUrls.BY_ID, SectionJpaImpl.class, id)
									.getBody();
				} else {
					response =
							adhocGet(RestUrls.CONTENT + RestUrls.SECTION + RestUrls.BY_ID,
									SectionJpaImpl.class, id).getBody();
				}
			} else if (type.equals(Clause.class)) {
				if (eagerKids) {
					response =
							adhocGet(
									RestUrls.CONTENT + RestUrls.CLAUSE_EAGER_KIDS
											+ RestUrls.BY_ID, ClauseJpaImpl.class, id)
									.getBody();
				} else {
					response =
							adhocGet(RestUrls.CONTENT + RestUrls.CLAUSE + RestUrls.BY_ID,
									ClauseJpaImpl.class, id).getBody();
				}
			} else if (type.equals(Paragraph.class)) {
				response =
						adhocGet(RestUrls.CONTENT + RestUrls.PARAGRAPH + RestUrls.BY_ID,
								ParagraphJpaImpl.class, id).getBody();
			} else {
				response =
						adhocGet(RestUrls.CONTENT + RestUrls.BY_ID, Content.class, id)
								.getBody();
			}
		} else {
			response =
					adhocGet(RestUrls.CONTENT + RestUrls.BY_ID, ContentJpaImpl.class, id)
							.getBody();
		}
		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String, java.lang.Class)
	 */
	@Override
	public Object get(String code, Class<?> type) {
		return get(code, type, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String, java.lang.Class,
	 * boolean)
	 */
	@Override
	public Object get(String code, Class<?> type, boolean eagerKids) {
		if (type.equals(AdvancedDocument.class)) {
			if (eagerKids) {
				return adhocGet(
						RestUrls.CONTENT + RestUrls.DOCUMENT_EAGER_KIDS + RestUrls.BY_CODE,
						AdvancedDocumentJpaImpl.class, code).getBody();
			} else {
				return adhocGet(RestUrls.CONTENT + RestUrls.DOCUMENT + RestUrls.BY_CODE,
						AdvancedDocumentJpaImpl.class, code).getBody();
			}
		} else if (type.equals(Section.class)) {
			if (eagerKids) {
				return adhocGet(
						RestUrls.CONTENT + RestUrls.SECTION_EAGER_KIDS + RestUrls.BY_CODE,
						SectionJpaImpl.class, code).getBody();
			} else {
				return adhocGet(RestUrls.CONTENT + RestUrls.SECTION + RestUrls.BY_CODE,
						SectionJpaImpl.class, code).getBody();
			}
		} else if (type.equals(Clause.class)) {
			if (eagerKids) {
				return adhocGet(
						RestUrls.CONTENT + RestUrls.CLAUSE_EAGER_KIDS + RestUrls.BY_CODE,
						ClauseJpaImpl.class, code).getBody();
			} else {
				return adhocGet(RestUrls.CONTENT + RestUrls.CLAUSE + RestUrls.BY_CODE,
						ClauseJpaImpl.class, code).getBody();
			}
		} else if (type.equals(Paragraph.class)) {
			return adhocGet(RestUrls.CONTENT + RestUrls.PARAGRAPH + RestUrls.BY_CODE,
					ParagraphJpaImpl.class, code).getBody();
		} else {
			return (Content) get(code);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String, java.lang.String)
	 */
	@Override
	public Object get(String projectId, String code) {
		return get(code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String, java.lang.String,
	 * java.lang.Class)
	 */
	@Override
	public Object get(String projectId, String code, Class<?> type) {
		return get(code, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String, java.lang.String,
	 * java.lang.Class, boolean)
	 */
	@Override
	public Object get(String projectId, String code, Class<?> type, boolean eagerKids) {
		return get(code, type, eagerKids);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getByCodeLike(java.lang.String,
	 * java.lang.Class)
	 */
	@Override
	public List<?> getByCodeLike(String like, Class<?> type) {
		if (type.equals(Content[].class)) {
			Content[] contents;
			contents =
					adhocGet(RestUrls.CONTENT + RestUrls.BY_CODE_LIKE, ContentJpaImpl[].class,
							like).getBody();
			if (Utils.isNotNullOrEmpty(contents)) {
				return Arrays.asList(contents);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getByProjectAndCodeLike( java.lang.String,
	 * java.lang.String, java.lang.Class)
	 */
	@Override
	public List<?> getByProjectAndCodeLike(String projectId, String like, Class<?> type) {
		return getByCodeLike(like, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getChildren(java.lang.Long, java.lang.Class)
	 */
	@Override
	public List<?> getChildren(Long id, Class<?> type) {
		return getChildren(id, type, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getChildren(java.lang.Long, java.lang.Class,
	 * boolean)
	 */
	@Override
	public List<?> getChildren(Long id, Class<?> type, boolean eagerKids) {
		if (type.equals(AdvancedDocument.class)) {
			Section[] sections;
			if (eagerKids) {
				sections =
						adhocGet(
								RestUrls.CONTENT + RestUrls.DOCUMENT
										+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_ID,
								SectionJpaImpl[].class, id).getBody();
			} else {
				sections =
						adhocGet(
								RestUrls.CONTENT + RestUrls.DOCUMENT + RestUrls.CHILDREN
										+ RestUrls.BY_ID, SectionJpaImpl[].class, id)
								.getBody();
			}
			if (Utils.isNotNullOrEmpty(sections)) {
				return Arrays.asList(sections);
			}
		} else if (type.equals(Section.class)) {
			Clause[] clauses;
			if (eagerKids) {
				clauses =
						adhocGet(
								RestUrls.CONTENT + RestUrls.SECTION
										+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_ID,
								ClauseJpaImpl[].class, id).getBody();
			} else {
				clauses =
						adhocGet(
								RestUrls.CONTENT + RestUrls.SECTION + RestUrls.CHILDREN
										+ RestUrls.BY_ID, ClauseJpaImpl[].class, id).getBody();
			}
			if (Utils.isNotNullOrEmpty(clauses)) {
				return Arrays.asList(clauses);
			}
		} else if (type.equals(Clause.class)) {
			Paragraph[] paragraphs;
			if (eagerKids) {
				paragraphs =
						adhocGet(
								RestUrls.CONTENT + RestUrls.CLAUSE
										+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_ID,
								ParagraphJpaImpl[].class, id).getBody();
			} else {
				paragraphs =
						adhocGet(
								RestUrls.CONTENT + RestUrls.CLAUSE + RestUrls.CHILDREN
										+ RestUrls.BY_ID, ParagraphJpaImpl[].class, id)
								.getBody();
			}
			if (Utils.isNotNullOrEmpty(paragraphs)) {
				return Arrays.asList(paragraphs);
			}
		} else {
			Content[] contents;
			if (eagerKids) {
				contents =
						adhocGet(
								RestUrls.CONTENT + RestUrls.CHILDREN_EAGER_KIDS
										+ RestUrls.BY_ID, ContentJpaImpl[].class, id)
								.getBody();
			} else {
				contents =
						adhocGet(RestUrls.CONTENT + RestUrls.CHILDREN + RestUrls.BY_ID,
								ContentJpaImpl[].class, id).getBody();
			}
			if (Utils.isNotNullOrEmpty(contents)) {
				return Arrays.asList(contents);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getChildren(java.lang.String, java.lang.Class)
	 */
	@Override
	public List<?> getChildren(String code, Class<?> type) {
		return getChildren(code, type, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getChildren(java.lang.String, java.lang.Class,
	 * boolean)
	 */
	@Override
	public List<?> getChildren(String code, Class<?> type, boolean eagerKids) {
		if (type.equals(AdvancedDocument.class)) {
			Section[] sections;
			if (eagerKids) {
				sections =
						adhocGet(
								RestUrls.CONTENT + RestUrls.DOCUMENT
										+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_CODE,
								SectionJpaImpl[].class, code).getBody();
			} else {
				sections =
						adhocGet(
								RestUrls.CONTENT + RestUrls.DOCUMENT + RestUrls.CHILDREN
										+ RestUrls.BY_CODE, SectionJpaImpl[].class, code)
								.getBody();
			}
			if (Utils.isNotNullOrEmpty(sections)) {
				return Arrays.asList(sections);
			}
		} else if (type.equals(Section.class)) {
			Clause[] clauses;
			if (eagerKids) {
				clauses =
						adhocGet(
								RestUrls.CONTENT + RestUrls.SECTION
										+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_CODE,
								ClauseJpaImpl[].class, code).getBody();
			} else {
				clauses =
						adhocGet(
								RestUrls.CONTENT + RestUrls.SECTION + RestUrls.CHILDREN
										+ RestUrls.BY_CODE, ClauseJpaImpl[].class, code)
								.getBody();
			}
			if (Utils.isNotNullOrEmpty(clauses)) {
				return Arrays.asList(clauses);
			}
		} else if (type.equals(Clause.class)) {
			Paragraph[] paragraphs;
			if (eagerKids) {
				paragraphs =
						adhocGet(
								RestUrls.CONTENT + RestUrls.CLAUSE
										+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_CODE,
								ParagraphJpaImpl[].class, code).getBody();
			} else {
				paragraphs =
						adhocGet(
								RestUrls.CONTENT + RestUrls.CLAUSE + RestUrls.CHILDREN
										+ RestUrls.BY_CODE, ParagraphJpaImpl[].class, code)
								.getBody();
			}
			if (Utils.isNotNullOrEmpty(paragraphs)) {
				return Arrays.asList(paragraphs);
			}
		} else {
			Content[] contents;
			if (eagerKids) {
				contents =
						adhocGet(
								RestUrls.CONTENT + RestUrls.CHILDREN_EAGER_KIDS
										+ RestUrls.BY_CODE, ContentJpaImpl[].class, code)
								.getBody();
			} else {
				contents =
						adhocGet(RestUrls.CONTENT + RestUrls.CHILDREN + RestUrls.BY_CODE,
								ContentJpaImpl[].class, code).getBody();
			}
			if (Utils.isNotNullOrEmpty(contents)) {
				return Arrays.asList(contents);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getChildren(java.lang.String,
	 * java.lang.String, java.lang.Class)
	 */
	@Override
	public List<?> getChildren(String projectId, String code, Class<?> type) {
		return getChildren(code, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getChildren(java.lang.String,
	 * java.lang.String, java.lang.Class, boolean)
	 */
	@Override
	public List<?> getChildren(String projectId, String code, Class<?> type, boolean eagerKids) {
		return getChildren(code, type, eagerKids);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#merge(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<?> merge(List<?> list) {
		List<Content> content = (List<Content>) list;
		return put(RestUrls.CONTENTS, new HttpEntity<Contents>(new Contents(content)),
				Contents.class).getBody().getContents();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#merge(java.lang.Object, java.lang.Class)
	 */
	@Override
	public Object merge(Object content, Class<?> type) {
		if (type.equals(AdvancedDocumentJpaImpl.class)) {
			return put(RestUrls.CONTENT + RestUrls.DOCUMENT,
					new HttpEntity<AdvancedDocumentJpaImpl>((AdvancedDocumentJpaImpl) content),
					AdvancedDocumentJpaImpl.class).getBody();
		} else if (type.equals(SectionJpaImpl.class)) {
			return put(RestUrls.CONTENT + RestUrls.SECTION,
					new HttpEntity<SectionJpaImpl>((SectionJpaImpl) content),
					SectionJpaImpl.class).getBody();
		} else if (type.equals(ClauseJpaImpl.class)) {
			return put(RestUrls.CONTENT + RestUrls.CLAUSE,
					new HttpEntity<ClauseJpaImpl>((ClauseJpaImpl) content),
					ClauseJpaImpl.class).getBody();
		} else if (type.equals(ParagraphJpaImpl.class)) {
			return put(RestUrls.CONTENT + RestUrls.PARAGRAPH,
					new HttpEntity<ParagraphJpaImpl>((ParagraphJpaImpl) content),
					ParagraphJpaImpl.class).getBody();
		} else {
			return put(RestUrls.CONTENT,
					new HttpEntity<ContentJpaImpl>((ContentJpaImpl) content),
					ContentJpaImpl.class).getBody();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#delete(java.lang.Object)
	 */
	@Override
	public void delete(Object object) {
		if (object instanceof Content) {
			Content content = (Content) object;
			super.delete(RestUrls.CONTENT + RestUrls.BY_ID, content.getId());
		}
	}

}