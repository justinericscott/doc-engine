package com.github.justinericscott.docengine.service.ix.types;

import com.github.justinericscott.docengine.annotation.ExcelColumn;
import com.github.justinericscott.docengine.annotation.ExcelColumnOrder;
import com.github.justinericscott.docengine.annotation.ExcelSheet;

@ExcelSheet("Excel Example Type")
public class ExampleExcelTypeWithCustomLabelsOrderedReversed {

	@ExcelColumnOrder(4)
	@ExcelColumn("Identification")
	private long id;
	@ExcelColumnOrder(3)
	@ExcelColumn("Name")
	private String name;
	@ExcelColumnOrder(2)
	@ExcelColumn("Description")
	private String description;
	@ExcelColumnOrder(1)
	@ExcelColumn("Positive")
	private boolean positive;

	public ExampleExcelTypeWithCustomLabelsOrderedReversed() {
		
	}
	
	public ExampleExcelTypeWithCustomLabelsOrderedReversed(ExampleExcelTypeWithCustomLabelsOrderedReversed ex) {
		this.id = ex.getId();
		this.name = ex.getName();
		this.description = ex.getDescription();
		this.positive = ex.getPositive();
	}
	
	public ExampleExcelTypeWithCustomLabelsOrderedReversed(final long id, final String name, final String description,
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
	public void setName(String name) {
		this.name = name;
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