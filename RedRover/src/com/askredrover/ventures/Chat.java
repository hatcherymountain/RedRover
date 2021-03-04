package com.askredrover.ventures;

public class Chat {

	private int chatid, pchatid, sowid, ventureid, author = 0;
	private String message = null;
	private java.sql.Date added = null;
	private java.sql.Timestamp entered = null;

	public Chat(int chatid, int pchatid, int sowid, int ventureid, java.sql.Timestamp entered, java.sql.Date added,
			int author, String message) {
		this.chatid = chatid;
		this.pchatid = pchatid;
		this.sowid = sowid;
		this.ventureid = ventureid;
		this.entered = entered;
		this.added = added;
		this.author = author;
		this.message = message;
	}

	public int chatid() {
		return chatid;
	}

	public int parentChatId() {
		return pchatid;
	}

	public int sowid() {
		return sowid;
	}

	public int ventureid() {
		return ventureid;
	}

	public java.sql.Timestamp entered() {
		return entered;
	}

	public java.sql.Date added() {
		return added;
	}

	public int author() {
		return author;
	}

	public String message() {
		return message;
	}

}
