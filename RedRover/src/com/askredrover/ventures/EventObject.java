package com.askredrover.ventures;
import java.sql.Date;
import java.sql.Timestamp;

public class EventObject implements Event {

	private int eventid,eid,aid,vid,sow,milestone,owner,priority,progress,status = 0;
	private String title,desc = null;
	private Date dateof,due,added = null;
	private Timestamp entered = null;
	
	public EventObject(int eventid, int eid, int aid, int vid, String title, String desc, int sow, int milestone, 
			Date dateof, Date due, Date added, Timestamp entered, int owner, int priority, int progress, int status)
	{
		this.eventid=eventid;
		this.eid=eid;
		this.aid=aid;
		this.vid=vid;
		this.title=title;
		this.desc=desc;
		this.sow=sow;
		this.milestone=milestone;
		this.dateof=dateof;
		this.due=due;
		this.added=added;
		this.entered=entered;
		this.owner=owner;
		this.priority=priority;
		this.progress=progress;
		this.status=status;
	}
	
	public int eventid() { return eventid; } 
	public int eid() { return eid;}
	public int aid() { return aid; }
	public int ventureid() { return vid; }
	public String title() { return title; }
	public String description() { return desc; }
	public boolean sow() { if(sow==1) { return true; } else { return false; }}
	public boolean milestone() { if(milestone == 1) { return true; } else { return false; }}
	public Date dateof() { return dateof; }
	public Date due() { return due; }
	public Date added() { return added; } 
	public Timestamp entered() { return entered; }
	public int owner() { return owner; }
	public int priority() { return priority; }
	public String priorityAsString() { return com.askredrover.Constants.eventPriorityAsString(priority); } 
	
	public int progress() { return progress; }
	public int status() { return status; }
	
	public String statusAsString() {
		return com.askredrover.Constants.eventStatusAsString(status);
	}
	
	
	public String priorityColorAsString()
	{
		return com.askredrover.Constants.eventPriorityColorAsString(priority);
	}
	
}
