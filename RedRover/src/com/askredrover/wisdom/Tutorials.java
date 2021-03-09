package com.askredrover.wisdom;

import com.eos.Eos;
import com.askredrover.RedRover;
import java.sql.*;
import java.util.ArrayList;

public class Tutorials {

	private Eos eos = null;
	private RedRover rr = null;

	public Tutorials(Eos eos, RedRover rr) {
		this.eos = eos;
		this.rr = rr;
	}

	/**
	 * Add new tutorial
	 * 
	 * @param title
	 * @param description
	 * @param storeid
	 * @param category
	 */
	public String add(String title, String description, String storeid, String categoryid, String roleid) {
		String tid = null;
		if (eos.isAdmin()) {

			Connection c = eos.c();
			PreparedStatement ps = null;
			ResultSet rs = null;

			try {

				int eid = eos.account().eid();
				int aid = eos.user().getAccountId();
				int cid = eos.d(categoryid);
				int sid = eos.d(storeid);
				int iRole = eos.d(roleid);

				if (cid > 0) { // Need categories!
					String today = com.eos.utils.Calendar.getTodayForSQL();

					String sql = "insert into wisdom_tutorials values(null," + eid + "," + aid + "," + sid + ",'"
							+ title + "','" + description + "'," + cid + ",'" + today + "',0," + iRole + ")";

					ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
					ps.executeUpdate();

					rs = ps.getGeneratedKeys();

					if (rs.next()) {
						tid = eos.e(rs.getInt(1));
					}
				}

			} catch (Exception e) {
				eos.log("Errors adding new tutorial. Err:" + e.toString(), "Tutorials", "add", 2);
			} finally {
				eos.cleanup(c, ps, rs);
			}

		}
		return tid;
	}

	/**
	 * Add a step.
	 * @param tid
	 * @param title
	 */
	public void addStep(String tid, String title) { 
		
		if(eos.isAdmin()) {
			
			Connection c = eos.c();
			Statement  s = null;
			
			try { 
				
				
				s = c.createStatement();
				
				int id = eos.d(tid);
				int indx = nextIndex(id);
				title = com.eos.Eos.clean(title);
				
				if(title.length() > 0)
				{
					
					String sql = "insert into wisdom_tutorial_step values(" + id + "," + indx + ",'" + title + "','','',0)";
					s.execute(sql);
					
				}
				
			} catch(Exception e)
			{
				eos.log("Errors adding step. Err:" + e.toString(),"Tutorials","addStep",2);
			} finally { 
				eos.cleanup(c,s);
			}
			
		}
		
	}
	
	
	/**
	 * Get steps in tutorial
	 * @param tid
	 * @return ArrayList Step.class
	 */
	public ArrayList<Step> steps(String tid) { 
		ArrayList<Step> lst = new ArrayList<Step>();
		
		Connection c = eos.c();
		Statement  s = null;
		ResultSet rs = null;
		
		try { 
			
			s = c.createStatement();
			int id = eos.d(tid);
			String sql = "select tutid,indx,title,description,url,fileid from wisdom_tutorial_step where tutid="+id+" order by indx asc";
			rs = s.executeQuery(sql);
			while(rs.next())
			{
				int t = rs.getInt(1);
				int i = rs.getInt(2);
				String ti = rs.getString(3);
				String d = rs.getString(4);
				String u = rs.getString(5);
				int fi = rs.getInt(6);
				
				lst.add(new Step(t,i,ti,d,u,fi));
				
			}
			
		} catch(Exception e) { 
			eos.log("Errors getting steps. Err;" + e.toString(),"Tutorials","steps",2);
		} finally { 
			eos.cleanup(c,s,rs);
		}
		
		return lst;
	}

	/**
	 * Gets a specific tutorial
	 * 
	 * @param tutid
	 * @return
	 */
	public Tutorial get(String tutid) {
		Tutorial t = null;

		if (eos.active()) {

			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				int eid = eos.account().eid();
				int id = eos.d(tutid);

				s = c.createStatement();
				String sql = "select tutid,eid,accountid,storeid,title,description,categoryid,added,status,roleid from wisdom_tutorials where eid="
						+ eid + " and tutid=" + id + "";
				
				rs = s.executeQuery(sql);
				
				while (rs.next()) {

					int tut = rs.getInt(1);
					int e = rs.getInt(2);
					int a = rs.getInt(3);
					int sid = rs.getInt(4);
					String title = rs.getString(5);
					String desc = rs.getString(6);
					int cid = rs.getInt(7);
					java.sql.Date added = rs.getDate(8);
					int status = rs.getInt(9);
					int rid = rs.getInt(10);

					t = new TutorialObject(tut, e, a, sid, title, desc, cid, added, status,rid);

				}

			} catch (Exception e) {
				eos.log("Errors getting tutorial. Err:" + e.toString(), "Tutorials", "get", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}

		}

		return t;
	}
	
	/**
	 * Get all tutorials
	 * 
	 * @return ArrayList of Tutorial.class
	 */
	public ArrayList<Tutorial> getAll() {
		ArrayList<Tutorial> lst = new ArrayList<Tutorial>();

		if (eos.active()) {

			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				int eid = eos.account().eid();

				s = c.createStatement();
				String sql = "select tutid,eid,accountid,storeid,title,description,categoryid,added,status,roleid from wisdom_tutorials where eid="
						+ eid + " order by added desc";
				rs = s.executeQuery(sql);
				while (rs.next()) {

					int tut = rs.getInt(1);
					int e = rs.getInt(2);
					int a = rs.getInt(3);
					int sid = rs.getInt(4);
					String title = rs.getString(5);
					String desc = rs.getString(6);
					int cid = rs.getInt(7);
					java.sql.Date added = rs.getDate(8);
					int status = rs.getInt(9);
					int rid = rs.getInt(10);

					Tutorial t = new TutorialObject(tut, e, a, sid, title, desc, cid, added, status,rid);
					lst.add(t);

				}

			} catch (Exception e) {
				eos.log("Errors getting all tutorials. Err:" + e.toString(), "Tutorials", "getAll", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}

		}

		return lst;
	}
	

	
	
	/**
	 * Gets next most likely index for a tutorial
	 * @param tid
	 * @return
	 */
	private int nextIndex(int tid) { 
		int i = 0;
		
		Connection c = eos.c();
		Statement  s = null;
		ResultSet rs = null;
		
		try { 
			
			s = c.createStatement();
			String sql = "select max(indx) from wisdom_tutorial_step where tutid=" + tid + "";
			rs = s.executeQuery(sql);
			while(rs.next())
			{
				int ci = rs.getInt(1);
				i = ci+1;
			}
		} catch(Exception e)
		{
			eos.log("Errors getting step index. Err:" + e.toString(),"Tutorials","nextIndex",2);
		} finally { 
			eos.cleanup(c,s,rs);
		}
		return i;
	}
	
	
	
	
	

}
