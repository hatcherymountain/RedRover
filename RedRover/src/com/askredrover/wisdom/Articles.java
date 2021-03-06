package com.askredrover.wisdom;

import com.eos.Eos;
import java.sql.*;
import com.askredrover.RedRover;
import java.util.ArrayList;

public class Articles {

	private Eos eos = null;
	private RedRover rr = null;

	public Articles(Eos eos, RedRover rr) {
		this.eos = eos;
		this.rr = rr;
	}

	/**
	 * Updates the header image for an article.
	 * 
	 * @param articleid
	 * @param fileid
	 */
	public void updateHeaderImage(String articleid, String fileid) {
		if (eos.isAdmin() || eos.isContributor()) {
			Connection c = eos.c();
			Statement s = null;
			try {

				s = c.createStatement();
				int fid = eos.d(fileid);
				int id = eos.d(articleid);
				String sql = "update wisdom_articles set headerimg=" + fid + " where articleid=" + id + "";
				s.execute(sql);

			} catch (Exception e) {
				eos.log("Errors updating article header. Err:" + e.toString(), "articles", "updateHeaderImage", 2);
			} finally {
				eos.cleanup(c, s);
			}
		}
	}

	/**
	 * 'Removes the image'
	 * 
	 * @param articleid
	 * @param fileid
	 */
	public void removeHeaderImage(String articleid) {
		if (eos.isAdmin() || eos.isContributor()) {
			Connection c = eos.c();
			Statement s = null;
			try {

				s = c.createStatement();
				int id = eos.d(articleid);
				String sql = "update wisdom_articles set headerimg=0 where articleid=" + id + "";
				s.execute(sql);

			} catch (Exception e) {
				eos.log("Errors updating article header to NULL. Err:" + e.toString(), "articles", "removeHeaderImage",
						2);
			} finally {
				eos.cleanup(c, s);
			}
		}
	}

	/**
	 * Updates Article Basic Metadata
	 * 
	 * @param HttpServletRequest
	 */
	public void updateMetaData(javax.servlet.http.HttpServletRequest r) {
		if (eos.isAdmin() || eos.isContributor()) {
			Connection c = eos.c();
			Statement s = null;

			try {

				s = c.createStatement();

				String aid = r.getParameter("aid");
				int id = eos.d(aid);
				
				String t = r.getParameter("title");
				t = com.eos.Eos.clean(t);
				
				String d = r.getParameter("description");
				d = com.eos.Eos.clean(d);
				
				String tags = r.getParameter("tags");
				tags = com.eos.Eos.clean(tags);
				
				String es = r.getParameter("essential");
				int iE = com.eos.utils.Strings.getIntFromString(es);
				
				String sid = r.getParameter("storeid");
				int iS = eos.d(sid);
				
				
				String rid = r.getParameter("roleid");
				int iR = eos.d(rid);
				
				String status = r.getParameter("status");
				int iStatus = com.eos.utils.Strings.getIntFromString(status);
				
				String catid = r.getParameter("catid"); int iCid = eos.d(catid);

				if (t.length() > 0) {
					s.addBatch("update wisdom_articles set title='" + t + "' where articleid=" + id + "");
				}
				s.addBatch("update wisdom_articles set description='" + d + "' where articleid=" + id + "");
				s.addBatch("update wisdom_articles set tags='" + tags + "' where articleid=" + id + "");
				s.addBatch("update wisdom_articles set essential=" + iE + " where articleid=" + id + "");
				s.addBatch("update wisdom_articles set storeid=" + iS + " where articleid=" + id + "");
				s.addBatch("update wisdom_articles set roleid=" + iR + " where articleid=" + id + "");
				s.addBatch("update wisdom_articles set categoryid=" + iCid + " where articleid=" + id + "");
				s.addBatch("update wisdom_articles set status=" + iStatus + " where articleid=" + id + "");

				s.executeBatch();

			} catch (Exception e) {
				eos.log("Errors updating metadata. Err:" + e.toString(), "Articles", "updateMetaData", 2);
			} finally {
				eos.cleanup(c, s);
			}
		}
	}

