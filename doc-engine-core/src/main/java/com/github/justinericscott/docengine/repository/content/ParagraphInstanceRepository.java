package com.github.justinericscott.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.models.ParagraphInstance;

public interface ParagraphInstanceRepository extends CrudRepository<ParagraphInstance, Long> {

	@Override
	Iterable<ParagraphInstance> findAll();

	ParagraphInstance findByProjectIdAndContentContentCd(String projectId, String contentCd);

	Iterable<ParagraphInstance> findByProjectIdAndContentContentCdLike(String projectId, String like);
}