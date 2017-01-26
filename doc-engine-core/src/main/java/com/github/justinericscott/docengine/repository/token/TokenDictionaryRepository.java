package com.github.justinericscott.docengine.repository.token;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.types.TokenDefinitionJpaImpl;

public interface TokenDictionaryRepository extends CrudRepository<TokenDefinitionJpaImpl, Long> {
	
	TokenDefinitionJpaImpl findByTokenCd(String code);
	
	Iterable<TokenDefinitionJpaImpl> findByTokenCdLike(String like);
}