package com.askredrover.wisdom;

public class TutorialObject implements Tutorial {

	private int tid, eid, aid, sid, cid, status, role = 0;
	private String title, desc, youtube = null;
	private java.sql.Date added = null;

	public TutorialObject(int tid, int eid, int aid, int sid, String title, String desc, int cid, java.sql.Date added,
			int status, int role, String youtube) {
		this.tid = tid;
		this.eid = eid;
		this.aid = aid;
		this.sid = sid;
		this.title = title;
		this.desc = desc;
		this.cid = cid;
		this.added = added;
		this.status = status;
		this.role = role;
		this.youtube = youtube;
	}

	public int tid() {
		return tid;
	}

	public String youtube() {
		return youtube;
	}

	public int eid() {
		return eid;
	}

	public int aid() {
		return aid;
	}

	public int storeid() {
		return sid;
	}

	public String title() {
		return title;
	}

	public String description() {
		return desc;
	}

	public int categoryid() {
		return cid;
	}

	public java.sql.Date added() {
		return added;
	}

	public int status() {
		return status;
	}

	public String statusAsString() {
		return com.askredrover.Constants.tutorialStatusAsString(status);
	}

	public int role() {
		return role;
	}
}
