package com.itgfirm.docengine.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.itgfirm.docengine.util.Utils;

/**
 * @author Justin Scott
 * 
 *         Base Class for all ORM needs.
 */
@Transactional
public abstract class AbstractHibernateOrmRepo {
	private static final Logger LOG = LogManager.getLogger(AbstractHibernateOrmRepo.class);
	@Autowired
	protected SessionFactory factory;

	public AbstractHibernateOrmRepo() {}

	/**
	 * Performs the delete operation.
	 * 
	 * @param object
	 */
	protected void delete(Object object) {
		if (Utils.isNotNullOrEmpty(object)) {
			Session session = getSession();
			session.delete(object);
			session.flush();
		} else {
			LOG.trace("Object Must Not Be Null Or Empty!");
		}
	}

	/**
	 * Performs the delete operation on a list of items.
	 * 
	 * @param list
	 */
	protected void deleteAll(List<?> list) {
		if (Utils.isNotNullOrEmpty(list)) {
			for (Object o : list) {
				delete(o);
			}
		}
	}

	/**
	 * Retrieves all records for a given Class.
	 * 
	 * @param persistentClass
	 * @return All records for a given Class
	 */
	protected List<?> get(Class<?> persistentClass) {
		if (Utils.isNotNullOrEmpty(persistentClass)) {
			List<?> result = getSession().createCriteria(persistentClass).list();
			if (Utils.isNotNullOrEmpty(result))
				return result;
		} else {
			LOG.trace("Class Must Not Be Null Or Empty!");
		}
		return null;
	}

	/**
	 * Retrieves one record for a given Class and ID.
	 * 
	 * @param persistentClass
	 * @param id
	 * @return One record for a given Class and ID
	 */
	protected Object get(Class<?> persistentClass, Serializable id) {
		if (Utils.isNotNullOrEmpty(persistentClass) && Utils.isNotNullOrZero((Long) id)) {
			Object result = getSession().get(persistentClass, id);
			if (Utils.isNotNullOrEmpty(result)) {
				return result;
			}
		} else {
			LOG.trace("Class And/Or ID Must Not Be Null, Empty Or Zero!");
		}
		return null;
	}

	/**
	 * Returns a list of objects based upon the provided query and parameter.
	 * 
	 * @param hqlQuery
	 * @param paramName
	 * @param value
	 * @return List of objects from the provided query.
	 */
	protected List<?> getWithQuery(String hqlQuery, String paramName, Object value) {
		if (Utils.isNotNullOrEmpty(hqlQuery)) {
			List<?> result = null;
			if (Utils.isNotNullOrEmpty(paramName) && Utils.isNotNullOrEmpty(value)) {
				try {
					result = createQuery(hqlQuery).setParameter(paramName, value).list();
				} catch (Throwable e) {
					LOG.trace("Problem With Query.", e);
				}
			} else if (!Utils.isNotNullOrEmpty(paramName) && !Utils.isNotNullOrEmpty(value)) {
				try {
					result = createQuery(hqlQuery).list();
				} catch (Throwable e) {
					LOG.trace("Problem With Query.", e);
				}
			}
			if (Utils.isNotNullOrEmpty(result))
				return result;
		} else {
			LOG.trace("Query Must Not Be Null Or Empty!");
		}
		return null;
	}

	/**
	 * Returns a list of objects based upon the provided query and parameters.
	 * 
	 * @param hqlQuery
	 * @param paramNames
	 * @param values
	 * @return List of objects based upon the provided query and parameters
	 */
	protected List<?> getWithQuery(String hqlQuery, String[] paramNames, Object[] values) {
		if (Utils.isNotNullOrEmpty(hqlQuery) && Utils.isNotNullOrEmpty(paramNames)
				&& Utils.isNotNullOrEmpty(values) && paramNames.length == values.length) {
			List<?> result = null;
			Query query = createQuery(hqlQuery);
			Iterator<String> k = Arrays.asList(paramNames).iterator();
			Iterator<Object> v = Arrays.asList(values).iterator();
			while (k.hasNext()) {
				String param = k.next();
				Object value = v.next();
				if (Utils.isNotNullOrEmpty(param) && Utils.isNotNullOrEmpty(value)) {
					if (value instanceof Boolean) {
						query.setBoolean(param, (Boolean) value);
					} else {
						query.setString(param, (String) value);
					}
				}
			}
			result = query.list();
			if (Utils.isNotNullOrEmpty(result))
				return result;
		} else {
			LOG.trace("Query, Parameter Names And/Or Parameter Value Must Not Be Null! "
					+ "Parameters And Values Also Must Be The Same Length!");
		}
		return null;
	}

	/**
	 * Initializes lazy-loaded objects.
	 * 
	 * @param object
	 */
	protected void initialize(Object object) {
		Hibernate.initialize(object);
	}

	/**
	 * Performs the add & update actions.
	 * 
	 * @param object
	 * @return The newly merged object
	 */
	protected Object merge(Object object) {
		if (Utils.isNotNullOrEmpty(object)) {
			Session session = getSession();
			object = session.merge(object);
			session.flush();
			return object;
		}
		LOG.trace("Object Must Not Be Null Or Empty!");
		return null;
	}

	/**
	 * Creates a Query object based upon the provided query.
	 * 
	 * @param queryString
	 * @return A Hibernate Query object.
	 */
	private Query createQuery(String queryString) {
		if (Utils.isNotNullOrEmpty(queryString)) {
			LOG.trace("Creating Query: " + queryString);
			return getSession().createQuery(queryString);
		} else {
			LOG.trace("Query Must Not Be Null Or Empty!");
			return null;
		}
	}

	/**
	 * Obtains the current Session from the Session Factory
	 * 
	 * @return An open Session
	 */
	private Session getSession() {
		return factory.getCurrentSession();
	}
}