package com.askredrover.wisdom.search;

import java.util.ArrayList;
import com.eos.files.File;
import com.askredrover.wisdom.Article;

public class ResultObject implements Result {

	private ArrayList<File> files = null;
	private ArrayList<Article> articles = null;

	public ResultObject(ArrayList<File> files, ArrayList<Article> articles) {
		this.files = files;
		this.articles = articles;
	}

	public ArrayList<File> files() {
		return files;
	}

	public ArrayList<Article> articles() {
		return articles;
	}

}
