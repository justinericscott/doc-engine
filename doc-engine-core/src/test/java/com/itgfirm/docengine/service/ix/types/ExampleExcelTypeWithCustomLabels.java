package com.itgfirm.docengine.service.ix.types;

import com.itgfirm.docengine.annotation.ExcelColumn;
import com.itgfirm.docengine.annotation.ExcelSheet;

@ExcelSheet("Excel Example Type")
public class ExampleExcelTypeWithCustomLabels {

	@ExcelColumn("Identification")
	private long id;
	@ExcelColumn("Name")
	private String name;
	@ExcelColumn("Description")
	private String description;
	@ExcelColumn("Positive")
	private boolean positive;

	public ExampleExcelTypeWithCustomLabels() {

	}

	public ExampleExcelTypeWithCustomLabels(final long id, final String name, final String description,
			final boolean positive) {
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