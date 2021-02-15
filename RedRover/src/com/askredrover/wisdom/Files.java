package com.askredrover.wisdom;

import com.eos.Eos;
import com.eos.files.File;

import java.util.ArrayList;

public class Files {
	
	private Eos eos = null;
	
	public Files(Eos eos)
	{
		this.eos = eos;
	}
	
	/**
	 * Check for category and return parsed list.
	 * @param category
	 * @param lst
	 * @return
	 */
	private ArrayList<com.eos.files.File> filterCategories(int iCat, ArrayList<com.eos.files.File> lst) { 
		int size = lst.size();
		ArrayList<com.eos.files.File> rlist = new ArrayList<com.eos.files.File>();
		for(int i = 0; i < size; i++) {
			com.eos.files.File f = (com.eos.files.File)lst.get(i);
			if(f.categoryid()==iCat) { 
				rlist.add(f);
			}
		}
		return rlist;
	}
	
	private ArrayList<com.eos.files.File> filterStores(int iStore, ArrayList<com.eos.files.File> lst) { 
		int size = lst.size();
		ArrayList<com.eos.files.File> rlist = new ArrayList<com.eos.files.File>();
		for(int i = 0; i < size; i++) {
			com.eos.files.File f = (com.eos.files.File)lst.get(i);
			if(f.companyid()==iStore) { 
				rlist.add(f);
			}
		}
		return rlist;
	}
	
	
	/**
	 * Gets list of all files and then determines whether to apply filters.
	 * @param category
	 * @param store
	 * @return ArrayList of Files.
	 */
	public ArrayList<com.eos.files.File> accountFiles(String category, String store) { 
	  
		ArrayList<com.eos.files.File>  lst1 = new ArrayList<com.eos.files.File>();
		ArrayList<com.eos.files.File>  lstFinal = new ArrayList<com.eos.files.File>();
		
		ArrayList<com.eos.files.File> initList = eos.files().getAccountFiles(); 
		
		int iCat = eos.d(category);
		int iStore = eos.d(store);
		
		if(iCat>0) { 
			lst1 = filterCategories(iCat,initList);
			if(iStore>0) { 
				lstFinal = filterStores(iStore,lst1); 
			} else { lstFinal = lst1; }
		} else { 
			if(iStore>0) { 
			lstFinal = filterStores(iStore,initList);
			} else { 
				lstFinal = initList;
			}
		}
		
		
		return lstFinal;
	}
}
