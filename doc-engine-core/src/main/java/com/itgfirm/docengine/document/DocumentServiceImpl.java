package com.itgfirm.docengine.document;

import static com.itgfirm.docengine.util.Constants.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.itgfirm.docengine.service.AdvancedContentService;
import com.itgfirm.docengine.service.InstanceService;
import com.itgfirm.docengine.types.jpa.AdvancedDocumentInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.AdvancedDocumentJpaImpl;
import com.itgfirm.docengine.types.jpa.ContentJpaImpl;
import com.itgfirm.docengine.types.jpa.InstanceJpaImpl;
import com.itgfirm.docengine.util.Utils;

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
	public InstanceJpaImpl create(String projectId, String code) {
		if (Utils.isNotNullOrEmpty(projectId) && Utils.isNotNullOrEmpty(code)) {
			LOG.trace("Attempting To Create A New Document Instance For " + "Project ID: " + projectId
					+ " | Document Code: " + code);
			ContentJpaImpl result = advanced.findByContentCd(code);
			if (Utils.isNotNullOrEmpty(result) && result instanceof ContentJpaImpl) {
				ContentJpaImpl content = (ContentJpaImpl) result;
				if (content.getCategory().equalsIgnoreCase("COMPLEX")) {
					AdvancedDocumentJpaImpl document = (AdvancedDocumentJpaImpl) advanced.findByContentCd(code, true);
					return instance.save(new AdvancedDocumentInstanceJpaImpl(document, projectId));
				} else {
					return instance.save(new InstanceJpaImpl(content, projectId));
				}
			}
		}
		LOG.warn("Project ID And Content Code Must Not Be Null/Empty!");
		return null;
	}
}