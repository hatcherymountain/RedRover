package com.askredrover.ventures;

import com.eos.Eos;
import com.askredrover.RedRover;
import java.sql.*;
import java.util.*;
import com.eos.accounts.User;

public class Ventures {

	private SOWS sows = null;
	private RedRover rr = null;
	private Eos eos = null;
	private ArrayList<Group> groups = null;

	public Ventures(Eos eos, RedRover rr) {
		this.eos = eos;
		this.rr = rr;
		loadGroups();
	}
	
	
	/**
	 * Public method to access SOWS/Objectives of any venture.
	 * @return
	 */
	public SOWS sows() { 
		if(sows==null) { 
			sows = new SOWS(eos,rr);
		}
		return sows;
	}
	
	
	public void removeVenture(String ventureid)
	{
		/**
		 * Remove SOW
		 * Remove Members
		 * Remove Events
		 * Remove Members
		 */
	}
	/**
	 * Loads the list of groups
	 */
	private void loadGroups() {
		groups = new ArrayList<Group>();
		if (eos.active()) {
			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				s = c.createStatement();
				rs = s.executeQuery("select groupid,groupname,description from rr_venture_groups where eid="
						+ eos.account().eid() + " order by groupname asc");
				while (rs.next()) {
					groups.add(new Group(rs.getInt(1), rs.getString(2), rs.getString(3)));
				}

			} catch (Exception e) {
				eos.log("Errors loading groups. Err:" + e.toString(), "Ventures", "loadgroups", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}

		}
	}

	/**
	 * informs the new members they have been added to a new venture.
	 * 
	 * @param vid
	 * @param ArrayList of User objects.
	 */
	private void notifyNewMembers(int vid, ArrayList<User> lst) {
		// TODO Notify Members
	}

	/**
	 * Adds members to the new venture...
	 * 
	 * @param int                ventureid
	 * @param HttpServletRequest r
	 */
	private void addInitialMembers(int vid, javax.servlet.http.HttpServletRequest r) {

		Connection c = eos.c();
		Statement s = null;

		ArrayList<User> notifylist = new ArrayList<User>();

		try {

			s = c.createStatement();
			String[] lst = r.getParameterValues("memberid");

			if(lst!=null) { 
				for (int x = 0; x < lst.length; x++) {
					String m = (String) lst[x];
					User mem = eos.users().getUser(m);
					 if(mem.getUserId() != eos.user().getUserId()) { 
					 int eid = eos.account().eid();
					 s.addBatch(
							"insert into rr_venture_members values(" + mem.getUserId() + "," + eid + "," + vid + ",0,0)");
					 notifylist.add(mem);
					}
				}
	
				s.executeBatch();
			
			}
		} catch (Exception e) {
			eos.log("Errors adding new venture members. Err:" + e.toString(), "Ventures", "addMembers[private]", 2);
		} finally {
			eos.cleanup(c, s);
			notifyNewMembers(vid, notifylist);
		}
	}
	
	
	private void addOwner(int vid) { 
		Connection c = eos.c();
		Statement  s = null;
		try { 
				
			s = c.createStatement();
			int eid = eos.account().eid();
			int r = com.askredrover.Constants.ROLE_OWNER;
			
			String sql = "insert into rr_venture_members values(" + eos.user().getUserId() + "," + eid + ","+vid+"," +r + ",1)";
			s.execute(sql);
				
		} catch(Exception e)
		{
			eos.log("Errors adding venture owner. Err:" + e.toString(),"Ventures","addOwner",2);
		} finally { 
			eos.cleanup(c,s);
		}
	}
	
	/**
	 * Adds members to the new venture...
	 * 
	 * @param int                ventureid
	 * @param HttpServletRequest r
	 */
	private void addMembers(int vid, javax.servlet.http.HttpServletRequest r) {

		Connection c = eos.c();
		Statement s = null;

		ArrayList<User> notifylist = new ArrayList<User>();

		try {

			s = c.createStatement();
			String[] lst = r.getParameterValues("memberid");
			if(lst!=null) { 
	
				for (int x = 0; x < lst.length; x++) {
					String m = (String) lst[x];
					User mem = eos.users().getUser(m);
					int eid = eos.account().eid();
					s.addBatch(
							"insert into rr_venture_members values(" + mem.getUserId() + "," + eid + "," + vid + ",0,0)");
					notifylist.add(mem);
				}
	
				s.executeBatch();
				
			}

		} catch (Exception e) {
			eos.log("Errors adding new venture members. Err:" + e.toString(), "Ventures", "addMembers[private]", 2);
		} finally {
			eos.cleanup(c, s);
			notifyNewMembers(vid, notifylist);
		}
	}

	/**
	 * Get Ventures
	 * 
	 * @param status
	 * @return
	 */
	public Venture getVenture(String ventureid) {

		Venture venture = null;

		if (eos.active()) {

			/** if iStatus == -1 we get ALL **/

			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				s = c.createStatement();

				int id = eos.d(ventureid);
				String sql = "select vid,eid,aid,cid,title,description,typeof,status,added,entered,author,groupid from rr_venture where vid="
						+ id + "";

				rs = s.executeQuery(sql);

				while (rs.next()) {
					int v = rs.getInt(1);
					int e = rs.getInt(2);
					int a = rs.getInt(3);
					int _c = rs.getInt(4);
					String t = rs.getString(5);
					String d = rs.getString(6);
					int to = rs.getInt(7);
					int st = rs.getInt(8);
					java.sql.Date ad = rs.getDate(9);
					java.sql.Timestamp en = rs.getTimestamp(10);
					int au = rs.getInt(11);
					int groupid = rs.getInt(12);

					venture = new VentureObject(v, e, a, _c, t, d, to, st, ad, en, au,groupid);

				}

			} catch (Exception e) {
				eos.log("Errors getting venture. Err:" + e.toString(), "Ventures", "getVenture", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}

		}
		return venture;
	}

