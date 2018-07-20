package com.github.justinericscott.docengine.repository.content;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.models.Content.Document;

//import com.github.justinericscott.docengine.models.Document;

public interface DocumentRepository extends CrudRepository<Document, Long> {
	
	@Override
	Iterable<Document> findAll();
	
	Optional<Document> findOptionalByContentCd(String code);
	
	Iterable<Document> findByContentCdLike(String like);
}