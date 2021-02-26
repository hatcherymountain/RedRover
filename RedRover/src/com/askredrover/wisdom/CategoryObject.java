package com.askredrover.wisdom;

public class CategoryObject implements Category {

	private int eid, catid, parentid, siblingid = 0;
	private String category, desc = null;

	public CategoryObject(int catid, int eid, String category, String desc, int parentid, int siblingid) {
		this.eid = eid;
		this.catid = catid;
		this.category = category; if(category == null) { category = ""; }
		this.desc = desc;
		this.parentid = parentid;
		this.siblingid = siblingid;
	}

	public int eid() {
		return eid;
	}

	public int catid() {
		return catid;
	}

	public String category() {
		return category;
	}

	public String categoryURLEncoded() {
		return com.eos.utils.Security.encodeForSafeUrl(category);
	}

	public String description() {
		return desc;
	}

	public int parentid() {
		return parentid;
	}

	public int siblingid() {
		return siblingid;
	}

	public boolean isSibling() {
		if (siblingid > 0) {
			return true;
		} else {
			return false;
		}
	}
}
