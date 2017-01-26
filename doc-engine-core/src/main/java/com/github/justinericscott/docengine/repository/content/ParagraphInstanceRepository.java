package com.github.justinericscott.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.github.justinericscott.docengine.types.ParagraphInstanceJpaImpl;

public interface ParagraphInstanceRepository extends CrudRepository<ParagraphInstanceJpaImpl, Long> {

	@Override
	Iterable<ParagraphInstanceJpaImpl> findAll();

	ParagraphInstanceJpaImpl findByProjectIdAndContentContentCd(String projectId, String contentCd);

	Iterable<ParagraphInstanceJpaImpl> findByProjectIdAndContentContentCdLike(String projectId, String like);
}