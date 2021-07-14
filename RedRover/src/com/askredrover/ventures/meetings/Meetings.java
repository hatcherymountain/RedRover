package com.askredrover.ventures.meetings;

import java.sql.*;
import java.util.ArrayList;
import com.eos.Eos;
import com.askredrover.RedRover;
import com.askredrover.ventures.*;

public class Meetings {

	private Eos eos = null;
	private RedRover rr = null;

	public Meetings(Eos eos, RedRover rr) {
		this.eos = eos;
		this.rr = rr;
	}

	/**
	 * Gets all meetings associated with a given venture.
	 * 
	 * @param ventureid
	 * @return
	 */
	public ArrayList<Meeting> getAll(String ventureid) {
		ArrayList<Meeting> lst = new ArrayList<Meeting>();

		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;
		try {

			int v = eos.d(ventureid);

			s = c.createStatement();
			String sql = "select mid,author,added,meetingdate,typeof,agenda, notes, title, aliaskey from rr_meetings where vid="
					+ v + " order by meetingdate desc";
			
			rs = s.executeQuery(sql);
			while (rs.next()) {
				int mid = rs.getInt(1);
				int a = rs.getInt(2);
				java.sql.Date ad = rs.getDate(3);
				java.sql.Date md = rs.getDate(4);
				int to = rs.getInt(5);
				String ag = rs.getString(6);
				String n = rs.getString(7);
				String title = rs.getString(8);
				String k = rs.getString(9);

				lst.add(new MeetingObject(mid, v, a, ad, md, to, ag, n, title, k));
			}

		} catch (Exception e) {
			eos.log("Errors getting meetings for Venture. Err:" + e.toString(), "Meetings", "get", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return lst;
	}

	/**
	 * Sends the meeting to the team!
	 * 
	 * @param meetid
	 */
	public void sendMeetingToTeam(String meetid) {
		Meeting m = this.get(meetid);
		if(m!=null)
		{
			String vid = eos.e(m.vid());
			Venture v = rr.ventures().getVenture(vid);
			if(v!=null) { 
			String title = v.title() + " &mdash; " + eos.date(m.meetingDate()) + "";
			String subject = "Meeting minutes for RedRover venture <B>" + title + "</b>";
			
			String minutes = m.agenda();
			String notes = m.notes();
			
			/** Build actions **/
			String actions = ""; 
			
			rr.emails().sendMeeting(vid, subject, minutes, notes, actions);
			
			}
		}
	}

	/**
	 * Removes a meeting action item
	 * 
	 * @param aid
	 */
	public void removeActionItem(String aid) {
		if (eos.active()) {

			Connection c = eos.c();
			Statement s = null;

			try {

				s = c.createStatement();
				int id = eos.d(aid);
				String sql = "delete from rr_meeting_actions where aid=" + id + "";
				s.execute(sql);

			} catch (Exception e) {
				eos.log("Errors removing action items. Err:" + e.toString(), "Meetings", "removeActionItem", 2);
			} finally {
				eos.cleanup(c, s);
			}
		}
	}

	public void updateActionStatus(String aid, String status) {
		if (eos.active()) {

			Connection c = eos.c();
			Statement s = null;

			try {

				s = c.createStatement();
				int id = eos.d(aid);
				int iStatus = com.eos.utils.Strings.getIntFromString(status);
				String sql = "update rr_meeting_actions set status=" + iStatus + " where  aid=" + id + "";
				s.execute(sql);

			} catch (Exception e) {
				eos.log("Errors updating action status. Err:" + e.toString(), "Meetings", "updateActionStatus", 2);
			} finally {
				eos.cleanup(c, s);
			}
		}
	}

	/**
	 * Toggles the status
	 * 
	 * @param aid
	 */
	public void updateActionItemStatus(String aid, String v) {
		if (eos.active()) {

			int id = eos.d(aid);

			int iV = com.eos.utils.Strings.getIntFromString(v);

			Connection c = eos.c();
			Statement s = null;
			try {

				s = c.createStatement();

				String sql = "update rr_meeting_actions set status=" + iV + " where aid=" + id + "";

				s.execute(sql);

			} catch (Exception e) {
				eos.log("Errors toggling action item status.Err:" + e.toString(), "Meetings", "toggleActionItemStatus",
						2);
			} finally {
				eos.cleanup(c, s);
			}
		}

	}

	/**
	 * Add action item for a given meeting
	 * 
	 * @param r
	 */
	public void addAction(javax.servlet.http.HttpServletRequest r) {
		if (eos.active()) {
			String m = r.getParameter("mid");
			String v = r.getParameter("vid");
			String o = r.getParameter("owner"); // owner
			String p = r.getParameter("priority"); // priority
			String t = r.getParameter("title");
			String d = r.getParameter("description");
			String wd = r.getParameter("when");
			String a = r.getParameter("autotask"); // create task from this action item?

			int iM = eos.d(m);
			int iV = eos.d(v);
			int iO = eos.d(o);
			int iP = com.eos.utils.Strings.getIntFromString(p);
			t = com.eos.Eos.clean(t);
			d = com.eos.Eos.clean(d);
			String w = com.eos.utils.Calendar.clean(wd);

			int iAuto = com.eos.utils.Forms.getCheckboxState(a);

			Connection c = eos.c();
			Statement s = null;
			try {

				s = c.createStatement();

				String sql = "insert into rr_meeting_actions values(null," + iM + "," + iV + "," + iO + "," + iP + ",'"
						+ t + "','" + d + "','" + w + "',1)";
				s.execute(sql);

			} catch (Exception e) {
				eos.log("Errors adding action for meeting. Err:" + e.toString(), "Meetings", "addAction", 2);
			} finally {
				eos.cleanup(c, s);
				if (iAuto == 1) {
					String lastActionItemID = getLastActionItemID();
					createTask(v, o, p, t, d, wd, lastActionItemID);
				}
			}
		}
	}

	/**
	 * Gets the last action identifier
	 * 
	 * @return
	 */
	private String getLastActionItemID() {
		String aid = null;

		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;

		try {

			s = c.createStatement();

			String sql = "select max(aid) from rr_meeting_actions";
			rs = s.executeQuery(sql);
			while (rs.next()) {
				aid = eos.e(rs.getInt(1));
			}

		} catch (Exception e) {
			eos.log("Errors getting lasting action item id. Err:" + e.toString(), "Meetings", "getLastActionID", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return aid;
	}

	/**
	 * Get a specific action
	 * 
	 * @param aid
	 * @param mid
	 * @return
	 */
	public Action action(String aid, String mid) {
		Action a = null;
		if (eos.active()) {
			int id = eos.d(aid);
			ArrayList<Action> lst = actions(mid);
			int size = lst.size();
			for (int i = 0; i < size; i++) {
				Action _a = (Action) lst.get(i);
				if (_a.aid() == id) {
					a = _a;
					break;
				}
			}
		}
		return a;
	}

	/**
	 * Get all action items for a meeting
	 * 
	 * @param mid
	 * @return
	 */
	public ArrayList<Action> actions(String mid) {
		ArrayList<Action> lst = new ArrayList<Action>();
		if (eos.active()) {
			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				s = c.createStatement();
				int id = eos.d(mid);

				String sql = "select aid,vid,owner,priority,title,note,due,status from rr_meeting_actions where mid="
						+ id + " order by due desc";
				rs = s.executeQuery(sql);
				while (rs.next()) {
					int a = rs.getInt(1);
					int v = rs.getInt(2);
					int o = rs.getInt(3);
					int p = rs.getInt(4);
					String t = rs.getString(5);
					String d = rs.getString(6);
					java.sql.Date due = rs.getDate(7);
					int status = rs.getInt(8);

					Action _a = new Action(a, id, v, o, p, t, d, due, status);
					lst.add(_a);
				}

			} catch (Exception e) {
				eos.log("Errors getting action items. Err:" + e.toString(), "Meetings", "actions", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}
		}
		return lst;
	}

	/**
	 * Takes action item and converts it to a task.
	 * 
	 * @param vid
	 * @param owner
	 * @param priority
	 * @param title
	 * @param description
	 * @param when
	 */
	private void createTask(String vid, String owner, String priority, String title, String description, String when,
			String aid) {
		rr.events().add(vid, title, description, "0", "0", when, when, owner, priority, "0", "1", "0", aid);
	}

	/**
	 * Saves the meeting basic elements -agenda and notes.
	 * 
	 * @param mid
	 * @param agenda
	 * @param notes
	 */
	public void saveBasics(String mid, String title, String agenda, String notes) {

		if (eos.active()) {
			Connection c = eos.c();
			Statement s = null;
			try {

				s = c.createStatement();

				agenda = com.eos.Eos.clean(agenda);
				notes = com.eos.Eos.clean(notes);
				title = com.eos.Eos.clean(title);
				int id = eos.d(mid);

				s.addBatch("update rr_meetings set agenda='" + agenda + "' where mid=" + id + "");
				s.addBatch("update rr_meetings set notes='" + notes + "' where mid=" + id + "");
				s.addBatch("update rr_meetings set title='" + title + "' where mid=" + id + "");
				s.executeBatch();

			} catch (Exception e) {
				eos.log("Errors saving meeting basics. Err:" + e.toString(), "Meetings", "saveBasics", 2);
			} finally {
				eos.cleanup(c, s);
			}
		}
	}

	/**
	 * uses specific key to get meeting
	 * 
	 * @param key
	 * @return
	 */
	public Meeting getFromKey(String key) {
		Meeting meeting = null;

		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;
		try {

			s = c.createStatement();
			key = com.eos.Eos.clean(key);
			String sql = "select mid,vid,author,added,meetingdate,typeof,agenda, notes, title from rr_meetings where aliaskey='" + key + "'";
			rs = s.executeQuery(sql);

			while (rs.next()) {

				int m = rs.getInt(1);
				int v = rs.getInt(2);
				int a = rs.getInt(3);
				java.sql.Date ad = rs.getDate(4);
				java.sql.Date md = rs.getDate(5);
				int to = rs.getInt(6);
				String ag = rs.getString(7);
				String n = rs.getString(8);
				String title = rs.getString(9);
				meeting = new MeetingObject(m, v, a, ad, md, to, ag, n, title, key);
			}

		} catch (Exception e) {
			eos.log("Errors getting meeting using alias KEY. Err:" + e.toString(), "Meetings", "getFromKey", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return meeting;
	}

	/**
	 * Get a specific meeting
	 * 
	 * @param meetingid
	 * @return
	 */
	public Meeting get(String meetingid) {
		Meeting m = null;
		int mid = eos.d(meetingid);
		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;
		try {

			s = c.createStatement();
			String sql = "select vid,author,added,meetingdate,typeof,agenda, notes, title, aliaskey from rr_meetings where mid="
					+ mid + "";
			rs = s.executeQuery(sql);

			while (rs.next()) {

				int v = rs.getInt(1);
				int a = rs.getInt(2);
				java.sql.Date ad = rs.getDate(3);
				java.sql.Date md = rs.getDate(4);
				int to = rs.getInt(5);
				String ag = rs.getString(6);
				String n = rs.getString(7);
				String title = rs.getString(8);
				String k = rs.getString(9);

				m = new MeetingObject(mid, v, a, ad, md, to, ag, n, title, k);
			}

		} catch (Exception e) {
			eos.log("Errors getting meeting. Err:" + e.toString(), "Meetings", "get", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return m;
	}

	private String generateKey() {
		return com.eos.utils.Strings.getRandomString() + "" + com.eos.utils.Math.getRandom5Numbers() + "";
	}

	/**
	 * Adds a new Venture meeting
	 * 
	 * @param ventureid
	 * @return
	 */
	public String add(String ventureid, String typeof, String meetingDate, String title) {
		String mid = null;
		Connection c = eos.c();
		PreparedStatement p = null;
		ResultSet rs = null;

		try {

			String today = com.eos.utils.Calendar.getTodayForSQL();
			int author = eos.user().getUserId();
			int vid = eos.d(ventureid);
			int iType = com.eos.utils.Strings.getIntFromString(typeof);
			meetingDate = com.eos.utils.Calendar.clean(meetingDate);
			title = com.eos.Eos.clean(title);
			if (title.length() == 0) {
				title = "Undefined Meeting";
			}
			String sql = "insert into rr_meetings values(null,?,?,'" + today + "','" + meetingDate + "',?,'','',?,?)";

			p = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			p.setInt(1, vid);
			p.setInt(2, author);
			p.setInt(3, iType);
			p.setString(4, title);
			p.setString(5, generateKey());

			p.executeUpdate();
			rs = p.getGeneratedKeys();

			if (rs.next()) {
				mid = eos.e(rs.getInt(1));
			}

		} catch (Exception e) {
			eos.log("Errors adding new venture meeting. Err:" + e.toString(), "Meetings", "add", 2);
		} finally {
			eos.cleanup(c, p, rs);
		}
		return mid;
	}

}
