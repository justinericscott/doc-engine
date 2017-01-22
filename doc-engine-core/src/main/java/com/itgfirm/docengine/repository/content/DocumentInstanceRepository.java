package com.itgfirm.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.itgfirm.docengine.types.DocumentInstanceJpaImpl;

public interface DocumentInstanceRepository extends CrudRepository<DocumentInstanceJpaImpl, Long> {

	@Override
	Iterable<DocumentInstanceJpaImpl> findAll();

	DocumentInstanceJpaImpl findByProjectIdAndContentContentCd(String projectId, String contentCd);

	Iterable<DocumentInstanceJpaImpl> findByProjectIdAndContentContentCdLike(String projectId, String like);
}