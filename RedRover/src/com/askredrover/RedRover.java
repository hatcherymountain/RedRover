package com.askredrover;

import com.eos.Eos;
import com.askredrover.utils.*;
import com.askredrover.pinpoint.Messages;
import com.askredrover.wisdom.*;
import com.askredrover.ventures.*;

public class RedRover {

	private Eos eos = null;
	private Numbers numbers = null;
	private Ventures ventures = null;
	private Messages messages = null;
	private Events events = null;

	private boolean showWisdom = false;
	private boolean showVentures = false;
	private Wisdom wisdom = null;
	private boolean admin = false;

	public RedRover(Eos eos) {
		this.eos = eos;
		showWisdom = true;
		showVentures = true;
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

	public boolean wisdomEnabled() {
		return showWisdom;
	}

	public boolean venturesEnabled() {
		return showVentures;
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

	public Wisdom wisdom() {
		if (wisdom == null) {
			wisdom = new Wisdom(eos, this);
		}
		return wisdom;
	}

}
