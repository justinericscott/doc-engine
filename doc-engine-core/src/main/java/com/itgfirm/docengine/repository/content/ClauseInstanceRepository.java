package com.itgfirm.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.itgfirm.docengine.types.ClauseInstanceJpaImpl;

public interface ClauseInstanceRepository extends CrudRepository<ClauseInstanceJpaImpl, Long> {

	Iterable<ClauseInstanceJpaImpl> findAll();

	ClauseInstanceJpaImpl findByProjectIdAndContentContentCd(String projectId, String contentCd);

	Iterable<ClauseInstanceJpaImpl> findByProjectIdAndContentContentCdLike(String projectId, String like);
}