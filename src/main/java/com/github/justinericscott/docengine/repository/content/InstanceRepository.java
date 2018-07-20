package com.github.justinericscott.docengine.repository.content;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.models.Instance;

public interface InstanceRepository extends CrudRepository<Instance, Long> {

	@Override
	Iterable<Instance> findAll();

	Optional<Instance> findOptionalByProjectIdAndContentContentCd(String projectId, String contentCd);

	Iterable<Instance> findByProjectIdAndContentContentCdLike(String projectId, String like);
}