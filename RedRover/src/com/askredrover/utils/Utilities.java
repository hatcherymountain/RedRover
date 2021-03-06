package com.askredrover.utils;

import com.eos.Eos;
import java.util.ArrayList;
import com.eos.accounts.Company;

public class Utilities {

	private Eos eos = null;

	public Utilities(Eos eos) {
		this.eos = eos;
	}

	/**
	 * Get stores as a selection.
	 * 
	 * @return
	 */
	public String storesAsSelection() {
		StringBuffer s = new StringBuffer();
		ArrayList<Company> stores = eos.accounts().getCompanies(eos.accountid(), false);
		int ssize = stores.size();
		for (int i = 0; i < ssize; i++) {
			Company c = (Company) stores.get(i);
			String cid = eos.e(c.getCID());
			s.append("<option value=" + cid + ">" + c.getName() + "</optjon>");
		}
		return s.toString();
	}

	/**
	 * Given a phone number return it in a formatted way.
	 * 
	 * @return
	 */
	public static String phoneFormatted(String phone) {
		if (phone.length() > 0) {
			String number = phone.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
			return number;
		} else {
			return "";
		}
	}

}
