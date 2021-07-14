package com.askredrover;

import java.util.ArrayList;
import com.eos.accounts.User;

public class Constants {

	public static final int ID_FILE = 0;
	public static final int ID_ARTICLE = 1;
	public static final int ID_TUTORIAL = 2;

	public static final int RECENT_PARAMETER = 14;
	public static final String TESTFLOW_KEYPHRASE = "test";

	public static final String[] VENTURE_ROLES = { "User", "Editor", "Project Manager", "Owner" };

	public static final int ROLE_USER = 0;
	public static final int ROLE_EDITOR = 1;
	public static final int ROLE_PROJECT_MANAGER = 2;
	public static final int ROLE_OWNER = 3;

	public static final String[] VENTURE_STATES = { "Developing", "Active", "Paused", "Archived" };
	public static final int VENTURE_STATE_ARCHIVED = 3;
	public static final String[] VENTURE_STATE_COLORS = { "warning", "success", "info", "danger" };

	public static final String[] EVENT_STATES = { "Inactive", "Active", "Completed" };

	public static final String[] EVENT_PRIORITIES = { "Low", "Medium", "High", "Urgent" };
	public static final String[] EVENT_PRIORITY_COLORS = { "secondary", "info", "warning", "danger" };

	public static final int EVENT_STATE_INACTIVE = 0;
	public static final int EVENT_STATE_ACTIVE = 1;
	public static final int EVENT_STATE_COMPLETED = 2;

	public static final String[] ARTICLE_STATES = { "Editing", "Review", "Live", "Archived" };
	public static final String[] ARTICLE_STATE_COLORS = { "warning", "info", "success", "danger" };

	public static final String[] TUTORIAL_STATES = { "Editing", "Review", "Live", "Archived" };
	public static final String[] TUTORIAL_STATE_COLORS = { "warning", "info", "success", "danger" };

	public static final String[] MEETING_TYPES = { "Weekly", "One off", "Urgent" };

	public static final String[] SOW_STATES = { "In Review", "Active", "Paused", "Completed" };
	public static final String[] SOW_STATE_COLORS = { "warning", "success", "info", "danger" };

	public static final String[] SOW_SENTIMENTS = { "Fantastic", "Great", "Good", "Average", "Could be better",
			"Not good", "Awful" };

	public static final String[] SOW_SENTIMENT_COLORS = { "success", "primary", "info", "muted", "secondary", "warning",
			"danger" };

	public static final String[] ACTIVITY_PRIORITIES = { "General", "Important" };

	public static final String[] BOOKMARK_TYPES = { "File", "Article", "Tutorial" };

	public static final int BOOKMARK_FILE = 0;

	public static final int BOOKMARK_ARTICLE = 1;

	public static final int BOOKMARK_TUTORIAL = 2;

	public static final String[] colors = { "charcoal", "crimson", "maroon", "stone", "tangerine", "red", "hatchery",
			"blue" };

	public static final String[] VENTURE_SENTIMENTS = { "Fantastic", "Great", "Good", "Average", "Could be better",
			"Not good", "Awful" };
	public static final String[] VENTURE_SENTIMENT_COLORS = { "success", "primary", "info", "muted", "secondary",
			"warning", "danger" };
	public static final int VENTURE_SENTIMENT_GOOD = 2;
	/** User Properties **/
	public static final String PROFILE_CITY = "city";

	public static final String PROFILE_COUNTRY = "country";

	public static final String PROFILE_SKILLS = "skills";

	public static final String PROFILE_POSITION = "position";
	public static final String PROFILE_GENDER = "gender";

	public static final String[] GENDERS = { "Undefined", "Male", "Female", "Other" };

