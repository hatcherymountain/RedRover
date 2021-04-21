package com.askredrover.wisdom.loyalty;

import com.eos.accounts.User;

public class Kudo {

	private User user = null;
	private int kid, eid, id, typeof = 0;

	public Kudo(int kid, int eid, User user, int id, int typeof) {

		this.kid = kid;
		this.eid = eid;
		this.user = user;
		this.id = id;
		this.typeof = typeof;

	}

	public int kid() {
		return kid;
	}

	public int eid() {
		return eid;
	}

	public User user() {
		return user;
	}

	public int id() {
		return id;
	}

	public int typeof() {
		return typeof;
	}

}
