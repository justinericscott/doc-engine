package com.github.justinericscott.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.types.SectionJpaImpl;

public interface SectionRepository extends CrudRepository<SectionJpaImpl, Long> {
	
	@Override
	Iterable<SectionJpaImpl> findAll();

	SectionJpaImpl findByContentCd(String code);
	
	Iterable<SectionJpaImpl> findByContentCdLike(String like);	
}