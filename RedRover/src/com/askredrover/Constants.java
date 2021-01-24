package com.askredrover;
import java.util.ArrayList;
import com.eos.accounts.User;

public class Constants {

	public static final String TESTFLOW_KEYPHRASE = "test";
	
	public static final ArrayList<String> letters()
	{
		
		ArrayList<String> l = new ArrayList<String>();
		l.add("A");
		l.add("B");
		l.add("C");
		l.add("D");
		l.add("E");
		l.add("F");
		l.add("G");
		l.add("H");
		l.add("I");
		l.add("J");
		l.add("K");
		l.add("L");
		l.add("M");
		l.add("N");
		l.add("O");
		l.add("P");
		l.add("Q");
		l.add("R");
		l.add("S");
		l.add("T");
		l.add("U");
		l.add("V");
		l.add("W");
		l.add("X");
		l.add("Y");
		l.add("Z");
		return l;
	}
	
	/**
	 * Given a firstname initial, can we return users that fit that letter?
	 * @param letter
	 * @param lst
	 * @return
	 */
	public static ArrayList<com.eos.accounts.User> getUserFromInitialLetter(String letter, ArrayList<com.eos.accounts.User> lst) 
	{
		ArrayList<User> newlst = new ArrayList<User>();
		int size = lst.size();
		
		
		for(int i = 0; i < size; i++) {
			User user = (User)lst.get(i);
			if(user.initChar().equalsIgnoreCase(letter)) { 
				newlst.add(user);
			}
		}
		
		return newlst;
	}
	
}
