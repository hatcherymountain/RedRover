package com.askredrover.ventures;

import java.sql.Date;

public interface SOW {

	public int sowid();

	public int ventureid();

	public int status();

	public Date starts();

	public Date ends();

	public Date actualClose();

	public String title();

	public String description();

	public boolean endVentureOnClose();

	public int authorizer();

	public boolean signed();

	public Date signDate();

	public boolean edited();

	public String statusAsString();

}
