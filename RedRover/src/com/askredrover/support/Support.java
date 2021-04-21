package com.askredrover.support;

import com.eos.Eos;
import java.sql.*;
import java.util.*;
import com.eos.accounts.User;

public class Support {

	private Eos eos = null;

	public Support(Eos eos) {
		this.eos = eos;
	}	
	
	/**
	 * Toggle Status
	 * @param sid
	 * @param current
	 */
	public void toggleStatus(String sid, String current)
	{
		if(eos.active())
		{
			Connection c = eos.c();
			Statement  s = null;
			
			try { 
				
				s = c.createStatement();
				
				int iC = eos.d(current);
				if(iC == 1) { 
					iC = 0;
				} else { 
					iC = 1;
				}
				
				int iSid = eos.d(sid);
				String sql = "update rr_support set status=" + iC + " where sid=" + iSid + "";
				s.execute(sql);
				
			} catch(Exception e) { 
				eos.log("Errors toggling status. Err:" + e.toString(),"Support","toggleStatus",2);
			} finally { 
				eos.cleanup(c, s);
			}
		}
	}

	/**
	 * Gets all the support or feedback entities. 0-support, 1 feedback
	 * 
	 * @param typeof
	 * @param onlyShowUnread ... if true, only shows items NOT set to "read"
	 * @return
	 */
	public ArrayList<Entity> get(int typeof, boolean onlyShowUnread) {
		ArrayList<Entity> lst = new ArrayList<Entity>();

		if (eos.active()) {

			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {
					
				s = c.createStatement();
				int eid = eos.account().eid();
				
				String sql = "select sid,accountid, added,entered,userid,reason,title,message,status from rr_support where eid="+eid+" and typeof=" + typeof + "";
				
				if(onlyShowUnread) { 
					sql = sql + " and status=0";
				}
				
				sql = sql + " order by added desc";
				
				rs = s.executeQuery(sql);
				
				while(rs.next())
				{
					int sid = rs.getInt(1);
					int aid = rs.getInt(2);
					java.sql.Date a = rs.getDate(3);
					java.sql.Timestamp e = rs.getTimestamp(4);
					int uid = rs.getInt(5);
					int r = rs.getInt(6);
					String t = rs.getString(7);
					String m = rs.getString(8);
					int sta = rs.getInt(9);
					
					/**
					 * 
	public Entity(int sid, int eid, int aid, java.sql.Date added, java.sql.Timestamp entered, int userid, int typeof,
			int reason, String title, String message, int status) {
			
					 */
					Entity en = new Entity(sid,eid,aid,a,e,uid,typeof,r,t,m,sta); lst.add(en);
				}
				
				
				
			} catch (Exception e) {
				eos.log("Errors getting support or feedback. Err:" + e.toString(), "Support", "get", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}

		}
		return lst;
	}

	/**
	 * Gets dropdown for the type ... and highlights current if available...
	 * 
	 * @param typeof
	 * @param current
	 * @return
	 */
	public static String getTypeAsSelection(String typeof, String current) {
		int iCurrent = com.eos.utils.Strings.getIntFromString(current);
		StringBuffer lst = new StringBuffer();
		int iType = com.eos.utils.Strings.getIntFromString(typeof);
		ArrayList<SupportType> types = com.askredrover.Constants.supportTypes(iType);

		int size = types.size();
		for (int i = 0; i < size; i++) {
			SupportType st = (SupportType) types.get(i);
			if (st.id() == iCurrent) {
				lst.append("<option selected value=" + st.id() + ">" + st.value() + "</option>");
			} else {
				lst.append("<option value=" + st.id() + ">" + st.value() + "</option>");
			}
		}

		return lst.toString();
	}

	/**
	 * Notifies the RedRover ADMINS for this account that feedback or support has
	 * been issues...
	 * 
	 * @param t
	 * @param m
	 * @param typeof
	 * @param reason
	 */
	private void notifyAdmins(String t, String m, String typeof, String reason, User user) {

		if (eos.active()) {

			int iType = com.eos.utils.Strings.getIntFromString(typeof);
			int iR = com.eos.utils.Strings.getIntFromString(reason);
			String r = com.askredrover.Constants.getSupportTypeAsString(iR);

			StringBuffer sb = new StringBuffer();
			String subj = null;

			if (iType == 0) {
				sb.append("Customer sent us the following support request...<BR>");
				subj = "Support request:" + t + "";
			} else {
				sb.append("Customer sent us the following feedback...<BR>");
				subj = "Feedback:" + t + "";
			}
			sb.append("<b>Name:</b> " + user.getFirstName() + "  " + user.getLastName() + "<BR>");
			sb.append("<b>Email:</b> <a href=" + user.getEmail() + ">" + user.getEmail() + "</a><BR>");
			sb.append("<b>Phone:</b> " + user.phoneFormatted() + "<br>");
			sb.append("<b>Title:</b> " + t + "<BR>");
			sb.append("<b>Reason:</b> " + r + "<br>");

			sb.append("<B>Message:</b> " + m + "");

			String aid = eos.e(eos.user().getAccountId());
			ArrayList<User> admins = eos.getUsers().getAccountAdmins(aid);
			int size = admins.size();
			eos.log("Number admins:" + size + "");
			for (int i = 0; i < size; i++) {
				User admin = (User) admins.get(i);
				if (admin != null) {
					eos.emails().send("notifyadminofrequest.html", admin.getEmail(), subj, admin.getFirstName(), "", "",
							"", sb.toString());
					eos.log("Sent " + admin.getEmail() + " email...");
				}
			}
		}

	}

	/**
	 * Thanks the customer for what they posted.
	 * 
	 * @param t
	 * @param m
	 * @param typeof
	 * @param reason
	 */
	private void notifyCustomer(String t, String m, String typeof, String reason) {

		if (eos.active()) {

			int iType = com.eos.utils.Strings.getIntFromString(typeof);
			int iR = com.eos.utils.Strings.getIntFromString(reason);
			String r = com.askredrover.Constants.getSupportTypeAsString(iR);

			String email = eos.user().getEmail();
			String f = eos.user().getFirstName();

			StringBuffer sb = new StringBuffer();
			String subj = null;

			if (iType == 0) {
				sb.append("You sent us the following support request...<BR>");
				subj = "Support request:" + t + "";
			} else {
				sb.append("You sent us the following feedback...<BR>");
				subj = "Feedback:" + t + "";
			}
			sb.append("<b>Title:</b> " + t + "<BR>");
			sb.append("<b>Reason:</b> " + r + "<br>");
			sb.append("<B>Message:</b> " + m + "");

			/**
			 * public boolean send(String templateId, String to, String subject, String
			 * firstname, String lastname, String password, String url, String extras) {
			 * return send(templateId, to, subject, firstname, lastname, password, url,
			 * extras, "", "", ""); }
			 */
			eos.emails().send("thanksforrequest.html", email, subj, f, "", "", "", sb.toString());
			// eos.log("Sent email to " + email + "");
		}
	}

	/**
	 * Posts new feedback or support
	 * 
	 * @param title
	 * @param message
	 * @param typeof
	 * @param reason
	 */
	public void post(String title, String message, String typeof, String reason) {
		User user = null;
		if (eos.active()) {
			int iType = com.eos.utils.Strings.getIntFromString(typeof);
			int iReason = com.eos.utils.Strings.getIntFromString(reason);
			String added = com.eos.utils.Calendar.getTodayForSQL();
			int uid = eos.user().getUserId();
			int aid = eos.user().getAccountId();
			int eid = eos.account().eid();
			title = com.eos.Eos.clean(title);
			message = com.eos.Eos.clean(message);

			user = eos.user();

			Connection c = eos.c();
			Statement s = null;

			try {

				s = c.createStatement();
				String sql = "insert into rr_support values(null," + eid + "," + aid + ",'" + added + "',null," + uid
						+ "," + iType + "," + iReason + ",'" + title + "','" + message + "',0)";
				s.execute(sql);

			} catch (Exception e) {
				eos.log("Errors posting RedRover feedback. Err:" + e.toString(), "Support", "post", 2);
			} finally {
				eos.cleanup(c, s);
				notifyAdmins(title, message, typeof, reason, user);
				notifyCustomer(title, message, typeof, reason);
			}
		}
	}

}
