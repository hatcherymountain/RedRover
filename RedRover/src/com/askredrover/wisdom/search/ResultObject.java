package com.askredrover.wisdom.search;

import java.util.ArrayList;
import com.eos.files.File;
import com.askredrover.wisdom.Article;
import com.askredrover.wisdom.Tutorial;

public class ResultObject implements Result {

	private ArrayList<File> files = null;
	private ArrayList<Article> articles = null;
	private ArrayList<Tutorial> tutorials = null;

	public ResultObject(ArrayList<File> files, ArrayList<Article> articles, ArrayList<Tutorial> tutorials) {
		this.files = files;
		this.articles = articles;
		this.tutorials=tutorials;
	}

	public ArrayList<File> files() {
		return files;
	}

	public ArrayList<Article> articles() {
		return articles;
	}

		
	public ArrayList<Tutorial> tutorials() { 
		return tutorials; 
	}

}	
