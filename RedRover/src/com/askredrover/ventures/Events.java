package com.askredrover.ventures;

import java.sql.*;
import java.util.ArrayList;
import com.askredrover.RedRover;
import com.eos.Eos;

public class Events {

	private Eos eos = null;
	private RedRover rr = null;

	public Events(Eos eos, RedRover rr) {
		this.eos = eos;
		this.rr = rr;
	}

	/**
	 * Adds new event straight from request object
	 * 
	 * @param r
	 */
	public void add(javax.servlet.http.HttpServletRequest r) {

	}

	/**
	 * Primary event adding method. Has all options
	 * 
	 * @return
	 */
	public String add(String ventureid, String title, String description, String sow, String milestone, String dateof,
			String due, String ownerid, String priority, String progress, String status) {

		int vid = eos.d(ventureid);
		int iSow = com.eos.utils.Strings.getIntFromString(sow);
		int iMile = com.eos.utils.Strings.getIntFromString(milestone);
		int iOwner = eos.d(ownerid);
		int iPriority = com.eos.utils.Strings.getIntFromString(priority);
		int iProgress = com.eos.utils.Strings.getIntFromString(progress);
		int iStatus = com.eos.utils.Strings.getIntFromString(status);

		return add(vid, title, description, iSow, iMile, dateof, due, iOwner, iPriority, iProgress, iStatus);
	}

	/**
	 * Get events/tasks that are SOW-related
	 * 
	 * @param ventureid
	 * @return
	 */
	public ArrayList<Event> SOWEvents(String ventureid) {
		ArrayList<Event> lst = new ArrayList<Event>();

		if (eos.active()) {

			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				s = c.createStatement();
				int id = eos.d(ventureid);

				String sql = "select eventid,eid,accountid,title,description,milestone, dateof,due,added,entered,owner,priority,progress,status from rr_events where vid="
						+ id + " and sow=1 order by due desc";

				rs = s.executeQuery(sql);
				while (rs.next()) {
					int e = rs.getInt(1);
					int ei = rs.getInt(2);
					int a = rs.getInt(3);
					String t = rs.getString(4);
					String d = rs.getString(5);
					int m = rs.getInt(6);
					java.sql.Date when = rs.getDate(7);
					java.sql.Date due = rs.getDate(8);
					java.sql.Date add = rs.getDate(9);
					java.sql.Timestamp entered = rs.getTimestamp(10);
					int owner = rs.getInt(11);
					int priority = rs.getInt(12);
					int progress = rs.getInt(13);
					int status = rs.getInt(14);

					Event ev = new EventObject(e, ei, a, id, t, d, 1, m, when, due, add, entered, owner, priority,
							progress, status);
					lst.add(ev);
				}

			} catch (Exception e) {
				eos.log("Errors getting SOW events. Err:" + e.toString(), "Events", "sowEvents", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}

		}

		return lst;
	}

	/**
	 * Sets complete
	 * 
	 * @param sowid
	 */
	public void completeSOWEvent(String eventid) {
		if (eos.active()) {
			Connection c = eos.c();
			Statement s = null;
			try {

				s = c.createStatement();
				int eid = eos.d(eventid);
				// ACTIVITY

				String sql = "update rr_events set status=" + com.askredrover.Constants.EVENT_STATE_COMPLETED
						+ " where eventid=" + eid + "";
				s.execute(sql);

			} catch (Exception e) {
				eos.log("Errors completing SOW task. Err:" + e.toString(), "Events", "completeSOWEvent", 2);
			} finally {
				eos.cleanup(c, s);
			}
		}
	}

	/**
	 * Adds a SOW /task/event
	 * 
	 * @param vid
	 * @param title
	 * @param description
	 */
	public void addSOWEvent(String vid, String title, String description, String priority) {
		if (eos.active()) {

			Connection c = eos.c();
			Statement s = null;
			try {

				s = c.createStatement();

				int id = eos.d(vid);
				int uid = eos.user().getUserId();
				int iPriority = com.eos.utils.Strings.getIntFromString(priority);

				add(id, title, description, 1, 1, "", "", uid, iPriority, 0,
						com.askredrover.Constants.EVENT_STATE_ACTIVE);

			} catch (Exception e) {
				eos.log("Errors adding SOW task. Err:" + e.toString(), "Events", "addSOWEvent", 2);
			} finally {
				eos.cleanup(c, s);
			}
		}
	}

	/**
	 * Does the work of adding the event with all necessary checks etc.
	 * 
	 * @param vid
	 * @param title
	 * @param description
	 * @param sow
	 * @param milestone
	 * @param dateof
	 * @param due
	 * @param ownerid
	 * @param priority
	 * @param progress
	 * @param status
	 * @return
	 */
	private String add(int vid, String title, String description, int sow, int milestone, String dateof, String due,
			int ownerid, int priority, int progress, int status) {
		String eventid = null;

		if (eos.active()) {

			Connection c = eos.c();
			PreparedStatement ps = null;
			ResultSet rs = null;

			try {

				int eid = eos.account().eid();
				int accountid = eos.user().getAccountId();
				title = com.eos.Eos.clean(title);
				description = com.eos.Eos.clean(description);
				String today = com.eos.utils.Calendar.getTodayForSQL();

				String _due = "";

				if (due.length() == 0) {
					_due = com.eos.utils.Calendar.NO_EXPIRE_DATE;
				} else {
					_due = com.eos.utils.Calendar.clean(due);
				}

				String _dateOf = "";
				if (dateof.length() == 0) {
					_dateOf = com.eos.utils.Calendar.NO_EXPIRE_DATE;
				} else {
					_dateOf = com.eos.utils.Calendar.clean(dateof);
				}

				String sql = "insert into rr_events values(null," + eid + "," + accountid + "," + vid + ",'" + title
						+ "','" + description + "'," + sow + "," + milestone + "," + "'" + _dateOf + "','" + _due
						+ "','" + today + "',null," + ownerid + "," + priority + "," + progress + "," + status + ")";
				eos.log(sql);

				ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

				ps.executeUpdate();

				rs = ps.getGeneratedKeys();

				if (rs.next()) {
					eventid = eos.e(rs.getInt(1));
				}

			} catch (Exception e) {
				eos.log("Errors adding event. Err:" + e.toString(), "Events", "add", 2);
			} finally {
				eos.cleanup(c, ps, rs);
			}

		}

		return eventid;
	}

}
