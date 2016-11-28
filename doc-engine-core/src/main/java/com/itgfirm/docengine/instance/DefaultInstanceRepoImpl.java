package com.itgfirm.docengine.instance;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.hibernate.QueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itgfirm.docengine.data.AbstractHibernateOrmRepo;
import com.itgfirm.docengine.data.DataConstants;
import com.itgfirm.docengine.types.jpa.AdvancedDocumentInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.ClauseInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.DocumentInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.InstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.ParagraphInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.SectionInstanceJpaImpl;
import com.itgfirm.docengine.util.Utils;

/**
 * @author Justin Scott
 * 
 *         Default Content Instance Repository
 */
// @Repository
@Deprecated
class DefaultInstanceRepoImpl extends AbstractHibernateOrmRepo {
	private static final Logger LOG = LoggerFactory.getLogger(DefaultInstanceRepoImpl.class);

	public DefaultInstanceRepoImpl() {
	}

	@SuppressWarnings("unchecked")
	public List<InstanceJpaImpl> get() {
		LOG.trace("Attempting to get all Instances.");
		List<InstanceJpaImpl> instances = (List<InstanceJpaImpl>) super.get(InstanceJpaImpl.class);
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

	public InstanceJpaImpl get(Long id) {
		LOG.trace("Attempting to get Instance by ID.");
		return (InstanceJpaImpl) super.get(InstanceJpaImpl.class, id);
	}

	public InstanceJpaImpl get(String projectId, String code) {
		if (Utils.isNotNullOrEmpty(projectId) && Utils.isNotNullOrEmpty(code)) {
			String[] paramNames = { DataConstants.PARAM_PROJECT_ID, DataConstants.PARAM_CONTENT_CD };
			Object[] values = { projectId, code };
			List<?> instances = getWithQuery(DataConstants.GET_INSTANCE_BY_PROJECT_ID_AND_CONTENT_CD, paramNames,
					values);
			if (Utils.isNotNullOrEmpty(instances)) {
				return (InstanceJpaImpl) instances.get(0);
			}
		}
		return null;
	}

	public List<InstanceJpaImpl> getByProjectAndCodeLike(String projectId, String like) {
		LOG.trace("Attempting to get Instances by project ID and code-like.");
		if (Utils.isNotNullOrEmpty(projectId) && Utils.isNotNullOrEmpty(like)) {
			LOG.trace("Attempting To Get Instance For Project And Code-Like: " + projectId + ": " + like);
			String[] paramNames = { DataConstants.PARAM_PROJECT_ID, DataConstants.PARAM_CONTENT_CD };
			String[] values = { projectId, "%" + like + "%" };
			return getWithQuery(DataConstants.GET_INSTANCES_BY_PROJECT_AND_CODE_LIKE, paramNames, values);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<InstanceJpaImpl> getWithQuery(String query, String paramName, Object value) {
		LOG.trace("Attempting to get Instances with query.");
		try {
			return (List<InstanceJpaImpl>) super.getWithQuery(query, paramName, value);
		} catch (NoSuchElementException | IllegalArgumentException e) {
			LOG.error("Problem With Query.", e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<InstanceJpaImpl> getWithQuery(String query, String[] paramNames, Object[] values) {
		LOG.trace("Attempting to get Instances with multi-parameter query.");
		try {
			return (List<InstanceJpaImpl>) super.getWithQuery(query, paramNames, values);
		} catch (NoSuchElementException | IllegalArgumentException | QueryException e) {
			LOG.error("Problem With Query.", e);
			return null;
		}
	}

	public InstanceJpaImpl initialize(InstanceJpaImpl instance) {
		if (Utils.isNotNullOrEmpty(instance)) {
			if (Utils.isNotNullOrZero(instance.getId())) {
				instance = get(instance.getId());
			} else if (Utils.isNotNullOrEmpty(instance.getContent().getContentCd())) {
				instance = get(instance.getProjectId(), instance.getContent().getContentCd());
			} else {
				LOG.trace("Not Found In The Database.");
				return instance;
			}
			if (instance instanceof AdvancedDocumentInstanceJpaImpl) {
				LOG.trace("Attempting to initialize Document children.");
				AdvancedDocumentInstanceJpaImpl d = (AdvancedDocumentInstanceJpaImpl) instance;
				super.initialize(d.getSections());
				if (Utils.isNotNullOrEmpty(d.getSections())) {
					for (SectionInstanceJpaImpl s : d.getSections()) {
						super.initialize(s.getClauses());
						if (Utils.isNotNullOrEmpty(s.getClauses())) {
							for (ClauseInstanceJpaImpl c : s.getClauses()) {
								super.initialize(c.getParagraphs());
							}
						}
					}
				}
				return d;
			} else if (instance instanceof SectionInstanceJpaImpl) {
				LOG.trace("Attempting to initialize Section children.");
				SectionInstanceJpaImpl s = (SectionInstanceJpaImpl) instance;
				super.initialize(s.getClauses());
				if (Utils.isNotNullOrEmpty(s.getClauses())) {
					for (ClauseInstanceJpaImpl c : s.getClauses()) {
						super.initialize(c.getParagraphs());
					}
				}
				return s;
			} else if (instance instanceof ClauseInstanceJpaImpl) {
				LOG.trace("Attempting to initialize Clause children.");
				ClauseInstanceJpaImpl c = (ClauseInstanceJpaImpl) instance;
				super.initialize(c.getParagraphs());
				return c;
			}
		}
		return instance;
	}

	public InstanceJpaImpl merge(InstanceJpaImpl instance) {
		LOG.trace("Attempting to merge Instance.");
		if (Utils.isNotNullOrEmpty(instance) && instance.isValid()) {
			String code = instance.getContent().getContentCd();
			String projectId = instance.getProjectId();
			if (Utils.isNotNullOrZero(instance.getId())) {
				LOG.trace("Merging existing Instance.");
				return (InstanceJpaImpl) super.merge((InstanceJpaImpl) instance);
			} else if (instance instanceof AdvancedDocumentInstanceJpaImpl) {
				LOG.trace("Attempting to merge AdvancedDocumentInstance.");
				if (!Utils.isNotNullOrEmpty(get(projectId, code))) {
					LOG.trace("Merging AdvancedDocumentInstance.");
					return (AdvancedDocumentInstanceJpaImpl) super.merge((InstanceJpaImpl) instance);
				}
			} else if (instance instanceof DocumentInstanceJpaImpl) {
				LOG.trace("Attempting to merge DocumentInstance.");
				if (!Utils.isNotNullOrEmpty(get(projectId, code))) {
					LOG.trace("Merging DocumentInstance.");
					return (DocumentInstanceJpaImpl) super.merge((InstanceJpaImpl) instance);
				}
			} else if (instance instanceof SectionInstanceJpaImpl) {
				LOG.trace("Attempting to merge SectionInstance.");
				if (!Utils.isNotNullOrEmpty(get(projectId, code))) {
					LOG.trace("Merging SectionInstance.");
					return (SectionInstanceJpaImpl) super.merge((InstanceJpaImpl) instance);
				}
			} else if (instance instanceof ClauseInstanceJpaImpl) {
				LOG.trace("Attempting to merge ClauseInstance.");
				if (!Utils.isNotNullOrEmpty(get(projectId, code))) {
					LOG.trace("Merging ClauseInstance.");
					return (ClauseInstanceJpaImpl) super.merge((InstanceJpaImpl) instance);
				}
			} else if (instance instanceof ParagraphInstanceJpaImpl) {
				LOG.trace("Attempting to merge ParagraphInstance.");
				if (!Utils.isNotNullOrEmpty(get(projectId, code))) {
					LOG.trace("Merging ParagraphInstance.");
					return (ParagraphInstanceJpaImpl) super.merge((InstanceJpaImpl) instance);
				}
			} else {
				LOG.trace("Attempting to merge unknown Instance.");
				if (!Utils.isNotNullOrEmpty(get(projectId, code))) {
					LOG.trace("Merging unknown Instance.");
					return (InstanceJpaImpl) super.merge((InstanceJpaImpl) instance);
				} else {
					LOG.trace("Found Pre-existing Instance: Project ID - " + projectId + " | Code - " + code);
					return instance;
				}
			}
		} else {
			LOG.error("Object, Content, Content ID And/Or Project ID " + "Must Not Be Null Or Empty!");
		}
		LOG.error("Unexpected Outcome! Most Likely Due To Invalid Parameters.");
		return instance;
	}

	public boolean deleteAll() {
		LOG.trace("Attempting to delete all Instances.");
		List<InstanceJpaImpl> instances = get();
		if (Utils.isNotNullOrEmpty(instances)) {
//			super.deleteAll(instances);
			instances = get();
			if (Utils.isNotNullOrEmpty(instances)) {
				String error = "Some Items Were NOT Deleted!!!\n";
				for (InstanceJpaImpl i : instances) {
					error.concat(" ----- INSTANCE ITEM WAS NOT DELETED: " + i.getContent().getContentCd() + " !!!\n");
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

	public boolean delete(InstanceJpaImpl instance) {
		if (Utils.isNotNullOrEmpty(instance)) {
			LOG.trace("Attempting To Delete Instance: " + instance.getContent().getContentCd() + " For Project + "
					+ instance.getProjectId());
			Long id = instance.getId();
//			super.delete((InstanceJpaImpl) instance);
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