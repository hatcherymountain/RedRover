package com.askredrover.wisdom;
import com.eos.files.File;
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
	 * Get the map list for a particular article.
	 * @param aid
	 * @return
	 */
	private ArrayList<Integer> getMapList(int aid) { 
		ArrayList<Integer> lst = new ArrayList<Integer>();
		if(eos.active())
		{
			Connection c = eos.c();
			Statement  s = null;
			ResultSet rs = null;
			
			try { 
				
				s = c.createStatement();
				rs = s.executeQuery("select distinct linkedid from wisdom_linkmap where articleid=" + aid + "");
				while(rs.next())
				{
					lst.add(rs.getInt(1));
				}
				
			} catch(Exception e) { 
				eos.log("Errors getting map list. Err:" + e.toString(),"Articles","getMapList",2);
			} finally {
				eos.cleanup(c, s,rs);
			}
			
		}
		
		return lst;
	}
	
	/**
	 * Get all articles linked to a parent article.
	 * @param parentid
	 * @return ArrayList of Article objects.
	 */
	public ArrayList<Article> getLinkedArticles(String parentid) { 
		ArrayList<Article> lst = new ArrayList<Article>();
		int pid = eos.d(parentid);
		if(eos.active())
		{
			Connection c = eos.c();
			Statement  s = null;
			ResultSet rs = null;
			
			try { 
				
				s = c.createStatement();
				rs = s.executeQuery("select distinct linkedid from wisdom_linkmap where articleid=" + pid + "");
				while(rs.next())
				{
					int id = rs.getInt(1);
					Article a = this.get(id);
					if(a!=null) { 
						lst.add(a);
					}
				}
				
			} catch(Exception e) { 
				eos.log("Errors getting map list. Err:" + e.toString(),"Articles","getMapList",2);
			} finally {
				eos.cleanup(c, s,rs);
			}
			
		}
		return lst;
	}/**
	 * Get all articles linked to a parent article.
	 * @param parentid
	 * @return ArrayList of Article objects.
	 */
	public ArrayList<File> getLinkedFiles(String parentid) { 
		ArrayList<File> lst = new ArrayList<File>();
		int pid = eos.d(parentid);
		if(eos.active())
		{
			Connection c = eos.c();
			Statement  s = null;
			ResultSet rs = null;
			
			try { 
				
				s = c.createStatement();
				rs = s.executeQuery("select distinct fileid from wisdom_linkfiles where articleid=" + pid + "");
				while(rs.next())
				{
					int id = rs.getInt(1);
					String _fid = eos.e(id);
					File f = eos.files().getFile(_fid);
					if(f!=null) { 
						lst.add(f);
					}
				}
				
			} catch(Exception e) { 
				eos.log("Errors getting map FILE list. Err:" + e.toString(),"Articles","getLinkedFiles",2);
			} finally {
				eos.cleanup(c, s,rs);
			}
			
		}
		return lst;
	}
	
	
	
	/**
	 * removes an article linked to a parent.
	 * @param parentid
	 * @param aid
	 */
	public void removeLinkedArticle(String parentid, String aid)
	{
		if(eos.isAdmin())
		{
			Connection c = eos.c();
			Statement  s = null;
			try { 
				
				s = c.createStatement();
				
				int pid = eos.d(parentid);
				int id = eos.d(aid);
				
				String sql = "delete from wisdom_linkmap where articleid=" + pid + " and linkedid=" + id + "";
				s.execute(sql);
				
			} catch(Exception e)
			{
				eos.log("Errors removing linked article. Err:" + e.toString(),"Articles","removeLinkedArticle",2);
			} finally { 
				eos.cleanup(c,s);
			}
		}
	}
	
	/**
	 * removes an file linked to a parent.
	 * @param parentid
	 * @param Encoded File Identifier
	 */
	public void removeLinkedFile(String parentid, String fid)
	{
		if(eos.isAdmin())
		{
			Connection c = eos.c();
			Statement  s = null;
			try { 
				
				s = c.createStatement();
				
				int pid = eos.d(parentid);
				int id = eos.d(fid);
				
				String sql = "delete from wisdom_linkfiles where articleid=" + pid + " and fileid=" + id + "";
				s.execute(sql);
				
			} catch(Exception e)
			{
				eos.log("Errors removing linked FILE. Err:" + e.toString(),"Articles","removeLinkedFile",2);
			} finally { 
				eos.cleanup(c,s);
			}
		}
	}
	
	
	/**
	 * Responsible for linking files to an article.
	 * @param parentid
	 * @param fileid
	 * @param checked
	 */
	public void linkFile(String parentid, String fileid, String checked)
	{
		int iPid = eos.d(parentid);
		int iFid = eos.d(fileid);
		checked = com.eos.Eos.clean(checked);
		int iCheck = 0;
		if(checked.equals("true")) { iCheck = 1; } 
		
		Connection c = eos.c();
		Statement  s = null;

		try { 
			
			s = c.createStatement();
			
			if(iCheck == 1) { 
				
				s.execute("insert into wisdom_linkfiles values(" + iPid + "," + iFid + ")");
				
			} else { 
				
				s.execute("delete from wisdom_linkfiles where articleid=" + iPid + " and fileid=" + iFid + "");
				
			}
			
			
			
			
		} catch(Exception e)
		{
			eos.log("Errors linking or delinking files from an article. Err:" + e.toString(),"Articles","linkFile",2);
		} finally { 
			eos.cleanup(c,s);
		}
		
	}
	/**
	 * Links an article
	 * @param parentid
	 * @param articleid
	 * @param checked
	 */
	public void link(String parentid, String articleid, String checked)
	{
		int iPid = eos.d(parentid);
		int iAid = eos.d(articleid);
		checked = com.eos.Eos.clean(checked);
		int iCheck = 0;
		if(checked.equals("true")) { iCheck = 1; } 
		
		Connection c = eos.c();
		Statement  s = null;

		try { 
			
			s = c.createStatement();
			
			if(iCheck == 1) { 
				
				s.execute("insert into wisdom_linkmap values(" + iPid + "," + iAid + ")");
				
			} else { 
				
				s.execute("delete from wisdom_linkmap where articleid=" + iPid + " and linkedid=" + iAid + "");
				
			}
			
			
			
			
		} catch(Exception e)
		{
			eos.log("Errors linking or delinking article. Err:" + e.toString(),"Articles","link",2);
		} finally { 
			eos.cleanup(c,s);
		}
		
	}
	
	/**
	 * Is a given article linked to a parent article?
	 * @param parentid
	 * @param aid
	 * @return
	 */
	public boolean isArticleLinked(String parentid, String aid) { 
		boolean is = false;
		
		Connection c = eos.c();
		Statement  s = null;
		ResultSet rs = null;
		
		try { 
		
			int pid = eos.d(parentid);
			int id  = eos.d(aid);
			
			s = c.createStatement();
			String sql = "select count(*) from wisdom_linkmap where articleid=" + pid + " and linkedid=" + id + "";
			rs = s.executeQuery(sql);
			while(rs.next())
			{
				if(rs.getInt(1)>0) { 
					is = true;
				}
			}
			
		} catch(Exception e) { 
			eos.log("Errors determining whether article linked to a parent article. Err::" + e.toString(),"Articles","isArticleLinked",2);
		} finally { 
			eos.cleanup(c, s, rs);
		}
		
		return is;
	}
	/**
	 * Is a given article linked to a parent article?
	 * @param parentid
	 * @param aid
	 * @return
	 */
	public boolean isFileLinked(String parentid, String fid) { 
		boolean is = false;
		
		Connection c = eos.c();
		Statement  s = null;
		ResultSet rs = null;
		
		try { 
		
			int pid = eos.d(parentid);
			int id  = eos.d(fid);
			
			s = c.createStatement();
			String sql = "select count(*) from wisdom_linkfiles where articleid=" + pid + " and fileid=" + id + "";
			rs = s.executeQuery(sql);
			while(rs.next())
			{
				if(rs.getInt(1)>0) { 
					is = true;
				}
			}
			
		} catch(Exception e) { 
			eos.log("Errors determining whether FILE linked to a parent article. Err::" + e.toString(),"Articles","isFileLinked",2);
		} finally { 
			eos.cleanup(c, s, rs);
		}
		
		return is;
	}	
	/**
	 * Determines whether a given article is or is not in the list.
	 * @param lst
	 * @param id
	 * @return
	 */
	private boolean inLinkList(ArrayList<Integer> lst, int id) { 
		boolean is  = false;
		
		int size = lst.size();
		for(int i = 0; i < size; i++) {
			int eid = (int)lst.get(i);
			if(eid==id) { 
				is = true; break;
			}
		}
		
		return is;
	}
	
	
	/**
	 * Links other articles to a given article
	 */
	public void link(javax.servlet.http.HttpServletRequest r)
	{
		if(eos.isAdmin()) { 
			
			Connection c = eos.c();
			Statement  s = null;
			
			try { 
				
				
				s = c.createStatement();
				
				String aid = r.getParameter("articleid"); int iAid = eos.d(aid);
				if(iAid > 0)
				{
					
					ArrayList<Integer> lst = getMapList(iAid);
					
					/** Get the links **/
					String[] linkids = r.getParameterValues("linkid");
					int linksize  = linkids.length;
					
					for(int i = 0; i < linksize; i++) { 
						String linkid = (String)linkids[i];
						int id = eos.d(linkid);
						
						if(!inLinkList(lst,id)) { 
							s.addBatch("insert into wisdom_linkmap values(" + iAid + "," + id + ")");
						}
					}
					
					s.executeBatch();
					
					
				}
				
				
				
			} catch(Exception e)
			{
				eos.log("Errors linking articles to a parent. Err:" + e.toString(),"Articles","link",2);
			} finally { 
				eos.cleanup(c,s);
			}
			
			
		}
	}
	
	
	/**
	 * Links other files to a given article
	 */
	public void linkFile(javax.servlet.http.HttpServletRequest r)
	{
		if(eos.isAdmin()) { 
			
			Connection c = eos.c();
			Statement  s = null;
			
			try { 
				
				
				s = c.createStatement();
				
				String aid = r.getParameter("articleid"); int iAid = eos.d(aid);
				if(iAid > 0)
				{
					
					ArrayList<Integer> lst = getMapList(iAid);
					
					/** Get the links **/
					String[] linkids = r.getParameterValues("linkid");
					int linksize  = linkids.length;
					
					for(int i = 0; i < linksize; i++) { 
						String linkid = (String)linkids[i];
						int id = eos.d(linkid);
						
						if(!inLinkList(lst,id)) { 
							s.addBatch("insert into wisdom_linkmap values(" + iAid + "," + id + ")");
						}
					}
					
					s.executeBatch();
					
					
				}
				
				
				
			} catch(Exception e)
			{
				eos.log("Errors linking articles to a parent. Err:" + e.toString(),"Articles","link",2);
			} finally { 
				eos.cleanup(c,s);
			}
			
			
		}
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
	 * Removes a section file.
	 * 
	 * @param sid
	 * @param fileid
	 */
	public void removeSectionFile(String sid, String fileid) {

		if (eos.isAdmin()) {
			Connection c = eos.c();
			Statement s = null;
			try {
				s = c.createStatement();
				int id = eos.d(sid);
				int fid = eos.d(fileid);
				String sql = "delete from wisdom_article_section_files where sid=" + id + " and fileid=" + fid + "";
				s.execute(sql);

			} catch (Exception e) {
				eos.log("Errors removing aticle section file. Err:" + e.toString(), "Articles", "removeSectionFile", 2);
			} finally {
				eos.cleanup(c, s);
			}

		}

	}

	public void addSectionFile(String articleid, String sectionid, String fileid) {
		if (eos.isAdmin()) {
			Connection c = eos.c();
			Statement s = null;
			try {
				s = c.createStatement();
				int id = eos.d(articleid);
				int sid = eos.d(sectionid);
				int f = eos.d(fileid);
				String sql = "insert into  wisdom_article_section_files values(null," + id + "," + sid + "," + f + ")";

				s.execute(sql);

			} catch (Exception e) {
				eos.log("Errors adding aticle section file. Err:" + e.toString(), "Articles", "addSectionFile", 2);
			} finally {
				eos.cleanup(c, s);
			}

		}
	}

	/**
	 * Get all files in a given section...
	 * 
	 * @param aid
	 * @param sid
	 * @return
	 */
	public ArrayList<com.eos.files.File> files(String aid, String sid) {
		ArrayList<com.eos.files.File> files = new ArrayList<com.eos.files.File>();

		if (eos.active()) {
			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				int iAid = eos.d(aid);
				int iSid = eos.d(sid);
				s = c.createStatement();
				String sql = "select fileid from wisdom_article_section_files where articleid=" + iAid + " and sid="
						+ iSid + "";
				rs = s.executeQuery(sql);
				while (rs.next()) {
					int fid = rs.getInt(1);
					String fileid = eos.e(fid);
					com.eos.files.File f = eos.files().getFile(fileid);
					if (f != null) {
						files.add(f);
					}
				}

			} catch (Exception e) {
				eos.log("Errors getting article section files. Err:" + e.toString(), "Articles", "files", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}

		}
		return files;
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

				String catid = r.getParameter("catid");
				int iCid = eos.d(catid);

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
	 * Get an article using encoded article identifier
	 * @param articleid
	 * @return Article.class
	 */
	public Article get(String articleid) {
		return get(eos.d(articleid));
	}
	/**
	 * get a specific article
	 * 
	 * @param articleid
	 * @return
	 */
	private Article get(int aid) {
		Article article = null;

		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;
		try {

			s = c.createStatement();
			
			String sql = "select accountid,storeid,title,description,added,entered,author,essential,tags,headerimg,status,roleid,categoryid,youtube from wisdom_articles where articleid="
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
				String youtube = rs.getString(14);

				article = new ArticleObject(aid, a, st, t, d, added, entered, author, ess, tags, headerimg, status, rid,
						cid, youtube);

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
	 * 
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
					+ "description,added,entered,author,essential,tags,headerimg,status,roleid,youtube from wisdom_articles where categoryid="
					+ cid + " and status=2 order by added desc";

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
				String yt = rs.getString(14);

				ArticleObject article = new ArticleObject(aid, a, st, t, d, added, entered, author, ess, tags,
						headerimg, status, rid, cid, yt);

				if (st > 0) {

					if (rr.wisdom().search().isInStore(st)) {
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
					+ "description,added,entered,author,essential,tags,headerimg,status,roleid,categoryid,youtube from wisdom_articles order by added desc";

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
				String yt = rs.getString(15);

				ArticleObject article = new ArticleObject(aid, a, st, t, d, added, entered, author, ess, tags,
						headerimg, status, rid, cid, yt);
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
			String categoryid, String youtube) {
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
				youtube = com.eos.Eos.clean(youtube);
				int aid = eos.user().getAccountId();
				int uid = eos.user().getUserId();
				int cid = eos.d(categoryid);

				String sql = "insert into wisdom_articles values(null," + aid + "," + sid + "," + "'" + title + "','"
						+ description + "','" + today + "',null," + uid + "," + iEssential + ",'" + tags + "',0,0,"
						+ iRole + "," + cid + ",'" + youtube + "')";
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
	 * Updates indexing values.
	 * @param articleid
	 * @param sectionid
	 * @param indx
	 */
	public void updateSectionIndex(String sectionid, String indx)
	{
		if(eos.isAdmin())
		{
			Connection c = eos.c();
			Statement  s = null;
			try { 
				
				s = c.createStatement();
				int se = eos.d(sectionid);
				int i = com.eos.utils.Math.getIntFromString(indx);
				
				String sql = "update wisdom_article_sections set indx=" + i + " where sectionid=" + se + "";
				s.execute(sql);
				
			} catch(Exception e)
			{
				eos.log("Errors updating section index. Err:" + e.toString(),"Articles","updateSectionIndex",2);
			} finally { 
				eos.cleanup(c,s);
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
