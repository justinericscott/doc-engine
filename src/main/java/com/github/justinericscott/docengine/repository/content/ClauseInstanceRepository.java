package com.github.justinericscott.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.models.Instance.ClauseInstance;

//import com.github.justinericscott.docengine.models.ClauseInstance;

public interface ClauseInstanceRepository extends CrudRepository<ClauseInstance, Long> {

	Iterable<ClauseInstance> findAll();

	ClauseInstance findByProjectIdAndContentContentCd(String projectId, String contentCd);

	Iterable<ClauseInstance> findByProjectIdAndContentContentCdLike(String projectId, String like);
}