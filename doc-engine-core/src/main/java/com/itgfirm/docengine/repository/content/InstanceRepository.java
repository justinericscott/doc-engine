package com.itgfirm.docengine.repository.content;

import org.springframework.data.repository.CrudRepository;

import com.itgfirm.docengine.types.InstanceJpaImpl;

public interface InstanceRepository extends CrudRepository<InstanceJpaImpl, Long> {

	@Override
	Iterable<InstanceJpaImpl> findAll();

	InstanceJpaImpl findByProjectIdAndContentContentCd(String projectId, String contentCd);

	Iterable<InstanceJpaImpl> findByProjectIdAndContentContentCdLike(String projectId, String like);
}