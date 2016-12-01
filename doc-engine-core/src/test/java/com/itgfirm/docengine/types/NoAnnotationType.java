package com.itgfirm.docengine.types;

public class NoAnnotationType {

	private long id;
	private String name;
	private String description;
	private boolean isTrue;

	public NoAnnotationType() {
		
	}
	
	public NoAnnotationType(final long id, final String name, final String description,
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