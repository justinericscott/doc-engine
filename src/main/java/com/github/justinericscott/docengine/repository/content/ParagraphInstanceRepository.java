package com.github.justinericscott.docengine.repository.content;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.models.Instance.ParagraphInstance;

public interface ParagraphInstanceRepository extends CrudRepository<ParagraphInstance, Long> {

	@Override
	Iterable<ParagraphInstance> findAll();

	Optional<ParagraphInstance> findOptionalByProjectIdAndContentContentCd(String projectId, String contentCd);

	Iterable<ParagraphInstance> findByProjectIdAndContentContentCdLike(String projectId, String like);
}