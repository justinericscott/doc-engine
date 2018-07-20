package com.github.justinericscott.docengine.repository.content;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.models.Content.Paragraph;

public interface ParagraphRepository extends CrudRepository<Paragraph, Long> {
	
	@Override
	Iterable<Paragraph> findAll();

	Optional<Paragraph> findOptionalByContentCd(String code);
	
	Iterable<Paragraph> findByContentCdLike(String like);	
}