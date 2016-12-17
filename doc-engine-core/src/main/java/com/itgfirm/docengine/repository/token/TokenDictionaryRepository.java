package com.itgfirm.docengine.repository.token;

import org.springframework.data.repository.CrudRepository;

import com.itgfirm.docengine.types.TokenDefinitionJpaImpl;

public interface TokenDictionaryRepository extends CrudRepository<TokenDefinitionJpaImpl, Long> {
	
	TokenDefinitionJpaImpl findByTokenCd(String code);
	
	Iterable<TokenDefinitionJpaImpl> findByTokenCdLike(String like);
}