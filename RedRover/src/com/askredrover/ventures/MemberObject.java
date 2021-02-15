package com.askredrover.ventures;

public class MemberObject implements Member {

	private int userid, eid, vid, role, owner = 0;

	public MemberObject(int userid, int eid, int vid, int role, int owner) {
		this.userid = userid;
		this.eid = eid;
		this.vid = vid;
		this.role = role;
		this.owner = owner;
	}

	public int userid() {
		return userid;
	}

	public int eid() {
		return eid;
	}

	public int vid() {
		return vid;
	}

	public int role() {
		return role;
	}

	public String roleAsString() {
		return com.askredrover.Constants.roleAsString(role);
	}

	public boolean owner() {
		if (owner == 1) {
			return true;
		} else {
			return false;
		}
	}

}
