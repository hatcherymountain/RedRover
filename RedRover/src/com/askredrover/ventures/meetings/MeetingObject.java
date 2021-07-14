package com.askredrover.ventures.meetings;

import java.sql.Date;

public class MeetingObject implements Meeting {

	private int mid, author, vid, typeof = 0;
	private Date added, meetingdate = null;
	private String agenda, notes, title, key = null;

	public MeetingObject(int mid, int vid, int author, Date added, Date meetingdate, int typeof, String agenda,
			String notes, String title, String key) {
		this.mid = mid;
		this.author = author;
		this.vid = vid;
		this.typeof = typeof;
		this.added = added;
		this.meetingdate = meetingdate;
		this.agenda = agenda;
		this.notes = notes;
		this.title = title;
		this.key = key;
	}

	public int mid() {
		return mid;
	}

	public String key() {
		return key;
	}

	public int vid() {
		return vid;
	}

	public int author() {
		return author;
	}

	public java.sql.Date added() {
		return added;
	}

	public java.sql.Date meetingDate() {
		return meetingdate;
	}

	public int typeof() {
		return typeof;
	}

	public String typeAsString() {
		return com.askredrover.Constants.meetingTypeAsString(typeof);
	}

	public String agenda() {
		return agenda;
	}

	public String notes() {
		return notes;
	}

	public String title() {
		return title;
	}
}
