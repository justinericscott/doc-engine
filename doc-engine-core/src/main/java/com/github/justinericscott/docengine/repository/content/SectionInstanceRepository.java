package com.github.justinericscott.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.types.SectionInstanceJpaImpl;

public interface SectionInstanceRepository extends CrudRepository<SectionInstanceJpaImpl, Long> {

	@Override
	Iterable<SectionInstanceJpaImpl> findAll();

	SectionInstanceJpaImpl findByProjectIdAndContentContentCd(String projectId, String contentCd);

	Iterable<SectionInstanceJpaImpl> findByProjectIdAndContentContentCdLike(String projectId, String like);
}