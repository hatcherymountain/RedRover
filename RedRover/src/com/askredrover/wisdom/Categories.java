package com.askredrover.wisdom;

import com.eos.Eos;
import java.sql.*;
import java.util.ArrayList;
import com.askredrover.RedRover;

public class Categories {

	private RedRover rr = null;
	private Eos eos = null;
	private ArrayList<Category> cats = null;

	private void reset() {
		cats.clear();
		loadEnterpriseCategories();
	}

	public Categories(Eos eos, RedRover rr) {
		this.eos = eos;
		this.rr = rr;
		if (eos.active()) {
			loadEnterpriseCategories();
		} else {
			cats = new ArrayList<Category>();
		}
	}

	public ArrayList<Category> categories() {
		return cats;
	}

	/**
	 * Adds a new category
	 * 
	 * @param pid
	 * @param name
	 * @param desc
	 */
	public void add(String pid, String name, String desc, String sibling) {

		if (rr.admin()) {

			Connection c = eos.c();
			Statement s = null;

			try {
				if (name.length() > 0) {

					int eid = eos.account().eid();
					int id = eos.d(pid);
					desc = com.eos.Eos.clean(desc);
					int siblingid = eos.d(sibling);
					s = c.createStatement();

					String sql = "insert into wisdom_categories values(null," + eid + ",'" + name + "','" + desc + "',"
							+ id + "," + siblingid + ")";

					s.execute(sql);

				}

			} catch (Exception e) {
				eos.log("Errors adding new category. Err:" + e.toString(), "Categories", "add", 2);
			} finally {
				eos.cleanup(c, s);
				reset();
			}
		} else {
			System.out.println("not an admin");

		}
	}

	public void updateDescription(String catid, String value) {
		if (eos.isAdmin()) {
			Connection c = eos.c();
			Statement s = null;
			try {

				s = c.createStatement();
				int id = eos.d(catid);
				value = com.eos.Eos.clean(value);

				String sql = "update wisdom_categories set description='" + value + "' where catid=" + id + "";
				s.execute(sql);

			} catch (Exception e) {
				eos.log("Errors updating title. Err:" + e.toString(), "Categories", "updateTitle", 2);
			} finally {
				eos.cleanup(c, s);
				reset();
			}
		}
	}

	public void updateTitle(String catid, String value) {
		if (eos.isAdmin()) {
			Connection c = eos.c();
			Statement s = null;
			try {

				s = c.createStatement();
				int id = eos.d(catid);
				value = com.eos.Eos.clean(value);

				String sql = "update wisdom_categories set category='" + value + "' where catid=" + id + "";
				s.execute(sql);

			} catch (Exception e) {
				eos.log("Errors updating title. Err:" + e.toString(), "Categories", "updateTitle", 2);
			} finally {
				eos.cleanup(c, s);
				reset();
			}
		}
	}

	/**
	 * How many files are associated with a category?
	 * 
	 * @param catid
	 * @return
	 */
	public int countFiles(String catid) {
		int count = 0;

		if (eos.active()) {

			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				s = c.createStatement();
				int cid = eos.d(catid);
				String sql = "select count(*) from eos_files where categoryid=" + cid + " and status=1";

				rs = s.executeQuery(sql);
				while (rs.next()) {
					count = rs.getInt(1);
				}

			} catch (Exception e) {
				eos.log("Errors counting category files. Err:" + e.toString(), "Categories", "countFiles", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}

		}

		return count;
	}

	public boolean canDelete(String catid) {
		boolean can = false;

		if (eos.isAdmin()) {
			int ct = countTutorials(catid);
			int ca = countArticles(catid);
			int cf = countFiles(catid);
			int t = ct + ca + cf;
			if (t == 0) {
				can = true;
			}
		}

		return can;
	}

	/**
	 * Get all counts and roll up.
	 * 
	 * @param Encoded parent identifier.
	 * @return int total count
	 */
	public int totalArticleCount(String parentid) {
		int total = 0;
		int iPid = eos.d(parentid);
		total = countArticles(parentid); // this gets # to the parent. Typically 0.

		/** Get siblings for parent **/
		ArrayList<Category> kids = children(iPid);
		int csize = kids.size();
		for (int i = 0; i < csize; i++) {
			Category kid = (Category) kids.get(i);
			String kidid = eos.e(kid.catid());
			total = total + countArticles(kidid);

			/** Siblings? **/
			ArrayList<Category> siblings = siblings(kid.catid());
			int ssize = siblings.size();
			for (int y = 0; y < ssize; y++) {
				Category sib = (Category) siblings.get(y);
				String sibid = eos.e(sib.catid());
				total = total + countArticles(sibid);
			}
		}
		return total;
	}

