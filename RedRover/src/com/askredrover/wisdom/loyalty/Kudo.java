package com.askredrover.wisdom.loyalty;

public class Kudo {

	private int kid, eid, userid, id, typeof = 0;

	private String comment = null;

	public Kudo(int kid, int eid, int userid, int id, int typeof, String comment) {

		this.kid = kid;
		this.eid = eid;
		this.userid = userid;
		this.id = id;
		this.typeof = typeof;
		this.comment = comment;

	}

	public int kid() {
		return kid;
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

	public String comment() {
		return comment;
	}

}