	/**
	 * get a specific article
	 * 
	 * @param articleid
	 * @return
	 */
	public Article get(String articleid) {
		Article article = null;

		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;
		try {

			s = c.createStatement();
			int aid = eos.d(articleid);
			String sql = "select accountid,storeid,title,description,added,entered,author,essential,tags,headerimg,status,roleid,categoryid from wisdom_articles where articleid="
					+ aid + "";

			rs = s.executeQuery(sql);
			while (rs.next()) {
				int a = rs.getInt(1);
				int st = rs.getInt(2);
				String t = rs.getString(3);
				String d = rs.getString(4);
				java.sql.Date added = rs.getDate(5);
				java.sql.Timestamp entered = rs.getTimestamp(6);
				int author = rs.getInt(7);
				int ess = rs.getInt(8);
				String tags = rs.getString(9);
				int headerimg = rs.getInt(10);
				int status = rs.getInt(11);
				int rid = rs.getInt(12);
				int cid = rs.getInt(13);

				article = new ArticleObject(aid, a, st, t, d, added, entered, author, ess, tags, headerimg, status, rid,
						cid);

			}

		} catch (Exception e) {
			eos.log("Errors getting article. Err;" + e.toString(), "Articles", "get", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return article;
	}
	
	/**
	 * Get articles that are end user facing.
	 * @param catid
	 * @return
	 */
	public ArrayList<Article> getArticlesForUser(String catid) {

		ArrayList<Article> lst = new ArrayList<Article>();

		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;
		try {
			
			int cid = eos.d(catid);
			s = c.createStatement();
			String sql = "select articleid,accountid,storeid,title,"
					+ "description,added,entered,author,essential,tags,headerimg,status,roleid from wisdom_articles where categoryid=" + cid + " and status=2 order by added desc";

			rs = s.executeQuery(sql);
			while (rs.next()) {
				int aid = rs.getInt(1);
				int a = rs.getInt(2);
				int st = rs.getInt(3);
				String t = rs.getString(4);
				String d = rs.getString(5);
				java.sql.Date added = rs.getDate(6);
				java.sql.Timestamp entered = rs.getTimestamp(7);
				int author = rs.getInt(8);
				int ess = rs.getInt(9);
				String tags = rs.getString(10);
				int headerimg = rs.getInt(11);
				int status = rs.getInt(12);
				int rid = rs.getInt(13);

				ArticleObject article = new ArticleObject(aid, a, st, t, d, added, entered, author, ess, tags,
						headerimg, status, rid, cid);
				
				if(st>0) { 
					
					if(rr.wisdom().search().isInStore(st)) {
						lst.add(article);
					}
					
				} else { 
					lst.add(article);
				}
			}

		} catch (Exception e) {
			eos.log("Errors getting ALL articles for user. Err;" + e.toString(), "Articles", "getArticlesForUser", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return lst;
	}
	
	
	/**
	 * Get all articles
	 * 
	 * @return
	 */
	public ArrayList<Article> getAll() {

		ArrayList<Article> lst = new ArrayList<Article>();

		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;
		try {

			s = c.createStatement();
			String sql = "select articleid,accountid,storeid,title,"
					+ "description,added,entered,author,essential,tags,headerimg,status,roleid,categoryid from wisdom_articles order by added desc";

			rs = s.executeQuery(sql);
			while (rs.next()) {
				int aid = rs.getInt(1);
				int a = rs.getInt(2);
				int st = rs.getInt(3);
				String t = rs.getString(4);
				String d = rs.getString(5);
				java.sql.Date added = rs.getDate(6);
				java.sql.Timestamp entered = rs.getTimestamp(7);
				int author = rs.getInt(8);
				int ess = rs.getInt(9);
				String tags = rs.getString(10);
				int headerimg = rs.getInt(11);
				int status = rs.getInt(12);
				int rid = rs.getInt(13);
				int cid = rs.getInt(14);

				ArticleObject article = new ArticleObject(aid, a, st, t, d, added, entered, author, ess, tags,
						headerimg, status, rid, cid);
				lst.add(article);

			}

		} catch (Exception e) {
			eos.log("Errors getting ALL articles. Err;" + e.toString(), "Articles", "getAll", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return lst;
	}

	/**
	 * Gets it going.
	 * 
	 * @param title
	 * @param description
	 * @param essential
	 */
	public String add(String title, String description, String tags, String essential, String storeid, String roleid,
			String categoryid) {
		String articleid = null;
		if (eos.isAdmin() || eos.isContributor()) {
			
			Connection c = eos.c();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {

				String today = com.eos.utils.Calendar.getTodayForSQL();
				int sid = eos.d(storeid);
				title = com.eos.Eos.clean(title);
				description = com.eos.Eos.clean(description);
				tags = com.eos.Eos.clean(tags);
				int iEssential = com.eos.utils.Strings.getIntFromString(essential);
				int iRole = eos.d(roleid);

				int aid = eos.user().getAccountId();
				int uid = eos.user().getUserId();
				int cid = eos.d(categoryid);

				String sql = "insert into wisdom_articles values(null," + aid + "," + sid + "," + "'" + title + "','"
						+ description + "','" + today + "',null," + uid + "," + iEssential + ",'" + tags + "',0,0,"
						+ iRole + "," + cid + ")";
				// eos.log(sql);
				ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
				ps.executeUpdate();

				rs = ps.getGeneratedKeys();

				if (rs.next()) {
					articleid = eos.e(rs.getInt(1));
				}

			} catch (Exception e) {
				eos.log("Errors adding article. Err:" + e.toString(), "Articles", "add", 2);
			} finally {
				eos.cleanup(c, ps, rs);
			}
		}
		return articleid;
	}

	/** SECTIONS CODE **/

	/**
	 * Section TYPES: 0 = Heading 1 = Text (body) 2 = Quote 3 = Img 4 = Image left
	 * and text right 5 = Img right and text left.
	 */

	private int getNextIndex(int aid) {
		int id = 0;

		if (eos.active()) {
			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;
			try {
				s = c.createStatement();
				rs = s.executeQuery("select max(indx) from wisdom_article_sections where articleid=" + aid + "");
				while (rs.next()) {
					int _id = rs.getInt(1);
					id = _id + 1;
				}
			} catch (Exception e) {
				eos.log("Errors getting next index value for Sections. Err;" + e.toString(), "Articles", "getNextIndex",
						2);
			} finally {
				eos.cleanup(c, s, rs);
			}

		}
		return id;
	}

	public void addSection(String aid, String typeof, String text, String fileid) {
		if (eos.isAdmin() || eos.isContributor()) {
			Connection c = eos.c();
			Statement s = null;

			try {

				s = c.createStatement();
				int id = eos.d(aid);
				int to = com.eos.utils.Strings.getIntFromString(typeof);
				text = com.eos.Eos.clean(text);
				int fid = eos.d(fileid);

				String sql = "insert into wisdom_article_sections values(null," + id + "," + getNextIndex(id) + "," + to
						+ ",'" + text + "'," + fid + ")";
				s.execute(sql);

			} catch (Exception e) {
				eos.log("Errors adding article section. Err;" + e.toString(), "Articles", "addSection", 2);
			} finally {
				eos.cleanup(c, s);
			}
		}
	}

	/**
	 * Updates the file reference.
	 * 
	 * @param sectionid
	 * @param fileid
	 */
	public void updateSectionImage(String sectionid, String fileid) {
		if (eos.isAdmin() || eos.isContributor()) {

			Connection c = eos.c();
			Statement s = null;

			try {
				s = c.createStatement();
				int id = eos.d(sectionid);
				int fid = eos.d(fileid);
				s.execute("update wisdom_article_sections set file1=" + fid + " where sectionid=" + id + "");

			} catch (Exception e) {
				eos.log("Errors updating section image. Err:" + e.toString(), "Articles", "updateSectionImage", 2);
			} finally {
				eos.cleanup(c, s);
			}
		}
	}

	/**
	 * Removes a section
	 * 
	 * @param sid
	 */
	public void updateSectionText(String sectionid, String text) {
		if (eos.isAdmin() || eos.isContributor()) {

			Connection c = eos.c();
			Statement s = null;

			try {
				s = c.createStatement();
				int id = eos.d(sectionid);
				text = com.eos.Eos.clean(text);
				String sql = "update wisdom_article_sections set text1='" + text + "' where sectionid=" + id + "";
				eos.log(sql);
				s.execute(sql);

			} catch (Exception e) {
				eos.log("Errors updating section text. Err:" + e.toString(), "Articles", "updateSectionText", 2);
			} finally {
				eos.cleanup(c, s);
			}
		}
	}

	/**
	 * Removes a section
	 * 
	 * @param sid
	 */
	public void removeSectionImage(String sectionid) {
		if (eos.isAdmin() || eos.isContributor()) {

			Connection c = eos.c();
			Statement s = null;

			try {
				s = c.createStatement();
				int id = eos.d(sectionid);
				s.execute("update wisdom_article_sections set file1=0 where sectionid=" + id + "");

			} catch (Exception e) {
				eos.log("Errors removing section image. Err:" + e.toString(), "Articles", "removeSection", 2);
			} finally {
				eos.cleanup(c, s);
			}
		}
	}

	/**
	 * Removes the entire Article
	 * 
	 * @param aid
	 */
	public void remove(String aid) {

		// todo some auditing needed on this.
		if (eos.isAdmin() || eos.isContributor()) {

			Connection c = eos.c();
			Statement s = null;

			try {
				int id = eos.d(aid);
				s = c.createStatement();
				s.addBatch("delete from wisdom_article_sections where articleid=" + id + "");
				s.addBatch("delete from wisdom_articles where articleid=" + id + "");
				s.executeBatch();

			} catch (Exception e) {
				eos.log("Errors removing article. Err:" + e.toString(), "Articles", "remove", 2);
			} finally {
				eos.cleanup(c, s);
			}
		}

	}

	/**
	 * Removes a section
	 * 
	 * @param sid
	 */
	public void updateSectionType(String sectionid, String newType) {
		if (eos.isAdmin() || eos.isContributor()) {

			Connection c = eos.c();
			Statement s = null;

			try {
				int iType = com.eos.utils.Strings.getIntFromString(newType);
				s = c.createStatement();
				int id = eos.d(sectionid);
				s.execute("update  wisdom_article_sections set typeof=" + iType + " where sectionid=" + id + "");

			} catch (Exception e) {
				eos.log("Errors updating section Err:" + e.toString(), "Articles", "removeSection", 2);
			} finally {
				eos.cleanup(c, s);
			}
		}
	}

	/**
	 * Removes a section
	 * 
	 * @param sid
	 */
	public void removeSection(String sid) {
		if (eos.isAdmin() || eos.isContributor()) {
			Connection c = eos.c();
			Statement s = null;
			try {

				s = c.createStatement();
				int id = eos.d(sid);
				s.execute("delete from wisdom_article_sections where sectionid=" + id + "");

			} catch (Exception e) {
				eos.log("Errors removing section. Err:" + e.toString(), "Articles", "removeSection", 2);
			} finally {
				eos.cleanup(c, s);
			}

		}
	}

	/**
	 * Gets the sections ORDERED by index
	 * 
	 * @param aid
	 * @return ArrayList<Section>
	 */
	public ArrayList<Section> getSections(String aid) {
		ArrayList<Section> lst = new ArrayList<Section>();
		if (eos.active()) {
			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				s = c.createStatement();
				int id = eos.d(aid);
				rs = s.executeQuery(
						"select sectionid,indx,typeof,text1,file1 from wisdom_article_sections where articleid=" + id
								+ " order by indx asc");
				while (rs.next()) {
					int sid = rs.getInt(1);
					int ind = rs.getInt(2);
					int to = rs.getInt(3);
					String t = rs.getString(4);
					int f = rs.getInt(5);
					Section _section = new Section(sid, id, ind, to, t, f);
					lst.add(_section);
				}

			} catch (Exception e) {
				eos.log("Errors getting sections. Err:" + e.toString(), "Articles", "getSections", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}
		}
		return lst;
	}

}
