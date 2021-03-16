package com.askredrover.ventures;

public class Type {

	private int typeid = 0;
	private String name, description = null;

	public Type(int typeid, String name, String description) {
		this.typeid = typeid;
		this.name = name;
		this.description = description;
	}

	public int typeid() {
		return typeid;
	}

	public String name() {
		return name;
	}

	public String description() {
		return description;
	}

}
