package com.askredrover.utils;
import com.eos.Eos;
import java.sql.*;
import com.askredrover.RedRover;

public class Users {
	
	private Eos eos = null;
	private RedRover rr = null;
	
	public Users(Eos eos, RedRover rr)
	{
		this.eos=eos;
		this.rr = rr;
	}
	
	/**
	 * Removes a user using the EOS system and then removes RedRover-specific needs.
	 * @param userid
	 */
	public void removeUser(String userid)
	{
		if(eos.isAdmin() || eos.isContributor())
		{
			eos.getUsers().removeUser(userid);
			
			Connection c = eos.c();
			Statement  s = null;
			try { 
				
				int uid = eos.d(userid);
				s = c.createStatement();
				s.execute("delete from rr_venture_members where userid=" + uid + "");
				
			} catch(Exception e ) { 
				eos.log("Errors removing user redrover attributes. Err:" + e.toString(),"utils","removeUser",2);
			} finally { 
				eos.cleanup(c, s);
			}
		}
	}
	
}
