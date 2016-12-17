package com.itgfirm.docengine.repository.content;

import static com.itgfirm.docengine.DocEngine.Constants.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;

import com.itgfirm.docengine.types.ContentJpaImpl;

@Qualifier(AUTOWIRE_QUALIFIER_DEFAULT)
public interface ContentRepository extends CrudRepository<ContentJpaImpl, Long> {

	/**
	 * Returns Content based on the provided content code.
	 * 
	 * @param <T>
	 * 
	 * @param contentCd
	 *            Content code to search with.
	 * @return The Content that has the provided content code.
	 */
	ContentJpaImpl findByContentCd(String contentCd);

	/**
	 * Returns contents based on the provided content code. This uses a LIKE
	 * operator, you will need to wrap your search term in %%.
	 * 
	 * @param <T>
	 * 
	 * @param like
	 *            The search term to use for content codes.
	 * @return Contents that have a match with the content code-like string in
	 *         their content code.
	 */
	Iterable<? extends ContentJpaImpl> findByContentCdLike(String like);
}