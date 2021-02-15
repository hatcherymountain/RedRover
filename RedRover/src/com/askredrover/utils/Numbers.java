package com.askredrover.utils;

import java.sql.*;
import java.util.*;
import com.eos.Eos;

public class Numbers {

	private Eos eos = null;
	private ArrayList<Number> nums = null;

	public Numbers(Eos eos) {
		this.eos = eos;
		loadNumbers();
	}

	/**
	 * Adds a test flow...
	 * 
	 * @param aid
	 * @param uid
	 * @param id
	 */
	private void addTestFlow(int aid, int uid, int id) {
		if (eos.active()) {
			Connection c = eos.c();
			Statement s = null;

			try {

				Number number = this.number(id);
				if (number != null) {

					String phone = number.number();
					String output = "Welcome to " + eos.name() + "";
					String sql = "insert into rr_flows values(null," + aid + ",0,0,'" + phone + "','test','" + output
							+ "',0,0)";
					s = c.createStatement();
					s.execute(sql);

				}

			} catch (Exception e) {
				eos.log("Errros adding test flow. err:" + e.toString(), "Numbers", "addTestFlow", 2);
			} finally {
				eos.cleanup(c, s);
			}
		}
	}

	/**
	 * Enables the customer to set an nid
	 * 
	 * @param nid
	 */
	public void customerAssignNumber(String nid) {

		if (eos.isCustomerAdmin() || eos.isAdmin() || eos.isContributor()) {

			Connection c = eos.c();
			Statement s = null;
			try {
				s = c.createStatement();
				// TODO identity limit and restrictions
				int id = eos.d(nid);
				int aid = eos.user().getAccountId();
				int uid = eos.user().getUserId();
				String sql = "insert into rr_assigned_numbers values(" + aid + ",0," + id + "," + uid + ")";
				// eos.log(sql);
				s.execute(sql);
				eos.audit().audit("Number " + id + " assigned to account " + aid + "", "customerAssignNumber",
						"Numbers", "customerAssignNumber");

				addTestFlow(aid, uid, id);

			} catch (Exception e) {
				eos.log("Errors assigning an account a number. Err:" + e.toString(), "Numbers", "assignNumber", 2);
			} finally {
				eos.cleanup(c, s);

			}

		}

	}

	public ArrayList<Number> getAccountNumbers() {
		return getAccountNumbers(eos.user().getAccountId());
	}

	/**
	 * Get all numbers assigned to an account.
	 * 
	 * @param accountid
	 * @return
	 */
	public ArrayList<Number> getAccountNumbers(int accountid) {
		ArrayList<Number> lst = new ArrayList<Number>();

		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;
		try {
			s = c.createStatement();

			String sql = "select nid from rr_assigned_numbers where accountid=" + accountid + "";

			rs = s.executeQuery(sql);
			while (rs.next()) {
				Number n = number(rs.getInt(1));
				lst.add(n);

			}

		} catch (Exception e) {
			eos.log("Errors getting numbers for an account. Err:" + e.toString(), "Numbers", "getAccountNumbers", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return lst;
	}

	public ArrayList<Number> getAccountNumbers(String accountid) {
		return getAccountNumbers(eos.d(accountid));
	}

	/**
	 * Get all numbers NOT assigned within the system.
	 * 
	 * @return ArrayList<Number>
	 */
	public ArrayList<Number> getAvailableNumbers() {

		ArrayList<Number> lst = new ArrayList<Number>();
		if (eos.isCustomerAdmin() || eos.isAdmin() || eos.isContributor()) {

			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {
				s = c.createStatement();

				String sql = "";
				int count = numberNumbersAssigned();

				if (count > 0) {
					sql = "select a.nid, a.number, b.nid from rr_numbers a, rr_assigned_numbers b WHERE a.nid!=b.nid order by a.number asc";
				} else {
					sql = "select nid,number from rr_numbers order by number asc";
				}

				eos.log(sql);
				rs = s.executeQuery(sql);
				while (rs.next()) {

					int n = rs.getInt(1);
					String num = rs.getString(2);

					lst.add(new Number(n, num));

				}

			} catch (Exception e) {
				eos.log("Errors assigning an account a number. Err:" + e.toString(), "Numbers", "assignNumber", 2);
			} finally {
				eos.cleanup(c, s);
			}

		}
		return lst;
	}

	/**
	 * GET HTML Selection List of Available Numbers.
	 * 
	 * @return String
	 */
	public String getAvailableNumbersAsSelection() {
		StringBuffer sb = new StringBuffer();

		ArrayList<Number> lst = getAvailableNumbers();
		int size = lst.size();
		for (int i = 0; i < size; i++) {
			Number num = (Number) lst.get(i);
			sb.append("<option value=\"" + eos.e(num.nid()) + "\">" + num.number() + "</option>");
		}
		return sb.toString();
	}

	public boolean hasNumber() {
		return hasNumber(eos.user().getAccountId());
	}

	/**
	 * Does this account have a number?
	 * 
	 * @param accountid
	 * @return
	 */
	private boolean hasNumber(int accountid) {
		int size = getAccountNumbers(accountid).size();
		if (size > 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean hasNumber(String accountid) {
		return hasNumber(eos.d(accountid));
	}

	/**
	 * Loads all numbers in the system.
	 */
	private void loadNumbers() {
		nums = new ArrayList<Number>();

		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;

		try {

			s = c.createStatement();
			rs = s.executeQuery("select nid,number from rr_numbers order by number asc");

			while (rs.next()) {
				nums.add(new Number(rs.getInt(1), rs.getString(2)));
			}

		} catch (Exception e) {
			eos.log("Errors loading numbers. Err;" + e.toString(), "Numbers", "loadNumbers", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}
	}

	/**
	 * @param numberid
	 * @return Number
	 */
	private Number number(int numberid) {
		Number num = null;

		int size = nums.size();// from global list.
		for (int x = 0; x < size; x++) {
			Number n = (Number) nums.get(x);
			if (n.nid() == numberid) {
				num = n;
				break;
			}
		}

		return num;
	}

	public Number number(String numberid) {
		return number(eos.d(numberid));
	}

	/**
	 * Get a number
	 * 
	 * @param numberid
	 * @return
	 */
	private String numberAsString(int numberid) {
		String number = "";
		int size = nums.size();// from global list.
		for (int x = 0; x < size; x++) {
			Number n = (Number) nums.get(x);
			if (n.nid() == numberid) {
				number = n.number();
				break;
			}
		}
		return number;
	}

	/**
	 * Get a number from id
	 * 
	 * @param numberid
	 * @return
	 */
	public String numberAsString(String numberid) {
		return numberAsString(eos.d(numberid));
	}

	/**
	 * How many numbers have been assigned?
	 * 
	 * @return
	 */
	private int numberNumbersAssigned() {
		int count = 0;

		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;

		try {
			s = c.createStatement();
			String sql = "select count(*) from rr_assigned_numbers";
			rs = s.executeQuery(sql);
			while (rs.next()) {

				count = rs.getInt(1);
			}

		} catch (Exception e) {
			eos.log("Errors assigning an account a number. Err:" + e.toString(), "Numbers", "assignNumber", 2);
		} finally {
			eos.cleanup(c, s);
		}

		return count;
	}

}