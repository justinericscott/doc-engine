package com.github.justinericscott.docengine.repository.content;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.models.Content.Section;

public interface SectionRepository extends CrudRepository<Section, Long> {
	
	@Override
	Iterable<Section> findAll();

	Optional<Section> findOptionalByContentCd(String code);
	
	Iterable<Section> findByContentCdLike(String like);	
}