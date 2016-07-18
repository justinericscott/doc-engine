/**
 * TODO: License
 */
package com.itgfirm.docengine.pipeline;

import java.util.List;
import java.util.Map;

/**
 * @author Justin Scott
 * 
 *         TODO: Description
 */
public class DefaultProjectImpl implements Project {

	private String projectNumber;
	private String leaseNumber;
	private String documentCd;
	private String packageCd;
	private String phase;
	private Map<String, ?> tokens;
	private List<Exhibit> exhibit;

	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	public String getLeaseNumber() {
		return leaseNumber;
	}

	public void setLeaseNumber(String leaseNumber) {
		this.leaseNumber = leaseNumber;
	}

	public String getDocumentCd() {
		return documentCd;
	}

	public void setDocumentCd(String documentCd) {
		this.documentCd = documentCd;
	}

	public String getPackageCd() {
		return packageCd;
	}

	public void setPackageCd(String packageCd) {
		this.packageCd = packageCd;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public Map<String, ?> getTokens() {
		return tokens;
	}

	public void setTokens(Map<String, ?> tokens) {
		this.tokens = tokens;
	}

	public List<Exhibit> getExhibits() {
		return exhibit;
	}

	public void setExhibits(List<Exhibit> exhibit) {
		this.exhibit = exhibit;
	}
}