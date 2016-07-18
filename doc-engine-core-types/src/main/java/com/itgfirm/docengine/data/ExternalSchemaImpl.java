package com.itgfirm.docengine.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Justin Scott
 * 
 *         External Schema Data Model (non-persistent)
 */
public class ExternalSchemaImpl implements ExternalSchema {

	private String name;
	private List<ExternalEntity> tables;

	public ExternalSchemaImpl() {}

	public ExternalSchemaImpl(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ExternalEntity> getTables() {
		return tables;
	}

	public void addTable(ExternalEntity table) {
		if (tables == null) {
			tables = new ArrayList<ExternalEntity>();
		}
		tables.add(table);
	}

	public void setTables(List<ExternalEntity> tables) {
		this.tables = tables;
	}
}