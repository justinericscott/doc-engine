package com.itgfirm.docengine.service.token.types;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Justin Scott
 * 
 *         External Entity Data Model (non-persistent)
 */
public class ExternalEntity {

	private String name;
	private String description;
	private Collection<ExternalAttribute> columns;

	public ExternalEntity() {
	}

	public ExternalEntity(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Iterable<ExternalAttribute> getColumns() {
		return columns;
	}

	public void addColumn(ExternalAttribute column) {
		if (columns == null) {
			columns = new ArrayList<ExternalAttribute>();
		}
		columns.add(column);
	}

	public void setColumns(Collection<ExternalAttribute> columns) {
		this.columns = columns;
	}
}