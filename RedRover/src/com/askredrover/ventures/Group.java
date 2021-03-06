package com.askredrover.ventures;

public class Group {

	private int groupid = 0;
	private String name, description = null;

	public Group(int groupid, String name, String description) {
		this.groupid = groupid;
		this.name = name;
		this.description = description;
	}

	public int groupid() {
		return groupid;
	}

	public String name() {
		return name;
	}

	public String description() {
		return description;
	}

}
