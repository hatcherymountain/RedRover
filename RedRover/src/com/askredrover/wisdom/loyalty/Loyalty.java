package com.askredrover.wisdom.loyalty;

import com.eos.Eos;
import java.sql.*;
import java.util.ArrayList;
import com.askredrover.RedRover;

public class Loyalty {

	private Eos eos = null;
	private RedRover rr = null;

	public Loyalty(Eos eos, RedRover rr) {
		this.eos = eos;
		this.rr = rr;
	}
	
	/**
	 * Has something already been bookmarked??
	 * @param id
	 * @param typeof
	 * @return
	 */
	private boolean bookmarked(int id, int typeof) {
		boolean has = false;
		
		
			
		return has;
	}
	
	/**
	 * What are MY bookmarks?
	 * @return
	 */
	public ArrayList<Bookmark> bookmarks() { 
		ArrayList<Bookmark> lst = new ArrayList<Bookmark>();
		if(eos.active())
		{
			
			
		}
		return lst;
	}

	/**
	 * Bookmarks an asset
	 * 
	 * @param identifier
	 * @param typeof
	 */
	public void bookmark(String identifier, String typeof) {
		int id = eos.d(identifier);
		int to = com.eos.utils.Strings.getIntFromString(typeof);
		if (!bookmarked(id, to)) {
			Connection c = eos.c();
			Statement s = null;

			try {

				s = c.createStatement();

				String today = com.eos.utils.Calendar.getTodayForSQL();
				int eid = eos.account().eid();
				int uid = eos.user().getUserId();

				String sql = "insert into wisdom_bookmarks values(null," + eid + "," + uid + "," + id + "," + to + ",'"
						+ today + "')";
				s.execute(sql);

			} catch (Exception e) {
				eos.log("Errors bookmarking. Err:" + e.toString(), "Loyalty", "bookmark", 2);
			} finally {
				eos.cleanup(c, s);
			}
		}
	}

	/**
	 * What kudos do we have for a given entity?
	 * 
	 * @param identifier
	 * @param typeof
	 * @return
	 */
	public ArrayList<Kudo> kudos(String identifier, String typeof) {
		ArrayList<Kudo> lst = new ArrayList<Kudo>();

		Connection c = eos.c();
		Statement s = null;

		return lst;
	}
}
