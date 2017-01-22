package com.itgfirm.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.itgfirm.docengine.types.ParagraphJpaImpl;

public interface ParagraphRepository extends CrudRepository<ParagraphJpaImpl, Long> {
	
	@Override
	Iterable<ParagraphJpaImpl> findAll();

	ParagraphJpaImpl findByContentCd(String code);
	
	Iterable<ParagraphJpaImpl> findByContentCdLike(String like);	
}