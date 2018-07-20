package com.github.justinericscott.docengine.repository.content;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.models.Instance.SectionInstance;

public interface SectionInstanceRepository extends CrudRepository<SectionInstance, Long> {

	@Override
	Iterable<SectionInstance> findAll();

	Optional<SectionInstance> findOptionalByProjectIdAndContentContentCd(String projectId, String contentCd);

	Iterable<SectionInstance> findByProjectIdAndContentContentCdLike(String projectId, String like);
}