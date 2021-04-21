package com.askredrover.pinpoint;

import com.eos.accounts.User;
import java.sql.*;
import com.eos.Eos;
import java.util.ArrayList;

public class Messages {

	private Eos eos = null;
	private StringBuffer logTeamSMS = null;

	public Messages(Eos eos) {
		this.eos = eos;
	}

	/**
	 * Get Log from sending the team SMS.
	 * 
	 * @return
	 */
	public StringBuffer getTeamSMSLog() {
		if (logTeamSMS == null) {
			logTeamSMS = new StringBuffer();
			return logTeamSMS;
		} else {
			return logTeamSMS;
		}
	}

	/**
	 * One of the most basic functions ... texting the company team.
	 */
	public void smsTeam(javax.servlet.http.HttpServletRequest r) {
		if (logTeamSMS != null) { // clear it!
			logTeamSMS.setLength(0);
		}

		ArrayList<User> members = eos.getUsers().ourTeam();
		int size = members.size();
		String msg = r.getParameter("sms");
		msg = com.eos.utils.Strings.absoluteTruncation(msg, 160);

		if (msg.length() > 0) {
			logTeamSMS = new StringBuffer();
			logTeamSMS.append("<p class=lead text-dark>" + msg + "</p>");
			logTeamSMS.append("<b>Recipients:</b><ul>");
			for (int i = 0; i < size; i++) {
				User u = (User) members.get(i);
				if (u.phone().length() > 1) {
					eos.sms().send(msg, u.phone(), u.getUserId());
					logTeamSMS.append(
							"" + u.getFirstName() + " " + u.getLastName() + " &mdash; " + u.phoneFormatted() + "");
					// TODO TRACE
				}
			}
			logTeamSMS.append("</ul>");
		}
	}

	/**
	 * Given a keyphrase and a specific number, get a message back.
	 * 
	 * @param phoneName
	 * @param keyPhrase
	 * @return
	 */
	public ArrayList<MessageOption> getMessageForkeyPhraseAndPhoneNumber(String phoneName, String keyPhrase) {
		ArrayList<MessageOption> lst = new ArrayList<MessageOption>();

		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {

			String sql = "Select fid,accountid,pid,cid,phone,input,output,indx,parent_fid from rr_flows where phone =? and input=?";
			ps = c.prepareStatement(sql);
			ps.setString(1, phoneName);
			ps.setString(2, keyPhrase);
			rs = ps.executeQuery();
			while (rs.next()) {
				int fid = rs.getInt(1);
				int accountid = rs.getInt(2);
				int pid = rs.getInt(3);
				int cid = rs.getInt(4);
				String phone = rs.getString(5);
				String input = rs.getString(6);
				String output = rs.getString(7);
				int indx = rs.getInt(8);
				int parentFid = rs.getInt(9);
				lst.add(new MessageOption(fid, accountid, pid, cid, phone, input, output, indx, parentFid));
			}

		} catch (Exception e) {
			eos.log("Errors getting messaging for keyPhrase. Err:" + e.toString(), "Messaging",
					"getMessageForkeyPhraseAndPhoneNumber", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return lst;
	}

	/**
	 * Get messages aligned with a phone number.
	 * 
	 * @param phoneName
	 * @return ArrayList of Message objects
	 */
	public ArrayList<Message> getMessagesForPhoneNumber(String phoneName) {
		ArrayList<Message> lst = new ArrayList<Message>();

		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {

			String sql = "select c.original_number, c.destination_number, c.kephrase, c.message, c.msgid, c.parent_msgid, c.status,  c.added from hatchery_venture_messagestore p right JOIN hatchery_venture_messagestore c on p.parent_msgid = c.msgid where (c.original_number =? or c.destination_number =?) order by added";
			ps = c.prepareStatement(sql);
			ps.setString(1, phoneName);
			ps.setString(2, phoneName);
			rs = ps.executeQuery();
			while (rs.next()) {

				String originationNumber = rs.getString(1);
				String destinationNumber = rs.getString(2);
				String messageKeyword = rs.getString(3);
				String messageBody = rs.getString(4);
				String inboundMessageId = rs.getString(5);
				String previousPublishedMessageId = rs.getString(6);
				String status = rs.getString(7);
				java.sql.Timestamp added = rs.getTimestamp(8);

				lst.add(new MessageObject(originationNumber, destinationNumber, messageKeyword, messageBody,
						inboundMessageId, previousPublishedMessageId, status, added));
			}

		} catch (Exception e) {
			eos.log("Errors getting messaging for phone number. Err:" + e.toString(), "Messaging",
					"getMessageForPhoneNumber", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return lst;
	}

	/**
	 * Saves a message.
	 * 
	 * @param originationNumber
	 * @param destinationNumber
	 * @param messageKeyword
	 * @param messageBody
	 * @param inboundMessageId
	 * @param previousPublishedMessageId
	 * @param deliveryStatus
	 * @return
	 */
	public int saveMessage(String originationNumber, String destinationNumber, String messageKeyword,
			String messageBody, String inboundMessageId, String previousPublishedMessageId, String deliveryStatus) {

		Connection c = eos.c();
		PreparedStatement p = null;
		ResultSet r = null;
		int mid = 0;

		try {

			String sql = "insert into hatchery_venture_messagestore (mid,original_number, destination_number, kephrase, message, msgid,parent_msgid, status) values(null,?,?,?,?,?,?,?)";

			p = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

			p.setString(1, originationNumber);
			p.setString(2, destinationNumber);
			p.setString(3, messageKeyword);
			p.setString(4, com.eos.Eos.clean(messageBody));
			p.setString(5, inboundMessageId);
			p.setString(6, previousPublishedMessageId);
			p.setString(7, deliveryStatus);

			int rowid = p.executeUpdate();

			r = p.getGeneratedKeys();

			if (r.next()) {
				mid = r.getInt(1);
			}

		} catch (Exception e) {
			eos.log("Errors saving message to DB, Err:" + e.toString(), "Messaging", "saveMessage", 2);
		} finally {
			eos.cleanup(c, p, r);
		}
		return mid;
	}

}
