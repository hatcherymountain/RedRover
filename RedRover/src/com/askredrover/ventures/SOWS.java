package com.askredrover.ventures;
import java.sql.*;
import java.util.ArrayList;
import com.askredrover.RedRover;
import com.eos.Eos;

public class SOWS {
	
	private Eos eos = null;
	private RedRover rr = null;
	
	public SOWS(Eos eos, RedRover rr) { 
		this.eos=eos;
		this.rr=rr;
	}
	
	/**
	 * Update the SOW core.
	 * @param sowid
	 * @param title
	 * @param desc
	 * @param starts
	 * @param ends
	 */
	public  void updateCore(String sowid, String title, String desc,  String starts, String ends)
	{
		if(eos.active())
		{
			Connection c = eos.c();
			Statement  s = null;
			
			try { 
				
				s = c.createStatement();
				
				int sid = eos.d(sowid);
				title = com.eos.Eos.clean(title);
				desc  = com.eos.Eos.clean(desc);
				starts = com.eos.utils.Calendar.clean(starts);
				ends   = com.eos.utils.Calendar.clean(ends);
				
				s = c.createStatement();
				s.addBatch("update rr_sow set title='" + title + "' where sowid=" + sid + "");
				s.addBatch("update rr_sow set description='" + desc + "' where sowid=" + sid + "");
				s.addBatch("update rr_sow set starts='" + starts + "' where sowid=" + sid + "");
				s.addBatch("update rr_sow set ends='" + ends + "' where sowid=" + sid + "");
				s.addBatch("update rr_sow set firstesit=1 where sowid=" + sid + "");
				s.executeBatch();
				
			} catch(Exception e)
			{
				eos.log("Errors updating SOW core. Err:" + e.toString(),"SOWS","updateCore",2);
			} finally { 
				eos.cleanup(c,s);
			}
		}
	}
	/**
	 * Gets the SOW using venture id
	 * @param ventureid
	 * @return
	 */
	public SOW ventureSOW(String ventureid)
	{
		SOW  sow = null;
		
		Connection c = eos.c(); 
		Statement  s = null;
		ResultSet rs = null;
		try { 
			
			s = c.createStatement();
			int id = eos.d(ventureid);
			
			String sql = "select sowid,status,starts,ends,actualend,title,description,endventureonclose,authorizer,issigned,signdate,firstedit from rr_sow where vid="+id+"";
			rs = s.executeQuery(sql);
			
			while(rs.next())
			{
				int sid = rs.getInt(1);
				int st = rs.getInt(2);
				java.sql.Date starts = rs.getDate(3);
				java.sql.Date ends   = rs.getDate(4);
				java.sql.Date actualend = rs.getDate(5);
				String title = rs.getString(6);
				String desc  = rs.getString(7);
				int endonclose = rs.getInt(8);
				int authorizer = rs.getInt(9);
				int signed = rs.getInt(10);
				java.sql.Date signdate = rs.getDate(11);
				int fedit = rs.getInt(12);
				
				sow = new SowObject(sid,id,st,starts,ends,actualend,title,desc,endonclose,authorizer,signed,signdate,fedit);
			
			}
			
		} catch(Exception e)
		{
			eos.log("Errors getting venture SOW. Err:" + e.toString(),"SOWS","ventureSOW",2);
		} finally { 
			eos.cleanup(c,s,rs);
		}
		return sow;
	}
	
}
