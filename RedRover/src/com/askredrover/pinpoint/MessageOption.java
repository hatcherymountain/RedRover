package com.askredrover.pinpoint;

public class MessageOption {

	private int fid, pid, accountid, cid, indx, parentFid = 0;
	private String input, output, phone = null;

	public MessageOption(int fid, int accountid, int pid, int cid, String phone, String input, String output, int indx,
			int parentFid) {
		this.fid = fid;
		this.accountid = accountid;
		this.pid = pid;
		this.cid = cid;
		this.phone = phone;
		this.input = input;
		this.output = output;
		this.indx = indx;
		this.parentFid = parentFid;
	}

	public int accountid() {
		return accountid;
	}

	public int cid() {
		return cid;
	}

	public int index() {
		return indx;
	}

	public String input() {
		return input;
	}

	public int optionid() {
		return fid;
	}

	public String output() {
		return output;
	}

	public int parentFid() {
		return parentFid;
	}

	public String phone() {
		return phone;
	}

	public int pid() {
		return pid;
	}
}