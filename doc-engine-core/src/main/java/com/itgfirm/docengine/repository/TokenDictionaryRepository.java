package com.itgfirm.docengine.repository;

import org.springframework.data.repository.CrudRepository;

import com.itgfirm.docengine.types.jpa.TokenDefinitionJpaImpl;

public interface TokenDictionaryRepository extends CrudRepository<TokenDefinitionJpaImpl, Long> {
	
	TokenDefinitionJpaImpl findByTokenCd(String code);
	
	Iterable<TokenDefinitionJpaImpl> findByTokenCdLike(String like);
}