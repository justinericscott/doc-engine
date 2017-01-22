package com.itgfirm.docengine.service.content;

import static com.itgfirm.docengine.DocEngine.Constants.*;
import static com.itgfirm.docengine.util.Utils.isNotNullOrEmpty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.itgfirm.docengine.types.DocumentInstanceJpaImpl;
import com.itgfirm.docengine.types.DocumentJpaImpl;
import com.itgfirm.docengine.types.ContentJpaImpl;
import com.itgfirm.docengine.types.InstanceJpaImpl;

@SuppressWarnings("unused")
//@Service
@Deprecated
class InitializerServiceImpl implements InitializerService {
	private static final Logger LOG = LoggerFactory.getLogger(InitializerServiceImpl.class);

//	@Autowired
//	@Qualifier(AUTOWIRE_QUALIFIER_INSTANCE)
//	private InstanceService instance;

//	@Autowired
//	@Qualifier(AUTOWIRE_QUALIFIER_DOCUMENT)
//	private DocumentService advanced;

	public InitializerServiceImpl() {
		// Default constructor for Spring
	} 
	
//	@Override
//	public final InstanceJpaImpl create(final String projectId, final String code) {
//		if (isNotNullOrEmpty(projectId) && isNotNullOrEmpty(code)) {
//			final ContentJpaImpl content = advanced.findByCode(code);
//			if (isNotNullOrEmpty(content) && content.isValid(true)) {
//				if (content instanceof DocumentJpaImpl) {
//					final DocumentJpaImpl document = (DocumentJpaImpl) content;
////					return instance.save(new DocumentInstanceJpaImpl(document, projectId));
//				} else if (content instanceof ContentJpaImpl) {
////					return instance.save(new InstanceJpaImpl(content, projectId));
//				} else {
//					LOG.error("The content found is either not a base class instance or an advanced document class instance!");
//				}
//			} else {
//				LOG.debug("Project ID And Content Code Must Not Be Null/Empty!");		
//			}
//		}		
//		return null;
//	}
}