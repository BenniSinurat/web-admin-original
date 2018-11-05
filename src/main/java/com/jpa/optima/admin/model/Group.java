package com.jpa.optima.admin.model;

public class Group {
	private String name;
	private String description;
	private Integer pinLength;

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

	public Integer getPinLength() {
		return pinLength;
	}

	public void setPinLength(Integer pinLength) {
		this.pinLength = pinLength;
	}

}
