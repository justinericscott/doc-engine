package com.itgfirm.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.itgfirm.docengine.types.DocumentJpaImpl;

public interface DocumentRepository extends CrudRepository<DocumentJpaImpl, Long> {
	
	@Override
	Iterable<DocumentJpaImpl> findAll();
	
	DocumentJpaImpl findByContentCd(String code);
	
	Iterable<DocumentJpaImpl> findByContentCdLike(String like);
}