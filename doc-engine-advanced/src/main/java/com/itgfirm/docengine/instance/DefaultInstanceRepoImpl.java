package com.itgfirm.docengine.instance;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.QueryException;
import org.springframework.stereotype.Repository;

import com.itgfirm.docengine.data.AbstractHibernateOrmRepo;
import com.itgfirm.docengine.data.DataConstants;
import com.itgfirm.docengine.types.AdvancedDocumentInstance;
import com.itgfirm.docengine.types.ClauseInstance;
import com.itgfirm.docengine.types.DocumentInstance;
import com.itgfirm.docengine.types.Instance;
import com.itgfirm.docengine.types.ParagraphInstance;
import com.itgfirm.docengine.types.SectionInstance;
import com.itgfirm.docengine.types.jpa.InstanceJpaImpl;
import com.itgfirm.docengine.util.Utils;

/**
 * @author Justin Scott
 * 
 *         Default Content Instance Repository
 */
@Repository
class DefaultInstanceRepoImpl extends AbstractHibernateOrmRepo implements InstanceRepo {
	private static final Logger LOG = LogManager.getLogger(DefaultInstanceRepoImpl.class);

	public DefaultInstanceRepoImpl() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.repo.InstanceRepo#get()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Instance> get() {
		LOG.trace("Attempting to get all Instances.");
		List<Instance> instances = (List<Instance>) super.get(InstanceJpaImpl.class);
		if (Utils.isNotNullOrEmpty(instances)) {
			int size = instances.size();
			LOG.trace("Found " + size + " Templates In Content Repo.");
			if (size > 0) {
				Collections.sort(instances);
				return instances;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.repo.InstanceRepo#get(java.lang.Long)
	 */
	@Override
	public Instance get(Long id) {
		LOG.trace("Attempting to get Instance by ID.");
		return (Instance) super.get(InstanceJpaImpl.class, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceRepo#get(java.lang.String, java.lang.String)
	 */
	@Override
	public Instance get(String projectId, String code) {
		if (Utils.isNotNullOrEmpty(projectId) && Utils.isNotNullOrEmpty(code)) {
			String[] paramNames = {
					DataConstants.PARAM_PROJECT_ID, DataConstants.PARAM_CONTENT_CD
			};
			Object[] values = {
					projectId, code
			};
			List<?> instances =
					getWithQuery(DataConstants.GET_INSTANCE_BY_PROJECT_ID_AND_CONTENT_CD,
							paramNames, values);
			if (Utils.isNotNullOrEmpty(instances)) {
				return (Instance) instances.get(0);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceRepo#getByProjectAndCodeLike(java
	 * .lang.String, java.lang.String)
	 */
	@Override
	public List<Instance> getByProjectAndCodeLike(String projectId, String like) {
		LOG.trace("Attempting to get Instances by project ID and code-like.");
		if (Utils.isNotNullOrEmpty(projectId) && Utils.isNotNullOrEmpty(like)) {
			LOG.trace("Attempting To Get Instance For Project And Code-Like: " + projectId
					+ ": " + like);
			String[] paramNames = {
					DataConstants.PARAM_PROJECT_ID, DataConstants.PARAM_CONTENT_CD
			};
			String[] values = {
					projectId, "%" + like + "%"
			};
			return getWithQuery(DataConstants.GET_INSTANCES_BY_PROJECT_AND_CODE_LIKE,
					paramNames, values);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.InstanceRepo#getWithQuery(java.lang.String,
	 * java.lang.String, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Instance> getWithQuery(String query, String paramName, Object value) {
		LOG.trace("Attempting to get Instances with query.");
		try {
			return (List<Instance>) super.getWithQuery(query, paramName, value);
		} catch (NoSuchElementException | IllegalArgumentException e) {
			LOG.error("Problem With Query.", e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.InstanceRepo#getWithQuery(java.lang.String,
	 * java.lang.String[], java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Instance> getWithQuery(String query, String[] paramNames, Object[] values) {
		LOG.trace("Attempting to get Instances with multi-parameter query.");
		try {
			return (List<Instance>) super.getWithQuery(query, paramNames, values);
		} catch (NoSuchElementException | IllegalArgumentException | QueryException e) {
			LOG.error("Problem With Query.", e);
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceRepo#initialize(
	 * com.itgfirm.docengine.types.jpa.jpa.Instance)
	 */
	@Override
	public Instance initialize(Instance instance) {
		if (Utils.isNotNullOrEmpty(instance)) {
			if (Utils.isNotNullOrZero(instance.getId())) {
				instance = get(instance.getId());
			} else if (Utils.isNotNullOrEmpty(instance.getContent().getContentCd())) {
				instance = get(instance.getProjectId(), instance.getContent().getContentCd());
			} else {
				LOG.trace("Not Found In The Database.");
				return instance;
			}
			if (instance instanceof AdvancedDocumentInstance) {
				LOG.trace("Attempting to initialize Document children.");
				AdvancedDocumentInstance d = (AdvancedDocumentInstance) instance;
				super.initialize(d.getSections());
				if (Utils.isNotNullOrEmpty(d.getSections())) {
					for (SectionInstance s : d.getSections()) {
						super.initialize(s.getClauses());
						if (Utils.isNotNullOrEmpty(s.getClauses())) {
							for (ClauseInstance c : s.getClauses()) {
								super.initialize(c.getParagraphs());
							}
						}
					}
				}
				return d;
			} else if (instance instanceof SectionInstance) {
				LOG.trace("Attempting to initialize Section children.");
				SectionInstance s = (SectionInstance) instance;
				super.initialize(s.getClauses());
				if (Utils.isNotNullOrEmpty(s.getClauses())) {
					for (ClauseInstance c : s.getClauses()) {
						super.initialize(c.getParagraphs());
					}
				}
				return s;
			} else if (instance instanceof ClauseInstance) {
				LOG.trace("Attempting to initialize Clause children.");
				ClauseInstance c = (ClauseInstance) instance;
				super.initialize(c.getParagraphs());
				return c;
			}
		}
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceRepo#merge(com.itgfirm.docengine
	 * .models.Instance)
	 */
	@Override
	public Instance merge(Instance instance) {
		LOG.trace("Attempting to merge Instance.");
		if (Utils.isNotNullOrEmpty(instance) && instance.isValid()) {
			String code = instance.getContent().getContentCd();
			String projectId = instance.getProjectId();
			if (Utils.isNotNullOrZero(instance.getId())) {
				LOG.trace("Merging existing Instance.");
				return (Instance) super.merge((InstanceJpaImpl) instance);
			} else if (instance instanceof AdvancedDocumentInstance) {
				LOG.trace("Attempting to merge AdvancedDocumentInstance.");
				if (!Utils.isNotNullOrEmpty(get(projectId, code))) {
					LOG.trace("Merging AdvancedDocumentInstance.");
					return (AdvancedDocumentInstance) super.merge((InstanceJpaImpl) instance);
				}
			} else if (instance instanceof DocumentInstance) {
				LOG.trace("Attempting to merge DocumentInstance.");
				if (!Utils.isNotNullOrEmpty(get(projectId, code))) {
					LOG.trace("Merging DocumentInstance.");
					return (DocumentInstance) super.merge((InstanceJpaImpl) instance);
				}
			} else if (instance instanceof SectionInstance) {
				LOG.trace("Attempting to merge SectionInstance.");
				if (!Utils.isNotNullOrEmpty(get(projectId, code))) {
					LOG.trace("Merging SectionInstance.");
					return (SectionInstance) super.merge((InstanceJpaImpl) instance);
				}
			} else if (instance instanceof ClauseInstance) {
				LOG.trace("Attempting to merge ClauseInstance.");
				if (!Utils.isNotNullOrEmpty(get(projectId, code))) {
					LOG.trace("Merging ClauseInstance.");
					return (ClauseInstance) super.merge((InstanceJpaImpl) instance);
				}
			} else if (instance instanceof ParagraphInstance) {
				LOG.trace("Attempting to merge ParagraphInstance.");
				if (!Utils.isNotNullOrEmpty(get(projectId, code))) {
					LOG.trace("Merging ParagraphInstance.");
					return (ParagraphInstance) super.merge((InstanceJpaImpl) instance);
				}
			} else {
				LOG.trace("Attempting to merge unknown Instance.");
				if (!Utils.isNotNullOrEmpty(get(projectId, code))) {
					LOG.trace("Merging unknown Instance.");
					return (Instance) super.merge((InstanceJpaImpl) instance);
				} else {
					LOG.trace("Found Pre-existing Instance: Project ID - " + projectId
							+ " | Code - " + code);
					return instance;
				}
			}
		} else {
			LOG.error("Object, Content, Content ID And/Or Project ID "
					+ "Must Not Be Null Or Empty!");
		}
		LOG.error("Unexpected Outcome! Most Likely Due To Invalid Parameters.");
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceRepo#deleteAll()
	 */
	@Override
	public boolean deleteAll() {
		LOG.trace("Attempting to delete all Instances.");
		List<Instance> instances = get();
		if (Utils.isNotNullOrEmpty(instances)) {
			super.deleteAll(instances);
			instances = get();
			if (Utils.isNotNullOrEmpty(instances)) {
				String error = "Some Items Were NOT Deleted!!!\n";
				for (Instance i : instances) {
					error.concat(" ----- INSTANCE ITEM WAS NOT DELETED: "
							+ i.getContent().getContentCd() + " !!!\n");
				}
				LOG.trace(error);
				return false;
			} else {
				LOG.trace("All items deleted as expected.");
				return true;
			}
		} else {
			LOG.trace("Nothing to delete.");
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceRepo#delete(
	 * com.itgfirm.docengine.types.jpa.jpa.Instance)
	 */
	@Override
	public boolean delete(Instance instance) {
		if (Utils.isNotNullOrEmpty(instance)) {
			LOG.trace("Attempting To Delete Instance: " + instance.getContent().getContentCd()
					+ " For Project + " + instance.getProjectId());
			Long id = instance.getId();
			super.delete((InstanceJpaImpl) instance);
			instance = get(id);
			if (Utils.isNotNullOrEmpty(instance)) {
				LOG.trace("Item Was NOT Deleted: " + instance.getContent().getContentCd());
				return false;
			} else {
				LOG.trace("Item deleted as expected.");
				return true;
			}
		}
		return false;
	}
}