package com.itgfirm.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.itgfirm.docengine.types.ContentJpaImpl;

public interface ContentRepository extends CrudRepository<ContentJpaImpl, Long> {

	@Override
	Iterable<ContentJpaImpl> findAll();
	
	Iterable<ContentJpaImpl> findByCategory(String type);
	
	ContentJpaImpl findByContentCd(String contentCd);

	Iterable<ContentJpaImpl> findByContentCdLike(String like);
}