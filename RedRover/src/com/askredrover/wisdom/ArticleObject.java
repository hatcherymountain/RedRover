package com.askredrover.wisdom;

public class ArticleObject implements Article {
	
	private int articleid, accountid,storeid,author,essential,headerimg,status,roleid = 0;
	private String title,description,tags = null;
	private java.sql.Date added = null;
	private java.sql.Timestamp entered = null;
	
	public ArticleObject(int articleid,int accountid, int storeid, String title, String description, java.sql.Date added, java.sql.Timestamp entered, int author, int essential,
			String tags, int headerimg, int status, int roleid)
	{
		this.articleid=articleid;
		this.accountid=accountid;
		this.storeid=storeid;
		this.title=title;
		this.description = description;
		this.added=added;
		this.entered = entered;
		this.author=author;
		this.essential=essential;
		this.tags=tags;
		this.headerimg=headerimg;
		this.status=status;
		this.roleid=roleid;
	}
	
	
	public int articleid() {
		return articleid;
	}

	public int accountid() {
		return accountid;
	}

	public int storeid() {
		return storeid;
	}

	public String title() {
		return title;
	}

	public String description() {
		return description;
	}

	public java.sql.Date added() {
		return added;
	}

	public java.sql.Timestamp entered() {
		return entered;
	}

	public int author() {
		return author;
	}

	public boolean essential() {
		if (essential == 1) {
			return true;
		} else {
			return false;
		}
	}

	public String tags() {
		return tags;
	}

	public int headerImg() {
		return headerimg;
	}

	public int status() {
		return status;
	}

	public int roleid() {
		return roleid;
	}

	public String statusAsString() {
		return com.askredrover.Constants.articleStatusAsString(status);
	}
}
