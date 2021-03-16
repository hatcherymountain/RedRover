package com.askredrover.wisdom;

public class Step {

	private int tutid, indx, fileid,stepid = 0;
	private String title, url, description = null;

	public Step(int stepid, int tutid, int indx, String title, String description, String url, int fileid) {
		this.tutid = tutid;
		this.indx = indx;
		this.title = title;
		this.description = description;
		this.url = url;
		this.fileid = fileid;
		this.stepid=stepid;
	}
	
	public int stepid() { return stepid; }

	public int tid() {
		return tutid;
	}

	public int index() {
		return indx;
	}

	public String title() {
		return title;
	}

	public String description() {
		return description;
	}

	public String url() {
		return url;
	}

	public int fileid() {
		return fileid;
	}

}
