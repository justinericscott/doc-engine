package com.itgfirm.docengine.types;

import com.itgfirm.docengine.annotation.ExcelColumn;
import com.itgfirm.docengine.annotation.ExcelColumnOrder;
import com.itgfirm.docengine.annotation.ExcelSheet;

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
	@ExcelColumn("Boolean Test")
	private boolean positive;

	public ExampleExcelTypeWithCustomLabelsOrderedReversed() {
		
	}
	
	public ExampleExcelTypeWithCustomLabelsOrderedReversed(ExampleExcelTypeWithCustomLabelsOrderedReversed ex) {
		this.id = ex.getId();
		this.name = ex.getName();
		this.description = ex.getDescription();
		this.positive = ex.isPositive();
	}
	
	public ExampleExcelTypeWithCustomLabelsOrderedReversed(final long id, final String name, final String description,
			final boolean isTrue) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.positive = isTrue;
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
	
	public boolean isPositive() {
		return positive;
	}

	@Override
	public String toString() {
		return "ID: " + id + " | Name: " + name + " | Description: " + description;
	}
}