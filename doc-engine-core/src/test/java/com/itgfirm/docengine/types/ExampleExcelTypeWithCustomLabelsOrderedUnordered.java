package com.itgfirm.docengine.types;

import com.itgfirm.docengine.annotation.ExcelColumn;
import com.itgfirm.docengine.annotation.ExcelColumnOrder;
import com.itgfirm.docengine.annotation.ExcelSheet;

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
	@ExcelColumn("Boolean Test")
	private boolean isTrue;

	public ExampleExcelTypeWithCustomLabelsOrderedUnordered() {
		
	}
	
	public ExampleExcelTypeWithCustomLabelsOrderedUnordered(final long id, final String name, final String description,
			final boolean isTrue) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.isTrue = isTrue;
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
	
	public boolean isTrue() {
		return isTrue;
	}

	@Override
	public String toString() {
		return "ID: " + id + " | Name: " + name + " | Description: " + description;
	}
}