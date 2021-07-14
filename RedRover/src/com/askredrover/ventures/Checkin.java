package com.askredrover.ventures;


import java.text.SimpleDateFormat;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import java.util.Properties;

import com.eos.Eos;
import com.eos.database.Database;

public class Checkin implements Runnable {

	private ServletContextEvent context = null;

	private Database db = null;

	public Checkin(ServletContextEvent context) {
		this.context = context;
	}

	/**
	 * Is the text been sent @ a respectable time?
	 * 
	 * @param hour
	 * @return
	 */
	private boolean acceptableTime(String hour) {
		boolean is = false;

		// System.out.println("CURRENT HOUR: " + hour);
		if (hour.equals("8") || hour.equals("08")) {
			is = true;
		} else if (hour.equals("9") || hour.equals("09")) {
			is = true;
		} else if (hour.equals("10")) {
			is = true;
		} else if (hour.equals("11")) {
			is = true;
		} else if (hour.equals("12")) {
			is = true;
		} else if (hour.equals("13")) {
			is = true;
		} else if (hour.equals("14")) {
			is = true;
		} else if (hour.equals("15")) {
			is = true;
		} else if (hour.equals("16")) {
			is = true;
		} else if (hour.equals("17")) {
			is = true;
		} else if (hour.equals("18")) {
			is = true;
		}

		// System.out.println("Is this acceptable? " + is);
		return is;
	}

	@Override
	public void run() {

		java.util.Calendar cal = java.util.Calendar.getInstance();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH");
		String t = sdf.format(cal.getTime());

		db = new Database();

		if (acceptableTime(t)) {
			
		}
		
		

	}

   
	
	/**
    	private void communicate(ArrayList<EventPrecis> lst) {

		int size = lst.size();

		for (int i = 0; i < size; i++) {

			EventPrecis ep = (EventPrecis) lst.get(i);
			String petname = getPetName(ep.animalid());
			ArrayList<UserPrecis> users = getUsers(ep.familyid());

			int userid = ep.userid();
			String url = (String) context.getServletContext().getInitParameter("app.url");

			url = url + "/confirm?k=" + ep.key();
			String time = com.eos.Constants.getTime(ep.starthour(), ep.startmin());
			String msg = "Confirm your grooming appointment tomorrow for " + petname + " on "
					+ com.eos.Eos.date(ep.when()) + " at " + time + ". " + url + "";

			com.eos.sms.SMSManager sms = new com.eos.sms.SMSManager();

			if (userid == 0) {

				int nsize = users.size();

				for (int x = 0; x < nsize; x++) {

					UserPrecis u = (UserPrecis) users.get(x);

					if (u.number().length() > 0) {

						boolean canSms = canSMS(u.userid());
						if (canSms && u.number().length() > 0) {
							sms.send(msg, u.number(), u.userid());
						}
					}
					break;

				}

			} else {

				String number = getNumber(userid);
				boolean canSms = canSMS(userid);
				if (canSms && number.length() > 0) {
					sms.send(msg, number, userid);
				}
			}

		}

	}
	
	**/

	/**
	 * Can we SMS the user?
	 * 
	 * @param userid
	 * @return
	 */
	private boolean canSMS(int userid) {

		boolean can = false;

		Connection c = db.getConnection();
		Statement s = null;
		ResultSet rs = null;

		try {

			s = c.createStatement();
			String sql = "select sms from eos_profile where userid=" + userid + "";

			rs = s.executeQuery(sql);

			while (rs.next()) {
				if (rs.getInt(1) == 1) {
					can = true;
				}
			}

		} catch (Exception e) {
			System.err.println("Errors determining whether a user allows SMS? . Err:" + e.toString());
		} finally {
			db.clean(c, s, rs);
		}

		return can;
	}

	/**
	 * Get user's number
	 * 
	 * @param userid
	 * @return
	 */
	private String getNumber(int userid) {
		String num = "";

		Connection c = db.getConnection();
		Statement s = null;
		ResultSet rs = null;

		try {

			s = c.createStatement();
			String sql = "select telephone from eos_users where userid=" + userid + "";

			rs = s.executeQuery(sql);

			while (rs.next()) {
				num = rs.getString(1);
			}

		} catch (Exception e) {
			System.err.println("Errors finding telephone numbers. Err:" + e.toString());
		} finally {
			db.clean(c, s, rs);
		}

		return num;
	}

}
