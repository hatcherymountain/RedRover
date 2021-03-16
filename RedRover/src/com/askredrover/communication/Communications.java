package com.askredrover.communication;
import java.sql.*;
import java.util.*;
import com.eos.Eos;
import com.askredrover.RedRover;

public class Communications {
	
	private Eos eos = null;
	private RedRover rr = null;
	
	public Communications(Eos eos, RedRover rr)
	{
		this.eos=eos;
		this.rr=rr;
	}
	
	/**
	 * Super important method. It checks to see if there is a record for this user before we start querying it. If there is no record, we create
	 * a stub with defaults.
	 * @param User identifier as int
	 */
	private void checkUserCommunicationFramework(int userid) { 
		Connection c = eos.c();
		Statement  s = null;
		ResultSet rs = null;
		try { 
			int count = 0;
			s = c.createStatement();
			rs = s.executeQuery("select count(*) from rr_user_communications where userid=" + userid + "");
			while(rs.next())
			{
				count = rs.getInt(1);
			}
			
			if(count == 0) { 
				
				String today = com.eos.utils.Calendar.getTodayForSQL();
				String sql = "insert into rr_user_communications values(" + userid+",1,1,1,1,1,1,'"+today+"')";
				s.execute(sql);
				
			}
			
			
		} catch(Exception e)
		{
			eos.log("Erros getting communication framework information. Err:" + e.toString(),"Communications","checkUserCommunicationFramework",2);
		} finally { 
			eos.cleanup(c,s,rs);
		}
	}
	/**
	 * Get User Communications. This provides a single call framework making it much easier to determine what you should and should not do for a user.
	 * Additional methods will support this when functions need to run to initiate comms.
	 * @param userid
	 * @return Communication.class
	 */
	public Communication getUserCommunication(String userid)
	{ 
		Communication com = null;
		int uid = eos.d(userid);
		
		/** Check we have a record in db to operate with for this specific user **/
		checkUserCommunicationFramework(uid);
		
		
		Connection c = eos.c();
		Statement  s = null;
		ResultSet rs = null;
		
		try { 
			
			boolean sms = false;
			String phone = ""; 
			String email = ""; 
			boolean weekly = false;
			boolean monthly = false;
			boolean annual = false;
			boolean verbose = false;
			boolean groupc = false;
			boolean companyc = false;
			java.sql.Date la = null;
			
			com.eos.accounts.Profile profile = eos.getUsers().profile(userid);
			if(profile!=null)
			{
				sms = profile.sms();
			}
			
			com.eos.accounts.User user = eos.getUsers().getUser(userid);
			if(user!=null && user.getStatus()==1) { 
				phone = user.phone();
				email = user.getEmail();
			}
			
			
			s = c.createStatement();
			
			String sql = "select weekly,monthly, annual,verbose,groupconsolidation,companyconsolidation,lastupdated from rr_user_communications where userid=" + uid+"";
			rs = s.executeQuery(sql);
			
			while(rs.next())
			{
				int w = rs.getInt(1); if(w==1) { weekly = true; } 
				int m = rs.getInt(2); if(m==1) { monthly = true; } 
				int a = rs.getInt(3); if(a==1) { annual = true; }
				int v = rs.getInt(4); if(v == 1) { verbose = true; } 
				int gc= rs.getInt(5); if(gc==1) { groupc = true; }
				int cc= rs.getInt(6); if(cc == 1) { companyc = true; }  
				la = rs.getDate(7);
			 }
			
			com = new CommunicationObject(uid,sms,phone,email,weekly,monthly,annual,verbose,groupc,companyc,la);
			
		} catch(Exception e) { 
			eos.log("Error getting user communication. Err:" + e.toString(),"Communications","getUserCommunications",2);
		} finally { 
			eos.cleanup(c,s,rs);
		}
		return com;
	}
}
	
