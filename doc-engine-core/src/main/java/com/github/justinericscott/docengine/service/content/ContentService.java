/**
 * TODO: License
 */
package com.github.justinericscott.docengine.service.content;

//import com.github.justinericscott.docengine.models.Clause;
import com.github.justinericscott.docengine.models.Content;
import com.github.justinericscott.docengine.models.Content.Clause;
import com.github.justinericscott.docengine.models.Content.Document;
import com.github.justinericscott.docengine.models.Content.Paragraph;
import com.github.justinericscott.docengine.models.Content.Section;
import com.github.justinericscott.docengine.models.Contents;
//import com.github.justinericscott.docengine.models.Document;
//import com.github.justinericscott.docengine.models.Paragraph;
//import com.github.justinericscott.docengine.models.Section;

/**
 * @author Justin Scott
 * 
 *         Represents the Content Service as a whole. The Content Service will
 *         be a "base" service, it will only interact with its REST and Repo
 *         counterparts.
 * 
 */
public interface ContentService {

	boolean delete(Content content);
	
	boolean delete(Contents content);
	
	boolean delete(Content[] content);

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

	Content findByCode(String code);
	
	<T> T findByCode(String code, Class<T> type);
	
	<T> T findByCode(String code, Class<T> type, boolean eagerKids);

	Contents findByCodeLike(String like);
	
	<T> T findByCodeLike(String like, Class<T> type);
	
	<T> T findByCodeLike(String like, Class<T> type, boolean eagerKids);

	Content findOne(Long id);
	
	<T> T findOne(Long id, Class<T> type);
	
	<T> T findOne(Long id, Class<T> type, boolean eagerKids);

	<T> T getChildren(Long id, Class<T> type);

	<T> T getChildren(Long id, Class<T> type, boolean eagerKids);

	<T, P> T getChildren(P content, Class<T> type);

	<T, P> T getChildren(P content, Class<T> type, boolean eagerKids);

	<T> T getChildren(String code, Class<T> type);

	<T> T getChildren(String code, Class<T> type, boolean eagerKids);

	Content save(Content content);
	
	Iterable<?> save(Iterable<?> objects);
	
	<T> Iterable<T> save(final Iterable<T> objects, Class<T> type);
	
	Contents save(Contents contents);
	
	Document save(Document document);
	
	Section save(Section section);

	Clause save(Clause clause);

	Paragraph save(Paragraph paragraph);
}