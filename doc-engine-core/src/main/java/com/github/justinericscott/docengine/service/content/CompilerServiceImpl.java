/**
 * TODO: License
 */
package com.github.justinericscott.docengine.service.content;

import static com.github.justinericscott.docengine.service.content.CompilerServiceImpl.CompilerConstants.*;
import static com.github.justinericscott.docengine.util.Utils.isNotNullOrEmpty;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

import org.springframework.stereotype.Service;

import com.github.justinericscott.docengine.types.ClauseInstanceJpaImpl;
import com.github.justinericscott.docengine.types.DocumentInstanceJpaImpl;
import com.github.justinericscott.docengine.types.ParagraphInstanceJpaImpl;
import com.github.justinericscott.docengine.types.ParagraphJpaImpl;
import com.github.justinericscott.docengine.types.SectionInstanceJpaImpl;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Service
class CompilerServiceImpl implements CompilerService {

	@Override
	public String compileDocument(DocumentInstanceJpaImpl document) {
		// TODO: Trace log entry
		StringBuilder sb = new StringBuilder();
		getAllSectionsForDocumentHTML(sb, document);
		return sb.toString();
	}

	private void appendClauseHTML(StringBuilder sb, ClauseInstanceJpaImpl clause) {
		// TODO: Trace log entry
		StringBuilder clauseStr = null;
		if (STATUS_AUTO_OUT.equals(clause.getStatusCd()) || STATUS_MAN_OUT.equals(clause.getStatusCd())) {
			clauseStr = new StringBuilder();
			clauseStr.append(clause.getClause().getContentNumber());
			clauseStr.append(htmlTab(3));
			if (clause.isStrikeHeader()) {
				String clauseHeader = "";
				if (isNotNullOrEmpty(clause.getBody())) {
					clauseHeader = clause.getBody();
				} else if (clause.getClause() != null) {
					clauseHeader = clause.getClause().getBody();
				}
				clauseStr.append(htmlTag("span", clauseHeader, "strike"));
				clauseStr.append(htmlTab(3));
			}
			clauseStr.append("INTENTIONALLY DELETED");
			sb.append(htmlTag("h2", clauseStr.toString(), ""));
		} else {
			clauseStr = new StringBuilder();
			clauseStr.append(clause.getClause().getContentNumber());
			clauseStr.append(htmlTab(3));
			if (isNotNullOrEmpty(clause.getBody())) {
				clauseStr.append(clause.getBody());
			} else if (clause.getClause() != null) {
				clauseStr.append(clause.getClause().getBody());
			}
			sb.append("<div class=\"group\">");
			sb.append(htmlTag("h2", clauseStr.toString(), ""));
		}
	}

	private String appendParagraphHTML(ParagraphInstanceJpaImpl instance, String recursionString,
			boolean isParentIncluded) {
		// TODO: Trace log entry
		StringBuilder returnString = new StringBuilder();
		ParagraphJpaImpl source = (ParagraphJpaImpl) instance.getParagraph();
		if (source != null) {
			boolean hasCustomBody = isNotNullOrEmpty(instance.getBody());
			boolean isIncluded = (instance.getStatusCd().equals(STATUS_AUTO_IN)
					|| instance.getStatusCd().equals(STATUS_MAN_IN));
			boolean isParent = source.isParent();
			boolean isOption = source.isOption();
			boolean isSubPara = source.isSubPara();
			boolean isTable = source.getContentCd().contains("table");
			String tag;
			if (isSubPara) {
				tag = "li";
			} else if (isTable) {
				tag = null;
			} else {
				tag = "p";
			}
			String blueText = "";
			if (recursionString == null)
				recursionString = "";
			String templateBody = null;
			if (!isIncluded && !isOption && isParentIncluded) {
				if (tag == null)
					tag = "p";
				templateBody = htmlTag(tag, "INTENTIONALLY DELETED");
			} else if (isIncluded && hasCustomBody) {
				if (isParent && tag.equalsIgnoreCase("p")) {
					templateBody = htmlTag(tag, instance.getBody()) + recursionString;
				} else {
					templateBody = htmlTag(tag, instance.getBody() + recursionString);
				}
			} else if (isIncluded) {
				if (isParent && tag.equalsIgnoreCase("p")) {
					templateBody = htmlTag(tag, source.getBody()) + recursionString;
				} else {
					templateBody = htmlTag(tag, source.getBody() + recursionString);
				}
			}
			if (templateBody != null) {
				returnString.append(blueText + templateBody);
			}
		}
		return returnString.toString();
	}
	
	private void appendSectionHTML(StringBuilder sb, SectionInstanceJpaImpl section, boolean first) {
		// TODO: Trace log entry
		String content = String.format("SECTION%s%s%s%s", htmlTab(3),
				section.getSection().getContentNumber(), htmlTab(3), section.getSection().getBody());
		if (!first)
			sb.append(htmlSelfCloseTag("br", "break"));
		sb.append(htmlSelfCloseTag("hr"));
		sb.append(htmlTag("h1", content, ""));
		sb.append(htmlSelfCloseTag("hr"));
	}

	private void getAllClausesForSectionHTML(StringBuilder sb, SectionInstanceJpaImpl section) {
		// TODO: Trace log entry
		for (ClauseInstanceJpaImpl clause : section.getClauses()) {
			appendClauseHTML(sb, clause);
			if (STATUS_AUTO_IN.equals(clause.getStatusCd()) || STATUS_MAN_IN.equals(clause.getStatusCd())) {
				if (clause.getClause() != null) {
					getAllParagraphsForClauseHTML(sb, clause);
				}
			}
		}
	}

