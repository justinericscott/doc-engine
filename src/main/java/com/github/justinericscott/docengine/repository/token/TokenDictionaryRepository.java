package com.github.justinericscott.docengine.repository.token;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.models.TokenDefinition;

public interface TokenDictionaryRepository extends CrudRepository<TokenDefinition, Long> {
	
	Optional<TokenDefinition> findOptionalByTokenCd(String code);
	
	Iterable<TokenDefinition> findByTokenCdLike(String like);
}