	/**
	 * Get all file counts and roll up
	 * 
	 * @param parentid
	 * @return int total count
	 */
	public int totalTutorialCount(String parentid) {
		int total = 0;
		int iPid = eos.d(parentid);
		total = countTutorials(parentid); // this gets # to the parent. Typically 0.

		/** Get siblings for parent **/
		ArrayList<Category> kids = children(iPid);
		int csize = kids.size();
		for (int i = 0; i < csize; i++) {
			Category kid = (Category) kids.get(i);
			String kidid = eos.e(kid.catid());
			total = total + countTutorials(kidid);

			/** Siblings? **/
			ArrayList<Category> siblings = siblings(kid.catid());
			int ssize = siblings.size();
			for (int y = 0; y < ssize; y++) {
				Category sib = (Category) siblings.get(y);
				String sibid = eos.e(sib.catid());
				total = total + countTutorials(sibid);
			}
		}
		return total;
	}

	/**
	 * Get all file counts and roll up
	 * 
	 * @param parentid
	 * @return int total count
	 */
	public int totalFileCount(String parentid) {
		int total = 0;
		int iPid = eos.d(parentid);
		total = countFiles(parentid); // this gets # to the parent. Typically 0.

		/** Get siblings for parent **/
		ArrayList<Category> kids = children(iPid);
		int csize = kids.size();
		for (int i = 0; i < csize; i++) {
			Category kid = (Category) kids.get(i);
			String kidid = eos.e(kid.catid());
			total = total + countFiles(kidid);

			/** Siblings? **/
			ArrayList<Category> siblings = siblings(kid.catid());
			int ssize = siblings.size();
			for (int y = 0; y < ssize; y++) {
				Category sib = (Category) siblings.get(y);
				String sibid = eos.e(sib.catid());
				total = total + countFiles(sibid);
			}
		}
		return total;
	}

	/**
	 * Total number of items
	 * 
	 * @param catid
	 * @return
	 */
	public int totalItems(String catid) {
		int t = 0;

		int ct = countTutorials(catid);
		int ca = countArticles(catid);
		int cf = countFiles(catid);
		t = ct + ca + cf;
		return t;
	}

	public void remove(String catid) {
		if (canDelete(catid)) {
			if (eos.isAdmin()) {
				Connection c = eos.c();
				Statement s = null;
				try {
					s = c.createStatement();
					int id = eos.d(catid);
					String sql = "delete from wisdom_categories where catid=" + id + "";
					s.execute(sql);

				} catch (Exception e) {
					eos.log("Errors removing a category. Err;" + e.toString(), "Categories", "remove", 2);
				} finally {
					eos.cleanup(c, s);
					reset();
				}

			}
		}
	}

	public int countTutorials(String catid) {
		int count = 0;

		if (eos.active()) {

			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				s = c.createStatement();
				int cid = eos.d(catid);
				String sql = "select count(*) from wisdom_tutorials where categoryid=" + cid + "";
				rs = s.executeQuery(sql);
				while (rs.next()) {
					count = rs.getInt(1);
				}

			} catch (Exception e) {
				eos.log("Errors counting category tutorials. Err:" + e.toString(), "Categories", "countTutorials", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}

		}

		return count;
	}

	/**
	 * Articles Count
	 * 
	 * @param catid
	 * @return
	 */
	public int countArticles(String catid) {
		int count = 0;

		if (eos.active()) {

			Connection c = eos.c();
			Statement s = null;
			ResultSet rs = null;

			try {

				s = c.createStatement();
				int cid = eos.d(catid);
				String sql = "select count(*) from wisdom_articles where categoryid=" + cid + " and status=2";
				rs = s.executeQuery(sql);
				while (rs.next()) {
					count = rs.getInt(1);
				}

			} catch (Exception e) {
				eos.log("Errors counting category articles. Err:" + e.toString(), "Categories", "countArticles", 2);
			} finally {
				eos.cleanup(c, s, rs);
			}

		}

		return count;
	}

	/**
	 * Get parent categories.
	 * 
	 * @return
	 */
	public ArrayList<Category> parentCategories() {
		ArrayList<Category> lst = new ArrayList<Category>();
		int size = cats.size();
		for (int i = 0; i < size; i++) {
			Category category = (Category) cats.get(i);
			if (category.parentid() == 0 && category.siblingid() == 0) {
				lst.add(category);
			}
		}
		return lst;
	}

