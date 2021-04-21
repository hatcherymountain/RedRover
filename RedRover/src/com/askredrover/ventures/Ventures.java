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
	private ArrayList<Type> types = null;

	public Ventures(Eos eos, RedRover rr) {
		this.eos = eos;
		this.rr = rr;
		loadTypes();
	}

	public ArrayList<Type> ventureTypes() {
		return types;
	}

	public String ventureTypeAsString(String tid) {
		String vt = null;
		Type _t = ventureType(tid);
		if (_t != null) {
			vt = _t.name();
		} else {
			vt = "Undefined";
		}
		return vt;
	}

	/**
	 * Get a specific Type
	 * 
	 * @param tid
	 * @return
	 */
	public Type ventureType(String tid) {
		Type t = null;
		int id = com.eos.utils.Strings.getIntFromString(tid);
		int size = types.size();
		for (int i = 0; i < size; i++) {
			Type _type = (Type) types.get(i);
			if (_type.typeid() == id) {
				t = _type;
				break;
			}
		}
		return t;
	}

	/**
	 * Get Ventures types as an HTML select list.
	 * 
	 * @return
	 */
	public String ventureTypesAsSelectList() {
		StringBuffer sb = new StringBuffer();
		if (eos.active()) {
			int size = types.size();
			for (int i = 0; i < size; i++) {
				Type t = (Type) types.get(i);

				if (t.name().equalsIgnoreCase("New Product")) {
					sb.append("<option selected value=" + i + ">" + t.name() + "</option>");
				} else {
					sb.append("<option value=" + i + ">" + t.name() + "</option>");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Defines the types of projects we provide in Ventures.
	 */
	private void loadTypes() {
		if (eos.active()) {
			types = new ArrayList<Type>();
			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;
			try {
				s = c.createStatement();
				rs = s.executeQuery("select typeid,name,description from rr_venture_types order by name asc");
				while (rs.next()) {
					int t = rs.getInt(1);
					String n = rs.getString(2);
					String d = rs.getString(3);
					if (d == null) {
						d = "";
					}
					types.add(new Type(t, n, d));
				}

			} catch (Exception e) {
				eos.log("Errors getting venture types. Err:" + e.toString(), "Ventures", "loadTypes", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}
		}
	}

	/**
	 * Public method to access SOWS/Objectives of any venture.
	 * 
	 * @return
	 */
	public SOWS sows() {
		if (sows == null) {
			sows = new SOWS(eos, rr);
		}
		return sows;
	}

	public void removeVenture(String ventureid) {
		/**
		 * Remove SOW Remove Members Remove Events Remove Members
		 */
	}

	/**
	 * Loads the list of groups
	 */
	public ArrayList<Group> groups() {

		ArrayList<Group> groups = new ArrayList<Group>();

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
		return groups;
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

			if (lst != null) {
				for (int x = 0; x < lst.length; x++) {
					String m = (String) lst[x];
					User mem = eos.users().getUser(m);
					if (mem.getUserId() != eos.user().getUserId()) {
						int eid = eos.account().eid();
						s.addBatch("insert into rr_venture_members values(" + mem.getUserId() + "," + eid + "," + vid
								+ ",0,0)");
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
		Statement s = null;
		try {

			s = c.createStatement();
			int eid = eos.account().eid();
			int r = com.askredrover.Constants.ROLE_OWNER;

			String sql = "insert into rr_venture_members values(" + eos.user().getUserId() + "," + eid + "," + vid + ","
					+ r + ",1)";
			s.execute(sql);

		} catch (Exception e) {
			eos.log("Errors adding venture owner. Err:" + e.toString(), "Ventures", "addOwner", 2);
		} finally {
			eos.cleanup(c, s);
		}
	}

	public ArrayList<Member> members(String vid) {
		return members(eos.d(vid));
	}

	/**
	 * Get all members of a venture.
	 * 
	 * @param vid
	 * @return
	 */
	private ArrayList<Member> members(int vid) {
		ArrayList<Member> lst = new ArrayList<Member>();

		if (eos.active()) {

			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {
				int eid = eos.account().eid();

				s = c.createStatement();
				String sql = "select userid,userrole,isowner from rr_venture_members where eid=" + eid + " and vid="
						+ vid + "";
				rs = s.executeQuery(sql);
				while (rs.next()) {
					int uid = rs.getInt(1);
					int ur = rs.getInt(2);
					int uo = rs.getInt(3);
					Member m = new MemberObject(uid, eid, vid, ur, uo);
					lst.add(m);
				}

			} catch (Exception e) {
				eos.log("Errors getting venture members. Err;" + e.toString(), "Ventures", "members", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}

		}

		return lst;
	}

	public boolean isMember(String vid, String userid) {
		return isMember(eos.d(vid), eos.d(userid));
	}

	public boolean isMember(String vid) {
		return isMember(eos.d(vid), eos.user().getUserId());
	}

	/**
	 * Is a given user a member of the venture?
	 * 
	 * @param vid
	 * @param userid
	 * @return
	 */
	private boolean isMember(int vid, int userid) {
		boolean is = false;
		ArrayList<Member> lst = members(vid);
		int size = lst.size();
		for (int i = 0; i < size; i++) {
			Member m = (Member) lst.get(i);
			if (m.userid() == userid) {
				is = true;
				break;
			}
		}

		return is;
	}

	/**
	 * Get the membership role a person has
	 * 
	 * @param vid
	 * @param userid
	 * @return Member.class
	 */
	private Member getMembership(int vid, int userid) {
		Member m = null;

		ArrayList<Member> lst = members(vid);
		int size = lst.size();
		for (int i = 0; i < size; i++) {
			Member _m = (Member) lst.get(i);
			if (_m.userid() == userid) {
				m = _m;
				break;
			}
		}

		return m;
	}

	public Member getMembership(String vid, String userid) {
		return getMembership(eos.d(vid), eos.d(userid));
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
			if (lst != null) {

				for (int x = 0; x < lst.length; x++) {
					String m = (String) lst[x];
					User mem = eos.users().getUser(m);
					int eid = eos.account().eid();
					s.addBatch("insert into rr_venture_members values(" + mem.getUserId() + "," + eid + "," + vid
							+ ",0,0)");
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
	 * Get Ventures for a particular user.
	 * 
	 * @param userid
	 * @return
	 */
	public ArrayList<Venture> getVentures(String userid) {
		ArrayList<Venture> lst = new ArrayList<Venture>();

		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;

		try {

			s = c.createStatement();
			int uid = eos.d(userid);
			int eid = eos.account().eid();
			String sql = "select distinct vid from rr_venture_members where eid=" + eid + " and userid=" + uid + "";
			rs = s.executeQuery(sql);
			while (rs.next()) {
				int id = rs.getInt(1);
				Venture v = this.getVenture(id);
				if (v != null) {
					lst.add(v);
				}
			}

		} catch (Exception e) {
			eos.log("Errors getting ventures for a user. Err:" + e.toString(), "Ventures", "getVentures[uid]", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return lst;
	}

	/**
	 * Get a venture using a venture identifier (enc)
	 * 
	 * @param ventureid
	 * @return Venture.class
	 */
	public Venture getVenture(String ventureid) {
		return getVenture(eos.d(ventureid));
	}

	/**
	 * Get Ventures
	 * 
	 * @param status
	 * @return
	 */
	private Venture getVenture(int id) {

		Venture venture = null;

		if (eos.active()) {

			/** if iStatus == -1 we get ALL **/

			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				s = c.createStatement();

				String sql = "select vid,eid,aid,cid,title,description,typeof,status,added,entered,author,groupid,color,progress,sentiment from rr_venture where vid="
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
					String color = rs.getString(13);
					int pro = rs.getInt(14);
					int sen = rs.getInt(15);

					venture = new VentureObject(v, e, a, _c, t, d, to, st, ad, en, au, groupid, color, pro, sen);

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
				String sql = "select vid,eid,aid,cid,title,description,typeof,status,added,entered,author,groupid, color,progress,sentiment from rr_venture where eid="
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
					String color = rs.getString(13);
					int pro = rs.getInt(14);
					int sen = rs.getInt(15);

					VentureObject ven = new VentureObject(v, e, a, _c, t, d, to, st, ad, en, au, groupid, color, pro,
							sen);

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
	 * Find Ventures linked to a group.
	 * 
	 * @param groupid
	 * @param notArchived ... if true, does NOT show archived Ventures in the
	 *                    resultset
	 * @return ArrayList of Venture.class
	 */
	public ArrayList<Venture> groupVentures(String groupid, boolean notArchived) {
		ArrayList<Venture> lst = new ArrayList<Venture>();

		if (eos.active()) {

			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;
			int gid = eos.d(groupid);

			try {

				int eid = eos.account().eid();
				s = c.createStatement();
				String sql = "select vid from rr_venture where eid=" + eid + " and groupid=" + gid + "";
				if (notArchived) {
					sql = sql + " and status < " + com.askredrover.Constants.VENTURE_STATE_ARCHIVED + "";
				}

				sql = sql + " order by title asc";

				// eos.log(sql);

				rs = s.executeQuery(sql);
				while (rs.next()) {
					int v = rs.getInt(1);
					String vid = eos.e(v);
					Venture venture = this.getVenture(vid);
					if (venture != null) {
						lst.add(venture);
					}
				}

			} catch (Exception e) {
				eos.log("Errors getting ventures linked to group. Err;" + e.toString(), "Ventures", "groupVentures", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}

		}

		return lst;
	}

	/**
	 * Get the group selection as a HTML selection list
	 * 
	 * @param selectName
	 * @return String HTML
	 */
	public String getGroupsAsSelection(String selectName, String currentlySelected) {
		StringBuffer sb = new StringBuffer();

		int size = groups().size();
		int iCurrent = eos.d(currentlySelected);

		sb.append("<select name=\"" + selectName + "\" class=\"form-control\">");
		sb.append("<option value=\"0\">Select a group</option/>");
		for (int x = 0; x < size; x++) {

			Group g = (Group) groups().get(x);
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
	 * 
	 * @param vid
	 */
	private void addSOWStub(int vid) {
		Connection c = eos.c();
		Statement s = null;

		try {

			s = c.createStatement();
			String d = com.eos.utils.Calendar.NO_EXPIRE_DATE;
			String sql = "insert into rr_sow values(null," + vid + ",0,'" + d + "','" + d + "','" + d
					+ "','Objectives','',0,0,0,'" + d + "',0)";
			s.execute(sql);

		} catch (Exception e) {
			eos.log("Errors adding SOW stub for new venture. Err:" + e.toString(), "Ventures", "addSOWStub", 2);
		} finally {
			eos.cleanup(c, s);
		}
	}

	/**
	 * Indicate when it all began :)
	 * 
	 * @param vid
	 */
	public void addInitialEvent(int vid, String title, String desc, String today, String owner) {
		String notset = com.eos.utils.Calendar.NO_EXPIRE_DATE;

	}

	/**
	 * Check if the group exists.
	 * 
	 * @param title
	 * @return boolean
	 */
	private boolean groupExists(String title) {
		boolean exists = false;
		ArrayList<Group> lst = groups();
		int size = lst.size();
		for (int i = 0; i < size; i++) {
			Group g = (Group) lst.get(i);
			if (g.name().equalsIgnoreCase(title)) {
				exists = true;
				break;
			}
		}
		return exists;
	}

	/**
	 * Adds a new group
	 * 
	 * @param HttpServletRequest
	 */
	public void addGroup(javax.servlet.http.HttpServletRequest r) {
		if (eos.isAdmin()) {

			Connection c = eos.c();
			Statement s = null;
			try {

				int eid = eos.account().eid();
				String t = r.getParameter("title");
				t = com.eos.Eos.clean(t).trim();
				String d = r.getParameter("description");
				d = com.eos.Eos.clean(d);

				if (t.length() > 0 && (!groupExists(t))) {
					s = c.createStatement();
					String sql = "insert into rr_venture_groups values(null," + eid + ",'" + t + "','" + d + "')";
					s.execute(sql);
				}

			} catch (Exception e) {
				eos.log("Errors adding new group. Err:" + e.toString(), "Ventures", "addGroup", 2);
			} finally {
				eos.cleanup(c, s);
			}

		}
	}

	/**
	 * Adds default Event!
	 * 
	 * @param vid
	 * @param title
	 * @param desc
	 */
	private void addInitialEvent(int vid, String title, String desc) {
		Connection c = eos.c();
		Statement s = null;
		try {

			s = c.createStatement();
			String today = com.eos.utils.Calendar.getTodayForSQL();
			int eid = eos.account().eid();
			int aid = eos.user().getAccountId();
			String notset = com.eos.utils.Calendar.NO_EXPIRE_DATE;
			int uid = eos.user().getUserId();
			String sql = "insert into rr_events values(null," + eid + "," + aid + "," + vid + ",'" + title + "','"
					+ desc + "',0,1,'" + today + "','" + notset + "','" + today + "',null," + uid + ",1,0,0)";
			s.execute(sql);

		} catch (Exception e) {
			eos.log("Errors adding intial event. Err:" + e.toString(), "Ventures", "addInitialEvent", 2);
		} finally {
			eos.cleanup(c, s);
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
		String title = "";
		String desc = "";

		try {

			int eid = eos.account().eid();
			int aid = eos.user().getAccountId();
			String today = com.eos.utils.Calendar.getTodayForSQL();
			int iGroup = eos.d(r.getParameter("groupid"));

			String sql = "insert into rr_venture values(null,?,?,?,?,?,?,?,'" + today + "',NOW(),?,?,?,0,?)";

			p = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			title = com.eos.Eos.clean(r.getParameter("title"));
			desc = com.eos.Eos.clean(r.getParameter("vinfo"));
			String color = com.eos.Eos.clean(r.getParameter("color"));
			String _type = r.getParameter("vtype");
			int iType = com.eos.utils.Strings.getIntFromString(_type);

			if (title.length() == 0) {
				title = "Venture - " + today + "";
			}

			p.setInt(1, eid);
			p.setInt(2, aid);
			p.setInt(3, eos.d(r.getParameter("cid")));
			p.setString(4, title);
			p.setString(5, desc);
			p.setInt(6, iType);
			p.setInt(7, 0);
			p.setInt(8, eos.user().getUserId());
			p.setInt(9, iGroup);
			p.setString(10, color);
			p.setInt(11, com.askredrover.Constants.VENTURE_SENTIMENT_GOOD); // Most projects start of with a good
																			// sentiment...

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
				addInitialEvent(vid, title, desc);
				addOwner(vid);
				addInitialMembers(vid, r);
				addSOWStub(vid);
			}
		}

		return eos.e(vid);
	}

}
