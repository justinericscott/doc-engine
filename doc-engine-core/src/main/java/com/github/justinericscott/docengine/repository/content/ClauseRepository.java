package com.github.justinericscott.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.models.Clause;

public interface ClauseRepository extends CrudRepository<Clause, Long> {

	@Override
	Iterable<Clause> findAll();
	
	Clause findByContentCd(String code);
	
	Iterable<Clause> findByContentCdLike(String like);	
}