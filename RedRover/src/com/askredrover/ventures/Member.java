package com.askredrover.ventures;

public interface Member {

	public int userid();

	public int eid();

	public int vid();

	public int role();

	public String roleAsString();

	public boolean owner();

	public boolean editor();
	
	public boolean dailyCheckin();
}
