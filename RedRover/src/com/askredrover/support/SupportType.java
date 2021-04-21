package com.askredrover.support;

public class SupportType {

	private int id, typeof = 0;
	private String value = null;

	public SupportType(int id, String value, int typeof) {
		this.id = id;
		this.typeof = typeof;
		this.value = value;
	}

	public int id() {
		return id;
	}

	public int typeof() {
		return typeof;
	}

	public String value() {
		return value;
	}
}
