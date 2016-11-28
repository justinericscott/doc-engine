/**
 * TODO: License
 */
package com.itgfirm.docengine.compiler;

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.itgfirm.docengine.types.jpa.AdvancedDocumentInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.ClauseInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.ParagraphInstanceJpaImpl;
import com.itgfirm.docengine.types.jpa.ParagraphJpaImpl;
import com.itgfirm.docengine.types.jpa.SectionInstanceJpaImpl;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
@Service
class DefaultCompilerServiceImpl implements CompilerService {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itgfirm.docengine.compiler.CompilerService#compileDocument(
	 * com.itgfirm.docengine.types.jpa.DocumentInstance)
	 */
	@Override
	public String compileDocument(AdvancedDocumentInstanceJpaImpl document) {
		// TODO: Trace log entry
		StringBuilder sb = new StringBuilder();
		getAllSectionsForDocumentHTML(sb, document);
		return sb.toString();
	}

	/**
	 * TODO: Description
	 * 
	 * @param sb
	 * @param doc
	 */
	private void getAllSectionsForDocumentHTML(StringBuilder sb, AdvancedDocumentInstanceJpaImpl doc) {
		// TODO: Trace log entry
		boolean first = true;
		for (SectionInstanceJpaImpl section : doc.getSections()) {
			appendSectionHTML(sb, section, first);
			getAllClausesForSectionHTML(sb, section);
			first = false;
		}
	}

	/**
	 * TODO: Description
	 * 
	 * @param sb
	 * @param section
	 * @param first
	 */
	private void appendSectionHTML(StringBuilder sb, SectionInstanceJpaImpl section, boolean first) {
		// TODO: Trace log entry
		String content =
				String.format("SECTION%s%s%s%s", CompilerUtils.htmlTab(3), section
						.getContent().getContentNumber(), CompilerUtils.htmlTab(3), section
						.getContent().getBody());
		if (!first)
			sb.append(CompilerUtils.htmlSelfCloseTag("br", "break"));
		sb.append(CompilerUtils.htmlSelfCloseTag("hr", ""));
		sb.append(CompilerUtils.htmlTag("h1", content, ""));
		sb.append(CompilerUtils.htmlSelfCloseTag("hr", ""));
	}

	/**
	 * TODO: Description
	 * 
	 * @param sb
	 * @param section
	 */
	private void getAllClausesForSectionHTML(StringBuilder sb, SectionInstanceJpaImpl section) {
		// TODO: Trace log entry
		for (ClauseInstanceJpaImpl clause : section.getClauses()) {
			appendClauseHTML(sb, clause);
			if (CompilerConstants.STATUS_AUTO_IN.equals(clause.getStatusCd())
					|| CompilerConstants.STATUS_MAN_IN.equals(clause.getStatusCd())) {
				if (clause.getContent() != null) {
					getAllParagraphsForClauseHTML(sb, clause);
				}
			}
		}
	}

	/**
	 * TODO: Description
	 * 
	 * @param sb
	 * @param clause
	 */
	private void appendClauseHTML(StringBuilder sb, ClauseInstanceJpaImpl clause) {
		// TODO: Trace log entry
		StringBuilder clauseStr = null;
		if (CompilerConstants.STATUS_AUTO_OUT.equals(clause.getStatusCd())
				|| CompilerConstants.STATUS_MAN_OUT.equals(clause.getStatusCd())) {
			clauseStr = new StringBuilder();
			clauseStr.append(clause.getContent().getContentNumber());
			clauseStr.append(CompilerUtils.htmlTab(3));
			if (clause.isStrikeHeader()) {
				String clauseHeader = "";
				if (StringUtils.isNotBlank(clause.getBody())) {
					clauseHeader = clause.getBody();
				} else if (clause.getContent() != null) {
					clauseHeader = clause.getBody();
				}
				clauseStr.append(CompilerUtils.htmlTag("span", clauseHeader, "strike"));
				clauseStr.append(CompilerUtils.htmlTab(3));
			}
			clauseStr.append("INTENTIONALLY DELETED");
			sb.append(CompilerUtils.htmlTag("h2", clauseStr.toString(), ""));
		} else {
			clauseStr = new StringBuilder();
			clauseStr.append(clause.getContent().getContentNumber());
			clauseStr.append(CompilerUtils.htmlTab(3));
			if (StringUtils.isNotBlank(clause.getBody())) {
				clauseStr.append(clause.getBody());
			} else if (clause.getContent() != null) {
				clauseStr.append(clause.getBody());
			}
			sb.append("<div class=\"group\">");
			sb.append(CompilerUtils.htmlTag("h2", clauseStr.toString(), ""));
		}
	}

	/**
	 * TODO: Description
	 * 
	 * @param sb
	 * @param clause
	 */
	private void getAllParagraphsForClauseHTML(StringBuilder sb, ClauseInstanceJpaImpl clause) {
		// TODO: Trace log entry
		Iterator<ParagraphInstanceJpaImpl> it = clause.getParagraphs().iterator();
		while (it.hasNext()) {
			sb.append(getAllParagraphInstancesRecurse(it, false));
		}
	}

	/**
	 * TODO: Description
	 * 
	 * @param it
	 * @param isNestedList
	 * @return {@link String}
	 */
	private String getAllParagraphInstancesRecurse(Iterator<ParagraphInstanceJpaImpl> it,
			boolean isNestedList) {
		// TODO: Trace log entry
		StringBuilder returnString = new StringBuilder();
		ParagraphInstanceJpaImpl parentInstance = null;
		ParagraphInstanceJpaImpl currentInstance = null;
		while (it.hasNext()) {
			currentInstance = it.next();
			ParagraphJpaImpl source = (ParagraphJpaImpl) currentInstance.getContent();
			if (source != null) {
				// Initialize flags
				boolean isFirst = source.isFirst();
				boolean isFirstParagraphInClause = (source.getOrderBy() == 1);
				boolean isIncluded =
						(currentInstance.getStatusCd()
								.equals(CompilerConstants.STATUS_AUTO_IN) || currentInstance
								.getStatusCd().equals(CompilerConstants.STATUS_MAN_IN));
				boolean isLast = source.isLast();
				boolean isOption = source.isOption();
				boolean isParent = source.isParent();
				boolean isParentIncluded = true;
				boolean isSubPara = source.isSubPara();
				if (parentInstance != null) {
					isParentIncluded =
							(parentInstance.getStatusCd().equals(
									CompilerConstants.STATUS_AUTO_IN) || parentInstance
									.getStatusCd().equals(CompilerConstants.STATUS_MAN_IN));
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
				returnString.append(appendParagraphHTML(currentInstance, recursionString,
						isParentIncluded));
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
				// Closes keep-together on clause headers and first item in clause
				if (isFirstParagraphInClause && !isSubPara) {
					returnString.append("</div>");
				}
			}
		}// Next paragraph or done
			// Final result for clause
		return returnString.toString();
	}

	/**
	 * TODO: Description
	 * 
	 * @param instance
	 * @param recursionString
	 * @param isParentIncluded
	 * @return {@link String}
	 */
	private String appendParagraphHTML(ParagraphInstanceJpaImpl instance, String recursionString,
			boolean isParentIncluded) {
		// TODO: Trace log entry
		StringBuilder returnString = new StringBuilder();
		ParagraphJpaImpl source = (ParagraphJpaImpl) instance.getContent();
		if (source != null) {
			boolean hasCustomBody = StringUtils.isNotBlank(instance.getCustomBody());
			boolean isIncluded =
					(instance.getStatusCd().equals(CompilerConstants.STATUS_AUTO_IN) || instance
							.getStatusCd().equals(CompilerConstants.STATUS_MAN_IN));
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
				templateBody = CompilerUtils.htmlTag(tag, "INTENTIONALLY DELETED");
			} else if (isIncluded && hasCustomBody) {
				if (isParent && tag.equalsIgnoreCase("p")) {
					templateBody =
							CompilerUtils.htmlTag(tag, instance.getCustomBody())
									+ recursionString;
				} else {
					templateBody =
							CompilerUtils.htmlTag(tag, instance.getCustomBody()
									+ recursionString);
				}
			} else if (isIncluded) {
				if (isParent && tag.equalsIgnoreCase("p")) {
					templateBody =
							CompilerUtils.htmlTag(tag, source.getBody()) + recursionString;
				} else {
					templateBody =
							CompilerUtils.htmlTag(tag, source.getBody() + recursionString);
				}
			}
			if (templateBody != null) {
				returnString.append(blueText + templateBody);
			}
		}
		return returnString.toString();
	}
}