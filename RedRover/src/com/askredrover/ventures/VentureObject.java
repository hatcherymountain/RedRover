package com.askredrover.ventures;

public class VentureObject implements Venture {

	private int vid, eid, aid, cid, to, status, author, groupid = 0;
	private String t, d = null;
	private java.sql.Date added = null;
	private java.sql.Timestamp entered = null;

	public VentureObject(int vid, int eid, int aid, int cid, String t, String d, int to, int status,
			java.sql.Date added, java.sql.Timestamp entered, int author, int groupid) {
		this.vid = vid;
		this.eid = eid;
		this.aid = aid;
		this.cid = cid;
		this.t = t;
		this.d = d;
		this.to = to;
		this.status = status;
		this.added = added;
		this.entered = entered;
		this.author = author;
		this.groupid = groupid;
	}

	public int vid() {
		return vid;
	}

	public int eid() {
		return eid;
	}

	public int aid() {
		return aid;
	}

	public int cid() {
		return cid;
	}

	public String title() {
		return t;
	}

	public String description() {
		return d;
	}

	public int typeof() {
		return to;
	}

	public int status() {
		return status;
	}

	public String statusAsString() {
		return com.askredrover.Constants.ventureStatusAsString(status);
	}

	public String statusColorAsString() {
		return com.askredrover.Constants.ventureStatusColorAsString(status);
	}

	public java.sql.Date added() {
		return added;
	}

	public java.sql.Timestamp entered() {
		return entered;
	}

	public int author() {
		return author;
	}

	public int groupid() {
		return groupid;
	}
}
