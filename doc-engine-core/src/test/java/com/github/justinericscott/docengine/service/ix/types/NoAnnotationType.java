package com.github.justinericscott.docengine.service.ix.types;

public class NoAnnotationType {

	private long id;
	private String name;
	private String description;
	private boolean positive;

	public NoAnnotationType() {
		
	}
	
	public NoAnnotationType(final long id, final String name, final String description,
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