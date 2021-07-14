package com.askredrover.ventures.meetings;

public class Action {

	private int aid, mid, vid, owner, priority, status = 0;
	private String title, note = null;
	private java.sql.Date due = null;

	public Action(int aid, int mid, int vid, int owner, int priority, String title, String note, java.sql.Date due,
			int status) {
		this.aid = aid;
		this.mid = mid;
		this.vid = vid;
		this.owner = owner;
		this.priority = priority;
		this.title = title;
		this.note = note;
		this.due = due;
		this.status = status;
	}

	public int aid() {
		return aid;
	}

	public int vid() {
		return vid;
	}

	public int mid() {
		return mid;
	}

	public int owner() {
		return owner;
	}

	public int priority() {
		return priority;
	}

	public String priorityAsString() {
		return com.askredrover.Constants.eventPriorityAsString(priority);
	}

	public String title() {
		return title;
	}

	public String note() {
		return note;
	}

	public java.sql.Date due() {
		return due;
	}

	public int status() {
		return status;
	}

	public String statusAsString() {
		return com.askredrover.Constants.eventStatusAsString(status);
	}
}
