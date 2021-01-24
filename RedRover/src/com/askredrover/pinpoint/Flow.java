package com.askredrover.pinpoint;

public class Flow {

	private int fid, accountid, pid, cid, indx = 0;
	private String phone, input, output = null;

	public Flow(int fid, int accountid, int pid, int cid, String phone, String input, String output, int indx) {
		this.fid = fid;
		this.accountid = accountid;
		this.pid = pid;
		this.cid = cid;
		this.phone = phone;
		this.input = input;
		this.output = output;
		this.indx = indx;
	}

	public int flowid() {
		return fid;
	}

	public int accountid() {
		return accountid;
	}

	public int pid() {
		return pid;
	} // project id

	public int cid() {
		return cid;
	}

	public String phone() {
		return phone;
	}

	public String input() {
		return input;
	}

	public String keyphrase() {
		return input;
	}

	public String output() {
		return output;
	}

	public int index() {
		return indx;
	}

}
