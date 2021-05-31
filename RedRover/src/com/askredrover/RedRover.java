package com.askredrover;

import com.askredrover.wisdom.loyalty.Loyalty;
import com.eos.Eos;
import com.askredrover.utils.*;
import com.askredrover.pinpoint.Messages;
import com.askredrover.wisdom.*;
import com.askredrover.ventures.*;
import com.askredrover.communication.*;
import com.askredrover.support.*;

public class RedRover {

	private Eos eos = null;
	private Numbers numbers = null;
	private Ventures ventures = null;
	private Messages messages = null;
	private Events events = null;
	private Communications comms = null;

	private boolean showWisdom = false;
	private boolean showVentures = false;
	private Wisdom wisdom = null;
	private Users users = null;
	private Loyalty loyal = null;
	private Support support = null;

	public RedRover(Eos eos) {
		this.eos = eos;
		showWisdom = true;
		showVentures = true;
	}

	/**
	 * Access to Support.
	 * 
	 * @return
	 */
	public Support support() {
		support = new Support(eos);
		return support;
	}

	/**
	 * Later we can add more layers ABOVE the EOS idea of an ADMIN. We need to do
	 * this as Pointr creates some problems
	 * 
	 * @return
	 */
	public boolean admin() {
		if (eos.active()) {
			if (eos.isAdmin()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * User Communications Framework. Provides the backbone for how we communicate
	 * with customers and when.
	 */
	public Communications communication() {
		if (comms == null) {
			comms = new Communications(eos, this);
		}
		return comms;
	}

	public boolean editor() {
		if (eos.active()) {
			if (eos.isContributor()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public Events events() {
		if (events == null) {
			events = new Events(eos, this);
		}
		return events;
	}

	/**
	 * Where do we want to put the files?
	 * 
	 * @return
	 */
	public String fileLocation() {
		String loc = "";
		if (eos.active()) {
			int eid = eos.account().eid();
			loc = "wisdom/" + eid + "";
		}
		return loc;
	}

	/**
	 * Loyalty includes bookmarketing and kudos!
	 * 
	 * @return
	 */
	public Loyalty loyalty() {
		if (loyal == null) {
			loyal = new Loyalty(eos, this);
		}
		return loyal;
	}

	/**
	 * Access to Phone Message utilities.
	 * 
	 * @return
	 */
	public Messages messages() {
		if (messages == null) {
			messages = new Messages(eos);
		}
		return messages;
	}

	/**
	 * Anything to do with RedRover AWS Pinpoint Numbers
	 * 
	 * @return
	 */
	public Numbers numbers() {
		if (numbers == null) {
			numbers = new Numbers(eos);
		}
		return numbers;
	}

	/**
	 * Users services
	 * 
	 * @return
	 */
	public Users users() {
		if (users == null) {
			users = new Users(eos);
		}
		return users;
	}

	/**
	 * Get all ventures
	 * 
	 * @return
	 */
	public Ventures ventures() {
		if (ventures == null) {
			ventures = new Ventures(eos, this);
		}
		return ventures;
	}

	public boolean venturesEnabled() {
		return showVentures;
	}

	public Wisdom wisdom() {
		if (wisdom == null) {
			wisdom = new Wisdom(eos, this);
		}
		return wisdom;
	}

	public boolean wisdomEnabled() {
		return showWisdom;
	}

}
