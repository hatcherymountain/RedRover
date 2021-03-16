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
					
					String sql = "insert into wisdom_tutorial_step values(null," + id + "," + indx + ",'" + title + "','','',0)";
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
	
	public void remove(String tutid)
	{
		if(eos.isAdmin())
		{
			Connection c = eos.c();
			Statement  s = null;
			try {
				s = c.createStatement();
				int id = eos.d(tutid);
				String sql = "delete from wisdom_tutorial_step where tutid=" + id + "";
				s.execute(sql);
				
				String sql2 = "delete from wisdom_tutorials where tutid=" + id + "";
				s.execute(sql2);
				
			} catch(Exception e) { 
				eos.log("Errors removing a tutorial. Err:" + e.toString(),"Tutorials","removeStep",2);
			} finally { 
				eos.cleanup(c, s);
			}
		}
	}
	
	
	
	/**
	 * Remove a step.
	 * @param stepid
	 */
	public void removeStep(String stepid)
	{
		if(eos.isAdmin())
		{
			Connection c = eos.c();
			Statement  s = null;
			try {
				s = c.createStatement();
				int id = eos.d(stepid);
				String sql = "delete from wisdom_tutorial_step where stepid=" + id + "";
				s.execute(sql);
				
			} catch(Exception e) { 
				eos.log("Errors removing a step. Err:" + e.toString(),"Tutorials","removeStep",2);
			} finally { 
				eos.cleanup(c, s);
			}
		}
	}
	
	/**
	 * Get a step
	 * @param sid
	 * @return Step.class
	 */
	public Step getStep(String sid) { 
		Step step = null;
		
		Connection c = eos.c();
		Statement  s = null;
		ResultSet rs = null;
		
		try { 
			
			s = c.createStatement();
			int id = eos.d(sid);
			String sql = "select tutid,indx,title,description,url,fileid from wisdom_tutorial_step where stepid="+id+"";
			rs = s.executeQuery(sql);
			while(rs.next())
			{	
				
				int t = rs.getInt(1);
				int i = rs.getInt(2);
				String ti = rs.getString(3);
				String d = rs.getString(4);
				String u = rs.getString(5);
				int fi = rs.getInt(6);
				
				step = new Step(id,t,i,ti,d,u,fi);
				
			}
			
		} catch(Exception e) { 
			eos.log("Errors getting a specific step object. Err;" + e.toString(),"Tutorials","getStep",2);
		} finally { 
			eos.cleanup(c,s,rs);
		}
		
		return step;
	}
	
	/**
	 * Updates tutorial
	 * @param r
	 */
	public void update(javax.servlet.http.HttpServletRequest r)  
	{
		if(eos.isAdmin())
		{
			Connection c = eos.c();
			Statement  s = null;
			try {
				
				s =c.createStatement();
				int id = eos.d(r.getParameter("tid"));

				String status = r.getParameter("status");
				int iStatus = com.eos.utils.Strings.getIntFromString(status);
				
				String role = r.getParameter("role"); int iRole = eos.d(role);
				
				String storeid = r.getParameter("storeid"); int sid = eos.d(storeid);
				
				String title = r.getParameter("title"); title = com.eos.Eos.clean(title);
				
				String desc  = r.getParameter("desc"); desc = com.eos.Eos.clean(desc);
				
				String category = r.getParameter("categoryid"); int iCat = eos.d(category);
				
				s.addBatch("update wisdom_tutorials set status="+ iStatus + " where tutid=" + id + "");
				s.addBatch("update wisdom_tutorials set categoryid="+ iCat + " where tutid=" + id + "");
				s.addBatch("update wisdom_tutorials set roleid="+ iRole + " where tutid=" + id + "");
				s.addBatch("update wisdom_tutorials set storeid="+ sid + " where tutid=" + id + "");
				s.addBatch("update wisdom_tutorials set title='"+ title + "' where tutid=" + id + "");
				s.addBatch("update wisdom_tutorials set description='"+ desc + "' where tutid=" + id + "");
			
				s.executeBatch();
		
				
			} catch(Exception e)
			{
				eos.log("Errors updating tutorial metadata. Err:" + e.toString(),"Step","updateStepFile",2);
			} finally { 
				eos.cleanup(c,s);
			}
		}
	}
	
	
	
	/**
	 * Update Step to include file.
	 * @param sid
	 * @param fileid
	 */
	public void updateStepFile(String sid, String fileid)
	{
		if(eos.isAdmin())
		{
			Connection c = eos.c();
			Statement  s = null;
			try {
				
				s =c.createStatement();
				int id = eos.d(sid);
				int fid = eos.d(fileid);
				
				s.execute("update wisdom_tutorial_step set fileid=" + fid + " where stepid=" + id + "");
				
			} catch(Exception e)
			{
				eos.log("Errors updating step file. Err:" + e.toString(),"Step","updateStepFile",2);
			} finally { 
				eos.cleanup(c,s);
			}
		}
	}
	
	/**
	 * Remove Step File.
	 * @param sid
	 */
	public void removeFile(String sid)
	{
		if(eos.isAdmin())
		{
			Connection c = eos.c();
			Statement  s = null;
			try {
				
				s =c.createStatement();
				int id = eos.d(sid);
				
				s.execute("update wisdom_tutorial_step set fileid=0 where stepid=" + id + "");
				
			} catch(Exception e)
			{
				eos.log("Errors updating step file. Err:" + e.toString(),"Step","updateStepFile",2);
			} finally { 
				eos.cleanup(c,s);
			}
		}
	}
	
	
	
	
	
	/**
	 * Updates the basic text attributes of a step.
	 * @param sid
	 * @param title
	 * @param description
	 * @param url
	 * @param index
	 */
	public void updateStepContent(String sid, String title, String description, String url, String index)
	{
		if(eos.isAdmin())
		{
			Connection c = eos.c();
			Statement  s = null;
			try {
				
				s =c.createStatement();
				int id = eos.d(sid);
				
				title = com.eos.Eos.clean(title);
				if(title.length() > 0)  { 
					s.addBatch("update wisdom_tutorial_step set title='" + title + "' where stepid="+id +"");
				}
				
				description = com.eos.Eos.clean(description);
				s.addBatch("update wisdom_tutorial_step set description='" + description + "' where stepid="+id +"");
				
				url = com.eos.Eos.clean(url);
				s.addBatch("update wisdom_tutorial_step set url='" + url + "' where stepid="+id +"");
				
				int iIndex = com.eos.utils.Strings.getIntFromString(index);
				s.addBatch("update wisdom_tutorial_step set indx=" + iIndex + " where stepid="+id +"");
				
				s.executeBatch();
				
			} catch(Exception e)
			{
				eos.log("Errors updating step content. Err:" + e.toString(),"Step","updateStepContent",2);
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
			String sql = "select stepid,tutid,indx,title,description,url,fileid from wisdom_tutorial_step where tutid="+id+" order by indx asc";
			rs = s.executeQuery(sql);
			while(rs.next())
			{	
				int sid = rs.getInt(1);
				int t = rs.getInt(2);
				int i = rs.getInt(3);
				String ti = rs.getString(4);
				String d = rs.getString(5);
				String u = rs.getString(6);
				int fi = rs.getInt(7);
				
				lst.add(new Step(sid,t,i,ti,d,u,fi));
				
			}
			
		} catch(Exception e) { 
			eos.log("Errors getting steps. Err;" + e.toString(),"Tutorials","steps",2);
		} finally { 
			eos.cleanup(c,s,rs);
		}
		
		return lst;
	}
	
	
	public ArrayList<Tutorial> getTutorialsForUser(String categoryid) { 
		return getTutorialsForUser(eos.d(categoryid));
	}
	
	private ArrayList<Tutorial> getTutorialsForUser(int cid) { 
		
		ArrayList<Tutorial> lst = new ArrayList<Tutorial>();

		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;
		try {
			
			int eid = eos.account().eid();
			
			s = c.createStatement();
			String sql = "select tutid,accountid,storeid,title,description,added,status,roleid from wisdom_tutorials where eid="
					+ eid + " and categoryid=" + cid + " and status=2 order by added desc";

			rs = s.executeQuery(sql);
			while (rs.next()) {
				
				int tid = rs.getInt(1);
				int aid = rs.getInt(2);
				int sid = rs.getInt(3);
				String t = rs.getString(4);
				String d = rs.getString(5);
				java.sql.Date added = rs.getDate(6);
				int status = rs.getInt(7);
				int role = rs.getInt(8);
				
				
				Tutorial tut = new TutorialObject(tid, eid, aid, sid,t,d, cid, added, status,role);
				
				if(sid>0) { 
					
					if(rr.wisdom().search().isInStore(sid)) {
						lst.add(tut);
					}
					
				} else { 
					lst.add(tut);
				}
			}

		} catch (Exception e) {
			eos.log("Errors getting ALL tutorials for user. Err;" + e.toString(), "Articles", "getArticlesForUser", 2);
		} finally {
			eos.cleanup(c, s, rs);
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