	public Category category(String cid) {
		return category(eos.d(cid));
	}

	/**
	 * Get a specific category
	 * 
	 * @param cid
	 * @return
	 */
	private Category category(int cid) {
		Category c = null;

		int size = cats.size();
		for (int i = 0; i < size; i++) {
			Category category = (Category) cats.get(i);
			if (category.catid() == cid) {
				c = category;
				break;
			}
		}

		return c;
	}

	/**
	 * Child categories.
	 * 
	 * @param pid
	 * @return
	 */
	public ArrayList<Category> children(int pid) {
		ArrayList<Category> lst = new ArrayList<Category>();
		int size = cats.size();
		for (int i = 0; i < size; i++) {
			Category category = (Category) cats.get(i);
			if (category.parentid() == pid && category.siblingid() == 0) {
				lst.add(category);
			}
		}
		return lst;
	}

	public ArrayList<Category> siblings(int catid) {
		ArrayList<Category> lst = new ArrayList<Category>();
		int size = cats.size();
		for (int i = 0; i < size; i++) {
			Category category = (Category) cats.get(i);
			if (category.siblingid() == catid && category.parentid() == 0) {
				lst.add(category);
			}
		}
		return lst;
	}

	/**
	 * Typically will load Cats for the account UNLESS the account is < 100 and then
	 * we will reserve for Pointr and Dee
	 */
	private void loadEnterpriseCategories() {
		cats = new ArrayList<Category>();

		Connection c = eos.c();
		Statement s = null;
		ResultSet rs = null;

		try {

			s = c.createStatement();
			int eid = eos.account().eid();
			String sql = "select catid,category,description,parentid,siblingid from wisdom_categories where eid=" + eid
					+ " order by category asc";

			rs = s.executeQuery(sql);

			while (rs.next()) {
				int cid = rs.getInt(1);
				String cat = rs.getString(2);
				String desc = rs.getString(3);
				int pid = rs.getInt(4);
				int sid = rs.getInt(5);
				Category category = new CategoryObject(cid, eid, cat, desc, pid, sid);
				cats.add(category);
			}

		} catch (Exception e) {
			eos.log("Errors getting enterprise categories. Err:" + e.toString(), "Categories",
					"loadEnterpriseCategories", 2);
		} finally {
			eos.cleanup(c, s, rs);
		}

	}

	public String categorySelection() {
		return categorySelection(eos.e(0));
	}

	public String categorySelection(String catid) {
		return categorySelection(eos.d(catid));
	}

	/**
	 * Get a SELECT list of ALL categories including SIBLINGs and return an option
	 * list.
	 * 
	 * @return
	 */
	private String categorySelection(int catid) {
		StringBuffer sb = new StringBuffer();

		/** Get Parents **/

		ArrayList<Category> parents = this.parentCategories();
		int psize = parents.size();

		for (int p = 0; p < psize; p++) {
			Category parent = (Category) parents.get(p);
			String pcid = eos.e(parent.catid());

			ArrayList<Category> kids = this.children(parent.catid());
			int ksize = kids.size();

			if (parent.catid() == catid) {
				sb.append("<option selected value=\"" + pcid + "\">" + parent.category() + "</option>");
			} else {
				sb.append("<option value=\"" + pcid + "\">" + parent.category() + "</option>");
			}

			if (ksize > 0) {
				for (int k = 0; k < ksize; k++) {
					Category kid = (Category) kids.get(k);
					String kcid = eos.e(kid.catid());
					ArrayList<Category> siblings = this.siblings(kid.catid());
					int sibsize = siblings.size();

					if (kid.catid() == catid) {
						sb.append("<option selected value=\"" + kcid + "\">----" + kid.category() + "</option>");
					} else {
						sb.append("<option value=\"" + kcid + "\">----" + kid.category() + "</option>");
					}
					if (sibsize > 0) {

						for (int s = 0; s < sibsize; s++) {
							Category sibling = (Category) siblings.get(s);
							String scid = eos.e(sibling.catid());
							if (sibling.catid() == catid) {
								sb.append("<option selected value=\"" + scid + "\">-----------" + sibling.category()
										+ "</option>");
							} else {
								sb.append("<option value=\"" + scid + "\">-----------" + sibling.category()
										+ "</option>");
							}
						}
					}

				}
			}

		}

		return sb.toString();
	}

}
