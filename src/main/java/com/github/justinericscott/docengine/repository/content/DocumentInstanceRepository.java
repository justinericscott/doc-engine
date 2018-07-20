package com.github.justinericscott.docengine.repository.content;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.models.Instance.DocumentInstance;

public interface DocumentInstanceRepository extends CrudRepository<DocumentInstance, Long> {

	@Override
	Iterable<DocumentInstance> findAll();

	Optional<DocumentInstance> findOptionalByProjectIdAndContentContentCd(String projectId, String contentCd);

	Iterable<DocumentInstance> findByProjectIdAndContentContentCdLike(String projectId, String like);
}