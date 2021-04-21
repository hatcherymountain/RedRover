package com.askredrover.ventures;

public class VentureObject implements Venture {

	private int vid, eid, aid, cid, to, status, author, groupid, progress, sentiment = 0;
	private String t, d, color = null;
	private java.sql.Date added = null;
	private java.sql.Timestamp entered = null;

	public VentureObject(int vid, int eid, int aid, int cid, String t, String d, int to, int status,
			java.sql.Date added, java.sql.Timestamp entered, int author, int groupid, String color, int progress,
			int sentiment) {
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
		this.color = color;
		this.progress = progress;
		this.sentiment = sentiment;
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

	public String color() {
		if (color == null) {
			color = "red";
		}
		return color;
	}

	public int progress() {
		return progress;
	}

	public int sentiment() {
		return sentiment;
	}

	public String sentimentAsString() {
		return com.askredrover.Constants.sentimentAsString(sentiment);
	}

}
