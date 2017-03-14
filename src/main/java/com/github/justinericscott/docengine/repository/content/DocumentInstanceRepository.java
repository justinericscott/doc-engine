package com.github.justinericscott.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.models.Instance.DocumentInstance;

//import com.github.justinericscott.docengine.models.DocumentInstance;

public interface DocumentInstanceRepository extends CrudRepository<DocumentInstance, Long> {

	@Override
	Iterable<DocumentInstance> findAll();

	DocumentInstance findByProjectIdAndContentContentCd(String projectId, String contentCd);

	Iterable<DocumentInstance> findByProjectIdAndContentContentCdLike(String projectId, String like);
}