package com.askredrover.ventures;

public interface Venture {

	public int vid();

	public int eid();

	public int aid();

	public int cid();

	public String title();

	public String description();

	public int typeof();

	public int status();

	public java.sql.Date added();

	public java.sql.Timestamp entered();

	public int author();

	public int groupid();

	public String statusAsString();

	public String statusColorAsString();

	public String color();

	public int progress();

	public int sentiment();

	public String sentimentAsString();

}
