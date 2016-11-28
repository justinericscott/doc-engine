package com.itgfirm.docengine.content;



/**
 * @author Justin Scott
 * 
 *         Default implementation of the Content Repository.
 */
@Deprecated
class AdvancedContentRepoImpl  extends DefaultContentRepoImpl  {

	public AdvancedContentRepoImpl() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.content.ContentRepo#initialize(
	 * com.itgfirm.docengine.types.jpa.jpa.Content)
	 */
//	public ContentJpaImpl initialize(ContentJpaImpl content) {
//		if (isNotNullOrEmpty(content)) {
//			if (isNotNullOrZero(content.getId())) {
//				content = get(content.getId());
//			} else if (isNotNullOrEmpty(content.getContentCd())) {
//				content = get(content.getContentCd());
//			} else {
//				return content;
//			}
//			if (content instanceof AdvancedDocumentJpaImpl) {
//				AdvancedDocumentJpaImpl d = (AdvancedDocumentJpaImpl) content;
//				super.initialize(d.getSections());
//				if (isNotNullOrEmpty(d.getSections())) {
//					for (SectionJpaImpl s : d.getSections()) {
//						super.initialize(s.getClauses());
//						if (isNotNullOrEmpty(s.getClauses())) {
//							for (ClauseJpaImpl c : s.getClauses()) {
//								super.initialize(c.getParagraphs());
//							}
//						}
//					}
//				}
//				return d;
//			} else if (content instanceof SectionJpaImpl) {
//				SectionJpaImpl s = (SectionJpaImpl) content;
//				super.initialize(s.getClauses());
//				if (isNotNullOrEmpty(s.getClauses())) {
//					for (ClauseJpaImpl c : s.getClauses()) {
//						super.initialize(c.getParagraphs());
//					}
//				}
//				return s;
//			} else if (content instanceof ClauseJpaImpl) {
//				ClauseJpaImpl c = (ClauseJpaImpl) content;
//				super.initialize(c.getParagraphs());
//				return c;
//			}
//		}
//		return content;
//	}
}