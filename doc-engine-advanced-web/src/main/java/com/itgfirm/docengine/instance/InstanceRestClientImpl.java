package com.itgfirm.docengine.instance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

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
import com.itgfirm.docengine.util.AdvancedWebConstants;
import com.itgfirm.docengine.util.Utils;
import com.itgfirm.docengine.web.AbstractRestClient;
import com.itgfirm.docengine.web.RestClient;
import com.itgfirm.docengine.web.RestUrls;

/**
 * @author Justin Scott
 * 
 *         REST Client for the Instance Service, to be used internally only
 */
@Component(AdvancedWebConstants.NAMED_INSTANCE_REST_CLIENT)
class InstanceRestClientImpl extends AbstractRestClient implements RestClient {
	@SuppressWarnings("unused")
	private static final Logger LOG = LogManager.getLogger(InstanceRestClientImpl.class);

	public InstanceRestClientImpl() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get()
	 */
	@Override
	public List<?> get() {
		return Arrays.asList(adhocGet(RestUrls.INSTANCES, InstanceJpaImpl[].class).getBody());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.Long)
	 */
	@Override
	public Instance get(Long id) {
		return get(id, Instance.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String)
	 */
	@Override
	public Instance get(String code) {
		return get(null, code);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String, java.lang.String)
	 */
	@Override
	public Instance get(String projectId, String code) {
		return get(projectId, code, InstanceJpaImpl.class, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.Long, java.lang.Class)
	 */
	@Override
	public Instance get(Long id, Class<?> type) {
		return (Instance) get(id, type, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.Long, java.lang.Class, boolean)
	 */
	@Override
	public Instance get(Long id, Class<?> type, boolean eagerKids) {
		if (AdvancedDocumentInstance.class.isAssignableFrom(type)) {
			if (eagerKids) {
				return adhocGet(
						RestUrls.INSTANCE + RestUrls.DOCUMENT_EAGER_KIDS + RestUrls.BY_ID,
						AdvancedDocumentInstanceJpaImpl.class, id).getBody();
			} else {
				return adhocGet(RestUrls.INSTANCE + RestUrls.DOCUMENT + RestUrls.BY_ID,
						AdvancedDocumentInstanceJpaImpl.class, id).getBody();
			}
		} else if (SectionInstance.class.isAssignableFrom(type)) {
			if (eagerKids) {
				return adhocGet(
						RestUrls.INSTANCE + RestUrls.SECTION_EAGER_KIDS + RestUrls.BY_ID,
						SectionInstanceJpaImpl.class, id).getBody();
			} else {
				return adhocGet(RestUrls.INSTANCE + RestUrls.SECTION + RestUrls.BY_ID,
						SectionInstanceJpaImpl.class, id).getBody();
			}
		} else if (ClauseInstance.class.isAssignableFrom(type)) {
			if (eagerKids) {
				return adhocGet(
						RestUrls.INSTANCE + RestUrls.CLAUSE_EAGER_KIDS + RestUrls.BY_ID,
						ClauseInstanceJpaImpl.class, id).getBody();
			} else {
				return adhocGet(RestUrls.INSTANCE + RestUrls.CLAUSE + RestUrls.BY_ID,
						ClauseInstanceJpaImpl.class, id).getBody();
			}
		} else if (ParagraphInstance.class.isAssignableFrom(type)) {
			return adhocGet(RestUrls.INSTANCE + RestUrls.PARAGRAPH + RestUrls.BY_ID,
					ParagraphInstanceJpaImpl.class, id).getBody();
		} else {
			return adhocGet(RestUrls.INSTANCE + RestUrls.BY_ID, InstanceJpaImpl.class, id)
					.getBody();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String, java.lang.Class)
	 */
	@Override
	public Instance get(String code, Class<?> type) {
		return get(null, code, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String, java.lang.Class,
	 * boolean)
	 */
	@Override
	public Instance get(String code, Class<?> type, boolean eagerKids) {
		return get(null, code, type, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String, java.lang.String,
	 * java.lang.Class)
	 */
	@Override
	public Instance get(String projectId, String code, Class<?> type) {
		return get(projectId, code, type, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#get(java.lang.String, java.lang.String,
	 * java.lang.Class, boolean)
	 */
	@Override
	public Instance get(String projectId, String code, Class<?> type, boolean eagerKids) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(RestUrls.PARAM_PROJECT_ID, projectId);
		params.put(RestUrls.PARAM_CODE, code);
		if (type != null) {
			if (AdvancedDocumentInstance.class.isAssignableFrom(type)) {
				if (eagerKids) {
					return adhocGet(
							RestUrls.INSTANCE + RestUrls.DOCUMENT_EAGER_KIDS
									+ RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE,
									AdvancedDocumentInstanceJpaImpl.class, params).getBody();
				} else {
					return adhocGet(
							RestUrls.INSTANCE + RestUrls.DOCUMENT + RestUrls.BY_PROJECT_ID
									+ RestUrls.BY_CODE, AdvancedDocumentInstanceJpaImpl.class, params)
							.getBody();
				}
			} else if (SectionInstance.class.isAssignableFrom(type)) {
				if (eagerKids) {
					return adhocGet(
							RestUrls.INSTANCE + RestUrls.SECTION_EAGER_KIDS
									+ RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE,
							SectionInstanceJpaImpl.class, params).getBody();
				} else {
					return adhocGet(
							RestUrls.INSTANCE + RestUrls.SECTION + RestUrls.BY_PROJECT_ID
									+ RestUrls.BY_CODE, SectionInstanceJpaImpl.class, params)
							.getBody();
				}
			} else if (ClauseInstanceJpaImpl.class.isAssignableFrom(type)) {
				if (eagerKids) {
					return adhocGet(
							RestUrls.INSTANCE + RestUrls.CLAUSE_EAGER_KIDS
									+ RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE,
							ClauseInstanceJpaImpl.class, params).getBody();
				} else {
					return adhocGet(
							RestUrls.INSTANCE + RestUrls.CLAUSE + RestUrls.BY_PROJECT_ID
									+ RestUrls.BY_CODE, ClauseInstanceJpaImpl.class, params)
							.getBody();
				}
			} else if (ParagraphInstance.class.isAssignableFrom(type)) {
				return adhocGet(
						RestUrls.INSTANCE + RestUrls.PARAGRAPH + RestUrls.BY_PROJECT_ID
								+ RestUrls.BY_CODE, ParagraphInstanceJpaImpl.class, params)
						.getBody();
			} else if (Instance.class.isAssignableFrom(type)) {
				return adhocGet(RestUrls.INSTANCE + RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE,
						InstanceJpaImpl.class, params).getBody();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getByCodeLike(java.lang.String,
	 * java.lang.Class)
	 */
	@Override
	public List<Instance> getByCodeLike(String like, Class<?> type) {
		return getByProjectAndCodeLike(null, like, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getByProjectAndCodeLike( java.lang.String,
	 * java.lang.String, java.lang.Class)
	 */
	@Override
	public List<Instance> getByProjectAndCodeLike(String projectId, String like, Class<?> type) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(RestUrls.PARAM_PROJECT_ID, projectId);
		params.put(RestUrls.PARAM_LIKE, like);
		if (type.equals(Instance[].class)) {
			Instance[] instances;
			instances =
					adhocGet(
							RestUrls.INSTANCE + RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE_LIKE,
							Instance[].class, params).getBody();
			if (Utils.isNotNullOrEmpty(instances)) {
				return Arrays.asList(instances);
			}
		}
		return null;
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
		if (AdvancedDocumentInstance.class.isAssignableFrom(type)) {
			SectionInstanceJpaImpl[] sections;
			if (eagerKids) {
				sections =
						adhocGet(
								RestUrls.INSTANCE + RestUrls.DOCUMENT
										+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_ID,
								SectionInstanceJpaImpl[].class, id).getBody();
			} else {
				sections =
						adhocGet(
								RestUrls.INSTANCE + RestUrls.DOCUMENT + RestUrls.CHILDREN
										+ RestUrls.BY_ID, SectionInstanceJpaImpl[].class, id)
								.getBody();
			}
			if (Utils.isNotNullOrEmpty(sections)) {
				return Arrays.asList(sections);
			}
		} else if (SectionInstance.class.isAssignableFrom(type)) {
			ClauseInstanceJpaImpl[] clauses;
			if (eagerKids) {
				clauses =
						adhocGet(
								RestUrls.INSTANCE + RestUrls.SECTION
										+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_ID,
								ClauseInstanceJpaImpl[].class, id).getBody();
			} else {
				clauses =
						adhocGet(
								RestUrls.INSTANCE + RestUrls.SECTION + RestUrls.CHILDREN
										+ RestUrls.BY_ID, ClauseInstanceJpaImpl[].class, id)
								.getBody();
			}
			if (Utils.isNotNullOrEmpty(clauses)) {
				return Arrays.asList(clauses);
			}
		} else if (ClauseInstance.class.isAssignableFrom(type)) {
			ParagraphInstanceJpaImpl[] paragraphs;
			if (eagerKids) {
				paragraphs =
						adhocGet(
								RestUrls.INSTANCE + RestUrls.CLAUSE
										+ RestUrls.CHILDREN_EAGER_KIDS + RestUrls.BY_ID,
								ParagraphInstanceJpaImpl[].class, id).getBody();
			} else {
				paragraphs =
						adhocGet(
								RestUrls.INSTANCE + RestUrls.CLAUSE + RestUrls.CHILDREN
										+ RestUrls.BY_ID, ParagraphInstanceJpaImpl[].class, id)
								.getBody();
			}
			if (Utils.isNotNullOrEmpty(paragraphs)) {
				return Arrays.asList(paragraphs);
			}
		} else {
			InstanceJpaImpl[] instances;
			if (eagerKids) {
				instances =
						adhocGet(
								RestUrls.INSTANCE + RestUrls.CHILDREN_EAGER_KIDS
										+ RestUrls.BY_ID, InstanceJpaImpl[].class, id)
								.getBody();
			} else {
				instances =
						adhocGet(RestUrls.INSTANCE + RestUrls.CHILDREN + RestUrls.BY_ID,
								InstanceJpaImpl[].class, id).getBody();
			}
			if (Utils.isNotNullOrEmpty(instances)) {
				return Arrays.asList(instances);
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
		return getChildren(null, code, type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getChildren(java.lang.String, java.lang.Class,
	 * boolean)
	 */
	@Override
	public List<?> getChildren(String code, Class<?> type, boolean eagerKids) {
		return getChildren(null, code, type, eagerKids);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getChildren(java.lang.String,
	 * java.lang.String, java.lang.Class)
	 */
	@Override
	public List<?> getChildren(String projectId, String code, Class<?> type) {
		return getChildren(projectId, code, type, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#getChildren(java.lang.String,
	 * java.lang.String, java.lang.Class, boolean)
	 */
	@Override
	public List<?> getChildren(String projectId, String code, Class<?> type, boolean eagerKids) {
		Map<String, String> params = new HashMap<String, String>();
		params.put(RestUrls.PARAM_PROJECT_ID, projectId);
		params.put(RestUrls.PARAM_CODE, code);
		if (type.equals(AdvancedDocumentInstanceJpaImpl.class)) {
			SectionInstanceJpaImpl[] sections;
			if (eagerKids) {
				sections =
						adhocGet(
								RestUrls.INSTANCE + RestUrls.DOCUMENT
										+ RestUrls.CHILDREN_EAGER_KIDS
										+ RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE,
								SectionInstanceJpaImpl[].class, params).getBody();
			} else {
				sections =
						adhocGet(
								RestUrls.INSTANCE + RestUrls.DOCUMENT + RestUrls.CHILDREN
										+ RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE,
								SectionInstanceJpaImpl[].class, params).getBody();
			}
			if (Utils.isNotNullOrEmpty(sections)) {
				return Arrays.asList(sections);
			}
		} else if (type.equals(SectionInstanceJpaImpl.class)) {
			ClauseInstanceJpaImpl[] clauses;
			if (eagerKids) {
				clauses =
						adhocGet(
								RestUrls.INSTANCE + RestUrls.SECTION
										+ RestUrls.CHILDREN_EAGER_KIDS
										+ RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE,
								ClauseInstanceJpaImpl[].class, params).getBody();
			} else {
				clauses =
						adhocGet(
								RestUrls.INSTANCE + RestUrls.SECTION + RestUrls.CHILDREN
										+ RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE,
								ClauseInstanceJpaImpl[].class, params).getBody();
			}
			if (Utils.isNotNullOrEmpty(clauses)) {
				return Arrays.asList(clauses);
			}
		} else if (type.equals(ClauseInstanceJpaImpl.class)) {
			ParagraphInstanceJpaImpl[] paragraphs;
			if (eagerKids) {
				paragraphs =
						adhocGet(
								RestUrls.INSTANCE + RestUrls.CLAUSE
										+ RestUrls.CHILDREN_EAGER_KIDS
										+ RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE,
								ParagraphInstanceJpaImpl[].class, params).getBody();
			} else {
				paragraphs =
						adhocGet(
								RestUrls.INSTANCE + RestUrls.CLAUSE + RestUrls.CHILDREN
										+ RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE,
								ParagraphInstanceJpaImpl[].class, params).getBody();
			}
			if (Utils.isNotNullOrEmpty(paragraphs)) {
				return Arrays.asList(paragraphs);
			}
		} else {
			InstanceJpaImpl[] instances;
			if (eagerKids) {
				instances =
						adhocGet(
								RestUrls.INSTANCE + RestUrls.CHILDREN_EAGER_KIDS
										+ RestUrls.BY_PROJECT_ID + RestUrls.BY_CODE,
								InstanceJpaImpl[].class, params).getBody();
			} else {
				instances =
						adhocGet(
								RestUrls.INSTANCE + RestUrls.CHILDREN + RestUrls.BY_PROJECT_ID
										+ RestUrls.BY_CODE, InstanceJpaImpl[].class, params)
								.getBody();
			}
			if (Utils.isNotNullOrEmpty(instances)) {
				return Arrays.asList(instances);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#merge(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<?> merge(List<?> list) {
		List<Instance> instances = (List<Instance>) list;
		return put(RestUrls.INSTANCES, new HttpEntity<Instances>(new Instances(instances)),
				Instances.class).getBody().getInstances();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#merge(java.lang.Object, java.lang.Class)
	 */
	@Override
	public Instance merge(Object object, Class<?> type) {
		if (type.equals(AdvancedDocumentInstanceJpaImpl.class)) {
			AdvancedDocumentInstance instance = (AdvancedDocumentInstance) object;
			return put(RestUrls.INSTANCE + RestUrls.DOCUMENT,
					new HttpEntity<AdvancedDocumentInstance>(instance), AdvancedDocumentInstanceJpaImpl.class)
					.getBody();
		} else if (type.equals(SectionInstanceJpaImpl.class)) {
			return put(RestUrls.INSTANCE + RestUrls.SECTION,
					new HttpEntity<SectionInstance>((SectionInstance) object),
					SectionInstanceJpaImpl.class).getBody();
		} else if (type.equals(ClauseInstanceJpaImpl.class)) {
			return put(RestUrls.INSTANCE + RestUrls.CLAUSE,
					new HttpEntity<ClauseInstance>((ClauseInstance) object),
					ClauseInstanceJpaImpl.class).getBody();
		} else if (type.equals(ParagraphInstanceJpaImpl.class)) {
			return put(RestUrls.INSTANCE + RestUrls.PARAGRAPH,
					new HttpEntity<ParagraphInstance>((ParagraphInstance) object),
					ParagraphInstanceJpaImpl.class).getBody();
		} else {
			return put(RestUrls.INSTANCE, new HttpEntity<Instance>((Instance) object),
					InstanceJpaImpl.class).getBody();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.web.RestClient#delete(java.lang.Object)
	 */
	@Override
	public void delete(Object object) {
		if (object instanceof Instance) {
			Instance instance = (Instance) object;
			delete(RestUrls.INSTANCE + RestUrls.BY_ID, instance.getId());
		}
	}
}