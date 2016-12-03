package com.itgfirm.docengine.service;

import static com.itgfirm.docengine.DocEngine.Constants.*;
import static com.itgfirm.docengine.util.Utils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.itgfirm.docengine.types.jpa.AdvancedDocumentInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.AdvancedDocumentJpaImpl;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.types.jpa.InstanceJpaImpl;

@Service
public class DocumentServiceImpl implements DocumentService {
	private static final Logger LOG = LoggerFactory.getLogger(DocumentServiceImpl.class);

	@Autowired
	@Qualifier(AUTOWIRE_QUALIFIER_INSTANCE)
	private InstanceService instance;

	@Autowired
	@Qualifier(AUTOWIRE_QUALIFIER_ADVANCED)
	private AdvancedContentService advanced;

	@Override
	public final InstanceJpaImpl create(final String projectId, final String code) {
		if (isNotNullOrEmpty(projectId) && isNotNullOrEmpty(code)) {
			final ContentJpaImpl content = advanced.findByContentCd(code);
			if (isNotNullOrEmpty(content) && content.isValid(true)) {
				if (content instanceof AdvancedDocumentJpaImpl) {
					final AdvancedDocumentJpaImpl document = (AdvancedDocumentJpaImpl) content;
					return instance.save(new AdvancedDocumentInstanceJpaImpl(document, projectId));
				} else if (content instanceof ContentJpaImpl) {
					return instance.save(new InstanceJpaImpl(content, projectId));
				} else {
					LOG.error("The content found is either not a base class instance or an advanced document class instance!");
				}
			} else {
				LOG.debug("Project ID And Content Code Must Not Be Null/Empty!");		
			}
		}		
		return null;
	}
}