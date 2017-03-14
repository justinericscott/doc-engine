package com.github.justinericscott.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.models.Content.Document;

//import com.github.justinericscott.docengine.models.Document;

public interface DocumentRepository extends CrudRepository<Document, Long> {
	
	@Override
	Iterable<Document> findAll();
	
	Document findByContentCd(String code);
	
	Iterable<Document> findByContentCdLike(String like);
}