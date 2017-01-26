package com.itgfirm.docengine.service.ix.types;

import com.itgfirm.docengine.annotation.ExcelColumn;
import com.itgfirm.docengine.annotation.ExcelColumnOrder;
import com.itgfirm.docengine.annotation.ExcelSheet;

@ExcelSheet("Excel Example Type")
public class ExampleExcelTypeWithCustomLabelsOrdered {

	@ExcelColumnOrder(1)
	@ExcelColumn("Identification")
	private long id;
	@ExcelColumnOrder(2)
	@ExcelColumn("Name")
	private String name;
	@ExcelColumnOrder(3)
	@ExcelColumn("Description")
	private String description;
	@ExcelColumnOrder(4)
	@ExcelColumn("Positive")
	private Boolean positive;

	public ExampleExcelTypeWithCustomLabelsOrdered() {
		// Deafult constructor for Spring
	}

	public ExampleExcelTypeWithCustomLabelsOrdered(ExampleExcelTypeWithCustomLabelsOrdered ex) {
		this.id = ex.getId();
		this.name = ex.getName();
		this.description = ex.getDescription();
		this.positive = ex.getPositive();
	}

	public ExampleExcelTypeWithCustomLabelsOrdered(final long id, final String name,
			final String description, final Boolean positive) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.positive = positive;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Boolean getPositive() {
		return positive;
	}

	public void setPositive(Boolean positive) {
		this.positive = positive;
	}

	@Override
	public String toString() {
		return "ID: " + id + " | Name: " + name + " | Description: " + description;
	}
}