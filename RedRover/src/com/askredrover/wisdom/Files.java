package com.askredrover.wisdom;

import com.eos.Eos;
import com.eos.files.File;
import java.sql.*;
import java.util.ArrayList;

public class Files {

	private Eos eos = null;

	public Files(Eos eos) {
		this.eos = eos;
	}

	/**
	 * Check for category and return parsed list.
	 * 
	 * @param category
	 * @param lst
	 * @return
	 */
	private ArrayList<com.eos.files.File> filterCategories(int iCat, ArrayList<com.eos.files.File> lst) {
		int size = lst.size();
		ArrayList<com.eos.files.File> rlist = new ArrayList<com.eos.files.File>();
		for (int i = 0; i < size; i++) {
			com.eos.files.File f = (com.eos.files.File) lst.get(i);
			if (f.categoryid() == iCat) {
				rlist.add(f);
			}
		}
		return rlist;
	}

	private ArrayList<com.eos.files.File> filterStores(int iStore, ArrayList<com.eos.files.File> lst) {
		int size = lst.size();
		ArrayList<com.eos.files.File> rlist = new ArrayList<com.eos.files.File>();
		for (int i = 0; i < size; i++) {
			com.eos.files.File f = (com.eos.files.File) lst.get(i);
			if (f.companyid() == iStore) {
				rlist.add(f);
			}
		}
		return rlist;
	}

	public void updateFile(String fileid) {

	}

	/**
	 * Will work in correspondence with eos.files removal, but for now we just
	 * remove from the db.
	 * 
	 * @param fileid
	 */
	public void removeFile(String fileid) {
		if (eos.isContributor() || eos.isAdmin()) {
			Connection c = eos.c();
			Statement s = null;
			try {
				int fid = eos.d(fileid);
				s = c.createStatement();
				String sql = "delete from eos_files where fileid=" + fid + "";
				s.execute(sql);
				eos.audit().audit("Delete file " + fid + "", "files", "removeFile", "remove");

			} catch (Exception e) {
				eos.log("Errors removing a Wisdom file. Err: " + e.toString(), "Files", "removeFile", 2);
			} finally {
				eos.cleanup(c, s);
			}
		}
	}

	/**
	 * Updates the file metadata.
	 */
	public void update(javax.servlet.http.HttpServletRequest r) {
		if (eos.isAdmin() || eos.isContributor()) {
			Connection c = eos.c();
			Statement s = null;

			try {

				s = c.createStatement();

				int id = eos.d(r.getParameter("fid"));

				if (id > 0) {

					/** Get the values **/
					String storeid = r.getParameter("companyid");
					int sid = eos.d(storeid);
					String catid = r.getParameter("catid");
					int cid = eos.d(catid);
					String title = r.getParameter("title");
					title = com.eos.Eos.clean(title);
					String desc = r.getParameter("description");
					desc = com.eos.Eos.clean(desc);
					String tag = r.getParameter("tag");
					tag = com.eos.Eos.clean(tag);
					tag = com.eos.utils.Strings.absoluteTruncation(tag, 50);
					String status = r.getParameter("status");
					int iStatus = com.eos.utils.Strings.getIntFromString(status);
					String roleid = r.getParameter("roleid");
					int rid = eos.d(roleid);

					/** updates in batch form **/
					s.addBatch("update eos_files set companyid=" + sid + " where fileid=" + id + "");
					s.addBatch("update eos_files set categoryid=" + cid + " where fileid=" + id + "");
					s.addBatch("update eos_files set title='" + title + "' where fileid=" + id + "");
					s.addBatch("update eos_files set description='" + desc + "' where fileid=" + id + "");
					s.addBatch("update eos_files set tag='" + tag + "' where fileid=" + id + "");
					s.addBatch("update eos_files set status=" + iStatus + " where fileid=" + id + "");
					s.addBatch("update eos_files set roleid=" + rid + " where fileid=" + id + "");

					/** Do the update **/
					s.executeBatch();
					eos.audit().audit("Updated file with id:" + id + "", "update", "files", "update");
				}

			} catch (Exception e) {
				eos.log("Errors updating file metadata. Err:" + e.toString(), "Files", "update", 2);
			} finally {
				eos.cleanup(c, s);
			}
		}
	}

