package com.github.justinericscott.docengine.repository.content;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.models.Instance.ClauseInstance;

public interface ClauseInstanceRepository extends CrudRepository<ClauseInstance, Long> {

	Iterable<ClauseInstance> findAll();

	Optional<ClauseInstance> findOptionalByProjectIdAndContentContentCd(String projectId, String contentCd);

	Iterable<ClauseInstance> findByProjectIdAndContentContentCdLike(String projectId, String like);
}