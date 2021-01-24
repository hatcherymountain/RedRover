package com.askredrover.pinpoint;
import com.eos.Eos;
import java.sql.*;
import java.util.*;
import com.askredrover.*;

public class Flows {
	
	private Eos eos = null;
	private RedRover rr = null;
	
	public Flows(Eos eos, RedRover redrover)
	{
		this.eos=eos;
		this.rr = redrover;
	}
	
	/**
	 * When a new account comes on board, we go through a simple test flow. This flow is later removed ONCE team members are added to the system
	 * 
	 * @param nid
	 */
	public void addOnboardingFlow(String nid)
	{
		
		if(eos.active())
		{
			
			Connection c = eos.c();
			Statement  s = null;
			
			try { 
				
				 String phone = rr.numbers().numberAsString(nid);
					 if(phone!=null) { 
					 s = c.createStatement();
					 int aid = eos.user().getAccountId();
					 String sql = "insert into rr_flows values(null,"+aid+",0,0,'"+phone+"','test','Welcome to RedRover!',0)";
					 s.execute(sql);
				}
				
			} catch(Exception e)
			{
				eos.log("Errors setting up onboarding flow. Err:" + e.toString(),"Flows","addOnboardingFlow",2);
			} finally { 
				eos.cleanup(c,s);
			}
			
		}
	}
	
}
