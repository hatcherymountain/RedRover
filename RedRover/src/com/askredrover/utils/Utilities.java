package com.askredrover.utils;

public class Utilities {
	
	
	/**
	 * Given a phone number return it in a formatted way.
	 * @return
	 */
	public static  String phoneFormatted(String phone) {
		if (phone.length() > 0) {
			String number = phone.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "($1) $2-$3");
			return number;
		} else {
			return "";
		}
	}
	
	
	
}
