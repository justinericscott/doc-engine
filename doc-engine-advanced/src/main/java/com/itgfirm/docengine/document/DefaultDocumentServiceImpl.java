/**
 * TODO: License
 */
package com.itgfirm.docengine.document;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itgfirm.docengine.content.AdvancedContentService;
import com.itgfirm.docengine.instance.InstanceService;
import com.itgfirm.docengine.types.AdvancedDocument;
import com.itgfirm.docengine.types.AdvancedDocumentInstance;
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.Instance;
import com.itgfirm.docengine.types.jpa.AdvancedDocumentInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.InstanceJpaImpl;
import com.itgfirm.docengine.util.Utils;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Service
public class DefaultDocumentServiceImpl implements DocumentService {
	private static final Logger LOG = LogManager.getLogger(DefaultDocumentServiceImpl.class);
	@Autowired
	private InstanceService instanceService;
	@Autowired
	private AdvancedContentService contentService;

	/**
	 * TODO: Description
	 */
	public DefaultDocumentServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.document.DocumentService#create( java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Instance create(String projectId, String code) {
		if (Utils.isNotNullOrEmpty(projectId) && Utils.isNotNullOrEmpty(code)) {
			LOG.trace("Attempting To Create A New Document Instance For " + "Project ID: "
					+ projectId + " | Document Code: " + code);
			Object result = contentService.get(code);
			if (Utils.isNotNullOrEmpty(result) && result instanceof Content) {
				Content content = (Content) result;
				if (content.getCategory().equalsIgnoreCase("COMPLEX")) {
					AdvancedDocument document = (AdvancedDocument) contentService.get(code, true);
					return (AdvancedDocumentInstance) instanceService
							.merge(new AdvancedDocumentInstanceJpaImpl(document, projectId));
				} else {
					return (Instance) instanceService.merge(new InstanceJpaImpl(content,
							projectId));
				}
			}
		}
		LOG.warn("Project ID And Content Code Must Not Be Null/Empty!");
		return null;
	}
}