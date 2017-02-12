package com.github.justinericscott.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.models.Section;

public interface SectionRepository extends CrudRepository<Section, Long> {
	
	@Override
	Iterable<Section> findAll();

	Section findByContentCd(String code);
	
	Iterable<Section> findByContentCdLike(String like);	
}