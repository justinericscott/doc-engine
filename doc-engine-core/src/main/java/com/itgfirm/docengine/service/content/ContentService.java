/**
 * TODO: License
 */
package com.itgfirm.docengine.service.content;

import com.itgfirm.docengine.types.ClauseJpaImpl;
import com.itgfirm.docengine.types.ContentJpaImpl;
import com.itgfirm.docengine.types.Contents;
import com.itgfirm.docengine.types.DocumentJpaImpl;
import com.itgfirm.docengine.types.ParagraphJpaImpl;
import com.itgfirm.docengine.types.SectionJpaImpl;

/**
 * @author Justin Scott
 * 
 *         Represents the Content Service as a whole. The Content Service will
 *         be a "base" service, it will only interact with its REST and Repo
 *         counterparts.
 * 
 */
public interface ContentService {

	boolean delete(ContentJpaImpl content);
	
	boolean delete(Contents content);
	
	boolean delete(ContentJpaImpl[] content);

	boolean delete(Long id);
	
	boolean delete(Long id, Class<?> type);

	boolean delete(String code);

	<T> boolean delete(String code, Class<T> type);
	
	<T> boolean delete(T object, Class<T> type);
	
	boolean deleteAll();

	<T> boolean deleteByCodeLike(String like, Class<T> type);

	Contents findAll();
	
	<T> T findAll(Class<T> type);
	
	<T> T findAll(Class<T> type, boolean eagerKids);

	ContentJpaImpl findByCode(String code);
	
	<T> T findByCode(String code, Class<T> type);
	
	<T> T findByCode(String code, Class<T> type, boolean eagerKids);

	Contents findByCodeLike(String like);
	
	<T> T findByCodeLike(String like, Class<T> type);
	
	<T> T findByCodeLike(String like, Class<T> type, boolean eagerKids);

	ContentJpaImpl findOne(Long id);
	
	<T> T findOne(Long id, Class<T> type);
	
	<T> T findOne(Long id, Class<T> type, boolean eagerKids);

	<T> T getChildren(Long id, Class<T> type);

	<T> T getChildren(Long id, Class<T> type, boolean eagerKids);

	<T, P> T getChildren(P content, Class<T> type);

	<T, P> T getChildren(P content, Class<T> type, boolean eagerKids);

	<T> T getChildren(String code, Class<T> type);

	<T> T getChildren(String code, Class<T> type, boolean eagerKids);

	ContentJpaImpl save(ContentJpaImpl content);
	
	Contents save(Contents contents);
	
	DocumentJpaImpl save(DocumentJpaImpl document);
	
	SectionJpaImpl save(SectionJpaImpl section);

	ClauseJpaImpl save(ClauseJpaImpl clause);

	ParagraphJpaImpl save(ParagraphJpaImpl paragraph);
}