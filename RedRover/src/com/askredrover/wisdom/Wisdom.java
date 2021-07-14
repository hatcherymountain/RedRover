package com.askredrover.wisdom;

import com.askredrover.wisdom.search.Searches;
import com.eos.Eos;
import com.askredrover.RedRover;

public class Wisdom {

	private Files files = null;
	private Eos eos = null;
	private RedRover rr = null;
	private Categories categories = null;
	private Articles articles = null;
	private Searches search = null;
	private Tutorials tuts = null;

	public Wisdom(Eos eos, RedRover rr) {
		this.eos = eos;
		this.rr = rr;
	}

	public Files files() {
		if (files == null) {
			files = new Files(eos, rr);
		}
		return files;
	}

	public Tutorials tutorials() {
		if (tuts == null) {
			tuts = new Tutorials(eos, rr);
		}
		return tuts;
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

	public Articles articles() {
		if (articles == null) {
			articles = new Articles(eos, rr);
		}
		return articles;
	}

	/**
	 * Access to all things search in the Wisdom system.
	 * 
	 * @return
	 */
	public Searches search() {
		if (search == null) {
			search = new Searches(eos, rr);
		}
		return search;
	}
}
