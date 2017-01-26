package com.github.justinericscott.docengine.service.ix.types;

import com.github.justinericscott.docengine.annotation.ExcelColumn;
import com.github.justinericscott.docengine.annotation.ExcelSheet;

@ExcelSheet
public class ExampleExcelType {

	@ExcelColumn
	private long id;
	@ExcelColumn
	private String name;
	@ExcelColumn
	private String description;
	@ExcelColumn
	private boolean positive;

	public ExampleExcelType() {

	}

	public ExampleExcelType(final long id, final String name, final String description, final boolean positive) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.positive = positive;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean getPositive() {
		return positive;
	}

	@Override
	public String toString() {
		return "ID: " + id + " | Name: " + name + " | Description: " + description;
	}
}