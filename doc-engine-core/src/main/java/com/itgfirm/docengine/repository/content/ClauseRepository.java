package com.itgfirm.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.itgfirm.docengine.types.ClauseJpaImpl;

public interface ClauseRepository extends CrudRepository<ClauseJpaImpl, Long> {

	@Override
	Iterable<ClauseJpaImpl> findAll();
	
	ClauseJpaImpl findByContentCd(String code);
	
	Iterable<ClauseJpaImpl> findByContentCdLike(String like);	
}