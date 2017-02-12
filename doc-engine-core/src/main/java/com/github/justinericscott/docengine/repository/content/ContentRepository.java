package com.github.justinericscott.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.models.Content;

public interface ContentRepository extends CrudRepository<Content, Long> {

	@Override
	Iterable<Content> findAll();
	
	Iterable<Content> findByCategory(String type);
	
	Content findByContentCd(String contentCd);

	Iterable<Content> findByContentCdLike(String like);
}