	/**
	 * Get Ventures
	 * 
	 * @param status
	 * @return
	 */
	public ArrayList<Venture> get(String status) {
		ArrayList<Venture> lst = new ArrayList<Venture>();
		if (eos.active()) {
			int iStatus = eos.d(status);

			/** if iStatus == -1 we get ALL **/

			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				s = c.createStatement();

				int eid = eos.account().eid();
				String sql = "select vid,eid,aid,cid,title,description,typeof,status,added,entered,author,groupid from rr_venture where eid="
						+ eid + "";
				if (iStatus > -1) {
					sql = sql + " and status=" + iStatus + "";
				}

				rs = s.executeQuery(sql);

				while (rs.next()) {
					int v = rs.getInt(1);
					int e = rs.getInt(2);
					int a = rs.getInt(3);
					int _c = rs.getInt(4);
					String t = rs.getString(5);
					String d = rs.getString(6);
					int to = rs.getInt(7);
					int st = rs.getInt(8);
					java.sql.Date ad = rs.getDate(9);
					java.sql.Timestamp en = rs.getTimestamp(10);
					int au = rs.getInt(11);
					int groupid = rs.getInt(12);

					VentureObject ven = new VentureObject(v, e, a, _c, t, d, to, st, ad, en, au,groupid);

					lst.add(ven);

				}

			} catch (Exception e) {
				eos.log("Errors getting ventures. Err:" + e.toString(), "Ventures", "get", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}

		}
		return lst;
	}

	/**
	 * Return the groups.
	 * 
	 * @return
	 */
	public ArrayList<Group> groups() {
		return groups;
	}

	/**
	 * Get the group selection as a HTML selection list
	 * 
	 * @param selectName
	 * @return String HTML
	 */
	public String getGroupsAsSelection(String selectName, String currentlySelected) {
		StringBuffer sb = new StringBuffer();

		int size = groups.size();
		int iCurrent = eos.d(currentlySelected);

		sb.append("<select name=\"" + selectName + "\" class=\"form-control\">");
		sb.append("<option value=\"0\">Select a group</option/>");
		for (int x = 0; x < size; x++) {

			Group g = (Group) groups.get(x);
			String eid = eos.e(g.groupid());

			if (g.groupid() == iCurrent) {
				sb.append("<option selected value=\"" + eid + "\">" + g.name() + "</option>");
			} else {
				sb.append("<option  value=\"" + eid + "\">" + g.name() + "</option>");
			}
		}

		sb.append("</select>");

		return sb.toString();
	}
	
	/**
	 * Adds a SOW or set of Objectives to the new Venture.
	 * @param vid
	 */
	private void addSOWStub(int vid)
	{
		Connection c = eos.c();
		Statement  s = null;
		
		try { 
			
			s = c.createStatement();
			String d = com.eos.utils.Calendar.NO_EXPIRE_DATE;
			String sql = "insert into rr_sow values(null,"+vid+",0,'"+d+"','"+d+"','"+d+"','Objectives','',0,0,0,'"+d+"',0)";
			s.execute(sql);
			
		} catch(Exception e)
		{
			eos.log("Errors adding SOW stub for new venture. Err:" + e.toString(),"Ventures","addSOWStub",2);
		} finally { 
			eos.cleanup(c,s);
		}
	}
	
	/**
	 * Adds a new Venture
	 * 
	 * @param Request object
	 * @return String encoded venture identifier. Need to check for 0/null.
	 */
	public String add(javax.servlet.http.HttpServletRequest r) {
		int vid = 0;

		Connection c = eos.c();
		PreparedStatement p = null;
		ResultSet rs = null;

		try {

			int eid = eos.account().eid();
			int aid = eos.user().getAccountId();
			String today = com.eos.utils.Calendar.getTodayForSQL();
			int iGroup = eos.d(r.getParameter("groupid"));

			String sql = "insert into rr_venture values(null,?,?,?,?,?,?,?,'" + today + "',null,?,?)";

			p = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

			p.setInt(1, eid);
			p.setInt(2, aid);
			p.setInt(3, eos.d(r.getParameter("cid")));
			p.setString(4, com.eos.Eos.clean(r.getParameter("title")));
			p.setString(5, com.eos.Eos.clean(r.getParameter("vinfo")));
			p.setInt(6, eos.d(r.getParameter("vtype")));
			p.setInt(7, 0);
			p.setInt(8, eos.user().getUserId());
			p.setInt(9,iGroup);
			
			p.executeUpdate();
			rs = p.getGeneratedKeys();

			if (rs.next()) {
				vid = rs.getInt(1);
			}

		} catch (Exception e) {
			eos.log("Errors adding new venture. Err:" + e.toString(), "Ventures", "add", 2);
		} finally {
			eos.cleanup(c, p, rs);
			if (vid > 0) {
				addOwner(vid);
				addInitialMembers(vid, r);
				addSOWStub(vid);
			}
		}

		return eos.e(vid);
	}

}
