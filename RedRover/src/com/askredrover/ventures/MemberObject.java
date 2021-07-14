package com.askredrover.ventures;

public class MemberObject implements Member {

	private int userid, eid, vid, role, owner, checkin = 0;

	public MemberObject(int userid, int eid, int vid, int role, int owner, int checkin) {
		this.userid = userid;
		this.eid = eid;
		this.vid = vid;
		this.role = role;
		this.owner = owner;
		this.checkin = checkin;
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
		if (owner == 1 || role == com.askredrover.Constants.ROLE_OWNER) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Alias to ProjectManager method
	 * 
	 * @return
	 */
	public boolean pm() {
		return projectManager();
	}

	public boolean projectManager() {
		if (owner == 1 || role == com.askredrover.Constants.ROLE_PROJECT_MANAGER) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Can this member manage elements through the project
	 * 
	 * @return
	 */
	public boolean manage() {

		if (projectManager() || owner() || editor()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean editor() {
		if (role == com.askredrover.Constants.ROLE_EDITOR) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Does this member get a daily checkin message Mon->Friday?
	 */
	public boolean dailyCheckin() {
		if (checkin == 1) {
			return true;
		} else {
			return false;
		}
	}
}
