package com.askredrover.ventures.meetings;

public interface Meeting {

	public int mid();

	public int vid();

	public int author();

	public java.sql.Date added();

	public java.sql.Date meetingDate();

	public int typeof();

	public String typeAsString();

	public String agenda();

	public String notes();

	public String title();

	public String key();
}
