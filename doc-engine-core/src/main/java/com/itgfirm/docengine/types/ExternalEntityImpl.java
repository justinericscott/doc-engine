package com.itgfirm.docengine.types;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Justin Scott
 * 
 *         External Entity Data Model (non-persistent)
 */
public class ExternalEntityImpl {

	private String name;
	private String description;
	private Collection<ExternalAttributeImpl> columns;

	public ExternalEntityImpl() {
	}

	public ExternalEntityImpl(String name, String description) {
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

	public Iterable<ExternalAttributeImpl> getColumns() {
		return columns;
	}

	public void addColumn(ExternalAttributeImpl column) {
		if (columns == null) {
			columns = new ArrayList<ExternalAttributeImpl>();
		}
		columns.add(column);
	}

	public void setColumns(Collection<ExternalAttributeImpl> columns) {
		this.columns = columns;
	}
}