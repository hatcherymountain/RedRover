package com.askredrover.utils;

import com.eos.Eos;
import java.sql.*;
import com.askredrover.RedRover;
import com.eos.accounts.Profile;

public class Users {

	private Eos eos = null;

	public Users(Eos eos) {
		this.eos = eos;
	}

	/**
	 * Get User profile
	 * 
	 * @param userid
	 * @return
	 */
	public Profile profile(String userid) {
		return eos.users().profile(userid);
	}

	/**
	 * Updates user profile.
	 * 
	 * @param r
	 */
	public void updateUser(javax.servlet.http.HttpServletRequest r) {
		if (eos.active()) {

			String u = r.getParameter("userid");
			String f = r.getParameter("f");
			String l = r.getParameter("l");
			String ro = r.getParameter("r");
			String p1 = r.getParameter("p1");
			String p2 = r.getParameter("p2");
			String pos = r.getParameter("pos");
			String city = r.getParameter("city");
			String cou = r.getParameter("country");
			String status = r.getParameter("status");
			String un = r.getParameter("un");
			String sk = r.getParameter("skills");

			eos.getUsers().updateProfileBasics(u, f, l, ro, p1, "", p2, un, status);

			/**
			 * Now try update the user data
			 */

			pos = com.eos.Eos.clean(pos);
			eos.users().properties().update(u, com.askredrover.Constants.PROFILE_POSITION, pos);

			pos = com.eos.Eos.clean(sk);
			eos.users().properties().update(u, com.askredrover.Constants.PROFILE_SKILLS, sk);

			city = com.eos.Eos.clean(city);
			eos.users().properties().update(u, com.askredrover.Constants.PROFILE_CITY, city);

			cou = com.eos.Eos.clean(cou);
			eos.users().properties().update(u, com.askredrover.Constants.PROFILE_COUNTRY, cou);

		}
	}

	/**
	 * Saves the communication preferences
	 * 
	 * @param r
	 */
	public void updateCommunicationPreferences(javax.servlet.http.HttpServletRequest r) {
		if (eos.active()) {

			String u = r.getParameter("userid");
			String p = r.getParameter("prefer");
			String s = r.getParameter("sms");
			String w = r.getParameter("weekends");
			String pref = r.getParameter("preferstowork");

			String sv = "true";
			int iSMSV = com.eos.utils.Forms.getCheckboxState(s);
			eos.log("IMSV:" + iSMSV);
			if (iSMSV == 0) {
				sv = "false";
			}

			eos.log(sv);
			eos.users().updateSMS(u, sv);
			eos.users().updatePreferredCommunications(u, p);
			eos.users().updatePrefersToWork(u, pref);

			int iWEEK = com.eos.utils.Forms.getCheckboxState(w);
			eos.users().updateWorksWeekends(u, "" + iWEEK + "");

		}
	}

	/**
	 * Removes a user using the EOS system and then removes RedRover-specific needs.
	 * 
	 * @param userid
	 */
	public void removeUser(String userid) {
		if (eos.isAdmin() || eos.isContributor()) {
			eos.getUsers().removeUser(userid);

			Connection c = eos.c();
			Statement s = null;
			try {

				int uid = eos.d(userid);
				s = c.createStatement();
				s.execute("delete from rr_venture_members where userid=" + uid + "");

			} catch (Exception e) {
				eos.log("Errors removing user redrover attributes. Err:" + e.toString(), "utils", "removeUser", 2);
			} finally {
				eos.cleanup(c, s);
			}
		}
	}

	/**
	 * Set the city
	 * 
	 * @param userid
	 * @param city
	 */
	public void setCity(String userid, String city) {
		if (eos.active()) {
			city = com.eos.Eos.clean(city);
			eos.users().properties().update(userid, com.askredrover.Constants.PROFILE_CITY, city);
		}
	}

	public String getCity(String userid) {
		String c = "";
		if (eos.active()) {
			c = eos.users().properties().property(userid, com.askredrover.Constants.PROFILE_CITY);
		}

		return c;
	}

	/**
	 * Set the city
	 * 
	 * @param userid
	 * @param city
	 */
	public void setCountry(String userid, String country) {
		if (eos.active()) {
			country = com.eos.Eos.clean(country);
			eos.users().properties().update(userid, com.askredrover.Constants.PROFILE_COUNTRY, country);
		}
	}

	public String getCountry(String userid) {
		String c = "";
		if (eos.active()) {
			c = eos.users().properties().property(userid, com.askredrover.Constants.PROFILE_COUNTRY);
		}

		return c;
	}

	/**
	 * Set Position
	 * 
	 * @param userid
	 * @param city
	 */
	public void setPosition(String userid, String pos) {
		if (eos.active()) {
			pos = com.eos.Eos.clean(pos);
			eos.users().properties().update(userid, com.askredrover.Constants.PROFILE_POSITION, pos);
		}
	}

	/**
	 * Get Users's Current Position
	 * 
	 * @param userid
	 * @return
	 */
	public String getPosition(String userid) {
		String c = "";
		if (eos.active()) {
			c = eos.users().properties().property(userid, com.askredrover.Constants.PROFILE_POSITION);
		}

		if (c.length() == 0) {
			c = "Position undefined";
		}
		return c;
	}

	/**
	 * Set Gender
	 * 
	 * @param userid
	 * @param city
	 */
	public void setGender(String userid, String gender) {
		if (eos.active()) {
			gender = com.eos.Eos.clean(gender);
			eos.users().properties().update(userid, com.askredrover.Constants.PROFILE_GENDER, gender);
		}
	}

	/**
	 * Get Users's Current Position
	 * 
	 * @param userid
	 * @return
	 */
	public String getGender(String userid) {
		String c = "";
		if (eos.active()) {
			c = eos.users().properties().property(userid, com.askredrover.Constants.PROFILE_GENDER);
		}

		return c;
	}

	public int getGenderAsInt(String userid) {
		return com.eos.utils.Strings.getIntFromString(getGender(userid));
	}

	/**
	 * Set Position
	 * 
	 * @param userid
	 * @param city
	 */
	public void setSkills(String userid, String skills) {
		if (eos.active()) {
			skills = com.eos.Eos.clean(skills);
			eos.users().properties().update(userid, com.askredrover.Constants.PROFILE_SKILLS, skills);
		}
	}

	/**
	 * Get Users's Current Position
	 * 
	 * @param userid
	 * @return
	 */
	public String getSkills(String userid) {
		String c = "";
		if (eos.active()) {
			c = eos.users().properties().property(userid, com.askredrover.Constants.PROFILE_SKILLS);
		}

		return c;
	}

	/**
	 * Tries to provide a useful location using combo of city and country
	 * 
	 * @return
	 */
	public String getLocation(String userid) {

		String location = "Undefined";
		if (eos.active()) {
			String city = this.getCity(userid);
			String country = this.getCountry(userid);

			if (city.length() > 0 && country.length() == 0) {
				location = city;
			} else if (city.length() == 0 && country.length() > 0) {
				location = country;
			} else if (city.length() > 0 && country.length() > 0) {
				location = city + "," + country + "";
			}

		}
		return location;
	}

}
