package com.itgfirm.docengine.data;

/**
 * @author Justin Scott
 * 
 *         External Attribute Data Model (non-persistent)
 */
public class ExternalAttributeImpl implements ExternalAttribute {

	private String name;
	private String description;
	private Integer datatype;
	private boolean isNullable = false;

	public ExternalAttributeImpl() {}

	public ExternalAttributeImpl(String name, String description, Integer datatype) {
		this.name = name;
		this.description = description;
		this.datatype = datatype;
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

	public Integer getDatatype() {
		return datatype;
	}

	public void setDatatype(Integer datatype) {
		this.datatype = datatype;
	}

	public boolean isNullable() {
		return isNullable;
	}

	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}
}