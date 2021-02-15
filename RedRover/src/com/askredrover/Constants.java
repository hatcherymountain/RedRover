package com.askredrover;

import java.util.ArrayList;
import com.eos.accounts.User;

public class Constants {

	public static final String TESTFLOW_KEYPHRASE = "test";

	public static final String[] VENTURE_ROLES = { "User", "Editor", "Administrator" };

	public static final int ROLE_USER = 0;
	public static final int ROLE_EDITOR = 1;
	public static final int ROLE_OWNER = 2;

	public static final String[] VENTURE_STATES = { "Developing", "Active", "Paused", "Archived" };
	public static final String[] VENTURE_STATE_COLORS = { "warning", "success", "info", "danger" };

	public static final String[] EVENT_STATES = { "Inactive", "Active", "Completed" };
	public static final String[] EVENT_PRIORITIES = { "Low", "Medium", "High", "Urgent" };

	public static String ventureStatusColorAsString(int status) {
		String s = null;
		try {
			s = (String) VENTURE_STATE_COLORS[status];
		} catch (Exception e) {
			s = (String) VENTURE_STATE_COLORS[0];
		}
		return s;
	}

	/**
	 * Event states as an HTML Selection list.
	 * 
	 * @param current
	 * @return
	 */
	public static String eventStatesAsSelection(int current) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < EVENT_STATES.length; i++) {
			String p = (String) EVENT_STATES[i];
			if (i == current) {
				sb.append("<option select value=" + i + ">" + p + "</option>");
			} else {
				sb.append("<option value=" + i + ">" + p + "</option>");
			}
		}

		return sb.toString();
	}

	/**
	 * HTML Event Priorities as HTML select list.
	 * 
	 * @param current
	 * @return
	 */
	public static String eventPrioritiesAsSelection(int current) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < EVENT_PRIORITIES.length; i++) {
			String p = (String) EVENT_PRIORITIES[i];
			if (i == current) {
				sb.append("<option select value=" + i + ">" + p + "</option>");
			} else {
				sb.append("<option value=" + i + ">" + p + "</option>");
			}
		}

		return sb.toString();
	}

	/**
	 * What is the priority of an event
	 * 
	 * @param status
	 * @return
	 */
	public static String eventPriorityAsString(int status) {
		String s = null;
		try {
			s = (String) EVENT_PRIORITIES[status];
		} catch (Exception e) {
			s = (String) EVENT_PRIORITIES[0];
		}
		return s;
	}

	public static String eventStatusAsString(int status) {
		String s = null;
		try {
			s = (String) EVENT_STATES[status];
		} catch (Exception e) {
			s = (String) EVENT_STATES[0];
		}
		return s;
	}

	/**
	 * Get the venture status as a String
	 * 
	 * @param status
	 * @return
	 */
	public static String ventureStatusAsString(int status) {
		String s = null;
		try {
			s = (String) VENTURE_STATES[status];
		} catch (Exception e) {
			s = (String) VENTURE_STATES[0];
		}
		return s;
	}

	public static String roleAsString(int current) {
		String r = null;
		try {
			r = (String) VENTURE_ROLES[current];
		} catch (Exception e) {
			r = (String) VENTURE_ROLES[0];
		}

		return r;
	}

	public static final ArrayList<String> letters() {

		ArrayList<String> l = new ArrayList<String>();
		l.add("A");
		l.add("B");
		l.add("C");
		l.add("D");
		l.add("E");
		l.add("F");
		l.add("G");
		l.add("H");
		l.add("I");
		l.add("J");
		l.add("K");
		l.add("L");
		l.add("M");
		l.add("N");
		l.add("O");
		l.add("P");
		l.add("Q");
		l.add("R");
		l.add("S");
		l.add("T");
		l.add("U");
		l.add("V");
		l.add("W");
		l.add("X");
		l.add("Y");
		l.add("Z");
		return l;
	}

	/**
	 * Given a firstname initial, can we return users that fit that letter?
	 * 
	 * @param letter
	 * @param lst
	 * @return
	 */
	public static ArrayList<com.eos.accounts.User> getUserFromInitialLetter(String letter,
			ArrayList<com.eos.accounts.User> lst) {
		ArrayList<User> newlst = new ArrayList<User>();
		int size = lst.size();

		for (int i = 0; i < size; i++) {
			User user = (User) lst.get(i);
			if (user.initChar().equalsIgnoreCase(letter)) {
				newlst.add(user);
			}
		}

		return newlst;
	}

}
