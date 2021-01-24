package com.askredrover.utils;

public class Number {

	private int nid = 0;
	private String number = null;

	public Number(int nid, String number) {
		this.nid = nid;
		this.number = number;
	}

	public int nid() {
		return nid;
	}

	public String number() {
		return number;
	}

	public String numberFormatted() {
		return Utilities.phoneFormatted(number);
	}
}
