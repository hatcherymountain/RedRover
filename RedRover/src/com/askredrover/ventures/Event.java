package com.askredrover.ventures;

import java.sql.Date;
import java.sql.Timestamp;

public interface Event {

	public int eventid();

	public int eid();

	public int aid();

	public int ventureid();

	public String title();

	public String description();

	public boolean sow();

	public boolean milestone();

	public Date dateof();

	public Date due();

	public Date added();

	public Timestamp entered();

	public int owner();

	public int priority();

	public String priorityAsString();
	
	public String priorityColorAsString();

	public int progress();

	public int status();

	public String statusAsString();

}
