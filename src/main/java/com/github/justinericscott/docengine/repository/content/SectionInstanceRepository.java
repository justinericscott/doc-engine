package com.github.justinericscott.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.models.Instance.SectionInstance;

//import com.github.justinericscott.docengine.models.SectionInstance;

public interface SectionInstanceRepository extends CrudRepository<SectionInstance, Long> {

	@Override
	Iterable<SectionInstance> findAll();

	SectionInstance findByProjectIdAndContentContentCd(String projectId, String contentCd);

	Iterable<SectionInstance> findByProjectIdAndContentContentCdLike(String projectId, String like);
}