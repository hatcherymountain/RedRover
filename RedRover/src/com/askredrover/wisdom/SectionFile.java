package com.askredrover.wisdom;

public class SectionFile {

	private int asfid, aid, sectionid, fileid = 0;

	public SectionFile(int asfid, int aid, int sectionid, int fileid) {
		this.asfid = asfid;
		this.aid = aid;
		this.sectionid = sectionid;
		this.fileid = fileid;
	}

	public int asfid() {
		return asfid;
	}

	public int articleid() {
		return aid;
	}

	public int sectionid() {
		return sectionid;
	}

	public int fileid() {
		return fileid;
	}

}
