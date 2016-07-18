package com.itgfirm.docengine.instance;

import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itgfirm.docengine.data.DataConstants;
import com.itgfirm.docengine.types.Instance;
import com.itgfirm.docengine.types.jpa.AdvancedDocumentInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.ClauseInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.SectionInstanceJpaImpl;
import com.itgfirm.docengine.util.Utils;

/**
 * @author Justin Scott
 * 
 *         Default Content Instance Service
 */
@Service
class DefaultInstanceServiceImpl implements InstanceService {
	private static final Logger LOG = LogManager.getLogger(DefaultInstanceServiceImpl.class);
	@Autowired
	private InstanceRepo repo;

	public DefaultInstanceServiceImpl() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceService#get()
	 */
	@Override
	public List<Instance> get() {
		return repo.get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceService#get( java.lang.Long)
	 */
	@Override
	public Instance get(Long id) {
		return get(id, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceService#get(java.lang.Long, boolean)
	 */
	@Override
	public Instance get(Long id, boolean eagerKids) {
		if (eagerKids) {
			return repo.initialize(repo.get(id));
		} else {
			return repo.get(id);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceService#get(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Instance get(String projectId, String code) {
		return get(projectId, code, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceService#get( java.lang.String,
	 * java.lang.String, java.lang.Class, boolean)
	 */
	@Override
	public Instance get(String projectId, String code, boolean eagerKids) {
		if (eagerKids) {
			return repo.initialize(repo.get(projectId, code));
		} else {
			Instance instance = repo.get(projectId, code);
			return instance;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceService#getChildren( java.lang.Long)
	 */
	@Override
	public List<? extends Instance> getChildren(Long id) {
		return getChildren(id, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceService#getChildren( java.lang.Long,
	 * boolean)
	 */
	@Override
	public List<? extends Instance> getChildren(Long id, boolean eagerKids) {
		if (Utils.isNotNullOrZero(id)) {
			Instance instance = get(id);
			if (Utils.isNotNullOrEmpty(instance)) {
				return getChildren(instance, eagerKids);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceService#getChildren( java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<? extends Instance> getChildren(String projectId, String code) {
		return getChildren(projectId, code, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceService#getChildren( java.lang.String,
	 * java.lang.String, boolean)
	 */
	@Override
	public List<? extends Instance> getChildren(String projectId, String code,
			boolean eagerKids) {
		if (Utils.isNotNullOrEmpty(code) && Utils.isNotNullOrEmpty(projectId)) {
			Instance result = get(projectId, code);
			return getChildren(result, eagerKids);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceService#getChildren(
	 * com.itgfirm.docengine.types.jpa.jpa.Instance)
	 */
	@Override
	public List<? extends Instance> getChildren(Instance instance) {
		return getChildren(instance, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceService#getChildren(
	 * com.itgfirm.docengine.types.jpa.jpa.Instance, boolean)
	 */
	@Override
	public List<? extends Instance> getChildren(Instance instance, boolean eagerKids) {
		if (Utils.isNotNullOrEmpty(instance)) {
			List<? extends Instance> children = null;
			if (instance.getClass().equals(AdvancedDocumentInstanceJpaImpl.class)) {
				children =
						repo.getWithQuery(DataConstants.GET_SECTION_INSTANCES_BY_PARENT,
								DataConstants.PARAM_DOCUMENT, instance);
			} else if (instance.getClass().equals(SectionInstanceJpaImpl.class)) {
				children =
						repo.getWithQuery(DataConstants.GET_CLAUSE_INSTANCES_BY_PARENT,
								DataConstants.PARAM_SECTION, instance);
			} else if (instance.getClass().equals(ClauseInstanceJpaImpl.class)) {
				children =
						repo.getWithQuery(DataConstants.GET_PARAGRAPH_INSTANCES_BY_PARENT,
								DataConstants.PARAM_CLAUSE, instance);
			}
			if (eagerKids && Utils.isNotNullOrEmpty(children)) {
				@SuppressWarnings("unchecked")
				ListIterator<Instance> iter = (ListIterator<Instance>) children.listIterator();
				while (iter.hasNext()) {
					iter.set(repo.initialize(iter.next()));
				}
			}
			return children;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceService#getByProjectAndCodeLike(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public List<Instance> getByProjectAndCodeLike(String projectId, String like) {
		return repo.getByProjectAndCodeLike(projectId, like);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceService#merge(
	 * com.itgfirm.docengine.types.jpa.jpa.Instance)
	 */
	@Override
	public Instance merge(Instance instance) {
		return repo.merge(instance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceService#merge(java.util.List)
	 */
	@Override
	public List<Instance> merge(List<Instance> instances) {
		if (Utils.isNotNullOrEmpty(instances)) {
			LOG.trace("Attempting To Merge List Of Instances: " + instances.size() + " Items.");
			ListIterator<Instance> iter = instances.listIterator();
			while (iter.hasNext()) {
				iter.set(merge(iter.next()));
			}
			return instances;
		}
		LOG.trace("List Of Instances Must Not Be Null!");
		return instances;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceService#delete(java.lang.Long)
	 */
	@Override
	public boolean delete(Long id) {
		return delete(get(id));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceService#delete( java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public boolean delete(String projectId, String code) {
		LOG.trace("Attempting to delete Instance by project ID and code.");
		return delete(get(projectId, code));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.instance.InstanceService#delete(
	 * com.itgfirm.docengine.types.jpa.jpa.Instance)
	 */
	@Override
	public boolean delete(Instance instance) {
		return repo.delete(instance);
	}

}