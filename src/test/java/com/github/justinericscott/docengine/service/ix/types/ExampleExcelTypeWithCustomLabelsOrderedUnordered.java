package com.github.justinericscott.docengine.service.ix.types;

import com.github.justinericscott.docengine.annotation.ExcelColumn;
import com.github.justinericscott.docengine.annotation.ExcelColumnOrder;
import com.github.justinericscott.docengine.annotation.ExcelSheet;

@ExcelSheet("Excel Example Type")
public class ExampleExcelTypeWithCustomLabelsOrderedUnordered {

	@ExcelColumn("Identification")
	private long id;
	@ExcelColumnOrder(1)
	@ExcelColumn("Name")
	private String name;
	@ExcelColumn("Description")
	private String description;
	@ExcelColumnOrder(2)
	@ExcelColumn("Positive")
	private boolean positive;

	public ExampleExcelTypeWithCustomLabelsOrderedUnordered() {
		
	}
	
	public ExampleExcelTypeWithCustomLabelsOrderedUnordered(final long id, final String name, final String description,
			final boolean isPositive) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.positive = isPositive;
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