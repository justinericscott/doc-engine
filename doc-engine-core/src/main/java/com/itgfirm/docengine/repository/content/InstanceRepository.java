package com.itgfirm.docengine.repository.content;

import static com.itgfirm.docengine.DocEngine.Constants.*;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;

import com.itgfirm.docengine.types.InstanceJpaImpl;

@Qualifier(AUTOWIRE_QUALIFIER_INSTANCE)
public interface InstanceRepository extends CrudRepository<InstanceJpaImpl, Long> {	
	
	InstanceJpaImpl findByProjectIdAndContentContentCd(String projectId, String contentCd);
	
	Iterable<InstanceJpaImpl> findByProjectIdAndContentContentCdLike(String projectId, String like);
}