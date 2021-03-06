package com.askredrover.wisdom;

public interface Article {

	public int articleid();

	public int accountid();

	public int storeid();

	public String title();

	public String description();

	public java.sql.Date added();

	public java.sql.Timestamp entered();

	public int author();

	public boolean essential();

	public String tags();

	public int headerImg();

	public int status();

	public int roleid();

	public int categoryid();

	public String statusAsString();
	
	public boolean live();

}
