package com.askredrover.wisdom;

import com.eos.Eos;
import com.askredrover.RedRover;

public class Wisdom {
	
	private Files files = null;
	private Eos eos = null;
	private RedRover rr = null;
	private Categories categories = null;

	public Wisdom(Eos eos, RedRover rr) {
		this.eos = eos;
		this.rr = rr;
	}
	
	public Files files() { 
		if(files==null) { 
			files = new Files(eos);
		}
		return files;
	}

	/**
	 * All wisdom categories
	 * 
	 * @return
	 */
	public Categories categories() {
		if (categories == null) {
			categories = new Categories(eos, rr);
		}
		return categories;
	}

}