	/**
	 * Article states as an HTML Selection list.
	 * 
	 * @param current
	 * @return
	 */
	public static String articleStatesAsSelection(int current) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < ARTICLE_STATES.length; i++) {
			String p = (String) ARTICLE_STATES[i];
			if (i == current) {
				sb.append("<option selected value=" + i + ">" + p + "</option>");
			} else {
				sb.append("<option value=" + i + ">" + p + "</option>");
			}
		}

		return sb.toString();
	}

	public static String articleStatusAsString(int status) {
		String s = null;
		try {
			s = (String) ARTICLE_STATES[status];
		} catch (Exception e) {
			s = (String) ARTICLE_STATES[0];
		}
		return s;
	}

	public static String articleStatusColorAsString(int status) {
		String s = null;
		try {
			s = (String) ARTICLE_STATE_COLORS[status];
		} catch (Exception e) {
			s = (String) ARTICLE_STATE_COLORS[0];
		}
		return s;
	}

	/**
	 * HTML Event Priorities as HTML select list.
	 * 
	 * @param current
	 * @return
	 */
	public static String eventPrioritiesAsSelection(int current) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < EVENT_PRIORITIES.length; i++) {
			String p = (String) EVENT_PRIORITIES[i];
			if (i == current) {
				sb.append("<option selected value=" + i + ">" + p + "</option>");
			} else {
				sb.append("<option value=" + i + ">" + p + "</option>");
			}
		}

		return sb.toString();
	}

	/**
	 * What is the priority of an event
	 * 
	 * @param status
	 * @return
	 */
	public static String eventPriorityAsString(int status) {
		String s = null;
		try {
			s = (String) EVENT_PRIORITIES[status];
		} catch (Exception e) {
			s = (String) EVENT_PRIORITIES[0];
		}
		return s;
	}

	public static String eventPriorityColorAsString(int priority) {
		String s = null;
		try {
			s = (String) EVENT_PRIORITY_COLORS[priority];
		} catch (Exception e) {
			s = (String) EVENT_PRIORITY_COLORS[0];
		}
		return s;
	}

	/**
	 * Event states as an HTML Selection list.
	 * 
	 * @param current
	 * @return
	 */
	public static String eventStatesAsSelection(int current) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < EVENT_STATES.length; i++) {
			String p = (String) EVENT_STATES[i];
			if (i == current) {
				sb.append("<option selected value=" + i + ">" + p + "</option>");
			} else {
				sb.append("<option value=" + i + ">" + p + "</option>");
			}
		}

		return sb.toString();
	}

	public static String eventStatusAsString(int status) {
		String s = null;
		try {
			s = (String) EVENT_STATES[status];
		} catch (Exception e) {
			s = (String) EVENT_STATES[0];
		}
		return s;
	}

	public static final String getGender(int current) {
		String g = null;

		for (int i = 0; i < GENDERS.length; i++) {
			String _g = (String) GENDERS[i];
			if (i == current) {
				g = _g;
				break;
			}
		}

		if (g == null) {
			g = (String) GENDERS[0];
		}
		return g;

	}

	public static final int getGenderAsInt(int current) {
		int iG = 0;

		for (int i = 0; i < GENDERS.length; i++) {
			if (i == current) {
				iG = i;
				break;
			}
		}

		return iG;

	}

	/**
	 * Get genders as a selection list.
	 * 
	 * @param current
	 * @return
	 */
	public static String getGendersAsSelection(int current) {
		StringBuffer sb = new StringBuffer();

		for (int x = 0; x < GENDERS.length; x++) {
			String s = (String) GENDERS[x];
			if (x == current) {
				sb.append("<option selected value=" + x + ">" + s + "</option>");
			} else {
				sb.append("<option  value=" + x + ">" + s + "</option>");
			}
		}

		return sb.toString();
	}

	/**
	 * Get Support Type
	 * 
	 * @param id
	 * @return
	 */
	public static com.askredrover.support.SupportType getSupportType(int id) {
		com.askredrover.support.SupportType s = null;
		ArrayList<com.askredrover.support.SupportType> types = loadSupportTypes();
		int size = types.size();

		for (int i = 0; i < size; i++) {
			com.askredrover.support.SupportType st = (com.askredrover.support.SupportType) types.get(i);
			if (st.id() == id) {
				s = st;
				break;
			}
		}

		return s;
	}

	/**
	 * Gets the support type as a String value
	 * 
	 * @param id
	 * @return
	 */
	public static String getSupportTypeAsString(int id) {
		String s = null;
		ArrayList<com.askredrover.support.SupportType> types = loadSupportTypes();
		int size = types.size();

		for (int i = 0; i < size; i++) {
			com.askredrover.support.SupportType st = (com.askredrover.support.SupportType) types.get(i);
			if (st.id() == id) {
				s = st.value();
			}
		}

		return s;
	}

	/**
	 * Given a firstname initial, can we return users that fit that letter?
	 * 
	 * @param letter
	 * @param lst
	 * @return
	 */
	public static ArrayList<com.eos.accounts.User> getUserFromInitialLetter(String letter,
			ArrayList<com.eos.accounts.User> lst) {
		ArrayList<User> newlst = new ArrayList<User>();
		int size = lst.size();

		for (int i = 0; i < size; i++) {
			User user = (User) lst.get(i);
			if (user.initChar().equalsIgnoreCase(letter)) {
				newlst.add(user);
			}
		}

		return newlst;
	}

	/**
	 * Get the venture roles as a selection list.
	 * 
	 * @param selected
	 * @return
	 */
	public static String getVentureRolesAsString(int selected) {
		StringBuffer sb = new StringBuffer();

		int size = VENTURE_ROLES.length;
		for (int i = 0; i < size; i++) {
			String _r = (String) VENTURE_ROLES[i];
			if (selected == i) {
				sb.append("<option selected value=" + i + ">" + _r + "</option>");
			} else {
				sb.append("<option value=" + i + ">" + _r + "</option>");
			}
		}

		return sb.toString();
	}

	public static final ArrayList<String> letters() {

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

	private static ArrayList<com.askredrover.support.SupportType> loadSupportTypes() {
		ArrayList<com.askredrover.support.SupportType> lst = new ArrayList<com.askredrover.support.SupportType>();

		lst.add(new com.askredrover.support.SupportType(1, "Add Something New", 0));
		lst.add(new com.askredrover.support.SupportType(2, "Bug", 0));
		lst.add(new com.askredrover.support.SupportType(3, "Cannot Find Something", 0));
		lst.add(new com.askredrover.support.SupportType(4, "Password Problems", 0));
		lst.add(new com.askredrover.support.SupportType(5, "UI/UX Problem", 0));

		// Feedback Start ID at 50

		lst.add(new com.askredrover.support.SupportType(51, "Improvement Needed", 1));
		lst.add(new com.askredrover.support.SupportType(52, "Performance", 1));
		lst.add(new com.askredrover.support.SupportType(53, "Requesting New Feature", 1));
		lst.add(new com.askredrover.support.SupportType(54, "UI/UX", 1));
		lst.add(new com.askredrover.support.SupportType(55, "New Content", 1));

		return lst;
	}

	/**
	 * Get a meeting type as String
	 * 
	 * @param current
	 * @return
	 */
	public static String meetingTypeAsString(int current) {
		String mt = null;

		try {

			mt = (String) MEETING_TYPES[current];
		} catch (Exception e) {
			mt = (String) MEETING_TYPES[0];
		}

		return mt;
	}

	/**
	 * get the meeting options as a select html option list.
	 * 
	 * @param current
	 * @return String
	 */
	public static String meetingTypesAsSelection(int current) {

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < MEETING_TYPES.length; i++) {
			String p = (String) MEETING_TYPES[i];
			if (i == current) {
				sb.append("<option selected value=" + i + ">" + p + "</option>");
			} else {
				sb.append("<option value=" + i + ">" + p + "</option>");
			}
		}

		return sb.toString();

	}

	public static String roleAsString(int current) {
		String r = null;
		try {
			r = (String) VENTURE_ROLES[current];
		} catch (Exception e) {
			r = (String) VENTURE_ROLES[0];
		}

		return r;
	}

	/**
	 * What is the current venture sentiment.
	 * 
	 * @param current
	 * @return
	 */
	public static String sentimentAsString(int current) {
		String sentiment = null;

		for (int i = 0; i < VENTURE_SENTIMENTS.length; i++) {
			String s = (String) VENTURE_SENTIMENTS[i];
			if (i == current) {
				sentiment = s;
			}
		}

		if (sentiment == null) {
			sentiment = (String) VENTURE_SENTIMENTS[3];
		}

		return sentiment;
	}

	public static String sentimentColor(int current) {
		String sentiment = null;

		for (int i = 0; i < VENTURE_SENTIMENT_COLORS.length; i++) {
			String s = (String) VENTURE_SENTIMENT_COLORS[i];
			if (i == current) {
				sentiment = s;
			}
		}

		if (sentiment == null) {
			sentiment = (String) VENTURE_SENTIMENT_COLORS[3];
		}

		return sentiment;
	}

	public static String sowProgressAsSelection(int current) {

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < 101; i += 10) {

			if (i == current) {
				sb.append("<option selected value=" + i + ">" + i + "%</option>");
			} else {
				sb.append("<option value=" + i + ">" + i + "%</option>");
			}
		}

		return sb.toString();

	}

	public static String sowSentiment(int current) {

		String s = null;

		try {

			s = (String) SOW_SENTIMENTS[current];

		} catch (Exception e) {
			s = (String) SOW_SENTIMENTS[0];
		}

		return s;

	}

	/**
	 * Get SoW sentiments as a selection string...
	 * 
	 * @param current
	 * @return
	 */
	public static String sowSentimentsAsSelection(int current) {

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < SOW_SENTIMENTS.length; i++) {
			String p = (String) SOW_SENTIMENTS[i];
			if (i == current) {
				sb.append("<option selected value=" + i + ">" + p + "</option>");
			} else {
				sb.append("<option value=" + i + ">" + p + "</option>");
			}
		}

		return sb.toString();

	}

	public static String sowStatesAsSelection(int current) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < SOW_STATES.length; i++) {
			String p = (String) SOW_STATES[i];
			if (i == current) {
				sb.append("<option selected value=" + i + ">" + p + "</option>");
			} else {
				sb.append("<option value=" + i + ">" + p + "</option>");
			}
		}

		return sb.toString();
	}

	public static String sowStatusAsString(int status) {
		String s = null;
		try {
			s = (String) SOW_STATES[status];
		} catch (Exception e) {
			s = (String) SOW_STATES[0];
		}
		return s;
	}

	public static String sowStatusColorAsString(int status) {
		String s = null;
		try {
			s = (String) SOW_STATE_COLORS[status];
		} catch (Exception e) {
			s = (String) SOW_STATE_COLORS[0];
		}
		return s;
	}

	/**
	 * Typeof indicates whether support or feedback. This will be changed in the
	 * future to be out of the JSON MetaInf files. (properties.json)
	 * 
	 * @param typeof int - 0= support and 1 = feedback
	 * @return ArrayList of SupportType.class
	 */
	public static ArrayList<com.askredrover.support.SupportType> supportTypes(int typeof) {

		// Todo put this in properties.json @ some point.
		ArrayList<com.askredrover.support.SupportType> lst = new ArrayList<com.askredrover.support.SupportType>();

		ArrayList<com.askredrover.support.SupportType> types = loadSupportTypes();
		int size = types.size();

		for (int i = 0; i < size; i++) {
			com.askredrover.support.SupportType st = (com.askredrover.support.SupportType) types.get(i);
			if (st.typeof() == typeof) {
				lst.add(st);
			}
		}

		return lst;
	}

	public static String tutorialStatesAsSelection(int current) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < TUTORIAL_STATES.length; i++) {
			String p = (String) TUTORIAL_STATES[i];
			if (i == current) {
				sb.append("<option selected value=" + i + ">" + p + "</option>");
			} else {
				sb.append("<option value=" + i + ">" + p + "</option>");
			}
		}

		return sb.toString();
	}

	public static String tutorialStatusAsString(int status) {
		String s = null;
		try {
			s = (String) TUTORIAL_STATES[status];
		} catch (Exception e) {
			s = (String) TUTORIAL_STATES[0];
		}
		return s;
	}

	public static String tutorialStatusColorAsString(int status) {
		String s = null;
		try {
			s = (String) TUTORIAL_STATE_COLORS[status];
		} catch (Exception e) {
			s = (String) TUTORIAL_STATE_COLORS[0];
		}
		return s;
	}

	/**
	 * Get various venture states as HTML select list.
	 * 
	 * @param current
	 * @return
	 */
	public static String ventureStatesAsSelection(int current) {
		StringBuffer sb = new StringBuffer();

		for (int x = 0; x < VENTURE_STATES.length; x++) {
			String s = (String) VENTURE_STATES[x];
			if (x == current) {
				sb.append("<option selected value=" + x + ">" + s + "</option>");
			} else {
				sb.append("<option  value=" + x + ">" + s + "</option>");
			}
		}

		return sb.toString();
	}

	/**
	 * Get the venture status as a String
	 * 
	 * @param status
	 * @return
	 */
	public static String ventureStatusAsString(int status) {
		String s = null;
		try {
			s = (String) VENTURE_STATES[status];
		} catch (Exception e) {
			s = (String) VENTURE_STATES[0];
		}
		return s;
	}

	public static String ventureStatusColorAsString(int status) {
		String s = null;
		try {
			s = (String) VENTURE_STATE_COLORS[status];
		} catch (Exception e) {
			s = (String) VENTURE_STATE_COLORS[0];
		}
		return s;
	}

}