	/**
	 * Looking for recent files~!
	 * 
	 * @param category
	 * @param store
	 * @return
	 */
	public ArrayList<com.eos.files.File> recentAccountFiles(String category, String store) {

		ArrayList<com.eos.files.File> lst1 = new ArrayList<com.eos.files.File>();
		ArrayList<com.eos.files.File> lstFinal = new ArrayList<com.eos.files.File>();
		ArrayList<com.eos.files.File> lstRecentFinal = new ArrayList<com.eos.files.File>();

		ArrayList<com.eos.files.File> initList = eos.files().getAccountFiles();

		int iCat = eos.d(category);
		int iStore = eos.d(store);

		if (iCat > 0) {
			lst1 = filterCategories(iCat, initList);
			if (iStore > 0) {
				lstFinal = filterStores(iStore, lst1);
			} else {
				lstFinal = lst1;
			}
		} else {
			if (iStore > 0) {
				lstFinal = filterStores(iStore, initList);
			} else {
				lstFinal = initList;
			}
		}

		for (int y = 0; y < lstFinal.size(); y++) {
			com.eos.files.File _f = (com.eos.files.File) lstFinal.get(y);
			int age = com.eos.utils.Calendar.calculateAge(eos.date(_f.added()));
			if (age <= com.askredrover.Constants.RECENT_PARAMETER) {
				lstRecentFinal.add(_f);
			}
		}

		return lstRecentFinal;
	}

	/**
	 * Does THIS person who is active pass the check in security?
	 * 
	 * @param File
	 * @return boolean
	 */
	public boolean passSecurityCheck(File file) {
		boolean passes = false;

		if (eos.active()) {

			if (eos.isAdmin()) {

				passes = true;

			} else {

				if (eos.user().getRole() <= file.roleid()) {
					passes = true;
				}

			}

		}

		return passes;
	}

	/**
	 * Is this person in the store if it is designated as such?
	 * 
	 * @param storeid
	 * @return
	 */
	private boolean isInStore(int storeid) {
		boolean is = false;

		if (eos.active()) {

			if (eos.user().getCompanyId() == storeid) {
				is = true;
			}

		}

		return is;
	}

	/**
	 * Get files for a category.
	 * @param category
	 * @return ArrayList<com.eos.files.File>
	 */
	public ArrayList<com.eos.files.File> fileForUsers(String category) {

		ArrayList<com.eos.files.File> initList = eos.files().getAccountFilesWithCategory();
		int size = initList.size();
		ArrayList<com.eos.files.File> lst = new ArrayList<com.eos.files.File>();
		int iCat = eos.d(category);

		for (int x = 0; x < size; x++) {

			com.eos.files.File f = (com.eos.files.File) initList.get(x);

			if (f.active() && f.categoryid() == iCat) {

				if (passSecurityCheck(f)) {

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

		return lst;

	}

	/**
	 * Gets list of all files and then determines whether to apply filters.
	 * 
	 * @param category
	 * @param store
	 * @return ArrayList of Files.
	 */
	public ArrayList<com.eos.files.File> accountFiles(String category, String store) {

		ArrayList<com.eos.files.File> lst1 = new ArrayList<com.eos.files.File>();
		ArrayList<com.eos.files.File> lstFinal = new ArrayList<com.eos.files.File>();

		ArrayList<com.eos.files.File> initList = eos.files().getAccountFilesWithCategory(); // We want files with a
																							// category ONLY

		int iCat = eos.d(category);
		int iStore = eos.d(store);

		if (iCat > 0) {
			lst1 = filterCategories(iCat, initList);
			if (iStore > 0) {
				lstFinal = filterStores(iStore, lst1);
			} else {
				lstFinal = lst1;
			}
		} else {
			if (iStore > 0) {
				lstFinal = filterStores(iStore, initList);
			} else {
				lstFinal = initList;
			}
		}

		return lstFinal;
	}
}
