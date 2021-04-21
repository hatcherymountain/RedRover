package com.askredrover.wisdom.search;

public class Search {

	private int sid, storeid, userid, files, articles, tuts = 0;
	private String added, term = null;

	public Search(int sid, int userid, String added, String term, int files, int articles, int tuts) {
		this.sid = sid;
		this.userid = userid;
		this.added = added;
		this.term = term;
		this.files = files;
		this.articles = articles;
		this.tuts = tuts;
	}

	public int searchId() {
		return sid;
	}

	public int userid() {
		return userid;
	}

	public String added() {
		return added;
	}

	public String term() {
		return term;
	}

	public int files() {
		return files;
	}

	public int articles() {
		return articles;
	}

	public int tutorials() {
		return tuts;
	}

}
