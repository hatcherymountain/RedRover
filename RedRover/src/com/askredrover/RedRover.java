package com.askredrover;

import com.eos.Eos;
import com.askredrover.utils.*;
import com.askredrover.pinpoint.Messages;

public class RedRover {

	private Eos eos = null;
	private Numbers numbers = null;
	private Messages messages = null;

	public RedRover(Eos eos) {
		this.eos = eos;
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

}
