package com.github.justinericscott.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.models.Paragraph;

public interface ParagraphRepository extends CrudRepository<Paragraph, Long> {
	
	@Override
	Iterable<Paragraph> findAll();

	Paragraph findByContentCd(String code);
	
	Iterable<Paragraph> findByContentCdLike(String like);	
}