package com.askredrover.utils;

import com.eos.Eos;
import java.sql.*;
import com.askredrover.RedRover;

public class Users {

	private Eos eos = null;
	private RedRover rr = null;

	public Users(Eos eos, RedRover rr) {
		this.eos = eos;
		this.rr = rr;
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
		
		if(c.length()==0) { 
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
