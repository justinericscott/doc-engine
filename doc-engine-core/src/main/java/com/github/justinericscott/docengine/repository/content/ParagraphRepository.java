package com.github.justinericscott.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.types.ParagraphJpaImpl;

public interface ParagraphRepository extends CrudRepository<ParagraphJpaImpl, Long> {
	
	@Override
	Iterable<ParagraphJpaImpl> findAll();

	ParagraphJpaImpl findByContentCd(String code);
	
	Iterable<ParagraphJpaImpl> findByContentCdLike(String like);	
}