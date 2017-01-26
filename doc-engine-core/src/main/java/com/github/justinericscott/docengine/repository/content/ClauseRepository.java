package com.github.justinericscott.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.types.ClauseJpaImpl;

public interface ClauseRepository extends CrudRepository<ClauseJpaImpl, Long> {

	@Override
	Iterable<ClauseJpaImpl> findAll();
	
	ClauseJpaImpl findByContentCd(String code);
	
	Iterable<ClauseJpaImpl> findByContentCdLike(String like);	
}