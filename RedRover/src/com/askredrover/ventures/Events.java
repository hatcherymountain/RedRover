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

				String dueStr = null;
				if (due == null) {
					dueStr = com.eos.utils.Calendar.NO_EXPIRE_DATE;
				} else {
					dueStr = com.eos.utils.Calendar.clean(due);
				}

				String dateofStr = null;
				if (dateof == null) {
					dateofStr = com.eos.utils.Calendar.NO_EXPIRE_DATE;
				} else {
					dateofStr = com.eos.utils.Calendar.clean(dateof);
				}

				String sql = "insert into rr_events values(null,?,?,?,?,?,?,?,'" + dateofStr + "','" + dueStr + "','"
						+ today + "',null,?,?,?,?)";

				ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
				ps.setInt(1, eid);
				ps.setInt(2, accountid);
				ps.setInt(3, vid);
				ps.setString(4, title);
				ps.setString(5, description);
				ps.setInt(6, sow);
				ps.setInt(7, milestone);
				ps.setInt(8, ownerid);
				ps.setInt(9, priority);
				ps.setInt(10, progress);
				ps.setInt(11, status);

				ps.executeUpdate();

				rs = ps.getGeneratedKeys();

				if (rs.next()) {
					eventid = eos.e(rs.getInt(1));
				}

			} catch (Exception e) {
				eos.log("Errors adding new base animal details. Err:" + e.toString(), "Animals", "add", 2);
			} finally {
				eos.cleanup(c, ps, rs);
			}

		}

		return eventid;
	}

}
