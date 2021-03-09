package com.askredrover.wisdom;

public class Step {

	private int tutid, indx, fileid = 0;
	private String title, url, description = null;

	public Step(int tutid, int indx, String title, String description, String url, int fileid) {
		this.tutid = tutid;
		this.indx = indx;
		this.title = title;
		this.description = description;
		this.url = url;
		this.fileid = fileid;
	}

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
