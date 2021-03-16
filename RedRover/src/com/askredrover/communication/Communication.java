package com.askredrover.communication;

public interface Communication {

	public int userid();

	public boolean sms();

	public String phone();

	public String email();

	public boolean weekly();

	public boolean monthly();
	
	public boolean annual();

	public boolean verbose();

	public boolean groupConsolidation();

	public boolean companyConsolidation();
	
	public java.sql.Date lastUpdated();

}
