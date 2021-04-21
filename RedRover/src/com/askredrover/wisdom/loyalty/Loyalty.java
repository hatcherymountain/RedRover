package com.askredrover.wisdom.loyalty;

import com.eos.accounts.User;
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
	 * Has a given thing been bookmarked?
	 * 
	 * @param oid
	 * @param typeof
	 * @return
	 */
	public boolean isBookmarked(String oid, String typeof) {
		boolean has = false;

		if (eos.active()) {
			int id = eos.d(oid);
			int to = com.eos.utils.Strings.getIntFromString(typeof);
			has = bookmarked(id, to);
		}

		return has;
	}

	
	/**
	 * How many views??
	 * @param to
	 * @param oid
	 */
	public int views(String to, String oid)
	{
		int views=0;
		if(eos.active())
		{
			Connection c = eos.c(); Statement  s= null; ResultSet rs=null;
			try { 
				
				s = c.createStatement();
				int iT = com.eos.utils.Strings.getIntFromString(to);
				int id = eos.d(oid);
				
				s = c.createStatement();
				int uid = eos.user().getUserId();
				int eid = eos.account().eid();
				
				String sql = "select count(*) from wisdom_views where eid=" + eid + " and userid=" + uid + " and typeof="+iT+" and id=" + id + "";
				rs = s.executeQuery(sql);
				while(rs.next())
				{
					views = rs.getInt(1);
				}
				
				
			} catch(Exception e)
			{
				eos.log("Errors tracking a view. Err:" + e.toString(),"Loyalty","setView",2);
			} finally { 
				eos.cleanup(c,s,rs);
			}
		}
		return views;
	}
	
	/**
	 * Has the active user seen??
	 * @param to
	 * @param oid
	 */
	public boolean seen(String to, String oid)
	{
		boolean has = false;
		if(eos.active())
		{
			Connection c = eos.c(); Statement  s= null; ResultSet rs=null;
			try { 
				
				s = c.createStatement();
				int iT = com.eos.utils.Strings.getIntFromString(to);
				int id = eos.d(oid);
				
				s = c.createStatement();
				int uid = eos.user().getUserId();
				int eid = eos.account().eid();
				
				String sql = "select count(*) from wisdom_views where eid=" + eid + " and userid=" + uid + " and typeof="+iT+" and id=" + id + "";
				rs = s.executeQuery(sql);
				while(rs.next())
				{
					if(rs.getInt(1)>0) { 
						has = true;
					}
				}
				
				
			} catch(Exception e)
			{
				eos.log("Errors tracking a view. Err:" + e.toString(),"Loyalty","setView",2);
			} finally { 
				eos.cleanup(c,s,rs);
			}
		}
		return has;
	}
	
	
	/**
	 * Establishes a View
	 * @param typeof = 0 file, 1 article, 2 tut
	 * @param oid
	 */
	public void setView(String to, String oid)
	{
		if(eos.active())
		{
			Connection c = eos.c(); Statement  s= null;
			try { 
				
				s = c.createStatement();
				int iT = com.eos.utils.Strings.getIntFromString(to);
				int id = eos.d(oid);
				
				s = c.createStatement();
				String t = com.eos.utils.Calendar.getTodayForSQL();
				int uid = eos.user().getUserId();
				int eid = eos.account().eid();
				String sql = "insert into wisdom_views values("+uid+","+ eid+","+iT+","+id+",'"+t+"')";
				s.execute(sql);
			} catch(Exception e)
			{
				eos.log("Errors tracking a view. Err:" + e.toString(),"Loyalty","setView",2);
			} finally { 
				eos.cleanup(c,s);
			}
		}
	}
	
	
	/**
	 * Has something already been bookmarked??
	 * 
	 * @param id
	 * @param typeof
	 * @return boolean
	 */
	private boolean bookmarked(int id, int typeof) {

		boolean has = false;

		if (eos.active()) {
			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				s = c.createStatement();

				int eid = eos.account().eid();
				int uid = eos.user().getUserId();
				String sql = "select count(*) from wisdom_bookmarks where eid=" + eid + " and userid=" + uid
						+ " and id=" + id + " and typeof=" + typeof + "";
				rs = s.executeQuery(sql);
				while (rs.next()) {
					if (rs.getInt(1) > 0) {
						has = true;
					}
				}
			} catch (Exception e) {
				eos.log("Errors determining if something is bookedmarked or not. Err:" + e.toString(), "Loyalty",
						"bookmarked", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}
		}
		return has;
	}

	/**
	 * Removes a bookmark.
	 *
	 * @param bid
	 */
	public void removeBookmark(String bid) {

		if (eos.active()) {
			Connection c = eos.c();
			Statement s = null;
			int id = eos.d(bid);

			try {

				s = c.createStatement();

				String sql = "delete from wisdom_bookmarks where bid=" + id + "";
				s.execute(sql);

			} catch (Exception e) {
				eos.log("Errors removing bookmark. Err:" + e.toString(), "Loyalty", "removeBookmark", 2);
			} finally {
				eos.cleanup(c, s);
			}
		}
	}

	/**
	 * File Bookmarks
	 * 
	 * @return
	 */
	public ArrayList<Bookmark> fileBookmarks() {
		ArrayList<Bookmark> lstnew = new ArrayList<Bookmark>();

		if (eos.active()) {

			ArrayList<Bookmark> lst = bookmarks();
			int size = lst.size();
			for (int i = 0; i < size; i++) {
				Bookmark b = (Bookmark) lst.get(i);
				if (b.typeof() == 0) {
					lstnew.add(b);
				}
			}
		}
		return lstnew;
	}

	/**
	 * Article Bookmarks
	 * 
	 * @return
	 */
	public ArrayList<Bookmark> articleBookmarks() {
		ArrayList<Bookmark> lstnew = new ArrayList<Bookmark>();

		if (eos.active()) {

			ArrayList<Bookmark> lst = bookmarks();
			int size = lst.size();
			for (int i = 0; i < size; i++) {
				Bookmark b = (Bookmark) lst.get(i);
				if (b.typeof() == com.askredrover.Constants.BOOKMARK_ARTICLE) {
					lstnew.add(b);
				}
			}
		}
		return lstnew;
	}

	/**
	 * Tutorial Bookmarks
	 * 
	 * @return
	 */
	public ArrayList<Bookmark> tutorialBookmarks() {
		ArrayList<Bookmark> lstnew = new ArrayList<Bookmark>();

		if (eos.active()) {

			ArrayList<Bookmark> lst = bookmarks();
			int size = lst.size();
			for (int i = 0; i < size; i++) {
				Bookmark b = (Bookmark) lst.get(i);
				if (b.typeof() == com.askredrover.Constants.BOOKMARK_TUTORIAL) {
					lstnew.add(b);
				}
			}
		}
		return lstnew;
	}

	/**
	 * What are MY bookmarks?
	 * 
	 * @return
	 */
	public ArrayList<Bookmark> bookmarks() {
		ArrayList<Bookmark> lst = new ArrayList<Bookmark>();
		if (eos.active()) {

			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				s = c.createStatement();

				int eid = eos.account().eid();
				int uid = eos.user().getUserId();
				String sql = "select bid,id,typeof,added from wisdom_bookmarks where eid=" + eid + " and userid=" + uid
						+ "";

				rs = s.executeQuery(sql);
				while (rs.next()) {
					int bid = rs.getInt(1);
					int id = rs.getInt(2);
					int to = rs.getInt(3);
					java.sql.Date added = rs.getDate(4);
					Bookmark bm = new Bookmark(bid, eid, uid, id, to, added);
					lst.add(bm);
				}
			} catch (Exception e) {
				eos.log("Errors determining if something is bookedmarked or not. Err:" + e.toString(), "Loyalty",
						"bookmarked", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}

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
	 * Has the users already added this kudo??
	 * 
	 * @param eid
	 * @param id
	 * @param tid
	 * @return
	 */
	private boolean kudoExists(int eid, int userid, int id, int tid) {
		boolean exists = false;

		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;

		try {

			s = c.createStatement();
			String sql = "select count(*) from wisdom_kudos where eid=" + eid + " and userid=" + userid + " and id="
					+ id + " and typeof=" + tid + "";
			rs = s.executeQuery(sql);
			while (rs.next()) {
				if (rs.getInt(1) > 0) {
					exists = true;
				}
			}

		} catch (Exception e) {
			eos.log("Errors determining whether a kudo exists. Err:" + e.toString(), "Loyalty", "kudoExists", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return exists;
	}

	/**
	 * Add a kudo for the given entity
	 * 
	 * @param oid
	 * @param typeof
	 */
	public void kudo(String oid, String typeof) {

		if (eos.active()) {
			int uid = eos.user().getUserId();
			int eid = eos.account().eid();
			int id = eos.d(oid);
			int tid = com.eos.utils.Strings.getIntFromString(typeof);

			if (!kudoExists(eid, uid, id, tid)) {

				Connection c = eos.c();
				Statement s = null;

				try {

					s = c.createStatement();

					String sql = "insert into wisdom_kudos values(null," + eid + "," + uid + "," + id + "," + tid + ")";
					s.execute(sql);

				} catch (Exception e) {
					eos.log("Errors adding new kudo. Err:" + e.toString(), "Loyalty", "kudo", 2);
				} finally {
					eos.cleanup(c, s);
				}

			}

		}

	}

	/**
	 * What kudos do we have for a given entity?
	 * 
	 * @param identifier
	 * @param typeof
	 * @return ArrayList of Kudo.class
	 */
	public ArrayList<Kudo> kudos(String identifier, String typeof) {
		ArrayList<Kudo> lst = new ArrayList<Kudo>();

		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;

		try {

			int id = eos.d(identifier);
			int t = com.eos.utils.Strings.getIntFromString(typeof);
			int eid = eos.account().eid();

			s = c.createStatement();

			String sql = "select kid,userid from wisdom_kudos where eid=" + eid + " and id=" + id + " and typeof=" + t
					+ "";
			rs = s.executeQuery(sql);
			while (rs.next()) {
				int kid = rs.getInt(1);
				int uid = rs.getInt(2);
				String userid = eos.e(uid);
				User u = eos.getUsers().getUser(userid);

				Kudo k = new Kudo(kid, eid, u, id, t);
				lst.add(k);
			}

		} catch (Exception e) {
			eos.log("Errors getting kudos for a given object. Err:" + e.toString(), "Loyalty", "kudos", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return lst;
	}

}
