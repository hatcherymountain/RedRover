package com.askredrover.wisdom.loyalty;

public class Bookmark {

	private int bid, eid, userid, id, typeof = 0;
	private java.sql.Date added = null;

	public Bookmark(int bid, int eid, int userid, int id, int typeof, java.sql.Date added) {
		this.bid = bid;
		this.eid = eid;
		this.userid = userid;
		this.id = id;
		this.typeof = typeof;
		this.added = added;
	}

	public int bid() {
		return bid;
	}

	public int eid() {
		return eid;
	}

	public int userid() {
		return userid;
	}

	public int id() {
		return id;
	}

	public int typeof() {
		return typeof;
	}

	public java.sql.Date added() {
		return added;
	}

}
