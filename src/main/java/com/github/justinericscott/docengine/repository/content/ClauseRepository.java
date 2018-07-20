package com.github.justinericscott.docengine.repository.content;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.models.Content.Clause;

public interface ClauseRepository extends CrudRepository<Clause, Long> {

	@Override
	Iterable<Clause> findAll();
	
	Optional<Clause> findOptionalByContentCd(String code);
	
	Iterable<Clause> findByContentCdLike(String like);	
}