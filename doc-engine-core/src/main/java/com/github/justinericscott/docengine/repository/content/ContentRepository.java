package com.github.justinericscott.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.types.ContentJpaImpl;

public interface ContentRepository extends CrudRepository<ContentJpaImpl, Long> {

	@Override
	Iterable<ContentJpaImpl> findAll();
	
	Iterable<ContentJpaImpl> findByCategory(String type);
	
	ContentJpaImpl findByContentCd(String contentCd);

	Iterable<ContentJpaImpl> findByContentCdLike(String like);
}