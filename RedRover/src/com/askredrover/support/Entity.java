package com.askredrover.support;

public class Entity {

	private int sid, eid, aid, userid, typeof, reason, status = 0;
	private String title, message = null;
	private java.sql.Date added = null;
	private java.sql.Timestamp entered = null;

	public Entity(int sid, int eid, int aid, java.sql.Date added, java.sql.Timestamp entered, int userid, int typeof,
			int reason, String title, String message, int status) {
		this.sid = sid;
		this.eid = eid;
		this.aid = aid;
		this.added = added;
		this.entered = entered;
		this.userid = userid;
		this.typeof = typeof;
		this.reason = reason;
		this.title = title;
		this.message = message;
		this.status = status;
	}

	public int supportid() {
		return sid;
	}

	public int eid() {
		return eid;
	}

	public int accountid() {
		return aid;
	}

	public java.sql.Date added() {
		return added;
	}

	public java.sql.Timestamp entered() {
		return entered;
	}

	public int userid() {
		return userid;
	}

	public int typeof() {
		return typeof;
	}

	public int reason() {
		return reason;
	}

	public String title() {
		return title;
	}

	public String message() {
		return message;
	}

	public int status() {
		return status;
	}

}
