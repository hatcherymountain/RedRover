package com.askredrover.wisdom.search;

import java.util.Arrays;

import java.util.LinkedHashSet;
import java.sql.*;
import java.util.ArrayList;
import com.eos.Eos;
import com.askredrover.RedRover;
import com.askredrover.wisdom.Article;
import com.askredrover.wisdom.Tutorial;

public class Searches {

	private Eos eos = null;
	private RedRover rr = null;

	public Searches(Eos eos, RedRover rr) {
		this.eos = eos;
		this.rr = rr;
	}

	/**
	 * What searches have taken place?
	 * 
	 * @return
	 */
	public ArrayList<Search> history() {

		ArrayList<Search> lst = new ArrayList<Search>();

		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;

		try {

			int eid = eos.account().eid();
			s = c.createStatement();
			// public Search(int sid, int userid, String added, String term, int files, int
			// articles, int tuts)
			String sql = "select searchid, userid, added, term, files, articles,tutorials from wisdom_searches where eid="
					+ eid + " order by added desc";
			rs = s.executeQuery(sql);
			while (rs.next()) {
				int sid = rs.getInt(1);
				int uid = rs.getInt(2);
				java.sql.Date added = rs.getDate(3);
				String a = eos.date(added);
				String t = rs.getString(4);
				int f = rs.getInt(5);
				int ar = rs.getInt(6);
				int tu = rs.getInt(7);

				Search _s = new Search(sid, uid, a, t, f, ar, tu);
				lst.add(_s);
			}

		} catch (Exception e) {
			eos.log("Errors getting the search history. Err:" + e.toString(), "Searches", "history", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}
		return lst;

	}

	public ArrayList<Search> recentSearches() {

		ArrayList<Search> lst = new ArrayList<Search>();

		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;

		try {

			int eid = eos.account().eid();
			s = c.createStatement();
			// public Search(int sid, int userid, String added, String term, int files, int
			// articles, int tuts)
			String sql = "select searchid, userid, added, term, files, articles,tutorials from wisdom_searches where eid="
					+ eid + " order by added desc limit 10";
			rs = s.executeQuery(sql);
			while (rs.next()) {
				int sid = rs.getInt(1);
				int uid = rs.getInt(2);
				java.sql.Date added = rs.getDate(3);
				String a = eos.date(added);
				String t = rs.getString(4);
				int f = rs.getInt(5);
				int ar = rs.getInt(6);
				int tu = rs.getInt(7);

				Search _s = new Search(sid, uid, a, t, f, ar, tu);
				lst.add(_s);
			}

		} catch (Exception e) {
			eos.log("Errors getting the search history. Err:" + e.toString(), "Searches", "history", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}
		return lst;

	}

	/**
	 * Get all articles
	 * 
	 * @return ArrayList Article.class
	 */
	public ArrayList<com.askredrover.wisdom.Article> allArticles() {

		ArrayList<com.askredrover.wisdom.Article> lst = new ArrayList<com.askredrover.wisdom.Article>();

		if (eos.active()) {

			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				ArrayList<com.askredrover.wisdom.Article> origLst = rr.wisdom().articles().getAll();
				int size = origLst.size();

				for (int i = 0; i < size; i++) {

					com.askredrover.wisdom.Article a = (com.askredrover.wisdom.Article) origLst.get(i);

					if (a.categoryid() > 0 && a.live()) {

						if (a.storeid() > 0) {

							if (isInStore(a.storeid())) {
								lst.add(a);
							}

						} else {
							lst.add(a);
						}

					}
				}

			} catch (Exception e) {
				eos.log("Errors getting list of all articles. Err;" + e.toString(), "Searches", "allArticles", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}

		}

		return lst;

	}

	/**
	 * Get back ALL files. The file must be categoried, be active and the user must
	 * have access to the file AND if a store is defined be a member of that store.
	 * 
	 * @return ArrayList of File.class.
	 */
	public ArrayList<com.eos.files.File> allFiles() {
		ArrayList<com.eos.files.File> lst = new ArrayList<com.eos.files.File>();
		if (eos.active()) {

			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				ArrayList<com.eos.files.File> origLst = eos.files().getAccountFiles(eos.user().getAccountId());
					
				int size = origLst.size();

				
				for (int i = 0; i < size; i++) {
					
					com.eos.files.File f = (com.eos.files.File) origLst.get(i);
					
					
					if (f.categoryid() > 0 && f.active()) {

						if (rr.wisdom().files().passSecurityCheck(f)) {
								
							
							
							if (f.companyid() > 0) {
								
								
								
								if (isInStore(f.companyid())) {
									lst.add(f);
								} 
								
								
							} else {
								lst.add(f);
							}

						}
					}
				}

			} catch (Exception e) {
				eos.log("Errors getting list of all files. Err;" + e.toString(), "Searches", "allFiles", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}

		}
		
		//eos.log("Retun SIZE:" + lst.size());
		return lst;
	}

	/**
	 * Get all 'public' tutorials
	 * 
	 * @return
	 */
	public ArrayList<com.askredrover.wisdom.Tutorial> allTutorials() {

		ArrayList<com.askredrover.wisdom.Tutorial> lst = new ArrayList<com.askredrover.wisdom.Tutorial>();

		if (eos.active()) {

			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				ArrayList<com.askredrover.wisdom.Tutorial> origLst = rr.wisdom().tutorials().getAll();
				int size = origLst.size();

				for (int i = 0; i < size; i++) {

					com.askredrover.wisdom.Tutorial a = (com.askredrover.wisdom.Tutorial) origLst.get(i);

					if (a.categoryid() > 0 && (a.status() == 2)) {

						if (a.storeid() > 0) {

							if (isInStore(a.storeid())) {
								lst.add(a);
							}

						} else {
							lst.add(a);
						}

					}
				}

			} catch (Exception e) {
				eos.log("Errors getting list of all tutorials. Err;" + e.toString(), "Searches", "allTutorials", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}

		}

		return lst;

	}

	/**
	 * Get deduplicated Articles & their sections.
	 * 
	 * @param t  - titles
	 * @param d  - descriptions
	 * @param s- sections
	 * @param w  - tags
	 * @return
	 */
	private ArrayList<Article> deduplicatedArticles(ArrayList<Integer> t, ArrayList<Integer> d, ArrayList<Integer> s,
			ArrayList<Integer> w) {
		ArrayList<Article> lst = new ArrayList<Article>();
		t.addAll(d);
		t.addAll(s);
		t.addAll(w);

		LinkedHashSet<Integer> hashSet = new LinkedHashSet<>(t);
		ArrayList<Integer> deduped = new ArrayList<>(hashSet);

		int size = deduped.size();
		for (int i = 0; i < size; i++) {

			int iA = (int) deduped.get(i);
			String aid = eos.e(iA);
			Article a = rr.wisdom().articles().get(aid);
			if (a != null) {

				if (a.status() == 2) { // live

					if (eos.users().isAllowed(a.roleid())) {

						if (a.storeid() > 0) {

							if (this.isInStore(a.storeid())) {
								lst.add(a);
							}

						} else {

							lst.add(a);

						}

					}

				}

			}

		}
		return lst;
	}

	/**
	 * Dedup files.
	 * 
	 * @param t
	 * @param d
	 * @param w
	 * @return
	 */
	private ArrayList<com.eos.files.File> deduplicatedFiles(ArrayList<Integer> t, ArrayList<Integer> d,
			ArrayList<Integer> w) {
		ArrayList<com.eos.files.File> lst = new ArrayList<com.eos.files.File>();
		t.addAll(d);
		t.addAll(w);

		LinkedHashSet<Integer> hashSet = new LinkedHashSet<>(t);
		ArrayList<Integer> deduped = new ArrayList<>(hashSet);

		/** Now let's get files and check permissions **/

		int size = deduped.size();
		for (int i = 0; i < size; i++) {
			int iF = (int) deduped.get(i);
			String fid = eos.e(iF);
			com.eos.files.File file = eos.files().getFile(fid);

			/**
			 * Now Check
			 */

			if (file.status() == 1) {

				if (eos.users().isAllowed(file.roleid())) {

					if (file.companyid() > 0) {

						if (this.isInStore(file.companyid())) {
							lst.add(file);
						}

					} else {

						lst.add(file);

					}

				}

			}

		}

		return lst;
	}

	private ArrayList<Tutorial> deduplicatedTutorials(ArrayList<Integer> t, ArrayList<Integer> d,
			ArrayList<Integer> s) {
		ArrayList<Tutorial> lst = new ArrayList<Tutorial>();
		t.addAll(d);
		t.addAll(s);

		LinkedHashSet<Integer> hashSet = new LinkedHashSet<>(t);
		ArrayList<Integer> deduped = new ArrayList<>(hashSet);

		int size = deduped.size();
		for (int i = 0; i < size; i++) {

			int iA = (int) deduped.get(i);
			String tid = eos.e(iA);
			Tutorial a = rr.wisdom().tutorials().get(tid);
			if (a != null) {

				if (a.status() == 2) { // live

					if (eos.users().isAllowed(a.role())) {

						if (a.storeid() > 0) {

							if (this.isInStore(a.storeid())) {
								lst.add(a);
							}

						} else {

							lst.add(a);

						}

					}

				}

			}

		}
		return lst;
	}

	/**
	 * Is this person in the store if it is designated as such?
	 * 
	 * @param storeid
	 * @return
	 */
	public boolean isInStore(int storeid) {
		boolean is = false;

		if (eos.active()) {

			if (eos.user().getCompanyId() == storeid) {
				is = true;
			}

		}

		return is;
	}

	/**
	 * Get recent articles.
	 * 
	 * @return ArrayList of Article.class
	 */
	public ArrayList<com.askredrover.wisdom.Article> recentArticles(int howmanydays) {
		ArrayList<com.askredrover.wisdom.Article> lst = new ArrayList<com.askredrover.wisdom.Article>();
		if (eos.active()) {
			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				s = c.createStatement();
				String sql = "select articleid from wisdom_articles where status=2 and added > now()-INTERVAL " + howmanydays + " day order by added desc";
				rs = s.executeQuery(sql);

				while (rs.next()) {
					int iA = rs.getInt(1);
					String aid = eos.e(iA);
					com.askredrover.wisdom.Article article = rr.wisdom().articles().get(aid);
					if (article != null) {

						if (eos.getUsers().isAllowed(article.roleid())) {

							if (article.storeid() > 0) {

								if (isInStore(article.storeid())) {
									lst.add(article);
								}

							} else {

								lst.add(article);

							}

						}

					}
				}

			} catch (Exception e) {
				eos.log("Errors getting recent articles. Err:" + e.toString(), "Searches", "recentArticles", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}

		}

		return lst;
	}
	
	
	/**
	 * Get recent articles.
	 * 
	 * @return ArrayList of Article.class
	 */
	public ArrayList<com.askredrover.wisdom.Article> recentArticles() {
		ArrayList<com.askredrover.wisdom.Article> lst = new ArrayList<com.askredrover.wisdom.Article>();
		if (eos.active()) {
			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				s = c.createStatement();
				String sql = "select articleid from wisdom_articles where status=2 order by added desc limit 8";
				rs = s.executeQuery(sql);

				while (rs.next()) {
					int iA = rs.getInt(1);
					String aid = eos.e(iA);
					com.askredrover.wisdom.Article article = rr.wisdom().articles().get(aid);
					if (article != null) {

						if (eos.getUsers().isAllowed(article.roleid())) {

							if (article.storeid() > 0) {

								if (isInStore(article.storeid())) {
									lst.add(article);
								}

							} else {

								lst.add(article);

							}

						}

					}
				}

			} catch (Exception e) {
				eos.log("Errors getting recent articles. Err:" + e.toString(), "Searches", "recentArticles", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}

		}

		return lst;
	}

	/**
	 * Recent Files.
	 * 
	 * @param threshold
	 * @return
	 */
	public ArrayList<com.eos.files.File> recentFiles(int howmanydays) {

		ArrayList<com.eos.files.File> lst = new ArrayList<com.eos.files.File>();

		if (eos.active()) {
			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				s = c.createStatement();

				String sql = "select fileid from eos_files where categoryid>0 and status=1 and added > now()-INTERVAL " + howmanydays + " day order by added desc";

				rs = s.executeQuery(sql);

				while (rs.next()) {
					int fid = rs.getInt(1);
					String _f = eos.e(fid);
					com.eos.files.File file = eos.files().getFile(_f);
					if (file != null) {

						if (rr.wisdom().files().passSecurityCheck(file)) {

							if (eos.isAdmin()) {
								lst.add(file);
							} else {
								if (file.companyid() > 0) {

									if (isInStore(file.companyid())) {
										lst.add(file);
									}

								} else {

									lst.add(file);

								}
							}
						}

					}
				}

			} catch (Exception e) {
				eos.log("Errors getting recent files. Err:" + e.toString(), "Searches", "recentFiles", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}
		}

		return lst;
	}

	
	
	/**
	 * Recent Files.
	 * 
	 * @param threshold
	 * @return
	 */
	public ArrayList<com.eos.files.File> recentFiles() {

		ArrayList<com.eos.files.File> lst = new ArrayList<com.eos.files.File>();

		if (eos.active()) {
			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				s = c.createStatement();

				String sql = "select fileid from eos_files where categoryid>0 and status=1 order by added desc limit 8";

				rs = s.executeQuery(sql);

				while (rs.next()) {
					int fid = rs.getInt(1);
					String _f = eos.e(fid);
					com.eos.files.File file = eos.files().getFile(_f);
					if (file != null) {

						if (rr.wisdom().files().passSecurityCheck(file)) {

							if (eos.isAdmin()) {
								lst.add(file);
							} else {
								if (file.companyid() > 0) {

									if (isInStore(file.companyid())) {
										lst.add(file);
									}

								} else {

									lst.add(file);

								}
							}
						}

					}
				}

			} catch (Exception e) {
				eos.log("Errors getting recent files. Err:" + e.toString(), "Searches", "recentFiles", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}
		}

		return lst;
	}

	/**
	 * Get all recent tutorials
	 * 
	 * @return ArrayList of Tutorials
	 */
	public ArrayList<com.askredrover.wisdom.Tutorial> recentTutorials(int howmanydays) {
		ArrayList<com.askredrover.wisdom.Tutorial> lst = new ArrayList<com.askredrover.wisdom.Tutorial>();
		if (eos.active()) {
			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				s = c.createStatement();
				String sql = "select tutid from wisdom_tutorials where status=2 and  added > now()-INTERVAL " + howmanydays + " day order by added desc";
				rs = s.executeQuery(sql);

				while (rs.next()) {
					int iA = rs.getInt(1);
					String tid = eos.e(iA);
					com.askredrover.wisdom.Tutorial tut = rr.wisdom().tutorials().get(tid);
					if (tut != null) {

						if (eos.getUsers().isAllowed(tut.role())) {

							if (tut.storeid() > 0) {

								if (isInStore(tut.storeid())) {
									lst.add(tut);
								}

							} else {

								lst.add(tut);

							}

						}

					}
				}

			} catch (Exception e) {
				eos.log("Errors getting recent tutorials. Err:" + e.toString(), "Searches", "recentTutorials", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}

		}

		return lst;
	}
	
	
	/**
	 * Get all recent tutorials
	 * 
	 * @return ArrayList of Tutorials
	 */
	public ArrayList<com.askredrover.wisdom.Tutorial> recentTutorials() {
		ArrayList<com.askredrover.wisdom.Tutorial> lst = new ArrayList<com.askredrover.wisdom.Tutorial>();
		if (eos.active()) {
			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				s = c.createStatement();
				String sql = "select tutid from wisdom_tutorials where status=2 order by added desc limit 8";
				rs = s.executeQuery(sql);

				while (rs.next()) {
					int iA = rs.getInt(1);
					String tid = eos.e(iA);
					com.askredrover.wisdom.Tutorial tut = rr.wisdom().tutorials().get(tid);
					if (tut != null) {

						if (eos.getUsers().isAllowed(tut.role())) {

							if (tut.storeid() > 0) {

								if (isInStore(tut.storeid())) {
									lst.add(tut);
								}

							} else {

								lst.add(tut);

							}

						}

					}
				}

			} catch (Exception e) {
				eos.log("Errors getting recent tutorials. Err:" + e.toString(), "Searches", "recentTutorials", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}

		}

		return lst;
	}
	
	/**
	 * Is this a search for ID like A###, F###, T###
	 * @param term
	 * @return
	 */
	private boolean isIDSearch(String term)
	{
		boolean is = false;
		
		term = term.toLowerCase();
		/** Is the first letter in search a, f or t? **/
		String initChar = term.substring(0,1);
		
		if(initChar.equals("a") || initChar.equals("f") || initChar.equals("t")) {
			
			/** is the 2nd character numeric? **/
			
			boolean isDig = Character.isDigit(term.charAt(1));
			
			if(isDig) { 
				is = true;
			}
			
		}
		
		
		return is;
	}
	
	
	/**
	 * Finds articles by the PRIMARY KEY passed in as a string.
	 * @param term
	 * @return
	 */
	private ArrayList<Article> findArticlesByID(String term) { 
		ArrayList<Article> lst = new ArrayList<Article>();
		
		Connection c = eos.c();
		Statement  s = null;
		ResultSet rs = null;
		
		try { 
			
			/** double check to make sure nothing insideous passed along **/
			term = com.eos.Eos.clean(term);
			
			s = c.createStatement();
			String sql = "select articleid from wisdom_articles where articleid=" + term + "";
			eos.log(sql);
			rs = s.executeQuery(sql);
			while(rs.next())
			{
				int aid = rs.getInt(1); String strAid = eos.e(aid);
				Article a = rr.wisdom().articles().get(strAid);
				if(a!=null) { 
					lst.add(a);
				}
			}
			
		} catch(Exception e)
		{
			eos.log("Errors finding articles by ID. Err;" + e.toString(),"Searches","findArticlesByID",2);
		} finally { 
			eos.cleanup(c, s,rs);
		}
		return lst;
	}
	
	private ArrayList<Tutorial> findTutorialsByID(String term) { 
		ArrayList<Tutorial> lst = new ArrayList<Tutorial>();
		
		Connection c = eos.c();
		Statement  s = null;
		ResultSet rs = null;
		
		try { 
			
			/** double check to make sure nothing insideous passed along **/
			term = com.eos.Eos.clean(term);
			
			s = c.createStatement();
			rs = s.executeQuery("select tutid from wisdom_tutorials where tutid=" + term + "");
			while(rs.next())
			{
				int tid = rs.getInt(1); String strTid = eos.e(tid);
				Tutorial t = rr.wisdom().tutorials().get(strTid);
				if(t!=null) { 
					lst.add(t);
				}
			}
			
		} catch(Exception e)
		{
			eos.log("Errors finding tutorials by ID. Err;" + e.toString(),"Searches","findTutorialsByID",2);
		} finally { 
			eos.cleanup(c, s,rs);
		}
		return lst;
	}
	
	
	private ArrayList<com.eos.files.File> findFilesByID(String term) { 
		ArrayList<com.eos.files.File> lst = new ArrayList<com.eos.files.File>();
		
		Connection c = eos.c();
		Statement  s = null;
		ResultSet rs = null;
		
		try { 
			
			/** double check to make sure nothing insideous passed along **/
			term = com.eos.Eos.clean(term);
			
			s = c.createStatement();
			String sql = "select fileid from eos_files where fileid=" + term + "";
			eos.log(sql);
			rs = s.executeQuery(sql);
			while(rs.next())
			{
				int fid = rs.getInt(1); String strFid = eos.e(fid);
				com.eos.files.File file = eos.files().getFile(strFid);
				if(file!=null) { 
					lst.add(file);
				}
			}
			
		} catch(Exception e)
		{
			eos.log("Errors finding articles by ID. Err;" + e.toString(),"Searches","findFilesByID",2);
		} finally { 
			eos.cleanup(c, s,rs);
		}
		return lst;
	}

	/**
	 * Searches both files and the articles (and later tutorials) using term. We use
	 * full text search ... and dedup results.
	 * 
	 * @param term
	 * @return
	 */
	public Result search(String term) {
		Result res = null;
		if (eos.active()) {

			term = com.eos.utils.Strings.absoluteTruncation(term, 255); // just for safety
			eos.log("Search term:" + term);
			
			if (term.length() > 1) {
					
				boolean idSearch = isIDSearch(term);
				if(!idSearch) { 
				
				eos.log("Normal search");	
					
				// Activity
				ArrayList<com.eos.files.File> files = searchFiles(term);
				ArrayList<Article> articles = searchArticles(term);
				ArrayList<Tutorial> tuts = searchTutorials(term);
					res = new ResultObject(files, articles, tuts);

					trackSearch(term, files.size(), articles.size(), tuts.size());
				
				} else { 
					
					
					eos.log("ID Search");
					
					/** Get the string to search through for ID searches **/
					String initChar = term.substring(0,1).toLowerCase();
					String idterm = term.substring(1);
					
					eos.log("Searching:" + idterm);
					
					ArrayList<com.eos.files.File> files = new ArrayList<com.eos.files.File>();
					ArrayList<Article> articles = new ArrayList<Article>();
					ArrayList<Tutorial> tuts = new ArrayList<Tutorial>();
					
					if(initChar.equals("a")) { 
						 articles = findArticlesByID(idterm);
					} else if(initChar.equals("f")) { 
						 files = findFilesByID(idterm);
					} else { 
						 tuts = findTutorialsByID(idterm);
					}
					res = new ResultObject(files, articles, tuts);
					trackSearch(term, files.size(), articles.size(), tuts.size());
				}
			}

		}
		return res;
	}

	/**
	 * Search fulltext article descriptions
	 * @param term
	 * @return
	 */
	private ArrayList<Integer> searchArticleDescriptions(String term) {

		ArrayList<Integer> lst = new ArrayList<Integer>();
		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;

		try {

			s = c.createStatement();
			rs = s.executeQuery("select articleid from wisdom_articles where match(description) against('" + term
					+ "' WITH QUERY EXPANSION)");
			while (rs.next()) {
				lst.add(rs.getInt(1));
			}

		} catch (Exception e) {
			eos.log("Errors searching article titles. Err;" + e.toString(), "Searches", "searchArticleTitles", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return lst;

	}

	public ArrayList<Article> searchArticles(String term) {
		ArrayList<Article> lst = new ArrayList<Article>();
		term = com.eos.Eos.clean(term);
		if (term.length() > 1) {

			ArrayList<Integer> lstTitles = searchArticleTitles(term);
			ArrayList<Integer> lstDesc = searchArticleDescriptions(term);
			ArrayList<Integer> lstSections = searchArticleSections(term);
			ArrayList<Integer> lstTags = searchArticleTags(term);
			lst = deduplicatedArticles(lstTitles, lstDesc, lstSections, lstTags);

		}

		return lst;
	}

	private ArrayList<Integer> searchArticleSections(String term) {

		ArrayList<Integer> lst = new ArrayList<Integer>();
		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;

		try {

			s = c.createStatement();
			rs = s.executeQuery("select articleid from wisdom_article_sections where match(text1) against('" + term
					+ "' WITH QUERY EXPANSION)");
			while (rs.next()) {
				lst.add(rs.getInt(1));
			}

		} catch (Exception e) {
			eos.log("Errors searching article titles. Err;" + e.toString(), "Searches", "searchArticleTitles", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return lst;

	}

	private ArrayList<Integer> searchArticleTitles(String term) {

		ArrayList<Integer> lst = new ArrayList<Integer>();
		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;

		try {

			s = c.createStatement();
			rs = s.executeQuery("select articleid from wisdom_articles where match(title) against('" + term
					+ "' WITH QUERY EXPANSION)");
			while (rs.next()) {
				lst.add(rs.getInt(1));
			}

		} catch (Exception e) {
			eos.log("Errors searching article titles. Err;" + e.toString(), "Searches", "searchArticleTitles", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return lst;

	}

	private ArrayList<Integer> searchArticleTags(String term) {

		ArrayList<Integer> lst = new ArrayList<Integer>();
		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;

		try {

			s = c.createStatement();
			rs = s.executeQuery("select articleid from wisdom_articles where match(tags) against('" + term
					+ "' WITH QUERY EXPANSION)");
			while (rs.next()) {
				lst.add(rs.getInt(1));
			}

		} catch (Exception e) {
			eos.log("Errors searching article tags. Err;" + e.toString(), "Searches", "searchArticleTags", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return lst;

	}

	/**
	 * Search file descriptions
	 * 
	 * @param term
	 * @return
	 */
	private ArrayList<Integer> searchFileDescriptions(String term) {

		ArrayList<Integer> lst = new ArrayList<Integer>();
		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;

		try {

			s = c.createStatement();
			rs = s.executeQuery("select fileid from eos_files where match(description) against('" + term
					+ "' WITH QUERY EXPANSION)");
			while (rs.next()) {
				lst.add(rs.getInt(1));
			}

		} catch (Exception e) {
			eos.log("Errors searching file descriptions. Err;" + e.toString(), "Searches", "searchFileTitles", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return lst;

	}

	/**
	 * Search through title and description
	 * 
	 * @param term
	 * @return
	 */
	private ArrayList<com.eos.files.File> searchFiles(String term) {

		ArrayList<com.eos.files.File> lst = new ArrayList<com.eos.files.File>();
		ArrayList<Integer> lstTitles = new ArrayList<Integer>();
		ArrayList<Integer> lstDesc = new ArrayList<Integer>();
		ArrayList<Integer> lstTags = new ArrayList<Integer>();

		term = com.eos.Eos.clean(term);
		if (term.length() > 1) {

			lstTitles = searchFileTitles(term);
			lstDesc = searchFileDescriptions(term);
			lstTags = searchFileTags(term);
			lst = deduplicatedFiles(lstTitles, lstDesc, lstTags);

		}

		return lst;
	}

	/**
	 * Search titles
	 * 
	 * @param term
	 * @return
	 */
	private ArrayList<Integer> searchFileTitles(String term) {

		ArrayList<Integer> lst = new ArrayList<Integer>();
		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;

		try {

			s = c.createStatement();
			rs = s.executeQuery(
					"select fileid from eos_files where match(title) against('" + term + "' WITH QUERY EXPANSION)");
			while (rs.next()) {
				lst.add(rs.getInt(1));
			}

		} catch (Exception e) {
			eos.log("Errors searching file titles. Err;" + e.toString(), "Searches", "searchFileTitles", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return lst;

	}

	/**
	 * Search file tags
	 * 
	 * @param term
	 * @return
	 */
	private ArrayList<Integer> searchFileTags(String term) {

		ArrayList<Integer> lst = new ArrayList<Integer>();
		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;

		try {

			s = c.createStatement();
			rs = s.executeQuery(
					"select fileid from eos_files where match(tag) against('" + term + "' WITH QUERY EXPANSION)");
			while (rs.next()) {
				lst.add(rs.getInt(1));
			}

		} catch (Exception e) {
			eos.log("Errors searching file tags. Err;" + e.toString(), "Searches", "searchFileTags", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return lst;

	}

	private ArrayList<Integer> searchTutorialDescriptions(String term) {

		ArrayList<Integer> lst = new ArrayList<Integer>();
		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;

		try {

			s = c.createStatement();
			rs = s.executeQuery("select tutid from wisdom_tutorials where match(description) against('" + term
					+ "' WITH QUERY EXPANSION)");
			while (rs.next()) {
				lst.add(rs.getInt(1));
			}

		} catch (Exception e) {
			eos.log("Errors searching tutorial titles. Err;" + e.toString(), "Searches", "searchTutorialDescriptions",
					2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return lst;

	}

	public ArrayList<Tutorial> searchTutorials(String term) {
		ArrayList<Tutorial> lst = new ArrayList<Tutorial>();
		term = com.eos.Eos.clean(term);
		if (term.length() > 1) {
			ArrayList<Integer> lstTitles = searchTutorialTitles(term);
			ArrayList<Integer> lstDesc = searchTutorialDescriptions(term);
			ArrayList<Integer> lstSteps = searchTutorialSteps(term);
			lst = deduplicatedTutorials(lstTitles, lstDesc, lstSteps);

		}

		return lst;
	}

	private ArrayList<Integer> searchTutorialSteps(String term) {

		ArrayList<Integer> lst = new ArrayList<Integer>();
		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;

		try {

			s = c.createStatement();
			rs = s.executeQuery("select tutid from wisdom_tutorial_step where match(description) against('" + term
					+ "' WITH QUERY EXPANSION)");
			while (rs.next()) {
				lst.add(rs.getInt(1));
			}

		} catch (Exception e) {
			eos.log("Errors searching tutorial steps. Err;" + e.toString(), "Searches", "searchTutorialSteps", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return lst;

	}

	private ArrayList<Integer> searchTutorialTitles(String term) {

		ArrayList<Integer> lst = new ArrayList<Integer>();
		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;

		try {

			s = c.createStatement();
			rs = s.executeQuery("select tutid from wisdom_tutorials where match(title) against('" + term
					+ "' WITH QUERY EXPANSION)");
			while (rs.next()) {
				lst.add(rs.getInt(1));
			}

		} catch (Exception e) {
			eos.log("Errors searching tutorial titles. Err;" + e.toString(), "Searches", "searchTutorialTitles", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

		return lst;

	}

	/**
	 * Tracks that someone searched.
	 * 
	 * @param term
	 */
	private void trackSearch(String term, int filesize, int articlesize, int tutsize) {
		if (eos.active()) {

			term = com.eos.Eos.clean(term);
			if (term.length() > 1) {

				Connection c = eos.c();
				Statement s = null;

				try {

					s = c.createStatement();

					int eid = eos.account().eid();
					int uid = eos.user().getUserId();
					int aid = eos.user().getAccountId();
					int cid = eos.user().getCompanyId();

					String today = com.eos.utils.Calendar.getTodayForSQL();

					String sql = "insert into wisdom_searches values(null," + eid + "," + aid + "," + cid + "," + uid
							+ ",'" + today + "',null,'" + term + "'," + filesize + "," + articlesize + "," + tutsize
							+ ")";
					s.execute(sql);

				} catch (Exception e) {
					eos.log("Errors tracking search. Err:" + e.toString(), "Searches", "trackSearch", 2);
				} finally {
					eos.cleanup(c, s);
				}
			}

		}
	}

}
