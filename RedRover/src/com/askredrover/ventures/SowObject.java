package com.askredrover.ventures;

import java.sql.Date;

public class SowObject implements SOW {

	private int sowid, vid, status, authorizer, signed, endventureonclose, edited, progress, sentiment = 0;
	private String title, description = null;
	private Date starts, ends, actual, signdate = null;
	private double budget = 0;

	public SowObject(int sowid, int vid, int status, Date starts, Date ends, Date actual, String title,
			String description, int endventureonclose, int authorizer, int signed, Date signdate, int edited,
			double budget, int progress, int sentiment) {
		this.sowid = sowid;
		this.vid = vid;
		this.status = status;
		this.starts = starts;
		this.ends = ends;
		this.actual = actual;
		this.title = title;
		this.description = description;
		this.endventureonclose = endventureonclose;
		this.authorizer = authorizer;
		this.signed = signed;
		this.signdate = signdate;
		this.edited = edited;
		this.budget = budget;
		this.progress = progress;
		this.sentiment = sentiment;
	}

	public double budget() {
		return budget;
	}

	public int sowid() {
		return sowid;
	}

	public int ventureid() {
		return vid;
	}

	public int status() {
		return status;
	}

	public Date starts() {
		return starts;
	}

	public Date ends() {
		return ends;
	}

	public Date actualClose() {
		return actual;
	}

	public String title() {
		return title;
	}

	public String description() {
		return description;
	}

	public boolean endVentureOnClose() {
		if (endventureonclose == 1) {
			return true;
		} else {
			return false;
		}
	}

	public int authorizer() {
		return authorizer;
	}

	public boolean signed() {
		if (signed == 1) {
			return true;
		} else {
			return false;
		}
	}

	public String statusAsString() {
		return com.askredrover.Constants.sowStatusAsString(status);
	}

	public Date signDate() {
		return signdate;
	}

	public boolean edited() {
		if (edited == 1) {
			return true;
		} else {
			return false;
		}
	}

	public String sentimentAsString() {
		return com.askredrover.Constants.sentimentAsString(sentiment);
	}

	public int sentiment() {
		return sentiment;
	}

	public int progress() {
		return progress;
	}

}
