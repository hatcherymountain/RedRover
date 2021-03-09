package com.askredrover.ventures;

import java.sql.*;
import java.util.ArrayList;
import com.askredrover.RedRover;
import com.eos.Eos;

public class SOWS {

	private Eos eos = null;
	private RedRover rr = null;

	public SOWS(Eos eos, RedRover rr) {
		this.eos = eos;
		this.rr = rr;
	}

	/**
	 * Links file to SOW.
	 * 
	 * @param sowid
	 * @param fileid
	 */
	public void addFile(String sowid, String fileid, String role) {
		if (eos.active()) {

			// TODO ACTIVITY

			int sid = eos.d(sowid);
			int fid = eos.d(fileid);
			int iRole = com.eos.utils.Strings.getIntFromString(role);
			Connection c = eos.c();
			Statement s = null;
			try {

				s = c.createStatement();
				String sql = "insert into rr_sow_files values(" + sid + "," + fid + "," + iRole + ")";
				s.execute(sql);

			} catch (Exception e) {
				eos.log("Errors adding file. Err:" + e.toString(), "SOWS", "addFile", 2);
			} finally {
				eos.cleanup(c, s);
			}
		}
	}
	
	public void removeTask(String eventid) {
		rr.events().remove(eventid);
	}
	
	

	/**
	 * Removes the file from the SOW.
	 * 
	 * @param sowid
	 * @param fileid
	 */
	public void removeFile(String sowid, String fileid) {
		if (eos.active()) {
			Connection c = eos.c();
			Statement s = null;
			try {

				// TODO ACTIVITY
				int sid = eos.d(sowid);
				int fid = eos.d(fileid);
				s = c.createStatement();
				String sql = "delete from rr_sow_files where sowid=" + sid + " and fileid=" + fid + "";
				s.execute(sql);

			} catch (Exception e) {
				eos.log("Errors removing file. Err:" + e.toString(), "SOWS", "removeFile", 2);
			} finally {
				eos.cleanup(c, s);
			}
		}
	}

	/**
	 * Get all linked files.
	 * 
	 * @param sowid
	 * @return
	 */
	public ArrayList<com.eos.files.File> files(String sowid) {
		ArrayList<com.eos.files.File> lst = new ArrayList<com.eos.files.File>();

		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;

		try {

			s = c.createStatement();
			int sid = eos.d(sowid);
			String sql = "select distinct fileid,roleid from rr_sow_files where sowid=" + sid + "";
			rs = s.executeQuery(sql);
			while (rs.next()) {
				String fileid = eos.e(rs.getInt(1));
				com.eos.files.File file = eos.files().getFile(fileid);
				if (file != null) {
					// if(eos.users().isAllowed(file.roleid())) {
					lst.add(file);
					// }
				}
			}

		} catch (Exception e) {
			eos.log("Errors getting files. Err:" + e.toString(), "SOWs", "files", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}
		return lst;
	}
	
	/**
	 * sets the task state
	 * @param eventid
	 * @param status
	 */
	public void setTaskState(String eventid, String status)
	{
		int iStatus = 0;
		if(eos.active())
		{
			Connection c = eos.c();
			Statement  s = null;
			try { 
				
				s = c.createStatement();
				
				
				status = com.eos.Eos.clean(status); //just being careful.
				
				int id = eos.d(eventid);
				
				if(status.equals("true")) { 
					iStatus = com.askredrover.Constants.EVENT_STATE_COMPLETED;
				} else { 
					iStatus = com.askredrover.Constants.EVENT_STATE_ACTIVE;
				}
				
				String sql = "update rr_events set status=" + iStatus + " where eventid=" + id + "";
				s.execute(sql);
				
				
				
			} catch(Exception e)
			{
				eos.log("Errors setting task status. Err:" + e.toString(),"SOWS","setTaskState",2);
			} finally { 
				eos.cleanup(c,s);
			}
		}
		
	}

	/**
	 * Update the SOW core.
	 * 
	 * @param sowid
	 * @param title
	 * @param desc
	 * @param starts
	 * @param ends
	 */
	public void updateCore(String sowid, String title, String desc, String starts, String ends, String status, String milestones) {
		if (eos.active()) {
		
			Connection c = eos.c();
			Statement s = null;

			try {

				s = c.createStatement();

				int sid = eos.d(sowid);
				
				title = com.eos.Eos.clean(title);
				desc = com.eos.Eos.clean(desc);
				String startsClean = com.eos.utils.Calendar.clean(starts);
				String endsClean = com.eos.utils.Calendar.clean(ends);
				
				
				int iS = com.eos.utils.Strings.getIntFromString(status);
				
				s = c.createStatement();
				s.addBatch("update rr_sow set title='" + title + "' where sowid=" + sid + "");
				s.addBatch("update rr_sow set description='" + desc + "' where sowid=" + sid + "");
				s.addBatch("update rr_sow set starts='" + startsClean + "' where sowid=" + sid + "");
				s.addBatch("update rr_sow set ends='" + endsClean + "' where sowid=" + sid + "");
				s.addBatch("update rr_sow set firstedit=1 where sowid=" + sid + "");
				s.addBatch("update rr_sow set status=" + iS + " where sowid=" + sid + "");
				s.executeBatch();
				
				
				

			} catch (Exception e) {
				eos.log("Errors updating SOW core. Err:" + e.toString(), "SOWS", "updateCore", 2);
			} finally {
				eos.cleanup(c, s);
				
				if(com.eos.utils.Forms.getCheckboxState(milestones) == 1) { 
					setSOWDateMilestones(sowid,starts,ends);
				}
			}
		}
	}
	
	/**
	 * Sets milestones dates.
	 * @param sowid
	 * @param start
	 * @param end
	 */
	private void setSOWDateMilestones(String sowid, String start, String end)
	{
		SOW sow = ventureSOW(sowid);
		if(sow!=null)
		{
			String vid = eos.e(sow.ventureid());
			rr.events().addSOWDateMilestones(vid, start, end);
		}
	}

	/**
	 * Gets the SOW using venture id
	 * 
	 * @param ventureid
	 * @return
	 */
	public SOW ventureSOW(String ventureid) {
		SOW sow = null;

		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;
		try {

			s = c.createStatement();
			int id = eos.d(ventureid);

			String sql = "select sowid,status,starts,ends,actualend,title,description,endventureonclose,authorizer,issigned,signdate,firstedit from rr_sow where vid="
					+ id + "";
			rs = s.executeQuery(sql);

			while (rs.next()) {
				int sid = rs.getInt(1);
				int st = rs.getInt(2);
				java.sql.Date starts = rs.getDate(3);
				java.sql.Date ends = rs.getDate(4);
				java.sql.Date actualend = rs.getDate(5);
				String title = rs.getString(6);
				String desc = rs.getString(7);
				int endonclose = rs.getInt(8);
				int authorizer = rs.getInt(9);
				int signed = rs.getInt(10);
				java.sql.Date signdate = rs.getDate(11);
				int fedit = rs.getInt(12);

				sow = new SowObject(sid, id, st, starts, ends, actualend, title, desc, endonclose, authorizer, signed,
						signdate, fedit);

			}

		} catch (Exception e) {
			eos.log("Errors getting venture SOW. Err:" + e.toString(), "SOWS", "ventureSOW", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}
		return sow;
	}
	
	
	

}
