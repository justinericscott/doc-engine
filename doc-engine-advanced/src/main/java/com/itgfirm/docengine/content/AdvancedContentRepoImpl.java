package com.itgfirm.docengine.content;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.itgfirm.docengine.types.AdvancedDocument;
import com.itgfirm.docengine.types.Clause;
import com.itgfirm.docengine.types.Content;
import com.itgfirm.docengine.types.Section;
import com.itgfirm.docengine.util.Utils;

/**
 * @author Justin Scott
 * 
 *         Default implementation of the Content Repository.
 */
@Repository @Qualifier("advanced")
class AdvancedContentRepoImpl extends DefaultContentRepoImpl implements AdvancedContentRepo {
	private static final Logger LOG = LogManager.getLogger(AdvancedContentRepoImpl.class);

	public AdvancedContentRepoImpl() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentRepo#initialize(
	 * com.itgfirm.docengine.types.jpa.jpa.Content)
	 */
	@Override
	public Content initialize(Content content) {
		if (Utils.isNotNullOrEmpty(content)) {
			if (Utils.isNotNullOrZero(content.getId())) {
				content = get(content.getId());
			} else if (Utils.isNotNullOrEmpty(content.getContentCd())) {
				content = get(content.getContentCd());
			} else {
				LOG.trace("Not Found In The Database.");
				return content;
			}
			if (content instanceof AdvancedDocument) {
				LOG.trace("Attempting to initialize Document children.");
				AdvancedDocument d = (AdvancedDocument) content;
				super.initialize(d.getSections());
				if (Utils.isNotNullOrEmpty(d.getSections())) {
					for (Section s : d.getSections()) {
						super.initialize(s.getClauses());
						if (Utils.isNotNullOrEmpty(s.getClauses())) {
							for (Clause c : s.getClauses()) {
								super.initialize(c.getParagraphs());
							}
						}
					}
				}
				return d;
			} else if (content instanceof Section) {
				LOG.trace("Attempting to initialize Section children.");
				Section s = (Section) content;
				super.initialize(s.getClauses());
				if (Utils.isNotNullOrEmpty(s.getClauses())) {
					for (Clause c : s.getClauses()) {
						super.initialize(c.getParagraphs());
					}
				}
				return s;
			} else if (content instanceof Clause) {
				LOG.trace("Attempting to initialize Clause children.");
				Clause c = (Clause) content;
				super.initialize(c.getParagraphs());
				return c;
			}
		}
		return content;
	}
}