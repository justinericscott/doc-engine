package com.github.justinericscott.docengine.repository.token;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.models.TokenDefinition;

public interface TokenDictionaryRepository extends CrudRepository<TokenDefinition, Long> {
	
	TokenDefinition findByTokenCd(String code);
	
	Iterable<TokenDefinition> findByTokenCdLike(String like);
}