package com.itgfirm.docengine.types;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Justin Scott
 * 
 *         External Schema Data Model (non-persistent)
 */
public class ExternalSchemaImpl {

	private String name;
	private Collection<ExternalEntityImpl> tables;
	private String url;

	public ExternalSchemaImpl() {
	}

	public ExternalSchemaImpl(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Iterable<ExternalEntityImpl> getTables() {
		return tables;
	}

	public void addTable(ExternalEntityImpl table) {
		if (tables == null) {
			tables = new ArrayList<ExternalEntityImpl>();
		}
		tables.add(table);
	}

	public void setTables(Collection<ExternalEntityImpl> tables) {
		this.tables = tables;
	}

	
	public final String getUrl() {
		return url;
	}

	
	public final void setUrl(final String url) {
		this.url = url;
	}
}