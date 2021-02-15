package com.askredrover.wisdom;

public interface Category {

	public int eid();

	public int catid();

	public String category();

	public String categoryURLEncoded();

	public String description();

	public int parentid();

	public int siblingid();

	public boolean isSibling();

}
