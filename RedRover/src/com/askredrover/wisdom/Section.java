package com.askredrover.wisdom;

public class Section {

	private int sid,articleid,indx,typeof,fileid = 0;
	private String text = null;
	public Section(int sid, int articleid,int indx, int typeof, String text, int fileid)
	{
		this.sid=sid;
		this.articleid=articleid;
		this.indx=indx;
		this.typeof=typeof;
		this.text=text;
		this.fileid = fileid; 
	}
	
	public int sid() { return sid; }
	public int articleid() { return articleid; }
	public int indx() { return indx; } 
	public int typeof() { return typeof; } 
	public String text() { return text; }
	public int fileid() { return fileid; }
	
	public String getTypeAsString() { 
		
		String t = null;
		
		switch(typeof) { 
			
		case 0: t = "Heading Text"; break;
		case 1: t = "Body Text"; break;
		case 2: t = "Quote"; break;
		case 3: t = "Image"; break;
		case 4: t = "Image on Left & Text on Right"; break;
		case 5: t = "Image on Right & Text on Left"; break;
		case 6: t = "YouTube or Vimeo Video"; break;
		}
		
		
		return t;
		
	}
}
