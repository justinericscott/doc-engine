package com.github.justinericscott.docengine.service.token.types;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Justin Scott
 * 
 *         External Schema Data Model (non-persistent)
 */
public class ExternalSchema {

	private String name;
	private Collection<ExternalEntity> tables;
	private String url;

	public ExternalSchema() {
	}

	public ExternalSchema(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Iterable<ExternalEntity> getTables() {
		return tables;
	}

	public void addTable(ExternalEntity table) {
		if (tables == null) {
			tables = new ArrayList<ExternalEntity>();
		}
		tables.add(table);
	}

	public void setTables(Collection<ExternalEntity> tables) {
		this.tables = tables;
	}

	
	public final String getUrl() {
		return url;
	}

	
	public final void setUrl(final String url) {
		this.url = url;
	}
}