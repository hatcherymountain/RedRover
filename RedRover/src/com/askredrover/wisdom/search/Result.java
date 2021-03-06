package com.askredrover.wisdom.search;

import java.util.ArrayList;

import com.askredrover.wisdom.Article;
import com.eos.files.File;

public interface Result {

	
	public ArrayList<File> files();

	public ArrayList<Article> articles();
}