	private void getAllParagraphsForClauseHTML(StringBuilder sb, ClauseInstanceJpaImpl clause) {
		// TODO: Trace log entry
		Iterator<ParagraphInstanceJpaImpl> it = clause.getParagraphs().iterator();
		while (it.hasNext()) {
			sb.append(getAllParagraphInstancesRecurse(it, false));
		}
	}

	private String getAllParagraphInstancesRecurse(Iterator<ParagraphInstanceJpaImpl> it, boolean isNestedList) {
		// TODO: Trace log entry
		StringBuilder returnString = new StringBuilder();
		ParagraphInstanceJpaImpl parentInstance = null;
		ParagraphInstanceJpaImpl currentInstance = null;
		while (it.hasNext()) {
			currentInstance = it.next();
			ParagraphJpaImpl source = (ParagraphJpaImpl) currentInstance.getParagraph();
			if (source != null) {
				// Initialize flags
				boolean isFirst = source.isFirst();
				boolean isFirstParagraphInClause = (source.getOrderBy() == 1);
				boolean isIncluded = (currentInstance.getStatusCd().equals(STATUS_AUTO_IN)
						|| currentInstance.getStatusCd().equals(STATUS_MAN_IN));
				boolean isLast = source.isLast();
				boolean isOption = source.isOption();
				boolean isParent = source.isParent();
				boolean isParentIncluded = true;
				boolean isSubPara = source.isSubPara();
				if (parentInstance != null) {
					isParentIncluded = (parentInstance.getStatusCd().equals(STATUS_AUTO_IN)
							|| parentInstance.getStatusCd().equals(STATUS_MAN_IN));
				}
				boolean isTable = source.getContentCd().contains("table");
				// Opens Ordered List tag, if necessary
				if (isFirst && (!isOption || (isOption && isIncluded))) {
					if (!isTable) { // Tables cannot be in ordered lists
						if (!isFirstParagraphInClause) {
							isNestedList = true;
						}
						returnString.append("<ol>");
					}
				}
				// Sets parent instance
				// Recursively builds any included children paragraphs
				String recursionString = "";
				if (isParent && (!isOption || (isOption && isIncluded))) {
					parentInstance = currentInstance;
					recursionString = getAllParagraphInstancesRecurse(it, true);
				}
				// Appends included content to the return string
				returnString.append(appendParagraphHTML(currentInstance, recursionString, isParentIncluded));
				// Closes Ordered List tag, if necessary
				// Returns the compiled return string, for recursion
				if (isLast && (!isOption || (isOption && isIncluded))) {
					if (!isTable) {
						returnString.append("</ol>");
						if ((isFirst || isSubPara) && !isNestedList) {
							returnString.append("</div>");
						}
					} else if (isTable) {
						if (isFirstParagraphInClause) {
							returnString.append("</div>");
						}
					}
					return returnString.toString();
				}
				// Closes keep-together on clause headers and first item in
				// clause
				if (isFirstParagraphInClause && !isSubPara) {
					returnString.append("</div>");
				}
			}
		} // Next paragraph or done
			// Final result for clause
		return returnString.toString();
	}

	private void getAllSectionsForDocumentHTML(StringBuilder sb, DocumentInstanceJpaImpl doc) {
		// TODO: Trace log entry
		boolean first = true;
		for (SectionInstanceJpaImpl section : doc.getSections()) {
			appendSectionHTML(sb, section, first);
			getAllClausesForSectionHTML(sb, section);
			first = false;
		}
	}

	private String htmlSelfCloseTag(String htmlTag) {
		if (htmlTag == null)
			throw new IllegalArgumentException("HTML Tag Must Not Be Null");
		return htmlSelfCloseTag(htmlTag, null);
	}

	private String htmlSelfCloseTag(String htmlTag, String cssClass) {
		if (htmlTag == null)
			throw new IllegalArgumentException("HTML Tag Must Not Be Null");
		if (cssClass == null) {
			return String.format("<%s />", htmlTag);
		} else {
			return String.format("<%s class=\"%s\"/>", htmlTag, cssClass);
		}
	}

	private String htmlTab(int count) {
		return StringUtils.repeat("&nbsp;", count);
	}

	private String htmlTag(String htmlTag, String content) {
		if (content == null)
			throw new IllegalArgumentException("ContentRepo Must Not Be Null!");
		return htmlTag(htmlTag, content, null);
	}

	private String htmlTag(String htmlTag, String content, String cssClass) {
		if (content == null)
			throw new IllegalArgumentException("ContentRepo Must Not Be Null!");
		if (cssClass == null && htmlTag == null) {
			return content;
		} else if (htmlTag == null) {
			return content;
		} else if (cssClass == null && htmlTag != null) {
			return String.format("<%s>%s</%s>", htmlTag, content, htmlTag);
		} else {
			return String.format("<%s class=\"%s\">%s</%s>", htmlTag, cssClass, content,
					htmlTag);
		}
	}

	static class CompilerConstants {
		static final String STATUS_AUTO_IN = "Automatically Included";
		static final String STATUS_AUTO_OUT = "Automatically Excluded";
		static final String STATUS_MAN_IN = "Manually Included";
		static final String STATUS_MAN_OUT = "Manually Excluded";
		static final String STATUS_PENDING = "Pending Review";
	}
}