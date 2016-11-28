package com.itgfirm.docengine.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Justin Scott
 * 
 *         External Schema Data Model (non-persistent)
 */
public class ExternalSchemaImpl {

	private String name;
	private List<ExternalEntityImpl> tables;

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

	public List<ExternalEntityImpl> getTables() {
		return tables;
	}

	public void addTable(ExternalEntityImpl table) {
		if (tables == null) {
			tables = new ArrayList<ExternalEntityImpl>();
		}
		tables.add(table);
	}

	public void setTables(List<ExternalEntityImpl> tables) {
		this.tables = tables;
	}
}