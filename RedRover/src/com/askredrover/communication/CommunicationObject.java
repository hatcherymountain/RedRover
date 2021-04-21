package com.askredrover.communication;

public class CommunicationObject implements Communication {

	private int userid = 0;
	private boolean sms, weekly, monthly, annual, verbose, groupConsolidation, companyConsolidation = false;
	private String phone, email = null;
	private java.sql.Date lastUpdated = null;

	public CommunicationObject(int userid, boolean sms, String phone, String email, boolean weekly, boolean monthly,
			boolean annual, boolean verbose, boolean groupConsolidation, boolean companyConsolidation,
			java.sql.Date lastUpdated) {

		this.userid = userid;
		this.sms = sms;
		this.phone = phone;
		this.email = email;
		this.weekly = weekly;
		this.monthly = monthly;
		this.annual = annual;
		this.verbose = verbose;
		this.groupConsolidation = groupConsolidation;
		this.companyConsolidation = companyConsolidation;
		this.lastUpdated = lastUpdated;

	}

	public int userid() {
		return userid;
	}

	public boolean sms() {
		return sms;
	}

	public String phone() {
		return phone;
	}

	public String email() {
		return email;
	}

	public boolean weekly() {
		return weekly;
	}

	public boolean monthly() {
		return monthly;
	}

	public boolean verbose() {
		return verbose;
	}

	public boolean groupConsolidation() {
		return groupConsolidation;
	}

	public boolean companyConsolidation() {
		return companyConsolidation;
	}

	public boolean annual() {
		return annual;
	}

	public java.sql.Date lastUpdated() {
		return lastUpdated;
	}
}
