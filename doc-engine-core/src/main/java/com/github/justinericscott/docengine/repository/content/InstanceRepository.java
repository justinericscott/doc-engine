package com.github.justinericscott.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.models.Instance;

public interface InstanceRepository extends CrudRepository<Instance, Long> {

	@Override
	Iterable<Instance> findAll();

	Instance findByProjectIdAndContentContentCd(String projectId, String contentCd);

	Iterable<Instance> findByProjectIdAndContentContentCdLike(String projectId, String like);
}